package com.example.peter.thekitchenmenu.domain.usecase.recipe.recipe;

import com.example.peter.thekitchenmenu.domain.usecase.recipe.RecipeRequestAbstract;

import javax.annotation.Nonnull;

import static com.example.peter.thekitchenmenu.domain.usecase.recipe.recipe.Recipe.DO_NOT_CLONE;

public final class RecipeRequest extends RecipeRequestAbstract {

    private RecipeRequest(@Nonnull String id, @Nonnull String cloneToId) {
        this.id = id;
        this.cloneToId = cloneToId;
    }

    @Nonnull
    @Override
    public String toString() {
        return "RecipeRequest{" +
                "id='" + id + '\'' +
                ", cloneToId='" + cloneToId + '\'' +
                '}';
    }

    public static class Builder {
        private String id;
        private String cloneToId;

        public static Builder getDefault() {
            return new Builder().
                    setId("").
                    setCloneToId(DO_NOT_CLONE);
        }

        public Builder setId(String id) {
            this.id = id;
            return this;
        }

        public Builder setCloneToId(String cloneToId) {
            this.cloneToId = cloneToId;
            return this;
        }

        public RecipeRequest build() {
            return new RecipeRequest(
                    id,
                    cloneToId
            );
        }
    }
}
