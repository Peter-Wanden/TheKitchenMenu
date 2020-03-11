package com.example.peter.thekitchenmenu.domain.usecase.recipe.recipeduration;


import com.example.peter.thekitchenmenu.domain.usecase.recipe.RecipeRequestBase;

import java.util.Objects;

import javax.annotation.Nonnull;

public final class RecipeDurationRequest extends RecipeRequestBase<RecipeDurationRequest.Model> {

    private RecipeDurationRequest(@Nonnull String id, @Nonnull Model model) {
        super(id, model);
    }

    @Nonnull
    @Override
    public Model getModel() {
        return model;
    }

    @Nonnull
    @Override
    public String toString() {
        return "RecipeDurationRequest{" +
                "id='" + id + '\'' +
                ", model=" + model +
                '}';
    }

    public static class Builder extends RecipeRequestBase.Builder<Builder> {

        @Override
        public Builder getDefault() {
            return new Builder().
                    setId("").
                    setModel(Model.Builder.
                            getDefault().
                            build());
        }

        @Override
        public RecipeDurationRequest build() {
            return new RecipeDurationRequest(id, (RecipeDurationRequest.Model) model);
        }

        @Override
        protected Builder self() {
            return this;
        }
    }

    public static final class Model implements RecipeRequestBase.RecipeRequestModel {
        private final int prepHours;
        private final int prepMinutes;
        private final int cookHours;
        private final int cookMinutes;

        private Model(int prepHours, int prepMinutes, int cookHours, int cookMinutes) {
            this.prepHours = prepHours;
            this.prepMinutes = prepMinutes;
            this.cookHours = cookHours;
            this.cookMinutes = cookMinutes;
        }

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

        @Override
        public String toString() {
            return "Model{" +
                    "prepHours=" + prepHours +
                    ", prepMinutes=" + prepMinutes +
                    ", cookHours=" + cookHours +
                    ", cookMinutes=" + cookMinutes +
                    '}';
        }

        public static class Builder {
            private int prepHours;
            private int prepMinutes;
            private int cookHours;
            private int cookMinutes;

            public static Builder getDefault() {
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
                this.prepHours = prepHours;
                return this;
            }

            public Builder setPrepMinutes(int prepMinutes) {
                this.prepMinutes = prepMinutes;
                return this;
            }

            public Builder setCookHours(int cookHours) {
                this.cookHours = cookHours;
                return this;
            }

            public Builder setCookMinutes(int cookMinutes) {
                this.cookMinutes = cookMinutes;
                return this;
            }

            public Model build() {
                return new Model(
                        prepHours,
                        prepMinutes,
                        cookHours,
                        cookMinutes
                );
            }
        }
    }
}