package com.example.peter.thekitchenmenu.domain.businessentity;

import com.example.peter.thekitchenmenu.domain.usecasenew.common.failreasons.FailReasons;
import com.example.peter.thekitchenmenu.domain.usecasenew.model.DomainModel;

import java.util.List;

import javax.annotation.Nonnull;

public class BusinessEntityResponse<ENTITY_MODEL extends DomainModel.BusinessEntityModel> {
    @Nonnull
    private final ENTITY_MODEL model;
    @Nonnull
    private final List<FailReasons> failReasons;

    public BusinessEntityResponse(@Nonnull ENTITY_MODEL model, @Nonnull List<FailReasons> failReasons) {
        this.model = model;
        this.failReasons = failReasons;
    }

    @Nonnull
    @Override
    public String toString() {
        return "EntityResponse{" +
                "model=" + model +
                ", failReasons=" + failReasons +
                '}';
    }

    @Nonnull
    public ENTITY_MODEL getModel() {
        return model;
    }

    @Nonnull
    public List<FailReasons> getFailReasons() {
        return failReasons;
    }
}
