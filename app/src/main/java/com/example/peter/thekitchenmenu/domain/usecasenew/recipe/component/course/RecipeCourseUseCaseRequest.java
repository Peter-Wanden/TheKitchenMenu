package com.example.peter.thekitchenmenu.domain.usecasenew.recipe.component.course;

import com.example.peter.thekitchenmenu.domain.usecasenew.common.message.UseCaseRequest;
import com.example.peter.thekitchenmenu.domain.usecasenew.common.message.UseCaseResponse;

import javax.annotation.Nonnull;

public class RecipeCourseUseCaseRequest
        extends
        UseCaseRequest<RecipeCourseUseCaseRequestModel> {

    public static class Builder
            extends
            UseCaseRequest.Builder<
            Builder,
            RecipeCourseUseCaseRequest,
            RecipeCourseUseCaseRequestModel> {

        public Builder() {
            super(new RecipeCourseUseCaseRequest());
        }

        public Builder basedOnResponse(@Nonnull UseCaseResponse<RecipeCourseUseCaseResponseModel> response) {
            useCaseRequest.dataId = response.getDataId();
            useCaseRequest.domainId = response.getDomainId();

            useCaseRequest.requestModel = new RecipeCourseUseCaseRequestModel.Builder()
                    .setCourseList(response.getResponseModel().getCourseList())
                    .build();

            return self();
        }

        @Override
        public Builder self() {
            return this;
        }
    }
}
