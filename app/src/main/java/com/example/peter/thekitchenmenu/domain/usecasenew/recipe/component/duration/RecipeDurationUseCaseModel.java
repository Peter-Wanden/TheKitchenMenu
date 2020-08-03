package com.example.peter.thekitchenmenu.domain.usecasenew.recipe.component.duration;

import com.example.peter.thekitchenmenu.domain.usecasenew.model.DomainModel;

import javax.annotation.Nonnull;

public final class RecipeDurationUseCaseModel
        implements
        DomainModel.UseCaseModel {

    private int prepTime;
    private int cookTime;

    RecipeDurationUseCaseModel(int prepTime, int cookTime) {
        this.prepTime = prepTime;
        this.cookTime = cookTime;
    }

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
}
