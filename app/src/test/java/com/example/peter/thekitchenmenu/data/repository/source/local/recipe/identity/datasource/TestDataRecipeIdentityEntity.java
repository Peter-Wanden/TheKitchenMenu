package com.example.peter.thekitchenmenu.data.repository.source.local.recipe.identity.datasource;

import com.example.peter.thekitchenmenu.data.repository.recipe.identity.TestDataRecipeIdentity;
import com.example.peter.thekitchenmenu.domain.usecase.recipe.component.identity.RecipeIdentity;
import com.example.peter.thekitchenmenu.domain.usecase.recipe.component.identity.RecipeIdentityPersistenceModel;

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

    public static List<RecipeIdentityEntity> getAllNew() {
        List<RecipeIdentityEntity> entities = new ArrayList<>();
        for (RecipeIdentityPersistenceModel m : TestDataRecipeIdentity.getAllNew()) {
            entities.add(convertModelToEntity(m)
            );
        }
        return entities;
    }

    public static List<RecipeIdentityEntity> getAllByDomainId(String domainId) {
        List<RecipeIdentityEntity> entities = new ArrayList<>();
        for (RecipeIdentityPersistenceModel m : TestDataRecipeIdentity.getAllByDomainId(domainId)) {
            entities.add(convertModelToEntity(m)
            );
        }
        return entities;
    }

    public static RecipeIdentityEntity getInvalidExistingTitleTooShort() {
        return convertModelToEntity(
                TestDataRecipeIdentity.getInvalidExistingTitleTooShort()
        );
    }

    public static RecipeIdentityEntity getInvalidExistingTitleTooLong() {
        return convertModelToEntity(
                TestDataRecipeIdentity.getInvalidExistingTitleTooLong()
        );
    }

    public static RecipeIdentityEntity getInvalidExistingTitleValidDescriptionTooLong() {
        return convertModelToEntity(
                TestDataRecipeIdentity.getInvalidExistingTitleValidDescriptionTooLong()
        );
    }

    public static RecipeIdentityEntity getInvalidExistingTitleTooShortDescriptionTooLong() {
        return convertModelToEntity(
                TestDataRecipeIdentity.getInvalidExistingTitleTooShortDescriptionTooLong()
        );
    }

    public static RecipeIdentityEntity getInvalidExistingTitleTooLongDescriptionTooLong() {
        return convertModelToEntity(
                TestDataRecipeIdentity.getInvalidExistingTitleTooLongDescriptionTooLong()
        );
    }

    public static RecipeIdentityEntity getValidExistingTitleValidDescriptionValid() {
        return convertModelToEntity(
                TestDataRecipeIdentity.getValidExistingTitleValidDescriptionValid()
        );
    }

    public static RecipeIdentityEntity getValidExistingTitleValid() {
        return convertModelToEntity(
                TestDataRecipeIdentity.getValidExistingTitleValid()
        );
    }

    public static RecipeIdentityEntity getValidCompleteFromAnotherUser() {
        return convertModelToEntity(
                TestDataRecipeIdentity.getValidCompleteFromAnotherUser()
        );
    }

    public static RecipeIdentityEntity getInvalidFromAnotherUser() {
        return convertModelToEntity(
                TestDataRecipeIdentity.getInvalidFromAnotherUser()
        );
    }

    public static RecipeIdentityEntity getValidCompleteAfterCopied() {
        return convertModelToEntity(
                TestDataRecipeIdentity.getValidCompleteAfterCopied()
        );
    }

    public static RecipeIdentityEntity getValidAfterInvalidCopy() {
        return convertModelToEntity(
                TestDataRecipeIdentity.getValidAfterInvalidCopy()
        );
    }

    public static RecipeIdentityEntity getValidCopiedDescriptionUpdated() {
        return convertModelToEntity(
                TestDataRecipeIdentity.getValidCopiedDescriptionUpdated()
        );
    }

    public static List<RecipeIdentityEntity> getValidIdentityEntities() {
        List<RecipeIdentityEntity> entities = new ArrayList<>();
        for (RecipeIdentityPersistenceModel m : TestDataRecipeIdentity.getAllValidModels()) {
            entities.add(convertModelToEntity(m));
        }
        return entities;
    }

    public static List<RecipeIdentityEntity> getAll() {
        List<RecipeIdentityEntity> entities = new ArrayList<>();
        for (RecipeIdentityPersistenceModel m : TestDataRecipeIdentity.getAll()) {
            entities.add(convertModelToEntity(m));
        }
        return entities;
    }

    private static RecipeIdentityEntity convertModelToEntity(RecipeIdentityPersistenceModel m) {
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
