package com.example.peter.thekitchenmenu.data.repository.source.local.recipe.duration.datasource;

import com.example.peter.thekitchenmenu.data.repository.recipe.duration.TestDataRecipeDuration;
import com.example.peter.thekitchenmenu.domain.usecase.recipe.component.duration.RecipeDurationPersistenceDomainModel;

import java.util.ArrayList;
import java.util.List;

public class TestDataRecipeDurationEntity {

    public static int getMaxPrepTime() {
        return 6000;
    }

    public static int getMaxCookTime() {
        return 6000;
    }

    public static RecipeDurationEntity getValidNew() {
        return convertDomainModelToDatabaseEntity(TestDataRecipeDuration.
                getNewActiveDefault());
    }

    public static RecipeDurationEntity getInvalidNewPrepTimeInvalid() {
        return convertDomainModelToDatabaseEntity(TestDataRecipeDuration.
                getNewInvalidPrepMinutes());
    }

    public static RecipeDurationEntity getInvalidNewCookTimeInvalid() {
        return convertDomainModelToDatabaseEntity(TestDataRecipeDuration.
                getInvalidNewCookMinutes());
    }

    public static RecipeDurationEntity getValidNewPrepTimeValid() {
        return convertDomainModelToDatabaseEntity(TestDataRecipeDuration.
                getNewValidPrepTime());
    }

    public static RecipeDurationEntity getValidNewCookTimeValid() {
        return convertDomainModelToDatabaseEntity(TestDataRecipeDuration.
                getNewValidCookTime());
    }

    public static RecipeDurationEntity getValidNewComplete() {
        return convertDomainModelToDatabaseEntity(TestDataRecipeDuration.
                getNewValidPrepTimeValidCookTime());
    }

    public static List<RecipeDurationEntity> getAllNew() {
        List<RecipeDurationEntity> entities = new ArrayList<>();
        for (RecipeDurationPersistenceDomainModel m : TestDataRecipeDuration.getAllNew()) {
            entities.add(convertDomainModelToDatabaseEntity(m));
        }
        return entities;
    }

    public static RecipeDurationEntity getInvalidExistingComplete() {
        return convertDomainModelToDatabaseEntity(TestDataRecipeDuration.
                getExistingInvalidPreAndCookTime());
    }

    public static RecipeDurationEntity getValidExistingComplete() {
        return convertDomainModelToDatabaseEntity(TestDataRecipeDuration.
                getExistingValidPrepTimeValidCookTime());
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
            RecipeDurationPersistenceDomainModel m) {
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











































