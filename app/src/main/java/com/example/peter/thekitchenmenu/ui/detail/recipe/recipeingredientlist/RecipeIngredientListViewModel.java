package com.example.peter.thekitchenmenu.ui.detail.recipe.recipeingredientlist;

import android.content.Context;

import androidx.databinding.ObservableArrayList;
import androidx.databinding.ObservableBoolean;
import androidx.databinding.ObservableList;
import androidx.lifecycle.ViewModel;

import com.example.peter.thekitchenmenu.data.entity.RecipeIngredientEntity;
import com.example.peter.thekitchenmenu.data.repository.DataSource;
import com.example.peter.thekitchenmenu.data.repository.RepositoryRecipeIngredient;

import java.util.List;

public class RecipeIngredientListViewModel extends ViewModel {

    private final RepositoryRecipeIngredient repositoryRecipeIngredient;
    private Context context;
    private RecipeIngredientListNavigator navigator;

    public final ObservableList<RecipeIngredientEntity> recipeIngredients =
            new ObservableArrayList<>();
    public final ObservableBoolean hasIngredients = new ObservableBoolean(false);

    private String recipeId;

    public RecipeIngredientListViewModel(RepositoryRecipeIngredient repositoryRecipeIngredient,
                                         Context context) {
        this.repositoryRecipeIngredient = repositoryRecipeIngredient;
        this.context = context;
    }

    void setNavigator(RecipeIngredientListNavigator navigator) {
        this.navigator = navigator;
    }

    void onActivityDestroyed() {
        navigator = null;
    }

    void start(String recipeId) {
        this.recipeId = recipeId;
        loadRecipeIngredients(recipeId);
    }

    private void loadRecipeIngredients(String recipeId) {
        repositoryRecipeIngredient.getByRecipeId(
                recipeId,
                new DataSource.GetAllCallback<RecipeIngredientEntity>() {
            @Override
            public void onAllLoaded(List<RecipeIngredientEntity> recipeIngredients) {
                hasIngredients.set(true);
                RecipeIngredientListViewModel.this.recipeIngredients.clear();
                RecipeIngredientListViewModel.this.recipeIngredients.addAll(recipeIngredients);
            }

            @Override
            public void onDataNotAvailable() {
                hasIngredients.set(false);
            }
        });
    }

    public void addIngredientButtonPressed() {
        navigator.addRecipeIngredient(recipeId);
    }
}
