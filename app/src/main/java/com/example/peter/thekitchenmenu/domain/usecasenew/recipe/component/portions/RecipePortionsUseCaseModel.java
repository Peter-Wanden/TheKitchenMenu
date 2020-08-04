package com.example.peter.thekitchenmenu.domain.usecasenew.recipe.component.portions;

import com.example.peter.thekitchenmenu.domain.usecasenew.model.DomainModel;

import javax.annotation.Nonnull;

public final class RecipePortionsUseCaseModel
        implements
        DomainModel.UseCaseModel {

    private int servings;
    private int sittings;

    RecipePortionsUseCaseModel(int servings, int sittings) {
        this.servings = servings;
        this.sittings = sittings;
    }

    public int getServings() {
        return servings;
    }

    public int getSittings() {
        return sittings;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof RecipePortionsUseCaseModel)) return false;

        RecipePortionsUseCaseModel that = (RecipePortionsUseCaseModel) o;

        if (servings != that.servings) return false;
        return sittings == that.sittings;
    }

    @Override
    public int hashCode() {
        int result = servings;
        result = 31 * result + sittings;
        return result;
    }

    @Nonnull
    @Override
    public String toString() {
        return "RecipePortionsUseCaseModel{" +
                "servings=" + servings +
                ", sittings=" + sittings +
                '}';
    }
}
