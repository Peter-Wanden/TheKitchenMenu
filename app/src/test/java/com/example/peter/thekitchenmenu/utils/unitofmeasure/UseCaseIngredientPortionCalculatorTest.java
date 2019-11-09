package com.example.peter.thekitchenmenu.utils.unitofmeasure;

import com.example.peter.thekitchenmenu.data.entity.IngredientEntity;
import com.example.peter.thekitchenmenu.data.entity.RecipeIngredientQuantityEntity;
import com.example.peter.thekitchenmenu.data.entity.RecipePortionsEntity;
import com.example.peter.thekitchenmenu.data.model.MeasurementModel;
import com.example.peter.thekitchenmenu.data.repository.DataSource;
import com.example.peter.thekitchenmenu.data.repository.RepositoryIngredient;
import com.example.peter.thekitchenmenu.data.repository.RepositoryRecipeIngredient;
import com.example.peter.thekitchenmenu.data.repository.RepositoryRecipePortions;
import com.example.peter.thekitchenmenu.testdata.IngredientEntityTestData;
import com.example.peter.thekitchenmenu.testdata.MeasurementModelTestData;
import com.example.peter.thekitchenmenu.testdata.RecipeIngredientQuantityEntityTestData;
import com.example.peter.thekitchenmenu.testdata.RecipePortionsEntityTestData;
import com.example.peter.thekitchenmenu.utils.TimeProvider;
import com.example.peter.thekitchenmenu.utils.UniqueIdProvider;

import org.junit.*;
import org.mockito.*;

import static com.example.peter.thekitchenmenu.utils.unitofmeasure.UnitOfMeasureConstants.*;
import static com.example.peter.thekitchenmenu.utils.unitofmeasure.UseCaseIngredientPortionCalculator.*;
import static org.mockito.Mockito.*;
import static org.junit.Assert.assertEquals;

public class UseCaseIngredientPortionCalculatorTest {

    // region constants ----------------------------------------------------------------------------
    private final double DELTA = 0.0001;

    private RecipeIngredientQuantityEntity QUANTITY_NEW_INVALID =
            RecipeIngredientQuantityEntityTestData.getNewInvalid();

    private RecipeIngredientQuantityEntity QUANTITY_NEW_VALID_METRIC =
            RecipeIngredientQuantityEntityTestData.getNewValidMetric();

    private RecipeIngredientQuantityEntity QUANTITY_EXISTING_VALID_METRIC =
            RecipeIngredientQuantityEntityTestData.getExistingValidMetric();

    private RecipeIngredientQuantityEntity QUANTITY_EXISTING_VALID_IMPERIAL_SPOON =
            RecipeIngredientQuantityEntityTestData.getExistingValidImperialSpoon();

    private RecipePortionsEntity PORTIONS_NEW_VALID_FOUR =
            RecipePortionsEntityTestData.getNewValidFourPortions();

    private RecipePortionsEntity PORTIONS_EXISTING_VALID_NINE =
            RecipePortionsEntityTestData.getExistingValid();

    private IngredientEntity INGREDIENT_NEW_VALID_NAME =
            IngredientEntityTestData.getNewValidName();

    private IngredientEntity INGREDIENT_NEW_VALID_NAME_MAX_CONVERSION_FACTOR =
            IngredientEntityTestData.getNewValidNameMaxConversionFactor();

    private IngredientEntity INGREDIENT_EXISTING_VALID_NAME_DESCRIPTION =
            IngredientEntityTestData.getExistingValidNameValidDescription();

    private IngredientEntity INGREDIENT_NEW_VALID_NAME_DESCRIPTION =
            IngredientEntityTestData.getNewValidNameValidDescription();

    private RecipeIngredientQuantityEntity QUANTITY_NEW_VALID_MAX_MASS_DIV_FOUR_PORTIONS =
            RecipeIngredientQuantityEntityTestData.getNewValidMetricMaxMassDivFourPortions();

    //----------------------
    private MeasurementModel MEASUREMENT_EMPTY_FOUR_PORTIONS =
            MeasurementModelTestData.getInvalidEmptyFourPortionsSet();
    private MeasurementResult MEASUREMENT_EMPTY_FOUR_PORTIONS_RESULT =
            UseCaseIngredientPortionCalculatorTestData.
                    getResultInvalidEmptyFourPortionsSet();

    //----------------------
    private MeasurementModel MEASUREMENT_INVALID_TOTAL_ONE =
            MeasurementModelTestData.getNewInvalidTotalMeasurementOne();
    private MeasurementResult MEASUREMENT_INVALID_TOTAL_ONE_RESULT =
            UseCaseIngredientPortionCalculatorTestData.getResultNewInvalidTotalMeasurementOne();

    //----------------------
    private MeasurementModel MEASUREMENT_VALID_TOTAL_ONE =
            MeasurementModelTestData.getNewValidTotalMeasurementOne();
    private MeasurementResult MEASUREMENT_VALID_TOTAL_ONE_RESULT =
            UseCaseIngredientPortionCalculatorTestData.getResultNewValidTotalMeasurementOne();

    //----------------------
    private MeasurementModel MEASUREMENT_INVALID_TOTAL_TWO =
            MeasurementModelTestData.getNewInvalidTotalMeasurementTwo();
    private MeasurementResult MEASUREMENT_INVALID_TOTAL_TWO_RESULT =
            UseCaseIngredientPortionCalculatorTestData.getResultNewInvalidTotalMeasurementTwo();

    //----------------------
    private MeasurementModel MEASUREMENT_VALID_TOTAL_TWO =
            MeasurementModelTestData.getNewValidTotalMeasurementTwo();
    private MeasurementResult MEASUREMENT_VALID_TOTAL_TWO_RESULT =
            UseCaseIngredientPortionCalculatorTestData.getResultNewValidTotalMeasurementTwo();

    //----------------------
    private MeasurementModel MEASUREMENT_UNIT_OF_MEASURE_CHANGED_IMPERIAL_SPOON =
            MeasurementModelTestData.getNewInvalidUnitOfMeasureChangedImperialSpoon();
    private MeasurementResult MEASUREMENT_UNIT_OF_MEASURE_CHANGED_IMPERIAL_SPOON_RESULT =
            UseCaseIngredientPortionCalculatorTestData.
                    getResultNewInvalidUnitOfMeasureChangedImperialSpoon();

    //----------------------
    private MeasurementModel MEASUREMENT_NEW_VALID_HALF_IMPERIAL_SPOON_UNIT_ONE_UPDATED =
            MeasurementModelTestData.getNewValidHalfImperialSpoonUnitOneUpdated();
    private MeasurementResult MEASUREMENT_NEW_VALID_HALF_IMPERIAL_SPOON_UNIT_ONE_UPDATED_RESULT =
            UseCaseIngredientPortionCalculatorTestData.
                    getNewValidHalfImperialSpoonUnitOneUpdatedResult();

    //----------------------
    private MeasurementModel MEASUREMENT_INVALID_CONVERSION_FACTOR =
            MeasurementModelTestData.getNewInvalidConversionFactor();
    private MeasurementResult MEASUREMENT_INVALID_CONVERSION_FACTOR_RESULT =
            UseCaseIngredientPortionCalculatorTestData.getResultNewInvalidConversionFactor();

    //----------------------
    private MeasurementModel MEASUREMENT_VALID_MAX_CONVERSION_FACTOR =
            MeasurementModelTestData.getNewValidImperialSpoonWithConversionFactor();
    private MeasurementResult MEASUREMENT_VALID_MAX_CONVERSION_FACTOR_RESULT =
            UseCaseIngredientPortionCalculatorTestData.
                    getResultNewValidImperialSpoonWithConversionFactor();

    //----------------------
    private MeasurementModel MEASUREMENT_EXISTING_VALID_METRIC =
            MeasurementModelTestData.getExistingValidMetric();
    private MeasurementResult MEASUREMENT_EXISTING_VALID_METRIC_RESULT =
            UseCaseIngredientPortionCalculatorTestData.getResultExistingValidMetric();

    //----------------------
    private MeasurementModel MEASUREMENT_EXISTING_INVALID_TOTAL_ONE =
            MeasurementModelTestData.getExistingMetricInvalidTotalOne();
    private MeasurementResult MEASUREMENT_EXISTING_INVALID_TOTAL_ONE_RESULT =
            UseCaseIngredientPortionCalculatorTestData.getExistingMetricInvalidTotalOneResult();

    //----------------------
    private MeasurementModel MEASUREMENT_EXISTING_INVALID_TOTAL_TWO =
            MeasurementModelTestData.getExistingMetricInvalidTotalTwo();
    private MeasurementResult MEASUREMENT_EXISTING_INVALID_TOTAL_TWO_RESULT =
            UseCaseIngredientPortionCalculatorTestData.getExistingInvalidTotalTwoResult();

    //----------------------
    private MeasurementModel MEASUREMENT_EXISTING_VALID_TOTAL_TWO_UPDATED =
            MeasurementModelTestData.getExistingMetricValidTwoUpdated();
    private MeasurementResult MEASUREMENT_EXISTING_VALID_TOTAL_TWO_UPDATED_RESULT =
            UseCaseIngredientPortionCalculatorTestData.getExistingMetricValidTwoUpdatedResult();

    //----------------------
    private MeasurementModel MEASUREMENT_EXISTING_INVALID_UNIT_OF_MEASURE_CHANGED =
            MeasurementModelTestData.getExistingMetricUnitOfMeasureUpdatedToImperial();
    private MeasurementResult MEASUREMENT_EXISTING_INVALID_UNIT_OF_MEASURE_CHANGED_RESULT =
            UseCaseIngredientPortionCalculatorTestData.
                    getExistingMetricUnitOfMeasureUpdatedToImperialResult();

    //----------------------
    private MeasurementModel MEASUREMENT_EXISTING_INVALID_CONVERSION_FACTOR =
            MeasurementModelTestData.getExistingImperialSpoonInvalidConversionFactor();
    private MeasurementResult MEASUREMENT_EXISTING_INVALID_CONVERSION_FACTOR_RESULT =
            UseCaseIngredientPortionCalculatorTestData.
                    getExistingMetricInvalidConversionFactorResult();

    // endregion constants -------------------------------------------------------------------------
    // region helper fields ------------------------------------------------------------------------
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
    @Mock
    UseCasePortionCallback viewModelMock;
    @Captor
    ArgumentCaptor<MeasurementResult> resultArgumentCaptor;
    @Captor
    ArgumentCaptor<RecipeIngredientQuantityEntity> recipeIngredientCaptor;
    @Captor
    ArgumentCaptor<IngredientEntity> ingredientArgumentCaptor;
    // endregion helper fields ---------------------------------------------------------------------

    private UseCaseIngredientPortionCalculator SUT;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        SUT = new UseCaseIngredientPortionCalculator(
                repoRecipePortionsMock,
                repoRecipeIngredientMock,
                repoIngredientMock,
                idProviderMock,
                timeProviderMock);

        SUT.registerListener(viewModelMock);
    }

    @Test
    public void startNewRecipeAndIngredientId_databaseNotCalled() {
        // Arrange
        when(idProviderMock.getUId()).thenReturn(QUANTITY_NEW_INVALID.getId());
        // Act
        SUT.start(QUANTITY_NEW_INVALID.getRecipeId(), QUANTITY_NEW_INVALID.getIngredientId());
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
        SUT.start(QUANTITY_NEW_INVALID.getRecipeId(), QUANTITY_NEW_INVALID.getIngredientId());
        verifyRepoIngredientCalledReturnNewValidNameValidDescription();
        verifyRepoPortionsCalledReturnNewValidFourPortions();
        // Assert incomplete model returned
        verify(viewModelMock).useCasePortionResult(eq(MEASUREMENT_EMPTY_FOUR_PORTIONS_RESULT));
    }

    @Test
    public void startNewRecipeAndIngredientId_invalidTotalMeasurementOneUpdated_notSaved() {
        // Arrange
        whenIdProviderReturnNewValidId();
        whenTimeProviderThenReturnNewValidTime();

        // Act
        SUT.start(QUANTITY_NEW_VALID_METRIC.getRecipeId(),
                QUANTITY_NEW_VALID_METRIC.getIngredientId());
        verifyRepoIngredientCalledAndReturnNewValidName();
        verifyRepoPortionsCalledAndReturnNewValidFourPortions();

        SUT.processModel(MEASUREMENT_INVALID_TOTAL_ONE);
        // Assert
        verifyNoMoreInteractions(repoRecipeIngredientMock);
    }

    @Test
    public void startNewRecipeAndIngredientId_invalidTotalMeasurementOneUpdated_INVALID_TOTAL_MEASUREMENT_ONE() {
        // Arrange
        whenIdProviderReturnNewValidId();
        whenTimeProviderThenReturnNewValidTime();

        // Act
        SUT.start(QUANTITY_NEW_VALID_METRIC.getRecipeId(),
                QUANTITY_NEW_VALID_METRIC.getIngredientId());
        verifyRepoIngredientCalledAndReturnNewValidName();
        verifyRepoPortionsCalledAndReturnNewValidFourPortions();

        SUT.processModel(MEASUREMENT_INVALID_TOTAL_ONE);
        // Assert
        verify(viewModelMock, times((2))).useCasePortionResult(resultArgumentCaptor.capture());
        MeasurementResult actualResult = resultArgumentCaptor.getValue();
        assertEquals(MEASUREMENT_INVALID_TOTAL_ONE_RESULT, actualResult);
    }

    @Test
    public void startNewRecipeAndIngredientId_validTotalMeasurementOneUpdated_saved() {
        // Arrange
        whenIdProviderReturnNewValidId();
        whenTimeProviderThenReturnNewValidTime();
        // Act
        SUT.start(QUANTITY_NEW_VALID_METRIC.getRecipeId(),
                QUANTITY_NEW_VALID_METRIC.getIngredientId());
        verifyRepoIngredientCalledAndReturnNewValidName();
        verifyRepoPortionsCalledAndReturnNewValidFourPortions();

        SUT.processModel(MEASUREMENT_VALID_TOTAL_ONE);

        // Assert
        verify(repoRecipeIngredientMock).save(eq(QUANTITY_NEW_VALID_METRIC));
    }

    @Test
    public void startNewRecipeAndIngredientId_validTotalMeasurementOneUpdated_RESULT_OK() {
        // Arrange
        whenIdProviderReturnNewValidId();
        whenTimeProviderThenReturnNewValidTime();

        // Act
        SUT.start(QUANTITY_NEW_VALID_METRIC.getRecipeId(),
                QUANTITY_NEW_VALID_METRIC.getIngredientId());
        verifyRepoIngredientCalledAndReturnNewValidName();
        verifyRepoPortionsCalledAndReturnNewValidFourPortions();

        SUT.processModel(MEASUREMENT_VALID_TOTAL_ONE);
        // Assert
        verify(viewModelMock, times((2))).useCasePortionResult(resultArgumentCaptor.capture());
        MeasurementResult actualResult = resultArgumentCaptor.getValue();
        assertEquals(MEASUREMENT_VALID_TOTAL_ONE_RESULT, actualResult);
    }

    @Test
    public void startNewRecipeAndIngredientId_invalidTotalMeasurementTwoUpdated_notSaved() {
        // Arrange
        whenIdProviderReturnNewValidId();
        whenTimeProviderThenReturnNewValidTime();

        // Act
        SUT.start(QUANTITY_NEW_VALID_METRIC.getRecipeId(),
                QUANTITY_NEW_VALID_METRIC.getIngredientId());
        verifyRepoIngredientCalledAndReturnNewValidName();
        verifyRepoPortionsCalledAndReturnNewValidFourPortions();

        SUT.processModel(MEASUREMENT_INVALID_TOTAL_TWO);
        // Assert
        verifyNoMoreInteractions(repoRecipeIngredientMock);
    }

    @Test
    public void startNewRecipeAndIngredientId_invalidTotalMeasurementTwoUpdated_INVALID_TOTAL_MEASUREMENT_TWO() {
        // Arrange
        whenIdProviderReturnNewValidId();
        whenTimeProviderThenReturnNewValidTime();

        // Act
        SUT.start(QUANTITY_NEW_VALID_METRIC.getRecipeId(),
                QUANTITY_NEW_VALID_METRIC.getIngredientId());
        verifyRepoIngredientCalledAndReturnNewValidName();
        verifyRepoPortionsCalledAndReturnNewValidFourPortions();

        SUT.processModel(MEASUREMENT_INVALID_TOTAL_TWO);
        // Assert
        verify(viewModelMock, times((2))).useCasePortionResult(resultArgumentCaptor.capture());
        assertEquals(MEASUREMENT_INVALID_TOTAL_TWO_RESULT, resultArgumentCaptor.getValue());
    }

    @Test
    public void startNewRecipeAndIngredientId_validTotalMeasurementTwoUpdated_saved() {
        // Arrange
        whenIdProviderReturnNewValidId();
        whenTimeProviderThenReturnNewValidTime();

        // Act
        SUT.start(QUANTITY_NEW_VALID_METRIC.getRecipeId(),
                QUANTITY_NEW_VALID_METRIC.getIngredientId());
        verifyRepoIngredientCalledAndReturnNewValidName();
        verifyRepoPortionsCalledAndReturnNewValidFourPortions();

        SUT.processModel(MEASUREMENT_VALID_TOTAL_TWO);
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
        SUT.start(QUANTITY_NEW_VALID_METRIC.getRecipeId(),
                QUANTITY_NEW_VALID_METRIC.getIngredientId());
        verifyRepoIngredientCalledAndReturnNewValidName();
        verifyRepoPortionsCalledAndReturnNewValidFourPortions();

        SUT.processModel(MEASUREMENT_VALID_TOTAL_TWO);
        // Assert
        verify(viewModelMock, times((2))).useCasePortionResult(resultArgumentCaptor.capture());
        MeasurementResult actualResult = resultArgumentCaptor.getValue();
        assertEquals(MEASUREMENT_VALID_TOTAL_TWO_RESULT, actualResult);
        assertEquals(ResultStatus.RESULT_OK, actualResult.getResult());
    }

    @Test
    public void startNewRecipeAndIngredientId_unitOfMeasureChanged_INVALID_MEASUREMENT() {
        // Arrange
        whenIdProviderReturnNewValidId();
        whenTimeProviderThenReturnNewValidTime();

        // Act
        SUT.start(QUANTITY_NEW_VALID_METRIC.getRecipeId(),
                QUANTITY_NEW_VALID_METRIC.getIngredientId());
        verifyRepoIngredientCalledAndReturnNewValidName();
        verifyRepoPortionsCalledAndReturnNewValidFourPortions();

        SUT.processModel(MEASUREMENT_UNIT_OF_MEASURE_CHANGED_IMPERIAL_SPOON);
        // Assert
        verify(viewModelMock, times((2))).useCasePortionResult(resultArgumentCaptor.capture());
        MeasurementResult actualResult = resultArgumentCaptor.getValue();
        assertEquals(MEASUREMENT_UNIT_OF_MEASURE_CHANGED_IMPERIAL_SPOON_RESULT, actualResult);
        assertEquals(ResultStatus.INVALID_MEASUREMENT, actualResult.getResult());
    }

    @Test
    public void startNewRecipeAndIngredientId_unitOfMeasureChanged_emptyModel() {
        // Arrange
        whenIdProviderReturnNewValidId();
        whenTimeProviderThenReturnNewValidTime();
        // Act
        SUT.start(QUANTITY_NEW_VALID_METRIC.getRecipeId(),
                QUANTITY_NEW_VALID_METRIC.getIngredientId());
        verifyRepoIngredientCalledAndReturnNewValidName();
        verifyRepoPortionsCalledAndReturnNewValidFourPortions();

        SUT.processModel(MEASUREMENT_UNIT_OF_MEASURE_CHANGED_IMPERIAL_SPOON);
        // Assert
        verify(viewModelMock, times((2))).useCasePortionResult(resultArgumentCaptor.capture());
        MeasurementResult actualResult = resultArgumentCaptor.getValue();
        assertEquals(MEASUREMENT_UNIT_OF_MEASURE_CHANGED_IMPERIAL_SPOON_RESULT, actualResult);

        MeasurementModel actualModel = actualResult.getModel();
        assertEquals(MEASUREMENT_UNIT_OF_MEASURE_CHANGED_IMPERIAL_SPOON_RESULT.getModel(), actualModel);
    }

    @Test
    public void startNewRecipeAndIngredientId_unitOfMeasureChangedImperialSpoon_userWalkThrough() {
        // Arrange
        whenIdProviderReturnNewValidId();
        whenTimeProviderThenReturnNewValidTime();
        // Act
        SUT.start(QUANTITY_NEW_VALID_METRIC.getRecipeId(),
                QUANTITY_NEW_VALID_METRIC.getIngredientId());
        verifyRepoIngredientCalledAndReturnNewValidName();
        verifyRepoPortionsCalledAndReturnNewValidFourPortions();

        // Start of user interaction //
        // Change unit of measure
        SUT.processModel(MEASUREMENT_UNIT_OF_MEASURE_CHANGED_IMPERIAL_SPOON);
        // Assert
        verify(viewModelMock, times((2))).useCasePortionResult(resultArgumentCaptor.capture());
        MeasurementResult unitOfMeasureResult = resultArgumentCaptor.getValue();
        assertEquals(MEASUREMENT_UNIT_OF_MEASURE_CHANGED_IMPERIAL_SPOON_RESULT, unitOfMeasureResult);

        // Change measurement one
        SUT.processModel(MEASUREMENT_NEW_VALID_HALF_IMPERIAL_SPOON_UNIT_ONE_UPDATED);
        verify(viewModelMock, times((3))).useCasePortionResult(resultArgumentCaptor.capture());
        MeasurementResult halfSpoonResult = resultArgumentCaptor.getValue();
        assertEquals(MEASUREMENT_NEW_VALID_HALF_IMPERIAL_SPOON_UNIT_ONE_UPDATED_RESULT, halfSpoonResult);
    }

    @Test
    public void startNewRecipeAndIngredientId_invalidConversionFactor_notSaved() {
        // Arrange
        whenIdProviderReturnNewValidId();
        whenTimeProviderThenReturnNewValidTime();
        // Act
        SUT.start(QUANTITY_NEW_VALID_METRIC.getRecipeId(),
                QUANTITY_NEW_VALID_METRIC.getIngredientId());
        verifyRepoIngredientCalledAndReturnNewValidName();
        verifyRepoPortionsCalledAndReturnNewValidFourPortions();

        SUT.processModel(MEASUREMENT_INVALID_CONVERSION_FACTOR);
        // Assert
        verifyNoMoreInteractions(repoRecipeIngredientMock);
    }

    @Test
    public void startNewRecipeAndIngredientId_invalidConversionFactor_INVALID_CONVERSION_FACTOR() {
        // Arrange
        whenIdProviderReturnNewValidId();
        whenTimeProviderThenReturnNewValidTime();

        // Act
        SUT.start(QUANTITY_NEW_VALID_METRIC.getRecipeId(),
                QUANTITY_NEW_VALID_METRIC.getIngredientId());
        verifyRepoIngredientCalledAndReturnNewValidName();
        verifyRepoPortionsCalledAndReturnNewValidFourPortions();

        SUT.processModel(MEASUREMENT_INVALID_CONVERSION_FACTOR);
        // Assert
        verify(viewModelMock, times((2))).useCasePortionResult(resultArgumentCaptor.capture());
        MeasurementResult actualResult = resultArgumentCaptor.getValue();
        assertEquals(MEASUREMENT_INVALID_CONVERSION_FACTOR_RESULT, actualResult);
        MeasurementResult result = resultArgumentCaptor.getValue();
        assertEquals(ResultStatus.INVALID_CONVERSION_FACTOR, result.getResult());
    }

    @Test
    public void startNewRecipeAndIngredientId_validConversionFactor_saved() {
        // Arrange
        whenIdProviderReturnNewValidId();
        whenTimeProviderThenReturnNewValidTime();
        // Act
        SUT.start(QUANTITY_NEW_VALID_METRIC.getRecipeId(),
                QUANTITY_NEW_VALID_METRIC.getIngredientId());
        verifyRepoIngredientCalledAndReturnNewValidName();
        verifyRepoPortionsCalledAndReturnNewValidFourPortions();

        SUT.processModel(MEASUREMENT_UNIT_OF_MEASURE_CHANGED_IMPERIAL_SPOON);
        SUT.processModel(MEASUREMENT_VALID_MAX_CONVERSION_FACTOR); // setConversion factor
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
        SUT.start(QUANTITY_NEW_VALID_METRIC.getRecipeId(),
                QUANTITY_NEW_VALID_METRIC.getIngredientId());
        verifyRepoIngredientCalledAndReturnNewValidName();
        verifyRepoPortionsCalledAndReturnNewValidFourPortions();

        SUT.processModel(MEASUREMENT_UNIT_OF_MEASURE_CHANGED_IMPERIAL_SPOON);
        SUT.processModel(MEASUREMENT_VALID_MAX_CONVERSION_FACTOR); // setConversion factor
        SUT.processModel(MEASUREMENT_VALID_MAX_CONVERSION_FACTOR); // setMeasurementOne
        SUT.processModel(MEASUREMENT_VALID_MAX_CONVERSION_FACTOR); // setMeasurementTwo
        // Assert
        verify(repoRecipeIngredientMock, times((2))).save(recipeIngredientCaptor.capture());
        verify(viewModelMock, times((5))).useCasePortionResult(resultArgumentCaptor.capture());
        MeasurementResult actualResult = resultArgumentCaptor.getValue();
        assertEquals(MEASUREMENT_VALID_MAX_CONVERSION_FACTOR_RESULT, actualResult);
    }

    @Test
    public void startNewRecipeAndIngredientId_validConversionFactor_RESULT_OK() {
        whenIdProviderReturnNewValidId();
        whenTimeProviderThenReturnNewValidTime();
        // Act
        SUT.start(QUANTITY_NEW_VALID_METRIC.getRecipeId(),
                QUANTITY_NEW_VALID_METRIC.getIngredientId());
        verifyRepoIngredientCalledAndReturnNewValidName();
        verifyRepoPortionsCalledAndReturnNewValidFourPortions();

        SUT.processModel(MEASUREMENT_UNIT_OF_MEASURE_CHANGED_IMPERIAL_SPOON);
        SUT.processModel(MEASUREMENT_VALID_MAX_CONVERSION_FACTOR); // setConversion factor
        SUT.processModel(MEASUREMENT_VALID_MAX_CONVERSION_FACTOR); // setMeasurementOne
        SUT.processModel(MEASUREMENT_VALID_MAX_CONVERSION_FACTOR); // setMeasurementTwo
        // Assert
        verify(repoRecipeIngredientMock, times((2))).save(recipeIngredientCaptor.capture());
        verify(viewModelMock, times((5))).useCasePortionResult(resultArgumentCaptor.capture());
        MeasurementResult actualResult = resultArgumentCaptor.getValue();
        assertEquals(MEASUREMENT_VALID_MAX_CONVERSION_FACTOR_RESULT, actualResult);
        assertEquals(ResultStatus.RESULT_OK, actualResult.getResult());
    }

    @Test
    public void startExistingRecipeIngredientId_entitiesLoaded() {
        // Arrange
        // Act
        SUT.start(QUANTITY_EXISTING_VALID_METRIC.getId());
        // Assert
        verifyRepoRecipeIngredientCalledReturnExistingValidMetric();
        verifyRepoIngredientCalledAndReturnExistingValidNameDescription();
        verifyRepoPortionsCalledAndReturnExistingValidNinePortions();
    }

    @Test
    public void startExistingRecipeIngredientId_expectedResultSentToViewModel() {
        // Arrange
        // Act
        SUT.start(QUANTITY_EXISTING_VALID_METRIC.getId());
        // Assert
        verifyRepoRecipeIngredientCalledReturnExistingValidMetric();
        verifyRepoIngredientCalledAndReturnExistingValidNameDescription();
        verifyRepoPortionsCalledAndReturnExistingValidNinePortions();

        verify(viewModelMock).useCasePortionResult(resultArgumentCaptor.capture());
        MeasurementResult actualResult = resultArgumentCaptor.getValue();
        assertEquals(MEASUREMENT_EXISTING_VALID_METRIC_RESULT, actualResult);
    }

    @Test
    public void startExistingRecipeIngredientId_invalidTotalMeasurementOneUpdated_onlyValidValueSaved() {
        // Arrange
        // Act
        SUT.start(QUANTITY_EXISTING_VALID_METRIC.getId());
        verifyRepoRecipeIngredientCalledReturnExistingValidMetric();
        verifyRepoIngredientCalledAndReturnExistingValidNameDescription();
        verifyRepoPortionsCalledAndReturnExistingValidNinePortions();

        SUT.processModel(MEASUREMENT_EXISTING_INVALID_TOTAL_ONE);

        // Assert
        verify(viewModelMock, times((2))).useCasePortionResult(resultArgumentCaptor.capture());
        MeasurementResult actualResult = resultArgumentCaptor.getValue();
        assertEquals(MEASUREMENT_EXISTING_INVALID_TOTAL_ONE_RESULT, actualResult);

        verify(repoRecipeIngredientMock).save(recipeIngredientCaptor.capture());
        RecipeIngredientQuantityEntity quantityEntity = recipeIngredientCaptor.getValue();

        assertEquals(MEASUREMENT_EXISTING_INVALID_TOTAL_ONE_RESULT.getModel().getItemBaseUnits(),
                quantityEntity.getItemBaseUnits(), DELTA);
    }

    @Test
    public void startExistingRecipeIngredientId_invalidTotalMeasurementTwoUpdated_INVALID_TOTAL_MEASUREMENT_TWO() {
        // Arrange
        // Act
        SUT.start(QUANTITY_EXISTING_VALID_METRIC.getId());
        verifyRepoRecipeIngredientCalledReturnExistingValidMetric();
        verifyRepoIngredientCalledAndReturnExistingValidNameDescription();
        verifyRepoPortionsCalledAndReturnExistingValidNinePortions();

        SUT.processModel(MEASUREMENT_EXISTING_INVALID_TOTAL_TWO);
        // Assert
        verify(viewModelMock, times((2))).useCasePortionResult(resultArgumentCaptor.capture());
        MeasurementResult actualResult = resultArgumentCaptor.getValue();
        assertEquals(MEASUREMENT_EXISTING_INVALID_TOTAL_TWO_RESULT, actualResult);

        ResultStatus resultStatus = actualResult.getResult();
        assertEquals(ResultStatus.INVALID_TOTAL_MEASUREMENT_TWO, resultStatus);

        verify(repoRecipeIngredientMock).save(recipeIngredientCaptor.capture());
        RecipeIngredientQuantityEntity quantityEntity = recipeIngredientCaptor.getValue();
        assertEquals(MEASUREMENT_EXISTING_INVALID_TOTAL_TWO_RESULT.getModel().getItemBaseUnits(),
                quantityEntity.getItemBaseUnits(), DELTA);
    }

    @Test
    public void startExistingRecipeIngredientId_validTotalMeasurementTwoUpdated_saved() {
        // Arrange
        // Act
        SUT.start(QUANTITY_EXISTING_VALID_METRIC.getId());

        verifyRepoRecipeIngredientCalledReturnExistingValidMetric();
        verifyRepoIngredientCalledAndReturnExistingValidNameDescription();
        verifyRepoPortionsCalledAndReturnExistingValidNinePortions();

        SUT.processModel(MEASUREMENT_EXISTING_VALID_TOTAL_TWO_UPDATED);
        // Assert
        verify(repoRecipeIngredientMock).save(recipeIngredientCaptor.capture());
        RecipeIngredientQuantityEntity saveResult = recipeIngredientCaptor.getValue();
        double expectedItemBaseUnits = MEASUREMENT_EXISTING_VALID_TOTAL_TWO_UPDATED_RESULT.
                getModel().getItemBaseUnits();
        double actualItemBseUnits = saveResult.getItemBaseUnits();
        assertEquals(expectedItemBaseUnits, actualItemBseUnits, DELTA);
    }

    @Test
    public void startExistingRecipeIngredientId_validTotalMeasurementTwoUpdated_RESULT_OK() {
        // Arrange
        // Act
        SUT.start(QUANTITY_EXISTING_VALID_METRIC.getId());
        verifyRepoRecipeIngredientCalledReturnExistingValidMetric();
        verifyRepoIngredientCalledAndReturnExistingValidNameDescription();
        verifyRepoPortionsCalledAndReturnExistingValidNinePortions();

        SUT.processModel(MEASUREMENT_EXISTING_VALID_TOTAL_TWO_UPDATED);
        // Assert
        verify(viewModelMock, times((2))).useCasePortionResult(resultArgumentCaptor.capture());
        ResultStatus resultStatus = resultArgumentCaptor.getValue().getResult();
        assertEquals(ResultStatus.RESULT_OK, resultStatus);
    }

    @Test
    public void startExistingRecipeIngredientId_unitOfMeasureChanged_INVALID_MEASUREMENT() {
        // Arrange
        // Act
        SUT.start(QUANTITY_EXISTING_VALID_METRIC.getId());
        // Assert
        verifyRepoRecipeIngredientCalledReturnExistingValidMetric();
        verifyRepoIngredientCalledAndReturnExistingValidNameDescription();
        verifyRepoPortionsCalledAndReturnExistingValidNinePortions();

        SUT.processModel(MEASUREMENT_EXISTING_INVALID_UNIT_OF_MEASURE_CHANGED);
        // Assert
        verify(viewModelMock, times((2))).useCasePortionResult(resultArgumentCaptor.capture());
        ResultStatus resultStatus = resultArgumentCaptor.getValue().getResult();
        assertEquals(ResultStatus.INVALID_MEASUREMENT, resultStatus);
    }

    @Test
    public void startExistingRecipeIngredientId_unitOfMeasureChanged_emptyModel() {
        // Arrange
        // Act
        SUT.start(QUANTITY_EXISTING_VALID_METRIC.getId());
        // Assert
        verifyRepoRecipeIngredientCalledReturnExistingValidMetric();
        verifyRepoIngredientCalledAndReturnExistingValidNameDescription();
        verifyRepoPortionsCalledAndReturnExistingValidNinePortions();

        SUT.processModel(MEASUREMENT_EXISTING_INVALID_UNIT_OF_MEASURE_CHANGED);
        // Assert
        verify(viewModelMock, times((2))).useCasePortionResult(resultArgumentCaptor.capture());
        MeasurementModel actualModel = resultArgumentCaptor.getValue().getModel();
        assertEquals(MEASUREMENT_EXISTING_INVALID_UNIT_OF_MEASURE_CHANGED_RESULT.getModel(),
                actualModel);
    }

    @Test
    public void startExistingRecipeIngredientId_invalidConversionFactor_notSaved() {
        // Arrange
        // Act
        SUT.start(QUANTITY_EXISTING_VALID_METRIC.getId());
        // Assert
        verifyRepoRecipeIngredientCalledReturnExistingValidMetric();
        verifyRepoIngredientCalledAndReturnExistingValidNameDescription();
        verifyRepoPortionsCalledAndReturnExistingValidNinePortions();

        SUT.processModel(MEASUREMENT_EXISTING_INVALID_CONVERSION_FACTOR);
        // Assert
        verifyNoMoreInteractions(repoRecipeIngredientMock);
    }

    @Test
    public void startExistingRecipeIngredientId_invalidConversionFactor_INVALID_CONVERSION_FACTOR() {
        // Arrange
        // Act
        SUT.start(QUANTITY_EXISTING_VALID_METRIC.getId());
        // Assert
        verifyRepoRecipeIngredientCalledReturnExistingValidMetric();
        verifyRepoIngredientCalledAndReturnExistingValidNameDescription();
        verifyRepoPortionsCalledAndReturnExistingValidNinePortions();

        SUT.processModel(MEASUREMENT_EXISTING_INVALID_CONVERSION_FACTOR);
        // Assert
        verify(viewModelMock, times((2))).useCasePortionResult(resultArgumentCaptor.capture());
        ResultStatus actualResultStatus = resultArgumentCaptor.getValue().getResult();
        assertEquals(ResultStatus.INVALID_CONVERSION_FACTOR, actualResultStatus);
    }

    @Test
    public void startExistingRecipeIngredientId_invalidConversionFactor_measurementValuesNotChanged() {
        // Act
        SUT.start(QUANTITY_EXISTING_VALID_IMPERIAL_SPOON.getId());
        // Assert

        verifyRepoRecipeIngredientCalledReturnExistingValidMetric();
        verifyRepoIngredientCalledAndReturnExistingValidNameDescription();
        verifyRepoPortionsCalledAndReturnExistingValidNinePortions();

        SUT.processModel(MEASUREMENT_EXISTING_INVALID_CONVERSION_FACTOR);
        // Assert
        verify(viewModelMock, times((2))).useCasePortionResult(resultArgumentCaptor.capture());
        MeasurementResult actualResult = resultArgumentCaptor.getValue();
        assertEquals(MEASUREMENT_EXISTING_INVALID_CONVERSION_FACTOR_RESULT, actualResult);

        MeasurementModel actualModel = resultArgumentCaptor.getValue().getModel();
        assertEquals(MEASUREMENT_EXISTING_INVALID_CONVERSION_FACTOR, actualModel);
    }

    @Test
    public void startExistingRecipeIngredientId_userUpdateExistingFlow_allValuesAsExpected() {
        // Arrange
        int portions = MEASUREMENT_EXISTING_VALID_METRIC.getNumberOfItems();
        double numberOfTeaspoons = 2;
        double teaspoonVolume = IMPERIAL_SPOON_UNIT_ONE;
        double itemBaseUnits = (numberOfTeaspoons * teaspoonVolume / portions) *
                MAX_CONVERSION_FACTOR;

        when(timeProviderMock.getCurrentTimeInMills()).thenReturn(
                QUANTITY_EXISTING_VALID_METRIC.getCreateDate(),
                INGREDIENT_EXISTING_VALID_NAME_DESCRIPTION.getLastUpdate(),
                QUANTITY_EXISTING_VALID_METRIC.getLastUpdate());

        MeasurementSubtype subtype = MeasurementSubtype.fromInt(
                QUANTITY_EXISTING_VALID_METRIC.getUnitOfMeasureSubtype());

        UnitOfMeasure unitOfMeasureStartValues = subtype.getMeasurementClass();
        unitOfMeasureStartValues.numberOfItemsIsSet(portions);
        unitOfMeasureStartValues.itemBaseUnitsAreSet(QUANTITY_EXISTING_VALID_METRIC.getItemBaseUnits());
        MeasurementModel expectedModelLoaded = getMeasurementModel(unitOfMeasureStartValues);

        MeasurementModel requestUnitOfMeasureChangeFromUi = new MeasurementModel(
                unitOfMeasureStartValues.getMeasurementType(),
                MeasurementSubtype.IMPERIAL_SPOON,
                unitOfMeasureStartValues.getNumberOfMeasurementUnits(),
                unitOfMeasureStartValues.isConversionFactorEnabled(),
                unitOfMeasureStartValues.getConversionFactor(),
                unitOfMeasureStartValues.getItemBaseUnits(),
                unitOfMeasureStartValues.getTotalBaseUnits(),
                unitOfMeasureStartValues.getNumberOfItems(),
                unitOfMeasureStartValues.getTotalMeasurementOne(),
                unitOfMeasureStartValues.getItemMeasurementOne(),
                unitOfMeasureStartValues.getTotalMeasurementTwo(),
                unitOfMeasureStartValues.getItemMeasurementTwo(),
                unitOfMeasureStartValues.isValidMeasurement(),
                unitOfMeasureStartValues.getMinimumMeasurementOne(),
                unitOfMeasureStartValues.getMaximumMeasurementOne(),
                unitOfMeasureStartValues.getMaximumMeasurementTwo(),
                unitOfMeasureStartValues.getMeasurementUnitsDigitWidths()
        );

        UnitOfMeasure unitOfMeasureChangeToSpoonValues =
                MeasurementSubtype.IMPERIAL_SPOON.getMeasurementClass();
        unitOfMeasureChangeToSpoonValues.numberOfItemsIsSet(
                unitOfMeasureStartValues.getNumberOfItems());
        unitOfMeasureChangeToSpoonValues.conversionFactorIsSet(
                unitOfMeasureStartValues.getConversionFactor());

        MeasurementModel expectedResultFromUnitOfMeasureChange =
                getMeasurementModel(unitOfMeasureChangeToSpoonValues);

        MeasurementModel measurementOneChangeFromUi = new MeasurementModel(
                unitOfMeasureChangeToSpoonValues.getMeasurementType(),
                unitOfMeasureChangeToSpoonValues.getMeasurementSubtype(),
                unitOfMeasureChangeToSpoonValues.getNumberOfMeasurementUnits(),
                unitOfMeasureChangeToSpoonValues.isConversionFactorEnabled(),
                unitOfMeasureChangeToSpoonValues.getConversionFactor(),
                unitOfMeasureChangeToSpoonValues.getItemBaseUnits(),
                unitOfMeasureChangeToSpoonValues.getTotalBaseUnits(),
                unitOfMeasureChangeToSpoonValues.getNumberOfItems(),
                numberOfTeaspoons,
                unitOfMeasureChangeToSpoonValues.getItemMeasurementOne(),
                unitOfMeasureChangeToSpoonValues.getTotalMeasurementTwo(),
                unitOfMeasureChangeToSpoonValues.getItemMeasurementTwo(),
                unitOfMeasureChangeToSpoonValues.isValidMeasurement(),
                unitOfMeasureChangeToSpoonValues.getMinimumMeasurementOne(),
                unitOfMeasureChangeToSpoonValues.getMaximumMeasurementOne(),
                unitOfMeasureChangeToSpoonValues.getMaximumMeasurementTwo(),
                unitOfMeasureChangeToSpoonValues.getMeasurementUnitsDigitWidths()
        );

        unitOfMeasureChangeToSpoonValues.totalMeasurementOneIsSet(
                measurementOneChangeFromUi.getTotalMeasurementOne());

        MeasurementModel expectedResultFromMeasurementOneChange =
                getMeasurementModel(unitOfMeasureChangeToSpoonValues);

        RecipeIngredientQuantityEntity expectedQuantityEntitySaveAfterMeasurementOneChange =
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

        MeasurementModel conversionFactorChangeFromUi = new MeasurementModel(
                expectedResultFromMeasurementOneChange.getType(),
                expectedResultFromMeasurementOneChange.getSubtype(),
                expectedResultFromMeasurementOneChange.getNumberOfMeasurementUnits(),
                expectedResultFromMeasurementOneChange.isConversionFactorEnabled(),
                MAX_CONVERSION_FACTOR,
                expectedResultFromMeasurementOneChange.getItemBaseUnits(),
                expectedResultFromMeasurementOneChange.getTotalBaseUnits(),
                expectedResultFromMeasurementOneChange.getNumberOfItems(),
                expectedResultFromMeasurementOneChange.getTotalMeasurementOne(),
                expectedResultFromMeasurementOneChange.getItemMeasurementOne(),
                expectedResultFromMeasurementOneChange.getTotalMeasurementTwo(),
                expectedResultFromMeasurementOneChange.getItemMeasurementTwo(),
                expectedResultFromMeasurementOneChange.isValidMeasurement(),
                expectedResultFromMeasurementOneChange.getMinimumMeasurement(),
                expectedResultFromMeasurementOneChange.getMaxMeasurementOne(),
                expectedResultFromMeasurementOneChange.getMaxMeasurementTwo(),
                expectedResultFromMeasurementOneChange.getMeasurementUnitDigitWidths()
        );

        unitOfMeasureChangeToSpoonValues.conversionFactorIsSet(
                conversionFactorChangeFromUi.getConversionFactor());

        MeasurementModel expectedResultFromConversionFactorChanged = getMeasurementModel(
                unitOfMeasureChangeToSpoonValues);

        IngredientEntity expectedIngredientEntitySaveAfterConversionFactorUpdated =
                new IngredientEntity(
                        INGREDIENT_EXISTING_VALID_NAME_DESCRIPTION.getId(),
                        INGREDIENT_EXISTING_VALID_NAME_DESCRIPTION.getName(),
                        INGREDIENT_EXISTING_VALID_NAME_DESCRIPTION.getDescription(),
                        expectedResultFromConversionFactorChanged.getConversionFactor(),
                        INGREDIENT_EXISTING_VALID_NAME_DESCRIPTION.getCreatedBy(),
                        INGREDIENT_EXISTING_VALID_NAME_DESCRIPTION.getCreateDate(),
                        INGREDIENT_EXISTING_VALID_NAME_DESCRIPTION.getLastUpdate()
                );

        RecipeIngredientQuantityEntity expectedQuantityEntityAfterConversionFactorApplied =
                new RecipeIngredientQuantityEntity(
                        QUANTITY_EXISTING_VALID_METRIC.getId(),
                        QUANTITY_EXISTING_VALID_METRIC.getRecipeId(),
                        QUANTITY_EXISTING_VALID_METRIC.getIngredientId(),
                        QUANTITY_EXISTING_VALID_METRIC.getProductId(),
                        itemBaseUnits,
                        expectedResultFromUnitOfMeasureChange.getSubtype().asInt(),
                        QUANTITY_EXISTING_VALID_METRIC.getCreatedBy(),
                        QUANTITY_EXISTING_VALID_METRIC.getCreateDate(),
                        QUANTITY_EXISTING_VALID_METRIC.getLastUpdate()
                );

        // Act
        SUT.start(QUANTITY_EXISTING_VALID_METRIC.getId());
        // verify database called and return data
        verifyRepoRecipeIngredientCalledReturnExistingValidMetric();
        verifyRepoIngredientCalledAndReturnExistingValidNameDescription();
        verifyRepoPortionsCalledAndReturnExistingValidNinePortions();
        // verify existing data loaded and set to UI
        verify(viewModelMock).useCasePortionResult(resultArgumentCaptor.capture());
        MeasurementModel actualModelLoaded = resultArgumentCaptor.getValue().getModel();
        assertEquals(expectedModelLoaded, actualModelLoaded);
        assertEquals(ResultStatus.RESULT_OK, resultArgumentCaptor.getValue().getResult());

        //** Start of simulated user interaction **//
        // user updates unit of measure
        SUT.processModel(requestUnitOfMeasureChangeFromUi);
        // verify expected UI updates returned
        verify(viewModelMock, times((2))).useCasePortionResult(resultArgumentCaptor.capture());
        MeasurementResult unitOfMeasureChangeResult = resultArgumentCaptor.getValue();
        MeasurementModel actualResultFromUnitOfMeasureChange = unitOfMeasureChangeResult.getModel();
        assertEquals(expectedResultFromUnitOfMeasureChange, actualResultFromUnitOfMeasureChange);
        assertEquals(ResultStatus.INVALID_MEASUREMENT, unitOfMeasureChangeResult.getResult());
        // confirm nothing saved
        verifyNoMoreInteractions(repoRecipeIngredientMock);

        // user updates measurement unit one
        SUT.processModel(measurementOneChangeFromUi);
        // verify expected UI updates returned
        verify(viewModelMock, times(3)).useCasePortionResult(
                resultArgumentCaptor.capture());
        MeasurementModel actualResultFromMeasurementOneChange =
                resultArgumentCaptor.getValue().getModel();
        assertEquals(expectedResultFromMeasurementOneChange, actualResultFromMeasurementOneChange);
        assertEquals(ResultStatus.RESULT_OK, resultArgumentCaptor.getValue().getResult());
        // verify valid measurement saved to quantity entity
        verify(repoRecipeIngredientMock).save(recipeIngredientCaptor.capture());
        RecipeIngredientQuantityEntity actualQuantityEntity = recipeIngredientCaptor.getValue();
        assertEquals(expectedQuantityEntitySaveAfterMeasurementOneChange, actualQuantityEntity);

        // user updates conversion factor
        SUT.processModel(conversionFactorChangeFromUi);
        // verify expected UI updates returned
        verify(viewModelMock, times((4))).useCasePortionResult(resultArgumentCaptor.capture());
        MeasurementModel actualResultFromConversionFactorChange =
                resultArgumentCaptor.getValue().getModel();
        assertEquals(expectedResultFromConversionFactorChanged,
                actualResultFromConversionFactorChange);
        assertEquals(ResultStatus.RESULT_OK, resultArgumentCaptor.getValue().getResult());
        // verify ingredient conversion factor saved
        verify(repoIngredientMock).save(ingredientArgumentCaptor.capture());
        IngredientEntity actualIngredientEntity = ingredientArgumentCaptor.getValue();
        assertEquals(expectedIngredientEntitySaveAfterConversionFactorUpdated,
                actualIngredientEntity);
        // verify valid measurement saved to quantity entity
        verify(repoRecipeIngredientMock, times((2))).save(recipeIngredientCaptor.capture());
        RecipeIngredientQuantityEntity actualQuantityEntityWithUpdatedConversion =
                recipeIngredientCaptor.getValue();
        assertEquals(expectedQuantityEntityAfterConversionFactorApplied,
                actualQuantityEntityWithUpdatedConversion);


    }

    // region helper methods -----------------------------------------------------------------------
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

    private void verifyRepoIngredientCalledAndReturnExistingValidNameDescription() {
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
                unitOfMeasure.getNumberOfMeasurementUnits(),
                unitOfMeasure.isConversionFactorEnabled(),
                unitOfMeasure.getConversionFactor(),
                unitOfMeasure.getItemBaseUnits(),
                unitOfMeasure.getTotalBaseUnits(),
                unitOfMeasure.getNumberOfItems(),
                unitOfMeasure.getTotalMeasurementOne(),
                unitOfMeasure.getItemMeasurementOne(),
                unitOfMeasure.getTotalMeasurementTwo(),
                unitOfMeasure.getItemMeasurementTwo(),
                unitOfMeasure.isValidMeasurement(),
                unitOfMeasure.getMinimumMeasurementOne(),
                unitOfMeasure.getMaximumMeasurementOne(),
                unitOfMeasure.getMaximumMeasurementTwo(),
                unitOfMeasure.getMeasurementUnitsDigitWidths()
        );
    }
    // endregion helper methods --------------------------------------------------------------------

    // region helper classes -----------------------------------------------------------------------
    // endregion helper classes --------------------------------------------------------------------
}