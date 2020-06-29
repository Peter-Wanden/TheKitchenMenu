package com.example.peter.thekitchenmenu.data.repository.source.local.recipe.metadata.dataadapter;

import com.example.peter.thekitchenmenu.data.repository.source.local.DomainModelConverter;
import com.example.peter.thekitchenmenu.data.repository.source.local.recipe.metadata.datasource.parent.RecipeMetadataParentEntity;
import com.example.peter.thekitchenmenu.domain.usecase.recipe.component.metadata.RecipeMetadata.ComponentState;
import com.example.peter.thekitchenmenu.domain.usecase.recipe.component.metadata.RecipeMetadataPersistenceModel;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nonnull;

public class RecipeMetadataConverter
        implements
        DomainModelConverter<RecipeMetadataPersistenceModel, RecipeMetadataParentEntity> {

    @Override
    public RecipeMetadataPersistenceModel convertParentEntityToDomainModel(
            @Nonnull RecipeMetadataParentEntity entity) {
        return new RecipeMetadataPersistenceModel.Builder().
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
            @Nonnull RecipeMetadataPersistenceModel model) {
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
    public List<RecipeMetadataPersistenceModel> convertParentEntitiesToDomainModels(
            @Nonnull List<RecipeMetadataParentEntity> entities) {

        List<RecipeMetadataPersistenceModel> models = new ArrayList<>();
        entities.forEach(entity -> models.add(convertParentEntityToDomainModel(entity)));
        return models;
    }
}
