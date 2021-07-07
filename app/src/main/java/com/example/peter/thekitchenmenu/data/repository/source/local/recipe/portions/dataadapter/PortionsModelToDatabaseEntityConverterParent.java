package com.example.peter.thekitchenmenu.data.repository.source.local.recipe.portions.dataadapter;

import com.example.peter.thekitchenmenu.data.repository.source.local.PersistenceModelToDatabaseEntityConverter;
import com.example.peter.thekitchenmenu.data.repository.source.local.recipe.portions.datasource.RecipePortionsEntity;
import com.example.peter.thekitchenmenu.domain.usecasenew.recipe.component.portions.RecipePortionsUseCasePersistenceModel;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nonnull;

public class PortionsModelToDatabaseEntityConverterParent
        implements
        PersistenceModelToDatabaseEntityConverter<RecipePortionsUseCasePersistenceModel, RecipePortionsEntity> {

    @Override
    public RecipePortionsUseCasePersistenceModel convertParentEntityToDomainModel(
            @Nonnull RecipePortionsEntity entity) {
        return new RecipePortionsUseCasePersistenceModel.Builder().
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
            @Nonnull RecipePortionsUseCasePersistenceModel parent) {
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
    public List<RecipePortionsUseCasePersistenceModel> convertParentEntitiesToDomainModels(
            @Nonnull List<RecipePortionsEntity> entities) {
        List<RecipePortionsUseCasePersistenceModel> models = new ArrayList<>();
        for (RecipePortionsEntity e : entities) {
            models.add(convertParentEntityToDomainModel(e));
        }
        return models;
    }
}
