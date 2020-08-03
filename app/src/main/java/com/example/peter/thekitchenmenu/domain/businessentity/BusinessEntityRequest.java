package com.example.peter.thekitchenmenu.domain.businessentity;

import com.example.peter.thekitchenmenu.domain.usecasenew.model.DomainModel;

import javax.annotation.Nonnull;

public class BusinessEntityRequest<ENTITY_MODEL extends DomainModel.BusinessEntityModel> {
    @Nonnull
    private final ENTITY_MODEL model;

    public BusinessEntityRequest(@Nonnull ENTITY_MODEL model) {
        this.model = model;
    }

    @Nonnull
    public ENTITY_MODEL getModel() {
        return model;
    }

    @Nonnull
    @Override
    public String toString() {
        return "EntityRequest{" +
                "model=" + model +
                '}';
    }
}
