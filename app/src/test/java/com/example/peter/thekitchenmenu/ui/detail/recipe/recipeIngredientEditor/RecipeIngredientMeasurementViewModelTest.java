package com.example.peter.thekitchenmenu.ui.detail.recipe.recipeIngredientEditor;

import android.content.res.Resources;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;

import com.example.peter.thekitchenmenu.app.Constants;
import com.example.peter.thekitchenmenu.data.entity.RecipeIngredientEntity;
import com.example.peter.thekitchenmenu.data.repository.DataSource;
import com.example.peter.thekitchenmenu.data.repository.RepositoryIngredient;
import com.example.peter.thekitchenmenu.data.repository.RepositoryRecipeIngredient;
import com.example.peter.thekitchenmenu.testdata.IngredientEntityTestData;
import com.example.peter.thekitchenmenu.testdata.ProductEntityTestData;
import com.example.peter.thekitchenmenu.testdata.RecipeEntityTestData;
import com.example.peter.thekitchenmenu.ui.detail.recipe.recipeingredienteditor.RecipeIngredientMeasurementViewModel;
import com.example.peter.thekitchenmenu.utils.ParseIntegerFromObservableHandler;
import com.example.peter.thekitchenmenu.utils.TimeProvider;
import com.example.peter.thekitchenmenu.utils.UniqueIdProvider;
import com.example.peter.thekitchenmenu.utils.unitofmeasure.MeasurementSubtype;

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

public class RecipeIngredientMeasurementViewModelTest {

    // region constants ----------------------------------------------------------------------------
    private RecipeIngredientEntity NEW_INVALID = new RecipeIngredientEntity(
            "newId",
            RecipeEntityTestData.getInvalidNew().getId(),
            IngredientEntityTestData.getNew().getId(),
            ProductEntityTestData.getNewInvalid().getId(),
            0,
            1,
            Constants.getUserId().getValue(),
            10L,
            10L
    );

    private RecipeIngredientEntity NEW_VALID_MEASUREMENT_ONE = new RecipeIngredientEntity(
            "new_valid_measurement_one_id",
            RecipeEntityTestData.getValidNew().getId(),
            IngredientEntityTestData.getNewValidName().getId(),
            "",
            1500,
            1,
            Constants.getUserId().getValue(),
            10L,
            10L
    );
    // endregion constants -------------------------------------------------------------------------

    // region helper fields ------------------------------------------------------------------------
    @Rule
    public InstantTaskExecutorRule instantExecutorRule = new InstantTaskExecutorRule();
    @Mock
    private RepositoryIngredient repositoryIngredientMock;
    @Mock
    private RepositoryRecipeIngredient dataSourceMock;
    @Captor
    ArgumentCaptor<DataSource.GetEntityCallback<RecipeIngredientEntity>>
            getEntityCallbackArgumentCaptor;
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

        SUT = new RecipeIngredientMeasurementViewModel(
                repositoryIngredientMock,
                dataSourceMock,
                resourcesMock
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
        assertEquals(MeasurementSubtype.TYPE_METRIC_MASS, SUT.subtype.get());
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
    public void startNewRecipeAndIngredientId_measurementOneUpdated_saved() {
        // Arrange
        // Act
        SUT.start(NEW_VALID_MEASUREMENT_ONE.getRecipeId(), NEW_VALID_MEASUREMENT_ONE.getIngredientId());
        SUT.measurementOne.set(
                String.valueOf(NEW_VALID_MEASUREMENT_ONE.getBaseUnits()));
        // Assert
        verify(dataSourceMock).save(eq(NEW_VALID_MEASUREMENT_ONE));
    }

    // startNewRecipeAndIngredientId_measurementTwoUpdated_saved
    // startNewRecipeAndIngredientId_measurementTwoUpdatedInvalidValue_notSaved

    // startNewRecipeAndIngredientId_unitOfMeasureImperialMass_noOfUnitsTwo
    // startNewRecipeAndIngredientId_measurementOneUpdated_processedAsDouble

    // startNewRecipeAndIngredientId_unitOfMeasureCount_numberOfUnitsOne
    // startNewRecipeAndIngredientId_measurementOneUpdated_processedAsInteger

    // startExistingRecipeAndIngredientId_existingModelRetrievedFromDatabase
    // startExistingRecipeAndIngredientId_existingModelValuesSetToObservables
    // startExistingRecipeAndIngredientId_unitOneUpdated_saved
    // startExistingRecipeAndIngredientId_unitTwoUpdated_saved
    // startExistingRecipeAndIngredientId_unitTwoUpdatedInvalidValue_errorMessageSet
    // startExistingRecipeAndIngredientId_
}
