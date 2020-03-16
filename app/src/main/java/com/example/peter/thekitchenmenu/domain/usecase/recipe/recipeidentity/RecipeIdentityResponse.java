package com.example.peter.thekitchenmenu.domain.usecase.recipe.recipeidentity;

import com.example.peter.thekitchenmenu.domain.usecase.recipe.RecipeResponse;
import com.example.peter.thekitchenmenu.domain.usecase.recipe.RecipeResponseMetadata;
import com.example.peter.thekitchenmenu.domain.usecase.recipe.RecipeResponseModel;

import java.util.Objects;

import javax.annotation.Nonnull;

public final class RecipeIdentityResponse extends RecipeResponse<RecipeIdentityResponse.Model> {

    @Nonnull
    @Override
    public String toString() {
        return "RecipeIdentityResponse{" +
                "id=" + id +
                ", metadata=" + metadata +
                ", model=" + model +
                '}';
    }

    public static class Builder extends RecipeResponseBuilder<
            Builder,
            RecipeIdentityResponse,
            Model> {

        public Builder() {
            response = new RecipeIdentityResponse();
        }

        public Builder getDefault() {
            return new Builder().
                    setId("").
                    setMetadata(new RecipeResponseMetadata.Builder().
                            getDefault().
                            build()).
                    setModel(new Model.Builder().
                            getDefault().
                            build());
        }

        @Override
        protected Builder self() {
            return this;
        }
    }

    public static final class Model extends RecipeResponseModel {
        private String title;
        private String description;

        private Model() {}

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
                    ", description='" + description +
                    '}';
        }

        public static class Builder extends RecipeResponseModelBuilder<
                Builder,
                RecipeIdentityResponse.Model> {

            public Builder() {
                model = new RecipeIdentityResponse.Model();
            }

            public Builder getDefault() {
                return new Builder().
                        setTitle("").
                        setDescription("");
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