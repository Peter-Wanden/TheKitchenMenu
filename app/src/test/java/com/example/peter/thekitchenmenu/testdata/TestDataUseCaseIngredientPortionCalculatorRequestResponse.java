package com.example.peter.thekitchenmenu.testdata;

import com.example.peter.thekitchenmenu.domain.unitofmeasureentities.UnitOfMeasureConstants;
import com.example.peter.thekitchenmenu.domain.usecase.UseCaseIngredientPortionCalculator;
import com.example.peter.thekitchenmenu.domain.usecase.UseCaseIngredientPortionCalculator.ResponseValues;
import com.example.peter.thekitchenmenu.domain.usecase.UseCaseIngredientPortionCalculator.RequestValues;

public class TestDataUseCaseIngredientPortionCalculatorRequestResponse {

    private static String NO_ID = "";

    public static RequestValues getRequestEmptyFourPortions() {
        return new RequestValues(
                TestDataRecipeEntity.getNewInvalid().getId(),
                TestDataIngredientEntity.getNewValidName().getId(),
                NO_ID,
                UnitOfMeasureConstants.DEFAULT_MEASUREMENT_MODEL);
    }
    public static ResponseValues getResponseEmptyFourPortions() {
        return new ResponseValues(TestDataMeasurementModel.getInvalidEmptyFourPortionsSet(),
                        UseCaseIngredientPortionCalculator.ResultStatus.INVALID_MEASUREMENT);
    }

    public static RequestValues getRequestInvalidTotalUnitOne() {
        return new RequestValues(
                TestDataRecipeEntity.getNewInvalid().getId(),
                TestDataIngredientEntity.getNewValidName().getId(),
                NO_ID,
                TestDataMeasurementModel.getNewInvalidTotalUnitOne());
    }
    public static ResponseValues getResponseInvalidTotalUnitOne() {
        return new ResponseValues(TestDataMeasurementModel.getNewInvalidTotalUnitOne(),
                UseCaseIngredientPortionCalculator.ResultStatus.INVALID_TOTAL_UNIT_ONE);
    }

}
