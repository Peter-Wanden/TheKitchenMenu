package com.example.peter.thekitchenmenu.ui.detail.recipe.recipeIngredientEditor;

import android.content.res.Resources;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;

import com.example.peter.thekitchenmenu.R;
import com.example.peter.thekitchenmenu.data.entity.RecipeIngredientEntity;
import com.example.peter.thekitchenmenu.data.entity.RecipePortionsEntity;
import com.example.peter.thekitchenmenu.data.repository.DataSource;
import com.example.peter.thekitchenmenu.data.repository.RepositoryRecipeIngredient;
import com.example.peter.thekitchenmenu.data.repository.RepositoryRecipePortions;
import com.example.peter.thekitchenmenu.testdata.RecipeIngredientEntityTestData;
import com.example.peter.thekitchenmenu.testdata.RecipePortionsEntityTestData;
import com.example.peter.thekitchenmenu.ui.detail.recipe.recipeingredienteditor.RecipeIngredientMeasurementViewModel;
import com.example.peter.thekitchenmenu.utils.TimeProvider;
import com.example.peter.thekitchenmenu.utils.UniqueIdProvider;
import com.example.peter.thekitchenmenu.utils.unitofmeasure.MeasurementSubtype;
import com.example.peter.thekitchenmenu.utils.unitofmeasure.UnitOfMeasure;
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

    private RecipeIngredientEntity NEW_INVALID =
            RecipeIngredientEntityTestData.getNewInvalid();

    private RecipeIngredientEntity NEW_VALID_METRIC =
            RecipeIngredientEntityTestData.getNewValidMetric();

    private RecipeIngredientEntity NEW_VALID_IMPERIAL =
            RecipeIngredientEntityTestData.getNewValidImperial();

    private static final double NEW_VALID_IMPERIAL_OZ = 5.1;

    private RecipeIngredientEntity EXISTING_VALID_METRIC =
            RecipeIngredientEntityTestData.getExistingValidMetric();

    private RecipePortionsEntity NEW_VALID_FOUR_PORTIONS =
            RecipePortionsEntityTestData.getNewValidFourPortions();

    private RecipePortionsEntity NEW_VALID_SIXTEEN_PORTIONS =
            RecipePortionsEntityTestData.getNewValidSixteenPortions();

    private RecipePortionsEntity EXISTING_VALID_NINE_PORTIONS =
            RecipePortionsEntityTestData.getExistingValid();

    private RecipeIngredientEntity EXISTING_VALID_METRIC_MEASUREMENT_UPDATED =
            RecipeIngredientEntityTestData.getExistingValidMetricMeasurementUpdated();
    // endregion constants -------------------------------------------------------------------------

    // region helper fields ------------------------------------------------------------------------
    @Rule
    public InstantTaskExecutorRule instantExecutorRule = new InstantTaskExecutorRule();
    @Mock
    private RepositoryRecipePortions repositoryRecipePortionsMock;
    @Mock
    private RepositoryRecipeIngredient repositoryRecipeIngredient;
    @Captor
    ArgumentCaptor<DataSource.GetEntityCallback<RecipeIngredientEntity>>
            getRecipeIngredientCallbackCaptor;
    @Captor
    ArgumentCaptor<DataSource.GetEntityCallback<RecipePortionsEntity>>
            getRecipePortionsCallbackCaptor;
    @Mock
    private TimeProvider timeProviderMock;
    @Mock
    private Resources resourcesMock;
    @Mock
    private UniqueIdProvider idProviderMock;
    // endregion helper fields ---------------------------------------------------------------------

    private RecipeIngredientMeasurementViewModel SUT;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        setupResources();

        SUT = new RecipeIngredientMeasurementViewModel(
                repositoryRecipePortionsMock,
                repositoryRecipeIngredient,
                resourcesMock,
                idProviderMock,
                timeProviderMock
        );
    }

    @Test
    public void startNewRecipeAndIngredientId_databaseNotCalled() {
        // Arrange
        when(idProviderMock.getUId()).thenReturn(NEW_INVALID.getId());
        // Act
        SUT.start(NEW_INVALID.getRecipeId(), NEW_INVALID.getIngredientId());
        // Assert
        verifyNoMoreInteractions(repositoryRecipeIngredient);
    }

    @Test
    public void startNewRecipeAndIngredientId_measurementTypeDefaultMetricMass() {
        // Arrange
        when(idProviderMock.getUId()).thenReturn(NEW_INVALID.getId());
        // Act
        SUT.start(NEW_INVALID.getRecipeId(), NEW_INVALID.getIngredientId());
        // Assert
        assertEquals(MeasurementSubtype.METRIC_MASS, SUT.subtype.get());
    }

    @Test
    public void startNewRecipeAndIngredientId_spinnerSetToPositionZero() {
        // Arrange
        when(idProviderMock.getUId()).thenReturn(NEW_INVALID.getId());
        // Act
        SUT.start(NEW_INVALID.getRecipeId(), NEW_INVALID.getIngredientId());
        // Assert
        assertEquals(0, SUT.unitOfMeasureSpinnerInt.get());
    }

    @Test
    public void startNewRecipeAndIngredientId_noOfUnitsTwo() {
        // Arrange
        when(idProviderMock.getUId()).thenReturn(NEW_INVALID.getId());
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

        int totalBaseUnits = (int) NEW_VALID_METRIC.getItemBaseUnits() *
                NEW_VALID_FOUR_PORTIONS.getServings() *
                NEW_VALID_FOUR_PORTIONS.getSittings();
        // Act
        SUT.start(NEW_VALID_METRIC.getRecipeId(), NEW_VALID_METRIC.getIngredientId());

        verify(repositoryRecipePortionsMock).getPortionsForRecipe(eq(NEW_VALID_METRIC.getRecipeId()),
                getRecipePortionsCallbackCaptor.capture());
        getRecipePortionsCallbackCaptor.getValue().onEntityLoaded(NEW_VALID_FOUR_PORTIONS);

        SUT.measurementOne.set(String.valueOf(totalBaseUnits));
        // Assert
        verify(repositoryRecipeIngredient).save(eq(NEW_VALID_METRIC));
    }

    @Test
    public void startNewRecipeAndIngredientId_measurementTwoUpdated_saved() {
        // Arrange
        whenIdProviderReturnNewValidId();
        whenTimeProviderThenReturnNewValidTime();

        int totalBaseUnits = (int) NEW_VALID_METRIC.getItemBaseUnits() *
                NEW_VALID_SIXTEEN_PORTIONS.getServings() *
                NEW_VALID_SIXTEEN_PORTIONS.getSittings();

        // Act
        SUT.start(NEW_VALID_METRIC.getRecipeId(), NEW_VALID_METRIC.getIngredientId());

        verify(repositoryRecipePortionsMock).getPortionsForRecipe(eq(NEW_VALID_METRIC.getRecipeId()),
                getRecipePortionsCallbackCaptor.capture());
        getRecipePortionsCallbackCaptor.getValue().onEntityLoaded(NEW_VALID_SIXTEEN_PORTIONS);

        SUT.measurementOne.set(String.valueOf(totalBaseUnits % 1000));
        SUT.measurementTwo.set(String.valueOf(totalBaseUnits / 1000));
        // Assert
        verify(repositoryRecipeIngredient).save(eq(NEW_VALID_METRIC));
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
        verifyNoMoreInteractions(repositoryRecipeIngredient);
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
        when(idProviderMock.getUId()).thenReturn(NEW_VALID_METRIC.getId());
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
        verify(repositoryRecipeIngredient).save(eq(NEW_VALID_IMPERIAL));
    }

    @Test
    public void startNewRecipeAndIngredientId_unitOfMeasureCount_numberOfUnitsOne() {
        // Arrange
        when(idProviderMock.getUId()).thenReturn(NEW_INVALID.getId());
        // Act
        SUT.start(NEW_INVALID.getRecipeId(), NEW_INVALID.getIngredientId());
        SUT.unitOfMeasureSpinnerInt.set(MeasurementSubtype.COUNT.asInt());
        // Assert
        assertEquals(1, SUT.numberOfMeasurementUnits.get());
    }

    @Test
    public void startExistingRecipeIngredientId_existingModelRetrievedFromDatabase() {
        // Arrange
        // Act
        SUT.start(EXISTING_VALID_METRIC.getId());
        // Assert
        verify(repositoryRecipeIngredient).getById(eq(EXISTING_VALID_METRIC.getId()),
                getRecipeIngredientCallbackCaptor.capture());
        getRecipeIngredientCallbackCaptor.getValue().onEntityLoaded(EXISTING_VALID_METRIC);

        verify(repositoryRecipePortionsMock).getPortionsForRecipe(
                eq(EXISTING_VALID_METRIC.getRecipeId()),
                getRecipePortionsCallbackCaptor.capture());
        getRecipePortionsCallbackCaptor.getValue().onEntityLoaded(EXISTING_VALID_NINE_PORTIONS);
    }

    @Test
    public void startExistingRecipeIngredientId_existingModelValuesSetToObservables() {
        // Arrange
        UnitOfMeasure unitOfMeasure = MeasurementSubtype.fromInt(
                EXISTING_VALID_METRIC.getUnitOfMeasureSubtype()).getMeasurementClass();

        int portions = EXISTING_VALID_NINE_PORTIONS.getServings() *
                EXISTING_VALID_NINE_PORTIONS.getSittings();
        // Act
        SUT.start(EXISTING_VALID_METRIC.getId());
        // Assert
        verify(repositoryRecipeIngredient).getById(eq(EXISTING_VALID_METRIC.getId()),
                getRecipeIngredientCallbackCaptor.capture());
        getRecipeIngredientCallbackCaptor.getValue().onEntityLoaded(EXISTING_VALID_METRIC);

        verify(repositoryRecipePortionsMock).getPortionsForRecipe(
                eq(EXISTING_VALID_METRIC.getRecipeId()),
                getRecipePortionsCallbackCaptor.capture());
        getRecipePortionsCallbackCaptor.getValue().onEntityLoaded(EXISTING_VALID_NINE_PORTIONS);

        assertEquals(EXISTING_VALID_METRIC.getUnitOfMeasureSubtype(), SUT.unitOfMeasureSpinnerInt.get());
        assertEquals(EXISTING_VALID_METRIC.getUnitOfMeasureSubtype(), SUT.subtype.get().asInt());
        assertEquals(unitOfMeasure.getNumberOfMeasurementUnits(), SUT.numberOfMeasurementUnits.get());

        assertEquals(
                (int) EXISTING_VALID_METRIC.getItemBaseUnits() * portions / 1000,
                Integer.parseInt(SUT.measurementTwo.get()));
        assertEquals(
                (int) EXISTING_VALID_METRIC.getItemBaseUnits() * portions % 1000 ,
                Integer.parseInt(SUT.measurementOne.get()));
    }

    @Test
    public void startExistingRecipeIngredientId_measurementUpdated_saved() {
        // Arrange
        when(timeProviderMock.getCurrentTimestamp()).thenReturn(
                EXISTING_VALID_METRIC_MEASUREMENT_UPDATED.getLastUpdate());

        int portions = EXISTING_VALID_NINE_PORTIONS.getServings() *
                EXISTING_VALID_NINE_PORTIONS.getSittings();
        // Act
        SUT.start(EXISTING_VALID_METRIC.getId());

        verify(repositoryRecipeIngredient).getById(eq(EXISTING_VALID_METRIC.getId()),
                getRecipeIngredientCallbackCaptor.capture());
        getRecipeIngredientCallbackCaptor.getValue().onEntityLoaded(EXISTING_VALID_METRIC);

        verify(repositoryRecipePortionsMock).getPortionsForRecipe(
                eq(EXISTING_VALID_METRIC.getRecipeId()),
                getRecipePortionsCallbackCaptor.capture());
        getRecipePortionsCallbackCaptor.getValue().onEntityLoaded(EXISTING_VALID_NINE_PORTIONS);

        SUT.measurementTwo.set(String.valueOf(
                (int) EXISTING_VALID_METRIC_MEASUREMENT_UPDATED.getItemBaseUnits() * portions / 1000
        ));
        SUT.measurementOne.set(String.valueOf(
                (int) EXISTING_VALID_METRIC_MEASUREMENT_UPDATED.getItemBaseUnits() * portions % 1000
        ));
        // Assert
        verify(repositoryRecipeIngredient).save(EXISTING_VALID_METRIC_MEASUREMENT_UPDATED);
    }

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
