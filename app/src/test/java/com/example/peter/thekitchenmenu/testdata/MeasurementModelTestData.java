package com.example.peter.thekitchenmenu.testdata;

import com.example.peter.thekitchenmenu.data.model.MeasurementModel;
import com.example.peter.thekitchenmenu.utils.unitofmeasure.MeasurementResult;
import com.example.peter.thekitchenmenu.utils.unitofmeasure.MeasurementSubtype;
import com.example.peter.thekitchenmenu.utils.unitofmeasure.MeasurementType;
import com.example.peter.thekitchenmenu.utils.unitofmeasure.UnitOfMeasure;
import com.example.peter.thekitchenmenu.utils.unitofmeasure.UseCaseIngredientPortionCalculator;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.NumberFormat;

import static com.example.peter.thekitchenmenu.testdata.RecipeIngredientQuantityEntityTestData.getNewValidImperialSpoonMaxConversionFactor;
import static com.example.peter.thekitchenmenu.utils.unitofmeasure.UnitOfMeasureConstants.*;

public class MeasurementModelTestData {

    //-----------
    public static MeasurementModel measurementGetInvalidEmptyFourPortionsSet() {
        UnitOfMeasure unitOfMeasure = getSubtypeFromNewInvalidMetric().getMeasurementClass();
        boolean isPortionsSet = unitOfMeasure.numberOfItemsIsSet(getEmptyModelFourPortions());

        if (!isPortionsSet) {
            throwNumberOfItemsException(isPortionsSet);
        }

        return new MeasurementModel(
                unitOfMeasure.getMeasurementType(),
                unitOfMeasure.getMeasurementSubtype(),
                unitOfMeasure.getNumberOfMeasurementUnits(),
                unitOfMeasure.isConversionFactorEnabled(),
                unitOfMeasure.getConversionFactor(),
                unitOfMeasure.getItemBaseUnits(),
                unitOfMeasure.getTotalBaseUnits(),
                unitOfMeasure.getNumberOfItems(),
                unitOfMeasure.getTotalMeasurementOne(),
                unitOfMeasure.getItemMeasurementOne(),
                unitOfMeasure.getTotalMeasurementTwo(),
                unitOfMeasure.getItemMeasurementTwo(),
                unitOfMeasure.isValidMeasurement(),
                unitOfMeasure.getMinimumMeasurementOne(),
                unitOfMeasure.getMaximumMeasurementOne(),
                unitOfMeasure.getMaximumMeasurementTwo(),
                unitOfMeasure.getMeasurementUnitsDigitWidths()
        );
    }

    private static MeasurementSubtype getSubtypeFromNewInvalidMetric() {
        return MeasurementSubtype.fromInt(RecipeIngredientQuantityEntityTestData.
                getNewInvalid().getUnitOfMeasureSubtype());
    }

    //----------
    public static MeasurementModel measurementGetNewInvalidTotalMeasurementOne() {
        UnitOfMeasure unitOfMeasure = getSubtypeForValidNewMetric().getMeasurementClass();
        boolean isNumberOfItemsSet = unitOfMeasure.numberOfItemsIsSet(getEmptyModelFourPortions());

        if (!isNumberOfItemsSet) {
            throwNumberOfItemsException(isNumberOfItemsSet);
        }

        if (unitOfMeasure.isConversionFactorEnabled()) {
            double conversionFactor = measurementGetInvalidEmptyFourPortionsSet().
                    getConversionFactor();

            boolean isConversionFactorSet = unitOfMeasure.conversionFactorIsSet(conversionFactor);

            if (!isConversionFactorSet)
                throwConversionFactorException(isConversionFactorSet);
        }

        double invalidTotalMeasurementOne;
        MeasurementType type = unitOfMeasure.getMeasurementType();
        switch (type) {
            case MASS:
                invalidTotalMeasurementOne = MAX_MASS + 1;
                break;
            case VOLUME:
                invalidTotalMeasurementOne = MAX_VOLUME + 1;
                break;
            case COUNT:
                throw new RuntimeException("Can't set totalMeasurementOne() for type COUNT");
            default:
                throw new RuntimeException("Unit of measure Type not recognised");
        }

        boolean isTotalMeasurementOneSet = unitOfMeasure.
                totalMeasurementOneIsSet(invalidTotalMeasurementOne);

        if (isTotalMeasurementOneSet)
            throwMeasurementOneException(isTotalMeasurementOneSet);

        return new MeasurementModel(
                unitOfMeasure.getMeasurementType(),
                unitOfMeasure.getMeasurementSubtype(),
                unitOfMeasure.getNumberOfMeasurementUnits(),
                unitOfMeasure.isConversionFactorEnabled(),
                unitOfMeasure.getConversionFactor(),
                unitOfMeasure.getItemBaseUnits(),
                unitOfMeasure.getTotalBaseUnits(),
                unitOfMeasure.getNumberOfItems(),
                invalidTotalMeasurementOne,
                unitOfMeasure.getItemMeasurementOne(),
                unitOfMeasure.getTotalMeasurementTwo(),
                unitOfMeasure.getItemMeasurementTwo(),
                unitOfMeasure.isValidMeasurement(),
                unitOfMeasure.getMinimumMeasurementOne(),
                unitOfMeasure.getItemMeasurementOne(),
                unitOfMeasure.getItemMeasurementTwo(),
                unitOfMeasure.getMeasurementUnitsDigitWidths()
        );
    }

    //-----------
    public static MeasurementModel measurementGetNewValidTotalMeasurementOne() {
        UnitOfMeasure unitOfMeasure = getSubtypeForValidNewMetric().getMeasurementClass();

        boolean isPortionsSet = unitOfMeasure.numberOfItemsIsSet(getEmptyModelFourPortions());
        if (!isPortionsSet) {
            throwNumberOfItemsException(isPortionsSet);
        }

        boolean isConversionFactorSet;
        if (unitOfMeasure.isConversionFactorEnabled()) {
            isConversionFactorSet = unitOfMeasure.conversionFactorIsSet(
                    measurementGetInvalidEmptyFourPortionsSet().getConversionFactor());
            if (!isConversionFactorSet) {
                throwConversionFactorException(isConversionFactorSet);
            }
        }

        UnitOfMeasure forCalculatingUnitOneValue = getSubtypeForValidNewMetric().getMeasurementClass();
        boolean isTotalBaseUnitsSet = forCalculatingUnitOneValue.totalBaseUnitsAreSet(
                getBaseUnitsForNewValidTotalMeasurementOne());
        if (!isTotalBaseUnitsSet) {
            throwTotalBaseUnitsException(isTotalBaseUnitsSet);
        }

        boolean isTotalMeasurementOneIsSet = unitOfMeasure.totalMeasurementOneIsSet(
                forCalculatingUnitOneValue.getTotalMeasurementOne());
        if (!isTotalMeasurementOneIsSet) {
            throwMeasurementOneException(isTotalMeasurementOneIsSet);
        }

        return getMeasurementModel(unitOfMeasure);
    }
    private static double getBaseUnitsForNewValidTotalMeasurementOne() {
        return RecipeIngredientQuantityEntityTestData.getNewValidMetric().getItemBaseUnits() *
                RecipePortionsEntityTestData.getNewValidFourPortions().getServings() *
                RecipePortionsEntityTestData.getNewValidFourPortions().getServings();
    }

    //-----------
    public static MeasurementModel measurementGetNewInvalidTotalMeasurementTwo() {
        UnitOfMeasure unitOfMeasure = getSubtypeForValidNewMetric().getMeasurementClass();
        boolean isNumberOfItemsSet = unitOfMeasure.numberOfItemsIsSet(getEmptyModelFourPortions());

        if (!isNumberOfItemsSet) {
            throwNumberOfItemsException(isNumberOfItemsSet);
        }

        if (unitOfMeasure.isConversionFactorEnabled()) {
            double conversionFactor = measurementGetInvalidEmptyFourPortionsSet().
                    getConversionFactor();

            boolean isConversionFactorSet = unitOfMeasure.conversionFactorIsSet(conversionFactor);

            if (!isConversionFactorSet)
                throwConversionFactorException(isConversionFactorSet);
        }

        UnitOfMeasure forCalculatingMaxValueOfTotalTwo = getSubtypeForValidNewMetric().
                getMeasurementClass();

        MeasurementType type = unitOfMeasure.getMeasurementType();
        switch (type) {
            case MASS:
                forCalculatingMaxValueOfTotalTwo.totalBaseUnitsAreSet(MAX_MASS);
                break;
            case VOLUME:
                forCalculatingMaxValueOfTotalTwo.totalBaseUnitsAreSet(MAX_VOLUME);
                break;
            case COUNT:
                throw new RuntimeException("Can't set totalMeasurementOne() for type COUNT");
            default:
                throw new RuntimeException("Unit of measure Type not recognised");
        }

        int totalMeasurementTwo = forCalculatingMaxValueOfTotalTwo.getTotalMeasurementTwo();
        int invalidTotalMeasurementTwo = totalMeasurementTwo + 1;
        boolean isTotalMeasurementTwoSet = unitOfMeasure.
                totalMeasurementTwoIsSet(invalidTotalMeasurementTwo);

        if (isTotalMeasurementTwoSet)
            throwMeasurementTwoException(isTotalMeasurementTwoSet);

        return new MeasurementModel(
                unitOfMeasure.getMeasurementType(),
                unitOfMeasure.getMeasurementSubtype(),
                unitOfMeasure.getNumberOfMeasurementUnits(),
                unitOfMeasure.isConversionFactorEnabled(),
                unitOfMeasure.getConversionFactor(),
                unitOfMeasure.getItemBaseUnits(),
                unitOfMeasure.getTotalBaseUnits(),
                unitOfMeasure.getNumberOfItems(),
                invalidTotalMeasurementTwo,
                unitOfMeasure.getItemMeasurementOne(),
                unitOfMeasure.getTotalMeasurementTwo(),
                unitOfMeasure.getItemMeasurementTwo(),
                unitOfMeasure.isValidMeasurement(),
                unitOfMeasure.getMinimumMeasurementOne(),
                unitOfMeasure.getItemMeasurementOne(),
                unitOfMeasure.getItemMeasurementTwo(),
                unitOfMeasure.getMeasurementUnitsDigitWidths()
        );
    }

    //-----------
    public static MeasurementModel measurementGetNewValidTotalMeasurementTwo() {
        UnitOfMeasure unitOfMeasure = getSubtypeForValidNewMetric().getMeasurementClass();

        boolean isNumberOfItemsSet = unitOfMeasure.numberOfItemsIsSet(getEmptyModelFourPortions());
        if (!isNumberOfItemsSet) {
            throwNumberOfItemsException(isNumberOfItemsSet);
        }

        if (unitOfMeasure.isConversionFactorEnabled()) {
            double conversionFactor = measurementGetInvalidEmptyFourPortionsSet().
                    getConversionFactor();

            boolean isConversionFactorSet = unitOfMeasure.conversionFactorIsSet(conversionFactor);

            if (!isConversionFactorSet)
                throwConversionFactorException(isConversionFactorSet);
        }

        UnitOfMeasure forCalculatingMaxValueOfTotalTwo = getSubtypeForValidNewMetric().
                getMeasurementClass();

        MeasurementType type = unitOfMeasure.getMeasurementType();
        switch (type) {
            case MASS:
                forCalculatingMaxValueOfTotalTwo.totalBaseUnitsAreSet(MAX_MASS);
                break;
            case VOLUME:
                forCalculatingMaxValueOfTotalTwo.totalBaseUnitsAreSet(MAX_VOLUME);
                break;
            case COUNT:
                throw new RuntimeException("Can't set totalMeasurementOne() for type COUNT");
            default:
                throw new RuntimeException("Unit of measure Type not recognised");
        }

        int totalMeasurementTwo = forCalculatingMaxValueOfTotalTwo.getTotalMeasurementTwo();
        boolean isTotalMeasurementTwoSet = unitOfMeasure.
                totalMeasurementTwoIsSet(totalMeasurementTwo);

        if (!isTotalMeasurementTwoSet)
            throwMeasurementTwoException(isTotalMeasurementTwoSet);

        return getMeasurementModel(unitOfMeasure);
    }

    private static MeasurementSubtype getSubtypeForValidNewMetric() {
        return MeasurementSubtype.fromInt(RecipeIngredientQuantityEntityTestData.
                getNewValidMetric().getUnitOfMeasureSubtype());
    }

    //-----------
    public static MeasurementModel measurementGetNewInvalidUnitOfMeasureChangedImperialSpoon() {
        UnitOfMeasure unitOfMeasure = getSubtypeForNewValidImperialSpoon().getMeasurementClass();

        boolean isNumberOfItemsSet = unitOfMeasure.numberOfItemsIsSet(getEmptyModelFourPortions());
        if (!isNumberOfItemsSet) {
            throwNumberOfItemsException(isNumberOfItemsSet);
        }

        if (unitOfMeasure.isConversionFactorEnabled()) {
            double conversionFactor = measurementGetInvalidEmptyFourPortionsSet().
                    getConversionFactor();

            boolean isConversionFactorSet = unitOfMeasure.conversionFactorIsSet(conversionFactor);

            if (!isConversionFactorSet)
                throwConversionFactorException(isConversionFactorSet);
        }

        return getMeasurementModel(unitOfMeasure);
    }

    //-----------
    public static MeasurementModel measurementGetNewValidHalfImperialSpoonUnitOneUpdated() {
        UnitOfMeasure unitOfMeasure = getSubtypeForNewValidImperialSpoon().getMeasurementClass();
        UnitOfMeasure forCalculatingUnitOneValue = getSubtypeForNewValidImperialSpoon().
                getMeasurementClass();

        boolean isNumberOfItemsSet = unitOfMeasure.numberOfItemsIsSet(getEmptyModelFourPortions());
        forCalculatingUnitOneValue.numberOfItemsIsSet(getEmptyModelFourPortions());
        if (!isNumberOfItemsSet) {
            throwNumberOfItemsException(isNumberOfItemsSet);
        }

        if (unitOfMeasure.isConversionFactorEnabled()) {
            double conversionFactor = measurementGetInvalidEmptyFourPortionsSet().
                    getConversionFactor();

            boolean isConversionFactorSet = unitOfMeasure.conversionFactorIsSet(conversionFactor);
            forCalculatingUnitOneValue.conversionFactorIsSet(conversionFactor);

            if (!isConversionFactorSet)
                throwConversionFactorException(isConversionFactorSet);
        }

        forCalculatingUnitOneValue.itemBaseUnitsAreSet(RecipeIngredientQuantityEntityTestData.
                        getNewValidImperialOneTeaspoonNoConversionFactor().
                        getItemBaseUnits());

        boolean isTotalMeasurementOneSet = unitOfMeasure.totalMeasurementOneIsSet(
                forCalculatingUnitOneValue.getTotalMeasurementOne()
        );

        if (!isTotalMeasurementOneSet)
            throwMeasurementOneException(isTotalMeasurementOneSet);

        return getMeasurementModel(unitOfMeasure);
    }

    //-----------
    public static MeasurementModel measurementGetNewInvalidConversionFactor() {
        UnitOfMeasure unitOfMeasure = getSubtypeForNewValidImperialSpoon().getMeasurementClass();

        boolean isNumberOfItemsSet = unitOfMeasure.numberOfItemsIsSet(getEmptyModelFourPortions());
        if (!isNumberOfItemsSet) {
            throwNumberOfItemsException(isNumberOfItemsSet);
        }


        double conversionFactor = MAX_CONVERSION_FACTOR + .1;
        if (unitOfMeasure.isConversionFactorEnabled()) {
            boolean isConversionFactorSet = unitOfMeasure.conversionFactorIsSet(conversionFactor);
            if (isConversionFactorSet)
                throwConversionFactorException(isConversionFactorSet);
        }

        return new MeasurementModel(
                unitOfMeasure.getMeasurementType(),
                unitOfMeasure.getMeasurementSubtype(),
                unitOfMeasure.getNumberOfMeasurementUnits(),
                unitOfMeasure.isConversionFactorEnabled(),
                conversionFactor,
                unitOfMeasure.getItemBaseUnits(),
                unitOfMeasure.getTotalBaseUnits(),
                unitOfMeasure.getNumberOfItems(),
                unitOfMeasure.getTotalMeasurementOne(),
                unitOfMeasure.getItemMeasurementOne(),
                unitOfMeasure.getTotalMeasurementTwo(),
                unitOfMeasure.getItemMeasurementTwo(),
                unitOfMeasure.isValidMeasurement(),
                unitOfMeasure.getMinimumMeasurementOne(),
                unitOfMeasure.getMaximumMeasurementOne(),
                unitOfMeasure.getMaximumMeasurementTwo(),
                unitOfMeasure.getMeasurementUnitsDigitWidths()
        );
    }

    //-----------
    public static MeasurementModel measurementGetNewValidImperialSpoonWithConversionFactor() {
        UnitOfMeasure unitOfMeasure = getSubtypeForNewValidImperialSpoon().getMeasurementClass();

        boolean isNumberOfItemsSet = unitOfMeasure.numberOfItemsIsSet(getEmptyModelFourPortions());
        if (!isNumberOfItemsSet) {
            throwNumberOfItemsException(isNumberOfItemsSet);
        }

        if (unitOfMeasure.isConversionFactorEnabled()) {
            boolean isConversionFactorSet = unitOfMeasure.conversionFactorIsSet(
                    MAX_CONVERSION_FACTOR);

            if (!isConversionFactorSet)
                throwConversionFactorException(isConversionFactorSet);
        }

        boolean itemBaseUnitsAreSet = unitOfMeasure.itemBaseUnitsAreSet(
                getNewValidImperialSpoonMaxConversionFactor().getItemBaseUnits()
        );
        if (!itemBaseUnitsAreSet) {
            throwItemBaseUnitsAreSetException(itemBaseUnitsAreSet);
        }

        return getMeasurementModel(unitOfMeasure);
    }

    private static MeasurementSubtype getSubtypeForNewValidImperialSpoon() {
        return MeasurementSubtype.fromInt(RecipeIngredientQuantityEntityTestData.
                getNewValidImperialOneTeaspoonNoConversionFactor().getUnitOfMeasureSubtype());
    }

    private static int getEmptyModelFourPortions() {
        return RecipePortionsEntityTestData.getNewValidFourPortions().getServings()
                * RecipePortionsEntityTestData.getNewValidFourPortions().getSittings();
    }

    //-----------
    public static MeasurementModel measurementGetExistingValidMetric() {
        UnitOfMeasure unitOfMeasure = getExistingValidSubtype().getMeasurementClass();

        boolean isNumberOfItemsSet = unitOfMeasure.numberOfItemsIsSet(getExistingValidNinePortions());
        if (!isNumberOfItemsSet) {
            throwNumberOfItemsException(isNumberOfItemsSet);
        }

        if (unitOfMeasure.isConversionFactorEnabled()) {
            boolean isConversionFactorSet = unitOfMeasure.conversionFactorIsSet(
                    getExistingValidConversionFactor());

            if (!isConversionFactorSet)
                throwConversionFactorException(isConversionFactorSet);
        }

        boolean itemBaseUnitsAreSet = unitOfMeasure.itemBaseUnitsAreSet(
                RecipeIngredientQuantityEntityTestData.getExistingValidMetric().getItemBaseUnits()
        );
        if (!itemBaseUnitsAreSet) {
            throwItemBaseUnitsAreSetException(itemBaseUnitsAreSet);
        }


        return getMeasurementModel(unitOfMeasure);
    }

    public static MeasurementResult getResultExistingValidMetric() {
        return new MeasurementResult(
                measurementGetExistingValidMetric(),
                UseCaseIngredientPortionCalculator.ResultStatus.RESULT_OK
        );
    }

    private static MeasurementSubtype getExistingValidSubtype() {
        return MeasurementSubtype.fromInt(RecipeIngredientQuantityEntityTestData.
                getExistingValidMetric().
                getUnitOfMeasureSubtype());
    }

    private static int getExistingValidNinePortions() {
        return RecipePortionsEntityTestData.getExistingValid().getServings() *
                RecipePortionsEntityTestData.getExistingValid().getSittings();
    }

    private static double getExistingValidConversionFactor() {
        return IngredientEntityTestData.getExistingValidNameValidDescription().getConversionFactor();
    }


    public static MeasurementResult getExistingInvalidTotalTwoResult() {
        return new MeasurementResult(
                getExistingMetricInvalidTotalTwoModelResult(),
                UseCaseIngredientPortionCalculator.ResultStatus.INVALID_TOTAL_MEASUREMENT_TWO
        );
    }

    private static MeasurementModel getMeasurementModel(UnitOfMeasure unitOfMeasure) {
        return new MeasurementModel(
                unitOfMeasure.getMeasurementType(),
                unitOfMeasure.getMeasurementSubtype(),
                unitOfMeasure.getNumberOfMeasurementUnits(),
                unitOfMeasure.isConversionFactorEnabled(),
                unitOfMeasure.getConversionFactor(),
                unitOfMeasure.getItemBaseUnits(),
                unitOfMeasure.getTotalBaseUnits(),
                unitOfMeasure.getNumberOfItems(),
                unitOfMeasure.getTotalMeasurementOne(),
                unitOfMeasure.getItemMeasurementOne(),
                unitOfMeasure.getTotalMeasurementTwo(),
                unitOfMeasure.getItemMeasurementTwo(),
                unitOfMeasure.isValidMeasurement(),
                unitOfMeasure.getMinimumMeasurementOne(),
                unitOfMeasure.getMaximumMeasurementOne(),
                unitOfMeasure.getMaximumMeasurementTwo(),
                unitOfMeasure.getMeasurementUnitsDigitWidths()
        );
    }

    private static void throwNumberOfItemsException(boolean isSet) {
        if (isSet)
            throw new RuntimeException("numberOfItems() is true and should be false, there, may " +
                    "be a problem with the test data.");
        else
            throw new RuntimeException("numberOfItems() is false and should be true, there may " +
                    "be a problem with the test data.");
    }

    private static void throwMeasurementOneException(boolean isSet) {
        if (isSet)
            throw new RuntimeException("totalMeasurementOne is true and it should be false. " +
                    "There may be a problem with the test data");
        else
            throw new RuntimeException("totalMeasurementOne is false and it should be true. " +
                    "There may be a problem with the test data.");
    }

    private static void throwMeasurementTwoException(boolean isSet) {
        if (isSet)
            throw new RuntimeException("totalMeasurementTwo() is true and it should be false. " +
                    "There may be a problem with the test data");
        else
            throw new RuntimeException("totalMeasurementTwo() is false and it should be true. " +
                    "There may be a problem with the test data.");
    }

    private static void throwConversionFactorException(boolean isSet) {
        if (isSet)
            throw new RuntimeException("isConversionFactorSet is true and should be false. " +
                    "There may be a problem with the test data.");
        else
            throw new RuntimeException("isConversionFactorSet is false and should be true. " +
                    "There may be a problem with the test data.");
    }

    private static void throwTotalBaseUnitsException(boolean isSet) {
        if (isSet)
            throw new RuntimeException("totalBaseUnitsAreSet() is true and should be false. " +
                    "There may be a problem with the test data.");
        else
            throw new RuntimeException("totalBaseUnitsAreSet() is false and should be true. " +
                    "There may be a problem with the test data.");
    }

    private static void throwItemBaseUnitsAreSetException(boolean isSet) {
        if (isSet)
            throw new RuntimeException("itemBaseUnitsAreSet() is true and should be false. " +
                    "There may be a problem with the test data.");
        else
            throw new RuntimeException("itemBaseUnitsAreSet() is false and should be true. " +
                    "There may be a problem with the test data.");
    }

    public static MeasurementModel getExistingMetricInvalidTotalOne() {
        return new MeasurementModel(
                measurementGetExistingValidMetric().getSubtype(),
                measurementGetExistingValidMetric().getNumberOfItems(),
                measurementGetExistingValidMetric().getConversionFactor(),
                MAX_VOLUME + 1,
                measurementGetExistingValidMetric().getTotalMeasurementTwo(),
                measurementGetExistingValidMetric().getItemMeasurementOne(),
                measurementGetExistingValidMetric().getItemMeasurementTwo(),
                measurementGetExistingValidMetric().getItemBaseUnits()
        );
    }

    public static MeasurementResult getExistingMetricInvalidTotalOneResult() {
        return new MeasurementResult(
                getExistingMetricInvalidTotalOneResultModel(),
                UseCaseIngredientPortionCalculator.ResultStatus.INVALID_TOTAL_MEASUREMENT_ONE
        );
    }

    private static MeasurementModel getExistingMetricInvalidTotalOneResultModel() {
        return new MeasurementModel(
                getExistingValidSubtype(),
                getExistingValidNinePortions(),
                getExistingValidConversionFactor(),
                getExistingMetricInvalidTotalOne().getTotalMeasurementOne(),
                getExistingValidTotalMeasurementTwo(),
                ((int) getExistingMetricValidItemBaseUnitsMinusTotalMeasurementOne() % 1000),
                getExistingValidItemMeasurementTwo(),
                getExistingMetricValidItemBaseUnitsMinusTotalMeasurementOne()
        );
    }

    public static MeasurementModel getExistingMetricInvalidTotalTwo() {
        return new MeasurementModel(
                getExistingValidSubtype(),
                getExistingValidNinePortions(),
                getExistingValidConversionFactor(),
                getExistingValidTotalMeasurementOne(),
                (int) MAX_VOLUME / 1000 + 1,
                getExistingValidItemMeasurementOne(),
                getExistingValidItemMeasurementTwo(),
                RecipeIngredientQuantityEntityTestData.getExistingValidMetric().getItemBaseUnits()
        );
    }

    private static MeasurementModel getExistingMetricInvalidTotalTwoModelResult() {
        return new MeasurementModel(
                getExistingValidSubtype(),
                getExistingValidNinePortions(),
                getExistingValidConversionFactor(),
                getExistingValidTotalMeasurementOne(),
                getExistingMetricInvalidTotalTwo().getTotalMeasurementTwo(),
                (int) (getExistingMetricValidItemBaseUnits() % 1000),
                0,
                getExistingMetricValidItemBaseUnits()
        );
    }

    public static MeasurementModel getExistingMetricValidTwoUpdated() {
        return new MeasurementModel(
                measurementGetExistingValidMetric().getSubtype(),
                measurementGetExistingValidMetric().getNumberOfItems(),
                measurementGetExistingValidMetric().getConversionFactor(),
                measurementGetExistingValidMetric().getTotalMeasurementOne(),
                getExistingMetricValidUpdatedTwoValue(),
                measurementGetExistingValidMetric().getItemMeasurementOne(),
                measurementGetExistingValidMetric().getItemMeasurementTwo(),
                measurementGetExistingValidMetric().getItemBaseUnits()
        );
    }

    public static MeasurementResult getExistingMetricValidTwoUpdatedResult() {
        return new MeasurementResult(
                getExistingMetricValidTwoUpdatedResultModel(),
                UseCaseIngredientPortionCalculator.ResultStatus.RESULT_OK
        );
    }

    private static MeasurementModel getExistingMetricValidTwoUpdatedResultModel() {
        return new MeasurementModel(
                measurementGetExistingValidMetric().getSubtype(),
                measurementGetExistingValidMetric().getNumberOfItems(),
                measurementGetExistingValidMetric().getConversionFactor(),
                measurementGetExistingValidMetric().getTotalMeasurementOne(),
                getExistingMetricValidUpdatedTwoValue(),
                (int) getExistingMetricValidTwoUpdatedItemBaseUnits(),
                measurementGetExistingValidMetric().getItemMeasurementTwo(),
                getExistingMetricValidTwoUpdatedItemBaseUnits()
        );
    }

    private static double getExistingMetricValidTwoUpdatedItemBaseUnits() {
        return (((double) getExistingMetricValidUpdatedTwoValue() * 1000) +
                measurementGetExistingValidMetric().getTotalMeasurementOne()) /
                (double) measurementGetExistingValidMetric().getNumberOfItems();
    }

    private static int getExistingMetricValidUpdatedTwoValue() {
        return 4;
    }

    public static MeasurementModel getExistingMetricUnitOfMeasureUpdatedToImperial() {
        return new MeasurementModel(
                MeasurementSubtype.IMPERIAL_MASS,
                measurementGetExistingValidMetric().getNumberOfItems(),
                measurementGetExistingValidMetric().getConversionFactor(),
                measurementGetExistingValidMetric().getTotalMeasurementOne(),
                measurementGetExistingValidMetric().getTotalMeasurementTwo(),
                measurementGetExistingValidMetric().getItemMeasurementOne(),
                measurementGetExistingValidMetric().getItemMeasurementTwo(),
                measurementGetExistingValidMetric().getItemBaseUnits()
        );
    }

    public static MeasurementResult getExistingMetricUnitOfMeasureUpdatedToImperialResult() {
        return new MeasurementResult(
                getExistingMetricUnitOfMeasureUpdatedToImperialResultModel(),
                UseCaseIngredientPortionCalculator.ResultStatus.INVALID_MEASUREMENT
        );
    }

    private static MeasurementModel getExistingMetricUnitOfMeasureUpdatedToImperialResultModel() {
        return new MeasurementModel(
                MeasurementSubtype.IMPERIAL_MASS,
                getExistingMetricUnitOfMeasureUpdatedToImperial().getNumberOfItems(),
                getExistingMetricUnitOfMeasureUpdatedToImperial().getConversionFactor(),
                measurementGetInvalidEmptyFourPortionsSet().getTotalMeasurementOne(),
                measurementGetInvalidEmptyFourPortionsSet().getTotalMeasurementTwo(),
                measurementGetInvalidEmptyFourPortionsSet().getItemMeasurementOne(),
                measurementGetInvalidEmptyFourPortionsSet().getItemMeasurementTwo(),
                measurementGetInvalidEmptyFourPortionsSet().getItemBaseUnits()
        );
    }

    public static MeasurementModel getExistingMetricInvalidConversionFactor() {
        return new MeasurementModel(
                measurementGetExistingValidMetric().getSubtype(),
                measurementGetExistingValidMetric().getNumberOfItems(),
                MAX_CONVERSION_FACTOR + .01,
                measurementGetExistingValidMetric().getTotalMeasurementOne(),
                measurementGetExistingValidMetric().getTotalMeasurementTwo(),
                measurementGetExistingValidMetric().getItemMeasurementOne(),
                measurementGetExistingValidMetric().getItemMeasurementTwo(),
                measurementGetExistingValidMetric().getItemBaseUnits()
        );
    }

    public static MeasurementResult getExistingMetricInvalidConversionFactorResult() {
        return new MeasurementResult(
                getExistingMetricInvalidConversionFactor(),
                UseCaseIngredientPortionCalculator.ResultStatus.INVALID_CONVERSION_FACTOR
        );
    }

    private static double roundDecimalToTenths(double valueToRound) {
        NumberFormat decimalFormat = NumberFormat.getInstance();
        decimalFormat.setRoundingMode(RoundingMode.HALF_EVEN);
        ((DecimalFormat) decimalFormat).applyPattern("##.#");
        return Double.parseDouble(decimalFormat.format(valueToRound));
    }
}
