package com.example.peter.thekitchenmenu.domain.usecase.recipe.component.identity;

import com.example.peter.thekitchenmenu.domain.usecase.common.usecasemessage.UseCaseMessageModelDataIdMetadata;
import com.example.peter.thekitchenmenu.domain.usecase.common.UseCaseBase;
import com.example.peter.thekitchenmenu.domain.model.UseCaseMetadataModel;
import com.example.peter.thekitchenmenu.domain.usecasenew.recipe.component.identity.RecipeIdentityUseCaseResponseModel;

import javax.annotation.Nonnull;

public final class RecipeIdentityResponse
        extends
        UseCaseMessageModelDataIdMetadata<RecipeIdentityUseCaseResponseModel>
        implements
        UseCaseBase.Response {

    @Nonnull
    @Override
    public String toString() {
        return "RecipeIdentityResponse{" +
                "dataId='" + dataId + '\'' +
                ", domainId='" + domainId + '\'' +
                ", metadata=" + metadata +
                ", model=" + model +
                "'}'";
    }

    private RecipeIdentityResponse() {}

    public static class Builder
            extends
            MessageModelDataIdMetadataBuilder<
                    Builder,
                    RecipeIdentityResponse,
                    RecipeIdentityUseCaseResponseModel> {

        public Builder() {
            message = new RecipeIdentityResponse();
        }

        @Override
        public Builder getDefault() {
            message.dataId = "";
            message.domainId = "";
            message.model = new RecipeIdentityUseCaseResponseModel.Builder().getDefault().build();
            message.metadata = new UseCaseMetadataModel.Builder().getDefault().build();
            return self();
        }

        @Override
        protected Builder self() {
            return this;
        }
    }
}