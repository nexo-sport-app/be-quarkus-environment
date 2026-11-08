package it.nexoapp.env.api.transcoding.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * The reference lists exposed by the transcoding list endpoint.
 */
@Getter
@RequiredArgsConstructor
public enum ListType {

    /** The list of states. */
    STATE(State.class),
    /** The list of regions. */
    REGION(Region.class),
    /** The list of provinces. */
    PROVINCE(Province.class),
    /** The list of municipalities. */
    MUNICIPALITY(Municipality.class),

    ;

    /** The item type returned for this list. */
    private final Class<? extends ListItem> item;

}
