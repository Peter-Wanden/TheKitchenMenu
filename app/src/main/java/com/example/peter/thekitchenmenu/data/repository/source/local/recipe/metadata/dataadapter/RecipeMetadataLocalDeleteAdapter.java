package com.example.peter.thekitchenmenu.data.repository.source.local.recipe.metadata.dataadapter;

import com.example.peter.thekitchenmenu.data.repository.source.local.dataadapter.PrimitiveDataSource;
import com.example.peter.thekitchenmenu.data.repository.source.local.recipe.metadata.datasource.componentstate.RecipeComponentStateLocalDataSource;
import com.example.peter.thekitchenmenu.data.repository.source.local.recipe.metadata.datasource.failreason.RecipeFailReasonsLocalDataSource;
import com.example.peter.thekitchenmenu.data.repository.source.local.recipe.metadata.datasource.parent.RecipeMetadataParentEntity;
import com.example.peter.thekitchenmenu.data.repository.source.local.recipe.metadata.datasource.parent.RecipeMetadataParentLocalDataSource;

import java.util.List;

import javax.annotation.Nonnull;

public class RecipeMetadataLocalDeleteAdapter {
    @Nonnull
    private final RecipeMetadataParentLocalDataSource parentDataSource;
    @Nonnull
    private final RecipeComponentStateLocalDataSource componentStateDataSource;
    @Nonnull
    private final RecipeFailReasonsLocalDataSource recipeFailReasonsDataSource;

    public RecipeMetadataLocalDeleteAdapter(@Nonnull RecipeMetadataParentLocalDataSource parentDataSource,
                                     @Nonnull RecipeComponentStateLocalDataSource componentStateDataSource,
                                     @Nonnull RecipeFailReasonsLocalDataSource recipeFailReasonsDataSource) {
        this.parentDataSource = parentDataSource;
        this.componentStateDataSource = componentStateDataSource;
        this.recipeFailReasonsDataSource = recipeFailReasonsDataSource;
    }

    public void deleteDataId(String dataId) {
        recipeFailReasonsDataSource.deleteAllByParentId(dataId);
        componentStateDataSource.deleteAllByParentId(dataId);
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
        parentDataSource.getAllByDomainId(
                domainId,
                new PrimitiveDataSource.GetAllPrimitiveCallback<RecipeMetadataParentEntity>() {
                    @Override
                    public void onAllLoaded(List<RecipeMetadataParentEntity> entities) {
                        if (!entities.isEmpty()) {
                            for (RecipeMetadataParentEntity e : entities) {
                                deleteDataId(e.getDataId());
                            }
                        }
                    }

                    @Override
                    public void onDataUnavailable() {

                    }
                }
        );
    }
}
