package com.example.peter.thekitchenmenu.data.repository.source.remote;

import androidx.annotation.NonNull;

import com.example.peter.thekitchenmenu.data.entity.RecipeEntity;
import com.example.peter.thekitchenmenu.data.repository.DataSource;

public class RecipeRemoteDataSource implements DataSource<RecipeEntity> {

    private static RecipeRemoteDataSource INSTANCE;

    public static RecipeRemoteDataSource getInstance() {
        if (INSTANCE == null)
            INSTANCE = new RecipeRemoteDataSource();
        return INSTANCE;
    }

    @Override
    public void getAll(@NonNull GetAllCallback callback) {
        callback.onDataNotAvailable();
    }

    @Override
    public void getById(@NonNull String recipeId, @NonNull GetEntityCallback<RecipeEntity> callback) {
        callback.onDataNotAvailable();
    }

    @Override
    public void save(@NonNull RecipeEntity recipeEntity) {
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
    public void deleteById(@NonNull String id) {

    }
}
