package com.example.peter.thekitchenmenu.domain.usecasenew.recipe.invoker.metadata;

import com.example.peter.thekitchenmenu.domain.usecase.recipe.component.metadata.RecipeMetadataResponse;
import com.example.peter.thekitchenmenu.domain.usecasenew.common.metadata.ComponentState;
import com.example.peter.thekitchenmenu.domain.usecasenew.common.model.BaseDomainModel;
import com.example.peter.thekitchenmenu.domain.usecasenew.common.model.DomainModel;
import com.example.peter.thekitchenmenu.domain.usecasenew.recipe.component.RecipeComponentNameName;

import java.util.HashMap;
import java.util.Objects;

import javax.annotation.Nonnull;

public final class RecipeMacroMetadataUseCaseRequestModel
        extends
        BaseDomainModel
        implements
        DomainModel.UseCaseRequestModel {

    private HashMap<RecipeComponentNameName, ComponentState> componentStates;

    private RecipeMacroMetadataUseCaseRequestModel(){}

    public HashMap<RecipeComponentNameName, ComponentState> getComponentStates() {
        return componentStates;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof RecipeMacroMetadataUseCaseRequestModel)) return false;

        RecipeMacroMetadataUseCaseRequestModel that = (RecipeMacroMetadataUseCaseRequestModel) o;

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
                    RecipeMacroMetadataUseCaseRequestModel> {

        public Builder() {
            super(new RecipeMacroMetadataUseCaseRequestModel());
        }

        @Override
        public Builder basedOnModel(RecipeMacroMetadataUseCaseRequestModel model) {
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

        public Builder setComponentStates(HashMap<RecipeComponentNameName,
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
