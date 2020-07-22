package com.example.peter.thekitchenmenu.data.repository.recipe.identity;

import com.example.peter.thekitchenmenu.commonmocks.StringMaker;
import com.example.peter.thekitchenmenu.data.repository.recipe.metadata.TestDataRecipeMetadata;
import com.example.peter.thekitchenmenu.domain.usecase.recipe.component.identity.RecipeIdentityUseCasePersistenceModel;
import com.example.peter.thekitchenmenu.domain.usecase.recipe.component.identity.RecipeIdentityTest;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TestDataRecipeIdentity {

    private static final String NEW_RECIPE_DOMAIN_ID = TestDataRecipeMetadata.
            getInvalidDefault().
            getDomainId();

    private static final String EXISTING_RECIPE_DOMAIN_ID = TestDataRecipeMetadata.
            getValidUnchanged().
            getDomainId();

    private static final String RECIPE_DOMAIN_ID_FROM_ANOTHER_USER = TestDataRecipeMetadata.
            getValidFromAnotherUser().getDomainId();

    public static RecipeIdentityUseCasePersistenceModel getNewInvalidActiveDefault() {
        return new RecipeIdentityUseCasePersistenceModel.Builder().
                getDefault().
                setDataId("dataId-recipeIdentity-id0").
                setDomainId(NEW_RECIPE_DOMAIN_ID).
                setCreateDate(10L).
                setLastUpdate(10L).
                build();
    }

    public static RecipeIdentityUseCasePersistenceModel getInvalidNewTitleTooShort() {
        String titleTooShort = new StringMaker().
                makeStringOfExactLength(RecipeIdentityTest.TITLE_MIN_LENGTH).
                includeStringAtStart("newTitleTooShort").
                thenRemoveOneCharacter().
                build();

        return new RecipeIdentityUseCasePersistenceModel.Builder().
                getDefault().
                setDataId("dataId-recipeIdentity-id1").
                setDomainId(NEW_RECIPE_DOMAIN_ID).
                setTitle(titleTooShort).
                setCreateDate(10L).
                setLastUpdate(20L).
                build();
    }

    public static RecipeIdentityUseCasePersistenceModel getInvalidNewTitleTooLong() {
        String titleTooLong = new StringMaker().
                makeStringOfExactLength(RecipeIdentityTest.TITLE_MAX_LENGTH).
                includeStringAtStart("newTitleTooLong").
                thenAddOneCharacter().
                build();

        return new RecipeIdentityUseCasePersistenceModel.Builder().
                getDefault().
                setDataId("dataId-recipeIdentity=id2").
                setDomainId(NEW_RECIPE_DOMAIN_ID).
                setTitle(titleTooLong).
                setCreateDate(10L).
                setLastUpdate(30L).
                build();
    }

    public static RecipeIdentityUseCasePersistenceModel getInvalidNewTitleTooLongDescriptionTooLong() {
        String descriptionTooLong = new StringMaker().
                makeStringOfExactLength(RecipeIdentityTest.DESCRIPTION_MAX_LENGTH).
                includeStringAtStart("newDescriptionTooLong").
                thenAddOneCharacter().
                build();

        return new RecipeIdentityUseCasePersistenceModel.Builder().
                getDefault().
                setDataId("dataId-recipeIdentity-id3").
                setDomainId(NEW_RECIPE_DOMAIN_ID).
                setTitle(getInvalidNewTitleTooLong().getTitle()).
                setDescription(descriptionTooLong).
                setCreateDate(10L).
                setLastUpdate(40L).
                build();
    }

    public static RecipeIdentityUseCasePersistenceModel getInvalidNewTitleTooShortDescriptionValid() {
        return new RecipeIdentityUseCasePersistenceModel.Builder().
                setDataId("dataId-recipeIdentity-id4").
                setDomainId(NEW_RECIPE_DOMAIN_ID).
                setTitle(getInvalidNewTitleTooShort().getTitle()).
                setDescription(getValidNewComplete().getDescription()).
                setCreateDate(10L).
                setLastUpdate(50L).
                build();
    }

    public static RecipeIdentityUseCasePersistenceModel getValidNewTitleValid() {
        return new RecipeIdentityUseCasePersistenceModel.Builder().
                getDefault().
                setDataId("dataId-recipeIdentity=id5").
                setDomainId(NEW_RECIPE_DOMAIN_ID).
                setTitle(getValidNewComplete().getTitle()).
                setCreateDate(10L). // new saved models have the same create and last update dates
                setLastUpdate(10L).
                build();
    }

    public static RecipeIdentityUseCasePersistenceModel getValidNewComplete() {
        String validTitle = new StringMaker().
                makeStringOfExactLength(RecipeIdentityTest.TITLE_MAX_LENGTH).
                includeStringAtStart("newValidTitle").
                build();
        String validDescription = new StringMaker().
                makeStringOfExactLength(RecipeIdentityTest.DESCRIPTION_MAX_LENGTH).
                includeStringAtStart("newValidDescription").
                build();

        return new RecipeIdentityUseCasePersistenceModel.Builder().
                setDataId("dataId-recipeIdentity-id6").
                setDomainId(NEW_RECIPE_DOMAIN_ID).
                setTitle(validTitle).
                setDescription(validDescription).
                setCreateDate(70L).
                setLastUpdate(70L).
                build();
    }

    public static List<RecipeIdentityUseCasePersistenceModel> getAllNew() {
        return Arrays.asList(
                getNewInvalidActiveDefault(),
                getInvalidNewTitleTooShort(),
                getInvalidNewTitleTooLong(),
                getInvalidNewTitleTooLongDescriptionTooLong(),
                getInvalidNewTitleTooShortDescriptionValid(),
                getValidNewTitleValid(),
                getValidNewComplete()
        );
    }

    public static RecipeIdentityUseCasePersistenceModel getInvalidExistingTitleTooShort() {
        String invalidExistingTitleTooShort = new StringMaker().
                makeStringOfExactLength(RecipeIdentityTest.TITLE_MIN_LENGTH).
                includeStringAtStart("invalidExistingTitleTooShort").
                thenRemoveOneCharacter().
                build();

        return new RecipeIdentityUseCasePersistenceModel.Builder().
                getDefault().
                setDataId("dataId-recipeIdentity-id10").
                setDomainId(EXISTING_RECIPE_DOMAIN_ID).
                setTitle(invalidExistingTitleTooShort).
                setCreateDate(20L).
                setLastUpdate(20L).
                build();
    }

    public static RecipeIdentityUseCasePersistenceModel getInvalidExistingTitleTooLong() {
        String invalidExistingTitleTooLong = new StringMaker().
                makeStringOfExactLength(RecipeIdentityTest.TITLE_MAX_LENGTH).
                includeStringAtStart("invalidExistingTitleTooLong").thenAddOneCharacter().
                build();

        return new RecipeIdentityUseCasePersistenceModel.Builder().
                getDefault().
                setDataId("dataId-recipeIdentity-id11").
                setDomainId(EXISTING_RECIPE_DOMAIN_ID).
                setTitle(invalidExistingTitleTooLong).
                setCreateDate(20L).
                setLastUpdate(30L).
                build();
    }

    public static RecipeIdentityUseCasePersistenceModel getInvalidExistingTitleValidDescriptionTooLong() {
        String existingDescriptionTooLong = new StringMaker().
                makeStringOfExactLength(RecipeIdentityTest.DESCRIPTION_MAX_LENGTH).
                includeStringAtStart("existingDescriptionTooLong").
                thenAddOneCharacter().
                build();

        return new RecipeIdentityUseCasePersistenceModel.Builder().
                setDataId("dataId-recipeIdentity-12").
                setDomainId(EXISTING_RECIPE_DOMAIN_ID).
                setTitle(getValidExistingTitleValidDescriptionValid().getTitle()).
                setDescription(existingDescriptionTooLong).
                setCreateDate(20L).
                setLastUpdate(40L).
                build();
    }

    public static RecipeIdentityUseCasePersistenceModel getInvalidExistingTitleTooShortDescriptionTooLong() {
        return new RecipeIdentityUseCasePersistenceModel.Builder().
                setDataId("dataID-recipeIdentity-13").
                setDomainId(EXISTING_RECIPE_DOMAIN_ID).
                setTitle(getInvalidExistingTitleTooShort().getTitle()).
                setDescription(getInvalidExistingTitleValidDescriptionTooLong().getDescription()).
                setCreateDate(20L).
                setLastUpdate(50L).
                build();
    }

    public static RecipeIdentityUseCasePersistenceModel getInvalidExistingTitleTooLongDescriptionTooLong() {
        return new RecipeIdentityUseCasePersistenceModel.Builder().
                setDataId("dataId-recipeIdentity-14").
                setDomainId(EXISTING_RECIPE_DOMAIN_ID).
                setTitle(getInvalidExistingTitleTooLong().getTitle()).
                setDescription(getInvalidExistingTitleValidDescriptionTooLong().getDescription()).
                setCreateDate(20L).
                setLastUpdate(60L).
                build();
    }

    public static RecipeIdentityUseCasePersistenceModel getValidExistingTitleValidDescriptionValid() {
        String validExistingCompleteTitle = new StringMaker().
                makeStringOfExactLength(RecipeIdentityTest.TITLE_MAX_LENGTH).
                includeStringAtStart("validExistingCompleteTitle").
                build();
        String validExistingCompleteDescription = new StringMaker().
                makeStringOfExactLength(RecipeIdentityTest.DESCRIPTION_MAX_LENGTH).
                includeStringAtStart("validExistingCompleteDescription").
                build();

        return new RecipeIdentityUseCasePersistenceModel.Builder().
                setDataId("dataId-recipeIdentity-id15").
                setDomainId(EXISTING_RECIPE_DOMAIN_ID).
                setTitle(validExistingCompleteTitle).
                setDescription(validExistingCompleteDescription).
                setCreateDate(20L).
                setLastUpdate(70L).
                build();
    }

    public static RecipeIdentityUseCasePersistenceModel getValidExistingTitleValid() {
        return new RecipeIdentityUseCasePersistenceModel.Builder().
                getDefault().
                setDataId("dataId-recipeIdentity-id16").
                setDomainId(EXISTING_RECIPE_DOMAIN_ID).
                setTitle(getValidExistingTitleValidDescriptionValid().getTitle()).
                setCreateDate(20L).
                setLastUpdate(80L).
                build();
    }

    public static List<RecipeIdentityUseCasePersistenceModel> getAllExisting() {
        return Arrays.asList(
                getInvalidExistingTitleTooShort(),
                getInvalidExistingTitleTooLong(),
                getInvalidExistingTitleValidDescriptionTooLong(),
                getInvalidExistingTitleTooShortDescriptionTooLong(),
                getInvalidExistingTitleTooLongDescriptionTooLong(),
                getValidExistingTitleValidDescriptionValid(),
                getValidExistingTitleValid()
        );
    }

    public static RecipeIdentityUseCasePersistenceModel getValidCompleteFromAnotherUser() {
        String validCompleteFromAnotherUserTitle = new StringMaker().
                makeStringOfExactLength(RecipeIdentityTest.TITLE_MAX_LENGTH).
                includeStringAtStart("validCompleteFromAnotherUserTitle").
                build();
        String validCompleteFromAnotherUserDescription = new StringMaker().
                makeStringOfExactLength(RecipeIdentityTest.DESCRIPTION_MAX_LENGTH).
                includeStringAtStart("validCompleteFromAnotherUserDescription").
                build();

        return new RecipeIdentityUseCasePersistenceModel.Builder().
                setDataId("dataId-recipeIdentity-id20").
                setDomainId(RECIPE_DOMAIN_ID_FROM_ANOTHER_USER).
                setTitle(validCompleteFromAnotherUserTitle).
                setDescription(validCompleteFromAnotherUserDescription).
                setCreateDate(30L).
                setLastUpdate(30L).
                build();
    }

    public static RecipeIdentityUseCasePersistenceModel getInvalidFromAnotherUser() {
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

        return new RecipeIdentityUseCasePersistenceModel.Builder().
                setDataId("dataId-recipeIdentity-id21").
                setDomainId(RECIPE_DOMAIN_ID_FROM_ANOTHER_USER).
                setTitle(invalidCompleteFromAnotherUserTitle).
                setDescription(invalidCompleteFromAnotherUserDescription).
                setCreateDate(30L).
                setLastUpdate(40L).
                build();
    }

    public static List<RecipeIdentityUseCasePersistenceModel> getAllFromAnotherUser() {
        return Arrays.asList(
                getValidCompleteFromAnotherUser(),
                getInvalidFromAnotherUser()
        );
    }

    public static RecipeIdentityUseCasePersistenceModel getValidCompleteAfterCopied() {
        return new RecipeIdentityUseCasePersistenceModel.Builder().
                setDataId("dataId-recipeIdentity-id30").
                setDomainId(NEW_RECIPE_DOMAIN_ID).
                setTitle(getValidCompleteFromAnotherUser().getTitle()).
                setDescription(getValidCompleteFromAnotherUser().getDescription()).
                setCreateDate(40L).
                setLastUpdate(40L).
                build();
    }

    public static RecipeIdentityUseCasePersistenceModel getValidCopiedDescriptionUpdated() {
        String validCopiedUpdatedDescription = new StringMaker().
                makeStringOfExactLength(RecipeIdentityTest.DESCRIPTION_MAX_LENGTH).
                includeStringAtStart("validClonedValidUpdatedDescription").
                build();

        return new RecipeIdentityUseCasePersistenceModel.Builder().
                setDataId("dataId-recipeIdentity-id31")    .
                setDomainId(NEW_RECIPE_DOMAIN_ID).
                setTitle(getValidCompleteFromAnotherUser().getTitle()).
                setDescription(validCopiedUpdatedDescription).
                setCreateDate(40L).
                setLastUpdate(50L).
                build();
    }

    public static RecipeIdentityUseCasePersistenceModel getValidAfterInvalidCopy() {
        StringBuilder truncatedTitle = new StringBuilder().
                append(getInvalidFromAnotherUser().getTitle());
        truncatedTitle.setLength(RecipeIdentityTest.TITLE_MAX_LENGTH);

        StringBuilder truncatedDescription = new StringBuilder().
                append(getInvalidFromAnotherUser().getDescription());
        truncatedDescription.setLength(RecipeIdentityTest.DESCRIPTION_MAX_LENGTH);

        return new RecipeIdentityUseCasePersistenceModel.Builder().
                setDataId("dataId-recipeIdentity-id32").
                setDomainId(NEW_RECIPE_DOMAIN_ID).
                setTitle(truncatedTitle.toString()).
                setDescription(truncatedDescription.toString()).
                setCreateDate(40L).
                setLastUpdate(60L).
                build();
    }

    public static List<RecipeIdentityUseCasePersistenceModel> getAllCopied() {
        return Arrays.asList(
                getValidCompleteAfterCopied(),
                getValidCopiedDescriptionUpdated(),
                getValidAfterInvalidCopy()
        );
    }

    public static List<RecipeIdentityUseCasePersistenceModel> getAllValidModels() {
        return Arrays.asList(
                getValidNewComplete(),
                getValidNewTitleValid(),
                getValidExistingTitleValid(),
                getValidExistingTitleValidDescriptionValid(),
                getValidCompleteFromAnotherUser(),
                getValidCompleteAfterCopied(),
                getValidCopiedDescriptionUpdated(),
                getValidAfterInvalidCopy()
        );
    }

    public static List<RecipeIdentityUseCasePersistenceModel> getAllByDomainId(String domainId) {
        List<RecipeIdentityUseCasePersistenceModel> models = new ArrayList<>();
        for (RecipeIdentityUseCasePersistenceModel m : getAll()) {
            if (domainId.equals(m.getDomainId())) {
                models.add(m);
            }
        }
        return models;
    }

    public static List<RecipeIdentityUseCasePersistenceModel> getAll() {
        List<RecipeIdentityUseCasePersistenceModel> models = new ArrayList<>();
        models.addAll(getAllNew());
        models.addAll(getAllExisting());
        models.addAll(getAllFromAnotherUser());
        models.addAll(getAllCopied());
        return models;
    }

    public static RecipeIdentityUseCasePersistenceModel getActiveByDomainId(String domainId) {
        long lastUpdate = 0;
        RecipeIdentityUseCasePersistenceModel model = new RecipeIdentityUseCasePersistenceModel.Builder().
                getDefault().build();
        for (RecipeIdentityUseCasePersistenceModel m : getAllByDomainId(domainId)) {
            if (m.getLastUpdate() > lastUpdate) {
                model = m;
                lastUpdate = m.getLastUpdate();
            }
        }
        return model;
    }
}





