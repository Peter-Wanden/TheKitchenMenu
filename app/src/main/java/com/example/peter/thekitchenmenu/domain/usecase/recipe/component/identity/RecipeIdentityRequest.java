package com.example.peter.thekitchenmenu.domain.usecase.recipe.component.identity;

import com.example.peter.thekitchenmenu.domain.usecase.common.usecasemessage.UseCaseMessageModelDataId;
import com.example.peter.thekitchenmenu.domain.usecase.common.UseCaseBase;

import javax.annotation.Nonnull;

public final class RecipeIdentityRequest
        extends
        UseCaseMessageModelDataId<RecipeIdentityRequestModel>
        implements
        UseCaseBase.Request {

    @Nonnull
    @Override
    public String toString() {
        return "RecipeIdentityRequest{" +
                "dataId='" + dataId + '\'' +
                ", domainId='" + domainId + '\'' +
                ", model=" + model +
                '}';
    }

    private RecipeIdentityRequest() {}

    public static class Builder
            extends
            UseCaseMessageModelDataIdBuilder<
                    Builder,
                    RecipeIdentityRequest,
                    RecipeIdentityRequestModel> {

        public Builder() {
            message = new RecipeIdentityRequest();
        }

        @Override
        public Builder getDefault() {
            message.dataId = "";
            message.domainId = "";
            message.model = new RecipeIdentityRequestModel.Builder().getDefault().build();
            return self();
        }

        public Builder basedOnResponse(RecipeIdentityResponse response) {
            message.dataId = response.getDataId();
            message.domainId = response.getDomainId();
            message.model = new RecipeIdentityRequestModel.Builder().
                    basedOnResponseModel(response.getDomainModel()).
                    build();
            return self();
        }

        @Override
        protected Builder self() {
            return this;
        }
    }

}