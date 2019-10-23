package com.example.peter.thekitchenmenu.testdata;

import com.example.peter.thekitchenmenu.data.model.MeasurementModel;
import com.example.peter.thekitchenmenu.utils.unitofmeasure.MeasurementResult;
import com.example.peter.thekitchenmenu.utils.unitofmeasure.MeasurementSubtype;
import com.example.peter.thekitchenmenu.utils.unitofmeasure.UnitOfMeasureConstants;

public class MeasurementModelTestData {

    public static MeasurementModel getEmptyModel() {
        return new MeasurementModel(
                MeasurementSubtype.METRIC_MASS,
                getEmptyModelPortions(),
                1,
                0,
                0,
                0,
                0,
                0
        );
    }

    private static int getEmptyModelPortions() {
        return RecipePortionsEntityTestData.getNewValidFourPortions().getServings()
                * RecipePortionsEntityTestData.getNewValidFourPortions().getSittings();
    }

    public static MeasurementResult getResultInvalidMeasurement() {
        return new MeasurementResult(
                getEmptyModel(),
                MeasurementResult.ResultStatus.INVALID_MEASUREMENT);
    }

    public static MeasurementModel getNewInvalidTotalMeasurementOne() {
        return new MeasurementModel(
                getNewValidMetricSubtype(),
                getEmptyModelPortions(),
                getEmptyModel().getConversionFactor(),
                UnitOfMeasureConstants.MAX_MASS + 1,
                0,
                0,
                0,
                0
        );
    }

    private static MeasurementSubtype getNewValidMetricSubtype() {
        return MeasurementSubtype.fromInt(RecipeIngredientQuantityEntityTestData.
                getNewValidMetric().
                getUnitOfMeasureSubtype());
    }

    public static MeasurementResult getResultNewInvalidTotalMeasurementOne() {
        return new MeasurementResult(
                getEmptyModel(),
                MeasurementResult.ResultStatus.INVALID_TOTAL_MEASUREMENT_ONE
        );
    }

    public static MeasurementModel getNewValidTotalMeasurementOne() {
        return new MeasurementModel(
                getNewValidMetricSubtype(),
                getEmptyModelPortions(),
                getEmptyModel().getConversionFactor(),
                getNewValidMetricTotalBasUnits(),
                0,
                0,
                0,
                0
        );
    }

    private static double getNewValidMetricTotalBasUnits() {
        return RecipeIngredientQuantityEntityTestData.getNewValidMetric().getItemBaseUnits() *
                RecipePortionsEntityTestData.getNewValidFourPortions().getServings() *
                RecipePortionsEntityTestData.getNewValidFourPortions().getServings();
    }

    private static MeasurementModel getNewTotalMeasurementOneResultModel() {
        return new MeasurementModel(
                getNewValidMetricSubtype(),
                getEmptyModelPortions(),
                getEmptyModel().getConversionFactor(),
                (getNewValidMetricTotalBasUnits() % 1000),
                ((int) getNewValidMetricTotalBasUnits() / 1000),
                (getNewValidMetricTotalBasUnits() % 1000 / getEmptyModelPortions()),
                ((int) getNewValidMetricTotalBasUnits() / 1000 / getEmptyModelPortions()),
                (getNewValidMetricTotalBasUnits() / getEmptyModelPortions()));
    }

    public static MeasurementResult getResultNewValidTotalMeasurementOne() {
        return new MeasurementResult(
                getNewTotalMeasurementOneResultModel(),
                MeasurementResult.ResultStatus.RESULT_OK
        );
    }

    public static MeasurementModel getNewInvalidTotalMeasurementTwo() {
        return new MeasurementModel(
                getNewValidMetricSubtype(),
                getEmptyModelPortions(),
                getEmptyModel().getConversionFactor(),
                0,
                (int) UnitOfMeasureConstants.MAX_MASS / 1000 + 1,
                0,
                0,
                0
        );
    }

    public static MeasurementResult getResultNewInvalidTotalMeasurementTwo() {
        return new MeasurementResult(
                getEmptyModel(),
                MeasurementResult.ResultStatus.INVALID_TOTAL_MEASUREMENT_TWO
        );
    }

    public static MeasurementModel getNewValidTotalMeasurementTwo() {
        return new MeasurementModel(
                getNewValidMetricSubtype(),
                getEmptyModelPortions(),
                getEmptyModel().getConversionFactor(),
                getEmptyModel().getTotalMeasurementOne(),
                ((int) UnitOfMeasureConstants.MAX_MASS / 1000),
                getEmptyModel().getItemMeasurementOne(),
                getEmptyModel().getItemMeasurementTwo(),
                getEmptyModel().getItemBaseUnits()
        );
    }

    private static MeasurementModel getNewTotalMeasurementTwoModelResult() {
        return new MeasurementModel(
                getNewValidMetricSubtype(),
                getEmptyModelPortions(),
                getEmptyModel().getConversionFactor(),
                getEmptyModel().getTotalMeasurementOne(),
                getNewValidTotalMeasurementTwo().getTotalMeasurementTwo(),
                (UnitOfMeasureConstants.MAX_MASS / getEmptyModelPortions() % 1000),
                ((int) UnitOfMeasureConstants.MAX_MASS / getEmptyModelPortions() / 1000),
                (UnitOfMeasureConstants.MAX_MASS / getEmptyModelPortions())
        );
    }

    public static MeasurementResult getResultNewValidTotalMeasurementTwo() {
        return new MeasurementResult(
                getNewTotalMeasurementTwoModelResult(),
                MeasurementResult.ResultStatus.RESULT_OK
        );
    }

    public static MeasurementModel getNewInvalidUnitOfMeasureChangedImperialSpoon() {
        return new MeasurementModel(
                MeasurementSubtype.IMPERIAL_SPOON,
                getEmptyModel().getNumberOfItems(),
                getEmptyModel().getConversionFactor(),
                getEmptyModel().getTotalMeasurementOne(),
                getEmptyModel().getTotalMeasurementTwo(),
                getEmptyModel().getItemMeasurementOne(),
                getEmptyModel().getItemMeasurementTwo(),
                getEmptyModel().getItemBaseUnits()
        );
    }

    public static MeasurementResult getResultNewInvalidUnitOfMeasureChangedImperialSpoon() {
        return new MeasurementResult(
                getNewInvalidUnitOfMeasureChangedImperialSpoon(),
                MeasurementResult.ResultStatus.INVALID_MEASUREMENT
        );
    }

    public static MeasurementModel getNewValidHalfImperialSpoonUnitOneUpdated() {
        return new MeasurementModel(
                getNewInvalidUnitOfMeasureChangedImperialSpoon().getSubtype(),
                getNewInvalidUnitOfMeasureChangedImperialSpoon().getNumberOfItems(),
                getNewInvalidUnitOfMeasureChangedImperialSpoon().getConversionFactor(),
                0.5,
                getNewInvalidUnitOfMeasureChangedImperialSpoon().getTotalMeasurementTwo(),
                getNewInvalidUnitOfMeasureChangedImperialSpoon().getItemMeasurementOne(),
                getNewInvalidUnitOfMeasureChangedImperialSpoon().getItemMeasurementTwo(),
                getNewInvalidUnitOfMeasureChangedImperialSpoon().getItemBaseUnits()
        );
    }

    public static MeasurementResult getNewValidHalfImperialSpoonUnitOneUpdatedResult() {
        return new MeasurementResult(
                getNewValidHalfImperialSpoonUnitOneUpdatedResultModel(),
                MeasurementResult.ResultStatus.RESULT_OK
        );
    }

    private static MeasurementModel getNewValidHalfImperialSpoonUnitOneUpdatedResultModel() {
        return new MeasurementModel(
                getNewInvalidUnitOfMeasureChangedImperialSpoon().getSubtype(),
                getNewInvalidUnitOfMeasureChangedImperialSpoon().getNumberOfItems(),
                getNewInvalidUnitOfMeasureChangedImperialSpoon().getConversionFactor(),
                0.5,
                getNewInvalidUnitOfMeasureChangedImperialSpoon().getTotalMeasurementTwo(),
                (0.5 / getNewInvalidUnitOfMeasureChangedImperialSpoon().getNumberOfItems()),
                getNewInvalidUnitOfMeasureChangedImperialSpoon().getItemMeasurementTwo(),
                (5 * 0.5 / getNewInvalidUnitOfMeasureChangedImperialSpoon().getNumberOfItems())
        );
    }

    public static MeasurementModel getNewInvalidConversionFactor() {
        return new MeasurementModel(
                getNewValidMetricSubtype(),
                getEmptyModelPortions(),
                UnitOfMeasureConstants.MAX_CONVERSION_FACTOR + 0.1,
                0,
                0,
                0,
                0,
                0
        );
    }

    public static MeasurementResult getResultNewInvalidConversionFactor() {
        return new MeasurementResult(getEmptyModel(),
                MeasurementResult.ResultStatus.INVALID_CONVERSION_FACTOR
        );
    }

    public static MeasurementModel getNewValidImperialSpoonWithConversionFactor() {
        return new MeasurementModel(
                getNewInvalidUnitOfMeasureChangedImperialSpoon().getSubtype(),
                getEmptyModelPortions(),
                UnitOfMeasureConstants.MAX_CONVERSION_FACTOR,
                1,
                1,
                1,
                0,
                (getImperialTeaspoonVolume() + getImperialTableSpoonVolume()) *
                        UnitOfMeasureConstants.MAX_CONVERSION_FACTOR / getEmptyModelPortions()
        );
    }

    public static MeasurementResult getResultNewValidImperialSpoonWithConversionFactor() {
        return new MeasurementResult(
                getNewValidImperialSpoonWithConversionFactor(),
                MeasurementResult.ResultStatus.RESULT_OK
        );
    }

    private static double getImperialTeaspoonVolume() {
        return 5;
    }

    private static double getImperialTableSpoonVolume() {
        return 15;
    }

    public static MeasurementModel getExistingMetricValid() {
        return new MeasurementModel(
                getExistingValidSubtype(),
                getExistingValidNinePortions(),
                getExistingValidConversionFactor(),
                getExistingValidTotalMeasurementOne(),
                getExistingValidTotalMeasurementTwo(),
                getExistingValidItemMeasurementOne(),
                getExistingValidItemMeasurementTwo(),
                RecipeIngredientQuantityEntityTestData.getExistingValidMetric().getItemBaseUnits()
        );
    }

    public static MeasurementResult getResultExistingMetricValid() {
        return new MeasurementResult(
                getExistingMetricValid(),
                MeasurementResult.ResultStatus.RESULT_OK
        );
    }

    public static MeasurementModel getExistingMetricInvalidTotalOne() {
        return new MeasurementModel(
                getExistingMetricValid().getSubtype(),
                getExistingMetricValid().getNumberOfItems(),
                getExistingMetricValid().getConversionFactor(),
                UnitOfMeasureConstants.MAX_VOLUME + 1,
                getExistingMetricValid().getTotalMeasurementTwo(),
                getExistingMetricValid().getItemMeasurementOne(),
                getExistingMetricValid().getItemMeasurementTwo(),
                getExistingMetricValid().getItemBaseUnits()
        );
    }

    public static MeasurementResult getExistingMetricInvalidTotalOneResult() {
        return new MeasurementResult(
                getExistingMetricInvalidTotalOneResultModel(),
                MeasurementResult.ResultStatus.INVALID_TOTAL_MEASUREMENT_ONE
        );
    }

    private static MeasurementModel getExistingMetricInvalidTotalOneResultModel() {
        return new MeasurementModel(
                getExistingValidSubtype(),
                getExistingValidNinePortions(),
                getExistingValidConversionFactor(),
                0,
                getExistingValidTotalMeasurementTwo(),
                ((int) getExistingMetricValidItemBaseUnitsMinusTotalMeasurementOne() % 1000),
                getExistingValidItemMeasurementTwo(),
                getExistingMetricValidItemBaseUnitsMinusTotalMeasurementOne()
        );
    }

    private static int getExistingValidTotalMeasurementTwo() {
        return (int) RecipeIngredientQuantityEntityTestData.
                getExistingValidMetric().
                getItemBaseUnits() * getExistingValidNinePortions() / 1000;
    }

    private static double getExistingMetricValidItemBaseUnitsMinusTotalMeasurementOne() {
        return getExistingMetricValid().getTotalMeasurementTwo()
                * 1000 / (double) getExistingMetricValid().getNumberOfItems();
    }

    public static MeasurementModel getExistingMetricInvalidTotalTwo() {
        return new MeasurementModel(
                getExistingValidSubtype(),
                getExistingValidNinePortions(),
                getExistingValidConversionFactor(),
                getExistingValidTotalMeasurementOne(),
                (int) UnitOfMeasureConstants.MAX_VOLUME / 1000 + 1,
                getExistingValidItemMeasurementOne(),
                getExistingValidItemMeasurementTwo(),
                RecipeIngredientQuantityEntityTestData.getExistingValidMetric().getItemBaseUnits()
        );
    }

    private static int getExistingValidItemMeasurementTwo() {
        return (int) RecipeIngredientQuantityEntityTestData.
                getExistingValidMetric().
                getItemBaseUnits() / 1000;
    }

    private static double getExistingValidItemMeasurementOne() {
        return RecipeIngredientQuantityEntityTestData.
                getExistingValidMetric().
                getItemBaseUnits() % 1000;
    }

    public static MeasurementResult getExistingInvalidTotalTwoResult() {
        return new MeasurementResult(
                getExistingMetricInvalidTotalTwoModelResult(),
                MeasurementResult.ResultStatus.INVALID_TOTAL_MEASUREMENT_TWO
        );
    }

    private static MeasurementModel getExistingMetricInvalidTotalTwoModelResult() {
        return new MeasurementModel(
                getExistingValidSubtype(),
                getExistingValidNinePortions(),
                getExistingValidConversionFactor(),
                getExistingValidTotalMeasurementOne(),
                0,
                (int) getExistingMetricValidItemBaseUnits() % 1000,
                0,
                getExistingMetricValidItemBaseUnits()
        );
    }

    private static double getExistingMetricValidItemBaseUnits() {
        return getExistingMetricValid().getTotalMeasurementOne() /
                (double) getExistingMetricValid().getNumberOfItems();
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

    private static double getExistingValidTotalMeasurementOne() {
        return RecipeIngredientQuantityEntityTestData.
                getExistingValidMetric().
                getItemBaseUnits() * getExistingValidNinePortions() % 1000;
    }

    public static MeasurementModel getExistingMetricValidTwoUpdated() {
        return new MeasurementModel(
                getExistingMetricValid().getSubtype(),
                getExistingMetricValid().getNumberOfItems(),
                getExistingMetricValid().getConversionFactor(),
                getExistingMetricValid().getTotalMeasurementOne(),
                getExistingMetricValidUpdatedTwoValue(),
                getExistingMetricValid().getItemMeasurementOne(),
                getExistingMetricValid().getItemMeasurementTwo(),
                getExistingMetricValid().getItemBaseUnits()
        );
    }

    public static MeasurementResult getExistingMetricValidTwoUpdatedResult() {
        return new MeasurementResult(
                getExistingMetricValidTwoUpdatedResultModel(),
                MeasurementResult.ResultStatus.RESULT_OK
        );
    }

    private static MeasurementModel getExistingMetricValidTwoUpdatedResultModel() {
        return new MeasurementModel(
                getExistingMetricValid().getSubtype(),
                getExistingMetricValid().getNumberOfItems(),
                getExistingMetricValid().getConversionFactor(),
                getExistingMetricValid().getTotalMeasurementOne(),
                getExistingMetricValidUpdatedTwoValue(),
                (int) getExistingMetricValidTwoUpdatedItemBaseUnits(),
                getExistingMetricValid().getItemMeasurementTwo(),
                getExistingMetricValidTwoUpdatedItemBaseUnits()
        );
    }

    private static double getExistingMetricValidTwoUpdatedItemBaseUnits() {
        return (((double) getExistingMetricValidUpdatedTwoValue() * 1000) +
                getExistingMetricValid().getTotalMeasurementOne()) /
                (double) getExistingMetricValid().getNumberOfItems();
    }

    private static int getExistingMetricValidUpdatedTwoValue() {
        return 4;
    }

    public static MeasurementModel getExistingMetricUnitOfMeasureUpdatedToImperial() {
        return new MeasurementModel(
                MeasurementSubtype.IMPERIAL_MASS,
                getExistingMetricValid().getNumberOfItems(),
                getExistingMetricValid().getConversionFactor(),
                getExistingMetricValid().getTotalMeasurementOne(),
                getExistingMetricValid().getTotalMeasurementTwo(),
                getExistingMetricValid().getItemMeasurementOne(),
                getExistingMetricValid().getItemMeasurementTwo(),
                getExistingMetricValid().getItemBaseUnits()
        );
    }

    public static MeasurementResult getExistingMetricUnitOfMeasureUpdatedToImperialResult() {
        return new MeasurementResult(
                getExistingMetricUnitOfMeasureUpdatedToImperialResultModel(),
                MeasurementResult.ResultStatus.INVALID_MEASUREMENT
        );
    }

    private static MeasurementModel getExistingMetricUnitOfMeasureUpdatedToImperialResultModel() {
        return new MeasurementModel(
                MeasurementSubtype.IMPERIAL_MASS,
                getExistingMetricUnitOfMeasureUpdatedToImperial().getNumberOfItems(),
                getExistingMetricUnitOfMeasureUpdatedToImperial().getConversionFactor(),
                getEmptyModel().getTotalMeasurementOne(),
                getEmptyModel().getTotalMeasurementTwo(),
                getEmptyModel().getItemMeasurementOne(),
                getEmptyModel().getItemMeasurementTwo(),
                getEmptyModel().getItemBaseUnits()
        );
    }

    public static MeasurementModel getExistingMetricInvalidConversionFactor() {
        return new MeasurementModel(
                getExistingMetricValid().getSubtype(),
                getExistingMetricValid().getNumberOfItems(),
                UnitOfMeasureConstants.MAX_CONVERSION_FACTOR + .01,
                getExistingMetricValid().getTotalMeasurementOne(),
                getExistingMetricValid().getTotalMeasurementTwo(),
                getExistingMetricValid().getItemMeasurementOne(),
                getExistingMetricValid().getItemMeasurementTwo(),
                getExistingMetricValid().getItemBaseUnits()
        );
    }

    public static MeasurementResult getExistingMetricInvalidConversionFactorResult() {
        return new MeasurementResult(
                getExistingMetricValid(),
                MeasurementResult.ResultStatus.INVALID_CONVERSION_FACTOR
        );
    }
}
