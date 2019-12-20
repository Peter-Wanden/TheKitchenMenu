package com.example.peter.thekitchenmenu.domain.usecase.recipeportions;

import com.example.peter.thekitchenmenu.domain.model.PersistenceModel;

import java.util.Objects;

import javax.annotation.Nonnull;

public class UseCaseRecpiePortions {

    public static final class Model implements PersistenceModel {
        @Nonnull
        private final String id;
        @Nonnull
        private final String recipeId;

        private final int servings;
        private final int sittings;
        private final long createDate;
        private final long lastUpdate;

        public Model(@Nonnull String id,
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
            Model model = (Model) o;
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

        @Override
        public String toString() {
            return "Model{" +
                    "id='" + id + '\'' +
                    ", recipeId='" + recipeId + '\'' +
                    ", servings=" + servings +
                    ", sittings=" + sittings +
                    ", createDate=" + createDate +
                    ", lastUpdate=" + lastUpdate +
                    '}';
        }
    }
}
