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
public interface DomainDataAccess<T extends DomainPersistenceModel> {

    interface GetAllDomainModelsCallback<E extends DomainPersistenceModel> {

        void onAllDomainModelsLoaded(List<E> models);

        void onDomainModelsUnavailable();
    }

    interface GetDomainModelCallback<T extends DomainPersistenceModel> {

        void onDomainModelLoaded(T model);

        void onDomainModelUnavailable();
    }

    void getByDataId(@Nonnull String dataId, @Nonnull GetDomainModelCallback<T> callback);

    void getActiveByDomainId(@Nonnull String domainId, @Nonnull GetDomainModelCallback<T> callback);

    void getAll(@Nonnull GetAllDomainModelsCallback<T> callback);

    void save(@Nonnull T model);

    void refreshData();

    void deleteByDataId(String dataId);

    void deleteAllByDomainId(@Nonnull String domainId);

    void deleteAll();
}
