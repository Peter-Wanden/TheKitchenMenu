package com.example.peter.thekitchenmenu.domain.usecase.recipe.component.portions;

import com.example.peter.thekitchenmenu.domain.model.BaseDomainPersistenceModel;

import java.util.Objects;

import javax.annotation.Nonnull;

public final class RecipePortionsPersistenceModel
        extends BaseDomainPersistenceModel {

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
            domainModel = new RecipePortionsPersistenceModel();
        }

        public Builder getDefault() {
            domainModel.dataId = "";
            domainModel.domainId = "";
            domainModel.servings = 1;
            domainModel.sittings = 1;
            domainModel.createDate = 0L;
            domainModel.lastUpdate = 0L;
            return self();
        }

        public Builder basedOnModel(
                @Nonnull RecipePortionsPersistenceModel m) {
            domainModel.dataId = m.getDataId();
            domainModel.domainId = m.getDomainId();
            domainModel.servings = m.getServings();
            domainModel.sittings = m.getSittings();
            domainModel.createDate = m.getCreateDate();
            domainModel.lastUpdate = m.getLastUpdate();
            return self();
        }

        public Builder setDataId(@Nonnull String dataId) {
            domainModel.dataId = dataId;
            return self();
        }

        public Builder setDomainId(@Nonnull String domainId) {
            domainModel.domainId = domainId;
            return self();
        }

        public Builder setServings(int servings) {
            domainModel.servings = servings;
            return self();
        }

        public Builder setSittings(int sittings) {
            domainModel.sittings = sittings;
            return self();
        }

        public Builder setCreateDate(long createDate) {
            domainModel.createDate = createDate;
            return self();
        }

        public Builder setLastUpdate(long lastUpdate) {
            domainModel.lastUpdate = lastUpdate;
            return self();
        }

        @Override
        protected Builder self() {
            return this;
        }
    }
}