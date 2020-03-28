package com.example.peter.thekitchenmenu.domain.usecase.recipe.component.recipeportions;

import com.example.peter.thekitchenmenu.domain.usecase.UseCaseRequestWithDomainModel;
import com.example.peter.thekitchenmenu.domain.usecase.UseCaseDomainModel;

import java.util.Objects;

import javax.annotation.Nonnull;

public final class RecipePortionsRequest extends UseCaseRequestWithDomainModel<RecipePortionsRequest.Model> {

    @Nonnull
    @Override
    public String toString() {
        return "RecipePortionsRequest{" +
                "id='" + dataId + '\'' +
                ", model=" + model +
                '}';
    }

    public static class Builder extends UseCaseRequestBuilder<Builder, RecipePortionsRequest, Model> {

        public Builder() {
            request = new RecipePortionsRequest();
        }

        public Builder getDefault() {
            return new Builder().
                    setDataId("").
                    setModel(Model.Builder.
                            getDefault().
                            build());
        }

        public Builder basedOnResponse(RecipePortionsResponse response) {
            request.dataId = response.getId();
            request.model.servings = response.getModel().getServings();
            request.model.sittings = response.getModel().getSittings();
            return self();
        }

        @Override
        protected Builder self() {
            return this;
        }
    }

    public static final class Model extends UseCaseDomainModel {
        private int servings;
        private int sittings;

        public int getServings() {
            return servings;
        }

        public int getSittings() {
            return sittings;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Model model = (Model) o;
            return servings == model.servings &&
                    sittings == model.sittings;
        }

        @Override
        public int hashCode() {
            return Objects.hash(servings, sittings);
        }

        @Override
        public String toString() {
            return "Model{" +
                    "servings=" + servings +
                    ", sittings=" + sittings +
                    '}';
        }

        public static class Builder extends DomainModelBuilder<Builder, Model> {

            public Builder() {
                model = new Model();
            }

            public static Builder getDefault() {
                return new Builder().
                        setServings(RecipePortions.MIN_SERVINGS).
                        setSittings(RecipePortions.MIN_SITTINGS);
            }

            public static Builder basedOnPortionsResponseModel(RecipePortionsResponse.Model model) {
                return new Builder().
                        setServings(model.getServings()).
                        setSittings(model.getSittings());
            }

            public Builder setServings(int servings) {
                model.servings = servings;
                return self();
            }

            public Builder setSittings(int sittings) {
                model.sittings = sittings;
                return self();
            }

            @Override
            protected Builder self() {
                return this;
            }
        }
    }
}
