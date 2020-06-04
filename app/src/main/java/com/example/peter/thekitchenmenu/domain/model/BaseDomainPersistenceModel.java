package com.example.peter.thekitchenmenu.domain.model;

public abstract class BaseDomainPersistenceModel
        extends BaseDomainModel
        implements DomainPersistenceModel {

    protected String dataId;
    protected String domainId;

    @Override
    public String getDataId() {
        return dataId;
    }

    @Override
    public String getDomainId() {
        return domainId;
    }
}
