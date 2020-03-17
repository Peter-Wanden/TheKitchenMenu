package com.example.peter.thekitchenmenu.domain.usecase.recipeingredientcalculator;

import com.example.peter.thekitchenmenu.commonmocks.UseCaseSchedulerMock;
import com.example.peter.thekitchenmenu.data.entity.IngredientEntity;
import com.example.peter.thekitchenmenu.data.entity.RecipeIngredientEntity;
import com.example.peter.thekitchenmenu.data.entity.RecipePortionsEntity;
import com.example.peter.thekitchenmenu.data.repository.DataSource;
import com.example.peter.thekitchenmenu.data.repository.RepositoryIngredient;
import com.example.peter.thekitchenmenu.data.repository.RepositoryRecipeIngredient;
import com.example.peter.thekitchenmenu.data.repository.RepositoryRecipePortions;
import com.example.peter.thekitchenmenu.domain.usecase.UseCaseHandler;
import com.example.peter.thekitchenmenu.domain.entity.unitofmeasure.UnitOfMeasureConstants;
import com.example.peter.thekitchenmenu.domain.entity.unitofmeasure.MeasurementSubtype;
import com.example.peter.thekitchenmenu.domain.entity.unitofmeasure.UnitOfMeasure;
import com.example.peter.thekitchenmenu.domain.usecase.recipe.recipeingredientcalculator.IngredientCalculator;
import com.example.peter.thekitchenmenu.domain.usecase.recipe.recipeingredientcalculator.IngredientCalculatorRequest;
import com.example.peter.thekitchenmenu.domain.usecase.recipe.recipeingredientcalculator.IngredientCalculatorResponse;
import com.example.peter.thekitchenmenu.testdata.TestDataIngredientEntity;
import com.example.peter.thekitchenmenu.testdata.TestDataMeasurementModel;
import com.example.peter.thekitchenmenu.testdata.TestDataRecipeIngredientQuantityEntity;
import com.example.peter.thekitchenmenu.testdata.TestDataRecipePortionsEntity;
import com.example.peter.thekitchenmenu.testdata.TestDataUseCasePortionCalculatorRequestResponse;
import com.example.peter.thekitchenmenu.domain.utils.TimeProvider;
import com.example.peter.thekitchenmenu.domain.utils.UniqueIdProvider;
import com.example.peter.thekitchenmenu.domain.entity.model.MeasurementModel;
import com.example.peter.thekitchenmenu.domain.entity.model.MeasurementModelBuilder;

import org.junit.*;
import org.mockito.*;

import java.util.ArrayList;
import java.util.List;

import static com.example.peter.thekitchenmenu.domain.entity.unitofmeasure.UnitOfMeasureConstants.*;
import static com.example.peter.thekitchenmenu.domain.usecase.recipe.recipeingredientcalculator.IngredientCalculator.*;
import static org.mockito.Mockito.*;
import static org.junit.Assert.assertEquals;

/**
 * Tests the boundary between UI components and use case
 * and the boundary between the use case and data persistence
 */
public class IngredientCalculatorTest {

    // region constants ----------------------------------------------------------------------------
    private final double DELTA = 0.0001;
    private final String NO_INGREDIENT_ID = "";
    private final String NO_RECIPE_INGREDIENT_ID = "";
    private final String NO_RECIPE_ID = "";

    private List<IngredientCalculatorResponse> responses = new ArrayList<>();

    // region - RECIPE INGREDIENT QUANTITY TEST DATA
    private RecipeIngredientEntity QUANTITY_NEW_INVALID =
            TestDataRecipeIngredientQuantityEntity.getNewInvalid();

    private RecipeIngredientEntity QUANTITY_NEW_VALID_METRIC =
            TestDataRecipeIngredientQuantityEntity.getNewValidMetric();

    private RecipeIngredientEntity QUANTITY_EXISTING_VALID_METRIC =
            TestDataRecipeIngredientQuantityEntity.getExistingValidMetric();

    private RecipeIngredientEntity QUANTITY_EXISTING_VALID_IMPERIAL_SPOON =
            TestDataRecipeIngredientQuantityEntity.getExistingValidImperialTwoSpoons();

    private RecipeIngredientEntity QUANTITY_NEW_VALID_MAX_MASS_DIV_FOUR_PORTIONS =
            TestDataRecipeIngredientQuantityEntity.getNewValidMetricMaxMassDivFourPortions();
    // endregion - RECIPE INGREDIENT QUANTITY TEST DATA

    // region - RECIPE PORTIONS TEST DATA
    private RecipePortionsEntity PORTIONS_NEW_VALID_FOUR =
            TestDataRecipePortionsEntity.getNewValidFourPortions();

    private RecipePortionsEntity PORTIONS_EXISTING_VALID_NINE =
            TestDataRecipePortionsEntity.getExistingValidNinePortions();
    // endregion - RECIPE PORTIONS TEST DATA

    // region - INGREDIENT TEST DATA
    private IngredientEntity INGREDIENT_NEW_VALID_NAME =
            TestDataIngredientEntity.getNewValidName();

    private IngredientEntity INGREDIENT_NEW_VALID_NAME_MAX_CONVERSION_FACTOR =
            TestDataIngredientEntity.getNewValidNameMaxConversionFactor();

    private IngredientEntity INGREDIENT_EXISTING_VALID_NAME_DESCRIPTION =
            TestDataIngredientEntity.getExistingValidNameValidDescriptionNoConversionFactor();

    private IngredientEntity INGREDIENT_NEW_VALID_NAME_DESCRIPTION =
            TestDataIngredientEntity.getNewValidNameValidDescription();
    // endregion - INGREDIENT TEST DATA

    // region - REQUEST & RESPONSE PAIRS TEST DATA
    private IngredientCalculatorRequest REQUEST_NEW_EMPTY_FOUR_PORTIONS =
            TestDataUseCasePortionCalculatorRequestResponse.
                    getRequestEmptyFourPortions();
    private IngredientCalculatorResponse RESPONSE_NEW_EMPTY_FOUR_PORTIONS =
            TestDataUseCasePortionCalculatorRequestResponse.
                    getResponseEmptyFourPortions();

    private IngredientCalculatorRequest REQUEST_NEW_INVALID_UNIT_ONE =
            TestDataUseCasePortionCalculatorRequestResponse.
                    getRequestInvalidTotalUnitOne();
    private IngredientCalculatorResponse RESPONSE_NEW_INVALID_UNIT_ONE =
            TestDataUseCasePortionCalculatorRequestResponse.
                    getResponseInvalidTotalUnitOne();

    private IngredientCalculatorRequest REQUEST_NEW_VALID_UNIT_ONE =
            TestDataUseCasePortionCalculatorRequestResponse.
                    getRequestValidTotalUnitOne();
    private IngredientCalculatorResponse RESPONSE_NEW_VALID_UNIT_ONE =
            TestDataUseCasePortionCalculatorRequestResponse.
                    getResponseValidTotalUnitOne();

    private IngredientCalculatorRequest REQUEST_NEW_INVALID_UNIT_TWO =
            TestDataUseCasePortionCalculatorRequestResponse.
                    getRequestInvalidTotalUnitTwo();
    private IngredientCalculatorResponse RESPONSE_NEW_INVALID_UNIT_TWO =
            TestDataUseCasePortionCalculatorRequestResponse.
                    getResponseInvalidTotalUnitTwo();

    private IngredientCalculatorRequest REQUEST_NEW_VALID_UNIT_TWO =
            TestDataUseCasePortionCalculatorRequestResponse.
                    getRequestValidTotalUnitTwo();
    private IngredientCalculatorResponse RESPONSE_NEW_VALID_UNIT_TWO =
            TestDataUseCasePortionCalculatorRequestResponse.
                    getResponseValidTotalUnitTwo();

    private IngredientCalculatorRequest REQUEST_NEW_UNIT_OF_MEASURE_CHANGE_IMPERIAL_SPOON =
            TestDataUseCasePortionCalculatorRequestResponse.
                    getRequestUnitOfMeasureChangeImperialSpoon();
    private IngredientCalculatorResponse RESPONSE_NEW_UNIT_OF_MEASURE_CHANGE_IMPERIAL_SPOON =
            TestDataUseCasePortionCalculatorRequestResponse.
                    getResponseUnitOfMeasureChangeImperialSpoon();

    private IngredientCalculatorRequest REQUEST_NEW_VALID_IMPERIAL_SPOON_UNIT_ONE_UPDATED_HALF_SPOON =
            TestDataUseCasePortionCalculatorRequestResponse.
                    getRequestNewValidImperialSpoonUnitOneUpdatedHalfSpoon();
    private IngredientCalculatorResponse RESPONSE_NEW_VALID_IMPERIAL_SPOON_UNIT_ONE_UPDATED_HALF_SPOON =
            TestDataUseCasePortionCalculatorRequestResponse.
                    getResponseNewValidImperialSpoonUnitOneUpdatedHalfSpoon();

    private IngredientCalculatorRequest REQUEST_NEW_INVALID_IMPERIAL_SPOON_CONVERSION_FACTOR =
            TestDataUseCasePortionCalculatorRequestResponse.
                    getRequestNewValidImperialSpoonInvalidConversionFactor();
    private IngredientCalculatorResponse RESPONSE_NEW_INVALID_IMPERIAL_SPOON_CONVERSION_FACTOR =
            TestDataUseCasePortionCalculatorRequestResponse.
                    getResponseNewValidImperialSpoonInvalidConversionFactor();

    private IngredientCalculatorRequest REQUEST_NEW_VALID_IMPERIAL_SPOON_CONVERSION_FACTOR =
            TestDataUseCasePortionCalculatorRequestResponse.
                    getRequestNewValidImperialSpoonValidConversionFactor();
    private IngredientCalculatorResponse RESPONSE_NEW_VALID_IMPERIAL_SPOON_CONVERSION_FACTOR =
            TestDataUseCasePortionCalculatorRequestResponse.
                    getResponseNewValidImperialSpoonValidConversionFactor();

    private IngredientCalculatorRequest REQUEST_EXISTING_VALID_METRIC =
            TestDataUseCasePortionCalculatorRequestResponse.
                    getRequestExistingValidMetric();
    private IngredientCalculatorResponse RESPONSE_EXISTING_VALID_METRIC =
            TestDataUseCasePortionCalculatorRequestResponse.
                    getResponseExistingValidMetric();

    private IngredientCalculatorRequest REQUEST_EXISTING_INVALID_METRIC_UNIT_ONE =
            TestDataUseCasePortionCalculatorRequestResponse.
                    getRequestExistingMetricInvalidTotalUnitOne();
    private IngredientCalculatorResponse RESPONSE_EXISTING_INVALID_METRIC_UNIT_ONE =
            TestDataUseCasePortionCalculatorRequestResponse.
                    getResponseExistingMetricInvalidTotalUnitOne();

    private IngredientCalculatorRequest REQUEST_EXISTING_VALID_METRIC_UNIT_ONE =
            TestDataUseCasePortionCalculatorRequestResponse.
                    getRequestExistingMetricValidTotalUnitOne();
    private IngredientCalculatorResponse RESPONSE_EXISTING_VALID_METRIC_UNIT_ONE =
            TestDataUseCasePortionCalculatorRequestResponse.
                    getResponseExistingMetricValidTotalUnitOne();

    private IngredientCalculatorRequest REQUEST_EXISTING_VALID_METRIC_UNIT_TWO =
            TestDataUseCasePortionCalculatorRequestResponse.
                    getRequestExistingMetricValidTotalUnitTwo();
    private IngredientCalculatorResponse RESPONSE_EXISTING_VALID_METRIC_UNIT_TWO =
            TestDataUseCasePortionCalculatorRequestResponse.
                    getResponseExistingMetricValidTotalUnitTwo();

    private IngredientCalculatorRequest REQUEST_EXISTING_UNIT_OF_MEASURE_CHANGE_IMPERIAL_SPOON =
            TestDataUseCasePortionCalculatorRequestResponse.
                    getRequestExistingMetricUnitOfMeasureChangedToImperialSpoon();
    private IngredientCalculatorResponse RESPONSE_EXISTING_UNIT_OF_MEASURE_CHANGE_IMPERIAL_SPOON =
            TestDataUseCasePortionCalculatorRequestResponse.
                    getResponseExistingMetricUnitOfMeasureChangedToImperialSpoon();

    private IngredientCalculatorRequest REQUEST_EXISTING_VALID_IMPERIAL_SPOON =
            TestDataUseCasePortionCalculatorRequestResponse.
                    getRequestExistingValidImperialSpoon();
    private IngredientCalculatorResponse RESPONSE_EXISTING_VALID_IMPERIAL_SPOON =
            TestDataUseCasePortionCalculatorRequestResponse.
                    getResponseExistingImperialSpoon();

    private IngredientCalculatorRequest REQUEST_EXISTING_INVALID_IMPERIAL_SPOON_CONVERSION_FACTOR =
            TestDataUseCasePortionCalculatorRequestResponse.
                    getRequestExistingImperialSpoonInvalidConversionFactor();
    private IngredientCalculatorResponse RESPONSE_EXISTING_INVALID_IMPERIAL_SPOON_CONVERSION_FACTOR =
            TestDataUseCasePortionCalculatorRequestResponse.
                    getResponseExistingImperialSpoonInvalidConversionFactor();
    // endregion - REQUEST & RESPONSE PAIRS TEST DATA

    // endregion constants -------------------------------------------------------------------------
    // region helper fields ------------------------------------------------------------------------
    private UseCaseHandler handler;
    private IngredientCalculatorResponse actualResponse;
    @Mock
    RepositoryRecipePortions repoRecipePortionsMock;
    @Captor
    ArgumentCaptor<DataSource.GetEntityCallback<RecipePortionsEntity>>
            getRecipePortionsCallbackCaptor;
    @Mock
    RepositoryRecipeIngredient repoRecipeIngredientMock;
    @Captor
    ArgumentCaptor<DataSource.GetEntityCallback<RecipeIngredientEntity>>
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
    @Captor
    ArgumentCaptor<RecipeIngredientEntity> recipeIngredientCaptor;
    @Captor
    ArgumentCaptor<IngredientEntity> ingredientArgumentCaptor;
    // endregion helper fields ---------------------------------------------------------------------

    private IngredientCalculator SUT;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);

        handler = new UseCaseHandler(new UseCaseSchedulerMock());

        SUT = new IngredientCalculator(
                repoRecipePortionsMock,
                repoRecipeIngredientMock,
                repoIngredientMock,
                idProviderMock,
                timeProviderMock
        );
    }

    @Test
    public void startNewRecipeAndIngredientId_invalidMeasurementNotSaved() {
        // Arrange
        when(idProviderMock.getUId()).thenReturn(QUANTITY_NEW_INVALID.getId());
        // Act
        handler.execute(SUT, REQUEST_NEW_EMPTY_FOUR_PORTIONS, getResponseCallback());
        // Assert
        verifyRepoIngredientCalledReturnNewValidNameValidDescription();
        verifyRepoPortionsCalledReturnNewValidFourPortions();
        // Assert recipeIngredient not saved
        verifyNoMoreInteractions(repoRecipeIngredientMock);
    }

    @Test
    public void startNewRecipeAndIngredientId_INVALID_MEASUREMENT() {
        // Arrange
        when(idProviderMock.getUId()).thenReturn(QUANTITY_NEW_INVALID.getId());
        // Act
        handler.execute(SUT, REQUEST_NEW_EMPTY_FOUR_PORTIONS, getResponseCallback());
        // Assert
        verifyRepoIngredientCalledReturnNewValidNameValidDescription();
        verifyRepoPortionsCalledReturnNewValidFourPortions();
        assertEquals(RESPONSE_NEW_EMPTY_FOUR_PORTIONS, actualResponse);
        assertEquals(ResultStatus.INVALID_MEASUREMENT, actualResponse.getResultStatus());
    }

    @Test
    public void startNewRecipeAndIngredientId_invalidTotalUnitOneUpdated_notSaved() {
        // Arrange
        whenIdProviderReturnNewValidId();
        whenTimeProviderThenReturnNewValidTime();
        handler.execute(SUT, REQUEST_NEW_EMPTY_FOUR_PORTIONS, getResponseCallback());
        verifyRepoIngredientCalledAndReturnNewValidName();
        verifyRepoPortionsCalledAndReturnNewValidFourPortions();
        // Act
        handler.execute(SUT, REQUEST_NEW_INVALID_UNIT_ONE, getResponseCallback());
        // Assert
        verifyNoMoreInteractions(repoRecipeIngredientMock);
    }

    @Test
    public void startNewRecipeAndIngredientId_invalidTotalUnitOneUpdated_INVALID_TOTAL_UNIT_ONE() {
        // Arrange
        whenIdProviderReturnNewValidId();
        whenTimeProviderThenReturnNewValidTime();
        handler.execute(SUT, REQUEST_NEW_EMPTY_FOUR_PORTIONS, getResponseCallback());
        verifyRepoIngredientCalledAndReturnNewValidName();
        verifyRepoPortionsCalledAndReturnNewValidFourPortions();
        // Act
        handler.execute(SUT, REQUEST_NEW_INVALID_UNIT_ONE, getResponseCallback());
        // Assert
        assertEquals(RESPONSE_NEW_INVALID_UNIT_ONE, actualResponse);
        assertEquals(ResultStatus.INVALID_TOTAL_UNIT_ONE, actualResponse.getResultStatus());
    }

    @Test
    public void startNewRecipeAndIngredientId_validTotalUnitOneUpdated_saved() {
        // Arrange
        whenIdProviderReturnNewValidId();
        whenTimeProviderThenReturnNewValidTime();
        handler.execute(SUT, REQUEST_NEW_EMPTY_FOUR_PORTIONS, getResponseCallback());
        verifyRepoIngredientCalledAndReturnNewValidName();
        verifyRepoPortionsCalledAndReturnNewValidFourPortions();
        // Act
        handler.execute(SUT, REQUEST_NEW_VALID_UNIT_ONE, getResponseCallback());
        // Assert
        verify(repoRecipeIngredientMock).save(eq(QUANTITY_NEW_VALID_METRIC));
    }

    @Test
    public void startNewRecipeAndIngredientId_validTotalUnitOneUpdated_RESULT_OK() {
        // Arrange
        whenIdProviderReturnNewValidId();
        whenTimeProviderThenReturnNewValidTime();
        handler.execute(SUT, REQUEST_NEW_EMPTY_FOUR_PORTIONS, getResponseCallback());
        verifyRepoIngredientCalledAndReturnNewValidName();
        verifyRepoPortionsCalledAndReturnNewValidFourPortions();
        // Act
        handler.execute(SUT, REQUEST_NEW_VALID_UNIT_ONE, getResponseCallback());
        // Assert
        assertEquals(RESPONSE_NEW_VALID_UNIT_ONE, actualResponse);
        assertEquals(ResultStatus.RESULT_OK, actualResponse.getResultStatus());
    }

    @Test
    public void startNewRecipeAndIngredientId_invalidTotalUnitTwoUpdated_notSaved() {
        // Arrange
        whenIdProviderReturnNewValidId();
        whenTimeProviderThenReturnNewValidTime();
        handler.execute(SUT, REQUEST_NEW_EMPTY_FOUR_PORTIONS, getResponseCallback());
        verifyRepoIngredientCalledAndReturnNewValidName();
        verifyRepoPortionsCalledAndReturnNewValidFourPortions();
        // Act
        handler.execute(SUT, REQUEST_NEW_INVALID_UNIT_TWO, getResponseCallback());
        // Assert
        verifyNoMoreInteractions(repoRecipeIngredientMock);
    }

    @Test
    public void startNewRecipeAndIngredientId_invalidTotalUnitTwoUpdated_INVALID_TOTAL_UNIT_TWO() {
        // Arrange
        whenIdProviderReturnNewValidId();
        whenTimeProviderThenReturnNewValidTime();
        handler.execute(SUT, REQUEST_NEW_EMPTY_FOUR_PORTIONS, getResponseCallback());
        verifyRepoIngredientCalledAndReturnNewValidName();
        verifyRepoPortionsCalledAndReturnNewValidFourPortions();
        // Act
        handler.execute(SUT, REQUEST_NEW_INVALID_UNIT_TWO, getResponseCallback());
        // Assert
        assertEquals(RESPONSE_NEW_INVALID_UNIT_TWO, actualResponse);
        assertEquals(ResultStatus.INVALID_TOTAL_UNIT_TWO, actualResponse.getResultStatus());
    }

    @Test
    public void startNewRecipeAndIngredientId_validTotalUnitTwoUpdated_saved() {
        // Arrange
        whenIdProviderReturnNewValidId();
        whenTimeProviderThenReturnNewValidTime();
        handler.execute(SUT, REQUEST_NEW_EMPTY_FOUR_PORTIONS, getResponseCallback());
        verifyRepoIngredientCalledAndReturnNewValidName();
        verifyRepoPortionsCalledAndReturnNewValidFourPortions();
        // Act
        handler.execute(SUT, REQUEST_NEW_VALID_UNIT_TWO, getResponseCallback());
        // Assert
        verify(repoRecipeIngredientMock).save(recipeIngredientCaptor.capture());
        RecipeIngredientEntity actualResult = recipeIngredientCaptor.getValue();
        assertEquals(QUANTITY_NEW_VALID_MAX_MASS_DIV_FOUR_PORTIONS, actualResult);
    }

    @Test
    public void startNewRecipeAndIngredientId_validTotalUnitTwoUpdated_RESULT_OK() {
        // Arrange
        whenIdProviderReturnNewValidId();
        whenTimeProviderThenReturnNewValidTime();
        handler.execute(SUT, REQUEST_NEW_EMPTY_FOUR_PORTIONS, getResponseCallback());
        verifyRepoIngredientCalledAndReturnNewValidName();
        verifyRepoPortionsCalledAndReturnNewValidFourPortions();
        // Act
        handler.execute(SUT, REQUEST_NEW_VALID_UNIT_TWO, getResponseCallback());
        // Assert
        assertEquals(RESPONSE_NEW_VALID_UNIT_TWO, actualResponse);
        assertEquals(ResultStatus.RESULT_OK, actualResponse.getResultStatus());
    }

    @Test
    public void startNewRecipeAndIngredientId_unitOfMeasureChanged_INVALID_MEASUREMENT() {
        // Arrange
        whenIdProviderReturnNewValidId();
        whenTimeProviderThenReturnNewValidTime();
        handler.execute(SUT, REQUEST_NEW_EMPTY_FOUR_PORTIONS, getResponseCallback());
        verifyRepoIngredientCalledAndReturnNewValidName();
        verifyRepoPortionsCalledAndReturnNewValidFourPortions();
        // Act
        handler.execute(SUT, REQUEST_NEW_UNIT_OF_MEASURE_CHANGE_IMPERIAL_SPOON, getResponseCallback());
        // Assert
        assertEquals(2, responses.size());
        assertEquals(RESPONSE_NEW_UNIT_OF_MEASURE_CHANGE_IMPERIAL_SPOON, actualResponse);
    }

    @Test
    public void startNewRecipeAndIngredientId_sequenceUnitOfMeasureChangedTwiceWithConversionFactorChange_conversionFactorDefault() {
        // Arrange
        whenIdProviderReturnNewValidId();
        whenTimeProviderThenReturnNewValidTime();
        String recipeId = REQUEST_NEW_EMPTY_FOUR_PORTIONS.getRecipeId();
        String ingredientId = REQUEST_NEW_EMPTY_FOUR_PORTIONS.getIngredientId();
        handler.execute(SUT, REQUEST_NEW_EMPTY_FOUR_PORTIONS, getResponseCallback());
        verifyRepoIngredientCalledAndReturnNewValidName();
        verifyRepoPortionsCalledAndReturnNewValidFourPortions();
        // Act
        // first unit of measure change
        handler.execute(SUT, REQUEST_NEW_UNIT_OF_MEASURE_CHANGE_IMPERIAL_SPOON, getResponseCallback());
        // Arrange
        // Update conversion factor and return model
        MeasurementModel resultAfterUnitOfMeasureChange = actualResponse.getModel();
        MeasurementModel updatedConversionFactor = MeasurementModelBuilder.
                basedOnModel(resultAfterUnitOfMeasureChange).
                setConversionFactor(MAX_CONVERSION_FACTOR).
                build();

        IngredientCalculatorRequest requestValuesWithConversionFactor =
                new IngredientCalculatorRequest(
                        recipeId, ingredientId, NO_RECIPE_INGREDIENT_ID, updatedConversionFactor);

        // Act
        handler.execute(SUT, requestValuesWithConversionFactor, getResponseCallback());
        // Assert
        // Check conversion factor set in response
        MeasurementModel modelWithConversionFactor = actualResponse.getModel();
        assertEquals(MAX_CONVERSION_FACTOR, modelWithConversionFactor.getConversionFactor(), DELTA);
        // Arrange
        // Second unit of measure change
        MeasurementModel modelWithUnitOfMeasureChange = MeasurementModelBuilder.
                basedOnModel(modelWithConversionFactor).
                setSubtype(MeasurementSubtype.METRIC_MASS).
                build();
        IngredientCalculatorRequest requestUnitOfMeasureChange =
                new IngredientCalculatorRequest(
                        recipeId, ingredientId, NO_RECIPE_INGREDIENT_ID, modelWithUnitOfMeasureChange);
        // Act
        handler.execute(SUT, requestUnitOfMeasureChange, getResponseCallback());
        // Assert - conversion factor has reset to default
        assertEquals(UnitOfMeasureConstants.NO_CONVERSION_FACTOR,
                actualResponse.getModel().getConversionFactor(), DELTA);
    }

    @Test
    public void startNewRecipeAndIngredientId_sequenceUnitOfMeasureChangedImperialSpoonAndUpdateMeasurementOne() {
        // Arrange
        whenIdProviderReturnNewValidId();
        whenTimeProviderThenReturnNewValidTime();
        handler.execute(SUT, REQUEST_NEW_EMPTY_FOUR_PORTIONS, getResponseCallback());
        verifyRepoIngredientCalledAndReturnNewValidName();
        verifyRepoPortionsCalledAndReturnNewValidFourPortions();
        // Act
        // Change unit of measure
        handler.execute(SUT, REQUEST_NEW_UNIT_OF_MEASURE_CHANGE_IMPERIAL_SPOON, getResponseCallback());
        // Assert
        assertEquals(2, responses.size());
        assertEquals(RESPONSE_NEW_UNIT_OF_MEASURE_CHANGE_IMPERIAL_SPOON, actualResponse);
        // Act
        // Change unit one
        handler.execute(SUT, REQUEST_NEW_VALID_IMPERIAL_SPOON_UNIT_ONE_UPDATED_HALF_SPOON,
                getResponseCallback());
        // Assert
        assertEquals(3, responses.size());
        assertEquals(RESPONSE_NEW_VALID_IMPERIAL_SPOON_UNIT_ONE_UPDATED_HALF_SPOON, actualResponse);
    }

    @Test
    public void startNewRecipeAndIngredientId_invalidConversionFactor_notSaved() {
        // Arrange
        whenIdProviderReturnNewValidId();
        whenTimeProviderThenReturnNewValidTime();
        handler.execute(SUT, REQUEST_NEW_EMPTY_FOUR_PORTIONS, getResponseCallback());
        verifyRepoIngredientCalledAndReturnNewValidName();
        verifyRepoPortionsCalledAndReturnNewValidFourPortions();
        // Act
        // Change to unit of measure that supports conversion factor
        handler.execute(SUT, REQUEST_NEW_UNIT_OF_MEASURE_CHANGE_IMPERIAL_SPOON, getResponseCallback());
        // Attempt to set out of bounds conversion factor
        handler.execute(SUT, REQUEST_NEW_INVALID_IMPERIAL_SPOON_CONVERSION_FACTOR,
                getResponseCallback());
        // Assert out of bounds value is not saved
        verifyNoMoreInteractions(repoRecipeIngredientMock);
    }

    @Test
    public void startNewRecipeAndIngredientId_invalidConversionFactor_INVALID_CONVERSION_FACTOR() {
        // Arrange
        whenIdProviderReturnNewValidId();
        whenTimeProviderThenReturnNewValidTime();
        handler.execute(SUT, REQUEST_NEW_EMPTY_FOUR_PORTIONS, getResponseCallback());
        verifyRepoIngredientCalledAndReturnNewValidName();
        verifyRepoPortionsCalledAndReturnNewValidFourPortions();
        // Act
        // Change to unit of measure that supports conversion factor
        handler.execute(SUT, REQUEST_NEW_UNIT_OF_MEASURE_CHANGE_IMPERIAL_SPOON, getResponseCallback());
        // Attempt to set out of bounds conversion factor
        handler.execute(SUT, REQUEST_NEW_INVALID_IMPERIAL_SPOON_CONVERSION_FACTOR,
                getResponseCallback());
        // Assert
        assertEquals(RESPONSE_NEW_INVALID_IMPERIAL_SPOON_CONVERSION_FACTOR.getResultStatus(),
                actualResponse.getResultStatus());
    }

    @Test
    public void startNewRecipeAndIngredientId_validConversionFactor_saved() {
        // Arrange
        whenIdProviderReturnNewValidId();
        whenTimeProviderThenReturnNewValidTime();
        handler.execute(SUT, REQUEST_NEW_EMPTY_FOUR_PORTIONS, getResponseCallback());
        verifyRepoIngredientCalledAndReturnNewValidName();
        verifyRepoPortionsCalledAndReturnNewValidFourPortions();
        // Act
        handler.execute(SUT, REQUEST_NEW_UNIT_OF_MEASURE_CHANGE_IMPERIAL_SPOON, getResponseCallback());
        handler.execute(SUT, REQUEST_NEW_VALID_IMPERIAL_SPOON_CONVERSION_FACTOR, getResponseCallback());
        // Assert
        verify(repoIngredientMock).save(ingredientArgumentCaptor.capture());
        IngredientEntity actualResult = ingredientArgumentCaptor.getValue();
        assertEquals(INGREDIENT_NEW_VALID_NAME_MAX_CONVERSION_FACTOR, actualResult);
    }

    @Test
    public void startNewRecipeAndIngredientId_validConversionFactor_itemBaseUnitsUpdated() {
        whenIdProviderReturnNewValidId();
        whenTimeProviderThenReturnNewValidTime();
        handler.execute(SUT, REQUEST_NEW_EMPTY_FOUR_PORTIONS, getResponseCallback());
        verifyRepoIngredientCalledAndReturnNewValidName();
        verifyRepoPortionsCalledAndReturnNewValidFourPortions();
        // Act - Change to conversion factor enabled unit of measure
        handler.execute(SUT, REQUEST_NEW_UNIT_OF_MEASURE_CHANGE_IMPERIAL_SPOON, getResponseCallback());
        // Apply a conversion factor
        handler.execute(SUT, REQUEST_NEW_VALID_IMPERIAL_SPOON_CONVERSION_FACTOR, getResponseCallback());
        // Arrange
        MeasurementModel unitOneUpdated = MeasurementModelBuilder.basedOnModel(
                REQUEST_NEW_VALID_IMPERIAL_SPOON_CONVERSION_FACTOR.getModel()).
                setTotalUnitOne(1).build();
        IngredientCalculatorRequest requestUnitOneUpdated = new IngredientCalculatorRequest(
                REQUEST_NEW_VALID_IMPERIAL_SPOON_CONVERSION_FACTOR.getRecipeId(),
                REQUEST_NEW_VALID_IMPERIAL_SPOON_CONVERSION_FACTOR.getIngredientId(),
                NO_RECIPE_INGREDIENT_ID,
                unitOneUpdated);
        // Act - Update unit one
        handler.execute(SUT, requestUnitOneUpdated, getResponseCallback());
        // Get response from unit one updated
        IngredientCalculatorResponse responseUnitOneUpdated = actualResponse;
        // Arrange
        MeasurementModel unitTwoUpdated = MeasurementModelBuilder.basedOnModel(
                responseUnitOneUpdated.getModel()).
                setTotalUnitTwo(1).build();
        IngredientCalculatorRequest requestUnitTwoUpdated = new IngredientCalculatorRequest(
                REQUEST_NEW_VALID_IMPERIAL_SPOON_CONVERSION_FACTOR.getRecipeId(),
                REQUEST_NEW_VALID_IMPERIAL_SPOON_CONVERSION_FACTOR.getIngredientId(),
                NO_RECIPE_INGREDIENT_ID,
                unitTwoUpdated);
        // Act - update unit two
        handler.execute(SUT, requestUnitTwoUpdated, getResponseCallback());
        // Assert item base units are correct
        assertEquals(
                RESPONSE_NEW_VALID_IMPERIAL_SPOON_CONVERSION_FACTOR.getModel().getItemBaseUnits(),
                actualResponse.getModel().getItemBaseUnits(),
                DELTA);
    }

    @Test
    public void startNewRecipeAndIngredientId_validConversionFactor_RESULT_OK() {
        // Arrange
        whenIdProviderReturnNewValidId();
        whenTimeProviderThenReturnNewValidTime();
        handler.execute(SUT, REQUEST_NEW_EMPTY_FOUR_PORTIONS, getResponseCallback());
        verifyRepoIngredientCalledAndReturnNewValidName();
        verifyRepoPortionsCalledAndReturnNewValidFourPortions();
        // Act - change to conversion factor enabled unit of measure
        handler.execute(SUT, REQUEST_NEW_UNIT_OF_MEASURE_CHANGE_IMPERIAL_SPOON, getResponseCallback());
        // Apply a conversion factor
        handler.execute(SUT, REQUEST_NEW_VALID_IMPERIAL_SPOON_CONVERSION_FACTOR, getResponseCallback());
        // Arrange
        MeasurementModel unitOneUpdated = MeasurementModelBuilder.basedOnModel(
                REQUEST_NEW_VALID_IMPERIAL_SPOON_CONVERSION_FACTOR.getModel()).
                setTotalUnitOne(1).build();
        IngredientCalculatorRequest requestUnitOneUpdated = new IngredientCalculatorRequest(
                REQUEST_NEW_VALID_IMPERIAL_SPOON_CONVERSION_FACTOR.getRecipeId(),
                REQUEST_NEW_VALID_IMPERIAL_SPOON_CONVERSION_FACTOR.getIngredientId(),
                NO_RECIPE_INGREDIENT_ID,
                unitOneUpdated);
        // Act - Update unit one
        handler.execute(SUT, requestUnitOneUpdated, getResponseCallback());
        // Get response from unit one updated
        IngredientCalculatorResponse responseUnitOneUpdated = actualResponse;
        // Arrange
        MeasurementModel unitTwoUpdated = MeasurementModelBuilder.basedOnModel(
                responseUnitOneUpdated.getModel()).
                setTotalUnitTwo(1).build();
        IngredientCalculatorRequest requestUnitTwoUpdated = new IngredientCalculatorRequest(
                REQUEST_NEW_VALID_IMPERIAL_SPOON_CONVERSION_FACTOR.getRecipeId(),
                REQUEST_NEW_VALID_IMPERIAL_SPOON_CONVERSION_FACTOR.getIngredientId(),
                NO_RECIPE_INGREDIENT_ID,
                unitTwoUpdated);
        // Act - update unit two
        handler.execute(SUT, requestUnitTwoUpdated, getResponseCallback());
        // Assert
        assertEquals(ResultStatus.RESULT_OK, actualResponse.getResultStatus());
    }

    @Test
    public void startExistingRecipeIngredientId_entitiesLoaded() {
        // Arrange
        // Act
        handler.execute(SUT, REQUEST_EXISTING_VALID_METRIC, getResponseCallback());
        // Assert
        verifyRepoRecipeIngredientCalledReturnExistingValidMetric();
        verifyRepoIngredientCalledAndReturnExistingValidNameDescriptionNoConversionFactor();
        verifyRepoPortionsCalledAndReturnExistingValidNinePortions();
    }

    @Test
    public void startExistingRecipeIngredientId_RESULT_OK() {
        // Arrange
        // Act
        handler.execute(SUT, REQUEST_EXISTING_VALID_METRIC, getResponseCallback());
        // Assert
        verifyRepoRecipeIngredientCalledReturnExistingValidMetric();
        verifyRepoIngredientCalledAndReturnExistingValidNameDescriptionNoConversionFactor();
        verifyRepoPortionsCalledAndReturnExistingValidNinePortions();
        assertEquals(RESPONSE_EXISTING_VALID_METRIC, actualResponse);
        assertEquals(ResultStatus.RESULT_OK, actualResponse.getResultStatus());
    }

    @Test
    public void startExistingRecipeIngredientId_invalidTotalUnitOneUpdated_valueNotSaved() {
        // Arrange
        handler.execute(SUT, REQUEST_EXISTING_VALID_METRIC, getResponseCallback());
        verifyRepoRecipeIngredientCalledReturnExistingValidMetric();
        verifyRepoIngredientCalledAndReturnExistingValidNameDescriptionNoConversionFactor();
        verifyRepoPortionsCalledAndReturnExistingValidNinePortions();
        // Act
        handler.execute(SUT, REQUEST_EXISTING_INVALID_METRIC_UNIT_ONE, getResponseCallback());
        // Assert
        verifyNoMoreInteractions(repoRecipeIngredientMock);
    }

    @Test
    public void startExistingRecipeIngredientId_invalidTotalUnitOneUpdated_INVALID_TOTAL_UNIT_ONE() {
        // Arrange
        handler.execute(SUT, REQUEST_EXISTING_VALID_METRIC, getResponseCallback());
        verifyRepoRecipeIngredientCalledReturnExistingValidMetric();
        verifyRepoIngredientCalledAndReturnExistingValidNameDescriptionNoConversionFactor();
        verifyRepoPortionsCalledAndReturnExistingValidNinePortions();
        // Act
        handler.execute(SUT, REQUEST_EXISTING_INVALID_METRIC_UNIT_ONE, getResponseCallback());
        // Assert
        assertEquals(ResultStatus.INVALID_TOTAL_UNIT_ONE, actualResponse.getResultStatus());
    }

    @Test
    public void startExistingRecipeIngredientId_validTotalUnitOneUpdated_saved() {
        // Arrange
        String recipeId = REQUEST_EXISTING_VALID_METRIC.getRecipeId();
        String ingredientId = REQUEST_EXISTING_VALID_METRIC.getIngredientId();
        String recipeIngredientId = REQUEST_EXISTING_VALID_METRIC.getRecipeIngredientId();

        handler.execute(SUT, REQUEST_EXISTING_VALID_METRIC, getResponseCallback());
        verifyRepoRecipeIngredientCalledReturnExistingValidMetric();
        verifyRepoIngredientCalledAndReturnExistingValidNameDescriptionNoConversionFactor();
        verifyRepoPortionsCalledAndReturnExistingValidNinePortions();

        MeasurementModel model = MeasurementModelBuilder.
                basedOnModel(actualResponse.getModel()).
                setTotalUnitOne(8000).
                build();

        IngredientCalculatorRequest request = new IngredientCalculatorRequest.Builder().
                setRecipeId(recipeId).
                setIngredientId(ingredientId).
                setRecipeIngredientId(recipeIngredientId).
                setMeasurementModel(model).
                build();
        // Act
        handler.execute(SUT, request, getResponseCallback());
        // Assert
        assertEquals(RESPONSE_EXISTING_VALID_METRIC_UNIT_ONE, actualResponse);
        verify(repoRecipeIngredientMock).save(anyObject());
    }

    @Test
    public void startExistingRecipeIngredientId_validTotalUnitOneUpdated_RESULT_OK() {
        // Arrange
        String recipeId = REQUEST_EXISTING_VALID_METRIC.getRecipeId();
        String ingredientId = REQUEST_EXISTING_VALID_METRIC.getIngredientId();
        String recipeIngredientId = REQUEST_EXISTING_VALID_METRIC.getRecipeIngredientId();

        handler.execute(SUT, REQUEST_EXISTING_VALID_METRIC, getResponseCallback());
        verifyRepoRecipeIngredientCalledReturnExistingValidMetric();
        verifyRepoIngredientCalledAndReturnExistingValidNameDescriptionNoConversionFactor();
        verifyRepoPortionsCalledAndReturnExistingValidNinePortions();

        MeasurementModel model = MeasurementModelBuilder.
                basedOnModel(actualResponse.getModel()).
                setTotalUnitOne(8000).
                build();

        IngredientCalculatorRequest request = new IngredientCalculatorRequest.Builder().
                setRecipeId(recipeId).
                setIngredientId(ingredientId).
                setRecipeIngredientId(recipeIngredientId).
                setMeasurementModel(model).
                build();
        // Act
        handler.execute(SUT, request, getResponseCallback());

        // Assert
        assertEquals(RESPONSE_EXISTING_VALID_METRIC_UNIT_ONE, actualResponse);
    }

    @Test
    public void startExistingRecipeIngredientId_invalidTotalUnitTwoUpdated_valueNotSaved() {
        // Arrange
        handler.execute(SUT, REQUEST_EXISTING_VALID_METRIC, getResponseCallback());
        verifyRepoRecipeIngredientCalledReturnExistingValidMetric();
        verifyRepoIngredientCalledAndReturnExistingValidNameDescriptionNoConversionFactor();
        verifyRepoPortionsCalledAndReturnExistingValidNinePortions();
        // Act
        handler.execute(SUT, REQUEST_EXISTING_INVALID_METRIC_UNIT_ONE, getResponseCallback());
        // Assert
        verifyNoMoreInteractions(repoRecipeIngredientMock);
    }

    @Test
    public void startExistingRecipeIngredientId_invalidTotalUnitTwoUpdated_INVALID_TOTAL_UNIT_TWO() {
        // Arrange
        handler.execute(SUT, REQUEST_EXISTING_VALID_METRIC, getResponseCallback());
        verifyRepoRecipeIngredientCalledReturnExistingValidMetric();
        verifyRepoIngredientCalledAndReturnExistingValidNameDescriptionNoConversionFactor();
        verifyRepoPortionsCalledAndReturnExistingValidNinePortions();
        // Act
        handler.execute(SUT, REQUEST_EXISTING_INVALID_METRIC_UNIT_ONE, getResponseCallback());
        // Assert
        assertEquals(RESPONSE_EXISTING_INVALID_METRIC_UNIT_ONE.getResultStatus(),
                actualResponse.getResultStatus());
    }

    @Test
    public void startExistingRecipeIngredientId_validTotalUnitTwoUpdated_saved() {
        // Arrange
        handler.execute(SUT, REQUEST_EXISTING_VALID_METRIC, getResponseCallback());
        verifyRepoRecipeIngredientCalledReturnExistingValidMetric();
        verifyRepoIngredientCalledAndReturnExistingValidNameDescriptionNoConversionFactor();
        verifyRepoPortionsCalledAndReturnExistingValidNinePortions();
        // Act
        handler.execute(SUT, REQUEST_EXISTING_VALID_METRIC_UNIT_TWO, getResponseCallback());
        // Assert
        verify(repoRecipeIngredientMock).save(recipeIngredientCaptor.capture());
        RecipeIngredientEntity saveResult = recipeIngredientCaptor.getValue();
        double expectedBaseUnits = RESPONSE_EXISTING_VALID_METRIC_UNIT_TWO.
                getModel().getItemBaseUnits();
        double actualItemBseUnits = saveResult.getItemBaseUnits();
        assertEquals(expectedBaseUnits, actualItemBseUnits, DELTA);
    }

    @Test
    public void startExistingRecipeIngredientId_validTotalUnitTwoUpdated_RESULT_OK() {
        // Arrange
        handler.execute(SUT, REQUEST_EXISTING_VALID_METRIC, getResponseCallback());
        verifyRepoRecipeIngredientCalledReturnExistingValidMetric();
        verifyRepoIngredientCalledAndReturnExistingValidNameDescriptionNoConversionFactor();
        verifyRepoPortionsCalledAndReturnExistingValidNinePortions();
        // Act
        handler.execute(SUT, REQUEST_EXISTING_VALID_METRIC_UNIT_TWO, getResponseCallback());
        // Assert
        assertEquals(RESPONSE_EXISTING_VALID_METRIC_UNIT_TWO.getResultStatus(),
                actualResponse.getResultStatus());
    }

    @Test
    public void startExistingRecipeIngredientId_unitOfMeasureChanged_INVALID_MEASUREMENT() {
        // Act
        handler.execute(SUT, REQUEST_EXISTING_VALID_METRIC, getResponseCallback());
        verifyRepoRecipeIngredientCalledReturnExistingValidMetric();
        verifyRepoIngredientCalledAndReturnExistingValidNameDescriptionNoConversionFactor();
        verifyRepoPortionsCalledAndReturnExistingValidNinePortions();
        // Arrange
        handler.execute(SUT, REQUEST_EXISTING_UNIT_OF_MEASURE_CHANGE_IMPERIAL_SPOON,
                getResponseCallback());
        // Assert
        assertEquals(RESPONSE_EXISTING_UNIT_OF_MEASURE_CHANGE_IMPERIAL_SPOON.getResultStatus(),
                actualResponse.getResultStatus());
    }

    @Test
    public void startExistingRecipeIngredientId_unitOfMeasureChanged_emptyModel() {
        // Arrange
        handler.execute(SUT, REQUEST_EXISTING_VALID_METRIC, getResponseCallback());
        verifyRepoRecipeIngredientCalledReturnExistingValidMetric();
        verifyRepoIngredientCalledAndReturnExistingValidNameDescriptionNoConversionFactor();
        verifyRepoPortionsCalledAndReturnExistingValidNinePortions();
        // Act
        handler.execute(SUT, REQUEST_EXISTING_UNIT_OF_MEASURE_CHANGE_IMPERIAL_SPOON,
                getResponseCallback());
        // Assert
        assertEquals(RESPONSE_EXISTING_UNIT_OF_MEASURE_CHANGE_IMPERIAL_SPOON.getModel(),
                actualResponse.getModel());
    }

    @Test
    public void startExistingRecipeIngredientId_invalidConversionFactor_notSaved() {
        // Arrange
        // Act
        handler.execute(SUT, REQUEST_EXISTING_VALID_IMPERIAL_SPOON, getResponseCallback());
        // Assert
        verifyRepoRecipeIngredientCalledReturnExistingValidImperialSpoon();
        verifyRepoIngredientCalledAndReturnExistingValidNameDescriptionNoConversionFactor();
        verifyRepoPortionsCalledAndReturnExistingValidNinePortions();
        assertEquals(RESPONSE_EXISTING_VALID_IMPERIAL_SPOON, actualResponse);
        // Attempt to set invalid conversion factor
        handler.execute(SUT, REQUEST_EXISTING_INVALID_IMPERIAL_SPOON_CONVERSION_FACTOR,
                getResponseCallback());
        // Assert
        verifyNoMoreInteractions(repoRecipeIngredientMock);
    }

    @Test
    public void startExistingRecipeIngredientId_invalidConversionFactor_INVALID_CONVERSION_FACTOR() {
        // Arrange
        // Act
        handler.execute(SUT, REQUEST_EXISTING_VALID_IMPERIAL_SPOON, getResponseCallback());
        // Assert
        verifyRepoRecipeIngredientCalledReturnExistingValidImperialSpoon();
        verifyRepoIngredientCalledAndReturnExistingValidNameDescriptionNoConversionFactor();
        verifyRepoPortionsCalledAndReturnExistingValidNinePortions();
        assertEquals(RESPONSE_EXISTING_VALID_IMPERIAL_SPOON, actualResponse);
        // Attempt to set invalid conversion factor
        handler.execute(SUT, REQUEST_EXISTING_INVALID_IMPERIAL_SPOON_CONVERSION_FACTOR,
                getResponseCallback());
        // Assert
        assertEquals(RESPONSE_EXISTING_INVALID_IMPERIAL_SPOON_CONVERSION_FACTOR.getResultStatus(),
                actualResponse.getResultStatus());
    }

    @Test
    public void startExistingRecipeIngredientId_invalidConversionFactor_measurementValuesNotChanged() {
        // Act
        handler.execute(SUT, REQUEST_EXISTING_VALID_IMPERIAL_SPOON, getResponseCallback());
        // Assert
        verifyRepoRecipeIngredientCalledReturnExistingValidImperialSpoon();
        verifyRepoIngredientCalledAndReturnExistingValidNameDescriptionNoConversionFactor();
        verifyRepoPortionsCalledAndReturnExistingValidNinePortions();
        // Attempt to set invalid conversion factor
        handler.execute(SUT, REQUEST_EXISTING_INVALID_IMPERIAL_SPOON_CONVERSION_FACTOR,
                getResponseCallback());
        // Assert
        assertEquals(RESPONSE_EXISTING_INVALID_IMPERIAL_SPOON_CONVERSION_FACTOR, actualResponse);
    }

    @Test
    public void startExistingRecipeIngredientId_userUpdateSequence_allValuesAsExpected() {
        // Arrange
        int portions = TestDataMeasurementModel.getValidExistingMetric().getNumberOfItems();
        double numberOfTeaspoons = 2;
        double itemBaseUnits = (numberOfTeaspoons * IMPERIAL_SPOON_UNIT_ONE / portions) *
                MAX_CONVERSION_FACTOR;

        when(timeProviderMock.getCurrentTimeInMills()).thenReturn(
                QUANTITY_EXISTING_VALID_METRIC.getCreateDate(),
                INGREDIENT_EXISTING_VALID_NAME_DESCRIPTION.getLastUpdate(),
                QUANTITY_EXISTING_VALID_METRIC.getLastUpdate());

        MeasurementSubtype subtype = MeasurementSubtype.fromInt(
                QUANTITY_EXISTING_VALID_METRIC.getMeasurementSubtype());

        UnitOfMeasure unitOfMeasureStartValues = subtype.getMeasurementClass();
        unitOfMeasureStartValues.isNumberOfItemsSet(portions);
        unitOfMeasureStartValues.isItemBaseUnitsSet(QUANTITY_EXISTING_VALID_METRIC.getItemBaseUnits());

        UnitOfMeasure unitOfMeasureChangeToSpoonValues =
                MeasurementSubtype.IMPERIAL_SPOON.getMeasurementClass();
        unitOfMeasureChangeToSpoonValues.isNumberOfItemsSet(
                unitOfMeasureStartValues.getNumberOfItems());
        unitOfMeasureChangeToSpoonValues.isConversionFactorSet(
                unitOfMeasureStartValues.getConversionFactor());

        MeasurementModel measurementOneChangeFromUi = MeasurementModelBuilder.
                basedOnUnitOfMeasure(unitOfMeasureChangeToSpoonValues).
                setTotalUnitOne(numberOfTeaspoons).build();

        unitOfMeasureChangeToSpoonValues.isTotalUnitOneSet(
                measurementOneChangeFromUi.getTotalUnitOne());

        MeasurementModel expectedResultFromMeasurementOneChange =
                getMeasurementModel(unitOfMeasureChangeToSpoonValues);

        MeasurementModel conversionFactorChangeFromUi = MeasurementModelBuilder.
                basedOnModel(expectedResultFromMeasurementOneChange).
                setConversionFactor(MAX_CONVERSION_FACTOR).
                build();

        unitOfMeasureChangeToSpoonValues.isConversionFactorSet(
                conversionFactorChangeFromUi.getConversionFactor());

        // Act - RecipeRequestAbstract an existing ingredient quantity measurement
        handler.execute(SUT, REQUEST_EXISTING_VALID_METRIC, getResponseCallback());
        // verify database called and return data
        verifyRepoRecipeIngredientCalledReturnExistingValidMetric();
        verifyRepoIngredientCalledAndReturnExistingValidNameDescriptionNoConversionFactor();
        verifyRepoPortionsCalledAndReturnExistingValidNinePortions();
        // verify existing data loaded
        assertEquals(RESPONSE_EXISTING_VALID_METRIC, actualResponse);

        //** Start of simulated user interaction **//
        // user updates unit of measure
        MeasurementModel updatedUnitOfMeasureModel = MeasurementModelBuilder.
                basedOnModel(actualResponse.getModel()).
                setSubtype(MeasurementSubtype.IMPERIAL_SPOON).
                build();

        IngredientCalculatorRequest updatedUnitOfMeasureRequest =
                new IngredientCalculatorRequest(
                        NO_RECIPE_ID,
                        NO_INGREDIENT_ID,
                        REQUEST_EXISTING_VALID_METRIC.getRecipeIngredientId(),
                        updatedUnitOfMeasureModel);
        handler.execute(SUT, updatedUnitOfMeasureRequest, getResponseCallback());
        // verify expected model and result status response
        assertEquals(MeasurementSubtype.IMPERIAL_SPOON, actualResponse.getModel().getSubtype());
        assertEquals(ResultStatus.INVALID_MEASUREMENT, actualResponse.getResultStatus());
        // confirm nothing saved
        verifyNoMoreInteractions(repoRecipeIngredientMock);

        // user updates unit one
        MeasurementModel updatedUnitOneModel = MeasurementModelBuilder.
                basedOnModel(actualResponse.getModel()).
                setTotalUnitOne(numberOfTeaspoons).
                build();
        IngredientCalculatorRequest updatedUnitOneRequest = new IngredientCalculatorRequest(
                NO_RECIPE_ID,
                NO_INGREDIENT_ID,
                REQUEST_EXISTING_VALID_METRIC.getRecipeIngredientId(),
                updatedUnitOneModel
        );
        handler.execute(SUT, updatedUnitOneRequest, getResponseCallback());
        // verify expected model and result status response
        assertEquals(numberOfTeaspoons, actualResponse.getModel().getTotalUnitOne(), DELTA);
        assertEquals(ResultStatus.RESULT_OK, actualResponse.getResultStatus());
        // verify changes saved to quantity entity
        verify(repoRecipeIngredientMock).save(recipeIngredientCaptor.capture());
        RecipeIngredientEntity actualQuantityEntityAfterUnitOneChange =
                recipeIngredientCaptor.getValue();
        RecipeIngredientEntity expectedQuantityEntityUnitOneChange =
                new RecipeIngredientEntity(
                        QUANTITY_EXISTING_VALID_METRIC.getId(),
                        QUANTITY_EXISTING_VALID_METRIC.getRecipeId(),
                        QUANTITY_EXISTING_VALID_METRIC.getIngredientId(),
                        QUANTITY_EXISTING_VALID_METRIC.getProductId(),
                        expectedResultFromMeasurementOneChange.getItemBaseUnits(),
                        expectedResultFromMeasurementOneChange.getSubtype().asInt(),
                        QUANTITY_EXISTING_VALID_METRIC.getCreatedBy(),
                        QUANTITY_EXISTING_VALID_METRIC.getCreateDate(),
                        QUANTITY_EXISTING_VALID_METRIC.getLastUpdate()
                );

        assertEquals(expectedQuantityEntityUnitOneChange, actualQuantityEntityAfterUnitOneChange);

        // user updates conversion factor
        MeasurementModel updatedConversionFactorModel = MeasurementModelBuilder.
                basedOnModel(actualResponse.getModel()).
                setConversionFactor(MAX_CONVERSION_FACTOR).
                build();
        IngredientCalculatorRequest updatedConversionFactorRequest =
                new IngredientCalculatorRequest(
                        NO_RECIPE_ID,
                        NO_INGREDIENT_ID,
                        REQUEST_EXISTING_VALID_METRIC.getRecipeIngredientId(),
                        updatedConversionFactorModel
                );
        handler.execute(SUT, updatedConversionFactorRequest, getResponseCallback());

        // verify expected UI updates returned
        assertEquals(numberOfTeaspoons, actualResponse.getModel().getTotalUnitOne(), DELTA);
        assertEquals(MAX_CONVERSION_FACTOR, actualResponse.getModel().getConversionFactor(), DELTA);
        assertEquals(ResultStatus.RESULT_OK, actualResponse.getResultStatus());

        // verify changes saved to ingredient entity
        verify(repoIngredientMock).save(ingredientArgumentCaptor.capture());
        IngredientEntity actualIngredientEntity = ingredientArgumentCaptor.getValue();
        IngredientEntity expectedIngredientEntity =
                new IngredientEntity(
                        INGREDIENT_EXISTING_VALID_NAME_DESCRIPTION.getId(),
                        INGREDIENT_EXISTING_VALID_NAME_DESCRIPTION.getName(),
                        INGREDIENT_EXISTING_VALID_NAME_DESCRIPTION.getDescription(),
                        MAX_CONVERSION_FACTOR,
                        INGREDIENT_EXISTING_VALID_NAME_DESCRIPTION.getCreatedBy(),
                        INGREDIENT_EXISTING_VALID_NAME_DESCRIPTION.getCreateDate(),
                        INGREDIENT_EXISTING_VALID_NAME_DESCRIPTION.getLastUpdate()
                );
        assertEquals(expectedIngredientEntity, actualIngredientEntity);

        // verify changes saved to quantity entity
        verify(repoRecipeIngredientMock, times((2))).save(recipeIngredientCaptor.capture());
        RecipeIngredientEntity actualQuantityEntity = recipeIngredientCaptor.getValue();
        RecipeIngredientEntity expectedQuantityEntityAfterConversionFactorApplied =
                new RecipeIngredientEntity(
                        QUANTITY_EXISTING_VALID_METRIC.getId(),
                        QUANTITY_EXISTING_VALID_METRIC.getRecipeId(),
                        QUANTITY_EXISTING_VALID_METRIC.getIngredientId(),
                        QUANTITY_EXISTING_VALID_METRIC.getProductId(),
                        itemBaseUnits,
                        updatedUnitOfMeasureModel.getSubtype().asInt(),
                        QUANTITY_EXISTING_VALID_METRIC.getCreatedBy(),
                        QUANTITY_EXISTING_VALID_METRIC.getCreateDate(),
                        QUANTITY_EXISTING_VALID_METRIC.getLastUpdate()
                );
        assertEquals(expectedQuantityEntityAfterConversionFactorApplied, actualQuantityEntity);
    }

    // region helper methods -----------------------------------------------------------------------
    private Callback<IngredientCalculatorResponse> getResponseCallback() {
        return new Callback<IngredientCalculatorResponse>() {

            @Override
            public void onSuccess(IngredientCalculatorResponse response) {
                IngredientCalculatorTest.this.responses.add(response);
                IngredientCalculatorTest.this.actualResponse = response;
            }

            @Override
            public void onError(IngredientCalculatorResponse response) {
                IngredientCalculatorTest.this.responses.add(response);
                IngredientCalculatorTest.this.actualResponse = response;
            }
        };
    }

    private void verifyRepoIngredientCalledAndReturnNewValidName() {
        verify(repoIngredientMock).getById(
                eq(INGREDIENT_NEW_VALID_NAME.getId()),
                getIngredientCallbackCaptor.capture());
        getIngredientCallbackCaptor.getValue().onEntityLoaded(INGREDIENT_NEW_VALID_NAME);
    }

    private void verifyRepoIngredientCalledReturnNewValidNameValidDescription() {
        verify(repoIngredientMock).getById(eq(
                INGREDIENT_NEW_VALID_NAME_DESCRIPTION.getId()),
                getIngredientCallbackCaptor.capture());
        getIngredientCallbackCaptor.getValue().onEntityLoaded(
                INGREDIENT_NEW_VALID_NAME_DESCRIPTION);
    }

    private void verifyRepoIngredientCalledAndReturnExistingValidNameDescriptionNoConversionFactor() {
        verify(repoIngredientMock).getById(
                eq(INGREDIENT_EXISTING_VALID_NAME_DESCRIPTION.getId()),
                getIngredientCallbackCaptor.capture());
        getIngredientCallbackCaptor.getValue().onEntityLoaded(
                INGREDIENT_EXISTING_VALID_NAME_DESCRIPTION);
    }

    private void verifyRepoPortionsCalledReturnNewValidFourPortions() {
        verify(repoRecipePortionsMock).getPortionsForRecipe(
                eq(QUANTITY_NEW_INVALID.getRecipeId()),
                getRecipePortionsCallbackCaptor.capture());
        getRecipePortionsCallbackCaptor.getValue().onEntityLoaded(PORTIONS_NEW_VALID_FOUR);
    }

    private void verifyRepoPortionsCalledAndReturnNewValidFourPortions() {
        verify(repoRecipePortionsMock).getPortionsForRecipe(
                eq(QUANTITY_NEW_VALID_METRIC.getRecipeId()),
                getRecipePortionsCallbackCaptor.capture());
        getRecipePortionsCallbackCaptor.getValue().onEntityLoaded(PORTIONS_NEW_VALID_FOUR);
    }

    private void verifyRepoPortionsCalledAndReturnExistingValidNinePortions() {
        verify(repoRecipePortionsMock).getPortionsForRecipe(
                eq(QUANTITY_EXISTING_VALID_METRIC.getRecipeId()),
                getRecipePortionsCallbackCaptor.capture());
        getRecipePortionsCallbackCaptor.getValue().onEntityLoaded(PORTIONS_EXISTING_VALID_NINE);
    }

    private void verifyRepoRecipeIngredientCalledReturnExistingValidMetric() {
        verify(repoRecipeIngredientMock).getById(eq(QUANTITY_EXISTING_VALID_METRIC.getId()),
                getRecipeIngredientCallbackCaptor.capture());
        getRecipeIngredientCallbackCaptor.getValue().onEntityLoaded(QUANTITY_EXISTING_VALID_METRIC);
    }

    private void verifyRepoRecipeIngredientCalledReturnExistingValidImperialSpoon() {
        verify(repoRecipeIngredientMock).getById(
                eq(QUANTITY_EXISTING_VALID_IMPERIAL_SPOON.getId()),
                getRecipeIngredientCallbackCaptor.capture());
        getRecipeIngredientCallbackCaptor.getValue().onEntityLoaded(
                QUANTITY_EXISTING_VALID_IMPERIAL_SPOON);
    }

    private void whenIdProviderReturnNewValidId() {
        when(idProviderMock.getUId()).thenReturn(QUANTITY_NEW_VALID_METRIC.getId());
    }

    private void whenTimeProviderThenReturnNewValidTime() {
        when(timeProviderMock.getCurrentTimeInMills()).
                thenReturn(QUANTITY_NEW_VALID_METRIC.getCreateDate());
    }

    private MeasurementModel getMeasurementModel(UnitOfMeasure unitOfMeasure) {
        return MeasurementModelBuilder.basedOnUnitOfMeasure(unitOfMeasure).build();
    }
    // endregion helper methods --------------------------------------------------------------------

    // region helper classes -----------------------------------------------------------------------
    // endregion helper classes --------------------------------------------------------------------
}