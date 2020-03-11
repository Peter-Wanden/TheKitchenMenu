package com.example.peter.thekitchenmenu.domain.usecase.recipe.recipeportions;

import com.example.peter.thekitchenmenu.domain.usecase.recipe.RecipeRequestBase;

import java.util.Objects;

import javax.annotation.Nonnull;

public final class RecipePortionsRequest extends RecipeRequestBase<RecipePortionsRequest.Model> {

    private RecipePortionsRequest(@Nonnull String id,
                                  @Nonnull Model model) {
        super(id, model);
    }

    @Nonnull
    @Override
    public Model getModel() {
        return model;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RecipePortionsRequest that = (RecipePortionsRequest) o;
        return id.equals(that.id)  &&
                model.equals(that.model);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, model);
    }

    @Override
    public String toString() {
        return "RecipePortionsRequest{" +
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

        public Builder setId(String id) {
            this.id = id;
            return this;
        }

        public Builder setModel(Model model) {
            this.model = model;
            return this;
        }

        public RecipePortionsRequest build() {
            return new RecipePortionsRequest(
                    id,
                    model
            );
        }
    }

    public static final class Model implements RecipeRequestBase.RecipeRequestModel {
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
                        setServings(RecipePortions.MIN_SERVINGS).
                        setSittings(RecipePortions.MIN_SITTINGS);
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
