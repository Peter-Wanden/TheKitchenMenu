package com.example.peter.thekitchenmenu.domain.usecase;

import com.example.peter.thekitchenmenu.domain.model.DomainPersistenceModel;

public abstract class BasePersistence
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
