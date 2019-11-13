package com.example.peter.thekitchenmenu.testdata;

import com.example.peter.thekitchenmenu.data.model.MeasurementModel;
import com.example.peter.thekitchenmenu.utils.unitofmeasure.MeasurementSubtype;
import com.example.peter.thekitchenmenu.utils.unitofmeasure.MeasurementType;
import com.example.peter.thekitchenmenu.utils.unitofmeasure.UnitOfMeasure;

import static com.example.peter.thekitchenmenu.testdata.RecipeIngredientQuantityEntityTestData.getNewValidImperialSpoonMaxConversionFactor;
import static com.example.peter.thekitchenmenu.utils.unitofmeasure.UnitOfMeasureConstants.*;

public class MeasurementModelTestData {

    //-----------
    public static MeasurementModel getInvalidEmptyFourPortionsSet() {
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
    public static MeasurementModel getNewInvalidTotalMeasurementOne() {
        UnitOfMeasure unitOfMeasure = getSubtypeForValidNewMetric().getMeasurementClass();
        boolean isNumberOfItemsSet = unitOfMeasure.numberOfItemsIsSet(getEmptyModelFourPortions());

        if (!isNumberOfItemsSet) {
            throwNumberOfItemsException(isNumberOfItemsSet);
        }

        if (unitOfMeasure.isConversionFactorEnabled()) {
            double conversionFactor = getInvalidEmptyFourPortionsSet().
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
                unitOfMeasure.getMaximumMeasurementOne(),
                unitOfMeasure.getMaximumMeasurementTwo(),
                unitOfMeasure.getMeasurementUnitsDigitWidths()
        );
    }

    //-----------
    public static MeasurementModel getNewValidTotalMeasurementOne() {
        UnitOfMeasure unitOfMeasure = getSubtypeForValidNewMetric().getMeasurementClass();

        boolean isPortionsSet = unitOfMeasure.numberOfItemsIsSet(getEmptyModelFourPortions());
        if (!isPortionsSet) {
            throwNumberOfItemsException(isPortionsSet);
        }

        boolean isConversionFactorSet;
        if (unitOfMeasure.isConversionFactorEnabled()) {
            isConversionFactorSet = unitOfMeasure.conversionFactorIsSet(
                    getInvalidEmptyFourPortionsSet().getConversionFactor());
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
    public static MeasurementModel getNewInvalidTotalMeasurementTwo() {
        UnitOfMeasure unitOfMeasure = getSubtypeForValidNewMetric().getMeasurementClass();
        boolean isNumberOfItemsSet = unitOfMeasure.numberOfItemsIsSet(getEmptyModelFourPortions());

        if (!isNumberOfItemsSet) {
            throwNumberOfItemsException(isNumberOfItemsSet);
        }

        if (unitOfMeasure.isConversionFactorEnabled()) {
            double conversionFactor = getInvalidEmptyFourPortionsSet().
                    getConversionFactor();

            boolean isConversionFactorSet = unitOfMeasure.conversionFactorIsSet(conversionFactor);

            if (!isConversionFactorSet) {
                throwConversionFactorException(isConversionFactorSet);
            }
        }

        int invalidMeasurementTwo = unitOfMeasure.getMaximumMeasurementTwo() + 1;
        boolean isTotalMeasurementTwoSet = unitOfMeasure.totalMeasurementTwoIsSet(
                invalidMeasurementTwo);

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
                unitOfMeasure.getTotalMeasurementOne(),
                unitOfMeasure.getItemMeasurementOne(),
                invalidMeasurementTwo,
                unitOfMeasure.getItemMeasurementTwo(),
                unitOfMeasure.isValidMeasurement(),
                unitOfMeasure.getMinimumMeasurementOne(),
                unitOfMeasure.getMaximumMeasurementOne(),
                unitOfMeasure.getMaximumMeasurementTwo(),
                unitOfMeasure.getMeasurementUnitsDigitWidths()
        );
    }

    //-----------
    public static MeasurementModel getNewValidTotalMeasurementTwo() {
        UnitOfMeasure unitOfMeasure = getSubtypeForValidNewMetric().getMeasurementClass();

        boolean isNumberOfItemsSet = unitOfMeasure.numberOfItemsIsSet(getEmptyModelFourPortions());
        if (!isNumberOfItemsSet) {
            throwNumberOfItemsException(isNumberOfItemsSet);
        }

        if (unitOfMeasure.isConversionFactorEnabled()) {
            double conversionFactor = getInvalidEmptyFourPortionsSet().
                    getConversionFactor();

            boolean isConversionFactorSet = unitOfMeasure.conversionFactorIsSet(conversionFactor);

            if (!isConversionFactorSet)
                throwConversionFactorException(isConversionFactorSet);
        }

        int totalMeasurementTwo = unitOfMeasure.getMaximumMeasurementTwo();
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
    public static MeasurementModel getNewInvalidUnitOfMeasureChangedImperialSpoon() {
        UnitOfMeasure unitOfMeasure = getSubtypeForNewValidImperialSpoon().getMeasurementClass();

        boolean isNumberOfItemsSet = unitOfMeasure.numberOfItemsIsSet(getEmptyModelFourPortions());
        if (!isNumberOfItemsSet) {
            throwNumberOfItemsException(isNumberOfItemsSet);
        }

        if (unitOfMeasure.isConversionFactorEnabled()) {
            double conversionFactor = getInvalidEmptyFourPortionsSet().
                    getConversionFactor();

            boolean isConversionFactorSet = unitOfMeasure.conversionFactorIsSet(conversionFactor);

            if (!isConversionFactorSet)
                throwConversionFactorException(isConversionFactorSet);
        }

        return getMeasurementModel(unitOfMeasure);
    }

    //-----------
    public static MeasurementModel getNewValidHalfImperialSpoonUnitOneUpdated() {
        UnitOfMeasure unitOfMeasure = getSubtypeForNewValidImperialSpoon().getMeasurementClass();
        UnitOfMeasure forCalculatingUnitOneValue = getSubtypeForNewValidImperialSpoon().
                getMeasurementClass();

        boolean isNumberOfItemsSet = unitOfMeasure.numberOfItemsIsSet(getEmptyModelFourPortions());
        forCalculatingUnitOneValue.numberOfItemsIsSet(getEmptyModelFourPortions());
        if (!isNumberOfItemsSet) {
            throwNumberOfItemsException(isNumberOfItemsSet);
        }

        if (unitOfMeasure.isConversionFactorEnabled()) {
            double conversionFactor = getInvalidEmptyFourPortionsSet().
                    getConversionFactor();

            boolean isConversionFactorSet = unitOfMeasure.conversionFactorIsSet(conversionFactor);
            forCalculatingUnitOneValue.conversionFactorIsSet(conversionFactor);

            if (!isConversionFactorSet)
                throwConversionFactorException(isConversionFactorSet);
        }

        forCalculatingUnitOneValue.itemBaseUnitsAreSet(RecipeIngredientQuantityEntityTestData.
                getNewValidImperialOneTeaspoonFourPortionsNoConversionFactor().
                getItemBaseUnits());

        boolean isTotalMeasurementOneSet = unitOfMeasure.totalMeasurementOneIsSet(
                forCalculatingUnitOneValue.getTotalMeasurementOne()
        );

        if (!isTotalMeasurementOneSet)
            throwMeasurementOneException(isTotalMeasurementOneSet);

        return getMeasurementModel(unitOfMeasure);
    }

    //-----------
    public static MeasurementModel getNewInvalidConversionFactor() {
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
    public static MeasurementModel getNewValidImperialSpoonWithConversionFactor() {
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
                getNewValidImperialOneTeaspoonFourPortionsNoConversionFactor().getUnitOfMeasureSubtype());
    }

    private static int getEmptyModelFourPortions() {
        return RecipePortionsEntityTestData.getNewValidFourPortions().getServings()
                * RecipePortionsEntityTestData.getNewValidFourPortions().getSittings();
    }

    //-----------
    public static MeasurementModel getExistingValidMetric() {
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

    public static MeasurementModel getExistingMetricInvalidTotalOne() {
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

        boolean isItemBaseUnitsSet = unitOfMeasure.itemBaseUnitsAreSet(
                RecipeIngredientQuantityEntityTestData.getExistingValidMetric().getItemBaseUnits()
        );

        if (!isItemBaseUnitsSet)
            throwItemBaseUnitsAreSetException(isItemBaseUnitsSet);

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
                unitOfMeasure.getMaximumMeasurementOne(),
                unitOfMeasure.getMaximumMeasurementTwo(),
                unitOfMeasure.getMeasurementUnitsDigitWidths()
        );
    }

    //-----------
    public static MeasurementModel getExistingMetricInvalidTotalTwo() {
        UnitOfMeasure unitOfMeasure = getExistingValidSubtype().getMeasurementClass();

        if (unitOfMeasure.isConversionFactorEnabled()) {
            double conversionFactor = getExistingValidConversionFactor();

            boolean isConversionFactorSet = unitOfMeasure.conversionFactorIsSet(conversionFactor);

            if (!isConversionFactorSet)
                throwConversionFactorException(isConversionFactorSet);
        }

        boolean isNumberOfItemsSet = unitOfMeasure.numberOfItemsIsSet(getExistingValidNinePortions());
        if (!isNumberOfItemsSet) {
            throwNumberOfItemsException(isNumberOfItemsSet);
        }

        boolean isItemBaseUnitsSet = unitOfMeasure.itemBaseUnitsAreSet(
                RecipeIngredientQuantityEntityTestData.getExistingValidMetric().getItemBaseUnits()
        );

        if (!isItemBaseUnitsSet)
            throwItemBaseUnitsAreSetException(isItemBaseUnitsSet);

        int maxTotalMeasurementTwo = unitOfMeasure.getMaximumMeasurementTwo();
        int invalidTotalMeasurementTwo = maxTotalMeasurementTwo + 1;
        boolean isTotalMeasurementTwoSet = unitOfMeasure.
                totalMeasurementTwoIsSet(invalidTotalMeasurementTwo);

        if (isTotalMeasurementTwoSet) {
            throwMeasurementTwoException(isTotalMeasurementTwoSet);
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
                invalidTotalMeasurementTwo,
                unitOfMeasure.getItemMeasurementTwo(),
                unitOfMeasure.isValidMeasurement(),
                unitOfMeasure.getMinimumMeasurementOne(),
                unitOfMeasure.getMaximumMeasurementOne(),
                unitOfMeasure.getMaximumMeasurementTwo(),
                unitOfMeasure.getMeasurementUnitsDigitWidths()
        );
    }

    //-----------
    public static MeasurementModel getExistingMetricValidTwoUpdated() {
        UnitOfMeasure unitOfMeasure = getExistingValidSubtype().getMeasurementClass();

        boolean isNumberOfItemsSet = unitOfMeasure.numberOfItemsIsSet(getExistingValidNinePortions());
        if (!isNumberOfItemsSet) {
            throwNumberOfItemsException(isNumberOfItemsSet);
        }

        if (unitOfMeasure.isConversionFactorEnabled()) {
            double conversionFactor = getExistingValidConversionFactor();

            boolean isConversionFactorSet = unitOfMeasure.conversionFactorIsSet(conversionFactor);

            if (!isConversionFactorSet)
                throwConversionFactorException(isConversionFactorSet);
        }

        double itemBaseUnits = RecipeIngredientQuantityEntityTestData.getExistingValidMetric().
                getItemBaseUnits();
        boolean isItemBaseUnitsSet = unitOfMeasure.itemBaseUnitsAreSet(itemBaseUnits);

        if (!isItemBaseUnitsSet) {
            throwItemBaseUnitsAreSetException(isItemBaseUnitsSet);
        }

        int maxTotalMeasurementTwo = 4;
        boolean isTotalMeasurementTwoSet = unitOfMeasure.
                totalMeasurementTwoIsSet(maxTotalMeasurementTwo);

        if (!isTotalMeasurementTwoSet) {
            throwMeasurementTwoException(isTotalMeasurementTwoSet);
        }

        return getMeasurementModel(unitOfMeasure);
    }

    //-----------
    public static MeasurementModel getExistingMetricUnitOfMeasureUpdatedToImperial() {
        UnitOfMeasure unitOfMeasure = MeasurementSubtype.IMPERIAL_MASS.getMeasurementClass();

        boolean isNumberOfItemsSet = unitOfMeasure.numberOfItemsIsSet(
                getExistingValidMetric().getNumberOfItems());
        if (!isNumberOfItemsSet) {
            throwNumberOfItemsException(isNumberOfItemsSet);
        }

        if (unitOfMeasure.isConversionFactorEnabled()) {
            double conversionFactor = getExistingValidMetric().getConversionFactor();

            boolean isConversionFactorSet = unitOfMeasure.conversionFactorIsSet(conversionFactor);

            if (!isConversionFactorSet)
                throwConversionFactorException(isConversionFactorSet);
        }

        return getMeasurementModel(unitOfMeasure);
    }

    private static MeasurementSubtype getExistingValidSubtype() {
        return MeasurementSubtype.fromInt(RecipeIngredientQuantityEntityTestData.
                getExistingValidMetric().getUnitOfMeasureSubtype());
    }

    private static int getExistingValidNinePortions() {
        return RecipePortionsEntityTestData.getExistingValidNinePortions().getServings() *
                RecipePortionsEntityTestData.getExistingValidNinePortions().getSittings();
    }

    private static double getExistingValidConversionFactor() {
        return IngredientEntityTestData.getExistingValidNameValidDescriptionNoConversionFactor().getConversionFactor();
    }

    //-----------
    public static MeasurementModel getExistingImperialSpoonInvalidConversionFactor() {
        UnitOfMeasure unitOfMeasure = getSubtypeForNewValidImperialSpoon().getMeasurementClass();

        boolean isNumberOfItemsSet = unitOfMeasure.numberOfItemsIsSet(getExistingValidNinePortions());
        if (!isNumberOfItemsSet) {
            throwNumberOfItemsException(isNumberOfItemsSet);
        }

        double itemBaseUnits = RecipeIngredientQuantityEntityTestData.
                getExistingValidImperialTwoSpoons().getItemBaseUnits();
        unitOfMeasure.itemBaseUnitsAreSet(itemBaseUnits);

        double conversionFactor = MAX_CONVERSION_FACTOR + .01;
        if (unitOfMeasure.isConversionFactorEnabled()) {
            boolean isConversionFactorSet = unitOfMeasure.conversionFactorIsSet(conversionFactor);
            if (isConversionFactorSet) {
                throwConversionFactorException(isConversionFactorSet);
            }
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
}
