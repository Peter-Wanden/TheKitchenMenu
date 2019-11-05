package com.example.peter.thekitchenmenu.ui.detail.recipe.recipeingredienteditor;

import android.content.res.Resources;

import androidx.databinding.Bindable;
import androidx.databinding.Observable;
import androidx.databinding.ObservableBoolean;
import androidx.databinding.ObservableField;
import androidx.databinding.library.baseAdapters.BR;

import com.example.peter.thekitchenmenu.R;
import com.example.peter.thekitchenmenu.data.model.MeasurementModel;
import com.example.peter.thekitchenmenu.ui.ObservableViewModel;
import com.example.peter.thekitchenmenu.utils.NumberFormatter;
import com.example.peter.thekitchenmenu.utils.unitofmeasure.MeasurementResult;
import com.example.peter.thekitchenmenu.utils.unitofmeasure.MeasurementSubtype;
import com.example.peter.thekitchenmenu.utils.unitofmeasure.UseCaseConversionFactorStatus;
import com.example.peter.thekitchenmenu.utils.unitofmeasure.UnitOfMeasure;
import com.example.peter.thekitchenmenu.utils.unitofmeasure.UnitOfMeasureConstants;
import com.example.peter.thekitchenmenu.utils.unitofmeasure.UseCaseIngredientPortionCalculator;

import static com.example.peter.thekitchenmenu.utils.unitofmeasure.UseCaseConversionFactorStatus.*;

public class RecipeIngredientMeasurementViewModel
        extends
        ObservableViewModel
        implements
        UseCaseIngredientPortionCalculator.UseCasePortionCallback,
        UseCaseConversionFactorCallback {

    private static final String TAG = "tkm-RecipeIngredientMea";

    private Resources resources;
    private NumberFormatter numberFormatter;

    private static final int MEASUREMENT_ERROR = -1;
    private MeasurementSubtype subtype = MeasurementSubtype.METRIC_MASS;
    private UnitOfMeasure unitOfMeasure = subtype.getMeasurementClass();
    private int numberOfMeasurementUnits = unitOfMeasure.getNumberOfMeasurementUnits();
    private String conversionFactorInView = String.valueOf(unitOfMeasure.getConversionFactor());

    private UseCaseIngredientPortionCalculator useCasePortionCalculator;
    private UseCaseConversionFactorStatus useCaseConversionFactorStatus;

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
    private boolean updatingUi;

    public RecipeIngredientMeasurementViewModel(UseCaseIngredientPortionCalculator useCasePortionCalculator,
                                                UseCaseConversionFactorStatus useCaseConversionFactor,
                                                Resources resources,
                                                NumberFormatter numberFormatter) {
        this.useCasePortionCalculator = useCasePortionCalculator;
        this.useCaseConversionFactorStatus = useCaseConversionFactor;
        this.resources = resources;
        this.numberFormatter = numberFormatter;

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

        advancedCheckBox.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable sender, int propertyId) {
                advancedCheckBoxUpdated();
            }
        });
        registerAsListenerOfUseCases();
    }

    private void registerAsListenerOfUseCases() {
        useCasePortionCalculator.registerListener(this);
        useCaseConversionFactorStatus.registerListener(this);
    }

    public void start(String recipeId, String ingredientId) {
        useCasePortionCalculator.start(recipeId, ingredientId);
    }

    public void start(String recipeIngredientId) {
        useCasePortionCalculator.start(recipeIngredientId);
    }


    @Bindable
    public MeasurementSubtype getSubtype() {
        return subtype;
    }

    public void setSubtype(MeasurementSubtype subtype) {
        if (this.subtype != subtype) {
            this.subtype = subtype;
            notifyPropertyChanged(BR.subtype);
            setupForNewSubtype();
        }
    }

    private void setupForNewSubtype() {
        unitOfMeasure = subtype.getMeasurementClass();
        isConversionFactorEnabled.set(unitOfMeasure.isConversionFactorEnabled());
        updateNumberOfMeasurementUnits();
        updateMeasurementModel();
    }

    @Bindable
    public int getNumberOfMeasurementUnits() {
        return numberOfMeasurementUnits;
    }

    private void updateNumberOfMeasurementUnits() {
        if (numberOfMeasurementUnitsHaveChanged()) {
            numberOfMeasurementUnits = unitOfMeasure.getNumberOfMeasurementUnits();
            notifyPropertyChanged(BR.numberOfMeasurementUnits);
        }
    }

    private boolean numberOfMeasurementUnitsHaveChanged() {
        return numberOfMeasurementUnits != unitOfMeasure.getNumberOfMeasurementUnits();
    }

    @Bindable
    public String getConversionFactorInView() {
        return conversionFactorInView;
    }

    public void setConversionFactorInView(String conversionFactorInView) {
        if (!updatingUi) {
            if (isConversionFactorChanged(conversionFactorInView)) {
                if (!conversionFactorInView.isEmpty()) {
                    double conversionFactorParsed = parseDecimalFromString(conversionFactorInView);

                    if (conversionFactorParsed == MEASUREMENT_ERROR)
                        conversionFactorErrorMessage.set(numberFormatExceptionErrorMessage());
                    else {
                        this.conversionFactorParsed = conversionFactorParsed;
                        updateMeasurementModel();
                    }
                }
            }
        }
    }

    private boolean isConversionFactorChanged(String conversionFactorInView) {
        return !this.conversionFactorInView.equals(conversionFactorInView);
    }

    private void measurementOneUpdated() {
        if (!updatingUi)
            processMeasurementOne();
    }

    private void processMeasurementOne() {
        double measurementOneParsed = parseDecimalFromObservable(measurementOne);
        if (measurementOneParsed != MEASUREMENT_ERROR) {
            this.measurementOneParsed = measurementOneParsed;
            updateMeasurementModel();
        }
    }

    private double parseDecimalFromString(String decimalStringToParse) {
        try {
            return Double.parseDouble(decimalStringToParse);
        } catch (NumberFormatException e) {
            return MEASUREMENT_ERROR;
        }
    }

    private String numberFormatExceptionErrorMessage() {
        return resources.getString(R.string.number_format_exception);
    }

    private double parseDecimalFromObservable(ObservableField<String> measurement) {
        String rawMeasurement = measurement.get();
        if (rawMeasurement.isEmpty() || rawMeasurement.equals("."))
            return 0;
        try {
            return Double.parseDouble(rawMeasurement);
        } catch (NumberFormatException e) {
            if (measurement == measurementOne)
                measurementOneErrorMessage.set(
                        resources.getString(R.string.number_format_exception));
            return MEASUREMENT_ERROR;
        }
    }

    private void measurementTwoUpdated() {
        if (!updatingUi)
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
            useCasePortionCalculator.processModel(updatedMeasurementModel());
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
    public void dataLoadingFailed(UseCaseIngredientPortionCalculator.FailReason reason) {
        // TODO
    }

    @Override
    public void useCasePortionResult(MeasurementResult result) {
        measurementModel = result.getModel();
        processMeasurementModelResult();
        processResultStatus(result.getResult());
    }

    private void processMeasurementModelResult() {
        updatingUi = true;
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

        if (subtype != unitOfMeasure.getMeasurementSubtype()) {
            subtype = unitOfMeasure.getMeasurementSubtype();
            notifyPropertyChanged(BR.subtype);
        }

        isConversionFactorEnabled.set(unitOfMeasure.isConversionFactorEnabled());
    }

    private boolean conversionFactorHasChanged() {
        return measurementModel.getConversionFactor() != conversionFactorParsed;
    }

    private void updateConversionFactor() {
        conversionFactorParsed = measurementModel.getConversionFactor();
        conversionFactorInView = numberFormatter.formatDecimalForDisplay(conversionFactorParsed);
        notifyPropertyChanged(BR.conversionFactorInView);
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

    private void processResultStatus(UseCaseIngredientPortionCalculator.ResultStatus resultStatus) {
        if (resultStatus == UseCaseIngredientPortionCalculator.ResultStatus.INVALID_CONVERSION_FACTOR) {
            conversionFactorErrorMessage.set(null);
            conversionFactorErrorMessage.set(resources.getString(
                    R.string.conversion_factor_error_message,
                    UnitOfMeasureConstants.MIN_CONVERSION_FACTOR,
                    UnitOfMeasureConstants.MAX_CONVERSION_FACTOR));

        } else if (resultStatus == UseCaseIngredientPortionCalculator.ResultStatus.INVALID_TOTAL_MEASUREMENT_ONE) {
            measurementOneErrorMessage.set(null);
            measurementOneErrorMessage.set("Tablespoons and/or teaspoons need to have a value between 0.1 tsp and 666 Tbsp");

        } else if (resultStatus == UseCaseIngredientPortionCalculator.ResultStatus.INVALID_TOTAL_MEASUREMENT_TWO) {
            measurementTwoErrorMessage.set(null);
            measurementTwoErrorMessage.set("Tablespoons and/or teaspoons need to have a value between 0.1 tsp and 666 Tbsp");

        } else if (resultStatus == UseCaseIngredientPortionCalculator.ResultStatus.RESULT_OK) {
            hideAllInputErrors();
        }
        useCaseConversionFactorStatus.getConversionFactorStatus(
                subtype,
                useCasePortionCalculator.getIngredientId());
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
        updatingUi = false;
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

    @Override
    protected void onCleared() {
        useCasePortionCalculator.unregisterListener(this);
        useCaseConversionFactorStatus.unregisterListener(this);
        super.onCleared();
    }
}