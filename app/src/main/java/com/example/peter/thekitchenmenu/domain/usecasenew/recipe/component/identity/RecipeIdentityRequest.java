package com.example.peter.thekitchenmenu.domain.usecasenew.recipe.component.identity;

import com.example.peter.thekitchenmenu.domain.usecasenew.common.message.UseCaseRequest;
import com.example.peter.thekitchenmenu.domain.usecasenew.common.message.UseCaseResponse;

public final class RecipeIdentityRequest
        extends
        UseCaseRequest<RecipeIdentityRequestModel> {

    public static class Builder
            extends
            UseCaseRequest.Builder<Builder, RecipeIdentityRequest, RecipeIdentityRequestModel> {

        public Builder() {
            super(new RecipeIdentityRequest());
        }

        public Builder basedOnResponse(UseCaseResponse<RecipeIdentityResponseModel> response) {

            useCaseRequest.dataId = response.getDataId();
            useCaseRequest.domainId = response.getDomainId();

            useCaseRequest.requestModel = new RecipeIdentityRequestModel.Builder()
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
