package com.example.peter.thekitchenmenu.ui.detail.recipe.recipeingredienteditor;

import androidx.databinding.Observable;
import androidx.databinding.ObservableField;
import androidx.databinding.ObservableInt;
import androidx.lifecycle.ViewModel;

import com.example.peter.thekitchenmenu.data.entity.RecipeIngredientEntity;
import com.example.peter.thekitchenmenu.data.repository.RepositoryIngredient;
import com.example.peter.thekitchenmenu.data.repository.RepositoryRecipeIngredient;
import com.example.peter.thekitchenmenu.utils.unitofmeasure.MeasurementSubtype;
import com.example.peter.thekitchenmenu.utils.unitofmeasure.UnitOfMeasure;

public class RecipeIngredientMeasurementViewModel extends ViewModel {

    private static final String TAG = "tkm-RecipeIngredientMeasurementVM";

    private RepositoryIngredient repositoryIngredient;
    private RepositoryRecipeIngredient repositoryRecipeIngredient;

    public final ObservableInt unitOfMeasureSpinnerInt = new ObservableInt();
    public final ObservableField<MeasurementSubtype> subtype = new ObservableField<>();
    public final ObservableInt numberOfMeasurementUnits = new ObservableInt();

    private String recipeId;
    private String ingredientId;
    private RecipeIngredientEntity ingredientEntity;
    private UnitOfMeasure unitOfMeasure;

    public RecipeIngredientMeasurementViewModel(
            RepositoryIngredient repositoryIngredient,
            RepositoryRecipeIngredient repositoryRecipeIngredient) {
        this.repositoryIngredient = repositoryIngredient;
        this.repositoryRecipeIngredient = repositoryRecipeIngredient;

        unitOfMeasureSpinnerInt.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable sender, int propertyId) {
                subTypeUpdated();
            }
        });
    }

    public void start(String recipeId, String ingredientId) {
        subTypeUpdated();
    }

    private void subTypeUpdated() {
        MeasurementSubtype newSubtype = getSubtypeFromSpinnerPosition();
        if (subtype.get() != newSubtype) {
            subtype.set(newSubtype);
            unitOfMeasure = newSubtype.getMeasurementClass();
            setNumberOfMeasurementUnits();
        }
    }

    private MeasurementSubtype getSubtypeFromSpinnerPosition() {
        return MeasurementSubtype.valueOf(unitOfMeasureSpinnerInt.get());
    }

    private void setNumberOfMeasurementUnits() {
        numberOfMeasurementUnits.set(unitOfMeasure.getNumberOfMeasurementUnits());
    }
}
