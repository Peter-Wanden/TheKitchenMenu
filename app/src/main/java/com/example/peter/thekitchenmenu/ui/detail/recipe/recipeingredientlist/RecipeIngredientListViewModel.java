package com.example.peter.thekitchenmenu.ui.detail.recipe.recipeingredientlist;

import androidx.databinding.ObservableArrayList;
import androidx.databinding.ObservableBoolean;
import androidx.databinding.ObservableList;
import androidx.lifecycle.ViewModel;

import com.example.peter.thekitchenmenu.domain.usecase.UseCaseBase;
import com.example.peter.thekitchenmenu.domain.usecase.UseCaseHandler;
import com.example.peter.thekitchenmenu.domain.usecase.recipe.recipeingredientlist.RecipeIngredientListItemModel;
import com.example.peter.thekitchenmenu.domain.usecase.recipe.recipeingredientlist.RecipeIngredientList;
import com.example.peter.thekitchenmenu.domain.usecase.recipe.recipeingredientlist.RecipeIngredientListRequest;
import com.example.peter.thekitchenmenu.domain.usecase.recipe.recipeingredientlist.RecipeIngredientListResponse;
import com.example.peter.thekitchenmenu.ui.detail.recipe.recipeingredienteditor.RecipeIngredientEditorActivity;

public class RecipeIngredientListViewModel extends ViewModel {

    private static final String TAG = "tkm-" + RecipeIngredientListViewModel.class.getSimpleName()
            + ":";

    private RecipeIngredientListNavigator navigator;
    private UseCaseHandler handler;
    private RecipeIngredientList useCase;

    public final ObservableList<RecipeIngredientListItemModel> recipeIngredientsModels =
            new ObservableArrayList<>();
    public final ObservableBoolean hasIngredients = new ObservableBoolean(false);

    private String recipeId = "";

    public RecipeIngredientListViewModel(UseCaseHandler handler,
                                         RecipeIngredientList useCase) {
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
        if (this.recipeId.isEmpty() || !this.recipeId.equals(recipeId)) {
            this.recipeId = recipeId;
            loadRecipeIngredients();
        }
    }

    void onActivityResult(int requestCode, int resultCode) {
        if (requestCode == RecipeIngredientEditorActivity.REQUEST_ADD_RECIPE_INGREDIENT) {
            if (resultCode == RecipeIngredientEditorActivity.RESULT_OK) {
                loadRecipeIngredients();
            }
        }
    }

    private void loadRecipeIngredients() {
        handler.executeAsync(
                useCase,
                new RecipeIngredientListRequest(recipeId),
                new UseCaseBase.Callback<RecipeIngredientListResponse>() {
                    @Override
                    public void onSuccessResponse(RecipeIngredientListResponse response) {
                        if (response.getListItemModels().size() > 0) {
                            hasIngredients.set(true);
                            recipeIngredientsModels.clear();
                            recipeIngredientsModels.addAll(response.getListItemModels());
                        } else {
                            hasIngredients.set(false);
                        }
                    }

                    @Override
                    public void onErrorResponse(RecipeIngredientListResponse response) {
                        hasIngredients.set(false);
                    }
                });
    }

    public void addIngredientButtonPressed() {
        navigator.addRecipeIngredient(recipeId);
    }

    void deleteRecipeIngredient(String recipeIngredientId) {
    }
}
