package com.example.peter.thekitchenmenu.data.source.remote;

import androidx.annotation.NonNull;

import com.example.peter.thekitchenmenu.data.entity.RecipeEntity;
import com.example.peter.thekitchenmenu.data.repository.RecipeDataSource;

public class RecipeRemoteDataSource implements RecipeDataSource {

    private static RecipeRemoteDataSource INSTANCE;

    public static RecipeRemoteDataSource getInstance() {
        if (INSTANCE == null)
            INSTANCE = new RecipeRemoteDataSource();
        return INSTANCE;

    }

    @Override
    public void getAll(@NonNull LoadAllCallback callback) {
        callback.onDataNotAvailable();
    }

    @Override
    public void getById(@NonNull String recipeId, @NonNull GetItemCallback callback) {
        callback.onDataNotAvailable();
    }

    @Override
    public void save(@NonNull RecipeEntity recipeEntity) {
        // Not required because the {@link RecipeRepository} handles the logic of refreshing the
        // recipes from all the available data sources.
    }

    @Override
    public void refresh() {

    }

    @Override
    public void deleteAll() {

    }

    @Override
    public void deleteById(@NonNull String id) {

    }
}
