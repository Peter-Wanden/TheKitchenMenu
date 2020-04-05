package com.example.peter.thekitchenmenu.data.repository;

import com.example.peter.thekitchenmenu.data.primitivemodel.PrimitiveModel;

import java.util.List;

import javax.annotation.Nonnull;

/**
 * Base interface that sits between the persistence framework and use case persistence
 * layer {@link DataAccess}.
 * Implemented by data adapters that convert domain model objects to primitive data structures for
 * persistence frameworks that require it.
 *
 * Objects with a parent / child relationship with their internal data structures should implement
 * {@link PrimitiveDataSourceParent} or {@link PrimitiveDataSourceChild} as required.
 *
 * @param <T> primitive data structures that extend {@link PrimitiveModel}
 */
public interface PrimitiveDataSource<T extends PrimitiveModel> {

    interface GetAllCallback<E extends PrimitiveModel> {

        void onAllLoaded(List<E> entities);

        void onDataUnavailable();
    }

    interface GetEntityCallback<T extends PrimitiveModel> {

        void onEntityLoaded(T entity);

        void onDataUnavailable();
    }

    void getByDataId(@Nonnull String dataId, @Nonnull GetEntityCallback<T> callback);

    void getAll(@Nonnull GetAllCallback<T> callback);

    void save(@Nonnull T entity);

    void refreshData();

    void deleteByDataId(@Nonnull String dataId);

    void deleteAll();
}
