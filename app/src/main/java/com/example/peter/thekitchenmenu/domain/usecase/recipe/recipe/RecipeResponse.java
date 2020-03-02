package com.example.peter.thekitchenmenu.domain.usecase.recipe.recipe;

import com.example.peter.thekitchenmenu.app.Constants;
import com.example.peter.thekitchenmenu.domain.usecase.UseCase;
import com.example.peter.thekitchenmenu.domain.usecase.recipe.RecipeResponseAbstract;

import java.util.Objects;

import javax.annotation.Nonnull;

public final class RecipeResponse extends RecipeResponseAbstract {
    @Nonnull
    private final String parentId;
    @Nonnull
    private final String createdBy;
    private final long createDate;
    private final long lastUpdate;

    private RecipeResponse(@Nonnull String id,
                           @Nonnull String parentId,
                           @Nonnull String createdBy,
                           long createDate,
                           long lastUpdate) {
        this.id = id;
        this.parentId = parentId;
        this.createdBy = createdBy;
        this.createDate = createDate;
        this.lastUpdate = lastUpdate;
    }

    @Nonnull
    public String getParentId() {
        return parentId;
    }

    @Nonnull
    public String getCreatedBy() {
        return createdBy;
    }

    public long getCreateDate() {
        return createDate;
    }

    public long getLastUpdate() {
        return lastUpdate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RecipeResponse response = (RecipeResponse) o;
        return id.equals(response.id) &&
                parentId.equals(response.parentId) &&
                createdBy.equals(response.createdBy) &&
                createDate == response.createDate &&
                lastUpdate == response.lastUpdate;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, parentId, createdBy, createDate, lastUpdate);
    }

    @Nonnull
    @Override
    public String toString() {
        return "RecipeResponse{" +
                "id='" + id + '\'' +
                ", parentId='" + parentId + '\'' +
                ", createdBy='" + createdBy + '\'' +
                ", createDate=" + createDate +
                ", lastUpdate=" + lastUpdate +
                '}';
    }

    public static class Builder {
        private String id;
        private String parentId;
        private String createdBy;
        private long createDate;
        private long lastUpdate;

        public static Builder getDefault() {
            return new Builder().
                    setId("").
                    setParentId("").
                    setCreatedBy(Constants.getUserId()).
                    setCreateDate(0L).
                    setLastUpdate(0L);
        }

        public Builder setId(String id) {
            this.id = id;
            return this;
        }

        public Builder setParentId(String parentId) {
            this.parentId = parentId;
            return this;
        }

        public Builder setCreatedBy(String createdBy) {
            this.createdBy = createdBy;
            return this;
        }

        public Builder setCreateDate(long createDate) {
            this.createDate = createDate;
            return this;
        }

        public Builder setLastUpdate(long lastUpdate) {
            this.lastUpdate = lastUpdate;
            return this;
        }

        public RecipeResponse build() {
            return new RecipeResponse(
                    id,
                    parentId,
                    createdBy,
                    createDate,
                    lastUpdate
            );
        }
    }
}
