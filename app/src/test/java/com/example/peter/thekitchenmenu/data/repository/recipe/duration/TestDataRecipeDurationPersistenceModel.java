package com.example.peter.thekitchenmenu.data.repository.recipe.duration;

import com.example.peter.thekitchenmenu.data.repository.recipe.metadata.TestDataRecipeMetadata;
import com.example.peter.thekitchenmenu.domain.usecase.recipe.component.duration.RecipeDurationPersistenceModel;

public class TestDataRecipeDurationPersistenceModel {

    public static final String NEW_RECIPE_ID =
            TestDataRecipeMetadata.getDataUnavailable().getDomainId();

    public static final String EXISTING_RECIPE_ID =
            TestDataRecipeMetadata.getValidUnchanged().getDomainId();

    public static int getMaxPrepTime() {
        return 6000;
    }

    public static int getMaxCookTime() {
        return 6000;
    }

    public static RecipeDurationPersistenceModel getValidNewEmpty() {
        return new RecipeDurationPersistenceModel.Builder().
                setDataId("dataId-recipeDurationNew-id0").
                setDomainId(NEW_RECIPE_ID).
                setPrepTime(0).
                setCookTime(0).
                setCreateDate(TestDataRecipeMetadata.
                        getDataUnavailable().
                        getCreateDate()).
                setLastUpdate(TestDataRecipeMetadata.
                        getDataUnavailable().
                        getLastUpdate()).
                build();
    }
}
