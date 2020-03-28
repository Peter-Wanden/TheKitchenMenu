package com.example.peter.thekitchenmenu.ui.detail.recipe.recipeingredientlist;

import androidx.databinding.ObservableField;
import androidx.lifecycle.ViewModel;

import com.example.peter.thekitchenmenu.domain.usecase.UseCase;
import com.example.peter.thekitchenmenu.domain.usecase.UseCaseHandler;
import com.example.peter.thekitchenmenu.domain.usecase.recipe.component.recipeidentity.RecipeIdentity;
import com.example.peter.thekitchenmenu.domain.usecase.recipe.component.recipeidentity.RecipeIdentityRequest;
import com.example.peter.thekitchenmenu.domain.usecase.recipe.component.recipeidentity.RecipeIdentityResponse;
import com.example.peter.thekitchenmenu.domain.usecase.recipe.component.recipeportions.RecipePortions;
import com.example.peter.thekitchenmenu.domain.usecase.recipe.component.recipeportions.RecipePortionsRequest;
import com.example.peter.thekitchenmenu.domain.usecase.recipe.component.recipeportions.RecipePortionsResponse;

public class RecipeNameAndPortionsViewModel extends ViewModel {

    private UseCaseHandler handler;
    private RecipeIdentity recipeIdentity;
    private RecipePortions recipePortions;

    public final ObservableField<String> recipeTitleObservable = new ObservableField<>();
    public final ObservableField<String> servingsObservable = new ObservableField<>();
    public final ObservableField<String> sittingsObservable = new ObservableField<>();
    public final ObservableField<String> portionsObservable = new ObservableField<>();

    public RecipeNameAndPortionsViewModel(UseCaseHandler handler,
                                          RecipeIdentity recipeIdentity,
                                          RecipePortions recipePortions) {
        this.handler = handler;
        this.recipeIdentity = recipeIdentity;
        this.recipePortions = recipePortions;
    }

    public void start(String recipeId) {
        getRecipeIdentityData(recipeId);
        getRecipePortionsData(recipeId);
    }

    private void getRecipeIdentityData(String recipeId) {
        RecipeIdentityRequest request = new RecipeIdentityRequest.Builder().
                getDefault().
                setDataId(recipeId).
                build();

        handler.execute(
                recipeIdentity,
                request,
                new UseCase.Callback<RecipeIdentityResponse>() {

            @Override
            public void onSuccess(RecipeIdentityResponse response) {
                setIdentityToView(response.getModel());
            }

            @Override
            public void onError(RecipeIdentityResponse response) {

            }
        });
    }

    private void setIdentityToView(RecipeIdentityResponse.Model model) {
        recipeTitleObservable.set(model.getTitle());
    }

    private void getRecipePortionsData(String recipeId) {
        RecipePortionsRequest request = new RecipePortionsRequest.Builder().
                getDefault().
                setDataId(recipeId).
                build();

        handler.execute(
                recipePortions,
                request,
                new UseCase.Callback<RecipePortionsResponse>() {

            @Override
            public void onSuccess(RecipePortionsResponse response) {
                setPortionsToView(response.getModel());
            }

            @Override
            public void onError(RecipePortionsResponse response) {

            }
        });
    }

    private void setPortionsToView(RecipePortionsResponse.Model model) {
        servingsObservable.set(String.valueOf(model.getServings()));
        sittingsObservable.set(String.valueOf(model.getSittings()));
        portionsObservable.set(String.valueOf(model.getPortions()));
    }
}
