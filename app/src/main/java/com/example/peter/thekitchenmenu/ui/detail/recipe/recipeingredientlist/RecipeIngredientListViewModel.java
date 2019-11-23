package com.example.peter.thekitchenmenu.ui.detail.recipe.recipeingredientlist;

import android.content.Context;

import androidx.databinding.ObservableArrayList;
import androidx.databinding.ObservableBoolean;
import androidx.databinding.ObservableList;
import androidx.lifecycle.ViewModel;

import com.example.peter.thekitchenmenu.data.entity.RecipeIngredientQuantityEntity;
import com.example.peter.thekitchenmenu.data.repository.DataSource;
import com.example.peter.thekitchenmenu.data.repository.RepositoryRecipeIngredient;

import java.util.List;

public class RecipeIngredientListViewModel extends ViewModel {

    private final RepositoryRecipeIngredient repositoryRecipeIngredient;
    private Context context;
    private RecipeIngredientListNavigator navigator;

    public final ObservableList<RecipeIngredientQuantityEntity> recipeIngredients =
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
                new DataSource.GetAllCallback<RecipeIngredientQuantityEntity>() {
                    @Override
                    public void onAllLoaded(List<RecipeIngredientQuantityEntity> recipeIngredients) {
                        hasIngredients.set(true);
                        RecipeIngredientListViewModel.this.recipeIngredients.clear();
                        RecipeIngredientListViewModel.this.recipeIngredients.addAll(recipeIngredients);

                        for (RecipeIngredientQuantityEntity entity : recipeIngredients) {
                            System.out.println("tkm-" + entity);
                        }
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
