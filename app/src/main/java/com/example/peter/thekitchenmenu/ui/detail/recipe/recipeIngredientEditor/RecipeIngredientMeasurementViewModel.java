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

    private UseCaseIngredientPortionCalculator useCasePortionCalculator;

    private UseCaseConversionFactorStatus useCaseConversionFactorStatus;
    public final ObservableField<String> conversionFactorErrorMessage = new ObservableField<>();
    public final ObservableBoolean advancedCheckBox = new ObservableBoolean();
    public final ObservableBoolean showConversionFactorFields = new ObservableBoolean();
    public final ObservableBoolean showUneditableConversionFactorFields = new ObservableBoolean();


    private boolean isConversionFactorEnabled;

    public final ObservableField<String> measurementOneErrorMessage = new ObservableField<>();
    public final ObservableField<String> measurementTwoErrorMessage = new ObservableField<>();

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

        advancedCheckBox.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable sender, int propertyId) {
                advancedCheckBoxUpdated();
            }
        });
        registerWithUseCase();
    }

    private void registerWithUseCase() {
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
        return measurementModel.getSubtype();
    }

    public void setSubtype(MeasurementSubtype subtype) {
        if (!updatingUi) {
            if (measurementModel.getSubtype() != subtype) {
                useCasePortionCalculator.processModel(new MeasurementModel(
                        measurementModel.getType(),
                        subtype,
                        measurementModel.getNumberOfMeasurementUnits(),
                        measurementModel.isConversionFactorEnabled(),
                        measurementModel.getConversionFactor(),
                        measurementModel.getItemBaseUnits(),
                        measurementModel.getTotalBaseUnits(),
                        measurementModel.getNumberOfItems(),
                        measurementModel.getTotalMeasurementOne(),
                        measurementModel.getItemMeasurementOne(),
                        measurementModel.getTotalMeasurementTwo(),
                        measurementModel.getItemMeasurementTwo(),
                        measurementModel.isValidMeasurement(),
                        measurementModel.getMinimumMeasurement(),
                        measurementModel.getMaxMeasurementOne(),
                        measurementModel.getMaxMeasurementTwo(),
                        measurementModel.getMeasurementUnitDigitWidths()
                ));
            }
        }
    }

    @Bindable
    public int getNumberOfMeasurementUnits() {
        return measurementModel.getNumberOfMeasurementUnits();
    }

    @Bindable
    public boolean isConversionFactorEnabled() {
        return isConversionFactorEnabled;
    }

    @Bindable
    public String getConversionFactor() {
        return numberFormatter.formatDecimalForDisplay(measurementModel.getConversionFactor());
    }

    public void setConversionFactor(String conversionFactor) {
        if (!updatingUi) {
            if (!conversionFactor.isEmpty()) {
                double conversionFactorParsed = parseDecimalFromString(conversionFactor);

                if (conversionFactorParsed == MEASUREMENT_ERROR)
                    conversionFactorErrorMessage.set(numberFormatExceptionErrorMessage());

                else if (isConversionFactorChanged(conversionFactorParsed)) {
                    useCasePortionCalculator.processModel(new MeasurementModel(
                            measurementModel.getType(),
                            measurementModel.getSubtype(),
                            measurementModel.getNumberOfMeasurementUnits(),
                            measurementModel.isConversionFactorEnabled(),
                            conversionFactorParsed,
                            measurementModel.getItemBaseUnits(),
                            measurementModel.getTotalBaseUnits(),
                            measurementModel.getNumberOfItems(),
                            measurementModel.getTotalMeasurementOne(),
                            measurementModel.getItemMeasurementOne(),
                            measurementModel.getTotalMeasurementTwo(),
                            measurementModel.getItemMeasurementTwo(),
                            measurementModel.isValidMeasurement(),
                            measurementModel.getMinimumMeasurement(),
                            measurementModel.getMaxMeasurementOne(),
                            measurementModel.getMaxMeasurementTwo(),
                            measurementModel.getMeasurementUnitDigitWidths()
                    ));
                }
            }
        }
    }


    private boolean isConversionFactorChanged(double conversionFactorParsed) {
        return Double.compare(measurementModel.getConversionFactor(), conversionFactorParsed) != 0;
    }

    @Bindable
    public String getMeasurementOne() {
        return numberFormatter.formatDecimalForDisplay(measurementModel.getTotalMeasurementOne());
    }

    public void setMeasurementOne(String measurementOne) {
        if (!updatingUi) {
            if (!measurementOne.isEmpty()) {
                double measurementOneParsed = parseDecimalFromString(measurementOne);

                if (measurementOneParsed == MEASUREMENT_ERROR)
                    measurementOneErrorMessage.set(numberFormatExceptionErrorMessage());

                else if (isMeasurementOneChanged(measurementOneParsed)) {
                    useCasePortionCalculator.processModel(new MeasurementModel(
                            measurementModel.getType(),
                            measurementModel.getSubtype(),
                            measurementModel.getNumberOfMeasurementUnits(),
                            measurementModel.isConversionFactorEnabled(),
                            measurementModel.getConversionFactor(),
                            measurementModel.getItemBaseUnits(),
                            measurementModel.getTotalBaseUnits(),
                            measurementModel.getNumberOfItems(),
                            measurementOneParsed,
                            measurementModel.getItemMeasurementOne(),
                            measurementModel.getTotalMeasurementTwo(),
                            measurementModel.getItemMeasurementTwo(),
                            measurementModel.isValidMeasurement(),
                            measurementModel.getMinimumMeasurement(),
                            measurementModel.getMaxMeasurementOne(),
                            measurementModel.getMaxMeasurementTwo(),
                            measurementModel.getMeasurementUnitDigitWidths()
                    ));
                }
            }
        }
    }

    private boolean isMeasurementOneChanged(double measurementOne) {
        return Double.compare(measurementModel.getTotalMeasurementOne(), measurementOne) != 0;
    }

    private double parseDecimalFromString(String decimalStringToParse) {
        try {
            return Double.parseDouble(decimalStringToParse);
        } catch (NumberFormatException e) {
            return MEASUREMENT_ERROR;
        }
    }


    @Bindable
    public String getMeasurementTwo() {
        return numberFormatter.formatIntegerForDisplay(measurementModel.getTotalMeasurementTwo());
    }

    public void setMeasurementTwo(String measurementTwo) {
        if (!updatingUi) {
            if (!measurementTwo.isEmpty()) {
                int measurementTwoParsed = parseIntegerFromString(measurementTwo);

                if (measurementTwoParsed == MEASUREMENT_ERROR)
                    measurementTwoErrorMessage.set(numberFormatExceptionErrorMessage());

                else if (isMeasurementTwoChanged(measurementTwoParsed)){
                    useCasePortionCalculator.processModel(new MeasurementModel(
                            measurementModel.getType(),
                            measurementModel.getSubtype(),
                            measurementModel.getNumberOfMeasurementUnits(),
                            measurementModel.isConversionFactorEnabled(),
                            measurementModel.getConversionFactor(),
                            measurementModel.getItemBaseUnits(),
                            measurementModel.getTotalBaseUnits(),
                            measurementModel.getNumberOfItems(),
                            measurementModel.getTotalMeasurementOne(),
                            measurementModel.getItemMeasurementOne(),
                            measurementTwoParsed,
                            measurementModel.getItemMeasurementTwo(),
                            measurementModel.isValidMeasurement(),
                            measurementModel.getMinimumMeasurement(),
                            measurementModel.getMaxMeasurementOne(),
                            measurementModel.getMaxMeasurementTwo(),
                            measurementModel.getMeasurementUnitDigitWidths()
                    ));
                }
            }
        }
    }

    private boolean isMeasurementTwoChanged(int measurementTwo) {
        return Double.compare(measurementModel.getTotalMeasurementTwo(), measurementTwo) != 0;
    }

    private int parseIntegerFromString(String measurementTwoInView) {
        try {
            return Integer.parseInt(measurementTwoInView);
        } catch (NumberFormatException e) {
            return MEASUREMENT_ERROR;
        }
    }

    private String numberFormatExceptionErrorMessage() {
        return resources.getString(R.string.number_format_exception);
    }

    @Override
    public void dataLoadingFailed(UseCaseIngredientPortionCalculator.FailReason reason) {
        // TODO
    }

    @Override
    public void useCasePortionResult(MeasurementResult result) {
        processModelResult(result.getModel());
        processResultStatus(result.getResult());
    }

    private void processModelResult(MeasurementModel resultModel) {
        measurementModel = resultModel;
        updatingUi = true;

        notifyPropertyChanged(BR.subtype);
        notifyPropertyChanged(BR.numberOfMeasurementUnits);
        notifyPropertyChanged(BR.conversionFactorEnabled);
        notifyPropertyChanged(BR.conversionFactor);
        notifyPropertyChanged(BR.measurementOne);
        notifyPropertyChanged(BR.measurementTwo);
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
        useCaseConversionFactorStatus.getStatus(subtype, useCasePortionCalculator.getIngredientId());
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
//        isConversionFactorEnabledObservable.set(false);
        showConversionFactorFields.set(false);
        showUneditableConversionFactorFields.set(false);
    }

    private void showUneditableConversionFactorInformation() {
//        isConversionFactorEnabledObservable.set(true);
        showConversionFactorFields.set(false);
        showUneditableConversionFactorFields.set(true);
    }

    private void showOptionToAddConversionFactorInformation() {
//        isConversionFactorEnabledObservable.set(true);
        showConversionFactorFields.set(false);
        advancedCheckBox.set(false);
        showUneditableConversionFactorFields.set(false);
    }

    private void showAllConversionFactorInformation() {
//        isConversionFactorEnabledObservable.set(true);
        showConversionFactorFields.set(true);
        advancedCheckBox.set(true);
        showUneditableConversionFactorFields.set(false);
    }

    private void setupForNewSubtype() {
        unitOfMeasure = subtype.getMeasurementClass();
//        isConversionFactorEnabledObservable.set(unitOfMeasure.isConversionFactorEnabled());
        updateNumberOfMeasurementUnits();
//        updateMeasurementModel();
    }

    private boolean numberOfMeasurementUnitsHaveChanged() {
        return numberOfMeasurementUnits != unitOfMeasure.getNumberOfMeasurementUnits();
    }

    private void updateNumberOfMeasurementUnits() {
        if (numberOfMeasurementUnitsHaveChanged()) {
            numberOfMeasurementUnits = unitOfMeasure.getNumberOfMeasurementUnits();
            notifyPropertyChanged(BR.numberOfMeasurementUnits);
        }
    }

    private void advancedCheckBoxUpdated() {
//        if (shouldShowConversionFactorFields())
            showConversionFactorFields.set(true);
//        else
//            showConversionFactorFields.set(false);
    }

//    private boolean shouldShowConversionFactorFields() {
////        return isConversionFactorEnabledObservable.get() && advancedCheckBox.get();
//    }

//    private void updateMeasurementModel() {
//        if (measurementModelHasChanged())
//            useCasePortionCalculator.processModel(updatedMeasurementModel());
//    }
//
//    private boolean measurementModelHasChanged() {
//        return !measurementModel.equals(updatedMeasurementModel());
//    }

//    private MeasurementModel updatedMeasurementModel() {
//        return new MeasurementModel(
//                unitOfMeasure.getMeasurementSubtype(),
//                measurementModel.getNumberOfItems(),
//                conversionFactorParsed,
//                measurementOneParsed,
//                measurementTwoParsed,
//                measurementModel.getItemMeasurementOne(),
//                measurementModel.getItemMeasurementTwo(),
//                measurementModel.getItemBaseUnits()
//        );
//    }

    @Override
    protected void onCleared() {
        useCasePortionCalculator.unregisterListener(this);
        useCaseConversionFactorStatus.unregisterListener(this);
        super.onCleared();
    }
}