package it.nexoapp.env.api.transcoding.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Value;

import java.io.Serial;

@Value
@Builder
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
public class Municipality extends ListItem {

    @Serial
    private static final long serialVersionUID = -8072064628950120838L;

    String code;
    String name;
    String zipCode;
    String belfiore;

}