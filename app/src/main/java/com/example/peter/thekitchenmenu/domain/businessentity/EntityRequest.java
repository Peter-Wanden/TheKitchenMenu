package com.example.peter.thekitchenmenu.domain.businessentity;

import com.example.peter.thekitchenmenu.domain.usecasenew.model.DomainModel;

import javax.annotation.Nonnull;

public class EntityRequest<ENTITY_MODEL extends DomainModel.EntityModel> {
    @Nonnull
    private final ENTITY_MODEL model;

    public EntityRequest(@Nonnull ENTITY_MODEL model) {
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
