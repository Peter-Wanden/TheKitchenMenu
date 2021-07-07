package com.example.peter.thekitchenmenu.domain.usecasenew.recipe.invoker.metadata;

import com.example.peter.thekitchenmenu.domain.usecasenew.common.failreasons.FailReasons;
import com.example.peter.thekitchenmenu.domain.usecasenew.common.metadata.ComponentState;
import com.example.peter.thekitchenmenu.domain.usecasenew.common.model.BaseDomainModel;
import com.example.peter.thekitchenmenu.domain.usecasenew.common.model.DomainModel;
import com.example.peter.thekitchenmenu.domain.usecasenew.recipe.component.RecipeComponentNameName;

import java.util.HashMap;
import java.util.List;
import java.util.Objects;

import javax.annotation.Nonnull;

public final class RecipeMacroMetadataUseCaseResponseModel
        extends
        BaseDomainModel
        implements
        DomainModel.UseCaseResponseModel {

    private ComponentState componentState;
    private HashMap<RecipeComponentNameName, ComponentState> componentStates;
    private List<FailReasons> failReasons;

    private RecipeMacroMetadataUseCaseResponseModel(){}

    public ComponentState getComponentState() {
        return componentState;
    }

    public HashMap<RecipeComponentNameName, ComponentState> getComponentStates() {
        return componentStates;
    }

    public List<FailReasons> getFailReasons() {
        return failReasons;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof RecipeMacroMetadataUseCaseResponseModel)) return false;

        RecipeMacroMetadataUseCaseResponseModel that = (RecipeMacroMetadataUseCaseResponseModel) o;

        if (componentState != that.componentState) return false;
        if (!Objects.equals(componentStates, that.componentStates))
            return false;
        return Objects.equals(failReasons, that.failReasons);
    }

    @Nonnull
    @Override
    public String toString() {
        return "RecipeMetadataUseCaseResponseModel{" +
                "componentState=" + componentState +
                ", componentStates=" + componentStates +
                ", failReasons=" + failReasons +
                '}';
    }

    @Override
    public int hashCode() {
        int result = componentState != null ? componentState.hashCode() : 0;
        result = 31 * result + (componentStates != null ? componentStates.hashCode() : 0);
        result = 31 * result + (failReasons != null ? failReasons.hashCode() : 0);
        return result;
    }

    public static class Builder
            extends BaseDomainModelBuilder<Builder, RecipeMacroMetadataUseCaseResponseModel> {

        public Builder() {
            super(new RecipeMacroMetadataUseCaseResponseModel());
        }

        public Builder setComponentState(ComponentState componentState) {
            domainModel.componentState = componentState;
            return self();
        }

        public Builder setComponentStates(HashMap<RecipeComponentNameName, ComponentState> componentStates) {
            domainModel.componentStates = componentStates;
            return this;
        }

        public Builder setFailReasons(List<FailReasons> failReasons) {
            domainModel.failReasons = failReasons;
            return self();
        }

        @Override
        public Builder getDefault() {
            domainModel.componentStates = new HashMap<>();
            return self();
        }

        @Override
        public Builder basedOnModel(RecipeMacroMetadataUseCaseResponseModel model) {
            domainModel.componentStates = model.componentStates;
            return self();
        }

        @Override
        protected Builder self() {
            return this;
        }
    }
}
