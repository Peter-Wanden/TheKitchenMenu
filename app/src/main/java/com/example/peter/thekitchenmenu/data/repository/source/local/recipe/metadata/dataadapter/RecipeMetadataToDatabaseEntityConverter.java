package com.example.peter.thekitchenmenu.data.repository.source.local.recipe.metadata.dataadapter;

import com.example.peter.thekitchenmenu.data.repository.source.local.PersistenceModelToDatabaseEntityConverter;
import com.example.peter.thekitchenmenu.data.repository.source.local.recipe.metadata.datasource.parent.RecipeMetadataParentEntity;
import com.example.peter.thekitchenmenu.domain.usecase.recipe.component.metadata.RecipeMetadata.ComponentState;
import com.example.peter.thekitchenmenu.domain.usecase.recipe.component.metadata.RecipeMetadataPersistenceDomainModel;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nonnull;

public class RecipeMetadataToDatabaseEntityConverter
        implements
        PersistenceModelToDatabaseEntityConverter<RecipeMetadataPersistenceDomainModel, RecipeMetadataParentEntity> {

    @Override
    public RecipeMetadataPersistenceDomainModel convertParentEntityToDomainModel(
            @Nonnull RecipeMetadataParentEntity entity) {
        return new RecipeMetadataPersistenceDomainModel.Builder().
                setDataId(entity.getDataId()).
                setDomainId(entity.getDomainId()).
                setParentDomainId(entity.getParentDomainId()).
                setRecipeState(ComponentState.fromInt(entity.getRecipeStateId())).
                setCreatedBy(entity.getCreatedBy()).
                setCreateDate(entity.getCreateDate()).
                setLastUpdate(entity.getLastUpdate()).
                build();
    }

    @Override
    public RecipeMetadataParentEntity convertParentDomainModelToEntity(
            @Nonnull RecipeMetadataPersistenceDomainModel model) {
        return new RecipeMetadataParentEntity.Builder().
                setDataId(model.getDataId()).
                setDomainId(model.getDomainId()).
                setRecipeParentDomainId(model.getParentDomainId()).
                setRecipeStateId(model.getComponentState().id()).
                setCreatedBy(model.getCreatedBy()).
                setCreateDate(model.getCreateDate()).
                setLastUpdate(model.getLastUpdate()).
                build();
    }

    @Override
    public List<RecipeMetadataPersistenceDomainModel> convertParentEntitiesToDomainModels(
            @Nonnull List<RecipeMetadataParentEntity> entities) {

        List<RecipeMetadataPersistenceDomainModel> models = new ArrayList<>();
        entities.forEach(entity -> models.add(convertParentEntityToDomainModel(entity)));
        return models;
    }
}
