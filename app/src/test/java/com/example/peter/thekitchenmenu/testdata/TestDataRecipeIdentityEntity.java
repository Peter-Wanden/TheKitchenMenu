package com.example.peter.thekitchenmenu.testdata;

import com.example.peter.thekitchenmenu.commonmocks.StringMaker;
import com.example.peter.thekitchenmenu.data.repository.source.local.recipe.identity.datasource.RecipeIdentityEntity;
import com.example.peter.thekitchenmenu.data.repository.source.local.recipe.metadata.TestDataRecipeMetadataEntity;
import com.example.peter.thekitchenmenu.domain.usecase.recipe.component.identity.RecipeIdentityPersistenceModel;
import com.example.peter.thekitchenmenu.domain.usecase.recipe.component.identity.RecipeIdentityTest;

import java.util.ArrayList;
import java.util.List;



public class TestDataRecipeIdentityEntity {

    private static final RecipeIdentityPersistenceModel defaultModel = new RecipeIdentityPersistenceModel.Builder().
            getDefault().
            build();

    public static RecipeIdentityEntity getInvalidNewEmpty() {
        return null;
//        new RecipeIdentityEntity(
//                TestDataRecipeMetadataEntity.getNewInvalidParent().getDataId(),
//                defaultModel.getTitle(),
//                defaultModel.getDescription(),
//                TestDataRecipeMetadataEntity.getNewInvalidParent().getCreateDate(),
//                TestDataRecipeMetadataEntity.getNewInvalidParent().getLastUpdate()
//        );
    }

    public static RecipeIdentityEntity getInvalidNewTitleTooShortDescriptionDefault() {
        String titleTooShort = new StringMaker().
                makeStringOfExactLength(RecipeIdentityTest.TITLE_MIN_LENGTH).
                thenRemoveOneCharacter().
                build();

        return null;
//        new RecipeIdentityEntity(
//                TestDataRecipeMetadataEntity.getNewInvalidParent().getDataId(),
//                titleTooShort,
//                defaultModel.getDescription(),
//                TestDataRecipeMetadataEntity.getNewInvalidParent().getCreateDate(),
//                TestDataRecipeMetadataEntity.getNewInvalidParent().getLastUpdate()
//        );
    }

    public static RecipeIdentityEntity getInvalidNewTitleTooLongDescriptionDefault() {
        String titleTooLong = new StringMaker().
                makeStringOfExactLength(RecipeIdentityTest.TITLE_MAX_LENGTH).
                includeStringAtStart("invalidTitle").
                thenAddOneCharacter().build();

        return null;
//        new RecipeIdentityEntity(
//                TestDataRecipeMetadataEntity.getNewInvalidParent().getDataId(),
//                titleTooLong,
//                defaultModel.getDescription(),
//                TestDataRecipeMetadataEntity.getNewInvalidParent().getCreateDate(),
//                TestDataRecipeMetadataEntity.getNewInvalidParent().getLastUpdate()
//        );
    }

    public static RecipeIdentityEntity getInvalidNewTitleTooLongDescriptionTooLong() {
        String descriptionTooLong = new StringMaker().
                makeStringOfExactLength(RecipeIdentityTest.DESCRIPTION_MAX_LENGTH).
                includeStringAtStart("descriptionTooLong").
                thenAddOneCharacter().
                build();

        return null;
//        new RecipeIdentityEntity(
//                TestDataRecipeMetadataEntity.getNewInvalidParent().getDataId(),
//                getInvalidNewTitleTooLongDescriptionDefault().getTitle(),
//                descriptionTooLong,
//                TestDataRecipeMetadataEntity.getNewInvalidParent().getCreateDate(),
//                TestDataRecipeMetadataEntity.getNewInvalidParent().getLastUpdate()
//        );
    }

    public static RecipeIdentityEntity getInvalidNewTitleTooShortDescriptionValid() {
        return null;
//        new RecipeIdentityEntity(
//                TestDataRecipeMetadataEntity.getNewInvalidParent().getDataId(),
//                getInvalidNewTitleTooShortDescriptionDefault().getTitle(),
//                getValidNewComplete().getDescription(),
//                TestDataRecipeMetadataEntity.getNewInvalidParent().getCreateDate(),
//                TestDataRecipeMetadataEntity.getNewInvalidParent().getLastUpdate()
//        );
    }

    public static RecipeIdentityEntity getValidNewTitleValidDescriptionDefault() {
        return null;
//        new RecipeIdentityEntity(
//                TestDataRecipeMetadataEntity.getNewValid().getDataId(),
//                getValidNewComplete().getTitle(),
//                defaultModel.getDescription(),
//                TestDataRecipeMetadataEntity.getNewValid().getCreateDate(),
//                TestDataRecipeMetadataEntity.getNewValid().getLastUpdate()
//        );
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

        return null;
//        new RecipeIdentityEntity(
//                TestDataRecipeMetadataEntity.getNewValid().getDataId(),
//                newValidTitle,
//                newValidDescription,
//                TestDataRecipeMetadataEntity.getNewValid().getCreateDate(),
//                TestDataRecipeMetadataEntity.getNewValid().getLastUpdate()
//        );
    }

    public static RecipeIdentityEntity getInvalidExistingTitleTooShortDefaultDescription() {
        String invalidExistingTitleTooShort = new StringMaker().
                makeStringOfExactLength(RecipeIdentityTest.TITLE_MIN_LENGTH).
                includeStringAtStart("invalidExistingTitleTooShort").
                thenRemoveOneCharacter().
                build();


        return null;
//        new RecipeIdentityEntity(
//                TestDataRecipeMetadataEntity.getInvalidExisting().getDataId(),
//                invalidExistingTitleTooShort,
//                defaultModel.getDescription(),
//                TestDataRecipeMetadataEntity.getInvalidExisting().getCreateDate(),
//                TestDataRecipeMetadataEntity.getInvalidExisting().getLastUpdate()
//        );
    }

    public static RecipeIdentityEntity getInvalidExistingTitleTooLongDefaultDescription() {
        String invalidExistingTitleTooLong = new StringMaker().
                makeStringOfExactLength(RecipeIdentityTest.TITLE_MAX_LENGTH).
                includeStringAtStart("invalidExistingTitleTooLong").thenAddOneCharacter().
                build();

        return null;
//        new RecipeIdentityEntity(
//                TestDataRecipeMetadataEntity.getInvalidExisting().getDataId(),
//                invalidExistingTitleTooLong,
//                defaultModel.getDescription(),
//                TestDataRecipeMetadataEntity.getInvalidExisting().getCreateDate(),
//                TestDataRecipeMetadataEntity.getInvalidExisting().getLastUpdate()
//        );
    }

    public static RecipeIdentityEntity getInvalidExistingTitleValidDescriptionTooLong() {
        String existingDescriptionTooLong = new StringMaker().
                makeStringOfExactLength(RecipeIdentityTest.DESCRIPTION_MAX_LENGTH).
                includeStringAtStart("existingDescriptionTooLong").
                thenAddOneCharacter().
                build();

        return null;
//        new RecipeIdentityEntity(
//                TestDataRecipeMetadataEntity.getInvalidExisting().getDataId(),
//                getValidExistingTitleValidDescriptionValid().getTitle(),
//                existingDescriptionTooLong,
//                TestDataRecipeMetadataEntity.getInvalidExisting().getCreateDate(),
//                TestDataRecipeMetadataEntity.getInvalidExisting().getLastUpdate()
//        );
    }

    public static RecipeIdentityEntity getInvalidExistingTitleTooShortDescriptionTooLong() {
        return null;
//        new RecipeIdentityEntity(
//                TestDataRecipeMetadataEntity.getInvalidExisting().getDataId(),
//                getInvalidExistingTitleTooShortDefaultDescription().getTitle(),
//                getInvalidExistingTitleValidDescriptionTooLong().getDescription(),
//                TestDataRecipeMetadataEntity.getInvalidExisting().getCreateDate(),
//                TestDataRecipeMetadataEntity.getInvalidExisting().getLastUpdate()
//        );
    }

    public static RecipeIdentityEntity getInvalidExistingTitleTooLongDescriptionTooLong() {
        return null;
//        new RecipeIdentityEntity(
//                TestDataRecipeMetadataEntity.getInvalidExisting().getDataId(),
//                getInvalidExistingTitleTooLongDefaultDescription().getTitle(),
//                getInvalidExistingTitleValidDescriptionTooLong().getDescription(),
//                TestDataRecipeMetadataEntity.getInvalidExisting().getCreateDate(),
//                TestDataRecipeMetadataEntity.getInvalidExisting().getLastUpdate()
//        );
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

        return null;
//        new RecipeIdentityEntity(
//                TestDataRecipeMetadataEntity.getValidExisting().getDataId(),
//                validExistingCompleteTitle,
//                validExistingCompleteDescription,
//                TestDataRecipeMetadataEntity.getValidExisting().getCreateDate(),
//                TestDataRecipeMetadataEntity.getValidExisting().getLastUpdate()
//        );
    }

    public static RecipeIdentityEntity getValidExistingTitleValidDescriptionDefault() {
        return null;
//        new RecipeIdentityEntity(
//                TestDataRecipeMetadataEntity.getInvalidExisting().getDataId(),
//                getValidExistingTitleValidDescriptionValid().getTitle(),
//                defaultModel.getDescription(),
//                TestDataRecipeMetadataEntity.getValidExisting().getCreateDate(),
//                TestDataRecipeMetadataEntity.getValidExisting().getLastUpdate()
//        );
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

        return null;
//        new RecipeIdentityEntity(
//                TestDataRecipeMetadataEntity.getValidFromAnotherUser().getDataId(),
//                validCompleteFromAnotherUserTitle,
//                validCompleteFromAnotherUserDescription,
//                TestDataRecipeMetadataEntity.getValidFromAnotherUser().getCreateDate(),
//                TestDataRecipeMetadataEntity.getValidFromAnotherUser().getLastUpdate()
//        );
    }

    public static RecipeIdentityEntity getValidCompleteAfterCloned() {
        return null;
//        new RecipeIdentityEntity(
//                TestDataRecipeMetadataEntity.getNewValid().getDataId(),
//                getValidCompleteFromAnotherUser().getTitle(),
//                getValidCompleteFromAnotherUser().getDescription(),
//                TestDataRecipeMetadataEntity.getNewValid().getCreateDate(),
//                TestDataRecipeMetadataEntity.getNewValid().getLastUpdate()
//        );
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

        return null;
//        new RecipeIdentityEntity(
//                TestDataRecipeMetadataEntity.getInvalidFromAnotherUser().getDataId(),
//                invalidCompleteFromAnotherUserTitle,
//                invalidCompleteFromAnotherUserDescription,
//                TestDataRecipeMetadataEntity.getInvalidFromAnotherUser().getCreateDate(),
//                TestDataRecipeMetadataEntity.getInvalidFromAnotherUser().getLastUpdate()
//        );
    }

    public static RecipeIdentityEntity getValidAfterInvalidClonedData() {
        StringBuilder truncatedTitle = new StringBuilder().
                append(getInvalidFromAnotherUser().getTitle());
        truncatedTitle.setLength(RecipeIdentityTest.TITLE_MAX_LENGTH);

        StringBuilder truncatedDescription = new StringBuilder().
                append(getInvalidFromAnotherUser().getDescription());
        truncatedDescription.setLength(RecipeIdentityTest.DESCRIPTION_MAX_LENGTH);

        return null;
//        new RecipeIdentityEntity(
//                TestDataRecipeMetadataEntity.getInvalidNewCloned().getDataId(),
//                truncatedTitle.toString(),
//                truncatedDescription.toString(),
//                TestDataRecipeMetadataEntity.getInvalidNewCloned().getCreateDate(),
//                TestDataRecipeMetadataEntity.getInvalidNewCloned().getLastUpdate()
//        );
    }

    public static RecipeIdentityEntity getValidClonedDescriptionUpdated() {
        String validClonedValidUpdatedDescription = new StringMaker().
                makeStringOfExactLength(RecipeIdentityTest.DESCRIPTION_MAX_LENGTH).
                includeStringAtStart("validClonedValidUpdatedDescription").
                build();

        return null;
//        new RecipeIdentityEntity(
//                TestDataRecipeMetadataEntity.getValidNewCloned().getDataId(),
//                getValidCompleteFromAnotherUser().getTitle(),
//                validClonedValidUpdatedDescription,
//                TestDataRecipeMetadataEntity.getValidNewCloned().getLastUpdate(),
//                TestDataRecipeMetadataEntity.getValidNewCloned().getCreateDate()
//        );
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
