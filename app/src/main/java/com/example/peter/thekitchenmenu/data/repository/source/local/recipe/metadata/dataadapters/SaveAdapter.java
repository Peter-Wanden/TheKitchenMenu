package com.example.peter.thekitchenmenu.data.repository.source.local.recipe.metadata.dataadapters;

import com.example.peter.thekitchenmenu.data.repository.source.local.recipe.metadata.componentstate.RecipeComponentStateEntity;
import com.example.peter.thekitchenmenu.data.repository.source.local.recipe.metadata.componentstate.RecipeComponentStateLocalDataSource;
import com.example.peter.thekitchenmenu.data.repository.source.local.recipe.metadata.failreason.RecipeFailReasonEntity;
import com.example.peter.thekitchenmenu.data.repository.source.local.recipe.metadata.failreason.RecipeFailReasonsLocalDataSource;
import com.example.peter.thekitchenmenu.data.repository.source.local.recipe.metadata.parent.RecipeMetadataParentEntity;
import com.example.peter.thekitchenmenu.data.repository.source.local.recipe.metadata.parent.RecipeMetadataParentEntityLocalDataSource;
import com.example.peter.thekitchenmenu.domain.model.FailReasons;
import com.example.peter.thekitchenmenu.domain.usecase.recipe.metadata.RecipeMetadata;
import com.example.peter.thekitchenmenu.domain.usecase.recipe.metadata.RecipeMetadataPersistenceModel;
import com.example.peter.thekitchenmenu.domain.utils.UniqueIdProvider;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nonnull;

public class SaveAdapter {

    @Nonnull
    private final RecipeMetadataParentEntityLocalDataSource parentDataSource;
    @Nonnull
    private final RecipeComponentStateLocalDataSource componentStateDataSource;
    @Nonnull
    private final RecipeFailReasonsLocalDataSource recipeFailReasonsDataSource;
    @Nonnull
    private final UniqueIdProvider idProvider;

    public SaveAdapter(@Nonnull RecipeMetadataParentEntityLocalDataSource parentDataSource,
                       @Nonnull RecipeComponentStateLocalDataSource componentStateDataSource,
                       @Nonnull RecipeFailReasonsLocalDataSource recipeFailReasonsDataSource,
                       @Nonnull UniqueIdProvider idProvider) {
        this.parentDataSource = parentDataSource;
        this.componentStateDataSource = componentStateDataSource;
        this.recipeFailReasonsDataSource = recipeFailReasonsDataSource;
        this.idProvider = idProvider;
    }

    public void adaptAndSave(@Nonnull RecipeMetadataPersistenceModel model) {
        saveParentEntity(model);
        saveComponentStates(model);
        saveFailReasons(model);
    }

    private void saveParentEntity(RecipeMetadataPersistenceModel model) {
        RecipeMetadataParentEntity e = new RecipeMetadataParentEntity.Builder().
                setDataId(model.getDataId()).
                setDomainId(model.getDomainId()).
                setRecipeParentId(model.getRecipeParentId()).
                setRecipeStateId(model.getRecipeState().getId()).
                setCreatedBy(model.getCreatedBy()).
                setCreateDate(model.getCreateDate()).
                setLastUpdate(model.getLastUpdate()).
                build();

        parentDataSource.save(e);
    }

    private void saveComponentStates(RecipeMetadataPersistenceModel model) {
        List<RecipeComponentStateEntity> entityList = new ArrayList<>();

        for (RecipeMetadata.ComponentName name : model.getComponentStates().keySet()) {
            String dataId = idProvider.getUId();
            String parentId = model.getDataId();
            int componentNameAsInt = name.getId();
            int componentStateAsInt = model.getComponentStates().get(name).getId();

            entityList.add(
                    new RecipeComponentStateEntity(
                            dataId,
                            parentId,
                            componentNameAsInt,
                            componentStateAsInt
                    ));
        }
        RecipeComponentStateEntity[] saveArray = new RecipeComponentStateEntity[entityList.size()];
        componentStateDataSource.saveAll(entityList.toArray(saveArray));
    }

    private void saveFailReasons(RecipeMetadataPersistenceModel model) {
        List<RecipeFailReasonEntity> entityList = new ArrayList<>();

        for (FailReasons f : model.getFailReasons()) {
            String dataId = idProvider.getUId();
            String parentDataId = model.getDataId();
            int failReasonAsInt = f.getId();

            entityList.add(new RecipeFailReasonEntity(
                    dataId,
                    parentDataId,
                    failReasonAsInt
            ));
        }
        RecipeFailReasonEntity[] saveArray = new RecipeFailReasonEntity[entityList.size()];
        recipeFailReasonsDataSource.saveAll(entityList.toArray(saveArray));
    }
}
