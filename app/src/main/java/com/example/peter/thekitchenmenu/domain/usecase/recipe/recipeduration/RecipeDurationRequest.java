package com.example.peter.thekitchenmenu.domain.usecase.recipe.recipeduration;


import com.example.peter.thekitchenmenu.domain.usecase.recipe.RecipeRequest;
import com.example.peter.thekitchenmenu.domain.usecase.recipe.RecipeRequestModel;

import java.util.Objects;

import javax.annotation.Nonnull;

public final class RecipeDurationRequest extends RecipeRequest<RecipeDurationRequest.Model> {

    public static class Builder extends RecipeRequestBuilder<Builder, RecipeDurationRequest, Model> {

        public Builder() {
            request = new RecipeDurationRequest();
        }

        public Builder getDefault() {
            return new Builder().
                    setId("").
                    setModel(new Model.Builder().
                            getDefault().
                            build());
        }

        @Override
        protected Builder self() {
            return this;
        }
    }

    public static final class Model extends RecipeRequestModel {
        private int prepHours;
        private int prepMinutes;
        private int cookHours;
        private int cookMinutes;

        public int getPrepHours() {
            return prepHours;
        }

        public int getPrepMinutes() {
            return prepMinutes;
        }

        public int getCookHours() {
            return cookHours;
        }

        public int getCookMinutes() {
            return cookMinutes;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Model model = (Model) o;
            return prepHours == model.prepHours &&
                    prepMinutes == model.prepMinutes &&
                    cookHours == model.cookHours &&
                    cookMinutes == model.cookMinutes;
        }

        @Override
        public int hashCode() {
            return Objects.hash(prepHours, prepMinutes, cookHours, cookMinutes);
        }

        @Nonnull
        @Override
        public String toString() {
            return "Model{" +
                    "prepHours=" + prepHours +
                    ", prepMinutes=" + prepMinutes +
                    ", cookHours=" + cookHours +
                    ", cookMinutes=" + cookMinutes +
                    '}';
        }

        public static class Builder extends RecipeRequestModelBuilder<Builder, Model> {

            public Builder() {
                model = new Model();
            }

            public Builder getDefault() {
                return new Builder().
                        setPrepHours(0).
                        setPrepMinutes(0).
                        setCookHours(0).
                        setCookMinutes(0);
            }

            public static Builder basedOnDurationResponseModel(RecipeDurationResponse.Model model) {
                return new Builder().
                        setPrepHours(model.getPrepHours()).
                        setPrepMinutes(model.getPrepMinutes()).
                        setCookHours(model.getCookHours()).
                        setCookMinutes(model.getCookMinutes());
            }

            public Builder setPrepHours(int prepHours) {
                model.prepHours = prepHours;
                return self();
            }

            public Builder setPrepMinutes(int prepMinutes) {
                model.prepMinutes = prepMinutes;
                return self();
            }

            public Builder setCookHours(int cookHours) {
                model.cookHours = cookHours;
                return self();
            }

            public Builder setCookMinutes(int cookMinutes) {
                model.cookMinutes = cookMinutes;
                return self();
            }

            @Override
            protected Builder self() {
                return this;
            }
        }
    }
}