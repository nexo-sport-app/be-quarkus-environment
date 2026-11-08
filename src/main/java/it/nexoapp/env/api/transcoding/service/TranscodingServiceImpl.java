package it.nexoapp.env.api.transcoding.service;

import it.nexoapp.common.domain.Date;
import it.nexoapp.common.infra.transcoding.TranscodingFacade;
import it.nexoapp.common.infra.transcoding.dto.TranscodeCommand;
import it.nexoapp.common.infra.transcoding.exception.TranscodingException;
import it.nexoapp.env.api.transcoding.dto.ListType;
import it.nexoapp.env.api.transcoding.dto.TranscodeResponse;
import jakarta.enterprise.context.ApplicationScoped;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

@ApplicationScoped
@RequiredArgsConstructor
class TranscodingServiceImpl implements TranscodingService {

    /* - logger - */
    private static final Logger logger = LoggerFactory.getLogger(TranscodingServiceImpl.class);

    /* - facade - */
    private final TranscodingFacade transcodingFacade;

    /* - registry - */
    private final ListHandlerRegistry registry;

    @Override
    @SuppressWarnings("unchecked")
    public <T> List<T> list(final ListType type, Date referenceDate) {
        if(referenceDate == null) {
            logger.debug("No reference date specified, using today");
            referenceDate = Date.today();
        }

        return (List<T>) transcodingFacade.getList(
                type.name().toLowerCase(),
                registry.handlerFor(type),
                referenceDate
        );
    }

    @Override
    public TranscodeResponse transcode(final TranscodeCommand command) throws TranscodingException {
        final String transcode = transcodingFacade.transcode(command);
        return new TranscodeResponse(transcode);
    }

}