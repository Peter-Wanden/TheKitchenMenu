package com.example.peter.thekitchenmenu.domain.usecase.recipe.recipeidentity;

import com.example.peter.thekitchenmenu.domain.usecase.recipe.RecipeRequest;
import com.example.peter.thekitchenmenu.domain.usecase.recipe.RecipeRequestModel;

import java.util.Objects;

import javax.annotation.Nonnull;

public final class RecipeIdentityRequest extends RecipeRequest<RecipeIdentityRequest.Model> {

    @Nonnull
    @Override
    public String toString() {
        return "RecipeIdentityRequest{" +
                "id='" + id + '\'' +
                ", model=" + model +
                '}';
    }

    public static class Builder extends RecipeRequestBuilder<Builder, RecipeIdentityRequest, Model> {

        public Builder() {
            request = new RecipeIdentityRequest();
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
        private String title;
        private String description;

        public String getTitle() {
            return title;
        }

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

        @Nonnull
        @Override
        public String toString() {
            return "Model{" +
                    "title='" + title + '\'' +
                    ", description='" + description + '\'' +
                    '}';
        }

        public static class Builder extends RecipeRequestModelBuilder<Builder, Model> {

            public Builder() {
                model = new Model();
            }

            public Builder getDefault() {
                return new Builder().
                        setTitle("").
                        setDescription("");
            }

            public static Builder basedOnIdentityResponseModel(RecipeIdentityResponse.Model model) {
                return new Builder().
                        setTitle(model.getTitle()).
                        setDescription(model.getDescription());
            }

            public Builder setTitle(String title) {
                model.title = title;
                return self();
            }

            public Builder setDescription(String description) {
                model.description = description;
                return self();
            }

            @Override
            protected Builder self() {
                return this;
            }
        }
    }
}