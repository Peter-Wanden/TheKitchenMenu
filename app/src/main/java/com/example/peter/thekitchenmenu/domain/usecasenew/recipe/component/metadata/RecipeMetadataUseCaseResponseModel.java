package com.example.peter.thekitchenmenu.domain.usecasenew.recipe.component.metadata;

import com.example.peter.thekitchenmenu.domain.usecasenew.common.metadata.ComponentState;
import com.example.peter.thekitchenmenu.domain.usecasenew.common.model.BaseDomainModel;
import com.example.peter.thekitchenmenu.domain.usecasenew.common.model.DomainModel;
import com.example.peter.thekitchenmenu.domain.usecasenew.recipe.component.RecipeComponentName;

import java.util.HashMap;
import java.util.Objects;

import javax.annotation.Nonnull;

public final class RecipeMetadataUseCaseResponseModel
        extends
        BaseDomainModel
        implements
        DomainModel.UseCaseResponseModel {

    private HashMap<RecipeComponentName, ComponentState> componentStates;

    private RecipeMetadataUseCaseResponseModel(){}

    public HashMap<RecipeComponentName, ComponentState> getComponentStates() {
        return componentStates;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof RecipeMetadataUseCaseResponseModel)) return false;

        RecipeMetadataUseCaseResponseModel that = (RecipeMetadataUseCaseResponseModel) o;

        return Objects.equals(componentStates, that.componentStates);
    }

    @Override
    public int hashCode() {
        return componentStates != null ? componentStates.hashCode() : 0;
    }

    @Nonnull
    @Override
    public String toString() {
        return "RecipeMetadataUseCaseResponseModel{" +
                "componentStates=" + componentStates +
                '}';
    }

    public static class Builder
            extends BaseDomainModelBuilder<Builder, RecipeMetadataUseCaseResponseModel> {

        public Builder() {
            super(new RecipeMetadataUseCaseResponseModel());
        }

        @Override
        public Builder getDefault() {
            domainModel.componentStates = new HashMap<>();
            return self();
        }

        @Override
        public Builder basedOnModel(RecipeMetadataUseCaseResponseModel model) {
            domainModel.componentStates = model.componentStates;
            return self();
        }

        public Builder setComponentStates(
                @Nonnull HashMap<RecipeComponentName, ComponentState> componentStates) {
            domainModel.componentStates = componentStates;
            return this;
        }

        @Override
        protected Builder self() {
            return this;
        }
    }
}
