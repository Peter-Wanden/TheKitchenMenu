package com.example.peter.thekitchenmenu.domain.usecasenew.recipe.invoker.metadata;

import com.example.peter.thekitchenmenu.domain.usecasenew.common.failreasons.FailReasons;
import com.example.peter.thekitchenmenu.domain.usecasenew.common.metadata.ComponentState;
import com.example.peter.thekitchenmenu.domain.usecasenew.common.model.BaseDomainModel;
import com.example.peter.thekitchenmenu.domain.usecasenew.common.model.DomainModel;
import com.example.peter.thekitchenmenu.domain.usecasenew.recipe.component.RecipeComponentNameName;

import java.util.HashMap;
import java.util.List;

import javax.annotation.Nonnull;

public final class RecipeMacroMetadataUseCaseModel
        extends
        BaseDomainModel
        implements
        DomainModel.UseCaseModel {

    private ComponentState componentState;
    private HashMap<RecipeComponentNameName, ComponentState> componentStates;
    private List<FailReasons> failReasons;

    private RecipeMacroMetadataUseCaseModel() {}

    @Nonnull
    public ComponentState getComponentState() {
        return componentState;
    }

    @Nonnull
    public HashMap<RecipeComponentNameName, ComponentState> getComponentStates() {
        return componentStates;
    }

    @Nonnull
    public List<FailReasons> getFailReasons() {
        return failReasons;
    }

    public static class Builder
            extends
            BaseDomainModelBuilder<Builder, RecipeMacroMetadataUseCaseModel> {

        public Builder() {
            super(new RecipeMacroMetadataUseCaseModel());
        }

        public Builder setComponentState(ComponentState componentState) {
            domainModel.componentState = componentState;
            return this;
        }

        public Builder setComponentStates(
                HashMap<RecipeComponentNameName, ComponentState> componentStates) {
            domainModel.componentStates = componentStates;
            return self();
        }

        public Builder setFailReasons(List<FailReasons> failReasons) {
            domainModel.failReasons = failReasons;
            return self();
        }

        @Override
        public Builder getDefault() {
            domainModel.componentState = ComponentState.INVALID_DEFAULT;
            domainModel.componentStates = new HashMap<>();
            return self();
        }

        @Override
        public Builder basedOnModel(RecipeMacroMetadataUseCaseModel model) {
            domainModel.componentState = model.componentState;
            domainModel.componentStates = model.componentStates;
            domainModel.failReasons = model.failReasons;
            return self();
        }

        @Override
        protected Builder self() {
            return this;
        }
    }
}
