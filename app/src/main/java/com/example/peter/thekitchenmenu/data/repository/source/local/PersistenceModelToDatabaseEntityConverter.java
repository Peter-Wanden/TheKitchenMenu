package com.example.peter.thekitchenmenu.data.repository.source.local;

import com.example.peter.thekitchenmenu.data.primitivemodel.EntityModel;
import com.example.peter.thekitchenmenu.domain.usecasenew.common.model.BaseDomainPersistenceModel;

import java.util.List;

import javax.annotation.Nonnull;

public interface PersistenceModelToDatabaseEntityConverter<
        PARENT extends BaseDomainPersistenceModel,
        ENTITY extends EntityModel> {

    PARENT convertParentEntityToDomainModel(@Nonnull ENTITY entity);

    ENTITY convertParentDomainModelToEntity(@Nonnull PARENT parent);

    List<PARENT> convertParentEntitiesToDomainModels(@Nonnull List<ENTITY> entities);
}
