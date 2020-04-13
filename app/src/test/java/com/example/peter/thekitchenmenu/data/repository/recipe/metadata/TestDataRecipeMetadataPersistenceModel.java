package com.example.peter.thekitchenmenu.data.repository.recipe.metadata;

import com.example.peter.thekitchenmenu.app.Constants;
import com.example.peter.thekitchenmenu.domain.model.CommonFailReason;
import com.example.peter.thekitchenmenu.domain.model.FailReasons;
import com.example.peter.thekitchenmenu.domain.usecase.recipe.metadata.RecipeMetadataPersistenceModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static com.example.peter.thekitchenmenu.domain.usecase.recipe.metadata.RecipeMetadata.*;

public class TestDataRecipeMetadataPersistenceModel {

    public static RecipeMetadataPersistenceModel getNewInvalid() {
        return new RecipeMetadataPersistenceModel.Builder().
                setDataId("recipeMetadata0").
                setDomainId("recipeId0").
                setRecipeParentId("").
                setRecipeState(RecipeState.DATA_UNAVAILABLE).
                setFailReasons(getFailReasonsForNewInvalid()).
                setComponentStates(getComponentStatesForNewInvalid()).
                setCreatedBy(Constants.getUserId()).
                setCreateDate(0L).
                setLastUpdate(0L).
                build();
    }

    private static List<FailReasons> getFailReasonsForNewInvalid() {
        List<FailReasons> failReasons = new ArrayList<>();
        failReasons.add(CommonFailReason.DATA_UNAVAILABLE);
        return failReasons;
    }

    private static HashMap<ComponentName, ComponentState> getComponentStatesForNewInvalid() {
        HashMap<ComponentName, ComponentState> componentStates = new HashMap<>();
        componentStates.put(ComponentName.COURSE, ComponentState.DATA_UNAVAILABLE);
        componentStates.put(ComponentName.DURATION, ComponentState.DATA_UNAVAILABLE);
        componentStates.put(ComponentName.IDENTITY, ComponentState.DATA_UNAVAILABLE);
        componentStates.put(ComponentName.PORTIONS, ComponentState.DATA_UNAVAILABLE);
        return componentStates;
    }
}
