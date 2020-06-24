package com.example.peter.thekitchenmenu.domain.usecase.recipe.component.portions;

import com.example.peter.thekitchenmenu.domain.model.BaseDomainPersistenceModel;

import java.util.Objects;

import javax.annotation.Nonnull;

public final class RecipePortionsPersistenceModel
        extends BaseDomainPersistenceModel {

    private int servings;
    private int sittings;

    private RecipePortionsPersistenceModel() {}

    public int getServings() {
        return servings;
    }

    public int getSittings() {
        return sittings;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof RecipePortionsPersistenceModel)) return false;
        if (!super.equals(o)) return false;
        RecipePortionsPersistenceModel that = (RecipePortionsPersistenceModel) o;
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
            extends PersistenceModelBuilder<Builder, RecipePortionsPersistenceModel> {

        public Builder() {
            domainModel = new RecipePortionsPersistenceModel();
        }

        @Override
        public Builder getDefault() {
            domainModel.dataId = "";
            domainModel.domainId = "";
            domainModel.servings = 1;
            domainModel.sittings = 1;
            domainModel.createDate = 0L;
            domainModel.lastUpdate = 0L;
            return self();
        }

        @Override
        public Builder basedOnModel(@Nonnull RecipePortionsPersistenceModel m) {
            domainModel.dataId = m.getDataId();
            domainModel.domainId = m.getDomainId();
            domainModel.servings = m.getServings();
            domainModel.sittings = m.getSittings();
            domainModel.createDate = m.getCreateDate();
            domainModel.lastUpdate = m.getLastUpdate();
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

        @Override
        protected Builder self() {
            return this;
        }
    }
}