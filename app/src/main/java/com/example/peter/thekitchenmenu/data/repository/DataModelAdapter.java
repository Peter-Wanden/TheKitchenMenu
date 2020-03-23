package com.example.peter.thekitchenmenu.data.repository;

import com.example.peter.thekitchenmenu.domain.model.PersistenceModel;

import java.util.List;

import javax.annotation.Nonnull;

/**
 * Used by the use cases and business entities to send and receive data structures to and from the
 * DataLayer.
 *
 * When persisting data, users of this interface convert object data models to primitive data
 * models before sending them to the data layer using the {@link PrimitiveDataSource} interface.
 *
 * When retrieving data, users of this interface use the {@link PrimitiveDataSource} interface
 * to get the required data and convert it into object data models before returning to the calling
 * callback.
 *
 * @param <T> any object data models that extends the {@link PersistenceModel} interface.
 *            Persistence model objects should only be used for storing and retrieving data.
 */
public interface DataModelAdapter<T extends PersistenceModel> {

    interface GetAllCallback<E extends PersistenceModel> {

        void onAllLoaded(List<E> models);

        void onDataUnavailable();
    }

    interface GetModelCallback<T extends PersistenceModel> {

        void onModelLoaded(T model);

        void onModelUnavailable();
    }

    void getAll(@Nonnull GetAllCallback<T> callback);

    void getById(@Nonnull String id, @Nonnull GetModelCallback<T> callback);

    void save(@Nonnull T model);

    void refreshData();

    void deleteAll();

    void deleteById(@Nonnull String id);
}
