package com.example.peter.thekitchenmenu.domain.usecase.recipeingredientlist;

import com.example.peter.thekitchenmenu.data.entity.IngredientEntity;
import com.example.peter.thekitchenmenu.data.entity.RecipeIngredientQuantityEntity;
import com.example.peter.thekitchenmenu.data.entity.RecipePortionsEntity;
import com.example.peter.thekitchenmenu.data.repository.DataSource;
import com.example.peter.thekitchenmenu.data.repository.RepositoryIngredient;
import com.example.peter.thekitchenmenu.data.repository.RepositoryRecipeIngredient;
import com.example.peter.thekitchenmenu.data.repository.RepositoryRecipePortions;
import com.example.peter.thekitchenmenu.domain.UseCase;
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
public class RecipeIngredientList extends
        UseCase<RecipeIngredientListRequest, RecipeIngredientListResponse> {

    private static final String TAG = "tkm-" + RecipeIngredientList.class.getSimpleName() + ": ";

    private RepositoryRecipeIngredient repoRecipeIngredient;
    private RepositoryIngredient repoIngredient;
    private RepositoryRecipePortions repoPortions;

    private String recipeId;
    private Map<String, RecipeIngredientQuantityEntity> recipeIngredientQuantities =
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
    protected void execute(RecipeIngredientListRequest request) {
        System.out.println(TAG + request);
        recipeId = request.getRecipeId();
        getPortionsForRecipe();
    }

    private void getPortionsForRecipe() {
        repoPortions.getPortionsForRecipe(
                recipeId,
                new DataSource.GetEntityCallback<RecipePortionsEntity>() {
                    @Override
                    public void onEntityLoaded(RecipePortionsEntity portions) {
                        RecipeIngredientList.this.portions =
                                portions.getServings() * portions.getSittings();
                        getRecipeIngredientQuantities();
                    }

                    @Override
                    public void onDataNotAvailable() {

                    }
                });
    }

    private void getRecipeIngredientQuantities() {
        recipeIngredientQuantities.clear();

        repoRecipeIngredient.getByRecipeId(
                recipeId,
                new DataSource.GetAllCallback<RecipeIngredientQuantityEntity>() {
                    @Override
                    public void onAllLoaded(List<RecipeIngredientQuantityEntity> entities) {
                        for (RecipeIngredientQuantityEntity entity : entities) {
                            recipeIngredientQuantities.put(entity.getIngredientId(), entity);
                        }
                        getRecipeIngredients();
                    }

                    @Override
                    public void onDataNotAvailable() {

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
        repoIngredient.getById(ingredientId, new DataSource.GetEntityCallback<IngredientEntity>() {
            @Override
            public void onEntityLoaded(IngredientEntity ingredient) {
                ingredients.put(ingredient.getId(), ingredient);
                createResponseModels();
            }

            @Override
            public void onDataNotAvailable() {

            }
        });
    }

    private void createResponseModels() {
        if (isIngredientsFinishedLoading()) {
            listItemModels.clear();
            for (String ingredientId : recipeIngredientQuantities.keySet()) {

                RecipeIngredientQuantityEntity recipeIngredient = recipeIngredientQuantities.
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

    private MeasurementModel getMeasurementModel(RecipeIngredientQuantityEntity quantityEntity,
                                                 IngredientEntity ingredient) {

        UnitOfMeasure unitOfMeasure = MeasurementSubtype.fromInt(quantityEntity.
                getUnitOfMeasureSubtype()).getMeasurementClass();
        unitOfMeasure.isNumberOfItemsSet(portions);

        if (unitOfMeasure.isConversionFactorEnabled()) {
            unitOfMeasure.isConversionFactorSet(ingredient.getConversionFactor());
        }
        unitOfMeasure.isItemBaseUnitsSet(quantityEntity.getItemBaseUnits());

        return MeasurementModelBuilder.basedOnUnitOfMeasure(unitOfMeasure).build();
    }
}