package com.example.peter.thekitchenmenu.ui.detail.recipe.recipeingredientlist;

import android.content.Context;

import androidx.annotation.Nullable;
import androidx.databinding.ObservableField;
import androidx.lifecycle.ViewModel;

import com.example.peter.thekitchenmenu.app.Constants;
import com.example.peter.thekitchenmenu.data.entity.RecipeIngredientEntity;
import com.example.peter.thekitchenmenu.data.repository.RepositoryIngredient;
import com.example.peter.thekitchenmenu.data.repository.RepositoryRecipeIngredient;

import java.lang.ref.WeakReference;

public class RecipeIngredientListItemViewModel
        extends ViewModel {

    @Nullable
    private WeakReference<RecipeIngredientListItemNavigator> navigator;
    private RepositoryRecipeIngredient repositoryRecipeIngredient;
    private RepositoryIngredient repositoryIngredient;

    private final ObservableField<RecipeIngredientEntity> recipeIngredientObservable =
            new ObservableField<>();
    public final ObservableField<String> ingredientNameObservable = new ObservableField<>();
    public final ObservableField<String> ingredientMeasurementObservable = new ObservableField<>();
    public final ObservableField<String> ingredientMeasurementUnitObservable = new ObservableField<>();

    private RecipeIngredientEntity ingredientEntity = new RecipeIngredientEntity(
            "id",
            "recipeId",
            "ingredientId",
            "productId",
            1000,
            0,
            Constants.getUserId().getValue(),
            10L,
            10L
    );


    public RecipeIngredientListItemViewModel(Context context,
                                             RepositoryRecipeIngredient repositoryRecipeIngredient,
                                             RepositoryIngredient repositoryIngredient) {
        this.repositoryRecipeIngredient = repositoryRecipeIngredient;
        this.repositoryIngredient = repositoryIngredient;
    }

    public void setNavigator(RecipeIngredientListItemNavigator navigator) {
        this.navigator = new WeakReference<>(navigator);
    }

    public void setRecipeIngredient(RecipeIngredientEntity recipeIngredient) {
        recipeIngredientObservable.set(recipeIngredient);
    }

    public void start(String recipeId) {

    }

    public void deleteIngredientClicked() {

    }
}
