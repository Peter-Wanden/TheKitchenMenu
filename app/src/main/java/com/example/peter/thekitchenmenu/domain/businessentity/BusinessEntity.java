package com.example.peter.thekitchenmenu.domain.businessentity;

import com.example.peter.thekitchenmenu.domain.usecasenew.common.model.DomainModel;
import com.example.peter.thekitchenmenu.domain.usecasenew.common.failreasons.FailReasons;

import java.util.ArrayList;
import java.util.List;

public abstract class BusinessEntity<ENTITY_MODEL extends DomainModel.BusinessEntityModel> {

    public interface EntityCallback<RESPONSE> {
        void onProcessed(RESPONSE response);
    }

    protected BusinessEntityRequest<ENTITY_MODEL> request;
    protected ENTITY_MODEL model;
    protected List<FailReasons> failReasons;
    protected EntityCallback<BusinessEntityResponse<ENTITY_MODEL>> callback;

    public void execute(BusinessEntityRequest<ENTITY_MODEL> request,
                        EntityCallback<BusinessEntityResponse<ENTITY_MODEL>> callback) {
        this.request = request;
        this.model = request.getModel();
        this.failReasons = new ArrayList<>();
        this.callback = callback;

        beginProcessingDomainModel();
    }

    protected abstract void beginProcessingDomainModel();

    protected abstract void sendResponse();
}
