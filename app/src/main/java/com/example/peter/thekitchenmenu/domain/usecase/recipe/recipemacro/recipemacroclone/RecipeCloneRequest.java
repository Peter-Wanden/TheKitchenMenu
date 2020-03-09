package com.example.peter.thekitchenmenu.domain.usecase.recipe.recipemacro.recipemacroclone;

import com.example.peter.thekitchenmenu.domain.usecase.recipe.RecipeRequestAbstract;

public class RecipeCloneRequest extends RecipeRequestAbstract {

    private RecipeCloneRequest(String id, String cloneToId) {
        this.id = id;
        this.cloneToId = cloneToId;
    }

    public String getId() {
        return id;
    }

    public String getCloneToId() {
        return cloneToId;
    }

    public static class Builder {
        String id;
        String cloneToId;

        public Builder setId(String id) {
            this.id = id;
            return this;
        }

        public Builder setCloneToId(String cloneToId) {
            this.cloneToId = cloneToId;
            return this;
        }

        public RecipeCloneRequest build() {
            return new RecipeCloneRequest(
                    id,
                    cloneToId
            );
        }
    }
}
