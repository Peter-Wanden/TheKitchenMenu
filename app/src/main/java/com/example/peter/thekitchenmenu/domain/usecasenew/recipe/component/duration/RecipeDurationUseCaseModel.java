package com.example.peter.thekitchenmenu.domain.usecasenew.recipe.component.duration;

import com.example.peter.thekitchenmenu.domain.usecasenew.common.model.BaseDomainModel;
import com.example.peter.thekitchenmenu.domain.usecasenew.common.model.DomainModel;

import javax.annotation.Nonnull;

public final class RecipeDurationUseCaseModel
        extends
        BaseDomainModel
        implements
        DomainModel.UseCaseModel {

    private int prepTime;
    private int cookTime;

    private RecipeDurationUseCaseModel(){}

    public int getPrepTime() {
        return prepTime;
    }

    public int getCookTime() {
        return cookTime;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof RecipeDurationUseCaseModel)) return false;

        RecipeDurationUseCaseModel that = (RecipeDurationUseCaseModel) o;

        if (prepTime != that.prepTime) return false;
        return cookTime == that.cookTime;
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
        return "RecipeDurationUseCaseModel{" +
                "prepTime=" + prepTime +
                ", cookTime=" + cookTime +
                '}';
    }

    public static class Builder
            extends
            BaseDomainModelBuilder<Builder, RecipeDurationUseCaseModel> {

        public Builder() {
            super(new RecipeDurationUseCaseModel());
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
        public Builder getDefault() {
            domainModel.prepTime = RecipeDurationUseCase.MIN_PREP_TIME;
            domainModel.cookTime = RecipeDurationUseCase.MIN_COOK_TIME;
            return self();
        }

        @Override
        public Builder basedOnModel(RecipeDurationUseCaseModel model) {
            domainModel.prepTime = model.prepTime;
            domainModel.cookTime = model.cookTime;
            return self();
        }

        @Override
        protected Builder self() {
            return this;
        }
    }
}
