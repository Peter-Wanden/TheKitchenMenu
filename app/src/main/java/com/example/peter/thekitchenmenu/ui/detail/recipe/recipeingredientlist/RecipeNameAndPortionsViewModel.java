package com.example.peter.thekitchenmenu.ui.detail.recipe.recipeingredientlist;

import androidx.databinding.ObservableField;
import androidx.lifecycle.ViewModel;

import com.example.peter.thekitchenmenu.domain.UseCaseCommand;
import com.example.peter.thekitchenmenu.domain.UseCaseHandler;
import com.example.peter.thekitchenmenu.domain.usecase.recipeidentity.RecipeIdentity;
import com.example.peter.thekitchenmenu.domain.usecase.recipeidentity.RecipeIdentityModel;
import com.example.peter.thekitchenmenu.domain.usecase.recipeidentity.RecipeIdentityRequest;
import com.example.peter.thekitchenmenu.domain.usecase.recipeidentity.RecipeIdentityResponse;
import com.example.peter.thekitchenmenu.domain.usecase.recipeportions.UseCaseRecipePortions;

public class RecipeNameAndPortionsViewModel extends ViewModel {

    private UseCaseHandler handler;
    private RecipeIdentity identity;
    private UseCaseRecipePortions portions;

    public final ObservableField<String> recipeTitleObservable = new ObservableField<>();
    public final ObservableField<String> servingsObservable = new ObservableField<>();
    public final ObservableField<String> sittingsObservable = new ObservableField<>();
    public final ObservableField<String> portionsObservable = new ObservableField<>();

    public RecipeNameAndPortionsViewModel(UseCaseHandler handler,
                                          RecipeIdentity identity,
                                          UseCaseRecipePortions portions) {
        this.handler = handler;
        this.identity = identity;
        this.portions = portions;
    }

    public void start(String recipeId) {
        getRecipeIdentityData(recipeId);
        getRecipePortionsData(recipeId);
    }

    private void getRecipeIdentityData(String recipeId) {
        RecipeIdentityRequest request = new RecipeIdentityRequest.Builder().
                getDefault().
                setRecipeId(recipeId).
                build();

        handler.execute(
                identity,
                request,
                new UseCaseCommand.Callback<RecipeIdentityResponse>() {

            @Override
            public void onSuccess(RecipeIdentityResponse response) {
                setIdentityToView(response.getModel());
            }

            @Override
            public void onError(RecipeIdentityResponse response) {

            }
        });
    }

    private void setIdentityToView(RecipeIdentityModel model) {
        recipeTitleObservable.set(model.getTitle());
    }

    private void getRecipePortionsData(String recipeId) {
        UseCaseRecipePortions.Request request = new UseCaseRecipePortions.Request.Builder().
                getDefault().
                setRecipeId(recipeId).
                build();

        handler.execute(
                portions,
                request,
                new UseCaseCommand.Callback<UseCaseRecipePortions.Response>() {

            @Override
            public void onSuccess(UseCaseRecipePortions.Response response) {
                setPortionsToView(response.getModel());
            }

            @Override
            public void onError(UseCaseRecipePortions.Response response) {

            }
        });
    }

    private void setPortionsToView(UseCaseRecipePortions.Model model) {
        servingsObservable.set(String.valueOf(model.getServings()));
        sittingsObservable.set(String.valueOf(model.getSittings()));
        portionsObservable.set(String.valueOf(calculatePortions(model)));
    }

    private int calculatePortions(UseCaseRecipePortions.Model model) {
        return model.getServings() * model.getSittings();
    }
}
