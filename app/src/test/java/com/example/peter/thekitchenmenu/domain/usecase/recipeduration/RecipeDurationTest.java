package com.example.peter.thekitchenmenu.domain.usecase.recipeduration;

import com.example.peter.thekitchenmenu.commonmocks.UseCaseSchedulerMock;
import com.example.peter.thekitchenmenu.data.entity.RecipeDurationEntity;
import com.example.peter.thekitchenmenu.data.repository.DataSource;
import com.example.peter.thekitchenmenu.data.repository.RepositoryRecipeDuration;
import com.example.peter.thekitchenmenu.domain.usecase.UseCaseHandler;
import com.example.peter.thekitchenmenu.domain.usecase.recipe.recipestate.RecipeStateCalculator;
import com.example.peter.thekitchenmenu.domain.utils.TimeProvider;
import com.example.peter.thekitchenmenu.testdata.TestDataRecipeDurationEntity;

import org.junit.*;
import org.mockito.*;

import static com.example.peter.thekitchenmenu.domain.usecase.UseCaseCommand.*;
import static com.example.peter.thekitchenmenu.domain.usecase.recipe.Recipe.DO_NOT_CLONE;

import static com.example.peter.thekitchenmenu.domain.usecase.recipeduration.RecipeDuration.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

public class RecipeDurationTest {

    // region constants ----------------------------------------------------------------------------
    private static final RecipeDurationEntity VALID_NEW_EMPTY =
            TestDataRecipeDurationEntity.getValidNewEmpty();
    private static final RecipeDurationEntity VALID_NEW_PREP_TIME_VALID =
            TestDataRecipeDurationEntity.getValidNewPrepTimeValid();
    private static final RecipeDurationEntity VALID_NEW_COOK_TIME_VALID =
            TestDataRecipeDurationEntity.getValidNewCookTimeValid();
    private static final RecipeDurationEntity VALID_EXISTING_COMPLETE =
            TestDataRecipeDurationEntity.getValidExistingComplete();
    private static final RecipeDurationEntity VALID_COMPLETE_FROM_ANOTHER_USER =
            TestDataRecipeDurationEntity.getValidCompleteFromAnotherUser();
    private static final RecipeDurationEntity VALID_NEW_CLONED =
            TestDataRecipeDurationEntity.getValidNewCloned();
    private static final RecipeDurationEntity VALID_NEW_CLONED_PREP_TIME_UPDATED =
            TestDataRecipeDurationEntity.getValidNewClonedPrepTimeUpdated();
    // endregion constants -------------------------------------------------------------------------

    // region helper fields ------------------------------------------------------------------------
    private UseCaseHandler handler;
    private RecipeDurationResponse durationOnSuccessResponse;
    private RecipeDurationResponse durationOnErrorResponse;
    @Mock
    RepositoryRecipeDuration repoMock;
    @Captor
    ArgumentCaptor<DataSource.GetEntityCallback<RecipeDurationEntity>> repoCallback;
    @Mock
    TimeProvider timeProviderMock;
    public static int MAX_PREP_TIME = 6000;
    public static int MAX_COOK_TIME = 6000;
    // endregion helper fields ---------------------------------------------------------------------

    private RecipeDuration SUT;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        SUT = givenUseCase();
    }

    private RecipeDuration givenUseCase() {
        handler = new UseCaseHandler(new UseCaseSchedulerMock());
        return new RecipeDuration(repoMock, timeProviderMock, MAX_PREP_TIME, MAX_COOK_TIME);
    }

    @Test
    public void startNewRecipeId_componentStateDATA_UNAVAILABLE() {
        // Arrange
        String recipeId = VALID_NEW_EMPTY.getId();
        // Act
        givenNewModel(recipeId);
        // Assert
        assertEquals(RecipeStateCalculator.ComponentState.DATA_UNAVAILABLE,
                durationOnErrorResponse.getState());
    }

    @Test
    public void startNewRecipeId_invalidPrepHours_invalidValueNotSaved() {
        // Arrange
        String recipeId = VALID_NEW_EMPTY.getId();
        givenNewModel(recipeId);
        RecipeDurationRequest.Model invalidModel = RecipeDurationRequest.Model.Builder.
                basedOnDurationResponseModel(durationOnErrorResponse.getModel()).
                setPrepHours(MAX_PREP_TIME / 60 + 1).
                build();
        RecipeDurationRequest request = new RecipeDurationRequest.Builder().
                setRecipeId(recipeId).
                setCloneToRecipeId(DO_NOT_CLONE).
                setModel(invalidModel).
                build();
        // Act
        handler.execute(SUT, request, getUseCaseCallback());
        // Assert
        verifyNoMoreInteractions(repoMock);
    }

    @Test
    public void startNewRecipeId_invalidPrepHours_resultINVALID_CHANGED() {
        // Arrange
        String recipeId = VALID_NEW_EMPTY.getId();
        givenNewModel(recipeId);
        RecipeDurationRequest.Model invalidModel = RecipeDurationRequest.Model.Builder.
                basedOnDurationResponseModel(durationOnErrorResponse.getModel()).
                setPrepHours(MAX_PREP_TIME / 60 + 1).
                build();
        RecipeDurationRequest request = new RecipeDurationRequest.Builder().
                setRecipeId(recipeId).
                setCloneToRecipeId(DO_NOT_CLONE).
                setModel(invalidModel).
                build();
        // Act
        handler.execute(SUT, request, getUseCaseCallback());
        // Assert
        assertEquals(RecipeStateCalculator.ComponentState.INVALID_CHANGED,
                durationOnErrorResponse.getState());
    }

    @Test
    public void startNewRecipeId_invalidPrepHours_FaiReasonINVALID_PREP_TIME() {
        // Arrange
        String recipeId = VALID_NEW_EMPTY.getId();
        givenNewModel(recipeId);
        RecipeDurationRequest.Model invalidModel = RecipeDurationRequest.Model.Builder.
                basedOnDurationResponseModel(durationOnErrorResponse.getModel()).
                setPrepHours(MAX_PREP_TIME / 60 + 1).
                build();
        RecipeDurationRequest request = new RecipeDurationRequest.Builder().
                setRecipeId(recipeId).
                setCloneToRecipeId(DO_NOT_CLONE).
                setModel(invalidModel).
                build();
        // Act
        handler.execute(SUT, request, getUseCaseCallback());
        // Assert
        assertEquals(1, durationOnErrorResponse.getFailReasons().size());
        assertTrue(durationOnErrorResponse.getFailReasons().contains(FailReason.INVALID_PREP_TIME));
    }

    @Test
    public void startNewRecipeId_validPrepHours_resultVALID_CHANGED() {
        // Arrange
        String recipeId = VALID_NEW_EMPTY.getId();
        givenNewModel(recipeId);
        RecipeDurationRequest.Model validModel = RecipeDurationRequest.Model.Builder.
                basedOnDurationResponseModel(durationOnErrorResponse.getModel()).
                setPrepHours(VALID_NEW_PREP_TIME_VALID.getPrepTime() / 60).
                build();
        RecipeDurationRequest validRequest = new RecipeDurationRequest.Builder().
                setRecipeId(recipeId).
                setCloneToRecipeId(DO_NOT_CLONE).
                setModel(validModel).
                build();

        // Act
        handler.execute(SUT, validRequest, getUseCaseCallback());
        // Assert
        assertEquals(RecipeStateCalculator.ComponentState.VALID_CHANGED,
                durationOnSuccessResponse.getState());
    }

    @Test
    public void startNewRecipeId_validPrepHours_failReasonNONE() {
        // Arrange
        String recipeId = VALID_NEW_EMPTY.getId();
        givenNewModel(recipeId);
        RecipeDurationRequest.Model validModel = RecipeDurationRequest.Model.Builder.
                basedOnDurationResponseModel(durationOnErrorResponse.getModel()).
                setPrepHours(VALID_NEW_PREP_TIME_VALID.getPrepTime() / 60).
                build();
        RecipeDurationRequest validRequest = new RecipeDurationRequest.Builder().
                setRecipeId(recipeId).
                setCloneToRecipeId(DO_NOT_CLONE).
                setModel(validModel).
                build();

        // Act
        handler.execute(SUT, validRequest, getUseCaseCallback());
        // Assert
        assertTrue(durationOnSuccessResponse.getFailReasons().contains(FailReason.NONE));
    }

    @Test
    public void startNewRecipeId_validPrepHours_prepHoursSaved() {
        // Arrange
        String recipeId = VALID_NEW_EMPTY.getId();
        whenTimeProviderCalledReturn(VALID_NEW_PREP_TIME_VALID.getCreateDate());
        givenNewModel(recipeId);
        RecipeDurationRequest.Model validModel = RecipeDurationRequest.Model.Builder.
                basedOnDurationResponseModel(durationOnErrorResponse.getModel()).
                setPrepHours(VALID_NEW_PREP_TIME_VALID.getPrepTime() / 60).
                build();
        RecipeDurationRequest validRequest = new RecipeDurationRequest.Builder().
                setRecipeId(recipeId).
                setCloneToRecipeId(DO_NOT_CLONE).
                setModel(validModel).
                build();

        // Act
        handler.execute(SUT, validRequest, getUseCaseCallback());
        // Assert
        verify(repoMock).save(eq(VALID_NEW_PREP_TIME_VALID));
    }

    @Test
    public void startNewRecipeId_invalidPrepMinutes_invalidValueNotSaved() {
        // Arrange
        String recipeId = VALID_NEW_EMPTY.getId();
        givenNewModel(recipeId);
        RecipeDurationRequest.Model invalidModel = RecipeDurationRequest.Model.Builder.
                basedOnDurationResponseModel(durationOnErrorResponse.getModel()).
                setPrepMinutes(MAX_PREP_TIME + 1).
                build();
        RecipeDurationRequest request = new RecipeDurationRequest.Builder().
                setRecipeId(recipeId).
                setCloneToRecipeId(DO_NOT_CLONE).
                setModel(invalidModel).
                build();
        // Act
        handler.execute(SUT, request, getUseCaseCallback());
        // Assert
        verifyNoMoreInteractions(repoMock);
    }

    @Test
    public void startNewRecipeId_invalidPrepMinutes_resultINVALID_CHANGED() {
        // Arrange
        String recipeId = VALID_NEW_EMPTY.getId();
        givenNewModel(recipeId);
        RecipeDurationRequest.Model invalidModel = RecipeDurationRequest.Model.Builder.
                basedOnDurationResponseModel(durationOnErrorResponse.getModel()).
                setPrepMinutes(MAX_PREP_TIME + 1).
                build();
        RecipeDurationRequest request = new RecipeDurationRequest.Builder().
                setRecipeId(recipeId).
                setCloneToRecipeId(DO_NOT_CLONE).
                setModel(invalidModel).
                build();
        // Act
        handler.execute(SUT, request, getUseCaseCallback());
        // Assert
        assertEquals(RecipeStateCalculator.ComponentState.INVALID_CHANGED,
                durationOnErrorResponse.getState());
    }

    @Test
    public void startNewRecipeId_invalidPrepMinutes_FaiReasonINVALID_PREP_TIME() {
        // Arrange
        String recipeId = VALID_NEW_EMPTY.getId();
        givenNewModel(recipeId);
        RecipeDurationRequest.Model invalidModel = RecipeDurationRequest.Model.Builder.
                basedOnDurationResponseModel(durationOnErrorResponse.getModel()).
                setPrepMinutes(MAX_PREP_TIME + 1).
                build();
        RecipeDurationRequest request = new RecipeDurationRequest.Builder().
                setRecipeId(recipeId).
                setCloneToRecipeId(DO_NOT_CLONE).
                setModel(invalidModel).
                build();
        // Act
        handler.execute(SUT, request, getUseCaseCallback());
        // Assert
        assertEquals(1, durationOnErrorResponse.getFailReasons().size());
        assertTrue(durationOnErrorResponse.getFailReasons().contains(FailReason.INVALID_PREP_TIME));
    }

    @Test
    public void startNewRecipeId_validPrepMinutes_resultVALID_CHANGED() {
        // Arrange
        String recipeId = VALID_NEW_EMPTY.getId();
        givenNewModel(recipeId);
        RecipeDurationRequest.Model validModel = RecipeDurationRequest.Model.Builder.
                basedOnDurationResponseModel(durationOnErrorResponse.getModel()).
                setPrepMinutes(VALID_NEW_PREP_TIME_VALID.getPrepTime()).
                build();
        RecipeDurationRequest validRequest = new RecipeDurationRequest.Builder().
                setRecipeId(recipeId).
                setCloneToRecipeId(DO_NOT_CLONE).
                setModel(validModel).
                build();
        // Act
        handler.execute(SUT, validRequest, getUseCaseCallback());
        // Assert
        assertEquals(RecipeStateCalculator.ComponentState.VALID_CHANGED,
                durationOnSuccessResponse.getState());
    }

    @Test
    public void startNewRecipeId_validPrepMinutes_failReasonNONE() {
        // Arrange
        String recipeId = VALID_NEW_EMPTY.getId();
        givenNewModel(recipeId);
        RecipeDurationRequest.Model validModel = RecipeDurationRequest.Model.Builder.
                basedOnDurationResponseModel(durationOnErrorResponse.getModel()).
                setPrepMinutes(VALID_NEW_PREP_TIME_VALID.getPrepTime()).
                build();
        RecipeDurationRequest validRequest = new RecipeDurationRequest.Builder().
                setRecipeId(recipeId).
                setCloneToRecipeId(DO_NOT_CLONE).
                setModel(validModel).
                build();
        // Act
        handler.execute(SUT, validRequest, getUseCaseCallback());
        // Assert
        assertEquals(1, durationOnSuccessResponse.getFailReasons().size());
        assertTrue(durationOnSuccessResponse.getFailReasons().contains(FailReason.NONE));
    }

    @Test
    public void startNewRecipeId_validPrepMinutes_prepMinutesSaved() {
        // Arrange
        String recipeId = VALID_NEW_EMPTY.getId();
        whenTimeProviderCalledReturn(VALID_NEW_PREP_TIME_VALID.getCreateDate());
        givenNewModel(recipeId);
        RecipeDurationRequest.Model validModel = RecipeDurationRequest.Model.Builder.
                basedOnDurationResponseModel(durationOnErrorResponse.getModel()).
                setPrepMinutes(VALID_NEW_PREP_TIME_VALID.getPrepTime()).
                build();
        RecipeDurationRequest validRequest = new RecipeDurationRequest.Builder().
                setRecipeId(recipeId).
                setCloneToRecipeId(DO_NOT_CLONE).
                setModel(validModel).
                build();
        // Act
        handler.execute(SUT, validRequest, getUseCaseCallback());
        // Assert
        verify(repoMock).save(eq(VALID_NEW_PREP_TIME_VALID));
    }

    @Test
    public void startNewRecipeId_invalidCookHours_invalidValueNotSaved() {
        // Arrange
        String recipeId = VALID_NEW_EMPTY.getId();
        givenNewModel(recipeId);
        RecipeDurationRequest.Model invalidModel = RecipeDurationRequest.Model.Builder.
                basedOnDurationResponseModel(durationOnErrorResponse.getModel()).
                setCookHours(MAX_COOK_TIME / 60 + 1).
                build();
        RecipeDurationRequest invalidRequest = new RecipeDurationRequest.Builder().
                setRecipeId(recipeId).
                setCloneToRecipeId(DO_NOT_CLONE).
                setModel(invalidModel).
                build();
        // Act
        handler.execute(SUT, invalidRequest, getUseCaseCallback());
        // Assert
        verifyNoMoreInteractions(repoMock);
    }

    @Test
    public void startNewRecipeId_invalidCookHours_resultINVALID_CHANGED() {
        // Arrange
        String recipeId = VALID_NEW_EMPTY.getId();
        givenNewModel(recipeId);
        RecipeDurationRequest.Model invalidModel = RecipeDurationRequest.Model.Builder.
                basedOnDurationResponseModel(durationOnErrorResponse.getModel()).
                setCookHours(MAX_COOK_TIME / 60 + 1).
                build();
        RecipeDurationRequest invalidRequest = new RecipeDurationRequest.Builder().
                setRecipeId(recipeId).
                setCloneToRecipeId(DO_NOT_CLONE).
                setModel(invalidModel).
                build();
        // Act
        handler.execute(SUT, invalidRequest, getUseCaseCallback());
        // Assert
        assertEquals(RecipeStateCalculator.ComponentState.INVALID_CHANGED,
                durationOnErrorResponse.getState());
    }

    @Test
    public void startNewRecipeId_invalidCookHours_failReasonINVALID_COOK_TIME() {
        // Arrange
        String recipeId = VALID_NEW_EMPTY.getId();
        givenNewModel(recipeId);
        RecipeDurationRequest.Model invalidModel = RecipeDurationRequest.Model.Builder.
                basedOnDurationResponseModel(durationOnErrorResponse.getModel()).
                setCookHours(MAX_COOK_TIME / 60 + 1).
                build();
        RecipeDurationRequest invalidRequest = new RecipeDurationRequest.Builder().
                setRecipeId(recipeId).
                setCloneToRecipeId(DO_NOT_CLONE).
                setModel(invalidModel).
                build();
        // Act
        handler.execute(SUT, invalidRequest, getUseCaseCallback());
        // Assert
        assertEquals(1, durationOnErrorResponse.getFailReasons().size());
        assertTrue(durationOnErrorResponse.getFailReasons().contains(FailReason.INVALID_COOK_TIME));
    }

    @Test
    public void startNewRecipeId_validCookHours_validValueSaved() {
        // Arrange
        String recipeId = VALID_NEW_EMPTY.getId();
        whenTimeProviderCalledReturn(VALID_NEW_COOK_TIME_VALID.getCreateDate());
        givenNewModel(recipeId);
        RecipeDurationRequest.Model validModel = RecipeDurationRequest.Model.Builder.
                basedOnDurationResponseModel(durationOnErrorResponse.getModel()).
                setCookHours(VALID_NEW_COOK_TIME_VALID.getCookTime() / 60).
                build();
        RecipeDurationRequest validRequest = new RecipeDurationRequest.Builder().
                setRecipeId(recipeId).
                setCloneToRecipeId(DO_NOT_CLONE).
                setModel(validModel).
                build();
        // Act
        handler.execute(SUT, validRequest, getUseCaseCallback());
        // Assert
        verify(repoMock).save(eq(VALID_NEW_COOK_TIME_VALID));
    }

    @Test
    public void startNewRecipeId_validCookHours_resultVALID_CHANGED() {
        // Arrange
        String recipeId = VALID_NEW_EMPTY.getId();
        givenNewModel(recipeId);
        RecipeDurationRequest.Model validModel = RecipeDurationRequest.Model.Builder.
                basedOnDurationResponseModel(durationOnErrorResponse.getModel()).
                setCookHours(VALID_NEW_COOK_TIME_VALID.getCookTime() / 60).
                build();
        RecipeDurationRequest validRequest = new RecipeDurationRequest.Builder().
                setRecipeId(recipeId).
                setCloneToRecipeId(DO_NOT_CLONE).
                setModel(validModel).
                build();
        // Act
        handler.execute(SUT, validRequest, getUseCaseCallback());
        // Assert
        assertEquals(RecipeStateCalculator.ComponentState.VALID_CHANGED,
                durationOnSuccessResponse.getState());
    }

    @Test
    public void startNewRecipeId_validCookHours_failReasonNONE() {
        // Arrange
        String recipeId = VALID_NEW_EMPTY.getId();
        givenNewModel(recipeId);
        RecipeDurationRequest.Model validModel = RecipeDurationRequest.Model.Builder.
                basedOnDurationResponseModel(durationOnErrorResponse.getModel()).
                setCookHours(VALID_NEW_COOK_TIME_VALID.getCookTime() / 60).
                build();
        RecipeDurationRequest validRequest = new RecipeDurationRequest.Builder().
                setRecipeId(recipeId).
                setCloneToRecipeId(DO_NOT_CLONE).
                setModel(validModel).
                build();
        // Act
        handler.execute(SUT, validRequest, getUseCaseCallback());
        // Assert
        assertEquals(1, durationOnSuccessResponse.getFailReasons().size());
        assertTrue(durationOnSuccessResponse.getFailReasons().contains(FailReason.NONE));
    }

    @Test
    public void startNewRecipeId_invalidCookMinutes_invalidValueNotSaved() {
        // Arrange
        String recipeId = VALID_NEW_EMPTY.getId();
        givenNewModel(recipeId);
        RecipeDurationRequest.Model invalidModel = RecipeDurationRequest.Model.Builder.
                basedOnDurationResponseModel(durationOnErrorResponse.getModel()).
                setCookMinutes(MAX_COOK_TIME + 1).
                build();
        RecipeDurationRequest request = new RecipeDurationRequest.Builder().
                setRecipeId(recipeId).
                setCloneToRecipeId(DO_NOT_CLONE).
                setModel(invalidModel).
                build();
        // Act
        handler.execute(SUT, request, getUseCaseCallback());
        // Assert
        verifyNoMoreInteractions(repoMock);
    }

    @Test
    public void startNewRecipeId_invalidCookMinutes_resultINVALID_CHANGED() {
        // Arrange
        String recipeId = VALID_NEW_EMPTY.getId();
        givenNewModel(recipeId);
        RecipeDurationRequest.Model invalidModel = RecipeDurationRequest.Model.Builder.
                basedOnDurationResponseModel(durationOnErrorResponse.getModel()).
                setCookMinutes(MAX_COOK_TIME + 1).
                build();
        RecipeDurationRequest request = new RecipeDurationRequest.Builder().
                setRecipeId(recipeId).
                setCloneToRecipeId(DO_NOT_CLONE).
                setModel(invalidModel).
                build();
        // Act
        handler.execute(SUT, request, getUseCaseCallback());
        // Assert
        // Assert
        assertEquals(RecipeStateCalculator.ComponentState.INVALID_CHANGED,
                durationOnErrorResponse.getState());
    }

    @Test
    public void startNewRecipeId_invalidCookMinutes_failReasonINVALID_COOK_TIME() {
        // Arrange
        String recipeId = VALID_NEW_EMPTY.getId();
        givenNewModel(recipeId);
        RecipeDurationRequest.Model invalidModel = RecipeDurationRequest.Model.Builder.
                basedOnDurationResponseModel(durationOnErrorResponse.getModel()).
                setCookMinutes(MAX_COOK_TIME + 1).
                build();
        RecipeDurationRequest request = new RecipeDurationRequest.Builder().
                setRecipeId(recipeId).
                setCloneToRecipeId(DO_NOT_CLONE).
                setModel(invalidModel).
                build();
        // Act
        handler.execute(SUT, request, getUseCaseCallback());
        // Assert
        assertEquals(1, durationOnErrorResponse.getFailReasons().size());
        assertTrue(durationOnErrorResponse.getFailReasons().contains(FailReason.INVALID_COOK_TIME));
    }

    @Test
    public void startNewRecipeId_validCookMinutes_validValueSaved() {
        // Arrange
        String recipeId = VALID_NEW_EMPTY.getId();
        whenTimeProviderCalledReturn(VALID_NEW_COOK_TIME_VALID.getCreateDate());
        givenNewModel(recipeId);
        RecipeDurationRequest.Model model = RecipeDurationRequest.Model.Builder.
                basedOnDurationResponseModel(durationOnErrorResponse.getModel()).
                setCookMinutes(VALID_NEW_COOK_TIME_VALID.getCookTime())
                .build();
        RecipeDurationRequest request = new RecipeDurationRequest.Builder().
                setRecipeId(recipeId).
                setCloneToRecipeId(DO_NOT_CLONE).
                setModel(model).
                build();
        // Act
        handler.execute(SUT, request, getUseCaseCallback());
        // Assert
        verify(repoMock).save(eq(VALID_NEW_COOK_TIME_VALID));
    }

    @Test
    public void startNewRecipeId_validCookMinutes_resultVALID_CHANGED() {
        // Arrange
        String recipeId = VALID_NEW_EMPTY.getId();
        givenNewModel(recipeId);
        whenTimeProviderCalledReturn(VALID_NEW_COOK_TIME_VALID.getCreateDate());
        RecipeDurationRequest.Model model = RecipeDurationRequest.Model.Builder.
                basedOnDurationResponseModel(durationOnErrorResponse.getModel()).
                setCookMinutes(VALID_NEW_COOK_TIME_VALID.getCookTime())
                .build();
        RecipeDurationRequest request = new RecipeDurationRequest.Builder().
                setRecipeId(recipeId).
                setCloneToRecipeId(DO_NOT_CLONE).
                setModel(model).
                build();
        // Act
        handler.execute(SUT, request, getUseCaseCallback());
        // Assert
        assertEquals(RecipeStateCalculator.ComponentState.VALID_CHANGED,
                durationOnSuccessResponse.getState());
    }

    @Test
    public void startNewRecipeId_validCookMinutes_failReasonNONE() {
        // Arrange
        String recipeId = VALID_NEW_EMPTY.getId();
        whenTimeProviderCalledReturn(VALID_NEW_COOK_TIME_VALID.getCreateDate());
        givenNewModel(recipeId);
        RecipeDurationRequest.Model model = RecipeDurationRequest.Model.Builder.
                basedOnDurationResponseModel(durationOnErrorResponse.getModel()).
                setCookMinutes(VALID_NEW_COOK_TIME_VALID.getCookTime())
                .build();
        RecipeDurationRequest request = new RecipeDurationRequest.Builder().
                setRecipeId(recipeId).
                setCloneToRecipeId(DO_NOT_CLONE).
                setModel(model).
                build();
        // Act
        handler.execute(SUT, request, getUseCaseCallback());
        // Assert
        assertEquals(1, durationOnSuccessResponse.getFailReasons().size());
        assertTrue(durationOnSuccessResponse.getFailReasons().contains(FailReason.NONE));
    }

    @Test
    public void startValidExistingRecipeId_correctValuesReturned() {
        // Arrange
        String recipeId = VALID_EXISTING_COMPLETE.getId();
        // Act
        givenValidExistingModel(recipeId);
        // Assert
        assertEquals(MAX_PREP_TIME, durationOnSuccessResponse.getModel().getTotalPrepTime());
        assertEquals(MAX_COOK_TIME, durationOnSuccessResponse.getModel().getTotalCookTime());
    }

    @Test
    public void startValidExistingRecipeId_resultVALID_UNCHANGED() {
        // Arrange
        String recipeId = VALID_EXISTING_COMPLETE.getId();
        // Act
        givenValidExistingModel(recipeId);
        // Assert
        assertEquals(RecipeStateCalculator.ComponentState.VALID_UNCHANGED,
                durationOnSuccessResponse.getState());
    }

    @Test
    public void startWithCloned_existingAndNewRecipeId_databaseCalledWithExistingId() {
        // Act
        givenClonedModel();
    }

    @Test
    public void startWithCloned_existingAndNewRecipeId_existingClonedAndSavedWithNewRecipeId() {
        // Arrange
        // Act
        givenClonedModel();
        // Assert
        verify(repoMock).save(eq(VALID_NEW_CLONED));
    }

    @Test
    public void startWithCloned_prepTimeChanged_savedWithUpdatedPrepTime() {
        // Arrange
        whenTimeProviderCalledReturn(VALID_NEW_CLONED_PREP_TIME_UPDATED.getCreateDate());
        givenClonedModel();
        RecipeDurationRequest.Model model = RecipeDurationRequest.Model.Builder.
                basedOnDurationResponseModel(durationOnSuccessResponse.getModel()).
                setPrepHours(VALID_NEW_CLONED_PREP_TIME_UPDATED.getPrepTime() / 60).
                setPrepMinutes(VALID_NEW_CLONED_PREP_TIME_UPDATED.getPrepTime() % 60).
                build();
        RecipeDurationRequest request = new RecipeDurationRequest.Builder().
                setRecipeId(VALID_NEW_EMPTY.getId()).
                setCloneToRecipeId(DO_NOT_CLONE).
                setModel(model).
                build();
        // Act
        handler.execute(SUT, request, getUseCaseCallback());
        // Assert
        verify(repoMock).save(VALID_NEW_CLONED_PREP_TIME_UPDATED);
    }

    @Test
    public void startWithCloned_cloneAttemptFromDataUnavailable_componentStateDATA_UNAVAILABLE() {
        // Arrange
        String cloneFromRecipeId = VALID_COMPLETE_FROM_ANOTHER_USER.getId();
        String cloneToRecipeId = VALID_NEW_EMPTY.getId();

        RecipeDurationRequest request = RecipeDurationRequest.Builder.
                getDefault().
                setRecipeId(cloneFromRecipeId).
                setCloneToRecipeId(cloneToRecipeId).
                build();
        // Act
        handler.execute(SUT, request, getUseCaseCallback());
        // Assert
        verify(repoMock).getById(eq(cloneFromRecipeId),repoCallback.capture());
        repoCallback.getValue().onDataNotAvailable();
        verifyNoMoreInteractions(repoMock);
        assertEquals(RecipeStateCalculator.ComponentState.DATA_UNAVAILABLE,
                durationOnErrorResponse.getState());
    }

    // region helper methods -----------------------------------------------------------------------
    private Callback<RecipeDurationResponse> getUseCaseCallback() {

        return new Callback<RecipeDurationResponse>() {
            @Override
            public void onSuccess(RecipeDurationResponse response) {
                durationOnSuccessResponse = response;
            }

            @Override
            public void onError(RecipeDurationResponse response) {
                durationOnErrorResponse = response;
            }
        };
    }

    private void whenTimeProviderCalledReturn(long time) {
        when(timeProviderMock.getCurrentTimeInMills()).thenReturn(time);
    }

    private void simulateNothingReturnedFromRepo(String recipeId) {
        verify(repoMock).getById(eq(recipeId), repoCallback.capture());
        repoCallback.getValue().onDataNotAvailable();
    }

    private void givenNewModel(String recipeId) {

        RecipeDurationRequest request = RecipeDurationRequest.Builder.
                getDefault().
                setRecipeId(recipeId).
                build();

        handler.execute(SUT, request, getUseCaseCallback());
        simulateNothingReturnedFromRepo(recipeId);
    }

    private void givenValidExistingModel(String recipeId) {
        // Arrange
        RecipeDurationRequest request = RecipeDurationRequest.Builder.
                getDefault().
                setRecipeId(recipeId).build();
        // Act
        handler.execute(SUT, request, getUseCaseCallback());
        verify(repoMock).getById(eq(recipeId), repoCallback.capture());
        repoCallback.getValue().onEntityLoaded(VALID_EXISTING_COMPLETE);
    }

    private void givenClonedModel() {
        // Arrange
        whenTimeProviderCalledReturn(VALID_NEW_CLONED.getCreateDate());
        RecipeDurationRequest request = RecipeDurationRequest.Builder.
                getDefault().
                setRecipeId(VALID_COMPLETE_FROM_ANOTHER_USER.getId()).
                setCloneToRecipeId(VALID_NEW_EMPTY.getId()).
                build();
        // Act
        handler.execute(SUT, request, getUseCaseCallback());
        verify(repoMock).getById(eq(VALID_COMPLETE_FROM_ANOTHER_USER.getId()),
                repoCallback.capture());
        repoCallback.getValue().onEntityLoaded(VALID_COMPLETE_FROM_ANOTHER_USER);
    }
    // endregion helper methods --------------------------------------------------------------------

    // region helper classes -----------------------------------------------------------------------
    // endregion helper classes --------------------------------------------------------------------
}