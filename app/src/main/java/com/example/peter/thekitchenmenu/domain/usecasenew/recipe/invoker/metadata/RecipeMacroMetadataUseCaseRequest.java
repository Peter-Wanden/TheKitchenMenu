package com.example.peter.thekitchenmenu.domain.usecasenew.recipe.invoker.metadata;

import com.example.peter.thekitchenmenu.domain.usecasenew.common.message.UseCaseRequest;

public final class RecipeMacroMetadataUseCaseRequest
        extends
        UseCaseRequest<RecipeMacroMetadataUseCaseRequestModel> {

    private RecipeMacroMetadataUseCaseRequest(){}

    public static class Builder
            extends UseCaseRequest.Builder<
            Builder,
            RecipeMacroMetadataUseCaseRequest,
            RecipeMacroMetadataUseCaseRequestModel> {

        public Builder() {
            super(new RecipeMacroMetadataUseCaseRequest());
        }

        @Override
        public Builder self() {
            return this;
        }
    }
}
