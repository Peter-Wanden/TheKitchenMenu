package com.example.peter.thekitchenmenu.domain.usecasenew.recipe.component.portions;

import com.example.peter.thekitchenmenu.domain.usecasenew.common.model.BaseDomainModel;
import com.example.peter.thekitchenmenu.domain.usecasenew.common.model.DomainModel;

import javax.annotation.Nonnull;

public final class RecipePortionsUseCaseModel
        extends
        BaseDomainModel
        implements
        DomainModel.UseCaseModel {

    private int servings;
    private int sittings;

    private RecipePortionsUseCaseModel() {}

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

    public static class Builder
            extends BaseDomainModelBuilder<Builder, RecipePortionsUseCaseModel> {

        public Builder() {
            super(new RecipePortionsUseCaseModel());
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
        public Builder getDefault() {
            domainModel.servings = RecipePortionsUseCase.MIN_SERVINGS;
            domainModel.sittings = RecipePortionsUseCase.MIN_SITTINGS;
            return self();
        }

        @Override
        public Builder basedOnModel(RecipePortionsUseCaseModel model) {
            domainModel.servings = model.servings;
            domainModel.sittings = model.sittings;
            return self();
        }

        @Override
        protected Builder self() {
            return this;
        }
    }
}
