package com.example.peter.thekitchenmenu.domain.usecase.recipe.recipeportions;

import com.example.peter.thekitchenmenu.domain.usecase.recipe.RecipeRequest;
import com.example.peter.thekitchenmenu.domain.usecase.recipe.RecipeRequestModel;

import java.util.Objects;

import javax.annotation.Nonnull;

public final class RecipePortionsRequest extends RecipeRequest<RecipePortionsRequest.Model> {

    @Nonnull
    @Override
    public String toString() {
        return "RecipePortionsRequest{" +
                "id='" + id + '\'' +
                ", model=" + model +
                '}';
    }

    public static class Builder extends RecipeRequestBuilder<Builder, RecipePortionsRequest, Model> {

        public Builder() {
            request = new RecipePortionsRequest();
        }

        public Builder getDefault() {
            return new Builder().
                    setId("").
                    setModel(Model.Builder.
                            getDefault().
                            build());
        }

        @Override
        protected Builder self() {
            return this;
        }
    }

    public static final class Model extends RecipeRequestModel {
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

        public static class Builder extends RecipeRequestModelBuilder<Builder, Model> {

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
