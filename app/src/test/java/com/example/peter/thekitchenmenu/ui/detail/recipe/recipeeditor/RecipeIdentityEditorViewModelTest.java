package com.example.peter.thekitchenmenu.ui.detail.recipe.recipeeditor;

import android.content.res.Resources;

import com.example.peter.thekitchenmenu.R;
import com.example.peter.thekitchenmenu.commonmocks.UseCaseSchedulerMock;
import com.example.peter.thekitchenmenu.data.entity.RecipeIdentityEntity;
import com.example.peter.thekitchenmenu.data.repository.DataSource;
import com.example.peter.thekitchenmenu.data.repository.RepositoryRecipeIdentity;
import com.example.peter.thekitchenmenu.domain.UseCaseHandler;
import com.example.peter.thekitchenmenu.domain.usecase.recipeIdentity.UseCaseRecipeIdentity;
import com.example.peter.thekitchenmenu.testdata.TestDataRecipeIdentityEntity;
import com.example.peter.thekitchenmenu.testdata.TestDataRecipeValidator;
import com.example.peter.thekitchenmenu.ui.utils.TextValidator;
import com.example.peter.thekitchenmenu.utils.TimeProvider;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;

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

    private static final String SHORT_TEXT_MIN_LENGTH = "3";
    private static final String SHORT_TEXT_MAX_LENGTH = "70";
    private static final String LONG_TEXT_MIN_LENGTH = "0";
    private static final String LONG_TEXT_MAX_LENGTH = "2900";
    private static final String ERROR_MESSAGE_TOO_LONG = "error_message_too_long";
    private static final String SHORT_TEXT_VALIDATION_ERROR = "short_text_validation_error";
    private static final String LONG_TEXT_VALIDATION_ERROR = "long_text_validation_error";

    // endregion constants -------------------------------------------------------------------------

    // region helper fields ------------------------------------------------------------------------
    @Mock
    Resources resourcesMock;
    @Mock
    TextValidator textValidatorMock;
    @Mock
    RepositoryRecipeIdentity repoMock;
    @Captor
    ArgumentCaptor<DataSource.GetEntityCallback<RecipeIdentityEntity>> identityCallbackCaptor;
    @Mock
    TimeProvider timeProviderMock;
    @Mock
    RecipeValidation.RecipeValidatorModelSubmission modelValidationSubmitterMock;
    @Captor
    ArgumentCaptor<RecipeModelStatus> statusCaptor;

    // endregion helper fields ---------------------------------------------------------------------

    private RecipeIdentityEditorViewModel SUT;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        setupResources();

        SUT = givenViewModel();
        SUT.setModelValidationSubmitter(modelValidationSubmitterMock);
    }

    private RecipeIdentityEditorViewModel givenViewModel() {
        UseCaseHandler handler = new UseCaseHandler(new UseCaseSchedulerMock());
        UseCaseRecipeIdentity useCase = new UseCaseRecipeIdentity(repoMock, timeProviderMock);

        return new RecipeIdentityEditorViewModel(handler, useCase, resourcesMock, textValidatorMock);
    }

    @Test
    public void startNewRecipeId_nothingSetToObservers() {
        // Arrange
        String recipeId = INVALID_NEW_EMPTY.getId();
        String expectedTitle = INVALID_NEW_EMPTY.getTitle();
        String expectedDescription = INVALID_NEW_EMPTY.getDescription();
        // Act
        SUT.start(recipeId);
        // Assert
        simulateNothingReturnedFromDatabase(recipeId);

        assertEquals(expectedTitle, SUT.getTitle());
        assertEquals(expectedDescription, SUT.getDescription());
        assertNull(SUT.titleErrorMessage.get());
        assertNull(SUT.descriptionErrorMessage.get());
    }

    @Test
    public void startNewRecipeId_RecipeModelStatusINVALID_UNCHANGED() {
        // Arrange
        String recipeId = INVALID_NEW_EMPTY.getId();
        // Act
        SUT.start(recipeId);
        simulateNothingReturnedFromDatabase(recipeId);
        // Assert
        verify(modelValidationSubmitterMock).submitModelStatus(statusCaptor.capture());
        RecipeModelStatus actualStatus = statusCaptor.getValue();
        assertEquals(INVALID_UNCHANGED, actualStatus);
    }

    @Test
    public void startNewRecipeId_invalidTitle_errorMessageSetToObservable() {
        // Arrange
        String invalidTitle = INVALID_NEW_TITLE_INVALID.getTitle();
        String minLength = "3";
        String maxLength = "70";

        TextValidator.Request request = new TextValidator.Request(
                TextValidator.RequestType.SHORT_TEXT,
                invalidTitle);
        TextValidator.Response response = new TextValidator.Response(
                TextValidator.Result.TOO_SHORT, invalidTitle, minLength, maxLength);

        when(textValidatorMock.validateText(request)).thenReturn(response);
        String recipeId = INVALID_NEW_EMPTY.getId();
        // Act
        SUT.start(recipeId);
        simulateNothingReturnedFromDatabase(recipeId);

        SUT.setTitle(INVALID_NEW_TITLE_INVALID.getTitle());
        // Assert
        assertEquals(SHORT_TEXT_VALIDATION_ERROR, SUT.titleErrorMessage.get());
    }

    private void simulateNothingReturnedFromDatabase(String recipeId) {
        verify(repoMock).getById(eq(recipeId), identityCallbackCaptor.capture());
        identityCallbackCaptor.getValue().onDataNotAvailable();
    }

    @Test
    public void startNewRecipeId_invalidTitle_recipeModelStatusINVALID_CHANGED() {
        // Arrange
        whenShortTextValidationReturnTooShortError("title");
        // Act
        SUT.start(INVALID_NEW_EMPTY.getId());
        SUT.setTitle(INVALID_NEW_TITLE_INVALID.getTitle());
        // Assert
        verify(modelValidationSubmitterMock, times((2))).submitModelStatus(
                statusCaptor.capture());
        RecipeModelStatus actualStatus = statusCaptor.getValue();
        assertEquals(INVALID_CHANGED, actualStatus);
    }

    @Test
    public void startNewRecipeId_invalidTitleValidDescription_emptyEntitySaved() {
        whenShortTextValidationReturnTooShortError("title");
        whenLongTextValidationReturnValidated("description");
        // Act
        SUT.start(INVALID_NEW_EMPTY.getId());
        SUT.setTitle(INVALID_NEW_TITLE_INVALID_DESCRIPTION_VALID.getTitle());
        SUT.setDescription(INVALID_NEW_TITLE_INVALID_DESCRIPTION_VALID.getDescription());
        // Assert
    }

    @Test
    public void startNewRecipeId_validTitle_errorMessageObservableNull() {
        // Arrange
        whenShortTextValidationReturnValidated("title");
        // Act
        SUT.start(INVALID_NEW_EMPTY.getId());
        SUT.setTitle(VALID_NEW_TITLE_VALID.getTitle());
        // Assert
        assertNull(SUT.titleErrorMessage.get());
    }

    @Test
    public void startNewRecipeId_validTitle_titleSaved() {
        // Arrange
        whenShortTextValidationReturnValidated("title");
        // Act
        SUT.start(INVALID_NEW_EMPTY.getId());
        SUT.setTitle(VALID_NEW_TITLE_VALID.getTitle());
        // Assert
    }

    @Test
    public void startNewRecipeId_validTitle_recipeModelStatusVALID_CHANGED() {
        // Arrange
        whenShortTextValidationReturnValidated("title");
        whenLongTextValidationReturnValidated("description");
        // Act
        SUT.start(INVALID_NEW_EMPTY.getId());
        SUT.setTitle(VALID_NEW_TITLE_VALID.getTitle());
        // Assert
        verify(modelValidationSubmitterMock, times((2))).
                submitModelStatus(statusCaptor.capture());
        RecipeModelStatus actualStatus = statusCaptor.getValue();
        assertEquals(VALID_CHANGED, actualStatus);
    }

    @Test
    public void startNewRecipeId_validTitleInvalidDescription_errorMessageSetToObservable() {
        // Arrange
        whenShortTextValidationReturnValidated("title");
        whenLongTextValidationReturnTooLongError("description");
        // Act
        SUT.start(INVALID_NEW_EMPTY.getId());
        SUT.setTitle(VALID_NEW_TITLE_VALID.getTitle());
        SUT.setDescription("Doesn't matter what is here as returning an error message!");
        // Assert
        assertEquals(LONG_TEXT_VALIDATION_ERROR, SUT.descriptionErrorMessage.get());
    }

    @Test
    public void startNewRecipeId_validTitleInvalidDescription_descriptionNotSaved() {
        // Arrange
        whenShortTextValidationReturnValidated("title");
        whenLongTextValidationReturnTooLongError("description");
        // Act
        SUT.start(INVALID_NEW_EMPTY.getId());
        SUT.setTitle(VALID_NEW_TITLE_VALID.getTitle());
        SUT.setDescription("Doesn't matter what is here as returning an error message!");
    }

    @Test
    public void startNewRecipeId_validTitleInvalidDescription_recipeModelStatusINVALID_CHANGED() {
        // Arrange
        whenShortTextValidationReturnValidated("title");
        whenLongTextValidationReturnTooLongError("description");
        // Act
        SUT.start(INVALID_NEW_EMPTY.getId());
        SUT.setTitle(VALID_NEW_TITLE_VALID.getTitle());
        SUT.setDescription("Doesn't matter what is here as returning an error message!");
        // Assert
        verify(modelValidationSubmitterMock, times((3))).submitModelStatus(
                statusCaptor.capture());
        RecipeModelStatus actualStatus = statusCaptor.getValue();
        assertEquals(INVALID_CHANGED, actualStatus);
    }

    @Test
    public void startNewRecipeId_validTitleValidDescription_errorMessageObservableNull() {
        // Arrange
        whenShortTextValidationReturnValidated("title");
        whenLongTextValidationReturnValidated("description");
        // Act
        SUT.start(INVALID_NEW_EMPTY.getId());
        SUT.setTitle(VALID_NEW_COMPLETE.getTitle());
        SUT.setDescription(VALID_NEW_COMPLETE.getDescription());
        // Assert
        assertNull(SUT.titleErrorMessage.get());
        assertNull(SUT.descriptionErrorMessage.get());
    }

    @Test
    public void startNewRecipeId_validTitleValidDescription_saved() {
        // Arrange
        whenShortTextValidationReturnValidated("title");
        whenLongTextValidationReturnValidated("description");
        // Act
        SUT.start(INVALID_NEW_EMPTY.getId());
        SUT.setTitle(VALID_NEW_COMPLETE.getTitle());
        SUT.setDescription(VALID_NEW_COMPLETE.getDescription());
        // Assert
    }

    @Test
    public void startNewRecipeId_validTitleValidDescription_recipeModelStatusVALID_CHANGED() {
        // Arrange
        whenShortTextValidationReturnValidated("title");
        whenLongTextValidationReturnValidated("description");
        // Act
        SUT.start(INVALID_NEW_EMPTY.getId());
        SUT.setTitle(VALID_NEW_COMPLETE.getTitle());
        SUT.setDescription(VALID_NEW_COMPLETE.getDescription());
        // Assert
        verify(modelValidationSubmitterMock, times((3))).submitModelStatus(
                statusCaptor.capture());
        RecipeModelStatus actualStatus = statusCaptor.getValue();
        assertEquals(VALID_CHANGED, actualStatus);
    }

    @Test
    public void startValidExistingRecipeId_titleSetToObservable() {
        // Arrange
        whenShortTextValidationReturnValidated("title");
        whenLongTextValidationReturnValidated("description");
        // Act
        SUT.start(VALID_EXISTING_COMPLETE.getId());
        // Assert
        assertEquals(VALID_EXISTING_COMPLETE.getTitle(), SUT.getTitle());
    }

    @Test
    public void startValidExistingRecipeId_descriptionSetToObservable() {
        // Arrange
        whenShortTextValidationReturnValidated("title");
        whenLongTextValidationReturnValidated("description");
        // Act
        SUT.start(VALID_EXISTING_COMPLETE.getId());
        // Assert
        assertEquals(VALID_EXISTING_COMPLETE.getDescription(), SUT.getDescription());
    }

    @Test
    public void startValidExistingRecipeId_recipeModelStatusVALID_UNCHANGED() {
        // Arrange
        whenShortTextValidationReturnValidated("title");
        whenLongTextValidationReturnValidated("description");
        // Act
        SUT.start(VALID_EXISTING_COMPLETE.getId());
        // Assert
        verify(modelValidationSubmitterMock, times((2))).submitModelStatus(statusCaptor.capture());
        RecipeModelStatus actualStatus = statusCaptor.getValue();
        assertEquals(VALID_UNCHANGED, actualStatus);
    }

    @Test
    public void startValidExistingRecipeId_invalidTitle_errorMessageSetToObservable() {
        // Arrange
        whenShortTextValidationReturnValidated("title");
        whenLongTextValidationReturnValidated("description");
        // Act
        SUT.start(VALID_EXISTING_COMPLETE.getId());
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
    }

    @Test
    public void startWithCloned_validExistingAndNewRecipeId_existingCopiedAndSavedWithNewId() {
        // Arrange
        whenShortTextValidationReturnValidated("title");
        whenLongTextValidationReturnValidated("description");
        // Act
        SUT.startByCloningModel(VALID_FROM_ANOTHER_USER.getId(), INVALID_NEW_EMPTY.getId());
        // Assert
    }

    @Test
    public void startWithCloned_validExistingAndNewRecipeIdValidDescriptionChanged_existingCopiedAndSavedWithUpdatedDescription() {
        // Arrange
        ArgumentCaptor<RecipeIdentityEntity> ac = ArgumentCaptor.forClass(RecipeIdentityEntity.class);
        whenShortTextValidationReturnValidated("title");
        whenLongTextValidationReturnValidated("description");
        // Act
        SUT.startByCloningModel(VALID_FROM_ANOTHER_USER.getId(), INVALID_NEW_EMPTY.getId());
        SUT.setDescription(VALID_CLONED_DESCRIPTION_UPDATED.getDescription());
        // Assert
        assertEquals(VALID_CLONED_DESCRIPTION_UPDATED, ac.getAllValues().get(1));
    }

    @Test
    public void startNewRecipeId_titleValidDescriptionValid_newEntitySaved() {
        // Arrange
        whenShortTextValidationReturnValidated("title");
        whenLongTextValidationReturnValidated("description");
        // Act
        SUT.start(INVALID_NEW_EMPTY.getId());
        SUT.setTitle(VALID_NEW_COMPLETE.getTitle());
        SUT.setDescription(VALID_NEW_COMPLETE.getDescription());
        // Assert
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


    private void setupResources() {
        when(resourcesMock.getString(
                eq(R.string.input_error_text_too_short),
                anyString(),
                anyString())).
                thenReturn(SHORT_TEXT_VALIDATION_ERROR);
        when(resourcesMock.getString(
                eq(R.string.input_error_text_too_long),
                anyString(),
                anyString())).
                thenReturn(SHORT_TEXT_VALIDATION_ERROR);
    }

    private void whenShortTextValidationReturnValidated(String title) {
        when(textValidatorMock.validateText(eq(getShortTextRequest(title)))).
                thenReturn(getShortTextValidResponse(title));
    }

    private void whenShortTextValidationReturnTooShortError(String title) {
        when(textValidatorMock.validateText(eq(getShortTextRequest(title)))).
                thenReturn(getShortTextTooShortResponse(title));
    }

    private void whenShortTextValidationReturnTooLongError(String title) {
        when(textValidatorMock.validateText(eq(getShortTextRequest(title)))).
                thenReturn(getShortTextTooLongResponse(title));
    }

    private TextValidator.Request getShortTextRequest(String title) {
        return new TextValidator.Request(TextValidator.RequestType.SHORT_TEXT, title);
    }

    private TextValidator.Response getShortTextValidResponse(String title) {
        return new TextValidator.Response(TextValidator.Result.VALID, title,
                SHORT_TEXT_MIN_LENGTH, SHORT_TEXT_MAX_LENGTH);
    }

    private TextValidator.Response getShortTextTooShortResponse(String title) {
        return new TextValidator.Response(TextValidator.Result.TOO_SHORT, title,
                SHORT_TEXT_MIN_LENGTH, SHORT_TEXT_MAX_LENGTH);
    }

    private TextValidator.Response getShortTextTooLongResponse(String title) {
        return new TextValidator.Response(TextValidator.Result.TOO_LONG, title,
                SHORT_TEXT_MIN_LENGTH, SHORT_TEXT_MAX_LENGTH);
    }

    private void whenLongTextValidationReturnValidated(String description) {
        when(textValidatorMock.validateText(getLongTextRequest(description))).
                thenReturn(getLongTextValidResponse(description));
    }

    private void whenLongTextValidationReturnTooLongError(String description) {
        when(textValidatorMock.validateText(getLongTextRequest(description))).
                thenReturn(getLongTextTooLongValidationResponse(description));
    }

    private TextValidator.Request getLongTextRequest(String description) {
        return new TextValidator.Request(TextValidator.RequestType.LONG_TEXT, description);
    }

    private TextValidator.Response getLongTextValidResponse(String description) {
        return new TextValidator.Response(TextValidator.Result.VALID, description,
                LONG_TEXT_MIN_LENGTH, LONG_TEXT_MAX_LENGTH);
    }

    private TextValidator.Response getLongTextTooLongValidationResponse(String description) {
        return new TextValidator.Response(TextValidator.Result.TOO_LONG, description,
                LONG_TEXT_MIN_LENGTH, LONG_TEXT_MAX_LENGTH);
    }
    // endregion helper methods --------------------------------------------------------------------

    // region helper classes -----------------------------------------------------------------------

    // endregion helper classes --------------------------------------------------------------------

}