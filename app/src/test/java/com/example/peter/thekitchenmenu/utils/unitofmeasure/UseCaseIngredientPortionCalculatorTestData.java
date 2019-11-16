package com.example.peter.thekitchenmenu.utils.unitofmeasure;

import com.example.peter.thekitchenmenu.domain.usecase.MeasurementResult;
import com.example.peter.thekitchenmenu.domain.usecase.UseCaseIngredientPortionCalculator;
import com.example.peter.thekitchenmenu.testdata.MeasurementModelTestData;

public class UseCaseIngredientPortionCalculatorTestData {

    public static MeasurementResult getResultInvalidEmptyFourPortionsSet() {
        return new MeasurementResult(
                MeasurementModelTestData.getInvalidEmptyFourPortionsSet(),
                UseCaseIngredientPortionCalculator.ResultStatus.INVALID_MEASUREMENT);
    }

    public static MeasurementResult getResultNewInvalidTotalMeasurementOne() {
        return new MeasurementResult(
                MeasurementModelTestData.getNewInvalidTotalMeasurementOne(),
                UseCaseIngredientPortionCalculator.ResultStatus.INVALID_TOTAL_MEASUREMENT_ONE
        );
    }

    public static MeasurementResult getResultNewValidTotalMeasurementOne() {
        return new MeasurementResult(
                MeasurementModelTestData.getNewValidTotalMeasurementOne(),
                UseCaseIngredientPortionCalculator.ResultStatus.RESULT_OK
        );
    }

    public static MeasurementResult getResultNewInvalidTotalMeasurementTwo() {
        return new MeasurementResult(
                MeasurementModelTestData.getNewInvalidTotalMeasurementTwo(),
                UseCaseIngredientPortionCalculator.ResultStatus.INVALID_TOTAL_MEASUREMENT_TWO
        );
    }

    public static MeasurementResult getResultNewValidTotalMeasurementTwo() {
        return new MeasurementResult(
                MeasurementModelTestData.getNewValidTotalMeasurementTwo(),
                UseCaseIngredientPortionCalculator.ResultStatus.RESULT_OK
        );
    }

    public static MeasurementResult getResultNewInvalidUnitOfMeasureChangedImperialSpoon() {
        return new MeasurementResult(
                MeasurementModelTestData.getNewInvalidUnitOfMeasureChangedImperialSpoon(),
                UseCaseIngredientPortionCalculator.ResultStatus.INVALID_MEASUREMENT
        );
    }

    public static MeasurementResult getResultNewInvalidUnitOfMeasureChangedMetricMass() {
        return new MeasurementResult(
                MeasurementModelTestData.getNewInvalidUnitOfMeasureChangedMetricMass(),
                UseCaseIngredientPortionCalculator.ResultStatus.INVALID_MEASUREMENT
        );
    }

    public static MeasurementResult getNewValidHalfImperialSpoonUnitOneUpdatedResult() {
        return new MeasurementResult(
                MeasurementModelTestData.getNewValidHalfImperialSpoonUnitOneUpdated(),
                UseCaseIngredientPortionCalculator.ResultStatus.RESULT_OK
        );
    }

    public static MeasurementResult getResultNewInvalidConversionFactor() {
        return new MeasurementResult(
                MeasurementModelTestData.getNewInvalidConversionFactor(),
                UseCaseIngredientPortionCalculator.ResultStatus.INVALID_CONVERSION_FACTOR
        );
    }

    public static MeasurementResult getResultNewValidImperialSpoonWithConversionFactor() {
        return new MeasurementResult(
                MeasurementModelTestData.getNewValidImperialSpoonWithConversionFactor(),
                UseCaseIngredientPortionCalculator.ResultStatus.RESULT_OK
        );
    }

    public static MeasurementResult getResultExistingValidMetric() {
        return new MeasurementResult(
                MeasurementModelTestData.getExistingValidMetric(),
                UseCaseIngredientPortionCalculator.ResultStatus.RESULT_OK
        );
    }

    public static MeasurementResult getExistingMetricInvalidTotalOneResult() {
        return new MeasurementResult(
                MeasurementModelTestData.getExistingMetricInvalidTotalOne(),
                UseCaseIngredientPortionCalculator.ResultStatus.INVALID_TOTAL_MEASUREMENT_ONE
        );
    }

    public static MeasurementResult getExistingInvalidTotalTwoResult() {
        return new MeasurementResult(
                MeasurementModelTestData.getExistingMetricInvalidTotalTwo(),
                UseCaseIngredientPortionCalculator.ResultStatus.INVALID_TOTAL_MEASUREMENT_TWO
        );
    }

    public static MeasurementResult getExistingMetricValidTwoUpdatedResult() {
        return new MeasurementResult(
                MeasurementModelTestData.getExistingMetricValidTwoUpdated(),
                UseCaseIngredientPortionCalculator.ResultStatus.RESULT_OK
        );
    }

    public static MeasurementResult getExistingMetricUnitOfMeasureUpdatedToImperialResult() {
        return new MeasurementResult(
                MeasurementModelTestData.getExistingMetricUnitOfMeasureUpdatedToImperial(),
                UseCaseIngredientPortionCalculator.ResultStatus.INVALID_MEASUREMENT
        );
    }

    public static MeasurementResult getExistingMetricInvalidConversionFactorResult() {
        return new MeasurementResult(
                MeasurementModelTestData.getExistingImperialSpoonInvalidConversionFactor(),
                UseCaseIngredientPortionCalculator.ResultStatus.INVALID_CONVERSION_FACTOR
        );
    }
}
