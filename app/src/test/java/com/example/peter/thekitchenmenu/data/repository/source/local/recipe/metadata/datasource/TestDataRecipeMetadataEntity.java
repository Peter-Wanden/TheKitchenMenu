package com.example.peter.thekitchenmenu.data.repository.source.local.recipe.metadata.datasource;

import com.example.peter.thekitchenmenu.data.repository.recipe.metadata.TestDataRecipeMetadata;
import com.example.peter.thekitchenmenu.data.repository.source.local.recipe.metadata.datasource.componentstate.RecipeComponentStateEntity;
import com.example.peter.thekitchenmenu.data.repository.source.local.recipe.metadata.datasource.failreason.RecipeFailReasonEntity;
import com.example.peter.thekitchenmenu.data.repository.source.local.recipe.metadata.datasource.parent.RecipeMetadataParentEntity;
import com.example.peter.thekitchenmenu.domain.usecasenew.common.failreasons.FailReasons;
import com.example.peter.thekitchenmenu.domain.usecase.recipe.component.metadata.RecipeMetadata;
import com.example.peter.thekitchenmenu.domain.usecase.recipe.component.metadata.RecipeMetadataPersistenceModel;

import java.util.ArrayList;
import java.util.List;

public class TestDataRecipeMetadataEntity {

    public static RecipeMetadataParentEntity getDataUnavailableParentEntity() {
        return getParentEntityFromPersistenceModel(TestDataRecipeMetadata.
                getInvalidDefault()
        );
    }

    public static List<RecipeFailReasonEntity> getDataUnavailableFailReasonEntities() {
        return getFailReasonEntitiesFromPersistentModel(TestDataRecipeMetadata.
                getInvalidDefault()
        );
    }

    public static List<RecipeComponentStateEntity> getDataUnavailableComponentStateEntities() {
        return getComponentStateEntitiesFromPersistentModel(TestDataRecipeMetadata.
                getInvalidDefault()
        );
    }

    public static RecipeMetadataParentEntity getValidChanged0ParentEntity() {
        return getParentEntityFromPersistenceModel(TestDataRecipeMetadata.
                getValidChanged()
        );
    }

    public static List<RecipeFailReasonEntity> getValidChanged0FailReasonEntities() {
        return getFailReasonEntitiesFromPersistentModel(TestDataRecipeMetadata.
                getValidChanged()
        );
    }

    public static List<RecipeComponentStateEntity> getValidChanged0ComponentStateEntities() {
        return getComponentStateEntitiesFromPersistentModel(TestDataRecipeMetadata.
                getValidChanged()
        );
    }

    public static RecipeMetadataParentEntity getValidChangedParentEntity() {
        return getParentEntityFromPersistenceModel(TestDataRecipeMetadata.
                getValidChanged()
        );
    }

    public static List<RecipeMetadataParentEntity> getValidChangedParentEntityList() {
        List<RecipeMetadataParentEntity> entities = new ArrayList<>();
        entities.add(0, getValidChanged0ParentEntity());
        entities.add(1, getValidChangedParentEntity());
        return entities;
    }

    private static RecipeMetadataParentEntity getParentEntityFromPersistenceModel(
            RecipeMetadataPersistenceModel m) {
        return new RecipeMetadataParentEntity.Builder().
                setDataId(m.getDataId()).
                setDomainId(m.getDomainId()).
                setRecipeParentDomainId(m.getParentDomainId()).
                setRecipeStateId(m.getComponentState().id()).
                setCreatedBy(m.getCreatedBy()).
                setCreateDate(m.getCreateDate()).
                setLastUpdate(m.getLastUpdate()).
                build();
    }

    private static List<RecipeFailReasonEntity> getFailReasonEntitiesFromPersistentModel(
            RecipeMetadataPersistenceModel m) {
        List<RecipeFailReasonEntity> entities = new ArrayList<>();
        int dataId = 0;
        for (FailReasons f : m.getFailReasons()) {
            entities.add(
                    new RecipeFailReasonEntity(
                            String.valueOf(dataId),
                            m.getDataId(),
                            f.getId())
            );
            dataId++;
        }
        return entities;
    }

    private static List<RecipeComponentStateEntity> getComponentStateEntitiesFromPersistentModel(
            RecipeMetadataPersistenceModel model) {
        List<RecipeComponentStateEntity> e = new ArrayList<>();
        int dataId = 0;
        for (RecipeMetadata.ComponentName name : model.getComponentStates().keySet()) {
            e.add(
                    new RecipeComponentStateEntity(
                            String.valueOf(dataId),
                            model.getDataId(),
                            name.getId(),
                            model.getComponentStates().get(name).id()
                    )
            );
            dataId++;
        }
        return e;
    }

    public static List<RecipeMetadataParentEntity> getAllByDomainId(String domainId) {
        List<RecipeMetadataParentEntity> entities = new ArrayList<>();
        for (RecipeMetadataPersistenceModel m : TestDataRecipeMetadata.getAll()) {
            if (domainId.equals(m.getDomainId())) {
                entities.add(getParentEntityFromPersistenceModel(m));
            }
        }
        return entities;
    }
}
