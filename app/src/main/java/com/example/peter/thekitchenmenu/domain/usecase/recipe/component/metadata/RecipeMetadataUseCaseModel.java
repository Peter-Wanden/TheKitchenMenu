package com.example.peter.thekitchenmenu.domain.usecase.recipe.component.metadata;

import com.example.peter.thekitchenmenu.domain.usecasenew.common.metadata.ComponentState;
import com.example.peter.thekitchenmenu.domain.usecasenew.common.model.DomainModel;
import com.example.peter.thekitchenmenu.domain.usecasenew.recipe.component.RecipeComponentNameName;

import java.util.HashMap;

import javax.annotation.Nonnull;

public class RecipeMetadataUseCaseModel
        implements
        DomainModel.UseCaseModel {

    @Nonnull
    private final String parentDomainId;
    @Nonnull
    private final HashMap<RecipeComponentNameName, ComponentState> componentStates;

    public RecipeMetadataUseCaseModel(@Nonnull String parentDomainId,
                                      @Nonnull HashMap<RecipeComponentNameName, ComponentState> componentStates) {

        this.parentDomainId = parentDomainId;
        this.componentStates = componentStates;
    }

    @Nonnull
    public String getParentDomainId() {
        return parentDomainId;
    }

    @Nonnull
    public HashMap<RecipeComponentNameName, ComponentState> getComponentStates() {
        return componentStates;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof RecipeMetadataUseCaseModel)) return false;

        RecipeMetadataUseCaseModel that = (RecipeMetadataUseCaseModel) o;

        if (!parentDomainId.equals(that.parentDomainId)) return false;
        return componentStates.equals(that.componentStates);
    }

    @Override
    public int hashCode() {
        int result = parentDomainId.hashCode();
        result = 31 * result + componentStates.hashCode();
        return result;
    }

    @Nonnull
    @Override
    public String toString() {
        return "RecipeMetadataUseCaseModel{" +
                "parentDomainId='" + parentDomainId + '\'' +
                ", componentStates=" + componentStates +
                '}';
    }


}