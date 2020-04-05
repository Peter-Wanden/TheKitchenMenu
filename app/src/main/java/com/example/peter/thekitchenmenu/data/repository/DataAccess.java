package com.example.peter.thekitchenmenu.data.repository;

import com.example.peter.thekitchenmenu.domain.model.DomainPersistenceModel;

import java.util.List;

import javax.annotation.Nonnull;

/**
 * Implemented by the use cases and business entities to send and receive data structures to and
 * from the dataLayer.
 *
 * @param <T> any domain object data models that extend the {@link DomainPersistenceModel}
 *           interface.
 */
public interface DataAccess<T extends DomainPersistenceModel> {

    interface GetAllDomainModelsCallback<E extends DomainPersistenceModel> {

        void onAllLoaded(List<E> models);

        void onModelsUnavailable();
    }

    interface GetDomainModelCallback<T extends DomainPersistenceModel> {

        void onModelLoaded(T model);

        void onModelUnavailable();
    }

    void getAll(@Nonnull GetAllDomainModelsCallback<T> callback);

    void getByDataId(@Nonnull String dataId, @Nonnull GetDomainModelCallback<T> callback);

    void getLatestByDomainId(@Nonnull String domainId, @Nonnull GetDomainModelCallback<T> callback);

    void save(@Nonnull T model);

    void refreshData();

    void deleteByDataId(String dataId);

    void deleteAllByDomainId(@Nonnull String domainId);

    void deleteAll();
}
