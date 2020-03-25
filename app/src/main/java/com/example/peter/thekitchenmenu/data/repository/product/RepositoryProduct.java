package com.example.peter.thekitchenmenu.data.repository.product;

import com.example.peter.thekitchenmenu.data.primitivemodel.product.ProductEntity;
import com.example.peter.thekitchenmenu.data.repository.PrimitiveDataSource;
import com.example.peter.thekitchenmenu.data.repository.Repository;

import javax.annotation.Nonnull;

public class RepositoryProduct extends Repository<ProductEntity> {

    private RepositoryProduct(
            @Nonnull PrimitiveDataSource<ProductEntity> remoteDataSource,
            @Nonnull PrimitiveDataSource<ProductEntity> localDataSource) {
        this.remoteDataSource = remoteDataSource;
        this.localDataSource = localDataSource;
    }

    public static RepositoryProduct getInstance(
            PrimitiveDataSource<ProductEntity> remoteDataSource,
            PrimitiveDataSource<ProductEntity> localDataSource) {

        if (INSTANCE == null)
            INSTANCE = new RepositoryProduct(remoteDataSource, localDataSource);
        return (RepositoryProduct) INSTANCE;
    }
}
