package com.example.peter.thekitchenmenu.data.repository.source.local.recipe.portions.dataadapter;

import com.example.peter.thekitchenmenu.data.repository.source.local.PersistenceModelToDatabaseEntityConverter;
import com.example.peter.thekitchenmenu.data.repository.source.local.recipe.portions.datasource.RecipePortionsEntity;
import com.example.peter.thekitchenmenu.domain.usecase.recipe.component.portions.RecipePortionsPersistenceDomainModel;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nonnull;

public class PortionsModelToDatabaseEntityConverterParent
        implements
        PersistenceModelToDatabaseEntityConverter<RecipePortionsPersistenceDomainModel, RecipePortionsEntity> {

    @Override
    public RecipePortionsPersistenceDomainModel convertParentEntityToDomainModel(
            @Nonnull RecipePortionsEntity entity) {
        return new RecipePortionsPersistenceDomainModel.Builder().
                setDataId(entity.getDataId()).
                setDomainId(entity.getDomainId()).
                setServings(entity.getServings()).
                setSittings(entity.getSittings()).
                setCreateDate(entity.getCreateDate()).
                setLastUpdate(entity.getLastUpdate()).
                build();
    }

    @Override
    public RecipePortionsEntity convertParentDomainModelToEntity(
            @Nonnull RecipePortionsPersistenceDomainModel parent) {
        return new RecipePortionsEntity(
                parent.getDataId(),
                parent.getDomainId(),
                parent.getServings(),
                parent.getSittings(),
                parent.getCreateDate(),
                parent.getLastUpdate()
        );
    }

    @Override
    public List<RecipePortionsPersistenceDomainModel> convertParentEntitiesToDomainModels(
            @Nonnull List<RecipePortionsEntity> entities) {
        List<RecipePortionsPersistenceDomainModel> models = new ArrayList<>();
        for (RecipePortionsEntity e : entities) {
            models.add(convertParentEntityToDomainModel(e));
        }
        return models;
    }
}
