package com.example.peter.thekitchenmenu.ui.detail.recipe.recipeingredientlist;

import androidx.databinding.ObservableField;
import androidx.lifecycle.ViewModel;

import com.example.peter.thekitchenmenu.data.entity.RecipeIdentityEntity;
import com.example.peter.thekitchenmenu.data.entity.RecipePortionsEntity;
import com.example.peter.thekitchenmenu.data.repository.DataSource;
import com.example.peter.thekitchenmenu.data.repository.RepositoryRecipeIdentity;
import com.example.peter.thekitchenmenu.data.repository.RepositoryRecipePortions;

public class RecipeNameAndPortionsViewModel extends ViewModel {

    private RepositoryRecipeIdentity repositoryRecipeIdentity;
    private RepositoryRecipePortions repositoryRecipePortions;

    public final ObservableField<String> recipeTitleObservable = new ObservableField<>();
    public final ObservableField<String> servingsObservable = new ObservableField<>();
    public final ObservableField<String> sittingsObservable = new ObservableField<>();
    public final ObservableField<String> portionsObservable = new ObservableField<>();

    public RecipeNameAndPortionsViewModel(RepositoryRecipeIdentity repositoryRecipeIdentity,
                                          RepositoryRecipePortions repositoryRecipePortions) {
        this.repositoryRecipeIdentity = repositoryRecipeIdentity;
        this.repositoryRecipePortions = repositoryRecipePortions;
    }

    public void start(String recipeId) {
        getRecipeIdentity(recipeId);
        getRecipePortions(recipeId);
    }

    private void getRecipeIdentity(String recipeId) {
        repositoryRecipeIdentity.getById(
                recipeId,
                new DataSource.GetEntityCallback<RecipeIdentityEntity>() {
            @Override
            public void onEntityLoaded(RecipeIdentityEntity recipeIdentity) {
                setIdentityToView(recipeIdentity);
            }

            @Override
            public void onDataNotAvailable() {

            }
        });
    }

    private void setIdentityToView(RecipeIdentityEntity recipeIdentity) {
        recipeTitleObservable.set(recipeIdentity.getTitle());
    }

    private void getRecipePortions(String recipeId) {
        repositoryRecipePortions.getPortionsForRecipe(recipeId,
                new DataSource.GetEntityCallback<RecipePortionsEntity>() {
            @Override
            public void onEntityLoaded(RecipePortionsEntity recipePortions) {
                setPortionsToView(recipePortions);
            }

            @Override
            public void onDataNotAvailable() {

            }
        });
    }

    private void setPortionsToView(RecipePortionsEntity recipePortions) {
        servingsObservable.set(String.valueOf(recipePortions.getServings()));
        sittingsObservable.set(String.valueOf(recipePortions.getSittings()));
        portionsObservable.set(String.valueOf(
                recipePortions.getServings() * recipePortions.getSittings()));
    }
}
