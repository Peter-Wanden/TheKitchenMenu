package com.example.peter.thekitchenmenu.testdata;

import com.example.peter.thekitchenmenu.commonmocks.StringMaker;
import com.example.peter.thekitchenmenu.data.primitivemodel.recipe.RecipeIdentityEntity;
import com.example.peter.thekitchenmenu.domain.usecase.recipe.component.recipeidentity.RecipeIdentityPersistenceModel;
import com.example.peter.thekitchenmenu.domain.usecase.recipe.component.recipeidentity.RecipeIdentityTest;

import java.util.ArrayList;
import java.util.List;



public class TestDataRecipeIdentityEntity {

    private static final RecipeIdentityPersistenceModel defaultModel = RecipeIdentityPersistenceModel.Builder.
            getDefault().
            build();

    public static RecipeIdentityEntity getInvalidNewEmpty() {
        return new RecipeIdentityEntity(
                TestDataRecipeMetaDataEntity.getNewInvalid().getId(),
                defaultModel.getTitle(),
                defaultModel.getDescription(),
                TestDataRecipeMetaDataEntity.getNewInvalid().getCreateDate(),
                TestDataRecipeMetaDataEntity.getNewInvalid().getLastUpdate()
        );
    }

    public static RecipeIdentityEntity getInvalidNewTitleTooShortDescriptionDefault() {
        String titleTooShort = new StringMaker().
                makeStringOfExactLength(RecipeIdentityTest.TITLE_MIN_LENGTH).
                thenRemoveOneCharacter().
                build();

        return new RecipeIdentityEntity(
                TestDataRecipeMetaDataEntity.getNewInvalid().getId(),
                titleTooShort,
                defaultModel.getDescription(),
                TestDataRecipeMetaDataEntity.getNewInvalid().getCreateDate(),
                TestDataRecipeMetaDataEntity.getNewInvalid().getLastUpdate()
        );
    }

    public static RecipeIdentityEntity getInvalidNewTitleTooLongDescriptionDefault() {
        String titleTooLong = new StringMaker().
                makeStringOfExactLength(RecipeIdentityTest.TITLE_MAX_LENGTH).
                includeStringAtStart("invalidTitle").
                thenAddOneCharacter().build();

        return new RecipeIdentityEntity(
                TestDataRecipeMetaDataEntity.getNewInvalid().getId(),
                titleTooLong,
                defaultModel.getDescription(),
                TestDataRecipeMetaDataEntity.getNewInvalid().getCreateDate(),
                TestDataRecipeMetaDataEntity.getNewInvalid().getLastUpdate()
        );
    }

    public static RecipeIdentityEntity getInvalidNewTitleTooLongDescriptionTooLong() {
        String descriptionTooLong = new StringMaker().
                makeStringOfExactLength(RecipeIdentityTest.DESCRIPTION_MAX_LENGTH).
                includeStringAtStart("descriptionTooLong").
                thenAddOneCharacter().
                build();

        return new RecipeIdentityEntity(
                TestDataRecipeMetaDataEntity.getNewInvalid().getId(),
                getInvalidNewTitleTooLongDescriptionDefault().getTitle(),
                descriptionTooLong,
                TestDataRecipeMetaDataEntity.getNewInvalid().getCreateDate(),
                TestDataRecipeMetaDataEntity.getNewInvalid().getLastUpdate()
        );
    }

    public static RecipeIdentityEntity getInvalidNewTitleTooShortDescriptionValid() {
        return new RecipeIdentityEntity(
                TestDataRecipeMetaDataEntity.getNewInvalid().getId(),
                getInvalidNewTitleTooShortDescriptionDefault().getTitle(),
                getValidNewComplete().getDescription(),
                TestDataRecipeMetaDataEntity.getNewInvalid().getCreateDate(),
                TestDataRecipeMetaDataEntity.getNewInvalid().getLastUpdate()
        );
    }

    public static RecipeIdentityEntity getValidNewTitleValidDescriptionDefault() {
        return new RecipeIdentityEntity(
                TestDataRecipeMetaDataEntity.getNewValid().getId(),
                getValidNewComplete().getTitle(),
                defaultModel.getDescription(),
                TestDataRecipeMetaDataEntity.getNewValid().getCreateDate(),
                TestDataRecipeMetaDataEntity.getNewValid().getLastUpdate()
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
                TestDataRecipeMetaDataEntity.getNewValid().getId(),
                newValidTitle,
                newValidDescription,
                TestDataRecipeMetaDataEntity.getNewValid().getCreateDate(),
                TestDataRecipeMetaDataEntity.getNewValid().getLastUpdate()
        );
    }

    public static RecipeIdentityEntity getInvalidExistingTitleTooShortDefaultDescription() {
        String invalidExistingTitleTooShort = new StringMaker().
                makeStringOfExactLength(RecipeIdentityTest.TITLE_MIN_LENGTH).
                includeStringAtStart("invalidExistingTitleTooShort").
                thenRemoveOneCharacter().
                build();


        return new RecipeIdentityEntity(
                TestDataRecipeMetaDataEntity.getInvalidExisting().getId(),
                invalidExistingTitleTooShort,
                defaultModel.getDescription(),
                TestDataRecipeMetaDataEntity.getInvalidExisting().getCreateDate(),
                TestDataRecipeMetaDataEntity.getInvalidExisting().getLastUpdate()
        );
    }

    public static RecipeIdentityEntity getInvalidExistingTitleTooLongDefaultDescription() {
        String invalidExistingTitleTooLong = new StringMaker().
                makeStringOfExactLength(RecipeIdentityTest.TITLE_MAX_LENGTH).
                includeStringAtStart("invalidExistingTitleTooLong").thenAddOneCharacter().
                build();

        return new RecipeIdentityEntity(
                TestDataRecipeMetaDataEntity.getInvalidExisting().getId(),
                invalidExistingTitleTooLong,
                defaultModel.getDescription(),
                TestDataRecipeMetaDataEntity.getInvalidExisting().getCreateDate(),
                TestDataRecipeMetaDataEntity.getInvalidExisting().getLastUpdate()
        );
    }

    public static RecipeIdentityEntity getInvalidExistingTitleValidDescriptionTooLong() {
        String existingDescriptionTooLong = new StringMaker().
                makeStringOfExactLength(RecipeIdentityTest.DESCRIPTION_MAX_LENGTH).
                includeStringAtStart("existingDescriptionTooLong").
                thenAddOneCharacter().
                build();

        return new RecipeIdentityEntity(
                TestDataRecipeMetaDataEntity.getInvalidExisting().getId(),
                getValidExistingTitleValidDescriptionValid().getTitle(),
                existingDescriptionTooLong,
                TestDataRecipeMetaDataEntity.getInvalidExisting().getCreateDate(),
                TestDataRecipeMetaDataEntity.getInvalidExisting().getLastUpdate()
        );
    }

    public static RecipeIdentityEntity getInvalidExistingTitleTooShortDescriptionTooLong() {
        return new RecipeIdentityEntity(
                TestDataRecipeMetaDataEntity.getInvalidExisting().getId(),
                getInvalidExistingTitleTooShortDefaultDescription().getTitle(),
                getInvalidExistingTitleValidDescriptionTooLong().getDescription(),
                TestDataRecipeMetaDataEntity.getInvalidExisting().getCreateDate(),
                TestDataRecipeMetaDataEntity.getInvalidExisting().getLastUpdate()
        );
    }

    public static RecipeIdentityEntity getInvalidExistingTitleTooLongDescriptionTooLong() {
        return new RecipeIdentityEntity(
                TestDataRecipeMetaDataEntity.getInvalidExisting().getId(),
                getInvalidExistingTitleTooLongDefaultDescription().getTitle(),
                getInvalidExistingTitleValidDescriptionTooLong().getDescription(),
                TestDataRecipeMetaDataEntity.getInvalidExisting().getCreateDate(),
                TestDataRecipeMetaDataEntity.getInvalidExisting().getLastUpdate()
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
                TestDataRecipeMetaDataEntity.getValidExisting().getId(),
                validExistingCompleteTitle,
                validExistingCompleteDescription,
                TestDataRecipeMetaDataEntity.getValidExisting().getCreateDate(),
                TestDataRecipeMetaDataEntity.getValidExisting().getLastUpdate()
        );
    }

    public static RecipeIdentityEntity getValidExistingTitleValidDescriptionDefault() {
        return new RecipeIdentityEntity(
                TestDataRecipeMetaDataEntity.getInvalidExisting().getId(),
                getValidExistingTitleValidDescriptionValid().getTitle(),
                defaultModel.getDescription(),
                TestDataRecipeMetaDataEntity.getValidExisting().getCreateDate(),
                TestDataRecipeMetaDataEntity.getValidExisting().getLastUpdate()
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
                TestDataRecipeMetaDataEntity.getValidFromAnotherUser().getId(),
                validCompleteFromAnotherUserTitle,
                validCompleteFromAnotherUserDescription,
                TestDataRecipeMetaDataEntity.getValidFromAnotherUser().getCreateDate(),
                TestDataRecipeMetaDataEntity.getValidFromAnotherUser().getLastUpdate()
        );
    }

    public static RecipeIdentityEntity getValidCompleteAfterCloned() {
        return new RecipeIdentityEntity(
                TestDataRecipeMetaDataEntity.getNewValid().getId(),
                getValidCompleteFromAnotherUser().getTitle(),
                getValidCompleteFromAnotherUser().getDescription(),
                TestDataRecipeMetaDataEntity.getNewValid().getCreateDate(),
                TestDataRecipeMetaDataEntity.getNewValid().getLastUpdate()
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
                TestDataRecipeMetaDataEntity.getInvalidFromAnotherUser().getId(),
                invalidCompleteFromAnotherUserTitle,
                invalidCompleteFromAnotherUserDescription,
                TestDataRecipeMetaDataEntity.getInvalidFromAnotherUser().getCreateDate(),
                TestDataRecipeMetaDataEntity.getInvalidFromAnotherUser().getLastUpdate()
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
                TestDataRecipeMetaDataEntity.getInvalidNewCloned().getId(),
                truncatedTitle.toString(),
                truncatedDescription.toString(),
                TestDataRecipeMetaDataEntity.getInvalidNewCloned().getCreateDate(),
                TestDataRecipeMetaDataEntity.getInvalidNewCloned().getLastUpdate()
        );
    }

    public static RecipeIdentityEntity getValidClonedDescriptionUpdated() {
        String validClonedValidUpdatedDescription = new StringMaker().
                makeStringOfExactLength(RecipeIdentityTest.DESCRIPTION_MAX_LENGTH).
                includeStringAtStart("validClonedValidUpdatedDescription").
                build();

        return new RecipeIdentityEntity(
                TestDataRecipeMetaDataEntity.getValidNewCloned().getId(),
                getValidCompleteFromAnotherUser().getTitle(),
                validClonedValidUpdatedDescription,
                TestDataRecipeMetaDataEntity.getValidNewCloned().getLastUpdate(),
                TestDataRecipeMetaDataEntity.getValidNewCloned().getCreateDate()
        );
    }

    public static List<RecipeIdentityEntity> getValidIdentityEntities() {
        List<RecipeIdentityEntity> entityList = new ArrayList<>();
        entityList.add(getValidNewComplete());
        entityList.add(getValidExistingTitleValidDescriptionValid());
        entityList.add(getValidCompleteFromAnotherUser());
        entityList.add(getValidCompleteAfterCloned());
        entityList.add(getValidClonedDescriptionUpdated());
        return entityList;
    }
}
