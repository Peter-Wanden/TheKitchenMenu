package com.example.peter.thekitchenmenu.utils.unitofmeasure;

import com.example.peter.thekitchenmenu.domain.usecase.MeasurementResult;
import com.example.peter.thekitchenmenu.domain.usecase.UseCaseIngredientPortionCalculator;
import com.example.peter.thekitchenmenu.testdata.TestDataMeasurementModel;

public class UseCaseIngredientPortionCalculatorTestData {

    public static MeasurementResult getResultInvalidEmptyFourPortionsSet() {
        return new MeasurementResult(
                TestDataMeasurementModel.getInvalidEmptyFourPortionsSet(),
                UseCaseIngredientPortionCalculator.ResultStatus.INVALID_MEASUREMENT);
    }

    public static MeasurementResult getResultNewInvalidTotalMeasurementOne() {
        return new MeasurementResult(
                TestDataMeasurementModel.getNewInvalidTotalUnitOne(),
                UseCaseIngredientPortionCalculator.ResultStatus.INVALID_TOTAL_UNIT_ONE
        );
    }

    public static MeasurementResult getResultNewValidTotalMeasurementOne() {
        return new MeasurementResult(
                TestDataMeasurementModel.getNewValidTotalMeasurementOne(),
                UseCaseIngredientPortionCalculator.ResultStatus.RESULT_OK
        );
    }

    public static MeasurementResult getResultNewInvalidTotalMeasurementTwo() {
        return new MeasurementResult(
                TestDataMeasurementModel.getNewInvalidTotalMeasurementTwo(),
                UseCaseIngredientPortionCalculator.ResultStatus.INVALID_TOTAL_UNIT_TWO
        );
    }

    public static MeasurementResult getResultNewValidTotalMeasurementTwo() {
        return new MeasurementResult(
                TestDataMeasurementModel.getNewValidTotalMeasurementTwo(),
                UseCaseIngredientPortionCalculator.ResultStatus.RESULT_OK
        );
    }

    public static MeasurementResult getResultNewInvalidUnitOfMeasureChangedImperialSpoon() {
        return new MeasurementResult(
                TestDataMeasurementModel.getNewInvalidUnitOfMeasureChangedImperialSpoon(),
                UseCaseIngredientPortionCalculator.ResultStatus.INVALID_MEASUREMENT
        );
    }

    public static MeasurementResult getResultNewInvalidUnitOfMeasureChangedMetricMass() {
        return new MeasurementResult(
                TestDataMeasurementModel.getNewInvalidUnitOfMeasureChangedMetricMass(),
                UseCaseIngredientPortionCalculator.ResultStatus.INVALID_MEASUREMENT
        );
    }

    public static MeasurementResult getNewValidHalfImperialSpoonUnitOneUpdatedResult() {
        return new MeasurementResult(
                TestDataMeasurementModel.getNewValidHalfImperialSpoonUnitOneUpdated(),
                UseCaseIngredientPortionCalculator.ResultStatus.RESULT_OK
        );
    }

    public static MeasurementResult getResultNewInvalidConversionFactor() {
        return new MeasurementResult(
                TestDataMeasurementModel.getNewInvalidConversionFactor(),
                UseCaseIngredientPortionCalculator.ResultStatus.INVALID_CONVERSION_FACTOR
        );
    }

    public static MeasurementResult getResultNewValidImperialSpoonWithConversionFactor() {
        return new MeasurementResult(
                TestDataMeasurementModel.getNewValidImperialSpoonWithConversionFactor(),
                UseCaseIngredientPortionCalculator.ResultStatus.RESULT_OK
        );
    }

    public static MeasurementResult getResultExistingValidMetric() {
        return new MeasurementResult(
                TestDataMeasurementModel.getExistingValidMetric(),
                UseCaseIngredientPortionCalculator.ResultStatus.RESULT_OK
        );
    }

    public static MeasurementResult getExistingMetricInvalidTotalOneResult() {
        return new MeasurementResult(
                TestDataMeasurementModel.getExistingMetricInvalidTotalOne(),
                UseCaseIngredientPortionCalculator.ResultStatus.INVALID_TOTAL_UNIT_ONE
        );
    }

    public static MeasurementResult getExistingInvalidTotalTwoResult() {
        return new MeasurementResult(
                TestDataMeasurementModel.getExistingMetricInvalidTotalTwo(),
                UseCaseIngredientPortionCalculator.ResultStatus.INVALID_TOTAL_UNIT_TWO
        );
    }

    public static MeasurementResult getExistingMetricValidTwoUpdatedResult() {
        return new MeasurementResult(
                TestDataMeasurementModel.getExistingMetricValidTwoUpdated(),
                UseCaseIngredientPortionCalculator.ResultStatus.RESULT_OK
        );
    }

    public static MeasurementResult getExistingMetricUnitOfMeasureUpdatedToImperialResult() {
        return new MeasurementResult(
                TestDataMeasurementModel.getExistingMetricUnitOfMeasureUpdatedToImperial(),
                UseCaseIngredientPortionCalculator.ResultStatus.INVALID_MEASUREMENT
        );
    }

    public static MeasurementResult getExistingMetricInvalidConversionFactorResult() {
        return new MeasurementResult(
                TestDataMeasurementModel.getExistingImperialSpoonInvalidConversionFactor(),
                UseCaseIngredientPortionCalculator.ResultStatus.INVALID_CONVERSION_FACTOR
        );
    }
}
