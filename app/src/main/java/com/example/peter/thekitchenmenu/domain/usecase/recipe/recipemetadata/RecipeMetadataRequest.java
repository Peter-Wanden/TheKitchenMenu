package com.example.peter.thekitchenmenu.domain.usecase.recipe.recipemetadata;

import com.example.peter.thekitchenmenu.domain.usecase.recipe.RecipeRequest;
import com.example.peter.thekitchenmenu.domain.usecase.recipe.RecipeRequestModel;

import java.util.Objects;

import javax.annotation.Nonnull;

public final class RecipeMetadataRequest extends RecipeRequest<RecipeMetadataRequest.Model> {

    @Override
    public String toString() {
        return "RecipeRequest{" +
                "id='" + id + '\'' +
                ", model=" + model +
                '}';
    }

    public static class Builder extends RecipeRequestBuilder<Builder, RecipeMetadataRequest, Model> {

        public Builder() {
            request = new RecipeMetadataRequest();
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
        @Nonnull
        private String parentId;

        @Nonnull
        public String getParentId() {
            return parentId;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Model model = (Model) o;
            return parentId.equals(model.parentId);
        }

        @Override
        public int hashCode() {
            return Objects.hash(parentId);
        }


        @Override
        public String toString() {
            return "Model{" +
                    "parentId='" + parentId + '\'' +
                    '}';
        }

        public static class Builder extends RecipeRequestModelBuilder<Builder, Model> {

            public Builder() {
                model = new Model();
            }

            public static Builder getDefault() {
                return new Builder().setParentId("");
            }

            public Builder setParentId(String parentId) {
                model.parentId = parentId;
                return self();
            }

            @Override
            protected Builder self() {
                return this;
            }
        }
    }
}
