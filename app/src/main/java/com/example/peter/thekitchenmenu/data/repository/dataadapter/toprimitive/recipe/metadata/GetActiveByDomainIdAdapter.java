package com.example.peter.thekitchenmenu.data.repository.dataadapter.toprimitive.recipe.metadata;

import com.example.peter.thekitchenmenu.data.repository.dataadapter.toprimitive.PrimitiveDataSource;
import com.example.peter.thekitchenmenu.data.repository.source.local.recipe.metadata.parent.RecipeMetadataParentEntity;
import com.example.peter.thekitchenmenu.data.repository.source.local.recipe.metadata.parent.RecipeMetadataParentEntityLocalDataSource;
import com.example.peter.thekitchenmenu.domain.usecase.recipe.metadata.RecipeMetadataModelPersistence;

import java.util.List;

import javax.annotation.Nonnull;

public class GetActiveByDomainIdAdapter {

    public interface Callback {
        void onModelCreated(@Nonnull RecipeMetadataModelPersistence model);

        void onDataUnavailable();
    }

    @Nonnull
    private final RecipeMetadataParentEntityLocalDataSource parentDataSource;
    @Nonnull
    private final RecipeMetadataLocalGetByDataIdAdapter dataIdAdapter;

    private Callback callback;
    private String lastUpdatedDataId = "";

    public GetActiveByDomainIdAdapter(
            @Nonnull RecipeMetadataParentEntityLocalDataSource parentDataSource,
            @Nonnull RecipeMetadataLocalGetByDataIdAdapter dataIdAdapter) {
        this.parentDataSource = parentDataSource;
        this.dataIdAdapter = dataIdAdapter;
    }

    public void adaptToDomainModel(@Nonnull String domainId, Callback callback) {
        this.callback = callback;
        getLatestParentEntityIdFromDomainId(domainId);
    }

    private void getLatestParentEntityIdFromDomainId(String domainId) {
        parentDataSource.getAllByDomainId(
                domainId,
                new PrimitiveDataSource.GetAllPrimitiveCallback<RecipeMetadataParentEntity>() {
                    @Override
                    public void onAllLoaded(List<RecipeMetadataParentEntity> entities) {
                        filterLatest(entities);
                    }

                    @Override
                    public void onDataUnavailable() {
                        callback.onDataUnavailable();
                    }
                }
        );
    }

    private void filterLatest(List<RecipeMetadataParentEntity> entities) {
        long lastUpdated = 0;

        for (RecipeMetadataParentEntity e : entities) {
            if (e.getLastUpdate() > lastUpdated) {
                lastUpdated = e.getLastUpdate();
                lastUpdatedDataId = e.getId();
            }
        }
        if (lastUpdatedDataId.isEmpty()) {
            callback.onDataUnavailable();
        } else {
            getModel();
        }
    }

    private void getModel() {
        dataIdAdapter.adaptToDomainModel(
                lastUpdatedDataId,
                new RecipeMetadataLocalGetByDataIdAdapter.Callback() {
                    @Override
                    public void onModelCreated(@Nonnull RecipeMetadataModelPersistence model) {
                        callback.onModelCreated(model);
                    }

                    @Override
                    public void onDataUnavailable() {
                        callback.onDataUnavailable();
                    }
                }
        );
    }
}
