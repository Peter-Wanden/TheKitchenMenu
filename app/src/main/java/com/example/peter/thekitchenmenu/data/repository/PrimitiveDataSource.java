package com.example.peter.thekitchenmenu.data.repository;

import com.example.peter.thekitchenmenu.data.primitivemodel.PrimitiveModel;

import java.util.List;

import javax.annotation.Nonnull;

/**
 * Sits between the persistence framework and use case persistence layer {@link DataAccess}.
 * Implemented by data adapters that convert domain model objects to primitive data structures for
 * persistence frameworks that require it.
 *
 * Primitive data structures should ony be used for saving and retrieving data. They should not be
 * used or manipulated by the use cases, business entities or any other part of the application.
 *
 * @param <T> primitive data structures that extend {@link PrimitiveModel} which are represented
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

    void getAll(@Nonnull GetAllCallback<T> callback);

    void getAllByDomainId(@Nonnull String domainId, GetAllCallback<T> callback);

    void getByDataId(@Nonnull String dataId, @Nonnull GetEntityCallback<T> callback);

    void save(@Nonnull T entity);

    void refreshData();

    void deleteByDataId(@Nonnull String dataId);

    void deleteAllByDomainId(@Nonnull String domainId);

    void deleteAll();
}
