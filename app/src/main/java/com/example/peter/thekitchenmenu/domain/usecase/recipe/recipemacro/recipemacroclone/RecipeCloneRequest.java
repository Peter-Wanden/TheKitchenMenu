package com.example.peter.thekitchenmenu.domain.usecase.recipe.recipemacro.recipemacroclone;

import com.example.peter.thekitchenmenu.domain.usecase.recipe.RecipeRequestBase;

import javax.annotation.Nonnull;

public class RecipeCloneRequest extends RecipeRequestBase<RecipeCloneRequest.Model> {

    private RecipeCloneRequest(@Nonnull String id,@Nonnull Model model) {
        super(id, model);
    }

    @Override
    public Model getModel() {
        return model;
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

        public RecipeCloneRequest build() {
            return new RecipeCloneRequest(
                    id,
                    model
            );
        }
    }

    public static final class Model implements RecipeRequestBase.RecipeRequestModel {
        private final String cloneToId;

        public Model(String cloneToId) {
            this.cloneToId = cloneToId;
        }

        public String getCloneToId() {
            return cloneToId;
        }

        public static class Builder {
            String cloneToId;

            public static Builder getDefault() {
                return new Builder().setCloneToId("");
            }

            public Builder setCloneToId(String cloneToId) {
                this.cloneToId = cloneToId;
                return this;
            }

            public Model build() {
                return new Model(
                        cloneToId
                );
            }
        }
    }
}
