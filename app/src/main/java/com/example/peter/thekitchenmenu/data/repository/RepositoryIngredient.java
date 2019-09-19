package com.example.peter.thekitchenmenu.data.repository;

import androidx.annotation.NonNull;

import com.example.peter.thekitchenmenu.data.entity.IngredientEntity;

public class RepositoryIngredient extends Repository<IngredientEntity> {

    public static RepositoryIngredient INSTANCE = null;

    private RepositoryIngredient(@NonNull DataSource<IngredientEntity> remoteDataSource,
                                 @NonNull DataSource<IngredientEntity> localDataSource) {
        this.remoteDataSource = remoteDataSource;
        this.localDataSource = localDataSource;
    }

    public static RepositoryIngredient getInstance(DataSource<IngredientEntity> remoteDataSource,
                                                   DataSource<IngredientEntity> localDataSource) {
        if (INSTANCE == null)
            INSTANCE = new RepositoryIngredient(remoteDataSource, localDataSource);
        return INSTANCE;
    }
}
