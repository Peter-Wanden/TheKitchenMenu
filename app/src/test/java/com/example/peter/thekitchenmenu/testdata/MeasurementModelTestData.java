package com.example.peter.thekitchenmenu.testdata;

import com.example.peter.thekitchenmenu.data.model.MeasurementModel;
import com.example.peter.thekitchenmenu.utils.unitofmeasure.MeasurementResult;
import com.example.peter.thekitchenmenu.utils.unitofmeasure.MeasurementSubtype;
import com.example.peter.thekitchenmenu.utils.unitofmeasure.UnitOfMeasure;
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
                (getNewValidMetricTotalBasUnits()  % 1000 / getEmptyModelPortions()),
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

    public static MeasurementModel getNewInvalidUnitOfMeasureChanged() {
        return new MeasurementModel(
                MeasurementSubtype.IMPERIAL_MASS,
                getEmptyModel().getNumberOfItems(),
                getEmptyModel().getConversionFactor(),
                getEmptyModel().getTotalMeasurementOne(),
                getEmptyModel().getTotalMeasurementTwo(),
                getEmptyModel().getItemMeasurementOne(),
                getEmptyModel().getItemMeasurementTwo(),
                getEmptyModel().getItemBaseUnits()
        );
    }

    public static MeasurementResult getResultNewInvalidUnitOfMeasureChanged() {
        return new MeasurementResult(
                getNewInvalidUnitOfMeasureChanged(),
                MeasurementResult.ResultStatus.INVALID_MEASUREMENT
        );
    }

    public static MeasurementModel getExistingValid() {
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

    private static int getExistingValidTotalMeasurementTwo() {
        return (int) RecipeIngredientQuantityEntityTestData.
                getExistingValidMetric().
                getItemBaseUnits() * getExistingValidNinePortions() / 1000;
    }

    private static double getExistingValidItemMeasurementOne() {
        return RecipeIngredientQuantityEntityTestData.
                getExistingValidMetric().
                getItemBaseUnits() % 1000;
    }

    private static int getExistingValidItemMeasurementTwo() {
        return (int) RecipeIngredientQuantityEntityTestData.
                getExistingValidMetric().
                getItemBaseUnits() / 1000;
    }
}
