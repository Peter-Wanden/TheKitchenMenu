package com.example.peter.thekitchenmenu.domain.usecasenew.recipe.invoker.metadata;

import com.example.peter.thekitchenmenu.data.repository.source.local.recipe.metadata.datasource.componentstate.RecipeComponentStateEntity;
import com.example.peter.thekitchenmenu.data.repository.source.local.recipe.metadata.datasource.failreason.RecipeFailReasonEntity;
import com.example.peter.thekitchenmenu.data.repository.source.local.recipe.metadata.datasource.parent.RecipeMetadataParentEntity;
import com.example.peter.thekitchenmenu.domain.usecasenew.common.failreasons.FailReasons;
import com.example.peter.thekitchenmenu.domain.usecasenew.common.metadata.ComponentState;
import com.example.peter.thekitchenmenu.domain.usecasenew.common.model.BaseDomainPersistenceModel;
import com.example.peter.thekitchenmenu.domain.usecasenew.recipe.component.RecipeComponentNameName;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

import javax.annotation.Nonnull;

/**
 * Represented in primitive form by {@link RecipeMetadataParentEntity},
 * {@link RecipeComponentStateEntity} & {@link RecipeFailReasonEntity}
 */
public final class RecipeMacroMetadataUseCasePersistenceModel
        extends
        BaseDomainPersistenceModel {

    private ComponentState componentState;
    private HashMap<RecipeComponentNameName, ComponentState> componentStates;
    private List<FailReasons> failReasons;


    private RecipeMacroMetadataUseCasePersistenceModel(){}

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
        if (!(o instanceof RecipeMacroMetadataUseCasePersistenceModel)) return false;

        RecipeMacroMetadataUseCasePersistenceModel that = (RecipeMacroMetadataUseCasePersistenceModel) o;

        if (componentState != that.componentState) return false;
        if (!Objects.equals(componentStates, that.componentStates))
            return false;
        return Objects.equals(failReasons, that.failReasons);
    }

    @Override
    public int hashCode() {
        int result = componentState != null ? componentState.hashCode() : 0;
        result = 31 * result + (componentStates != null ? componentStates.hashCode() : 0);
        result = 31 * result + (failReasons != null ? failReasons.hashCode() : 0);
        return result;
    }

    @Nonnull
    @Override
    public String toString() {
        return "RecipeMetadataUseCasePersistenceModel{" +
                "componentState=" + componentState +
                ", componentStates=" + componentStates +
                ", failReasons=" + failReasons +
                ", dataId='" + dataId + '\'' +
                ", domainId='" + domainId + '\'' +
                '}';
    }

    public static class Builder
            extends
            PersistenceModelBuilder<Builder, RecipeMacroMetadataUseCasePersistenceModel> {

        public Builder() {
            super(new RecipeMacroMetadataUseCasePersistenceModel());
        }

        @Override
        public Builder getDefault() {
            domainModel.dataId = "";
            domainModel.domainId = "";
            domainModel.componentState = ComponentState.INVALID_UNCHANGED;
            domainModel.componentStates = new HashMap<>();
            domainModel.failReasons = new ArrayList<>();
            domainModel.createDate = 0L;
            domainModel.lastUpdate = 0L;
            return self();
        }

        @Override
        public Builder basedOnModel(RecipeMacroMetadataUseCasePersistenceModel model) {
            domainModel.dataId = model.getDataId();
            domainModel.domainId = model.getDomainId();
            domainModel.componentState = model.getComponentState();
            domainModel.componentStates = model.getComponentStates();
            domainModel.failReasons = model.getFailReasons();
            domainModel.createDate = model.getCreateDate();
            domainModel.lastUpdate = model.getLastUpdate();
            return self();
        }

        public Builder setComponentState(ComponentState state) {
            domainModel.componentState = state;
            return self();
        }

        public Builder setComponentStates(HashMap<RecipeComponentNameName, ComponentState> componentStates) {
            domainModel.componentStates = componentStates;
            return self();
        }

        public Builder setFailReasons(List<FailReasons> failReasons) {
            domainModel.failReasons = failReasons;
            return self();
        }

        @Override
        protected Builder self() {
            return this;
        }
    }
}
