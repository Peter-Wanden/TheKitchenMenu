package com.example.peter.thekitchenmenu.data.repository;

import com.example.peter.thekitchenmenu.data.primitivemodel.ingredient.IngredientEntity;

import javax.annotation.Nonnull;

public class RepositoryIngredient extends Repository<IngredientEntity> {

    public static RepositoryIngredient INSTANCE = null;

    private RepositoryIngredient(@Nonnull PrimitiveDataSource<IngredientEntity> remoteDataSource,
                                 @Nonnull PrimitiveDataSource<IngredientEntity> localDataSource) {
        this.remoteDataSource = remoteDataSource;
        this.localDataSource = localDataSource;
    }

    public static RepositoryIngredient getInstance(PrimitiveDataSource<IngredientEntity> remoteDataSource,
                                                   PrimitiveDataSource<IngredientEntity> localDataSource) {
        if (INSTANCE == null)
            INSTANCE = new RepositoryIngredient(remoteDataSource, localDataSource);
        return INSTANCE;
    }
}
