package com.example.peter.thekitchenmenu.data.repository.source.remote;

import androidx.annotation.NonNull;

import com.example.peter.thekitchenmenu.data.entity.RecipeDurationEntity;
import com.example.peter.thekitchenmenu.data.repository.DataSource;

public class RecipeDurationRemoteDataSource implements DataSource<RecipeDurationEntity> {

    private static RecipeDurationRemoteDataSource INSTANCE;

    public static RecipeDurationRemoteDataSource getInstance() {
        if (INSTANCE == null)
            INSTANCE = new RecipeDurationRemoteDataSource();
        return INSTANCE;
    }


    @Override
    public void getAll(@NonNull GetAllCallback<RecipeDurationEntity> callback) {
        callback.onDataNotAvailable();
    }

    @Override
    public void getById(@NonNull String id, @NonNull GetEntityCallback<RecipeDurationEntity> callback) {
        callback.onDataNotAvailable();
    }

    @Override
    public void save(@NonNull RecipeDurationEntity object) {

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
