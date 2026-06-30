package it.nexoapp.env.api.transcoding.service;

import it.nexoapp.common.domain.Date;
import it.nexoapp.common.infra.transcoding.dto.TranscodeCommand;
import it.nexoapp.common.infra.transcoding.exception.TranscodingException;
import it.nexoapp.env.api.transcoding.dto.ListType;
import it.nexoapp.env.api.transcoding.dto.TranscodeResponse;

import java.util.List;

public interface TranscodingService {

    <T> List<T> list(final ListType type, final Date referenceDate);

    TranscodeResponse transcode(TranscodeCommand command) throws TranscodingException;

}