package com.example.peter.thekitchenmenu.domain.usecase.recipe.recipe;

import com.example.peter.thekitchenmenu.domain.usecase.recipe.RecipeRequestAbstract;

import java.util.Objects;

import javax.annotation.Nonnull;

public final class RecipeRequest extends RecipeRequestAbstract {

    private String parentId;

    private RecipeRequest(@Nonnull String id, @Nonnull String cloneToId, @Nonnull String parentId) {
        this.id = id;
        this.cloneToId = cloneToId;
        this.parentId = parentId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RecipeRequest that = (RecipeRequest) o;
        return parentId.equals(that.parentId) &&
                cloneToId.equals(that.cloneToId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(parentId, cloneToId);
    }

    @Nonnull
    @Override
    public String toString() {
        return "RecipeRequest{" +
                "id='" + id + '\'' +
                ", cloneToId=" + cloneToId + '\'' +
                ", parentId='" + parentId + '\'' +
                '}';
    }

    public static class Builder {
        private String id;
        private String cloneToId;
        private String parentId;

        public static Builder getDefault() {
            return new Builder().
                    setId("").
                    setCloneToId("").
                    setParentId("");
        }

        public Builder setId(String id) {
            this.id = id;
            return this;
        }

        public Builder setCloneToId(String cloneToId) {
            this.cloneToId = cloneToId;
            return this;
        }

        public Builder setParentId(String parentId) {
            this.parentId = parentId;
            return this;
        }

        public RecipeRequest build() {
            return new RecipeRequest(
                    id,
                    cloneToId,
                    parentId
            );
        }
    }
}
