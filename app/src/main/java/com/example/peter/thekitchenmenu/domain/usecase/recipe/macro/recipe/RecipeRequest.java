package com.example.peter.thekitchenmenu.domain.usecase.recipe.macro.recipe;

import com.example.peter.thekitchenmenu.domain.usecase.UseCase;
import com.example.peter.thekitchenmenu.domain.usecase.UseCaseDomainMessageBase;

public final class RecipeRequest
        extends UseCaseDomainMessageBase
        implements UseCase.Request {

    private RecipeRequest() {}

    public static class Builder extends UseCaseMessageBuilder<Builder, RecipeRequest> {

        public Builder() {
            message = new RecipeRequest();
        }

        @Override
        public Builder getDefault() {
            message.dataId = "";
            message.domainId = "";
            return self();
        }

        @Override
        protected Builder self() {
            return this;
        }
    }
}
