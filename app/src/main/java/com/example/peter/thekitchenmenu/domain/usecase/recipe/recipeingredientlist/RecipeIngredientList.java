package com.example.peter.thekitchenmenu.domain.usecase.recipe.recipeingredientlist;

import com.example.peter.thekitchenmenu.data.repository.DomainDataAccess;
import com.example.peter.thekitchenmenu.data.repository.ingredient.DataAccessIngredient;
import com.example.peter.thekitchenmenu.data.repository.recipe.DataAccessRecipeIngredient;
import com.example.peter.thekitchenmenu.data.repository.recipe.RecipePortionsUseCasseDataAccess;
import com.example.peter.thekitchenmenu.domain.usecase.common.UseCaseBase;
import com.example.peter.thekitchenmenu.domain.businessentity.unitofmeasure.model.MeasurementModel;
import com.example.peter.thekitchenmenu.domain.businessentity.unitofmeasure.model.MeasurementModelBuilder;
import com.example.peter.thekitchenmenu.domain.businessentity.unitofmeasure.UnitOfMeasure;
import com.example.peter.thekitchenmenu.domain.usecase.ingredient.IngredientPersistenceModel;
import com.example.peter.thekitchenmenu.domain.usecasenew.recipe.component.portions.RecipePortionsUseCasePersistenceModel;
import com.example.peter.thekitchenmenu.domain.usecase.recipe.recipeingredient.RecipeIngredientPersistenceModel;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Returns a list of ingredients for a given recipeId
 */
public class RecipeIngredientList extends UseCaseBase {

    private static final String TAG = "tkm-" + RecipeIngredientList.class.getSimpleName() + ": ";

    private DataAccessRecipeIngredient repoRecipeIngredient;
    private DataAccessIngredient repoIngredient;
    private RecipePortionsUseCasseDataAccess repoPortions;

    private String recipeId;
    private Map<String, RecipeIngredientPersistenceModel> recipeIngredientQuantities =
            new LinkedHashMap<>();
    private Map<String, IngredientPersistenceModel> ingredients = new LinkedHashMap<>();
    private List<RecipeIngredientListItemModel> listItemModels = new ArrayList<>();
    private int portions;

    public RecipeIngredientList(DataAccessRecipeIngredient repoRecipeIngredient,
                                DataAccessIngredient repoIngredient,
                                RecipePortionsUseCasseDataAccess repoPortions) {
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
        repoPortions.getByDomainId(
                recipeId,
                new DomainDataAccess.GetDomainModelCallback<RecipePortionsUseCasePersistenceModel>() {
                    @Override
                    public void onPersistenceModelLoaded(RecipePortionsUseCasePersistenceModel model) {
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
                new DomainDataAccess.GetAllDomainModelsCallback<RecipeIngredientPersistenceModel>() {
                    @Override
                    public void onAllDomainModelsLoaded(List<RecipeIngredientPersistenceModel> models) {
                        for (RecipeIngredientPersistenceModel m : models) {
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
                new DomainDataAccess.GetDomainModelCallback<IngredientPersistenceModel>() {
                    @Override
                    public void onPersistenceModelLoaded(IngredientPersistenceModel model) {
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

                RecipeIngredientPersistenceModel recipeIngredient = recipeIngredientQuantities.
                        get(ingredientId);

                IngredientPersistenceModel ingredient = ingredients.get(ingredientId);

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

    private MeasurementModel getMeasurementModel(RecipeIngredientPersistenceModel recipeIngredient,
                                                 IngredientPersistenceModel ingredient) {

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