package it.nexoapp.env.api.transcoding.dto;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Characterization test pinning the JSON contract of the transcoding list items.
 * The expectations were captured from the pre-conversion Lombok classes, so they
 * must hold unchanged for the record implementations.
 */
class ListItemJsonContractTest {

    /* - serializer - */
    private final ObjectMapper mapper = new ObjectMapper();

    // ------------------------------------------------------------------------

    /**
     * A state serializes with code, name and belfiore in this order.
     *
     * @throws JsonProcessingException on serialization failure
     */
    @Test
    void stateJsonContract() throws JsonProcessingException {
        final State state = State.builder().code("100").name("Italia").belfiore("Z000").build();
        assertEquals("{\"code\":\"100\",\"name\":\"Italia\",\"belfiore\":\"Z000\"}", mapper.writeValueAsString(state));
    }

    // ------------------------------------------------------------------------

    /**
     * A state without belfiore serializes the null explicitly.
     *
     * @throws JsonProcessingException on serialization failure
     */
    @Test
    void stateNullBelfioreJsonContract() throws JsonProcessingException {
        final State state = State.builder().code("100").name("Italia").build();
        assertEquals("{\"code\":\"100\",\"name\":\"Italia\",\"belfiore\":null}", mapper.writeValueAsString(state));
    }

    // ------------------------------------------------------------------------

    /**
     * A region serializes with code and name.
     *
     * @throws JsonProcessingException on serialization failure
     */
    @Test
    void regionJsonContract() throws JsonProcessingException {
        final Region region = Region.builder().code("03").name("Lombardia").build();
        assertEquals("{\"code\":\"03\",\"name\":\"Lombardia\"}", mapper.writeValueAsString(region));
    }

    // ------------------------------------------------------------------------

    /**
     * A province serializes with code and name.
     *
     * @throws JsonProcessingException on serialization failure
     */
    @Test
    void provinceJsonContract() throws JsonProcessingException {
        final Province province = Province.builder().code("015").name("Milano").build();
        assertEquals("{\"code\":\"015\",\"name\":\"Milano\"}", mapper.writeValueAsString(province));
    }

    // ------------------------------------------------------------------------

    /**
     * A municipality serializes with code, name, zipCode and belfiore in this order.
     *
     * @throws JsonProcessingException on serialization failure
     */
    @Test
    void municipalityJsonContract() throws JsonProcessingException {
        final Municipality municipality = Municipality.builder()
                .code("015146").name("Milano").zipCode("20121").belfiore("F205").build();
        assertEquals(
                "{\"code\":\"015146\",\"name\":\"Milano\",\"zipCode\":\"20121\",\"belfiore\":\"F205\"}",
                mapper.writeValueAsString(municipality)
        );
    }

    // ------------------------------------------------------------------------

    /**
     * All list items are records and the only permitted subclasses of the sealed interface.
     */
    @Test
    void listItemsAreRecordsPermittedBySealedInterface() {
        final Class<?>[] items = {State.class, Region.class, Province.class, Municipality.class};
        assertTrue(ListItem.class.isSealed());
        assertEquals(Set.of(items), Set.of(ListItem.class.getPermittedSubclasses()));
        for (final Class<?> item : items) {
            assertTrue(item.isRecord(), item.getSimpleName() + " must be a record");
        }
    }

}
