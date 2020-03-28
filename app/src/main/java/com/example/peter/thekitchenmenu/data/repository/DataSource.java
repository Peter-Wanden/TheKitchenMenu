package com.example.peter.thekitchenmenu.data.repository;

import com.example.peter.thekitchenmenu.domain.model.PersistenceModel;

import java.util.List;

import javax.annotation.Nonnull;

/**
 * Used by the use cases and business entities to send and receive data structures to and from the
 * DataLayer.
 *
 * At the framework level, implementers convert object data models to data models best suited to
 * the database framework and back.
 *
 * @param <T> any object data models that extends the {@link PersistenceModel} interface.
 *            Persistence model objects should only be used for storing and retrieving data!
 */
public interface DataSource<T extends PersistenceModel> {

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
