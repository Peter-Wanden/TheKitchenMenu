package com.example.peter.thekitchenmenu.data.repository.source.remote;

import androidx.annotation.NonNull;

import com.example.peter.thekitchenmenu.data.entity.RecipeIdentityEntity;
import com.example.peter.thekitchenmenu.data.repository.DataSource;

public class RecipeIdentityRemoteDataSource implements DataSource<RecipeIdentityEntity> {

    private static RecipeIdentityRemoteDataSource INSTANCE;

    public static RecipeIdentityRemoteDataSource getInstance() {
        if (INSTANCE == null)
            INSTANCE = new RecipeIdentityRemoteDataSource();
        return INSTANCE;
    }

    @Override
    public void getAll(@NonNull GetAllCallback<RecipeIdentityEntity> callback) {
        callback.onDataNotAvailable();
    }

    @Override
    public void getById(@NonNull String id, @NonNull GetEntityCallback<RecipeIdentityEntity> callback) {
        callback.onDataNotAvailable();
    }

    @Override
    public void save(@NonNull RecipeIdentityEntity object) {

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
