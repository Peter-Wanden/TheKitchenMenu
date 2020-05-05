package com.example.peter.thekitchenmenu.data.repository.recipe.duration;

import com.example.peter.thekitchenmenu.data.repository.recipe.metadata.TestDataRecipeMetadata;
import com.example.peter.thekitchenmenu.domain.usecase.recipe.component.duration.RecipeDurationPersistenceModel;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TestDataRecipeDuration {

    public static final int MIN_PREP_TIME = 0;
    public static final int MAX_PREP_TIME = 6000;
    public static final int MIN_COOK_TIME = 0;
    public static final int MAX_COOK_TIME = 6000;

    public static final String NEW_RECIPE_ID = TestDataRecipeMetadata.
            getDataUnavailable().
            getDomainId();

    public static final String EXISTING_RECIPE_ID = TestDataRecipeMetadata.
            getValidUnchanged().
            getDomainId();

    public static final String RECIPE_ID_FROM_ANOTHER_USER = TestDataRecipeMetadata.
            getValidFromAnotherUser().getDomainId();

    public static RecipeDurationPersistenceModel getValidNew() {
        return new RecipeDurationPersistenceModel.Builder().
                setDataId("dataId-recipeDuration-new-id0").
                setDomainId(NEW_RECIPE_ID).
                setPrepTime(MIN_PREP_TIME).
                setCookTime(MIN_COOK_TIME).
                setCreateDate(10L).
                setLastUpdate(20L).
                build();
    }

    public static RecipeDurationPersistenceModel getInvalidNewPrepTimeInvalid() {
        return new RecipeDurationPersistenceModel.Builder().
                setDataId("dataId-recipeDuration-new-id1").
                setDomainId(NEW_RECIPE_ID).
                setPrepTime(MAX_PREP_TIME + 1).
                setCookTime(MIN_COOK_TIME).
                setCreateDate(10L).
                setLastUpdate(30L).
                build();
    }

    public static RecipeDurationPersistenceModel getInvalidNewCookTimeInvalid() {
        return new RecipeDurationPersistenceModel.Builder().
                setDataId("dataId-recipeDuration-new-id2").
                setDomainId(NEW_RECIPE_ID).
                setPrepTime(MIN_PREP_TIME).
                setCookTime(MAX_COOK_TIME + 1).
                setCreateDate(10L).
                setLastUpdate(40L).
                build();
    }

    public static RecipeDurationPersistenceModel getValidNewPrepTimeValid() {
        return new RecipeDurationPersistenceModel.Builder().
                setDataId("dataId-recipeDuration-new-id3").
                setDomainId(NEW_RECIPE_ID).
                setPrepTime(MAX_PREP_TIME).
                setCookTime(MIN_COOK_TIME).
                setCreateDate(10L).
                setLastUpdate(50L).
                build();
    }

    public static RecipeDurationPersistenceModel getValidNewCookTimeValid() {
        return new RecipeDurationPersistenceModel.Builder().
                setDataId("dataId-recipeDuration-new-id4").
                setDomainId(NEW_RECIPE_ID).
                setPrepTime(MIN_PREP_TIME).
                setCookTime(MAX_COOK_TIME).
                setCreateDate(10L).
                setLastUpdate(60L).
                build();
    }

    public static RecipeDurationPersistenceModel getValidNewComplete() {
        return new RecipeDurationPersistenceModel.Builder().
                setDataId("dataId-recipeDuration-new-id5").
                setDomainId(NEW_RECIPE_ID).
                setPrepTime(MAX_PREP_TIME).
                setCookTime(MAX_COOK_TIME).
                setCreateDate(10L).
                setLastUpdate(70L).
                build();
    }

    public static List<RecipeDurationPersistenceModel> getAllNew() {
        return Arrays.asList(
                getValidNew(),
                getInvalidNewPrepTimeInvalid(),
                getInvalidNewCookTimeInvalid(),
                getValidNewPrepTimeValid(),
                getValidNewCookTimeValid(),
                getValidNewComplete()
                );
    }

    public static RecipeDurationPersistenceModel getInvalidExistingComplete() {
        return new RecipeDurationPersistenceModel.Builder().
                setDataId("dataId-recipeDuration-existing-id0").
                setDomainId(EXISTING_RECIPE_ID).
                setPrepTime(MAX_PREP_TIME + 1).
                setCookTime(MAX_COOK_TIME + 1).
                setCreateDate(20L).
                setLastUpdate(20L).
                build();
    }

    public static RecipeDurationPersistenceModel getValidExistingComplete() {
        return new RecipeDurationPersistenceModel.Builder().
                setDataId("dataId-recipeDuration-existing-id1").
                setDomainId(EXISTING_RECIPE_ID).
                setPrepTime(MAX_PREP_TIME).
                setCookTime(MAX_COOK_TIME).
                setCreateDate(20L).
                setLastUpdate(20L).
                build();
    }

    public static List<RecipeDurationPersistenceModel> getAllExisting() {
        return Arrays.asList(
                getInvalidExistingComplete(),
                getValidExistingComplete()
        );
    }

    public static RecipeDurationPersistenceModel getValidCompleteFromAnotherUser() {
        return new RecipeDurationPersistenceModel.Builder().
                setDataId("dataId-recipeDuration-anotherUser-id0").
                setDomainId(RECIPE_ID_FROM_ANOTHER_USER).
                setPrepTime(MAX_PREP_TIME).
                setCookTime(MAX_COOK_TIME).
                setCreateDate(30L).
                setLastUpdate(30L).
                build();
    }

    public static RecipeDurationPersistenceModel getInvalidCompleteFromAnotherUser() {
        return new RecipeDurationPersistenceModel.Builder().
                setDataId("dataId-recipeDuration-anotherUser-id1").
                setDomainId(RECIPE_ID_FROM_ANOTHER_USER).
                setPrepTime(MAX_PREP_TIME + 1).
                setCookTime(MAX_COOK_TIME + 1).
                setCreateDate(30L).
                setLastUpdate(30L).
                build();
    }

    public static List<RecipeDurationPersistenceModel> getAllFromAnotherUser() {
        return Arrays.asList(
                getValidCompleteFromAnotherUser(),
                getInvalidCompleteFromAnotherUser()
        );
    }

    public static RecipeDurationPersistenceModel getValidNewCopied() {
        return new RecipeDurationPersistenceModel.Builder().
                setDataId("dataId-recipeDuration-copied-id0").
                setDomainId(TestDataRecipeMetadata.getValidCopied().getDomainId()).
                setPrepTime(getValidCompleteFromAnotherUser().getPrepTime()).
                setCookTime(getValidCompleteFromAnotherUser().getCookTime()).
                setCreateDate(40L).
                setLastUpdate(40L).
                build();
    }

    public static RecipeDurationPersistenceModel getInvalidNewCopied() {
        return new RecipeDurationPersistenceModel.Builder().
                setDataId("dataId-recipeDuration-copied-id1").
                setDomainId(TestDataRecipeMetadata.getInvalidCopied().getDomainId()).
                setPrepTime(getInvalidCompleteFromAnotherUser().getPrepTime()).
                setCookTime(getInvalidCompleteFromAnotherUser().getCookTime()).
                setCreateDate(40L).
                setLastUpdate(40L).
                build();
    }

    public static RecipeDurationPersistenceModel getValidNewCopiedPrepTimeUpdated() {
        return new RecipeDurationPersistenceModel.Builder().
                setDataId("dataId-recipeDuration-copied-id2").
                setDomainId(getInvalidNewCopied().getDomainId()).
                setPrepTime(MAX_PREP_TIME / 2).
                setCookTime(getValidCompleteFromAnotherUser().getCookTime()).
                setCreateDate(getInvalidNewCopied().getCreateDate()).
                setLastUpdate(50L).
                build();
    }

    public static List<RecipeDurationPersistenceModel> getAllCopied() {
        return Arrays.asList(
                getValidNewCopied(),
                getInvalidNewCopied(),
                getValidNewCopiedPrepTimeUpdated()
        );
    }

    public static List<RecipeDurationPersistenceModel> getAll() {
        List<RecipeDurationPersistenceModel> models = new ArrayList<>();
        models.addAll(getAllNew());
        models.addAll(getAllExisting());
        models.addAll(getAllFromAnotherUser());
        models.addAll(getAllCopied());
        return models;
    }

    public static RecipeDurationPersistenceModel getActiveByDomainId(String domainId) {
        long lastUpdate = 0;
        RecipeDurationPersistenceModel model = new RecipeDurationPersistenceModel.Builder().
                getDefault().build();
        for (RecipeDurationPersistenceModel m : getAll()) {
            if (lastUpdate < m.getLastUpdate()) {
                model = m;
                lastUpdate = m.getLastUpdate();
            }
        }
        return model;
    }
}
