package it.nexoapp.env.api.transcoding.service;

import it.nexoapp.common.constant.Language;
import it.nexoapp.env.infra.transcoding.TranscodingFacade;
import it.nexoapp.env.infra.transcoding.dto.ListHandler;
import it.nexoapp.env.infra.transcoding.dto.TranscodingItem;
import it.nexoapp.env.infra.transcoding.utils.TranscodingUtils;
import it.nexoapp.env.api.transcoding.dto.Municipality;
import it.nexoapp.env.api.transcoding.utils.TranscodingConstant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * A handler implementation for processing a list of {@link TranscodingItem} objects
 * and converting them into a unified {@link Municipality} object.
 */
class MunicipalityListHandler implements ListHandler<Municipality> {

    /* - logger - */
    private static final Logger logger = LoggerFactory.getLogger(MunicipalityListHandler.class);

    /**
     * {@inheritDoc}
     * @param itemList {@inheritDoc}
     * @return {@inheritDoc}
     */
    @Override
    public Municipality handle(final List<TranscodingItem> itemList) {
        logger.trace("Handling 'Municipality' items");
        final Municipality.MunicipalityBuilder builder = Municipality.builder();

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

            /* zipCode */
            if (TranscodingUtils.checkItem(item, TranscodingConstant.ZIPCODE_DOMAIN, TranscodingFacade.DEFAULT_LANGUAGE)) {
                logger.trace("Setting 'zipCode'={}", item.value());
                builder.belfiore(item.value());
            }

            /* belfiore */
            if (TranscodingUtils.checkItem(item, TranscodingConstant.BELFIORE_DOMAIN, TranscodingFacade.DEFAULT_LANGUAGE)) {
                logger.trace("Setting 'belfiore'={}", item.value());
                builder.belfiore(item.value());
            }

        }

        logger.trace("Finish handling object");
        return builder.build();
    }

}