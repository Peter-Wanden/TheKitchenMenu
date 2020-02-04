package com.example.peter.thekitchenmenu.domain.usecase.recipe.recipestate;

import com.example.peter.thekitchenmenu.domain.model.PersistenceModel;

import java.util.Objects;

import javax.annotation.Nonnull;

public final class RecipePersistenceModel implements PersistenceModel {
    @Nonnull
    private final String id;
    @Nonnull
    private final String parentId;
    @Nonnull
    private final String createdBy;
    private final long createDate;
    private final long lastUpdate;
    private final boolean isDraft;

    private RecipePersistenceModel(@Nonnull String id,
                                  @Nonnull String parentId,
                                  @Nonnull String createdBy,
                                  long createDate,
                                  long lastUpdate,
                                  boolean isDraft) {
        this.id = id;
        this.parentId = parentId;
        this.createdBy = createdBy;
        this.createDate = createDate;
        this.lastUpdate = lastUpdate;
        this.isDraft = isDraft;
    }

    @Override
    @Nonnull
    public String getId() {
        return id;
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

    public boolean isDraft() {
        return isDraft;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RecipePersistenceModel that = (RecipePersistenceModel) o;
        return createDate == that.createDate &&
                lastUpdate == that.lastUpdate &&
                isDraft == that.isDraft &&
                id.equals(that.id) &&
                parentId.equals(that.parentId) &&
                createdBy.equals(that.createdBy);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, parentId, createdBy, createDate, lastUpdate, isDraft);
    }

    @Override
    public String toString() {
        return "RecipePersistenceModel{" +
                "id='" + id + '\'' +
                ", parentId='" + parentId + '\'' +
                ", createdBy='" + createdBy + '\'' +
                ", createDate=" + createDate +
                ", lastUpdate=" + lastUpdate +
                ", isDraft=" + isDraft +
                '}';
    }

    public static class Builder {
        private String id;
        private String parentId;
        private String createdBy;
        private long createDate;
        private long lastUpdate;
        private boolean isDraft;

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

        public Builder setDraft(boolean draft) {
            isDraft = draft;
            return this;
        }

        public RecipePersistenceModel build() {
            return new RecipePersistenceModel(
                    id,
                    parentId,
                    createdBy,
                    createDate,
                    lastUpdate,
                    isDraft
            );
        }
    }
}
