package com.example.peter.thekitchenmenu.domain.usecase.recipe.recipeingredientlist;

import com.example.peter.thekitchenmenu.data.repository.DomainDataAccess;
import com.example.peter.thekitchenmenu.data.repository.ingredient.RepositoryIngredient;
import com.example.peter.thekitchenmenu.data.repository.recipe.RepositoryRecipeIngredient;
import com.example.peter.thekitchenmenu.data.repository.recipe.RepositoryRecipePortions;
import com.example.peter.thekitchenmenu.domain.usecase.common.UseCaseBase;
import com.example.peter.thekitchenmenu.domain.entity.model.MeasurementModel;
import com.example.peter.thekitchenmenu.domain.entity.model.MeasurementModelBuilder;
import com.example.peter.thekitchenmenu.domain.entity.unitofmeasure.UnitOfMeasure;
import com.example.peter.thekitchenmenu.domain.usecase.ingredient.IngredientPersistenceDomainModel;
import com.example.peter.thekitchenmenu.domain.usecase.recipe.component.portions.RecipePortionsPersistenceDomainModel;
import com.example.peter.thekitchenmenu.domain.usecase.recipe.recipeingredient.RecipeIngredientPersistenceDomainModel;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Returns a list of ingredients for a given recipeId
 */
public class RecipeIngredientList extends UseCaseBase {

    private static final String TAG = "tkm-" + RecipeIngredientList.class.getSimpleName() + ": ";

    private RepositoryRecipeIngredient repoRecipeIngredient;
    private RepositoryIngredient repoIngredient;
    private RepositoryRecipePortions repoPortions;

    private String recipeId;
    private Map<String, RecipeIngredientPersistenceDomainModel> recipeIngredientQuantities =
            new LinkedHashMap<>();
    private Map<String, IngredientPersistenceDomainModel> ingredients = new LinkedHashMap<>();
    private List<RecipeIngredientListItemModel> listItemModels = new ArrayList<>();
    private int portions;

    public RecipeIngredientList(RepositoryRecipeIngredient repoRecipeIngredient,
                                RepositoryIngredient repoIngredient,
                                RepositoryRecipePortions repoPortions) {
        this.repoRecipeIngredient = repoRecipeIngredient;
        this.repoIngredient = repoIngredient;
        this.repoPortions = repoPortions;
    }

    @Override
    protected <Q extends Request> void execute(Q request) {
        RecipeIngredientListRequest rir = (RecipeIngredientListRequest) request;

        System.out.println(TAG + rir);
        recipeId = rir.getRecipeId();
        getPortionsForRecipe();
    }

    private void getPortionsForRecipe() {
        repoPortions.getActiveByDomainId(
                recipeId,
                new DomainDataAccess.GetDomainModelCallback<RecipePortionsPersistenceDomainModel>() {
                    @Override
                    public void onPersistenceModelLoaded(RecipePortionsPersistenceDomainModel model) {
                        RecipeIngredientList.this.portions =
                                model.getServings() * model.getSittings();
                        getRecipeIngredientQuantities();
                    }

                    @Override
                    public void onPersistenceModelUnavailable() {

                    }
                }
        );
    }

    private void getRecipeIngredientQuantities() {
        recipeIngredientQuantities.clear();

        repoRecipeIngredient.getAllByRecipeId(
                recipeId,
                new DomainDataAccess.GetAllDomainModelsCallback<RecipeIngredientPersistenceDomainModel>() {
                    @Override
                    public void onAllDomainModelsLoaded(List<RecipeIngredientPersistenceDomainModel> models) {
                        for (RecipeIngredientPersistenceDomainModel m : models) {
                            recipeIngredientQuantities.put(m.getIngredientDomainId(), m);
                        }
                        getRecipeIngredients();
                    }

                    @Override
                    public void onDomainModelsUnavailable() {

                    }
                }
        );
    }

    private void getRecipeIngredients() {
        ingredients.clear();
        for (String ingredientId : recipeIngredientQuantities.keySet()) {
            getIngredient(ingredientId);
        }
    }

    private void getIngredient(String ingredientId) {
        repoIngredient.getByDataId(
                ingredientId,
                new DomainDataAccess.GetDomainModelCallback<IngredientPersistenceDomainModel>() {
                    @Override
                    public void onPersistenceModelLoaded(IngredientPersistenceDomainModel model) {
                        ingredients.put(model.getDataId(), model);
                        createResponseModels();
                    }

                    @Override
                    public void onPersistenceModelUnavailable() {

                    }
                }
        );
    }

    private void createResponseModels() {
        if (isIngredientsFinishedLoading()) {
            listItemModels.clear();
            for (String ingredientId : recipeIngredientQuantities.keySet()) {

                RecipeIngredientPersistenceDomainModel recipeIngredient = recipeIngredientQuantities.
                        get(ingredientId);

                IngredientPersistenceDomainModel ingredient = ingredients.get(ingredientId);

                RecipeIngredientListItemModel listItemModel = new RecipeIngredientListItemModel(
                        recipeIngredient.getDataId(),
                        ingredientId,
                        ingredient.getName(),
                        ingredient.getDescription(),
                        getMeasurementModel(recipeIngredient, ingredient)
                );
                listItemModels.add(listItemModel);
            }
            RecipeIngredientListResponse response = new RecipeIngredientListResponse(listItemModels);
            System.out.println(TAG + response);
            getUseCaseCallback().onUseCaseSuccess(response);
        }
    }

    private boolean isIngredientsFinishedLoading() {
        return recipeIngredientQuantities.keySet().equals(ingredients.keySet());
    }

    private MeasurementModel getMeasurementModel(RecipeIngredientPersistenceDomainModel recipeIngredient,
                                                 IngredientPersistenceDomainModel ingredient) {

        UnitOfMeasure unitOfMeasure = recipeIngredient.
                getMeasurementModel().
                getSubtype().
                getMeasurementClass();
        unitOfMeasure.isNumberOfItemsSet(portions);

        if (unitOfMeasure.isConversionFactorEnabled()) {
            unitOfMeasure.isConversionFactorSet(ingredient.getConversionFactor());
        }
        unitOfMeasure.isItemBaseUnitsSet(recipeIngredient.getMeasurementModel().getItemBaseUnits());

        return MeasurementModelBuilder.basedOnUnitOfMeasure(unitOfMeasure).build();
    }
}