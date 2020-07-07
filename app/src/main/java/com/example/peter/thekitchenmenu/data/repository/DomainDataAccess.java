package com.example.peter.thekitchenmenu.data.repository;

import com.example.peter.thekitchenmenu.domain.model.DomainModel;

import java.util.List;

import javax.annotation.Nonnull;

/**
 * Implemented by the use cases and business entities to send and receive data structures to and
 * from the dataLayer.
 *
 * @param <T> any domain object data models that extend the {@link DomainModel.PersistenceDomainModel}
 *           interface.
 */
public interface DomainDataAccess<T extends DomainModel.PersistenceDomainModel> {

    interface GetAllDomainModelsCallback<E extends DomainModel.PersistenceDomainModel> {

        void onAllDomainModelsLoaded(List<E> models);

        void onDomainModelsUnavailable();
    }

    interface GetDomainModelCallback<T extends DomainModel.PersistenceDomainModel> {

        void onPersistenceModelLoaded(T model);

        void onPersistenceModelUnavailable();
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
