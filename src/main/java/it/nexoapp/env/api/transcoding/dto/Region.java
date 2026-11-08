package it.nexoapp.env.api.transcoding.dto;

import lombok.Builder;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

import java.io.Serial;

/**
 * A region reference list item.
 *
 * @param code the ISTAT code of the region
 * @param name the Italian name of the region
 */
@Builder
@Schema(description = "A region reference list item")
public record Region(

        @Schema(description = "ISTAT code of the region", examples = "03")
        String code,

        @Schema(description = "Italian name of the region", examples = "Lombardia")
        String name

) implements ListItem {

    /** Serialization version. */
    @Serial
    private static final long serialVersionUID = 3654108240760350783L;

}
