package com.example.peter.thekitchenmenu.domain.model;

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

        ENTITY_MODEL convertUseCaseToEntityModel(USE_CASE_MODEL useCaseModel);

        USE_CASE_MODEL convertPersistenceToDomainModel(PERSISTENCE_MODEL model);

        USE_CASE_MODEL convertRequestToUseCaseModel(REQUEST_MODEL model);

        USE_CASE_MODEL convertEntityToUseCaseModel(ENTITY_MODEL entityModel);

        PERSISTENCE_MODEL createNewPersistenceModel();

        PERSISTENCE_MODEL createArchivedPersistenceModel(PERSISTENCE_MODEL model);

        RESPONSE_MODEL convertUseCaseToResponseModel();
    }
}
