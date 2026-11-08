package it.nexoapp.env.api.transcoding.service;

import it.nexoapp.common.domain.Date;
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
            effectiveDate = Date.today();
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
        final String transcode = transcodingFacade.transcode(command);
        return new TranscodeResponse(transcode);
    }

}