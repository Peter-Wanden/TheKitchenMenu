package com.example.peter.thekitchenmenu.ui.detail.recipe.recipeIngredientEditor;

import android.content.res.Resources;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;

import com.example.peter.thekitchenmenu.R;
import com.example.peter.thekitchenmenu.commonmocks.UseCaseSchedulerMock;
import com.example.peter.thekitchenmenu.data.entity.IngredientEntity;
import com.example.peter.thekitchenmenu.data.entity.RecipeEntity;
import com.example.peter.thekitchenmenu.data.entity.RecipeIngredientQuantityEntity;
import com.example.peter.thekitchenmenu.domain.usecase.UseCaseConversionFactorStatus;
import com.example.peter.thekitchenmenu.domain.usecase.UseCaseHandler;
import com.example.peter.thekitchenmenu.testdata.TestDataIngredientEntity;
import com.example.peter.thekitchenmenu.testdata.TestDataRecipeEntity;
import com.example.peter.thekitchenmenu.testdata.TestDataRecipeIngredientQuantityEntity;
import com.example.peter.thekitchenmenu.ui.detail.recipe.recipeingredienteditor.RecipeIngredientMeasurementViewModel;
import com.example.peter.thekitchenmenu.utils.NumberFormatter;
import com.example.peter.thekitchenmenu.domain.usecase.MeasurementResult;
import com.example.peter.thekitchenmenu.domain.unitofmeasureentities.UnitOfMeasureConstants;
import com.example.peter.thekitchenmenu.domain.usecase.UseCasePortionCalculator;
import com.example.peter.thekitchenmenu.utils.unitofmeasure.UseCaseIngredientPortionCalculatorTestData;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class RecipeIngredientMeasurementViewModelTest {

    // region constants ----------------------------------------------------------------------------
    private static final String NUMBER_FORMAT_EXCEPTION_ERROR =
            "Please enter numbers only.";
    private static final String CONVERSION_FACTOR_ERROR_MESSAGE =
            "Please enter a number between n1 and n2";

    private RecipeEntity RECIPE_VALID_NEW = TestDataRecipeEntity.getNewValid();

    private IngredientEntity INGREDIENT_NEW_VALID_NAME_DESCRIPTION =
            TestDataIngredientEntity.getNewValidNameValidDescription();

    private MeasurementResult MEASUREMENT_NEW_EMPTY_INVALID =
            UseCaseIngredientPortionCalculatorTestData.getResultInvalidEmptyFourPortionsSet();

    private MeasurementResult MEASUREMENT_NEW_INVALID_ONE =
            UseCaseIngredientPortionCalculatorTestData.getResultNewInvalidTotalMeasurementOne();

    private MeasurementResult MEASUREMENT_NEW_VALID_ONE =
            UseCaseIngredientPortionCalculatorTestData.getResultNewValidTotalMeasurementOne();

    private MeasurementResult MEASUREMENT_NEW_INVALID_TWO =
            UseCaseIngredientPortionCalculatorTestData.getResultNewInvalidTotalMeasurementTwo();

    private MeasurementResult MEASUREMENT_NEW_VALID_TWO =
            UseCaseIngredientPortionCalculatorTestData.getResultNewValidTotalMeasurementTwo();

    private MeasurementResult MEASUREMENT_NEW_INVALID_UNIT_OF_MEASURE_SPOON =
            UseCaseIngredientPortionCalculatorTestData.
                    getResultNewInvalidUnitOfMeasureChangedImperialSpoon();

    private MeasurementResult MEASUREMENT_NEW_INVALID_CONVERSION_FACTOR =
            UseCaseIngredientPortionCalculatorTestData.getResultNewInvalidConversionFactor();

    private MeasurementResult MEASUREMENT_NEW_VALID_IMPERIAL_SPOON_WITH_CONVERSION_FACTOR =
            UseCaseIngredientPortionCalculatorTestData.
                    getResultNewValidImperialSpoonWithConversionFactor();

    private RecipeIngredientQuantityEntity QUANTITY_EXISTING_VALID_METRIC =
            TestDataRecipeIngredientQuantityEntity.getExistingValidMetric();

    private MeasurementResult MEASUREMENT_EXISTING_VALID_METRIC =
            UseCaseIngredientPortionCalculatorTestData.getResultExistingValidMetric();

    // endregion constants -------------------------------------------------------------------------

    // region helper fields ------------------------------------------------------------------------
    @Rule
    public InstantTaskExecutorRule instantExecutorRule = new InstantTaskExecutorRule();
    @Mock
    private UseCaseHandler handler;
    @Mock
    private Resources resourcesMock;
    @Mock
    UseCasePortionCalculator useCasePortionCalculatorMock;
    @Mock
    UseCaseConversionFactorStatus useCaseConversionFactorMock;
    @Captor
    ArgumentCaptor<String> recipeIdCaptor;
    @Captor
    ArgumentCaptor<String> ingredientIdCaptor;
    @Mock
    NumberFormatter numberFormatterMock;
    // endregion helper fields ---------------------------------------------------------------------

    private RecipeIngredientMeasurementViewModel SUT;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        setupResources();

        handler = new UseCaseHandler(new UseCaseSchedulerMock());

        SUT = new RecipeIngredientMeasurementViewModel(
                handler,
                useCasePortionCalculatorMock,
                useCaseConversionFactorMock,
                resourcesMock,
                numberFormatterMock
        );
    }

    @Test
    public void startRecipeIdIngredientId_correctIdsSentToUseCase() {
        // Arrange
        String recipeId = RECIPE_VALID_NEW.getId();
        String ingredientId = INGREDIENT_NEW_VALID_NAME_DESCRIPTION.getId();
        // Act
        SUT.start(recipeId, ingredientId);
        // Assert
        // todo - create a callback capture
        verify(useCasePortionCalculatorMock).start(recipeIdCaptor.capture(), ingredientIdCaptor.capture());
        assertEquals(RECIPE_VALID_NEW.getId(), recipeIdCaptor.getValue());
        assertEquals(INGREDIENT_NEW_VALID_NAME_DESCRIPTION.getId(), ingredientIdCaptor.getValue());
    }
//
//    @Test
//    public void startRecipeIdIngredientId_emptyModelNotSentToUseCase() {
//        // Arrange
//        // Act
//        SUT.start(RECIPE_VALID_NEW.getId(), INGREDIENT_NEW_VALID_NAME_DESCRIPTION.getId());
//        // Assert
//        verify(useCasePortionCalculatorMock).start(recipeIdCaptor.capture(), ingredientIdCaptor.capture());
//        SUT.useCasePortionResult(MEASUREMENT_NEW_EMPTY_INVALID);
//        verify(useCasePortionCalculatorMock, never()).processModel(anyObject());
//    }
//
//    @Test
//    public void startRecipeIdIngredientId_emptyModelReturned_valuesSetToDisplay() {
//        // Arrange
//        MeasurementModel model = MEASUREMENT_NEW_EMPTY_INVALID.getModel();
//        when(numberFormatterMock.formatDecimalForDisplay(eq(model.getConversionFactor()))).
//                thenReturn(String.valueOf(model.getConversionFactor()));
//        // Act
//        SUT.start(RECIPE_VALID_NEW.getId(), INGREDIENT_NEW_VALID_NAME_DESCRIPTION.getId());
//        // Assert
//        verify(useCasePortionCalculatorMock).start(recipeIdCaptor.capture(), ingredientIdCaptor.capture());
//        SUT.useCasePortionResult(MEASUREMENT_NEW_EMPTY_INVALID);
//        // Assert
//
//        assertEquals(model.getSubtype(), SUT.getSubtype());
//        assertEquals(String.valueOf(model.getConversionFactor()), SUT.getConversionFactor());
//        assertNull(SUT.getConversionFactorErrorMessage());
//        assertFalse(SUT.isConversionFactorEnabled());
//    }
//
//    @Test
//    public void startRecipeIdIngredientId_invalidMeasurementOneReturned_errorMessageShown() {
//        // Arrange
//        // Act
//        SUT.start(RECIPE_VALID_NEW.getId(), INGREDIENT_NEW_VALID_NAME_DESCRIPTION.getId());
//        // Assert
//        verify(useCasePortionCalculatorMock).start(recipeIdCaptor.capture(), ingredientIdCaptor.capture());
//        SUT.useCasePortionResult(MEASUREMENT_NEW_INVALID_ONE);
//
//        assertEquals("Tablespoons and/or teaspoons need to have a value between 0.1 tsp and 666 Tbsp",
//                SUT.getUnitOneErrorMessage());
//    }
//
//    @Test
//    public void startRecipeIdIngredientId_measurementOneValidReturned_valuesSetToDisplay() {
//        // Arrange
//        double expectedMeasurement = MEASUREMENT_NEW_VALID_ONE.getModel().
//                getTotalUnitOne();
//        when(numberFormatterMock.formatDecimalForDisplay(eq(expectedMeasurement))).
//                thenReturn(String.valueOf(expectedMeasurement));
//        String expectedMeasurementOne = String.valueOf(expectedMeasurement);
//        // Act
//        SUT.start(RECIPE_VALID_NEW.getId(), INGREDIENT_NEW_VALID_NAME_DESCRIPTION.getId());
//        verify(useCasePortionCalculatorMock).start(
//                recipeIdCaptor.capture(), ingredientIdCaptor.capture());
//        SUT.useCasePortionResult(MEASUREMENT_NEW_VALID_ONE);
//        // Assert
//        String actualMeasurementOne = SUT.getUnitOne();
//        assertEquals(expectedMeasurementOne, actualMeasurementOne);
//    }
//
//    @Test
//    public void startRecipeIdIngredientId_measurementTwoInvalidReturned_errorMessageShown() {
//        // Arrange
//        String expectedError = "Tablespoons and/or teaspoons need to have a value between 0.1 tsp and 666 Tbsp";
//        // Act
//        SUT.start(RECIPE_VALID_NEW.getId(), INGREDIENT_NEW_VALID_NAME_DESCRIPTION.getId());
//        verify(useCasePortionCalculatorMock).start(recipeIdCaptor.capture(), ingredientIdCaptor.capture());
//        SUT.useCasePortionResult(MEASUREMENT_NEW_INVALID_TWO);
//        // Assert
//        String actualError = SUT.getUnitTwoErrorMessage();
//        assertEquals(expectedError, actualError);
//    }
//
//    @Test
//    public void startRecipeIdIngredientId_measurementTwoValidReturned_valueSetToDisplay() {
//        // Arrange
//        int expectedMeasurement = MEASUREMENT_NEW_VALID_TWO.getModel().getTotalUnitTwo();
//        when(numberFormatterMock.formatIntegerForDisplay(eq(expectedMeasurement))).
//                thenReturn(String.valueOf(expectedMeasurement));
//        String expectedResult = String.valueOf(expectedMeasurement);
//        // Act
//        SUT.start(RECIPE_VALID_NEW.getId(), INGREDIENT_NEW_VALID_NAME_DESCRIPTION.getId());
//        verify(useCasePortionCalculatorMock).start(recipeIdCaptor.capture(), ingredientIdCaptor.capture());
//        SUT.useCasePortionResult(MEASUREMENT_NEW_VALID_TWO);
//        // Assert
//        String actualResult = SUT.getUnitTwo();
//        assertEquals(expectedResult, actualResult);
//    }
//
//    @Test
//    public void startRecipeIdIngredientId_unitOfMeasureChangedToSpoon_subtypeUpdated() {
//        // Arrange
//        MeasurementSubtype expectedSubType = MEASUREMENT_NEW_INVALID_UNIT_OF_MEASURE_SPOON.
//                getModel().getSubtype();
//        // Act
//        SUT.start(RECIPE_VALID_NEW.getId(), INGREDIENT_NEW_VALID_NAME_DESCRIPTION.getId());
//        verify(useCasePortionCalculatorMock).start(recipeIdCaptor.capture(), ingredientIdCaptor.capture());
//        SUT.useCasePortionResult(MEASUREMENT_NEW_INVALID_UNIT_OF_MEASURE_SPOON);
//        // Assert
//        MeasurementSubtype actualSubType = SUT.getSubtype();
//        assertEquals(expectedSubType, actualSubType);
//    }
//
//    @Test
//    public void startRecipeIdIngredientId_invalidConversionFactor_errorShown() {
//        // Arrange
//        // Act
//        SUT.start(RECIPE_VALID_NEW.getId(), INGREDIENT_NEW_VALID_NAME_DESCRIPTION.getId());
//        verify(useCasePortionCalculatorMock).start(recipeIdCaptor.capture(), ingredientIdCaptor.capture());
//        SUT.useCasePortionResult(MEASUREMENT_NEW_INVALID_CONVERSION_FACTOR);
//        // Assert
//        String actualErrorMessage = SUT.getConversionFactorErrorMessage();
//        assertEquals(CONVERSION_FACTOR_ERROR_MESSAGE, actualErrorMessage);
//    }
//
//    @Test
//    public void startRecipeIdIngredientId_imperialSpoonWithConversionFactor_conversionFactorEnabledTrue() {
//        // Arrange
//        // Act
//        SUT.start(RECIPE_VALID_NEW.getId(), INGREDIENT_NEW_VALID_NAME_DESCRIPTION.getId());
//        verify(useCasePortionCalculatorMock).start(recipeIdCaptor.capture(), ingredientIdCaptor.capture());
//        SUT.useCasePortionResult(MEASUREMENT_NEW_VALID_IMPERIAL_SPOON_WITH_CONVERSION_FACTOR);
//        // Assert
//        assertTrue(SUT.isConversionFactorEnabled());
//    }
//
//    // startRecipeIdIngredientId_measurementOneSetToHalfTeaspoon_RESULT_OK
//    // startRecipeIdIngredientId_
//
//    @Test
//    public void startRecipeIngredientId_existingValuesSetToDisplay() {
//        // Arrange
//        MeasurementModel model = MEASUREMENT_EXISTING_VALID_METRIC.getModel();
//        MeasurementSubtype expectedSubtype = model.getSubtype();
//        String expectedConversionFactor = String.valueOf(model.getConversionFactor());
//        String expectedMeasurementOne = String.valueOf(model.getTotalUnitOne());
//        String expectedMeasurementTwo = String.valueOf(model.getTotalUnitTwo());
//
//        when(numberFormatterMock.formatDecimalForDisplay(model.getConversionFactor())).
//                thenReturn(expectedConversionFactor);
//        when(numberFormatterMock.formatDecimalForDisplay(model.getTotalUnitOne())).
//                thenReturn(expectedMeasurementOne);
//        when(numberFormatterMock.formatIntegerForDisplay(model.getTotalUnitTwo())).
//                thenReturn(expectedMeasurementTwo);
//        // Act
//        SUT.start(QUANTITY_EXISTING_VALID_METRIC.getId());
//        // Assert
//        verify(useCasePortionCalculatorMock).start(eq(QUANTITY_EXISTING_VALID_METRIC.getId()));
//        SUT.useCasePortionResult(MEASUREMENT_EXISTING_VALID_METRIC);
//
//        assertEquals(expectedSubtype, SUT.getSubtype());
//        assertEquals(expectedConversionFactor, SUT.getConversionFactor());
//        assertEquals(expectedMeasurementOne, SUT.getUnitOne());
//        assertEquals(expectedMeasurementTwo, SUT.getUnitTwo());
//    }

    // region helper methods -----------------------------------------------------------------------
    private void setupResources() {
        when(resourcesMock.getString(eq(R.string.number_format_exception))).
                thenReturn(NUMBER_FORMAT_EXCEPTION_ERROR);
        when(resourcesMock.getString(R.string.conversion_factor_error_message,
                UnitOfMeasureConstants.MIN_CONVERSION_FACTOR,
                UnitOfMeasureConstants.MAX_CONVERSION_FACTOR)).
                thenReturn(CONVERSION_FACTOR_ERROR_MESSAGE);
    }
    // endregion helper methods --------------------------------------------------------------------

    // region helper classes -----------------------------------------------------------------------
    // endregion helper classes --------------------------------------------------------------------
}
