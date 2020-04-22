package com.example.peter.thekitchenmenu.testdata;

import com.example.peter.thekitchenmenu.commonmocks.StringMaker;
import com.example.peter.thekitchenmenu.data.repository.recipe.identity.TestDataRecipeIdentity;
import com.example.peter.thekitchenmenu.data.repository.source.local.recipe.identity.datasource.RecipeIdentityEntity;
import com.example.peter.thekitchenmenu.domain.usecase.recipe.component.identity.RecipeIdentityPersistenceModel;
import com.example.peter.thekitchenmenu.domain.usecase.recipe.component.identity.RecipeIdentityTest;

import java.util.ArrayList;
import java.util.List;


public class TestDataRecipeIdentityEntity {

    public static RecipeIdentityEntity getInvalidNewEmpty() {
        return convertModelToEntity(
                TestDataRecipeIdentity.getInvalidNewEmpty()
        );
    }

    public static RecipeIdentityEntity getInvalidNewTitleTooShort() {
        return convertModelToEntity(
                TestDataRecipeIdentity.getInvalidNewTitleTooShort()
        );
    }

    public static RecipeIdentityEntity getInvalidNewTitleTooLong() {
        return convertModelToEntity(
                TestDataRecipeIdentity.getInvalidNewTitleTooLong()
        );
    }

    public static RecipeIdentityEntity getInvalidNewTitleTooLongDescriptionTooLong() {
        return convertModelToEntity(
                TestDataRecipeIdentity.getInvalidNewTitleTooLongDescriptionTooLong()
        );
    }

    public static RecipeIdentityEntity getInvalidNewTitleTooShortDescriptionValid() {
        return convertModelToEntity(
                TestDataRecipeIdentity.getInvalidNewTitleTooShortDescriptionValid()
        );
    }

    public static RecipeIdentityEntity getValidNewTitleValid() {
        return convertModelToEntity(
                TestDataRecipeIdentity.getValidNewTitleValid()
        );
    }

    public static RecipeIdentityEntity getValidNewComplete() {
        return convertModelToEntity(
                TestDataRecipeIdentity.getValidNewComplete()
        );
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

    private static RecipeIdentityEntity convertModelToEntity(
            RecipeIdentityPersistenceModel m) {
        return new RecipeIdentityEntity(
                m.getDataId(),
                m.getDomainId(),
                m.getTitle(),
                m.getDescription(),
                m.getCreateDate(),
                m.getLastUpdate()
        );
    }
}
