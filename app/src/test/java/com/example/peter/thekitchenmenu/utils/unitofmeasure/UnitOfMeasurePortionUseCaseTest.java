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
import com.example.peter.thekitchenmenu.testdata.RecipeIngredientQuantityEntityTestData;
import com.example.peter.thekitchenmenu.testdata.RecipePortionsEntityTestData;
import com.example.peter.thekitchenmenu.utils.TimeProvider;
import com.example.peter.thekitchenmenu.utils.UniqueIdProvider;

import org.junit.*;
import org.mockito.*;

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

    private MeasurementModel MEASUREMENT_EMPTY = new MeasurementModel(
            MeasurementSubtype.METRIC_MASS,
            PORTIONS_NEW_VALID_FOUR.getServings() *
                    PORTIONS_NEW_VALID_FOUR.getSittings(),
            1,
            0,
            0,
            0,
            0,
            0
    );
    private MeasurementResult RESULT_INVALID_MEASUREMENT = new MeasurementResult(
            MEASUREMENT_EMPTY,
            MeasurementResult.ResultStatus.INVALID_MEASUREMENT
    );

    private MeasurementModel MEASUREMENT_EXISTING_VALID = new MeasurementModel(
            MeasurementSubtype.fromInt(QUANTITY_EXISTING_VALID_METRIC.getUnitOfMeasureSubtype()),
            portions(),
            INGREDIENT_EXISTING_VALID_NAME_DESCRIPTION.getConversionFactor(),
            totalOne(),
            totalTwo(),
            QUANTITY_EXISTING_VALID_METRIC.getItemBaseUnits() % 1000,
            (int) QUANTITY_EXISTING_VALID_METRIC.getItemBaseUnits() / 1000,
            QUANTITY_EXISTING_VALID_METRIC.getItemBaseUnits()
    );

    private int portions() {
        return PORTIONS_EXISTING_VALID_NINE.getServings() *
                PORTIONS_EXISTING_VALID_NINE.getServings();
    }

    private double totalOne() {
        return QUANTITY_EXISTING_VALID_METRIC.getItemBaseUnits() * portions() % 1000;
    }

    private int totalTwo() {
        return (int) QUANTITY_EXISTING_VALID_METRIC.getItemBaseUnits() * portions() / 1000;
    }

    private MeasurementResult RESULT_EXISTING_VALID_METRIC = new MeasurementResult(
            MEASUREMENT_EXISTING_VALID,
            MeasurementResult.ResultStatus.RESULT_OK);

    private final double DELTA = 0.0001;
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
                timeProviderMock,
                viewModelMock);
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
        verify(viewModelMock).setResult(eq(RESULT_INVALID_MEASUREMENT));
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
        verify(viewModelMock).setResult(eq(RESULT_INVALID_MEASUREMENT));
    }

    @Test
    public void startNewRecipeAndIngredientId_invalidTotalMeasurementOneUpdated_notSaved() {
        // Arrange
        whenIdProviderReturnNewValidId();
        whenTimeProviderThenReturnNewValidTime();

        MeasurementModel model = new MeasurementModel(
                MeasurementSubtype.fromInt(QUANTITY_NEW_VALID_METRIC.getUnitOfMeasureSubtype()),
                PORTIONS_NEW_VALID_FOUR.getServings() *
                        PORTIONS_NEW_VALID_FOUR.getSittings(),
                INGREDIENT_NEW_VALID_NAME.getConversionFactor(),
                UnitOfMeasureConstants.MAXIMUM_MASS + 1,
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

        SUT.setModel(model); // Set new measurement
        // Assert
        verifyNoMoreInteractions(repositoryRecipeIngredientMock);
    }

    @Test
    public void startNewRecipeAndIngredientId_invalidTotalMeasurementOneUpdated_INVALID_TOTAL_MEASUREMENT_ONE() {
        // Arrange
        ArgumentCaptor<MeasurementResult> ac = ArgumentCaptor.forClass(MeasurementResult.class);
        whenIdProviderReturnNewValidId();
        whenTimeProviderThenReturnNewValidTime();

        MeasurementModel model = new MeasurementModel(
                MeasurementSubtype.fromInt(QUANTITY_NEW_VALID_METRIC.getUnitOfMeasureSubtype()),
                PORTIONS_NEW_VALID_FOUR.getServings() *
                        PORTIONS_NEW_VALID_FOUR.getSittings(),
                INGREDIENT_NEW_VALID_NAME.getConversionFactor(),
                UnitOfMeasureConstants.MAXIMUM_MASS + 1,
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

        SUT.setModel(model); // Set new measurement
        // Assert
        verify(viewModelMock, times(2)).setResult(ac.capture());
        MeasurementResult measurementResult = ac.getValue();
        assertEquals(MeasurementResult.ResultStatus.INVALID_TOTAL_MEASUREMENT_ONE,
                measurementResult.getResult());
    }

    @Test
    public void startNewRecipeAndIngredientId_validTotalMeasurementOneUpdated_saved() {
        // Arrange
        whenIdProviderReturnNewValidId();
        whenTimeProviderThenReturnNewValidTime();

        int totalBaseUnits = (int) QUANTITY_NEW_VALID_METRIC.getItemBaseUnits() *
                PORTIONS_NEW_VALID_FOUR.getServings() *
                PORTIONS_NEW_VALID_FOUR.getSittings();

        MeasurementModel model = new MeasurementModel(
                MeasurementSubtype.fromInt(QUANTITY_NEW_VALID_METRIC.getUnitOfMeasureSubtype()),
                PORTIONS_NEW_VALID_FOUR.getServings() *
                        PORTIONS_NEW_VALID_FOUR.getSittings(),
                INGREDIENT_NEW_VALID_NAME.getConversionFactor(),
                totalBaseUnits, // update to measurementOne
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

        SUT.setModel(model); // Set new measurement
        // Assert
        verify(repositoryRecipeIngredientMock).save(eq(QUANTITY_NEW_VALID_METRIC));
    }

    @Test
    public void startNewRecipeAndIngredientId_validTotalMeasurementOneUpdated_RESULT_OK() {
        // Arrange
        ArgumentCaptor<MeasurementResult> ac = ArgumentCaptor.forClass(MeasurementResult.class);
        whenIdProviderReturnNewValidId();
        whenTimeProviderThenReturnNewValidTime();

        int portions = PORTIONS_NEW_VALID_FOUR.getServings() * PORTIONS_NEW_VALID_FOUR.getSittings();
        int totalBaseUnits = (int) QUANTITY_NEW_VALID_METRIC.getItemBaseUnits() * portions;

        MeasurementModel inputModel = new MeasurementModel(
                MeasurementSubtype.fromInt(QUANTITY_NEW_VALID_METRIC.getUnitOfMeasureSubtype()),
                PORTIONS_NEW_VALID_FOUR.getServings() *
                        PORTIONS_NEW_VALID_FOUR.getSittings(),
                INGREDIENT_NEW_VALID_NAME.getConversionFactor(),
                totalBaseUnits, // update to measurementOne
                0,
                0,
                0,
                0
        );

        MeasurementModel outputModel = new MeasurementModel(
                MeasurementSubtype.fromInt(QUANTITY_NEW_VALID_METRIC.getUnitOfMeasureSubtype()),
                PORTIONS_NEW_VALID_FOUR.getServings() *
                        PORTIONS_NEW_VALID_FOUR.getSittings(),
                INGREDIENT_NEW_VALID_NAME.getConversionFactor(),
                totalBaseUnits, // update to measurementOne
                0,
                totalBaseUnits / portions,
                0,
                totalBaseUnits / portions
        );

        // Act
        SUT.start(QUANTITY_NEW_VALID_METRIC.getRecipeId(),
                QUANTITY_NEW_VALID_METRIC.getIngredientId());
        verifyRepoIngredientCalledAndReturnNewValidName();
        verifyRepoPortionsCalledAndReturnNewValidFourPortions();

        SUT.setModel(inputModel); // Set new measurement
        // Assert
        verify(viewModelMock, times(2)).setResult(ac.capture());
        MeasurementModel measurementModel = ac.getValue().getModel();
        MeasurementResult.ResultStatus resultStatus = ac.getValue().getResult();
        assertEquals(outputModel, measurementModel);
        assertEquals(MeasurementResult.ResultStatus.RESULT_OK, resultStatus);
    }

    @Test
    public void startNewRecipeAndIngredientId_invalidTotalMeasurementTwoUpdated_notSaved() {
        // Arrange
        whenIdProviderReturnNewValidId();
        whenTimeProviderThenReturnNewValidTime();

        MeasurementModel model = new MeasurementModel(
                MeasurementSubtype.fromInt(QUANTITY_NEW_VALID_METRIC.getUnitOfMeasureSubtype()),
                PORTIONS_NEW_VALID_FOUR.getServings() *
                        PORTIONS_NEW_VALID_FOUR.getSittings(),
                INGREDIENT_NEW_VALID_NAME.getConversionFactor(),
                0,
                (int) UnitOfMeasureConstants.MAXIMUM_MASS / 1000 + 1,
                0,
                0,
                0
        );

        // Act
        SUT.start(QUANTITY_NEW_VALID_METRIC.getRecipeId(),
                QUANTITY_NEW_VALID_METRIC.getIngredientId());
        verifyRepoIngredientCalledAndReturnNewValidName();
        verifyRepoPortionsCalledAndReturnNewValidFourPortions();

        SUT.setModel(model); // Set new measurement
        // Assert
        verifyNoMoreInteractions(repositoryRecipeIngredientMock);
    }

    @Test
    public void startNewRecipeAndIngredientId_invalidTotalMeasurementTwoUpdated_INVALID_TOTAL_MEASUREMENT_TWO() {
        // Arrange
        ArgumentCaptor<MeasurementResult> ac = ArgumentCaptor.forClass(MeasurementResult.class);
        whenIdProviderReturnNewValidId();
        whenTimeProviderThenReturnNewValidTime();

        MeasurementModel model = new MeasurementModel(
                MeasurementSubtype.fromInt(QUANTITY_NEW_VALID_METRIC.getUnitOfMeasureSubtype()),
                PORTIONS_NEW_VALID_FOUR.getServings() *
                        PORTIONS_NEW_VALID_FOUR.getSittings(),
                INGREDIENT_NEW_VALID_NAME.getConversionFactor(),
                0,
                (int) UnitOfMeasureConstants.MAXIMUM_MASS / 1000 + 1,
                0,
                0,
                0
        );

        // Act
        SUT.start(QUANTITY_NEW_VALID_METRIC.getRecipeId(),
                QUANTITY_NEW_VALID_METRIC.getIngredientId());
        verifyRepoIngredientCalledAndReturnNewValidName();
        verifyRepoPortionsCalledAndReturnNewValidFourPortions();

        SUT.setModel(model); // Set new measurement
        // Assert
        verify(viewModelMock, times(2)).setResult(ac.capture());
        MeasurementResult measurementResult = ac.getValue();
        assertEquals(MeasurementResult.ResultStatus.INVALID_TOTAL_MEASUREMENT_TWO,
                measurementResult.getResult());
    }

    @Test
    public void startNewRecipeAndIngredientId_validTotalMeasurementTwoUpdated_saved() {
        // Arrange
        ArgumentCaptor<RecipeIngredientQuantityEntity> ac =
                ArgumentCaptor.forClass(RecipeIngredientQuantityEntity.class);
        whenIdProviderReturnNewValidId();
        whenTimeProviderThenReturnNewValidTime();

        int portions = PORTIONS_NEW_VALID_FOUR.getServings() *
                PORTIONS_NEW_VALID_FOUR.getSittings();
        int totalBaseUnits = (int) UnitOfMeasureConstants.MAXIMUM_MASS;
        double expectedItemBaseUnits = UnitOfMeasureConstants.MAXIMUM_MASS / portions;

        MeasurementModel model = new MeasurementModel(
                MeasurementSubtype.fromInt(QUANTITY_NEW_VALID_METRIC.getUnitOfMeasureSubtype()),
                portions,
                INGREDIENT_NEW_VALID_NAME.getConversionFactor(),
                0,
                totalBaseUnits / 1000,
                0,
                0,
                0
        );

        // Act
        SUT.start(QUANTITY_NEW_VALID_METRIC.getRecipeId(),
                QUANTITY_NEW_VALID_METRIC.getIngredientId());
        verifyRepoIngredientCalledAndReturnNewValidName();
        verifyRepoPortionsCalledAndReturnNewValidFourPortions();

        SUT.setModel(model); // Set new measurement
        // Assert
        verify(repositoryRecipeIngredientMock).save(ac.capture());
        RecipeIngredientQuantityEntity quantityEntity = ac.getValue();
        assertEquals(expectedItemBaseUnits, quantityEntity.getItemBaseUnits(), DELTA);
    }

    @Test
    public void startNewRecipeAndIngredientId_validTotalMeasurementTwoUpdated_RESULT_OK() {
        // Arrange
        ArgumentCaptor<MeasurementResult> ac = ArgumentCaptor.forClass(MeasurementResult.class);
        whenIdProviderReturnNewValidId();
        whenTimeProviderThenReturnNewValidTime();

        int portions = PORTIONS_NEW_VALID_FOUR.getServings() *
                PORTIONS_NEW_VALID_FOUR.getSittings();
        int totalBaseUnits = (int) UnitOfMeasureConstants.MAXIMUM_MASS;

        MeasurementModel model = new MeasurementModel(
                MeasurementSubtype.fromInt(QUANTITY_NEW_VALID_METRIC.getUnitOfMeasureSubtype()),
                portions,
                INGREDIENT_NEW_VALID_NAME.getConversionFactor(),
                0,
                totalBaseUnits / 1000,
                0,
                0,
                0
        );

        // Act
        SUT.start(QUANTITY_NEW_VALID_METRIC.getRecipeId(),
                QUANTITY_NEW_VALID_METRIC.getIngredientId());
        verifyRepoIngredientCalledAndReturnNewValidName();
        verifyRepoPortionsCalledAndReturnNewValidFourPortions();

        SUT.setModel(model); // Set new measurement
        // Assert
        verify(viewModelMock, times(2)).setResult(ac.capture());
        MeasurementResult result = ac.getValue();
        assertEquals(MeasurementResult.ResultStatus.RESULT_OK, result.getResult());
    }

    @Test
    public void startNewRecipeAndIngredientId_unitOfMeasureChanged_INVALID_MEASUREMENT() {
        // Arrange
        ArgumentCaptor<MeasurementResult> ac = ArgumentCaptor.forClass(MeasurementResult.class);
        whenIdProviderReturnNewValidId();
        whenTimeProviderThenReturnNewValidTime();

        MeasurementModel expected = new MeasurementModel(
                MeasurementSubtype.IMPERIAL_MASS,
                MEASUREMENT_EMPTY.getNumberOfItems(),
                MEASUREMENT_EMPTY.getConversionFactor(),
                MEASUREMENT_EMPTY.getTotalMeasurementOne(),
                MEASUREMENT_EMPTY.getTotalMeasurementTwo(),
                MEASUREMENT_EMPTY.getItemMeasurementOne(),
                MEASUREMENT_EMPTY.getItemMeasurementTwo(),
                MEASUREMENT_EMPTY.getItemBaseUnits()
        );

        // Act
        SUT.start(QUANTITY_NEW_VALID_METRIC.getRecipeId(),
                QUANTITY_NEW_VALID_METRIC.getIngredientId());
        verifyRepoIngredientCalledAndReturnNewValidName();
        verifyRepoPortionsCalledAndReturnNewValidFourPortions();
        SUT.setModel(expected);
        // Assert
        verify(viewModelMock, times(3)).setResult(ac.capture());
        MeasurementResult result = ac.getValue();
        MeasurementModel actual = result.getModel();
        assertEquals(expected, actual);
    }

    // startNewRecipeAndIngredientId_unitOfMeasureChanged_emptyModel
    // startNewRecipeAndIngredientId_invalidConversionFactor_notSaved
    // startNewRecipeAndIngredientId_invalidConversionFactor_INVALID_CONVERSION_FACTOR
    // startNewRecipeAndIngredientId_validConversionFactor_saved
    // startNewRecipeAndIngredientId_validConversionFactor_itemBaseUnitsUpdated
    // startNewRecipeAndIngredientId_validConversionFactor_RESULT_OK

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
        SUT.start(QUANTITY_EXISTING_VALID_METRIC.getId());
        // Assert
        verifyRepoRecipeIngredientCalledReturnExistingValidMetric();
        verifyRepoIngredientCalledAndReturnExistingValidNameDescription();
        verifyRepoPortionsCalledAndReturnExistingValidNinePortions();
        verify(viewModelMock).setResult(RESULT_EXISTING_VALID_METRIC);
    }

    // startExistingRecipeIngredientId_invalidTotalMeasurementOneUpdated_notSaved
    // startExistingRecipeIngredientId_invalidTotalMeasurementTwoUpdated_INVALID_TOTAL_MEASUREMENT_TWO
    // startExistingRecipeIngredientId_validTotalMeasurementTwoUpdated_saved
    // startExistingRecipeIngredientId_validTotalMeasurementTwoUpdated_RESULT_OK
    // startExistingRecipeIngredientId_unitOfMeasureChanged_INVALID_MEASUREMENT
    // startExistingRecipeIngredientId_unitOfMeasureChanged_emptyModel
    // startExistingRecipeIngredientId_invalidConversionFactor_notSaved
    // startExistingRecipeIngredientId_invalidConversionFactor_INVALID_CONVERSION_FACTOR
    // startExistingRecipeIngredientId_validConversionFactor_saved
    // startExistingRecipeIngredientId_validConversionFactor_itemBaseUnitsUpdated
    // startExistingRecipeIngredientId_validConversionFactor_RESULT_OK


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