package com.example.peter.thekitchenmenu.ui.detail.recipe.recipeingredienteditor;

import android.content.res.Resources;

import androidx.databinding.Observable;
import androidx.databinding.ObservableField;
import androidx.databinding.ObservableInt;
import androidx.lifecycle.ViewModel;

import com.example.peter.thekitchenmenu.R;
import com.example.peter.thekitchenmenu.data.entity.RecipeIngredientEntity;
import com.example.peter.thekitchenmenu.data.repository.RepositoryIngredient;
import com.example.peter.thekitchenmenu.data.repository.RepositoryRecipeIngredient;
import com.example.peter.thekitchenmenu.utils.unitofmeasure.MeasurementSubtype;
import com.example.peter.thekitchenmenu.utils.unitofmeasure.UnitOfMeasure;

public class RecipeIngredientMeasurementViewModel extends ViewModel {

    private static final String TAG = "tkm-RecipeIngredientMeasurementVM";

    private RepositoryIngredient repositoryIngredient;
    private RepositoryRecipeIngredient repositoryRecipeIngredient;
    private Resources resources;

    private static final int MEASUREMENT_ERROR = -1;

    public final ObservableInt unitOfMeasureSpinnerInt = new ObservableInt();
    public final ObservableField<MeasurementSubtype> subtype = new ObservableField<>();
    public final ObservableInt numberOfMeasurementUnits = new ObservableInt();
    public final ObservableField<String> measurementOne = new ObservableField<>();
    public final ObservableField<String> measurementTwo = new ObservableField<>();
    public final ObservableField<String> measurementOneErrorMessage = new ObservableField<>();
    public final ObservableField<String> measurementTwoErrorMessage = new ObservableField<>();


    private String recipeId;
    private String ingredientId;
    private RecipeIngredientEntity ingredientEntity;
    private UnitOfMeasure unitOfMeasure;
    private String ingredientMeasurementOne;
    private String ingredientMeasurementTwo;

    public RecipeIngredientMeasurementViewModel(
            RepositoryIngredient repositoryIngredient,
            RepositoryRecipeIngredient repositoryRecipeIngredient,
            Resources resources) {

        this.repositoryIngredient = repositoryIngredient;
        this.repositoryRecipeIngredient = repositoryRecipeIngredient;
        this.resources = resources;

        unitOfMeasureSpinnerInt.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable sender, int propertyId) {
                subTypeUpdated();
            }
        });

        measurementOne.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable sender, int propertyId) {
                measurementOneUpdated();
            }
        });

        measurementTwo.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable sender, int propertyId) {
                measurementTwoUpdated();
            }
        });
    }

    public void start(String recipeId, String ingredientId) {
        this.recipeId = recipeId;
        this.ingredientId = ingredientId;
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
        return MeasurementSubtype.fromInt(unitOfMeasureSpinnerInt.get());
    }

    private void setNumberOfMeasurementUnits() {
        numberOfMeasurementUnits.set(unitOfMeasure.getNumberOfMeasurementUnits());
    }

    private void measurementOneUpdated() {
        int unitOneDigitsAfterDecimal =
                (int) unitOfMeasure.getMeasurementUnitsDigitWidths()[0].second;

        if (unitOneDigitsAfterDecimal > 0)
            processDecimalMeasurement();
        else
            processIntegerMeasurement(measurementOne);
    }

    private void processDecimalMeasurement() {
        double decimalMeasurement = parseDecimalFromString();
        if (decimalMeasurementHasChanged(decimalMeasurement)) {
            updateDecimalMeasurement(decimalMeasurement);
        }
    }

    private void updateDecimalMeasurement(double decimalMeasurement) {
        boolean measurementIsSet = unitOfMeasure.productMeasurementOneIsSet(decimalMeasurement);
        if (measurementIsSet) {
            updateUi();
        }
    }

    private boolean decimalMeasurementHasChanged(double newMeasurement) {
        double oldMeasurement = unitOfMeasure.getPackMeasurementOne();
        return oldMeasurement != newMeasurement;
    }

    private double parseDecimalFromString() {
        String rawMeasurement = measurementOne.get();
        if (rawMeasurement.isEmpty() || rawMeasurement.equals("."))
            measurementOne.set("");
        try {
            return Double.parseDouble(rawMeasurement);
        } catch (NumberFormatException e) {
            measurementOneErrorMessage.set(resources.getString(R.string.number_format_exception));
            return MEASUREMENT_ERROR;
        }
    }

    private void measurementTwoUpdated() {
        processIntegerMeasurement(measurementTwo);
    }

    private void processIntegerMeasurement(ObservableField<String> measurement) {
        int integerMeasurement = parseIntegerFromString(measurement);
        if (integerMeasurementHasChanged(measurement, integerMeasurement)) {
            updateIntegerMeasurement(measurement, integerMeasurement);
        }

        boolean measurementIsSet = false;
        if (measurement == measurementOne) {
            measurementIsSet = unitOfMeasure.productMeasurementOneIsSet(integerMeasurement);

        } else if (measurement == measurementTwo) {
            measurementIsSet = unitOfMeasure.productMeasurementTwoIsSet(integerMeasurement);
        }
        if (measurementIsSet)
            updateUi();
    }

    private int parseIntegerFromString(ObservableField<String> measurement) {
        String rawMeasurement = measurement.get();
        if (rawMeasurement.isEmpty())
            measurement.set("");
        try {
            return Integer.parseInt(rawMeasurement);
        } catch (NumberFormatException e) {
            if (measurement == measurementOne)
                measurementOneErrorMessage.set(resources.getString(R.string.number_format_exception));
            else if (measurement == measurementTwo)
                measurementTwoErrorMessage.set(resources.getString(R.string.number_format_exception));
            return MEASUREMENT_ERROR;
        }
    }

    private boolean integerMeasurementHasChanged(ObservableField<String> measurement, int newMeasurement) {
        int oldMeasurement = 0;
        if (measurement == measurementOne)
            oldMeasurement = (int) unitOfMeasure.getProductMeasurementOne();
        if (measurement == measurementTwo)
            oldMeasurement = unitOfMeasure.getProductMeasurementTwo();
        return oldMeasurement != newMeasurement;
    }

    private void updateIntegerMeasurement(ObservableField<String> measurement, int newMeasurement) {
        boolean measurementIsSet = false;
        if (measurement == measurementOne)
            measurementIsSet = unitOfMeasure.productMeasurementOneIsSet(newMeasurement);
        if (measurement == measurementTwo)
            measurementIsSet = unitOfMeasure.productMeasurementTwoIsSet(newMeasurement);
        if (measurementIsSet)
            updateUi();
    }

    private void updateUi() {
        if (subtype.get() != unitOfMeasure.getMeasurementSubtype())
            subtype.set(unitOfMeasure.getMeasurementSubtype());
        if (numberOfMeasurementUnits.get() != unitOfMeasure.getNumberOfMeasurementUnits())
            numberOfMeasurementUnits.set(unitOfMeasure.getNumberOfMeasurementUnits());

        if (unitsAfterDecimal() > 0)
            measurementOne.set(String.valueOf(unitOfMeasure.getPackMeasurementOne()));
        else
            measurementOne.set(String.valueOf((int) unitOfMeasure.getPackMeasurementOne()));

        if (numberOfMeasurementUnits.get() > 1)
            measurementTwo.set(String.valueOf(unitOfMeasure.getPackMeasurementTwo()));
    }

    private int unitsAfterDecimal() {
        return (int) unitOfMeasure.getMeasurementUnitsDigitWidths()[0].second;
    }
}
