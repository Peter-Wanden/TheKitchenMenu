package com.example.peter.thekitchenmenu.domain.usecase.recipe.recipeidentity;

import com.example.peter.thekitchenmenu.domain.usecase.recipe.RecipeRequestAbstract;

import java.util.Objects;

import javax.annotation.Nonnull;

public final class RecipeIdentityRequest extends RecipeRequestAbstract {
    @Nonnull
    private final Model model;

    private RecipeIdentityRequest(@Nonnull String id,
                                  @Nonnull Model model) {
        this.id = id;
        this.model = model;
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
        return id.equals(that.id) && model.equals(that.model);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, model);
    }

    @Nonnull
    @Override
    public String toString() {
        return "RecipeIdentityRequest{" +
                "id='" + id + '\'' +
                ", model=" + model +
                '}';
    }

    public static class Builder {
        private String id;
        private Model model;

        public static Builder getDefault() {
            return new Builder().
                    setId("").
                    setModel(Model.Builder.
                            getDefault().
                            build());
        }

        public static Builder basedOnResponse(RecipeIdentityResponse response) {
            return new Builder().
                    setId(response.getId()).
                    setModel(
                            new Model.Builder().
                                    setTitle(response.getModel().getTitle()).
                                    setDescription(response.getModel().getDescription()).
                                    build()
                    );
        }

        public Builder setId(String id) {
            this.id = id;
            return this;
        }

        public Builder setModel(Model model) {
            this.model = model;
            return this;
        }

        public RecipeIdentityRequest build() {
            return new RecipeIdentityRequest(
                    id,
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

            public static Builder basedOnIdentityResponseModel(RecipeIdentityResponse.Model model) {
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