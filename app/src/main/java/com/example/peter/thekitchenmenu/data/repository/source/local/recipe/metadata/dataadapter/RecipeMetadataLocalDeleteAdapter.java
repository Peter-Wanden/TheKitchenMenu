package com.example.peter.thekitchenmenu.data.repository.source.local.recipe.metadata.dataadapter;

import com.example.peter.thekitchenmenu.data.repository.source.local.dataadapter.PrimitiveDataSource;
import com.example.peter.thekitchenmenu.data.repository.source.local.recipe.metadata.datasource.componentstate.RecipeComponentStateLocalDataSource;
import com.example.peter.thekitchenmenu.data.repository.source.local.recipe.metadata.datasource.failreason.RecipeFailReasonsLocalDataSource;
import com.example.peter.thekitchenmenu.data.repository.source.local.recipe.metadata.datasource.parent.RecipeMetadataParentEntity;
import com.example.peter.thekitchenmenu.data.repository.source.local.recipe.metadata.datasource.parent.RecipeMetadataParentLocalDataSource;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nonnull;

public class RecipeMetadataLocalDeleteAdapter {

    @Nonnull
    private final RecipeMetadataParentLocalDataSource parentDataSource;
    @Nonnull
    private final RecipeComponentStateLocalDataSource componentStateDataSource;
    @Nonnull
    private final RecipeFailReasonsLocalDataSource recipeFailReasonsDataSource;

    public RecipeMetadataLocalDeleteAdapter(
            @Nonnull RecipeMetadataParentLocalDataSource parentDataSource,
            @Nonnull RecipeComponentStateLocalDataSource componentStateDataSource,
            @Nonnull RecipeFailReasonsLocalDataSource recipeFailReasonsDataSource) {
        this.parentDataSource = parentDataSource;
        this.componentStateDataSource = componentStateDataSource;
        this.recipeFailReasonsDataSource = recipeFailReasonsDataSource;
    }

    public void deleteByDataId(String dataId) {
        recipeFailReasonsDataSource.deleteAllByParentDataId(dataId);
        componentStateDataSource.deleteAllByParentDataId(dataId);
        parentDataSource.deleteByDataId(dataId);
    }

    public void deleteAllByDomainId(String domainId) {
        getParentDataIds(domainId);
    }

    public void deleteAll() {
        parentDataSource.deleteAll();
        componentStateDataSource.deleteAll();
        recipeFailReasonsDataSource.deleteAll();
    }

    private void getParentDataIds(String domainId) {
        List<String> parentDataIds = new ArrayList<>();
        parentDataSource.getAllByDomainId(
                domainId,
                new PrimitiveDataSource.GetAllPrimitiveCallback<RecipeMetadataParentEntity>() {
                    @Override
                    public void onAllLoaded(List<RecipeMetadataParentEntity> entities) {
                        if (!entities.isEmpty()) {
                            entities.forEach(entity -> parentDataIds.add(entity.getDataId()));
                            deleteRecordsByParentDataIds(parentDataIds);
                        }
                    }

                    @Override
                    public void onDataUnavailable() {

                    }
                }
        );
    }

    private void deleteRecordsByParentDataIds(List<String> parentDataIds) {
        parentDataIds.forEach(parentDataId -> {
            componentStateDataSource.deleteAllByParentDataId(parentDataId);
            recipeFailReasonsDataSource.deleteAllByParentDataId(parentDataId);
            parentDataSource.deleteByDataId(parentDataId);
        });
    }
}
