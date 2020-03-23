package com.example.peter.thekitchenmenu.data.repository;

import com.example.peter.thekitchenmenu.data.primitivemodel.recipe.RecipeDurationEntity;

import javax.annotation.Nonnull;

public class RepositoryRecipeDuration extends Repository<RecipeDurationEntity> {

    public static RepositoryRecipeDuration INSTANCE = null;

    private RepositoryRecipeDuration(@Nonnull PrimitiveDataSource<RecipeDurationEntity> remoteDataSource,
                                     @Nonnull PrimitiveDataSource<RecipeDurationEntity> localDataSource) {
        this.remoteDataSource = remoteDataSource;
        this.localDataSource = localDataSource;
    }

    public static  RepositoryRecipeDuration getInstance(
            PrimitiveDataSource<RecipeDurationEntity> remoteDataSource,
            PrimitiveDataSource<RecipeDurationEntity> localDataSource) {

        if (INSTANCE == null)
            INSTANCE = new RepositoryRecipeDuration(remoteDataSource, localDataSource);
        return INSTANCE;
    }
}
