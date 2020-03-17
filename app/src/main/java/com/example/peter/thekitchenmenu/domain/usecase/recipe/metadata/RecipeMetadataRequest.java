package com.example.peter.thekitchenmenu.domain.usecase.recipe.metadata;

import com.example.peter.thekitchenmenu.domain.usecase.recipe.component.RecipeComponentRequest;
import com.example.peter.thekitchenmenu.domain.usecase.recipe.RecipeDataModel;

import java.util.Objects;

import javax.annotation.Nonnull;

public final class RecipeMetadataRequest extends RecipeComponentRequest<RecipeMetadataRequest.Model> {

    @Override
    public String toString() {
        return "RecipeMetadataRequest{" +
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

    public static final class Model extends RecipeDataModel {
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

        public static class Builder extends RecipeDataModelBuilder<Builder, Model> {

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
