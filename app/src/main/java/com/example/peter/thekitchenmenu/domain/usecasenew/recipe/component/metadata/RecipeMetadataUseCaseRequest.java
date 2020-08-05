package com.example.peter.thekitchenmenu.domain.usecasenew.recipe.component.metadata;

import com.example.peter.thekitchenmenu.domain.usecasenew.common.message.UseCaseRequest;

public final class RecipeMetadataUseCaseRequest
        extends
        UseCaseRequest<RecipeMetadataUseCaseRequestModel> {

    private RecipeMetadataUseCaseRequest(){}

    public static class Builder
            extends UseCaseRequest.Builder<
            Builder,
            RecipeMetadataUseCaseRequest,
            RecipeMetadataUseCaseRequestModel> {

        public Builder() {
            super(new RecipeMetadataUseCaseRequest());
        }

        @Override
        public Builder self() {
            return this;
        }
    }
}
