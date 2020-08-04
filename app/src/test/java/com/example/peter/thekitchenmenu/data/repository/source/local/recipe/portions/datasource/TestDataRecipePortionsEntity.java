package com.example.peter.thekitchenmenu.data.repository.source.local.recipe.portions.datasource;

import com.example.peter.thekitchenmenu.data.repository.recipe.portions.TestDataRecipePortions;
import com.example.peter.thekitchenmenu.domain.usecasenew.recipe.component.portions.RecipePortionsUseCasePersistenceModel;

import java.util.ArrayList;
import java.util.List;

public class TestDataRecipePortionsEntity {

    public static RecipePortionsEntity getNewValidEmpty() {
        return convertModelToEntity(TestDataRecipePortions.
                getNewActiveDefault()
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
                getNewInvalidServingsTooHighSittingsTooHigh()
        );
    }

    public static RecipePortionsEntity getNewInvalidTooHighServingsValidSittings() {
        return convertModelToEntity(TestDataRecipePortions.
                getNewInvalidServingsTooHighSittingsValid()
        );
    }

    public static RecipePortionsEntity getNewValidServingsInvalidTooHighSittings() {
        return convertModelToEntity(TestDataRecipePortions.
                getNewInvalid_validServingsInvalidSittingsTooHigh()
        );
    }

    public static RecipePortionsEntity getNewValidServingsValidSittings() {
        return convertModelToEntity(TestDataRecipePortions.
                getNewValidServingsValidSittings()
        );
    }

    public static List<RecipePortionsEntity> getAllNew() {
        List<RecipePortionsEntity> entities = new ArrayList<>();
        for (RecipePortionsUseCasePersistenceModel m : TestDataRecipePortions.getAllNew()) {
            entities.add(convertModelToEntity(m));
        }
        return entities;
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

    public static List<RecipePortionsEntity> getAllExisting() {
        List<RecipePortionsEntity> entities = new ArrayList<>();
        for (RecipePortionsUseCasePersistenceModel m : TestDataRecipePortions.getAllExisting()) {
            entities.add(convertModelToEntity(m));
        }
        return entities;
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

    public static List<RecipePortionsEntity> getAllFromAnotherUser() {
        List<RecipePortionsEntity> entities = new ArrayList<>();
        for (RecipePortionsUseCasePersistenceModel m : TestDataRecipePortions.getAllFromAnotherUser()) {
            entities.add(convertModelToEntity(m));
        }
        return entities;
    }

    public static List<RecipePortionsEntity> getAll() {
        List<RecipePortionsEntity> entities = new ArrayList<>();
        entities.addAll(getAllNew());
        entities.addAll(getAllExisting());
        entities.addAll(getAllFromAnotherUser());
        return entities;
    }

    public static List<RecipePortionsEntity> getAllByDomainId(String domainId) {
        List<RecipePortionsEntity> entities = new ArrayList<>();
        for (RecipePortionsUseCasePersistenceModel m : TestDataRecipePortions.getAll()) {
            if (domainId.equals(m.getDomainId())) {
                entities.add(convertModelToEntity(m));
            }
        }
        return entities;
    }

    private static RecipePortionsEntity convertModelToEntity(RecipePortionsUseCasePersistenceModel m) {
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
