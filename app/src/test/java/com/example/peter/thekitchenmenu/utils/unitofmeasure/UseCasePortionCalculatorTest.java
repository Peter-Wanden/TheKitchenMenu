package com.example.peter.thekitchenmenu.utils.unitofmeasure;

import com.example.peter.thekitchenmenu.commonmocks.UseCaseSchedulerMock;
import com.example.peter.thekitchenmenu.data.entity.IngredientEntity;
import com.example.peter.thekitchenmenu.data.entity.RecipeIngredientQuantityEntity;
import com.example.peter.thekitchenmenu.data.entity.RecipePortionsEntity;
import com.example.peter.thekitchenmenu.data.repository.DataSource;
import com.example.peter.thekitchenmenu.data.repository.RepositoryIngredient;
import com.example.peter.thekitchenmenu.data.repository.RepositoryRecipeIngredient;
import com.example.peter.thekitchenmenu.data.repository.RepositoryRecipePortions;
import com.example.peter.thekitchenmenu.domain.unitofmeasureentities.UnitOfMeasureConstants;
import com.example.peter.thekitchenmenu.domain.unitofmeasureentities.MeasurementSubtype;
import com.example.peter.thekitchenmenu.domain.unitofmeasureentities.UnitOfMeasure;
import com.example.peter.thekitchenmenu.domain.usecase.UseCase;
import com.example.peter.thekitchenmenu.domain.usecase.UseCaseHandler;
import com.example.peter.thekitchenmenu.domain.usecase.UseCasePortionCalculator;
import com.example.peter.thekitchenmenu.testdata.TestDataIngredientEntity;
import com.example.peter.thekitchenmenu.testdata.TestDataMeasurementModel;
import com.example.peter.thekitchenmenu.testdata.TestDataRecipeIngredientQuantityEntity;
import com.example.peter.thekitchenmenu.testdata.TestDataRecipePortionsEntity;
import com.example.peter.thekitchenmenu.testdata.TestDataUseCaseIngredientPortionCalculatorRequestResponse;
import com.example.peter.thekitchenmenu.utils.TimeProvider;
import com.example.peter.thekitchenmenu.utils.UniqueIdProvider;
import com.example.peter.thekitchenmenu.domain.model.MeasurementModel;
import com.example.peter.thekitchenmenu.domain.model.MeasurementModelBuilder;

import org.junit.*;
import org.mockito.*;

import java.util.ArrayList;
import java.util.List;

import static com.example.peter.thekitchenmenu.domain.unitofmeasureentities.UnitOfMeasureConstants.*;
import static com.example.peter.thekitchenmenu.domain.usecase.UseCasePortionCalculator.*;
import static com.example.peter.thekitchenmenu.domain.usecase.UseCasePortionCalculator.RequestValues;
import static com.example.peter.thekitchenmenu.domain.usecase.UseCasePortionCalculator.ResponseValues;
import static org.mockito.Mockito.*;
import static org.junit.Assert.assertEquals;

/**
 * Tests the boundary between UI components and use case
 * and the boundary between the use case and data persistence
 */
public class UseCasePortionCalculatorTest {

    // region constants ----------------------------------------------------------------------------
    private final double DELTA = 0.0001;
    private final String NO_INGREDIENT_ID = "";
    private final String NO_RECIPE_INGREDIENT_ID = "";
    private final String NO_RECIPE_ID = "";

    private List<ResponseValues> responses = new ArrayList<>();

    private RecipeIngredientQuantityEntity QUANTITY_NEW_INVALID =
            TestDataRecipeIngredientQuantityEntity.getNewInvalid();

    private RequestValues REQUEST_EMPTY_FOUR_PORTIONS =
            TestDataUseCaseIngredientPortionCalculatorRequestResponse.
                    getRequestEmptyFourPortions();
    private ResponseValues RESPONSE_EMPTY_FOUR_PORTIONS =
            TestDataUseCaseIngredientPortionCalculatorRequestResponse.
                    getResponseEmptyFourPortions();

    private RequestValues REQUEST_INVALID_UNIT_ONE =
            TestDataUseCaseIngredientPortionCalculatorRequestResponse.
                    getRequestInvalidTotalUnitOne();
    private ResponseValues RESPONSE_INVALID_UNIT_ONE =
            TestDataUseCaseIngredientPortionCalculatorRequestResponse.
                    getResponseInvalidTotalUnitOne();

    private RequestValues REQUEST_VALID_UNIT_ONE =
            TestDataUseCaseIngredientPortionCalculatorRequestResponse.
                    getRequestValidTotalUnitOne();
    private ResponseValues RESPONSE_VALID_UNIT_ONE =
            TestDataUseCaseIngredientPortionCalculatorRequestResponse.
                    getResponseValidTotalUnitOne();

    private RequestValues REQUEST_INVALID_UNIT_TWO =
            TestDataUseCaseIngredientPortionCalculatorRequestResponse.
                    getRequestInvalidTotalUnitTwo();
    private ResponseValues RESPONSE_INVALID_UNIT_TWO =
            TestDataUseCaseIngredientPortionCalculatorRequestResponse.
                    getResponseInvalidTotalUnitTwo();

    private RequestValues REQUEST_VALID_UNIT_TWO =
            TestDataUseCaseIngredientPortionCalculatorRequestResponse.
                    getRequestValidTotalUnitTwo();
    private ResponseValues RESPONSE_VALID_UNIT_TWO =
            TestDataUseCaseIngredientPortionCalculatorRequestResponse.
                    getResponseValidTotalUnitTwo();

    private RequestValues REQUEST_UNIT_OF_MEASURE_IMPERIAL_SPOON =
            TestDataUseCaseIngredientPortionCalculatorRequestResponse.
                    getRequestUnitOfMeasureChangeImperialSpoon();
    private ResponseValues RESPONSE_UNIT_OF_MEASURE_IMPERIAL_SPOON =
            TestDataUseCaseIngredientPortionCalculatorRequestResponse.
                    getResponseUnitOfMeasureChangeImperialSpoon();

    private RequestValues REQUEST_NEW_VALID_IMPERIAL_SPOON_UNIT_ONE_UPDATED_HALF_SPOON =
            TestDataUseCaseIngredientPortionCalculatorRequestResponse.
                    getRequestNewValidImperialSpoonUnitOneUpdatedHalfSpoon();
    private ResponseValues RESPONSE_NEW_VALID_IMPERIAL_SPOON_UNIT_ONE_UPDATED_HALF_SPOON =
            TestDataUseCaseIngredientPortionCalculatorRequestResponse.
                    getResponseNewValidImperialSpoonUnitOneUpdatedHalfSpoon();

    private RequestValues REQUEST_NEW_VALID_IMPERIAL_SPOON_INVALID_CONVERSION_FACTOR =
            TestDataUseCaseIngredientPortionCalculatorRequestResponse.
                    getRequestNewValidImperialSpoonInvalidConversionFactor();
    private ResponseValues RESPONSE_NEW_VALID_IMPERIAL_SPOON_INVALID_CONVERSION_FACTOR =
            TestDataUseCaseIngredientPortionCalculatorRequestResponse.
                    getResponseNewValidImperialSpoonInvalidConversionFactor();

    private RequestValues REQUEST_IMPERIAL_SPOON_VALID_CONVERSION_FACTOR =
            TestDataUseCaseIngredientPortionCalculatorRequestResponse.
                    getRequestNewValidImperialSpoonValidConversionFactor();
    private ResponseValues RESPONSE_NEW_VALID_IMPERIAL_SPOON_VALID_CONVERSION_FACTOR =
            TestDataUseCaseIngredientPortionCalculatorRequestResponse.
                    getResponseNewValidImperialSpoonValidConversionFactor();

    private RequestValues REQUEST_VALID_EXISTING_METRIC =
            TestDataUseCaseIngredientPortionCalculatorRequestResponse.
                    getRequestExistingValidMetric();
    private ResponseValues RESPONSE_VALID_EXISTING_METRIC =
            TestDataUseCaseIngredientPortionCalculatorRequestResponse.
                    getResponseExistingValidMetric();

    private RequestValues REQUEST_EXISTING_METRIC_INVALID_UNIT_ONE =
            TestDataUseCaseIngredientPortionCalculatorRequestResponse.
                    getRequestExistingMetricInvalidTotalUnitOne();
    private ResponseValues RESPONSE_EXISTING_METRIC_INVALID_UNIT_ONE =
            TestDataUseCaseIngredientPortionCalculatorRequestResponse.
                    getResponseExistingMetricInvalidTotalUnitOne();

    private RequestValues REQUEST_EXISTING_METRIC_VALID_UNIT_TWO =
            TestDataUseCaseIngredientPortionCalculatorRequestResponse.
                    getRequestExistingMetricValidTotalUnitTwo();
    private ResponseValues RESPONSE_EXISTING_METRIC_VALID_UNIT_TWO =
            TestDataUseCaseIngredientPortionCalculatorRequestResponse.
                    getResponseExistingMetricValidTotalUnitTwo();

    private RequestValues REQUEST_EXISTING_METRIC_CHANGED_TO_IMPERIAL_SPOON =
            TestDataUseCaseIngredientPortionCalculatorRequestResponse.
                    getRequestExistingMetricUnitOfMeasureChangedToImperialSpoon();
    private ResponseValues RESPONSE_EXISTING_METRIC_CHANGED_TO_IMPERIAL_SPOON =
            TestDataUseCaseIngredientPortionCalculatorRequestResponse.
                    getResponseExistingMetricUnitOfMeasureChangedToImperialSpoon();

    private RequestValues REQUEST_EXISTING_IMPERIAL_SPOON =
            TestDataUseCaseIngredientPortionCalculatorRequestResponse.
                    getRequestExistingValidImperialSpoon();
    private ResponseValues RESPONSE_EXISTING_IMPERIAL_SPOON =
            TestDataUseCaseIngredientPortionCalculatorRequestResponse.
                    getResponseExistingImperialSpoon();

    private RequestValues REQUEST_EXISTING_IMPERIAL_SPOON_INVALID_CONVERSION_FACTOR =
            TestDataUseCaseIngredientPortionCalculatorRequestResponse.
                    getRequestExistingImperialSpoonInvalidConversionFactor();
    private ResponseValues RESPONSE_EXISTING_IMPERIAL_SPOON_INVALID_CONVERSION_FACTOR =
            TestDataUseCaseIngredientPortionCalculatorRequestResponse.
                    getResponseExistingImperialSpoonInvalidConversionFactor();

    private RecipeIngredientQuantityEntity QUANTITY_NEW_VALID_METRIC =
            TestDataRecipeIngredientQuantityEntity.getNewValidMetric();


    private RecipeIngredientQuantityEntity QUANTITY_EXISTING_VALID_METRIC =
            TestDataRecipeIngredientQuantityEntity.getExistingValidMetric();

    private RecipeIngredientQuantityEntity QUANTITY_EXISTING_VALID_IMPERIAL_SPOON =
            TestDataRecipeIngredientQuantityEntity.getExistingValidImperialTwoSpoons();

    private RecipePortionsEntity PORTIONS_NEW_VALID_FOUR =
            TestDataRecipePortionsEntity.getNewValidFourPortions();

    private RecipePortionsEntity PORTIONS_EXISTING_VALID_NINE =
            TestDataRecipePortionsEntity.getExistingValidNinePortions();

    private IngredientEntity INGREDIENT_NEW_VALID_NAME =
            TestDataIngredientEntity.getNewValidName();

    private IngredientEntity INGREDIENT_NEW_VALID_NAME_MAX_CONVERSION_FACTOR =
            TestDataIngredientEntity.getNewValidNameMaxConversionFactor();

    private IngredientEntity INGREDIENT_EXISTING_VALID_NAME_DESCRIPTION =
            TestDataIngredientEntity.getExistingValidNameValidDescriptionNoConversionFactor();

    private IngredientEntity INGREDIENT_NEW_VALID_NAME_DESCRIPTION =
            TestDataIngredientEntity.getNewValidNameValidDescription();

    private RecipeIngredientQuantityEntity QUANTITY_NEW_VALID_MAX_MASS_DIV_FOUR_PORTIONS =
            TestDataRecipeIngredientQuantityEntity.getNewValidMetricMaxMassDivFourPortions();

    // endregion constants -------------------------------------------------------------------------
    // region helper fields ------------------------------------------------------------------------
    private UseCaseHandler handler;
    private ResponseValues lastResponse;
    @Mock
    RepositoryRecipePortions repoRecipePortionsMock;
    @Captor
    ArgumentCaptor<DataSource.GetEntityCallback<RecipePortionsEntity>>
            getRecipePortionsCallbackCaptor;
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
    @Captor
    ArgumentCaptor<RecipeIngredientQuantityEntity> recipeIngredientCaptor;
    @Captor
    ArgumentCaptor<IngredientEntity> ingredientArgumentCaptor;
    // endregion helper fields ---------------------------------------------------------------------

    private UseCasePortionCalculator SUT;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);

        handler = new UseCaseHandler(new UseCaseSchedulerMock());

        SUT = new UseCasePortionCalculator(
                repoRecipePortionsMock,
                repoRecipeIngredientMock,
                repoIngredientMock,
                idProviderMock,
                timeProviderMock);
    }

    @Test
    public void startNewRecipeAndIngredientId_invalidMeasurementNotSaved() {
        // Arrange
        when(idProviderMock.getUId()).thenReturn(QUANTITY_NEW_INVALID.getId());
        // Act
        handler.execute(SUT, REQUEST_EMPTY_FOUR_PORTIONS, getResponseCallback());
        // Assert
        verifyRepoIngredientCalledReturnNewValidNameValidDescription();
        verifyRepoPortionsCalledReturnNewValidFourPortions();
        // Assert recipeIngredient not saved
        verifyNoMoreInteractions(repoRecipeIngredientMock);
    }

    @Test
    public void startNewRecipeAndIngredientId_resultInvalidMeasurement() {
        // Arrange
        when(idProviderMock.getUId()).thenReturn(QUANTITY_NEW_INVALID.getId());
        // Act
        handler.execute(SUT, REQUEST_EMPTY_FOUR_PORTIONS, getResponseCallback());
        // Assert
        verifyRepoIngredientCalledReturnNewValidNameValidDescription();
        verifyRepoPortionsCalledReturnNewValidFourPortions();
        assertEquals(RESPONSE_EMPTY_FOUR_PORTIONS, lastResponse);
    }

    @Test
    public void startNewRecipeAndIngredientId_invalidTotalMeasurementOneUpdated_notSaved() {
        // Arrange
        whenIdProviderReturnNewValidId();
        whenTimeProviderThenReturnNewValidTime();
        // Act
        handler.execute(SUT, REQUEST_EMPTY_FOUR_PORTIONS, getResponseCallback());
        verifyRepoIngredientCalledAndReturnNewValidName();
        verifyRepoPortionsCalledAndReturnNewValidFourPortions();
        handler.execute(SUT, REQUEST_INVALID_UNIT_ONE, getResponseCallback());
        // Assert
        verifyNoMoreInteractions(repoRecipeIngredientMock);
    }

    @Test
    public void startNewRecipeAndIngredientId_invalidTotalMeasurementOneUpdated_INVALID_TOTAL_MEASUREMENT_ONE() {
        // Arrange
        whenIdProviderReturnNewValidId();
        whenTimeProviderThenReturnNewValidTime();
        // Act
        handler.execute(SUT, REQUEST_INVALID_UNIT_ONE, getResponseCallback());
        verifyRepoIngredientCalledAndReturnNewValidName();
        verifyRepoPortionsCalledAndReturnNewValidFourPortions();
        handler.execute(SUT, REQUEST_INVALID_UNIT_ONE, getResponseCallback());
        // Assert
        assertEquals(2, responses.size());
        assertEquals(RESPONSE_INVALID_UNIT_ONE, lastResponse);
    }

    @Test
    public void startNewRecipeAndIngredientId_validTotalMeasurementOneUpdated_saved() {
        // Arrange
        whenIdProviderReturnNewValidId();
        whenTimeProviderThenReturnNewValidTime();
        // Act
        handler.execute(SUT, REQUEST_EMPTY_FOUR_PORTIONS, getResponseCallback());
        verifyRepoIngredientCalledAndReturnNewValidName();
        verifyRepoPortionsCalledAndReturnNewValidFourPortions();
        handler.execute(SUT, REQUEST_VALID_UNIT_ONE, getResponseCallback());
        // Assert
        verify(repoRecipeIngredientMock).save(eq(QUANTITY_NEW_VALID_METRIC));
    }

    @Test
    public void startNewRecipeAndIngredientId_validTotalMeasurementOneUpdated_RESULT_OK() {
        // Arrange
        whenIdProviderReturnNewValidId();
        whenTimeProviderThenReturnNewValidTime();
        // Act
        handler.execute(SUT, REQUEST_EMPTY_FOUR_PORTIONS, getResponseCallback());
        verifyRepoIngredientCalledAndReturnNewValidName();
        verifyRepoPortionsCalledAndReturnNewValidFourPortions();
        handler.execute(SUT, REQUEST_VALID_UNIT_ONE, getResponseCallback());
        // Assert
        assertEquals(2, responses.size());
        assertEquals(RESPONSE_VALID_UNIT_ONE, lastResponse);
    }

    @Test
    public void startNewRecipeAndIngredientId_invalidTotalMeasurementTwoUpdated_notSaved() {
        // Arrange
        whenIdProviderReturnNewValidId();
        whenTimeProviderThenReturnNewValidTime();
        // Act
        handler.execute(SUT, REQUEST_EMPTY_FOUR_PORTIONS, getResponseCallback());
        verifyRepoIngredientCalledAndReturnNewValidName();
        verifyRepoPortionsCalledAndReturnNewValidFourPortions();
        handler.execute(SUT, REQUEST_INVALID_UNIT_TWO, getResponseCallback());
        // Assert
        verifyNoMoreInteractions(repoRecipeIngredientMock);
    }

    @Test
    public void startNewRecipeAndIngredientId_invalidTotalMeasurementTwoUpdated_INVALID_TOTAL_MEASUREMENT_TWO() {
        // Arrange
        whenIdProviderReturnNewValidId();
        whenTimeProviderThenReturnNewValidTime();
        // Act
        handler.execute(SUT, REQUEST_EMPTY_FOUR_PORTIONS, getResponseCallback());
        verifyRepoIngredientCalledAndReturnNewValidName();
        verifyRepoPortionsCalledAndReturnNewValidFourPortions();
        handler.execute(SUT, REQUEST_INVALID_UNIT_TWO, getResponseCallback());
        // Assert
        assertEquals(2, responses.size());
        assertEquals(RESPONSE_INVALID_UNIT_TWO, lastResponse);
    }

    @Test
    public void startNewRecipeAndIngredientId_validTotalMeasurementTwoUpdated_saved() {
        // Arrange
        whenIdProviderReturnNewValidId();
        whenTimeProviderThenReturnNewValidTime();
        // Act
        handler.execute(SUT, REQUEST_EMPTY_FOUR_PORTIONS, getResponseCallback());
        verifyRepoIngredientCalledAndReturnNewValidName();
        verifyRepoPortionsCalledAndReturnNewValidFourPortions();
        handler.execute(SUT, REQUEST_VALID_UNIT_TWO, getResponseCallback());
        // Assert
        verify(repoRecipeIngredientMock).save(recipeIngredientCaptor.capture());
        RecipeIngredientQuantityEntity actualResult = recipeIngredientCaptor.getValue();
        assertEquals(QUANTITY_NEW_VALID_MAX_MASS_DIV_FOUR_PORTIONS, actualResult);
    }

    @Test
    public void startNewRecipeAndIngredientId_validTotalMeasurementTwoUpdated_RESULT_OK() {
        // Arrange
        whenIdProviderReturnNewValidId();
        whenTimeProviderThenReturnNewValidTime();
        // Act
        handler.execute(SUT, REQUEST_EMPTY_FOUR_PORTIONS, getResponseCallback());
        verifyRepoIngredientCalledAndReturnNewValidName();
        verifyRepoPortionsCalledAndReturnNewValidFourPortions();
        handler.execute(SUT, REQUEST_VALID_UNIT_TWO, getResponseCallback());
        // Assert
        assertEquals(2, responses.size());
        assertEquals(RESPONSE_VALID_UNIT_TWO, lastResponse);
    }

    @Test
    public void startNewRecipeAndIngredientId_unitOfMeasureChanged_INVALID_MEASUREMENT() {
        // Arrange
        whenIdProviderReturnNewValidId();
        whenTimeProviderThenReturnNewValidTime();
        // Act
        handler.execute(SUT, REQUEST_EMPTY_FOUR_PORTIONS, getResponseCallback());
        verifyRepoIngredientCalledAndReturnNewValidName();
        verifyRepoPortionsCalledAndReturnNewValidFourPortions();
        handler.execute(SUT, REQUEST_UNIT_OF_MEASURE_IMPERIAL_SPOON, getResponseCallback());
        // Assert
        assertEquals(2, responses.size());
        assertEquals(RESPONSE_UNIT_OF_MEASURE_IMPERIAL_SPOON, lastResponse);
    }

    @Test
    public void startNewRecipeAndIngredientId_unitOfMeasureChangedTwiceWithConversionFactorChange_conversionFactorDefault() {
        // Arrange
        whenIdProviderReturnNewValidId();
        whenTimeProviderThenReturnNewValidTime();
        String recipeId = REQUEST_EMPTY_FOUR_PORTIONS.getRecipeId();
        String ingredientId = REQUEST_EMPTY_FOUR_PORTIONS.getIngredientId();
        // Act
        handler.execute(SUT, REQUEST_EMPTY_FOUR_PORTIONS, getResponseCallback());
        verifyRepoIngredientCalledAndReturnNewValidName();
        verifyRepoPortionsCalledAndReturnNewValidFourPortions();
        // first unit of measure change
        handler.execute(SUT, REQUEST_UNIT_OF_MEASURE_IMPERIAL_SPOON, getResponseCallback());
        // Update conversion factor and return model
        MeasurementModel resultAfterUnitOfMeasureChange = lastResponse.getModel();
        MeasurementModel updatedConversionFactor = MeasurementModelBuilder.
                basedOnModel(resultAfterUnitOfMeasureChange).
                setConversionFactor(MAX_CONVERSION_FACTOR).
                build();
        RequestValues requestValuesWithConversionFactor = new RequestValues(
                recipeId, ingredientId, NO_RECIPE_INGREDIENT_ID, updatedConversionFactor);
        handler.execute(SUT, requestValuesWithConversionFactor, getResponseCallback());
        // Check conversion factor set ok in response
        MeasurementModel modelWithConversionFactor = lastResponse.getModel();
        assertEquals(MAX_CONVERSION_FACTOR, modelWithConversionFactor.getConversionFactor(), DELTA);
        // Second unit of measure change
        MeasurementModel modelWithUnitOfMeasureChange = MeasurementModelBuilder.
                basedOnModel(modelWithConversionFactor).
                setSubtype(MeasurementSubtype.METRIC_MASS).
                build();
        RequestValues requestUnitOfMeasureChange = new RequestValues(
                recipeId, ingredientId, NO_RECIPE_INGREDIENT_ID, modelWithUnitOfMeasureChange);
        handler.execute(SUT, requestUnitOfMeasureChange, getResponseCallback());
        // Assert - conversion factor has reset to default
        assertEquals(UnitOfMeasureConstants.NO_CONVERSION_FACTOR,
                lastResponse.getModel().getConversionFactor(), DELTA);
    }

    @Test
    public void startNewRecipeAndIngredientId_unitOfMeasureChangedImperialSpoonAndUpdateMeasurementOne_userWalkThrough() {
        // Arrange
        whenIdProviderReturnNewValidId();
        whenTimeProviderThenReturnNewValidTime();
        // Act
        handler.execute(SUT, REQUEST_EMPTY_FOUR_PORTIONS, getResponseCallback());
        verifyRepoIngredientCalledAndReturnNewValidName();
        verifyRepoPortionsCalledAndReturnNewValidFourPortions();
        // Change unit of measure
        handler.execute(SUT, REQUEST_UNIT_OF_MEASURE_IMPERIAL_SPOON, getResponseCallback());
        // Assert
        assertEquals(2, responses.size());
        assertEquals(RESPONSE_UNIT_OF_MEASURE_IMPERIAL_SPOON, lastResponse);
        // Change measurement one
        handler.execute(SUT, REQUEST_NEW_VALID_IMPERIAL_SPOON_UNIT_ONE_UPDATED_HALF_SPOON,
                getResponseCallback());
        // Assert
        assertEquals(3, responses.size());
        assertEquals(RESPONSE_NEW_VALID_IMPERIAL_SPOON_UNIT_ONE_UPDATED_HALF_SPOON, lastResponse);
    }

    @Test
    public void startNewRecipeAndIngredientId_invalidConversionFactor_notSaved() {
        // Arrange
        whenIdProviderReturnNewValidId();
        whenTimeProviderThenReturnNewValidTime();
        // Act
        handler.execute(SUT, REQUEST_EMPTY_FOUR_PORTIONS, getResponseCallback());
        verifyRepoIngredientCalledAndReturnNewValidName();
        verifyRepoPortionsCalledAndReturnNewValidFourPortions();
        // Change to unit of measure that supports conversion factor
        handler.execute(SUT, REQUEST_UNIT_OF_MEASURE_IMPERIAL_SPOON, getResponseCallback());
        // Attempt to set out of bounds conversion factor
        handler.execute(SUT, REQUEST_NEW_VALID_IMPERIAL_SPOON_INVALID_CONVERSION_FACTOR,
                getResponseCallback());
        // Assert
        verifyNoMoreInteractions(repoRecipeIngredientMock);
    }

    @Test
    public void startNewRecipeAndIngredientId_invalidConversionFactor_INVALID_CONVERSION_FACTOR() {
        // Arrange
        whenIdProviderReturnNewValidId();
        whenTimeProviderThenReturnNewValidTime();
        // Act
        handler.execute(SUT, REQUEST_EMPTY_FOUR_PORTIONS, getResponseCallback());
        verifyRepoIngredientCalledAndReturnNewValidName();
        verifyRepoPortionsCalledAndReturnNewValidFourPortions();
        // Change to unit of measure that supports conversion factor
        handler.execute(SUT, REQUEST_UNIT_OF_MEASURE_IMPERIAL_SPOON, getResponseCallback());
        // Attempt to set out of bounds conversion factor
        handler.execute(SUT, REQUEST_NEW_VALID_IMPERIAL_SPOON_INVALID_CONVERSION_FACTOR,
                getResponseCallback());
        // Assert
        assertEquals(RESPONSE_NEW_VALID_IMPERIAL_SPOON_INVALID_CONVERSION_FACTOR.getResultStatus(),
                lastResponse.getResultStatus());
    }

    @Test
    public void startNewRecipeAndIngredientId_validConversionFactor_saved() {
        // Arrange
        whenIdProviderReturnNewValidId();
        whenTimeProviderThenReturnNewValidTime();
        // Act
        handler.execute(SUT, REQUEST_EMPTY_FOUR_PORTIONS, getResponseCallback());
        verifyRepoIngredientCalledAndReturnNewValidName();
        verifyRepoPortionsCalledAndReturnNewValidFourPortions();
        handler.execute(SUT, REQUEST_UNIT_OF_MEASURE_IMPERIAL_SPOON, getResponseCallback());
        handler.execute(SUT, REQUEST_IMPERIAL_SPOON_VALID_CONVERSION_FACTOR, getResponseCallback());
        // Assert
        verify(repoIngredientMock).save(ingredientArgumentCaptor.capture());
        IngredientEntity actualResult = ingredientArgumentCaptor.getValue();
        assertEquals(INGREDIENT_NEW_VALID_NAME_MAX_CONVERSION_FACTOR, actualResult);
    }

    // invalid number of items as breaks smallest unit (ie 1000th of a gram)

    @Test
    public void startNewRecipeAndIngredientId_validConversionFactor_itemBaseUnitsUpdated() {
        whenIdProviderReturnNewValidId();
        whenTimeProviderThenReturnNewValidTime();
        // Act
        handler.execute(SUT, REQUEST_EMPTY_FOUR_PORTIONS, getResponseCallback());
        verifyRepoIngredientCalledAndReturnNewValidName();
        verifyRepoPortionsCalledAndReturnNewValidFourPortions();
        // Change to conversion factor enabled unit of measure
        handler.execute(SUT, REQUEST_UNIT_OF_MEASURE_IMPERIAL_SPOON, getResponseCallback());
        // Apply a conversion factor
        handler.execute(SUT, REQUEST_IMPERIAL_SPOON_VALID_CONVERSION_FACTOR, getResponseCallback());
        // Update unit one
        MeasurementModel unitOneUpdated = MeasurementModelBuilder.basedOnModel(
                REQUEST_IMPERIAL_SPOON_VALID_CONVERSION_FACTOR.getModel()).
                setTotalUnitOne(1).build();
        RequestValues requestUnitOneUpdated = new RequestValues(
                REQUEST_IMPERIAL_SPOON_VALID_CONVERSION_FACTOR.getRecipeId(),
                REQUEST_IMPERIAL_SPOON_VALID_CONVERSION_FACTOR.getIngredientId(),
                NO_RECIPE_INGREDIENT_ID,
                unitOneUpdated);
        handler.execute(SUT, requestUnitOneUpdated, getResponseCallback());
        // Get response from unit one updated
        ResponseValues responseUnitOneUpdated = lastResponse;
        // Update unit two
        MeasurementModel unitTwoUpdated = MeasurementModelBuilder.basedOnModel(
                responseUnitOneUpdated.getModel()).
                setTotalUnitTwo(1).build();
        RequestValues requestUnitTwoUpdated = new RequestValues(
                REQUEST_IMPERIAL_SPOON_VALID_CONVERSION_FACTOR.getRecipeId(),
                REQUEST_IMPERIAL_SPOON_VALID_CONVERSION_FACTOR.getIngredientId(),
                NO_RECIPE_INGREDIENT_ID,
                unitTwoUpdated);
        handler.execute(SUT, requestUnitTwoUpdated, getResponseCallback());
        // Assert item base units are correct
        assertEquals(RESPONSE_NEW_VALID_IMPERIAL_SPOON_VALID_CONVERSION_FACTOR.
                        getModel().getItemBaseUnits(),
                lastResponse.getModel().getItemBaseUnits(), DELTA);
    }

    @Test
    public void startNewRecipeAndIngredientId_validConversionFactor_RESULT_OK() {
        whenIdProviderReturnNewValidId();
        whenTimeProviderThenReturnNewValidTime();
        // Act
        handler.execute(SUT, REQUEST_EMPTY_FOUR_PORTIONS, getResponseCallback());
        verifyRepoIngredientCalledAndReturnNewValidName();
        verifyRepoPortionsCalledAndReturnNewValidFourPortions();
        // Change to conversion factor enabled unit of measure
        handler.execute(SUT, REQUEST_UNIT_OF_MEASURE_IMPERIAL_SPOON, getResponseCallback());
        // Apply a conversion factor
        handler.execute(SUT, REQUEST_IMPERIAL_SPOON_VALID_CONVERSION_FACTOR, getResponseCallback());
        // Update unit one
        MeasurementModel unitOneUpdated = MeasurementModelBuilder.basedOnModel(
                REQUEST_IMPERIAL_SPOON_VALID_CONVERSION_FACTOR.getModel()).
                setTotalUnitOne(1).build();
        RequestValues requestUnitOneUpdated = new RequestValues(
                REQUEST_IMPERIAL_SPOON_VALID_CONVERSION_FACTOR.getRecipeId(),
                REQUEST_IMPERIAL_SPOON_VALID_CONVERSION_FACTOR.getIngredientId(),
                NO_RECIPE_INGREDIENT_ID,
                unitOneUpdated);
        handler.execute(SUT, requestUnitOneUpdated, getResponseCallback());
        // Get response from unit one updated
        ResponseValues responseUnitOneUpdated = lastResponse;
        // Update unit two
        MeasurementModel unitTwoUpdated = MeasurementModelBuilder.basedOnModel(
                responseUnitOneUpdated.getModel()).
                setTotalUnitTwo(1).build();
        RequestValues requestUnitTwoUpdated = new RequestValues(
                REQUEST_IMPERIAL_SPOON_VALID_CONVERSION_FACTOR.getRecipeId(),
                REQUEST_IMPERIAL_SPOON_VALID_CONVERSION_FACTOR.getIngredientId(),
                NO_RECIPE_INGREDIENT_ID,
                unitTwoUpdated);
        handler.execute(SUT, requestUnitTwoUpdated, getResponseCallback());
        // Assert
        assertEquals(ResultStatus.RESULT_OK, lastResponse.getResultStatus());
    }

    @Test
    public void startExistingRecipeIngredientId_entitiesLoaded() {
        // Arrange
        // Act
        handler.execute(SUT, REQUEST_VALID_EXISTING_METRIC, getResponseCallback());
        // Assert
        verifyRepoRecipeIngredientCalledReturnExistingValidMetric();
        verifyRepoIngredientCalledAndReturnExistingValidNameDescriptionNoConversionFactor();
        verifyRepoPortionsCalledAndReturnExistingValidNinePortions();
    }

    @Test
    public void startExistingRecipeIngredientId_expectedResponse() {
        // Arrange
        // Act
        handler.execute(SUT, REQUEST_VALID_EXISTING_METRIC, getResponseCallback());
        // Assert
        verifyRepoRecipeIngredientCalledReturnExistingValidMetric();
        verifyRepoIngredientCalledAndReturnExistingValidNameDescriptionNoConversionFactor();
        verifyRepoPortionsCalledAndReturnExistingValidNinePortions();
        assertEquals(RESPONSE_VALID_EXISTING_METRIC, lastResponse);
    }

    @Test
    public void startExistingRecipeIngredientId_invalidTotalMeasurementOneUpdated_valueNotSaved() {
        // Arrange
        // Act
        handler.execute(SUT, REQUEST_VALID_EXISTING_METRIC, getResponseCallback());
        verifyRepoRecipeIngredientCalledReturnExistingValidMetric();
        verifyRepoIngredientCalledAndReturnExistingValidNameDescriptionNoConversionFactor();
        verifyRepoPortionsCalledAndReturnExistingValidNinePortions();
        handler.execute(SUT, REQUEST_EXISTING_METRIC_INVALID_UNIT_ONE, getResponseCallback());
        // Assert
        verifyNoMoreInteractions(repoRecipeIngredientMock);
    }

    @Test
    public void startExistingRecipeIngredientId_invalidTotalMeasurementTwoUpdated_INVALID_TOTAL_MEASUREMENT_TWO() {
        // Arrange
        // Act
        handler.execute(SUT, REQUEST_VALID_EXISTING_METRIC, getResponseCallback());
        verifyRepoRecipeIngredientCalledReturnExistingValidMetric();
        verifyRepoIngredientCalledAndReturnExistingValidNameDescriptionNoConversionFactor();
        verifyRepoPortionsCalledAndReturnExistingValidNinePortions();
        handler.execute(SUT, REQUEST_EXISTING_METRIC_INVALID_UNIT_ONE, getResponseCallback());
        // Assert
        assertEquals(RESPONSE_EXISTING_METRIC_INVALID_UNIT_ONE.getResultStatus(),
                lastResponse.getResultStatus());
    }

    @Test
    public void startExistingRecipeIngredientId_validTotalMeasurementTwoUpdated_saved() {
        // Arrange
        // Act
        handler.execute(SUT, REQUEST_VALID_EXISTING_METRIC, getResponseCallback());
        verifyRepoRecipeIngredientCalledReturnExistingValidMetric();
        verifyRepoIngredientCalledAndReturnExistingValidNameDescriptionNoConversionFactor();
        verifyRepoPortionsCalledAndReturnExistingValidNinePortions();
        handler.execute(SUT, REQUEST_EXISTING_METRIC_VALID_UNIT_TWO, getResponseCallback());
        // Assert
        verify(repoRecipeIngredientMock).save(recipeIngredientCaptor.capture());
        RecipeIngredientQuantityEntity saveResult = recipeIngredientCaptor.getValue();
        double expectedBaseUnits = RESPONSE_EXISTING_METRIC_VALID_UNIT_TWO.
                getModel().getItemBaseUnits();
        double actualItemBseUnits = saveResult.getItemBaseUnits();
        assertEquals(expectedBaseUnits, actualItemBseUnits, DELTA);
    }

    @Test
    public void startExistingRecipeIngredientId_validTotalMeasurementTwoUpdated_RESULT_OK() {
        // Arrange
        // Act
        handler.execute(SUT, REQUEST_VALID_EXISTING_METRIC, getResponseCallback());
        verifyRepoRecipeIngredientCalledReturnExistingValidMetric();
        verifyRepoIngredientCalledAndReturnExistingValidNameDescriptionNoConversionFactor();
        verifyRepoPortionsCalledAndReturnExistingValidNinePortions();
        handler.execute(SUT, REQUEST_EXISTING_METRIC_VALID_UNIT_TWO, getResponseCallback());
        // Assert
        assertEquals(RESPONSE_EXISTING_METRIC_VALID_UNIT_TWO.getResultStatus(),
                lastResponse.getResultStatus());
    }

    @Test
    public void startExistingRecipeIngredientId_unitOfMeasureChanged_INVALID_MEASUREMENT() {
        // Arrange
        // Act
        handler.execute(SUT, REQUEST_VALID_EXISTING_METRIC, getResponseCallback());
        verifyRepoRecipeIngredientCalledReturnExistingValidMetric();
        verifyRepoIngredientCalledAndReturnExistingValidNameDescriptionNoConversionFactor();
        verifyRepoPortionsCalledAndReturnExistingValidNinePortions();
        handler.execute(SUT, REQUEST_EXISTING_METRIC_CHANGED_TO_IMPERIAL_SPOON,
                getResponseCallback());
        // Assert
        assertEquals(RESPONSE_EXISTING_METRIC_CHANGED_TO_IMPERIAL_SPOON.getResultStatus(),
                lastResponse.getResultStatus());
    }

    @Test
    public void startExistingRecipeIngredientId_unitOfMeasureChanged_emptyModel() {
        // Arrange
        // Act
        handler.execute(SUT, REQUEST_VALID_EXISTING_METRIC, getResponseCallback());
        // Assert
        verifyRepoRecipeIngredientCalledReturnExistingValidMetric();
        verifyRepoIngredientCalledAndReturnExistingValidNameDescriptionNoConversionFactor();
        verifyRepoPortionsCalledAndReturnExistingValidNinePortions();
        handler.execute(SUT, REQUEST_EXISTING_METRIC_CHANGED_TO_IMPERIAL_SPOON,
                getResponseCallback());
        // Assert
        assertEquals(RESPONSE_EXISTING_METRIC_CHANGED_TO_IMPERIAL_SPOON.getModel(),
                lastResponse.getModel());
    }

    @Test
    public void startExistingRecipeIngredientId_invalidConversionFactor_notSaved() {
        // Arrange
        // Act
        handler.execute(SUT, REQUEST_EXISTING_IMPERIAL_SPOON, getResponseCallback());
        // Assert
        verifyRepoRecipeIngredientCalledReturnExistingValidImperialSpoon();
        verifyRepoIngredientCalledAndReturnExistingValidNameDescriptionNoConversionFactor();
        verifyRepoPortionsCalledAndReturnExistingValidNinePortions();
        assertEquals(RESPONSE_EXISTING_IMPERIAL_SPOON, lastResponse);
        // Attempt to set invalid conversion factor
        handler.execute(SUT, REQUEST_EXISTING_IMPERIAL_SPOON_INVALID_CONVERSION_FACTOR,
                getResponseCallback());
        // Assert
        verifyNoMoreInteractions(repoRecipeIngredientMock);
    }

    @Test
    public void startExistingRecipeIngredientId_invalidConversionFactor_INVALID_CONVERSION_FACTOR() {
        // Arrange
        // Act
        handler.execute(SUT, REQUEST_EXISTING_IMPERIAL_SPOON, getResponseCallback());
        // Assert
        verifyRepoRecipeIngredientCalledReturnExistingValidImperialSpoon();
        verifyRepoIngredientCalledAndReturnExistingValidNameDescriptionNoConversionFactor();
        verifyRepoPortionsCalledAndReturnExistingValidNinePortions();
        assertEquals(RESPONSE_EXISTING_IMPERIAL_SPOON, lastResponse);
        // Attempt to set invalid conversion factor
        handler.execute(SUT, REQUEST_EXISTING_IMPERIAL_SPOON_INVALID_CONVERSION_FACTOR,
                getResponseCallback());
        // Assert
        assertEquals(RESPONSE_EXISTING_IMPERIAL_SPOON_INVALID_CONVERSION_FACTOR.getResultStatus(),
                lastResponse.getResultStatus());
    }

    @Test
    public void startExistingRecipeIngredientId_invalidConversionFactor_measurementValuesNotChanged() {
        // Act
        handler.execute(SUT, REQUEST_EXISTING_IMPERIAL_SPOON, getResponseCallback());
        // Assert
        verifyRepoRecipeIngredientCalledReturnExistingValidImperialSpoon();
        verifyRepoIngredientCalledAndReturnExistingValidNameDescriptionNoConversionFactor();
        verifyRepoPortionsCalledAndReturnExistingValidNinePortions();
        // Attempt to set invalid conversion factor
        handler.execute(SUT, REQUEST_EXISTING_IMPERIAL_SPOON_INVALID_CONVERSION_FACTOR,
                getResponseCallback());
        // Assert
        assertEquals(RESPONSE_EXISTING_IMPERIAL_SPOON_INVALID_CONVERSION_FACTOR, lastResponse);
    }

    @Test
    public void startExistingRecipeIngredientId_userUpdateExistingFlow_allValuesAsExpected() {
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
                QUANTITY_EXISTING_VALID_METRIC.getUnitOfMeasureSubtype());

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

        // Act - Request an existing ingredient quantity measurement
        handler.execute(SUT, REQUEST_VALID_EXISTING_METRIC, getResponseCallback());
        // verify database called and return data
        verifyRepoRecipeIngredientCalledReturnExistingValidMetric();
        verifyRepoIngredientCalledAndReturnExistingValidNameDescriptionNoConversionFactor();
        verifyRepoPortionsCalledAndReturnExistingValidNinePortions();
        // verify existing data loaded
        assertEquals(RESPONSE_VALID_EXISTING_METRIC, lastResponse);

        //** Start of simulated user interaction **//
        // user updates unit of measure
        MeasurementModel updatedUnitOfMeasureModel = MeasurementModelBuilder.
                basedOnModel(lastResponse.getModel()).
                setSubtype(MeasurementSubtype.IMPERIAL_SPOON).
                build();

        RequestValues updatedUnitOfMeasureRequest = new RequestValues(
                NO_RECIPE_ID,
                NO_INGREDIENT_ID,
                REQUEST_VALID_EXISTING_METRIC.getRecipeIngredientId(),
                updatedUnitOfMeasureModel);
        handler.execute(SUT, updatedUnitOfMeasureRequest, getResponseCallback());
        // verify expected model and result status response
        assertEquals(MeasurementSubtype.IMPERIAL_SPOON, lastResponse.getModel().getSubtype());
        assertEquals(ResultStatus.INVALID_MEASUREMENT, lastResponse.getResultStatus());
        // confirm nothing saved
        verifyNoMoreInteractions(repoRecipeIngredientMock);

        // user updates unit one
        MeasurementModel updatedUnitOneModel = MeasurementModelBuilder.
                basedOnModel(lastResponse.getModel()).
                setTotalUnitOne(numberOfTeaspoons).
                build();
        RequestValues updatedUnitOneRequest = new RequestValues(
                NO_RECIPE_ID,
                NO_INGREDIENT_ID,
                REQUEST_VALID_EXISTING_METRIC.getRecipeIngredientId(),
                updatedUnitOneModel
        );
        handler.execute(SUT, updatedUnitOneRequest, getResponseCallback());
        // verify expected model and result status response
        assertEquals(numberOfTeaspoons, lastResponse.getModel().getTotalUnitOne(), DELTA);
        assertEquals(ResultStatus.RESULT_OK, lastResponse.getResultStatus());
        // verify changes saved to quantity entity
        verify(repoRecipeIngredientMock).save(recipeIngredientCaptor.capture());
        RecipeIngredientQuantityEntity actualQuantityEntityAfterUnitOneChange =
                recipeIngredientCaptor.getValue();
        RecipeIngredientQuantityEntity expectedQuantityEntityUnitOneChange =
                new RecipeIngredientQuantityEntity(
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
                basedOnModel(lastResponse.getModel()).
                setConversionFactor(MAX_CONVERSION_FACTOR).
                build();
        RequestValues updatedConversionFactorRequest = new RequestValues(
                NO_RECIPE_ID,
                NO_INGREDIENT_ID,
                REQUEST_VALID_EXISTING_METRIC.getRecipeIngredientId(),
                updatedConversionFactorModel
        );
        handler.execute(SUT, updatedConversionFactorRequest, getResponseCallback());

        // verify expected UI updates returned
        assertEquals(numberOfTeaspoons, lastResponse.getModel().getTotalUnitOne(), DELTA);
        assertEquals(MAX_CONVERSION_FACTOR, lastResponse.getModel().getConversionFactor(), DELTA);
        assertEquals(ResultStatus.RESULT_OK, lastResponse.getResultStatus());

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
        RecipeIngredientQuantityEntity actualQuantityEntity = recipeIngredientCaptor.getValue();
        RecipeIngredientQuantityEntity expectedQuantityEntityAfterConversionFactorApplied =
                new RecipeIngredientQuantityEntity(
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
    private UseCase.UseCaseCallback<ResponseValues>
    getResponseCallback() {
        return new UseCase.UseCaseCallback<ResponseValues>() {

            @Override
            public void onSuccess(ResponseValues response) {
                UseCasePortionCalculatorTest.this.responses.add(response);
                UseCasePortionCalculatorTest.this.lastResponse = response;
            }

            @Override
            public void onError(ResponseValues response) {
                UseCasePortionCalculatorTest.this.responses.add(response);
                UseCasePortionCalculatorTest.this.lastResponse = response;
            }
        };
    }

    private void verifyRepoIngredientCalledReturnNewValidNameValidDescription() {
        verify(repoIngredientMock).getById(eq(
                INGREDIENT_NEW_VALID_NAME_DESCRIPTION.getId()),
                getIngredientCallbackCaptor.capture());
        getIngredientCallbackCaptor.getValue().onEntityLoaded(
                INGREDIENT_NEW_VALID_NAME_DESCRIPTION);
    }

    private void verifyRepoPortionsCalledReturnNewValidFourPortions() {
        verify(repoRecipePortionsMock).getPortionsForRecipe(
                eq(QUANTITY_NEW_INVALID.getRecipeId()),
                getRecipePortionsCallbackCaptor.capture());
        getRecipePortionsCallbackCaptor.getValue().onEntityLoaded(
                PORTIONS_NEW_VALID_FOUR);
    }

    private void verifyRepoRecipeIngredientCalledReturnExistingValidMetric() {
        verify(repoRecipeIngredientMock).getById(eq(QUANTITY_EXISTING_VALID_METRIC.getId()),
                getRecipeIngredientCallbackCaptor.capture());
        getRecipeIngredientCallbackCaptor.getValue().onEntityLoaded(QUANTITY_EXISTING_VALID_METRIC);
    }

    private void verifyRepoIngredientCalledAndReturnExistingValidNameDescriptionNoConversionFactor() {
        verify(repoIngredientMock).getById(
                eq(INGREDIENT_EXISTING_VALID_NAME_DESCRIPTION.getId()),
                getIngredientCallbackCaptor.capture());
        getIngredientCallbackCaptor.getValue().onEntityLoaded(
                INGREDIENT_EXISTING_VALID_NAME_DESCRIPTION);
    }

    private void verifyRepoPortionsCalledAndReturnExistingValidNinePortions() {
        verify(repoRecipePortionsMock).getPortionsForRecipe(
                eq(QUANTITY_EXISTING_VALID_METRIC.getRecipeId()),
                getRecipePortionsCallbackCaptor.capture());
        getRecipePortionsCallbackCaptor.getValue().onEntityLoaded(PORTIONS_EXISTING_VALID_NINE);
    }

    private void verifyRepoIngredientCalledAndReturnNewValidName() {
        verify(repoIngredientMock).getById(
                eq(INGREDIENT_NEW_VALID_NAME.getId()),
                getIngredientCallbackCaptor.capture());
        getIngredientCallbackCaptor.getValue().onEntityLoaded(INGREDIENT_NEW_VALID_NAME);
    }

    private void verifyRepoPortionsCalledAndReturnNewValidFourPortions() {
        verify(repoRecipePortionsMock).getPortionsForRecipe(
                eq(QUANTITY_NEW_VALID_METRIC.getRecipeId()),
                getRecipePortionsCallbackCaptor.capture());
        getRecipePortionsCallbackCaptor.getValue().onEntityLoaded(PORTIONS_NEW_VALID_FOUR);
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
        return new MeasurementModel(
                unitOfMeasure.getMeasurementType(),
                unitOfMeasure.getMeasurementSubtype(),
                unitOfMeasure.getNumberOfUnits(),
                unitOfMeasure.isConversionFactorEnabled(),
                unitOfMeasure.getConversionFactor(),
                unitOfMeasure.getItemBaseUnits(),
                unitOfMeasure.getTotalBaseUnits(),
                unitOfMeasure.getNumberOfItems(),
                unitOfMeasure.getTotalUnitOne(),
                unitOfMeasure.getItemUnitOne(),
                unitOfMeasure.getTotalUnitTwo(),
                unitOfMeasure.getItemUnitTwo(),
                unitOfMeasure.isValidMeasurement(),
                unitOfMeasure.getMinUnitOneInBaseUnits(),
                unitOfMeasure.getMaxUnitOne(),
                unitOfMeasure.getMaxUnitTwo(),
                unitOfMeasure.getMaxUnitDigitWidths()
        );
    }
    // endregion helper methods --------------------------------------------------------------------

    // region helper classes -----------------------------------------------------------------------
//    public class UseCaseHandlerMock {
//        private final UseCaseScheduler mUseCaseScheduler;
//
//        UseCaseHandlerMock(UseCaseScheduler useCaseScheduler) {
//            mUseCaseScheduler = useCaseScheduler;
//        }
//
//        <T extends UseCase.RequestValues, R extends UseCase.ResponseValues> void execute(
//                final UseCase<T, R> useCase, T requestValues, UseCase.UseCaseCallback<R> callback) {
//
//            useCase.setRequestValues(requestValues);
//            useCase.setUseCaseCallback(
//                    new UseCaseHandlerMock.UiCallbackWrapper(callback, this));
//
//            mUseCaseScheduler.execute(useCase::run);
//        }
//
//        public <V extends UseCase.ResponseValues> void notifyResponse(
//                final V response,
//                final UseCase.UseCaseCallback<V> useCaseCallback) {
//
//            mUseCaseScheduler.notifyResponse(response, useCaseCallback);
//        }
//
//        private <V extends UseCase.ResponseValues> void notifyError(
//                final V response,
//                final UseCase.UseCaseCallback<V> useCaseCallback) {
//
//            mUseCaseScheduler.onError(response, useCaseCallback);
//        }
//
//        private final class UiCallbackWrapper<V extends UseCase.ResponseValues> implements
//                UseCase.UseCaseCallback<V> {
//
//            private final UseCase.UseCaseCallback<V> mCallback;
//            private final UseCaseHandlerMock mUseCaseHandler;
//
//            public UiCallbackWrapper(UseCase.UseCaseCallback<V> callback,
//                                     UseCaseHandlerMock useCaseHandler) {
//                mCallback = callback;
//                mUseCaseHandler = useCaseHandler;
//            }
//
//            @Override
//            public void onSuccess(V response) {
//                mUseCaseHandler.notifyResponse(response, mCallback);
//            }
//
//            @Override
//            public void onError(V response) {
//                mUseCaseHandler.notifyError(response, mCallback);
//            }
//        }
//    }
    // endregion helper classes --------------------------------------------------------------------
}