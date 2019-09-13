package com.example.peter.thekitchenmenu.data.repository;

import androidx.annotation.NonNull;

import com.example.peter.thekitchenmenu.data.entity.RecipeDurationEntity;

public class RepositoryRecipeDuration extends Repository<RecipeDurationEntity> {

    public static RepositoryRecipeDuration INSTANCE = null;

    private RepositoryRecipeDuration(@NonNull DataSource<RecipeDurationEntity> remoteDataSource,
                                     @NonNull DataSource<RecipeDurationEntity> localDataSource) {
        this.remoteDataSource = remoteDataSource;
        this.localDataSource = localDataSource;
    }

    public static  RepositoryRecipeDuration getInstance(
            DataSource<RecipeDurationEntity> remoteDataSource,
            DataSource<RecipeDurationEntity> localDataSource) {

        if (INSTANCE == null)
            INSTANCE = new RepositoryRecipeDuration(remoteDataSource, localDataSource);
        return INSTANCE;
    }
}
