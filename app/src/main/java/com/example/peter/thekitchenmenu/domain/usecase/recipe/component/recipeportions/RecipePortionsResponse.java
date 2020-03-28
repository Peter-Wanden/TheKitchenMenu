package com.example.peter.thekitchenmenu.domain.usecase.recipe.component.recipeportions;

import com.example.peter.thekitchenmenu.domain.usecase.UseCaseResponse;
import com.example.peter.thekitchenmenu.domain.usecase.UseCaseMetadata;
import com.example.peter.thekitchenmenu.domain.usecase.UseCaseDomainModel;

import java.util.Objects;

import javax.annotation.Nonnull;

public final class RecipePortionsResponse extends UseCaseResponse<RecipePortionsResponse.Model> {

    @Override
    public String toString() {
        return "RecipePortionsResponse{" +
                "id=" + id +
                ", metadata=" + metadata +
                ", model=" + model +
                '}';
    }

    public static class Builder extends UseCaseResponseBuilder<
                    Builder,
                    RecipePortionsResponse,
                    Model> {

        public Builder() {
            response = new RecipePortionsResponse();
        }

        public Builder getDefault() {
            return new Builder().
                    setId("").
                    setMetadata(new UseCaseMetadata.Builder().
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

    public static final class Model extends UseCaseDomainModel {

        private int servings;
        private int sittings;
        private int portions;

        private Model() {}

        public int getServings() {
            return servings;
        }

        public int getSittings() {
            return sittings;
        }

        public int getPortions() {
            return portions;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Model model = (Model) o;
            return servings == model.servings &&
                    sittings == model.sittings &&
                    portions == model.portions;
        }

        @Override
        public int hashCode() {
            return Objects.hash(servings, sittings, portions);
        }

        @Nonnull
        @Override
        public String toString() {
            return "Model{" +
                    "servings=" + servings +
                    ", sittings=" + sittings +
                    ", portions=" + portions +
                    '}';
        }

        public static class Builder extends DomainModelBuilder<
                                Builder,
                                Model> {

            public Builder() {
                model = new RecipePortionsResponse.Model();
            }

            public Builder getDefault() {
                return new Builder().
                        setServings(RecipePortions.MIN_SERVINGS).
                        setSittings(RecipePortions.MIN_SITTINGS).
                        setPortions(RecipePortions.MIN_SERVINGS * RecipePortions.MIN_SITTINGS);
            }

            public Builder setServings(int servings) {
                model.servings = servings;
                return self();
            }

            public Builder setSittings(int sittings) {
                model.sittings = sittings;
                return self();
            }

            public Builder setPortions(int portions) {
                model.portions = portions;
                return self();
            }

            @Override
            protected Builder self() {
                return this;
            }
        }
    }
}