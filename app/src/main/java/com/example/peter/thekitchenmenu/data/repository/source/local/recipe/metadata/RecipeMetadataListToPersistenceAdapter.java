package com.example.peter.thekitchenmenu.data.repository.source.local.recipe.metadata;

import com.example.peter.thekitchenmenu.data.repository.PrimitiveDataSource;
import com.example.peter.thekitchenmenu.data.repository.source.local.recipe.metadata.parent.RecipeMetadataParentEntity;
import com.example.peter.thekitchenmenu.domain.usecase.recipe.metadata.RecipeMetadataPersistenceModel;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nonnull;

public class RecipeMetadataListToPersistenceAdapter {

    interface Callback {
        void onAllLoaded(List<RecipeMetadataPersistenceModel> models);

        void onDataUnavailable();
    }

    @Nonnull
    private final PrimitiveDataSource<RecipeMetadataParentEntity> metadataSource;
    @Nonnull
    private final RecipeMetadataToPersistenceAdapter adapter;
    @Nonnull
    private final List<RecipeMetadataPersistenceModel> persistenceModels;

    private Callback callback;
    private int listSize;
    private int totalProcessed;

    public RecipeMetadataListToPersistenceAdapter(
            @Nonnull PrimitiveDataSource<RecipeMetadataParentEntity> metadataSource,
            @Nonnull RecipeMetadataToPersistenceAdapter adapter) {

        this.metadataSource = metadataSource;
        this.adapter = adapter;
        persistenceModels = new ArrayList<>();
    }

    void getAllAndNotify(@Nonnull Callback callback) {
        this.callback = callback;
        getData();
    }

    private void getData() {
        metadataSource.getAll(
                new PrimitiveDataSource.GetAllCallback<RecipeMetadataParentEntity>() {
                    @Override
                    public void onAllLoaded(List<RecipeMetadataParentEntity> parentEntities) {

                        if (parentEntities.isEmpty())
                            callback.onDataUnavailable();
                        else {
                            listSize = parentEntities.size();

                            for (RecipeMetadataParentEntity parentEntity : parentEntities) {
                                adapter.createModelAndNotify(
                                        parentEntity,
                                        new RecipeMetadataToPersistenceAdapter.Callback() {
                                            @Override
                                            public void onModelCreated(
                                                    RecipeMetadataPersistenceModel model) {

                                            }

                                            @Override
                                            public void onModelUnavailable() {

                                            }
                                        }parentEntity,
                                        e -> {
                                            persistenceModels.add(e);
                                            totalProcessed++;
                                            returnAll();
                                        }
                                );
                            }
                        }
                    }

                    @Override
                    public void onDataUnavailable() {
                        callback.onAllLoaded(new ArrayList<>());
                    }
                }
        );
    }

    private void createModel(RecipeMetadataParentEntity e) {

        adapter.createModelAndNotify(e, new RecipeMetadataToPersistenceAdapter.Callback() {
            @Override
            public void onModelCreated(RecipeMetadataPersistenceModel model) {
                persistenceModels.add(model);
                totalProcessed++;

            }

            @Override
            public void onModelUnavailable() {

            }
        });
    }

    private void returnAll() {
        if (totalProcessed == listSize) {
            listSize = 0;
            totalProcessed = 0;
            callback.onAllLoaded(persistenceModels);
        }
    }
}
