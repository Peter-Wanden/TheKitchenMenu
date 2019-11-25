package com.example.peter.thekitchenmenu.domain.usecase;

import androidx.annotation.NonNull;

import com.example.peter.thekitchenmenu.data.entity.IngredientEntity;
import com.example.peter.thekitchenmenu.data.entity.RecipeIngredientQuantityEntity;
import com.example.peter.thekitchenmenu.data.entity.RecipePortionsEntity;
import com.example.peter.thekitchenmenu.data.repository.DataSource;
import com.example.peter.thekitchenmenu.data.repository.RepositoryIngredient;
import com.example.peter.thekitchenmenu.data.repository.RepositoryRecipeIngredient;
import com.example.peter.thekitchenmenu.data.repository.RepositoryRecipePortions;
import com.example.peter.thekitchenmenu.domain.entity.model.MeasurementModel;
import com.example.peter.thekitchenmenu.domain.entity.model.MeasurementModelBuilder;
import com.example.peter.thekitchenmenu.domain.entity.unitofmeasure.MeasurementSubtype;
import com.example.peter.thekitchenmenu.domain.entity.unitofmeasure.UnitOfMeasure;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class UseCaseRecipeIngredientList extends
        UseCase<UseCaseRecipeIngredientList.RequestValues,
                UseCaseRecipeIngredientList.ResponseValues> {

    private RepositoryRecipeIngredient repoRecipeIngredient;
    private RepositoryIngredient repoIngredient;
    private RepositoryRecipePortions repoPortions;

    private String recipeId;
    private Map<String, RecipeIngredientQuantityEntity> recipeIngredientQuantities =
            new LinkedHashMap<>();
    private Map<String, IngredientEntity> ingredients =
            new LinkedHashMap<>();
    private List<RecipeIngredientListItemModel> listItemModels =
            new ArrayList<>();
    private int portions;

    public UseCaseRecipeIngredientList(RepositoryRecipeIngredient repoRecipeIngredient,
                                       RepositoryIngredient repoIngredient,
                                       RepositoryRecipePortions repoPortions) {
        this.repoRecipeIngredient = repoRecipeIngredient;
        this.repoIngredient = repoIngredient;
        this.repoPortions = repoPortions;
    }

    @Override
    protected void executeUseCase(UseCaseRecipeIngredientList.RequestValues requestValues) {
        recipeId = requestValues.getRecipeId();
        getPortionsForRecipe();
    }

    private void getPortionsForRecipe() {
        repoPortions.getPortionsForRecipe(
                recipeId,
                new DataSource.GetEntityCallback<RecipePortionsEntity>() {
            @Override
            public void onEntityLoaded(RecipePortionsEntity portions) {
                UseCaseRecipeIngredientList.this.portions =
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
                        ingredient.getName(),
                        ingredient.getDescription(),
                        getMeasurementModel(recipeIngredient, ingredient)
                );

                System.out.println("tkm-adding model:" + listItemModel);

                listItemModels.add(listItemModel);
            }

//            for (RecipeIngredientListItemModel model : listItemModels) {
//                System.out.println("tkm-ListItemModels=" + model);
//            }

            System.out.println("tkm- " + listItemModels.size() + " models added");

            getUseCaseCallback().onSuccess(
                    new UseCaseRecipeIngredientList.ResponseValues(listItemModels));
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

    public static final class RequestValues implements UseCase.RequestValues {
        @NonNull
        String recipeId;

        public RequestValues(@NonNull String recipeId) {
            this.recipeId = recipeId;
        }

        @NonNull
        public String getRecipeId() {
            return recipeId;
        }

        @Override
        public String toString() {
            return "RequestValues{" +
                    "recipeId='" + recipeId + '\'' +
                    '}';
        }
    }

    public static final class ResponseValues implements  UseCase.ResponseValues {
        private List<RecipeIngredientListItemModel> listItemModels;

        public ResponseValues(List<RecipeIngredientListItemModel> listItemModels) {
            this.listItemModels = listItemModels;
        }

        public List<RecipeIngredientListItemModel> getListItemModels() {
            return listItemModels;
        }

        @Override
        public String toString() {
            return "ResponseValues{" +
                    "listItemModels=" + listItemModels +
                    '}';
        }
    }
}