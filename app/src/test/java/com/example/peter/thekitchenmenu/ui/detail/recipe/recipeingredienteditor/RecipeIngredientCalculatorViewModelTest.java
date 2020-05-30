package com.example.peter.thekitchenmenu.ui.detail.recipe.recipeingredienteditor;

import android.content.res.Resources;

import com.example.peter.thekitchenmenu.commonmocks.UseCaseSchedulerMock;
import com.example.peter.thekitchenmenu.data.repository.source.local.ingredient.datasource.IngredientEntity;
import com.example.peter.thekitchenmenu.data.repository.source.local.recipe.recipeingredient.datasource.RecipeIngredientEntity;
import com.example.peter.thekitchenmenu.data.repository.source.local.recipe.portions.datasource.RecipePortionsEntity;
import com.example.peter.thekitchenmenu.data.repository.source.local.dataadapter.PrimitiveDataSource;
import com.example.peter.thekitchenmenu.data.repository.ingredient.RepositoryIngredient;
import com.example.peter.thekitchenmenu.data.repository.recipe.RepositoryRecipeIngredient;
import com.example.peter.thekitchenmenu.data.repository.recipe.RepositoryRecipePortions;
import com.example.peter.thekitchenmenu.domain.usecase.common.UseCaseHandler;
import com.example.peter.thekitchenmenu.domain.entity.model.MeasurementModel;
import com.example.peter.thekitchenmenu.domain.entity.unitofmeasure.MeasurementSubtype;
import com.example.peter.thekitchenmenu.domain.usecase.conversionfactorstatus.ConversionFactorStatus;
import com.example.peter.thekitchenmenu.domain.usecase.recipe.recipeingredient.RecipeIngredient;
import com.example.peter.thekitchenmenu.domain.usecase.recipe.recipeingredient.RecipeIngredientRequest;
import com.example.peter.thekitchenmenu.domain.usecase.recipe.recipeingredient.RecipeIngredientResponse;
import com.example.peter.thekitchenmenu.data.repository.source.local.ingredient.datasource.TestDataIngredientEntity;
import com.example.peter.thekitchenmenu.testdata.TestDataRecipeIngredientQuantityEntity;
import com.example.peter.thekitchenmenu.data.repository.source.local.recipe.portions.datasource.TestDataRecipePortionsEntity;
import com.example.peter.thekitchenmenu.testdata.TestDataUseCasePortionCalculatorRequestResponse;
import com.example.peter.thekitchenmenu.ui.detail.common.MeasurementErrorMessageMaker;
import com.example.peter.thekitchenmenu.ui.utils.NumberFormatter;
import com.example.peter.thekitchenmenu.domain.utils.TimeProvider;
import com.example.peter.thekitchenmenu.domain.utils.UniqueIdProvider;

import org.junit.Before;
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

public class RecipeIngredientCalculatorViewModelTest {

    // region constants ----------------------------------------------------------------------------
    // region - ERROR MESSAGES
    private static final String ERROR_MESSAGE = "errorMessage";
    // endregion - ERROR MESSAGES

    // region - USE CASE PORTION REQUEST/RESPONSE TEST DATA
    private RecipeIngredientRequest REQUEST_EMPTY_FOUR_PORTIONS =
            TestDataUseCasePortionCalculatorRequestResponse.getRequestEmptyFourPortions();
    private RecipeIngredientResponse RESPONSE_EMPTY_FOUR_PORTIONS =
            TestDataUseCasePortionCalculatorRequestResponse.getResponseEmptyFourPortions();

    private RecipeIngredientRequest REQUEST_INVALID_TOTAL_UNIT_ONE =
            TestDataUseCasePortionCalculatorRequestResponse.getRequestInvalidTotalUnitOne();

    private RecipeIngredientRequest REQUEST_VALID_UNIT_ONE =
            TestDataUseCasePortionCalculatorRequestResponse.getRequestValidTotalUnitOne();
    private RecipeIngredientResponse RESPONSE_VALID_UNIT_ONE =
            TestDataUseCasePortionCalculatorRequestResponse.getResponseValidTotalUnitOne();

    private RecipeIngredientRequest REQUEST_INVALID_UNIT_TWO =
            TestDataUseCasePortionCalculatorRequestResponse.getRequestInvalidTotalUnitTwo();

    private RecipeIngredientRequest REQUEST_VALID_UNIT_TWO =
            TestDataUseCasePortionCalculatorRequestResponse.getRequestValidTotalUnitTwo();
    private RecipeIngredientResponse RESPONSE_VALID_UNIT_TWO =
            TestDataUseCasePortionCalculatorRequestResponse.getResponseValidTotalUnitTwo();

    private RecipeIngredientRequest REQUEST_UNIT_OF_MEASURE_IMPERIAL_SPOON =
            TestDataUseCasePortionCalculatorRequestResponse.
                    getRequestUnitOfMeasureChangeImperialSpoon();

    private RecipeIngredientRequest REQUEST_INVALID_CONVERSION_FACTOR =
            TestDataUseCasePortionCalculatorRequestResponse.
                    getRequestNewValidImperialSpoonInvalidConversionFactor();

    private RecipeIngredientRequest REQUEST_VALID_CONVERSION_FACTOR =
            TestDataUseCasePortionCalculatorRequestResponse.
                    getRequestNewValidImperialSpoonValidConversionFactor();

    private RecipeIngredientRequest REQUEST_EXISTING_IMPERIAL_SPOON =
            TestDataUseCasePortionCalculatorRequestResponse.getRequestExistingValidImperialSpoon();
    private RecipeIngredientResponse RESPONSE_EXISTING_IMPERIAL_SPOON =
            TestDataUseCasePortionCalculatorRequestResponse.getResponseExistingImperialSpoon();
    // endregion - USE CASE PORTION REQUEST/RESPONSE TEST DATA

    // region - INGREDIENT TEST DATA
    private IngredientEntity INGREDIENT_NEW_VALID_NAME_DESCRIPTION =
            TestDataIngredientEntity.getValidNewNameValidDescriptionValid();

    private IngredientEntity INGREDIENT_EXISTING_VALID_NAME_DESCRIPTION =
            TestDataIngredientEntity.getExistingValidDefaultConversionFactor();
    // endregion - INGREDIENT TEST DATA

    // region - PORTIONS TEST DATA
    private RecipePortionsEntity PORTIONS_NEW_VALID_FOUR =
            TestDataRecipePortionsEntity.getNewValidFourPortions();

    private RecipePortionsEntity PORTIONS_EXISTING_VALID_NINE =
            TestDataRecipePortionsEntity.getExistingValidNinePortions();
    // endregion - PORTIONS TEST DATA

    // region - RECIPE INGREDIENT TEST DATA
    private RecipeIngredientEntity QUANTITY_NEW_INVALID =
            TestDataRecipeIngredientQuantityEntity.getNewInvalid();

    private RecipeIngredientEntity QUANTITY_NEW_VALID_METRIC =
            TestDataRecipeIngredientQuantityEntity.getNewValidMetric();

    private RecipeIngredientEntity QUANTITY_EXISTING_VALID_METRIC =
            TestDataRecipeIngredientQuantityEntity.getExistingValidMetric();

    private RecipeIngredientEntity QUANTITY_EXISTING_VALID_IMPERIAL_SPOON =
            TestDataRecipeIngredientQuantityEntity.getExistingValidImperialTwoSpoons();
    // endregion - RECIPE INGREDIENT TEST DATA

    // endregion constants -------------------------------------------------------------------------

    // region helper fields ------------------------------------------------------------------------
    @Mock
    Resources resourcesMock;
    @Mock
    RepositoryRecipePortions repoPortionsMock;
    @Captor
    ArgumentCaptor<PrimitiveDataSource.GetPrimitiveCallback<RecipePortionsEntity>>
            getPortionsCallbackCaptor;
    @Mock
    RepositoryRecipeIngredient repoRecipeIngredientMock;
    @Captor
    ArgumentCaptor<PrimitiveDataSource.GetPrimitiveCallback<RecipeIngredientEntity>>
            getRecipeIngredientCallbackCaptor;
    @Mock
    RepositoryIngredient repoIngredientMock;
    @Captor
    ArgumentCaptor<PrimitiveDataSource.GetPrimitiveCallback<IngredientEntity>>
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

    private RecipeIngredientCalculatorViewModel SUT;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        setupResources();

        SUT = givenViewModel();
    }

    private RecipeIngredientCalculatorViewModel givenViewModel() {
        UseCaseHandler useCaseHandler = new UseCaseHandler(new UseCaseSchedulerMock());
        RecipeIngredient portionCalculator = new RecipeIngredient(
                repoPortionsMock,
                repoRecipeIngredientMock,
                repoIngredientMock,
                idProviderMock,
                timeProviderMock
        );
        ConversionFactorStatus conversionFactorStatus = new ConversionFactorStatus(
                repoIngredientMock
        );
        return new RecipeIngredientCalculatorViewModel(
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
        when(idProviderMock.getUId()).thenReturn(QUANTITY_NEW_INVALID.getDataId());
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
        when(idProviderMock.getUId()).thenReturn(QUANTITY_NEW_INVALID.getDataId());
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
        when(idProviderMock.getUId()).thenReturn(QUANTITY_NEW_INVALID.getDataId());

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

        when(idProviderMock.getUId()).thenReturn(QUANTITY_NEW_INVALID.getDataId());

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
        when(idProviderMock.getUId()).thenReturn(QUANTITY_NEW_INVALID.getDataId());
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

        when(idProviderMock.getUId()).thenReturn(QUANTITY_NEW_VALID_METRIC.getDataId());

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
        when(idProviderMock.getUId()).thenReturn(QUANTITY_NEW_VALID_METRIC.getDataId());
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
        when(idProviderMock.getUId()).thenReturn(QUANTITY_NEW_VALID_METRIC.getDataId());
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
        when(idProviderMock.getUId()).thenReturn(QUANTITY_NEW_VALID_METRIC.getDataId());
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
//        verify(repoIngredientMock).getByDataId(
//                eq(INGREDIENT_NEW_VALID_NAME_DESCRIPTION.getDataId()),
//                getIngredientCallbackCaptor.capture());
        getIngredientCallbackCaptor.getValue().onEntityLoaded(
                INGREDIENT_NEW_VALID_NAME_DESCRIPTION);
    }

    private void verifyRepoPortionsCalledReturnNewValidFourPortions() {
//        verify(repoPortionsMock).getAllByDomainId(
//                eq(QUANTITY_NEW_INVALID.getRecipeDomainId()),
//                getPortionsCallbackCaptor.capture());
        getPortionsCallbackCaptor.getValue().onEntityLoaded(PORTIONS_NEW_VALID_FOUR);
    }

    private void verifyRepoRecipeIngredientCalledReturnExistingValidImperialSpoon() {
//        verify(repoRecipeIngredientMock).getByDataId(
//                eq(QUANTITY_EXISTING_VALID_IMPERIAL_SPOON.getDataId()),
//                getRecipeIngredientCallbackCaptor.capture());
        getRecipeIngredientCallbackCaptor.getValue().onEntityLoaded(
                QUANTITY_EXISTING_VALID_IMPERIAL_SPOON);
    }

    private void verifyRepoIngredientCalledAndReturnExistingValidNameDescriptionNoConversionFactor() {
//        verify(repoIngredientMock).getByDataId(
//                eq(INGREDIENT_EXISTING_VALID_NAME_DESCRIPTION.getDataId()),
//                getIngredientCallbackCaptor.capture());
        getIngredientCallbackCaptor.getValue().onEntityLoaded(
                INGREDIENT_EXISTING_VALID_NAME_DESCRIPTION);
    }

    private void verifyRepoPortionsCalledAndReturnExistingValidNinePortions() {
//        verify(repoPortionsMock).getAllByDomainId(
//                eq(QUANTITY_EXISTING_VALID_METRIC.getRecipeDomainId()),
//                getPortionsCallbackCaptor.capture());
        getPortionsCallbackCaptor.getValue().onEntityLoaded(PORTIONS_EXISTING_VALID_NINE);
    }
    // endregion helper methods --------------------------------------------------------------------

    // region helper classes -----------------------------------------------------------------------

    // endregion helper classes --------------------------------------------------------------------
}
