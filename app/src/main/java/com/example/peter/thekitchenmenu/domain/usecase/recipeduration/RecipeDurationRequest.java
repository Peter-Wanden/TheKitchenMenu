package com.example.peter.thekitchenmenu.domain.usecase.recipeduration;

import com.example.peter.thekitchenmenu.domain.usecase.UseCaseCommand;

import java.util.Objects;

import javax.annotation.Nonnull;

import static com.example.peter.thekitchenmenu.domain.usecase.recipe.Recipe.DO_NOT_CLONE;


public final class RecipeDurationRequest implements UseCaseCommand.Request {
    @Nonnull
    private final String recipeId;
    @Nonnull
    private final String cloneToRecipeId;
    @Nonnull
    private final RecipeDurationRequest.Model model;

    private RecipeDurationRequest(@Nonnull String recipeId,
                                 @Nonnull String cloneToRecipeId,
                                 @Nonnull RecipeDurationRequest.Model model) {
        this.recipeId = recipeId;
        this.cloneToRecipeId = cloneToRecipeId;
        this.model = model;
    }

    @Nonnull
    public String getRecipeId() {
        return recipeId;
    }

    @Nonnull
    public String getCloneToRecipeId() {
        return cloneToRecipeId;
    }

    @Nonnull
    public RecipeDurationRequest.Model getModel() {
        return model;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RecipeDurationRequest request = (RecipeDurationRequest) o;
        return recipeId.equals(request.recipeId) &&
                cloneToRecipeId.equals(request.cloneToRecipeId) &&
                model.equals(request.model);
    }

    @Nonnull
    @Override
    public String toString() {
        return "RecipeDurationRequest{" +
                "recipeId='" + recipeId + '\'' +
                ", cloneToRecipeId='" + cloneToRecipeId + '\'' +
                ", model=" + model +
                '}';
    }

    @Override
    public int hashCode() {
        return Objects.hash(recipeId, cloneToRecipeId, model);
    }

    public static class Builder {
        private String recipeId;
        private String cloneToRecipeId;
        private RecipeDurationRequest.Model model;

        public static Builder getDefault() {
            return new Builder().
                    setRecipeId("").
                    setCloneToRecipeId(DO_NOT_CLONE).
                    setModel(RecipeDurationRequest.Model.Builder.
                            getDefault().
                            build());
        }

        public Builder setRecipeId(String recipeId) {
            this.recipeId = recipeId;
            return this;
        }

        public Builder setCloneToRecipeId(String cloneToRecipeId) {
            this.cloneToRecipeId = cloneToRecipeId;
            return this;
        }

        public Builder setModel(RecipeDurationRequest.Model model) {
            this.model = model;
            return this;
        }

        public RecipeDurationRequest build() {
            return new RecipeDurationRequest(
                    recipeId,
                    cloneToRecipeId,
                    model
            );
        }
    }

    public static final class Model {
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

            public RecipeDurationRequest.Model build() {
                return new RecipeDurationRequest.Model(
                        prepHours,
                        prepMinutes,
                        cookHours,
                        cookMinutes
                );
            }
        }
    }
}