package com.example.peter.thekitchenmenu.domain.model;

import com.example.peter.thekitchenmenu.domain.usecase.common.UseCaseBase;

/**
 * Tagging interface for all domain data model classes
 */
public interface DomainModel {

    /**
     * Use case internal domain model
     */
    interface UseCaseDomainModel {
    }

    /**
     * All domain models that are to be persisted implement this interface
     */
    interface PersistenceDomainModel {

        String getDataId();

        String getDomainId();

        long getCreateDate();

        long getLastUpdate();
    }

    interface RequestDomainModel {

    }

    interface ResponseDomainModel {

    }
}
