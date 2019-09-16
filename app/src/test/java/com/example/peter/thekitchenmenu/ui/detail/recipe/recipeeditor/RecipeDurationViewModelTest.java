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

    private static final RecipeModelStatus UNCHANGED_INVALID =
            RecipeValidatorTestData.getDurationModelStatusUnchangedInvalid();
    private static final RecipeModelStatus UNCHANGED_VALID =
            RecipeValidatorTestData.getDurationModelStatusUnchangedValid();
    private static final RecipeModelStatus CHANGED_INVALID =
            RecipeValidatorTestData.getDurationModelStatusChangedInvalid();
    private static final RecipeModelStatus CHANGED_VALID =
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
    public void start_newRecipeId_newEmptyValuesSetToObservers() {
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
    public void start_newRecipeId_newEmptyEntitySaved() {
        // Arrange
        when(timeProviderMock.getCurrentTimestamp()).thenReturn(VALID_NEW_EMPTY.getCreateDate());
        // Act
        SUT.start(VALID_NEW_EMPTY.getId());
        simulateNothingReturnedFromDatabase();
        // Assert
        verify(durationEntityDataSourceMock).save(eq(VALID_NEW_EMPTY));
    }

    @Test
    public void start_newRecipeId_RecipeModelStatusUNCHANGED_VALID() {
        // Arrange
        ArgumentCaptor<RecipeModelStatus> ac = ArgumentCaptor.forClass(RecipeModelStatus.class);
        when(timeProviderMock.getCurrentTimestamp()).thenReturn(VALID_NEW_EMPTY.getCreateDate());
        // Act
        SUT.start(VALID_NEW_EMPTY.getId());
        simulateNothingReturnedFromDatabase();
        // Assert
        verify(modelValidationSubmitterMock).submitModelStatus(ac.capture());
        RecipeModelStatus modelStatus = ac.getValue();
        assertEquals(UNCHANGED_VALID, modelStatus);
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
    // startNewRecipeId_validPrepHoursEntered_recipeModelStatusCHANGED_VALID
    // startNewRecipeId_validPrepHoursEntered_prepHoursSaved
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
    // startNewRecipeId_invalidPrepHoursEntered_invalidValueNotSaved()
    // startNewRecipeId_invalidPrepHoursEntered_recipeModelStatusCHANGED_INVALID
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
    // startNewRecipeId_validPrepMinutesEntered_recipeModelStatusCHANGED_VALID
    // startNewRecipeId_validPrepMinutesEntered_preMinutesSaved
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
    // startNewRecipeId_invalidPrepMinutesEntered_recipeModelStatusCHANGED_INVALID
    // startNewRecipeId_invalidPrepMinutesEntered_invalidValueNotSaved
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
    public void startNewRecipeId_validPrepHoursAndMinutes_recipeModelStatusCHANGED_VALID() {
        // Arrange
        whenIntFromObserverMockReturnMaxPrepAndCookTimes();
        // Act
        SUT.start(VALID_NEW_EMPTY.getId());
        simulateNothingReturnedFromDatabase();
        SUT.prepHoursObservable.set(String.valueOf(VALID_NEW_PREP_TIME_VALID.getPrepTime() / 60));
        SUT.prepMinutesObservable.set(String.valueOf(VALID_NEW_PREP_TIME_VALID.getPrepTime() % 60));
        verify(modelValidationSubmitterMock).submitModelStatus(CHANGED_VALID);
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
    public void startNewRecipeId_invalidPrepHoursAndMinutes_recipeModelStatusCHANGED_INVALID() {
        // Arrange
        ArgumentCaptor<RecipeModelStatus> ac = ArgumentCaptor.forClass(RecipeModelStatus.class);
        whenIntFromObserverMockReturnMaxPrepAndCookTimesPlusOne();
        // Act
        SUT.start(VALID_NEW_EMPTY.getId());
        simulateNothingReturnedFromDatabase();
        SUT.prepHoursObservable.set(String.valueOf(MAX_PREP_TIME / 60));
        SUT.prepMinutesObservable.set(String.valueOf(MAX_PREP_TIME % 60 + 1));

        verify(modelValidationSubmitterMock, times(3)).submitModelStatus(ac.capture());
        verify(modelValidationSubmitterMock).submitModelStatus(eq(CHANGED_INVALID));
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
    // startNewRecipeId_validCookHours_RecipeModelStatusCHANGED_VALID
    // startNewRecipeId_validCookHours_cookHoursSaved
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
    // startNewRecipeId_invalidCookHours_RecipeModelStatusCHANGED_INVALID
    // startNewRecipeId_invalidCookHours_invalidValueNotSaved
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
    // startNewRecipeId_validCookMinutes_RecipeModelStatusCHANGED_VALID
    // startNewRecipeId_validCookMinutes_cookHoursSaved
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
    // startNewRecipeId_invalidCookMinutes_RecipeModelStatusCHANGED_INVALID
    // startNewRecipeId_invalidCookMinutes_invalidValueNotSaved

    // startNewRecipeId_validCookHoursAndMinutes_errorMessageObservableNull
    // startNewRecipeId_validCookHoursAndMinutes_recipeModelStatusCHANGED_VALID
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

    // startNewRecipeId_invalidCookHoursAndMinutes_RecipeModelStatusCHANGED_INVALID
    // startNewRecipeId_invalidCookHoursAndMinutes_onlyValidPartOfPrepTimeSaved

    @Test
    public void start_validExistingCompleteRecipeId_prepHoursSetToObservable() {
        // Arrange
        String prepHours = String.valueOf(VALID_EXISTING_COMPLETE.getPrepTime() / 60);
        // Act
        SUT.start(VALID_EXISTING_COMPLETE.getId());
        simulateGetValidExistingCompleteFromDatabase();
        // Assert
        assertEquals(prepHours, SUT.prepHoursObservable.get());
    }

    @Test
    public void start_validExistingCompleteRecipeId_prepMinutesSetToObservable() {
        // Arrange
        String prepMinutes = String.valueOf(VALID_EXISTING_COMPLETE.getPrepTime() % 60);
        // Act
        SUT.start(VALID_EXISTING_COMPLETE.getId());
        simulateGetValidExistingCompleteFromDatabase();
        // Assert
        assertEquals(prepMinutes, SUT.prepMinutesObservable.get());
    }

    @Test
    public void start_validExistingCompleteRecipeId_cookHoursSetToObservable() {
        // Arrange
        String cookHours = String.valueOf(VALID_EXISTING_COMPLETE.getCookTime() / 60);
        // Act
        SUT.start(VALID_EXISTING_COMPLETE.getId());
        simulateGetValidExistingCompleteFromDatabase();
        // Assert
        assertEquals(cookHours, SUT.cookHoursObservable.get());
    }

    @Test
    public void start_validExistingCompleteRecipeId_cookMinutesSetToObservable() {
        // Arrange
        String cookMinutes = String.valueOf(VALID_EXISTING_COMPLETE.getCookTime() % 60);
        // Act
        SUT.start(VALID_EXISTING_COMPLETE.getId());
        simulateGetValidExistingCompleteFromDatabase();
        // Assert
        assertEquals(cookMinutes, SUT.cookMinutesObservable.get());
    }

    @Test
    public void start_validExistingCompleteRecipeId_RecipeModelStatusUNCHANGED_VALID() {
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
        assertEquals(UNCHANGED_VALID, modelStatus);


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
    public void startWithCloned_existingAndNewRecipeId_existingSavedWithNewId() {
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
    public void startWithCloned_prepTimeChanged_savedWitUpdatedPrepTime() {
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
    public void startWithCloned_modelFromAnotherUserNotAvailable_newModelCreatedAndSaved() throws Exception {
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