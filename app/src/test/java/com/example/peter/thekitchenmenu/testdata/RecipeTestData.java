package com.example.peter.thekitchenmenu.testdata;

import com.example.peter.thekitchenmenu.app.Constants;
import com.example.peter.thekitchenmenu.data.entity.RecipeEntity;

public class RecipeTestData {

    public static int getMaxPrepTime() {
        return 6000;
    }

    public static int getMaxCookTime() {
        return 6000;
    }

    // An invalid empty recipe
    public static RecipeEntity getEmptyRecipeEntity() {
        return new RecipeEntity(
                "",
                "",
                "",
                0L,
                0L);
    }

    // A valid recipe entered by the current user
    public static RecipeEntity getValidExistingRecipeEntity() {
        return new RecipeEntity(
                "id",
                "id",
                Constants.getUserId().getValue(),
                10L,
                10L
        );
    }

    public static RecipeEntity getValidRecipeEntityFromAnotherUser() {
        return new RecipeEntity(
                "id1",
                "1d1",
                "anotherUser",
                5L,
                10L
        );
    }

    public static RecipeEntity getValidClonedRecipeEntity() {
        return new RecipeEntity(
                "id2",
                getValidRecipeEntityFromAnotherUser().getId(),
                Constants.getUserId().getValue(),
                20L,
                20L
        );
    }
}
