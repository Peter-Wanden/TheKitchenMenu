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
import com.example.peter.thekitchenmenu.utils.NumberFormatter;
import com.example.peter.thekitchenmenu.utils.unitofmeasure.MeasurementResult;
import com.example.peter.thekitchenmenu.utils.unitofmeasure.MeasurementSubtype;
import com.example.peter.thekitchenmenu.utils.unitofmeasure.UseCaseConversionFactorStatus;
import com.example.peter.thekitchenmenu.utils.unitofmeasure.UnitOfMeasure;
import com.example.peter.thekitchenmenu.utils.unitofmeasure.UnitOfMeasureConstants;
import com.example.peter.thekitchenmenu.utils.unitofmeasure.UseCasePortion;

import static com.example.peter.thekitchenmenu.utils.unitofmeasure.MeasurementResult.*;
import static com.example.peter.thekitchenmenu.utils.unitofmeasure.UseCaseConversionFactorStatus.*;

public class RecipeIngredientMeasurementViewModel
        extends
        AndroidViewModel
        implements
        UseCasePortion.UseCasePortionCallback,
        UseCaseConversionFactorCallback {

    private static final String TAG = "tkm-RecipeIngredientMea";

    private Resources resources;
    private NumberFormatter numberFormatter;

    private MeasurementSubtype defaultSubtype = MeasurementSubtype.METRIC_MASS;
    private UnitOfMeasure unitOfMeasure = defaultSubtype.getMeasurementClass();
    private UseCasePortion useCasePortion;
    private UseCaseConversionFactorStatus useCaseConversionFactorStatus;
    private static final int MEASUREMENT_ERROR = -1;

    public final ObservableField<MeasurementSubtype> subtype = new ObservableField<>(defaultSubtype);
    public final ObservableInt numberOfMeasurementUnits = new ObservableInt(
            unitOfMeasure.getNumberOfMeasurementUnits());
    public final ObservableField<String> conversionFactor = new ObservableField<>(
            String.valueOf(unitOfMeasure.getConversionFactor()));
    public final ObservableField<String> conversionFactorErrorMessage = new ObservableField<>();
    public final ObservableBoolean isConversionFactorEnabled = new ObservableBoolean();
    public final ObservableBoolean advancedCheckBox = new ObservableBoolean();
    public final ObservableBoolean showConversionFactorFields = new ObservableBoolean();
    public final ObservableBoolean showUneditableConversionFactorFields = new ObservableBoolean();
    public final ObservableField<String> measurementOne = new ObservableField<>();
    public final ObservableField<String> measurementTwo = new ObservableField<>();
    public final ObservableField<String> measurementOneErrorMessage = new ObservableField<>();
    public final ObservableField<String> measurementTwoErrorMessage = new ObservableField<>();

    private double conversionFactorParsed;
    private double measurementOneParsed;
    private int measurementTwoParsed;

    private MeasurementModel measurementModel;
    private boolean updatingDisplay;

    public RecipeIngredientMeasurementViewModel(Application application,
                                                UseCasePortion useCasePortion,
                                                UseCaseConversionFactorStatus useCaseConversionFactor,
                                                Resources resources,
                                                NumberFormatter numberFormatter) {
        super(application);
        this.useCasePortion = useCasePortion;
        this.useCaseConversionFactorStatus = useCaseConversionFactor;
        this.resources = resources;
        this.numberFormatter = numberFormatter;

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

        advancedCheckBox.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable sender, int propertyId) {
                advancedCheckBoxUpdated();
            }
        });
        registerAsListenerOfUseCaseResults();
    }

    private void registerAsListenerOfUseCaseResults() {
        useCasePortion.registerListener(this);
        useCaseConversionFactorStatus.registerListener(this);
    }

    public void start(String recipeId, String ingredientId) {
        useCasePortion.start(recipeId, ingredientId);
    }

    public void start(String recipeIngredientId) {
        useCasePortion.start(recipeIngredientId);
    }

    private void subTypeUpdated() {
        if (!updatingDisplay) {
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
        if (!updatingDisplay)
            processConversionFactor();
    }

    private void processConversionFactor() {
        double conversionFactorParsed = parseDecimalFromString(conversionFactor);
        if (conversionFactorParsed != MEASUREMENT_ERROR) {
            this.conversionFactorParsed = conversionFactorParsed;
            updateMeasurementModel();
        }
    }

    private void measurementOneUpdated() {
        if (!updatingDisplay)
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
        if (!updatingDisplay)
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
        if (rawMeasurement.isEmpty())
            return 0;
        try {
            return Integer.parseInt(rawMeasurement);
        } catch (NumberFormatException e) {
            measurementTwoErrorMessage.set(resources.getString(R.string.number_format_exception));
            return MEASUREMENT_ERROR;
        }
    }

    private void advancedCheckBoxUpdated() {
        if (shouldShowConversionFactorFields())
            showConversionFactorFields.set(true);
        else
            showConversionFactorFields.set(false);
    }

    private boolean shouldShowConversionFactorFields() {
        return isConversionFactorEnabled.get() && advancedCheckBox.get();
    }

    private void updateMeasurementModel() {
        if (measurementModelHasChanged())
            useCasePortion.processModel(updatedMeasurementModel());
    }

    private boolean measurementModelHasChanged() {
        return !measurementModel.equals(updatedMeasurementModel());
    }

    private MeasurementModel updatedMeasurementModel() {
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

    @Override
    public void dataLoadingFailed(UseCasePortion.FailReason reason) {

    }

    @Override
    public void useCasePortionResult(MeasurementResult result) {
        measurementModel = result.getModel();
        processMeasurementModelResult();
        processResultStatus(result.getResult());
    }

    private void processMeasurementModelResult() {
        updatingDisplay = true;
        if (measurementSubtypeHasChanged())
            updateUnitOfMeasureVariables();

        if (conversionFactorHasChanged())
            updateConversionFactor();

        if (totalMeasurementOneHasChanged())
            updateMeasurementOne();

        if (measurementTwoHasChanged())
            updateMeasurementTwo();
    }

    private boolean measurementSubtypeHasChanged() {
        return measurementModel.getSubtype() != unitOfMeasure.getMeasurementSubtype();
    }

    private void updateUnitOfMeasureVariables() {
        unitOfMeasure = measurementModel.getSubtype().getMeasurementClass();
        updateNumberOfMeasurementUnits();

        if (subtype.get() != unitOfMeasure.getMeasurementSubtype())
            subtype.set(unitOfMeasure.getMeasurementSubtype());

        isConversionFactorEnabled.set(unitOfMeasure.isConversionFactorEnabled());
    }

    private boolean conversionFactorHasChanged() {
        return measurementModel.getConversionFactor() != conversionFactorParsed;
    }

    private void updateConversionFactor() {
        conversionFactorParsed = measurementModel.getConversionFactor();
        conversionFactor.set(numberFormatter.formatDecimalForDisplay(conversionFactorParsed));
    }

    private boolean totalMeasurementOneHasChanged() {
        return measurementModel.getTotalMeasurementOne() != measurementOneParsed;
    }

    private void updateMeasurementOne() {
        measurementOneParsed = measurementModel.getItemMeasurementOne();
        measurementOne.set(numberFormatter.formatDecimalForDisplay(measurementOneParsed));
    }

    private boolean measurementTwoHasChanged() {
        return measurementModel.getTotalMeasurementTwo() != measurementTwoParsed;
    }

    private void updateMeasurementTwo() {
        measurementTwoParsed = measurementModel.getTotalMeasurementTwo();
        measurementTwo.set(numberFormatter.formatIntegerForDisplay(measurementTwoParsed));
    }

    private void processResultStatus(ResultStatus resultStatus) {
        if (resultStatus == ResultStatus.INVALID_CONVERSION_FACTOR) {
            conversionFactorErrorMessage.set(null);
            conversionFactorErrorMessage.set(resources.getString(
                    R.string.conversion_factor_error_message,
                    UnitOfMeasureConstants.MIN_CONVERSION_FACTOR,
                    UnitOfMeasureConstants.MAX_CONVERSION_FACTOR));

        } else if (resultStatus == ResultStatus.INVALID_TOTAL_MEASUREMENT_ONE) {
            measurementOneErrorMessage.set(null);
            measurementOneErrorMessage.set("Tablespoons and/or teaspoons need to have a value between 0.1 tsp and 666 Tbsp");

        } else if (resultStatus == ResultStatus.INVALID_TOTAL_MEASUREMENT_TWO) {
            measurementTwoErrorMessage.set(null);
            measurementTwoErrorMessage.set("Tablespoons and/or teaspoons need to have a value between 0.1 tsp and 666 Tbsp");

        } else if (resultStatus == ResultStatus.RESULT_OK) {
            hideAllInputErrors();
        }
        useCaseConversionFactorStatus.getConversionFactorStatus(
                subtype.get(),
                useCasePortion.getIngredientId());
    }

    private void hideAllInputErrors() {
        conversionFactorErrorMessage.set(null);
        measurementOneErrorMessage.set(null);
        measurementTwoErrorMessage.set(null);
    }

    @Override
    public void useCaseConversionFactorResult(UseCaseConversionFactorResult result) {
        if (result == UseCaseConversionFactorResult.DISABLED)
            hideAllConversionFactorInformation();

        else if (result == UseCaseConversionFactorResult.ENABLED_UNEDITABLE)
            showUneditableConversionFactorInformation();

        else if (result == UseCaseConversionFactorResult.ENABLED_EDITABLE_UNSET)
            showOptionToAddConversionFactorInformation();

        else if (result == UseCaseConversionFactorResult.ENABLED_EDITABLE_SET)
            showAllConversionFactorInformation();
        updatingDisplay = false;
    }

    private void hideAllConversionFactorInformation() {
        isConversionFactorEnabled.set(false);
        showConversionFactorFields.set(false);
        showUneditableConversionFactorFields.set(false);
    }

    private void showUneditableConversionFactorInformation() {
        isConversionFactorEnabled.set(true);
        showConversionFactorFields.set(false);
        showUneditableConversionFactorFields.set(true);
    }

    private void showOptionToAddConversionFactorInformation(){
        isConversionFactorEnabled.set(true);
        showConversionFactorFields.set(false);
        advancedCheckBox.set(false);
        showUneditableConversionFactorFields.set(false);
    }

    private void showAllConversionFactorInformation() {
        isConversionFactorEnabled.set(true);
        showConversionFactorFields.set(true);
        advancedCheckBox.set(true);
        showUneditableConversionFactorFields.set(false);
    }
}