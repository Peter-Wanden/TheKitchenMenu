package com.example.peter.thekitchenmenu.domain.usecasenew.recipe.component.duration;

import com.example.peter.thekitchenmenu.domain.usecasenew.common.message.UseCaseRequest;
import com.example.peter.thekitchenmenu.domain.usecasenew.common.message.UseCaseResponse;

import javax.annotation.Nonnull;

public class RecipeDurationUseCaseRequest
        extends
        UseCaseRequest<RecipeDurationUseCaseRequestModel> {

    private RecipeDurationUseCaseRequest(){}

    public static class Builder
            extends
            UseCaseRequest.Builder<
                    Builder,
                    RecipeDurationUseCaseRequest,
                    RecipeDurationUseCaseRequestModel> {

        public Builder() {
            super(new RecipeDurationUseCaseRequest());
        }

        public Builder basedOnResponse(
                @Nonnull UseCaseResponse<RecipeDurationUseCaseResponseModel> response) {
            useCaseRequest.dataId = response.getDataId();
            useCaseRequest.domainId = response.getDomainId();

            useCaseRequest.requestModel = new RecipeDurationUseCaseRequestModel.Builder()
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
