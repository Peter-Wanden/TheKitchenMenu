package com.example.peter.thekitchenmenu.data.repository.source.local.recipe.metadata.dataadapter;

import com.example.peter.thekitchenmenu.data.repository.source.local.recipe.metadata.datasource.componentstate.RecipeComponentStateEntity;
import com.example.peter.thekitchenmenu.data.repository.source.local.recipe.metadata.datasource.componentstate.RecipeComponentStateLocalDataSource;
import com.example.peter.thekitchenmenu.data.repository.source.local.recipe.metadata.datasource.failreason.RecipeFailReasonEntity;
import com.example.peter.thekitchenmenu.data.repository.source.local.recipe.metadata.datasource.failreason.RecipeFailReasonsLocalDataSource;
import com.example.peter.thekitchenmenu.data.repository.source.local.recipe.metadata.datasource.parent.RecipeMetadataParentEntity;
import com.example.peter.thekitchenmenu.data.repository.source.local.recipe.metadata.datasource.parent.RecipeMetadataParentLocalDataSource;
import com.example.peter.thekitchenmenu.domain.model.FailReasons;
import com.example.peter.thekitchenmenu.domain.usecase.recipe.metadata.RecipeMetadata;
import com.example.peter.thekitchenmenu.domain.usecase.recipe.metadata.RecipeMetadataPersistenceModel;
import com.example.peter.thekitchenmenu.domain.utils.UniqueIdProvider;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nonnull;

public class RecipeMetadataLocalSaveAdapter {

    @Nonnull
    private final RecipeMetadataParentLocalDataSource parentDataSource;
    @Nonnull
    private final RecipeComponentStateLocalDataSource componentStateDataSource;
    @Nonnull
    private final RecipeFailReasonsLocalDataSource failReasonsDataSource;
    @Nonnull
    private final UniqueIdProvider idProvider;

    public RecipeMetadataLocalSaveAdapter(
            @Nonnull RecipeMetadataParentLocalDataSource parentDataSource,
            @Nonnull RecipeComponentStateLocalDataSource componentStateDataSource,
            @Nonnull RecipeFailReasonsLocalDataSource failReasonsDataSource,
            @Nonnull UniqueIdProvider idProvider) {
        this.parentDataSource = parentDataSource;
        this.componentStateDataSource = componentStateDataSource;
        this.failReasonsDataSource = failReasonsDataSource;
        this.idProvider = idProvider;
    }

    public void save(@Nonnull RecipeMetadataPersistenceModel model) {
        saveParentEntity(model);
        saveComponentStates(model);
        saveFailReasons(model);
    }

    private void saveParentEntity(RecipeMetadataPersistenceModel model) {
        RecipeMetadataParentEntity e = new RecipeMetadataParentEntity.Builder().
                setDataId(model.getDataId()).
                setDomainId(model.getDomainId()).
                setRecipeParentDomainId(model.getParentDomainId()).
                setRecipeStateId(model.getRecipeState().errorLevel()).
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
            int componentStateAsInt = model.getComponentStates().get(name).errorLevel();

            entityList.add(
                    new RecipeComponentStateEntity(
                            dataId,
                            parentId,
                            componentNameAsInt,
                            componentStateAsInt
                    )
            );
        }
        componentStateDataSource.save(entityList.toArray(new RecipeComponentStateEntity[0]));
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
        failReasonsDataSource.save(entityList.toArray(saveArray));
    }
}
