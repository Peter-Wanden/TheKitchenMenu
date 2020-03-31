package com.example.peter.thekitchenmenu.data.repository;

import com.example.peter.thekitchenmenu.data.primitivemodel.PrimitiveModel;

import java.util.List;

import javax.annotation.Nonnull;

/**
 * Sits between the persistence framework and use case persistence layer {@link DataSource}.
 * Primitive data models should ony be used for saving and retrieving data. They should not be
 * used or manipulated by the use cases, business entities or any other part of the application.
 *
 * @param <T> data structures that extend {@link PrimitiveModel} which are represented by any data
 *            source that requires them.
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

    /**
     * Used while recreating object relational data models. returns lists (of primitive data
     * structures) within a parent object
     * @param parentDataId the parent object
     * @param callback self explanatory
     */
    void getAllByParentDataId(@Nonnull String parentDataId,
                              @Nonnull GetAllCallback<T> callback);

    void getByDataId(@Nonnull String dataId, @Nonnull GetEntityCallback<T> callback);

    void save(@Nonnull T entity);

    void refreshData();

    void deleteAllByParentId(String parentDataId);

    void deleteAll();

    void deleteByDataId(@Nonnull String dataId);
}
