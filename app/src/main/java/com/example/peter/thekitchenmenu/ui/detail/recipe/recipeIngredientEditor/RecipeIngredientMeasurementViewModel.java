package com.example.peter.thekitchenmenu.ui.detail.recipe.recipeingredienteditor;

import android.content.res.Resources;

import androidx.databinding.Bindable;
import androidx.databinding.library.baseAdapters.BR;

import com.example.peter.thekitchenmenu.R;
import com.example.peter.thekitchenmenu.domain.model.MeasurementModel;
import com.example.peter.thekitchenmenu.domain.model.MeasurementModelBuilder;
import com.example.peter.thekitchenmenu.domain.usecase.UseCase;
import com.example.peter.thekitchenmenu.domain.usecase.UseCaseConversionFactorStatus;
import com.example.peter.thekitchenmenu.domain.usecase.UseCaseHandler;
import com.example.peter.thekitchenmenu.domain.usecase.UseCasePortionCalculator;
import com.example.peter.thekitchenmenu.ui.ObservableViewModel;
import com.example.peter.thekitchenmenu.utils.NumberFormatter;
import com.example.peter.thekitchenmenu.domain.unitofmeasureentities.MeasurementSubtype;
import com.example.peter.thekitchenmenu.domain.unitofmeasureentities.UnitOfMeasureConstants;

import static com.example.peter.thekitchenmenu.domain.usecase.UseCaseConversionFactorStatus.*;

public class RecipeIngredientMeasurementViewModel extends ObservableViewModel {

//    private static final String TAG = "tkm-RecipeIngredientMea";

    private Resources resources;
    private NumberFormatter numberFormatter;

    private static final int MEASUREMENT_ERROR = -1;

    private UseCaseHandler useCaseHandler;
    private UseCasePortionCalculator useCasePortionCalculator;
    private UseCaseConversionFactorStatus useCaseConversionFactorStatus;

    private boolean advancedCheckBoxChecked;
    private boolean showConversionFactorFields;
    private boolean showUneditableConversionFactorFields;

    private String conversionFactorErrorMessage;
    private String unitOneErrorMessage;
    private String unitTwoErrorMessage;
    private String recipeId = "";
    private String ingredientId = "";
    private String recipeIngredientId = "";

    private MeasurementModel measurementModel = UnitOfMeasureConstants.DEFAULT_MEASUREMENT_MODEL;
    private boolean updatingUi;

    public RecipeIngredientMeasurementViewModel(UseCaseHandler useCaseHandler,
                                                UseCasePortionCalculator useCasePortionCalculator,
                                                UseCaseConversionFactorStatus useCaseConversionFactor,
                                                Resources resources,
                                                NumberFormatter numberFormatter) {
        this.useCaseHandler = useCaseHandler;
        this.useCasePortionCalculator = useCasePortionCalculator;
        this.useCaseConversionFactorStatus = useCaseConversionFactor;
        this.resources = resources;
        this.numberFormatter = numberFormatter;
    }

    public void start(String recipeId, String ingredientId) {
        // Only loads data on first start or change of recipeId, not after configuration change!
        if (this.recipeId == null || !this.recipeId.equals(recipeId)) {
            this.recipeId = recipeId;
            this.ingredientId = ingredientId;
            createPortionCalculatorRequestValues(measurementModel);
        }
    }

    public void start(String recipeIngredientId) {
        if (this.recipeIngredientId == null || !this.recipeIngredientId.equals(recipeIngredientId)) {
            this.recipeIngredientId = recipeIngredientId;
            createPortionCalculatorRequestValues(measurementModel);
        }
    }

    @Bindable
    public MeasurementSubtype getSubtype() {
        return measurementModel.getSubtype();
    }

    public void setSubtype(MeasurementSubtype subtype) {
        if (!updatingUi) {
            if (measurementModel.getSubtype() != subtype) {
                MeasurementModel model = MeasurementModelBuilder.
                        basedOnModel(measurementModel).
                        setSubtype(subtype).
                        build();

                createPortionCalculatorRequestValues(model);
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
                    MeasurementModel model = MeasurementModelBuilder.
                            basedOnModel(measurementModel).
                            setConversionFactor(conversionFactorParsed).
                            build();

                    createPortionCalculatorRequestValues(model);
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
                    MeasurementModel model = MeasurementModelBuilder.
                            basedOnModel(measurementModel).
                            setTotalUnitOne(unitOneParsed).
                            build();

                    createPortionCalculatorRequestValues(model);
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

                } else if (isUnitTwoChanged(unitTwoParsed)) {
                    MeasurementModel model = MeasurementModelBuilder.
                            basedOnModel(measurementModel).
                            setTotalUnitTwo(unitTwoParsed).
                            build();

                    createPortionCalculatorRequestValues(model);
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

    private void createPortionCalculatorRequestValues(MeasurementModel model) {
        UseCasePortionCalculator.RequestValues requestValues =
                new UseCasePortionCalculator.RequestValues(
                        recipeId,
                        ingredientId,
                        recipeIngredientId,
                        model
                );
        executePortionCalculator(requestValues);
    }

    private void executePortionCalculator(UseCasePortionCalculator.RequestValues requestValues) {
        useCaseHandler.execute(
                useCasePortionCalculator,
                requestValues,
                new UseCaseCallback<UseCasePortionCalculator.ResponseValues>() {
                    @Override
                    public void onSuccess(UseCasePortionCalculator.ResponseValues response) {
                        processModelResult(response.getModel());
                        processResultStatus(response.getResultStatus());

                    }

                    @Override
                    public void onError(UseCasePortionCalculator.ResponseValues response) {
                        processModelResult(response.getModel());
                        processResultStatus(response.getResultStatus());
                    }
                });
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

    private void processResultStatus(UseCasePortionCalculator.ResultStatus resultStatus) {
        if (resultStatus == UseCasePortionCalculator.
                ResultStatus.INVALID_CONVERSION_FACTOR) {
            conversionFactorErrorMessage = null;
            conversionFactorErrorMessage = resources.getString(
                    R.string.conversion_factor_error_message,
                    UnitOfMeasureConstants.MIN_CONVERSION_FACTOR,
                    UnitOfMeasureConstants.MAX_CONVERSION_FACTOR);

        } else if (resultStatus ==
                UseCasePortionCalculator.
                        ResultStatus.INVALID_TOTAL_UNIT_ONE) {
            unitOneErrorMessage = null;
            unitOneErrorMessage = "Tablespoons and/or teaspoons need to have a value " +
                    "between 0.1 tsp and 666 Tbsp";

        } else if (resultStatus == UseCasePortionCalculator.
                ResultStatus.INVALID_TOTAL_UNIT_TWO) {
            unitTwoErrorMessage = null;
            unitTwoErrorMessage = "Tablespoons and/or teaspoons need to have a value " +
                    "between 0.1 tsp and 666 Tbsp";

        } else if (resultStatus == UseCasePortionCalculator.ResultStatus.RESULT_OK) {
            hideAllInputErrors();
        }

        useCaseHandler.execute(useCaseConversionFactorStatus,
                new UseCaseConversionFactorStatus.RequestValues(
                        measurementModel.getSubtype(), useCasePortionCalculator.getIngredientId()),
                new UseCase.UseCaseCallback<UseCaseConversionFactorStatus.ResponseValues>() {

                    @Override
                    public void onSuccess(UseCaseConversionFactorStatus.ResponseValues
                                                  response) {
                        useCaseConversionFactorResult(response.getResult());
                    }

                    @Override
                    public void onError(UseCaseConversionFactorStatus.ResponseValues
                                                response) {
                        useCaseConversionFactorResult(response.getResult());
                    }
                });
    }

    private void hideAllInputErrors() {
        conversionFactorErrorMessage = null;
        unitOneErrorMessage = null;
        unitTwoErrorMessage = null;
    }

    private void useCaseConversionFactorResult(UseCaseResult result) {
        if (result == UseCaseResult.DISABLED) {
            hideAllConversionFactorInformation();

        } else if (result == UseCaseResult.ENABLED_UNEDITABLE) {
            showUneditableConversionFactorInformation();

        } else if (result == UseCaseResult.ENABLED_EDITABLE_UNSET) {
            showOptionToAddConversionFactorInformation();

        } else if (result == UseCaseResult.ENABLED_EDITABLE_SET) {
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
}