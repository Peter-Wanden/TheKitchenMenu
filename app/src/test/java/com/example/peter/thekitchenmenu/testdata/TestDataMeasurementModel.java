package com.example.peter.thekitchenmenu.testdata;

import com.example.peter.thekitchenmenu.domain.model.MeasurementModel;
import com.example.peter.thekitchenmenu.domain.unitofmeasureentities.MeasurementSubtype;
import com.example.peter.thekitchenmenu.domain.unitofmeasureentities.MeasurementType;
import com.example.peter.thekitchenmenu.domain.unitofmeasureentities.UnitOfMeasure;

import static com.example.peter.thekitchenmenu.testdata.TestDataRecipeIngredientQuantityEntity.getNewValidImperialSpoonMaxConversionFactor;
import static com.example.peter.thekitchenmenu.domain.unitofmeasureentities.UnitOfMeasureConstants.*;

public class TestDataMeasurementModel {

    //-----------
    public static MeasurementModel getInvalidEmptyFourPortionsSet() {
        UnitOfMeasure unitOfMeasure = getSubtypeFromNewInvalidMetric().getMeasurementClass();
        boolean isPortionsSet = unitOfMeasure.isNumberOfItemsSet(getEmptyModelFourPortions());

        if (!isPortionsSet) {
            throwNumberOfItemsException(isPortionsSet);
        }

        return new MeasurementModel(
                unitOfMeasure.getMeasurementType(),
                unitOfMeasure.getMeasurementSubtype(),
                unitOfMeasure.getNumberOfUnits(),
                unitOfMeasure.isConversionFactorEnabled(),
                unitOfMeasure.getConversionFactor(),
                unitOfMeasure.getItemBaseUnits(),
                unitOfMeasure.getTotalBaseUnits(),
                unitOfMeasure.getNumberOfItems(),
                unitOfMeasure.getTotalUnitOne(),
                unitOfMeasure.getItemUnitOne(),
                unitOfMeasure.getTotalUnitTwo(),
                unitOfMeasure.getItemUnitTwo(),
                unitOfMeasure.isValidMeasurement(),
                unitOfMeasure.getMinUnitOneInBaseUnits(),
                unitOfMeasure.getMaxUnitOne(),
                unitOfMeasure.getMaxUnitTwo(),
                unitOfMeasure.getMaxUnitDigitWidths()
        );
    }

    private static MeasurementSubtype getSubtypeFromNewInvalidMetric() {
        return MeasurementSubtype.fromInt(TestDataRecipeIngredientQuantityEntity.
                getNewInvalid().getUnitOfMeasureSubtype());
    }

    //----------
    public static MeasurementModel getNewInvalidTotalMeasurementOne() {
        UnitOfMeasure unitOfMeasure = getSubtypeForValidNewMetric().getMeasurementClass();
        boolean isNumberOfItemsSet = unitOfMeasure.isNumberOfItemsSet(getEmptyModelFourPortions());

        if (!isNumberOfItemsSet) {
            throwNumberOfItemsException(isNumberOfItemsSet);
        }

        if (unitOfMeasure.isConversionFactorEnabled()) {
            double conversionFactor = getInvalidEmptyFourPortionsSet().
                    getConversionFactor();

            boolean isConversionFactorSet = unitOfMeasure.isConversionFactorSet(conversionFactor);

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
                isTotalUnitOneSet(invalidTotalMeasurementOne);

        if (isTotalMeasurementOneSet)
            throwMeasurementOneException(isTotalMeasurementOneSet);

        return new MeasurementModel(
                unitOfMeasure.getMeasurementType(),
                unitOfMeasure.getMeasurementSubtype(),
                unitOfMeasure.getNumberOfUnits(),
                unitOfMeasure.isConversionFactorEnabled(),
                unitOfMeasure.getConversionFactor(),
                unitOfMeasure.getItemBaseUnits(),
                unitOfMeasure.getTotalBaseUnits(),
                unitOfMeasure.getNumberOfItems(),
                invalidTotalMeasurementOne,
                unitOfMeasure.getItemUnitOne(),
                unitOfMeasure.getTotalUnitTwo(),
                unitOfMeasure.getItemUnitTwo(),
                unitOfMeasure.isValidMeasurement(),
                unitOfMeasure.getMinUnitOneInBaseUnits(),
                unitOfMeasure.getMaxUnitOne(),
                unitOfMeasure.getMaxUnitTwo(),
                unitOfMeasure.getMaxUnitDigitWidths()
        );
    }

    //-----------
    public static MeasurementModel getNewValidTotalMeasurementOne() {
        UnitOfMeasure unitOfMeasure = getSubtypeForValidNewMetric().getMeasurementClass();

        boolean isPortionsSet = unitOfMeasure.isNumberOfItemsSet(getEmptyModelFourPortions());
        if (!isPortionsSet) {
            throwNumberOfItemsException(isPortionsSet);
        }

        boolean isConversionFactorSet;
        if (unitOfMeasure.isConversionFactorEnabled()) {
            isConversionFactorSet = unitOfMeasure.isConversionFactorSet(
                    getInvalidEmptyFourPortionsSet().getConversionFactor());
            if (!isConversionFactorSet) {
                throwConversionFactorException(isConversionFactorSet);
            }
        }

        UnitOfMeasure forCalculatingUnitOneValue = getSubtypeForValidNewMetric().getMeasurementClass();
        boolean isTotalBaseUnitsSet = forCalculatingUnitOneValue.isTotalBaseUnitsSet(
                getBaseUnitsForNewValidTotalMeasurementOne());
        if (!isTotalBaseUnitsSet) {
            throwTotalBaseUnitsException(isTotalBaseUnitsSet);
        }

        boolean isTotalMeasurementOneIsSet = unitOfMeasure.isTotalUnitOneSet(
                forCalculatingUnitOneValue.getTotalUnitOne());
        if (!isTotalMeasurementOneIsSet) {
            throwMeasurementOneException(isTotalMeasurementOneIsSet);
        }

        return getMeasurementModel(unitOfMeasure);
    }

    private static double getBaseUnitsForNewValidTotalMeasurementOne() {
        return TestDataRecipeIngredientQuantityEntity.getNewValidMetric().getItemBaseUnits() *
                TestDataRecipePortionsEntity.getNewValidFourPortions().getServings() *
                TestDataRecipePortionsEntity.getNewValidFourPortions().getServings();
    }

    //-----------
    public static MeasurementModel getNewInvalidTotalMeasurementTwo() {
        UnitOfMeasure unitOfMeasure = getSubtypeForValidNewMetric().getMeasurementClass();
        boolean isNumberOfItemsSet = unitOfMeasure.isNumberOfItemsSet(getEmptyModelFourPortions());

        if (!isNumberOfItemsSet) {
            throwNumberOfItemsException(isNumberOfItemsSet);
        }

        if (unitOfMeasure.isConversionFactorEnabled()) {
            double conversionFactor = getInvalidEmptyFourPortionsSet().
                    getConversionFactor();

            boolean isConversionFactorSet = unitOfMeasure.isConversionFactorSet(conversionFactor);

            if (!isConversionFactorSet) {
                throwConversionFactorException(isConversionFactorSet);
            }
        }

        int invalidMeasurementTwo = unitOfMeasure.getMaxUnitTwo() + 1;
        boolean isTotalMeasurementTwoSet = unitOfMeasure.isTotalUnitTwoSet(
                invalidMeasurementTwo);

        if (isTotalMeasurementTwoSet)
            throwMeasurementTwoException(isTotalMeasurementTwoSet);

        return new MeasurementModel(
                unitOfMeasure.getMeasurementType(),
                unitOfMeasure.getMeasurementSubtype(),
                unitOfMeasure.getNumberOfUnits(),
                unitOfMeasure.isConversionFactorEnabled(),
                unitOfMeasure.getConversionFactor(),
                unitOfMeasure.getItemBaseUnits(),
                unitOfMeasure.getTotalBaseUnits(),
                unitOfMeasure.getNumberOfItems(),
                unitOfMeasure.getTotalUnitOne(),
                unitOfMeasure.getItemUnitOne(),
                invalidMeasurementTwo,
                unitOfMeasure.getItemUnitTwo(),
                unitOfMeasure.isValidMeasurement(),
                unitOfMeasure.getMinUnitOneInBaseUnits(),
                unitOfMeasure.getMaxUnitOne(),
                unitOfMeasure.getMaxUnitTwo(),
                unitOfMeasure.getMaxUnitDigitWidths()
        );
    }

    //-----------
    public static MeasurementModel getNewValidTotalMeasurementTwo() {
        UnitOfMeasure unitOfMeasure = getSubtypeForValidNewMetric().getMeasurementClass();

        boolean isNumberOfItemsSet = unitOfMeasure.isNumberOfItemsSet(getEmptyModelFourPortions());
        if (!isNumberOfItemsSet) {
            throwNumberOfItemsException(isNumberOfItemsSet);
        }

        if (unitOfMeasure.isConversionFactorEnabled()) {
            double conversionFactor = getInvalidEmptyFourPortionsSet().
                    getConversionFactor();

            boolean isConversionFactorSet = unitOfMeasure.isConversionFactorSet(conversionFactor);

            if (!isConversionFactorSet)
                throwConversionFactorException(isConversionFactorSet);
        }

        int totalMeasurementTwo = unitOfMeasure.getMaxUnitTwo();
        boolean isTotalMeasurementTwoSet = unitOfMeasure.
                isTotalUnitTwoSet(totalMeasurementTwo);

        if (!isTotalMeasurementTwoSet)
            throwMeasurementTwoException(isTotalMeasurementTwoSet);

        return getMeasurementModel(unitOfMeasure);
    }

    private static MeasurementSubtype getSubtypeForValidNewMetric() {
        return MeasurementSubtype.fromInt(TestDataRecipeIngredientQuantityEntity.
                getNewValidMetric().getUnitOfMeasureSubtype());
    }

    //-----------
    public static MeasurementModel getNewInvalidUnitOfMeasureChangedImperialSpoon() {
        UnitOfMeasure unitOfMeasure = getSubtypeForNewValidImperialSpoon().getMeasurementClass();

        boolean isNumberOfItemsSet = unitOfMeasure.isNumberOfItemsSet(getEmptyModelFourPortions());
        if (!isNumberOfItemsSet) {
            throwNumberOfItemsException(isNumberOfItemsSet);
        }

        if (unitOfMeasure.isConversionFactorEnabled()) {
            double conversionFactor = getInvalidEmptyFourPortionsSet().
                    getConversionFactor();

            boolean isConversionFactorSet = unitOfMeasure.isConversionFactorSet(conversionFactor);

            if (!isConversionFactorSet)
                throwConversionFactorException(isConversionFactorSet);
        }

        return getMeasurementModel(unitOfMeasure);
    }

    public static MeasurementModel getNewInvalidUnitOfMeasureChangedMetricMass() {
        UnitOfMeasure unitOfMeasure = getSubtypeForValidNewMetric().getMeasurementClass();

        boolean isNumberOfItemsSet = unitOfMeasure.isNumberOfItemsSet(getEmptyModelFourPortions());
        if (!isNumberOfItemsSet) {
            throwNumberOfItemsException(isNumberOfItemsSet);
        }

        if (unitOfMeasure.isConversionFactorEnabled()) {
            double conversionFactor = getInvalidEmptyFourPortionsSet().
                    getConversionFactor();

            boolean isConversionFactorSet = unitOfMeasure.isConversionFactorSet(conversionFactor);

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

        boolean isNumberOfItemsSet = unitOfMeasure.isNumberOfItemsSet(getEmptyModelFourPortions());
        forCalculatingUnitOneValue.isNumberOfItemsSet(getEmptyModelFourPortions());
        if (!isNumberOfItemsSet) {
            throwNumberOfItemsException(isNumberOfItemsSet);
        }

        if (unitOfMeasure.isConversionFactorEnabled()) {
            double conversionFactor = getInvalidEmptyFourPortionsSet().
                    getConversionFactor();

            boolean isConversionFactorSet = unitOfMeasure.isConversionFactorSet(conversionFactor);
            forCalculatingUnitOneValue.isConversionFactorSet(conversionFactor);

            if (!isConversionFactorSet)
                throwConversionFactorException(isConversionFactorSet);
        }

        forCalculatingUnitOneValue.isItemBaseUnitsSet(TestDataRecipeIngredientQuantityEntity.
                getNewValidImperialOneTeaspoonFourPortionsNoConversionFactor().
                getItemBaseUnits());

        boolean isTotalMeasurementOneSet = unitOfMeasure.isTotalUnitOneSet(
                forCalculatingUnitOneValue.getTotalUnitOne()
        );

        if (!isTotalMeasurementOneSet)
            throwMeasurementOneException(isTotalMeasurementOneSet);

        return getMeasurementModel(unitOfMeasure);
    }

    //-----------
    public static MeasurementModel getNewInvalidConversionFactor() {
        UnitOfMeasure unitOfMeasure = getSubtypeForNewValidImperialSpoon().getMeasurementClass();

        boolean isNumberOfItemsSet = unitOfMeasure.isNumberOfItemsSet(getEmptyModelFourPortions());
        if (!isNumberOfItemsSet) {
            throwNumberOfItemsException(isNumberOfItemsSet);
        }


        double conversionFactor = MAX_CONVERSION_FACTOR + .1;
        if (unitOfMeasure.isConversionFactorEnabled()) {
            boolean isConversionFactorSet = unitOfMeasure.isConversionFactorSet(conversionFactor);
            if (isConversionFactorSet)
                throwConversionFactorException(isConversionFactorSet);
        }

        return new MeasurementModel(
                unitOfMeasure.getMeasurementType(),
                unitOfMeasure.getMeasurementSubtype(),
                unitOfMeasure.getNumberOfUnits(),
                unitOfMeasure.isConversionFactorEnabled(),
                conversionFactor,
                unitOfMeasure.getItemBaseUnits(),
                unitOfMeasure.getTotalBaseUnits(),
                unitOfMeasure.getNumberOfItems(),
                unitOfMeasure.getTotalUnitOne(),
                unitOfMeasure.getItemUnitOne(),
                unitOfMeasure.getTotalUnitTwo(),
                unitOfMeasure.getItemUnitTwo(),
                unitOfMeasure.isValidMeasurement(),
                unitOfMeasure.getMinUnitOneInBaseUnits(),
                unitOfMeasure.getMaxUnitOne(),
                unitOfMeasure.getMaxUnitTwo(),
                unitOfMeasure.getMaxUnitDigitWidths()
        );
    }

    //-----------
    public static MeasurementModel getNewValidImperialSpoonWithConversionFactor() {
        UnitOfMeasure unitOfMeasure = getSubtypeForNewValidImperialSpoon().getMeasurementClass();

        boolean isNumberOfItemsSet = unitOfMeasure.isNumberOfItemsSet(getEmptyModelFourPortions());
        if (!isNumberOfItemsSet) {
            throwNumberOfItemsException(isNumberOfItemsSet);
        }

        if (unitOfMeasure.isConversionFactorEnabled()) {
            boolean isConversionFactorSet = unitOfMeasure.isConversionFactorSet(
                    MAX_CONVERSION_FACTOR);

            if (!isConversionFactorSet)
                throwConversionFactorException(isConversionFactorSet);
        }

        boolean itemBaseUnitsAreSet = unitOfMeasure.isItemBaseUnitsSet(
                getNewValidImperialSpoonMaxConversionFactor().getItemBaseUnits()
        );
        if (!itemBaseUnitsAreSet) {
            throwItemBaseUnitsAreSetException(itemBaseUnitsAreSet);
        }

        return getMeasurementModel(unitOfMeasure);
    }

    private static MeasurementSubtype getSubtypeForNewValidImperialSpoon() {
        return MeasurementSubtype.fromInt(TestDataRecipeIngredientQuantityEntity.
                getNewValidImperialOneTeaspoonFourPortionsNoConversionFactor().getUnitOfMeasureSubtype());
    }

    private static int getEmptyModelFourPortions() {
        return TestDataRecipePortionsEntity.getNewValidFourPortions().getServings()
                * TestDataRecipePortionsEntity.getNewValidFourPortions().getSittings();
    }

    //-----------
    public static MeasurementModel getExistingValidMetric() {
        UnitOfMeasure unitOfMeasure = getExistingValidSubtype().getMeasurementClass();

        boolean isNumberOfItemsSet = unitOfMeasure.isNumberOfItemsSet(getExistingValidNinePortions());
        if (!isNumberOfItemsSet) {
            throwNumberOfItemsException(isNumberOfItemsSet);
        }

        if (unitOfMeasure.isConversionFactorEnabled()) {
            boolean isConversionFactorSet = unitOfMeasure.isConversionFactorSet(
                    getExistingValidConversionFactor());

            if (!isConversionFactorSet)
                throwConversionFactorException(isConversionFactorSet);
        }

        boolean itemBaseUnitsAreSet = unitOfMeasure.isItemBaseUnitsSet(
                TestDataRecipeIngredientQuantityEntity.getExistingValidMetric().getItemBaseUnits()
        );
        if (!itemBaseUnitsAreSet) {
            throwItemBaseUnitsAreSetException(itemBaseUnitsAreSet);
        }


        return getMeasurementModel(unitOfMeasure);
    }

    public static MeasurementModel getExistingMetricInvalidTotalOne() {
        UnitOfMeasure unitOfMeasure = getExistingValidSubtype().getMeasurementClass();

        boolean isNumberOfItemsSet = unitOfMeasure.isNumberOfItemsSet(getExistingValidNinePortions());
        if (!isNumberOfItemsSet) {
            throwNumberOfItemsException(isNumberOfItemsSet);
        }

        if (unitOfMeasure.isConversionFactorEnabled()) {
            boolean isConversionFactorSet = unitOfMeasure.isConversionFactorSet(
                    getExistingValidConversionFactor());

            if (!isConversionFactorSet)
                throwConversionFactorException(isConversionFactorSet);
        }

        boolean isItemBaseUnitsSet = unitOfMeasure.isItemBaseUnitsSet(
                TestDataRecipeIngredientQuantityEntity.getExistingValidMetric().getItemBaseUnits()
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
                isTotalUnitOneSet(invalidTotalMeasurementOne);

        if (isTotalMeasurementOneSet)
            throwMeasurementOneException(isTotalMeasurementOneSet);

        return new MeasurementModel(
                unitOfMeasure.getMeasurementType(),
                unitOfMeasure.getMeasurementSubtype(),
                unitOfMeasure.getNumberOfUnits(),
                unitOfMeasure.isConversionFactorEnabled(),
                unitOfMeasure.getConversionFactor(),
                unitOfMeasure.getItemBaseUnits(),
                unitOfMeasure.getTotalBaseUnits(),
                unitOfMeasure.getNumberOfItems(),
                invalidTotalMeasurementOne,
                unitOfMeasure.getItemUnitOne(),
                unitOfMeasure.getTotalUnitTwo(),
                unitOfMeasure.getItemUnitTwo(),
                unitOfMeasure.isValidMeasurement(),
                unitOfMeasure.getMinUnitOneInBaseUnits(),
                unitOfMeasure.getMaxUnitOne(),
                unitOfMeasure.getMaxUnitTwo(),
                unitOfMeasure.getMaxUnitDigitWidths()
        );
    }

    //-----------
    public static MeasurementModel getExistingMetricInvalidTotalTwo() {
        UnitOfMeasure unitOfMeasure = getExistingValidSubtype().getMeasurementClass();

        if (unitOfMeasure.isConversionFactorEnabled()) {
            double conversionFactor = getExistingValidConversionFactor();

            boolean isConversionFactorSet = unitOfMeasure.isConversionFactorSet(conversionFactor);

            if (!isConversionFactorSet)
                throwConversionFactorException(isConversionFactorSet);
        }

        boolean isNumberOfItemsSet = unitOfMeasure.isNumberOfItemsSet(getExistingValidNinePortions());
        if (!isNumberOfItemsSet) {
            throwNumberOfItemsException(isNumberOfItemsSet);
        }

        boolean isItemBaseUnitsSet = unitOfMeasure.isItemBaseUnitsSet(
                TestDataRecipeIngredientQuantityEntity.getExistingValidMetric().getItemBaseUnits()
        );

        if (!isItemBaseUnitsSet)
            throwItemBaseUnitsAreSetException(isItemBaseUnitsSet);

        int maxTotalMeasurementTwo = unitOfMeasure.getMaxUnitTwo();
        int invalidTotalMeasurementTwo = maxTotalMeasurementTwo + 1;
        boolean isTotalMeasurementTwoSet = unitOfMeasure.
                isTotalUnitTwoSet(invalidTotalMeasurementTwo);

        if (isTotalMeasurementTwoSet) {
            throwMeasurementTwoException(isTotalMeasurementTwoSet);
        }

        return new MeasurementModel(
                unitOfMeasure.getMeasurementType(),
                unitOfMeasure.getMeasurementSubtype(),
                unitOfMeasure.getNumberOfUnits(),
                unitOfMeasure.isConversionFactorEnabled(),
                unitOfMeasure.getConversionFactor(),
                unitOfMeasure.getItemBaseUnits(),
                unitOfMeasure.getTotalBaseUnits(),
                unitOfMeasure.getNumberOfItems(),
                unitOfMeasure.getTotalUnitOne(),
                unitOfMeasure.getItemUnitOne(),
                invalidTotalMeasurementTwo,
                unitOfMeasure.getItemUnitTwo(),
                unitOfMeasure.isValidMeasurement(),
                unitOfMeasure.getMinUnitOneInBaseUnits(),
                unitOfMeasure.getMaxUnitOne(),
                unitOfMeasure.getMaxUnitTwo(),
                unitOfMeasure.getMaxUnitDigitWidths()
        );
    }

    //-----------
    public static MeasurementModel getExistingMetricValidTwoUpdated() {
        UnitOfMeasure unitOfMeasure = getExistingValidSubtype().getMeasurementClass();

        boolean isNumberOfItemsSet = unitOfMeasure.isNumberOfItemsSet(getExistingValidNinePortions());
        if (!isNumberOfItemsSet) {
            throwNumberOfItemsException(isNumberOfItemsSet);
        }

        if (unitOfMeasure.isConversionFactorEnabled()) {
            double conversionFactor = getExistingValidConversionFactor();

            boolean isConversionFactorSet = unitOfMeasure.isConversionFactorSet(conversionFactor);

            if (!isConversionFactorSet)
                throwConversionFactorException(isConversionFactorSet);
        }

        double itemBaseUnits = TestDataRecipeIngredientQuantityEntity.getExistingValidMetric().
                getItemBaseUnits();
        boolean isItemBaseUnitsSet = unitOfMeasure.isItemBaseUnitsSet(itemBaseUnits);

        if (!isItemBaseUnitsSet) {
            throwItemBaseUnitsAreSetException(isItemBaseUnitsSet);
        }

        int maxTotalMeasurementTwo = 4;
        boolean isTotalMeasurementTwoSet = unitOfMeasure.
                isTotalUnitTwoSet(maxTotalMeasurementTwo);

        if (!isTotalMeasurementTwoSet) {
            throwMeasurementTwoException(isTotalMeasurementTwoSet);
        }

        return getMeasurementModel(unitOfMeasure);
    }

    //-----------
    public static MeasurementModel getExistingMetricUnitOfMeasureUpdatedToImperial() {
        UnitOfMeasure unitOfMeasure = MeasurementSubtype.IMPERIAL_MASS.getMeasurementClass();

        boolean isNumberOfItemsSet = unitOfMeasure.isNumberOfItemsSet(
                getExistingValidMetric().getNumberOfItems());
        if (!isNumberOfItemsSet) {
            throwNumberOfItemsException(isNumberOfItemsSet);
        }

        if (unitOfMeasure.isConversionFactorEnabled()) {
            double conversionFactor = getExistingValidMetric().getConversionFactor();

            boolean isConversionFactorSet = unitOfMeasure.isConversionFactorSet(conversionFactor);

            if (!isConversionFactorSet)
                throwConversionFactorException(isConversionFactorSet);
        }

        return getMeasurementModel(unitOfMeasure);
    }

    private static MeasurementSubtype getExistingValidSubtype() {
        return MeasurementSubtype.fromInt(TestDataRecipeIngredientQuantityEntity.
                getExistingValidMetric().getUnitOfMeasureSubtype());
    }

    private static int getExistingValidNinePortions() {
        return TestDataRecipePortionsEntity.getExistingValidNinePortions().getServings() *
                TestDataRecipePortionsEntity.getExistingValidNinePortions().getSittings();
    }

    private static double getExistingValidConversionFactor() {
        return TestDataIngredientEntity.getExistingValidNameValidDescriptionNoConversionFactor().getConversionFactor();
    }

    //-----------
    public static MeasurementModel getExistingImperialSpoonInvalidConversionFactor() {
        UnitOfMeasure unitOfMeasure = getSubtypeForNewValidImperialSpoon().getMeasurementClass();

        boolean isNumberOfItemsSet = unitOfMeasure.isNumberOfItemsSet(getExistingValidNinePortions());
        if (!isNumberOfItemsSet) {
            throwNumberOfItemsException(isNumberOfItemsSet);
        }

        double itemBaseUnits = TestDataRecipeIngredientQuantityEntity.
                getExistingValidImperialTwoSpoons().getItemBaseUnits();
        unitOfMeasure.isItemBaseUnitsSet(itemBaseUnits);

        double conversionFactor = MAX_CONVERSION_FACTOR + .01;
        if (unitOfMeasure.isConversionFactorEnabled()) {
            boolean isConversionFactorSet = unitOfMeasure.isConversionFactorSet(conversionFactor);
            if (isConversionFactorSet) {
                throwConversionFactorException(isConversionFactorSet);
            }
        }

        return new MeasurementModel(
                unitOfMeasure.getMeasurementType(),
                unitOfMeasure.getMeasurementSubtype(),
                unitOfMeasure.getNumberOfUnits(),
                unitOfMeasure.isConversionFactorEnabled(),
                conversionFactor,
                unitOfMeasure.getItemBaseUnits(),
                unitOfMeasure.getTotalBaseUnits(),
                unitOfMeasure.getNumberOfItems(),
                unitOfMeasure.getTotalUnitOne(),
                unitOfMeasure.getItemUnitOne(),
                unitOfMeasure.getTotalUnitTwo(),
                unitOfMeasure.getItemUnitTwo(),
                unitOfMeasure.isValidMeasurement(),
                unitOfMeasure.getMinUnitOneInBaseUnits(),
                unitOfMeasure.getMaxUnitOne(),
                unitOfMeasure.getMaxUnitTwo(),
                unitOfMeasure.getMaxUnitDigitWidths()
        );
    }

    private static MeasurementModel getMeasurementModel(UnitOfMeasure unitOfMeasure) {
        return new MeasurementModel(
                unitOfMeasure.getMeasurementType(),
                unitOfMeasure.getMeasurementSubtype(),
                unitOfMeasure.getNumberOfUnits(),
                unitOfMeasure.isConversionFactorEnabled(),
                unitOfMeasure.getConversionFactor(),
                unitOfMeasure.getItemBaseUnits(),
                unitOfMeasure.getTotalBaseUnits(),
                unitOfMeasure.getNumberOfItems(),
                unitOfMeasure.getTotalUnitOne(),
                unitOfMeasure.getItemUnitOne(),
                unitOfMeasure.getTotalUnitTwo(),
                unitOfMeasure.getItemUnitTwo(),
                unitOfMeasure.isValidMeasurement(),
                unitOfMeasure.getMinUnitOneInBaseUnits(),
                unitOfMeasure.getMaxUnitOne(),
                unitOfMeasure.getMaxUnitTwo(),
                unitOfMeasure.getMaxUnitDigitWidths()
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
            throw new RuntimeException("isTotalBaseUnitsSet() is true and should be false. " +
                    "There may be a problem with the test data.");
        else
            throw new RuntimeException("isTotalBaseUnitsSet() is false and should be true. " +
                    "There may be a problem with the test data.");
    }

    private static void throwItemBaseUnitsAreSetException(boolean isSet) {
        if (isSet)
            throw new RuntimeException("isItemBaseUnitsSet() is true and should be false. " +
                    "There may be a problem with the test data.");
        else
            throw new RuntimeException("isItemBaseUnitsSet() is false and should be true. " +
                    "There may be a problem with the test data.");
    }
}
