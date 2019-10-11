package com.example.peter.thekitchenmenu.data.repository.source.remote;

import androidx.annotation.NonNull;

import com.example.peter.thekitchenmenu.data.entity.RecipeIngredientQuantityEntity;
import com.example.peter.thekitchenmenu.data.repository.DataSourceRecipeIngredient;

public class RecipeIngredientRemoteDataSource implements DataSourceRecipeIngredient {

    private static RecipeIngredientRemoteDataSource INSTANCE;

    public static RecipeIngredientRemoteDataSource getInstance() {
        if (INSTANCE == null)
            INSTANCE = new RecipeIngredientRemoteDataSource();
        return INSTANCE;
    }

    @Override
    public void getByRecipeId(@NonNull String recipeId, @NonNull GetAllCallback<RecipeIngredientQuantityEntity> callback) {
        callback.onDataNotAvailable();
    }

    @Override
    public void getByProductId(@NonNull String productId, @NonNull GetAllCallback<RecipeIngredientQuantityEntity> callback) {
        callback.onDataNotAvailable();
    }

    @Override
    public void getByIngredientId(@NonNull String ingredientId, @NonNull GetAllCallback<RecipeIngredientQuantityEntity> callback) {
        callback.onDataNotAvailable();
    }

    @Override
    public void getAll(@NonNull GetAllCallback<RecipeIngredientQuantityEntity> callback) {
        callback.onDataNotAvailable();
    }

    @Override
    public void getById(@NonNull String id, @NonNull GetEntityCallback<RecipeIngredientQuantityEntity> callback) {
        callback.onDataNotAvailable();
    }

    @Override
    public void save(@NonNull RecipeIngredientQuantityEntity object) {

    }

    @Override
    public void refreshData() {
        // Not required because the {@link TasksRepository} handles the logic of refreshing the
        // data from all the available sources.
    }

    @Override
    public void deleteAll() {

    }

    @Override
    public void deleteById(@NonNull String id) {

    }
}
