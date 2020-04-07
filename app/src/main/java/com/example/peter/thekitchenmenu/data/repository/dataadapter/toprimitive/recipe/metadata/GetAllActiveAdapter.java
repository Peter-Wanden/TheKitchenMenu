package com.example.peter.thekitchenmenu.data.repository.dataadapter.toprimitive.recipe.metadata;

import com.example.peter.thekitchenmenu.data.repository.dataadapter.toprimitive.PrimitiveDataSource;
import com.example.peter.thekitchenmenu.data.repository.source.local.recipe.metadata.parent.RecipeMetadataParentEntity;
import com.example.peter.thekitchenmenu.data.repository.source.local.recipe.metadata.parent.RecipeMetadataParentEntityLocalDataSource;
import com.example.peter.thekitchenmenu.domain.usecase.recipe.metadata.RecipeMetadataModelPersistence;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.annotation.Nonnull;

public class GetAllActiveAdapter {

    public interface Callback {
        void onAllLoaded(@Nonnull List<RecipeMetadataModelPersistence> models);

        void onDataUnavailable();
    }

    @Nonnull
    private final RecipeMetadataParentEntityLocalDataSource parentDataSource;
    @Nonnull
    private final GetActiveByDomainIdAdapter domainIdAdapter;
    @Nonnull
    private final Set<String> parentDataIdList;
    @Nonnull
    private final List<RecipeMetadataModelPersistence> domainModels;

    private Callback callback;
    private int listSize;
    private int totalProcessed;

    public GetAllActiveAdapter(@Nonnull RecipeMetadataParentEntityLocalDataSource parentDataSource,
                               @Nonnull GetActiveByDomainIdAdapter domainIdAdapter) {
        this.parentDataSource = parentDataSource;
        this.domainIdAdapter = domainIdAdapter;

        parentDataIdList = new HashSet<>();
        domainModels = new ArrayList<>();
    }

    public void adaptLatestToDomainObjects(Callback callback) {
        this.callback = callback;
        getAllDomainIds();
    }

    private void getAllDomainIds() {
        parentDataSource.getAll(
                new PrimitiveDataSource.GetAllPrimitiveCallback<RecipeMetadataParentEntity>() {
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
            parentDataIdList.clear();
            for (RecipeMetadataParentEntity e : entities) {
                parentDataIdList.add(e.getDomainId());
            }
            listSize = parentDataIdList.size();
            getLatestModels();
        }
    }

    private void getLatestModels() {
        for (String domainId : parentDataIdList) {
            domainIdAdapter.adaptToDomainModel(domainId, new GetActiveByDomainIdAdapter.Callback() {
                @Override
                public void onModelCreated(@Nonnull RecipeMetadataModelPersistence model) {
                    totalProcessed ++;
                    domainModels.add(model);
                    returnAll();
                }

                @Override
                public void onDataUnavailable() {
                    totalProcessed ++;
                    returnAll();
                }
            });
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
