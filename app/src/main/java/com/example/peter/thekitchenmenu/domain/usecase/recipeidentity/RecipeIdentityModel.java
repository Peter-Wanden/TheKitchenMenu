package com.example.peter.thekitchenmenu.domain.usecase.recipeidentity;

import com.example.peter.thekitchenmenu.domain.model.PersistenceModel;

import java.util.Objects;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public final class RecipeIdentityModel implements PersistenceModel {
    @Nonnull
    private final String id;
    @Nonnull
    private final String title;
    @Nullable
    private final String description;
    private final long createDate;
    private final long lastUpdate;

    public RecipeIdentityModel(@Nonnull String id,
                 @Nonnull String title,
                 @Nullable String description,
                 long createDate,
                 long lastUpdate) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.createDate = createDate;
        this.lastUpdate = lastUpdate;
    }

    @Nonnull
    public String getId() {
        return id;
    }

    @Nonnull
    public String getTitle() {
        return title;
    }

    @Nullable
    public String getDescription() {
        return description;
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
        RecipeIdentityModel that = (RecipeIdentityModel) o;
        return createDate == that.createDate &&
                lastUpdate == that.lastUpdate &&
                id.equals(that.id) &&
                title.equals(that.title) &&
                Objects.equals(description, that.description);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, title, description, createDate, lastUpdate);
    }

    @Nonnull
    @Override
    public String toString() {
        return "RecipeIdentityModel{" +
                "id='" + id + '\'' +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", createDate=" + createDate +
                ", lastUpdate=" + lastUpdate +
                '}';
    }

    public static class Builder {
        private String id;
        private String title;
        private String description;
        private long createDate;
        private long lastUpdate;

        public static Builder basedOn(@Nonnull RecipeIdentityModel oldModel) {
            return new Builder().
                    setId(oldModel.getId()).
                    setTitle(oldModel.getTitle()).
                    setDescription(oldModel.getDescription()).
                    setCreateDate(oldModel.getCreateDate()).
                    setLastUpdate(oldModel.getLastUpdate());
        }

        public static Builder getDefault() {
            return new Builder().
                    setId("").
                    setTitle("").
                    setDescription("").
                    setCreateDate(0).
                    setLastUpdate(0);
        }

        public Builder setId(String id) {
            this.id = id;
            return this;
        }

        public Builder setTitle(String title) {
            this.title = title;
            return this;
        }

        public Builder setDescription(String description) {
            this.description = description;
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

        public RecipeIdentityModel build() {
            return new RecipeIdentityModel(
                    id,
                    title,
                    description,
                    createDate,
                    lastUpdate
            );
        }
    }
}
