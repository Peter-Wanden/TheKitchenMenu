package com.example.peter.thekitchenmenu.domain.entity.model;

import com.example.peter.thekitchenmenu.domain.usecase.common.failreasons.FailReasons;

import java.util.List;

import javax.annotation.Nonnull;

public abstract class EntityResponse<ENTITY_DOMAIN_MODEL extends EntityDomainModel> {

    @Nonnull
    private final List<FailReasons> failReasons;

    @Nonnull
    private final ENTITY_DOMAIN_MODEL model;

    protected EntityResponse(@Nonnull List<FailReasons> failReasons,
                             @Nonnull ENTITY_DOMAIN_MODEL model) {
        this.failReasons = failReasons;
        this.model = model;
    }

    @Nonnull
    public List<FailReasons> getFailReasons() {
        return failReasons;
    }

    @Nonnull
    public ENTITY_DOMAIN_MODEL getModel() {
        return model;
    }
}
