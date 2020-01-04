package com.example.peter.thekitchenmenu.domain.usecase.recipeportions;

import com.example.peter.thekitchenmenu.domain.model.PersistenceModel;

import java.util.Objects;

import javax.annotation.Nonnull;

import static com.example.peter.thekitchenmenu.domain.usecase.recipeportions.RecipePortions.MIN_SERVINGS;
import static com.example.peter.thekitchenmenu.domain.usecase.recipeportions.RecipePortions.MIN_SITTINGS;

public final class RecipePortionsModel implements PersistenceModel {
    @Nonnull
    private final String id;
    @Nonnull
    private final String recipeId;
    private final int servings;
    private final int sittings;
    private final long createDate;
    private final long lastUpdate;

    private RecipePortionsModel(@Nonnull String id,
                  @Nonnull String recipeId,
                  int servings,
                  int sittings,
                  long createDate,
                  long lastUpdate) {
        this.id = id;
        this.recipeId = recipeId;
        this.servings = servings;
        this.sittings = sittings;
        this.createDate = createDate;
        this.lastUpdate = lastUpdate;
    }

    @Override
    @Nonnull
    public String getId() {
        return id;
    }

    @Nonnull
    public String getRecipeId() {
        return recipeId;
    }

    public int getServings() {
        return servings;
    }

    public int getSittings() {
        return sittings;
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
        RecipePortionsModel model = (RecipePortionsModel) o;
        return servings == model.servings &&
                sittings == model.sittings &&
                createDate == model.createDate &&
                lastUpdate == model.lastUpdate &&
                id.equals(model.id) &&
                recipeId.equals(model.recipeId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, recipeId, servings, sittings, createDate, lastUpdate);
    }

    @Nonnull
    @Override
    public String toString() {
        return "RecipePortionsModel{" +
                "id='" + id + '\'' +
                ", recipeId='" + recipeId + '\'' +
                ", servings=" + servings +
                ", sittings=" + sittings +
                ", createDate=" + createDate +
                ", lastUpdate=" + lastUpdate +
                '}';
    }

    public static class Builder {
        private String id;
        private String recipeId;
        private int servings;
        private int sittings;
        private long createDate;
        private long lastUpdate;

        public static Builder basedOn(@Nonnull RecipePortionsModel oldModel) {
            return new Builder().
                    setId(oldModel.getId()).
                    setRecipeId(oldModel.getRecipeId()).
                    setServings(oldModel.getServings()).
                    setSittings(oldModel.getSittings()).
                    setCreateDate(oldModel.getCreateDate()).
                    setLastUpdate(oldModel.getLastUpdate());
        }

        public Builder getDefault() {
            return new Builder().
                    setId("").
                    setRecipeId("").
                    setServings(MIN_SERVINGS).
                    setSittings(MIN_SITTINGS).
                    setCreateDate(0L).
                    setLastUpdate(0L);
        }

        public Builder setId(@Nonnull String id) {
            this.id = id;
            return this;
        }

        public Builder setRecipeId(@Nonnull String recipeId) {
            this.recipeId = recipeId;
            return this;
        }

        public Builder setServings(int servings) {
            this.servings = servings;
            return this;
        }

        public Builder setSittings(int sittings) {
            this.sittings = sittings;
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

        public RecipePortionsModel build() {
            return new RecipePortionsModel(
                    id,
                    recipeId,
                    servings,
                    sittings,
                    createDate,
                    lastUpdate
            );
        }
    }
}