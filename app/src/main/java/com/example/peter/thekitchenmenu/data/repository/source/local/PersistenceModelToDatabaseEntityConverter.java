package com.example.peter.thekitchenmenu.data.repository.source.local;

import com.example.peter.thekitchenmenu.data.primitivemodel.EntityModel;
import com.example.peter.thekitchenmenu.domain.model.BasePersistenceModel;

import java.util.List;

import javax.annotation.Nonnull;

public interface PersistenceModelToDatabaseEntityConverter<
        PARENT extends BasePersistenceModel,
        ENTITY extends EntityModel> {

    PARENT convertParentEntityToDomainModel(@Nonnull ENTITY entity);

    ENTITY convertParentDomainModelToEntity(@Nonnull PARENT parent);

    List<PARENT> convertParentEntitiesToDomainModels(@Nonnull List<ENTITY> entities);
}
