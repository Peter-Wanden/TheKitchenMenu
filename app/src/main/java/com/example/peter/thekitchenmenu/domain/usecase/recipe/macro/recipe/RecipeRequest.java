package com.example.peter.thekitchenmenu.domain.usecase.recipe.macro.recipe;

import com.example.peter.thekitchenmenu.domain.usecase.UseCase;
import com.example.peter.thekitchenmenu.domain.usecase.BaseDomainMessage;

import javax.annotation.Nonnull;

public final class RecipeRequest
        extends BaseDomainMessage
        implements UseCase.Request {

    @Nonnull
    @Override
    public String toString() {
        return "RecipeRequest{" +
                "dataId='" + dataId + '\'' +
                ", domainId='" + domainId + '\'' +
                '}';
    }

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
