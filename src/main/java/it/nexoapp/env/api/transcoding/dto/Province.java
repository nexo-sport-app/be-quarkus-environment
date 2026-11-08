package it.nexoapp.env.api.transcoding.dto;

import lombok.Builder;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

import java.io.Serial;

/**
 * A province reference list item.
 *
 * @param code the ISTAT code of the province
 * @param name the Italian name of the province
 */
@Builder
@Schema(description = "A province reference list item")
public record Province(

        @Schema(description = "ISTAT code of the province", examples = "015")
        String code,

        @Schema(description = "Italian name of the province", examples = "Milano")
        String name

) implements ListItem {

    /** Serialization version. */
    @Serial
    private static final long serialVersionUID = 3654108240760350783L;

}
