package com.example.peter.thekitchenmenu.testdata;

import com.example.peter.thekitchenmenu.domain.model.MeasurementModel;
import com.example.peter.thekitchenmenu.domain.model.MeasurementModelBuilder;
import com.example.peter.thekitchenmenu.domain.unitofmeasureentities.MeasurementSubtype;
import com.example.peter.thekitchenmenu.domain.unitofmeasureentities.MeasurementType;
import com.example.peter.thekitchenmenu.domain.unitofmeasureentities.UnitOfMeasure;

import static com.example.peter.thekitchenmenu.testdata.TestDataRecipeIngredientQuantityEntity.getNewValidImperialSpoonMaxConversionFactor;
import static com.example.peter.thekitchenmenu.domain.unitofmeasureentities.UnitOfMeasureConstants.*;

public class TestDataMeasurementModel {

    public static MeasurementModel getInvalidEmptyFourPortionsSet() {
        UnitOfMeasure unitOfMeasure = getSubtypeFromNewInvalidMetric().getMeasurementClass();
        boolean isPortionsSet = unitOfMeasure.isNumberOfItemsSet(getEmptyModelFourPortions());

        if (!isPortionsSet) {
            throwNumberOfItemsException(isPortionsSet);
        }

        return getMeasurementModel(unitOfMeasure);
    }

    private static MeasurementSubtype getSubtypeFromNewInvalidMetric() {
        return MeasurementSubtype.fromInt(TestDataRecipeIngredientQuantityEntity.
                getNewInvalid().getUnitOfMeasureSubtype());
    }

    public static MeasurementModel getNewInvalidTotalUnitOne() {
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
            throwTotalUnitOneException(isTotalMeasurementOneSet);

        return MeasurementModelBuilder.
                basedOnUnitOfMeasure(unitOfMeasure).
                setTotalUnitOne(invalidTotalMeasurementOne).
                build();
    }

    public static MeasurementModel getNewValidTotalUnitOne() {
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
                getBaseUnitsForNewValidTotalUnitOne());
        if (!isTotalBaseUnitsSet) {
            throwTotalBaseUnitsException(isTotalBaseUnitsSet);
        }

        boolean isTotalMeasurementOneIsSet = unitOfMeasure.isTotalUnitOneSet(
                forCalculatingUnitOneValue.getTotalUnitOne());
        if (!isTotalMeasurementOneIsSet) {
            throwTotalUnitOneException(isTotalMeasurementOneIsSet);
        }

        return getMeasurementModel(unitOfMeasure);
    }

    private static double getBaseUnitsForNewValidTotalUnitOne() {
        return TestDataRecipeIngredientQuantityEntity.getNewValidMetric().getItemBaseUnits() *
                TestDataRecipePortionsEntity.getNewValidFourPortions().getServings() *
                TestDataRecipePortionsEntity.getNewValidFourPortions().getServings();
    }

    public static MeasurementModel getNewInvalidTotalUnitTwo() {
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

        int invalidUnitTwo = unitOfMeasure.getMaxUnitTwo() + 1;
        boolean isTotalUnitTwoSet = unitOfMeasure.isTotalUnitTwoSet(
                invalidUnitTwo);

        if (isTotalUnitTwoSet)
            throwUnitTwoException(isTotalUnitTwoSet);

        return MeasurementModelBuilder.
                basedOnUnitOfMeasure(unitOfMeasure).
                setTotalUnitTwo(invalidUnitTwo).
                build();
    }

    public static MeasurementModel getNewValidTotalUnitTwo() {
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

        int totalUnitTwo = unitOfMeasure.getMaxUnitTwo();
        boolean isTotalUnitTwoSet = unitOfMeasure.
                isTotalUnitTwoSet(totalUnitTwo);

        if (!isTotalUnitTwoSet)
            throwUnitTwoException(isTotalUnitTwoSet);

        return getMeasurementModel(unitOfMeasure);
    }

    private static MeasurementSubtype getSubtypeForValidNewMetric() {
        return MeasurementSubtype.fromInt(TestDataRecipeIngredientQuantityEntity.
                getNewValidMetric().getUnitOfMeasureSubtype());
    }

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

        boolean isTotalUnitOneSet = unitOfMeasure.isTotalUnitOneSet(
                forCalculatingUnitOneValue.getTotalUnitOne()
        );

        if (!isTotalUnitOneSet)
            throwTotalUnitOneException(isTotalUnitOneSet);

        return getMeasurementModel(unitOfMeasure);
    }

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

        return MeasurementModelBuilder.
                basedOnUnitOfMeasure(unitOfMeasure).
                setConversionFactor(conversionFactor).
                build();
    }

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

    public static MeasurementModel getValidExistingMetric() {
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

        double invalidTotalUnitOne;
        MeasurementType type = unitOfMeasure.getMeasurementType();
        switch (type) {
            case MASS:
                invalidTotalUnitOne = MAX_MASS + 1;
                break;
            case VOLUME:
                invalidTotalUnitOne = MAX_VOLUME + 1;
                break;
            case COUNT:
                throw new RuntimeException("Can't set totalMeasurementOne() for type COUNT");
            default:
                throw new RuntimeException("Unit of measure Type not recognised");
        }

        boolean isTotalMeasurementOneSet = unitOfMeasure.
                isTotalUnitOneSet(invalidTotalUnitOne);

        if (isTotalMeasurementOneSet)
            throwTotalUnitOneException(isTotalMeasurementOneSet);

        return MeasurementModelBuilder.
                basedOnUnitOfMeasure(unitOfMeasure).
                setTotalUnitOne(invalidTotalUnitOne).
                build();
    }

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
            throwUnitTwoException(isTotalMeasurementTwoSet);
        }

        return MeasurementModelBuilder.
                basedOnUnitOfMeasure(unitOfMeasure).
                setTotalUnitTwo(invalidTotalMeasurementTwo).
                build();
    }

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
            throwUnitTwoException(isTotalMeasurementTwoSet);
        }

        return getMeasurementModel(unitOfMeasure);
    }

    public static MeasurementModel getExistingMetricUnitOfMeasureUpdatedToImperial() {
        UnitOfMeasure unitOfMeasure = MeasurementSubtype.IMPERIAL_MASS.getMeasurementClass();

        boolean isNumberOfItemsSet = unitOfMeasure.isNumberOfItemsSet(
                getValidExistingMetric().getNumberOfItems());
        if (!isNumberOfItemsSet) {
            throwNumberOfItemsException(isNumberOfItemsSet);
        }

        if (unitOfMeasure.isConversionFactorEnabled()) {
            double conversionFactor = getValidExistingMetric().getConversionFactor();

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

    public static MeasurementModel getExistingImperialSpoonValidConversionFactorNotSet() {
        UnitOfMeasure unitOfMeasure = getSubtypeForNewValidImperialSpoon().getMeasurementClass();

        boolean isNumberOfItemsSet = unitOfMeasure.isNumberOfItemsSet(getExistingValidNinePortions());
        if (!isNumberOfItemsSet) {
            throwNumberOfItemsException(isNumberOfItemsSet);
        }

        double itemBaseUnits = TestDataRecipeIngredientQuantityEntity.
                getExistingValidImperialTwoSpoons().getItemBaseUnits();
        boolean isItemBaseUnitsSet = unitOfMeasure.isItemBaseUnitsSet(itemBaseUnits);
        if (!isItemBaseUnitsSet) {
            throwItemBaseUnitsAreSetException(isItemBaseUnitsSet);
        }

        return getMeasurementModel(unitOfMeasure);

    }

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

        return MeasurementModelBuilder.basedOnUnitOfMeasure(unitOfMeasure).
                setConversionFactor(conversionFactor).
                build();
    }

    private static MeasurementModel getMeasurementModel(UnitOfMeasure unitOfMeasure) {
        return MeasurementModelBuilder.basedOnUnitOfMeasure(unitOfMeasure).build();
    }

    private static void throwNumberOfItemsException(boolean isSet) {
        if (isSet)
            throw new RuntimeException("numberOfItems() is true and should be false, there, may " +
                    "be a problem with the test data.");
        else
            throw new RuntimeException("numberOfItems() is false and should be true, there may " +
                    "be a problem with the test data.");
    }

    private static void throwTotalUnitOneException(boolean isSet) {
        if (isSet)
            throw new RuntimeException("totalMeasurementOne is true and it should be false. " +
                    "There may be a problem with the test data");
        else
            throw new RuntimeException("totalMeasurementOne is false and it should be true. " +
                    "There may be a problem with the test data.");
    }

    private static void throwUnitTwoException(boolean isSet) {
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
