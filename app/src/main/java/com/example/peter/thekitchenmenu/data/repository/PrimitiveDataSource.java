package com.example.peter.thekitchenmenu.data.repository;

import com.example.peter.thekitchenmenu.data.primitivemodel.PrimitiveModel;

import java.util.List;

import javax.annotation.Nonnull;

/**
 * Used by the {@link DataModelAdapter} to send and receive primitive data models too and from the
 * frameworks data layer.
 *
 * Primitive data models should ony be used for saving and retrieving data
 * models. They should not be used or manipulated by the use cases, business entities or any other
 * part of the application.
 *
 * @param <T> data structures that extend {@link PrimitiveModel} which are represented in the
 *            repository.
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

    void getById(@Nonnull String id, @Nonnull GetEntityCallback<T> callback);

    void save(@Nonnull T entity);

    void refreshData();

    void deleteAll();

    void deleteById(@Nonnull String id);
}
