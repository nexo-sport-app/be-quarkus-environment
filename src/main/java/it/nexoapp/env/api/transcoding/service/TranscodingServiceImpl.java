package it.nexoapp.env.api.transcoding.service;

import it.nexoapp.common.domain.Date;
import it.nexoapp.env.config.EnvironmentConfig;
import it.nexoapp.env.infra.transcoding.TranscodingFacade;
import it.nexoapp.env.infra.transcoding.dto.ListHandler;
import it.nexoapp.env.infra.transcoding.dto.TranscodeCommand;
import it.nexoapp.env.infra.transcoding.exception.TranscodingException;
import it.nexoapp.env.api.transcoding.dto.ListItem;
import it.nexoapp.env.api.transcoding.dto.ListType;
import it.nexoapp.env.api.transcoding.dto.TranscodeResponse;
import jakarta.enterprise.context.ApplicationScoped;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Clock;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;

/**
 * Default {@link TranscodingService} implementation delegating to the {@link TranscodingFacade}.
 */
@ApplicationScoped
@RequiredArgsConstructor
class TranscodingServiceImpl implements TranscodingService {

    /* - logger - */
    private static final Logger logger = LoggerFactory.getLogger(TranscodingServiceImpl.class);

    /* - facade - */
    private final TranscodingFacade transcodingFacade;

    /* - registry - */
    private final ListHandlerRegistry registry;

    /* - clock - */
    private final Clock clock;

    /* - config - */
    private final EnvironmentConfig config;

    /**
     * {@inheritDoc}
     * @param type {@inheritDoc}
     * @param referenceDate {@inheritDoc}
     * @return {@inheritDoc}
     */
    @Override
    public List<? extends ListItem> list(final ListType type, final Date referenceDate) {
        final Date effectiveDate;
        if (referenceDate == null) {
            logger.debug("No reference date specified, using today");
            effectiveDate = today();
        } else {
            effectiveDate = referenceDate;
        }

        final ListHandler<? extends ListItem> handler = registry.handlerFor(type);
        return transcodingFacade.getList(
                type.name().toLowerCase(),
                handler,
                effectiveDate
        );
    }

    // -------------------------------------------------------------------------------------------------------------

    /**
     * {@inheritDoc}
     * @param command {@inheritDoc}
     * @return {@inheritDoc}
     * @throws TranscodingException {@inheritDoc}
     */
    @Override
    public TranscodeResponse transcode(final TranscodeCommand command) throws TranscodingException {
        final TranscodeCommand dated = command.date() == null
                ? withDate(command, today())
                : command;
        final String transcode = transcodingFacade.transcode(dated);
        return new TranscodeResponse(transcode);
    }

    // --------------------------------------------------------
    // -- PRIVATE METHODS
    // -------------------------------------------------------------------------------------------------------------

    /**
     * Computes today's calendar date from the injected clock in the configured zone
     * ({@code env.transcoding.zone}), never from the JVM default clock or zone.
     *
     * @return today's date in the configured zone
     */
    private Date today() {
        final ZoneId zone = ZoneId.of(config.transcoding().zone());
        return Date.of(LocalDate.now(clock.withZone(zone)));
    }

    // -------------------------------------------------------------------------------------------------------------

    /**
     * Copies a transcode command replacing its reference date.
     *
     * @param command the command to copy
     * @param date    the reference date of the copy
     * @return the dated command
     */
    private static TranscodeCommand withDate(final TranscodeCommand command, final Date date) {
        return new TranscodeCommand(
                command.list(),
                command.fromDomain(),
                command.fromLanguage(),
                command.fromValue(),
                command.toDomain(),
                command.toLanguage(),
                date
        );
    }

}