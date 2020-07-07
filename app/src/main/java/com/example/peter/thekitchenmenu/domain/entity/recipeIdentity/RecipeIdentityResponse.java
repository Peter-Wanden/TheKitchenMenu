package com.example.peter.thekitchenmenu.domain.entity.recipeIdentity;

import com.example.peter.thekitchenmenu.domain.entity.model.EntityResponse;
import com.example.peter.thekitchenmenu.domain.usecase.common.failreasons.FailReasons;

import java.util.List;

import javax.annotation.Nonnull;

public class RecipeIdentityResponse
        extends
        EntityResponse<RecipeIdentity.RecipeIdentityEntityDomainModel> {

    public RecipeIdentityResponse(@Nonnull List<FailReasons> failReasons,
                                  @Nonnull RecipeIdentity.RecipeIdentityEntityDomainModel model) {
        super(failReasons, model);
    }
}
