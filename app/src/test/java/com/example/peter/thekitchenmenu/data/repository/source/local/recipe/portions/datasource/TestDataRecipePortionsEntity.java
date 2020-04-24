package com.example.peter.thekitchenmenu.data.repository.source.local.recipe.portions.datasource;

import com.example.peter.thekitchenmenu.data.repository.recipe.portions.TestDataRecipePortions;
import com.example.peter.thekitchenmenu.domain.usecase.recipe.component.portions.RecipePortionsPersistenceModel;

public class TestDataRecipePortionsEntity {

    public static RecipePortionsEntity getNewValidEmpty() {
        return convertModelToEntity(TestDataRecipePortions.
                getNewValidEmpty()
        );
    }

    public static RecipePortionsEntity getNewValidFourPortions() {
        return convertModelToEntity(TestDataRecipePortions.
                getNewValidFourPortions()
        );
    }

    public static RecipePortionsEntity getNewValidSixteenPortions() {
        return convertModelToEntity(TestDataRecipePortions.
                getNewValidSixteenPortions()
        );
    }

    public static RecipePortionsEntity getNewInvalidTooHighServingsInvalidTooHighSittings() {
        return convertModelToEntity(TestDataRecipePortions.
                getNewInvalidTooHighServingsInvalidTooHighSittings()
        );
    }

    public static RecipePortionsEntity getNewInvalidTooHighServingsValidSittings() {
        return convertModelToEntity(TestDataRecipePortions.
                getNewInvalidTooHighServingsValidSittings()
        );
    }

    public static RecipePortionsEntity getNewValidServingsInvalidTooHighSittings() {
        return convertModelToEntity(TestDataRecipePortions.
                getNewValidServingsInvalidTooHighSittings()
        );
    }

    public static RecipePortionsEntity getNewValidServingsValidSittings() {
        return convertModelToEntity(TestDataRecipePortions.
                getNewValidServingsValidSittings()
        );
    }

    public static RecipePortionsEntity getExistingInvalidTooHighSittingsInvalidTooHighServings() {
        return convertModelToEntity(TestDataRecipePortions.
                getExistingInvalidTooHighSittingsInvalidTooHighServings()
        );
    }

    public static RecipePortionsEntity getExistingValidNinePortions() {
        return convertModelToEntity(TestDataRecipePortions.
                getExistingValidNinePortions()
        );
    }

    public static RecipePortionsEntity getExistingValidUpdatedServings() {
        return convertModelToEntity(TestDataRecipePortions.
                getExistingValidUpdatedServings()
        );
    }

    public static RecipePortionsEntity getExistingValidUpdatedSittings() {
        return convertModelToEntity(TestDataRecipePortions.
                getExistingValidUpdatedSittings()
        );
    }

    public static RecipePortionsEntity getExistingValidCopied() {
        return convertModelToEntity(TestDataRecipePortions.
                getExistingValidCopied()
        );
    }

    public static RecipePortionsEntity getExistingCopiedUpdatedSittingsServings() {
        return convertModelToEntity(TestDataRecipePortions.
                getExistingCopiedUpdatedSittingsServings()
        );
    }

    public static RecipePortionsEntity getValidFromAnotherUser() {
        return convertModelToEntity(TestDataRecipePortions.
                getValidFromAnotherUser()
        );
    }

    public static RecipePortionsEntity getValidCopiedFromAnotherUser() {
        return convertModelToEntity(TestDataRecipePortions.
                getValidCopiedFromAnotherUser()
        );
    }

    private static RecipePortionsEntity convertModelToEntity(RecipePortionsPersistenceModel m) {
        return new RecipePortionsEntity(
                m.getDataId(),
                m.getDomainId(),
                m.getServings(),
                m.getSittings(),
                m.getCreateDate(),
                m.getLastUpdate()
        );
    }
}
