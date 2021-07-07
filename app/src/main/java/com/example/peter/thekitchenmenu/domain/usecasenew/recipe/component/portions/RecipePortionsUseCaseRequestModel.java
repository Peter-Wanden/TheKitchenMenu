package com.example.peter.thekitchenmenu.domain.usecasenew.recipe.component.portions;

import com.example.peter.thekitchenmenu.domain.usecasenew.common.model.BaseDomainModel;
import com.example.peter.thekitchenmenu.domain.usecasenew.common.model.DomainModel;

import javax.annotation.Nonnull;

public final class RecipePortionsUseCaseRequestModel
        extends
        BaseDomainModel
        implements
        DomainModel.UseCaseRequestModel {

    private int servings;
    private int sittings;

    private RecipePortionsUseCaseRequestModel(){}

    public int getServings() {
        return servings;
    }

    public int getSittings() {
        return sittings;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof RecipePortionsUseCaseRequestModel)) return false;

        RecipePortionsUseCaseRequestModel that = (RecipePortionsUseCaseRequestModel) o;

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
        return "RecipePortionsRequestModel{" +
                "servings=" + servings +
                ", sittings=" + sittings +
                '}';
    }

    public static class Builder
            extends
            BaseDomainModelBuilder<Builder, RecipePortionsUseCaseRequestModel> {

        public Builder() {
            super(new RecipePortionsUseCaseRequestModel());
        }

        @Override
        public Builder getDefault() {
            domainModel.sittings = RecipePortionsUseCase.MIN_SITTINGS;
            domainModel.servings = RecipePortionsUseCase.MIN_SERVINGS;
            return self();
        }

        @Override
        public Builder basedOnModel(RecipePortionsUseCaseRequestModel model) {
            domainModel.servings = model.servings;
            domainModel.sittings = model.sittings;
            return self();
        }

        public Builder basedOnResponseModel(RecipePortionsUseCaseResponseModel model) {
            domainModel.servings = model.getServings();
            domainModel.sittings = model.getSittings();
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
