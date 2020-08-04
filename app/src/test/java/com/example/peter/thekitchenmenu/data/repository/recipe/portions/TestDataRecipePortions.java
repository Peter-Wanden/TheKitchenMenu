package com.example.peter.thekitchenmenu.data.repository.recipe.portions;

import com.example.peter.thekitchenmenu.data.repository.recipe.metadata.TestDataRecipeMetadata;
import com.example.peter.thekitchenmenu.domain.usecasenew.recipe.component.portions.RecipePortionsUseCasePersistenceModel;

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

    public static RecipePortionsUseCasePersistenceModel getNewActiveDefault() {
        return new RecipePortionsUseCasePersistenceModel.Builder().
                setDataId("dataId-recipePortions-id0").
                setDomainId(NEW_RECIPE_ID).
                setServings(MIN_SERVINGS).
                setSittings(MIN_SITTINGS).
                setCreateDate(10L).
                setLastUpdate(10L).
                build();
    }

    public static RecipePortionsUseCasePersistenceModel getNewArchivedDefault() {
        return new RecipePortionsUseCasePersistenceModel.Builder().
                basedOnModel(getNewActiveDefault()).
                setLastUpdate(20L).
                build();
    }

    public static RecipePortionsUseCasePersistenceModel getNewValidFourPortions() {
        return new RecipePortionsUseCasePersistenceModel.Builder().
                setDataId("dataId-recipePortions-id1").
                setDomainId(NEW_RECIPE_ID).
                setServings(2).
                setSittings(2).
                setCreateDate(10L).
                setLastUpdate(20L).
                build();
    }

    public static RecipePortionsUseCasePersistenceModel getNewValidSixteenPortions() {
        return new RecipePortionsUseCasePersistenceModel.Builder().
                setDataId("dataId-recipePortions-id2").
                setDomainId(NEW_RECIPE_ID).
                setServings(4).
                setSittings(4).
                setCreateDate(10L).
                setLastUpdate(30L).
                build();
    }

    public static RecipePortionsUseCasePersistenceModel getNewInvalidServingsTooLowSittingsValid() {
        return new RecipePortionsUseCasePersistenceModel.Builder()
                .setDataId("dataId-recipePortions-id3a")
                .setDomainId(NEW_RECIPE_ID)
                .setServings(MIN_SERVINGS -1)
                .setSittings(MIN_SERVINGS)
                .setCreateDate(10L)
                .setLastUpdate(35L)
                .build();
    }

    public static RecipePortionsUseCasePersistenceModel getNewInvalidServingsTooHighSittingsTooHigh() {
        return new RecipePortionsUseCasePersistenceModel.Builder().
                setDataId("dataId-recipePortions-id3").
                setDomainId(NEW_RECIPE_ID).
                setServings(MAX_SERVINGS + 1).
                setSittings(MAX_SITTINGS + 1).
                setCreateDate(10L).
                setLastUpdate(40L).
                build();
    }

    public static RecipePortionsUseCasePersistenceModel getNewInvalidServingsTooHighSittingsValid() {
        return new RecipePortionsUseCasePersistenceModel.Builder().
                setDataId("dataId-recipePortions-id4").
                setDomainId(NEW_RECIPE_ID).
                setServings(MAX_SERVINGS + 1).
                setSittings(MIN_SITTINGS).
                setCreateDate(10L).
                setLastUpdate(50L).
                build();
    }

    public static RecipePortionsUseCasePersistenceModel getNewInvalidValidServingsValidSittingsTooLow() {
        return new RecipePortionsUseCasePersistenceModel.Builder()
                .setDataId("dataId-recipePortions-id4a")
                .setDomainId(NEW_RECIPE_ID)
                .setServings(MIN_SERVINGS)
                .setSittings(MIN_SITTINGS - 1)
                .setCreateDate(10L)
                .setLastUpdate(10L)
                .build();
    }

    public static RecipePortionsUseCasePersistenceModel getNewInvalid_validServingsInvalidSittingsTooHigh() {
        return new RecipePortionsUseCasePersistenceModel.Builder().
                setDataId("dataId-recipePortions-id5").
                setDomainId(NEW_RECIPE_ID).
                setServings(MIN_SERVINGS).
                setSittings(MAX_SITTINGS + 1).
                setCreateDate(10L).
                setLastUpdate(60L).
                build();
    }

    public static RecipePortionsUseCasePersistenceModel getNewValidServingsValidSittings() {
        return new RecipePortionsUseCasePersistenceModel.Builder().
                setDataId("dataId-recipePortions-id6").
                setDomainId(NEW_RECIPE_ID).
                setServings(MAX_SERVINGS).
                setSittings(MAX_SITTINGS).
                setCreateDate(20L).
                setLastUpdate(20L).
                build();
    }

    public static RecipePortionsUseCasePersistenceModel getNewArchivedValidServingsValidSittings() {
        return new RecipePortionsUseCasePersistenceModel.Builder()
                .basedOnModel(getNewValidServingsValidSittings())
                .setDataId("dataId-recipePortions-id7")
                .setLastUpdate(30L)
                .build();
    }

    public static List<RecipePortionsUseCasePersistenceModel> getAllNew() {
        return Arrays.asList(
                getNewActiveDefault(),
                getNewValidFourPortions(),
                getNewValidSixteenPortions(),
                getNewInvalidServingsTooHighSittingsTooHigh(),
                getNewInvalidServingsTooHighSittingsValid(),
                getNewInvalid_validServingsInvalidSittingsTooHigh(),
                getNewValidServingsValidSittings()
        );
    }

    public static RecipePortionsUseCasePersistenceModel getExistingInvalidTooHighSittingsInvalidTooHighServings() {
        return new RecipePortionsUseCasePersistenceModel.Builder().
                setDataId("dataId-recipePortions-id10").
                setDomainId(EXISTING_RECIPE_ID).
                setServings(MAX_SERVINGS + 1).
                setSittings(MAX_SITTINGS + 1).
                setCreateDate(20L).
                setLastUpdate(20L).
                build();
    }

    public static RecipePortionsUseCasePersistenceModel getExistingValidNinePortions() {
        return new RecipePortionsUseCasePersistenceModel.Builder().
                setDataId("dataId-recipePortions-id11").
                setDomainId(EXISTING_RECIPE_ID).
                setServings(3).
                setSittings(3).
                setCreateDate(20L).
                setLastUpdate(30L).
                build();
    }

    public static RecipePortionsUseCasePersistenceModel getArchivedValidNinePortions() {
        return new RecipePortionsUseCasePersistenceModel.Builder().
                basedOnModel(getExistingValidNinePortions()).
                setLastUpdate(40L).
                build();
    }

    public static RecipePortionsUseCasePersistenceModel getExistingValidUpdatedServings() {
        return new RecipePortionsUseCasePersistenceModel.Builder().
                setDataId("dataId-recipePortions-id12").
                setDomainId(EXISTING_RECIPE_ID).
                setServings(getNewValidServingsValidSittings().getServings()).
                setSittings(getExistingValidNinePortions().getSittings()).
                setCreateDate(40L).
                setLastUpdate(40L).
                build();
    }

    public static RecipePortionsUseCasePersistenceModel getExistingValidUpdatedSittings() {
        return new RecipePortionsUseCasePersistenceModel.Builder().
                setDataId("dataId-recipePortions-id13").
                setDomainId(EXISTING_RECIPE_ID).
                setServings(getExistingValidNinePortions().getServings()).
                setSittings(getNewValidServingsValidSittings().getSittings()).
                setCreateDate(20L).
                setLastUpdate(50L).
                build();
    }

    public static RecipePortionsUseCasePersistenceModel getExistingValidCopied() {
        return new RecipePortionsUseCasePersistenceModel.Builder().
                setDataId("dataId-recipePortions-id14").
                setDomainId(EXISTING_RECIPE_ID).
                setServings(getExistingValidNinePortions().getServings()).
                setSittings(getExistingValidNinePortions().getSittings()).
                setCreateDate(20L).
                setLastUpdate(60L).
                build();
    }

    public static RecipePortionsUseCasePersistenceModel getExistingCopiedUpdatedSittingsServings() {
        return new RecipePortionsUseCasePersistenceModel.Builder().
                setDataId("dataId-recipePortions-id15").
                setDomainId(EXISTING_RECIPE_ID).
                setServings(getExistingValidUpdatedServings().getServings()).
                setSittings(getExistingValidUpdatedSittings().getSittings()).
                build();
    }

    public static List<RecipePortionsUseCasePersistenceModel> getAllExisting() {
        return Arrays.asList(
                getExistingInvalidTooHighSittingsInvalidTooHighServings(),
                getExistingValidNinePortions(),
                getExistingValidUpdatedServings(),
                getExistingValidUpdatedSittings(),
                getExistingValidCopied(),
                getExistingCopiedUpdatedSittingsServings()
        );
    }

    public static RecipePortionsUseCasePersistenceModel getValidFromAnotherUser() {
        return new RecipePortionsUseCasePersistenceModel.Builder().
                setDataId("dataId-recipePortions-id20").
                setDomainId(RECIPE_ID_FROM_ANOTHER_USER).
                setServings(5).
                setSittings(5).
                setCreateDate(30L).
                setLastUpdate(30L).
                build();
    }

    public static RecipePortionsUseCasePersistenceModel getValidCopiedFromAnotherUser() {
        return new RecipePortionsUseCasePersistenceModel.Builder().
                setDataId("dataId-recipePortions-id21").
                setDomainId(RECIPE_ID_FROM_ANOTHER_USER).
                setServings(5).
                setSittings(5).
                setCreateDate(30L).
                setLastUpdate(30L).
                build();
    }

    public static List<RecipePortionsUseCasePersistenceModel> getAllFromAnotherUser() {
        return Arrays.asList(
                getValidFromAnotherUser(),
                getValidCopiedFromAnotherUser()
        );
    }

    public static List<RecipePortionsUseCasePersistenceModel> getAll() {
        List<RecipePortionsUseCasePersistenceModel> models = new ArrayList<>();
        models.addAll(getAllNew());
        models.addAll(getAllExisting());
        models.addAll(getAllFromAnotherUser());
        return models;
    }

    public static List<RecipePortionsUseCasePersistenceModel> getAllByDomainId(String domainId) {
        List<RecipePortionsUseCasePersistenceModel> models = new ArrayList<>();
        for (RecipePortionsUseCasePersistenceModel m : getAll()) {
            if (domainId.equals(m.getDomainId())) {
                models.add(m);
            }
        }
        return models;
    }

    public static RecipePortionsUseCasePersistenceModel getActiveByDomainId(String domainId) {
        long lastUpdate = 0;
        RecipePortionsUseCasePersistenceModel model = new RecipePortionsUseCasePersistenceModel.Builder().
                getDefault().build();
        for (RecipePortionsUseCasePersistenceModel m : getAllByDomainId(domainId)) {
            if (lastUpdate < m.getLastUpdate()) {
                model = m;
                lastUpdate = m.getLastUpdate();
            }
        }
        return model;
    }
}
