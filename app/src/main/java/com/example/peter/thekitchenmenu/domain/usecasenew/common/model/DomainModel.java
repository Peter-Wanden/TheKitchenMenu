package com.example.peter.thekitchenmenu.domain.usecasenew.common.model;

/**
 * Tagging interface for all domain data model classes
 */
public interface DomainModel {

    /**
     * Business entity domain model
     */
    interface BusinessEntityModel {
    }

    /**
     * Use case domain model
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

    /**
     * Request domain model
     */
    interface UseCaseRequestModel {
    }

    /**
     * Response domain model
     */
    interface UseCaseResponseModel {
    }

}
