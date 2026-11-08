package it.nexoapp.env.infra.transcoding.utils;

import it.nexoapp.common.domain.Date;
import it.nexoapp.env.infra.transcoding.dto.CreateTranscodingCommand;
import it.nexoapp.env.infra.transcoding.dto.TranscodingItem;
import it.nexoapp.env.infra.transcoding.entity.TranscodingEntity;
import it.nexoapp.env.infra.transcoding.entity.TranscodingPK;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

/**
 * Unit test of the MapStruct-generated {@link TranscodingMapper}: the entity to item mapping with its null handling
 * and the default decoupling of a creation command. Plain JUnit, no Quarkus.
 */
class TranscodingMapperTest {

    /* - subject - */

    /** The generated implementation, instantiated directly. */
    private final TranscodingMapper mapper = new TranscodingMapperImpl();

    // ------------------------------------------------------------------------------------------

    /**
     * The domain, language and value of the primary key become the item.
     */
    @Test
    void entityMapsToItem() {
        final TranscodingEntity entity = TranscodingEntity.builder()
                .primaryKey(TranscodingPK.builder().domain("ISTAT").language("it").value("Roma").build())
                .build();

        assertEquals(new TranscodingItem("ISTAT", "it", "Roma"), mapper.decouple(entity));
    }

    // ------------------------------------------------------------------------------------------

    /**
     * A {@code null} entity maps to {@code null}.
     */
    @Test
    void nullEntityMapsToNull() {
        assertNull(mapper.decouple((TranscodingEntity) null));
    }

    // ------------------------------------------------------------------------------------------

    /**
     * An entity without primary key maps to an item with all components {@code null}.
     */
    @Test
    void entityWithoutPrimaryKeyMapsToEmptyItem() {
        assertEquals(new TranscodingItem(null, null, null), mapper.decouple(TranscodingEntity.builder().build()));
    }

    // ------------------------------------------------------------------------------------------

    /**
     * A creation command becomes one entity per item, all sharing one parent identifier, the list and the validity.
     */
    @Test
    void commandDecouplesIntoOneEntityPerItem() {
        final CreateTranscodingCommand command = new CreateTranscodingCommand(
                "COUNTRY",
                List.of(new TranscodingItem("ISTAT", "it", "Italia"), new TranscodingItem("ISO", "en", "Italy")),
                new Date(LocalDate.of(2026, 1, 1)),
                new Date(LocalDate.of(2026, 12, 31)));

        final List<TranscodingEntity> entities = mapper.decouple(command);

        assertEquals(2, entities.size());
        final TranscodingPK first = entities.get(0).getPrimaryKey();
        final TranscodingPK second = entities.get(1).getPrimaryKey();
        assertEquals(first.getId(), second.getId());
        assertEquals("COUNTRY", first.getList());
        assertEquals("ISTAT", first.getDomain());
        assertEquals("Italy", second.getValue());
        assertEquals(LocalDate.of(2026, 1, 1), first.getValidFrom());
        assertEquals(LocalDate.of(2026, 12, 31), first.getValidTo());
    }

}
