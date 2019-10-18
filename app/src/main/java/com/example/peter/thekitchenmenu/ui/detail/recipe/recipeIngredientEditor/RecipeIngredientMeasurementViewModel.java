package com.example.peter.thekitchenmenu.ui.detail.recipe.recipeingredienteditor;

import android.app.Application;
import android.content.res.Resources;

import androidx.databinding.Observable;
import androidx.databinding.ObservableBoolean;
import androidx.databinding.ObservableField;
import androidx.databinding.ObservableInt;
import androidx.lifecycle.AndroidViewModel;

import com.example.peter.thekitchenmenu.R;
import com.example.peter.thekitchenmenu.data.model.MeasurementModel;
import com.example.peter.thekitchenmenu.ui.UseCaseFactory;
import com.example.peter.thekitchenmenu.utils.unitofmeasure.MeasurementResult;
import com.example.peter.thekitchenmenu.utils.unitofmeasure.MeasurementSubtype;
import com.example.peter.thekitchenmenu.utils.unitofmeasure.PortionUseCaseViewModel;
import com.example.peter.thekitchenmenu.utils.unitofmeasure.UnitOfMeasure;
import com.example.peter.thekitchenmenu.utils.unitofmeasure.UnitOfMeasurePortionUseCase;

import static com.example.peter.thekitchenmenu.utils.unitofmeasure.MeasurementResult.*;

public class RecipeIngredientMeasurementViewModel
        extends AndroidViewModel
        implements PortionUseCaseViewModel {

    private MeasurementSubtype defaultSubtype = MeasurementSubtype.METRIC_MASS;
    private UnitOfMeasurePortionUseCase useCase;

    private Resources resources;

    private static final int MEASUREMENT_ERROR = -1;

    public final ObservableField<MeasurementSubtype> subtype = new ObservableField<>(defaultSubtype);
    public final ObservableInt numberOfMeasurementUnits = new ObservableInt();
    public final ObservableField<String> measurementOne = new ObservableField<>();
    public final ObservableField<String> measurementTwo = new ObservableField<>();
    public final ObservableField<String> measurementOneErrorMessage = new ObservableField<>();
    public final ObservableField<String> measurementTwoErrorMessage = new ObservableField<>();
    public final ObservableField<String> conversionFactor = new ObservableField<>();
    public final ObservableBoolean isConversionFactorEnabled = new ObservableBoolean();
    public final ObservableField<String> conversionFactorErrorMessage = new ObservableField<>();

    private UnitOfMeasure unitOfMeasure = defaultSubtype.getMeasurementClass();
    private double conversionFactorParsed;
    private double measurementOneParsed;
    private int measurementTwoParsed;

    private MeasurementModel measurementModel;

    public RecipeIngredientMeasurementViewModel(Application application,
                                                UnitOfMeasurePortionUseCase useCase) {
        super(application);
        this.resources = application.getResources();
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

    @Override
    public void setResult(MeasurementResult result) {
        measurementModel = result.getModel();
        processMeasurementModel();
        processResultStatus(result.getResult());
    }

    private void processMeasurementModel() {

    }

    private void processResultStatus(ResultStatus resultStatus) {
        if (resultStatus == ResultStatus.INVALID_CONVERSION_FACTOR) {
            conversionFactorErrorMessage.set(resources.getString(
                    R.string.conversion_factor_error_message));
        }
        if (resultStatus == ResultStatus.INVALID_TOTAL_MEASUREMENT_ONE) {
            measurementOneErrorMessage.set("Measurement one needs to be between x and y");
        }
        if (resultStatus == ResultStatus.INVALID_TOTAL_MEASUREMENT_TWO) {
            measurementTwoErrorMessage.set("Measurement two needs to be between x and y");
        }
    }

    private void subTypeUpdated() {
        unitOfMeasure = subtype.get().getMeasurementClass();
        updateNumberOfMeasurementUnits();
        updateMeasurementModel();
    }

    private void updateNumberOfMeasurementUnits() {
        if (numberOfMeasurementUnits.get() != unitOfMeasure.getNumberOfMeasurementUnits())
            numberOfMeasurementUnits.set(unitOfMeasure.getNumberOfMeasurementUnits());
    }

    private void conversionFactorUpdated() {
        double conversionFactorParsed = parseDecimalFromString(conversionFactor);
        if (conversionFactorParsed != MEASUREMENT_ERROR) {
            this.conversionFactorParsed = conversionFactorParsed;
            updateMeasurementModel();
        }
    }

    private void measurementOneUpdated() {
        processMeasurementOne();
    }

    private void processMeasurementOne() {
        double measurementOneParsed = parseDecimalFromString(measurementOne);
        if (measurementOneParsed != MEASUREMENT_ERROR) {
            this.measurementOneParsed = measurementOneParsed;
            updateMeasurementModel();
        }
    }

    private double parseDecimalFromString(ObservableField<String> measurement) {
        String rawValue = measurement.get();
        try {
            return Double.parseDouble(rawValue);
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
        processMeasurementTwo();
    }

    private void processMeasurementTwo() {
        int measurementTwoParsed = parseIntegerFromString();
        if (measurementTwoParsed != MEASUREMENT_ERROR) {
            this.measurementTwoParsed = measurementTwoParsed;
            updateMeasurementModel();
        }
    }

    private int parseIntegerFromString() {
        String rawMeasurement = measurementTwo.get();
        try {
            return Integer.parseInt(rawMeasurement);
        } catch (NumberFormatException e) {
            measurementTwoErrorMessage.set(resources.getString(R.string.number_format_exception));
            return MEASUREMENT_ERROR;
        }
    }

    private void updateMeasurementModel() {
        MeasurementModel updatedModel = getUpdatedMeasurementModel();
        useCase.setModel(updatedModel);
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
}