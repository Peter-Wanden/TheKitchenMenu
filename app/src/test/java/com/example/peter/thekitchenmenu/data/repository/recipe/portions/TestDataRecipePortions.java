package com.example.peter.thekitchenmenu.data.repository.recipe.portions;

import com.example.peter.thekitchenmenu.data.repository.recipe.metadata.TestDataRecipeMetadata;
import com.example.peter.thekitchenmenu.domain.usecase.recipe.component.portions.RecipePortionsPersistenceDomainModel;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TestDataRecipePortions {

    private static final String NEW_RECIPE_ID = TestDataRecipeMetadata.
            getInvalidDefault().
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

    public static RecipePortionsPersistenceDomainModel getNewActiveDefault() {
        return new RecipePortionsPersistenceDomainModel.Builder().
                setDataId("dataId-recipePortions-id0").
                setDomainId(NEW_RECIPE_ID).
                setServings(MIN_SERVINGS).
                setSittings(MIN_SITTINGS).
                setCreateDate(10L).
                setLastUpdate(10L).
                build();
    }

    public static RecipePortionsPersistenceDomainModel getNewArchivedDefault() {
        return new RecipePortionsPersistenceDomainModel.Builder().
                basedOnModel(getNewActiveDefault()).
                setLastUpdate(20L).
                build();
    }

    public static RecipePortionsPersistenceDomainModel getNewValidFourPortions() {
        return new RecipePortionsPersistenceDomainModel.Builder().
                setDataId("dataId-recipePortions-id1").
                setDomainId(NEW_RECIPE_ID).
                setServings(2).
                setSittings(2).
                setCreateDate(10L).
                setLastUpdate(20L).
                build();
    }

    public static RecipePortionsPersistenceDomainModel getNewValidSixteenPortions() {
        return new RecipePortionsPersistenceDomainModel.Builder().
                setDataId("dataId-recipePortions-id2").
                setDomainId(NEW_RECIPE_ID).
                setServings(4).
                setSittings(4).
                setCreateDate(10L).
                setLastUpdate(30L).
                build();
    }

    public static RecipePortionsPersistenceDomainModel getNewInvalidTooHighServingsInvalidTooHighSittings() {
        return new RecipePortionsPersistenceDomainModel.Builder().
                setDataId("dataId-recipePortions-id3").
                setDomainId(NEW_RECIPE_ID).
                setServings(MAX_SERVINGS + 1).
                setSittings(MAX_SITTINGS + 1).
                setCreateDate(10L).
                setLastUpdate(40L).
                build();
    }

    public static RecipePortionsPersistenceDomainModel getNewInvalidTooHighServingsValidSittings() {
        return new RecipePortionsPersistenceDomainModel.Builder().
                setDataId("dataId-recipePortions-id4").
                setDomainId(NEW_RECIPE_ID).
                setServings(MAX_SERVINGS + 1).
                setSittings(MIN_SITTINGS).
                setCreateDate(10L).
                setLastUpdate(50L).
                build();
    }

    public static RecipePortionsPersistenceDomainModel getNewValidServingsInvalidTooHighSittings() {
        return new RecipePortionsPersistenceDomainModel.Builder().
                setDataId("dataId-recipePortions-id5").
                setDomainId(NEW_RECIPE_ID).
                setServings(MIN_SERVINGS).
                setSittings(MAX_SITTINGS + 1).
                setCreateDate(10L).
                setLastUpdate(60L).
                build();
    }

    public static RecipePortionsPersistenceDomainModel getNewValidServingsValidSittings() {
        return new RecipePortionsPersistenceDomainModel.Builder().
                setDataId("data-recipePortions-id6").
                setDomainId(NEW_RECIPE_ID).
                setServings(MAX_SERVINGS).
                setSittings(MAX_SITTINGS).
                setCreateDate(20L).
                setLastUpdate(20L).
                build();
    }

    public static List<RecipePortionsPersistenceDomainModel> getAllNew() {
        return Arrays.asList(
                getNewActiveDefault(),
                getNewValidFourPortions(),
                getNewValidSixteenPortions(),
                getNewInvalidTooHighServingsInvalidTooHighSittings(),
                getNewInvalidTooHighServingsValidSittings(),
                getNewValidServingsInvalidTooHighSittings(),
                getNewValidServingsValidSittings()
        );
    }

    public static RecipePortionsPersistenceDomainModel getExistingInvalidTooHighSittingsInvalidTooHighServings() {
        return new RecipePortionsPersistenceDomainModel.Builder().
                setDataId("dataId-recipePortions-id10").
                setDomainId(EXISTING_RECIPE_ID).
                setServings(MAX_SERVINGS + 1).
                setSittings(MAX_SITTINGS + 1).
                setCreateDate(20L).
                setLastUpdate(20L).
                build();
    }

    public static RecipePortionsPersistenceDomainModel getExistingValidNinePortions() {
        return new RecipePortionsPersistenceDomainModel.Builder().
                setDataId("dataId-recipePortions-id11").
                setDomainId(EXISTING_RECIPE_ID).
                setServings(3).
                setSittings(3).
                setCreateDate(20L).
                setLastUpdate(30L).
                build();
    }

    public static RecipePortionsPersistenceDomainModel getArchivedValidNinePortions() {
        return new RecipePortionsPersistenceDomainModel.Builder().
                basedOnModel(getExistingValidNinePortions()).
                setLastUpdate(40L).
                build();
    }

    public static RecipePortionsPersistenceDomainModel getExistingValidUpdatedServings() {
        return new RecipePortionsPersistenceDomainModel.Builder().
                setDataId("dataId-recipePortions-id12").
                setDomainId(EXISTING_RECIPE_ID).
                setServings(getNewValidServingsValidSittings().getServings()).
                setSittings(getExistingValidNinePortions().getSittings()).
                setCreateDate(40L).
                setLastUpdate(40L).
                build();
    }

    public static RecipePortionsPersistenceDomainModel getExistingValidUpdatedSittings() {
        return new RecipePortionsPersistenceDomainModel.Builder().
                setDataId("dataId-recipePortions-id13").
                setDomainId(EXISTING_RECIPE_ID).
                setServings(getExistingValidNinePortions().getServings()).
                setSittings(getNewValidServingsValidSittings().getSittings()).
                setCreateDate(20L).
                setLastUpdate(50L).
                build();
    }

    public static RecipePortionsPersistenceDomainModel getExistingValidCopied() {
        return new RecipePortionsPersistenceDomainModel.Builder().
                setDataId("dataId-recipePortions-id14").
                setDomainId(EXISTING_RECIPE_ID).
                setServings(getExistingValidNinePortions().getServings()).
                setSittings(getExistingValidNinePortions().getSittings()).
                setCreateDate(20L).
                setLastUpdate(60L).
                build();
    }

    public static RecipePortionsPersistenceDomainModel getExistingCopiedUpdatedSittingsServings() {
        return new RecipePortionsPersistenceDomainModel.Builder().
                setDataId("dataId-recipePortions-id15").
                setDomainId(EXISTING_RECIPE_ID).
                setServings(getExistingValidUpdatedServings().getServings()).
                setSittings(getExistingValidUpdatedSittings().getSittings()).
                build();
    }

    public static List<RecipePortionsPersistenceDomainModel> getAllExisting() {
        return Arrays.asList(
                getExistingInvalidTooHighSittingsInvalidTooHighServings(),
                getExistingValidNinePortions(),
                getExistingValidUpdatedServings(),
                getExistingValidUpdatedSittings(),
                getExistingValidCopied(),
                getExistingCopiedUpdatedSittingsServings()
        );
    }

    public static RecipePortionsPersistenceDomainModel getValidFromAnotherUser() {
        return new RecipePortionsPersistenceDomainModel.Builder().
                setDataId("dataId-recipePortions-id20").
                setDomainId(RECIPE_ID_FROM_ANOTHER_USER).
                setServings(5).
                setSittings(5).
                setCreateDate(30L).
                setLastUpdate(30L).
                build();
    }

    public static RecipePortionsPersistenceDomainModel getValidCopiedFromAnotherUser() {
        return new RecipePortionsPersistenceDomainModel.Builder().
                setDataId("dataId-recipePortions-id21").
                setDomainId(RECIPE_ID_FROM_ANOTHER_USER).
                setServings(5).
                setSittings(5).
                setCreateDate(30L).
                setLastUpdate(30L).
                build();
    }

    public static List<RecipePortionsPersistenceDomainModel> getAllFromAnotherUser() {
        return Arrays.asList(
                getValidFromAnotherUser(),
                getValidCopiedFromAnotherUser()
        );
    }

    public static List<RecipePortionsPersistenceDomainModel> getAll() {
        List<RecipePortionsPersistenceDomainModel> models = new ArrayList<>();
        models.addAll(getAllNew());
        models.addAll(getAllExisting());
        models.addAll(getAllFromAnotherUser());
        return models;
    }

    public static List<RecipePortionsPersistenceDomainModel> getAllByDomainId(String domainId) {
        List<RecipePortionsPersistenceDomainModel> models = new ArrayList<>();
        for (RecipePortionsPersistenceDomainModel m : getAll()) {
            if (domainId.equals(m.getDomainId())) {
                models.add(m);
            }
        }
        return models;
    }

    public static RecipePortionsPersistenceDomainModel getActiveByDomainId(String domainId) {
        long lastUpdate = 0;
        RecipePortionsPersistenceDomainModel model = new RecipePortionsPersistenceDomainModel.Builder().
                getDefault().build();
        for (RecipePortionsPersistenceDomainModel m : getAllByDomainId(domainId)) {
            if (lastUpdate < m.getLastUpdate()) {
                model = m;
                lastUpdate = m.getLastUpdate();
            }
        }
        return model;
    }
}
