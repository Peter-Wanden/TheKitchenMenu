package com.example.peter.thekitchenmenu.data.repository.source.remote;

import com.example.peter.thekitchenmenu.data.primitivemodel.recipe.RecipeDurationEntity;
import com.example.peter.thekitchenmenu.data.repository.PrimitiveDataSource;

import javax.annotation.Nonnull;

public class RecipeDurationRemoteDataSource implements PrimitiveDataSource<RecipeDurationEntity> {

    private static RecipeDurationRemoteDataSource INSTANCE;

    public static RecipeDurationRemoteDataSource getInstance() {
        if (INSTANCE == null)
            INSTANCE = new RecipeDurationRemoteDataSource();
        return INSTANCE;
    }


    @Override
    public void getAll(@Nonnull GetAllCallback<RecipeDurationEntity> callback) {
        callback.onDataNotAvailable();
    }

    @Override
    public void getById(@Nonnull String id,
                        @Nonnull GetEntityCallback<RecipeDurationEntity> callback) {
        callback.onDataUnavailable();
    }

    @Override
    public void save(@Nonnull RecipeDurationEntity entity) {

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
    public void deleteById(@Nonnull String id) {

    }
}
