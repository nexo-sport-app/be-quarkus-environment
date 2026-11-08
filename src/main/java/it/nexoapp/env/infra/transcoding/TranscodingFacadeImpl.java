package it.nexoapp.env.infra.transcoding;

import io.quarkus.cache.CacheKey;
import io.quarkus.cache.CacheResult;
import it.nexoapp.common.domain.Date;
import it.nexoapp.env.infra.transcoding.dto.CreateTranscodingCommand;
import it.nexoapp.env.infra.transcoding.dto.ListHandler;
import it.nexoapp.env.infra.transcoding.dto.TranscodeCommand;
import it.nexoapp.env.infra.transcoding.dto.TranscodingItem;
import it.nexoapp.env.infra.transcoding.entity.TranscodingEntity;
import it.nexoapp.env.infra.transcoding.exception.FromTranscodeNotFoundException;
import it.nexoapp.env.infra.transcoding.exception.ToTranscodeNotFoundException;
import it.nexoapp.env.infra.transcoding.exception.TranscodingException;
import it.nexoapp.env.infra.transcoding.utils.TranscodingMapper;
import jakarta.enterprise.context.ApplicationScoped;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Default {@link TranscodingFacade} implementation, backed by {@link TranscodingRepository} and Redis-cached reads.
 */
@ApplicationScoped
@RequiredArgsConstructor
class TranscodingFacadeImpl implements TranscodingFacade {

    /* - logger - */
    private static final Logger logger = LoggerFactory.getLogger(TranscodingFacadeImpl.class);

    /* - repository - */
    private final TranscodingRepository repository;

    /* - utility - */
    private final TranscodingMapper mapper;

    // -----------------------------------------------------------------------------------------------------------------

    /**
     * {@inheritDoc}
     * @param <T> {@inheritDoc}
     * @param list {@inheritDoc}
     * @param handler {@inheritDoc}
     * @param referenceDate {@inheritDoc}
     * @return {@inheritDoc}
     */
    @Override
    @CacheResult(cacheName = "env.transcoding.list")
    public <T> List<T> getList(
            @CacheKey
            final String list,
            final ListHandler<T> handler,
            @CacheKey
            final Date referenceDate
    ) {
        logger.debug("Retrieving list {} with reference date {}", list, referenceDate);
        /* fetch entities */
        final List<TranscodingEntity> entities = repository.find(
                "primaryKey.list = ?1 AND primaryKey.validFrom < ?2 AND primaryKey.validTo > ?2",
                list,
                referenceDate.toLocalDate()
        ).list();
        logger.trace("Found {} entities", entities.size());

        /* group by id */
        final Map<UUID, List<TranscodingEntity>> groupedEntities = entities.stream()
                .collect(Collectors.groupingBy(
                                e -> e.getPrimaryKey().getId()
                        )
                );
        logger.trace("Mapped to {} id", groupedEntities.size());

        /* create the return list */
        final List<T> objectList = new ArrayList<>();

        /* convert to objects */
        for (final List<TranscodingEntity> value : groupedEntities.values()) {
            final List<TranscodingItem> relatedItemList = value.stream()
                    .map(mapper::decouple)
                    .toList();

            final T obj = handler.handle(relatedItemList);
            objectList.add(obj);
        }

        logger.debug("Finished retrieving list, found {} objects", objectList.size());
        return objectList;
    }

    /**
     * {@inheritDoc}
     * @param <T> {@inheritDoc}
     * @param list {@inheritDoc}
     * @param handler {@inheritDoc}
     * @return {@inheritDoc}
     */
    @Override
    @CacheResult(cacheName = "env.transcoding.list")
    public <T> List<T> getList(
            @CacheKey
            final String list,
            final ListHandler<T> handler
    ) {
        return getList(list, handler, Date.today());
    }

    // -----------------------------------------------------------------------------------------------------------------

    /**
     *  {@inheritDoc}
     * @param command {@inheritDoc}
     * @return {@inheritDoc}
     * @throws TranscodingException {@inheritDoc}
     */
    @Override
    @CacheResult(cacheName = "env.transcoding.transcode")
    public String transcode(final TranscodeCommand command) throws TranscodingException {
        /* set reference date */
        final Date referenceDate = command.date() == null ? Date.today() : command.date();
        logger.debug("Set reference date to {}", referenceDate);

        /* retrieve parent id */
        final TranscodingEntity fromEntity = repository.find(
                "primaryKey.list = ?1 AND primaryKey.domain = ?2 AND primaryKey.language = ?3 AND primaryKey.value = ?4 AND primaryKey.validFrom < ?5 AND primaryKey.validTo > ?5",
                command.list(),
                command.fromDomain(),
                command.fromLanguage(),
                command.fromValue(),
                referenceDate.toLocalDate()
        ).firstResult();

        /* check if transcoding exists */
        if(fromEntity == null) {
            logger.warn("Could not find transcoding for command={}, referenceDate={}", command, referenceDate);
            throw new FromTranscodeNotFoundException(
                    command.list(),
                    command.fromDomain(),
                    command.fromLanguage(),
                    command.fromValue(),
                    referenceDate
            );
        }

        /* extract parent id */
        final UUID parentId = fromEntity.getPrimaryKey().getId();
        logger.debug("Parent id found {}", parentId);

        /* search transcoded value */
        final TranscodingEntity transcodedEntity = repository.find(
                "primaryKey.id = ?1 AND primaryKey.list = ?2 AND primaryKey.domain = ?3 AND primaryKey.language = ?4 AND primaryKey.validFrom < ?5 AND primaryKey.validTo > ?5",
                parentId,
                command.list(),
                command.toDomain(),
                command.toLanguage(),
                referenceDate.toLocalDate()
        ).firstResult();
        if(transcodedEntity == null) {
            logger.warn("Could not find transcoded for id={} command={}, referenceDate={}", parentId, command, referenceDate);
            throw new ToTranscodeNotFoundException(
                    parentId,
                    command.list(),
                    command.toDomain(),
                    command.toLanguage(),
                    referenceDate
            );
        }


        return transcodedEntity
                .getPrimaryKey()
                .getValue();
    }

    // -----------------------------------------------------------------------------------------------------------------

    /**
     * {@inheritDoc}
     * @param commands {@inheritDoc}
     */
    @Override
    public void save(final List<CreateTranscodingCommand> commands) {
        logger.debug("Received {} transcoding create commands", commands.size());

        /* convert to entities */
        final List<TranscodingEntity> entities = commands.stream()
                .map(mapper::decouple)
                .flatMap(List::stream)
                .toList();

        /* save */
        persist(entities);
    }

    // -----------------------------------------------------------------------------------------------------------------

    /**
     * {@inheritDoc}
     * @param command {@inheritDoc}
     */
    @Override
    public void save(final CreateTranscodingCommand command) {
        logger.debug("Received a transcoding create commands");

        /* convert to entities */
        final List<TranscodingEntity> entities = mapper.decouple(command);

        /* save */
        repository.persist(entities);
    }

    // --------------------------------------------------------
    // -- PRIVATE METHODS
    // -----------------------------------------------------------------------------------------------------------------

    /**
     * Persists a list of transcoding entities into the repository.
     *
     * @param entities A list of {@link TranscodingEntity} objects to be persisted. These entities
     *                 represent transcoding data that has been converted and is ready for storage.
     */
    private void persist(final List<TranscodingEntity> entities) {
        logger.debug("Transcoding command converted to {} entities", entities.size());
        repository.persist(entities);
        logger.debug("Transcoding command persisted");
    }

    // -----------------------------------------------------------------------------------------------------------------

}
