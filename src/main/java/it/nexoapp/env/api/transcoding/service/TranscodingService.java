package it.nexoapp.env.api.transcoding.service;

import it.nexoapp.common.domain.Date;
import it.nexoapp.env.infra.transcoding.dto.TranscodeCommand;
import it.nexoapp.env.infra.transcoding.exception.TranscodingException;
import it.nexoapp.env.api.transcoding.dto.ListItem;
import it.nexoapp.env.api.transcoding.dto.ListType;
import it.nexoapp.env.api.transcoding.dto.TranscodeResponse;

import java.util.List;

/**
 * Service exposing the transcoding lists and value transcoding to the API layer.
 */
public interface TranscodingService {

    /**
     * Retrieves the items of a transcoding list valid at the given date.
     *
     * @param type          the list to retrieve
     * @param referenceDate the date the items must be valid at; {@code null} means today
     * @return the items of the requested list
     */
    List<? extends ListItem> list(final ListType type, final Date referenceDate);

    // -------------------------------------------------------------------------------------------------------------

    /**
     * Transcodes a value from one domain and language to another.
     *
     * @param command the transcoding request
     * @return the transcoded value
     * @throws TranscodingException if the source or the target value cannot be found
     */
    TranscodeResponse transcode(final TranscodeCommand command) throws TranscodingException;

}