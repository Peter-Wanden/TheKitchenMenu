package com.example.peter.thekitchenmenu.data.repository.source.local.recipe.metadata.dataadapter;

import com.example.peter.thekitchenmenu.data.repository.dataadapter.primitive.PrimitiveDataSource;
import com.example.peter.thekitchenmenu.data.repository.source.local.recipe.metadata.datasource.parent.RecipeMetadataParentEntity;
import com.example.peter.thekitchenmenu.data.repository.source.local.recipe.metadata.datasource.parent.RecipeMetadataParentLocalDataSource;
import com.example.peter.thekitchenmenu.domain.usecase.recipe.metadata.RecipeMetadataPersistenceModel;

import java.util.List;

import javax.annotation.Nonnull;

public class RecipeMetadataLocalGetActiveByDomainIdAdapter {

    public interface Callback {
        void onModelCreated(@Nonnull RecipeMetadataPersistenceModel model);

        void onDataUnavailable();
    }

    @Nonnull
    private final RecipeMetadataParentLocalDataSource parentDataSource;
    @Nonnull
    private final RecipeMetadataLocalGetByDataIdAdapter dataIdAdapter;

    private Callback callback;
    private String activeDataId = "";

    public RecipeMetadataLocalGetActiveByDomainIdAdapter(
            @Nonnull RecipeMetadataParentLocalDataSource parentDataSource,
            @Nonnull RecipeMetadataLocalGetByDataIdAdapter dataIdAdapter) {
        this.parentDataSource = parentDataSource;
        this.dataIdAdapter = dataIdAdapter;
    }

    public void getActiveModelForDomainId(@Nonnull String domainId, Callback callback) {
        this.callback = callback;
        getLatestParentEntityIdFromDomainId(domainId);
    }

    private void getLatestParentEntityIdFromDomainId(String domainId) {
        parentDataSource.getAllByDomainId(
                domainId,
                new PrimitiveDataSource.GetAllPrimitiveCallback<RecipeMetadataParentEntity>() {
                    @Override
                    public void onAllLoaded(List<RecipeMetadataParentEntity> entities) {
                        filterForActive(entities);
                    }

                    @Override
                    public void onDataUnavailable() {
                        callback.onDataUnavailable();
                    }
                }
        );
    }

    private void filterForActive(List<RecipeMetadataParentEntity> entities) {
        long lastUpdated = 0;
        activeDataId = "";

        for (RecipeMetadataParentEntity e : entities) {
            if (e.getLastUpdate() > lastUpdated) {
                lastUpdated = e.getLastUpdate();
                activeDataId = e.getDataId();
            }
        }
        if (activeDataId.isEmpty()) {
            callback.onDataUnavailable();
        } else {
            getModel();
        }
    }

    private void getModel() {
        dataIdAdapter.adaptToDomainModel(
                activeDataId,
                new RecipeMetadataLocalGetByDataIdAdapter.Callback() {
                    @Override
                    public void onModelCreated(@Nonnull RecipeMetadataPersistenceModel model) {
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
