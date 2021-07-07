package com.example.peter.thekitchenmenu.data.repository;

import com.example.peter.thekitchenmenu.domain.usecasenew.common.model.DomainModel;

import java.util.List;

import javax.annotation.Nonnull;

/**
 * Implemented by the use cases and business entities to send and receive data structures to and
 * from the dataLayer.
 *
 * @param <PERSISTENCE_MODEL> any domain object data models that extend the
 *                            {@link DomainModel.PersistenceModel} interface.
 */
public interface DomainDataAccess<PERSISTENCE_MODEL extends DomainModel.PersistenceModel> {

    interface GetAllDomainModelsCallback<E extends DomainModel.PersistenceModel> {

        void onAllDomainModelsLoaded(List<E> models);

        void onDomainModelsUnavailable();
    }

    interface GetDomainModelCallback<T extends DomainModel.PersistenceModel> {

        void onPersistenceModelLoaded(T model);

        void onPersistenceModelUnavailable();
    }

    void getByDataId(@Nonnull String dataId,
                     @Nonnull GetDomainModelCallback<PERSISTENCE_MODEL> callback);

    void getByDomainId(@Nonnull String domainId,
                       @Nonnull GetDomainModelCallback<PERSISTENCE_MODEL> callback);

    void getAll(@Nonnull GetAllDomainModelsCallback<PERSISTENCE_MODEL> callback);

    void save(@Nonnull PERSISTENCE_MODEL model);

    void refreshData();

    void deleteByDataId(@Nonnull String dataId);

    void deleteByDomainId(@Nonnull String domainId);

    void deleteAll();
}
