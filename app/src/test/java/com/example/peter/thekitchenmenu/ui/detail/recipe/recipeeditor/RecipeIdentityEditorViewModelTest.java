package com.example.peter.thekitchenmenu.ui.detail.recipe.recipeeditor;

import android.content.res.Resources;

import com.example.peter.thekitchenmenu.R;
import com.example.peter.thekitchenmenu.commonmocks.StringMaker;
import com.example.peter.thekitchenmenu.commonmocks.UseCaseSchedulerMock;
import com.example.peter.thekitchenmenu.data.entity.RecipeIdentityEntity;
import com.example.peter.thekitchenmenu.data.repository.DataSource;
import com.example.peter.thekitchenmenu.data.repository.RepositoryRecipeIdentity;
import com.example.peter.thekitchenmenu.domain.UseCaseHandler;
import com.example.peter.thekitchenmenu.domain.usecase.recipeIdentity.UseCaseRecipeIdentity;
import com.example.peter.thekitchenmenu.domain.usecase.textvalidation.UseCaseTextValidator;
import com.example.peter.thekitchenmenu.testdata.TestDataRecipeIdentityEntity;
import com.example.peter.thekitchenmenu.testdata.TestDataRecipeValidator;
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

    private static final String ERROR_MESSAGE_TOO_LONG = "ERROR_MESSAGE_TOO_LONG";
    private static final String ERROR_MESSAGE_TOO_SHORT = "ERROR_MESSAGE_TOO_LONG";

    // endregion constants -------------------------------------------------------------------------

    // region helper fields ------------------------------------------------------------------------
    @Mock
    Resources resourcesMock;
    @Mock
    RepositoryRecipeIdentity repoMock;
    @Captor
    ArgumentCaptor<DataSource.GetEntityCallback<RecipeIdentityEntity>> repoCallback;
    @Mock
    TimeProvider timeProviderMock;
    @Mock
    RecipeValidation.RecipeValidatorModelSubmission modelValidationSubmitterMock;
    @Captor
    ArgumentCaptor<RecipeModelStatus> statusCaptor;

    private int shortTextMinLength = 3;
    private int shortTextMaxLength = 70;
    private int longTextMinLength = 0;
    private int longTextMaxLength = 500;

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

        UseCaseRecipeIdentity useCaseRecipeIdentity = new UseCaseRecipeIdentity(
                repoMock, timeProviderMock);

        UseCaseTextValidator useCaseTextValidator = new UseCaseTextValidator(
                shortTextMinLength, shortTextMaxLength, longTextMinLength, longTextMaxLength);

        return new RecipeIdentityEditorViewModel(
                handler,
                useCaseRecipeIdentity,
                useCaseTextValidator,
                resourcesMock);
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
        String recipeId = INVALID_NEW_EMPTY.getId();
        String invalidTitle = INVALID_NEW_TITLE_INVALID.getTitle();
        // Act
        SUT.start(recipeId);
        simulateNothingReturnedFromDatabase(recipeId);

        SUT.setTitle(invalidTitle);
        // Assert
        assertEquals(ERROR_MESSAGE_TOO_SHORT, SUT.titleErrorMessage.get());
    }

    @Test
    public void startNewRecipeId_invalidTitle_recipeModelStatusINVALID_CHANGED() {
        // Arrange
        String invalidTitle = INVALID_NEW_TITLE_INVALID.getTitle();

        String recipeId = INVALID_NEW_EMPTY.getId();
        // Act
        SUT.start(recipeId);
        simulateNothingReturnedFromDatabase(recipeId);

        SUT.setTitle(invalidTitle);
        // Assert
        verify(modelValidationSubmitterMock, times((2))).submitModelStatus(statusCaptor.capture());
        RecipeModelStatus actualStatus = statusCaptor.getValue();
        assertEquals(INVALID_CHANGED, actualStatus);
    }

    @Test
    public void startNewRecipeId_invalidTitleValidDescription_nothingSaved() {
        String recipeId = INVALID_NEW_EMPTY.getId();
        String invalidTitle = INVALID_NEW_TITLE_INVALID_DESCRIPTION_VALID.getTitle();
        String validDescription = INVALID_NEW_TITLE_INVALID_DESCRIPTION_VALID.getDescription();
        // Act
        SUT.start(recipeId);
        simulateNothingReturnedFromDatabase(recipeId);

        SUT.setTitle(invalidTitle);
        SUT.setDescription(validDescription);
        // Assert
        verifyNoMoreInteractions(repoMock);
    }

    @Test
    public void startNewRecipeId_validTitle_errorMessageObservableNull() {
        // Arrange
        String recipeId = INVALID_NEW_EMPTY.getId();
        String title = VALID_NEW_TITLE_VALID.getTitle();
        // Act
        SUT.start(recipeId);
        simulateNothingReturnedFromDatabase(recipeId);
        SUT.setTitle(title);
        // Assert
        assertNull(SUT.titleErrorMessage.get());
    }

    @Test
    public void startNewRecipeId_validTitle_titleSaved() {
        // Arrange
        String recipeId = INVALID_NEW_EMPTY.getId();
        String title = VALID_NEW_TITLE_VALID.getTitle();
        long time = VALID_NEW_TITLE_VALID.getCreateDate();
        when(timeProviderMock.getCurrentTimeInMills()).thenReturn(time);
        // Act
        SUT.start(recipeId);
        simulateNothingReturnedFromDatabase(recipeId);
        SUT.setTitle(title);
        // Assert
        verify(repoMock).save(VALID_NEW_TITLE_VALID);
    }

    @Test
    public void startNewRecipeId_validTitle_recipeModelStatusVALID_CHANGED() {
        // Arrange
        String recipeId = INVALID_NEW_EMPTY.getId();
        String title = VALID_NEW_TITLE_VALID.getTitle();
        // Act
        SUT.start(recipeId);
        simulateNothingReturnedFromDatabase(recipeId);

        SUT.setTitle(title);
        // Assert
        verify(modelValidationSubmitterMock, times((2))).submitModelStatus(statusCaptor.capture());
        RecipeModelStatus actualStatus = statusCaptor.getValue();
        assertEquals(VALID_CHANGED, actualStatus);
    }

    @Test
    public void startNewRecipeId_validTitleInvalidDescription_errorMessageSetToObservable() {
        // Arrange
        String recipeId = INVALID_NEW_EMPTY.getId();

        String title = new StringMaker().
                makeStringOfExactLength(shortTextMaxLength).
                build();

        String description = new StringMaker().
                makeStringOfExactLength(longTextMaxLength).
                addOneCharacter().
                build();
        // Act
        SUT.start(recipeId);
        simulateNothingReturnedFromDatabase(recipeId);

        SUT.setTitle(title);
        SUT.setDescription(description);
        // Assert
        assertEquals(ERROR_MESSAGE_TOO_LONG, SUT.descriptionErrorMessage.get());
    }

    @Test
    public void startNewRecipeId_validTitleInvalidDescription_descriptionNotSaved() {
        // Arrange
        String recipeId = INVALID_NEW_EMPTY.getId();

        String title = new StringMaker().
                makeStringOfExactLength(shortTextMaxLength).
                build();

        String description = new StringMaker().
                makeStringOfExactLength(longTextMaxLength).
                addOneCharacter().
                build();
        // Act
        SUT.start(recipeId);
        simulateNothingReturnedFromDatabase(recipeId);
        SUT.setTitle(title);
        verify(repoMock).save(anyObject());
        SUT.setDescription(description);
        verifyNoMoreInteractions(repoMock);
    }

    @Test
    public void startNewRecipeId_validTitleInvalidDescription_recipeModelStatusINVALID_CHANGED() {
        // Arrange
        String recipeId = INVALID_NEW_EMPTY.getId();

        String title = new StringMaker().
                makeStringOfExactLength(shortTextMaxLength).
                build();

        String description = new StringMaker().
                makeStringOfExactLength(longTextMaxLength).
                addOneCharacter().
                build();
        // Act
        SUT.start(recipeId);
        simulateNothingReturnedFromDatabase(recipeId);

        SUT.setTitle(title);
        SUT.setDescription(description);
        // Assert
        verify(modelValidationSubmitterMock, times((3))).submitModelStatus(statusCaptor.capture());
        RecipeModelStatus actualStatus = statusCaptor.getValue();
        assertEquals(INVALID_CHANGED, actualStatus);
    }

    @Test
    public void startNewRecipeId_validTitleValidDescription_errorMessageObservableNull() {
        // Arrange
        String recipeId = INVALID_NEW_EMPTY.getId();
        String title = VALID_NEW_COMPLETE.getTitle();
        String description = VALID_NEW_COMPLETE.getDescription();
        // Act
        SUT.start(recipeId);
        simulateNothingReturnedFromDatabase(recipeId);

        SUT.setTitle(title);
        SUT.setDescription(description);
        // Assert
        assertNull(SUT.titleErrorMessage.get());
        assertNull(SUT.descriptionErrorMessage.get());
    }

    @Test
    public void startNewRecipeId_validTitleValidDescription_saved() {
        // Arrange
        String recipeId = INVALID_NEW_EMPTY.getId();
        String validTitle = VALID_NEW_COMPLETE.getTitle();
        String validDescription = VALID_NEW_COMPLETE.getDescription();
        long time = VALID_NEW_COMPLETE.getCreateDate();
        whenTimeProviderReturnTime(time);

        // Act
        SUT.start(recipeId);
        simulateNothingReturnedFromDatabase(recipeId);

        // Assert
        SUT.setTitle(validTitle);
        verify(repoMock).save(eq(VALID_NEW_TITLE_VALID));
        SUT.setDescription(validDescription);
        verify(repoMock).save(eq(VALID_NEW_COMPLETE));
    }

    @Test
    public void startNewRecipeId_validTitleValidDescription_recipeModelStatusVALID_CHANGED() {
        // Arrange
        String recipeId = INVALID_NEW_EMPTY.getId();
        String validTitle = VALID_NEW_COMPLETE.getTitle();
        String validDescription = VALID_NEW_COMPLETE.getDescription();
        long time = VALID_NEW_COMPLETE.getCreateDate();
        whenTimeProviderReturnTime(time);
        // Act
        SUT.start(recipeId);
        simulateNothingReturnedFromDatabase(recipeId);

        SUT.setTitle(validTitle);
        SUT.setDescription(validDescription);
        // Assert
        verify(modelValidationSubmitterMock, times((3))).submitModelStatus(statusCaptor.capture());
        RecipeModelStatus actualStatus = statusCaptor.getValue();
        assertEquals(VALID_CHANGED, actualStatus);
    }

    @Test
    public void startValidExistingRecipeId_textValuesSetToObservables() {
        // Arrange
        String recipeId = VALID_EXISTING_COMPLETE.getId();
        String title = VALID_EXISTING_COMPLETE.getTitle();
        String description = VALID_EXISTING_COMPLETE.getDescription();
        // Act
        SUT.start(recipeId);
        verify(repoMock).getById(eq(recipeId), repoCallback.capture());
        repoCallback.getValue().onEntityLoaded(VALID_EXISTING_COMPLETE);
        // Assert
        assertEquals(title, SUT.getTitle());
        assertEquals(description, SUT.getDescription());
    }

    @Test
    public void startValidExistingRecipeId_recipeModelStatusVALID_UNCHANGED() {
        // Arrange
        String recipeId = VALID_EXISTING_COMPLETE.getId();
        // Act
        SUT.start(recipeId);
        verify(repoMock).getById(eq(recipeId), repoCallback.capture());
        repoCallback.getValue().onEntityLoaded(VALID_EXISTING_COMPLETE);
        // Assert
        verify(modelValidationSubmitterMock).submitModelStatus(statusCaptor.capture());
        RecipeModelStatus actualStatus = statusCaptor.getValue();
        assertEquals(VALID_UNCHANGED, actualStatus);
    }
//    @Test
//    public void startValidExistingRecipeId_invalidTitle_errorMessageSetToObservable() {
//        // Arrange
//        String recipeId = INVALID_EXISTING_INCOMPLETE_INVALID_TITLE.getId();
//        String title = INVALID_EXISTING_INCOMPLETE_INVALID_TITLE.getTitle();
//        String description = INVALID_EXISTING_INCOMPLETE_INVALID_TITLE.getDescription();
//
//        whenShortTextValidationReturnTooShortError(title);
//        whenLongTextValidationReturnValidated(description);
//
//        // Act
//        SUT.start(recipeId);
//        verify(repoMock).getById(eq(recipeId), repoCallback.capture());
//        repoCallback.getValue().onEntityLoaded(INVALID_EXISTING_INCOMPLETE_INVALID_TITLE);
//
//        // Assert
//        assertEquals(ERROR_MESSAGE_TOO_SHORT, SUT.titleErrorMessage.get());
//    }
    // startValidExistingRecipeId_invalidTitle_notSaved

    // startValidExistingRecipeId_invalidTitle_recipeModelStatusINVALID_CHANGED

    // startInvalidExistingRecipeId_recipeModelStatusINVALID_UNCHANGED

    @Test
    public void startWithCloned_existingAndNewId_persistenceCalledWithExistingId() {
        // Arrange
        String cloneFromRecipeId = VALID_FROM_ANOTHER_USER.getId();
        String cloneToRecipeId = INVALID_NEW_EMPTY.getId();
        // Act
        SUT.startByCloningModel(cloneFromRecipeId, cloneToRecipeId);
        // Assert
        verify(repoMock).getById(eq(cloneFromRecipeId), anyObject());
    }

    @Test
    public void startWithCloned_existingAndNewId_existingCopiedAndSavedWithNewId() {
        // Arrange
        String cloneFromRecipeId = VALID_FROM_ANOTHER_USER.getId();
        String cloneToRecipeId = INVALID_NEW_EMPTY.getId();
        when(timeProviderMock.getCurrentTimeInMills()).thenReturn(VALID_NEW_CLONED.getCreateDate());
        // Act
        SUT.startByCloningModel(cloneFromRecipeId, cloneToRecipeId);
        simulateGetValidFromAnotherUserFromDatabase();
        // Assert
        verify(repoMock).save(eq(VALID_NEW_CLONED));
    }

    @Test
    public void startWithCloned_validExistingIdAndNewId_descriptionUpdatedCopiedAndSavedWithUpdatedDescription() {
        // Arrange
        String cloneFromRecipeId = VALID_FROM_ANOTHER_USER.getId();
        String cloneToRecipeId = INVALID_NEW_EMPTY.getId();
        long time = VALID_CLONED_DESCRIPTION_UPDATED.getCreateDate();

        whenTimeProviderReturnTime(time);
        // Act
        SUT.startByCloningModel(cloneFromRecipeId, cloneToRecipeId);
        verify(repoMock).getById(eq(cloneFromRecipeId), repoCallback.capture());
        repoCallback.getValue().onEntityLoaded(VALID_FROM_ANOTHER_USER);

        SUT.setDescription(VALID_CLONED_DESCRIPTION_UPDATED.getDescription());
        // Assert
        verify(repoMock).save(VALID_CLONED_DESCRIPTION_UPDATED);
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
                thenReturn(ERROR_MESSAGE_TOO_SHORT);
        when(resourcesMock.getString(
                eq(R.string.input_error_text_too_long),
                anyString(),
                anyString())).
                thenReturn(ERROR_MESSAGE_TOO_LONG);
    }

    private void whenTimeProviderReturnTime(long time) {
        when(timeProviderMock.getCurrentTimeInMills()).thenReturn(time);
    }

    private void simulateNothingReturnedFromDatabase(String recipeId) {
        verify(repoMock).getById(eq(recipeId), repoCallback.capture());
        repoCallback.getValue().onDataNotAvailable();
    }

    private void simulateGetValidFromAnotherUserFromDatabase() {
        verify(repoMock).getById(eq(VALID_FROM_ANOTHER_USER.getId()), repoCallback.capture());
        repoCallback.getValue().onEntityLoaded(VALID_FROM_ANOTHER_USER);
    }
    // endregion helper methods --------------------------------------------------------------------

    // region helper classes -----------------------------------------------------------------------

    // endregion helper classes --------------------------------------------------------------------

}