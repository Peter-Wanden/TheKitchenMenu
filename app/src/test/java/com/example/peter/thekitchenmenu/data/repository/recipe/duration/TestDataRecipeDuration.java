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

    public static final String NEW_RECIPE_DOMAIN_ID = TestDataRecipeMetadata.
            getInvalidDefault().
            getDomainId();

    public static final String EXISTING_RECIPE_DOMAIN_ID = TestDataRecipeMetadata.
            getValidUnchanged().
            getDomainId();

    public static final String RECIPE_ID_FROM_ANOTHER_USER = TestDataRecipeMetadata.
            getValidFromAnotherUser().getDomainId();

    // region Persistence models for testing adding and removing data elements
    // Valid MIN_PREP and MIN_COOK times are added as the default when no values have been entered
    public static RecipeDurationPersistenceModel getNewActiveDefault() {
        return new RecipeDurationPersistenceModel.Builder().
                setDataId("dataId-recipeDuration-new-id0").
                setDomainId(NEW_RECIPE_DOMAIN_ID).
                setPrepTime(MIN_PREP_TIME).
                setCookTime(MIN_COOK_TIME).
                setCreateDate(10L).
                setLastUpdate(10L).
                build();
    }

    // When domain data state changes to a new valid value, the previous persisted state is archived
    public static RecipeDurationPersistenceModel getNewArchivedDefault() {
        return new RecipeDurationPersistenceModel.Builder().
                basedOnModel(getNewActiveDefault()).
                setLastUpdate(20L). // updated to current time
                build();
    }

    // User enters invalid prep hours. This is an invalid state and should not be persisted
    public static RecipeDurationPersistenceModel getNewInvalidPrepHours() {
        return new RecipeDurationPersistenceModel.Builder().
                setDataId("dataId-recipeDuration-new-id1").
                setDomainId(NEW_RECIPE_DOMAIN_ID).
                setPrepTime(MAX_PREP_TIME + 60).
                setCookTime(MIN_COOK_TIME).
                setCreateDate(0L). // no date as invalid state, so not persisted
                setLastUpdate(0L).
                build();
    }

    // User enters invalid prep minutes. This is an invalid state and should not be persisted
    public static RecipeDurationPersistenceModel getNewInvalidPrepMinutes() {
        return new RecipeDurationPersistenceModel.Builder().
                setDataId("dataId-recipeDuration-new-id1").
                setDomainId(NEW_RECIPE_DOMAIN_ID).
                setPrepTime(MAX_PREP_TIME + 1).
                setCookTime(MIN_COOK_TIME).
                setCreateDate(0L). // no date as invalid state, so not persisted
                setLastUpdate(0L).
                build();
    }

    // User enters an invalid cook hours. This is an invalid state and should not be persisted
    public static RecipeDurationPersistenceModel getNewInvalidCookHours() {
        return new RecipeDurationPersistenceModel.Builder().
                setDataId("dataId-recipeDuration-new-id2").
                setDomainId(NEW_RECIPE_DOMAIN_ID).
                setPrepTime(MIN_PREP_TIME).
                setCookTime(MAX_COOK_TIME + 60).
                setCreateDate(0L). // no date as invalid state, so not persisted
                setLastUpdate(0L).
                build();
    }

    // User enters an invalid cook minutes. This is an invalid state and should not be persisted
    public static RecipeDurationPersistenceModel getInvalidNewCookMinutes() {
        return new RecipeDurationPersistenceModel.Builder().
                setDataId("dataId-recipeDuration-new-id2").
                setDomainId(NEW_RECIPE_DOMAIN_ID).
                setPrepTime(MIN_PREP_TIME).
                setCookTime(MAX_COOK_TIME + 1).
                setCreateDate(0L). // no date as invalid state, so not persisted
                setLastUpdate(0L).
                build();
    }

    // User enters a valid prep time, domain model is persisted
    public static RecipeDurationPersistenceModel getNewValidPrepTime() {
        return new RecipeDurationPersistenceModel.Builder().
                setDataId("dataId-recipeDuration-new-id3").
                setDomainId(NEW_RECIPE_DOMAIN_ID).
                setPrepTime(MAX_PREP_TIME).
                setCookTime(MIN_COOK_TIME).
                setCreateDate(20L). // updated to current time
                setLastUpdate(20L).
                build();
    }

    // When domain data state changes to a new valid value, the previous persisted state is archived
    public static RecipeDurationPersistenceModel getNewValidArchivedPrepTimeValid() {
        return new RecipeDurationPersistenceModel.Builder().
                basedOnModel(getNewValidPrepTime()).
                setLastUpdate(30L).
                build();
    }

    // User enters a valid cook time, domain model is persisted
    public static RecipeDurationPersistenceModel getNewValidCookTime() {
        return new RecipeDurationPersistenceModel.Builder().
                setDataId("dataId-recipeDuration-new-id4").
                setDomainId(NEW_RECIPE_DOMAIN_ID).
                setPrepTime(MIN_PREP_TIME).
                setCookTime(MAX_COOK_TIME).
                setCreateDate(30L). // updated to current time
                setLastUpdate(30L).
                build();
    }

    // When domain data state changes to a new valid value, the previous persisted state is archived
    public static RecipeDurationPersistenceModel getNewValidArchivedCookTimeValid() {
        return new RecipeDurationPersistenceModel.Builder().
                basedOnModel(getNewValidCookTime()).
                setLastUpdate(30L).
                build();
    }

    // User enters a valid prep and cook time, domain model is persisted
    public static RecipeDurationPersistenceModel getNewValidPrepTimeValidCookTime() {
        return new RecipeDurationPersistenceModel.Builder().
                setDataId("dataId-recipeDuration-new-id5").
                setDomainId(NEW_RECIPE_DOMAIN_ID).
                setPrepTime(MAX_PREP_TIME).
                setCookTime(MAX_COOK_TIME).
                setCreateDate(30L).
                setLastUpdate(30L).
                build();
    }

    // When domain data state changes to a new valid value, the previous persisted state is archived
    public static RecipeDurationPersistenceModel getNewArchivedPrepTimeValidCookTimeValid() {
        return new RecipeDurationPersistenceModel.Builder().
                basedOnModel(getNewValidPrepTimeValidCookTime()).
                setLastUpdate(40).
                build();
    }

    // user enters invalid prep time and invalid cook time/ . This is an invalid state and should
    // not be persisted
    public static RecipeDurationPersistenceModel getNewInvalidPrepTimeInvalidCookTime() {
        return new RecipeDurationPersistenceModel.Builder().
                setDataId("dataId-recipeDuration-new-id6").
                setDomainId(NEW_RECIPE_DOMAIN_ID).
                setPrepTime(MAX_PREP_TIME + 1).
                setCookTime(MAX_COOK_TIME + 1).
                setCreateDate(50).setLastUpdate(50).
                build();
    }

    public static List<RecipeDurationPersistenceModel> getAllNew() {
        return Arrays.asList(
                getNewActiveDefault(),
                getNewInvalidPrepMinutes(),
                getInvalidNewCookMinutes(),
                getNewValidPrepTime(),
                getNewValidCookTime(),
                getNewValidPrepTimeValidCookTime()
                );
    }
    // endregion Persistence models for testing adding and removing data elements

    // region Persistence models for testing existing loaded domain models
    public static RecipeDurationPersistenceModel getExistingValidPrepTimeValidCookTime() {
        return new RecipeDurationPersistenceModel.Builder().
                setDataId("dataId-recipeDuration-existing-id1").
                setDomainId(EXISTING_RECIPE_DOMAIN_ID).
                setPrepTime(MAX_PREP_TIME).
                setCookTime(MAX_COOK_TIME).
                setCreateDate(20L).
                setLastUpdate(20L).
                build();
    }

    public static RecipeDurationPersistenceModel getExistingInvalidPreAndCookTime() {
        return new RecipeDurationPersistenceModel.Builder().
                setDataId("dataId-recipeDuration-existing-id0").
                setDomainId(EXISTING_RECIPE_DOMAIN_ID).
                setPrepTime(MAX_PREP_TIME + 1).
                setCookTime(MAX_COOK_TIME + 1).
                setCreateDate(20L).
                setLastUpdate(20L).
                build();
    }

    public static List<RecipeDurationPersistenceModel> getAllExisting() {
        return Arrays.asList(
                getExistingInvalidPreAndCookTime(),
                getExistingValidPrepTimeValidCookTime()
        );
    }

    // endregion Persistence models for testing existing loaded domain models

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
