package it.nexoapp.env.api.transcoding.dto;

import java.io.Serializable;

/**
 * Marker for the reference list items returned by the transcoding list endpoint.
 * Only the records declared here can implement it.
 */
public sealed interface ListItem extends Serializable permits State, Region, Province, Municipality {

}
