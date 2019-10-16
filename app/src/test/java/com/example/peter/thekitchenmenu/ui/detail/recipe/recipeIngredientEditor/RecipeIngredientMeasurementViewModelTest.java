package com.example.peter.thekitchenmenu.ui.detail.recipe.recipeIngredientEditor;

import android.app.Application;
import android.content.res.Resources;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;

import com.example.peter.thekitchenmenu.R;
import com.example.peter.thekitchenmenu.data.entity.IngredientEntity;
import com.example.peter.thekitchenmenu.data.entity.RecipeIngredientQuantityEntity;
import com.example.peter.thekitchenmenu.data.entity.RecipePortionsEntity;
import com.example.peter.thekitchenmenu.data.repository.DataSource;
import com.example.peter.thekitchenmenu.data.repository.RepositoryIngredient;
import com.example.peter.thekitchenmenu.data.repository.RepositoryRecipeIngredient;
import com.example.peter.thekitchenmenu.data.repository.RepositoryRecipePortions;
import com.example.peter.thekitchenmenu.testdata.IngredientEntityTestData;
import com.example.peter.thekitchenmenu.testdata.RecipeIngredientQuantityEntityTestData;
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
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

public class RecipeIngredientMeasurementViewModelTest {

    // region constants ----------------------------------------------------------------------------
    private static final String NUMBER_FORMAT_EXCEPTION_ERROR = "Please enter numbers only.";

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

    // endregion constants -------------------------------------------------------------------------

    // region helper fields ------------------------------------------------------------------------
    @Rule
    public InstantTaskExecutorRule instantExecutorRule = new InstantTaskExecutorRule();
    @Mock
    private RepositoryRecipePortions repositoryRecipePortionsMock;
    @Captor
    ArgumentCaptor<DataSource.GetEntityCallback<RecipePortionsEntity>>
            getRecipePortionsCallbackCaptor;
    @Mock
    private RepositoryRecipeIngredient repositoryRecipeIngredientMock;
    @Captor
    ArgumentCaptor<DataSource.GetEntityCallback<RecipeIngredientQuantityEntity>>
            getRecipeIngredientCallbackCaptor;
    @Mock
    private RepositoryIngredient repositoryIngredientMock;
    @Captor
    ArgumentCaptor<DataSource.GetEntityCallback<IngredientEntity>>
            getIngredientCallbackCaptor;
    @Mock
    private TimeProvider timeProviderMock;
    @Mock
    private Resources resourcesMock;
    @Mock
    private UniqueIdProvider idProviderMock;
    @Mock
    Application applicationMock;
    // endregion helper fields ---------------------------------------------------------------------

    private RecipeIngredientMeasurementViewModel SUT;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        setupResources();

        SUT = new RecipeIngredientMeasurementViewModel(
                mock(Application.class),
                repositoryRecipePortionsMock,
                repositoryRecipeIngredientMock,
                repositoryIngredientMock,
                resourcesMock,
                idProviderMock,
                timeProviderMock
        );
    }

//    @Test
//    public void startNewRecipeAndIngredientId_databaseNotCalled() {
//        // Arrange
//        when(idProviderMock.getUId()).thenReturn(QUANTITY_NEW_INVALID.getId());
//        // Act
//        SUT.start(QUANTITY_NEW_INVALID.getRecipeId(), QUANTITY_NEW_INVALID.getIngredientId());
//        // Assert
//        verifyNoMoreInteractions(repositoryRecipeIngredientMock);
//    }

    @Test
    public void startNewRecipeAndIngredientId_measurementTypeDefaultMetricMass() {
        // Arrange
        when(idProviderMock.getUId()).thenReturn(QUANTITY_NEW_INVALID.getId());
        when(timeProviderMock.getCurrentTimestamp()).thenReturn(QUANTITY_NEW_INVALID.getCreateDate());
        // Act
        SUT.start(QUANTITY_NEW_INVALID.getRecipeId(), QUANTITY_NEW_INVALID.getIngredientId());
        verifyRepoIngredientCalledAndReturnNewValidName();
        verifyRepoPortionsCalledAndReturnNewValidSixteenPortions();

        // Assert
        assertEquals(MeasurementSubtype.METRIC_MASS, SUT.subtype.get());
    }

    @Test
    public void startNewRecipeAndIngredientId_spinnerSetToPositionZero() {
        // Arrange
        when(idProviderMock.getUId()).thenReturn(QUANTITY_NEW_INVALID.getId());
        // Act
        SUT.start(QUANTITY_NEW_INVALID.getRecipeId(), QUANTITY_NEW_INVALID.getIngredientId());
        // Assert
        assertEquals(MeasurementSubtype.METRIC_MASS, SUT.subtype.get());
    }

    @Test
    public void startNewRecipeAndIngredientId_noOfUnitsTwo() {
        // Arrange
        when(idProviderMock.getUId()).thenReturn(QUANTITY_NEW_INVALID.getId());
        // Act
        SUT.start(QUANTITY_NEW_INVALID.getRecipeId(), QUANTITY_NEW_INVALID.getIngredientId());
        verifyRepoIngredientCalledAndReturnNewValidName();
        verifyRepoPortionsCalledAndReturnNewValidSixteenPortions();
        // Assert
        assertEquals(2, SUT.numberOfMeasurementUnits.get());
    }

    @Test
    public void startNewRecipeAndIngredientId_validMeasurementOneUpdated_saved() {
        // Arrange
        whenIdProviderReturnNewValidId();
        whenTimeProviderThenReturnNewValidTime();

        int totalBaseUnits = (int) QUANTITY_NEW_VALID_METRIC.getItemBaseUnits() *
                PORTIONS_NEW_VALID_FOUR.getServings() *
                PORTIONS_NEW_VALID_FOUR.getSittings();
        // Act
        SUT.start(QUANTITY_NEW_VALID_METRIC.getRecipeId(), QUANTITY_NEW_VALID_METRIC.getIngredientId());
        verifyRepoIngredientCalledAndReturnNewValidName();
        verifyRepoPortionsCalledAndReturnNewValidFourPortions();

        SUT.measurementOne.set(String.valueOf(totalBaseUnits));
        // Assert
        verify(repositoryRecipeIngredientMock).save(eq(QUANTITY_NEW_VALID_METRIC));
    }

    @Test
    public void startNewRecipeAndIngredientId_measurementTwoUpdated_saved() {
        // Arrange
        whenIdProviderReturnNewValidId();
        whenTimeProviderThenReturnNewValidTime();

        int totalBaseUnits = (int) QUANTITY_NEW_VALID_METRIC.getItemBaseUnits() *
                PORTIONS_NEW_VALID_SIXTEEN.getServings() *
                PORTIONS_NEW_VALID_SIXTEEN.getSittings();

        // Act
        SUT.start(QUANTITY_NEW_VALID_METRIC.getRecipeId(), QUANTITY_NEW_VALID_METRIC.getIngredientId());
        verifyRepoIngredientCalledAndReturnNewValidName();
        verifyRepoPortionsCalledAndReturnNewValidSixteenPortions();

        SUT.measurementOne.set(String.valueOf(totalBaseUnits % 1000));
        SUT.measurementTwo.set(String.valueOf(totalBaseUnits / 1000));
        // Assert
        verify(repositoryRecipeIngredientMock).save(eq(QUANTITY_NEW_VALID_METRIC));
    }

    @Test
    public void startNewRecipeAndIngredientId_measurementTwoUpdatedInvalidValue_notSaved() {
        // Arrange
        whenIdProviderReturnNewValidId();
        whenTimeProviderThenReturnNewValidTime();
        // Act
        SUT.start(QUANTITY_NEW_VALID_METRIC.getRecipeId(), QUANTITY_NEW_VALID_METRIC.getIngredientId());

        SUT.measurementTwo.set(String.valueOf(UnitOfMeasureConstants.MAXIMUM_MASS / 1000 + 1));
        // Assert
        verifyNoMoreInteractions(repositoryRecipeIngredientMock);
    }

    @Test
    public void startNewRecipeAndIngredientId_measurementTwoUpdatedInvalidValue_originalValueRestored() {
        // Arrange
        whenIdProviderReturnNewValidId();
        whenTimeProviderThenReturnNewValidTime();
        // Act
        SUT.start(QUANTITY_NEW_VALID_METRIC.getRecipeId(), QUANTITY_NEW_VALID_METRIC.getIngredientId());
        verifyRepoIngredientCalledAndReturnNewValidName();
        verifyRepoPortionsCalledAndReturnNewValidFourPortions();
        SUT.measurementTwo.set(String.valueOf((int) UnitOfMeasureConstants.MAXIMUM_MASS / 1000 + 1));
        // Assert
        assertEquals("0", SUT.measurementOne.get());
        assertEquals("0", SUT.measurementTwo.get());
    }

    @Test
    public void startNewRecipeAndIngredientId_unitOfMeasureImperialMass_decimalMeasurementProcessed() {
        // Arrange
        when(timeProviderMock.getCurrentTimestamp()).thenReturn(QUANTITY_NEW_VALID_IMPERIAL.getCreateDate());
        when(idProviderMock.getUId()).thenReturn(QUANTITY_NEW_VALID_IMPERIAL.getId());
        // Act
        SUT.start(QUANTITY_NEW_VALID_IMPERIAL.getRecipeId(), QUANTITY_NEW_VALID_IMPERIAL.getIngredientId());
        verifyRepoIngredientCalledAndReturnNewValidName();
        verifyRepoPortionsCalledAndReturnNewValidFourPortions();
        SUT.subtype.set(MeasurementSubtype.IMPERIAL_MASS);
        SUT.measurementOne.set(String.valueOf(NEW_VALID_IMPERIAL_OZ));
        // Assert
        verify(repositoryRecipeIngredientMock).save(eq(QUANTITY_NEW_VALID_IMPERIAL));
    }

    @Test
    public void startNewRecipeAndIngredientId_unitOfMeasureSpoon_decimalMeasurementProcessed() {
        // Arrange
        when(timeProviderMock.getCurrentTimestamp()).thenReturn(
                QUANTITY_NEW_VALID_ONE_TEASPOON_NO_CONVERSION.getCreateDate());
        when(idProviderMock.getUId()).thenReturn(
                QUANTITY_NEW_VALID_ONE_TEASPOON_NO_CONVERSION.getId());

        int portions = PORTIONS_NEW_VALID_FOUR.getServings() * PORTIONS_NEW_VALID_FOUR.getSittings();
        int teaspoonVolume = 5;
        // Act
        SUT.start(QUANTITY_NEW_VALID_ONE_TEASPOON_NO_CONVERSION.getRecipeId(),
                QUANTITY_NEW_VALID_ONE_TEASPOON_NO_CONVERSION.getIngredientId());
        verifyRepoIngredientCalledAndReturnNewValidName();
        verifyRepoPortionsCalledAndReturnNewValidFourPortions();

        SUT.subtype.set(MeasurementSubtype.IMPERIAL_SPOON);
        SUT.measurementOne.set(String.valueOf(QUANTITY_NEW_VALID_ONE_TEASPOON_NO_CONVERSION.
                getItemBaseUnits() * portions / teaspoonVolume));
        // Assert
        verify(repositoryRecipeIngredientMock).save(eq(QUANTITY_NEW_VALID_ONE_TEASPOON_NO_CONVERSION));
    }

    @Test
    public void startNewRecipeAndIngredientId_unitOfMeasureCount_numberOfUnitsOne() {
        // Arrange
        when(idProviderMock.getUId()).thenReturn(QUANTITY_NEW_INVALID.getId());
        // Act
        SUT.start(QUANTITY_NEW_INVALID.getRecipeId(), QUANTITY_NEW_INVALID.getIngredientId());
        verifyRepoIngredientCalledAndReturnNewValidName();
        verifyRepoPortionsCalledAndReturnNewValidFourPortions();

        SUT.subtype.set(MeasurementSubtype.COUNT);
        // Assert
        assertEquals(1, SUT.numberOfMeasurementUnits.get());
    }

    @Test
    public void startNewRecipeAndIngredientId_unitOfMeasureSpoon_validMeasurementSavedWithoutConversionFactor() {
        // Arrange
        when(idProviderMock.getUId()).thenReturn(QUANTITY_NEW_INVALID.getId());
        when(timeProviderMock.getCurrentTimestamp()).thenReturn(
                QUANTITY_NEW_VALID_ONE_TEASPOON_NO_CONVERSION.getLastUpdate());
        // Act
        SUT.start(QUANTITY_NEW_INVALID.getRecipeId(), QUANTITY_NEW_INVALID.getIngredientId());
        // Assert
        verifyRepoIngredientCalledAndReturnNewValidName();
        verifyRepoPortionsCalledAndReturnNewValidFourPortions();
        SUT.subtype.set(MeasurementSubtype.IMPERIAL_SPOON);
        SUT.measurementOne.set("1");
        verifyNoMoreInteractions(repositoryIngredientMock);
        verify(repositoryRecipeIngredientMock).save(eq(QUANTITY_NEW_VALID_ONE_TEASPOON_NO_CONVERSION));
    }

    @Test
    public void startNewRecipeAndIngredientId_unitOfMeasureSpoon_conversionFactorValidSaved() {
        // Arrange
        when(idProviderMock.getUId()).thenReturn(QUANTITY_NEW_INVALID.getId());
        when(timeProviderMock.getCurrentTimestamp()).thenReturn(
                INGREDIENT_NEW_VALID_CONVERSION_FACTOR_UPDATED.getLastUpdate());
        // Act
        SUT.start(QUANTITY_NEW_INVALID.getRecipeId(), QUANTITY_NEW_INVALID.getIngredientId());
        // Assert
        verifyRepoIngredientCalledAndReturnNewValidNameValidDescription();
        verifyRepoPortionsCalledAndReturnNewValidFourPortions();
        SUT.subtype.set(MeasurementSubtype.IMPERIAL_SPOON);
        SUT.measurementOne.set("1");
        SUT.conversionFactor.set(String.valueOf(
                INGREDIENT_NEW_VALID_CONVERSION_FACTOR_UPDATED.getConversionFactor()));
        verify(repositoryIngredientMock).save(eq(INGREDIENT_NEW_VALID_CONVERSION_FACTOR_UPDATED));
    }

    @Test
    public void startNewRecipeAndIngredientId_unitOfMeasureSpoon_validMeasurementSavedWithConversionFactorApplied() {
        // Arrange
        when(idProviderMock.getUId()).thenReturn(QUANTITY_NEW_INVALID.getId());
        when(timeProviderMock.getCurrentTimestamp()).thenReturn(
                QUANTITY_NEW_VALID_ONE_TEASPOON_CONVERSION_APPLIED.getLastUpdate());
        // Act
        SUT.start(QUANTITY_NEW_INVALID.getRecipeId(), QUANTITY_NEW_INVALID.getIngredientId());
        // Assert
        verifyRepoIngredientCalledAndReturnNewValidNameValidDescription();
        verifyRepoPortionsCalledAndReturnNewValidFourPortions();
        SUT.subtype.set(MeasurementSubtype.IMPERIAL_SPOON);
        SUT.measurementOne.set("1");
        SUT.conversionFactor.set(String.valueOf(
                INGREDIENT_NEW_VALID_CONVERSION_FACTOR_UPDATED.getConversionFactor()));
        verify(repositoryRecipeIngredientMock).save(eq(QUANTITY_NEW_VALID_ONE_TEASPOON_CONVERSION_APPLIED));
    }

    @Test
    public void startExistingRecipeIngredientId_existingModelRetrievedFromDatabase() {
        // Arrange
        // Act
        SUT.start(QUANTITY_EXISTING_VALID_METRIC.getId());
        // Assert
        verifyRepoRecipeIngredientCalledAndReturnExistingValidMetric();
        verifyRepoIngredientCalledAndReturnExistingValidNameDescription();
        verifyRepoPortionsCalledAndReturnExistingValidNinePortions();
    }

    @Test
    public void startExistingRecipeIngredientId_existingModelValuesSetToObservables() {
        // Arrange
        UnitOfMeasure unitOfMeasure = MeasurementSubtype.fromInt(
                QUANTITY_EXISTING_VALID_METRIC.getUnitOfMeasureSubtype()).getMeasurementClass();

        int portions = PORTIONS_EXISTING_VALID_NINE.getServings() *
                PORTIONS_EXISTING_VALID_NINE.getSittings();
        // Act
        SUT.start(QUANTITY_EXISTING_VALID_METRIC.getId());
        // Assert
        verifyRepoRecipeIngredientCalledAndReturnExistingValidMetric();
        verifyRepoIngredientCalledAndReturnExistingValidNameDescription();
        verifyRepoPortionsCalledAndReturnExistingValidNinePortions();

        assertEquals(QUANTITY_EXISTING_VALID_METRIC.getUnitOfMeasureSubtype(), SUT.subtype.get().asInt());
        assertEquals(unitOfMeasure.getNumberOfMeasurementUnits(), SUT.numberOfMeasurementUnits.get());

        assertEquals(
                String.valueOf((int) QUANTITY_EXISTING_VALID_METRIC.getItemBaseUnits() * portions / 1000),
                SUT.measurementTwo.get());
        assertEquals(
                String.valueOf((int) QUANTITY_EXISTING_VALID_METRIC.getItemBaseUnits() * portions % 1000),
                SUT.measurementOne.get());
    }

    @Test
    public void startExistingRecipeIngredientId_measurementUpdated_saved() {
        // Arrange
        when(timeProviderMock.getCurrentTimestamp()).thenReturn(
                QUANTITY_EXISTING_VALID_METRIC_MEASUREMENT_UPDATED.getLastUpdate());

        int portions = PORTIONS_EXISTING_VALID_NINE.getServings() *
                PORTIONS_EXISTING_VALID_NINE.getSittings();
        // Act
        SUT.start(QUANTITY_EXISTING_VALID_METRIC.getId());
        verifyRepoRecipeIngredientCalledAndReturnExistingValidMetric();
        verifyRepoIngredientCalledAndReturnExistingValidNameDescription();
        verifyRepoPortionsCalledAndReturnExistingValidNinePortions();

        SUT.measurementTwo.set(String.valueOf(
                (int) QUANTITY_EXISTING_VALID_METRIC_MEASUREMENT_UPDATED.getItemBaseUnits() * portions / 1000
        ));
        SUT.measurementOne.set(String.valueOf(
                (int) QUANTITY_EXISTING_VALID_METRIC_MEASUREMENT_UPDATED.getItemBaseUnits() * portions % 1000
        ));
        // Assert
        verify(repositoryRecipeIngredientMock).save(QUANTITY_EXISTING_VALID_METRIC_MEASUREMENT_UPDATED);
    }

    // isConversionFactorEnabled
    // conversionFactorDisabled
    // Done button shown

    // startExistingRecipeIngredientId_ingredientEditorIsNotOwner_conversionFactorNotEnabled
    // startExistingRecipeIngredientId_ingredientEditorIsOwner_conversionFactorEnabled
    //

    // region helper methods -----------------------------------------------------------------------
    private void setupResources() {
        when(resourcesMock.getString(eq(R.string.number_format_exception))).
                thenReturn(NUMBER_FORMAT_EXCEPTION_ERROR);
    }

    private void verifyRepoPortionsCalledAndReturnNewValidSixteenPortions() {
        verify(repositoryRecipePortionsMock).getPortionsForRecipe(
                eq(QUANTITY_NEW_VALID_METRIC.getRecipeId()),
                getRecipePortionsCallbackCaptor.capture());
        getRecipePortionsCallbackCaptor.getValue().onEntityLoaded(PORTIONS_NEW_VALID_SIXTEEN);
    }

    private void verifyRepoPortionsCalledAndReturnNewValidFourPortions() {
        verify(repositoryRecipePortionsMock).getPortionsForRecipe(eq(QUANTITY_NEW_VALID_METRIC.getRecipeId()),
                getRecipePortionsCallbackCaptor.capture());
        getRecipePortionsCallbackCaptor.getValue().onEntityLoaded(PORTIONS_NEW_VALID_FOUR);
    }

    private void verifyRepoPortionsCalledAndReturnExistingValidNinePortions() {
        verify(repositoryRecipePortionsMock).getPortionsForRecipe(
                eq(QUANTITY_EXISTING_VALID_METRIC.getRecipeId()),
                getRecipePortionsCallbackCaptor.capture());
        getRecipePortionsCallbackCaptor.getValue().onEntityLoaded(PORTIONS_EXISTING_VALID_NINE);
    }

    private void verifyRepoIngredientCalledAndReturnExistingValidNameDescription() {
        verify(repositoryIngredientMock).getById(
                eq(INGREDIENT_EXISTING_VALID_NAME_DESCRIPTION.getId()),
                getIngredientCallbackCaptor.capture());
        getIngredientCallbackCaptor.getValue().onEntityLoaded(
                INGREDIENT_EXISTING_VALID_NAME_DESCRIPTION);
    }

    private void verifyRepoIngredientCalledAndReturnNewValidNameValidDescription() {
        verify(repositoryIngredientMock).getById(
                eq(INGREDIENT_NEW_VALID_NAME_DESCRIPTION.getId()),
                getIngredientCallbackCaptor.capture());
        getIngredientCallbackCaptor.getValue().onEntityLoaded(
                INGREDIENT_NEW_VALID_NAME_DESCRIPTION);
    }

    private void verifyRepoIngredientCalledAndReturnNewValidName() {
        verify(repositoryIngredientMock).getById(
                eq(INGREDIENT_NEW_VALID_NAME.getId()),
                getIngredientCallbackCaptor.capture());
        getIngredientCallbackCaptor.getValue().onEntityLoaded(INGREDIENT_NEW_VALID_NAME);
    }

    private void verifyRepoRecipeIngredientCalledAndReturnExistingValidMetric() {
        verify(repositoryRecipeIngredientMock).getById(eq(QUANTITY_EXISTING_VALID_METRIC.getId()),
                getRecipeIngredientCallbackCaptor.capture());
        getRecipeIngredientCallbackCaptor.getValue().onEntityLoaded(QUANTITY_EXISTING_VALID_METRIC);
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
