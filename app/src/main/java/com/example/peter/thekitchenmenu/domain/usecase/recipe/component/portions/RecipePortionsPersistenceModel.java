package com.example.peter.thekitchenmenu.domain.usecase.recipe.component.portions;

import com.example.peter.thekitchenmenu.domain.usecase.BasePersistence;

import java.util.Objects;

import javax.annotation.Nonnull;

public final class RecipePortionsPersistenceModel
        extends BasePersistence {

    private int servings;
    private int sittings;
    private long createDate;
    private long lastUpdate;

    private RecipePortionsPersistenceModel() {}

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
        RecipePortionsPersistenceModel that = (RecipePortionsPersistenceModel) o;
        return dataId.equals(that.dataId) &&
                domainId.equals(that.domainId) &&
                servings == that.servings &&
                sittings == that.sittings &&
                createDate == that.createDate &&
                lastUpdate == that.lastUpdate;
    }

    @Override
    public int hashCode() {
        return Objects.hash(dataId, domainId, servings, sittings, createDate, lastUpdate);
    }

    @Override
    public String toString() {
        return "RecipePortionsPersistenceModel{" +
                "dataId='" + dataId + '\'' +
                ", domainId='" + domainId + '\'' +
                ", servings=" + servings +
                ", sittings=" + sittings +
                ", createDate=" + createDate +
                ", lastUpdate=" + lastUpdate +
                '}';
    }

    public static class Builder
            extends DomainModelBuilder<Builder, RecipePortionsPersistenceModel> {

        public Builder() {
            model = new RecipePortionsPersistenceModel();
        }

        public Builder getDefault() {
            model.dataId = "";
            model.domainId = "";
            model.servings = 1;
            model.sittings = 1;
            model.createDate = 0L;
            model.lastUpdate = 0L;
            return self();
        }

        public Builder basedOnModel(
                @Nonnull RecipePortionsPersistenceModel m) {
            model.dataId = m.getDataId();
            model.domainId = m.getDomainId();
            model.servings = m.getServings();
            model.sittings = m.getSittings();
            model.createDate = m.getCreateDate();
            model.lastUpdate = m.getLastUpdate();
            return self();
        }

        public Builder setDataId(@Nonnull String dataId) {
            model.dataId = dataId;
            return self();
        }

        public Builder setDomainId(@Nonnull String domainId) {
            model.domainId = domainId;
            return self();
        }

        public Builder setServings(int servings) {
            model.servings = servings;
            return self();
        }

        public Builder setSittings(int sittings) {
            model.sittings = sittings;
            return self();
        }

        public Builder setCreateDate(long createDate) {
            model.createDate = createDate;
            return self();
        }

        public Builder setLastUpdate(long lastUpdate) {
            model.lastUpdate = lastUpdate;
            return self();
        }

        @Override
        protected Builder self() {
            return this;
        }
    }
}