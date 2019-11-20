package com.example.peter.thekitchenmenu.utils.unitofmeasure;

import com.example.peter.thekitchenmenu.domain.usecase.MeasurementResult;
import com.example.peter.thekitchenmenu.domain.usecase.UseCasePortionCalculator;
import com.example.peter.thekitchenmenu.testdata.TestDataMeasurementModel;

public class UseCaseIngredientPortionCalculatorTestData {

    public static MeasurementResult getResultInvalidEmptyFourPortionsSet() {
        return new MeasurementResult(
                TestDataMeasurementModel.getInvalidEmptyFourPortionsSet(),
                UseCasePortionCalculator.ResultStatus.INVALID_MEASUREMENT);
    }

    public static MeasurementResult getResultNewInvalidTotalMeasurementOne() {
        return new MeasurementResult(
                TestDataMeasurementModel.getNewInvalidTotalUnitOne(),
                UseCasePortionCalculator.ResultStatus.INVALID_TOTAL_UNIT_ONE
        );
    }

    public static MeasurementResult getResultNewValidTotalMeasurementOne() {
        return new MeasurementResult(
                TestDataMeasurementModel.getNewValidTotalUnitOne(),
                UseCasePortionCalculator.ResultStatus.RESULT_OK
        );
    }

    public static MeasurementResult getResultNewInvalidTotalMeasurementTwo() {
        return new MeasurementResult(
                TestDataMeasurementModel.getNewInvalidTotalUnitTwo(),
                UseCasePortionCalculator.ResultStatus.INVALID_TOTAL_UNIT_TWO
        );
    }

    public static MeasurementResult getResultNewValidTotalMeasurementTwo() {
        return new MeasurementResult(
                TestDataMeasurementModel.getNewValidTotalUnitTwo(),
                UseCasePortionCalculator.ResultStatus.RESULT_OK
        );
    }

    public static MeasurementResult getResultNewInvalidUnitOfMeasureChangedImperialSpoon() {
        return new MeasurementResult(
                TestDataMeasurementModel.getNewInvalidUnitOfMeasureChangedImperialSpoon(),
                UseCasePortionCalculator.ResultStatus.INVALID_MEASUREMENT
        );
    }

    public static MeasurementResult getResultNewInvalidUnitOfMeasureChangedMetricMass() {
        return new MeasurementResult(
                TestDataMeasurementModel.getNewInvalidUnitOfMeasureChangedMetricMass(),
                UseCasePortionCalculator.ResultStatus.INVALID_MEASUREMENT
        );
    }

    public static MeasurementResult getNewValidHalfImperialSpoonUnitOneUpdatedResult() {
        return new MeasurementResult(
                TestDataMeasurementModel.getNewValidHalfImperialSpoonUnitOneUpdated(),
                UseCasePortionCalculator.ResultStatus.RESULT_OK
        );
    }

    public static MeasurementResult getResultNewInvalidConversionFactor() {
        return new MeasurementResult(
                TestDataMeasurementModel.getNewInvalidConversionFactor(),
                UseCasePortionCalculator.ResultStatus.INVALID_CONVERSION_FACTOR
        );
    }

    public static MeasurementResult getResultNewValidImperialSpoonWithConversionFactor() {
        return new MeasurementResult(
                TestDataMeasurementModel.getNewValidImperialSpoonWithConversionFactor(),
                UseCasePortionCalculator.ResultStatus.RESULT_OK
        );
    }

    public static MeasurementResult getResultExistingValidMetric() {
        return new MeasurementResult(
                TestDataMeasurementModel.getValidExistingMetric(),
                UseCasePortionCalculator.ResultStatus.RESULT_OK
        );
    }

    public static MeasurementResult getExistingMetricInvalidTotalOneResult() {
        return new MeasurementResult(
                TestDataMeasurementModel.getExistingMetricInvalidTotalOne(),
                UseCasePortionCalculator.ResultStatus.INVALID_TOTAL_UNIT_ONE
        );
    }

    public static MeasurementResult getExistingInvalidTotalTwoResult() {
        return new MeasurementResult(
                TestDataMeasurementModel.getExistingMetricInvalidTotalTwo(),
                UseCasePortionCalculator.ResultStatus.INVALID_TOTAL_UNIT_TWO
        );
    }

    public static MeasurementResult getExistingMetricValidTwoUpdatedResult() {
        return new MeasurementResult(
                TestDataMeasurementModel.getExistingMetricValidTwoUpdated(),
                UseCasePortionCalculator.ResultStatus.RESULT_OK
        );
    }

    public static MeasurementResult getExistingMetricUnitOfMeasureUpdatedToImperialResult() {
        return new MeasurementResult(
                TestDataMeasurementModel.getExistingMetricUnitOfMeasureUpdatedToImperial(),
                UseCasePortionCalculator.ResultStatus.INVALID_MEASUREMENT
        );
    }

    public static MeasurementResult getExistingMetricInvalidConversionFactorResult() {
        return new MeasurementResult(
                TestDataMeasurementModel.getExistingImperialSpoonInvalidConversionFactor(),
                UseCasePortionCalculator.ResultStatus.INVALID_CONVERSION_FACTOR
        );
    }
}
