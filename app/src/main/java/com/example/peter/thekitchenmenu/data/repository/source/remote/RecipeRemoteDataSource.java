package com.example.peter.thekitchenmenu.data.repository.source.remote;

import com.example.peter.thekitchenmenu.data.primitivemodel.recipe.RecipeMetadataEntity;
import com.example.peter.thekitchenmenu.data.repository.PrimitiveDataSource;

import javax.annotation.Nonnull;

public class RecipeRemoteDataSource implements PrimitiveDataSource<RecipeMetadataEntity> {

    private static RecipeRemoteDataSource INSTANCE;

    public static RecipeRemoteDataSource getInstance() {
        if (INSTANCE == null)
            INSTANCE = new RecipeRemoteDataSource();
        return INSTANCE;
    }

    @Override
    public void getAll(@Nonnull GetAllCallback callback) {
        callback.onDataNotAvailable();
    }

    @Override
    public void getById(@Nonnull String recipeId,
                        @Nonnull GetEntityCallback<RecipeMetadataEntity> callback) {
        callback.onDataUnavailable();
    }

    @Override
    public void save(@Nonnull RecipeMetadataEntity entity) {
        // Not required because the {@link RepositoryRecipe} handles the logic of refreshing the
        // recipes from all the available data sources.
    }

    @Override
    public void refreshData() {

    }

    @Override
    public void deleteAll() {

    }

    @Override
    public void deleteById(@Nonnull String id) {

    }
}
