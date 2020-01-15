package com.example.peter.thekitchenmenu.testdata;

import com.example.peter.thekitchenmenu.commonmocks.StringMaker;
import com.example.peter.thekitchenmenu.data.entity.RecipeIdentityEntity;
import com.example.peter.thekitchenmenu.domain.usecase.recipeidentity.RecipeIdentityModel;

import java.util.ArrayList;
import java.util.List;

import static com.example.peter.thekitchenmenu.domain.usecase.recipeidentity.RecipeIdentityMediatorTest.*;

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

    public static RecipeIdentityEntity getInvalidNewTitleTooShortDefaultDescription() {
        String titleTooShort = new StringMaker().
                makeStringOfExactLength(TITLE_MIN_LENGTH).
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

    public static RecipeIdentityEntity getInvalidNewTitleTooLongDefaultDescription() {
        String titleTooLong = new StringMaker().
                makeStringOfExactLength(TITLE_MAX_LENGTH).
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
                makeStringOfExactLength(DESCRIPTION_MAX_LENGTH).
                includeStringAtStart("descriptionTooLong").
                thenAddOneCharacter().
                build();

        return new RecipeIdentityEntity(
                TestDataRecipeEntity.getNewInvalid().getId(),
                getInvalidNewTitleTooLongDefaultDescription().getTitle(),
                descriptionTooLong,
                TestDataRecipeEntity.getNewInvalid().getCreateDate(),
                TestDataRecipeEntity.getNewInvalid().getLastUpdate()
        );
    }

    public static RecipeIdentityEntity getInvalidNewTitleTooShortValidDescription() {
        return new RecipeIdentityEntity(
                TestDataRecipeEntity.getNewInvalid().getId(),
                getInvalidNewTitleTooShortDefaultDescription().getTitle(),
                getValidNewComplete().getDescription(),
                TestDataRecipeEntity.getNewInvalid().getCreateDate(),
                TestDataRecipeEntity.getNewInvalid().getLastUpdate()
        );
    }

    public static RecipeIdentityEntity getValidNewValidTitleUpdatedDefaultDescription() {
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
                makeStringOfExactLength(TITLE_MAX_LENGTH).
                includeStringAtStart("newValidTitle").
                build();
        String newValidDescription = new StringMaker().
                makeStringOfExactLength(DESCRIPTION_MAX_LENGTH).
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
                makeStringOfExactLength(TITLE_MIN_LENGTH).
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

    public static RecipeIdentityEntity getValidExistingComplete() {
        String validExistingCompleteTitle = new StringMaker().
                makeStringOfExactLength(TITLE_MAX_LENGTH).
                includeStringAtStart("validExistingCompleteTitle").
                build();
        String validExistingCompleteDescription = new StringMaker().
                makeStringOfExactLength(DESCRIPTION_MAX_LENGTH).
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

    public static RecipeIdentityEntity getValidCompleteFromAnotherUser() {
        String validCompleteFromAnotherUserTitle = new StringMaker().
                makeStringOfExactLength(TITLE_MAX_LENGTH).
                includeStringAtStart("validCompleteFromAnotherUserTitle").
                build();
        String validCompleteFromAnotherUserDescription = new StringMaker().
                makeStringOfExactLength(DESCRIPTION_MAX_LENGTH).
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

    public static RecipeIdentityEntity getInvalidCompleteFromAnotherUser() {
        String invalidCompleteFromAnotherUserTitle = new StringMaker().
                makeStringOfExactLength(TITLE_MAX_LENGTH).
                includeStringAtStart("invalidFromAnotherUserTitle").
                thenAddOneCharacter().
                build();
        String invalidCompleteFromAnotherUserDescription = new StringMaker().
                makeStringOfExactLength(DESCRIPTION_MAX_LENGTH).
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

    public static RecipeIdentityEntity getValidNewClonedComplete() {
        return new RecipeIdentityEntity(
                TestDataRecipeEntity.getNewValid().getId(),
                getValidCompleteFromAnotherUser().getTitle(),
                getValidCompleteFromAnotherUser().getDescription(),
                TestDataRecipeEntity.getNewValid().getCreateDate(),
                TestDataRecipeEntity.getNewValid().getLastUpdate()
        );
    }

    public static RecipeIdentityEntity getInvalidNewCloned() {
        return new RecipeIdentityEntity(
                TestDataRecipeEntity.getInvalidNewCloned().getId(),
                getInvalidCompleteFromAnotherUser().getTitle(),
                getInvalidCompleteFromAnotherUser().getDescription(),
                TestDataRecipeEntity.getInvalidNewCloned().getCreateDate(),
                TestDataRecipeEntity.getInvalidNewCloned().getLastUpdate()
        );
    }

    public static RecipeIdentityEntity getValidNewClonedDescriptionUpdatedComplete() {
        String validUpdatedDescription = new StringMaker().
                makeStringOfExactLength(DESCRIPTION_MAX_LENGTH).
                includeStringAtStart("validUpdatedDescription").
                build();

        return new RecipeIdentityEntity(
                TestDataRecipeEntity.getValidNewCloned().getId(),
                getValidCompleteFromAnotherUser().getTitle(),
                validUpdatedDescription,
                TestDataRecipeEntity.getValidNewCloned().getLastUpdate(),
                TestDataRecipeEntity.getValidNewCloned().getCreateDate()
        );
    }

    public static RecipeIdentityEntity getRecipeIdentityEntityById(String recipeId) {
        List<RecipeIdentityEntity> entityList = new ArrayList<>();
        entityList.add(getInvalidNewEmpty());
        entityList.add(getInvalidNewTitleTooShortDefaultDescription());
        entityList.add(getInvalidNewTitleTooShortValidDescription());
        entityList.add(getValidNewValidTitleUpdatedDefaultDescription());
        entityList.add(getValidNewComplete());
        entityList.add(getInvalidExistingTitleTooShortDefaultDescription());
        entityList.add(getValidExistingComplete());
        entityList.add(getValidCompleteFromAnotherUser());
        entityList.add(getInvalidCompleteFromAnotherUser());
        entityList.add(getValidNewClonedComplete());
        entityList.add(getInvalidNewCloned());
        entityList.add(getValidNewClonedDescriptionUpdatedComplete());

        for (RecipeIdentityEntity entity : entityList) {
            if (entity.getId().equals(recipeId)) {
                return entity;
            }
        }
        return null;
    }
}
