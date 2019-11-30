package com.example.peter.thekitchenmenu.ui.detail.recipe.recipeIngredientEditor;

import android.content.res.Resources;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;

import com.example.peter.thekitchenmenu.commonmocks.UseCaseSchedulerMock;
import com.example.peter.thekitchenmenu.data.entity.IngredientEntity;
import com.example.peter.thekitchenmenu.data.entity.RecipeIngredientQuantityEntity;
import com.example.peter.thekitchenmenu.data.entity.RecipePortionsEntity;
import com.example.peter.thekitchenmenu.data.repository.DataSource;
import com.example.peter.thekitchenmenu.data.repository.RepositoryIngredient;
import com.example.peter.thekitchenmenu.data.repository.RepositoryRecipeIngredient;
import com.example.peter.thekitchenmenu.data.repository.RepositoryRecipePortions;
import com.example.peter.thekitchenmenu.domain.entity.model.MeasurementModel;
import com.example.peter.thekitchenmenu.domain.entity.unitofmeasure.MeasurementSubtype;
import com.example.peter.thekitchenmenu.domain.usecase.conversionfactorstatus.UseCaseConversionFactorStatus;
import com.example.peter.thekitchenmenu.domain.usecase.UseCaseHandler;
import com.example.peter.thekitchenmenu.domain.usecase.recipeportioncalculator.UseCasePortionCalculatorRequest;
import com.example.peter.thekitchenmenu.domain.usecase.recipeportioncalculator.UseCasePortionCalculatorResponse;
import com.example.peter.thekitchenmenu.testdata.TestDataIngredientEntity;
import com.example.peter.thekitchenmenu.testdata.TestDataRecipeIngredientQuantityEntity;
import com.example.peter.thekitchenmenu.testdata.TestDataRecipePortionsEntity;
import com.example.peter.thekitchenmenu.testdata.TestDataUseCasePortionCalculatorRequestResponse;
import com.example.peter.thekitchenmenu.ui.detail.common.MeasurementErrorMessageMaker;
import com.example.peter.thekitchenmenu.ui.detail.recipe.recipeingredienteditor.RecipeIngredientMeasurementViewModel;
import com.example.peter.thekitchenmenu.ui.utils.NumberFormatter;
import com.example.peter.thekitchenmenu.domain.usecase.recipeportioncalculator.UseCasePortionCalculator;
import com.example.peter.thekitchenmenu.utils.TimeProvider;
import com.example.peter.thekitchenmenu.utils.UniqueIdProvider;

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
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class RecipeIngredientMeasurementViewModelTest {

    // region constants ----------------------------------------------------------------------------
    // region - ERROR MESSAGES
    private static final String ERROR_MESSAGE = "errorMessage";
    // endregion - ERROR MESSAGES

    // region - USE CASE PORTION REQUEST/RESPONSE TEST DATA
    private UseCasePortionCalculatorRequest REQUEST_EMPTY_FOUR_PORTIONS =
            TestDataUseCasePortionCalculatorRequestResponse.getRequestEmptyFourPortions();
    private UseCasePortionCalculatorResponse RESPONSE_EMPTY_FOUR_PORTIONS =
            TestDataUseCasePortionCalculatorRequestResponse.getResponseEmptyFourPortions();

    private UseCasePortionCalculatorRequest REQUEST_INVALID_TOTAL_UNIT_ONE =
            TestDataUseCasePortionCalculatorRequestResponse.getRequestInvalidTotalUnitOne();

    private UseCasePortionCalculatorRequest REQUEST_VALID_UNIT_ONE =
            TestDataUseCasePortionCalculatorRequestResponse.getRequestValidTotalUnitOne();
    private UseCasePortionCalculatorResponse RESPONSE_VALID_UNIT_ONE =
            TestDataUseCasePortionCalculatorRequestResponse.getResponseValidTotalUnitOne();

    private UseCasePortionCalculatorRequest REQUEST_INVALID_UNIT_TWO =
            TestDataUseCasePortionCalculatorRequestResponse.getRequestInvalidTotalUnitTwo();

    private UseCasePortionCalculatorRequest REQUEST_VALID_UNIT_TWO =
            TestDataUseCasePortionCalculatorRequestResponse.getRequestValidTotalUnitTwo();
    private UseCasePortionCalculatorResponse RESPONSE_VALID_UNIT_TWO =
            TestDataUseCasePortionCalculatorRequestResponse.getResponseValidTotalUnitTwo();

    private UseCasePortionCalculatorRequest REQUEST_UNIT_OF_MEASURE_IMPERIAL_SPOON =
            TestDataUseCasePortionCalculatorRequestResponse.
                    getRequestUnitOfMeasureChangeImperialSpoon();

    private UseCasePortionCalculatorRequest REQUEST_INVALID_CONVERSION_FACTOR =
            TestDataUseCasePortionCalculatorRequestResponse.
                    getRequestNewValidImperialSpoonInvalidConversionFactor();

    private UseCasePortionCalculatorRequest REQUEST_VALID_CONVERSION_FACTOR =
            TestDataUseCasePortionCalculatorRequestResponse.
                    getRequestNewValidImperialSpoonValidConversionFactor();

    private UseCasePortionCalculatorRequest REQUEST_EXISTING_IMPERIAL_SPOON =
            TestDataUseCasePortionCalculatorRequestResponse.getRequestExistingValidImperialSpoon();
    private UseCasePortionCalculatorResponse RESPONSE_EXISTING_IMPERIAL_SPOON =
            TestDataUseCasePortionCalculatorRequestResponse.getResponseExistingImperialSpoon();
    // endregion - USE CASE PORTION REQUEST/RESPONSE TEST DATA

    // region - INGREDIENT TEST DATA
    private IngredientEntity INGREDIENT_NEW_VALID_NAME_DESCRIPTION =
            TestDataIngredientEntity.getNewValidNameValidDescription();

    private IngredientEntity INGREDIENT_EXISTING_VALID_NAME_DESCRIPTION =
            TestDataIngredientEntity.getExistingValidNameValidDescriptionNoConversionFactor();
    // endregion - INGREDIENT TEST DATA

    // region - PORTIONS TEST DATA
    private RecipePortionsEntity PORTIONS_NEW_VALID_FOUR =
            TestDataRecipePortionsEntity.getNewValidFourPortions();

    private RecipePortionsEntity PORTIONS_EXISTING_VALID_NINE =
            TestDataRecipePortionsEntity.getExistingValidNinePortions();
    // endregion - PORTIONS TEST DATA

    // region - RECIPE INGREDIENT TEST DATA
    private RecipeIngredientQuantityEntity QUANTITY_NEW_INVALID =
            TestDataRecipeIngredientQuantityEntity.getNewInvalid();

    private RecipeIngredientQuantityEntity QUANTITY_NEW_VALID_METRIC =
            TestDataRecipeIngredientQuantityEntity.getNewValidMetric();

    private RecipeIngredientQuantityEntity QUANTITY_EXISTING_VALID_METRIC =
            TestDataRecipeIngredientQuantityEntity.getExistingValidMetric();

    private RecipeIngredientQuantityEntity QUANTITY_EXISTING_VALID_IMPERIAL_SPOON =
            TestDataRecipeIngredientQuantityEntity.getExistingValidImperialTwoSpoons();
    // endregion - RECIPE INGREDIENT TEST DATA

    // endregion constants -------------------------------------------------------------------------

    // region helper fields ------------------------------------------------------------------------
    @Rule
    public InstantTaskExecutorRule instantExecutorRule = new InstantTaskExecutorRule();
    @Mock
    Resources resourcesMock;
    @Mock
    RepositoryRecipePortions repoPortionsMock;
    @Captor
    ArgumentCaptor<DataSource.GetEntityCallback<RecipePortionsEntity>>
            getPortionsCallbackCaptor;
    @Mock
    RepositoryRecipeIngredient repoRecipeIngredientMock;
    @Captor
    ArgumentCaptor<DataSource.GetEntityCallback<RecipeIngredientQuantityEntity>>
            getRecipeIngredientCallbackCaptor;
    @Mock
    RepositoryIngredient repoIngredientMock;
    @Captor
    ArgumentCaptor<DataSource.GetEntityCallback<IngredientEntity>>
            getIngredientCallbackCaptor;
    @Mock
    UniqueIdProvider idProviderMock;
    @Mock
    TimeProvider timeProviderMock;
    @Mock
    NumberFormatter numberFormatterMock;
    @Mock
    MeasurementErrorMessageMaker errorMessageMakerMock;
    // endregion helper fields ---------------------------------------------------------------------

    private RecipeIngredientMeasurementViewModel SUT;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        setupResources();

        SUT = givenViewModel();
    }

    private RecipeIngredientMeasurementViewModel givenViewModel() {
        UseCaseHandler useCaseHandler = new UseCaseHandler(new UseCaseSchedulerMock());
        UseCasePortionCalculator portionCalculator = new UseCasePortionCalculator(
                repoPortionsMock,
                repoRecipeIngredientMock,
                repoIngredientMock,
                idProviderMock,
                timeProviderMock
        );
        UseCaseConversionFactorStatus conversionFactorStatus = new UseCaseConversionFactorStatus(
                repoIngredientMock
        );
        return new RecipeIngredientMeasurementViewModel(
                useCaseHandler,
                portionCalculator,
                conversionFactorStatus,
                resourcesMock,
                numberFormatterMock,
                errorMessageMakerMock
        );
    }

    @Test
    public void startRecipeIdIngredientId_noConversionFactor_conversionFactorFieldsNotShown() {
        // Arrange
        String recipeId = REQUEST_EMPTY_FOUR_PORTIONS.getRecipeId();
        String ingredientId = REQUEST_EMPTY_FOUR_PORTIONS.getIngredientId();
        double conversionFactor = REQUEST_EMPTY_FOUR_PORTIONS.getModel().getConversionFactor();
        // UseCasePortion interactions
        when(idProviderMock.getUId()).thenReturn(QUANTITY_NEW_INVALID.getId());
        when(numberFormatterMock.formatDecimalForDisplay(eq(conversionFactor))).
                thenReturn(String.valueOf(conversionFactor));
        // Act
        SUT.start(recipeId, ingredientId);
        // Assert
        verifyRepoIngredientCalledReturnNewValidNameValidDescription();
        verifyRepoPortionsCalledReturnNewValidFourPortions();

        assertFalse(SUT.isShowConversionFactorEditableFields());
        assertFalse(SUT.isShowConversionFactorUneditableFields());
    }

    @Test
    public void startRecipeIdIngredientId_emptyModelReturned_valuesSetToDisplay() {
        // Arrange
        String recipeId = REQUEST_EMPTY_FOUR_PORTIONS.getRecipeId();
        String ingredientId = REQUEST_EMPTY_FOUR_PORTIONS.getIngredientId();
        MeasurementModel model = RESPONSE_EMPTY_FOUR_PORTIONS.getModel();
        when(numberFormatterMock.formatDecimalForDisplay(eq(model.getConversionFactor()))).
                thenReturn(String.valueOf(model.getConversionFactor()));
        when(idProviderMock.getUId()).thenReturn(QUANTITY_NEW_INVALID.getId());
        // Act
        SUT.start(recipeId, ingredientId);
        // Assert
        verifyRepoIngredientCalledReturnNewValidNameValidDescription();
        verifyRepoPortionsCalledReturnNewValidFourPortions();

        assertEquals(model.getSubtype(), SUT.getSubtype());
        assertEquals(String.valueOf(model.getConversionFactor()), SUT.getConversionFactor());
        assertNull(SUT.getConversionFactorErrorMessage());
        assertFalse(SUT.isConversionFactorEnabled());
    }

    @Test
    public void startRecipeIdIngredientId_invalidUnitOne_errorMessageShown() {
        // Arrange
        MeasurementModel model = REQUEST_INVALID_TOTAL_UNIT_ONE.getModel();
        String recipeId = REQUEST_INVALID_TOTAL_UNIT_ONE.getRecipeId();
        String ingredientId = REQUEST_INVALID_TOTAL_UNIT_ONE.getIngredientId();
        when(idProviderMock.getUId()).thenReturn(QUANTITY_NEW_INVALID.getId());

        SUT.start(recipeId, ingredientId);
        verifyRepoIngredientCalledReturnNewValidNameValidDescription();
        verifyRepoPortionsCalledReturnNewValidFourPortions();
        // Act
        SUT.setUnitOne(String.valueOf(model.getTotalUnitOne()));
        // Assert
        assertEquals(ERROR_MESSAGE, SUT.getUnitOneErrorMessage());
    }

    @Test
    public void startRecipeIdIngredientId_validUnitOne_valuesSetToDisplay() {
        // Arrange
        String recipeId = REQUEST_VALID_UNIT_ONE.getRecipeId();
        String ingredientId = REQUEST_VALID_UNIT_ONE.getIngredientId();

        double postedUnitOneDecimal = REQUEST_VALID_UNIT_ONE.getModel().getTotalUnitOne();
        String postedUnitOne = String.valueOf(postedUnitOneDecimal);
        double expectedUnitOneDecimal = RESPONSE_VALID_UNIT_ONE.getModel().getTotalUnitOne();
        String expectedUnitOne = String.valueOf(expectedUnitOneDecimal);
        when(numberFormatterMock.formatDecimalForDisplay(eq(expectedUnitOneDecimal))).
                thenReturn(String.valueOf(expectedUnitOneDecimal));

        when(idProviderMock.getUId()).thenReturn(QUANTITY_NEW_INVALID.getId());

        SUT.start(recipeId, ingredientId);
        verifyRepoIngredientCalledReturnNewValidNameValidDescription();
        verifyRepoPortionsCalledReturnNewValidFourPortions();
        // Act
        SUT.setUnitOne(postedUnitOne);
        // Assert
        String actualMeasurementOne = SUT.getUnitOne();
        assertEquals(expectedUnitOne, actualMeasurementOne);
    }

    @Test
    public void startRecipeIdIngredientId_invalidUnitTwo_errorMessageShown() {
        // Arrange
        MeasurementModel model = REQUEST_INVALID_UNIT_TWO.getModel();
        String recipeId = REQUEST_INVALID_UNIT_TWO.getRecipeId();
        String ingredientId = REQUEST_INVALID_UNIT_TWO.getIngredientId();
        when(idProviderMock.getUId()).thenReturn(QUANTITY_NEW_INVALID.getId());
        SUT.start(recipeId, ingredientId);
        verifyRepoIngredientCalledReturnNewValidNameValidDescription();
        verifyRepoPortionsCalledReturnNewValidFourPortions();
        // Act
        SUT.setUnitTwo(String.valueOf(model.getTotalUnitTwo()));
        // Assert
        String actualError = SUT.getUnitTwoErrorMessage();
        assertEquals(ERROR_MESSAGE, actualError);
    }

    @Test
    public void startRecipeIdIngredientId_validUnitTwo_valueSetToDisplay() {
        // Arrange
        String recipeId = REQUEST_VALID_UNIT_TWO.getRecipeId();
        String ingredientId = REQUEST_VALID_UNIT_TWO.getIngredientId();

        int postedUnitTwoInt = REQUEST_VALID_UNIT_TWO.getModel().getTotalUnitTwo();
        String postedUnitTwo = String.valueOf(postedUnitTwoInt);
        int expectedUnitTwoInt = RESPONSE_VALID_UNIT_TWO.getModel().getTotalUnitTwo();
        String expectedUnitTwo = String.valueOf(expectedUnitTwoInt);

        when(numberFormatterMock.formatIntegerForDisplay(eq(expectedUnitTwoInt))).
                thenReturn(String.valueOf(expectedUnitTwoInt));

        when(idProviderMock.getUId()).thenReturn(QUANTITY_NEW_VALID_METRIC.getId());

        SUT.start(recipeId, ingredientId);
        verifyRepoIngredientCalledReturnNewValidNameValidDescription();
        verifyRepoPortionsCalledReturnNewValidFourPortions();
        // Act
        SUT.setUnitTwo(postedUnitTwo);
        // Assert
        String actualResult = SUT.getUnitTwo();
        assertEquals(expectedUnitTwo, actualResult);
    }

    @Test
    public void startRecipeIdIngredientId_unitOfMeasureChangedToSpoon_subtypeUpdated() {
        // Arrange
        String recipeId = REQUEST_UNIT_OF_MEASURE_IMPERIAL_SPOON.getRecipeId();
        String ingredientId = REQUEST_UNIT_OF_MEASURE_IMPERIAL_SPOON.getIngredientId();

        MeasurementSubtype postedSubtype = REQUEST_UNIT_OF_MEASURE_IMPERIAL_SPOON.getModel().
                getSubtype();
        when(idProviderMock.getUId()).thenReturn(QUANTITY_NEW_VALID_METRIC.getId());
        SUT.start(recipeId, ingredientId);
        verifyRepoIngredientCalledReturnNewValidNameValidDescription();
        verifyRepoPortionsCalledReturnNewValidFourPortions();
        // Act
        SUT.setSubtype(postedSubtype);
        // Assert
        MeasurementSubtype actualSubType = SUT.getSubtype();
        assertEquals(postedSubtype, actualSubType);
    }

    @Test
    public void startRecipeIdIngredientId_invalidConversionFactor_errorShown() {
        // Arrange
        String recipeId = REQUEST_INVALID_CONVERSION_FACTOR.getRecipeId();
        String ingredientId = REQUEST_INVALID_CONVERSION_FACTOR.getIngredientId();
        when(idProviderMock.getUId()).thenReturn(QUANTITY_NEW_VALID_METRIC.getId());
        double conversionFactorDecimal = REQUEST_INVALID_CONVERSION_FACTOR.getModel().
                getConversionFactor();
        String conversionFactor = String.valueOf(conversionFactorDecimal);
        SUT.start(recipeId, ingredientId);
        verifyRepoIngredientCalledReturnNewValidNameValidDescription();
        verifyRepoPortionsCalledReturnNewValidFourPortions();
        // Act
        SUT.setConversionFactor(conversionFactor);
        // Assert
        String actualErrorMessage = SUT.getConversionFactorErrorMessage();
        assertEquals(ERROR_MESSAGE, actualErrorMessage);
    }

    @Test
    public void startRecipeIdIngredientId_validConversionFactor_conversionFactorEnabledTrue() {
        // Arrange
        String recipeId = REQUEST_VALID_CONVERSION_FACTOR.getRecipeId();
        String ingredientId = REQUEST_VALID_CONVERSION_FACTOR.getIngredientId();
        when(idProviderMock.getUId()).thenReturn(QUANTITY_NEW_VALID_METRIC.getId());
        double invalidConversionFactorDecimal = REQUEST_VALID_CONVERSION_FACTOR.getModel().
                getConversionFactor();
        String conversionFactor = String.valueOf(invalidConversionFactorDecimal);
        SUT.start(recipeId, ingredientId);
        verifyRepoIngredientCalledReturnNewValidNameValidDescription();
        verifyRepoPortionsCalledReturnNewValidFourPortions();
        // Act
        SUT.setSubtype(MeasurementSubtype.IMPERIAL_SPOON);
        SUT.setConversionFactor(conversionFactor);
        // Assert
        assertTrue(SUT.isConversionFactorEnabled());
    }

    @Test
    public void startRecipeIngredientId_existingValuesSetToDisplay() {
        // Arrange
        String recipeIngredientId = REQUEST_EXISTING_IMPERIAL_SPOON.getRecipeIngredientId();
        MeasurementModel model = RESPONSE_EXISTING_IMPERIAL_SPOON.getModel();
        MeasurementSubtype expectedSubtype = model.getSubtype();
        String expectedConversionFactor = String.valueOf(model.getConversionFactor());
        String expectedMeasurementOne = String.valueOf(model.getTotalUnitOne());
        String expectedMeasurementTwo = String.valueOf(model.getTotalUnitTwo());

        when(numberFormatterMock.formatDecimalForDisplay(model.getConversionFactor())).
                thenReturn(expectedConversionFactor);
        when(numberFormatterMock.formatDecimalForDisplay(model.getTotalUnitOne())).
                thenReturn(expectedMeasurementOne);
        when(numberFormatterMock.formatIntegerForDisplay(model.getTotalUnitTwo())).
                thenReturn(expectedMeasurementTwo);
        // Act
        SUT.start(recipeIngredientId);
        verifyRepoRecipeIngredientCalledReturnExistingValidImperialSpoon();
        verifyRepoIngredientCalledAndReturnExistingValidNameDescriptionNoConversionFactor();
        verifyRepoPortionsCalledAndReturnExistingValidNinePortions();
        // Assert
        assertEquals(expectedSubtype, SUT.getSubtype());
        assertEquals(expectedConversionFactor, SUT.getConversionFactor());
        assertEquals(expectedMeasurementOne, SUT.getUnitOne());
        assertEquals(expectedMeasurementTwo, SUT.getUnitTwo());
    }

    // region helper methods -----------------------------------------------------------------------
    private void setupResources() {
        when(errorMessageMakerMock.getMeasurementErrorMessage(anyObject())).
                thenReturn(ERROR_MESSAGE);

        when(errorMessageMakerMock.getConversionFactorErrorMessage()).
                thenReturn(ERROR_MESSAGE);
    }

    private void verifyRepoIngredientCalledReturnNewValidNameValidDescription() {
        verify(repoIngredientMock).getById(eq(
                INGREDIENT_NEW_VALID_NAME_DESCRIPTION.getId()),
                getIngredientCallbackCaptor.capture());
        getIngredientCallbackCaptor.getValue().onEntityLoaded(
                INGREDIENT_NEW_VALID_NAME_DESCRIPTION);
    }

    private void verifyRepoPortionsCalledReturnNewValidFourPortions() {
        verify(repoPortionsMock).getPortionsForRecipe(eq(QUANTITY_NEW_INVALID.getRecipeId()),
                getPortionsCallbackCaptor.capture());
        getPortionsCallbackCaptor.getValue().onEntityLoaded(PORTIONS_NEW_VALID_FOUR);
    }

    private void verifyRepoRecipeIngredientCalledReturnExistingValidImperialSpoon() {
        verify(repoRecipeIngredientMock).getById(
                eq(QUANTITY_EXISTING_VALID_IMPERIAL_SPOON.getId()),
                getRecipeIngredientCallbackCaptor.capture());
        getRecipeIngredientCallbackCaptor.getValue().onEntityLoaded(
                QUANTITY_EXISTING_VALID_IMPERIAL_SPOON);
    }

    private void verifyRepoIngredientCalledAndReturnExistingValidNameDescriptionNoConversionFactor() {
        verify(repoIngredientMock).getById(
                eq(INGREDIENT_EXISTING_VALID_NAME_DESCRIPTION.getId()),
                getIngredientCallbackCaptor.capture());
        getIngredientCallbackCaptor.getValue().onEntityLoaded(
                INGREDIENT_EXISTING_VALID_NAME_DESCRIPTION);
    }

    private void verifyRepoPortionsCalledAndReturnExistingValidNinePortions() {
        verify(repoPortionsMock).getPortionsForRecipe(
                eq(QUANTITY_EXISTING_VALID_METRIC.getRecipeId()),
                getPortionsCallbackCaptor.capture());
        getPortionsCallbackCaptor.getValue().onEntityLoaded(PORTIONS_EXISTING_VALID_NINE);
    }
    // endregion helper methods --------------------------------------------------------------------

    // region helper classes -----------------------------------------------------------------------

    // endregion helper classes --------------------------------------------------------------------
}
