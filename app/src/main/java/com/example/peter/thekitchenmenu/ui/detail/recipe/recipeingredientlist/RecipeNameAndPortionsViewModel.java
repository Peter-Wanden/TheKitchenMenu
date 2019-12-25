package com.example.peter.thekitchenmenu.ui.detail.recipe.recipeingredientlist;

import androidx.databinding.ObservableField;
import androidx.lifecycle.ViewModel;

import com.example.peter.thekitchenmenu.data.entity.RecipeIdentityEntity;
import com.example.peter.thekitchenmenu.data.entity.RecipePortionsEntity;
import com.example.peter.thekitchenmenu.data.repository.DataSource;
import com.example.peter.thekitchenmenu.data.repository.RepositoryRecipeIdentity;
import com.example.peter.thekitchenmenu.data.repository.RepositoryRecipePortions;
import com.example.peter.thekitchenmenu.domain.UseCaseCommand;
import com.example.peter.thekitchenmenu.domain.UseCaseHandler;
import com.example.peter.thekitchenmenu.domain.usecase.recipeidentity.UseCaseRecipeIdentity;
import com.example.peter.thekitchenmenu.domain.usecase.recipeportions.UseCaseRecipePortions;

public class RecipeNameAndPortionsViewModel extends ViewModel {

    private UseCaseHandler handler;
    private UseCaseRecipeIdentity identity;
    private UseCaseRecipePortions portions;

    public final ObservableField<String> recipeTitleObservable = new ObservableField<>();
    public final ObservableField<String> servingsObservable = new ObservableField<>();
    public final ObservableField<String> sittingsObservable = new ObservableField<>();
    public final ObservableField<String> portionsObservable = new ObservableField<>();

    public RecipeNameAndPortionsViewModel(UseCaseHandler handler,
                                          UseCaseRecipeIdentity identity,
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
        UseCaseRecipeIdentity.Request request = new UseCaseRecipeIdentity.Request.Builder().
                getDefault().
                setRecipeId(recipeId).
                build();

        handler.execute(
                identity,
                request,
                new UseCaseCommand.Callback<UseCaseRecipeIdentity.Response>() {

            @Override
            public void onSuccess(UseCaseRecipeIdentity.Response response) {
                setIdentityToView(response.getModel());
            }

            @Override
            public void onError(UseCaseRecipeIdentity.Response response) {

            }
        });
    }

    private void setIdentityToView(UseCaseRecipeIdentity.Model model) {
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
