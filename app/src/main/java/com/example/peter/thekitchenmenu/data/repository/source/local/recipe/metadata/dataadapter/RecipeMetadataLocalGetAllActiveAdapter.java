package com.example.peter.thekitchenmenu.data.repository.source.local.recipe.metadata.dataadapter;

import com.example.peter.thekitchenmenu.data.repository.source.local.dataadapter.PrimitiveDataSource.GetAllPrimitiveCallback;
import com.example.peter.thekitchenmenu.data.repository.source.local.recipe.metadata.datasource.parent.RecipeMetadataParentEntity;
import com.example.peter.thekitchenmenu.data.repository.source.local.recipe.metadata.datasource.parent.RecipeMetadataParentLocalDataSource;
import com.example.peter.thekitchenmenu.domain.usecase.recipe.metadata.RecipeMetadataPersistenceModel;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.annotation.Nonnull;

public class RecipeMetadataLocalGetAllActiveAdapter {

    public interface Callback {
        void onAllLoaded(@Nonnull List<RecipeMetadataPersistenceModel> models);

        void onDataUnavailable();
    }

    @Nonnull
    private final RecipeMetadataParentLocalDataSource parentDataSource;
    @Nonnull
    private final RecipeMetadataLocalGetActiveByDomainIdAdapter domainIdAdapter;
    @Nonnull
    private final Set<String> parentDomainIdList;
    @Nonnull
    private final List<RecipeMetadataPersistenceModel> domainModels;

    private Callback callback;
    private int listSize;
    private int totalProcessed;

    public RecipeMetadataLocalGetAllActiveAdapter(
            @Nonnull RecipeMetadataParentLocalDataSource parentDataSource,
            @Nonnull RecipeMetadataLocalGetActiveByDomainIdAdapter domainIdAdapter) {
        this.parentDataSource = parentDataSource;
        this.domainIdAdapter = domainIdAdapter;

        parentDomainIdList = new HashSet<>();
        domainModels = new ArrayList<>();
    }

    public void adaptLatestToDomainObjects(Callback callback) {
        this.callback = callback;
        getAllDomainIds();
    }

    private void getAllDomainIds() {
        parentDataSource.getAll(
                new GetAllPrimitiveCallback<RecipeMetadataParentEntity>() {
                    @Override
                    public void onAllLoaded(List<RecipeMetadataParentEntity> entities) {
                        createUniqueDomainIdList(entities);
                    }

                    @Override
                    public void onDataUnavailable() {
                        callback.onDataUnavailable();
                    }
                }
        );
    }

    private void createUniqueDomainIdList(List<RecipeMetadataParentEntity> entities) {
        if (entities.isEmpty()) {
            callback.onDataUnavailable();
        } else {
            parentDomainIdList.clear();
            for (RecipeMetadataParentEntity e : entities) {
                parentDomainIdList.add(e.getDomainId());
            }
            listSize = parentDomainIdList.size();
            getLatestModels();
        }
    }

    private void getLatestModels() {
        for (String domainId : parentDomainIdList) {
            domainIdAdapter.getActiveModelForDomainId(
                    domainId,
                    new RecipeMetadataLocalGetActiveByDomainIdAdapter.Callback() {
                        @Override
                        public void onModelCreated(@Nonnull RecipeMetadataPersistenceModel model) {
                            totalProcessed++;
                            domainModels.add(model);
                            returnAll();
                        }

                        @Override
                        public void onDataUnavailable() {
                            totalProcessed++;
                            returnAll();
                        }
                    }
            );
        }
    }

    private void returnAll() {
        if (totalProcessed == listSize) {
            listSize = 0;
            totalProcessed = 0;
            callback.onAllLoaded(domainModels);
        }
    }
}
