package com.example.peter.thekitchenmenu.data.repository.recipe.portions;

import com.example.peter.thekitchenmenu.data.repository.recipe.metadata.TestDataRecipeMetadata;
import com.example.peter.thekitchenmenu.domain.usecase.recipe.component.portions.RecipePortionsPersistenceModel;

public class TestDataRecipePortions {

    private static final String NEW_RECIPE_ID = TestDataRecipeMetadata.
            getDataUnavailable().
            getDomainId();

    private static final String EXISTING_RECIPE_ID = TestDataRecipeMetadata.
            getValidUnchanged().
            getDomainId();

    private static final String RECIPE_ID_FROM_ANOTHER_USER = TestDataRecipeMetadata.
            getValidFromAnotherUser().getDomainId();

    public static final int MIN_SERVINGS = 1;
    public static final int MAX_SERVINGS = 10;
    public static final int MIN_SITTINGS = 1;
    public static final int MAX_SITTINGS = 10;

    public static RecipePortionsPersistenceModel getNewValidEmpty() {
        return new RecipePortionsPersistenceModel.Builder().
                setDataId("dataId-recipePortions-id0").
                setDomainId(NEW_RECIPE_ID).
                setServings(MIN_SERVINGS).
                setSittings(MIN_SITTINGS).
                setCreateDate(10L).
                setLastUpdate(10L).
                build();
    }

    public static RecipePortionsPersistenceModel getNewValidFourPortions() {
        return new RecipePortionsPersistenceModel.Builder().
                setDataId("dataId-recipePortions-id1").
                setDomainId(NEW_RECIPE_ID).
                setServings(2).
                setSittings(2).
                setCreateDate(10L).
                setLastUpdate(20L).
                build();
    }

    public static RecipePortionsPersistenceModel getNewValidSixteenPortions() {
        return new RecipePortionsPersistenceModel.Builder().
                setDataId("dataId-recipePortions-id2").
                setDomainId(NEW_RECIPE_ID).
                setServings(4).
                setSittings(4).
                setCreateDate(10L).
                setLastUpdate(30L).
                build();
    }

    public static RecipePortionsPersistenceModel getNewInvalidTooHighServingsInvalidTooHighSittings() {
        return new RecipePortionsPersistenceModel.Builder().
                setDataId("dataId-recipePortions-id3").
                setDomainId(NEW_RECIPE_ID).
                setServings(MAX_SERVINGS + 1).
                setSittings(MAX_SITTINGS + 1).
                setCreateDate(10L).
                setLastUpdate(40L).
                build();
    }

    public static RecipePortionsPersistenceModel getNewInvalidTooHighServingsValidSittings() {
        return new RecipePortionsPersistenceModel.Builder().
                setDataId("dataId-recipePortions-id4").
                setDomainId(NEW_RECIPE_ID).
                setServings(MAX_SERVINGS + 1).
                setSittings(MIN_SITTINGS).
                setCreateDate(10L).
                setLastUpdate(50L).
                build();
    }

    public static RecipePortionsPersistenceModel getNewValidServingsInvalidTooHighSittings() {
        return new RecipePortionsPersistenceModel.Builder().
                setDataId("dataId-recipePortions-id5").
                setDomainId(NEW_RECIPE_ID).
                setServings(MIN_SERVINGS).
                setSittings(MAX_SERVINGS + 1).
                setCreateDate(10L).
                setLastUpdate(60L).
                build();
    }

    public static RecipePortionsPersistenceModel getNewValidServingsValidSittings() {
        return new RecipePortionsPersistenceModel.Builder().
                setDataId("data-recipePortions-id6").
                setDomainId(NEW_RECIPE_ID).
                setServings(MAX_SERVINGS).
                setSittings(MAX_SITTINGS).
                setCreateDate(10L).
                setLastUpdate(70L).
                build();
    }

    public static RecipePortionsPersistenceModel getExistingInvalidTooHighSittingsInvalidTooHighServings() {
        return new RecipePortionsPersistenceModel.Builder().
                setDataId("dataId-recipePortions-id10").
                setDomainId(EXISTING_RECIPE_ID).
                setServings(MAX_SERVINGS + 1).
                setSittings(MAX_SITTINGS + 1).
                setCreateDate(20L).
                setLastUpdate(20L).
                build();
    }

    public static RecipePortionsPersistenceModel getExistingValidNinePortions() {
        return new RecipePortionsPersistenceModel.Builder().
                setDataId("dataId-recipePortions-id11").
                setDomainId(EXISTING_RECIPE_ID).
                setServings(3).
                setSittings(3).
                setCreateDate(20L).
                setLastUpdate(30L).
                build();
    }

    public static RecipePortionsPersistenceModel getExistingValidUpdatedServings() {
        return new RecipePortionsPersistenceModel.Builder().
                setDataId("dataId-recipePortions-id12").
                setDomainId(EXISTING_RECIPE_ID).
                setServings(getNewValidServingsValidSittings().getServings()).
                setSittings(getExistingValidNinePortions().getSittings()).
                setCreateDate(20L).
                setLastUpdate(40L).
                build();
    }

    public static RecipePortionsPersistenceModel getExistingValidUpdatedSittings() {
        return new RecipePortionsPersistenceModel.Builder().
                setDataId("dataId-recipePortions-id13").
                setDomainId(EXISTING_RECIPE_ID).
                setServings(getExistingValidNinePortions().getServings()).
                setSittings(getNewValidServingsValidSittings().getSittings()).
                setCreateDate(20L).
                setLastUpdate(50L).
                build();
    }

    public static RecipePortionsPersistenceModel getExistingValidCopied() {
        return new RecipePortionsPersistenceModel.Builder().
                setDataId("dataId-recipePortions-id14").
                setDomainId(EXISTING_RECIPE_ID).
                setServings(getExistingValidNinePortions().getServings()).
                setSittings(getExistingValidNinePortions().getSittings()).
                setCreateDate(20L).
                setLastUpdate(60L).
                build();
    }

    public static RecipePortionsPersistenceModel getExistingCopiedUpdatedSittingsServings() {
        return new RecipePortionsPersistenceModel.Builder().
                setDataId("dataId-recipePortions-id15").
                setDomainId(EXISTING_RECIPE_ID).
                setServings(getExistingValidUpdatedServings().getServings()).
                setSittings(getExistingValidUpdatedSittings().getSittings()).
                build();
    }

    public static RecipePortionsPersistenceModel getValidFromAnotherUser() {
        return new RecipePortionsPersistenceModel.Builder().
                setDataId("dataId-recipePortions-id20").
                setDomainId(RECIPE_ID_FROM_ANOTHER_USER).
                setServings(5).
                setSittings(5).
                setCreateDate(30L).
                setLastUpdate(30L).
                build();
    }

    public static RecipePortionsPersistenceModel getValidCopiedFromAnotherUser() {
        return new RecipePortionsPersistenceModel.Builder().
                setDataId("dataId-recipePortions-id21").
                setDomainId(RECIPE_ID_FROM_ANOTHER_USER).
                setServings(5).
                setSittings(5).
                setCreateDate(30L).
                setLastUpdate(30L).
                build();
    }
}
