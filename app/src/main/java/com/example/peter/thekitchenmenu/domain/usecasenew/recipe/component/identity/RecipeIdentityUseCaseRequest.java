package com.example.peter.thekitchenmenu.domain.usecasenew.recipe.component.identity;

import com.example.peter.thekitchenmenu.domain.usecasenew.common.message.UseCaseRequest;
import com.example.peter.thekitchenmenu.domain.usecasenew.common.message.UseCaseResponse;

import javax.annotation.Nonnull;

public final class RecipeIdentityUseCaseRequest
        extends
        UseCaseRequest<RecipeIdentityUseCaseRequestModel> {

    private RecipeIdentityUseCaseRequest(){}

    public static class Builder
            extends
            UseCaseRequest.Builder<
                    Builder,
                    RecipeIdentityUseCaseRequest,
                    RecipeIdentityUseCaseRequestModel> {

        public Builder() {
            super(new RecipeIdentityUseCaseRequest());
        }

        public Builder basedOnResponse(
                @Nonnull UseCaseResponse<RecipeIdentityUseCaseResponseModel> response) {
            useCaseRequest.dataId = response.getDataId();
            useCaseRequest.domainId = response.getDomainId();

            useCaseRequest.requestModel = new RecipeIdentityUseCaseRequestModel.Builder()
                    .basedOnResponseModel(response.getResponseModel())
                    .build();

            return self();
        }

        @Override
        public Builder self() {
            return this;
        }
    }
}
