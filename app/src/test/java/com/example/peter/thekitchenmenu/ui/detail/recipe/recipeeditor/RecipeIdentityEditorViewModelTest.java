package com.example.peter.thekitchenmenu.ui.detail.recipe.recipeeditor;

import android.content.res.Resources;

import com.example.peter.thekitchenmenu.R;
import com.example.peter.thekitchenmenu.data.entity.RecipeIdentityEntity;
import com.example.peter.thekitchenmenu.data.repository.DataSource;
import com.example.peter.thekitchenmenu.data.repository.RepositoryRecipeIdentity;
import com.example.peter.thekitchenmenu.testdata.TestDataRecipeIdentityEntity;
import com.example.peter.thekitchenmenu.testdata.TestDataRecipeValidator;
import com.example.peter.thekitchenmenu.ui.utils.TextValidationHandler;
import com.example.peter.thekitchenmenu.utils.*;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;

import static com.example.peter.thekitchenmenu.ui.utils.TextValidationHandler.VALIDATED;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class RecipeIdentityEditorViewModelTest {

    // region constants ----------------------------------------------------------------------------
    private static final RecipeIdentityEntity INVALID_NEW_EMPTY =
            TestDataRecipeIdentityEntity.getInvalidNewEmpty();
    private static final RecipeIdentityEntity INVALID_NEW_TITLE_INVALID =
            TestDataRecipeIdentityEntity.getInvalidNewTitleUpdatedWithInvalidValue();
    private static final RecipeIdentityEntity INVALID_NEW_TITLE_INVALID_DESCRIPTION_VALID =
            TestDataRecipeIdentityEntity.getInvalidNewTitleInvalidDescriptionValid();
    private static final RecipeIdentityEntity VALID_NEW_TITLE_VALID =
            TestDataRecipeIdentityEntity.getValidNewTitleUpdatedWithValidValue();
    private static final RecipeIdentityEntity VALID_NEW_COMPLETE =
            TestDataRecipeIdentityEntity.getValidNewComplete();
    private static final RecipeIdentityEntity INVALID_EXISTING_INCOMPLETE_INVALID_TITLE =
            TestDataRecipeIdentityEntity.getInvalidExistingIncomplete();
    private static final RecipeIdentityEntity VALID_EXISTING_COMPLETE =
            TestDataRecipeIdentityEntity.getValidExistingComplete();
    private static final RecipeIdentityEntity VALID_FROM_ANOTHER_USER =
            TestDataRecipeIdentityEntity.getValidCompleteFromAnotherUser();
    private static final RecipeIdentityEntity INVALID_FROM_ANOTHER_USER =
            TestDataRecipeIdentityEntity.getInvalidCompleteFromAnotherUser();
    private static final RecipeIdentityEntity VALID_NEW_CLONED =
            TestDataRecipeIdentityEntity.getValidNewCloned();
    private static final RecipeIdentityEntity INVALID_NEW_CLONED =
            TestDataRecipeIdentityEntity.getInvalidNewCloned();
    private static final RecipeIdentityEntity VALID_CLONED_DESCRIPTION_UPDATED =
            TestDataRecipeIdentityEntity.getValidNewClonedDescriptionUpdatedComplete();

    private RecipeModelStatus INVALID_UNCHANGED =
            TestDataRecipeValidator.getIdentityModelStatusUnchangedInvalid();
    private RecipeModelStatus INVALID_CHANGED =
            TestDataRecipeValidator.getIdentityModelStatusChangedInvalid();
    private RecipeModelStatus VALID_UNCHANGED =
            TestDataRecipeValidator.getIdentityModelStatusUnchangedValid();
    private RecipeModelStatus VALID_CHANGED =
            TestDataRecipeValidator.getIdentityModelStatusChangedValid();

    private static final String ERROR_MESSAGE_TOO_LONG = "error_message_too_long";

    private static final String SHORT_TEXT_VALIDATION_ERROR = "short_text_validation_error";
    private static final String LONG_TEXT_VALIDATION_ERROR = "long_text_validation_error";

    // endregion constants -------------------------------------------------------------------------

    // region helper fields ------------------------------------------------------------------------
//    @Rule
//    public InstantTaskExecutorRule instantExecutorRule = new InstantTaskExecutorRule();
    @Mock
    Resources resourcesMock;
    @Mock
    TextValidationHandler textValidationHandlerMock;
    @Captor
    ArgumentCaptor<DataSource.GetEntityCallback<RecipeIdentityEntity>>
            getEntityCallbackArgumentCaptor;
    @Mock
    RepositoryRecipeIdentity dataSourceMock;
    @Mock
    TimeProvider timeProviderMock;
    @Mock
    RecipeValidation.RecipeValidatorModelSubmission modelValidationSubmitterMock;
    @Captor
    ArgumentCaptor<RecipeModelStatus> statusArgumentCaptor;
    // endregion helper fields ---------------------------------------------------------------------

    private RecipeIdentityEditorViewModel SUT;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        setupResourceMockReturnValues();

        SUT = new RecipeIdentityEditorViewModel(
                dataSourceMock,
                timeProviderMock,
                resourcesMock,
                textValidationHandlerMock
        );

        SUT.setModelValidationSubmitter(modelValidationSubmitterMock);
    }

    @Test
    public void startNewRecipeId_nothingSetToObservers() {
        // Arrange
        // Act
        SUT.start(INVALID_NEW_EMPTY.getId());
        simulateNothingReturnedFromDatabase();
        // Assert
        assertEquals(INVALID_NEW_EMPTY.getTitle(), SUT.getTitle());
        assertEquals(INVALID_NEW_EMPTY.getDescription(), SUT.getDescription());
        assertNull(SUT.titleErrorMessage.get());
        assertNull(SUT.descriptionErrorMessage.get());
    }

    @Test
    public void startNewRecipeId_newEmptyEntityNotSaved() {
        // Arrange
        when(timeProviderMock.getCurrentTimeInMills()).thenReturn(INVALID_NEW_EMPTY.getCreateDate());
        when(textValidationHandlerMock.validateShortText(eq(resourcesMock), eq(""))).
                thenReturn(SHORT_TEXT_VALIDATION_ERROR);
        when(textValidationHandlerMock.validateLongText(eq(resourcesMock), eq(""))).
                thenReturn(VALIDATED);
        // Act
        SUT.start(INVALID_NEW_EMPTY.getId());
        simulateNothingReturnedFromDatabase();
        // Assert
        verifyNoMoreInteractions(dataSourceMock);
    }

    @Test
    public void startNewRecipeId_RecipeModelStatusINVALID_UNCHANGED() {
        // Arrange
        // Act
        SUT.start(INVALID_NEW_EMPTY.getId());
        simulateNothingReturnedFromDatabase();
        // Assert
        verify(modelValidationSubmitterMock).submitModelStatus(statusArgumentCaptor.capture());
        RecipeModelStatus actualStatus = statusArgumentCaptor.getValue();
        assertEquals(INVALID_UNCHANGED, actualStatus);
    }

    @Test
    public void startNewRecipeId_invalidTitle_errorMessageSetToObservable() {
        // Arrange
        whenShortTextValidationReturnErrorMessage();
        // Act
        SUT.start(INVALID_NEW_EMPTY.getId());
        simulateNothingReturnedFromDatabase();
        SUT.setTitle(INVALID_NEW_TITLE_INVALID.getTitle());
        // Assert
        assertEquals(SHORT_TEXT_VALIDATION_ERROR, SUT.titleErrorMessage.get());
    }

    @Test
    public void startNewRecipeId_invalidTitle_recipeModelStatusINVALID_CHANGED() {
        // Arrange
        whenShortTextValidationReturnErrorMessage();
        // Act
        SUT.start(INVALID_NEW_EMPTY.getId());
        simulateNothingReturnedFromDatabase();
        SUT.setTitle(INVALID_NEW_TITLE_INVALID.getTitle());
        // Assert
        verify(modelValidationSubmitterMock, times((2))).submitModelStatus(
                statusArgumentCaptor.capture());
        RecipeModelStatus actualStatus = statusArgumentCaptor.getValue();
        assertEquals(INVALID_CHANGED, actualStatus);
    }

    @Test
    public void startNewRecipeId_invalidTitleValidDescription_emptyEntitySaved() {
        whenShortTextValidationReturnErrorMessage();
        whenLongTextValidationReturnValidated();
        whenTimeProviderThenReturnNewEntityCreateDate();
        // Act
        SUT.start(INVALID_NEW_EMPTY.getId());
        simulateNothingReturnedFromDatabase();
        SUT.setTitle(INVALID_NEW_TITLE_INVALID_DESCRIPTION_VALID.getTitle());
        SUT.setDescription(INVALID_NEW_TITLE_INVALID_DESCRIPTION_VALID.getDescription());
        // Assert
        verifyNoMoreInteractions(dataSourceMock);
    }

    @Test
    public void startNewRecipeId_validTitle_errorMessageObservableNull() {
        // Arrange
        whenShortTextValidationReturnValidated();
        // Act
        SUT.start(INVALID_NEW_EMPTY.getId());
        simulateNothingReturnedFromDatabase();
        SUT.setTitle(VALID_NEW_TITLE_VALID.getTitle());
        // Assert
        assertNull(SUT.titleErrorMessage.get());
    }

    @Test
    public void startNewRecipeId_validTitle_titleSaved() {
        // Arrange
        whenShortTextValidationReturnValidated();
        whenTimeProviderThenReturnNewEntityCreateDate();
        // Act
        SUT.start(INVALID_NEW_EMPTY.getId());
        simulateNothingReturnedFromDatabase();
        SUT.setTitle(VALID_NEW_TITLE_VALID.getTitle());
        // Assert
        verify(dataSourceMock).save(eq(VALID_NEW_TITLE_VALID));
    }

    @Test
    public void startNewRecipeId_validTitle_recipeModelStatusVALID_CHANGED() {
        // Arrange
        whenShortTextValidationReturnValidated();
        whenLongTextValidationReturnValidated();
        // Act
        SUT.start(INVALID_NEW_EMPTY.getId());
        simulateNothingReturnedFromDatabase();
        SUT.setTitle(VALID_NEW_TITLE_VALID.getTitle());
        // Assert
        verify(modelValidationSubmitterMock, times((2))).
                submitModelStatus(statusArgumentCaptor.capture());
        RecipeModelStatus actualStatus = statusArgumentCaptor.getValue();
        assertEquals(VALID_CHANGED, actualStatus);
    }

    @Test
    public void startNewRecipeId_validTitleInvalidDescription_errorMessageSetToObservable() {
        // Arrange
        whenShortTextValidationReturnValidated();
        whenLongTextValidationReturnErrorMessage();
        // Act
        SUT.start(INVALID_NEW_EMPTY.getId());
        simulateNothingReturnedFromDatabase();
        SUT.setTitle(VALID_NEW_TITLE_VALID.getTitle());
        SUT.setDescription("Doesn't matter what is here as returning an error message!");
        // Assert
        assertEquals(LONG_TEXT_VALIDATION_ERROR, SUT.descriptionErrorMessage.get());
    }

    @Test
    public void startNewRecipeId_validTitleInvalidDescription_descriptionNotSaved() {
        // Arrange
        when(timeProviderMock.getCurrentTimeInMills()).
                thenReturn(VALID_NEW_TITLE_VALID.getCreateDate());
        whenShortTextValidationReturnValidated();
        whenLongTextValidationReturnErrorMessage();
        // Act
        SUT.start(INVALID_NEW_EMPTY.getId());
        simulateNothingReturnedFromDatabase();
        SUT.setTitle(VALID_NEW_TITLE_VALID.getTitle());
        SUT.setDescription("Doesn't matter what is here as returning an error message!");
        verify(dataSourceMock).save(eq(VALID_NEW_TITLE_VALID));
    }

    @Test
    public void startNewRecipeId_validTitleInvalidDescription_recipeModelStatusINVALID_CHANGED() {
        // Arrange
        whenShortTextValidationReturnValidated();
        whenLongTextValidationReturnErrorMessage();
        // Act
        SUT.start(INVALID_NEW_EMPTY.getId());
        simulateNothingReturnedFromDatabase();
        SUT.setTitle(VALID_NEW_TITLE_VALID.getTitle());
        SUT.setDescription("Doesn't matter what is here as returning an error message!");
        // Assert
        verify(modelValidationSubmitterMock, times((3))).submitModelStatus(
                statusArgumentCaptor.capture());
        RecipeModelStatus actualStatus = statusArgumentCaptor.getValue();
        assertEquals(INVALID_CHANGED, actualStatus);
    }

    @Test
    public void startNewRecipeId_validTitleValidDescription_errorMessageObservableNull() {
        // Arrange
        whenShortTextValidationReturnValidated();
        whenLongTextValidationReturnValidated();
        // Act
        SUT.start(INVALID_NEW_EMPTY.getId());
        simulateNothingReturnedFromDatabase();
        SUT.setTitle(VALID_NEW_COMPLETE.getTitle());
        SUT.setDescription(VALID_NEW_COMPLETE.getDescription());
        // Assert
        assertNull(SUT.titleErrorMessage.get());
        assertNull(SUT.descriptionErrorMessage.get());
    }

    @Test
    public void startNewRecipeId_validTitleValidDescription_saved() {
        // Arrange
        whenShortTextValidationReturnValidated();
        whenLongTextValidationReturnValidated();
        when(timeProviderMock.getCurrentTimeInMills()).thenReturn(VALID_NEW_COMPLETE.getCreateDate());
        // Act
        SUT.start(INVALID_NEW_EMPTY.getId());
        simulateNothingReturnedFromDatabase();
        SUT.setTitle(VALID_NEW_COMPLETE.getTitle());
        SUT.setDescription(VALID_NEW_COMPLETE.getDescription());
        // Assert
        verify(dataSourceMock, times((1))).save(eq(VALID_NEW_COMPLETE));
    }

    @Test
    public void startNewRecipeId_validTitleValidDescription_recipeModelStatusVALID_CHANGED() {
        // Arrange
        whenShortTextValidationReturnValidated();
        whenLongTextValidationReturnValidated();
        // Act
        SUT.start(INVALID_NEW_EMPTY.getId());
        simulateNothingReturnedFromDatabase();
        SUT.setTitle(VALID_NEW_COMPLETE.getTitle());
        SUT.setDescription(VALID_NEW_COMPLETE.getDescription());
        // Assert
        verify(modelValidationSubmitterMock, times((3))).submitModelStatus(
                statusArgumentCaptor.capture());
        RecipeModelStatus actualStatus = statusArgumentCaptor.getValue();
        assertEquals(VALID_CHANGED, actualStatus);
    }

    @Test
    public void startValidExistingRecipeId_titleSetToObservable() {
        // Arrange
        whenTextValidationValidateTitleAndDescription();
        // Act
        SUT.start(VALID_EXISTING_COMPLETE.getId());
        simulateGetValidExistingCompleteFromDatabase();
        // Assert
        assertEquals(VALID_EXISTING_COMPLETE.getTitle(), SUT.getTitle());
    }

    @Test
    public void startValidExistingRecipeId_descriptionSetToObservable() {
        // Arrange
        whenTextValidationValidateTitleAndDescription();
        // Act
        SUT.start(VALID_EXISTING_COMPLETE.getId());
        simulateGetValidExistingCompleteFromDatabase();
        // Assert
        assertEquals(VALID_EXISTING_COMPLETE.getDescription(), SUT.getDescription());
    }

    @Test
    public void startValidExistingRecipeId_recipeModelStatusVALID_UNCHANGED() {
        // Arrange
        whenTextValidationValidateTitleAndDescription();
        // Act
        SUT.start(VALID_EXISTING_COMPLETE.getId());
        // Assert
        simulateGetValidExistingCompleteFromDatabase();
        verify(modelValidationSubmitterMock, times((2))).submitModelStatus(statusArgumentCaptor.capture());
        RecipeModelStatus actualStatus = statusArgumentCaptor.getValue();
        assertEquals(VALID_UNCHANGED, actualStatus);
    }

    @Test
    public void startValidExistingRecipeId_invalidTitle_errorMessageSetToObservable() {
        // Arrange
        whenShortTextValidationReturnErrorMessage();
        whenLongTextValidationReturnValidated();
        // Act
        SUT.start(VALID_EXISTING_COMPLETE.getId());
        simulateGetValidExistingCompleteFromDatabase();
        SUT.setTitle(INVALID_EXISTING_INCOMPLETE_INVALID_TITLE.getTitle());
        // Assert
        assertEquals(SUT.titleErrorMessage.get(), SHORT_TEXT_VALIDATION_ERROR);
    }
    // startValidExistingRecipeId_invalidTitle_notSaved
    // startValidExistingRecipeId_invalidTitle_recipeModelStatusINVALID_CHANGED

    // startInvalidExistingRecipeId_recipeModelStatusINVALID_UNCHANGED

    @Test
    public void startWithCloned_validExistingAndNewRecipeId_databaseCalledWithExistingId() {
        // Arrange
        // Act
        SUT.startByCloningModel(
                VALID_FROM_ANOTHER_USER.getId(), INVALID_NEW_EMPTY.getId());
        // Assert
        verify(dataSourceMock).getById(eq(VALID_FROM_ANOTHER_USER.getId()), eq(SUT));
    }

    @Test
    public void startWithCloned_validExistingAndNewRecipeId_existingCopiedAndSavedWithNewId() {
        // Arrange
        whenTimeProviderThenReturnClonedTimes();
        whenShortTextValidationReturnValidated();
        whenLongTextValidationReturnValidated();
        // Act
        SUT.startByCloningModel(VALID_FROM_ANOTHER_USER.getId(), INVALID_NEW_EMPTY.getId());
        simulateGetValidEntityFromAnotherUserFromDatabase();
        // Assert
        verify(dataSourceMock).save(eq(VALID_NEW_CLONED));
    }

    @Test
    public void startWithCloned_validExistingAndNewRecipeIdValidDescriptionChanged_existingCopiedAndSavedWithUpdatedDescription() {
        // Arrange
        ArgumentCaptor<RecipeIdentityEntity> ac = ArgumentCaptor.forClass(RecipeIdentityEntity.class);
        whenTimeProviderThenReturnClonedTimes();
        whenShortTextValidationReturnValidated();
        whenLongTextValidationReturnValidated();
        // Act
        SUT.startByCloningModel(
                VALID_FROM_ANOTHER_USER.getId(), INVALID_NEW_EMPTY.getId());
        simulateGetValidEntityFromAnotherUserFromDatabase();
        SUT.setDescription(VALID_CLONED_DESCRIPTION_UPDATED.getDescription());
        // Assert
        verify(dataSourceMock, times((2))).save(ac.capture());
        assertEquals(VALID_CLONED_DESCRIPTION_UPDATED, ac.getAllValues().get(1));
    }

    @Test
    public void startNewRecipeId_titleValidDescriptionValid_newEntitySaved() {
        // Arrange
        whenShortTextValidationReturnValidated();
        whenLongTextValidationReturnValidated();
        whenTimeProviderThenReturnNewEntityCreateDate();
        // Act
        SUT.start(INVALID_NEW_EMPTY.getId());
        simulateNothingReturnedFromDatabase();
        SUT.setTitle(VALID_NEW_COMPLETE.getTitle());
        SUT.setDescription(VALID_NEW_COMPLETE.getDescription());
        // Assert
        verify(dataSourceMock).save(eq(VALID_NEW_COMPLETE));
    }

    @Test(expected = RuntimeException.class)
    public void start_nullRecipe_runtimeExceptionThrown() {
        // Arrange
        // Act
        SUT.start(null);
        // Assert
        ArrayList list = new ArrayList();
        RuntimeException exception = (RuntimeException) list.get(0);
        assertEquals(exception.getMessage(), "Recipe id cannot be null");
    }

    // region helper methods -------------------------------------------------------------------
    private void simulateNothingReturnedFromDatabase() {
        verify(dataSourceMock).getById(eq(INVALID_NEW_EMPTY.getId()),
                getEntityCallbackArgumentCaptor.capture());
        getEntityCallbackArgumentCaptor.getValue().onDataNotAvailable();
    }

    private void simulateGetValidExistingCompleteFromDatabase() {
        verify(dataSourceMock).getById(eq(
                VALID_EXISTING_COMPLETE.getId()),
                getEntityCallbackArgumentCaptor.capture());

        getEntityCallbackArgumentCaptor.getValue().onEntityLoaded(
                VALID_EXISTING_COMPLETE);
    }

    private void simulateGetValidEntityFromAnotherUserFromDatabase() {
        verify(dataSourceMock).getById(eq(
                VALID_FROM_ANOTHER_USER.getId()),
                getEntityCallbackArgumentCaptor.capture());

        getEntityCallbackArgumentCaptor.getValue().onEntityLoaded(
                VALID_FROM_ANOTHER_USER);
    }

    private void whenTextValidationValidateTitleAndDescription() {
        when(textValidationHandlerMock.validateShortText(
                eq(resourcesMock),
                eq(VALID_EXISTING_COMPLETE.getTitle()))).
                thenReturn(VALIDATED);

        when(textValidationHandlerMock.validateLongText(
                eq(resourcesMock),
                eq(VALID_EXISTING_COMPLETE.getDescription()))).
                thenReturn(VALIDATED);
    }

    private void setupResourceMockReturnValues() {
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

    private void whenTimeProviderThenReturnNewEntityCreateDate() {
        when(timeProviderMock.getCurrentTimeInMills()).thenReturn(
                INVALID_NEW_EMPTY.getCreateDate());
    }

    private void whenTimeProviderThenReturnExistingCreateDate() {
        when(timeProviderMock.getCurrentTimeInMills()).thenReturn(
                VALID_EXISTING_COMPLETE.getCreateDate());
    }

    private void whenTimeProviderThenReturnClonedTimes() {
        when(timeProviderMock.getCurrentTimeInMills()).thenReturn(
                VALID_NEW_CLONED.getLastUpdate());
    }
    // endregion helper methods --------------------------------------------------------------------

    // region helper classes -----------------------------------------------------------------------

    // endregion helper classes --------------------------------------------------------------------

}