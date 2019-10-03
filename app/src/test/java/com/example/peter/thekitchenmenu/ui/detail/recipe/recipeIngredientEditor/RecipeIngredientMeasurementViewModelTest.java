package com.example.peter.thekitchenmenu.ui.detail.recipe.recipeIngredientEditor;

import android.content.res.Resources;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;

import com.example.peter.thekitchenmenu.R;
import com.example.peter.thekitchenmenu.app.Constants;
import com.example.peter.thekitchenmenu.data.entity.RecipeIngredientEntity;
import com.example.peter.thekitchenmenu.data.entity.RecipePortionsEntity;
import com.example.peter.thekitchenmenu.data.repository.DataSource;
import com.example.peter.thekitchenmenu.data.repository.RepositoryRecipeIngredient;
import com.example.peter.thekitchenmenu.data.repository.RepositoryRecipePortions;
import com.example.peter.thekitchenmenu.testdata.IngredientEntityTestData;
import com.example.peter.thekitchenmenu.testdata.ProductEntityTestData;
import com.example.peter.thekitchenmenu.testdata.RecipeEntityTestData;
import com.example.peter.thekitchenmenu.ui.detail.recipe.recipeingredienteditor.RecipeIngredientMeasurementViewModel;
import com.example.peter.thekitchenmenu.utils.ParseIntegerFromObservableHandler;
import com.example.peter.thekitchenmenu.utils.TimeProvider;
import com.example.peter.thekitchenmenu.utils.UniqueIdProvider;
import com.example.peter.thekitchenmenu.utils.unitofmeasure.MeasurementSubtype;
import com.example.peter.thekitchenmenu.utils.unitofmeasure.UnitOfMeasureConstants;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

public class RecipeIngredientMeasurementViewModelTest {

    // region constants ----------------------------------------------------------------------------
    private static final String NUMBER_FORMAT_EXCEPTION_ERROR = "Please enter numbers only.";
    private static final int ONE_PORTION = 1;
    private static final int TEN_PORTIONS = 10;
    private static final int HUNDRED_PORTIONS = 100;
    private static final int SPINNER_POSITION_IMPERIAL_MASS = 1;
    private static final int SPINNER_POSITION_COUNT = MeasurementSubtype.COUNT.asInt();

    private RecipeIngredientEntity NEW_INVALID = new RecipeIngredientEntity(
            "newId",
            RecipeEntityTestData.getInvalidNew().getId(),
            IngredientEntityTestData.getNew().getId(),
            ProductEntityTestData.getNewInvalid().getId(),
            0,
            0,
            Constants.getUserId().getValue(),
            10L,
            10L
    );

    private RecipeIngredientEntity NEW_VALID_METRIC = new RecipeIngredientEntity(
            "new_valid_measurement_one_id",
            RecipeEntityTestData.getValidNew().getId(),
            IngredientEntityTestData.getNewValidName().getId(),
            "",
            1500,
            0,
            Constants.getUserId().getValue(),
            10L,
            10L
    );

    private RecipeIngredientEntity NEW_VALID_IMPERIAL = new RecipeIngredientEntity(
            NEW_VALID_METRIC.getId(),
            RecipeEntityTestData.getValidNew().getId(),
            IngredientEntityTestData.getNewValidName().getId(),
            "",
            144.5825679375, // 5.1lbs
            1,
            Constants.getUserId().getValue(),
            20L,
            20L
    );

    private static final double NEW_VALID_IMPERIAL_OZ = 5.1;

    private RecipeIngredientEntity EXISTING_VALID_METRIC = new RecipeIngredientEntity(
            "existing_valid_id",
            RecipeEntityTestData.getValidExisting().getId(),
            IngredientEntityTestData.getExistingValidNameValidDescription().getId(),
            ProductEntityTestData.getExistingValid().getId(),
            2000,
            2,
            Constants.getUserId().getValue(),
            30L,
            30L
    );

    // endregion constants -------------------------------------------------------------------------

    // region helper fields ------------------------------------------------------------------------
    @Rule
    public InstantTaskExecutorRule instantExecutorRule = new InstantTaskExecutorRule();
    @Mock
    private RepositoryRecipePortions repositoryRecipePortionsMock;
    @Mock
    private RepositoryRecipeIngredient dataSourceMock;
    @Captor
    ArgumentCaptor<DataSource.GetEntityCallback<RecipeIngredientEntity>>
            getRecipeIngredientCallbackCaptor;
    @Captor
    ArgumentCaptor<DataSource.GetEntityCallback<RecipePortionsEntity>>
            getEntityRecipePortionsCallbackCaptor;
    @Mock
    private TimeProvider timeProviderMock;
    @Mock
    private Resources resourcesMock;
    @Mock
    private UniqueIdProvider idProviderMock;
    @Mock
    ParseIntegerFromObservableHandler intFromObservableMock;
    // endregion helper fields ---------------------------------------------------------------------

    private RecipeIngredientMeasurementViewModel SUT;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        setupResources();

        SUT = new RecipeIngredientMeasurementViewModel(
                repositoryRecipePortionsMock,
                dataSourceMock,
                resourcesMock,
                idProviderMock,
                timeProviderMock
        );
    }

    @Test
    public void startNewRecipeAndIngredientId_databaseNotCalled() {
        // Arrange
        // Act
        SUT.start(NEW_INVALID.getRecipeId(), NEW_INVALID.getIngredientId());
        // Assert
        verifyNoMoreInteractions(dataSourceMock);
    }

    @Test
    public void startNewRecipeAndIngredientId_measurementTypeDefaultMetricMass() {
        // Arrange
        // Act
        SUT.start(NEW_INVALID.getRecipeId(), NEW_INVALID.getIngredientId());
        // Assert
        assertEquals(MeasurementSubtype.METRIC_MASS, SUT.subtype.get());
    }

    @Test
    public void startNewRecipeAndIngredientId_spinnerSetToPositionZero() {
        // Arrange
        // Act
        SUT.start(NEW_INVALID.getRecipeId(), NEW_INVALID.getIngredientId());
        // Assert
        assertEquals(0, SUT.unitOfMeasureSpinnerInt.get());
    }

    @Test
    public void startNewRecipeAndIngredientId_noOfUnitsTwo() {
        // Arrange
        // Act
        SUT.start(NEW_INVALID.getRecipeId(), NEW_INVALID.getIngredientId());
        // Assert
        assertEquals(2, SUT.numberOfMeasurementUnits.get());
    }

    @Test
    public void startNewRecipeAndIngredientId_validMeasurementOneUpdated_saved() {
        // Arrange
        whenIdProviderReturnNewValidId();
        whenTimeProviderThenReturnNewValidTime();
        // Act
        SUT.start(NEW_VALID_METRIC.getRecipeId(), NEW_VALID_METRIC.getIngredientId());
        SUT.measurementOne.set(String.valueOf((int) NEW_VALID_METRIC.getBaseUnits()));
        // Assert
        verify(dataSourceMock).save(eq(NEW_VALID_METRIC));
    }

    @Test
    public void startNewRecipeAndIngredientId_measurementTwoUpdated_saved() {
        // Arrange
        whenIdProviderReturnNewValidId();
        whenTimeProviderThenReturnNewValidTime();
        // Act
        SUT.start(NEW_VALID_METRIC.getRecipeId(), NEW_VALID_METRIC.getIngredientId());
        SUT.measurementOne.set(String.valueOf((int) NEW_VALID_METRIC.getBaseUnits() % 1000));
        SUT.measurementTwo.set(String.valueOf((int) NEW_VALID_METRIC.getBaseUnits() / 1000));
        // Assert
        verify(dataSourceMock).save(eq(NEW_VALID_METRIC));
    }

    @Test
    public void startNewRecipeAndIngredientId_measurementTwoUpdatedInvalidValue_notSaved() {
        // Arrange
        whenIdProviderReturnNewValidId();
        whenTimeProviderThenReturnNewValidTime();
        // Act
        SUT.start(NEW_VALID_METRIC.getRecipeId(), NEW_VALID_METRIC.getIngredientId());
        SUT.measurementTwo.set(String.valueOf(UnitOfMeasureConstants.MAXIMUM_MASS / 1000 + 1));
        // Assert
        verifyNoMoreInteractions(dataSourceMock);
    }

    @Test
    public void startNewRecipeAndIngredientId_measurementTwoUpdatedInvalidValue_originalValueRestored() {
        // Arrange
        whenIdProviderReturnNewValidId();
        whenTimeProviderThenReturnNewValidTime();
        // Act
        SUT.start(NEW_VALID_METRIC.getRecipeId(), NEW_VALID_METRIC.getIngredientId());
        SUT.measurementTwo.set(String.valueOf((int) UnitOfMeasureConstants.MAXIMUM_MASS / 1000 + 1));
        // Assert
        assertEquals("", SUT.measurementOne.get());
        assertEquals("", SUT.measurementTwo.get());
    }

    @Test
    public void startNewRecipeAndIngredientId_unitOfMeasureImperialMass_subtypeUpdated() {
        // Arrange
        // Act
        SUT.start(NEW_VALID_METRIC.getRecipeId(), NEW_VALID_METRIC.getIngredientId());
        SUT.unitOfMeasureSpinnerInt.set(MeasurementSubtype.IMPERIAL_MASS.asInt());
        // Assert
        assertEquals(MeasurementSubtype.IMPERIAL_MASS, SUT.subtype.get());
    }

    @Test
    public void startNewRecipeAndIngredientId_unitOfMeasureImperialMass_decimalMeasurementProcessed() {
        // Arrange
        when(timeProviderMock.getCurrentTimestamp()).thenReturn(NEW_VALID_IMPERIAL.getCreateDate());
        when(idProviderMock.getUId()).thenReturn(NEW_VALID_IMPERIAL.getId());
        // Act
        SUT.start(NEW_VALID_IMPERIAL.getRecipeId(), NEW_VALID_IMPERIAL.getIngredientId());
        SUT.unitOfMeasureSpinnerInt.set(MeasurementSubtype.IMPERIAL_MASS.asInt());
        SUT.measurementOne.set(String.valueOf(NEW_VALID_IMPERIAL_OZ));
        // Assert
        verify(dataSourceMock).save(eq(NEW_VALID_IMPERIAL));
    }

    @Test
    public void startNewRecipeAndIngredientId_unitOfMeasureCount_numberOfUnitsOne() {
        // Arrange
        // Act
        SUT.start(NEW_INVALID.getRecipeId(), NEW_INVALID.getIngredientId());
        SUT.unitOfMeasureSpinnerInt.set(MeasurementSubtype.COUNT.asInt());
        // Assert
        assertEquals(1, SUT.numberOfMeasurementUnits.get());
    }

    @Test
    public void startExistingRecipeAndIngredientId_existingModelRetrievedFromDatabase() {
        // Arrange
        // Act
        SUT.start(EXISTING_VALID_METRIC.getId());
        // Assert
    }

    // startExistingRecipeAndIngredientId_existingModelValuesSetToObservables
    // startExistingRecipeAndIngredientId_unitOneUpdated_saved
    // startExistingRecipeAndIngredientId_unitTwoUpdated_saved
    // startExistingRecipeAndIngredientId_unitTwoUpdatedInvalidValue_errorMessageSet
    // startExistingRecipeAndIngredientId_

    private void setupResources() {
        when(resourcesMock.getString(eq(R.string.number_format_exception))).
                thenReturn(NUMBER_FORMAT_EXCEPTION_ERROR);
    }

    private void whenIdProviderReturnNewValidId() {
        when(idProviderMock.getUId()).thenReturn(NEW_VALID_METRIC.getId());
    }

    private void whenTimeProviderThenReturnNewValidTime() {
        when(timeProviderMock.getCurrentTimestamp()).
                thenReturn(NEW_VALID_METRIC.getCreateDate());
    }
}
