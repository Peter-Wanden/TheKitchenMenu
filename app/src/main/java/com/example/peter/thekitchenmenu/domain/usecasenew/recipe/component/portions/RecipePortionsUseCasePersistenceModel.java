package com.example.peter.thekitchenmenu.domain.usecasenew.recipe.component.portions;

import com.example.peter.thekitchenmenu.domain.usecasenew.model.BaseDomainPersistenceModel;

import java.util.Objects;

import javax.annotation.Nonnull;

public final class RecipePortionsUseCasePersistenceModel
        extends BaseDomainPersistenceModel {

    private int servings;
    private int sittings;

    private RecipePortionsUseCasePersistenceModel() {}

    public int getServings() {
        return servings;
    }

    public int getSittings() {
        return sittings;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof RecipePortionsUseCasePersistenceModel)) return false;
        if (!super.equals(o)) return false;
        RecipePortionsUseCasePersistenceModel that = (RecipePortionsUseCasePersistenceModel) o;
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
            extends PersistenceModelBuilder<Builder, RecipePortionsUseCasePersistenceModel> {

        public Builder() {
            domainModel = new RecipePortionsUseCasePersistenceModel();
        }

        @Override
        public Builder getDefault() {
            domainModel.dataId = "";
            domainModel.domainId = "";
            domainModel.servings = RecipePortionsUseCase.MIN_SERVINGS;
            domainModel.sittings = RecipePortionsUseCase.MIN_SITTINGS;
            domainModel.createDate = 0L;
            domainModel.lastUpdate = 0L;
            return self();
        }

        @Override
        public Builder basedOnModel(@Nonnull RecipePortionsUseCasePersistenceModel m) {
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