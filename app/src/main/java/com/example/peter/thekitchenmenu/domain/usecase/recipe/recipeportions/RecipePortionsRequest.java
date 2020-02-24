package com.example.peter.thekitchenmenu.domain.usecase.recipe.recipeportions;

import com.example.peter.thekitchenmenu.domain.usecase.recipe.RecipeRequestAbstract;

import java.util.Objects;

import javax.annotation.Nonnull;

public final class RecipePortionsRequest extends RecipeRequestAbstract {
    @Nonnull
    private final RecipePortionsRequest.Model model;

    private RecipePortionsRequest(@Nonnull String id,
                                  @Nonnull String cloneToId,
                                  @Nonnull RecipePortionsRequest.Model model) {
        this.id = id;
        this.cloneToId = cloneToId;
        this.model = model;
    }

    @Nonnull
    public RecipePortionsRequest.Model getModel() {
        return model;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RecipePortionsRequest that = (RecipePortionsRequest) o;
        return id.equals(that.id) && cloneToId.equals(that.cloneToId) &&
                model.equals(that.model);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, cloneToId, model);
    }

    @Override
    public String toString() {
        return "RecipePortionsRequest{" +
                "id='" + id + '\'' +
                ", cloneToId='" + cloneToId + '\'' +
                ", model=" + model +
                '}';
    }

    public static class Builder {
        private String id;
        private String cloneToId;
        private RecipePortionsRequest.Model model;

        public static Builder getDefault() {
            return new Builder().
                    setId("").
                    setCloneToId("").
                    setModel(RecipePortionsRequest.Model.Builder.
                            getDefault().
                            build());
        }

        public Builder setId(String id) {
            this.id = id;
            return this;
        }

        public Builder setCloneToId(String cloneToId) {
            this.cloneToId = cloneToId;
            return this;
        }

        public Builder setModel(RecipePortionsRequest.Model model) {
            this.model = model;
            return this;
        }

        public RecipePortionsRequest build() {
            return new RecipePortionsRequest(
                    id,
                    cloneToId,
                    model
            );
        }
    }

    public static final class Model {
        private final int servings;
        private final int sittings;

        private Model(int servings, int sittings) {
            this.servings = servings;
            this.sittings = sittings;
        }

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

        public static class Builder {
            int servings;
            int sittings;

            public static Builder getDefault() {
                return new Builder().
                        setServings(1).
                        setSittings(1);
            }

            public static Builder basedOnPortionsResponseModel(RecipePortionsResponse.Model model) {
                return new Builder().
                        setServings(model.getServings()).
                        setSittings(model.getSittings());
            }

            public Builder setServings(int servings) {
                this.servings = servings;
                return this;
            }

            public Builder setSittings(int sittings) {
                this.sittings = sittings;
                return this;
            }

            public Model build() {
                return new Model(
                        servings,
                        sittings
                );
            }
        }
    }
}
