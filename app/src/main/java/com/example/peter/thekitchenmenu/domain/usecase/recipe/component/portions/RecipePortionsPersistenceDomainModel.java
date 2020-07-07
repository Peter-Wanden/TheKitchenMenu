package com.example.peter.thekitchenmenu.domain.usecase.recipe.component.portions;

import com.example.peter.thekitchenmenu.domain.model.BasePersistenceDomainModel;

import java.util.Objects;

import javax.annotation.Nonnull;

public final class RecipePortionsPersistenceDomainModel
        extends BasePersistenceDomainModel {

    private int servings;
    private int sittings;

    private RecipePortionsPersistenceDomainModel() {}

    public int getServings() {
        return servings;
    }

    public int getSittings() {
        return sittings;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof RecipePortionsPersistenceDomainModel)) return false;
        if (!super.equals(o)) return false;
        RecipePortionsPersistenceDomainModel that = (RecipePortionsPersistenceDomainModel) o;
        return servings == that.servings &&
                sittings == that.sittings;
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), servings, sittings);
    }

    @Nonnull
    @Override
    public String toString() {
        return "RecipePortionsPersistenceModel{" +
                "servings=" + servings +
                ", sittings=" + sittings +
                ", dataId='" + dataId + '\'' +
                ", domainId='" + domainId + '\'' +
                ", createDate=" + createDate +
                ", lastUpdate=" + lastUpdate +
                '}';
    }

    public static class Builder
            extends PersistenceModelBuilder<Builder, RecipePortionsPersistenceDomainModel> {

        public Builder() {
            persistenceModel = new RecipePortionsPersistenceDomainModel();
        }

        @Override
        public Builder getDefault() {
            persistenceModel.dataId = "";
            persistenceModel.domainId = "";
            persistenceModel.servings = 1;
            persistenceModel.sittings = 1;
            persistenceModel.createDate = 0L;
            persistenceModel.lastUpdate = 0L;
            return self();
        }

        @Override
        public Builder basedOnModel(@Nonnull RecipePortionsPersistenceDomainModel m) {
            persistenceModel.dataId = m.getDataId();
            persistenceModel.domainId = m.getDomainId();
            persistenceModel.servings = m.getServings();
            persistenceModel.sittings = m.getSittings();
            persistenceModel.createDate = m.getCreateDate();
            persistenceModel.lastUpdate = m.getLastUpdate();
            return self();
        }

        public Builder setServings(int servings) {
            persistenceModel.servings = servings;
            return self();
        }

        public Builder setSittings(int sittings) {
            persistenceModel.sittings = sittings;
            return self();
        }

        @Override
        protected Builder self() {
            return this;
        }
    }
}