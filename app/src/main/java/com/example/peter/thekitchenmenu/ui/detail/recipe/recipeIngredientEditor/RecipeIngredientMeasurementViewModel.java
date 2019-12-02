package com.example.peter.thekitchenmenu.ui.detail.recipe.recipeingredienteditor;

import android.content.res.Resources;

import androidx.annotation.NonNull;
import androidx.databinding.Bindable;
import androidx.databinding.library.baseAdapters.BR;
import androidx.lifecycle.MutableLiveData;

import com.example.peter.thekitchenmenu.R;
import com.example.peter.thekitchenmenu.domain.UseCaseHandler;
import com.example.peter.thekitchenmenu.domain.entity.model.MeasurementModel;
import com.example.peter.thekitchenmenu.domain.entity.model.MeasurementModelBuilder;
import com.example.peter.thekitchenmenu.domain.usecase.conversionfactorstatus.UseCaseConversionFactorStatus;
import com.example.peter.thekitchenmenu.domain.usecase.conversionfactorstatus.UseCaseConversionFactorStatusRequest;
import com.example.peter.thekitchenmenu.domain.usecase.conversionfactorstatus.UseCaseConversionFactorStatusResponse;
import com.example.peter.thekitchenmenu.domain.usecase.recipeportioncalculator.UseCasePortionCalculator;
import com.example.peter.thekitchenmenu.domain.usecase.recipeportioncalculator.UseCasePortionCalculatorRequest;
import com.example.peter.thekitchenmenu.domain.usecase.recipeportioncalculator.UseCasePortionCalculatorResponse;
import com.example.peter.thekitchenmenu.ui.ObservableViewModel;
import com.example.peter.thekitchenmenu.ui.detail.common.MeasurementErrorMessageMaker;
import com.example.peter.thekitchenmenu.ui.utils.NumberFormatter;
import com.example.peter.thekitchenmenu.domain.entity.unitofmeasure.MeasurementSubtype;
import com.example.peter.thekitchenmenu.domain.entity.unitofmeasure.UnitOfMeasureConstants;

import static androidx.core.util.Preconditions.checkNotNull;
import static com.example.peter.thekitchenmenu.domain.entity.unitofmeasure.UnitOfMeasureConstants.NOT_SET;
import static com.example.peter.thekitchenmenu.domain.usecase.conversionfactorstatus.UseCaseConversionFactorStatus.*;

public class RecipeIngredientMeasurementViewModel extends ObservableViewModel {

    private Resources resources;
    private RecipeIngredientEditorNavigator navigator;
    private UseCaseHandler useCaseHandler;
    private NumberFormatter numberFormatter;
    private MeasurementErrorMessageMaker errorMessageMaker;
    private UseCasePortionCalculator useCasePortionCalculator;
    private UseCaseConversionFactorStatus useCaseConversionFactorStatus;

    private static final int MEASUREMENT_ERROR = -1;

    private MutableLiveData<Boolean> isValidMeasurement = new MutableLiveData<>();

    private boolean isAdvancedCheckBoxChecked;
    private boolean isShowConversionFactorEditableFields;
    private boolean isShowConversionFactorUneditableFields;
    private boolean isConversionFactorChangedThisSession;

    private String conversionFactorErrorMessage;
    private String unitOneErrorMessage;
    private String unitTwoErrorMessage;
    private String recipeId = "";
    private String ingredientId = "";
    private String recipeIngredientId = "";

    private MeasurementModel measurementModel = UnitOfMeasureConstants.DEFAULT_MEASUREMENT_MODEL;
    private boolean updatingUi;

    public RecipeIngredientMeasurementViewModel(
            @NonNull UseCaseHandler useCaseHandler,
            @NonNull UseCasePortionCalculator useCasePortionCalculator,
            @NonNull UseCaseConversionFactorStatus useCaseConversionFactor,
            @NonNull Resources resources,
            @NonNull NumberFormatter numberFormatter,
            @NonNull MeasurementErrorMessageMaker errorMessageMaker) {

        this.useCaseHandler = checkNotNull(
                useCaseHandler, "useCaseHandler cannot be null");
        this.useCasePortionCalculator = checkNotNull(
                useCasePortionCalculator, "useCasePortionCalculator cannot be null");
        this.useCaseConversionFactorStatus = checkNotNull(
                useCaseConversionFactor, "useCaseConversionFactor cannot be null");
        this.resources = checkNotNull(
                resources, "resources cannot be null");
        this.numberFormatter = checkNotNull(
                numberFormatter, "numberFormatter cannot be null");
        this.errorMessageMaker = checkNotNull(
                errorMessageMaker, "error message maker cannot be null");
    }

    void setNavigator(RecipeIngredientEditorNavigator navigator) {
        this.navigator = navigator;
    }

    void onActivityDestroyed() {
        navigator = null;
    }

    public void start(String recipeId, String ingredientId) {
        if (isNewInstantiationOrRecipeIdChanged(recipeId)) {
            this.recipeId = recipeId;
            this.ingredientId = ingredientId;
            createPortionCalculatorRequestValues(measurementModel);
        }
    }

    private boolean isNewInstantiationOrRecipeIdChanged(String recipeId) {
        return this.recipeId.isEmpty() || !this.recipeId.equals(recipeId);
    }

    public void start(String recipeIngredientId) {
        if (isNewInstantiationOrRecipeIngredientIdChanged(recipeIngredientId)) {
            this.recipeIngredientId = recipeIngredientId;
            createPortionCalculatorRequestValues(measurementModel);
        }
    }

    private boolean isNewInstantiationOrRecipeIngredientIdChanged(String recipeIngredientId) {
        return this.recipeIngredientId.isEmpty() ||
                !this.recipeIngredientId.equals(recipeIngredientId);
    }

    @Bindable
    public MeasurementSubtype getSubtype() {
        return measurementModel.getSubtype();
    }

    public void setSubtype(MeasurementSubtype subtype) {
        if (!updatingUi) {
            if (isSubtypeChanged(subtype)) {
                processSubtype(subtype);
            }
        }
    }

    private boolean isSubtypeChanged(MeasurementSubtype subtype) {
        return measurementModel.getSubtype() != subtype;
    }

    private void processSubtype(MeasurementSubtype subtype) {
        MeasurementModel model = MeasurementModelBuilder.
                basedOnModel(measurementModel).
                setSubtype(subtype).
                build();

        createPortionCalculatorRequestValues(model);
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
    public boolean isShowConversionFactorEditableFields() {
        return isShowConversionFactorEditableFields;
    }

    @Bindable
    public boolean isShowConversionFactorUneditableFields() {
        return isShowConversionFactorUneditableFields;
    }

    @Bindable
    public boolean isAdvancedCheckBoxChecked() {
        return isAdvancedCheckBoxChecked;
    }

    public void setAdvancedCheckBoxChecked(boolean advancedCheckBoxChecked) {
        isShowConversionFactorEditableFields = advancedCheckBoxChecked;
        notifyPropertyChanged(BR.showConversionFactorEditableFields);
    }

    @Bindable
    public String getConversionFactor() {
        return numberFormatter.formatDecimalForDisplay(measurementModel.getConversionFactor());
    }

    public void setConversionFactor(String conversionFactor) {
        if (!updatingUi && !conversionFactor.equals(".")) {
            if (!conversionFactor.isEmpty()) {
                parseConversionFactor(conversionFactor);
            }
        }
    }

    private void parseConversionFactor(String conversionFactor) {
        double conversionFactorParsed = parseDecimalFromString(conversionFactor);

        if (conversionFactorParsed == MEASUREMENT_ERROR) {
            displayConversionFactorParseError();
        } else {
            processConversionFactor(conversionFactorParsed);
        }
    }

    private void displayConversionFactorParseError() {
        conversionFactorErrorMessage = numberFormatExceptionErrorMessage();
        notifyPropertyChanged(BR.conversionFactorErrorMessage);
    }

    private void processConversionFactor(double conversionFactor) {
        if (isConversionFactorChanged(conversionFactor) && conversionFactor > NOT_SET) {
            isConversionFactorChangedThisSession = true;
            MeasurementModel model = MeasurementModelBuilder.
                    basedOnModel(measurementModel).
                    setConversionFactor(conversionFactor).
                    build();

            createPortionCalculatorRequestValues(model);
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
        if (!updatingUi && !unitOne.equals(".")) {
            if (unitOne.isEmpty()) {
                processUnitOne(NOT_SET);
            } else {
                parseUnitOne(unitOne);
            }
        }
    }

    private void parseUnitOne(String unitOne) {
        double unitOneParsed = parseDecimalFromString(unitOne);

        if (unitOneParsed == MEASUREMENT_ERROR) {
            displayUnitOneParseError();
        } else {
            processUnitOne(unitOneParsed);
        }
    }

    private void displayUnitOneParseError() {
        unitOneErrorMessage = numberFormatExceptionErrorMessage();
        notifyPropertyChanged(BR.unitOneErrorMessage);
    }

    private void processUnitOne(double unitOne) {
        if (isUnitOneChanged(unitOne)) {
            MeasurementModel model = MeasurementModelBuilder.
                    basedOnModel(measurementModel).
                    setTotalUnitOne(unitOne).
                    build();

            createPortionCalculatorRequestValues(model);
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
            if (unitTwo.isEmpty()) {
                processUnitTwo(NOT_SET);
            } else {
                parseUnitTwo(unitTwo);
            }
        }
    }

    private void parseUnitTwo(String unitTwo) {
        int unitTwoParsed = parseIntegerFromString(unitTwo);

        if (unitTwoParsed == MEASUREMENT_ERROR) {
            displayUnitTwoParseError();
        } else {
            processUnitTwo(unitTwoParsed);
        }
    }

    private void displayUnitTwoParseError() {
        unitTwoErrorMessage = numberFormatExceptionErrorMessage();
        notifyPropertyChanged(BR.unitTwoErrorMessage);
    }

    private void processUnitTwo(int unitTwo) {
        if (isUnitTwoChanged(unitTwo)) {
            MeasurementModel model = MeasurementModelBuilder.
                    basedOnModel(measurementModel).
                    setTotalUnitTwo(unitTwo).
                    build();

            createPortionCalculatorRequestValues(model);
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
        UseCasePortionCalculatorRequest request = new UseCasePortionCalculatorRequest(
                        recipeId,
                        ingredientId,
                        recipeIngredientId,
                        model
                );
        executePortionCalculator(request);
    }

    private void executePortionCalculator(UseCasePortionCalculatorRequest request) {
        useCaseHandler.execute(
                useCasePortionCalculator,
                request,
                new Callback<UseCasePortionCalculatorResponse>() {
                    @Override
                    public void onSuccess(UseCasePortionCalculatorResponse response) {
                        processModelResult(response.getModel());
                        processResultStatus(response.getResultStatus());
                    }

                    @Override
                    public void onError(UseCasePortionCalculatorResponse response) {
                        processModelResult(response.getModel());
                        processResultStatus(response.getResultStatus());
                    }
                });
    }

    private void processModelResult(MeasurementModel resultModel) {
        measurementModel = resultModel;
        updatingUi = true;

        notifyPropertyChanged(BR.subtype);
        notifyPropertyChanged(BR.numberOfUnits);
        notifyPropertyChanged(BR.conversionFactor);
        notifyPropertyChanged(BR.conversionFactorEnabled);
        notifyPropertyChanged(BR.unitOne);
        notifyPropertyChanged(BR.unitTwo);
    }

    private void processResultStatus(UseCasePortionCalculator.ResultStatus resultStatus) {
        hideAllInputErrors();

        if (resultStatus == UseCasePortionCalculator.ResultStatus.INVALID_CONVERSION_FACTOR) {
            conversionFactorErrorMessage = errorMessageMaker.getConversionFactorErrorMessage();
            notifyPropertyChanged(BR.conversionFactorErrorMessage);

        } else if (resultStatus == UseCasePortionCalculator.ResultStatus.INVALID_TOTAL_UNIT_ONE) {
            unitOneErrorMessage = errorMessageMaker.getMeasurementErrorMessage(measurementModel);
            notifyPropertyChanged(BR.unitOneErrorMessage);

        } else if (resultStatus == UseCasePortionCalculator.ResultStatus.INVALID_TOTAL_UNIT_TWO) {
            unitTwoErrorMessage = errorMessageMaker.getMeasurementErrorMessage(measurementModel);
            notifyPropertyChanged(BR.unitTwoErrorMessage);
        }
        executeUseCaseConversionFactorStatus();
    }

    private void hideAllInputErrors() {
        conversionFactorErrorMessage = null;
        notifyPropertyChanged(BR.conversionFactorErrorMessage);
        unitOneErrorMessage = null;
        notifyPropertyChanged(BR.unitOneErrorMessage);
        unitTwoErrorMessage = null;
        notifyPropertyChanged(BR.unitTwoErrorMessage);
    }

    private void executeUseCaseConversionFactorStatus() {
        useCaseHandler.execute(
                useCaseConversionFactorStatus,
                getRequest(),
                getNewResponseCallback());
    }

    private UseCaseConversionFactorStatusRequest getRequest() {
        return new UseCaseConversionFactorStatusRequest(
                measurementModel.getSubtype(),
                useCasePortionCalculator.getIngredientId()
        );
    }

    private Callback<UseCaseConversionFactorStatusResponse>
    getNewResponseCallback() {
        return new Callback<UseCaseConversionFactorStatusResponse>() {

            @Override
            public void onSuccess(UseCaseConversionFactorStatusResponse response) {
                processConversionFactorResult(response.getResult());
            }

            @Override
            public void onError(UseCaseConversionFactorStatusResponse response) {
                processConversionFactorResult(response.getResult());
            }
        };
    }

    private void processConversionFactorResult(UseCaseResult result) {
        if (result == UseCaseResult.DISABLED) {
            hideAllConversionFactorInformation();

        } else if (result == UseCaseResult.ENABLED_UNEDITABLE) {
            showUneditableConversionFactorInformation();

        } else if (result == UseCaseResult.ENABLED_EDITABLE_UNSET) {
            if (!isConversionFactorChangedThisSession) {
                showOptionToAddConversionFactorInformation();
            }

        } else if (result == UseCaseResult.ENABLED_EDITABLE_SET) {
            showAllConversionFactorInformation();
        }

        if (measurementModel.isValidMeasurement()) {
            isValidMeasurement.setValue(true);
        } else {
            isValidMeasurement.setValue(false);
        }

        updatingUi = false;
    }

    private void hideAllConversionFactorInformation() {
        notifyPropertyChanged(BR.conversionFactorEnabled);
        isShowConversionFactorEditableFields = false;
        notifyPropertyChanged(BR.showConversionFactorEditableFields);
        isShowConversionFactorUneditableFields = false;
        notifyPropertyChanged(BR.showConversionFactorUneditableFields);
    }

    private void showUneditableConversionFactorInformation() {
        notifyPropertyChanged(BR.conversionFactorEnabled);
        isShowConversionFactorEditableFields = false;
        notifyPropertyChanged(BR.showConversionFactorEditableFields);
        isShowConversionFactorUneditableFields = true;
        notifyPropertyChanged(BR.showConversionFactorUneditableFields);
    }

    private void showOptionToAddConversionFactorInformation() {
        notifyPropertyChanged(BR.conversionFactorEnabled);
        isShowConversionFactorEditableFields = false;
        notifyPropertyChanged(BR.showConversionFactorEditableFields);
        isShowConversionFactorUneditableFields = false;
        notifyPropertyChanged(BR.showConversionFactorUneditableFields);
        isAdvancedCheckBoxChecked = false;
        notifyPropertyChanged(BR.advancedCheckBoxChecked);
    }

    private void showAllConversionFactorInformation() {
        notifyPropertyChanged(BR.conversionFactorEnabled);
        isShowConversionFactorEditableFields = true;
        notifyPropertyChanged(BR.showConversionFactorEditableFields);
        isShowConversionFactorUneditableFields = false;
        notifyPropertyChanged(BR.showConversionFactorUneditableFields);
        isAdvancedCheckBoxChecked = true;
        notifyPropertyChanged(BR.advancedCheckBoxChecked);
    }

    MutableLiveData<Boolean> getIsValidMeasurement() {
        return isValidMeasurement;
    }

    void donePressed() {
        navigator.donePressed();
    }
}