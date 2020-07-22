package com.example.peter.thekitchenmenu.data.repository.product;

import com.example.peter.thekitchenmenu.data.primitivemodel.product.ProductEntity;
import com.example.peter.thekitchenmenu.data.repository.source.local.dataadapter.PrimitiveDataSource;
import com.example.peter.thekitchenmenu.data.repository.DataAccess;
import com.example.peter.thekitchenmenu.domain.usecase.product.component.identity.ProductIdentityPersistenceModel;

public class DataAccessProduct extends DataAccess<ProductIdentityPersistenceModel> {

    protected static DataAccess<ProductIdentityPersistenceModel> INSTANCE = null;

//    private RepositoryProduct(
//            @Nonnull PrimitiveDataSource<ProductEntity> remoteDataSource,
//            @Nonnull PrimitiveDataSource<ProductEntity> localDataSource) {
//        this.remoteDomainDataAccess = remoteDataSource;
//        this.localDomainDataAccess = localDataSource;
//    }

    public static DataAccessProduct getInstance(
            PrimitiveDataSource<ProductEntity> remoteDataSource,
            PrimitiveDataSource<ProductEntity> localDataSource) {

//        if (INSTANCE == null) {
//            INSTANCE = new RepositoryProduct(remoteDataSource, localDataSource);
//        }
        return (DataAccessProduct) INSTANCE;
    }
}
