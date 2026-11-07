package it.nexoapp.env.api.transcoding.service;

import it.nexoapp.common.constant.Language;
import it.nexoapp.common.infra.transcoding.TranscodingFacade;
import it.nexoapp.common.infra.transcoding.dto.ListHandler;
import it.nexoapp.common.infra.transcoding.dto.TranscodingItem;
import it.nexoapp.common.infra.transcoding.utils.TranscodingUtils;
import it.nexoapp.env.api.transcoding.dto.Region;
import it.nexoapp.env.api.transcoding.utils.TranscodingConstant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * A handler implementation for processing a list of {@link TranscodingItem} objects
 * and converting them into a unified {@link Region} object.
 */
public class RegionListHandler implements ListHandler<Region> {

    /* - logger - */
    private static final Logger logger = LoggerFactory.getLogger(RegionListHandler.class);

    /**
     * {@inheritDoc}
     * @param itemList {@inheritDoc}
     * @return {@inheritDoc}
     */
    @Override
    public Region handle(final List<TranscodingItem> itemList) {
        logger.trace("Handling 'Region' items");
        final Region.RegionBuilder builder = Region.builder();

        for (final TranscodingItem item : itemList) {

            /* code */
            if (TranscodingUtils.checkItem(item, TranscodingConstant.ISTAT_DOMAIN, TranscodingFacade.DEFAULT_LANGUAGE)) {
                logger.trace("Setting 'code'={}", item.value());
                builder.code(item.value());
            }

            /* name */
            if (TranscodingUtils.checkItem(item, TranscodingFacade.DEFAULT_DOMAIN, Language.IT.name())) {
                logger.trace("Setting 'name'={}", item.value());
                builder.name(item.value());
            }

        }

        logger.trace("Finish handling object");
        return builder.build();
    }

}