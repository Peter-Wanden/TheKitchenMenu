package com.example.peter.thekitchenmenu.ui.detail.recipe.recipeeditor;

import android.content.res.Resources;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;

import com.example.peter.thekitchenmenu.R;
import com.example.peter.thekitchenmenu.data.entity.RecipeDurationEntity;
import com.example.peter.thekitchenmenu.data.repository.DataSource;
import com.example.peter.thekitchenmenu.testdata.RecipeDurationEntityTestData;
import com.example.peter.thekitchenmenu.testdata.RecipeValidatorTestData;
import com.example.peter.thekitchenmenu.utils.ParseIntegerFromObservableHandler;
import com.example.peter.thekitchenmenu.utils.TimeProvider;

import org.junit.*;
import org.mockito.*;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.*;

public class RecipeDurationViewModelTest {

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
    DataSource<RecipeDurationEntity> durationEntityDataSourceMock;
    @Captor
    ArgumentCaptor<DataSource.GetEntityCallback<RecipeDurationEntity>>
            getEntityCallbackArgumentCaptor;
    @Mock
    Resources resourcesMock;
    @Mock
    TimeProvider timeProviderMock;
    @Mock
    ParseIntegerFromObservableHandler intFromObservableMock;
    @Mock
    RecipeValidation.RecipeValidatorModelSubmission modelValidationSubmitterMock;
    // endregion helper fields ---------------------------------------------------------------------

    private RecipeDurationViewModel SUT;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        setupResourceMockReturnValues();

        SUT = new RecipeDurationViewModel(
                durationEntityDataSourceMock,
                resourcesMock,
                timeProviderMock,
                intFromObservableMock
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
        assertEquals(String.valueOf(VALID_NEW_EMPTY.getPrepTime() / 60),
                SUT.prepHoursObservable.get());
        assertEquals(String.valueOf(VALID_NEW_EMPTY.getPrepTime() % 60),
                SUT.prepMinutesObservable.get());
        assertEquals(String.valueOf(VALID_NEW_EMPTY.getCookTime() / 60),
                SUT.cookHoursObservable.get());
        assertEquals(String.valueOf(VALID_NEW_EMPTY.getCookTime() % 60),
                SUT.cookMinutesObservable.get());
    }

    @Test
    public void startNewRecipeId_newEmptyEntitySaved() {
        // Arrange
        when(timeProviderMock.getCurrentTimestamp()).thenReturn(VALID_NEW_EMPTY.getCreateDate());
        // Act
        SUT.start(VALID_NEW_EMPTY.getId());
        simulateNothingReturnedFromDatabase();
        // Assert
        verify(durationEntityDataSourceMock).save(eq(VALID_NEW_EMPTY));
    }

    @Test
    public void startNewRecipeId_RecipeModelStatusVALID_UNCHANGED() {
        // Arrange
        ArgumentCaptor<RecipeModelStatus> ac = ArgumentCaptor.forClass(RecipeModelStatus.class);
        when(timeProviderMock.getCurrentTimestamp()).thenReturn(VALID_NEW_EMPTY.getCreateDate());
        // Act
        SUT.start(VALID_NEW_EMPTY.getId());
        simulateNothingReturnedFromDatabase();
        // Assert
        verify(modelValidationSubmitterMock).submitModelStatus(ac.capture());
        RecipeModelStatus modelStatus = ac.getValue();
        assertEquals(VALID_UNCHANGED, modelStatus);
    }

    @Test
    public void startNewRecipeId_invalidPrepHours_errorMessageSetToObservable() {
        // Arrange
        when(intFromObservableMock.parseInt(anyObject(), eq(SUT.prepHoursObservable), anyInt())).
                thenReturn(INVALID_NEW_PREP_TIME_INVALID.getPrepTime() / 60 + 1);
        // Act
        SUT.start(VALID_NEW_EMPTY.getId());
        simulateNothingReturnedFromDatabase();
        SUT.prepHoursObservable.set(String.valueOf(
                INVALID_NEW_PREP_TIME_INVALID.getPrepTime() / 60 + 1));
        // Assert
        assertEquals(ERROR_MESSAGE_TIME_TOO_LONG, SUT.prepTimeErrorMessage.get());
    }

    @Test
    public void startNewRecipeId_invalidPrepHours_invalidValueNotSaved() {
        // Arrange
        ArgumentCaptor<RecipeDurationEntity> ac = ArgumentCaptor.forClass(RecipeDurationEntity.class);
        when(intFromObservableMock.parseInt(anyObject(), eq(SUT.prepHoursObservable), anyInt())).
                thenReturn(INVALID_NEW_PREP_TIME_INVALID.getPrepTime() / 60 + 1);
        when(timeProviderMock.getCurrentTimestamp()).thenReturn(VALID_NEW_EMPTY.getCreateDate());
        // Act
        SUT.start(VALID_NEW_EMPTY.getId());
        simulateNothingReturnedFromDatabase();
        SUT.prepHoursObservable.set(String.valueOf(
                INVALID_NEW_PREP_TIME_INVALID.getPrepTime() / 60 + 1));
        // Assert
        verify(durationEntityDataSourceMock).save(ac.capture());
        assertEquals(VALID_NEW_EMPTY, ac.getValue());
    }

    @Test
    public void startNewRecipeId_invalidPrepHours_recipeModelStatusINVALID_CHANGED() {
        // Arrange
        ArgumentCaptor<RecipeModelStatus> ac = ArgumentCaptor.forClass(RecipeModelStatus.class);
        when(intFromObservableMock.parseInt(anyObject(), eq(SUT.prepHoursObservable), anyInt())).
                thenReturn(INVALID_NEW_PREP_TIME_INVALID.getPrepTime() / 60 + 1);
        when(timeProviderMock.getCurrentTimestamp()).thenReturn(VALID_NEW_EMPTY.getCreateDate());
        // Act
        SUT.start(VALID_NEW_EMPTY.getId());
        simulateNothingReturnedFromDatabase();
        SUT.prepHoursObservable.set(String.valueOf(
                INVALID_NEW_PREP_TIME_INVALID.getPrepTime() / 60 + 1));
        // Assert
        verify(modelValidationSubmitterMock, times(2)).submitModelStatus(
                eq(INVALID_CHANGED));
    }

    @Test
    public void startNewRecipeId_validPrepHours_errorMessageObservableNull() {
        // Arrange
        when(intFromObservableMock.parseInt(anyObject(), eq(SUT.prepHoursObservable), anyInt())).
                thenReturn(VALID_NEW_PREP_TIME_VALID.getPrepTime() / 60);
        // Act
        SUT.start(VALID_NEW_EMPTY.getId());
        simulateNothingReturnedFromDatabase();
        SUT.prepHoursObservable.set(String.valueOf(VALID_NEW_PREP_TIME_VALID.getPrepTime() / 60));
        // Assert
        assertNull(SUT.cookTimeErrorMessage.get());
    }

    @Test
    public void startNewRecipeId_validPrepHours_recipeModelStatusVALID_CHANGED() {
        // Arrange
        ArgumentCaptor<RecipeModelStatus> ac = ArgumentCaptor.forClass(RecipeModelStatus.class);
        when(intFromObservableMock.parseInt(anyObject(), eq(SUT.prepHoursObservable), anyInt())).
                thenReturn(VALID_NEW_PREP_TIME_VALID.getPrepTime() / 60);
        // Act
        SUT.start(VALID_NEW_EMPTY.getId());
        simulateNothingReturnedFromDatabase();
        SUT.prepHoursObservable.set(String.valueOf(VALID_NEW_PREP_TIME_VALID.getPrepTime() / 60));
        // Assert
        verify(modelValidationSubmitterMock, times(2)).
                submitModelStatus(ac.capture());
        assertEquals(VALID_CHANGED, ac.getValue());
    }

    @Test
    public void startNewRecipeId_validPrepHours_prepHoursSaved() {
        // Arrange
        ArgumentCaptor<RecipeDurationEntity> ac =
                ArgumentCaptor.forClass(RecipeDurationEntity.class);
        when(intFromObservableMock.parseInt(anyObject(), eq(SUT.prepHoursObservable), anyInt())).
                thenReturn(VALID_NEW_PREP_TIME_VALID.getPrepTime() / 60);
        when(timeProviderMock.getCurrentTimestamp()).thenReturn(VALID_NEW_EMPTY.getCreateDate());
        // Act
        SUT.start(VALID_NEW_EMPTY.getId());
        simulateNothingReturnedFromDatabase();
        SUT.prepHoursObservable.set(String.valueOf(VALID_NEW_PREP_TIME_VALID.getPrepTime() / 60));
        // Assert
        verify(durationEntityDataSourceMock, times(3)).save(ac.capture());
        assertEquals(VALID_NEW_PREP_TIME_VALID, ac.getValue());
    }

    @Test
    public void startNewRecipeId_invalidPrepMinutes_errorMessageSetToObservable() {
        // Arrange
        when(intFromObservableMock.parseInt(anyObject(), eq(SUT.prepMinutesObservable), anyInt())).
                thenReturn(INVALID_NEW_PREP_TIME_INVALID.getPrepTime());
        // Act
        SUT.start(VALID_NEW_EMPTY.getId());
        simulateNothingReturnedFromDatabase();
        SUT.prepMinutesObservable.set(String.valueOf(INVALID_NEW_PREP_TIME_INVALID.getPrepTime()));
        // Assert
        assertEquals(ERROR_MESSAGE_TIME_TOO_LONG, SUT.prepTimeErrorMessage.get());
    }

    @Test
    public void startNewRecipeId_invalidPrepMinutes_recipeModelStatusINVALID_CHANGED() {
        // Arrange
        ArgumentCaptor<RecipeModelStatus> ac = ArgumentCaptor.forClass(RecipeModelStatus.class);
        when(intFromObservableMock.parseInt(anyObject(), eq(SUT.prepMinutesObservable), anyInt())).
                thenReturn(INVALID_NEW_PREP_TIME_INVALID.getPrepTime());
        // Act
        SUT.start(VALID_NEW_EMPTY.getId());
        simulateNothingReturnedFromDatabase();
        SUT.prepMinutesObservable.set(String.valueOf(INVALID_NEW_PREP_TIME_INVALID.getPrepTime()));
        // Assert
        verify(modelValidationSubmitterMock, times(2)).
                submitModelStatus(ac.capture());
        assertEquals(INVALID_CHANGED, ac.getValue());
    }

    @Test
    public void startNewRecipeId_invalidPrepMinutes_invalidValueNotSaved() {
        when(intFromObservableMock.parseInt(anyObject(), eq(SUT.prepMinutesObservable), anyInt())).
                thenReturn(INVALID_NEW_PREP_TIME_INVALID.getPrepTime());
        when(timeProviderMock.getCurrentTimestamp()).thenReturn(VALID_NEW_EMPTY.getCreateDate());
        // Act
        SUT.start(VALID_NEW_EMPTY.getId());
        simulateNothingReturnedFromDatabase();
        SUT.prepMinutesObservable.set(String.valueOf(INVALID_NEW_PREP_TIME_INVALID.getPrepTime()));
        // Assert
        verify(durationEntityDataSourceMock).save(eq(VALID_NEW_EMPTY));
    }

    @Test
    public void startNewRecipeId_validPrepMinutes_errorMessageObservableNull() {
        // Arrange
        when(intFromObservableMock.parseInt(anyObject(), eq(SUT.prepMinutesObservable), anyInt())).
                thenReturn(VALID_NEW_PREP_TIME_VALID.getPrepTime());
        // Act
        SUT.start(VALID_NEW_EMPTY.getId());
        simulateNothingReturnedFromDatabase();
        SUT.prepMinutesObservable.set(String.valueOf(VALID_NEW_PREP_TIME_VALID.getPrepTime()));
        // Assert
        assertNull(SUT.prepTimeErrorMessage.get());
    }

    @Test
    public void startNewRecipeId_validPrepMinutes_recipeModelStatusVALID_CHANGED() {
        // Arrange
        when(intFromObservableMock.parseInt(anyObject(), eq(SUT.prepMinutesObservable), anyInt())).
                thenReturn(VALID_NEW_PREP_TIME_VALID.getPrepTime());
        // Act
        SUT.start(VALID_NEW_EMPTY.getId());
        simulateNothingReturnedFromDatabase();
        SUT.prepMinutesObservable.set(String.valueOf(VALID_NEW_PREP_TIME_VALID.getPrepTime()));
        // Assert
        verify(modelValidationSubmitterMock, times(2)).
                submitModelStatus(eq(VALID_CHANGED));
    }

    @Test
    public void startNewRecipeId_validPrepMinutes_prepMinutesSaved() {
        // Arrange
        when(intFromObservableMock.parseInt(anyObject(), eq(SUT.prepMinutesObservable), anyInt())).
                thenReturn(VALID_NEW_PREP_TIME_VALID.getPrepTime());
        when(timeProviderMock.getCurrentTimestamp()).
                thenReturn(VALID_NEW_PREP_TIME_VALID.getCreateDate());
        // Act
        SUT.start(VALID_NEW_EMPTY.getId());
        simulateNothingReturnedFromDatabase();
        SUT.prepMinutesObservable.set(String.valueOf(VALID_NEW_PREP_TIME_VALID.getPrepTime()));
        // Assert
        verify(durationEntityDataSourceMock, times(2)).
                save(eq(VALID_NEW_PREP_TIME_VALID));
    }

    @Test
    public void startNewRecipeId_validPrepHoursAndMinutes_prepTimeSaved() {
        // Arrange
        ArgumentCaptor<RecipeDurationEntity> ac = ArgumentCaptor.forClass(RecipeDurationEntity.class);
        when(timeProviderMock.getCurrentTimestamp()).thenReturn(
                VALID_NEW_PREP_TIME_VALID.getCreateDate());
        whenIntFromObserverMockReturnMaxPrepAndCookTimes();
        // Act
        SUT.start(VALID_NEW_EMPTY.getId());
        simulateNothingReturnedFromDatabase();
        SUT.prepHoursObservable.set(String.valueOf(VALID_NEW_PREP_TIME_VALID.getPrepTime() / 60));
        SUT.prepMinutesObservable.set(String.valueOf(VALID_NEW_PREP_TIME_VALID.getPrepTime() % 60));
        // Assert
        verify(durationEntityDataSourceMock, times(2)).save(ac.capture());
        assertEquals(VALID_NEW_PREP_TIME_VALID, ac.getAllValues().get(1));
    }

    @Test
    public void startNewRecipeId_validPrepHoursAndMinutes_recipeModelStatusVALID_CHANGED() {
        // Arrange
        whenIntFromObserverMockReturnMaxPrepAndCookTimes();
        // Act
        SUT.start(VALID_NEW_EMPTY.getId());
        simulateNothingReturnedFromDatabase();
        SUT.prepHoursObservable.set(String.valueOf(VALID_NEW_PREP_TIME_VALID.getPrepTime() / 60));
        SUT.prepMinutesObservable.set(String.valueOf(VALID_NEW_PREP_TIME_VALID.getPrepTime() % 60));
        verify(modelValidationSubmitterMock).submitModelStatus(VALID_CHANGED);
    }

    @Test
    public void startNewRecipeId_invalidPrepHoursAndMinutes_onlyValidPartOfPrepTimeSaved() {
        // Arrange
        ArgumentCaptor<RecipeDurationEntity> ac = ArgumentCaptor.forClass(RecipeDurationEntity.class);
        when(timeProviderMock.getCurrentTimestamp()).thenReturn(
                INVALID_NEW_PREP_TIME_INVALID.getCreateDate());
        whenIntFromObserverMockReturnMaxPrepAndCookTimesPlusOne();
        // Act
        SUT.start(VALID_NEW_EMPTY.getId());
        simulateNothingReturnedFromDatabase();
        SUT.prepHoursObservable.set(String.valueOf(
                INVALID_NEW_PREP_TIME_INVALID.getPrepTime() / 60));
        SUT.prepMinutesObservable.set(String.valueOf(
                INVALID_NEW_PREP_TIME_INVALID.getPrepTime() % 60));
        // Assert
        verify(durationEntityDataSourceMock, times(2)).save(ac.capture());
        assertEquals(VALID_NEW_PREP_TIME_VALID, ac.getAllValues().get(1));
    }

    @Test
    public void startNewRecipeId_invalidPrepHoursAndMinutes_recipeModelStatusINVALID_CHANGED() {
        // Arrange
        ArgumentCaptor<RecipeModelStatus> ac = ArgumentCaptor.forClass(RecipeModelStatus.class);
        whenIntFromObserverMockReturnMaxPrepAndCookTimesPlusOne();
        // Act
        SUT.start(VALID_NEW_EMPTY.getId());
        simulateNothingReturnedFromDatabase();
        SUT.prepHoursObservable.set(String.valueOf(MAX_PREP_TIME / 60));
        SUT.prepMinutesObservable.set(String.valueOf(MAX_PREP_TIME % 60 + 1));

        verify(modelValidationSubmitterMock, times(3)).submitModelStatus(ac.capture());
        verify(modelValidationSubmitterMock).submitModelStatus(eq(INVALID_CHANGED));
    }

    @Test
    public void startNewRecipeId_validCookHours_errorMessageObservableNull() {
        // Arrange
        whenIntFromObserverMockReturnMaxPrepAndCookTimes();
        // Act
        SUT.start(VALID_NEW_EMPTY.getId());
        simulateNothingReturnedFromDatabase();
        SUT.cookHoursObservable.set(String.valueOf(VALID_NEW_COOK_TIME_VALID.getCookTime() / 60));
        // Assert
        assertNull(SUT.cookTimeErrorMessage.get());
    }

    @Test
    public void startNewRecipeId_validCookHours_RecipeModelStatusVALID_CHANGED() {
        // Arrange
        whenIntFromObserverMockReturnMaxPrepAndCookTimes();
        when(timeProviderMock.getCurrentTimestamp()).thenReturn(
                VALID_NEW_COOK_TIME_VALID.getCreateDate());
        // Act
        SUT.start(VALID_NEW_EMPTY.getId());
        simulateNothingReturnedFromDatabase();
        SUT.cookHoursObservable.set(String.valueOf(VALID_NEW_COOK_TIME_VALID.getCookTime() / 60));
        // Assert
        verify(modelValidationSubmitterMock).submitModelStatus(eq(VALID_CHANGED));
    }

    @Test
    public void startNewRecipeId_validCookHours_cookHoursSaved() {
        // Arrange
        whenIntFromObserverMockReturnMaxPrepAndCookTimes();
        when(timeProviderMock.getCurrentTimestamp()).thenReturn(
                VALID_NEW_COOK_TIME_VALID.getCreateDate());
        // Act
        SUT.start(VALID_NEW_EMPTY.getId());
        simulateNothingReturnedFromDatabase();
        SUT.cookHoursObservable.set(String.valueOf(VALID_NEW_COOK_TIME_VALID.getCookTime() / 60));
        // Assert
        verify(durationEntityDataSourceMock).save(eq(VALID_NEW_COOK_TIME_VALID));
    }

    @Test
    public void startNewRecipeId_invalidCookHours_errorMessageSetToObservable() {
        // Arrange
        when(intFromObservableMock.parseInt(anyObject(), eq(SUT.cookHoursObservable), anyInt())).
                thenReturn(INVALID_NEW_COOK_TIME_INVALID.getCookTime() / 60 + 1);
        // Act
        SUT.start(VALID_NEW_EMPTY.getId());
        simulateNothingReturnedFromDatabase();
        SUT.cookHoursObservable.set(String.valueOf(
                INVALID_NEW_COOK_TIME_INVALID.getCookTime() / 60 + 1));
        // Assert
        assertEquals(ERROR_MESSAGE_TIME_TOO_LONG, SUT.cookTimeErrorMessage.get());
    }

    @Test
    public void startNewRecipeId_invalidCookHours_RecipeModelStatusINVALID_CHANGED() {
        // Arrange
        when(intFromObservableMock.parseInt(anyObject(), eq(SUT.cookHoursObservable), anyInt())).
                thenReturn(INVALID_NEW_COOK_TIME_INVALID.getCookTime() / 60 + 1);
        // Act
        SUT.start(VALID_NEW_EMPTY.getId());
        simulateNothingReturnedFromDatabase();
        SUT.cookHoursObservable.set(String.valueOf(
                INVALID_NEW_COOK_TIME_INVALID.getCookTime() / 60 + 1));
        // Assert
        verify(modelValidationSubmitterMock, times(2)).
                submitModelStatus(eq(INVALID_CHANGED));
    }

    @Test
    public void startNewRecipeId_invalidCookHours_invalidValueNotSaved() {
        // Arrange
        when(intFromObservableMock.parseInt(anyObject(), eq(SUT.cookHoursObservable), anyInt())).
                thenReturn(INVALID_NEW_COOK_TIME_INVALID.getCookTime() / 60 + 1);
        when(timeProviderMock.getCurrentTimestamp()).thenReturn(VALID_NEW_EMPTY.getCreateDate());
        // Act
        SUT.start(VALID_NEW_EMPTY.getId());
        simulateNothingReturnedFromDatabase();
        SUT.cookHoursObservable.set(String.valueOf(
                INVALID_NEW_COOK_TIME_INVALID.getCookTime() / 60 + 1));
        // Assert
        verify(durationEntityDataSourceMock).save(eq(VALID_NEW_EMPTY));
    }

    @Test
    public void startNewRecipeId_validCookMinutes_errorMessageObservableNull() {
        // Arrange
        when(intFromObservableMock.parseInt(anyObject(), eq(SUT.cookMinutesObservable), anyInt())).
                thenReturn(0, VALID_NEW_COOK_TIME_VALID.getCookTime());
        // Act
        SUT.start(VALID_NEW_EMPTY.getId());
        simulateNothingReturnedFromDatabase();
        SUT.cookMinutesObservable.set(String.valueOf(VALID_NEW_COOK_TIME_VALID.getCookTime()));
        // Assert
        assertNull(SUT.cookTimeErrorMessage.get());
    }

    @Test
    public void startNewRecipeId_validCookMinutes_RecipeModelStatusVALID_CHANGED() {
        // Arrange
        when(intFromObservableMock.parseInt(anyObject(), eq(SUT.cookMinutesObservable), anyInt())).
                thenReturn(0, VALID_NEW_COOK_TIME_VALID.getCookTime());
        // Act
        SUT.start(VALID_NEW_EMPTY.getId());
        simulateNothingReturnedFromDatabase();
        SUT.cookMinutesObservable.set(String.valueOf(VALID_NEW_COOK_TIME_VALID.getCookTime()));
        // Assert
        verify(modelValidationSubmitterMock).submitModelStatus(eq(VALID_CHANGED));
    }

    @Test
    public void startNewRecipeId_validCookMinutes_cookHoursSaved() {
        // Arrange
        when(intFromObservableMock.parseInt(anyObject(), eq(SUT.cookMinutesObservable), anyInt())).
                thenReturn(0, VALID_NEW_COOK_TIME_VALID.getCookTime());
        // Act
        SUT.start(VALID_NEW_EMPTY.getId());
        simulateNothingReturnedFromDatabase();
        SUT.cookMinutesObservable.set(String.valueOf(VALID_NEW_COOK_TIME_VALID.getCookTime()));
        // Assert

    }

    @Test
    public void startNewRecipeId_invalidCookMinutes_errorMessageSetToObservable() {
        // Arrange
        when(intFromObservableMock.parseInt(anyObject(), eq(SUT.cookMinutesObservable), anyInt())).
                thenReturn(0, INVALID_NEW_COOK_TIME_INVALID.getCookTime());
        // Act
        SUT.start(VALID_NEW_EMPTY.getId());
        simulateNothingReturnedFromDatabase();
        SUT.cookMinutesObservable.set(String.valueOf(INVALID_NEW_COOK_TIME_INVALID.getCookTime()));
        // Assert
        assertEquals(ERROR_MESSAGE_TIME_TOO_LONG, SUT.cookTimeErrorMessage.get());
    }

    @Test
    public void startNewRecipeId_invalidCookMinutes_RecipeModelStatusINVALID_CHANGED() {
        // Arrange
        when(intFromObservableMock.parseInt(anyObject(), eq(SUT.cookMinutesObservable), anyInt())).
                thenReturn(0, INVALID_NEW_COOK_TIME_INVALID.getCookTime());
        // Act
        SUT.start(VALID_NEW_EMPTY.getId());
        simulateNothingReturnedFromDatabase();
        SUT.cookMinutesObservable.set(String.valueOf(INVALID_NEW_COOK_TIME_INVALID.getCookTime()));
        // Assert
        verify(modelValidationSubmitterMock).submitModelStatus(INVALID_CHANGED);
    }

    @Test
    public void startNewRecipeId_invalidCookMinutes_invalidValueNotSaved() {
        // Arrange
        when(intFromObservableMock.parseInt(anyObject(), eq(SUT.cookMinutesObservable), anyInt())).
                thenReturn(0, INVALID_NEW_COOK_TIME_INVALID.getCookTime());
        when(timeProviderMock.getCurrentTimestamp()).
                thenReturn(INVALID_NEW_COOK_TIME_INVALID.getCreateDate());
        // Act
        SUT.start(VALID_NEW_EMPTY.getId());
        simulateNothingReturnedFromDatabase();
        SUT.cookMinutesObservable.set(String.valueOf(INVALID_NEW_COOK_TIME_INVALID.getCookTime()));
        // Assert
        verify(durationEntityDataSourceMock).save(VALID_NEW_EMPTY);
    }

    @Test
    public void startNewRecipeId_validCookHoursAndMinutes_errorMessageObservableNull() {
        // Arrange
        whenIntFromObserverMockReturnMaxPrepAndCookTimes();
        // Act
        SUT.start(VALID_NEW_EMPTY.getId());
        simulateNothingReturnedFromDatabase();
        SUT.cookHoursObservable.set(String.valueOf(VALID_NEW_COOK_TIME_VALID.getCookTime() / 60));
        SUT.cookMinutesObservable.set(String.valueOf(VALID_NEW_COOK_TIME_VALID.getCookTime() % 60));
        // Assert
        assertNull(SUT.cookTimeErrorMessage.get());
    }

    @Test
    public void startNewRecipeId_validCookHoursAndMinutes_recipeModelStatusVALID_CHANGED() {
        // Arrange
        whenIntFromObserverMockReturnMaxPrepAndCookTimes();
        when(timeProviderMock.getCurrentTimestamp()).
                thenReturn(VALID_NEW_COOK_TIME_VALID.getCreateDate());
        // Act
        SUT.start(VALID_NEW_EMPTY.getId());
        simulateNothingReturnedFromDatabase();
        SUT.cookHoursObservable.set(String.valueOf(VALID_NEW_COOK_TIME_VALID.getCookTime() / 60));
        SUT.cookMinutesObservable.set(String.valueOf(VALID_NEW_COOK_TIME_VALID.getCookTime() % 60));
        // Assert
        verify(modelValidationSubmitterMock).submitModelStatus(eq(VALID_CHANGED));
    }

    @Test
    public void startNewRecipeId_validCookHoursAndMinutes_cookTimeSaved() {
        // Arrange
        ArgumentCaptor<RecipeDurationEntity> ac = ArgumentCaptor.forClass(
                RecipeDurationEntity.class);
        whenIntFromObserverMockReturnMaxPrepAndCookTimes();
        when(timeProviderMock.getCurrentTimestamp()).
                thenReturn(VALID_NEW_COOK_TIME_VALID.getCreateDate());
        // Act
        SUT.start(VALID_NEW_EMPTY.getId());
        simulateNothingReturnedFromDatabase();
        SUT.cookHoursObservable.set(String.valueOf(VALID_NEW_COOK_TIME_VALID.getCookTime() / 60));
        SUT.cookMinutesObservable.set(String.valueOf(VALID_NEW_COOK_TIME_VALID.getCookTime() % 60));
        // Assert
        verify(durationEntityDataSourceMock, times(2)).save(ac.capture());
        assertEquals(VALID_NEW_COOK_TIME_VALID, ac.getValue());
    }

    @Test
    public void startNewRecipeId_invalidCookHoursAndMinutes_errorMessageSetToObservable() {
        // Arrange
        whenIntFromObserverMockReturnMaxPrepAndCookTimesPlusOne();
        // Act
        SUT.start(VALID_NEW_EMPTY.getId());
        simulateNothingReturnedFromDatabase();
        SUT.cookHoursObservable.set(String.valueOf(
                INVALID_NEW_COOK_TIME_INVALID.getCookTime() / 60));
        SUT.cookMinutesObservable.set(String.valueOf(
                INVALID_NEW_COOK_TIME_INVALID.getCookTime() % 60 + 1));
        // Assert
        assertEquals(ERROR_MESSAGE_TIME_TOO_LONG, SUT.cookTimeErrorMessage.get());
    }

    @Test
    public void startNewRecipeId_invalidCookHoursAndMinutes_RecipeModelStatusINVALID_CHANGED() {
        // Arrange
        whenIntFromObserverMockReturnMaxPrepAndCookTimesPlusOne();
        // Act
        SUT.start(VALID_NEW_EMPTY.getId());
        simulateNothingReturnedFromDatabase();
        SUT.cookHoursObservable.set(String.valueOf(
                INVALID_NEW_COOK_TIME_INVALID.getCookTime() / 60));
        SUT.cookMinutesObservable.set(String.valueOf(
                INVALID_NEW_COOK_TIME_INVALID.getCookTime() % 60 + 1));
        // Assert
        verify(modelValidationSubmitterMock).submitModelStatus(eq(INVALID_CHANGED));
    }

    @Test
    public void startNewRecipeId_invalidCookHoursAndMinutes_onlyValidPartOfPrepTimeSaved() {
        // Arrange
        whenIntFromObserverMockReturnMaxPrepAndCookTimesPlusOne();
        when(timeProviderMock.getCurrentTimestamp()).thenReturn(
                INVALID_NEW_COOK_TIME_INVALID.getCreateDate());
        // Act
        SUT.start(VALID_NEW_EMPTY.getId());
        simulateNothingReturnedFromDatabase();
        SUT.cookHoursObservable.set(String.valueOf(
                INVALID_NEW_COOK_TIME_INVALID.getCookTime() / 60));
        SUT.cookMinutesObservable.set(String.valueOf(
                INVALID_NEW_COOK_TIME_INVALID.getCookTime() % 60 + 1));
        // Assert
        verify(durationEntityDataSourceMock).save(VALID_NEW_EMPTY);
    }

    @Test
    public void start_validExistingRecipeId_prepHoursSetToObservable() {
        // Arrange
        String prepHours = String.valueOf(VALID_EXISTING_COMPLETE.getPrepTime() / 60);
        // Act
        SUT.start(VALID_EXISTING_COMPLETE.getId());
        simulateGetValidExistingCompleteFromDatabase();
        // Assert
        assertEquals(prepHours, SUT.prepHoursObservable.get());
    }

    @Test
    public void start_validExistingRecipeId_prepMinutesSetToObservable() {
        // Arrange
        String prepMinutes = String.valueOf(VALID_EXISTING_COMPLETE.getPrepTime() % 60);
        // Act
        SUT.start(VALID_EXISTING_COMPLETE.getId());
        simulateGetValidExistingCompleteFromDatabase();
        // Assert
        assertEquals(prepMinutes, SUT.prepMinutesObservable.get());
    }

    @Test
    public void start_validExistingRecipeId_cookHoursSetToObservable() {
        // Arrange
        String cookHours = String.valueOf(VALID_EXISTING_COMPLETE.getCookTime() / 60);
        // Act
        SUT.start(VALID_EXISTING_COMPLETE.getId());
        simulateGetValidExistingCompleteFromDatabase();
        // Assert
        assertEquals(cookHours, SUT.cookHoursObservable.get());
    }

    @Test
    public void start_validExistingRecipeId_cookMinutesSetToObservable() {
        // Arrange
        String cookMinutes = String.valueOf(VALID_EXISTING_COMPLETE.getCookTime() % 60);
        // Act
        SUT.start(VALID_EXISTING_COMPLETE.getId());
        simulateGetValidExistingCompleteFromDatabase();
        // Assert
        assertEquals(cookMinutes, SUT.cookMinutesObservable.get());
    }

    @Test
    public void start_validExistingRecipeId_RecipeModelStatusVALID_UNCHANGED() {
        ArgumentCaptor<RecipeModelStatus> ac = ArgumentCaptor.forClass(RecipeModelStatus.class);
        when(timeProviderMock.getCurrentTimestamp()).thenReturn(
                VALID_EXISTING_COMPLETE.getCreateDate());
        whenIntFromObserverMockReturnExistingValidCompleteRecipeTimes();
        // Act
        SUT.start(VALID_EXISTING_COMPLETE.getId());
        simulateGetValidExistingCompleteFromDatabase();
        // Assert
        verify(modelValidationSubmitterMock).submitModelStatus(ac.capture());
        RecipeModelStatus modelStatus = ac.getValue();
        assertEquals(VALID_UNCHANGED, modelStatus);


    }

    @Test
    public void startWithCloned_existingAndNewRecipeId_databaseCalledWithExistingId() {
        // Arrange
        SUT.startByCloningModel(
                VALID_COMPLETE_FROM_ANOTHER_USER.getId(), VALID_NEW_EMPTY.getId());
        // Assert
        verify(durationEntityDataSourceMock).getById(
                eq(VALID_COMPLETE_FROM_ANOTHER_USER.getId()), eq(SUT));
    }

    @Test
    public void startWithCloned_existingAndNewRecipeId_existingFromAnotherUserCopiedAndSavedWithNewId() {
        // Arrange
        when(timeProviderMock.getCurrentTimestamp()).thenReturn(VALID_NEW_CLONED.getCreateDate());
        whenIntFromObserverMockReturnRecipeTimesFromAnotherUser();
        // Act
        SUT.startByCloningModel(
                VALID_COMPLETE_FROM_ANOTHER_USER.getId(), VALID_NEW_EMPTY.getId());
        simulateGetValidEntityFromAnotherUserFromDatabase();
        // Assert
        verify(durationEntityDataSourceMock).save(eq(VALID_NEW_CLONED));
    }

    @Test
    public void startWithCloned_prepTimeChanged_savedWithUpdatedPrepTime() {
        // Arrange
        ArgumentCaptor<RecipeDurationEntity> ac = ArgumentCaptor.forClass(RecipeDurationEntity.class);
        when(timeProviderMock.getCurrentTimestamp()).thenReturn(
                VALID_NEW_CLONED_PREP_TIME_UPDATED.getCreateDate());
        whenIntFromObserverMockReturnClonedPrepTimeUpdated();
        // Act
        SUT.startByCloningModel(
                VALID_COMPLETE_FROM_ANOTHER_USER.getId(), VALID_NEW_EMPTY.getId());
        simulateGetValidEntityFromAnotherUserFromDatabase();
        SUT.prepHoursObservable.set(
                String.valueOf(VALID_NEW_CLONED_PREP_TIME_UPDATED.getPrepTime() / 60));
        SUT.prepMinutesObservable.set(
                String.valueOf(VALID_NEW_CLONED_PREP_TIME_UPDATED.getPrepTime() % 60));
        // Assert
        verify(durationEntityDataSourceMock, times(3)).save(ac.capture());
        assertEquals(VALID_NEW_CLONED_PREP_TIME_UPDATED, ac.getAllValues().get(2));
    }

    @Test
    public void startWithCloned_modelFromAnotherUserNotAvailable_newModelCreatedAndSavedWithNewId() {
        // Arrange
        when(timeProviderMock.getCurrentTimestamp()).thenReturn(
                VALID_NEW_EMPTY.getCreateDate());
        // Act
        SUT.startByCloningModel(
                INVALID_COMPLETE_FROM_ANOTHER_USER.getId(), VALID_NEW_EMPTY.getId());

        verify(durationEntityDataSourceMock).getById(eq(INVALID_COMPLETE_FROM_ANOTHER_USER.getId()),
                getEntityCallbackArgumentCaptor.capture());
        getEntityCallbackArgumentCaptor.getValue().onDataNotAvailable();

        // Assert
        verify(durationEntityDataSourceMock).save(eq(VALID_NEW_EMPTY));
    }

    // region helper methods -----------------------------------------------------------------------
    private void setupResourceMockReturnValues() {
        when(resourcesMock.getInteger(R.integer.recipe_max_cook_time_in_minutes)).thenReturn(MAX_PREP_TIME);
        when(resourcesMock.getInteger(R.integer.recipe_max_prep_time_in_minutes)).thenReturn(MAX_COOK_TIME);
        when(resourcesMock.getString(R.string.input_error_recipe_prep_time_too_long)).thenReturn(ERROR_MESSAGE_TIME_TOO_LONG);
        when(resourcesMock.getString(R.string.input_error_recipe_cook_time_too_long)).thenReturn(ERROR_MESSAGE_TIME_TOO_LONG);
    }

    private void simulateNothingReturnedFromDatabase() {
        verify(durationEntityDataSourceMock).getById(eq(VALID_NEW_EMPTY.getId()),
                getEntityCallbackArgumentCaptor.capture());

        getEntityCallbackArgumentCaptor.getValue().onDataNotAvailable();
    }

    private void simulateGetValidExistingCompleteFromDatabase() {
        verify(durationEntityDataSourceMock).getById(eq(VALID_EXISTING_COMPLETE.getId()),
                getEntityCallbackArgumentCaptor.capture());

        getEntityCallbackArgumentCaptor.getValue().onEntityLoaded(VALID_EXISTING_COMPLETE);
    }

    private void simulateGetValidEntityFromAnotherUserFromDatabase() {
        verify(durationEntityDataSourceMock).getById(
                eq(VALID_COMPLETE_FROM_ANOTHER_USER.getId()),
                getEntityCallbackArgumentCaptor.capture());

        getEntityCallbackArgumentCaptor.getValue().onEntityLoaded(
                VALID_COMPLETE_FROM_ANOTHER_USER);
    }

    private void whenIntFromObserverMockReturnExistingValidCompleteRecipeTimes() {
        when(intFromObservableMock.parseInt(anyObject(), eq(SUT.prepHoursObservable), anyInt())).
                thenReturn(VALID_EXISTING_COMPLETE.getPrepTime() / 60);
        when(intFromObservableMock.parseInt(anyObject(), eq(SUT.prepMinutesObservable), anyInt())).
                thenReturn(VALID_EXISTING_COMPLETE.getPrepTime() % 60);
        when(intFromObservableMock.parseInt(anyObject(), eq(SUT.cookHoursObservable), anyInt())).
                thenReturn(VALID_EXISTING_COMPLETE.getCookTime() / 60);
        when(intFromObservableMock.parseInt(anyObject(), eq(SUT.cookMinutesObservable), anyInt())).
                thenReturn(VALID_EXISTING_COMPLETE.getCookTime() % 60);
    }

    private void whenIntFromObserverMockReturnMaxPrepAndCookTimes() {
        when(intFromObservableMock.parseInt(anyObject(), eq(SUT.prepHoursObservable), anyInt())).
                thenReturn(0, MAX_PREP_TIME / 60);
        when(intFromObservableMock.parseInt(anyObject(), eq(SUT.prepMinutesObservable), anyInt())).
                thenReturn(0, MAX_PREP_TIME % 60);
        when(intFromObservableMock.parseInt(anyObject(), eq(SUT.cookHoursObservable), anyInt())).
                thenReturn(0, MAX_COOK_TIME / 60);
        when(intFromObservableMock.parseInt(anyObject(), eq(SUT.cookMinutesObservable), anyInt())).
                thenReturn(0, MAX_COOK_TIME % 60);
    }

    private void whenIntFromObserverMockReturnMaxPrepAndCookTimesPlusOne() {
        when(intFromObservableMock.parseInt(anyObject(), eq(SUT.prepHoursObservable), anyInt())).
                thenReturn(0, MAX_PREP_TIME / 60);
        when(intFromObservableMock.parseInt(anyObject(), eq(SUT.prepMinutesObservable), anyInt())).
                thenReturn(0, MAX_PREP_TIME % 60 + 1);
        when(intFromObservableMock.parseInt(anyObject(), eq(SUT.cookHoursObservable), anyInt())).
                thenReturn(0, MAX_COOK_TIME / 60);
        when(intFromObservableMock.parseInt(anyObject(), eq(SUT.cookMinutesObservable), anyInt())).
                thenReturn(0, MAX_COOK_TIME % 60 + 1);
    }

    private void whenIntFromObserverMockReturnRecipeTimesFromAnotherUser() {
        when(intFromObservableMock.parseInt(anyObject(), eq(SUT.cookHoursObservable), anyInt())).
                thenReturn(VALID_COMPLETE_FROM_ANOTHER_USER.getCookTime() / 60);
        when(intFromObservableMock.parseInt(anyObject(), eq(SUT.cookMinutesObservable), anyInt())).
                thenReturn(VALID_COMPLETE_FROM_ANOTHER_USER.getCookTime() % 60);
        when(intFromObservableMock.parseInt(anyObject(), eq(SUT.prepHoursObservable), anyInt())).
                thenReturn(VALID_COMPLETE_FROM_ANOTHER_USER.getPrepTime() / 60);
        when(intFromObservableMock.parseInt(anyObject(), eq(SUT.prepMinutesObservable), anyInt())).
                thenReturn(VALID_COMPLETE_FROM_ANOTHER_USER.getPrepTime() % 60);
    }

    private void whenIntFromObserverMockReturnClonedPrepTimeUpdated() {
        when(intFromObservableMock.parseInt(anyObject(), eq(SUT.prepHoursObservable), anyInt())).
                thenReturn(VALID_NEW_CLONED_PREP_TIME_UPDATED.getPrepTime() / 60);
        when(intFromObservableMock.parseInt(anyObject(), eq(SUT.prepMinutesObservable), anyInt())).
                thenReturn(VALID_NEW_CLONED_PREP_TIME_UPDATED.getPrepTime() % 60);
        when(intFromObservableMock.parseInt(anyObject(), eq(SUT.cookHoursObservable), anyInt())).
                thenReturn(VALID_NEW_CLONED_PREP_TIME_UPDATED.getCookTime() / 60);
        when(intFromObservableMock.parseInt(anyObject(), eq(SUT.cookMinutesObservable), anyInt())).
                thenReturn(VALID_NEW_CLONED_PREP_TIME_UPDATED.getCookTime() % 60);
    }
    // endregion helper methods --------------------------------------------------------------------

    // region helper classes -----------------------------------------------------------------------
    // endregion helper classes --------------------------------------------------------------------


}