package com.example.peter.thekitchenmenu.data.repository.source.local.recipe.metadata.dataadapter;

import com.example.peter.thekitchenmenu.data.repository.source.local.PersistenceModelToDatabaseEntityConverter;
import com.example.peter.thekitchenmenu.data.repository.source.local.recipe.metadata.datasource.parent.RecipeMetadataParentEntity;
import com.example.peter.thekitchenmenu.domain.usecasenew.common.metadata.ComponentState;
import com.example.peter.thekitchenmenu.domain.usecasenew.recipe.invoker.metadata.RecipeMacroMetadataUseCasePersistenceModel;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nonnull;

public class RecipeMetadataToDatabaseEntityConverter
        implements
        PersistenceModelToDatabaseEntityConverter<RecipeMacroMetadataUseCasePersistenceModel, RecipeMetadataParentEntity> {

    @Override
    public RecipeMacroMetadataUseCasePersistenceModel convertParentEntityToDomainModel(
            @Nonnull RecipeMetadataParentEntity entity) {
        return new RecipeMacroMetadataUseCasePersistenceModel.Builder().
                setDataId(entity.getDataId()).
                setDomainId(entity.getDomainId()).
                setComponentState(ComponentState.fromInt(entity.getRecipeStateId())).
                setCreateDate(entity.getCreateDate()).
                setLastUpdate(entity.getLastUpdate()).
                build();
    }

    @Override
    public RecipeMetadataParentEntity convertParentDomainModelToEntity(
            @Nonnull RecipeMacroMetadataUseCasePersistenceModel model) {
        return new RecipeMetadataParentEntity.Builder().
                setDataId(model.getDataId()).
                setDomainId(model.getDomainId()).
                setRecipeStateId(model.getComponentState().id()).
                setCreateDate(model.getCreateDate()).
                setLastUpdate(model.getLastUpdate()).
                build();
    }

    @Override
    public List<RecipeMacroMetadataUseCasePersistenceModel> convertParentEntitiesToDomainModels(
            @Nonnull List<RecipeMetadataParentEntity> entities) {

        List<RecipeMacroMetadataUseCasePersistenceModel> models = new ArrayList<>();
        entities.forEach(entity -> models.add(convertParentEntityToDomainModel(entity)));
        return models;
    }
}
