package com.example.peter.thekitchenmenu.domain.usecase.recipe.recipeidentity;


import com.example.peter.thekitchenmenu.domain.usecase.UseCaseCommand;

import java.util.Objects;

import javax.annotation.Nonnull;

import static com.example.peter.thekitchenmenu.domain.usecase.recipe.recipestate.RecipeStateCalculator.*;

public final class RecipeIdentityRequest implements UseCaseCommand.Request {
    @Nonnull
    private static final ComponentName componentName = ComponentName.IDENTITY;
    @Nonnull
    private final String recipeId;
    @Nonnull
    private final String cloneToRecipeId;
    @Nonnull
    private final Model model;

    public RecipeIdentityRequest(@Nonnull String recipeId,
                                 @Nonnull String cloneToRecipeId,
                                 @Nonnull Model model) {
        this.recipeId = recipeId;
        this.cloneToRecipeId = cloneToRecipeId;
        this.model = model;
    }

    @Nonnull
    public ComponentName getComponentName() { // receiver
        return componentName;
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
    public Model getModel() {
        return model;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RecipeIdentityRequest that = (RecipeIdentityRequest) o;
        return recipeId.equals(that.recipeId) &&
                cloneToRecipeId.equals(that.cloneToRecipeId) &&
                model.equals(that.model);
    }

    @Override
    public int hashCode() {
        return Objects.hash(recipeId, cloneToRecipeId, model);
    }

    @Override
    public String toString() {
        return "RecipeIdentityRequest{" +
                "recipeId='" + recipeId + '\'' +
                ", cloneToRecipeId='" + cloneToRecipeId + '\'' +
                ", model=" + model +
                '}';
    }

    public static class Builder {
        private String recipeId;
        private String cloneToRecipeId;
        private Model model;

        public static Builder getDefault() {
            return new Builder().
                    setRecipeId("").
                    setCloneToRecipeId("").
                    setModel(Model.Builder.
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

        public Builder setModel(Model model) {
            this.model = model;
            return this;
        }

        public RecipeIdentityRequest build() {
            return new RecipeIdentityRequest(
                    recipeId,
                    cloneToRecipeId,
                    model
            );
        }
    }

    public static final class Model {
        @Nonnull
        private final String title;
        @Nonnull
        private final String description;

        private Model(@Nonnull String title, @Nonnull String description) {
            this.title = title;
            this.description = description;
        }

        @Nonnull
        public String getTitle() {
            return title;
        }

        @Nonnull
        public String getDescription() {
            return description;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Model model = (Model) o;
            return title.equals(model.title) &&
                    description.equals(model.description);
        }

        @Override
        public int hashCode() {
            return Objects.hash(title, description);
        }

        @Override
        public String toString() {
            return "Model{" +
                    "title='" + title + '\'' +
                    ", description='" + description + '\'' +
                    '}';
        }

        public static final class Builder {
            private String title;
            private String description;

            public static Builder basedOn(RecipeIdentityResponse.Model model) {
                return new Builder().
                        setTitle(model.getTitle()).
                        setDescription(model.getDescription());
            }

            public static Builder getDefault() {
                return new Builder().
                        setTitle("").
                        setDescription("");
            }

            public Builder setTitle(String title) {
                this.title = title;
                return this;
            }

            public Builder setDescription(String description) {
                this.description = description;
                return this;
            }

            public Model build() {
                return new Model(
                        title,
                        description
                );
            }
        }
    }
}