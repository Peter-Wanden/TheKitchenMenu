package com.example.peter.thekitchenmenu.domain.usecase.recipe.component.recipeduration;

import com.example.peter.thekitchenmenu.domain.model.PersistenceModel;

import java.util.Objects;

import javax.annotation.Nonnull;

public final class RecipeDurationPersistenceModel implements PersistenceModel {
    @Nonnull
    private final String id;
    private final int prepTime;
    private final int cookTime;
    private final long createDate;
    private final long lastUpdate;

    private RecipeDurationPersistenceModel(@Nonnull String id,
                                          int prepTime,
                                          int cookTime,
                                          long createDate,
                                          long lastUpdate) {
        this.id = id;
        this.prepTime = prepTime;
        this.cookTime = cookTime;
        this.createDate = createDate;
        this.lastUpdate = lastUpdate;
    }

    @Override
    @Nonnull
    public String getDataId() {
        return id;
    }

    public int getPrepTime() {
        return prepTime;
    }

    public int getCookTime() {
        return cookTime;
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
        RecipeDurationPersistenceModel that = (RecipeDurationPersistenceModel) o;
        return id.equals(that.id) &&
                prepTime == that.prepTime &&
                cookTime == that.cookTime &&
                createDate == that.createDate &&
                lastUpdate == that.lastUpdate;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, prepTime, cookTime, createDate, lastUpdate);
    }

    @Override
    public String toString() {
        return "RecipeDurationPersistenceModel{" +
                "id='" + id + '\'' +
                ", prepTime=" + prepTime +
                ", cookTime=" + cookTime +
                ", createDate=" + createDate +
                ", lastUpdate=" + lastUpdate +
                '}';
    }

    public static class Builder {
        private String id;
        private int prepTime;
        private int cookTime;
        private long createDate;
        private long lastUpdate;

        public static Builder getDefault() {
            return new Builder().setId("").
                    setPrepTime(0).
                    setCookTime(0).
                    setCreateDate(0L).
                    setLastUpdate(0L);
        }

        public static Builder basedOnPersistenceModel(@Nonnull RecipeDurationPersistenceModel model) {
            return new Builder().
                    setId(model.getDataId()).
                    setPrepTime(model.getPrepTime()).
                    setCookTime(model.getCookTime()).
                    setCreateDate(model.getCreateDate()).
                    setLastUpdate(model.getLastUpdate());
        }

        public Builder setId(String id) {
            this.id = id;
            return this;
        }

        public Builder setPrepTime(int prepTime) {
            this.prepTime = prepTime;
            return this;
        }

        public Builder setCookTime(int cookTime) {
            this.cookTime = cookTime;
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

        public RecipeDurationPersistenceModel build() {
            return new RecipeDurationPersistenceModel(
                    id,
                    prepTime,
                    cookTime,
                    createDate,
                    lastUpdate
            );
        }
    }
}
