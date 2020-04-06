package com.example.peter.thekitchenmenu.domain.usecase;

import com.example.peter.thekitchenmenu.domain.model.DomainPersistenceModel;
import com.example.peter.thekitchenmenu.domain.usecase.UseCaseDomainModel;

public abstract class PersistenceBase
        extends UseCaseDomainModel
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
