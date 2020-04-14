package com.example.peter.thekitchenmenu.data.repository.source.local.recipe.metadata;

import com.example.peter.thekitchenmenu.data.repository.source.local.recipe.metadata.datasource.componentstate.RecipeComponentStateEntity;
import com.example.peter.thekitchenmenu.data.repository.source.local.recipe.metadata.datasource.failreason.RecipeFailReasonEntity;
import com.example.peter.thekitchenmenu.data.repository.source.local.recipe.metadata.datasource.parent.RecipeMetadataParentEntity;
import com.example.peter.thekitchenmenu.domain.model.FailReasons;
import com.example.peter.thekitchenmenu.domain.usecase.recipe.metadata.RecipeMetadataPersistenceModel;

import java.util.ArrayList;
import java.util.List;

import static com.example.peter.thekitchenmenu.domain.usecase.recipe.metadata.RecipeMetadata.*;

public class TestDataRecipeMetadataEntity {

    public static RecipeMetadataParentEntity getRecipeMetadataParentEntityFromDomainModel(
            RecipeMetadataPersistenceModel model) {
        return new RecipeMetadataParentEntity.Builder().
                setDataId(model.getDataId()).
                setDomainId(model.getDomainId()).
                setRecipeParentDomainId(model.getParentDomainId()).
                setRecipeStateId(model.getRecipeState().getId()).
                setCreatedBy(model.getCreatedBy()).
                setCreateDate(model.getCreateDate()).
                setLastUpdate(model.getLastUpdate()).
                build();
    }

    public static List<RecipeFailReasonEntity> getRecipeFailReasonEntitiesFromDomainModel(
            RecipeMetadataPersistenceModel model) {

        List<RecipeFailReasonEntity> failReasonEntities = new ArrayList<>();
        int dataId = 0;
        for (FailReasons failReason : model.getFailReasons()) {
            failReasonEntities.add(
                    new RecipeFailReasonEntity(
                            String.valueOf(dataId),
                            model.getDataId(),
                            failReason.getId()
                    )
            );
            dataId ++;
        }
        return failReasonEntities;
    }

    public static List<RecipeComponentStateEntity> getComponentStateEntitiesFromDomainModel(
            RecipeMetadataPersistenceModel model) {
        List<RecipeComponentStateEntity> componentStateEntities = new ArrayList<>();

        int dataId = 0;
        for (ComponentName componentName : model.getComponentStates().keySet()) {
            componentStateEntities.add(
                    new RecipeComponentStateEntity(
                            String.valueOf(dataId),
                            model.getDataId(),
                            componentName.getId(),
                            model.getComponentStates().get(componentName).getId()
                    )
            );
            dataId ++;
        }
        return componentStateEntities;
    }
}
























