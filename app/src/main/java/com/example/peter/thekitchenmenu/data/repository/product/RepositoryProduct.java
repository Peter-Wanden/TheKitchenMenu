package com.example.peter.thekitchenmenu.data.repository.product;

import com.example.peter.thekitchenmenu.data.primitivemodel.product.ProductEntity;
import com.example.peter.thekitchenmenu.data.repository.source.local.dataadapter.PrimitiveDataSource;
import com.example.peter.thekitchenmenu.data.repository.Repository;
import com.example.peter.thekitchenmenu.domain.usecase.product.component.identity.ProductIdentityPersistenceModel;

import javax.annotation.Nonnull;

public class RepositoryProduct extends Repository<ProductIdentityPersistenceModel> {

//    private RepositoryProduct(
//            @Nonnull PrimitiveDataSource<ProductEntity> remoteDataSource,
//            @Nonnull PrimitiveDataSource<ProductEntity> localDataSource) {
//        this.remoteDomainDataAccess = remoteDataSource;
//        this.localDomainDataAccess = localDataSource;
//    }

    public static RepositoryProduct getInstance(
            PrimitiveDataSource<ProductEntity> remoteDataSource,
            PrimitiveDataSource<ProductEntity> localDataSource) {

//        if (INSTANCE == null) {
//            INSTANCE = new RepositoryProduct(remoteDataSource, localDataSource);
//        }
        return (RepositoryProduct) INSTANCE;
    }
}
