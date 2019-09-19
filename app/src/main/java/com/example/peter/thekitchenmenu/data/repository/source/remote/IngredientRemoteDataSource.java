package com.example.peter.thekitchenmenu.data.repository.source.remote;

import androidx.annotation.NonNull;

import com.example.peter.thekitchenmenu.data.entity.IngredientEntity;
import com.example.peter.thekitchenmenu.data.repository.DataSource;

public class IngredientRemoteDataSource implements DataSource<IngredientEntity> {

    private static IngredientRemoteDataSource INSTANCE;

    public static IngredientRemoteDataSource getInstance() {
        if (INSTANCE == null)
            INSTANCE = new IngredientRemoteDataSource();
        return INSTANCE;
    }

    @Override
    public void getAll(@NonNull GetAllCallback<IngredientEntity> callback) {
        callback.onDataNotAvailable();
    }

    @Override
    public void getById(@NonNull String id, @NonNull GetEntityCallback<IngredientEntity> callback) {
        callback.onDataNotAvailable();
    }

    @Override
    public void save(@NonNull IngredientEntity object) {

    }

    @Override
    public void refreshData() {
        // Not required because the {@link Repository} handles the logic of refreshing data
        // from all the available data sources.
    }

    @Override
    public void deleteAll() {

    }

    @Override
    public void deleteById(@NonNull String id) {

    }
}
