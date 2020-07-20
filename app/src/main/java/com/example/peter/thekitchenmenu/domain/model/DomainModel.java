package com.example.peter.thekitchenmenu.domain.model;

import javax.annotation.Nonnull;

/**
 * Tagging interface for all domain data model classes
 */
public interface DomainModel {

    interface EntityModel {

    }

    /**
     * Use case internal domain model
     */
    interface UseCaseModel {
    }

    /**
     * All domain models that are to be persisted implement this interface
     */
    interface PersistenceModel {

        String getDataId();

        String getDomainId();

        long getCreateDate();

        long getLastUpdate();
    }

    interface RequestModel {

    }

    interface ResponseModel {

    }

    interface ModelConverter<
            ENTITY_MODEL extends EntityModel,
            USE_CASE_MODEL extends UseCaseModel,
            PERSISTENCE_MODEL extends PersistenceModel,
            REQUEST_MODEL extends RequestModel,
            RESPONSE_MODEL extends ResponseModel> {

        ENTITY_MODEL convertUseCaseToEntityModel(@Nonnull USE_CASE_MODEL useCaseModel);

        USE_CASE_MODEL convertPersistenceToDomainModel(@Nonnull PERSISTENCE_MODEL model);

        USE_CASE_MODEL convertRequestToUseCaseModel(@Nonnull REQUEST_MODEL model);

        USE_CASE_MODEL convertEntityToUseCaseModel(@Nonnull ENTITY_MODEL entityModel);

        PERSISTENCE_MODEL createNewPersistenceModel();

        PERSISTENCE_MODEL createArchivedPersistenceModel(@Nonnull PERSISTENCE_MODEL model);

        RESPONSE_MODEL convertUseCaseToResponseModel(@Nonnull USE_CASE_MODEL model);
    }
}
