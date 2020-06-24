package com.example.peter.thekitchenmenu.domain.usecase.recipe.component.duration;

import com.example.peter.thekitchenmenu.domain.model.BaseDomainPersistenceModel;

import javax.annotation.Nonnull;

public final class RecipeDurationPersistenceModel
        extends
        BaseDomainPersistenceModel {

    private int prepTime;
    private int cookTime;

    private RecipeDurationPersistenceModel() {}

    public int getPrepTime() {
        return prepTime;
    }

    public int getCookTime() {
        return cookTime;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof RecipeDurationPersistenceModel)) return false;
        if (!super.equals(o)) return false;

        RecipeDurationPersistenceModel that = (RecipeDurationPersistenceModel) o;

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
            PersistenceModelBuilder<Builder, RecipeDurationPersistenceModel> {

        public Builder() {
            domainModel = new RecipeDurationPersistenceModel();
        }

        @Override
        public Builder getDefault() {
            domainModel.dataId = "";
            domainModel.domainId = "";
            domainModel.prepTime = 0;
            domainModel.cookTime = 0;
            domainModel.createDate = 0L;
            domainModel.lastUpdate = 0L;
            return self();
        }

        @Override
        public Builder basedOnModel(@Nonnull RecipeDurationPersistenceModel m) {
            domainModel.dataId = m.getDataId();
            domainModel.domainId = m.getDomainId();
            domainModel.prepTime = m.getPrepTime();
            domainModel.cookTime = m.getCookTime();
            domainModel.createDate = m.getCreateDate();
            domainModel.lastUpdate = m.getLastUpdate();
            return self();
        }

        public Builder setPrepTime(int prepTime) {
            domainModel.prepTime = prepTime;
            return self();
        }

        public Builder setCookTime(int cookTime) {
            domainModel.cookTime = cookTime;
            return self();
        }

        @Override
        protected Builder self() {
            return this;
        }
    }
}
