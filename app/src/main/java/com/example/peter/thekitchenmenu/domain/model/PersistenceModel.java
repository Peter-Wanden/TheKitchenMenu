package com.example.peter.thekitchenmenu.domain.model;

/**
 * All domain models that are to be persisted implement this interface
 * id = the unique id of the data structure assigned by the application
 */
public interface PersistenceModel {

    String getDataId();

    String getDomainId();
}
