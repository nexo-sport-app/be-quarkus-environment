package it.nexoapp.env.infra.transcoding.utils;

import it.nexoapp.env.infra.transcoding.dto.TranscodingItem;
import it.nexoapp.env.infra.transcoding.entity.TranscodingEntity;
import it.nexoapp.env.infra.transcoding.entity.TranscodingPK;
import jakarta.enterprise.context.ApplicationScoped;

/**
 * Hand-written {@link TranscodingMapper} implementation.
 */
@ApplicationScoped
class TranscodingMapperImpl implements TranscodingMapper {

    /**
     * {@inheritDoc}
     *
     * @param entity {@inheritDoc}
     * @return {@inheritDoc}
     */
    @Override
    public TranscodingItem decouple(final TranscodingEntity entity) {
        if (entity == null) {
            return null;
        }

        final TranscodingPK primaryKey = entity.getPrimaryKey();
        if (primaryKey == null) {
            return new TranscodingItem(null, null, null);
        }

        return new TranscodingItem(primaryKey.getDomain(), primaryKey.getLanguage(), primaryKey.getValue());
    }

}
