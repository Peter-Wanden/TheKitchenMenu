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
    private RecipeIngredientQuantityEntity QUANTITY_NEW_INVALID =
            RecipeIngredientQuantityEntityTestData.getNewInvalid();

    private RecipeIngredientQuantityEntity QUANTITY_NEW_VALID_METRIC =
            RecipeIngredientQuantityEntityTestData.getNewValidMetric();

    private RecipeIngredientQuantityEntity QUANTITY_NEW_VALID_IMPERIAL =
            RecipeIngredientQuantityEntityTestData.getNewValidImperial();

    private static final double NEW_VALID_IMPERIAL_OZ = 5.1;

    private RecipeIngredientQuantityEntity QUANTITY_EXISTING_VALID_METRIC =
            RecipeIngredientQuantityEntityTestData.getExistingValidMetric();

    private RecipePortionsEntity PORTIONS_NEW_VALID_FOUR =
            RecipePortionsEntityTestData.getNewValidFourPortions();

    private RecipePortionsEntity PORTIONS_NEW_VALID_SIXTEEN =
            RecipePortionsEntityTestData.getNewValidSixteenPortions();

    private RecipePortionsEntity PORTIONS_EXISTING_VALID_NINE =
            RecipePortionsEntityTestData.getExistingValid();

    private RecipeIngredientQuantityEntity QUANTITY_EXISTING_VALID_METRIC_MEASUREMENT_UPDATED =
            RecipeIngredientQuantityEntityTestData.getExistingValidMetricMeasurementUpdated();

    private IngredientEntity INGREDIENT_NEW_VALID_NAME =
            IngredientEntityTestData.getNewValidName();

    private IngredientEntity INGREDIENT_EXISTING_VALID_NAME_DESCRIPTION =
            IngredientEntityTestData.getExistingValidNameValidDescription();

    private RecipeIngredientQuantityEntity QUANTITY_NEW_VALID_ONE_TEASPOON_NO_CONVERSION =
            RecipeIngredientQuantityEntityTestData.
                    getNewValidImperialOneTeaspoonNoConversionFactor();

    private IngredientEntity INGREDIENT_EXISTING_VALID_NAME_CONVERSION_FACTOR_UPDATED =
            IngredientEntityTestData.getExistingValidWithConversionFactor();

    private IngredientEntity INGREDIENT_NEW_VALID_NAME_DESCRIPTION =
            IngredientEntityTestData.getNewValidNameValidDescription();

    private IngredientEntity INGREDIENT_NEW_VALID_CONVERSION_FACTOR_UPDATED =
            IngredientEntityTestData.getNewValidNameValidDescriptionConversionFactorUpdated();

    private RecipeIngredientQuantityEntity QUANTITY_NEW_VALID_ONE_TEASPOON_CONVERSION_APPLIED =
            RecipeIngredientQuantityEntityTestData.
                    getNewValidImperialOneTeaspoonConversionFactorApplied();

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

    private MeasurementModel MEASUREMENT_UNIT_OF_MEASURE_CHANGED =
            MeasurementModelTestData.getNewInvalidUnitOfMeasureChanged();
    private MeasurementResult MEASUREMENT_UNIT_OF_MEASURE_CHANGED_RESULT =
            MeasurementModelTestData.getResultNewInvalidUnitOfMeasureChanged();

    private final double DELTA = 0.0001;
    private MeasurementModel MEASUREMENT_EXISTING_VALID =
            MeasurementModelTestData.getExistingValid();

    private MeasurementResult RESULT_EXISTING_VALID_METRIC = new MeasurementResult(
            MEASUREMENT_EXISTING_VALID,
            ResultStatus.RESULT_OK);

    // endregion constants -------------------------------------------------------------------------
    // region helper fields ------------------------------------------------------------------------
    @Mock
    RepositoryRecipePortions repositoryRecipePortionsMock;
    @Captor
    ArgumentCaptor<DataSource.GetEntityCallback<RecipePortionsEntity>>
            getRecipePortionsCallbackCaptor;
    @Mock
    RepositoryRecipeIngredient repositoryRecipeIngredientMock;
    @Captor
    ArgumentCaptor<DataSource.GetEntityCallback<RecipeIngredientQuantityEntity>>
            getRecipeIngredientCallbackCaptor;
    @Mock
    RepositoryIngredient repositoryIngredientMock;
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
                repositoryRecipePortionsMock,
                repositoryRecipeIngredientMock,
                repositoryIngredientMock,
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
        verifyNoMoreInteractions(repositoryRecipeIngredientMock);
        // Assert incomplete model returned
        verify(viewModelMock).setResult(eq(MEASUREMENT_EMPTY_RESULT));
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
        verify(viewModelMock).setResult(eq(MEASUREMENT_EMPTY_RESULT));
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

        SUT.setModel(MEASUREMENT_INVALID_TOTAL_ONE);
        // Assert
        verifyNoMoreInteractions(repositoryRecipeIngredientMock);
    }

    @Test
    public void startNewRecipeAndIngredientId_invalidTotalMeasurementOneUpdated_INVALID_TOTAL_MEASUREMENT_ONE() {
        // Arrange
        ArgumentCaptor<MeasurementResult> actualResult = ArgumentCaptor.forClass(
                MeasurementResult.class);
        whenIdProviderReturnNewValidId();
        whenTimeProviderThenReturnNewValidTime();

        // Act
        SUT.start(QUANTITY_NEW_VALID_METRIC.getRecipeId(),
                QUANTITY_NEW_VALID_METRIC.getIngredientId());
        verifyRepoIngredientCalledAndReturnNewValidName();
        verifyRepoPortionsCalledAndReturnNewValidFourPortions();

        SUT.setModel(MEASUREMENT_INVALID_TOTAL_ONE);
        // Assert
        verify(viewModelMock, times(2)).setResult(actualResult.capture());
        assertEquals(MEASUREMENT_INVALID_TOTAL_ONE_RESULT, actualResult.getValue());
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

        SUT.setModel(MEASUREMENT_VALID_TOTAL_ONE);

        // Assert
        verify(repositoryRecipeIngredientMock).save(eq(QUANTITY_NEW_VALID_METRIC));
        verify(viewModelMock, times(2)).setResult(
                resultArgumentCaptor.capture());
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

        SUT.setModel(MEASUREMENT_VALID_TOTAL_ONE);
        // Assert
        verify(viewModelMock, times(2)).setResult(
                resultArgumentCaptor.capture());
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

        SUT.setModel(MEASUREMENT_INVALID_TOTAL_TWO);
        // Assert
        verifyNoMoreInteractions(repositoryRecipeIngredientMock);
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

        SUT.setModel(MEASUREMENT_INVALID_TOTAL_TWO);
        // Assert
        verify(viewModelMock, times(2)).setResult(
                resultArgumentCaptor.capture());
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

        SUT.setModel(MEASUREMENT_VALID_TOTAL_TWO);
        // Assert
        verify(repositoryRecipeIngredientMock).save(recipeIngredientCaptor.capture());
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

        SUT.setModel(MEASUREMENT_VALID_TOTAL_TWO);
        // Assert
        verify(viewModelMock, times(2)).setResult(
                resultArgumentCaptor.capture());
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

        SUT.setModel(MEASUREMENT_UNIT_OF_MEASURE_CHANGED);
        // Assert
        verify(viewModelMock, times(2)).setResult(
                resultArgumentCaptor.capture());
        MeasurementResult actualResult = resultArgumentCaptor.getValue();
        assertEquals(MEASUREMENT_UNIT_OF_MEASURE_CHANGED_RESULT, actualResult);
        assertEquals(ResultStatus.INVALID_MEASUREMENT, actualResult.getResult());
    }

    @Test
    public void startNewRecipeAndIngredientId_unitOfMeasureChanged_emptyModel() {
        // Arrange
        ArgumentCaptor<MeasurementResult> ac = ArgumentCaptor.forClass(MeasurementResult.class);
        whenIdProviderReturnNewValidId();
        whenTimeProviderThenReturnNewValidTime();

        // Act
        SUT.start(QUANTITY_NEW_VALID_METRIC.getRecipeId(),
                QUANTITY_NEW_VALID_METRIC.getIngredientId());
        verifyRepoIngredientCalledAndReturnNewValidName();
        verifyRepoPortionsCalledAndReturnNewValidFourPortions();

        SUT.setModel(MEASUREMENT_UNIT_OF_MEASURE_CHANGED);
        // Assert
        verify(viewModelMock, times(2)).setResult(
                resultArgumentCaptor.capture());
        MeasurementResult actualResult = resultArgumentCaptor.getValue();
        assertEquals(MEASUREMENT_UNIT_OF_MEASURE_CHANGED_RESULT, actualResult);

        MeasurementModel actualModel = actualResult.getModel();
        assertEquals(MEASUREMENT_UNIT_OF_MEASURE_CHANGED_RESULT.getModel(), actualModel);
    }

    @Test
    public void startNewRecipeAndIngredientId_invalidConversionFactor_notSaved() {
        // Arrange
        whenIdProviderReturnNewValidId();
        whenTimeProviderThenReturnNewValidTime();

        MeasurementModel invalidConversionFactorModel = new MeasurementModel(
                MeasurementSubtype.METRIC_MASS,
                1,
                UnitOfMeasureConstants.MAX_CONVERSION_FACTOR + 0.1,
                0,
                0,
                0,
                0,
                0
        );
        // Act
        SUT.start(QUANTITY_NEW_VALID_METRIC.getRecipeId(),
                QUANTITY_NEW_VALID_METRIC.getIngredientId());
        verifyRepoIngredientCalledAndReturnNewValidName();
        verifyRepoPortionsCalledAndReturnNewValidFourPortions();

        SUT.setModel(invalidConversionFactorModel);
        // Assert
        verifyNoMoreInteractions(repositoryRecipeIngredientMock);
    }

    @Test
    public void startNewRecipeAndIngredientId_invalidConversionFactor_INVALID_CONVERSION_FACTOR() {
        // Arrange
        whenIdProviderReturnNewValidId();
        whenTimeProviderThenReturnNewValidTime();

        MeasurementModel invalidConversionFactorModel = new MeasurementModel(
                MeasurementSubtype.METRIC_MASS,
                1,
                UnitOfMeasureConstants.MAX_CONVERSION_FACTOR + 0.1,
                0,
                0,
                0,
                0,
                0
        );
        // Act
        SUT.start(QUANTITY_NEW_VALID_METRIC.getRecipeId(),
                QUANTITY_NEW_VALID_METRIC.getIngredientId());
        verifyRepoIngredientCalledAndReturnNewValidName();
        verifyRepoPortionsCalledAndReturnNewValidFourPortions();

        SUT.setModel(invalidConversionFactorModel);
        // Assert
        verify(viewModelMock, times(2)).setResult(
                resultArgumentCaptor.capture());
        MeasurementResult result = resultArgumentCaptor.getValue();
        assertEquals(ResultStatus.INVALID_CONVERSION_FACTOR, result.getResult());
    }

    @Test
    public void startNewRecipeAndIngredientId_validConversionFactor_saved() {
        // Arrange
        whenIdProviderReturnNewValidId();
        whenTimeProviderThenReturnNewValidTime();

        MeasurementModel validConversionFactorModel = new MeasurementModel(
                MeasurementSubtype.METRIC_MASS,
                1,
                UnitOfMeasureConstants.MAX_CONVERSION_FACTOR,
                0,
                0,
                0,
                0,
                0
        );


        MeasurementModel validMeasurementModel = new MeasurementModel(
                MeasurementSubtype.METRIC_MASS,
                1,
                UnitOfMeasureConstants.MAX_CONVERSION_FACTOR,
                1000,
                0,
                0,
                0,
                0
        );

        // Act
        SUT.start(QUANTITY_NEW_VALID_METRIC.getRecipeId(),
                QUANTITY_NEW_VALID_METRIC.getIngredientId());
        verifyRepoIngredientCalledAndReturnNewValidName();
        verifyRepoPortionsCalledAndReturnNewValidFourPortions();

        SUT.setModel(validConversionFactorModel);
        SUT.setModel(validMeasurementModel);
        // Assert
        verify(repositoryRecipeIngredientMock).save(recipeIngredientCaptor.capture());
    }

    @Test
    public void startNewRecipeAndIngredientId_validConversionFactor_itemBaseUnitsUpdated() {
        // Arrange
        whenIdProviderReturnNewValidId();
        whenTimeProviderThenReturnNewValidTime();

        MeasurementModel validConversionFactorModel = new MeasurementModel(
                MeasurementSubtype.METRIC_MASS,
                1,
                UnitOfMeasureConstants.MAX_CONVERSION_FACTOR,
                0,
                0,
                0,
                0,
                0
        );


        MeasurementModel validMeasurementModel = new MeasurementModel(
                MeasurementSubtype.METRIC_MASS,
                1,
                UnitOfMeasureConstants.MAX_CONVERSION_FACTOR,
                1000,
                0,
                0,
                0,
                0
        );

        // Act
        SUT.start(QUANTITY_NEW_VALID_METRIC.getRecipeId(),
                QUANTITY_NEW_VALID_METRIC.getIngredientId());
        verifyRepoIngredientCalledAndReturnNewValidName();
        verifyRepoPortionsCalledAndReturnNewValidFourPortions();

        SUT.setModel(validConversionFactorModel);
        SUT.setModel(validMeasurementModel);
        // Assert
        verify(repositoryRecipeIngredientMock).save(recipeIngredientCaptor.capture());
        RecipeIngredientQuantityEntity quantityEntity = recipeIngredientCaptor.getValue();

        int portions = PORTIONS_NEW_VALID_FOUR.getServings() * PORTIONS_NEW_VALID_FOUR.getServings();
        double expectedItemBaseUnits = validMeasurementModel.getTotalMeasurementOne() *
                validConversionFactorModel.getConversionFactor() / portions;

        assertEquals(expectedItemBaseUnits, quantityEntity.getItemBaseUnits(), DELTA);
    }

    @Test
    public void startNewRecipeAndIngredientId_validConversionFactor_RESULT_OK() {
        // Arrange
        whenIdProviderReturnNewValidId();
        whenTimeProviderThenReturnNewValidTime();

        MeasurementModel validConversionFactorModel = new MeasurementModel(
                MeasurementSubtype.METRIC_MASS,
                1,
                UnitOfMeasureConstants.MAX_CONVERSION_FACTOR,
                0,
                0,
                0,
                0,
                0
        );


        MeasurementModel validMeasurementModel = new MeasurementModel(
                MeasurementSubtype.METRIC_MASS,
                1,
                UnitOfMeasureConstants.MAX_CONVERSION_FACTOR,
                1000,
                0,
                0,
                0,
                0
        );

        // Act
        SUT.start(QUANTITY_NEW_VALID_METRIC.getRecipeId(),
                QUANTITY_NEW_VALID_METRIC.getIngredientId());
        verifyRepoIngredientCalledAndReturnNewValidName();
        verifyRepoPortionsCalledAndReturnNewValidFourPortions();

        SUT.setModel(validConversionFactorModel);
        SUT.setModel(validMeasurementModel);
        // Assert
        verify(viewModelMock, times(3)).setResult(
                resultArgumentCaptor.capture());
        MeasurementResult result = resultArgumentCaptor.getValue();
        assertEquals(ResultStatus.RESULT_OK, result.getResult());
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
    public void startExistingRecipeIngredientId_RESULT_OK() {
        // Arrange
        // Act
        SUT.start(QUANTITY_EXISTING_VALID_METRIC.getId());
        // Assert
        verifyRepoRecipeIngredientCalledReturnExistingValidMetric();
        verifyRepoIngredientCalledAndReturnExistingValidNameDescription();
        verifyRepoPortionsCalledAndReturnExistingValidNinePortions();
        verify(viewModelMock).setResult(RESULT_EXISTING_VALID_METRIC);
    }

    @Test
    public void startExistingRecipeIngredientId_invalidTotalMeasurementOneUpdated_onlyValidValueSaved() {
        // Arrange
        double newInvalidTotalMeasurementOne = UnitOfMeasureConstants.MAX_VOLUME + 1;

        MeasurementModel invalidMeasurementModel = new MeasurementModel(
                MEASUREMENT_EXISTING_VALID.getSubtype(),
                MEASUREMENT_EXISTING_VALID.getNumberOfItems(),
                MEASUREMENT_EXISTING_VALID.getConversionFactor(),
                newInvalidTotalMeasurementOne, // updated invalid measurement one
                MEASUREMENT_EXISTING_VALID.getTotalMeasurementTwo(),
                MEASUREMENT_EXISTING_VALID.getItemMeasurementOne(),
                MEASUREMENT_EXISTING_VALID.getItemMeasurementTwo(),
                MEASUREMENT_EXISTING_VALID.getItemBaseUnits()
        );

        double expectedItemBaseUnits = MEASUREMENT_EXISTING_VALID.getTotalMeasurementTwo() * 1000 /
                (double) MEASUREMENT_EXISTING_VALID.getNumberOfItems();

        // Act
        SUT.start(QUANTITY_EXISTING_VALID_METRIC.getId());
        verifyRepoRecipeIngredientCalledReturnExistingValidMetric();
        verifyRepoIngredientCalledAndReturnExistingValidNameDescription();
        verifyRepoPortionsCalledAndReturnExistingValidNinePortions();

        SUT.setModel(invalidMeasurementModel);

        // Assert
        verify(viewModelMock, times(2)).setResult(resultArgumentCaptor.capture());
        MeasurementModel resultModel = resultArgumentCaptor.getValue().getModel();
        ResultStatus resultStatus = resultArgumentCaptor.getValue().getResult();

        assertEquals(0, resultModel.getTotalMeasurementOne(), DELTA);
        assertEquals(ResultStatus.INVALID_TOTAL_MEASUREMENT_ONE, resultStatus);

        verify(repositoryRecipeIngredientMock).save(recipeIngredientCaptor.capture());
        RecipeIngredientQuantityEntity quantityEntity = recipeIngredientCaptor.getValue();

        assertEquals(expectedItemBaseUnits, quantityEntity.getItemBaseUnits(), DELTA);
    }

    @Test
    public void startExistingRecipeIngredientId_invalidTotalMeasurementTwoUpdated_INVALID_TOTAL_MEASUREMENT_TWO() {
        // Arrange
        int newInvalidTotalMeasurementTwo = (int) UnitOfMeasureConstants.MAX_VOLUME / 1000 + 1;

        MeasurementModel invalidMeasurementModel = new MeasurementModel(
                MEASUREMENT_EXISTING_VALID.getSubtype(),
                MEASUREMENT_EXISTING_VALID.getNumberOfItems(),
                MEASUREMENT_EXISTING_VALID.getConversionFactor(),
                MEASUREMENT_EXISTING_VALID.getTotalMeasurementOne(),
                newInvalidTotalMeasurementTwo,
                MEASUREMENT_EXISTING_VALID.getItemMeasurementOne(),
                MEASUREMENT_EXISTING_VALID.getItemMeasurementTwo(),
                MEASUREMENT_EXISTING_VALID.getItemBaseUnits()
        );

        double expectedItemBaseUnits = MEASUREMENT_EXISTING_VALID.getTotalMeasurementOne() /
                (double) MEASUREMENT_EXISTING_VALID.getNumberOfItems();
        // Act
        SUT.start(QUANTITY_EXISTING_VALID_METRIC.getId());
        verifyRepoRecipeIngredientCalledReturnExistingValidMetric();
        verifyRepoIngredientCalledAndReturnExistingValidNameDescription();
        verifyRepoPortionsCalledAndReturnExistingValidNinePortions();

        SUT.setModel(invalidMeasurementModel);
        // Assert
        verify(viewModelMock, times(2)).setResult(
                resultArgumentCaptor.capture());
        MeasurementModel resultModel = resultArgumentCaptor.getValue().getModel();
        ResultStatus resultStatus = resultArgumentCaptor.getValue().getResult();

        assertEquals(0, resultModel.getTotalMeasurementTwo());
        assertEquals(ResultStatus.INVALID_TOTAL_MEASUREMENT_TWO, resultStatus);

        verify(repositoryRecipeIngredientMock).save(recipeIngredientCaptor.capture());
        RecipeIngredientQuantityEntity quantityEntity = recipeIngredientCaptor.getValue();

        assertEquals(expectedItemBaseUnits, quantityEntity.getItemBaseUnits(), DELTA);
    }

    @Test
    public void startExistingRecipeIngredientId_validTotalMeasurementTwoUpdated_saved() {
        // Arrange
        int updatedMeasurementTwo = 4;
        MeasurementModel measurementTwoUpdatedModel = new MeasurementModel(
                MEASUREMENT_EXISTING_VALID.getSubtype(),
                MEASUREMENT_EXISTING_VALID.getNumberOfItems(),
                MEASUREMENT_EXISTING_VALID.getConversionFactor(),
                MEASUREMENT_EXISTING_VALID.getTotalMeasurementOne(),
                updatedMeasurementTwo,
                MEASUREMENT_EXISTING_VALID.getItemMeasurementOne(),
                MEASUREMENT_EXISTING_VALID.getItemMeasurementTwo(),
                MEASUREMENT_EXISTING_VALID.getItemBaseUnits()
        );
        double expectedItemBaseUnits = (((double) updatedMeasurementTwo * 1000) +
                MEASUREMENT_EXISTING_VALID.getTotalMeasurementOne()) /
                (double) MEASUREMENT_EXISTING_VALID.getNumberOfItems();
        // Act
        SUT.start(QUANTITY_EXISTING_VALID_METRIC.getId());
        verifyRepoRecipeIngredientCalledReturnExistingValidMetric();
        verifyRepoIngredientCalledAndReturnExistingValidNameDescription();
        verifyRepoPortionsCalledAndReturnExistingValidNinePortions();

        SUT.setModel(measurementTwoUpdatedModel);
        // Assert
        verify(viewModelMock, times(2)).setResult(
                resultArgumentCaptor.capture());
        MeasurementModel resultModel = resultArgumentCaptor.getValue().getModel();
        assertEquals(expectedItemBaseUnits, resultModel.getItemBaseUnits(), DELTA);
    }

    @Test
    public void startExistingRecipeIngredientId_validTotalMeasurementTwoUpdated_RESULT_OK() {
        // Arrange
        int updatedMeasurementTwo = 4;
        MeasurementModel measurementTwoUpdatedModel = new MeasurementModel(
                MEASUREMENT_EXISTING_VALID.getSubtype(),
                MEASUREMENT_EXISTING_VALID.getNumberOfItems(),
                MEASUREMENT_EXISTING_VALID.getConversionFactor(),
                MEASUREMENT_EXISTING_VALID.getTotalMeasurementOne(),
                updatedMeasurementTwo,
                MEASUREMENT_EXISTING_VALID.getItemMeasurementOne(),
                MEASUREMENT_EXISTING_VALID.getItemMeasurementTwo(),
                MEASUREMENT_EXISTING_VALID.getItemBaseUnits()
        );
        double expectedItemBaseUnits = (((double) updatedMeasurementTwo * 1000) +
                MEASUREMENT_EXISTING_VALID.getTotalMeasurementOne()) /
                (double) MEASUREMENT_EXISTING_VALID.getNumberOfItems();
        // Act
        SUT.start(QUANTITY_EXISTING_VALID_METRIC.getId());
        verifyRepoRecipeIngredientCalledReturnExistingValidMetric();
        verifyRepoIngredientCalledAndReturnExistingValidNameDescription();
        verifyRepoPortionsCalledAndReturnExistingValidNinePortions();

        SUT.setModel(measurementTwoUpdatedModel);
        // Assert
        verify(viewModelMock, times(2)).setResult(
                resultArgumentCaptor.capture());
        ResultStatus resultStatus = resultArgumentCaptor.getValue().getResult();
        assertEquals(ResultStatus.RESULT_OK, resultStatus);
    }

    @Test
    public void startExistingRecipeIngredientId_unitOfMeasureChanged_INVALID_MEASUREMENT() {
        // Arrange
        MeasurementModel unitOfMeasureUpdatedModel = new MeasurementModel(
                MeasurementSubtype.IMPERIAL_MASS, // new value
                // Now old unit of measure values from UI
                MEASUREMENT_EXISTING_VALID.getNumberOfItems(),
                MEASUREMENT_EXISTING_VALID.getConversionFactor(),
                MEASUREMENT_EXISTING_VALID.getTotalMeasurementOne(),
                MEASUREMENT_EXISTING_VALID.getTotalMeasurementTwo(),
                MEASUREMENT_EXISTING_VALID.getItemMeasurementOne(),
                MEASUREMENT_EXISTING_VALID.getItemMeasurementTwo(),
                MEASUREMENT_EXISTING_VALID.getItemBaseUnits()
        );
        // Act
        SUT.start(QUANTITY_EXISTING_VALID_METRIC.getId());
        // Assert
        verifyRepoRecipeIngredientCalledReturnExistingValidMetric();
        verifyRepoIngredientCalledAndReturnExistingValidNameDescription();
        verifyRepoPortionsCalledAndReturnExistingValidNinePortions();

        SUT.setModel(unitOfMeasureUpdatedModel);
        // Assert
        verify(viewModelMock, times(2)).setResult(
                resultArgumentCaptor.capture());
        ResultStatus resultStatus = resultArgumentCaptor.getValue().getResult();
        assertEquals(ResultStatus.INVALID_MEASUREMENT, resultStatus);
    }

    @Test
    public void startExistingRecipeIngredientId_unitOfMeasureChanged_emptyModel() {
        // Arrange
        MeasurementModel unitOfMeasureUpdatedModelFromUi = new MeasurementModel(
                MeasurementSubtype.IMPERIAL_MASS, // new unit of measure from UI
                // old unit of measure values from UI
                MEASUREMENT_EXISTING_VALID.getNumberOfItems(),
                MEASUREMENT_EXISTING_VALID.getConversionFactor(),
                MEASUREMENT_EXISTING_VALID.getTotalMeasurementOne(),
                MEASUREMENT_EXISTING_VALID.getTotalMeasurementTwo(),
                MEASUREMENT_EXISTING_VALID.getItemMeasurementOne(),
                MEASUREMENT_EXISTING_VALID.getItemMeasurementTwo(),
                MEASUREMENT_EXISTING_VALID.getItemBaseUnits()
        );
        MeasurementModel expectedModelReturnedToUi = new MeasurementModel(
                MeasurementSubtype.IMPERIAL_MASS,
                MEASUREMENT_EXISTING_VALID.getNumberOfItems(),
                MEASUREMENT_EXISTING_VALID.getConversionFactor(),
                // Measurement values cleared as conversion between measurement types not used
                MEASUREMENT_EMPTY.getTotalMeasurementOne(),
                MEASUREMENT_EMPTY.getTotalMeasurementTwo(),
                MEASUREMENT_EMPTY.getItemMeasurementOne(),
                MEASUREMENT_EMPTY.getItemMeasurementTwo(),
                MEASUREMENT_EMPTY.getItemBaseUnits()
        );
        // Act
        SUT.start(QUANTITY_EXISTING_VALID_METRIC.getId());
        // Assert
        verifyRepoRecipeIngredientCalledReturnExistingValidMetric();
        verifyRepoIngredientCalledAndReturnExistingValidNameDescription();
        verifyRepoPortionsCalledAndReturnExistingValidNinePortions();

        SUT.setModel(unitOfMeasureUpdatedModelFromUi);
        // Assert
        verify(viewModelMock, times(2)).setResult(
                resultArgumentCaptor.capture());
        MeasurementModel actualModelReturnedToUi = resultArgumentCaptor.getValue().getModel();
        assertEquals(expectedModelReturnedToUi, actualModelReturnedToUi);
    }

    @Test
    public void startExistingRecipeIngredientId_invalidConversionFactor_notSaved() {
        // Arrange
        double invalidConversionFactor = UnitOfMeasureConstants.MAX_CONVERSION_FACTOR + 0.01;
        MeasurementModel invalidConversionFactorUpdatedFromUi = new MeasurementModel(
                MEASUREMENT_EXISTING_VALID.getSubtype(),
                MEASUREMENT_EXISTING_VALID.getNumberOfItems(),
                invalidConversionFactor,
                MEASUREMENT_EXISTING_VALID.getTotalMeasurementOne(),
                MEASUREMENT_EXISTING_VALID.getTotalMeasurementTwo(),
                MEASUREMENT_EXISTING_VALID.getItemMeasurementOne(),
                MEASUREMENT_EXISTING_VALID.getItemMeasurementTwo(),
                MEASUREMENT_EXISTING_VALID.getItemBaseUnits()
        );

        // Act
        SUT.start(QUANTITY_EXISTING_VALID_METRIC.getId());
        // Assert
        verifyRepoRecipeIngredientCalledReturnExistingValidMetric();
        verifyRepoIngredientCalledAndReturnExistingValidNameDescription();
        verifyRepoPortionsCalledAndReturnExistingValidNinePortions();

        SUT.setModel(invalidConversionFactorUpdatedFromUi);
        // Assert
        verifyNoMoreInteractions(repositoryRecipeIngredientMock);
    }

    @Test
    public void startExistingRecipeIngredientId_invalidConversionFactor_INVALID_CONVERSION_FACTOR() {
        // Arrange
        double invalidConversionFactor = UnitOfMeasureConstants.MAX_CONVERSION_FACTOR + 0.01;
        MeasurementModel invalidConversionFactorUpdatedFromUi = new MeasurementModel(
                MEASUREMENT_EXISTING_VALID.getSubtype(),
                MEASUREMENT_EXISTING_VALID.getNumberOfItems(),
                invalidConversionFactor,
                MEASUREMENT_EXISTING_VALID.getTotalMeasurementOne(),
                MEASUREMENT_EXISTING_VALID.getTotalMeasurementTwo(),
                MEASUREMENT_EXISTING_VALID.getItemMeasurementOne(),
                MEASUREMENT_EXISTING_VALID.getItemMeasurementTwo(),
                MEASUREMENT_EXISTING_VALID.getItemBaseUnits()
        );

        // Act
        SUT.start(QUANTITY_EXISTING_VALID_METRIC.getId());
        // Assert
        verifyRepoRecipeIngredientCalledReturnExistingValidMetric();
        verifyRepoIngredientCalledAndReturnExistingValidNameDescription();
        verifyRepoPortionsCalledAndReturnExistingValidNinePortions();

        SUT.setModel(invalidConversionFactorUpdatedFromUi);
        // Assert
        verify(viewModelMock, times(2)).setResult(
                resultArgumentCaptor.capture());
        ResultStatus actualResultStatus = resultArgumentCaptor.getValue().getResult();
        assertEquals(ResultStatus.INVALID_CONVERSION_FACTOR, actualResultStatus);
    }

    @Test
    public void startExistingRecipeIngredientId_invalidConversionFactor_measurementValuesNotChanged() {
        // Arrange
        double invalidConversionFactor = UnitOfMeasureConstants.MAX_CONVERSION_FACTOR + 0.01;
        MeasurementModel invalidConversionFactorUpdatedFromUi = new MeasurementModel(
                MEASUREMENT_EXISTING_VALID.getSubtype(),
                MEASUREMENT_EXISTING_VALID.getNumberOfItems(),
                invalidConversionFactor,
                MEASUREMENT_EXISTING_VALID.getTotalMeasurementOne(),
                MEASUREMENT_EXISTING_VALID.getTotalMeasurementTwo(),
                MEASUREMENT_EXISTING_VALID.getItemMeasurementOne(),
                MEASUREMENT_EXISTING_VALID.getItemMeasurementTwo(),
                MEASUREMENT_EXISTING_VALID.getItemBaseUnits()
        );
        MeasurementModel expectedModelReturnedToUi = new MeasurementModel(
                MEASUREMENT_EXISTING_VALID.getSubtype(),
                MEASUREMENT_EXISTING_VALID.getNumberOfItems(),
                MEASUREMENT_EXISTING_VALID.getConversionFactor(),
                MEASUREMENT_EXISTING_VALID.getTotalMeasurementOne(),
                MEASUREMENT_EXISTING_VALID.getTotalMeasurementTwo(),
                MEASUREMENT_EXISTING_VALID.getItemMeasurementOne(),
                MEASUREMENT_EXISTING_VALID.getItemMeasurementTwo(),
                MEASUREMENT_EXISTING_VALID.getItemBaseUnits()
        );

        // Act
        SUT.start(QUANTITY_EXISTING_VALID_METRIC.getId());
        // Assert
        verifyRepoRecipeIngredientCalledReturnExistingValidMetric();
        verifyRepoIngredientCalledAndReturnExistingValidNameDescription();
        verifyRepoPortionsCalledAndReturnExistingValidNinePortions();

        SUT.setModel(invalidConversionFactorUpdatedFromUi);
        // Assert
        verify(viewModelMock, times(2)).setResult(
                resultArgumentCaptor.capture());
        MeasurementModel actualModelReturnedToUi = resultArgumentCaptor.getValue().getModel();
        assertEquals(expectedModelReturnedToUi, actualModelReturnedToUi);
    }

    @Test
    public void startExistingRecipeIngredientId_userUpdateExistingFlow_allValuesAsExpected() {
        // Arrange
        int portions = MEASUREMENT_EXISTING_VALID.getNumberOfItems();
        double twoTeaspoons = 2;
        double volumePerTeaspoon = 5;
        double itemBaseUnitsWithConversionFactorApplied =
                (twoTeaspoons * volumePerTeaspoon / portions) *
                        UnitOfMeasureConstants.MAX_CONVERSION_FACTOR;
        when(timeProviderMock.getCurrentTimestamp()).thenReturn(
                QUANTITY_EXISTING_VALID_METRIC.getCreateDate(),
                INGREDIENT_EXISTING_VALID_NAME_DESCRIPTION.getLastUpdate(),
                QUANTITY_EXISTING_VALID_METRIC.getLastUpdate());

        MeasurementModel initialModelSetToUi = new MeasurementModel(
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
                initialModelSetToUi.getNumberOfItems(),
                initialModelSetToUi.getConversionFactor(),
                initialModelSetToUi.getTotalMeasurementOne(),
                initialModelSetToUi.getTotalMeasurementTwo(),
                initialModelSetToUi.getItemMeasurementOne(),
                initialModelSetToUi.getItemMeasurementTwo(),
                initialModelSetToUi.getItemBaseUnits()
        );

        MeasurementModel expectedResultFromUnitOfMeasureChange = new MeasurementModel(
                unitOfMeasureChangeToImperialSpoonFromUi.getSubtype(),
                portions,
                initialModelSetToUi.getConversionFactor(),
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
        verify(viewModelMock, times(1)).setResult(
                resultArgumentCaptor.capture());
        MeasurementModel existingDataLoadResult = resultArgumentCaptor.getValue().getModel();
        assertEquals(initialModelSetToUi, existingDataLoadResult);
        assertEquals(ResultStatus.RESULT_OK, resultArgumentCaptor.getValue().getResult());

        //** Start of simulated user interaction **//
        // user updates unit of measure
        SUT.setModel(unitOfMeasureChangeToImperialSpoonFromUi);
        // verify expected UI updates returned
        verify(viewModelMock, times(2)).setResult(
                resultArgumentCaptor.capture());
        MeasurementModel actualResultFromUnitOfMeasureChange =
                resultArgumentCaptor.getValue().getModel();
        assertEquals(expectedResultFromUnitOfMeasureChange, actualResultFromUnitOfMeasureChange);
        assertEquals(ResultStatus.INVALID_MEASUREMENT, resultArgumentCaptor.getValue().getResult());
        // confirm nothing saved
        verifyNoMoreInteractions(repositoryRecipeIngredientMock);

        // user updates measurement unit one
        SUT.setModel(measurementOneChangeFromUi);
        // verify expected UI updates returned
        verify(viewModelMock, times(3)).setResult(
                resultArgumentCaptor.capture());
        MeasurementModel actualResultFromMeasurementOneChange =
                resultArgumentCaptor.getValue().getModel();
        assertEquals(expectedResultFromMeasurementOneChange, actualResultFromMeasurementOneChange);
        assertEquals(ResultStatus.RESULT_OK, resultArgumentCaptor.getValue().getResult());
        // verify valid measurement saved to quantity entity
        verify(repositoryRecipeIngredientMock).save(recipeIngredientCaptor.capture());
        RecipeIngredientQuantityEntity actualQuantityEntity = recipeIngredientCaptor.getValue();
        assertEquals(expectedQuantityEntitySaveAfterMeasurementOneChange, actualQuantityEntity);

        // user updates conversion factor
        SUT.setModel(conversionFactorChangeFromUi);
        // verify expected UI updates returned
        verify(viewModelMock, times(4)).setResult(
                resultArgumentCaptor.capture());
        MeasurementModel actualResultFromConversionFactorChange =
                resultArgumentCaptor.getValue().getModel();
        assertEquals(expectedResultFromConversionFactorChanged,
                actualResultFromConversionFactorChange);
        assertEquals(ResultStatus.RESULT_OK, resultArgumentCaptor.getValue().getResult());
        // verify ingredient conversion factor saved
        verify(repositoryIngredientMock).save(ingredientArgumentCaptor.capture());
        IngredientEntity actualIngredientEntity = ingredientArgumentCaptor.getValue();
        assertEquals(expectedIngredientEntitySaveAfterConversionFactorUpdated,
                actualIngredientEntity);
        // verify valid measurement saved to quantity entity
        verify(repositoryRecipeIngredientMock, times(2)).save(
                recipeIngredientCaptor.capture());
        RecipeIngredientQuantityEntity actualQuantityEntityWithUpdatedConversion =
                recipeIngredientCaptor.getValue();
        assertEquals(expectedQuantityEntityAfterConversionFactorApplied,
                actualQuantityEntityWithUpdatedConversion);
    }

    // region helper methods -----------------------------------------------------------------------
    private void verifyRepoIngredientCalledReturnNewValidNameValidDescription() {
        verify(repositoryIngredientMock).getById(eq(
                INGREDIENT_NEW_VALID_NAME_DESCRIPTION.getId()),
                getIngredientCallbackCaptor.capture());
        getIngredientCallbackCaptor.getValue().onEntityLoaded(
                INGREDIENT_NEW_VALID_NAME_DESCRIPTION);
    }

    private void verifyRepoPortionsCalledReturnNewValidFourPortions() {
        verify(repositoryRecipePortionsMock).getPortionsForRecipe(
                eq(QUANTITY_NEW_INVALID.getRecipeId()),
                getRecipePortionsCallbackCaptor.capture());
        getRecipePortionsCallbackCaptor.getValue().onEntityLoaded(
                PORTIONS_NEW_VALID_FOUR);
    }

    private void verifyRepoRecipeIngredientCalledReturnExistingValidMetric() {
        verify(repositoryRecipeIngredientMock).getById(eq(QUANTITY_EXISTING_VALID_METRIC.getId()),
                getRecipeIngredientCallbackCaptor.capture());
        getRecipeIngredientCallbackCaptor.getValue().onEntityLoaded(QUANTITY_EXISTING_VALID_METRIC);
    }

    private void verifyRepoIngredientCalledAndReturnExistingValidNameDescription() {
        verify(repositoryIngredientMock).getById(
                eq(INGREDIENT_EXISTING_VALID_NAME_DESCRIPTION.getId()),
                getIngredientCallbackCaptor.capture());
        getIngredientCallbackCaptor.getValue().onEntityLoaded(
                INGREDIENT_EXISTING_VALID_NAME_DESCRIPTION);
    }

    private void verifyRepoPortionsCalledAndReturnExistingValidNinePortions() {
        verify(repositoryRecipePortionsMock).getPortionsForRecipe(
                eq(QUANTITY_EXISTING_VALID_METRIC.getRecipeId()),
                getRecipePortionsCallbackCaptor.capture());
        getRecipePortionsCallbackCaptor.getValue().onEntityLoaded(PORTIONS_EXISTING_VALID_NINE);
    }

    private void verifyRepoIngredientCalledAndReturnNewValidName() {
        verify(repositoryIngredientMock).getById(
                eq(INGREDIENT_NEW_VALID_NAME.getId()),
                getIngredientCallbackCaptor.capture());
        getIngredientCallbackCaptor.getValue().onEntityLoaded(INGREDIENT_NEW_VALID_NAME);
    }

    private void verifyRepoPortionsCalledAndReturnNewValidFourPortions() {
        verify(repositoryRecipePortionsMock).getPortionsForRecipe(
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