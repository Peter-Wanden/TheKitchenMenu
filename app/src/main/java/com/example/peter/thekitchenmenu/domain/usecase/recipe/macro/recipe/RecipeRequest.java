package com.example.peter.thekitchenmenu.domain.usecase.recipe.macro.recipe;

import com.example.peter.thekitchenmenu.domain.model.BaseDomainModel;
import com.example.peter.thekitchenmenu.domain.usecase.common.usecasemessage.UseCaseMessageModelDataId;
import com.example.peter.thekitchenmenu.domain.usecase.common.UseCaseBase;

import javax.annotation.Nonnull;

public final class RecipeRequest
        extends UseCaseMessageModelDataId<RecipeRequest.Model>
        implements UseCaseBase.Request {

    @Nonnull
    @Override
    public String toString() {
        return "RecipeRequest{" +
                "dataId='" + dataId + '\'' +
                ", domainId='" + domainId + '\'' +
                '}';
    }

    private RecipeRequest() {}

    public static class Builder extends MessageModelDataIdBuilder<Builder, RecipeRequest, Model> {

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

    public static class Model extends BaseDomainModel {
    }
}
