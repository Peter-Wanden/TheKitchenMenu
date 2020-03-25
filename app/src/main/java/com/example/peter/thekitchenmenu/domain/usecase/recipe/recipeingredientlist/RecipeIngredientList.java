package com.example.peter.thekitchenmenu.domain.usecase.recipe.recipeingredientlist;

import com.example.peter.thekitchenmenu.data.primitivemodel.ingredient.IngredientEntity;
import com.example.peter.thekitchenmenu.data.primitivemodel.ingredient.RecipeIngredientEntity;
import com.example.peter.thekitchenmenu.data.primitivemodel.recipe.RecipePortionsEntity;
import com.example.peter.thekitchenmenu.data.repository.PrimitiveDataSource;
import com.example.peter.thekitchenmenu.data.repository.ingredient.RepositoryIngredient;
import com.example.peter.thekitchenmenu.data.repository.recipe.RepositoryRecipeIngredient;
import com.example.peter.thekitchenmenu.data.repository.recipe.RepositoryRecipePortions;
import com.example.peter.thekitchenmenu.domain.usecase.UseCase;
import com.example.peter.thekitchenmenu.domain.entity.model.MeasurementModel;
import com.example.peter.thekitchenmenu.domain.entity.model.MeasurementModelBuilder;
import com.example.peter.thekitchenmenu.domain.entity.unitofmeasure.MeasurementSubtype;
import com.example.peter.thekitchenmenu.domain.entity.unitofmeasure.UnitOfMeasure;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Returns a list of ingredients for a given recipeId
 */
public class RecipeIngredientList extends UseCase {

    private static final String TAG = "tkm-" + RecipeIngredientList.class.getSimpleName() + ": ";

    private RepositoryRecipeIngredient repoRecipeIngredient;
    private RepositoryIngredient repoIngredient;
    private RepositoryRecipePortions repoPortions;

    private String recipeId;
    private Map<String, RecipeIngredientEntity> recipeIngredientQuantities =
            new LinkedHashMap<>();
    private Map<String, IngredientEntity> ingredients = new LinkedHashMap<>();
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
        repoPortions.getByRecipeId(
                recipeId,
                new PrimitiveDataSource.GetEntityCallback<RecipePortionsEntity>() {
                    @Override
                    public void onEntityLoaded(RecipePortionsEntity entity) {
                        RecipeIngredientList.this.portions =
                                entity.getServings() * entity.getSittings();
                        getRecipeIngredientQuantities();
                    }

                    @Override
                    public void onDataUnavailable() {

                    }
                });
    }

    private void getRecipeIngredientQuantities() {
        recipeIngredientQuantities.clear();

        repoRecipeIngredient.getAllByRecipeId(
                recipeId,
                new PrimitiveDataSource.GetAllCallback<RecipeIngredientEntity>() {
                    @Override
                    public void onAllLoaded(List<RecipeIngredientEntity> entities) {
                        for (RecipeIngredientEntity entity : entities) {
                            recipeIngredientQuantities.put(entity.getIngredientId(), entity);
                        }
                        getRecipeIngredients();
                    }

                    @Override
                    public void onDataUnavailable() {

                    }
                });
    }

    private void getRecipeIngredients() {
        ingredients.clear();
        for (String ingredientId : recipeIngredientQuantities.keySet()) {
            getIngredient(ingredientId);
        }
    }

    private void getIngredient(String ingredientId) {
        repoIngredient.getById(ingredientId, new PrimitiveDataSource.GetEntityCallback<IngredientEntity>() {
            @Override
            public void onEntityLoaded(IngredientEntity entity) {
                ingredients.put(entity.getId(), entity);
                createResponseModels();
            }

            @Override
            public void onDataUnavailable() {

            }
        });
    }

    private void createResponseModels() {
        if (isIngredientsFinishedLoading()) {
            listItemModels.clear();
            for (String ingredientId : recipeIngredientQuantities.keySet()) {

                RecipeIngredientEntity recipeIngredient = recipeIngredientQuantities.
                        get(ingredientId);

                IngredientEntity ingredient = ingredients.get(ingredientId);

                RecipeIngredientListItemModel listItemModel = new RecipeIngredientListItemModel(
                        recipeIngredient.getId(),
                        ingredientId,
                        ingredient.getName(),
                        ingredient.getDescription(),
                        getMeasurementModel(recipeIngredient, ingredient)
                );
                listItemModels.add(listItemModel);
            }
            RecipeIngredientListResponse response = new RecipeIngredientListResponse(listItemModels);
            System.out.println(TAG + response);
            getUseCaseCallback().onSuccess(response);
        }
    }

    private boolean isIngredientsFinishedLoading() {
        return recipeIngredientQuantities.keySet().equals(ingredients.keySet());
    }

    private MeasurementModel getMeasurementModel(RecipeIngredientEntity quantityEntity,
                                                 IngredientEntity ingredient) {

        UnitOfMeasure unitOfMeasure = MeasurementSubtype.fromInt(quantityEntity.
                getMeasurementSubtype()).getMeasurementClass();
        unitOfMeasure.isNumberOfItemsSet(portions);

        if (unitOfMeasure.isConversionFactorEnabled()) {
            unitOfMeasure.isConversionFactorSet(ingredient.getConversionFactor());
        }
        unitOfMeasure.isItemBaseUnitsSet(quantityEntity.getItemBaseUnits());

        return MeasurementModelBuilder.basedOnUnitOfMeasure(unitOfMeasure).build();
    }
}