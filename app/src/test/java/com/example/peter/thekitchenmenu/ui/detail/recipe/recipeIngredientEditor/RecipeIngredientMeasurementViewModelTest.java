package com.example.peter.thekitchenmenu.ui.detail.recipe.recipeIngredientEditor;

import android.app.Application;
import android.content.res.Resources;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;

import com.example.peter.thekitchenmenu.R;
import com.example.peter.thekitchenmenu.data.entity.IngredientEntity;
import com.example.peter.thekitchenmenu.data.entity.RecipeEntity;
import com.example.peter.thekitchenmenu.data.entity.RecipeIngredientQuantityEntity;
import com.example.peter.thekitchenmenu.data.model.MeasurementModel;
import com.example.peter.thekitchenmenu.testdata.IngredientEntityTestData;
import com.example.peter.thekitchenmenu.testdata.MeasurementModelTestData;
import com.example.peter.thekitchenmenu.testdata.RecipeEntityTestData;
import com.example.peter.thekitchenmenu.testdata.RecipeIngredientQuantityEntityTestData;
import com.example.peter.thekitchenmenu.ui.detail.recipe.recipeingredienteditor.RecipeIngredientMeasurementViewModel;
import com.example.peter.thekitchenmenu.utils.unitofmeasure.MeasurementResult;
import com.example.peter.thekitchenmenu.utils.unitofmeasure.MeasurementSubtype;
import com.example.peter.thekitchenmenu.utils.unitofmeasure.UnitOfMeasurePortionUseCase;

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
import static org.mockito.Matchers.anyObject;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class RecipeIngredientMeasurementViewModelTest {

    // region constants ----------------------------------------------------------------------------
    private static final String NUMBER_FORMAT_EXCEPTION_ERROR =
            "Please enter numbers only.";
    private static final String CONVERSION_FACTOR_ERROR_MESSAGE =
            "Please enter a number between n1 and n2";

    private RecipeEntity RECIPE_VALID_NEW = RecipeEntityTestData.getValidNew();

    private IngredientEntity INGREDIENT_NEW_VALID_NAME_DESCRIPTION =
            IngredientEntityTestData.getNewValidNameValidDescription();

    private MeasurementResult MEASUREMENT_NEW_EMPTY_INVALID =
            MeasurementModelTestData.getResultInvalidMeasurement();

    private MeasurementResult MEASUREMENT_NEW_INVALID_ONE =
            MeasurementModelTestData.getResultNewInvalidTotalMeasurementOne();

    private MeasurementResult MEASUREMENT_NEW_VALID_ONE =
            MeasurementModelTestData.getResultNewValidTotalMeasurementOne();

    private MeasurementResult MEASUREMENT_NEW_INVALID_TWO =
            MeasurementModelTestData.getResultNewInvalidTotalMeasurementTwo();

    private MeasurementResult MEASUREMENT_NEW_VALID_TWO =
            MeasurementModelTestData.getResultNewValidTotalMeasurementTwo();

    private MeasurementResult MEASUREMENT_NEW_INVALID_UNIT_OF_MEASURE_SPOON =
            MeasurementModelTestData.getResultNewInvalidUnitOfMeasureChangedImperialSpoon();

    private MeasurementResult MEASUREMENT_NEW_INVALID_CONVERSION_FACTOR =
            MeasurementModelTestData.getResultNewInvalidConversionFactor();

    private MeasurementResult MEASUREMENT_NEW_VALID_IMPERIAL_SPOON_WITH_CONVERSION_FACTOR =
            MeasurementModelTestData.getResultNewValidImperialSpoonWithConversionFactor();

    private RecipeIngredientQuantityEntity QUANTITY_EXISTING_VALID_METRIC =
            RecipeIngredientQuantityEntityTestData.getExistingValidMetric();

    private MeasurementResult MEASUREMENT_EXISTING_VALID_METRIC =
            MeasurementModelTestData.getResultExistingMetricValid();

    // endregion constants -------------------------------------------------------------------------

    // region helper fields ------------------------------------------------------------------------
    @Rule
    public InstantTaskExecutorRule instantExecutorRule = new InstantTaskExecutorRule();
    @Mock
    private Resources resourcesMock;
    @Mock
    UnitOfMeasurePortionUseCase useCaseMock;
    @Captor
    ArgumentCaptor<MeasurementModel> measurementModelCaptor;
    @Captor
    ArgumentCaptor<String> recipeIdCaptor;
    @Captor
    ArgumentCaptor<String> ingredientIdCaptor;
    @Captor
    ArgumentCaptor<String> recipeIngredientQuantityIdCaptor;
    // endregion helper fields ---------------------------------------------------------------------

    private RecipeIngredientMeasurementViewModel SUT;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        setupResources();

        SUT = new RecipeIngredientMeasurementViewModel(
                mock(Application.class),
                useCaseMock,
                resourcesMock
        );
    }

    @Test
    public void startRecipeIdIngredientId_correctIdsSentToUseCase() {
        // Arrange
        // Act
        SUT.start(RECIPE_VALID_NEW.getId(), INGREDIENT_NEW_VALID_NAME_DESCRIPTION.getId());
        // Assert
        verify(useCaseMock).start(recipeIdCaptor.capture(), ingredientIdCaptor.capture());
        assertEquals(RECIPE_VALID_NEW.getId(), recipeIdCaptor.getValue());
        assertEquals(INGREDIENT_NEW_VALID_NAME_DESCRIPTION.getId(), ingredientIdCaptor.getValue());
    }

    @Test
    public void startRecipeIdIngredientId_emptyModelNotSentToUseCase() {
        // Arrange
        // Act
        SUT.start(RECIPE_VALID_NEW.getId(), INGREDIENT_NEW_VALID_NAME_DESCRIPTION.getId());
        // Assert
        verify(useCaseMock).start(recipeIdCaptor.capture(), ingredientIdCaptor.capture());
        SUT.modelOut(MEASUREMENT_NEW_EMPTY_INVALID);
        verify(useCaseMock, never()).modelIn(anyObject());
    }

    @Test
    public void startRecipeIdIngredientId_emptyModelReturned_valuesSetToDisplay() {
        // Arrange
        // Act
        SUT.start(RECIPE_VALID_NEW.getId(), INGREDIENT_NEW_VALID_NAME_DESCRIPTION.getId());
        // Assert
        verify(useCaseMock).start(recipeIdCaptor.capture(), ingredientIdCaptor.capture());
        SUT.modelOut(MEASUREMENT_NEW_EMPTY_INVALID);
        // Assert
        MeasurementModel model = MEASUREMENT_NEW_EMPTY_INVALID.getModel();
        assertEquals(model.getSubtype(), SUT.subtype.get());
        assertEquals(String.valueOf(model.getConversionFactor()), SUT.conversionFactor.get());
        assertNull(SUT.conversionFactorErrorMessage.get());
        assertFalse(SUT.isConversionFactorEnabled.get());
    }

    @Test
    public void startRecipeIdIngredientId_invalidMeasurementOneReturned_errorMessageShown() {
        // Arrange
        // Act
        SUT.start(RECIPE_VALID_NEW.getId(), INGREDIENT_NEW_VALID_NAME_DESCRIPTION.getId());
        // Assert
        verify(useCaseMock).start(recipeIdCaptor.capture(), ingredientIdCaptor.capture());
        SUT.modelOut(MEASUREMENT_NEW_INVALID_ONE);

        assertEquals("Measurement one needs to be between x and y",
                SUT.measurementOneErrorMessage.get());
    }

    @Test
    public void startRecipeIdIngredientId_measurementOneValidReturned_valuesSetToDisplay() {
        // Arrange
        String expectedMeasurementOne = String.valueOf(MEASUREMENT_NEW_VALID_ONE.getModel().
                getItemMeasurementOne());
        // Act
        SUT.start(RECIPE_VALID_NEW.getId(), INGREDIENT_NEW_VALID_NAME_DESCRIPTION.getId());
        verify(useCaseMock).start(recipeIdCaptor.capture(), ingredientIdCaptor.capture());
        SUT.modelOut(MEASUREMENT_NEW_VALID_ONE);
        // Assert
        String actualMeasurementOne = SUT.measurementOne.get();
        assertEquals(expectedMeasurementOne, actualMeasurementOne);
    }

    @Test
    public void startRecipeIdIngredientId_measurementTwoInvalidReturned_errorMessageShown() {
        // Arrange
        String expectedError = "Measurement two needs to be between x and y";
        // Act
        SUT.start(RECIPE_VALID_NEW.getId(), INGREDIENT_NEW_VALID_NAME_DESCRIPTION.getId());
        verify(useCaseMock).start(recipeIdCaptor.capture(), ingredientIdCaptor.capture());
        SUT.modelOut(MEASUREMENT_NEW_INVALID_TWO);
        // Assert
        String actualError = SUT.measurementTwoErrorMessage.get();
        assertEquals(expectedError, actualError);
    }

    @Test
    public void startRecipeIdIngredientId_measurementTwoValidReturned_valueSetToDisplay() {
        // Arrange
        String expectedResult = String.valueOf(MEASUREMENT_NEW_VALID_TWO.getModel().
                getTotalMeasurementTwo());
        // Act
        SUT.start(RECIPE_VALID_NEW.getId(), INGREDIENT_NEW_VALID_NAME_DESCRIPTION.getId());
        verify(useCaseMock).start(recipeIdCaptor.capture(), ingredientIdCaptor.capture());
        SUT.modelOut(MEASUREMENT_NEW_VALID_TWO);
        // Assert
        String actualResult = SUT.measurementTwo.get();
        assertEquals(expectedResult, actualResult);
    }

    @Test
    public void startRecipeIdIngredientId_unitOfMeasureChangedToSpoon_subtypeUpdated() {
        // Arrange
        MeasurementSubtype expectedSubType = MEASUREMENT_NEW_INVALID_UNIT_OF_MEASURE_SPOON.
                getModel().getSubtype();
        // Act
        SUT.start(RECIPE_VALID_NEW.getId(), INGREDIENT_NEW_VALID_NAME_DESCRIPTION.getId());
        verify(useCaseMock).start(recipeIdCaptor.capture(), ingredientIdCaptor.capture());
        SUT.modelOut(MEASUREMENT_NEW_INVALID_UNIT_OF_MEASURE_SPOON);
        // Assert
        MeasurementSubtype actualSubType = SUT.subtype.get();
        assertEquals(expectedSubType, actualSubType);
    }

    @Test
    public void startRecipeIdIngredientId_invalidConversionFactor_errorShown() {
        // Arrange
        // Act
        SUT.start(RECIPE_VALID_NEW.getId(), INGREDIENT_NEW_VALID_NAME_DESCRIPTION.getId());
        verify(useCaseMock).start(recipeIdCaptor.capture(), ingredientIdCaptor.capture());
        SUT.modelOut(MEASUREMENT_NEW_INVALID_CONVERSION_FACTOR);
        // Assert
        String actualErrorMessage = SUT.conversionFactorErrorMessage.get();
        assertEquals(CONVERSION_FACTOR_ERROR_MESSAGE, actualErrorMessage);
    }

    @Test
    public void startRecipeIdIngredientId_imperialSpoonWithConversionFactor_conversionFactorEnabledTrue() {
        // Arrange
        // Act
        SUT.start(RECIPE_VALID_NEW.getId(), INGREDIENT_NEW_VALID_NAME_DESCRIPTION.getId());
        verify(useCaseMock).start(recipeIdCaptor.capture(), ingredientIdCaptor.capture());
        SUT.modelOut(MEASUREMENT_NEW_VALID_IMPERIAL_SPOON_WITH_CONVERSION_FACTOR);
        // Assert
        assertTrue(SUT.isConversionFactorEnabled.get());
    }

    // startRecipeIdIngredientId_measurementOneSetToHalfTeaspoon_RESULT_OK

    @Test
    public void startRecipeIngredientId_existingValuesSetToDisplay() {
        // Arrange
        MeasurementModel model = MEASUREMENT_EXISTING_VALID_METRIC.getModel();
        MeasurementSubtype expectedSubtype = model.getSubtype();
        String expectedConversionFactor = String.valueOf(model.getConversionFactor());
        String expectedMeasurementOne = String.valueOf(model.getTotalMeasurementOne());
        String expectedMeasurementTwo = String.valueOf(model.getTotalMeasurementTwo());

        // Act
        SUT.start(QUANTITY_EXISTING_VALID_METRIC.getId());
        // Assert
        verify(useCaseMock).start(eq(QUANTITY_EXISTING_VALID_METRIC.getId()));
        SUT.modelOut(MEASUREMENT_EXISTING_VALID_METRIC);

        assertEquals(expectedSubtype, SUT.subtype.get());
        assertEquals(expectedConversionFactor, SUT.conversionFactor.get());
        assertEquals(expectedMeasurementOne, SUT.measurementOne.get());
        assertEquals(expectedMeasurementTwo, SUT.measurementTwo.get());
    }

    // region helper methods -----------------------------------------------------------------------
    private void setupResources() {
        when(resourcesMock.getString(eq(R.string.number_format_exception))).
                thenReturn(NUMBER_FORMAT_EXCEPTION_ERROR);
        when(resourcesMock.getString(eq(R.string.conversion_factor_error_message))).
                thenReturn(CONVERSION_FACTOR_ERROR_MESSAGE);
    }
    // endregion helper methods --------------------------------------------------------------------

    // region helper classes -----------------------------------------------------------------------
    // endregion helper classes --------------------------------------------------------------------
}
