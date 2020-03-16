package com.example.peter.thekitchenmenu.domain.usecase.recipe.recipemetadata;

import com.example.peter.thekitchenmenu.domain.usecase.recipe.RecipeResponse;
import com.example.peter.thekitchenmenu.domain.usecase.recipe.RecipeResponseMetadata;
import com.example.peter.thekitchenmenu.domain.usecase.recipe.RecipeResponseModel;

import java.util.Objects;

import javax.annotation.Nonnull;

public final class RecipeMetadataResponse extends RecipeResponse<RecipeMetadataResponse.Model> {

    @Override
    public String toString() {
        return "RecipeMetadataResponse{" +
                "id='" + id + '\'' +
                ", metadata=" + metadata +
                ", model=" + model +
                '}';
    }

    public static class Builder extends RecipeResponseBuilder<
            Builder,
            RecipeMetadataResponse,
            Model> {

        public Builder() {
            response = new RecipeMetadataResponse();
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

        public Builder setId(String id) {
            response.id = id;
            return self();
        }

        public Builder setMetadata(RecipeResponseMetadata metadata) {
            response.metadata = metadata;
            return self();
        }

        public Builder setModel(Model model) {
            response.model = model;
            return self();
        }

        @Override
        protected Builder self() {
            return super.self();
        }
    }

    public static final class Model extends RecipeResponseModel {
        private String parentId;

        public Model() {}

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

        @Nonnull
        @Override
        public String toString() {
            return "Model{" +
                    "parentId='" + parentId + '\'' +
                    '}';
        }

        public static class Builder extends RecipeResponseModelBuilder<
                Builder,
                RecipeMetadataResponse.Model> {

            public Builder() {
                model = new RecipeMetadataResponse.Model();
            }

            public Builder getDefault() {
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
