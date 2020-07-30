package com.example.peter.thekitchenmenu.domain.businessentity;

import com.example.peter.thekitchenmenu.domain.usecasenew.model.DomainModel;
import com.example.peter.thekitchenmenu.domain.usecasenew.common.failreasons.FailReasons;

import java.util.ArrayList;
import java.util.List;

public abstract class BusinessEntity<ENTITY_MODEL extends DomainModel.EntityModel> {

    public interface EntityCallback<RESPONSE> {
        void onProcessed(RESPONSE response);
    }

    protected EntityRequest<ENTITY_MODEL> request;
    protected ENTITY_MODEL model;
    protected List<FailReasons> failReasons;
    protected EntityCallback<EntityResponse<ENTITY_MODEL>> callback;

    public void execute(EntityRequest<ENTITY_MODEL> request,
                        EntityCallback<EntityResponse<ENTITY_MODEL>> callback) {
        this.request = request;
        this.model = request.getModel();
        this.failReasons = new ArrayList<>();
        this.callback = callback;

        processDataElements();
    }

    protected abstract void processDataElements();

    protected abstract void sendResponse();
}
