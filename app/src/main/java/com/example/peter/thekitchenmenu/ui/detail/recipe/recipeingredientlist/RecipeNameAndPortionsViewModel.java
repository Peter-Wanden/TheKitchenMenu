package com.example.peter.thekitchenmenu.ui.detail.recipe.recipeingredientlist;

import androidx.databinding.ObservableField;
import androidx.lifecycle.ViewModel;

import com.example.peter.thekitchenmenu.domain.usecase.common.UseCaseBase;
import com.example.peter.thekitchenmenu.domain.usecase.common.UseCaseHandler;
import com.example.peter.thekitchenmenu.domain.usecase.recipe.component.identity.RecipeIdentity;
import com.example.peter.thekitchenmenu.domain.usecase.recipe.component.identity.RecipeIdentityRequest;
import com.example.peter.thekitchenmenu.domain.usecase.recipe.component.identity.RecipeIdentityResponse;
import com.example.peter.thekitchenmenu.domain.usecase.recipe.component.portions.RecipePortions;
import com.example.peter.thekitchenmenu.domain.usecase.recipe.component.portions.RecipePortionsRequest;
import com.example.peter.thekitchenmenu.domain.usecase.recipe.component.portions.RecipePortionsResponse;

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

        handler.executeAsync(
                recipeIdentity,
                request,
                new UseCaseBase.Callback<RecipeIdentityResponse>() {

            @Override
            public void onUseCaseSuccess(RecipeIdentityResponse response) {
                setIdentityToView(response.getDomainModel());
            }

            @Override
            public void onUseCaseError(RecipeIdentityResponse response) {

            }
        });
    }

    private void setIdentityToView(RecipeIdentityResponse.DomainModel domainModel) {
        recipeTitleObservable.set(domainModel.getTitle());
    }

    private void getRecipePortionsData(String recipeId) {
        RecipePortionsRequest request = new RecipePortionsRequest.Builder().
                getDefault().
                setDataId(recipeId).
                build();

        handler.executeAsync(
                recipePortions,
                request,
                new UseCaseBase.Callback<RecipePortionsResponse>() {

            @Override
            public void onUseCaseSuccess(RecipePortionsResponse response) {
                setPortionsToView(response.getDomainModel());
            }

            @Override
            public void onUseCaseError(RecipePortionsResponse response) {

            }
        });
    }

    private void setPortionsToView(RecipePortionsResponse.Model model) {
        servingsObservable.set(String.valueOf(model.getServings()));
        sittingsObservable.set(String.valueOf(model.getSittings()));
        portionsObservable.set(String.valueOf(model.getPortions()));
    }
}
