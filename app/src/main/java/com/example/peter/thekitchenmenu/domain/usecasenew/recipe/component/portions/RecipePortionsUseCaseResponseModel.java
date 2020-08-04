package com.example.peter.thekitchenmenu.domain.usecasenew.recipe.component.portions;

import com.example.peter.thekitchenmenu.domain.usecasenew.model.BaseDomainModel;
import com.example.peter.thekitchenmenu.domain.usecasenew.model.BaseDomainModelBuilder;
import com.example.peter.thekitchenmenu.domain.usecasenew.model.DomainModel;

import javax.annotation.Nonnull;

import static com.example.peter.thekitchenmenu.domain.usecasenew.recipe.component.portions.RecipePortionsUseCase.*;

public final class RecipePortionsUseCaseResponseModel
        extends
        BaseDomainModel
        implements
        DomainModel.UseCaseResponseModel {

    private int servings;
    private int sittings;
    private int portions;

    private RecipePortionsUseCaseResponseModel(){}

    public int getServings() {
        return servings;
    }

    public int getSittings() {
        return sittings;
    }

    public int getPortions() {
        return portions;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof RecipePortionsUseCaseResponseModel)) return false;

        RecipePortionsUseCaseResponseModel that = (RecipePortionsUseCaseResponseModel) o;

        if (servings != that.servings) return false;
        if (sittings != that.sittings) return false;
        return portions == that.portions;
    }

    @Override
    public int hashCode() {
        int result = servings;
        result = 31 * result + sittings;
        result = 31 * result + portions;
        return result;
    }

    @Nonnull
    @Override
    public String toString() {
        return "RecipePortionsUseCaseResponseModel{" +
                "servings=" + servings +
                ", sittings=" + sittings +
                ", portions=" + portions +
                '}';
    }

    public static class Builder
            extends
            BaseDomainModelBuilder<Builder, RecipePortionsUseCaseResponseModel> {

        public Builder() {
            domainModel = new RecipePortionsUseCaseResponseModel();
        }

        @Override
        public Builder getDefault() {
            domainModel.servings = MIN_SERVINGS;
            domainModel.sittings = MIN_SERVINGS;
            domainModel.portions = MIN_SERVINGS * MIN_SITTINGS;
            return self();
        }

        @Override
        public Builder basedOnModel(RecipePortionsUseCaseResponseModel model) {
            domainModel.servings = model.servings;
            domainModel.sittings = model.sittings;
            domainModel.portions = model.portions;
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

        public Builder setPortions(int portions) {
            domainModel.portions = portions;
            return self();
        }

        @Override
        protected Builder self() {
            return this;
        }
    }
}
