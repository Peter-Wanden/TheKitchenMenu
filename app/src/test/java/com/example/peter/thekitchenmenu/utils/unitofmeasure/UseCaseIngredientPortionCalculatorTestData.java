package com.example.peter.thekitchenmenu.utils.unitofmeasure;

import com.example.peter.thekitchenmenu.testdata.MeasurementModelTestData;

public class UseCaseIngredientPortionCalculatorTestData {

    public static MeasurementResult useCasePortionCalcGetResultInvalidEmptyFourPortionsSet() {
        return new MeasurementResult(
                MeasurementModelTestData.measurementGetInvalidEmptyFourPortionsSet(),
                UseCaseIngredientPortionCalculator.ResultStatus.INVALID_MEASUREMENT);
    }

    public static MeasurementResult getResultNewInvalidTotalMeasurementOne() {
        return new MeasurementResult(
                MeasurementModelTestData.measurementGetNewInvalidTotalMeasurementOne(),
                UseCaseIngredientPortionCalculator.ResultStatus.INVALID_TOTAL_MEASUREMENT_ONE
        );
    }

    public static MeasurementResult getResultNewValidTotalMeasurementOne() {
        return new MeasurementResult(
                MeasurementModelTestData.measurementGetNewValidTotalMeasurementOne(),
                UseCaseIngredientPortionCalculator.ResultStatus.RESULT_OK
        );
    }

    public static MeasurementResult getResultNewInvalidTotalMeasurementTwo() {
        return new MeasurementResult(
                MeasurementModelTestData.measurementGetNewInvalidTotalMeasurementTwo(),
                UseCaseIngredientPortionCalculator.ResultStatus.INVALID_TOTAL_MEASUREMENT_TWO
        );
    }

    public static MeasurementResult getResultNewValidTotalMeasurementTwo() {
        return new MeasurementResult(
                MeasurementModelTestData.measurementGetNewValidTotalMeasurementTwo(),
                UseCaseIngredientPortionCalculator.ResultStatus.RESULT_OK
        );
    }

    public static MeasurementResult getResultNewInvalidUnitOfMeasureChangedImperialSpoon() {
        return new MeasurementResult(
                MeasurementModelTestData.measurementGetNewInvalidUnitOfMeasureChangedImperialSpoon(),
                UseCaseIngredientPortionCalculator.ResultStatus.INVALID_MEASUREMENT
        );
    }

    public static MeasurementResult getNewValidHalfImperialSpoonUnitOneUpdatedResult() {
        return new MeasurementResult(
                MeasurementModelTestData.measurementGetNewValidHalfImperialSpoonUnitOneUpdated(),
                UseCaseIngredientPortionCalculator.ResultStatus.RESULT_OK
        );
    }

    public static MeasurementResult getResultNewInvalidConversionFactor() {
        return new MeasurementResult(
                MeasurementModelTestData.measurementGetNewInvalidConversionFactor(),
                UseCaseIngredientPortionCalculator.ResultStatus.INVALID_CONVERSION_FACTOR
        );
    }

    public static MeasurementResult getResultNewValidImperialSpoonWithConversionFactor() {
        return new MeasurementResult(
                MeasurementModelTestData.measurementGetNewValidImperialSpoonWithConversionFactor(),
                UseCaseIngredientPortionCalculator.ResultStatus.RESULT_OK
        );
    }

    public static MeasurementResult getResultExistingValidMetric() {
        return new MeasurementResult(
                MeasurementModelTestData.measurementGetExistingValidMetric(),
                UseCaseIngredientPortionCalculator.ResultStatus.RESULT_OK
        );
    }
}
