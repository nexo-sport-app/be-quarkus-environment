package it.nexoapp.env.api.transcoding.dto;

import it.nexoapp.common.infra.transcoding.dto.ListHandler;
import it.nexoapp.env.api.transcoding.service.MunicipalityListHandler;
import it.nexoapp.env.api.transcoding.service.ProvinceListHandler;
import it.nexoapp.env.api.transcoding.service.RegionListHandler;
import it.nexoapp.env.api.transcoding.service.StateListHandler;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ListType {

    STATE(new StateListHandler(), State.class),
    REGION(new RegionListHandler(), Region.class),
    PROVINCE(new ProvinceListHandler(), Province.class),
    MUNICIPALITY(new MunicipalityListHandler(), Municipality.class),

    ;

    private final ListHandler<?> handler;
    private final Class<? extends ListItem> item;

}