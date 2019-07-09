package com.example.peter.thekitchenmenu.data.repository;

import androidx.annotation.NonNull;

import com.example.peter.thekitchenmenu.data.entity.ProductEntity;

import static androidx.core.util.Preconditions.checkNotNull;

public class RepositoryProduct extends Repository<ProductEntity> {

    public static RepositoryProduct INSTANCE = null;

    private RepositoryProduct(
            @NonNull DataSource<ProductEntity> remoteDataSource,
            @NonNull DataSource<ProductEntity> localDataSource) {

        this.remoteDataSource = checkNotNull(remoteDataSource);
        this.localDataSource = checkNotNull(localDataSource);
    }

    public static RepositoryProduct getInstance(
            DataSource<ProductEntity> remoteDataSource,
            DataSource<ProductEntity> localDataSource) {

        if (INSTANCE == null)
            INSTANCE = new RepositoryProduct(remoteDataSource, localDataSource);
        return INSTANCE;
    }

}
