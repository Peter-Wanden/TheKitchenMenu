package com.example.peter.thekitchenmenu.data.repository.source.local.recipe.metadata;

import com.example.peter.thekitchenmenu.data.repository.source.local.recipe.metadata.componentstate.RecipeComponentStateEntity;
import com.example.peter.thekitchenmenu.data.repository.source.local.recipe.metadata.failreason.RecipeFailReasonEntity;
import com.example.peter.thekitchenmenu.data.repository.source.local.recipe.metadata.parent.RecipeMetadataParentEntity;
import com.example.peter.thekitchenmenu.domain.model.FailReasons;
import com.example.peter.thekitchenmenu.domain.usecase.recipe.metadata.RecipeMetadataPersistenceModel;
import com.example.peter.thekitchenmenu.domain.utils.UniqueIdProvider;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.annotation.Nonnull;

import static com.example.peter.thekitchenmenu.domain.usecase.recipe.metadata.RecipeMetadata.*;

public class RecipeMetadataToPrimitiveAdapter {

    private final UniqueIdProvider idProvider;
    private final RecipeMetadataPersistenceModel persistenceModel;

    private RecipeMetadataParentEntity parentEntity;
    private final List<RecipeFailReasonEntity> failReasonEntities;
    private final List<RecipeComponentStateEntity> componentStateEntities;

    RecipeMetadataToPrimitiveAdapter(
            @Nonnull UniqueIdProvider idProvider,
            @Nonnull RecipeMetadataPersistenceModel persistenceModel) {

        this.idProvider = idProvider;
        this.persistenceModel = persistenceModel;
        failReasonEntities = new ArrayList<>();
        componentStateEntities = new ArrayList<>();
    }

    Model convertToPrimitives() {
        createParentEntity();
        createComponentStateList();
        createFailReasonList();

        return new Model(
                parentEntity,
                componentStateEntities,
                failReasonEntities
        );
    }

    private void createParentEntity() {
        parentEntity = new RecipeMetadataParentEntity.Builder().
                setDataId(persistenceModel.getDataId()).
                setRecipeId(persistenceModel.getDomainId()).
                setRecipeParentId(persistenceModel.getRecipeParentId()).
                setRecipeStateId(persistenceModel.getRecipeState().getId()).
                setCreatedBy(persistenceModel.getCreatedBy()).
                setCreateDate(persistenceModel.getCreateDate()).
                setLastUpdate(persistenceModel.getLastUpdate()).
                build();

    }

    private void createComponentStateList() {
        RecipeComponentStateEntity componentStateEntity;

        for (ComponentName componentName : persistenceModel.getComponentStates().keySet()) {
            componentStateEntity = new RecipeComponentStateEntity(
                    idProvider.getUId(),
                    persistenceModel.getDataId(),
                    componentName.getId(),
                    persistenceModel.getComponentStates().get(componentName).getId()
            );
            componentStateEntities.add(componentStateEntity);
        }
    }

    private void createFailReasonList() {
        RecipeFailReasonEntity failReasonEntity;

        for (FailReasons failReason : persistenceModel.getFailReasons()) {
            failReasonEntity = new RecipeFailReasonEntity(
                    idProvider.getUId(),
                    persistenceModel.getDataId(),
                    failReason.getId()
            );
            failReasonEntities.add(failReasonEntity);
        }
    }

    public static final class Model {
        @Nonnull
        private final RecipeMetadataParentEntity parentEntity;
        @Nonnull
        private final List<RecipeComponentStateEntity> stateEntities;
        @Nonnull
        private final List<RecipeFailReasonEntity> failReasonEntities;

        private Model(
                @Nonnull RecipeMetadataParentEntity parentEntity,
                @Nonnull List<RecipeComponentStateEntity> stateEntities,
                @Nonnull List<RecipeFailReasonEntity> failReasonEntities) {
            this.parentEntity = parentEntity;
            this.stateEntities = stateEntities;
            this.failReasonEntities = failReasonEntities;
        }

        @Nonnull
        RecipeMetadataParentEntity getParentEntity() {
            return parentEntity;
        }

        @Nonnull
        List<RecipeComponentStateEntity> getStateEntities() {
            return stateEntities;
        }

        @Nonnull
        List<RecipeFailReasonEntity> getFailReasonEntities() {
            return failReasonEntities;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Model that = (Model) o;
            return parentEntity.equals(that.parentEntity) &&
                    stateEntities.equals(that.stateEntities) &&
                    failReasonEntities.equals(that.failReasonEntities);
        }

        @Override
        public int hashCode() {
            return Objects.hash(parentEntity, stateEntities, failReasonEntities);
        }

        @Nonnull
        @Override
        public String toString() {
            return "RecipeMetadataPrimitiveModels{" +
                    "parentEntity=" + parentEntity +
                    ", stateEntities=" + stateEntities +
                    ", failReasonEntities=" + failReasonEntities +
                    '}';
        }
    }
}
