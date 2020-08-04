package com.example.peter.thekitchenmenu.data.repository.source.local.recipe.metadata.dataadapter;

import com.example.peter.thekitchenmenu.data.repository.source.local.recipe.metadata.datasource.componentstate.RecipeComponentStateEntity;
import com.example.peter.thekitchenmenu.domain.usecasenew.recipe.component.RecipeComponentName;
import com.example.peter.thekitchenmenu.domain.usecasenew.common.metadata.ComponentState;
import com.example.peter.thekitchenmenu.domain.utils.UniqueIdProvider;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.annotation.Nonnull;

public class ComponentStateConverter {

    @Nonnull
    private final UniqueIdProvider idProvider;

    public ComponentStateConverter() {
        this.idProvider = new UniqueIdProvider();
    }

    public List<RecipeComponentStateEntity> convertToEntities(
            @Nonnull HashMap<RecipeComponentName, ComponentState> componentStates,
            String parentId) {

        List<RecipeComponentStateEntity> entities = new ArrayList<>();

        for (RecipeComponentName componentName : componentStates.keySet()) {
            String dataId = idProvider.getUId();
            int componentNameId = componentName.getId();
            int componentStateId = componentStates.get(componentName).id();

            entities.add(
                    new RecipeComponentStateEntity(
                            dataId,
                            parentId,
                            componentNameId,
                            componentStateId
                    )
            );
        }
        return entities;
    }
}
