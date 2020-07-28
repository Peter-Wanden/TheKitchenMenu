package com.example.peter.thekitchenmenu.domain.usecase.recipe.component.identity;

import com.example.peter.thekitchenmenu.domain.usecase.common.usecasemessage.message.UseCaseRequestWithId;

import javax.annotation.Nonnull;

public class UseCaseRequest<REQUEST_MODEL extends >
        extends
        UseCaseRequestWithId<RecipeIdentityUseCaseRequestModel> {

    private UseCaseRequest() {}

    @Nonnull
    @Override
    public String toString() {
        return "RecipeIdentityUseCaseRequest{" +
                "dataId='" + dataId + '\'' +
                ", domainId='" + domainId + '\'' +
                ", model=" + model +
                '}';
    }


}
