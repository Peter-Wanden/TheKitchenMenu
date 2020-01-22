package com.example.peter.thekitchenmenu.testdata;

import com.example.peter.thekitchenmenu.commonmocks.StringMaker;
import com.example.peter.thekitchenmenu.data.entity.RecipeIdentityEntity;
import com.example.peter.thekitchenmenu.domain.usecase.recipeidentity.RecipeIdentityModel;
import com.example.peter.thekitchenmenu.domain.usecase.recipeidentity.RecipeIdentityTest;

import java.util.ArrayList;
import java.util.List;



public class TestDataRecipeIdentityEntity {

    private static final RecipeIdentityModel defaultModel = RecipeIdentityModel.Builder.
            getDefault().
            build();

    public static RecipeIdentityEntity getInvalidNewEmpty() {
        return new RecipeIdentityEntity(
                TestDataRecipeEntity.getNewInvalid().getId(),
                defaultModel.getTitle(),
                defaultModel.getDescription(),
                TestDataRecipeEntity.getNewInvalid().getCreateDate(),
                TestDataRecipeEntity.getNewInvalid().getLastUpdate()
        );
    }

    public static RecipeIdentityEntity getInvalidNewTitleTooShortDescriptionDefault() {
        String titleTooShort = new StringMaker().
                makeStringOfExactLength(RecipeIdentityTest.TITLE_MIN_LENGTH).
                thenRemoveOneCharacter().
                build();

        return new RecipeIdentityEntity(
                TestDataRecipeEntity.getNewInvalid().getId(),
                titleTooShort,
                defaultModel.getDescription(),
                TestDataRecipeEntity.getNewInvalid().getCreateDate(),
                TestDataRecipeEntity.getNewInvalid().getLastUpdate()
        );
    }

    public static RecipeIdentityEntity getInvalidNewTitleTooLongDescriptionDefault() {
        String titleTooLong = new StringMaker().
                makeStringOfExactLength(RecipeIdentityTest.TITLE_MAX_LENGTH).
                includeStringAtStart("invalidTitle").
                thenAddOneCharacter().build();

        return new RecipeIdentityEntity(
                TestDataRecipeEntity.getNewInvalid().getId(),
                titleTooLong,
                defaultModel.getDescription(),
                TestDataRecipeEntity.getNewInvalid().getCreateDate(),
                TestDataRecipeEntity.getNewInvalid().getLastUpdate()
        );
    }

    public static RecipeIdentityEntity getInvalidNewTitleTooLongDescriptionTooLong() {
        String descriptionTooLong = new StringMaker().
                makeStringOfExactLength(RecipeIdentityTest.DESCRIPTION_MAX_LENGTH).
                includeStringAtStart("descriptionTooLong").
                thenAddOneCharacter().
                build();

        return new RecipeIdentityEntity(
                TestDataRecipeEntity.getNewInvalid().getId(),
                getInvalidNewTitleTooLongDescriptionDefault().getTitle(),
                descriptionTooLong,
                TestDataRecipeEntity.getNewInvalid().getCreateDate(),
                TestDataRecipeEntity.getNewInvalid().getLastUpdate()
        );
    }

    public static RecipeIdentityEntity getInvalidNewTitleTooShortDescriptionValid() {
        return new RecipeIdentityEntity(
                TestDataRecipeEntity.getNewInvalid().getId(),
                getInvalidNewTitleTooShortDescriptionDefault().getTitle(),
                getValidNewComplete().getDescription(),
                TestDataRecipeEntity.getNewInvalid().getCreateDate(),
                TestDataRecipeEntity.getNewInvalid().getLastUpdate()
        );
    }

    public static RecipeIdentityEntity getValidNewTitleValidDescriptionDefault() {
        return new RecipeIdentityEntity(
                TestDataRecipeEntity.getNewValid().getId(),
                getValidNewComplete().getTitle(),
                defaultModel.getDescription(),
                TestDataRecipeEntity.getNewValid().getCreateDate(),
                TestDataRecipeEntity.getNewValid().getLastUpdate()
        );
    }

    public static RecipeIdentityEntity getValidNewComplete() {
        String newValidTitle = new StringMaker().
                makeStringOfExactLength(RecipeIdentityTest.TITLE_MAX_LENGTH).
                includeStringAtStart("newValidTitle").
                build();
        String newValidDescription = new StringMaker().
                makeStringOfExactLength(RecipeIdentityTest.DESCRIPTION_MAX_LENGTH).
                includeStringAtStart("newValidDescription").
                build();

        return new RecipeIdentityEntity(
                TestDataRecipeEntity.getNewValid().getId(),
                newValidTitle,
                newValidDescription,
                TestDataRecipeEntity.getNewValid().getCreateDate(),
                TestDataRecipeEntity.getNewValid().getLastUpdate()
        );
    }

    public static RecipeIdentityEntity getInvalidExistingTitleTooShortDefaultDescription() {
        String invalidExistingTitleTooShort = new StringMaker().
                makeStringOfExactLength(RecipeIdentityTest.TITLE_MIN_LENGTH).
                includeStringAtStart("invalidExistingTitleTooShort").
                thenRemoveOneCharacter().
                build();


        return new RecipeIdentityEntity(
                TestDataRecipeEntity.getInvalidExisting().getId(),
                invalidExistingTitleTooShort,
                defaultModel.getDescription(),
                TestDataRecipeEntity.getInvalidExisting().getCreateDate(),
                TestDataRecipeEntity.getInvalidExisting().getLastUpdate()
        );
    }

    public static RecipeIdentityEntity getInvalidExistingTitleTooLongDefaultDescription() {
        String invalidExistingTitleTooLong = new StringMaker().
                makeStringOfExactLength(RecipeIdentityTest.TITLE_MAX_LENGTH).
                includeStringAtStart("invalidExistingTitleTooLong").thenAddOneCharacter().
                build();

        return new RecipeIdentityEntity(
                TestDataRecipeEntity.getInvalidExisting().getId(),
                invalidExistingTitleTooLong,
                defaultModel.getDescription(),
                TestDataRecipeEntity.getInvalidExisting().getCreateDate(),
                TestDataRecipeEntity.getInvalidExisting().getLastUpdate()
        );
    }

    public static RecipeIdentityEntity getInvalidExistingTitleValidDescriptionTooLong() {
        String existingDescriptionTooLong = new StringMaker().
                makeStringOfExactLength(RecipeIdentityTest.DESCRIPTION_MAX_LENGTH).
                includeStringAtStart("existingDescriptionTooLong").
                thenAddOneCharacter().
                build();

        return new RecipeIdentityEntity(
                TestDataRecipeEntity.getInvalidExisting().getId(),
                getValidExistingTitleValidDescriptionValid().getTitle(),
                existingDescriptionTooLong,
                TestDataRecipeEntity.getInvalidExisting().getCreateDate(),
                TestDataRecipeEntity.getInvalidExisting().getLastUpdate()
        );
    }

    public static RecipeIdentityEntity getInvalidExistingTitleTooShortDescriptionTooLong() {
        return new RecipeIdentityEntity(
                TestDataRecipeEntity.getInvalidExisting().getId(),
                getInvalidExistingTitleTooShortDefaultDescription().getTitle(),
                getInvalidExistingTitleValidDescriptionTooLong().getDescription(),
                TestDataRecipeEntity.getInvalidExisting().getCreateDate(),
                TestDataRecipeEntity.getInvalidExisting().getLastUpdate()
        );
    }

    public static RecipeIdentityEntity getInvalidExistingTitleTooLongDescriptionTooLong() {
        return new RecipeIdentityEntity(
                TestDataRecipeEntity.getInvalidExisting().getId(),
                getInvalidExistingTitleTooLongDefaultDescription().getTitle(),
                getInvalidExistingTitleValidDescriptionTooLong().getDescription(),
                TestDataRecipeEntity.getInvalidExisting().getCreateDate(),
                TestDataRecipeEntity.getInvalidExisting().getLastUpdate()
        );
    }

    public static RecipeIdentityEntity getValidExistingTitleValidDescriptionValid() {
        String validExistingCompleteTitle = new StringMaker().
                makeStringOfExactLength(RecipeIdentityTest.TITLE_MAX_LENGTH).
                includeStringAtStart("validExistingCompleteTitle").
                build();
        String validExistingCompleteDescription = new StringMaker().
                makeStringOfExactLength(RecipeIdentityTest.DESCRIPTION_MAX_LENGTH).
                includeStringAtStart("validExistingCompleteDescription").
                build();

        return new RecipeIdentityEntity(
                TestDataRecipeEntity.getValidExisting().getId(),
                validExistingCompleteTitle,
                validExistingCompleteDescription,
                TestDataRecipeEntity.getValidExisting().getCreateDate(),
                TestDataRecipeEntity.getValidExisting().getLastUpdate()
        );
    }

    public static RecipeIdentityEntity getValidExistingTitleValidDescriptionDefault() {
        return new RecipeIdentityEntity(
                TestDataRecipeEntity.getInvalidExisting().getId(),
                getValidExistingTitleValidDescriptionValid().getTitle(),
                defaultModel.getDescription(),
                TestDataRecipeEntity.getValidExisting().getCreateDate(),
                TestDataRecipeEntity.getValidExisting().getLastUpdate()
        );
    }

    public static RecipeIdentityEntity getValidCompleteFromAnotherUser() {
        String validCompleteFromAnotherUserTitle = new StringMaker().
                makeStringOfExactLength(RecipeIdentityTest.TITLE_MAX_LENGTH).
                includeStringAtStart("validCompleteFromAnotherUserTitle").
                build();
        String validCompleteFromAnotherUserDescription = new StringMaker().
                makeStringOfExactLength(RecipeIdentityTest.DESCRIPTION_MAX_LENGTH).
                includeStringAtStart("validCompleteFromAnotherUserDescription").
                build();

        return new RecipeIdentityEntity(
                TestDataRecipeEntity.getValidFromAnotherUser().getId(),
                validCompleteFromAnotherUserTitle,
                validCompleteFromAnotherUserDescription,
                TestDataRecipeEntity.getValidFromAnotherUser().getCreateDate(),
                TestDataRecipeEntity.getValidFromAnotherUser().getLastUpdate()
        );
    }

    public static RecipeIdentityEntity getValidCompleteAfterCloned() {
        return new RecipeIdentityEntity(
                TestDataRecipeEntity.getNewValid().getId(),
                getValidCompleteFromAnotherUser().getTitle(),
                getValidCompleteFromAnotherUser().getDescription(),
                TestDataRecipeEntity.getNewValid().getCreateDate(),
                TestDataRecipeEntity.getNewValid().getLastUpdate()
        );
    }

    public static RecipeIdentityEntity getInvalidFromAnotherUser() {
        String invalidCompleteFromAnotherUserTitle = new StringMaker().
                makeStringOfExactLength(RecipeIdentityTest.TITLE_MAX_LENGTH).
                includeStringAtStart("invalidFromAnotherUserTitle").
                thenAddOneCharacter().
                build();
        String invalidCompleteFromAnotherUserDescription = new StringMaker().
                makeStringOfExactLength(RecipeIdentityTest.DESCRIPTION_MAX_LENGTH).
                includeStringAtStart("validCompleteFromAnotherUserDescription").
                thenAddOneCharacter().
                build();

        return new RecipeIdentityEntity(
                TestDataRecipeEntity.getInvalidFromAnotherUser().getId(),
                invalidCompleteFromAnotherUserTitle,
                invalidCompleteFromAnotherUserDescription,
                TestDataRecipeEntity.getInvalidFromAnotherUser().getCreateDate(),
                TestDataRecipeEntity.getInvalidFromAnotherUser().getLastUpdate()
        );
    }

    public static RecipeIdentityEntity getValidAfterInvalidClonedData() {
        StringBuilder truncatedTitle = new StringBuilder().
                append(getInvalidFromAnotherUser().getTitle());
        truncatedTitle.setLength(RecipeIdentityTest.TITLE_MAX_LENGTH);

        StringBuilder truncatedDescription = new StringBuilder().
                append(getInvalidFromAnotherUser().getDescription());
        truncatedDescription.setLength(RecipeIdentityTest.DESCRIPTION_MAX_LENGTH);

        return new RecipeIdentityEntity(
                TestDataRecipeEntity.getInvalidNewCloned().getId(),
                truncatedTitle.toString(),
                truncatedDescription.toString(),
                TestDataRecipeEntity.getInvalidNewCloned().getCreateDate(),
                TestDataRecipeEntity.getInvalidNewCloned().getLastUpdate()
        );
    }

    public static RecipeIdentityEntity getValidClonedDescriptionUpdated() {
        String validClonedValidUpdatedDescription = new StringMaker().
                makeStringOfExactLength(RecipeIdentityTest.DESCRIPTION_MAX_LENGTH).
                includeStringAtStart("validClonedValidUpdatedDescription").
                build();

        return new RecipeIdentityEntity(
                TestDataRecipeEntity.getValidNewCloned().getId(),
                getValidCompleteFromAnotherUser().getTitle(),
                validClonedValidUpdatedDescription,
                TestDataRecipeEntity.getValidNewCloned().getLastUpdate(),
                TestDataRecipeEntity.getValidNewCloned().getCreateDate()
        );
    }

    public static RecipeIdentityEntity getRecipeIdentityEntityById(String recipeId) {
        List<RecipeIdentityEntity> entityList = new ArrayList<>();
        entityList.add(getInvalidNewEmpty());
        entityList.add(getInvalidNewTitleTooShortDescriptionDefault());
        entityList.add(getInvalidNewTitleTooShortDescriptionValid());
        entityList.add(getValidNewTitleValidDescriptionDefault());
        entityList.add(getValidNewComplete());
        entityList.add(getInvalidExistingTitleTooShortDefaultDescription());
        entityList.add(getValidExistingTitleValidDescriptionValid());
        entityList.add(getValidCompleteFromAnotherUser());
        entityList.add(getInvalidFromAnotherUser());
        entityList.add(getValidCompleteAfterCloned());
        entityList.add(getValidAfterInvalidClonedData());
        entityList.add(getValidClonedDescriptionUpdated());

        for (RecipeIdentityEntity entity : entityList) {
            if (entity.getId().equals(recipeId)) {
                return entity;
            }
        }
        return null;
    }
}
