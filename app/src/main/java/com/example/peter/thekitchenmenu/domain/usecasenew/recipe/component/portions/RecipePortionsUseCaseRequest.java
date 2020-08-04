package com.example.peter.thekitchenmenu.domain.usecasenew.recipe.component.portions;

import com.example.peter.thekitchenmenu.domain.usecasenew.common.message.UseCaseRequest;
import com.example.peter.thekitchenmenu.domain.usecasenew.common.message.UseCaseResponse;

import javax.annotation.Nonnull;

public class RecipePortionsUseCaseRequest
        extends
        UseCaseRequest<RecipePortionsUseCaseRequestModel> {

    private RecipePortionsUseCaseRequest(){}

    public static class Builder
            extends
            UseCaseRequest.Builder<
                    Builder,
                    RecipePortionsUseCaseRequest,
                    RecipePortionsUseCaseRequestModel> {

        public Builder() {
            super(new RecipePortionsUseCaseRequest());
        }

        public Builder basedOnResponse(
                @Nonnull UseCaseResponse<RecipePortionsUseCaseResponseModel> response) {
            useCaseRequest.dataId = response.getDataId();
            useCaseRequest.domainId = response.getDomainId();

            useCaseRequest.requestModel = new RecipePortionsUseCaseRequestModel.Builder()
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
