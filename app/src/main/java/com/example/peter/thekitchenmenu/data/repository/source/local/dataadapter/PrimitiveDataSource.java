package com.example.peter.thekitchenmenu.data.repository.source.local.dataadapter;

import com.example.peter.thekitchenmenu.data.primitivemodel.EntityModel;
import com.example.peter.thekitchenmenu.data.repository.DomainDataAccess;

import java.util.List;

import javax.annotation.Nonnull;

/**
 * Base interface that sits between the persistence framework and use case persistence
 * layer {@link DomainDataAccess}.
 * Implemented by data adapters that convert domain model objects to primitive data structures for
 * persistence frameworks that require it.
 *
 * Objects with a parent / child relationship with their internal data structures should implement
 * {@link PrimitiveDataSourceParent} or {@link PrimitiveDataSourceChild} as required.
 *
 * @param <T> primitive data structures that extend {@link EntityModel}
 */
public interface PrimitiveDataSource<T extends EntityModel> {

    interface GetAllPrimitiveCallback<E extends EntityModel> {

        void onAllLoaded(List<E> entities);

        void onDataUnavailable();
    }

    interface GetPrimitiveCallback<T extends EntityModel> {

        void onEntityLoaded(T entity);

        void onDataUnavailable();
    }

    void getByDataId(@Nonnull String dataId, @Nonnull GetPrimitiveCallback<T> callback);

    void getAll(@Nonnull GetAllPrimitiveCallback<T> callback);

    void save(@Nonnull T entity);

    void refreshData();

    void deleteByDataId(@Nonnull String dataId);

    void deleteAll();
}
