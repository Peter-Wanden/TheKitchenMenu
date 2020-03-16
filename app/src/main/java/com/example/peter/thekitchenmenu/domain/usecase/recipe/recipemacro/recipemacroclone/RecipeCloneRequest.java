package com.example.peter.thekitchenmenu.domain.usecase.recipe.recipemacro.recipemacroclone;

import com.example.peter.thekitchenmenu.domain.usecase.recipe.RecipeRequest;
import com.example.peter.thekitchenmenu.domain.usecase.recipe.RecipeRequestModel;

import java.util.Objects;

public final class RecipeCloneRequest extends RecipeRequest<RecipeCloneRequest.Model> {

    public static class Builder extends RecipeRequestBuilder<Builder, RecipeCloneRequest, Model> {

        public Builder() {
            request = new RecipeCloneRequest();
        }

        public Builder getDefault() {
            return new Builder().
                    setId("").
                    setModel(Model.Builder.
                            getDefault().
                            build());
        }

        @Override
        protected Builder self() {
            return this;
        }
    }

    public static final class Model extends RecipeRequestModel {
        private String cloneToId;

        public String getCloneToId() {
            return cloneToId;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Model model = (Model) o;
            return Objects.equals(cloneToId, model.cloneToId);
        }

        @Override
        public int hashCode() {
            return Objects.hash(cloneToId);
        }

        @Override
        public String toString() {
            return "Model{" +
                    "cloneToId='" + cloneToId + '\'' +
                    '}';
        }

        public static class Builder extends RecipeRequestModelBuilder<Builder, Model> {

            public Builder() {
                model = new Model();
            }

            public static Builder getDefault() {
                return new Builder().setCloneToId("");
            }

            public Builder setCloneToId(String cloneToId) {
                model.cloneToId = cloneToId;
                return self();
            }

            @Override
            protected Builder self() {
                return this;
            }
        }
    }
}
