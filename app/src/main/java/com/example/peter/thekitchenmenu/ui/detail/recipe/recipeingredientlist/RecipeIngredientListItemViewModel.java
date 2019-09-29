package com.example.peter.thekitchenmenu.ui.detail.recipe.recipeingredientlist;

import android.content.Context;

import androidx.annotation.Nullable;
import androidx.databinding.ObservableField;
import androidx.lifecycle.ViewModel;

import com.example.peter.thekitchenmenu.data.entity.RecipeIngredientEntity;
import com.example.peter.thekitchenmenu.data.repository.DataSource;
import com.example.peter.thekitchenmenu.data.repository.RepositoryRecipeIngredient;

import java.lang.ref.WeakReference;

public class RecipeIngredientListItemViewModel
        extends ViewModel
        implements DataSource.GetEntityCallback<RecipeIngredientEntity> {

    @Nullable
    private WeakReference<RecipeIngredientListItemNavigator> navigator;
    private RepositoryRecipeIngredient repository;

    private final ObservableField<RecipeIngredientEntity> recipeIngredientObservable =
            new ObservableField<>();
    public final ObservableField<String> ingredientNameObservable = new ObservableField<>();

    private RecipeIngredientEntity ingredientEntity = new RecipeIngredientEntity(
            "id",
            "recipeId",
            "ingredientId",
            "productId",
            1000,
            0
    );


    public RecipeIngredientListItemViewModel(Context context,
                                             RepositoryRecipeIngredient repository) {
        this.repository = repository;
    }

    public void setNavigator(RecipeIngredientListItemNavigator navigator) {
        this.navigator = new WeakReference<>(navigator);
    }

    public void setRecipeIngredient(RecipeIngredientEntity recipeIngredient) {
        recipeIngredientObservable.set(recipeIngredient);
    }

    public void deleteIngredientClicked() {

    }

    @Override
    public void onEntityLoaded(RecipeIngredientEntity object) {

    }

    @Override
    public void onDataNotAvailable() {

    }
}
