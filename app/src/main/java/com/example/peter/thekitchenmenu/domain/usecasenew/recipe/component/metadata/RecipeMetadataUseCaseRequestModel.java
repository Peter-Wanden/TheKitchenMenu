package com.example.peter.thekitchenmenu.domain.usecasenew.recipe.component.metadata;

import com.example.peter.thekitchenmenu.domain.usecase.recipe.component.metadata.RecipeMetadataResponse;
import com.example.peter.thekitchenmenu.domain.usecasenew.common.metadata.ComponentState;
import com.example.peter.thekitchenmenu.domain.usecasenew.common.model.BaseDomainModel;
import com.example.peter.thekitchenmenu.domain.usecasenew.common.model.DomainModel;
import com.example.peter.thekitchenmenu.domain.usecasenew.recipe.component.RecipeComponentName;

import java.util.HashMap;
import java.util.Objects;

import javax.annotation.Nonnull;

public final class RecipeMetadataUseCaseRequestModel
        extends
        BaseDomainModel
        implements
        DomainModel.UseCaseRequestModel {

    private HashMap<RecipeComponentName, ComponentState> componentStates;

    private RecipeMetadataUseCaseRequestModel(){}

    public HashMap<RecipeComponentName, ComponentState> getComponentStates() {
        return componentStates;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof RecipeMetadataUseCaseRequestModel)) return false;

        RecipeMetadataUseCaseRequestModel that = (RecipeMetadataUseCaseRequestModel) o;

        return Objects.equals(componentStates, that.componentStates);
    }

    @Override
    public int hashCode() {
        return componentStates != null ? componentStates.hashCode() : 0;
    }

    @Nonnull
    @Override
    public String toString() {
        return "RecipeMetadataUseCaseRequestModel{" +
                "componentStates=" + componentStates +
                '}';
    }

    public static class Builder
            extends
            BaseDomainModelBuilder<
                    Builder,
                    RecipeMetadataUseCaseRequestModel> {

        public Builder() {
            super(new RecipeMetadataUseCaseRequestModel());
        }

        @Override
        public Builder basedOnModel(RecipeMetadataUseCaseRequestModel model) {
            domainModel.componentStates = model.componentStates;
            return null;
        }

        @Override
        public Builder getDefault() {
            domainModel.componentStates = new HashMap<>();
            return self();
        }

        public Builder basedOnResponseModel(RecipeMetadataResponse.DomainModel m) {
            domainModel.componentStates = m.getComponentStates();
            return self();
        }

        public Builder setComponentStates(HashMap<RecipeComponentName,
                ComponentState> componentStates) {
            domainModel.componentStates = componentStates;
            return self();
        }

        @Override
        protected Builder self() {
            return this;
        }
    }
}
