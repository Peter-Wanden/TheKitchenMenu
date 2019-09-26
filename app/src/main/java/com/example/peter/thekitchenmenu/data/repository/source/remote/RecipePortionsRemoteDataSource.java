package com.example.peter.thekitchenmenu.data.repository.source.remote;

import androidx.annotation.NonNull;

import com.example.peter.thekitchenmenu.data.entity.RecipePortionsEntity;
import com.example.peter.thekitchenmenu.data.repository.DataSourceRecipePortions;

public class RecipePortionsRemoteDataSource implements DataSourceRecipePortions {

    private static RecipePortionsRemoteDataSource INSTANCE;

    public static RecipePortionsRemoteDataSource getInstance() {
        if (INSTANCE == null)
            INSTANCE = new RecipePortionsRemoteDataSource();
        return INSTANCE;
    }

    @Override
    public void getPortionsForRecipe(@NonNull String recipeId, @NonNull GetEntityCallback<RecipePortionsEntity> callback) {
        callback.onDataNotAvailable();
    }

    @Override
    public void getAll(@NonNull GetAllCallback<RecipePortionsEntity> callback) {
        callback.onDataNotAvailable();
    }

    @Override
    public void getById(@NonNull String id, @NonNull GetEntityCallback<RecipePortionsEntity> callback) {
        callback.onDataNotAvailable();
    }

    @Override
    public void save(@NonNull RecipePortionsEntity object) {

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
