package com.example.peter.thekitchenmenu.domain.entity;

import com.example.peter.thekitchenmenu.domain.entity.model.EntityDomainModel;
import com.example.peter.thekitchenmenu.domain.entity.model.EntityResponse;

public interface BusinessEntity<
        ENTITY_DOMAIN_MODEL extends EntityDomainModel,
        ENTITY_RESPONSE extends EntityResponse> {

    interface Callback<ENTITY_RESPONSE extends EntityResponse> {
        void onProcessed(ENTITY_RESPONSE response);
    }

    void processEntityModel(ENTITY_DOMAIN_MODEL requestModel, Callback<ENTITY_RESPONSE> callback);
}
