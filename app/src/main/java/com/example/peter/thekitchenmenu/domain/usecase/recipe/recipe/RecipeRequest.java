package com.example.peter.thekitchenmenu.domain.usecase.recipe.recipe;

import com.example.peter.thekitchenmenu.domain.usecase.recipe.RecipeRequestBase;

import java.util.Objects;

import javax.annotation.Nonnull;

public final class RecipeRequest extends RecipeRequestBase<RecipeRequest.Model> {

    private RecipeRequest(@Nonnull String id, @Nonnull Model model) {
        super(id, model);
    }

    @Nonnull
    @Override
    public Model getModel() {
        return model;
    }

    @Override
    public String toString() {
        return "RecipeRequest{" +
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

        public RecipeRequest build() {
            return new RecipeRequest(
                    id,
                    model
            );
        }
    }

    public static final class Model implements RecipeRequestBase.RecipeRequestModel {
        @Nonnull
        private final String parentId;

        private Model(@Nonnull String parentId) {
            this.parentId = parentId;
        }

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

        public static class Builder {
            private String parentId;

            public static Builder getDefault() {
                return new Builder().setParentId("");
            }

            public Builder setParentId(String parentId) {
                this.parentId = parentId;
                return this;
            }

            public Model build() {
                return new Model(
                        parentId
                );
            }
        }
    }
}
