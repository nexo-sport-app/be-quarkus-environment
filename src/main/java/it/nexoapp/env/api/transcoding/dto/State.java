package it.nexoapp.env.api.transcoding.dto;

import lombok.Builder;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

import java.io.Serial;

/**
 * A state (nation) reference list item.
 *
 * @param code     the ISTAT code of the state
 * @param name     the Italian name of the state
 * @param belfiore the Belfiore (cadastral) code of the state
 */
@Builder
@Schema(description = "A state reference list item")
public record State(

        @Schema(description = "ISTAT code of the state", examples = "100")
        String code,

        @Schema(description = "Italian name of the state", examples = "Italia")
        String name,

        @Schema(description = "Belfiore (cadastral) code of the state", examples = "Z000")
        String belfiore

) implements ListItem {

    /** Serialization version. */
    @Serial
    private static final long serialVersionUID = 3654108240760350783L;

}
