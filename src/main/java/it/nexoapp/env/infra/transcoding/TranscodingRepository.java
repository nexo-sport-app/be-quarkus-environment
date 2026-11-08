package it.nexoapp.env.infra.transcoding;

import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import it.nexoapp.env.infra.transcoding.entity.TranscodingEntity;
import it.nexoapp.env.infra.transcoding.entity.TranscodingPK;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
class TranscodingRepository implements PanacheRepositoryBase<TranscodingEntity, TranscodingPK> {

}