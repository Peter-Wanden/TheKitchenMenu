package com.example.peter.thekitchenmenu.ui.detail.recipe.recipeeditor;

import android.content.res.Resources;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;

import com.example.peter.thekitchenmenu.R;
import com.example.peter.thekitchenmenu.data.entity.RecipeDurationEntity;
import com.example.peter.thekitchenmenu.data.repository.DataSource;
import com.example.peter.thekitchenmenu.data.repository.RepositoryRecipeDuration;
import com.example.peter.thekitchenmenu.testdata.RecipeDurationEntityTestData;
import com.example.peter.thekitchenmenu.testdata.RecipeValidatorTestData;
import com.example.peter.thekitchenmenu.utils.TimeProvider;

import org.junit.*;
import org.mockito.*;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.*;

public class RecipeDurationEditorViewModelTest {

    // region constants ----------------------------------------------------------------------------
    private static final RecipeDurationEntity VALID_NEW_EMPTY =
            RecipeDurationEntityTestData.getValidNewEmpty();
    private static final RecipeDurationEntity INVALID_NEW_PREP_TIME_INVALID =
            RecipeDurationEntityTestData.getInvalidNewPrepTimeInvalid();
    private static final RecipeDurationEntity INVALID_NEW_COOK_TIME_INVALID =
            RecipeDurationEntityTestData.getInvalidNewCookTimeInvalid();
    private static final RecipeDurationEntity VALID_NEW_PREP_TIME_VALID =
            RecipeDurationEntityTestData.getValidNewPrepTimeValid();
    private static final RecipeDurationEntity VALID_NEW_COOK_TIME_VALID =
            RecipeDurationEntityTestData.getValidNewCookTimeValid();
    private static final RecipeDurationEntity VALID_NEW_COMPLETE =
            RecipeDurationEntityTestData.getValidNewComplete();
    private static final RecipeDurationEntity INVALID_EXISTING_COMPLETE =
            RecipeDurationEntityTestData.getInvalidExistingComplete();
    private static final RecipeDurationEntity VALID_EXISTING_COMPLETE =
            RecipeDurationEntityTestData.getValidExistingComplete();
    private static final RecipeDurationEntity VALID_COMPLETE_FROM_ANOTHER_USER =
            RecipeDurationEntityTestData.getValidCompleteFromAnotherUser();
    private static final RecipeDurationEntity INVALID_COMPLETE_FROM_ANOTHER_USER =
            RecipeDurationEntityTestData.getInvalidCompleteFromAnotherUser();
    private static final RecipeDurationEntity VALID_NEW_CLONED =
            RecipeDurationEntityTestData.getValidNewCloned();
    private static final RecipeDurationEntity INVALID_NEW_CLONED =
            RecipeDurationEntityTestData.getInvalidNewCloned();
    private static final RecipeDurationEntity VALID_NEW_CLONED_PREP_TIME_UPDATED =
            RecipeDurationEntityTestData.getValidNewClonedPrepTimeUpdated();

    private static final int MAX_PREP_TIME = RecipeDurationEntityTestData.getMaxPrepTime();
    private static final int MAX_COOK_TIME = RecipeDurationEntityTestData.getMaxCookTime();
    private static final String ERROR_MESSAGE_TIME_TOO_LONG = "error_message_time_too_long";

    private static final RecipeModelStatus INVALID_UNCHANGED =
            RecipeValidatorTestData.getDurationModelStatusUnchangedInvalid();
    private static final RecipeModelStatus INVALID_CHANGED =
            RecipeValidatorTestData.getDurationModelStatusChangedInvalid();
    private static final RecipeModelStatus VALID_UNCHANGED =
            RecipeValidatorTestData.getDurationModelStatusUnchangedValid();
    private static final RecipeModelStatus VALID_CHANGED =
            RecipeValidatorTestData.getDurationModelStatusChangedValid();

    // endregion constants -------------------------------------------------------------------------

    // region helper fields ------------------------------------------------------------------------
    @Rule
    public InstantTaskExecutorRule instantExecutorRule = new InstantTaskExecutorRule();
    @Mock
    RepositoryRecipeDuration repoDurationMock;
    @Captor
    ArgumentCaptor<DataSource.GetEntityCallback<RecipeDurationEntity>> getEntityCallbackCaptor;
    @Mock
    Resources resourcesMock;
    @Captor
    ArgumentCaptor<RecipeDurationEntity> durationEntityCaptor;
    @Captor
    ArgumentCaptor<RecipeModelStatus> modelStatusCaptor;
    @Mock
    TimeProvider timeProviderMock;
    @Mock
    RecipeValidation.RecipeValidatorModelSubmission modelValidationSubmitterMock;
    // endregion helper fields ---------------------------------------------------------------------

    private RecipeDurationEditorViewModel SUT;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        setupResourceMockReturnValues();

        SUT = new RecipeDurationEditorViewModel(
                repoDurationMock,
                resourcesMock,
                timeProviderMock
        );

        SUT.setModelValidationSubmitter(modelValidationSubmitterMock);
    }

    @Test
    public void startNewRecipeId_newEmptyValuesSetToObservers() {
        // Arrange
        // Act
        SUT.start(VALID_NEW_EMPTY.getId());
        simulateNothingReturnedFromDatabase();
        // Assert
        assertEquals(String.valueOf(VALID_NEW_EMPTY.getPrepTime() / 60), SUT.getPrepHoursInView());
        assertEquals(String.valueOf(VALID_NEW_EMPTY.getPrepTime() % 60), SUT.getPrepMinutesInView());
        assertEquals(String.valueOf(VALID_NEW_EMPTY.getCookTime() / 60), SUT.getCookHoursInView());
        assertEquals(String.valueOf(VALID_NEW_EMPTY.getCookTime() % 60), SUT.getCookMinutesInView());
    }

    @Test
    public void startNewRecipeId_newEmptyEntitySaved() {
        // Arrange
        when(timeProviderMock.getCurrentTimeInMills()).thenReturn(VALID_NEW_EMPTY.getCreateDate());
        // Act
        SUT.start(VALID_NEW_EMPTY.getId());
        simulateNothingReturnedFromDatabase();
        // Assert
        verify(repoDurationMock).save(durationEntityCaptor.capture());
        assertEquals(VALID_NEW_EMPTY, durationEntityCaptor.getValue());
    }

    @Test
    public void startNewRecipeId_RecipeModelStatusVALID_UNCHANGED() {
        // Arrange
        when(timeProviderMock.getCurrentTimeInMills()).thenReturn(VALID_NEW_EMPTY.getCreateDate());
        // Act
        SUT.start(VALID_NEW_EMPTY.getId());
        simulateNothingReturnedFromDatabase();
        // Assert
        verify(modelValidationSubmitterMock).submitModelStatus(modelStatusCaptor.capture());
        RecipeModelStatus modelStatus = modelStatusCaptor.getValue();
        assertEquals(VALID_UNCHANGED, modelStatus);
    }

    @Test
    public void startNewRecipeId_invalidPrepHours_errorMessageSetToObservable() {
        // Arrange
        // Act
        SUT.start(VALID_NEW_EMPTY.getId());
        simulateNothingReturnedFromDatabase();
        SUT.setPrepHoursInView(String.valueOf(INVALID_NEW_PREP_TIME_INVALID.getPrepTime() / 60 + 1));
        // Assert
        assertEquals(ERROR_MESSAGE_TIME_TOO_LONG, SUT.prepTimeErrorMessage.get());
    }

    @Test
    public void startNewRecipeId_invalidPrepHours_invalidValueNotSaved() {
        // Arrange
        when(timeProviderMock.getCurrentTimeInMills()).thenReturn(VALID_NEW_EMPTY.getCreateDate());
        // Act
        SUT.start(VALID_NEW_EMPTY.getId());
        simulateNothingReturnedFromDatabase();
        SUT.setPrepHoursInView(String.valueOf(INVALID_NEW_PREP_TIME_INVALID.getPrepTime() / 60 + 1));
        // Assert
        verify(repoDurationMock).save(durationEntityCaptor.capture());
        assertEquals(VALID_NEW_EMPTY, durationEntityCaptor.getValue());
    }

    @Test
    public void startNewRecipeId_invalidPrepHours_recipeModelStatusINVALID_CHANGED() {
        // Arrange
        when(timeProviderMock.getCurrentTimeInMills()).thenReturn(VALID_NEW_EMPTY.getCreateDate());
        // Act
        SUT.start(VALID_NEW_EMPTY.getId());
        simulateNothingReturnedFromDatabase();
        SUT.setPrepHoursInView(String.valueOf(INVALID_NEW_PREP_TIME_INVALID.getPrepTime() / 60 + 1));
        // Assert
        verify(modelValidationSubmitterMock, times((2))).
                submitModelStatus(modelStatusCaptor.capture());
        assertEquals(INVALID_CHANGED, modelStatusCaptor.getValue());
    }

    @Test
    public void startNewRecipeId_validPrepHours_errorMessageObservableNull() {
        // Arrange
        // Act
        SUT.start(VALID_NEW_EMPTY.getId());
        simulateNothingReturnedFromDatabase();
        SUT.setPrepHoursInView(String.valueOf(VALID_NEW_PREP_TIME_VALID.getPrepTime() / 60));
        // Assert
        assertNull(SUT.cookTimeErrorMessage.get());
    }

    @Test
    public void startNewRecipeId_validPrepHours_recipeModelStatusVALID_CHANGED() {
        // Arrange
        // Act
        SUT.start(VALID_NEW_EMPTY.getId());
        simulateNothingReturnedFromDatabase();
        SUT.setPrepHoursInView(String.valueOf(VALID_NEW_PREP_TIME_VALID.getPrepTime() / 60));
        // Assert
        verify(modelValidationSubmitterMock, times((2))).submitModelStatus(modelStatusCaptor.capture());
        assertEquals(VALID_CHANGED, modelStatusCaptor.getValue());
    }

    @Test
    public void startNewRecipeId_validPrepHours_prepHoursSaved() {
        // Arrange
        when(timeProviderMock.getCurrentTimeInMills()).thenReturn(VALID_NEW_EMPTY.getCreateDate());
        // Act
        SUT.start(VALID_NEW_EMPTY.getId());
        simulateNothingReturnedFromDatabase();
        SUT.setPrepHoursInView(String.valueOf(VALID_NEW_PREP_TIME_VALID.getPrepTime() / 60));
        // Assert
        verify(repoDurationMock, times((2))).save(durationEntityCaptor.capture());
        assertEquals(VALID_NEW_PREP_TIME_VALID, durationEntityCaptor.getValue());
    }

    @Test
    public void startNewRecipeId_invalidPrepMinutes_errorMessageSetToObservable() {
        // Arrange
        // Act
        SUT.start(VALID_NEW_EMPTY.getId());
        simulateNothingReturnedFromDatabase();
        SUT.setPrepMinutesInView(String.valueOf(INVALID_NEW_PREP_TIME_INVALID.getPrepTime()));
        // Assert
        assertEquals(ERROR_MESSAGE_TIME_TOO_LONG, SUT.prepTimeErrorMessage.get());
    }

    @Test
    public void startNewRecipeId_invalidPrepMinutes_recipeModelStatusINVALID_CHANGED() {
        // Arrange
        // Act
        SUT.start(VALID_NEW_EMPTY.getId());
        simulateNothingReturnedFromDatabase();
        SUT.setPrepMinutesInView(String.valueOf(INVALID_NEW_PREP_TIME_INVALID.getPrepTime()));
        // Assert
        verify(modelValidationSubmitterMock, times((2))).submitModelStatus(modelStatusCaptor.capture());
        assertEquals(INVALID_CHANGED, modelStatusCaptor.getValue());
    }

    @Test
    public void startNewRecipeId_invalidPrepMinutes_invalidValueNotSaved() {
        when(timeProviderMock.getCurrentTimeInMills()).thenReturn(VALID_NEW_EMPTY.getCreateDate());
        // Act
        SUT.start(VALID_NEW_EMPTY.getId());
        simulateNothingReturnedFromDatabase();
        SUT.setPrepMinutesInView(String.valueOf(INVALID_NEW_PREP_TIME_INVALID.getPrepTime()));
        // Assert
        verify(repoDurationMock).save(durationEntityCaptor.capture());
        assertEquals(VALID_NEW_EMPTY, durationEntityCaptor.getValue());
    }

    @Test
    public void startNewRecipeId_validPrepMinutes_errorMessageObservableNull() {
        // Arrange
        // Act
        SUT.start(VALID_NEW_EMPTY.getId());
        simulateNothingReturnedFromDatabase();
        SUT.setPrepMinutesInView(String.valueOf(VALID_NEW_PREP_TIME_VALID.getPrepTime()));
        // Assert
        assertNull(SUT.prepTimeErrorMessage.get());
    }

    @Test
    public void startNewRecipeId_validPrepMinutes_recipeModelStatusVALID_CHANGED() {
        // Arrange
        // Act
        SUT.start(VALID_NEW_EMPTY.getId());
        simulateNothingReturnedFromDatabase();
        SUT.setPrepMinutesInView(String.valueOf(VALID_NEW_PREP_TIME_VALID.getPrepTime()));
        // Assert
        verify(modelValidationSubmitterMock, times((2))).submitModelStatus(modelStatusCaptor.capture());
        assertEquals(VALID_CHANGED, modelStatusCaptor.getValue());
    }

    @Test
    public void startNewRecipeId_validPrepMinutes_prepMinutesSaved() {
        // Arrange
        when(timeProviderMock.getCurrentTimeInMills()).
                thenReturn(VALID_NEW_PREP_TIME_VALID.getCreateDate());
        // Act
        SUT.start(VALID_NEW_EMPTY.getId());
        simulateNothingReturnedFromDatabase();
        SUT.setPrepMinutesInView(String.valueOf(VALID_NEW_PREP_TIME_VALID.getPrepTime()));
        // Assert
        verify(repoDurationMock, times((2))).save(durationEntityCaptor.capture());
        assertEquals(VALID_NEW_PREP_TIME_VALID, durationEntityCaptor.getValue());
    }

    @Test
    public void startNewRecipeId_validPrepHoursAndMinutes_prepTimeSaved() {
        // Arrange
        when(timeProviderMock.getCurrentTimeInMills()).thenReturn(
                VALID_NEW_PREP_TIME_VALID.getCreateDate());
        // Act
        SUT.start(VALID_NEW_EMPTY.getId());
        simulateNothingReturnedFromDatabase();
        SUT.setPrepHoursInView(String.valueOf(VALID_NEW_PREP_TIME_VALID.getPrepTime() / 60));
        SUT.setPrepMinutesInView(String.valueOf(VALID_NEW_PREP_TIME_VALID.getPrepTime() % 60));
        // Assert
        verify(repoDurationMock, times((2))).save(durationEntityCaptor.capture());
        assertEquals(VALID_NEW_PREP_TIME_VALID, durationEntityCaptor.getAllValues().get(1));
    }

    @Test
    public void startNewRecipeId_validPrepHoursAndMinutes_recipeModelStatusVALID_CHANGED() {
        // Arrange
        // Act
        SUT.start(VALID_NEW_EMPTY.getId());
        simulateNothingReturnedFromDatabase();
        SUT.setPrepHoursInView(String.valueOf(VALID_NEW_PREP_TIME_VALID.getPrepTime() / 60));
        SUT.setPrepMinutesInView(String.valueOf(VALID_NEW_PREP_TIME_VALID.getPrepTime() % 60));
        verify(modelValidationSubmitterMock).submitModelStatus(VALID_CHANGED);
    }

    @Test
    public void startNewRecipeId_invalidPrepHoursAndMinutes_onlyValidPartOfPrepTimeSaved() {
        // Arrange
        when(timeProviderMock.getCurrentTimeInMills()).thenReturn(
                INVALID_NEW_PREP_TIME_INVALID.getCreateDate());
        // Act
        SUT.start(VALID_NEW_EMPTY.getId());
        simulateNothingReturnedFromDatabase();
        SUT.setPrepHoursInView(String.valueOf(INVALID_NEW_PREP_TIME_INVALID.getPrepTime() / 60));
        SUT.setPrepMinutesInView(String.valueOf(INVALID_NEW_PREP_TIME_INVALID.getPrepTime() % 60));
        // Assert
        verify(repoDurationMock, times((2))).save(durationEntityCaptor.capture());
        assertEquals(VALID_NEW_PREP_TIME_VALID, durationEntityCaptor.getValue());
    }

    @Test
    public void startNewRecipeId_invalidPrepHoursAndMinutes_recipeModelStatusINVALID_CHANGED() {
        // Arrange
        // Act
        SUT.start(VALID_NEW_EMPTY.getId());
        simulateNothingReturnedFromDatabase();
        SUT.setPrepHoursInView(String.valueOf(MAX_PREP_TIME / 60));
        SUT.setPrepMinutesInView(String.valueOf(MAX_PREP_TIME % 60 + 1));

        verify(modelValidationSubmitterMock, times((3))).submitModelStatus(modelStatusCaptor.capture());
        assertEquals(INVALID_CHANGED, modelStatusCaptor.getValue());
        verify(modelValidationSubmitterMock).submitModelStatus(eq(INVALID_CHANGED));
    }

    @Test
    public void startNewRecipeId_validCookHours_errorMessageObservableNull() {
        // Arrange
        // Act
        SUT.start(VALID_NEW_EMPTY.getId());
        simulateNothingReturnedFromDatabase();
        SUT.setCookHoursInView(String.valueOf(VALID_NEW_COOK_TIME_VALID.getCookTime() / 60));
        // Assert
        assertNull(SUT.cookTimeErrorMessage.get());
    }

    @Test
    public void startNewRecipeId_validCookHours_RecipeModelStatusVALID_CHANGED() {
        // Arrange
        when(timeProviderMock.getCurrentTimeInMills()).thenReturn(
                VALID_NEW_COOK_TIME_VALID.getCreateDate());
        // Act
        SUT.start(VALID_NEW_EMPTY.getId());
        simulateNothingReturnedFromDatabase();
        SUT.setCookHoursInView(String.valueOf(VALID_NEW_COOK_TIME_VALID.getCookTime() / 60));
        // Assert
        verify(modelValidationSubmitterMock, times((2))).submitModelStatus(modelStatusCaptor.capture());
        assertEquals(VALID_CHANGED, modelStatusCaptor.getValue());
    }

    @Test
    public void startNewRecipeId_validCookHours_cookHoursSaved() {
        // Arrange
        when(timeProviderMock.getCurrentTimeInMills()).thenReturn(
                VALID_NEW_COOK_TIME_VALID.getCreateDate());
        // Act
        SUT.start(VALID_NEW_EMPTY.getId());
        simulateNothingReturnedFromDatabase();
        SUT.setCookHoursInView(String.valueOf(VALID_NEW_COOK_TIME_VALID.getCookTime() / 60));
        // Assert
        verify(repoDurationMock, times((2))).save(durationEntityCaptor.capture());
        assertEquals(VALID_NEW_COOK_TIME_VALID, durationEntityCaptor.getValue());
    }

    @Test
    public void startNewRecipeId_invalidCookHours_errorMessageSetToObservable() {
        // Arrange
        // Act
        SUT.start(VALID_NEW_EMPTY.getId());
        simulateNothingReturnedFromDatabase();
        SUT.setCookHoursInView(String.valueOf(INVALID_NEW_COOK_TIME_INVALID.getCookTime() / 60 + 1));
        // Assert
        assertEquals(ERROR_MESSAGE_TIME_TOO_LONG, SUT.cookTimeErrorMessage.get());
    }

    @Test
    public void startNewRecipeId_invalidCookHours_RecipeModelStatusINVALID_CHANGED() {
        // Arrange
        // Act
        SUT.start(VALID_NEW_EMPTY.getId());
        simulateNothingReturnedFromDatabase();
        SUT.setCookHoursInView(String.valueOf(INVALID_NEW_COOK_TIME_INVALID.getCookTime() / 60 + 1));
        // Assert
        verify(modelValidationSubmitterMock, times((2))).submitModelStatus(modelStatusCaptor.capture());
        assertEquals(INVALID_CHANGED, modelStatusCaptor.getValue());
    }

    @Test
    public void startNewRecipeId_invalidCookHours_invalidValueNotSaved() {
        // Arrange
        when(timeProviderMock.getCurrentTimeInMills()).thenReturn(VALID_NEW_EMPTY.getCreateDate());
        // Act
        SUT.start(VALID_NEW_EMPTY.getId());
        simulateNothingReturnedFromDatabase();
        SUT.setCookHoursInView(String.valueOf(INVALID_NEW_COOK_TIME_INVALID.getCookTime() / 60 + 1));
        // Assert
        verify(repoDurationMock).save(durationEntityCaptor.capture());
        assertEquals(VALID_NEW_EMPTY, durationEntityCaptor.getValue());
    }

    @Test
    public void startNewRecipeId_validCookMinutes_errorMessageObservableNull() {
        // Arrange
        // Act
        SUT.start(VALID_NEW_EMPTY.getId());
        simulateNothingReturnedFromDatabase();
        SUT.setCookMinutesInView(String.valueOf(VALID_NEW_COOK_TIME_VALID.getCookTime()));
        // Assert
        assertNull(SUT.cookTimeErrorMessage.get());
    }

    @Test
    public void startNewRecipeId_validCookMinutes_RecipeModelStatusVALID_CHANGED() {
        // Arrange
        // Act
        SUT.start(VALID_NEW_EMPTY.getId());
        simulateNothingReturnedFromDatabase();
        SUT.setCookMinutesInView(String.valueOf(VALID_NEW_COOK_TIME_VALID.getCookTime()));
        // Assert
        verify(modelValidationSubmitterMock, times((2))).submitModelStatus(modelStatusCaptor.capture());
        assertEquals(VALID_CHANGED, modelStatusCaptor.getValue());
    }

    @Test
    public void startNewRecipeId_validCookMinutes_cookHoursSaved() {
        // Arrange
        when(timeProviderMock.getCurrentTimeInMills()).
                thenReturn(VALID_NEW_COOK_TIME_VALID.getCreateDate());
        // Act
        SUT.start(VALID_NEW_EMPTY.getId());
        simulateNothingReturnedFromDatabase();
        SUT.setCookMinutesInView(String.valueOf(VALID_NEW_COOK_TIME_VALID.getCookTime()));
        // Assert
        verify(repoDurationMock, times((2))).save(durationEntityCaptor.capture());
        assertEquals(VALID_NEW_COOK_TIME_VALID, durationEntityCaptor.getValue());
    }

    @Test
    public void startNewRecipeId_invalidCookMinutes_errorMessageSetToObservable() {
        // Arrange
        // Act
        SUT.start(VALID_NEW_EMPTY.getId());
        simulateNothingReturnedFromDatabase();
        SUT.setCookMinutesInView(String.valueOf(INVALID_NEW_COOK_TIME_INVALID.getCookTime()));
        // Assert
        assertEquals(ERROR_MESSAGE_TIME_TOO_LONG, SUT.cookTimeErrorMessage.get());
    }

    @Test
    public void startNewRecipeId_invalidCookMinutes_RecipeModelStatusINVALID_CHANGED() {
        // Arrange
        // Act
        SUT.start(VALID_NEW_EMPTY.getId());
        simulateNothingReturnedFromDatabase();
        SUT.setCookMinutesInView(String.valueOf(INVALID_NEW_COOK_TIME_INVALID.getCookTime()));
        // Assert
        verify(modelValidationSubmitterMock, times((2))).submitModelStatus(modelStatusCaptor.capture());
        assertEquals(INVALID_CHANGED, modelStatusCaptor.getValue());
    }

    @Test
    public void startNewRecipeId_invalidCookMinutes_invalidValueNotSaved() {
        // Arrange
        when(timeProviderMock.getCurrentTimeInMills()).
                thenReturn(INVALID_NEW_COOK_TIME_INVALID.getCreateDate());
        // Act
        SUT.start(VALID_NEW_EMPTY.getId());
        simulateNothingReturnedFromDatabase();
        SUT.setCookMinutesInView(String.valueOf(INVALID_NEW_COOK_TIME_INVALID.getCookTime()));
        // Assert
        verify(repoDurationMock).save(durationEntityCaptor.capture());
        assertEquals(VALID_NEW_EMPTY, durationEntityCaptor.getValue());
    }

    @Test
    public void startNewRecipeId_validCookHoursAndMinutes_errorMessageObservableNull() {
        // Arrange
        // Act
        SUT.start(VALID_NEW_EMPTY.getId());
        simulateNothingReturnedFromDatabase();
        SUT.setCookHoursInView(String.valueOf(VALID_NEW_COOK_TIME_VALID.getCookTime() / 60));
        SUT.setCookMinutesInView(String.valueOf(VALID_NEW_COOK_TIME_VALID.getCookTime() % 60));
        // Assert
        assertNull(SUT.cookTimeErrorMessage.get());
    }

    @Test
    public void startNewRecipeId_validCookHoursAndMinutes_recipeModelStatusVALID_CHANGED() {
        // Arrange
        when(timeProviderMock.getCurrentTimeInMills()).
                thenReturn(VALID_NEW_COOK_TIME_VALID.getCreateDate());
        // Act
        SUT.start(VALID_NEW_EMPTY.getId());
        simulateNothingReturnedFromDatabase();
        SUT.setCookHoursInView(String.valueOf(VALID_NEW_COOK_TIME_VALID.getCookTime() / 60));
        SUT.setCookMinutesInView(String.valueOf(VALID_NEW_COOK_TIME_VALID.getCookTime() % 60));
        // Assert
        verify(modelValidationSubmitterMock).submitModelStatus(eq(VALID_CHANGED));
    }

    @Test
    public void startNewRecipeId_validCookHoursAndMinutes_cookTimeSaved() {
        // Arrange
        when(timeProviderMock.getCurrentTimeInMills()).
                thenReturn(VALID_NEW_COOK_TIME_VALID.getCreateDate());
        // Act
        SUT.start(VALID_NEW_EMPTY.getId());
        simulateNothingReturnedFromDatabase();
        SUT.setCookHoursInView(String.valueOf(VALID_NEW_COOK_TIME_VALID.getCookTime() / 60));
        SUT.setCookMinutesInView(String.valueOf(VALID_NEW_COOK_TIME_VALID.getCookTime() % 60));
        // Assert
        verify(repoDurationMock, times((2))).save(durationEntityCaptor.capture());
        assertEquals(VALID_NEW_COOK_TIME_VALID, durationEntityCaptor.getValue());
    }

    @Test
    public void startNewRecipeId_invalidCookHoursAndMinutes_errorMessageSetToObservable() {
        // Arrange
        // Act
        SUT.start(VALID_NEW_EMPTY.getId());
        simulateNothingReturnedFromDatabase();
        SUT.setCookHoursInView(String.valueOf(INVALID_NEW_COOK_TIME_INVALID.getCookTime() / 60));
        SUT.setCookMinutesInView(String.valueOf(INVALID_NEW_COOK_TIME_INVALID.getCookTime() % 60 + 1));
        // Assert
        assertEquals(ERROR_MESSAGE_TIME_TOO_LONG, SUT.cookTimeErrorMessage.get());
    }

    @Test
    public void startNewRecipeId_invalidCookHoursAndMinutes_RecipeModelStatusINVALID_CHANGED() {
        // Arrange
        // Act
        SUT.start(VALID_NEW_EMPTY.getId());
        simulateNothingReturnedFromDatabase();
        SUT.setCookHoursInView(String.valueOf(INVALID_NEW_COOK_TIME_INVALID.getCookTime() / 60));
        SUT.setCookMinutesInView(String.valueOf(INVALID_NEW_COOK_TIME_INVALID.getCookTime() % 60 + 1));
        // Assert
        verify(modelValidationSubmitterMock).submitModelStatus(eq(INVALID_CHANGED));
    }

    @Test
    public void startNewRecipeId_invalidCookHoursAndMinutes_onlyValidPartOfPrepTimeSaved() {
        // Arrange
        when(timeProviderMock.getCurrentTimeInMills()).thenReturn(
                INVALID_NEW_COOK_TIME_INVALID.getCreateDate());
        // Act
        SUT.start(VALID_NEW_EMPTY.getId());
        simulateNothingReturnedFromDatabase();
        SUT.setCookHoursInView(String.valueOf(INVALID_NEW_COOK_TIME_INVALID.getCookTime() / 60));
        SUT.setCookMinutesInView(String.valueOf(INVALID_NEW_COOK_TIME_INVALID.getCookTime() % 60 + 1));
        // Assert
        verify(repoDurationMock).save(VALID_NEW_EMPTY);
    }

    @Test
    public void start_validExistingRecipeId_prepHoursSetToObservable() {
        // Arrange
        String prepHours = String.valueOf(VALID_EXISTING_COMPLETE.getPrepTime() / 60);
        // Act
        SUT.start(VALID_EXISTING_COMPLETE.getId());
        simulateGetValidExistingCompleteFromDatabase();
        // Assert
        assertEquals(prepHours, SUT.getPrepHoursInView());
    }

    @Test
    public void start_validExistingRecipeId_prepMinutesSetToObservable() {
        // Arrange
        String prepMinutes = String.valueOf(VALID_EXISTING_COMPLETE.getPrepTime() % 60);
        // Act
        SUT.start(VALID_EXISTING_COMPLETE.getId());
        simulateGetValidExistingCompleteFromDatabase();
        // Assert
        assertEquals(prepMinutes, SUT.getPrepMinutesInView());
    }

    @Test
    public void start_validExistingRecipeId_cookHoursSetToObservable() {
        // Arrange
        String cookHours = String.valueOf(VALID_EXISTING_COMPLETE.getCookTime() / 60);
        // Act
        SUT.start(VALID_EXISTING_COMPLETE.getId());
        simulateGetValidExistingCompleteFromDatabase();
        // Assert
        assertEquals(cookHours, SUT.getCookHoursInView());
    }

    @Test
    public void start_validExistingRecipeId_cookMinutesSetToObservable() {
        // Arrange
        String cookMinutes = String.valueOf(VALID_EXISTING_COMPLETE.getCookTime() % 60);
        // Act
        SUT.start(VALID_EXISTING_COMPLETE.getId());
        simulateGetValidExistingCompleteFromDatabase();
        // Assert
        assertEquals(cookMinutes, SUT.getCookMinutesInView());
    }

    @Test
    public void start_validExistingRecipeId_RecipeModelStatusVALID_UNCHANGED() {
        when(timeProviderMock.getCurrentTimeInMills()).thenReturn(
                VALID_EXISTING_COMPLETE.getCreateDate());
        // Act
        SUT.start(VALID_EXISTING_COMPLETE.getId());
        simulateGetValidExistingCompleteFromDatabase();
        // Assert
        verify(modelValidationSubmitterMock, times((2))).submitModelStatus(modelStatusCaptor.capture());
        RecipeModelStatus modelStatus = modelStatusCaptor.getValue();
        assertEquals(VALID_UNCHANGED, modelStatus);
    }

    @Test
    public void startWithCloned_existingAndNewRecipeId_databaseCalledWithExistingId() {
        // Arrange
        SUT.startByCloningModel(VALID_COMPLETE_FROM_ANOTHER_USER.getId(), VALID_NEW_EMPTY.getId());
        // Assert
        verify(repoDurationMock).getById(eq(VALID_COMPLETE_FROM_ANOTHER_USER.getId()), eq(SUT));
    }

    @Test
    public void startWithCloned_existingAndNewRecipeId_existingFromAnotherUserCopiedAndSavedWithNewId() {
        // Arrange
        when(timeProviderMock.getCurrentTimeInMills()).thenReturn(VALID_NEW_CLONED.getCreateDate());
        // Act
        SUT.startByCloningModel(VALID_COMPLETE_FROM_ANOTHER_USER.getId(), VALID_NEW_EMPTY.getId());
        simulateGetValidEntityFromAnotherUserFromDatabase();
        // Assert
        verify(repoDurationMock).save(eq(VALID_NEW_CLONED));
    }

    @Test
    public void startWithCloned_prepTimeChanged_savedWithUpdatedPrepTime() {
        // Arrange
        when(timeProviderMock.getCurrentTimeInMills()).thenReturn(
                VALID_NEW_CLONED_PREP_TIME_UPDATED.getCreateDate());
        // Act
        SUT.startByCloningModel(VALID_COMPLETE_FROM_ANOTHER_USER.getId(), VALID_NEW_EMPTY.getId());
        simulateGetValidEntityFromAnotherUserFromDatabase();
        SUT.setPrepHoursInView(String.valueOf(VALID_NEW_CLONED_PREP_TIME_UPDATED.getPrepTime() / 60));
        SUT.setPrepMinutesInView(String.valueOf(VALID_NEW_CLONED_PREP_TIME_UPDATED.getPrepTime() % 60));
        // Assert
        verify(repoDurationMock, times((3))).save(durationEntityCaptor.capture());
        assertEquals(VALID_NEW_CLONED_PREP_TIME_UPDATED, durationEntityCaptor.getValue());
    }

    @Test
    public void startWithCloned_modelFromAnotherUserNotAvailable_newModelCreatedAndSavedWithNewId() {
        // Arrange
        when(timeProviderMock.getCurrentTimeInMills()).thenReturn(VALID_NEW_EMPTY.getCreateDate());
        // Act
        SUT.startByCloningModel(INVALID_COMPLETE_FROM_ANOTHER_USER.getId(), VALID_NEW_EMPTY.getId());
        verify(repoDurationMock).getById(eq(INVALID_COMPLETE_FROM_ANOTHER_USER.getId()),
                getEntityCallbackCaptor.capture());
        getEntityCallbackCaptor.getValue().onDataNotAvailable();

        // Assert
        verify(repoDurationMock).save(eq(VALID_NEW_EMPTY));
    }

    // region helper methods -----------------------------------------------------------------------
    private void setupResourceMockReturnValues() {
        when(resourcesMock.getInteger(R.integer.recipe_max_cook_time_in_minutes)).thenReturn(MAX_PREP_TIME);
        when(resourcesMock.getInteger(R.integer.recipe_max_prep_time_in_minutes)).thenReturn(MAX_COOK_TIME);
        when(resourcesMock.getString(R.string.input_error_recipe_prep_time_too_long)).thenReturn(ERROR_MESSAGE_TIME_TOO_LONG);
        when(resourcesMock.getString(R.string.input_error_recipe_cook_time_too_long)).thenReturn(ERROR_MESSAGE_TIME_TOO_LONG);
    }

    private void simulateNothingReturnedFromDatabase() {
        verify(repoDurationMock).getById(eq(VALID_NEW_EMPTY.getId()),
                getEntityCallbackCaptor.capture());

        getEntityCallbackCaptor.getValue().onDataNotAvailable();
    }

    private void simulateGetValidExistingCompleteFromDatabase() {
        verify(repoDurationMock).getById(eq(VALID_EXISTING_COMPLETE.getId()),
                getEntityCallbackCaptor.capture());

        getEntityCallbackCaptor.getValue().onEntityLoaded(VALID_EXISTING_COMPLETE);
    }

    private void simulateGetValidEntityFromAnotherUserFromDatabase() {
        verify(repoDurationMock).getById(
                eq(VALID_COMPLETE_FROM_ANOTHER_USER.getId()),
                getEntityCallbackCaptor.capture());

        getEntityCallbackCaptor.getValue().onEntityLoaded(
                VALID_COMPLETE_FROM_ANOTHER_USER);
    }
    // endregion helper methods --------------------------------------------------------------------

    // region helper classes -----------------------------------------------------------------------
    // endregion helper classes --------------------------------------------------------------------
}