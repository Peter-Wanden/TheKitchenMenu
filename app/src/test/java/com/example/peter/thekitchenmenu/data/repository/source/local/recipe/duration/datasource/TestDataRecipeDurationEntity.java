package com.example.peter.thekitchenmenu.data.repository.source.local.recipe.duration.datasource;

import com.example.peter.thekitchenmenu.data.repository.recipe.duration.TestDataRecipeDuration;
import com.example.peter.thekitchenmenu.data.repository.recipe.metadata.TestDataRecipeMetadata;
import com.example.peter.thekitchenmenu.domain.usecase.recipe.component.duration.RecipeDurationPersistenceModel;

public class TestDataRecipeDurationEntity {

    public static int getMaxPrepTime() {
        return 6000;
    }

    public static int getMaxCookTime() {
        return 6000;
    }

    public static RecipeDurationEntity getValidNewEmpty() {
        return convertDomainModelToDatabaseEntity(TestDataRecipeDuration.
                getValidNew());
    }

    public static RecipeDurationEntity getInvalidNewPrepTimeInvalid() {
        return convertDomainModelToDatabaseEntity(TestDataRecipeDuration.
                getInvalidNewPrepTimeInvalid());
    }

    public static RecipeDurationEntity getInvalidNewCookTimeInvalid() {
        return convertDomainModelToDatabaseEntity(TestDataRecipeDuration.
                getInvalidNewCookTimeInvalid());
    }

    public static RecipeDurationEntity getValidNewPrepTimeValid() {
        return convertDomainModelToDatabaseEntity(TestDataRecipeDuration.
                getValidNewPrepTimeValid());
    }

    public static RecipeDurationEntity getValidNewCookTimeValid() {
        return convertDomainModelToDatabaseEntity(TestDataRecipeDuration.
                getValidNewCookTimeValid());
    }

    public static RecipeDurationEntity getValidNewComplete() {
        return convertDomainModelToDatabaseEntity(TestDataRecipeDuration.
                getValidNewComplete());
    }

    public static RecipeDurationEntity getInvalidExistingComplete() {
        return convertDomainModelToDatabaseEntity(TestDataRecipeDuration.
                getInvalidExistingComplete());
    }

    public static RecipeDurationEntity getValidExistingComplete() {
        return convertDomainModelToDatabaseEntity(TestDataRecipeDuration.
                getValidExistingComplete());
    }

    public static RecipeDurationEntity getValidCompleteFromAnotherUser() {
        return convertDomainModelToDatabaseEntity(TestDataRecipeDuration.
                getValidCompleteFromAnotherUser());
    }

    public static RecipeDurationEntity getInvalidCompleteFromAnotherUser() {
        return convertDomainModelToDatabaseEntity(TestDataRecipeDuration.
                getInvalidCompleteFromAnotherUser());
    }

    public static RecipeDurationEntity getValidNewCopied() {
        return convertDomainModelToDatabaseEntity(TestDataRecipeDuration.
                getValidNewCopied());
    }

    public static RecipeDurationEntity getInvalidNewCopied() {
        return convertDomainModelToDatabaseEntity(TestDataRecipeDuration.
                getInvalidNewCopied());
    }

    public static RecipeDurationEntity getValidNewCopiedPrepTimeUpdated() {
        return convertDomainModelToDatabaseEntity(TestDataRecipeDuration.
                getValidNewCopiedPrepTimeUpdated());
    }

    private static RecipeDurationEntity convertDomainModelToDatabaseEntity(
            RecipeDurationPersistenceModel m) {
        return new RecipeDurationEntity(
                m.getDataId(),
                m.getDomainId(),
                m.getPrepTime(),
                m.getCookTime(),
                m.getCreateDate(),
                m.getLastUpdate()
        );
    }
}











































