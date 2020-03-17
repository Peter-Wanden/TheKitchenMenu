package com.example.peter.thekitchenmenu.ui.detail.recipe.recipeingredientlist;

import android.text.Spanned;

import androidx.annotation.Nullable;
import androidx.databinding.ObservableField;
import androidx.lifecycle.ViewModel;
import com.example.peter.thekitchenmenu.domain.usecase.recipe.recipeingredientlist.RecipeIngredientListItemModel;
import com.example.peter.thekitchenmenu.ui.utils.unitofmeasure.MeasurementToSpannableConverter;

import java.lang.ref.WeakReference;

public class RecipeIngredientListItemViewModel
        extends ViewModel {

    @Nullable
    private WeakReference<RecipeIngredientListItemNavigator> navigator;
    private MeasurementToSpannableConverter formatter;

    private RecipeIngredientListItemModel listItemModel;

    public final ObservableField<String> ingredientNameObservable = new ObservableField<>();
    public final ObservableField<Spanned> ingredientMeasurementObservable = new ObservableField<>();

    public RecipeIngredientListItemViewModel(MeasurementToSpannableConverter formatter) {
        this.formatter = formatter;
    }

    public void setNavigator(RecipeIngredientListItemNavigator navigator) {
        this.navigator = new WeakReference<>(navigator);
    }

    void setListItemModel(RecipeIngredientListItemModel listItemModel) {
        this.listItemModel = listItemModel;
        setResultsToDisplay();
    }

    private void setResultsToDisplay() {
        ingredientNameObservable.set(listItemModel.getIngredientName());
        ingredientMeasurementObservable.set(formatter.formatMeasurement(
                listItemModel.getMeasurementModel()));
    }

    public void deleteIngredientClicked() {
        if (listItemModel == null) {
            // Click happened before recipe ingredient loaded, no-op
            return;
        }

        if (navigator != null && navigator.get() != null) {
            navigator.get().deleteRecipeIngredient(listItemModel.getRecipeIngredientId());
        }
    }

    public void editRecipeIngredient() {
        if (listItemModel == null) {
            return;
        }

        if (navigator != null && navigator.get() != null) {
            navigator.get().editRecipeIngredient(
                    listItemModel.getRecipeIngredientId(),
                    listItemModel.getIngredientId());
        }
    }
}
