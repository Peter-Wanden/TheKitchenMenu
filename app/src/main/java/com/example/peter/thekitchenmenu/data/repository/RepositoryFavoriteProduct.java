package com.example.peter.thekitchenmenu.data.repository;

import androidx.annotation.NonNull;

import com.example.peter.thekitchenmenu.data.entity.FavoriteProductEntity;

import static androidx.core.util.Preconditions.checkNotNull;

public class RepositoryFavoriteProduct extends Repository<FavoriteProductEntity> {

    public static RepositoryFavoriteProduct INSTANCE = null;

    private RepositoryFavoriteProduct(
            @NonNull DataSource<FavoriteProductEntity> remoteDataSource,
            @NonNull DataSource<FavoriteProductEntity> localDataSource) {

        this.remoteDataSource = checkNotNull(remoteDataSource);
        this.localDataSource = checkNotNull(localDataSource);
    }

    public static RepositoryFavoriteProduct getInstance(
            DataSource<FavoriteProductEntity> remoteDataSource,
            DataSource<FavoriteProductEntity> localDataSource) {

        if (INSTANCE == null)
            INSTANCE = new RepositoryFavoriteProduct(remoteDataSource, localDataSource);
        return INSTANCE;
    }
}
