package com.example.peter.thekitchenmenu.domain.usecase.recipe.component.portions;

import com.example.peter.thekitchenmenu.domain.usecase.MessageModelDataIdMetadata;
import com.example.peter.thekitchenmenu.domain.usecase.BaseDomainModel;
import com.example.peter.thekitchenmenu.domain.usecase.UseCaseBase;
import com.example.peter.thekitchenmenu.domain.usecase.UseCaseMetadataModel;

import java.util.Objects;

import javax.annotation.Nonnull;

public final class RecipePortionsResponse
        extends MessageModelDataIdMetadata<RecipePortionsResponse.Model>
        implements UseCaseBase.Response {

    @Override
    public String toString() {
        return "RecipePortionsResponse{" +
                "dataId='" + dataId + '\'' +
                ", domainId='" + domainId + '\'' +
                ", metadata=" + metadata +
                ", model=" + model +
                "'}'";
    }

    private RecipePortionsResponse() {}

    public static class Builder
            extends MessageModelDataIdMetadataBuilder<Builder, RecipePortionsResponse, Model> {

        public Builder() {
            message = new RecipePortionsResponse();
        }

        public Builder getDefault() {
            message.dataId = "";
            message.domainId = "";
            message.metadata = new UseCaseMetadataModel.Builder().getDefault().build();
            message.model = new Model.Builder().getDefault().build();
            return self();
        }

        @Override
        protected Builder self() {
            return this;
        }
    }

    public static final class Model extends BaseDomainModel {

        private int servings;
        private int sittings;
        private int portions;

        private Model() {
        }

        public int getServings() {
            return servings;
        }

        public int getSittings() {
            return sittings;
        }

        public int getPortions() {
            return portions;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Model model = (Model) o;
            return servings == model.servings &&
                    sittings == model.sittings &&
                    portions == model.portions;
        }

        @Override
        public int hashCode() {
            return Objects.hash(servings, sittings, portions);
        }

        @Nonnull
        @Override
        public String toString() {
            return "Model{" +
                    "servings=" + servings +
                    ", sittings=" + sittings +
                    ", portions=" + portions +
                    '}';
        }

        public static class Builder extends DomainModelBuilder<Builder, Model> {

            public Builder() {
                domainModel = new RecipePortionsResponse.Model();
            }

            public Builder getDefault() {
                domainModel.servings = RecipePortions.MIN_SERVINGS;
                domainModel.sittings = RecipePortions.MIN_SITTINGS;
                domainModel.portions = RecipePortions.MIN_SERVINGS * RecipePortions.MIN_SITTINGS;
                return self();
            }

            public Builder setServings(int servings) {
                domainModel.servings = servings;
                return self();
            }

            public Builder setSittings(int sittings) {
                domainModel.sittings = sittings;
                return self();
            }

            public Builder setPortions(int portions) {
                domainModel.portions = portions;
                return self();
            }

            @Override
            protected Builder self() {
                return this;
            }
        }
    }
}