package com.example.peter.thekitchenmenu.domain.usecasenew.recipe.component.duration;

import com.example.peter.thekitchenmenu.domain.usecasenew.common.model.BaseDomainPersistenceModel;

import javax.annotation.Nonnull;

public final class RecipeDurationUseCasePersistenceModel
        extends
        BaseDomainPersistenceModel {

    private int prepTime;
    private int cookTime;

    private RecipeDurationUseCasePersistenceModel() {}

    public int getPrepTime() {
        return prepTime;
    }

    public int getCookTime() {
        return cookTime;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof RecipeDurationUseCasePersistenceModel)) return false;

        RecipeDurationUseCasePersistenceModel model = (RecipeDurationUseCasePersistenceModel) o;

        if (prepTime != model.prepTime) return false;
        return cookTime == model.cookTime;
    }

    @Override
    public int hashCode() {
        int result = prepTime;
        result = 31 * result + cookTime;
        return result;
    }

    @Nonnull
    @Override
    public String toString() {
        return "RecipeDurationUseCasePersistenceModel{" +
                "prepTime=" + prepTime +
                ", cookTime=" + cookTime +
                ", dataId='" + dataId + '\'' +
                ", domainId='" + domainId + '\'' +
                ", createDate=" + createDate +
                ", lastUpdate=" + lastUpdate +
                '}';
    }

    public static class Builder
            extends
            PersistenceModelBuilder<Builder, RecipeDurationUseCasePersistenceModel> {

        public Builder() {
            super(new RecipeDurationUseCasePersistenceModel());
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
        public Builder basedOnModel(@Nonnull RecipeDurationUseCasePersistenceModel m) {
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
