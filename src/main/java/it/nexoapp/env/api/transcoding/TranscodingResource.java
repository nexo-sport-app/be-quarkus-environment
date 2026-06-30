package it.nexoapp.env.api.transcoding;

import it.nexoapp.common.domain.Date;
import it.nexoapp.common.infra.transcoding.dto.TranscodeCommand;
import it.nexoapp.env.api.transcoding.dto.ListType;
import it.nexoapp.env.api.transcoding.service.TranscodingService;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import lombok.RequiredArgsConstructor;

@Path("/transcoding")
@RequiredArgsConstructor
public class TranscodingResource {

    private final TranscodingService service;

    @GET
    @Path("/list/{listName}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response list(
            @PathParam("listName")
            final ListType listType,
            @QueryParam("referenceDate")
            final Date referenceDate
    ) {
        return Response
                .ok(service.list(listType, referenceDate))
                .build();
    }

    @GET
    @Path("/transcode")
    @Produces(MediaType.APPLICATION_JSON)
    public Response transcode(
            @QueryParam("list")
            final String list,
            @QueryParam("fromDomain")
            final String fromDomain,
            @QueryParam("fromLanguage")
            final String fromLanguage,
            @QueryParam("fromValue")
            final String fromValue,
            @QueryParam("toDomain")
            final String toDomain,
            @QueryParam("toLanguage")
            final String toLanguage,
            @QueryParam("referenceDate")
            final Date referenceDate
    ) throws Exception {
        final TranscodeCommand command = TranscodeCommand.builder()
                .list(list)
                .fromDomain(fromDomain)
                .fromLanguage(fromLanguage)
                .fromValue(fromValue)
                .toDomain(toDomain)
                .toLanguage(toLanguage)
                .date(referenceDate)
                .build();
        return Response
                .ok(service.transcode(command))
                .build();
    }

}
