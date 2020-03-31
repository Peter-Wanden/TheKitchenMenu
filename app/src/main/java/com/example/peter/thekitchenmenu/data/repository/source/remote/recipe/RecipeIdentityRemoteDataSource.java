package com.example.peter.thekitchenmenu.data.repository.source.remote.recipe;

import com.example.peter.thekitchenmenu.data.repository.source.local.recipe.identity.RecipeIdentityEntity;
import com.example.peter.thekitchenmenu.data.repository.PrimitiveDataSource;

import javax.annotation.Nonnull;

public class RecipeIdentityRemoteDataSource implements PrimitiveDataSource<RecipeIdentityEntity> {

    private static RecipeIdentityRemoteDataSource INSTANCE;

    public static RecipeIdentityRemoteDataSource getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new RecipeIdentityRemoteDataSource();
        }
        return INSTANCE;
    }

    @Override
    public void getAll(@Nonnull GetAllCallback<RecipeIdentityEntity> callback) {
        callback.onDataUnavailable();
    }

    @Override
    public void getByDataId(@Nonnull String dataId,
                            @Nonnull GetEntityCallback<RecipeIdentityEntity> callback) {
        callback.onDataUnavailable();
    }

    @Override
    public void save(@Nonnull RecipeIdentityEntity entity) {

    }

    @Override
    public void refreshData() {

    }

    @Override
    public void deleteAll() {

    }

    @Override
    public void deleteByDataId(@Nonnull String dataId) {

    }
}
