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

import static com.example.peter.thekitchenmenu.utils.unitofmeasure.MeasurementResult.*;
import static org.mockito.Matchers.*;
import static org.mockito.Mockito.*;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

public class UnitOfMeasurePortionUseCaseTest {

    // region constants ----------------------------------------------------------------------------
    private final double DELTA = 0.0001;

    private RecipeIngredientQuantityEntity QUANTITY_NEW_INVALID =
            RecipeIngredientQuantityEntityTestData.getNewInvalid();

    private RecipeIngredientQuantityEntity QUANTITY_NEW_VALID_METRIC =
            RecipeIngredientQuantityEntityTestData.getNewValidMetric();

    private RecipeIngredientQuantityEntity QUANTITY_EXISTING_VALID_METRIC =
            RecipeIngredientQuantityEntityTestData.getExistingValidMetric();

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
            RecipeIngredientQuantityEntityTestData.getNewValidMetricMaxMassDivPortions();

    private MeasurementModel MEASUREMENT_EMPTY =
            MeasurementModelTestData.getEmptyModel();
    private MeasurementResult MEASUREMENT_EMPTY_RESULT =
            MeasurementModelTestData.getResultInvalidMeasurement();

    private MeasurementModel MEASUREMENT_INVALID_TOTAL_ONE =
            MeasurementModelTestData.getNewInvalidTotalMeasurementOne();
    private MeasurementResult MEASUREMENT_INVALID_TOTAL_ONE_RESULT =
            MeasurementModelTestData.getResultNewInvalidTotalMeasurementOne();

    private MeasurementModel MEASUREMENT_VALID_TOTAL_ONE =
            MeasurementModelTestData.getNewValidTotalMeasurementOne();
    private MeasurementResult MEASUREMENT_VALID_TOTAL_ONE_RESULT =
            MeasurementModelTestData.getResultNewValidTotalMeasurementOne();

    private MeasurementModel MEASUREMENT_INVALID_TOTAL_TWO =
            MeasurementModelTestData.getNewInvalidTotalMeasurementTwo();
    private MeasurementResult MEASUREMENT_INVALID_TOTAL_TWO_RESULT =
            MeasurementModelTestData.getResultNewInvalidTotalMeasurementTwo();

    private MeasurementModel MEASUREMENT_VALID_TOTAL_TWO =
            MeasurementModelTestData.getNewValidTotalMeasurementTwo();
    private MeasurementResult MEASUREMENT_VALID_TOTAL_TWO_RESULT =
            MeasurementModelTestData.getResultNewValidTotalMeasurementTwo();

    private MeasurementModel MEASUREMENT_UNIT_OF_MEASURE_CHANGED_IMPERIAL_SPOON =
            MeasurementModelTestData.getNewInvalidUnitOfMeasureChangedImperialSpoon();
    private MeasurementResult MEASUREMENT_UNIT_OF_MEASURE_CHANGED_IMPERIAL_SPOON_RESULT =
            MeasurementModelTestData.getResultNewInvalidUnitOfMeasureChangedImperialSpoon();

    private MeasurementModel MEASUREMENT_NEW_VALID_HALF_IMPERIAL_SPOON_UNIT_ONE_UPDATED =
            MeasurementModelTestData.getNewValidHalfImperialSpoonUnitOneUpdated();
    private MeasurementResult MEASUREMENT_NEW_VALID_HALF_IMPERIAL_SPOON_UNIT_ONE_UPDATED_RESULT =
            MeasurementModelTestData.getNewValidHalfImperialSpoonUnitOneUpdatedResult();

    private MeasurementModel MEASUREMENT_INVALID_CONVERSION_FACTOR =
            MeasurementModelTestData.getNewInvalidConversionFactor();
    private MeasurementResult MEASUREMENT_INVALID_CONVERSION_FACTOR_RESULT =
            MeasurementModelTestData.getResultNewInvalidConversionFactor();

    private MeasurementModel MEASUREMENT_VALID_MAX_CONVERSION_FACTOR =
            MeasurementModelTestData.getNewValidImperialSpoonWithConversionFactor();
    private MeasurementResult MEASUREMENT_VALID_MAX_CONVERSION_FACTOR_RESULT =
            MeasurementModelTestData.getResultNewValidImperialSpoonWithConversionFactor();

    private RecipeIngredientQuantityEntity QUANTITY_NEW_VALID_IMPERIAL_SPOON_MAX_CONVERSION_FACTOR =
            RecipeIngredientQuantityEntityTestData.getNewValidImperialSpoonMaxConversionFactor();

    private MeasurementModel MEASUREMENT_EXISTING_VALID_METRIC =
            MeasurementModelTestData.getExistingMetricValid();
    private MeasurementResult MEASUREMENT_EXISTING_VALID_METRIC_RESULT =
            new MeasurementResult(MEASUREMENT_EXISTING_VALID_METRIC, ResultStatus.RESULT_OK);

    private MeasurementModel MEASUREMENT_EXISTING_INVALID_TOTAL_ONE =
            MeasurementModelTestData.getExistingMetricInvalidTotalOne();
    private MeasurementResult MEASUREMENT_EXISTING_INVALID_TOTAL_ONE_RESULT =
            MeasurementModelTestData.getExistingMetricInvalidTotalOneResult();

    private MeasurementModel MEASUREMENT_EXISTING_INVALID_TOTAL_TWO =
            MeasurementModelTestData.getExistingMetricInvalidTotalTwo();
    private MeasurementResult MEASUREMENT_EXISTING_INVALID_TOTAL_TWO_RESULT =
            MeasurementModelTestData.getExistingInvalidTotalTwoResult();

    private MeasurementModel MEASUREMENT_EXISTING_VALID_TOTAL_TWO_UPDATED =
            MeasurementModelTestData.getExistingMetricValidTwoUpdated();
    private MeasurementResult MEASUREMENT_EXISTING_VALID_TOTAL_TWO_UPDATED_RESULT =
            MeasurementModelTestData.getExistingMetricValidTwoUpdatedResult();

    private MeasurementModel MEASUREMENT_EXISTING_INVALID_UNIT_OF_MEASURE_CHANGED =
            MeasurementModelTestData.getExistingMetricUnitOfMeasureUpdatedToImperial();
    private MeasurementResult MEASUREMENT_EXISTING_INVALID_UNIT_OF_MEASURE_CHANGED_RESULT =
            MeasurementModelTestData.getExistingMetricUnitOfMeasureUpdatedToImperialResult();

    private MeasurementModel MEASUREMENT_EXISTING_INVALID_CONVERSION_FACTOR =
            MeasurementModelTestData.getExistingMetricInvalidConversionFactor();
    private MeasurementResult MEASUREMENT_EXISTING_INVALID_CONVERSION_FACTOR_RESULT =
            MeasurementModelTestData.getExistingMetricInvalidConversionFactorResult();

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
    PortionUseCaseViewModel viewModelMock;
    @Captor
    ArgumentCaptor<MeasurementResult> resultArgumentCaptor;
    @Captor
    ArgumentCaptor<RecipeIngredientQuantityEntity> recipeIngredientCaptor;
    @Captor
    ArgumentCaptor<IngredientEntity> ingredientArgumentCaptor;
    // endregion helper fields ---------------------------------------------------------------------

    private UnitOfMeasurePortionUseCase SUT;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        SUT = new UnitOfMeasurePortionUseCase(
                repoRecipePortionsMock,
                repoRecipeIngredientMock,
                repoIngredientMock,
                idProviderMock,
                timeProviderMock);

        SUT.setViewModel(viewModelMock);
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
        verify(viewModelMock).modelOut(eq(MEASUREMENT_EMPTY_RESULT));
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

        SUT.modelIn(MEASUREMENT_INVALID_TOTAL_ONE);
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

        SUT.modelIn(MEASUREMENT_INVALID_TOTAL_ONE);
        // Assert
        verify(viewModelMock, times((2))).modelOut(resultArgumentCaptor.capture());
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

        SUT.modelIn(MEASUREMENT_VALID_TOTAL_ONE);

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

        SUT.modelIn(MEASUREMENT_VALID_TOTAL_ONE);
        // Assert
        verify(viewModelMock, times((2))).modelOut(resultArgumentCaptor.capture());
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

        SUT.modelIn(MEASUREMENT_INVALID_TOTAL_TWO);
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

        SUT.modelIn(MEASUREMENT_INVALID_TOTAL_TWO);
        // Assert
        verify(viewModelMock, times((2))).modelOut(resultArgumentCaptor.capture());
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

        SUT.modelIn(MEASUREMENT_VALID_TOTAL_TWO);
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

        SUT.modelIn(MEASUREMENT_VALID_TOTAL_TWO);
        // Assert
        verify(viewModelMock, times((2))).modelOut(resultArgumentCaptor.capture());
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

        SUT.modelIn(MEASUREMENT_UNIT_OF_MEASURE_CHANGED_IMPERIAL_SPOON);
        // Assert
        verify(viewModelMock, times((2))).modelOut(resultArgumentCaptor.capture());
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

        SUT.modelIn(MEASUREMENT_UNIT_OF_MEASURE_CHANGED_IMPERIAL_SPOON);
        // Assert
        verify(viewModelMock, times((2))).modelOut(resultArgumentCaptor.capture());
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
        SUT.modelIn(MEASUREMENT_UNIT_OF_MEASURE_CHANGED_IMPERIAL_SPOON);
        // Assert
        verify(viewModelMock, times((2))).modelOut(resultArgumentCaptor.capture());
        MeasurementResult unitOfMeasureResult = resultArgumentCaptor.getValue();
        assertEquals(MEASUREMENT_UNIT_OF_MEASURE_CHANGED_IMPERIAL_SPOON_RESULT, unitOfMeasureResult);

        // Change measurement one
        SUT.modelIn(MEASUREMENT_NEW_VALID_HALF_IMPERIAL_SPOON_UNIT_ONE_UPDATED);
        System.out.println("modelIn=                       "
                + MEASUREMENT_NEW_VALID_HALF_IMPERIAL_SPOON_UNIT_ONE_UPDATED);
        verify(viewModelMock, times((3))).modelOut(resultArgumentCaptor.capture());
        MeasurementResult halfSpoonResult = resultArgumentCaptor.getValue();
        System.out.println("expect=" + MEASUREMENT_NEW_VALID_HALF_IMPERIAL_SPOON_UNIT_ONE_UPDATED_RESULT);
        System.out.println("result=" + halfSpoonResult);
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

        SUT.modelIn(MEASUREMENT_INVALID_CONVERSION_FACTOR);
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

        SUT.modelIn(MEASUREMENT_INVALID_CONVERSION_FACTOR);
        // Assert
        verify(viewModelMock, times((2))).modelOut(resultArgumentCaptor.capture());
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

        SUT.modelIn(MEASUREMENT_UNIT_OF_MEASURE_CHANGED_IMPERIAL_SPOON);
        SUT.modelIn(MEASUREMENT_VALID_MAX_CONVERSION_FACTOR); // setConversion factor
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

        SUT.modelIn(MEASUREMENT_UNIT_OF_MEASURE_CHANGED_IMPERIAL_SPOON);
        SUT.modelIn(MEASUREMENT_VALID_MAX_CONVERSION_FACTOR); // setConversion factor
        SUT.modelIn(MEASUREMENT_VALID_MAX_CONVERSION_FACTOR); // setMeasurementOne
        SUT.modelIn(MEASUREMENT_VALID_MAX_CONVERSION_FACTOR); // setMeasurementTwo
        // Assert
        verify(repoRecipeIngredientMock, times((2))).save(recipeIngredientCaptor.capture());
        verify(viewModelMock, times((5))).modelOut(resultArgumentCaptor.capture());
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

        SUT.modelIn(MEASUREMENT_UNIT_OF_MEASURE_CHANGED_IMPERIAL_SPOON);
        SUT.modelIn(MEASUREMENT_VALID_MAX_CONVERSION_FACTOR); // setConversion factor
        SUT.modelIn(MEASUREMENT_VALID_MAX_CONVERSION_FACTOR); // setMeasurementOne
        SUT.modelIn(MEASUREMENT_VALID_MAX_CONVERSION_FACTOR); // setMeasurementTwo
        // Assert
        verify(repoRecipeIngredientMock, times((2))).save(recipeIngredientCaptor.capture());
        verify(viewModelMock, times((5))).modelOut(resultArgumentCaptor.capture());
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

        verify(viewModelMock).modelOut(resultArgumentCaptor.capture());
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

        SUT.modelIn(MEASUREMENT_EXISTING_INVALID_TOTAL_ONE);

        // Assert
        verify(viewModelMock, times((2))).modelOut(resultArgumentCaptor.capture());
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

        SUT.modelIn(MEASUREMENT_EXISTING_INVALID_TOTAL_TWO);
        // Assert
        verify(viewModelMock, times((2))).modelOut(resultArgumentCaptor.capture());
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

        SUT.modelIn(MEASUREMENT_EXISTING_VALID_TOTAL_TWO_UPDATED);
        // Assert
        verify(repoRecipeIngredientMock).save(recipeIngredientCaptor.capture());
        RecipeIngredientQuantityEntity saveResult = recipeIngredientCaptor.getValue();
        assertEquals(MEASUREMENT_EXISTING_VALID_TOTAL_TWO_UPDATED_RESULT.
                        getModel().
                        getItemBaseUnits(),
                saveResult.getItemBaseUnits(), DELTA);
    }

    @Test
    public void startExistingRecipeIngredientId_validTotalMeasurementTwoUpdated_RESULT_OK() {
        // Arrange
        // Act
        SUT.start(QUANTITY_EXISTING_VALID_METRIC.getId());
        verifyRepoRecipeIngredientCalledReturnExistingValidMetric();
        verifyRepoIngredientCalledAndReturnExistingValidNameDescription();
        verifyRepoPortionsCalledAndReturnExistingValidNinePortions();

        SUT.modelIn(MEASUREMENT_EXISTING_VALID_TOTAL_TWO_UPDATED);
        // Assert
        verify(viewModelMock, times((2))).modelOut(resultArgumentCaptor.capture());
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

        SUT.modelIn(MEASUREMENT_EXISTING_INVALID_UNIT_OF_MEASURE_CHANGED);
        // Assert
        verify(viewModelMock, times((2))).modelOut(resultArgumentCaptor.capture());
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

        SUT.modelIn(MEASUREMENT_EXISTING_INVALID_UNIT_OF_MEASURE_CHANGED);
        // Assert
        verify(viewModelMock, times((2))).modelOut(resultArgumentCaptor.capture());
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

        SUT.modelIn(MEASUREMENT_EXISTING_INVALID_CONVERSION_FACTOR);
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

        SUT.modelIn(MEASUREMENT_EXISTING_INVALID_CONVERSION_FACTOR);
        // Assert
        verify(viewModelMock, times((2))).modelOut(resultArgumentCaptor.capture());
        ResultStatus actualResultStatus = resultArgumentCaptor.getValue().getResult();
        assertEquals(ResultStatus.INVALID_CONVERSION_FACTOR, actualResultStatus);
    }

    @Test
    public void startExistingRecipeIngredientId_invalidConversionFactor_measurementValuesNotChanged() {
        // Act
        SUT.start(QUANTITY_EXISTING_VALID_METRIC.getId());
        // Assert
        verifyRepoRecipeIngredientCalledReturnExistingValidMetric();
        verifyRepoIngredientCalledAndReturnExistingValidNameDescription();
        verifyRepoPortionsCalledAndReturnExistingValidNinePortions();

        SUT.modelIn(MEASUREMENT_EXISTING_INVALID_CONVERSION_FACTOR);
        // Assert
        verify(viewModelMock, times((2))).modelOut(resultArgumentCaptor.capture());
        MeasurementResult actualResult = resultArgumentCaptor.getValue();
        assertEquals(MEASUREMENT_EXISTING_INVALID_CONVERSION_FACTOR_RESULT, actualResult);

        MeasurementModel actualModel = resultArgumentCaptor.getValue().getModel();
        assertEquals(MEASUREMENT_EXISTING_VALID_METRIC, actualModel);
    }

    @Test
    public void startExistingRecipeIngredientId_userUpdateExistingFlow_allValuesAsExpected() {
        // Arrange
        int portions = MEASUREMENT_EXISTING_VALID_METRIC.getNumberOfItems();
        double twoTeaspoons = 2;
        double volumePerTeaspoon = 5;
        double itemBaseUnitsWithConversionFactorApplied =
                (twoTeaspoons * volumePerTeaspoon / portions) *
                        UnitOfMeasureConstants.MAX_CONVERSION_FACTOR;
        when(timeProviderMock.getCurrentTimestamp()).thenReturn(
                QUANTITY_EXISTING_VALID_METRIC.getCreateDate(),
                INGREDIENT_EXISTING_VALID_NAME_DESCRIPTION.getLastUpdate(),
                QUANTITY_EXISTING_VALID_METRIC.getLastUpdate());

        MeasurementModel expectedModelLoaded = new MeasurementModel(
                MeasurementSubtype.fromInt(QUANTITY_EXISTING_VALID_METRIC.getUnitOfMeasureSubtype()),
                portions,
                INGREDIENT_EXISTING_VALID_NAME_DESCRIPTION.getConversionFactor(),
                (QUANTITY_EXISTING_VALID_METRIC.getItemBaseUnits() * portions % 1000),
                ((int) QUANTITY_EXISTING_VALID_METRIC.getItemBaseUnits() * portions / 1000),
                (QUANTITY_EXISTING_VALID_METRIC.getItemBaseUnits() % 1000),
                ((int) QUANTITY_EXISTING_VALID_METRIC.getItemBaseUnits() / 1000),
                ((int) QUANTITY_EXISTING_VALID_METRIC.getItemBaseUnits())
        );

        MeasurementModel unitOfMeasureChangeToImperialSpoonFromUi = new MeasurementModel(
                MeasurementSubtype.IMPERIAL_SPOON,
                expectedModelLoaded.getNumberOfItems(),
                expectedModelLoaded.getConversionFactor(),
                expectedModelLoaded.getTotalMeasurementOne(),
                expectedModelLoaded.getTotalMeasurementTwo(),
                expectedModelLoaded.getItemMeasurementOne(),
                expectedModelLoaded.getItemMeasurementTwo(),
                expectedModelLoaded.getItemBaseUnits()
        );

        MeasurementModel expectedResultFromUnitOfMeasureChange = new MeasurementModel(
                unitOfMeasureChangeToImperialSpoonFromUi.getSubtype(),
                portions,
                expectedModelLoaded.getConversionFactor(),
                MEASUREMENT_EMPTY.getTotalMeasurementOne(),
                MEASUREMENT_EMPTY.getTotalMeasurementTwo(),
                MEASUREMENT_EMPTY.getItemMeasurementOne(),
                MEASUREMENT_EMPTY.getItemMeasurementTwo(),
                MEASUREMENT_EMPTY.getItemBaseUnits()
        );

        MeasurementModel measurementOneChangeFromUi = new MeasurementModel(
                expectedResultFromUnitOfMeasureChange.getSubtype(),
                expectedResultFromUnitOfMeasureChange.getNumberOfItems(),
                expectedResultFromUnitOfMeasureChange.getConversionFactor(),
                twoTeaspoons,
                expectedResultFromUnitOfMeasureChange.getTotalMeasurementTwo(),
                expectedResultFromUnitOfMeasureChange.getItemMeasurementOne(),
                expectedResultFromUnitOfMeasureChange.getItemMeasurementTwo(),
                expectedResultFromUnitOfMeasureChange.getItemBaseUnits()
        );

        MeasurementModel expectedResultFromMeasurementOneChange = new MeasurementModel(
                measurementOneChangeFromUi.getSubtype(),
                measurementOneChangeFromUi.getNumberOfItems(),
                measurementOneChangeFromUi.getConversionFactor(),
                measurementOneChangeFromUi.getTotalMeasurementOne(),
                measurementOneChangeFromUi.getTotalMeasurementTwo(),
                measurementOneChangeFromUi.getItemMeasurementOne(),
                measurementOneChangeFromUi.getItemMeasurementTwo(),
                (twoTeaspoons * volumePerTeaspoon / portions)
        );

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
                expectedResultFromMeasurementOneChange.getSubtype(),
                expectedResultFromMeasurementOneChange.getNumberOfItems(),
                UnitOfMeasureConstants.MAX_CONVERSION_FACTOR,
                expectedResultFromMeasurementOneChange.getTotalMeasurementOne(),
                expectedResultFromMeasurementOneChange.getTotalMeasurementTwo(),
                expectedResultFromMeasurementOneChange.getItemMeasurementOne(),
                expectedResultFromMeasurementOneChange.getItemMeasurementTwo(),
                expectedResultFromMeasurementOneChange.getTotalMeasurementOne()
        );

        MeasurementModel expectedResultFromConversionFactorChanged = new MeasurementModel(
                conversionFactorChangeFromUi.getSubtype(),
                conversionFactorChangeFromUi.getNumberOfItems(),
                conversionFactorChangeFromUi.getConversionFactor(),
                conversionFactorChangeFromUi.getTotalMeasurementOne(),
                conversionFactorChangeFromUi.getTotalMeasurementTwo(),
                conversionFactorChangeFromUi.getItemMeasurementOne(),
                conversionFactorChangeFromUi.getItemMeasurementTwo(),
                itemBaseUnitsWithConversionFactorApplied
        );

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
                        itemBaseUnitsWithConversionFactorApplied,
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
        verify(viewModelMock).modelOut(resultArgumentCaptor.capture());
        MeasurementModel actualModelLoaded = resultArgumentCaptor.getValue().getModel();
        assertEquals(expectedModelLoaded, actualModelLoaded);
        assertEquals(ResultStatus.RESULT_OK, resultArgumentCaptor.getValue().getResult());

        //** Start of simulated user interaction **//
        // user updates unit of measure
        SUT.modelIn(unitOfMeasureChangeToImperialSpoonFromUi);
        // verify expected UI updates returned
        verify(viewModelMock, times((2))).modelOut(resultArgumentCaptor.capture());
        MeasurementResult unitOfMeasureChangeResult = resultArgumentCaptor.getValue();
        MeasurementModel actualResultFromUnitOfMeasureChange = unitOfMeasureChangeResult.getModel();
        assertEquals(expectedResultFromUnitOfMeasureChange, actualResultFromUnitOfMeasureChange);
        assertEquals(ResultStatus.INVALID_MEASUREMENT, unitOfMeasureChangeResult.getResult());
        // confirm nothing saved
        verifyNoMoreInteractions(repoRecipeIngredientMock);

        // user updates measurement unit one
        SUT.modelIn(measurementOneChangeFromUi);
        // verify expected UI updates returned
        verify(viewModelMock, times(3)).modelOut(
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
        SUT.modelIn(conversionFactorChangeFromUi);
        // verify expected UI updates returned
        verify(viewModelMock, times(4)).modelOut(
                resultArgumentCaptor.capture());
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
        verify(repoRecipeIngredientMock, times(2)).save(
                recipeIngredientCaptor.capture());
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
        when(timeProviderMock.getCurrentTimestamp()).
                thenReturn(QUANTITY_NEW_VALID_METRIC.getCreateDate());
    }
    // endregion helper methods --------------------------------------------------------------------

    // region helper classes -----------------------------------------------------------------------
    // endregion helper classes --------------------------------------------------------------------
}