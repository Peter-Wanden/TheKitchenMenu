package com.example.peter.thekitchenmenu.ui.detail.recipe.recipeeditor;

import android.content.res.Resources;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;

import com.example.peter.thekitchenmenu.R;
import com.example.peter.thekitchenmenu.data.entity.RecipeEntity;
import com.example.peter.thekitchenmenu.data.entity.RecipeIdentityEntity;
import com.example.peter.thekitchenmenu.data.repository.DataSource;
import com.example.peter.thekitchenmenu.data.repository.DataSourceRecipeIdentity;
import com.example.peter.thekitchenmenu.testdata.RecipeIdentityTestData;
import com.example.peter.thekitchenmenu.utils.*;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.List;

import static com.example.peter.thekitchenmenu.testdata.RecipeTestData.*;
import static com.example.peter.thekitchenmenu.testdata.RecipeValidatorTestData.*;
import static org.junit.Assert.*;
import static org.mockito.Matchers.*;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class RecipeIdentityViewModelTest {

    // region constants ----------------------------------------------------------------------------
    private static final RecipeEntity VALID_EXISTING_RECIPE_ENTITY = getValidExistingRecipeEntity();
    private static final String VALID_EXISTING_RECIPE_ID = VALID_EXISTING_RECIPE_ENTITY.getId();
    private static final RecipeIdentityEntity VALID_EXISTING_IDENTITY_ENTITY =
            RecipeIdentityTestData.getValidRecipeIdentityEntity();

    private static final String VALID_EXISTING_IDENTITY_ID = VALID_EXISTING_IDENTITY_ENTITY.getId();
    private static final String VALID_EXISTING_TITLE = VALID_EXISTING_IDENTITY_ENTITY.getTitle();
    private static final String VALID_EXISTING_DESCRIPTION = VALID_EXISTING_IDENTITY_ENTITY.getDescription();
    private static final int VALID_EXISTING_PREP_TIME = VALID_EXISTING_IDENTITY_ENTITY.getPrepTime();
    private static final int VALID_EXISTING_COOK_TIME = VALID_EXISTING_IDENTITY_ENTITY.getCookTime();
    private static final long VALID_EXISTING_CREATE_DATE = VALID_EXISTING_IDENTITY_ENTITY.getCreateDate();
    private static final long VALID_EXISTING_LAST_UPDATE = VALID_EXISTING_IDENTITY_ENTITY.getLastUpdate();

    private static final String VALIDATED = TextValidationHandler.VALIDATED;

    private static final String INVALID_TITLE = "ti";
    private static final String INVALID_DESCRIPTION = "de";
    private static final String ERROR_MESSAGE_TOO_LONG = "error_message_too_long";
    private static final String ERROR_MESSAGE_TOO_SHORT = "error_message_too_short";

    private static final int MAX_PREP_TIME = getMaxPrepTime();
    private static final int MAX_COOK_TIME = getMaxCookTime();
    private static final String LONG_TEXT_VALIDATION_ERROR = "long_text_validation_error";
    private static final String SHORT_TEXT_VALIDATION_ERROR = "short_text_validation_error";

    private RecipeModelStatus IDENTITY_MODEL_UNCHANGED_INVALID = getIdentityModelStatusUnChangedInValid();
    private RecipeModelStatus IDENTITY_MODEL_UNCHANGED_VALID = getIdentityModelStatusUnChangedValid();
    private RecipeModelStatus IDENTITY_MODEL_CHANGED_INVALID = getIdentityModelStatusChangedInvalid();
    private RecipeModelStatus IDENTITY_MODEL_CHANGED_VALID = getIdentityModelStatusChangedValid();

    // endregion constants -------------------------------------------------------------------------

    // region helper fields ------------------------------------------------------------------------
    @Rule
    public InstantTaskExecutorRule instantExecutorRule = new InstantTaskExecutorRule();
    @Mock
    Resources resourcesMock;
    @Mock
    TextValidationHandler textValidationHandlerMock;
    @Mock
    ParseIntegerFromObservableHandler intFromObservableMock;
    @Captor
    ArgumentCaptor<DataSource.GetEntityCallback<RecipeIdentityEntity>> getEntityCallbackArgumentCaptor;
    @Mock
    DataSourceRecipeIdentity identityDataSourceMock;
    @Mock
    TimeProvider timeProviderMock;
    @Mock
    UniqueIdProvider uniqueIdProviderMock;
    @Mock
    RecipeValidation.RecipeValidatorModelSubmission modelSubmissionMock;
    // endregion helper fields ---------------------------------------------------------------------

    private RecipeIdentityViewModel SUT;

    @Before
    public void setup() throws Exception {
        MockitoAnnotations.initMocks(this);
        setupResourceMockReturnValues();
        when(uniqueIdProviderMock.getUId()).thenReturn(VALID_EXISTING_RECIPE_ID);

        SUT = new RecipeIdentityViewModel(
                identityDataSourceMock,
                timeProviderMock,
                uniqueIdProviderMock,
                resourcesMock,
                textValidationHandlerMock,
                intFromObservableMock);

        SUT.setModelSubmitter(modelSubmissionMock);
    }

    // onStart_noRecipeId_newRecipeIdSetToLiveData
    // onStart_noRecipeId_newEmptyRecipeSavedToDatabaseAsDraft

    @Test
    public void onStart_recipeId_databaseCalledWithRecipeId() throws Exception {
        // Arrange
        // Act
        SUT.onStart(VALID_EXISTING_RECIPE_ID);
        // Assert
        verify(identityDataSourceMock).getByRecipeId(eq(VALID_EXISTING_RECIPE_ID),
                getEntityCallbackArgumentCaptor.capture());
    }

    @Test
    public void onStart_recipeIdWithNoRecipe_nothingSetToObservers() throws Exception {
        // Arrange
        // Act
        SUT.onStart(VALID_EXISTING_RECIPE_ID);
        simulateNothingReturnedFromDatabase();
        // Assert
        assertEquals("", SUT.titleObservable.get());
        assertEquals("", SUT.descriptionObservable.get());
        assertEquals("", SUT.prepHoursObservable.get());
        assertEquals("", SUT.prepMinutesObservable.get());
        assertEquals("", SUT.cookHoursObservable.get());
        assertEquals("", SUT.cookMinutesObservable.get());
        assertNull(SUT.titleErrorMessage.get());
        assertNull(SUT.descriptionErrorMessage.get());
        assertNull(SUT.prepTimeErrorMessage.get());
        assertNull(SUT.cookTimeErrorMessage.get());
    }

    @Test
    public void onStart_recipeIdWithNoRecipe_RecipeModelStatus_IDENTITY_MODEL_UNCHANGED_INVALID() throws Exception {
        // Arrange
        ArgumentCaptor<RecipeModelStatus> ac = ArgumentCaptor.forClass(RecipeModelStatus.class);
        // Act
        SUT.onStart(VALID_EXISTING_RECIPE_ID);
        simulateNothingReturnedFromDatabase();
        // Assert
        verify(modelSubmissionMock).submitModelStatus(ac.capture());
        RecipeModelStatus modelStatus = ac.getValue();
        assertEquals(IDENTITY_MODEL_UNCHANGED_INVALID, modelStatus);
    }

    @Test
    public void onStart_validRecipeId_titleSetToObservable() throws Exception {
        // Arrange
        whenTextValidationValidateTitleAndDescription();
        // Act
        SUT.onStart(VALID_EXISTING_RECIPE_ID);
        simulateGetValidRecipeIdentityEntityFromDatabase();
        // Assert
        assertEquals(VALID_EXISTING_TITLE, SUT.titleObservable.get());
    }

    @Test
    public void onStart_validRecipeId_descriptionSetToObservable() throws Exception {
        // Arrange
        whenTextValidationValidateTitleAndDescription();
        // Act
        SUT.onStart(VALID_EXISTING_RECIPE_ID);
        simulateGetValidRecipeIdentityEntityFromDatabase();
        // Assert
        assertEquals(VALID_EXISTING_DESCRIPTION, SUT.descriptionObservable.get());
    }

    @Test
    public void onStart_validRecipeId_prepHoursObservableValueSet() throws Exception {
        // Arrange
        whenTextValidationValidateTitleAndDescription();
        String prepHours = String.valueOf(VALID_EXISTING_PREP_TIME / 60);
        // Act
        SUT.onStart(VALID_EXISTING_RECIPE_ID);
        simulateGetValidRecipeIdentityEntityFromDatabase();
        // Assert
        assertEquals(prepHours, SUT.prepHoursObservable.get());
    }

    @Test
    public void onStart_validRecipeId_prepMinutesObservableValueSet() throws Exception {
        // Arrange
        whenTextValidationValidateTitleAndDescription();
        String prepMinutes = String.valueOf(VALID_EXISTING_PREP_TIME % 60);
        // Act
        SUT.onStart(VALID_EXISTING_RECIPE_ID);
        simulateGetValidRecipeIdentityEntityFromDatabase();
        // Assert
        assertEquals(prepMinutes, SUT.prepMinutesObservable.get());
    }

    @Test
    public void onStart_validRecipeId_cookHoursObservableValueSet() throws Exception {
        // Arrange
        whenTextValidationValidateTitleAndDescription();
        String cookHours = String.valueOf(VALID_EXISTING_COOK_TIME / 60);
        // Act
        SUT.onStart(VALID_EXISTING_RECIPE_ID);
        simulateGetValidRecipeIdentityEntityFromDatabase();
        // Assert
        assertEquals(cookHours, SUT.cookHoursObservable.get());
    }

    @Test
    public void onStart_validRecipeId_cookMinutesObservableValueSet() throws Exception {
        // Arrange
        whenTextValidationValidateTitleAndDescription();
        String cookMinutes = String.valueOf(VALID_EXISTING_COOK_TIME % 60);
        // Act
        SUT.onStart(VALID_EXISTING_RECIPE_ID);
        simulateGetValidRecipeIdentityEntityFromDatabase();
        // Assert
        assertEquals(cookMinutes, SUT.cookMinutesObservable.get());
    }

    @Test
    public void onStart_validRecipeId_RecipeModelStatus_IDENTITY_MODEL_UNCHANGED_VALID() throws Exception {
        // Arrange
        ArgumentCaptor<RecipeModelStatus> ac = ArgumentCaptor.forClass(RecipeModelStatus.class);
        whenTextValidationValidateTitleAndDescription();
        whenIntFromObserverMockReturnValidRecipeTimes();
        // Act
        SUT.onStart(VALID_EXISTING_RECIPE_ID);
        // Assert
        simulateGetValidRecipeIdentityEntityFromDatabase();
        verify(modelSubmissionMock).submitModelStatus(ac.capture());
        RecipeModelStatus modelStatus = ac.getValue();
        assertEquals(IDENTITY_MODEL_UNCHANGED_VALID, modelStatus);
    }

    @Test
    public void observableTitleUpdatedWithInvalidValue_allOtherFieldsEmpty_errorMessageObservableUpdatedWithErrorMessage() throws Exception {
        // Arrange
        whenShortTextValidationReturnErrorMessage();
        // Act
        SUT.onStart(VALID_EXISTING_RECIPE_ID);
        simulateNothingReturnedFromDatabase();
        SUT.titleObservable.set("ti");
        // Assert
        assertEquals(SUT.titleErrorMessage.get(), SHORT_TEXT_VALIDATION_ERROR);
    }

    @Test
    public void observableTitleUpdatedWithInvalidValue_allOtherFieldsValid_errorMessageObservableUpdatedWithErrorMessage() throws Exception {
        // Arrange
        whenShortTextValidationReturnErrorMessage();
        whenLongTextValidationReturnValidated();
        // Act
        SUT.onStart(VALID_EXISTING_RECIPE_ID);
        simulateGetValidRecipeIdentityEntityFromDatabase();
        SUT.titleObservable.set("ti");
        // Assert
        assertEquals(SUT.titleErrorMessage.get(), SHORT_TEXT_VALIDATION_ERROR);
    }

    @Test
    public void observableTitleUpdatedWithInvalidValue_allOtherFieldsEmpty_RecipeModelStatus_IDENTITY_MODEL_CHANGED_INVALID() throws Exception {
        // Arrange
        ArgumentCaptor<RecipeModelStatus> ac = ArgumentCaptor.forClass(RecipeModelStatus.class);
        whenShortTextValidationReturnErrorMessage();
        // Act
        SUT.onStart(VALID_EXISTING_RECIPE_ID);
        simulateNothingReturnedFromDatabase();
        SUT.titleObservable.set(INVALID_TITLE);
        // Assert
        verify(modelSubmissionMock, times(2)).submitModelStatus(ac.capture());
        RecipeModelStatus modelStatus = ac.getValue();
        assertEquals(IDENTITY_MODEL_CHANGED_INVALID, modelStatus);
    }

    @Test
    public void observableTitleUpdatedWithValidValue_allOtherFieldsEmpty_errorMessageObservableUpdatedWithNull() throws Exception {
        // Arrange
        whenShortTextValidationReturnValidated();
        // Act
        SUT.onStart(VALID_EXISTING_RECIPE_ID);
        simulateNothingReturnedFromDatabase();
        SUT.titleObservable.set(VALID_EXISTING_TITLE);
        // Assert
        assertNull(SUT.titleErrorMessage.get());
    }

    @Test
    public void observableTitleUpdatedWithValidValue_allOtherFieldsEmpty_entitySavedToDatabase() throws Exception {
        // Arrange
        RecipeIdentityEntity identityEntity = new RecipeIdentityEntity(
                VALID_EXISTING_IDENTITY_ID,
                VALID_EXISTING_RECIPE_ID,
                VALID_EXISTING_TITLE,
                "",
                0,
                0,
                VALID_EXISTING_CREATE_DATE,
                VALID_EXISTING_LAST_UPDATE
        );
        whenShortTextValidationReturnValidated();
        whenIdProviderThenReturnExistingRecipeId();
        whenTimeProviderThenReturnExistingRecipeCreateDate();
        // Act
        SUT.onStart(VALID_EXISTING_RECIPE_ID);
        simulateNothingReturnedFromDatabase();
        SUT.titleObservable.set(VALID_EXISTING_TITLE);
        // Assert
        verify(identityDataSourceMock).save(eq(identityEntity));
    }

    @Test
    public void observableTitleUpdatedWithValidValue_allOtherFieldsEmpty_RecipeModelStatus_IDENTITY_MODEL_CHANGED_VALID() throws Exception {
        // Arrange
        ArgumentCaptor<RecipeModelStatus> ac = ArgumentCaptor.forClass(RecipeModelStatus.class);
        whenShortTextValidationReturnValidated();
        // Act
        SUT.onStart(VALID_EXISTING_RECIPE_ID);
        simulateNothingReturnedFromDatabase();
        SUT.titleObservable.set(VALID_EXISTING_TITLE);
        // Assert
        verify(modelSubmissionMock, times(2)).submitModelStatus(ac.capture());
        RecipeModelStatus modelStatus = ac.getValue();
        assertEquals(IDENTITY_MODEL_CHANGED_VALID, modelStatus);
    }

    @Test
    public void observableDescriptionUpdatedWithInvalidValue_titleValid_errorMessageObservableUpdatedWithErrorMessage() throws Exception {
        // Arrange
        whenShortTextValidationReturnValidated();
        whenLongTextValidationReturnErrorMessage();
        // Act
        SUT.onStart(VALID_EXISTING_RECIPE_ID);
        simulateNothingReturnedFromDatabase();
        SUT.titleObservable.set(VALID_EXISTING_TITLE);
        SUT.descriptionObservable.set(INVALID_DESCRIPTION);
        // Assert
        assertEquals(LONG_TEXT_VALIDATION_ERROR, SUT.descriptionErrorMessage.get());
    }

    @Test
    public void observableDescriptionUpdatedWithInvalidValue_titleValid_entityNotSavedToDatabase() throws Exception {
        // Arrange
        whenShortTextValidationReturnValidated();
        whenLongTextValidationReturnErrorMessage();
        // Act
        SUT.onStart(VALID_EXISTING_RECIPE_ID);
        simulateNothingReturnedFromDatabase();
        SUT.titleObservable.set(VALID_EXISTING_TITLE);
        SUT.descriptionObservable.set(INVALID_DESCRIPTION);
        // Assert (only one save for the valid title, no save for invalid description)
        verify(identityDataSourceMock).save(anyObject());
    }

    @Test
    public void observableDescriptionUpdatedWithInvalidValue_titleValid_RecipeModelStatus_IDENTITY_MODEL_CHANGED_INVALID() throws Exception {
        // Arrange
        ArgumentCaptor<RecipeModelStatus> ac = ArgumentCaptor.forClass(RecipeModelStatus.class);
        whenShortTextValidationReturnValidated();
        whenLongTextValidationReturnErrorMessage();
        // Act
        SUT.onStart(VALID_EXISTING_RECIPE_ID);
        simulateNothingReturnedFromDatabase();
        SUT.titleObservable.set(VALID_EXISTING_TITLE);
        SUT.descriptionObservable.set(INVALID_DESCRIPTION);
        // Assert
        verify(modelSubmissionMock, times(3)).submitModelStatus(ac.capture());
        RecipeModelStatus modelStatus = ac.getValue();
        assertEquals(IDENTITY_MODEL_CHANGED_INVALID, modelStatus);
    }

    @Test
    public void observableDescriptionUpdatedWithValidValue_titleValid_errorMessageObservableUpdatedWithNull() throws Exception {
        // Arrange
        whenShortTextValidationReturnValidated();
        whenLongTextValidationReturnValidated();
        // Act
        SUT.onStart(VALID_EXISTING_RECIPE_ID);
        simulateNothingReturnedFromDatabase();
        SUT.titleObservable.set(VALID_EXISTING_TITLE);
        SUT.descriptionObservable.set(VALID_EXISTING_DESCRIPTION);
        // Assert
        assertNull(SUT.titleErrorMessage.get());
        assertNull(SUT.descriptionErrorMessage.get());
    }

    @Test
    public void observableDescriptionUpdatedWithValidValue_titleValid_entitySavedToDatabase() throws Exception {
        // Arrange
        ArgumentCaptor<RecipeIdentityEntity> ac = ArgumentCaptor.forClass(RecipeIdentityEntity.class);
        whenShortTextValidationReturnValidated();
        whenLongTextValidationReturnValidated();
        whenTimeProviderThenReturnExistingRecipeCreateDate();
        // Act
        SUT.onStart(VALID_EXISTING_RECIPE_ID);
        simulateNothingReturnedFromDatabase();
        SUT.titleObservable.set(VALID_EXISTING_TITLE);
        SUT.descriptionObservable.set(VALID_EXISTING_DESCRIPTION);
        // Assert
        verify(identityDataSourceMock, times(2)).save(ac.capture());
        List<RecipeIdentityEntity> identityEntities = ac.getAllValues();
        RecipeIdentityEntity identityEntity = identityEntities.get(1);
        assertEquals(VALID_EXISTING_TITLE, identityEntity.getTitle());
        assertEquals(VALID_EXISTING_DESCRIPTION, identityEntity.getDescription());
    }

    @Test
    public void observableDescriptionUpdatedWithValidValue_titleValid_RecipeModelStatus_IDENTITY_MODEL_CHANGED_VALID() throws Exception {
        // Arrange
        ArgumentCaptor<RecipeModelStatus> ac = ArgumentCaptor.forClass(RecipeModelStatus.class);
        whenShortTextValidationReturnValidated();
        whenLongTextValidationReturnValidated();
        // Act
        SUT.onStart(VALID_EXISTING_RECIPE_ID);
        simulateNothingReturnedFromDatabase();
        SUT.titleObservable.set(VALID_EXISTING_TITLE);
        SUT.descriptionObservable.set(VALID_EXISTING_DESCRIPTION);
        // Assert
        verify(modelSubmissionMock, times(3)).submitModelStatus(ac.capture());
        RecipeModelStatus modelStatus = ac.getValue();
        assertEquals(IDENTITY_MODEL_CHANGED_VALID, modelStatus);
    }

    @Test
    public void observableDescriptionUpdatedWithValidValue_titleInvalid_entityNotSavedToDatabase() throws Exception {
        whenShortTextValidationReturnErrorMessage();
        whenLongTextValidationReturnValidated();
        whenTimeProviderThenReturnExistingRecipeCreateDate();
        // Act
        SUT.onStart(VALID_EXISTING_RECIPE_ID);
        simulateNothingReturnedFromDatabase();
        SUT.titleObservable.set(INVALID_TITLE);
        SUT.descriptionObservable.set(VALID_EXISTING_DESCRIPTION);
        // Assert
        verifyNoMoreInteractions(identityDataSourceMock);
    }

    @Test
    public void observablePrepMinutes_validValue_prepTimeErrorMessageObservableNull() throws Exception {
        whenIntFromObserver_thenReturn(VALID_EXISTING_PREP_TIME % 60);
        // Act
        SUT.onStart(VALID_EXISTING_RECIPE_ID);
        simulateNothingReturnedFromDatabase();
        SUT.prepMinutesObservable.set(String.valueOf(VALID_EXISTING_PREP_TIME % 60));
        // Assert
        assertNull(SUT.prepTimeErrorMessage.get());
    }

    @Test
    public void observablePrepMinutes_invalidValue_prepTimeErrorMessageObservableErrorMessage() throws Exception {
        whenIntFromObserver_thenReturn(MAX_PREP_TIME + 1);
        // Act
        SUT.onStart(VALID_EXISTING_RECIPE_ID);
        simulateNothingReturnedFromDatabase();
        SUT.prepMinutesObservable.set(String.valueOf(MAX_PREP_TIME + 1));
        // Assert
        assertEquals(ERROR_MESSAGE_TOO_LONG ,SUT.prepTimeErrorMessage.get());
    }

    @Test
    public void observablePrepHours_validValue_prepTimeErrorMessageObservableNull() throws Exception {
        whenIntFromObserver_thenReturn(MAX_PREP_TIME / 60);
        // Act
        SUT.onStart(VALID_EXISTING_RECIPE_ID);
        simulateNothingReturnedFromDatabase();
        SUT.prepHoursObservable.set(String.valueOf(MAX_PREP_TIME / 60));
        // Assert
        assertNull(SUT.prepTimeErrorMessage.get());
    }

    @Test
    public void observablePrepHours_invalidValue_prepTimeErrorMessageObservableErrorMessage() throws Exception {
        whenIntFromObserver_thenReturn(MAX_PREP_TIME / 60 + 1);
        // Act
        SUT.onStart(VALID_EXISTING_RECIPE_ID);
        simulateNothingReturnedFromDatabase();
        SUT.prepHoursObservable.set(String.valueOf(MAX_PREP_TIME / 60 + 1));
        // Assert
        assertEquals(ERROR_MESSAGE_TOO_LONG, SUT.prepTimeErrorMessage.get());
    }

    @Test
    public void observablePrepHoursMinutes_validPrepTime_prepTimeSaved() throws Exception {
        // Arrange
        whenShortTextValidationReturnValidated();
        whenLongTextValidationReturnValidated();
        // Act
        SUT.onStart(VALID_EXISTING_RECIPE_ID);
        simulateNothingReturnedFromDatabase();
        SUT.titleObservable.set(VALID_EXISTING_TITLE); // makes the model valid
        whenIntFromObserver_thenReturn(MAX_PREP_TIME / 60);
        SUT.prepHoursObservable.set(String.valueOf(MAX_PREP_TIME / 60));
        whenIntFromObserver_thenReturn(MAX_PREP_TIME % 60);
        SUT.prepMinutesObservable.set(String.valueOf(MAX_PREP_TIME % 60));
        // Assert
        verify(identityDataSourceMock, times(3)).save(anyObject());
    }

    @Test
    public void observablePrepHoursMinutes_invalidPrepTime_invalidTimeNotSaved() throws Exception {
        // Arrange
        whenShortTextValidationReturnValidated();
        // Act
        SUT.onStart(VALID_EXISTING_RECIPE_ID);
        simulateNothingReturnedFromDatabase();
        SUT.titleObservable.set(VALID_EXISTING_TITLE); // makes the model valid
        whenIntFromObserver_thenReturn(MAX_PREP_TIME / 60);
        SUT.prepHoursObservable.set(String.valueOf(MAX_PREP_TIME / 60));
        whenIntFromObserver_thenReturn(MAX_PREP_TIME % 60 + 1);
        SUT.prepMinutesObservable.set(String.valueOf(MAX_PREP_TIME % 60 + 1));
        // Assert (save title, save valid prep hours, don't save invalid prep time when minutes added)
        verify(identityDataSourceMock, times(2)).save(anyObject());
    }

    @Test
    public void observablePrepHoursMinutes_inValidPrepTime_RecipeModelStatus_IDENTITY_MODEL_CHANGED_INVALID() throws Exception {
        // Arrange
        whenShortTextValidationReturnValidated();
        // Act
        SUT.onStart(VALID_EXISTING_RECIPE_ID);
        simulateNothingReturnedFromDatabase();
        SUT.titleObservable.set(VALID_EXISTING_TITLE); // makes the model valid
        whenIntFromObserver_thenReturn(MAX_PREP_TIME / 60);
        SUT.prepHoursObservable.set(String.valueOf(MAX_PREP_TIME / 60));
        whenIntFromObserver_thenReturn(MAX_PREP_TIME % 60 + 1);
        SUT.prepMinutesObservable.set(String.valueOf(MAX_PREP_TIME % 60 + 1));
        verify(modelSubmissionMock).submitModelStatus(IDENTITY_MODEL_CHANGED_INVALID);
    }

    @Test
    public void observablePrepHoursMinutes_validPrepTime_RecipeModelStatus_IDENTITY_MODEL_CHANGED_VALID() throws Exception {
        // Arrange
        whenShortTextValidationReturnValidated();
        // Act
        SUT.onStart(VALID_EXISTING_RECIPE_ID);
        simulateNothingReturnedFromDatabase();
        SUT.titleObservable.set(VALID_EXISTING_TITLE); // makes the model valid
        whenIntFromObserver_thenReturn(MAX_PREP_TIME / 60);
        SUT.prepHoursObservable.set(String.valueOf(MAX_PREP_TIME / 60));
        whenIntFromObserver_thenReturn(MAX_PREP_TIME % 60);
        SUT.prepMinutesObservable.set(String.valueOf(MAX_PREP_TIME % 60 + 1));
        verify(modelSubmissionMock, times(3)).submitModelStatus(IDENTITY_MODEL_CHANGED_VALID);
    }

    @Test
    public void observableCookMinutes_validValue_cookTimeErrorMessageObservableNull() throws Exception {
        whenShortTextValidationReturnValidated();
        whenIntFromObserver_thenReturn(MAX_COOK_TIME);
        // Act
        SUT.onStart(VALID_EXISTING_RECIPE_ID);
        simulateNothingReturnedFromDatabase();
        SUT.titleObservable.set(VALID_EXISTING_TITLE); // make the model valid
        SUT.cookMinutesObservable.set(String.valueOf(MAX_COOK_TIME));
        // Assert
        assertNull(SUT.cookTimeErrorMessage.get());
    }

    @Test
    public void observableCookMinutes_invalidValue_cookTimeErrorMessageObservableErrorMessage() throws Exception {
        whenShortTextValidationReturnValidated();
        whenIntFromObserver_thenReturn(MAX_COOK_TIME + 1);
        // Act
        SUT.onStart(VALID_EXISTING_RECIPE_ID);
        simulateNothingReturnedFromDatabase();
        SUT.titleObservable.set(VALID_EXISTING_TITLE); // make the model valid
        SUT.cookMinutesObservable.set(String.valueOf(MAX_COOK_TIME + 1));
        // Assert
        assertEquals(ERROR_MESSAGE_TOO_LONG, SUT.cookTimeErrorMessage.get());
    }

    @Test
    public void observableCookHours_validValue_cookTimeErrorMessageObservableNull() throws Exception {
        whenShortTextValidationReturnValidated();
        whenIntFromObserver_thenReturn(MAX_COOK_TIME / 60);
        // Act
        SUT.onStart(VALID_EXISTING_RECIPE_ID);
        simulateNothingReturnedFromDatabase();
        SUT.titleObservable.set(VALID_EXISTING_TITLE); // make the model valid
        SUT.cookHoursObservable.set(String.valueOf(MAX_COOK_TIME / 60));
        // Assert
        assertNull(SUT.cookTimeErrorMessage.get());
    }

    @Test
    public void observableCookHours_invalidValue_cookTimeErrorMessageObservableErrorMessage() throws Exception {
        whenShortTextValidationReturnValidated();
        whenIntFromObserver_thenReturn(MAX_COOK_TIME / 60 + 1);
        // Act
        SUT.onStart(VALID_EXISTING_RECIPE_ID);
        simulateNothingReturnedFromDatabase();
        SUT.titleObservable.set(VALID_EXISTING_TITLE); // make the model valid
        SUT.cookHoursObservable.set(String.valueOf(MAX_COOK_TIME / 60 + 1));
        // Assert
        assertEquals(ERROR_MESSAGE_TOO_LONG, SUT.cookTimeErrorMessage.get());
    }

    @Test
    public void observableCookHoursMinutes_validTime_savedToDatabase() throws Exception {
        // Arrange
        whenShortTextValidationReturnValidated();
        when(intFromObservableMock.parseInt(anyObject(), anyObject(), anyInt())).
                thenReturn(MAX_COOK_TIME / 60,
                        MAX_COOK_TIME % 60);
        // Act
        SUT.onStart(VALID_EXISTING_RECIPE_ID);
        simulateNothingReturnedFromDatabase();
        SUT.titleObservable.set(VALID_EXISTING_TITLE);
        SUT.cookHoursObservable.set(String.valueOf(MAX_COOK_TIME / 60));
        SUT.cookMinutesObservable.set(String.valueOf(MAX_COOK_TIME % 60));
        // Assert
        verify(identityDataSourceMock, times(3)).save(anyObject());
    }

    @Test
    public void observableCookHoursMinutes_invalidValue_prepTimeErrorMessageObservableErrorString() throws Exception {
        whenShortTextValidationReturnValidated();
        when(intFromObservableMock.parseInt(anyObject(), anyObject(), anyInt())).
                thenReturn(MAX_COOK_TIME / 60,
                        MAX_COOK_TIME % 60 + 1);

        // Act
        SUT.onStart(VALID_EXISTING_RECIPE_ID);
        simulateNothingReturnedFromDatabase();
        SUT.titleObservable.set(VALID_EXISTING_TITLE); // make the model valid
        SUT.cookHoursObservable.set(String.valueOf(MAX_COOK_TIME / 60));
        SUT.cookMinutesObservable.set(String.valueOf(MAX_COOK_TIME % 60 +1));
        // Assert
        assertEquals(ERROR_MESSAGE_TOO_LONG, SUT.cookTimeErrorMessage.get());
    }

    @Test // Simulates user input for a complete new recipe IdentityModel
    public void allObservablesUpdatedWithValidData_emptyRecipeSupplied_identityModelSavedTDatabase() throws Exception {
        // Arrange
        ArgumentCaptor<RecipeIdentityEntity> ac =
                ArgumentCaptor.forClass(RecipeIdentityEntity.class);
        whenShortTextValidationReturnValidated();
        whenLongTextValidationReturnValidated();
        when(intFromObservableMock.parseInt(anyObject(), anyObject(), anyInt())).
                thenReturn(
                        VALID_EXISTING_PREP_TIME / 60,
                        VALID_EXISTING_PREP_TIME % 60,
                        VALID_EXISTING_COOK_TIME / 60,
                        VALID_EXISTING_COOK_TIME % 60);
        whenIdProviderThenReturnExistingRecipeId();
        whenTimeProviderThenReturnExistingRecipeCreateDate();
        // Act
        SUT.onStart(VALID_EXISTING_RECIPE_ID);
        simulateNothingReturnedFromDatabase();
        SUT.titleObservable.set(VALID_EXISTING_TITLE);
        SUT.descriptionObservable.set(VALID_EXISTING_DESCRIPTION);
        SUT.prepHoursObservable.set(String.valueOf(VALID_EXISTING_PREP_TIME / 60));
        SUT.prepMinutesObservable.set(String.valueOf(VALID_EXISTING_PREP_TIME % 60));
        SUT.cookHoursObservable.set(String.valueOf(VALID_EXISTING_COOK_TIME / 60));
        SUT.cookMinutesObservable.set(String.valueOf(VALID_EXISTING_COOK_TIME % 60));
        // Assert
        verify(identityDataSourceMock, times(6)).save(ac.capture());
        List<RecipeIdentityEntity> identityList = ac.getAllValues();
        RecipeIdentityEntity identityEntity = identityList.get(5);

        assertEquals(VALID_EXISTING_IDENTITY_ENTITY, identityEntity);
    }

    @Test(expected = RuntimeException.class)
    public void onStart_nullRecipe_runtimeExceptionThrown() throws Exception {
        // Arrange
        // Act
        SUT.onStart(null);
        // Assert
        ArrayList list = new ArrayList();
        RuntimeException exception = (RuntimeException) list.get(0);
        assertEquals(exception.getMessage(), "Recipe id cannot be null");
    }

    // region for helper methods -------------------------------------------------------------------
    private void simulateGetValidRecipeIdentityEntityFromDatabase() {
        verify(identityDataSourceMock).getByRecipeId(eq(VALID_EXISTING_RECIPE_ID),
                getEntityCallbackArgumentCaptor.capture());
        // simulate no recipe identity returned
        getEntityCallbackArgumentCaptor.getValue().onEntityLoaded(VALID_EXISTING_IDENTITY_ENTITY);
    }

    private void whenTextValidationValidateTitleAndDescription() {
        when(textValidationHandlerMock.validateShortText(
                eq(resourcesMock),
                eq(VALID_EXISTING_IDENTITY_ENTITY.getTitle()))).
                thenReturn(VALIDATED);

        when(textValidationHandlerMock.validateLongText(
                eq(resourcesMock),
                eq(VALID_EXISTING_IDENTITY_ENTITY.getDescription()))).
                thenReturn(VALIDATED);
    }

    private void simulateNothingReturnedFromDatabase() {
        verify(identityDataSourceMock).getByRecipeId(eq(VALID_EXISTING_RECIPE_ID),
                getEntityCallbackArgumentCaptor.capture());
        // simulate no recipe identity returned
        getEntityCallbackArgumentCaptor.getValue().onDataNotAvailable();
    }

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

    private void whenIntFromObserverMockReturnValidRecipeTimes() {
        when(intFromObservableMock.parseInt(anyObject(), eq(SUT.prepHoursObservable), anyInt())).
                thenReturn(VALID_EXISTING_PREP_TIME / 60);
        when(intFromObservableMock.parseInt(anyObject(), eq(SUT.prepMinutesObservable), anyInt())).
                thenReturn(VALID_EXISTING_PREP_TIME % 60);
        when(intFromObservableMock.parseInt(anyObject(), eq(SUT.cookHoursObservable), anyInt())).
                thenReturn(VALID_EXISTING_COOK_TIME / 60);
        when(intFromObservableMock.parseInt(anyObject(), eq(SUT.cookMinutesObservable), anyInt())).
                thenReturn(VALID_EXISTING_COOK_TIME % 60);
    }

    private void whenIdProviderThenReturnExistingRecipeId() {
        when(uniqueIdProviderMock.getUId()).thenReturn(
                VALID_EXISTING_IDENTITY_ENTITY.getId());
    }

    private void whenTimeProviderThenReturnExistingRecipeCreateDate() {
        when(timeProviderMock.getCurrentTimestamp()).thenReturn(
                VALID_EXISTING_IDENTITY_ENTITY.getCreateDate());
    }
    // endregion helper methods --------------------------------------------------------------------

    // region helper classes -----------------------------------------------------------------------

    // endregion helper classes --------------------------------------------------------------------

}