package com.example.peter.thekitchenmenu.ui.detail.recipe.recipeingredienteditor;

import android.content.res.Resources;

import androidx.databinding.Bindable;
import androidx.databinding.library.baseAdapters.BR;

import com.example.peter.thekitchenmenu.R;
import com.example.peter.thekitchenmenu.domain.model.MeasurementModel;
import com.example.peter.thekitchenmenu.domain.usecase.UseCase;
import com.example.peter.thekitchenmenu.domain.usecase.UseCaseConversionFactorStatus;
import com.example.peter.thekitchenmenu.domain.usecase.UseCaseHandler;
import com.example.peter.thekitchenmenu.ui.ObservableViewModel;
import com.example.peter.thekitchenmenu.utils.NumberFormatter;
import com.example.peter.thekitchenmenu.domain.usecase.MeasurementResult;
import com.example.peter.thekitchenmenu.domain.unitofmeasureentities.MeasurementSubtype;
import com.example.peter.thekitchenmenu.domain.unitofmeasureentities.UnitOfMeasureConstants;
import com.example.peter.thekitchenmenu.domain.usecase.UseCaseIngredientPortionCalculator;

import static com.example.peter.thekitchenmenu.domain.usecase.UseCaseConversionFactorStatus.*;

public class RecipeIngredientMeasurementViewModel
        extends
        ObservableViewModel
        implements
        UseCaseIngredientPortionCalculator.UseCasePortionCallback {

    private static final String TAG = "tkm-RecipeIngredientMea";

    private Resources resources;
    private NumberFormatter numberFormatter;

    private static final int MEASUREMENT_ERROR = -1;

    private UseCaseHandler useCaseHandler;
    private UseCaseIngredientPortionCalculator useCasePortionCalculator;
    private UseCaseConversionFactorStatus useCaseConversionFactorStatus;

    private boolean advancedCheckBoxChecked;
    private boolean showConversionFactorFields;
    private boolean showUneditableConversionFactorFields;

    private String conversionFactorErrorMessage;
    private String unitOneErrorMessage;
    private String unitTwoErrorMessage;
    private String recipeId;
    private String recipeIngredientId;

    private MeasurementModel measurementModel = UnitOfMeasureConstants.DEFAULT_MEASUREMENT_MODEL;
    private boolean updatingUi;

    public RecipeIngredientMeasurementViewModel(UseCaseHandler useCaseHandler,
                                                UseCaseIngredientPortionCalculator useCasePortionCalculator,
                                                UseCaseConversionFactorStatus useCaseConversionFactor,
                                                Resources resources,
                                                NumberFormatter numberFormatter) {
        this.useCaseHandler = useCaseHandler;
        this.useCasePortionCalculator = useCasePortionCalculator;
        this.useCaseConversionFactorStatus = useCaseConversionFactor;
        this.resources = resources;
        this.numberFormatter = numberFormatter;
        registerWithUseCase();
    }

    private void registerWithUseCase() {
        useCasePortionCalculator.registerListener(this);
    }

    public void start(String recipeId, String ingredientId) {
        if (this.recipeId == null || !this.recipeId.equals(recipeId)) {
            this.recipeId = recipeId;
            useCasePortionCalculator.start(recipeId, ingredientId);
        }
    }

    public void start(String recipeIngredientId) {
        if (this.recipeIngredientId == null || !this.recipeIngredientId.equals(recipeIngredientId)) {
            this.recipeIngredientId = recipeIngredientId;
            useCasePortionCalculator.start(recipeIngredientId);
        }
    }

    @Bindable
    public MeasurementSubtype getSubtype() {
        return measurementModel.getSubtype();
    }

    public void setSubtype(MeasurementSubtype subtype) {
        if (!updatingUi) {
            if (measurementModel.getSubtype() != subtype) {
                MeasurementModel model = new MeasurementModel(
                        measurementModel.getType(),
                        subtype,
                        measurementModel.getNumberOfUnits(),
                        measurementModel.isConversionFactorEnabled(),
                        measurementModel.getConversionFactor(),
                        measurementModel.getItemBaseUnits(),
                        measurementModel.getTotalBaseUnits(),
                        measurementModel.getNumberOfItems(),
                        measurementModel.getTotalUnitOne(),
                        measurementModel.getItemUnitOne(),
                        measurementModel.getTotalUnitTwo(),
                        measurementModel.getItemUnitTwo(),
                        measurementModel.isValidMeasurement(),
                        measurementModel.getMinUnitOne(),
                        measurementModel.getMaxUnitOne(),
                        measurementModel.getMaxUnitTwo(),
                        measurementModel.getMaxUnitDigitWidths()
                );

//                Log.d(TAG, "         setSubtype: " + model);
                useCasePortionCalculator.processModel(model);
            }
        }
    }

    @Bindable
    public int getNumberOfUnits() {
        return measurementModel.getNumberOfUnits();
    }

    @Bindable
    public boolean isConversionFactorEnabled() {
        return measurementModel.isConversionFactorEnabled();
    }

    @Bindable
    public boolean isShowConversionFactorFields() {
        return showConversionFactorFields;
    }

    @Bindable
    public boolean isShowUneditableConversionFactorFields() {
        return showUneditableConversionFactorFields;
    }

    @Bindable
    public boolean isAdvancedCheckBoxChecked() {
        return advancedCheckBoxChecked;
    }

    public void setAdvancedCheckBoxChecked(boolean advancedCheckBoxChecked) {
        showConversionFactorFields = advancedCheckBoxChecked;
        notifyPropertyChanged(BR.showConversionFactorFields);
    }

    @Bindable
    public String getConversionFactor() {
        return numberFormatter.formatDecimalForDisplay(measurementModel.getConversionFactor());
    }

    public void setConversionFactor(String conversionFactor) {
        if (!updatingUi) {
            if (!conversionFactor.isEmpty()) {
                double conversionFactorParsed = parseDecimalFromString(conversionFactor);

                if (conversionFactorParsed == MEASUREMENT_ERROR) {
                    conversionFactorErrorMessage = numberFormatExceptionErrorMessage();
                    notifyPropertyChanged(BR.conversionFactorErrorMessage);

                } else if (isConversionFactorChanged(conversionFactorParsed)) {
                    MeasurementModel model = new MeasurementModel(
                            measurementModel.getType(),
                            measurementModel.getSubtype(),
                            measurementModel.getNumberOfUnits(),
                            measurementModel.isConversionFactorEnabled(),
                            conversionFactorParsed,
                            measurementModel.getItemBaseUnits(),
                            measurementModel.getTotalBaseUnits(),
                            measurementModel.getNumberOfItems(),
                            measurementModel.getTotalUnitOne(),
                            measurementModel.getItemUnitOne(),
                            measurementModel.getTotalUnitTwo(),
                            measurementModel.getItemUnitTwo(),
                            measurementModel.isValidMeasurement(),
                            measurementModel.getMinUnitOne(),
                            measurementModel.getMaxUnitOne(),
                            measurementModel.getMaxUnitTwo(),
                            measurementModel.getMaxUnitDigitWidths()
                    );

//                    Log.d(TAG, "setConversionFactor: " + model);
                    useCasePortionCalculator.processModel(model);
                }
            }
        }
    }


    private boolean isConversionFactorChanged(double conversionFactorParsed) {
        return Double.compare(measurementModel.getConversionFactor(), conversionFactorParsed) != 0;
    }

    @Bindable
    public String getConversionFactorErrorMessage() {
        return conversionFactorErrorMessage;
    }

    @Bindable
    public String getUnitOne() {
        return numberFormatter.formatDecimalForDisplay(measurementModel.getTotalUnitOne());
    }

    public void setUnitOne(String unitOne) {
        if (!updatingUi) {
            if (!unitOne.isEmpty()) {
                double unitOneParsed = parseDecimalFromString(unitOne);

                if (unitOneParsed == MEASUREMENT_ERROR) {
                    unitOneErrorMessage = numberFormatExceptionErrorMessage();
                    notifyPropertyChanged(BR.unitOneErrorMessage);

                } else if (isUnitOneChanged(unitOneParsed)) {
                    MeasurementModel model = new MeasurementModel(
                            measurementModel.getType(),
                            measurementModel.getSubtype(),
                            measurementModel.getNumberOfUnits(),
                            measurementModel.isConversionFactorEnabled(),
                            measurementModel.getConversionFactor(),
                            measurementModel.getItemBaseUnits(),
                            measurementModel.getTotalBaseUnits(),
                            measurementModel.getNumberOfItems(),
                            unitOneParsed,
                            measurementModel.getItemUnitOne(),
                            measurementModel.getTotalUnitTwo(),
                            measurementModel.getItemUnitTwo(),
                            measurementModel.isValidMeasurement(),
                            measurementModel.getMinUnitOne(),
                            measurementModel.getMaxUnitOne(),
                            measurementModel.getMaxUnitTwo(),
                            measurementModel.getMaxUnitDigitWidths()
                    );
//                    Log.d(TAG, "         setUnitOne: " + model);
                    useCasePortionCalculator.processModel(model);
                }
            }
        }
    }

    private boolean isUnitOneChanged(double unitOne) {
        return Double.compare(measurementModel.getTotalUnitOne(), unitOne) != 0;
    }

    private double parseDecimalFromString(String decimalToParse) {
        try {
            return Double.parseDouble(decimalToParse);
        } catch (NumberFormatException e) {
            return MEASUREMENT_ERROR;
        }
    }

    @Bindable
    public String getUnitOneErrorMessage() {
        return unitOneErrorMessage;
    }

    @Bindable
    public String getUnitTwo() {
        return numberFormatter.formatIntegerForDisplay(measurementModel.getTotalUnitTwo());
    }

    public void setUnitTwo(String unitTwo) {
        if (!updatingUi) {
            if (!unitTwo.isEmpty()) {
                int unitTwoParsed = parseIntegerFromString(unitTwo);

                if (unitTwoParsed == MEASUREMENT_ERROR) {
                    unitTwoErrorMessage = numberFormatExceptionErrorMessage();
                    notifyPropertyChanged(BR.unitTwoErrorMessage);

                } else if (isUnitTwoChanged(unitTwoParsed)){
                    MeasurementModel model = new MeasurementModel(
                            measurementModel.getType(),
                            measurementModel.getSubtype(),
                            measurementModel.getNumberOfUnits(),
                            measurementModel.isConversionFactorEnabled(),
                            measurementModel.getConversionFactor(),
                            measurementModel.getItemBaseUnits(),
                            measurementModel.getTotalBaseUnits(),
                            measurementModel.getNumberOfItems(),
                            measurementModel.getTotalUnitOne(),
                            measurementModel.getItemUnitOne(),
                            unitTwoParsed,
                            measurementModel.getItemUnitTwo(),
                            measurementModel.isValidMeasurement(),
                            measurementModel.getMinUnitOne(),
                            measurementModel.getMaxUnitOne(),
                            measurementModel.getMaxUnitTwo(),
                            measurementModel.getMaxUnitDigitWidths()
                    );
//                    Log.d(TAG, "         setUnitTwo: " + model);
                    useCasePortionCalculator.processModel(model);
                }
            }
        }
    }

    private boolean isUnitTwoChanged(int unitTwo) {
        return Double.compare(measurementModel.getTotalUnitTwo(), unitTwo) != 0;
    }

    private int parseIntegerFromString(String measurementTwoInView) {
        try {
            return Integer.parseInt(measurementTwoInView);
        } catch (NumberFormatException e) {
            return MEASUREMENT_ERROR;
        }
    }

    @Bindable
    public String getUnitTwoErrorMessage() {
        return unitTwoErrorMessage;
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
//        Log.d(TAG, " processModelResult: " + resultModel);
        measurementModel = resultModel;
        updatingUi = true;

        notifyPropertyChanged(BR.subtype);
        notifyPropertyChanged(BR.numberOfUnits);
        notifyPropertyChanged(BR.conversionFactorEnabled);
        notifyPropertyChanged(BR.conversionFactor);
        notifyPropertyChanged(BR.unitOne);
        notifyPropertyChanged(BR.unitTwo);
    }

    private void processResultStatus(UseCaseIngredientPortionCalculator.ResultStatus resultStatus) {
        if (resultStatus == UseCaseIngredientPortionCalculator.
                ResultStatus.INVALID_CONVERSION_FACTOR) {
            conversionFactorErrorMessage = null;
            conversionFactorErrorMessage = resources.getString(
                    R.string.conversion_factor_error_message,
                    UnitOfMeasureConstants.MIN_CONVERSION_FACTOR,
                    UnitOfMeasureConstants.MAX_CONVERSION_FACTOR);

        } else if (resultStatus ==
                UseCaseIngredientPortionCalculator.
                        ResultStatus.INVALID_TOTAL_MEASUREMENT_ONE) {
            unitOneErrorMessage = null;
            unitOneErrorMessage = "Tablespoons and/or teaspoons need to have a value " +
                    "between 0.1 tsp and 666 Tbsp";

        } else if (resultStatus == UseCaseIngredientPortionCalculator.
                ResultStatus.INVALID_TOTAL_MEASUREMENT_TWO) {
            unitTwoErrorMessage = null;
            unitTwoErrorMessage = "Tablespoons and/or teaspoons need to have a value " +
                    "between 0.1 tsp and 666 Tbsp";

        } else if (resultStatus == UseCaseIngredientPortionCalculator.ResultStatus.RESULT_OK) {
            hideAllInputErrors();
        }

        useCaseHandler.execute(
                useCaseConversionFactorStatus,
                new UseCaseConversionFactorStatus.RequestValues(
                        measurementModel.getSubtype(),
                        useCasePortionCalculator.getIngredientId()),
                        new UseCase.UseCaseCallback<UseCaseConversionFactorStatus.ResponseValues>() {

                            @Override
                            public void onSuccess(UseCaseConversionFactorStatus.ResponseValues response) {
                                useCaseConversionFactorResult(response.getResult());
                            }

                            @Override
                            public void onError(UseCaseConversionFactorStatus.ResponseValues response) {
                                useCaseConversionFactorResult(response.getResult());
                            }
                        });
    }

    private void hideAllInputErrors() {
        conversionFactorErrorMessage = null;
        unitOneErrorMessage = null;
        unitTwoErrorMessage = null;
    }

    private void useCaseConversionFactorResult(UseCaseConversionFactorResult result) {
        if (result == UseCaseConversionFactorResult.DISABLED) {
            hideAllConversionFactorInformation();

        } else if (result == UseCaseConversionFactorResult.ENABLED_UNEDITABLE) {
            showUneditableConversionFactorInformation();

        } else if (result == UseCaseConversionFactorResult.ENABLED_EDITABLE_UNSET) {
            showOptionToAddConversionFactorInformation();

        } else if (result == UseCaseConversionFactorResult.ENABLED_EDITABLE_SET) {
            showAllConversionFactorInformation();
        }
        updatingUi = false;
    }

    private void hideAllConversionFactorInformation() {
        notifyPropertyChanged(BR.conversionFactorEnabled);
        showConversionFactorFields = false;
        notifyPropertyChanged(BR.showConversionFactorFields);
        showUneditableConversionFactorFields = false;
        notifyPropertyChanged(BR.showUneditableConversionFactorFields);
    }

    private void showUneditableConversionFactorInformation() {
        notifyPropertyChanged(BR.conversionFactorEnabled);
        showConversionFactorFields = false;
        notifyPropertyChanged(BR.showConversionFactorFields);
        showUneditableConversionFactorFields = true;
        notifyPropertyChanged(BR.showUneditableConversionFactorFields);
    }

    private void showOptionToAddConversionFactorInformation() {
        notifyPropertyChanged(BR.conversionFactorEnabled);
        showConversionFactorFields = false;
        notifyPropertyChanged(BR.showConversionFactorFields);
        showUneditableConversionFactorFields = false;
        notifyPropertyChanged(BR.showUneditableConversionFactorFields);
        advancedCheckBoxChecked = false;
        notifyPropertyChanged(BR.advancedCheckBoxChecked);
    }

    private void showAllConversionFactorInformation() {
        notifyPropertyChanged(BR.conversionFactorEnabled);
        showConversionFactorFields = true;
        notifyPropertyChanged(BR.showConversionFactorFields);
        showUneditableConversionFactorFields = false;
        notifyPropertyChanged(BR.showUneditableConversionFactorFields);
        advancedCheckBoxChecked = true;
        notifyPropertyChanged(BR.advancedCheckBoxChecked);
    }

    @Override
    protected void onCleared() {
        useCasePortionCalculator.unregisterListener(this);
        super.onCleared();
    }
}