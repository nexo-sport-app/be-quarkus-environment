package it.nexoapp.env.api.transcoding.service;

import it.nexoapp.common.infra.transcoding.dto.ListHandler;
import it.nexoapp.env.api.transcoding.dto.ListItem;
import it.nexoapp.env.api.transcoding.dto.ListType;
import jakarta.enterprise.context.ApplicationScoped;

/**
 * Maps every {@link ListType} to the {@link ListHandler} that builds its items.
 * Lives in the service layer so the dto package never depends on it.
 */
@ApplicationScoped
class ListHandlerRegistry {

    /* - handlers - */
    private final StateListHandler stateHandler = new StateListHandler();
    private final RegionListHandler regionHandler = new RegionListHandler();
    private final ProvinceListHandler provinceHandler = new ProvinceListHandler();
    private final MunicipalityListHandler municipalityHandler = new MunicipalityListHandler();

    // ------------------------------------------------------------------------

    /**
     * Resolves the handler of a list type. The switch is exhaustive on purpose,
     * so a new {@link ListType} constant fails compilation until it is mapped.
     *
     * @param type the list type
     * @return the handler building the items of the given list type
     */
    ListHandler<? extends ListItem> handlerFor(final ListType type) {
        return switch (type) {
            case STATE -> stateHandler;
            case REGION -> regionHandler;
            case PROVINCE -> provinceHandler;
            case MUNICIPALITY -> municipalityHandler;
        };
    }

}
