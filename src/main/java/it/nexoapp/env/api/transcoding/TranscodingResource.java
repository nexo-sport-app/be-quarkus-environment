package it.nexoapp.env.api.transcoding;

import it.nexoapp.common.domain.Date;
import it.nexoapp.common.security.AllowUnlinked;
import it.nexoapp.env.infra.transcoding.dto.TranscodeCommand;
import it.nexoapp.env.api.transcoding.dto.ListType;
import it.nexoapp.env.api.transcoding.service.TranscodingService;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import lombok.RequiredArgsConstructor;

/**
 * Global reference data (lists and transcodings), readable by any authenticated caller even when not linked to an
 * association.
 */
@AllowUnlinked
@Path("/transcoding")
@RequiredArgsConstructor
public class TranscodingResource {

    /* - service - */
    private final TranscodingService service;

    // -----------------------------------------------------------------------------------------------------------------

    /**
     * Returns the items of a reference list valid at the given date.
     * <p>
     * An unknown list name is a path parameter conversion failure and answers 404.
     *
     * @param listType      the requested list
     * @param referenceDate the reference date, or {@code null} for today in the configured zone
     * @return the list items
     */
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

    // -----------------------------------------------------------------------------------------------------------------

    /**
     * Transcodes a value from one domain and language to another.
     *
     * @param list          the list the value belongs to
     * @param fromDomain    the source domain
     * @param fromLanguage  the source language
     * @param fromValue     the value to transcode
     * @param toDomain      the target domain
     * @param toLanguage    the target language
     * @param referenceDate the reference date, or {@code null} for today in the configured zone
     * @return the transcoded value
     * @throws Exception when the transcoding fails
     */
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
