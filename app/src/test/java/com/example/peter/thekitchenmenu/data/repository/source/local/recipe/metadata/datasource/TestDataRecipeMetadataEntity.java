package com.example.peter.thekitchenmenu.data.repository.source.local.recipe.metadata.datasource;

import com.example.peter.thekitchenmenu.data.repository.recipe.metadata.TestDataRecipeMetadata;
import com.example.peter.thekitchenmenu.data.repository.source.local.recipe.metadata.datasource.componentstate.RecipeComponentStateEntity;
import com.example.peter.thekitchenmenu.data.repository.source.local.recipe.metadata.datasource.failreason.RecipeFailReasonEntity;
import com.example.peter.thekitchenmenu.data.repository.source.local.recipe.metadata.datasource.parent.RecipeMetadataParentEntity;
import com.example.peter.thekitchenmenu.domain.usecase.common.failreasons.FailReasons;
import com.example.peter.thekitchenmenu.domain.usecase.recipe.component.metadata.RecipeMetadata;
import com.example.peter.thekitchenmenu.domain.usecase.recipe.component.metadata.RecipeMetadataPersistenceModel;

import java.util.ArrayList;
import java.util.List;

public class TestDataRecipeMetadataEntity {

    public static RecipeMetadataParentEntity getDataUnavailableParentEntity() {
        return getParentEntityFromPersistenceModel(TestDataRecipeMetadata.
                getDataUnavailable()
        );
    }

    public static List<RecipeFailReasonEntity> getDataUnavailableFailReasonEntities() {
        return getFailReasonEntitiesFromPersistentModel(TestDataRecipeMetadata.
                getDataUnavailable()
        );
    }

    public static List<RecipeComponentStateEntity> getDataUnavailableComponentStateEntities() {
        return getComponentStateEntitiesFromPersistentModel(TestDataRecipeMetadata.
                getDataUnavailable()
        );
    }

    public static RecipeMetadataParentEntity getInvalidUnchangedParentEntity() {
        return getParentEntityFromPersistenceModel(TestDataRecipeMetadata.
                getInvalidUnchanged()
        );
    }

    public static List<RecipeFailReasonEntity> getInvalidUnchangedFailReasonEntities() {
        return getFailReasonEntitiesFromPersistentModel(TestDataRecipeMetadata.
                getValidUnchanged()
        );
    }

    public static List<RecipeComponentStateEntity> getInvalidUnchangedComponentStateEntities() {
        return getComponentStateEntitiesFromPersistentModel(TestDataRecipeMetadata.
                getInvalidUnchanged()
        );
    }

    public static RecipeMetadataParentEntity getInvalidChangedParentEntity() {
        return getParentEntityFromPersistenceModel(TestDataRecipeMetadata.
                getInvalidChanged()
        );
    }

    public static List<RecipeFailReasonEntity> getInvalidChangedFailReasonEntities() {
        return getFailReasonEntitiesFromPersistentModel(TestDataRecipeMetadata.
                getInvalidChanged()
        );
    }

    public static List<RecipeComponentStateEntity> getInvalidChangedComponentStateEntities() {
        return getComponentStateEntitiesFromPersistentModel(TestDataRecipeMetadata.
                getInvalidChanged()
        );
    }

    public static RecipeMetadataParentEntity getValidChanged0ParentEntity() {
        return getParentEntityFromPersistenceModel(TestDataRecipeMetadata.
                getValidChangedThree()
        );
    }

    public static List<RecipeFailReasonEntity> getValidChanged0FailReasonEntities() {
        return getFailReasonEntitiesFromPersistentModel(TestDataRecipeMetadata.
                getValidChangedThree()
        );
    }

    public static List<RecipeComponentStateEntity> getValidChanged0ComponentStateEntities() {
        return getComponentStateEntitiesFromPersistentModel(TestDataRecipeMetadata.
                getValidChangedThree()
        );
    }

    public static RecipeMetadataParentEntity getValidChanged1ParentEntity() {
        return getParentEntityFromPersistenceModel(TestDataRecipeMetadata.
                getValidChangedTwo()
        );
    }

    public static List<RecipeFailReasonEntity> getValidChanged1FailReasonEntities() {
        return getFailReasonEntitiesFromPersistentModel(TestDataRecipeMetadata.
                getValidChangedTwo()
        );
    }

    public static List<RecipeComponentStateEntity> getValidChanged1ComponentStateEntities() {
        return getComponentStateEntitiesFromPersistentModel(TestDataRecipeMetadata.
                getValidChangedTwo()
        );
    }

    public static RecipeMetadataParentEntity getValidChanged2ParentEntity() {
        return getParentEntityFromPersistenceModel(TestDataRecipeMetadata.
                getValidChangedOne()
        );
    }

    public static List<RecipeFailReasonEntity> getValidChanged2FailReasonEntities() {
        return getFailReasonEntitiesFromPersistentModel(TestDataRecipeMetadata.
                getValidChangedOne()
        );
    }

    public static List<RecipeComponentStateEntity> getValidChanged2ComponentStateEntities() {
        return getComponentStateEntitiesFromPersistentModel(TestDataRecipeMetadata.
                getValidChangedOne()
        );
    }

    public static List<RecipeMetadataParentEntity> getValidChangedParentEntityList() {
        List<RecipeMetadataParentEntity> entities = new ArrayList<>();
        entities.add(0, getValidChanged0ParentEntity());
        entities.add(1, getValidChanged1ParentEntity());
        entities.add(2, getValidChanged2ParentEntity());
        return entities;
    }

    public static RecipeMetadataParentEntity getValidUnchangedParentEntity() {
        return getParentEntityFromPersistenceModel(TestDataRecipeMetadata.
                getValidUnchanged()
        );
    }

    public static List<RecipeFailReasonEntity> getValidUnchangedFailReasonEntities() {
        return getFailReasonEntitiesFromPersistentModel(TestDataRecipeMetadata.
                getValidUnchanged()
        );
    }

    public static List<RecipeComponentStateEntity> getValidUnchangedComponentStateEntities() {
        return getComponentStateEntitiesFromPersistentModel(TestDataRecipeMetadata.
                getValidUnchanged()
        );
    }

    public static RecipeMetadataParentEntity getValidFromAnotherUserParentEntity() {
        return getParentEntityFromPersistenceModel(TestDataRecipeMetadata.
                getValidFromAnotherUser()
        );
    }

    public static List<RecipeFailReasonEntity> getValidFromAnotherUserFailReasonEntities() {
        return getFailReasonEntitiesFromPersistentModel(TestDataRecipeMetadata.
                getValidFromAnotherUser()
        );
    }

    public static List<RecipeComponentStateEntity>
    getValidFromAnotherUserComponentStateEntities() {
        return getComponentStateEntitiesFromPersistentModel(TestDataRecipeMetadata.
                getValidFromAnotherUser()
        );
    }

    public static RecipeMetadataParentEntity getInvalidFromAnotherUserParentEntity() {
        return getParentEntityFromPersistenceModel(TestDataRecipeMetadata.
                getInvalidFromAnotherUser()
        );
    }

    public static List<RecipeFailReasonEntity> getInvalidFromAnotherUserFailReasonEntities() {
        return getFailReasonEntitiesFromPersistentModel(TestDataRecipeMetadata.
                getInvalidFromAnotherUser()
        );
    }

    public static List<RecipeComponentStateEntity>
    getInvalidFromAnotherUserComponentStateEntities() {
        return getComponentStateEntitiesFromPersistentModel(TestDataRecipeMetadata.
                getInvalidFromAnotherUser()
        );
    }

    public static RecipeMetadataParentEntity getValidCopiedParentEntity() {
        return getParentEntityFromPersistenceModel(TestDataRecipeMetadata.
                getValidCopied()
        );
    }

    public static List<RecipeFailReasonEntity> getValidCopiedFailReasonEntities() {
        return getFailReasonEntitiesFromPersistentModel(TestDataRecipeMetadata.
                getValidCopied()
        );
    }

    public static List<RecipeComponentStateEntity> getValidCopiedComponentStateEntities() {
        return getComponentStateEntitiesFromPersistentModel(TestDataRecipeMetadata.
                getValidCopied()
        );
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
