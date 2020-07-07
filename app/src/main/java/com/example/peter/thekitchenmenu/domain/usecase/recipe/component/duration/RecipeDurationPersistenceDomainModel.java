package com.example.peter.thekitchenmenu.domain.usecase.recipe.component.duration;

import com.example.peter.thekitchenmenu.domain.model.BasePersistenceDomainModel;

import javax.annotation.Nonnull;

public final class RecipeDurationPersistenceDomainModel
        extends
        BasePersistenceDomainModel {

    private int prepTime;
    private int cookTime;

    private RecipeDurationPersistenceDomainModel() {}

    public int getPrepTime() {
        return prepTime;
    }

    public int getCookTime() {
        return cookTime;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof RecipeDurationPersistenceDomainModel)) return false;
        if (!super.equals(o)) return false;

        RecipeDurationPersistenceDomainModel that = (RecipeDurationPersistenceDomainModel) o;

        if (prepTime != that.prepTime) return false;
        return cookTime == that.cookTime;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + prepTime;
        result = 31 * result + cookTime;
        return result;
    }

    @Nonnull
    @Override
    public String toString() {
        return "RecipeDurationPersistenceModel{" +
                "dataId='" + dataId + '\'' +
                ", domainId='" + domainId + '\'' +
                ", prepTime=" + prepTime +
                ", cookTime=" + cookTime +
                ", createDate=" + createDate +
                ", lastUpdate=" + lastUpdate +
                '}';
    }

    public static class Builder
            extends
            PersistenceModelBuilder<Builder, RecipeDurationPersistenceDomainModel> {

        public Builder() {
            persistenceModel = new RecipeDurationPersistenceDomainModel();
        }

        @Override
        public Builder getDefault() {
            persistenceModel.dataId = "";
            persistenceModel.domainId = "";
            persistenceModel.prepTime = 0;
            persistenceModel.cookTime = 0;
            persistenceModel.createDate = 0L;
            persistenceModel.lastUpdate = 0L;
            return self();
        }

        @Override
        public Builder basedOnModel(@Nonnull RecipeDurationPersistenceDomainModel m) {
            persistenceModel.dataId = m.getDataId();
            persistenceModel.domainId = m.getDomainId();
            persistenceModel.prepTime = m.getPrepTime();
            persistenceModel.cookTime = m.getCookTime();
            persistenceModel.createDate = m.getCreateDate();
            persistenceModel.lastUpdate = m.getLastUpdate();
            return self();
        }

        public Builder setPrepTime(int prepTime) {
            persistenceModel.prepTime = prepTime;
            return self();
        }

        public Builder setCookTime(int cookTime) {
            persistenceModel.cookTime = cookTime;
            return self();
        }

        @Override
        protected Builder self() {
            return this;
        }
    }
}
