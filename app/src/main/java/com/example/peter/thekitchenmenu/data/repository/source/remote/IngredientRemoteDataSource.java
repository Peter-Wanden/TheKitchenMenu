package com.example.peter.thekitchenmenu.data.repository.source.remote;

import com.example.peter.thekitchenmenu.data.primitivemodel.ingredient.IngredientEntity;
import com.example.peter.thekitchenmenu.data.repository.dataadapter.primitive.PrimitiveDataSource;

import javax.annotation.Nonnull;

public class IngredientRemoteDataSource implements PrimitiveDataSource<IngredientEntity> {

    private static IngredientRemoteDataSource INSTANCE;

    public static IngredientRemoteDataSource getInstance() {
        if (INSTANCE == null)
            INSTANCE = new IngredientRemoteDataSource();
        return INSTANCE;
    }

    @Override
    public void getAll(@Nonnull GetAllPrimitiveCallback<IngredientEntity> callback) {
        callback.onDataUnavailable();
    }

    @Override
    public void getByDataId(@Nonnull String dataId,
                            @Nonnull GetPrimitiveCallback<IngredientEntity> callback) {
        callback.onDataUnavailable();
    }

    @Override
    public void save(@Nonnull IngredientEntity entity) {

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
    public void deleteByDataId(@Nonnull String dataId) {

    }
}
