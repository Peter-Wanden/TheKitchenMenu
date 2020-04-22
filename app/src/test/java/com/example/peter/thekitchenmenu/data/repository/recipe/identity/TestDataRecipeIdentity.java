package com.example.peter.thekitchenmenu.data.repository.recipe.identity;

import com.example.peter.thekitchenmenu.commonmocks.StringMaker;
import com.example.peter.thekitchenmenu.data.repository.recipe.metadata.TestDataRecipeMetadata;
import com.example.peter.thekitchenmenu.domain.usecase.recipe.component.identity.RecipeIdentityPersistenceModel;
import com.example.peter.thekitchenmenu.domain.usecase.recipe.component.identity.RecipeIdentityTest;

public class TestDataRecipeIdentity {

    public static final String NEW_RECIPE_ID = TestDataRecipeMetadata.
            getDataUnavailable().
            getDomainId();

    public static final String EXISTING_RECIPE_ID = TestDataRecipeMetadata.
            getValidUnchanged().
            getDomainId();

    public static final String RECIPE_ID_FROM_ANOTHER_USER = TestDataRecipeMetadata.
            getValidFromAnotherUser().getDomainId();

    public static RecipeIdentityPersistenceModel getInvalidNewEmpty() {
        return new RecipeIdentityPersistenceModel.Builder().
                getDefault().
                setDataId("dataId-recipeIdentity-id0").
                setDomainId(NEW_RECIPE_ID).
                setCreateDate(10L).
                setLastUpdate(10L).
                build();
    }

    public static RecipeIdentityPersistenceModel getInvalidNewTitleTooShort() {
        String titleTooShort = new StringMaker().
                makeStringOfExactLength(RecipeIdentityTest.TITLE_MIN_LENGTH).
                thenRemoveOneCharacter().
                build();

        return new RecipeIdentityPersistenceModel.Builder().
                getDefault().
                setDataId("dataId-recipeIdentity-id1").
                setDomainId(NEW_RECIPE_ID).
                setTitle(titleTooShort).
                setCreateDate(10L).
                setLastUpdate(20L).
                build();
    }

    public static RecipeIdentityPersistenceModel getInvalidNewTitleTooLong() {
        String titleTooLong = new StringMaker().
                makeStringOfExactLength(RecipeIdentityTest.TITLE_MAX_LENGTH).
                includeStringAtStart("invalidTitle").
                thenAddOneCharacter().build();

        return new RecipeIdentityPersistenceModel.Builder().
                getDefault().
                setDataId("dataId-recipeIdentity=id2").
                setDomainId(NEW_RECIPE_ID).
                setTitle(titleTooLong).
                setCreateDate(10L).
                setLastUpdate(30L).
                build();
    }

    public static RecipeIdentityPersistenceModel getInvalidNewTitleTooLongDescriptionTooLong() {
        String descriptionTooLong = new StringMaker().
                makeStringOfExactLength(RecipeIdentityTest.DESCRIPTION_MAX_LENGTH).
                includeStringAtStart("descriptionTooLong").
                thenAddOneCharacter().
                build();

        return new RecipeIdentityPersistenceModel.Builder().
                getDefault().
                setDataId("dataId-recipeIdentity-id3").
                setDomainId(NEW_RECIPE_ID).
                setTitle(getInvalidNewTitleTooLong().getTitle()).
                setDescription(descriptionTooLong).
                setCreateDate(10L).
                setLastUpdate(40L).
                build();
    }

    public static RecipeIdentityPersistenceModel getInvalidNewTitleTooShortDescriptionValid() {
        return new RecipeIdentityPersistenceModel.Builder().
                setDataId("dataId-recipeIdentity-id4").
                setDomainId(NEW_RECIPE_ID).
                setTitle(getInvalidNewTitleTooShort().getTitle()).
                setDescription(getValidNewComplete().getDescription()).
                setCreateDate(10L).
                setLastUpdate(50L).
                build();
    }

    public static RecipeIdentityPersistenceModel getValidNewTitleValid() {
        return new RecipeIdentityPersistenceModel.Builder().
                getDefault().
                setDataId("dataId-recipeIdentity=id5").
                setDomainId(NEW_RECIPE_ID).
                setTitle(getValidNewComplete().getTitle()).
                setCreateDate(10L).
                setLastUpdate(10L).
                build();
    }

    public static RecipeIdentityPersistenceModel getValidNewComplete() {
        String validTitle = new StringMaker().
                makeStringOfExactLength(RecipeIdentityTest.TITLE_MAX_LENGTH).
                includeStringAtStart("newValidTitle").
                build();
        String validDescription = new StringMaker().
                makeStringOfExactLength(RecipeIdentityTest.DESCRIPTION_MAX_LENGTH).
                includeStringAtStart("newValidDescription").
                build();

        return new RecipeIdentityPersistenceModel.Builder().
                setDataId("dataId-recipeIdentity-id5").
                setDomainId(NEW_RECIPE_ID).
                setTitle(validTitle).
                setDescription(validDescription).
                setCreateDate(10L).
                setLastUpdate(60L).
                build();
    }


}
