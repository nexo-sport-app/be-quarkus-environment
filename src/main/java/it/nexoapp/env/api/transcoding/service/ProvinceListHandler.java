package it.nexoapp.env.api.transcoding.service;

import it.nexoapp.common.constant.Language;
import it.nexoapp.env.infra.transcoding.dto.ListHandler;
import it.nexoapp.env.infra.transcoding.dto.TranscodingItem;
import it.nexoapp.env.infra.transcoding.utils.TranscodingUtils;
import it.nexoapp.env.api.transcoding.dto.Province;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * A handler implementation for processing a list of {@link TranscodingItem} objects
 * and converting them into a unified {@link Province} object.
 */
class ProvinceListHandler implements ListHandler<Province> {

    /* - logger - */
    private static final Logger logger = LoggerFactory.getLogger(ProvinceListHandler.class);

    /**
     * {@inheritDoc}
     * @param itemList {@inheritDoc}
     * @return {@inheritDoc}
     */
    @Override
    public Province handle(final List<TranscodingItem> itemList) {
        logger.trace("Handling 'Province' items");
        final Province.ProvinceBuilder builder = Province.builder();

        for (final TranscodingItem item : itemList) {

            /* code */
            if (TranscodingUtils.checkItem(item, "code", Language.IT.name())) {
                logger.trace("Setting 'code'={}", item.value());
                builder.code(item.value());
            }

            /* name */
            if (TranscodingUtils.checkItem(item, "name", Language.IT.name())) {
                logger.trace("Setting 'name'={}", item.value());
                builder.name(item.value());
            }

        }

        logger.trace("Finish handling object");
        return builder.build();
    }

}