package com.example.peter.thekitchenmenu.ui.detail.recipe.recipeingredientlist;

import androidx.databinding.ObservableArrayList;
import androidx.databinding.ObservableBoolean;
import androidx.databinding.ObservableList;
import androidx.lifecycle.ViewModel;

import com.example.peter.thekitchenmenu.domain.usecase.RecipeIngredientListItemModel;
import com.example.peter.thekitchenmenu.domain.usecase.UseCase;
import com.example.peter.thekitchenmenu.domain.usecase.UseCaseHandler;
import com.example.peter.thekitchenmenu.domain.usecase.UseCaseRecipeIngredientList;

public class RecipeIngredientListViewModel extends ViewModel {

    private RecipeIngredientListNavigator navigator;
    private UseCaseHandler handler;
    private UseCaseRecipeIngredientList useCase;

    public final ObservableList<RecipeIngredientListItemModel> recipeIngredientsModels =
            new ObservableArrayList<>();
    public final ObservableBoolean hasIngredients = new ObservableBoolean(false);

    private String recipeId;

    public RecipeIngredientListViewModel(UseCaseHandler handler,
                                         UseCaseRecipeIngredientList useCase) {
        this.handler = handler;
        this.useCase = useCase;
    }

    void setNavigator(RecipeIngredientListNavigator navigator) {
        this.navigator = navigator;
    }

    void onActivityDestroyed() {
        navigator = null;
    }

    void start(String recipeId) {
        this.recipeId = recipeId;
        loadRecipeIngredients();
    }

    private void loadRecipeIngredients() {
        handler.execute(
                useCase,
                new UseCaseRecipeIngredientList.RequestValues(recipeId),
                new UseCase.UseCaseCallback<UseCaseRecipeIngredientList.ResponseValues>() {
            @Override
            public void onSuccess(UseCaseRecipeIngredientList.ResponseValues response) {
                if (response.getListItemModels().size() > 0) {
                    hasIngredients.set(true);
                    recipeIngredientsModels.clear();
                    recipeIngredientsModels.addAll(response.getListItemModels());
                } else {
                    hasIngredients.set(false);
                }
            }

            @Override
            public void onError(UseCaseRecipeIngredientList.ResponseValues response) {
                hasIngredients.set(false);
            }
        });
    }

    public void addIngredientButtonPressed() {
        navigator.addRecipeIngredient(recipeId);
    }
}
