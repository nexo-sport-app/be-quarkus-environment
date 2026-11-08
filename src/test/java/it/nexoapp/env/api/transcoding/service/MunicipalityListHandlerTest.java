package it.nexoapp.env.api.transcoding.service;

import it.nexoapp.env.api.transcoding.dto.Municipality;
import it.nexoapp.env.api.transcoding.utils.TranscodingConstant;
import it.nexoapp.env.infra.transcoding.TranscodingFacade;
import it.nexoapp.env.infra.transcoding.dto.TranscodingItem;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Unit test of {@link MunicipalityListHandler}: every transcoding item must land in its own field.
 */
class MunicipalityListHandlerTest {

    /* - handler - */
    private final MunicipalityListHandler handler = new MunicipalityListHandler();

    // -----------------------------------------------------------------------------------------------------------------

    /**
     * The zip code is mapped to {@code zipCode} and does not overwrite {@code belfiore}.
     */
    @Test
    void zipCodeAndBelfioreAreMappedToTheirOwnFields() {
        final Municipality municipality = handler.handle(List.of(
                new TranscodingItem(TranscodingConstant.ISTAT_DOMAIN, TranscodingFacade.DEFAULT_LANGUAGE, "015146"),
                new TranscodingItem(TranscodingFacade.DEFAULT_DOMAIN, "IT", "Milano"),
                new TranscodingItem(TranscodingConstant.ZIPCODE_DOMAIN, TranscodingFacade.DEFAULT_LANGUAGE, "20121"),
                new TranscodingItem(TranscodingConstant.BELFIORE_DOMAIN, TranscodingFacade.DEFAULT_LANGUAGE, "F205")
        ));

        assertEquals("015146", municipality.code(), "code should be mapped");
        assertEquals("Milano", municipality.name(), "name should be mapped");
        assertEquals("20121", municipality.zipCode(), "zipCode should be mapped from the zipCode domain");
        assertEquals("F205", municipality.belfiore(), "belfiore must not be overwritten by the zip code");
    }

    // -----------------------------------------------------------------------------------------------------------------

    /**
     * The mapping does not depend on the order of the items: zip code after belfiore keeps both.
     */
    @Test
    void zipCodeAfterBelfioreKeepsBoth() {
        final Municipality municipality = handler.handle(List.of(
                new TranscodingItem(TranscodingConstant.BELFIORE_DOMAIN, TranscodingFacade.DEFAULT_LANGUAGE, "F205"),
                new TranscodingItem(TranscodingConstant.ZIPCODE_DOMAIN, TranscodingFacade.DEFAULT_LANGUAGE, "20121")
        ));

        assertEquals("20121", municipality.zipCode(), "zipCode should be mapped");
        assertEquals("F205", municipality.belfiore(), "belfiore should be kept");
    }

}
