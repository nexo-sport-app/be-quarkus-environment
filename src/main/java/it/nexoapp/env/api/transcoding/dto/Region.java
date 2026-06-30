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
public class Region extends ListItem {

    @Serial
    private static final long serialVersionUID = 3654108240760350783L;

    String code;
    String name;

}