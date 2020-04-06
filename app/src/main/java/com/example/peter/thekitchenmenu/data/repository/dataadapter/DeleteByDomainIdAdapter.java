package com.example.peter.thekitchenmenu.data.repository.dataadapter;

import com.example.peter.thekitchenmenu.data.repository.PrimitiveDataSource;
import com.example.peter.thekitchenmenu.data.repository.dataadapter.DeleteByDataIdAdapter;
import com.example.peter.thekitchenmenu.data.repository.source.local.recipe.metadata.parent.RecipeMetadataParentEntity;
import com.example.peter.thekitchenmenu.data.repository.source.local.recipe.metadata.parent.RecipeMetadataParentEntityLocalDataSource;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nonnull;

public class DeleteByDomainIdAdapter {
    @Nonnull
    private final RecipeMetadataParentEntityLocalDataSource parentDataSource;
    @Nonnull
    private final DeleteByDataIdAdapter deleteByDataIdAdapter;
    @Nonnull
    private final List<String> dataIds;

    public DeleteByDomainIdAdapter(
            @Nonnull RecipeMetadataParentEntityLocalDataSource parentDataSource,
            @Nonnull DeleteByDataIdAdapter deleteByDataIdAdapter) {
        this.parentDataSource = parentDataSource;
        this.deleteByDataIdAdapter = deleteByDataIdAdapter;

        dataIds = new ArrayList<>();
    }

    public void deleteAllForDomainId(String domainId) {
        getParentDataIds(domainId);
    }

    private void getParentDataIds(String domainId) {
        parentDataSource.getAllByDomainId(
                domainId,
                new PrimitiveDataSource.GetAllCallback<RecipeMetadataParentEntity>() {
            @Override
            public void onAllLoaded(List<RecipeMetadataParentEntity> entities) {
                for (RecipeMetadataParentEntity e : entities) {
                    dataIds.add(e.getDataId());
                }
                delete();
            }

            @Override
            public void onDataUnavailable() {

            }
        });
    }

    private void delete() {
        for (String dataId : dataIds) {
            deleteByDataIdAdapter.deleteDataId(dataId);
        }
    }
}
