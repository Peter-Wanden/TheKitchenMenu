package com.example.peter.thekitchenmenu.ui.detail.recipe.recipeingredienteditor;

import android.app.Application;
import android.content.res.Resources;
import android.util.Log;

import androidx.databinding.Observable;
import androidx.databinding.ObservableBoolean;
import androidx.databinding.ObservableField;
import androidx.databinding.ObservableInt;
import androidx.lifecycle.AndroidViewModel;

import com.example.peter.thekitchenmenu.R;
import com.example.peter.thekitchenmenu.data.model.MeasurementModel;
import com.example.peter.thekitchenmenu.utils.unitofmeasure.MeasurementResult;
import com.example.peter.thekitchenmenu.utils.unitofmeasure.MeasurementSubtype;
import com.example.peter.thekitchenmenu.utils.unitofmeasure.PortionUseCaseViewModel;
import com.example.peter.thekitchenmenu.utils.unitofmeasure.UnitOfMeasure;
import com.example.peter.thekitchenmenu.utils.unitofmeasure.UnitOfMeasurePortionUseCase;

import static com.example.peter.thekitchenmenu.utils.unitofmeasure.MeasurementResult.*;

public class RecipeIngredientMeasurementViewModel
        extends AndroidViewModel
        implements PortionUseCaseViewModel {

    private static final String TAG = "tkm-RecipeIngredientMea";

    private Resources resources;

    private MeasurementSubtype defaultSubtype = MeasurementSubtype.METRIC_MASS;
    private UnitOfMeasure unitOfMeasure = defaultSubtype.getMeasurementClass();
    private UnitOfMeasurePortionUseCase useCase;
    private static final int MEASUREMENT_ERROR = -1;

    public final ObservableField<MeasurementSubtype> subtype = new ObservableField<>(
            defaultSubtype);
    public final ObservableInt numberOfMeasurementUnits = new ObservableInt(
            unitOfMeasure.getNumberOfMeasurementUnits());
    public final ObservableField<String> conversionFactor = new ObservableField<>(String.valueOf(
            unitOfMeasure.getConversionFactor()));
    public final ObservableField<String> conversionFactorErrorMessage = new ObservableField<>();
    public final ObservableBoolean isConversionFactorEnabled = new ObservableBoolean();
    public final ObservableField<String> measurementOne = new ObservableField<>();
    public final ObservableField<String> measurementTwo = new ObservableField<>();
    public final ObservableField<String> measurementOneErrorMessage = new ObservableField<>();
    public final ObservableField<String> measurementTwoErrorMessage = new ObservableField<>();

    private double conversionFactorParsed;
    private double measurementOneParsed;
    private int measurementTwoParsed;

    private MeasurementModel measurementModel;
    private boolean measurementModelUpdating;

    public RecipeIngredientMeasurementViewModel(Application application,
                                                UnitOfMeasurePortionUseCase useCase,
                                                Resources resources) {
        super(application);
        this.resources = resources;
        this.useCase = useCase;

        subtype.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
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

        conversionFactor.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable sender, int propertyId) {
                conversionFactorUpdated();
            }
        });
        setViewModel();
    }

    private void setViewModel() {
        useCase.setViewModel(this);
    }

    public void start(String recipeId, String ingredientId) {
        useCase.start(recipeId, ingredientId);
    }

    public void start(String recipeIngredientId) {
        useCase.start(recipeIngredientId);
    }

    private void subTypeUpdated() {
        clearErrors();
        if (!measurementModelUpdating) {
            unitOfMeasure = subtype.get().getMeasurementClass();
            isConversionFactorEnabled.set(unitOfMeasure.isConversionFactorEnabled());
            updateNumberOfMeasurementUnits();
            updateMeasurementModel();
        }
    }

    private void updateNumberOfMeasurementUnits() {
        if (numberOfMeasurementUnits.get() != unitOfMeasure.getNumberOfMeasurementUnits())
            numberOfMeasurementUnits.set(unitOfMeasure.getNumberOfMeasurementUnits());
    }

    private void conversionFactorUpdated() {
        if (!measurementModelUpdating) {
            processConversionFactor();
        }
    }

    private void processConversionFactor() {
        clearErrors();
        double conversionFactorParsed = parseDecimalFromString(conversionFactor);
        if (conversionFactorParsed != MEASUREMENT_ERROR) {
            this.conversionFactorParsed = conversionFactorParsed;
            updateMeasurementModel();
        }
    }

    private void measurementOneUpdated() {
        if (!measurementModelUpdating)
            processMeasurementOne();
    }

    private void processMeasurementOne() {
        clearErrors();
        double measurementOneParsed = parseDecimalFromString(measurementOne);
        if (measurementOneParsed != MEASUREMENT_ERROR) {
            this.measurementOneParsed = measurementOneParsed;
            updateMeasurementModel();
        }
    }

    private double parseDecimalFromString(ObservableField<String> measurement) {
        String rawMeasurement = measurement.get();
        if (rawMeasurement.isEmpty() || rawMeasurement.equals("."))
            return 0;
        try {
            return Double.parseDouble(rawMeasurement);
        } catch (NumberFormatException e) {
            if (measurement == measurementOne)
                measurementOneErrorMessage.set(
                        resources.getString(R.string.number_format_exception));
            if (measurement == conversionFactor)
                conversionFactorErrorMessage.set(
                        resources.getString(R.string.number_format_exception));
            return MEASUREMENT_ERROR;
        }
    }

    private void measurementTwoUpdated() {
        if (!measurementModelUpdating)
            processMeasurementTwo();
    }

    private void processMeasurementTwo() {
        clearErrors();
        int measurementTwoParsed = parseIntegerFromString();
        if (measurementTwoParsed != MEASUREMENT_ERROR) {
            this.measurementTwoParsed = measurementTwoParsed;
            updateMeasurementModel();
        }
    }

    private void clearErrors() {
        conversionFactorErrorMessage.set(null);
        measurementOneErrorMessage.set(null);
        measurementTwoErrorMessage.set(null);
    }

    private int parseIntegerFromString() {
        String rawMeasurement = measurementTwo.get();
        if (rawMeasurement.isEmpty())
            return 0;
        try {
            return Integer.parseInt(rawMeasurement);
        } catch (NumberFormatException e) {
            measurementTwoErrorMessage.set(resources.getString(R.string.number_format_exception));
            return MEASUREMENT_ERROR;
        }
    }

    private void updateMeasurementModel() {
        if (measurementModelHasChanged()) {
            useCase.modelIn(getUpdatedMeasurementModel());
        }
    }

    private MeasurementModel getUpdatedMeasurementModel() {
        return new MeasurementModel(
                unitOfMeasure.getMeasurementSubtype(),
                measurementModel.getNumberOfItems(),
                conversionFactorParsed,
                measurementOneParsed,
                measurementTwoParsed,
                measurementModel.getItemMeasurementOne(),
                measurementModel.getItemMeasurementTwo(),
                measurementModel.getItemBaseUnits()
        );
    }

    private boolean measurementModelHasChanged() {
        return !measurementModel.equals(getUpdatedMeasurementModel());
    }

    @Override
    public void modelOut(MeasurementResult result) {
        Log.d(TAG, "modelOut: " + result);
        measurementModel = result.getModel();
        processMeasurementModel();
        processResultStatus(result.getResult());
    }

    private void processMeasurementModel() {
        measurementModelUpdating = true;
        if (measurementModel.getSubtype() != unitOfMeasure.getMeasurementSubtype()) {
            unitOfMeasure = measurementModel.getSubtype().getMeasurementClass();
            updateNumberOfMeasurementUnits();

            if (subtype.get() != unitOfMeasure.getMeasurementSubtype())
                subtype.set(unitOfMeasure.getMeasurementSubtype());

            isConversionFactorEnabled.set(unitOfMeasure.isConversionFactorEnabled());
        }

        if (measurementModel.getConversionFactor() != conversionFactorParsed) {
            conversionFactorParsed = measurementModel.getConversionFactor();
            conversionFactor.set(String.valueOf(conversionFactorParsed)); // format number
        }

        if (measurementModel.getTotalMeasurementOne() != measurementOneParsed) {
            measurementOneParsed = measurementModel.getItemMeasurementOne();
            formatDecimalNumberOutput(measurementOneParsed);
        }

        if (measurementModel.getTotalMeasurementTwo() != measurementTwoParsed) {
            measurementTwoParsed = measurementModel.getTotalMeasurementTwo();
            formatIntegerNumberOutput(measurementTwoParsed);
        }
    }

    private void processResultStatus(ResultStatus resultStatus) {
        if (resultStatus == ResultStatus.INVALID_CONVERSION_FACTOR) {
            conversionFactorErrorMessage.set(resources.getString(
                    R.string.conversion_factor_error_message));

        } else if (resultStatus == ResultStatus.INVALID_TOTAL_MEASUREMENT_ONE) {
            measurementOneErrorMessage.set("Measurement one needs to be between x and y");

        } else if (resultStatus == ResultStatus.INVALID_TOTAL_MEASUREMENT_TWO) {
            measurementTwoErrorMessage.set("Tablespoons and/or teaspoons needs to have a value");
        }
        measurementModelUpdating = false;
    }

    private void formatIntegerNumberOutput(int measurement) {
        if (measurement == measurementTwoParsed) {
            if (measurement == 0) {
                measurementTwo.set("");
            } else {
                measurementTwo.set(String.valueOf(measurementTwoParsed));
            }
        }
    }

    private void formatDecimalNumberOutput(double measurement) {
        if (measurement == measurementOneParsed) {
            if (measurement == 0) {
                measurementOne.set("");
            } else {
                measurementOne.set(String.valueOf(measurementOneParsed));
            }
        } else if (measurement == conversionFactorParsed) {
            if (measurement == 0) {
                conversionFactor.set("");
            } else {
                conversionFactor.set(String.valueOf(conversionFactorParsed));
            }
        }
    }
}