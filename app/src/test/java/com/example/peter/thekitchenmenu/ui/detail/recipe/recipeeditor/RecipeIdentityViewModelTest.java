package com.example.peter.thekitchenmenu.ui.detail.recipe.recipeeditor;

import android.content.res.Resources;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;

import com.example.peter.thekitchenmenu.R;
import com.example.peter.thekitchenmenu.data.entity.RecipeIdentityEntity;
import com.example.peter.thekitchenmenu.data.repository.DataSource;
import com.example.peter.thekitchenmenu.testdata.RecipeIdentityEntityTestData;
import com.example.peter.thekitchenmenu.testdata.RecipeValidatorTestData;
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

import static org.junit.Assert.*;
import static org.mockito.Matchers.*;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class RecipeIdentityViewModelTest {

    // region constants ----------------------------------------------------------------------------
    private static final RecipeIdentityEntity INVALID_NEW_EMPTY =
            RecipeIdentityEntityTestData.getInvalidNewEmpty();
    private static final RecipeIdentityEntity INVALID_NEW_TITLE_INVALID =
            RecipeIdentityEntityTestData.getInvalidNewTitleUpdatedWithInvalidValue();
    private static final RecipeIdentityEntity INVALID_NEW_TITLE_INVALID_DESCRIPTION_VALID =
            RecipeIdentityEntityTestData.getInvalidNewTitleInvalidDescriptionValid();
    private static final RecipeIdentityEntity VALID_NEW_TITLE_VALID =
            RecipeIdentityEntityTestData.getValidNewTitleUpdatedWithValidValue();
    private static final RecipeIdentityEntity VALID_NEW_COMPLETE =
            RecipeIdentityEntityTestData.getValidNewComplete();
    private static final RecipeIdentityEntity INVALID_EXISTING_INCOMPLETE_INVALID_TITLE =
            RecipeIdentityEntityTestData.getInvalidExistingIncomplete();
    private static final RecipeIdentityEntity VALID_EXISTING_COMPLETE =
            RecipeIdentityEntityTestData.getValidExistingComplete();
    private static final RecipeIdentityEntity VALID_FROM_ANOTHER_USER =
            RecipeIdentityEntityTestData.getValidCompleteFromAnotherUser();
    private static final RecipeIdentityEntity INVALID_FROM_ANOTHER_USER =
            RecipeIdentityEntityTestData.getInvalidCompleteFromAnotherUser();
    private static final RecipeIdentityEntity VALID_NEW_CLONED =
            RecipeIdentityEntityTestData.getValidNewCloned();
    private static final RecipeIdentityEntity INVALID_NEW_CLONED =
            RecipeIdentityEntityTestData.getInvalidNewCloned();
    private static final RecipeIdentityEntity VALID_CLONED_DESCRIPTION_UPDATED =
            RecipeIdentityEntityTestData.getValidNewClonedDescriptionUpdatedComplete();

    private RecipeModelStatus INVALID_UNCHANGED =
            RecipeValidatorTestData.getIdentityModelStatusUnchangedInvalid();
    private RecipeModelStatus INVALID_CHANGED =
            RecipeValidatorTestData.getIdentityModelStatusChangedInvalid();
    private RecipeModelStatus VALID_UNCHANGED =
            RecipeValidatorTestData.getIdentityModelStatusUnchangedValid();
    private RecipeModelStatus VALID_CHANGED =
            RecipeValidatorTestData.getIdentityModelStatusChangedValid();

    private static final String ERROR_MESSAGE_TOO_LONG = "error_message_too_long";

    private static final String SHORT_TEXT_VALIDATION_ERROR = "short_text_validation_error";
    private static final String LONG_TEXT_VALIDATION_ERROR = "long_text_validation_error";

    // endregion constants -------------------------------------------------------------------------

    // region helper fields ------------------------------------------------------------------------
    @Rule
    public InstantTaskExecutorRule instantExecutorRule = new InstantTaskExecutorRule();
    @Mock
    Resources resourcesMock;
    @Mock
    TextValidationHandler textValidationHandlerMock;
    @Captor
    ArgumentCaptor<DataSource.GetEntityCallback<RecipeIdentityEntity>>
            getEntityCallbackArgumentCaptor;
    @Mock
    DataSource<RecipeIdentityEntity> dataSourceMock;
    @Mock
    TimeProvider timeProviderMock;
    @Mock
    RecipeValidation.RecipeValidatorModelSubmission modelValidationSubmitterMock;
    // endregion helper fields ---------------------------------------------------------------------

    private RecipeIdentityViewModel SUT;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        setupResourceMockReturnValues();

        SUT = new RecipeIdentityViewModel(
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
        assertEquals(INVALID_NEW_EMPTY.getTitle(), SUT.titleObservable.get());
        assertEquals(INVALID_NEW_EMPTY.getDescription(), SUT.descriptionObservable.get());
        assertNull(SUT.titleErrorMessage.get());
        assertNull(SUT.descriptionErrorMessage.get());
    }

    @Test
    public void startNewRecipeId_newEmptyEntityNotSaved() {
        // Arrange
        when(timeProviderMock.getCurrentTimestamp()).thenReturn(INVALID_NEW_EMPTY.getCreateDate());
        // Act
        SUT.start(INVALID_NEW_EMPTY.getId());
        simulateNothingReturnedFromDatabase();
        // Assert
        verifyNoMoreInteractions(dataSourceMock);
    }

    @Test
    public void startNewRecipeId_RecipeModelStatusINVALID_UNCHANGED() {
        // Arrange
        ArgumentCaptor<RecipeModelStatus> ac = ArgumentCaptor.forClass(RecipeModelStatus.class);
        // Act
        SUT.start(INVALID_NEW_EMPTY.getId());
        simulateNothingReturnedFromDatabase();
        // Assert
        verify(modelValidationSubmitterMock).submitModelStatus(ac.capture());
        RecipeModelStatus modelStatus = ac.getValue();
        assertEquals(INVALID_UNCHANGED, modelStatus);
    }

    @Test
    public void startNewRecipeId_invalidTitle_errorMessageSetToObservable() {
        // Arrange
        whenShortTextValidationReturnErrorMessage();
        // Act
        SUT.start(INVALID_NEW_EMPTY.getId());
        simulateNothingReturnedFromDatabase();
        SUT.titleObservable.set(INVALID_NEW_TITLE_INVALID.getTitle());
        // Assert
        assertEquals(SHORT_TEXT_VALIDATION_ERROR, SUT.titleErrorMessage.get());
    }

    @Test
    public void startNewRecipeId_invalidTitle_recipeModelStatusINVALID_CHANGED() {
        // Arrange
        ArgumentCaptor<RecipeModelStatus> ac = ArgumentCaptor.forClass(RecipeModelStatus.class);
        whenShortTextValidationReturnErrorMessage();
        // Act
        SUT.start(INVALID_NEW_EMPTY.getId());
        simulateNothingReturnedFromDatabase();
        SUT.titleObservable.set(INVALID_NEW_TITLE_INVALID.getTitle());
        // Assert
        verify(modelValidationSubmitterMock, times(2)).
                submitModelStatus(ac.capture());
        RecipeModelStatus modelStatus = ac.getValue();
        assertEquals(INVALID_CHANGED, modelStatus);
    }

    @Test
    public void startNewRecipeId_invalidTitleValidDescription_emptyEntitySaved() {
        whenShortTextValidationReturnErrorMessage();
        whenLongTextValidationReturnValidated();
        whenTimeProviderThenReturnNewEntityCreateDate();
        // Act
        SUT.start(INVALID_NEW_EMPTY.getId());
        simulateNothingReturnedFromDatabase();
        SUT.titleObservable.set(INVALID_NEW_TITLE_INVALID_DESCRIPTION_VALID.getTitle());
        SUT.descriptionObservable.set(INVALID_NEW_TITLE_INVALID_DESCRIPTION_VALID.getDescription());
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
        SUT.titleObservable.set(VALID_NEW_TITLE_VALID.getTitle());
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
        SUT.titleObservable.set(VALID_NEW_TITLE_VALID.getTitle());
        // Assert
        verify(dataSourceMock).save(eq(VALID_NEW_TITLE_VALID));
    }

    @Test
    public void startNewRecipeId_validTitle_recipeModelStatusVALID_CHANGED() {
        // Arrange
        ArgumentCaptor<RecipeModelStatus> ac = ArgumentCaptor.forClass(RecipeModelStatus.class);
        whenShortTextValidationReturnValidated();
        // Act
        SUT.start(INVALID_NEW_EMPTY.getId());
        simulateNothingReturnedFromDatabase();
        SUT.titleObservable.set(VALID_NEW_TITLE_VALID.getTitle());
        // Assert
        verify(modelValidationSubmitterMock, times(2)).
                submitModelStatus(ac.capture());
        RecipeModelStatus modelStatus = ac.getValue();
        assertEquals(VALID_CHANGED, modelStatus);
    }

    @Test
    public void startNewRecipeId_validTitleInvalidDescription_errorMessageSetToObservable() {
        // Arrange
        whenShortTextValidationReturnValidated();
        whenLongTextValidationReturnErrorMessage();
        // Act
        SUT.start(INVALID_NEW_EMPTY.getId());
        simulateNothingReturnedFromDatabase();
        SUT.titleObservable.set(VALID_NEW_TITLE_VALID.getTitle());
        SUT.descriptionObservable.set("Doesn't matter what is here as returning an error message!");
        // Assert
        assertEquals(LONG_TEXT_VALIDATION_ERROR, SUT.descriptionErrorMessage.get());
    }

    @Test
    public void startNewRecipeId_validTitleInvalidDescription_descriptionNotSaved() {
        // Arrange
        when(timeProviderMock.getCurrentTimestamp()).
                thenReturn(VALID_NEW_TITLE_VALID.getCreateDate());
        whenShortTextValidationReturnValidated();
        whenLongTextValidationReturnErrorMessage();
        // Act
        SUT.start(INVALID_NEW_EMPTY.getId());
        simulateNothingReturnedFromDatabase();
        SUT.titleObservable.set(VALID_NEW_TITLE_VALID.getTitle());
        SUT.descriptionObservable.set("Doesn't matter what is here as returning an error message!");
        verify(dataSourceMock).save(eq(VALID_NEW_TITLE_VALID));
    }

    @Test
    public void startNewRecipeId_validTitleInvalidDescription_recipeModelStatusINVALID_CHANGED() {
        // Arrange
        ArgumentCaptor<RecipeModelStatus> ac = ArgumentCaptor.forClass(RecipeModelStatus.class);
        whenShortTextValidationReturnValidated();
        whenLongTextValidationReturnErrorMessage();
        // Act
        SUT.start(INVALID_NEW_EMPTY.getId());
        simulateNothingReturnedFromDatabase();
        SUT.titleObservable.set(VALID_NEW_TITLE_VALID.getTitle());
        SUT.descriptionObservable.set("Doesn't matter what is here as returning an error message!");
        // Assert
        verify(modelValidationSubmitterMock, times(3)).
                submitModelStatus(ac.capture());
        RecipeModelStatus modelStatus = ac.getValue();
        assertEquals(INVALID_CHANGED, modelStatus);
    }

    @Test
    public void startNewRecipeId_validTitleValidDescription_errorMessageObservableNull() {
        // Arrange
        whenShortTextValidationReturnValidated();
        whenLongTextValidationReturnValidated();
        // Act
        SUT.start(INVALID_NEW_EMPTY.getId());
        simulateNothingReturnedFromDatabase();
        SUT.titleObservable.set(VALID_NEW_COMPLETE.getTitle());
        SUT.descriptionObservable.set(VALID_NEW_COMPLETE.getDescription());
        // Assert
        assertNull(SUT.titleErrorMessage.get());
        assertNull(SUT.descriptionErrorMessage.get());
    }

    @Test
    public void startNewRecipeId_validTitleValidDescription_saved() {
        // Arrange
        ArgumentCaptor<RecipeIdentityEntity> ac = ArgumentCaptor.forClass(RecipeIdentityEntity.class);
        whenShortTextValidationReturnValidated();
        whenLongTextValidationReturnValidated();
        when(timeProviderMock.getCurrentTimestamp()).thenReturn(VALID_NEW_COMPLETE.getCreateDate());
        // Act
        SUT.start(INVALID_NEW_EMPTY.getId());
        simulateNothingReturnedFromDatabase();
        SUT.titleObservable.set(VALID_NEW_COMPLETE.getTitle());
        SUT.descriptionObservable.set(VALID_NEW_COMPLETE.getDescription());
        // Assert
        verify(dataSourceMock, times(1)).save(eq(VALID_NEW_COMPLETE));
    }

    @Test
    public void startNewRecipeId_validTitleValidDescription_recipeModelStatusVALID_CHANGED() {
        // Arrange
        ArgumentCaptor<RecipeModelStatus> ac = ArgumentCaptor.forClass(RecipeModelStatus.class);
        whenShortTextValidationReturnValidated();
        whenLongTextValidationReturnValidated();
        // Act
        SUT.start(INVALID_NEW_EMPTY.getId());
        simulateNothingReturnedFromDatabase();
        SUT.titleObservable.set(VALID_NEW_COMPLETE.getTitle());
        SUT.descriptionObservable.set(VALID_NEW_COMPLETE.getDescription());
        // Assert
        verify(modelValidationSubmitterMock, times(3)).
                submitModelStatus(ac.capture());
        RecipeModelStatus modelStatus = ac.getValue();
        assertEquals(VALID_CHANGED, modelStatus);
    }

    @Test
    public void startValidExistingRecipeId_titleSetToObservable() {
        // Arrange
        whenTextValidationValidateTitleAndDescription();
        // Act
        SUT.start(VALID_EXISTING_COMPLETE.getId());
        simulateGetValidExistingCompleteFromDatabase();
        // Assert
        assertEquals(VALID_EXISTING_COMPLETE.getTitle(), SUT.titleObservable.get());
    }

    @Test
    public void startValidExistingRecipeId_descriptionSetToObservable() {
        // Arrange
        whenTextValidationValidateTitleAndDescription();
        // Act
        SUT.start(VALID_EXISTING_COMPLETE.getId());
        simulateGetValidExistingCompleteFromDatabase();
        // Assert
        assertEquals(VALID_EXISTING_COMPLETE.getDescription(), SUT.descriptionObservable.get());
    }

    @Test
    public void startValidExistingRecipeId_recipeModelStatusVALID_UNCHANGED() {
        // Arrange
        ArgumentCaptor<RecipeModelStatus> ac = ArgumentCaptor.forClass(RecipeModelStatus.class);
        whenTextValidationValidateTitleAndDescription();
//        whenIntFromObserverMockReturnExistingValidCompleteRecipeTimes();
        // Act
        SUT.start(VALID_EXISTING_COMPLETE.getId());
        // Assert
        simulateGetValidExistingCompleteFromDatabase();
        verify(modelValidationSubmitterMock).submitModelStatus(ac.capture());
        RecipeModelStatus modelStatus = ac.getValue();
        assertEquals(VALID_UNCHANGED, modelStatus);
    }

    @Test
    public void startValidExistingRecipeId_invalidTitle_errorMessageSetToObservable() {
        // Arrange
        whenShortTextValidationReturnErrorMessage();
        whenLongTextValidationReturnValidated();
        // Act
        SUT.start(VALID_EXISTING_COMPLETE.getId());
        simulateGetValidExistingCompleteFromDatabase();
        SUT.titleObservable.set(INVALID_EXISTING_INCOMPLETE_INVALID_TITLE.getTitle());
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
        SUT.startByCloningModel(
                VALID_FROM_ANOTHER_USER.getId(), INVALID_NEW_EMPTY.getId());
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
        SUT.descriptionObservable.set(VALID_CLONED_DESCRIPTION_UPDATED.getDescription());
        // Assert
        verify(dataSourceMock, times(2)).save(ac.capture());
        assertEquals(VALID_CLONED_DESCRIPTION_UPDATED, ac.getAllValues().get(1));
    }

    @Test
    public void startNewRecipeId_titleValidDescriptionValid_newEntitySaved() {
        // Arrange
        ArgumentCaptor<RecipeIdentityEntity> ac = ArgumentCaptor.forClass(RecipeIdentityEntity.class);
        whenShortTextValidationReturnValidated();
        whenLongTextValidationReturnValidated();
        whenTimeProviderThenReturnNewEntityCreateDate();
        // Act
        SUT.start(INVALID_NEW_EMPTY.getId());
        simulateNothingReturnedFromDatabase();
        SUT.titleObservable.set(VALID_NEW_COMPLETE.getTitle());
        SUT.descriptionObservable.set(VALID_NEW_COMPLETE.getDescription());
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
                thenReturn(TextValidationHandler.VALIDATED);

        when(textValidationHandlerMock.validateLongText(
                eq(resourcesMock),
                eq(VALID_EXISTING_COMPLETE.getDescription()))).
                thenReturn(TextValidationHandler.VALIDATED);
    }

    private void setupResourceMockReturnValues() {
        when(resourcesMock.getString(R.string.input_error_recipe_prep_time_too_long)).thenReturn(ERROR_MESSAGE_TOO_LONG);
        when(resourcesMock.getString(R.string.input_error_recipe_cook_time_too_long)).thenReturn(ERROR_MESSAGE_TOO_LONG);
    }

    private void whenShortTextValidationReturnValidated() {
        when(textValidationHandlerMock.validateShortText(anyObject(), anyString())).
                thenReturn(TextValidationHandler.VALIDATED);
    }

    private void whenShortTextValidationReturnErrorMessage() {
        when(textValidationHandlerMock.validateShortText(anyObject(), anyString())).
                thenReturn(SHORT_TEXT_VALIDATION_ERROR);
    }

    private void whenLongTextValidationReturnValidated() {
        when(textValidationHandlerMock.validateLongText(anyObject(), anyString())).
                thenReturn(TextValidationHandler.VALIDATED);
    }

    private void whenLongTextValidationReturnErrorMessage() {
        when(textValidationHandlerMock.validateLongText(anyObject(), anyString())).
                thenReturn(LONG_TEXT_VALIDATION_ERROR);
    }

    private void whenTimeProviderThenReturnNewEntityCreateDate() {
        when(timeProviderMock.getCurrentTimestamp()).thenReturn(
                INVALID_NEW_EMPTY.getCreateDate());
    }

    private void whenTimeProviderThenReturnExistingCreateDate() {
        when(timeProviderMock.getCurrentTimestamp()).thenReturn(
                VALID_EXISTING_COMPLETE.getCreateDate());
    }

    private void whenTimeProviderThenReturnClonedTimes() {
        when(timeProviderMock.getCurrentTimestamp()).thenReturn(
                VALID_NEW_CLONED.getLastUpdate());
    }
    // endregion helper methods --------------------------------------------------------------------

    // region helper classes -----------------------------------------------------------------------

    // endregion helper classes --------------------------------------------------------------------

}