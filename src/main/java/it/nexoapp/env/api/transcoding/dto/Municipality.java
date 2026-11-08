package it.nexoapp.env.api.transcoding.dto;

import lombok.Builder;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

import java.io.Serial;

/**
 * A municipality reference list item.
 *
 * @param code     the ISTAT code of the municipality
 * @param name     the Italian name of the municipality
 * @param zipCode  the postal code of the municipality
 * @param belfiore the Belfiore (cadastral) code of the municipality
 */
@Builder
@Schema(description = "A municipality reference list item")
public record Municipality(

        @Schema(description = "ISTAT code of the municipality", examples = "015146")
        String code,

        @Schema(description = "Italian name of the municipality", examples = "Milano")
        String name,

        @Schema(description = "Postal code of the municipality", examples = "20121")
        String zipCode,

        @Schema(description = "Belfiore (cadastral) code of the municipality", examples = "F205")
        String belfiore

) implements ListItem {

    /** Serialization version. */
    @Serial
    private static final long serialVersionUID = -8072064628950120838L;

}
