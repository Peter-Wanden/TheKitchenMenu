package com.example.peter.thekitchenmenu.ui.detail.recipe.recipeeditor;

import android.content.res.Resources;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.lifecycle.Observer;

import com.example.peter.thekitchenmenu.R;
import com.example.peter.thekitchenmenu.data.entity.RecipeEntity;
import com.example.peter.thekitchenmenu.data.model.RecipeIdentityModel;
import com.example.peter.thekitchenmenu.utils.ParseIntegerFromObservable;
import com.example.peter.thekitchenmenu.utils.TextValidationHandler;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.List;

import static com.example.peter.thekitchenmenu.testdata.RecipeTestData.*;
import static org.junit.Assert.*;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.anyObject;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class RecipeIdentityViewModelTest {

    // region constants ----------------------------------------------------------------------------
    private static final RecipeEntity VALID_EXISTING_RECIPE_ENTITY = getValidExistingRecipeEntity();
    private static final RecipeIdentityModelMetaData VALID_EXISTING_IDENTITY_MODEL_METADATA =
            getValidExistingRecipeIdentityModelMetadataData();
    private static final RecipeIdentityModel VALID_EXISTING_IDENTITY_MODEL =
            getValidExistingRecipeIdentityModelData();

    private static final String VALID_EXISTING_TITLE = VALID_EXISTING_RECIPE_ENTITY.getTitle();
    private static final String VALID_EXISTING_DESCRIPTION = VALID_EXISTING_RECIPE_ENTITY.getDescription();
    private static final int VALID_EXISTING_PREP_TIME = VALID_EXISTING_RECIPE_ENTITY.getPreparationTime();
    private static final int VALID_EXISTING_COOK_TIME = VALID_EXISTING_RECIPE_ENTITY.getCookingTime();

    private static final String VALIDATED = TextValidationHandler.VALIDATED;
    private static final RecipeEntity EMPTY_RECIPE_ENTITY = getEmptyRecipeEntity();

    private static final String INVALID_TITLE = "ti";
    private static final String INVALID_DESCRIPTION = "de";
    private static final String ERROR_MESSAGE_TOO_LONG = "error_message_too_long";
    private static final String ERROR_MESSAGE_TOO_SHORT = "error_message_too_short";

    private static final int MAX_PREP_TIME = getMaxPrepTime();
    private static final int MAX_COOK_TIME = getMaxCookTime();
    private static final String LONG_TEXT_VALIDATION_ERROR = "long_text_validation_error";
    private static final String SHORT_TEXT_VALIDATION_ERROR = "short_text_validation_error";

    // endregion constants -------------------------------------------------------------------------

    // region helper fields ------------------------------------------------------------------------
    @Rule
    public InstantTaskExecutorRule instantExecutorRule = new InstantTaskExecutorRule();
    @Mock
    Resources resourcesMock;
    @Mock
    TextValidationHandler textValidationHandlerMock;
    @Mock
    Observer<RecipeIdentityModelMetaData> identityModelMetaDataObserverMock;
    @Mock
    ParseIntegerFromObservable intFromObservableMock;
    // endregion helper fields ---------------------------------------------------------------------

    private RecipeIdentityViewModel SUT;

    @Before
    public void setup() throws Exception {
        MockitoAnnotations.initMocks(this);
        setupResourceMockReturnValues();

        SUT = new RecipeIdentityViewModel(
                resourcesMock,
                textValidationHandlerMock,
                intFromObservableMock);

        SUT.getRecipeIdentityModelMetaData().observeForever(identityModelMetaDataObserverMock);
    }

    @Test
    public void onStart_emptyRecipeSupplied_identityMetadataNotEmitted() throws Exception {
        // Arrange
        // Act
        SUT.onStart(EMPTY_RECIPE_ENTITY);
        // Assert
        verifyNoMoreInteractions(identityModelMetaDataObserverMock);
    }

    @Test
    public void observableTitleUpdatedWithInvalidValue_emptyRecipeSupplied_identityMetaDataEmittedModelChangedInvalidModel() {
        // Arrange
        ArgumentCaptor<RecipeIdentityModelMetaData> ac =
                ArgumentCaptor.forClass(RecipeIdentityModelMetaData.class);
        whenShortTextValidationReturnErrorMessage();
        // Act
        SUT.onStart(EMPTY_RECIPE_ENTITY);
        SUT.titleObservable.set(INVALID_TITLE);
        // Assert
        verify(identityModelMetaDataObserverMock).onChanged(ac.capture());
        RecipeIdentityModelMetaData identityModelMetaData = ac.getValue();

        assertTrue(identityModelMetaData.isModelChanged());
        assertFalse(identityModelMetaData.isValidModel());

        RecipeIdentityModel identityModel = identityModelMetaData.getIdentityModel();
        assertEquals(INVALID_TITLE, identityModel.getTitle());
        assertEquals(EMPTY_RECIPE_ENTITY.getDescription(), identityModel.getDescription());
        assertEquals(EMPTY_RECIPE_ENTITY.getCookingTime(), identityModel.getCookTime());
        assertEquals(EMPTY_RECIPE_ENTITY.getPreparationTime(), identityModel.getPrepTime());

        assertEquals(SHORT_TEXT_VALIDATION_ERROR, SUT.titleErrorMessage.get());
    }

    @Test
    public void observableTitleUpdatedWithValidValue_emptyRecipeSupplied_identityMetaDataEmittedModelChangedInvalidModel() throws Exception {
        // Arrange
        ArgumentCaptor<RecipeIdentityModelMetaData> ac =
                ArgumentCaptor.forClass(RecipeIdentityModelMetaData.class);
        whenShortTextValidationReturnValidated();
        // Act
        SUT.onStart(EMPTY_RECIPE_ENTITY);
        SUT.titleObservable.set(VALID_EXISTING_TITLE);
        // Assert
        verify(identityModelMetaDataObserverMock).onChanged(ac.capture());

        RecipeIdentityModelMetaData identityModelMetaData = ac.getValue();
        assertTrue(identityModelMetaData.isModelChanged());
        assertFalse(identityModelMetaData.isValidModel());

        RecipeIdentityModel identityModel = identityModelMetaData.getIdentityModel();
        assertEquals(VALID_EXISTING_TITLE, identityModel.getTitle());
        assertEquals(EMPTY_RECIPE_ENTITY.getDescription(), identityModel.getDescription());
        assertEquals(EMPTY_RECIPE_ENTITY.getCookingTime(), identityModel.getCookTime());
        assertEquals(EMPTY_RECIPE_ENTITY.getPreparationTime(), identityModel.getPrepTime());

        assertNull(SUT.titleErrorMessage.get());
    }

    @Test
    public void observableDescriptionWithValidValue_emptyRecipeSupplied_identityMetaDataEmittedModelChangedInvalidModel() throws Exception {
        // Arrange
        ArgumentCaptor<RecipeIdentityModelMetaData> ac =
                ArgumentCaptor.forClass(RecipeIdentityModelMetaData.class);
        whenLongTextValidationReturnValidated();
        // Act
        SUT.onStart(EMPTY_RECIPE_ENTITY);
        SUT.descriptionObservable.set(VALID_EXISTING_DESCRIPTION);
        // Assert
        verify(identityModelMetaDataObserverMock).onChanged(ac.capture());

        RecipeIdentityModelMetaData identityModelMetaData = ac.getValue();
        assertTrue(identityModelMetaData.isModelChanged());
        assertFalse(identityModelMetaData.isValidModel());

        RecipeIdentityModel identityModel = identityModelMetaData.getIdentityModel();
        assertEquals(EMPTY_RECIPE_ENTITY.getTitle(), identityModel.getTitle());
        assertEquals(VALID_EXISTING_DESCRIPTION, identityModel.getDescription());
        assertEquals(EMPTY_RECIPE_ENTITY.getPreparationTime(), identityModel.getPrepTime());
        assertEquals(EMPTY_RECIPE_ENTITY.getCookingTime(), identityModel.getCookTime());

        assertNull(SUT.titleErrorMessage.get());
    }

    @Test
    public void observableDescriptionWithInvalidValue_emptyRecipeSupplied_identityMetaDataEmittedModelChangedInvalidModel() throws Exception {
        // Arrange
        ArgumentCaptor<RecipeIdentityModelMetaData> ac =
                ArgumentCaptor.forClass(RecipeIdentityModelMetaData.class);
        whenLongTextValidationReturnErrorMessage();
        // Act
        SUT.onStart(EMPTY_RECIPE_ENTITY);
        SUT.descriptionObservable.set(INVALID_DESCRIPTION);
        // Assert
        verify(identityModelMetaDataObserverMock).onChanged(ac.capture());

        RecipeIdentityModelMetaData identityModelMetaData = ac.getValue();
        assertTrue(identityModelMetaData.isModelChanged());
        assertFalse(identityModelMetaData.isValidModel());

        RecipeIdentityModel identityModel = identityModelMetaData.getIdentityModel();
        assertEquals(EMPTY_RECIPE_ENTITY.getTitle(), identityModel.getTitle());
        assertEquals(INVALID_DESCRIPTION, identityModel.getDescription());
        assertEquals(EMPTY_RECIPE_ENTITY.getPreparationTime(), identityModel.getPrepTime());
        assertEquals(EMPTY_RECIPE_ENTITY.getCookingTime(), identityModel.getCookTime());

        assertEquals(LONG_TEXT_VALIDATION_ERROR, SUT.descriptionErrorMessage.get());
    }

    @Test
    public void observablePrepMinutesWithValidValue_emptyRecipeSupplied_identityMetaDataEmittedModelChangedInvalidModel() throws Exception {
        ArgumentCaptor<RecipeIdentityModelMetaData> ac =
                ArgumentCaptor.forClass(RecipeIdentityModelMetaData.class);
        when(intFromObservableMock.parseInt(anyObject(), anyObject(), anyInt())).
                thenReturn(
                        EMPTY_RECIPE_ENTITY.getPreparationTime() / 60,
                        EMPTY_RECIPE_ENTITY.getPreparationTime() % 60,
                        EMPTY_RECIPE_ENTITY.getCookingTime() / 60,
                        EMPTY_RECIPE_ENTITY.getCookingTime() % 60,
                        VALID_EXISTING_PREP_TIME % 60);
        // Act
        SUT.onStart(EMPTY_RECIPE_ENTITY);
        SUT.prepMinutesObservable.set(String.valueOf(VALID_EXISTING_PREP_TIME % 60));
        // Assert
        verify(identityModelMetaDataObserverMock).onChanged(ac.capture());

        RecipeIdentityModelMetaData identityModelMetaData = ac.getValue();
        assertTrue(identityModelMetaData.isModelChanged());
        assertFalse(identityModelMetaData.isValidModel());

        RecipeIdentityModel identityModel = identityModelMetaData.getIdentityModel();
        assertEquals(EMPTY_RECIPE_ENTITY.getTitle(), identityModel.getTitle());
        assertEquals(EMPTY_RECIPE_ENTITY.getDescription(), identityModel.getDescription());
        assertEquals(VALID_EXISTING_PREP_TIME % 60, identityModel.getPrepTime());
        assertEquals(EMPTY_RECIPE_ENTITY.getCookingTime(), identityModel.getCookTime());

        assertNull(SUT.prepTimeErrorMessage.get());
    }

    @Test
    public void observableCookHoursWithValidValue_emptyRecipeSupplied_identityMetaDataEmittedModelChangedInvalidModel() throws Exception {
        ArgumentCaptor<RecipeIdentityModelMetaData> ac =
                ArgumentCaptor.forClass(RecipeIdentityModelMetaData.class);
        when(intFromObservableMock.parseInt(anyObject(), anyObject(), anyInt())).
                thenReturn(EMPTY_RECIPE_ENTITY.getPreparationTime() / 60,
                        EMPTY_RECIPE_ENTITY.getPreparationTime() % 60,
                        EMPTY_RECIPE_ENTITY.getCookingTime() / 60,
                        EMPTY_RECIPE_ENTITY.getCookingTime() % 60,
                        VALID_EXISTING_COOK_TIME / 60);
        // Act
        SUT.onStart(EMPTY_RECIPE_ENTITY);
        SUT.cookHoursObservable.set(String.valueOf(VALID_EXISTING_COOK_TIME / 60));
        // Assert
        verify(identityModelMetaDataObserverMock).onChanged(ac.capture());

        RecipeIdentityModelMetaData identityModelMetaData = ac.getValue();
        assertTrue(identityModelMetaData.isModelChanged());
        assertFalse(identityModelMetaData.isValidModel());

        RecipeIdentityModel identityModel = identityModelMetaData.getIdentityModel();
        assertEquals(EMPTY_RECIPE_ENTITY.getTitle(), identityModel.getTitle());
        assertEquals(EMPTY_RECIPE_ENTITY.getDescription(), identityModel.getDescription());
        assertEquals(EMPTY_RECIPE_ENTITY.getPreparationTime(), identityModel.getPrepTime());
        assertEquals(VALID_EXISTING_COOK_TIME / 60 * 60, identityModel.getCookTime());

        assertNull(SUT.cookTimeErrorMessage.get());
    }

    @Test
    public void observableCookMinutesWithValidValue_emptyRecipeSupplied_identityMetaDataEmittedModelChangedInvalidModel() throws Exception {
        ArgumentCaptor<RecipeIdentityModelMetaData> ac =
                ArgumentCaptor.forClass(RecipeIdentityModelMetaData.class);
        when(intFromObservableMock.parseInt(anyObject(), anyObject(), anyInt())).
                thenReturn(
                        EMPTY_RECIPE_ENTITY.getPreparationTime() / 60,
                        EMPTY_RECIPE_ENTITY.getPreparationTime() % 60,
                        EMPTY_RECIPE_ENTITY.getCookingTime() / 60,
                        EMPTY_RECIPE_ENTITY.getCookingTime() % 60,
                        VALID_EXISTING_COOK_TIME % 60);
        // Act
        SUT.onStart(EMPTY_RECIPE_ENTITY);
        SUT.cookMinutesObservable.set(String.valueOf(VALID_EXISTING_COOK_TIME % 60));
        // Assert
        verify(identityModelMetaDataObserverMock).onChanged(ac.capture());

        RecipeIdentityModelMetaData identityModelMetaData = ac.getValue();
        assertTrue(identityModelMetaData.isModelChanged());
        assertFalse(identityModelMetaData.isValidModel());

        RecipeIdentityModel identityModel = identityModelMetaData.getIdentityModel();
        assertEquals(EMPTY_RECIPE_ENTITY.getTitle(), identityModel.getTitle());
        assertEquals(EMPTY_RECIPE_ENTITY.getDescription(), identityModel.getDescription());
        assertEquals(EMPTY_RECIPE_ENTITY.getPreparationTime(), identityModel.getPrepTime());
        assertEquals(VALID_EXISTING_COOK_TIME % 60, identityModel.getCookTime());

        assertNull(SUT.cookTimeErrorMessage.get());
    }

    @Test // Simulates user input for a complete new recipe IdentityModel
    public void allObservablesUpdatedWithValidData_emptyRecipeSupplied_identityMetaDataEmittedModelChangedValidModel() throws Exception {
        // Arrange
        ArgumentCaptor<RecipeIdentityModelMetaData> ac =
                ArgumentCaptor.forClass(RecipeIdentityModelMetaData.class);
        whenShortTextValidationReturnValidated();
        whenLongTextValidationReturnValidated();
        when(intFromObservableMock.parseInt(anyObject(), anyObject(), anyInt())).
                thenReturn(
                        EMPTY_RECIPE_ENTITY.getPreparationTime() / 60,
                        EMPTY_RECIPE_ENTITY.getPreparationTime() % 60,
                        EMPTY_RECIPE_ENTITY.getCookingTime() / 60,
                        EMPTY_RECIPE_ENTITY.getCookingTime() % 60,
                        VALID_EXISTING_PREP_TIME / 60,
                        VALID_EXISTING_PREP_TIME % 60,
                        VALID_EXISTING_COOK_TIME / 60,
                        VALID_EXISTING_COOK_TIME % 60);
        // Act
        SUT.onStart(EMPTY_RECIPE_ENTITY);
        SUT.titleObservable.set(VALID_EXISTING_RECIPE_ENTITY.getTitle());
        SUT.descriptionObservable.set(VALID_EXISTING_RECIPE_ENTITY.getDescription());
        SUT.prepHoursObservable.set(String.valueOf(VALID_EXISTING_PREP_TIME / 60));
        SUT.prepMinutesObservable.set(String.valueOf(VALID_EXISTING_PREP_TIME % 60));
        SUT.cookHoursObservable.set(String.valueOf(VALID_EXISTING_COOK_TIME / 60));
        SUT.cookMinutesObservable.set(String.valueOf(VALID_EXISTING_COOK_TIME % 60));
        // Assert
        verify(identityModelMetaDataObserverMock, times(6)).onChanged(ac.capture());
        List<RecipeIdentityModelMetaData> identityModelMetaDataList = ac.getAllValues();

        RecipeIdentityModelMetaData modelMetaData = identityModelMetaDataList.get(5);
        assertTrue(modelMetaData.isModelChanged());
        assertTrue(modelMetaData.isValidModel());

        RecipeIdentityModel identityModel = modelMetaData.getIdentityModel();
        assertEquals(VALID_EXISTING_IDENTITY_MODEL, identityModel);
    }

    @Test(expected = RuntimeException.class)
    public void onStart_nullRecipe_runtimeExceptionThrown() throws Exception {
        // Arrange
        // Act
        SUT.onStart(null);
        // Assert
        ArrayList list = new ArrayList();
        RuntimeException exception = (RuntimeException) list.get(0);
        assertEquals(exception.getMessage(), "Recipe cannot be null");
    }

    @Test
    public void observableTitle_validModelUpdateTitleWithInvalidValue_identityMetaDataModelChangedInvalidModel() throws Exception {
        // Arrange
        ArgumentCaptor<RecipeIdentityModelMetaData> ac =
                ArgumentCaptor.forClass(RecipeIdentityModelMetaData.class);
        whenShortTextValidationReturnErrorMessage();
        whenLongTextValidationReturnValidated();
        whenIntFromObserver_thenReturn(VALID_EXISTING_RECIPE_ENTITY.getPreparationTime() / 60);
        // Act
        SUT.onStart(VALID_EXISTING_RECIPE_ENTITY);
        SUT.titleObservable.set("");
        // Assert
        verify(identityModelMetaDataObserverMock).onChanged(ac.capture());
        RecipeIdentityModelMetaData modelMetaData = ac.getValue();
        assertFalse(modelMetaData.isValidModel());
        assertTrue(modelMetaData.isModelChanged());
    }

    @Test
    public void observableTitle_updatedTitleWithValidValue_identityMetaDataModelChangedValidModel() throws Exception {
        // Arrange
        ArgumentCaptor<RecipeIdentityModelMetaData> ac =
                ArgumentCaptor.forClass(RecipeIdentityModelMetaData.class);
        when(textValidationHandlerMock.validateShortText(any(), any())).thenReturn(VALIDATED);
        when(textValidationHandlerMock.validateLongText(any(), any())).thenReturn(VALIDATED);
        when(intFromObservableMock.parseInt(anyObject(), anyObject(), anyInt())).thenReturn(
                VALID_EXISTING_PREP_TIME / 60);
        // Act
        SUT.onStart(VALID_EXISTING_RECIPE_ENTITY);
        SUT.titleObservable.set("This is the new valid title");
        // Assert
        verify(identityModelMetaDataObserverMock).onChanged(ac.capture());
        RecipeIdentityModelMetaData modelMetaData = ac.getValue();
        assertTrue(modelMetaData.isValidModel());
        assertTrue(modelMetaData.isModelChanged());
        RecipeIdentityModel identityModel = modelMetaData.getIdentityModel();
        assertEquals("This is the new valid title", identityModel.getTitle());
    }

    @Test
    public void observablePrepTime_invalidPrepTime_invalidModelReturned() throws Exception {
        // Arrange
        ArgumentCaptor<RecipeIdentityModelMetaData> ac =
                ArgumentCaptor.forClass(RecipeIdentityModelMetaData.class);
        whenShortTextValidationReturnValidated();
        whenLongTextValidationReturnValidated();
        when(intFromObservableMock.parseInt(anyObject(), anyObject(), anyInt())).
                thenReturn(
                        VALID_EXISTING_PREP_TIME / 60,
                        VALID_EXISTING_PREP_TIME % 60,
                        VALID_EXISTING_COOK_TIME / 60,
                        VALID_EXISTING_COOK_TIME % 60,
                        MAX_PREP_TIME / 60,
                        MAX_PREP_TIME % 60 + 1);

        // Act
        SUT.onStart(VALID_EXISTING_RECIPE_ENTITY);
        SUT.prepHoursObservable.set(String.valueOf(MAX_PREP_TIME / 60));
        SUT.prepMinutesObservable.set(String.valueOf(MAX_PREP_TIME % 60 + 1));
        // Assert
        verify(identityModelMetaDataObserverMock, times(2)).onChanged(ac.capture());
        List<RecipeIdentityModelMetaData> metaDataList = ac.getAllValues();

        RecipeIdentityModelMetaData modelMetaData = metaDataList.get(1);
        assertFalse(modelMetaData.isValidModel());
        assertTrue(modelMetaData.isModelChanged());

        RecipeIdentityModel identityModel = modelMetaData.getIdentityModel();
        assertEquals(MAX_PREP_TIME + 1, identityModel.getPrepTime());
        assertEquals(ERROR_MESSAGE_TOO_LONG, SUT.prepTimeErrorMessage.get());

    }

    @Test
    public void observableCookTime_invalidCookTime_invalidModelReturned() throws Exception {
        // Arrange
        ArgumentCaptor<RecipeIdentityModelMetaData> ac =
                ArgumentCaptor.forClass(RecipeIdentityModelMetaData.class);
        whenShortTextValidationReturnValidated();
        whenLongTextValidationReturnValidated();
        when(intFromObservableMock.parseInt(anyObject(), anyObject(), anyInt())).
                thenReturn(
                        VALID_EXISTING_PREP_TIME / 60,
                        VALID_EXISTING_PREP_TIME % 60,
                        VALID_EXISTING_COOK_TIME / 60,
                        VALID_EXISTING_COOK_TIME % 60,
                        MAX_COOK_TIME / 60,
                        MAX_COOK_TIME % 60 + 1);
        // Act
        SUT.onStart(VALID_EXISTING_RECIPE_ENTITY);
        SUT.cookHoursObservable.set(String.valueOf(MAX_COOK_TIME / 60));
        SUT.cookMinutesObservable.set(String.valueOf(MAX_COOK_TIME % 60 + 1));
        // Assert
        verify(identityModelMetaDataObserverMock, times(2)).onChanged(ac.capture());
        List<RecipeIdentityModelMetaData> modelMetaDataList = ac.getAllValues();
        RecipeIdentityModelMetaData modelMetaData = modelMetaDataList.get(1);
        assertFalse(modelMetaData.isValidModel());
        assertTrue(modelMetaData.isModelChanged());

        RecipeIdentityModel identityModel = modelMetaData.getIdentityModel();
        assertEquals(MAX_COOK_TIME + 1, identityModel.getCookTime());

        assertEquals(ERROR_MESSAGE_TOO_LONG, SUT.cookTimeErrorMessage.get());
    }

    // region for helper methods -------------------------------------------------------------------
    private void setupResourceMockReturnValues() {
        when(resourcesMock.getInteger(R.integer.recipe_max_cook_time_in_minutes)).thenReturn(MAX_PREP_TIME);
        when(resourcesMock.getInteger(R.integer.recipe_max_prep_time_in_minutes)).thenReturn(MAX_COOK_TIME);
        when(resourcesMock.getString(R.string.input_error_recipe_prep_time_too_long)).thenReturn(ERROR_MESSAGE_TOO_LONG);
        when(resourcesMock.getString(R.string.input_error_recipe_cook_time_too_long)).thenReturn(ERROR_MESSAGE_TOO_LONG);
    }

    private void whenShortTextValidationReturnValidated() {
        when(textValidationHandlerMock.validateShortText(anyObject(), anyString())).
                thenReturn(VALIDATED);
    }

    private void whenShortTextValidationReturnErrorMessage() {
        when(textValidationHandlerMock.validateShortText(anyObject(), anyString())).
                thenReturn(SHORT_TEXT_VALIDATION_ERROR);
    }

    private void whenLongTextValidationReturnValidated() {
        when(textValidationHandlerMock.validateLongText(anyObject(), anyString())).
                thenReturn(VALIDATED);
    }

    private void whenLongTextValidationReturnErrorMessage() {
        when(textValidationHandlerMock.validateLongText(anyObject(), anyString())).
                thenReturn(LONG_TEXT_VALIDATION_ERROR);
    }

    private void whenIntFromObserver_thenReturn(int returnValue) {
        when(intFromObservableMock.parseInt(anyObject(), anyObject(), anyInt())).thenReturn(
                returnValue);
    }
    // endregion helper methods --------------------------------------------------------------------

    // region helper classes -----------------------------------------------------------------------

    // endregion helper classes --------------------------------------------------------------------

}