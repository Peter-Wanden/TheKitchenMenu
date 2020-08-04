package com.example.peter.thekitchenmenu.domain.usecasenew.recipe.component.metadata;

import com.example.peter.thekitchenmenu.domain.usecasenew.common.metadata.ComponentState;
import com.example.peter.thekitchenmenu.domain.usecasenew.model.DomainModel;
import com.example.peter.thekitchenmenu.domain.usecasenew.recipe.component.RecipeComponentName;

import java.util.HashMap;
import java.util.Objects;

import javax.annotation.Nonnull;

public final class RecipeMetadataUseCaseModel
        implements
        DomainModel.UseCaseModel {

    @Nonnull
    private final String parentDomainId;
    @Nonnull
    private final HashMap<RecipeComponentName, ComponentState> componentStates;

    public RecipeMetadataUseCaseModel(@Nonnull String parentDomainId,
                               @Nonnull HashMap<RecipeComponentName, ComponentState> componentStates) {

        this.parentDomainId = parentDomainId;
        this.componentStates = componentStates;
    }

    @Nonnull
    public String getParentDomainId() {
        return parentDomainId;
    }

    @Nonnull
    public HashMap<RecipeComponentName, ComponentState> getComponentStates() {
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
