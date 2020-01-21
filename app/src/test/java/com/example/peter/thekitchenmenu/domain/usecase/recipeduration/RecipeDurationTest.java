package com.example.peter.thekitchenmenu.domain.usecase.recipeduration;

import com.example.peter.thekitchenmenu.commonmocks.UseCaseSchedulerMock;
import com.example.peter.thekitchenmenu.data.entity.RecipeDurationEntity;
import com.example.peter.thekitchenmenu.data.repository.DataSource;
import com.example.peter.thekitchenmenu.data.repository.RepositoryRecipeDuration;
import com.example.peter.thekitchenmenu.domain.usecase.UseCaseHandler;
import com.example.peter.thekitchenmenu.domain.usecase.recipe.recipestate.RecipeState;
import com.example.peter.thekitchenmenu.domain.utils.TimeProvider;
import com.example.peter.thekitchenmenu.testdata.TestDataRecipeDurationEntity;

import org.junit.*;
import org.mockito.*;

import static com.example.peter.thekitchenmenu.domain.usecase.UseCaseCommand.*;
import static com.example.peter.thekitchenmenu.domain.usecase.recipeduration.RecipeDuration.*;
import static com.example.peter.thekitchenmenu.domain.usecase.recipeduration.RecipeDuration.DO_NOT_CLONE;
import static org.junit.Assert.assertEquals;
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
    private RecipeDurationResponse actualResponse;
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
    public void startNewRecipeId_newEmptyModelSaved() {
        // Arrange
        whenTimeProviderCalledReturn(VALID_NEW_EMPTY.getCreateDate());
        String recipeId = VALID_NEW_EMPTY.getId();
        RecipeDurationModel model = RecipeDurationModel.Builder.
                getDefault().
                setId(recipeId).
                build();

        RecipeDurationRequest request = new RecipeDurationRequest(
                recipeId,
                DO_NOT_CLONE,
                model
        );
        // Act
        handler.execute(SUT, request, getUseCaseCallback());
        simulateNothingReturnedFromRepo(recipeId);
        // Assert
        verify(repoMock).save(VALID_NEW_EMPTY);
    }

    @Test
    public void startNewRecipeId_resultVALID_UNCHANGED() {
        // Arrange
        whenTimeProviderCalledReturn(VALID_NEW_EMPTY.getCreateDate());
        String recipeId = VALID_NEW_EMPTY.getId();
        RecipeDurationModel model = RecipeDurationModel.Builder.
                getDefault().
                setId(recipeId).
                build();

        RecipeDurationRequest request = new RecipeDurationRequest(
                recipeId,
                DO_NOT_CLONE,
                model
        );
        // Act
        handler.execute(SUT, request, getUseCaseCallback());
        simulateNothingReturnedFromRepo(recipeId);
        // Assert
        assertEquals(RecipeState.ComponentState.VALID_UNCHANGED, actualResponse.getState());
    }

    @Test
    public void startNewRecipeId_invalidPrepHours_invalidValueNotSaved() {
        // Arrange
        givenNewModel();
        // Act
        RecipeDurationModel invalidModel = RecipeDurationModel.Builder.
                basedOn(actualResponse.getModel()).
                setPrepHours(MAX_PREP_TIME / 60 + 1).
                build();
        RecipeDurationRequest invalidRequest = new RecipeDurationRequest(
                VALID_NEW_EMPTY.getId(), DO_NOT_CLONE, invalidModel
        );
        handler.execute(SUT, invalidRequest, getUseCaseCallback());
        // Assert
        verifyNoMoreInteractions(repoMock);
    }

    @Test
    public void startNewRecipeId_invalidPrepHours_resultINVALID_CHANGED() {
        // Arrange
        givenNewModel();
        // Act
        RecipeDurationModel invalidModel = RecipeDurationModel.Builder.
                basedOn(actualResponse.getModel()).
                setPrepHours(MAX_PREP_TIME / 60 + 1).
                build();
        RecipeDurationRequest invalidRequest = new RecipeDurationRequest(
                VALID_NEW_EMPTY.getId(), DO_NOT_CLONE, invalidModel
        );
        handler.execute(SUT, invalidRequest, getUseCaseCallback());
        // Assert
        assertEquals(RecipeState.ComponentState.INVALID_CHANGED, actualResponse.getState());
    }

    @Test
    public void startNewRecipeId_invalidPrepHours_FaiReasonINVALID_PREP_TIME() {
        // Arrange
        givenNewModel();
        // Act
        int noOfExpectedErrors = 1;
        RecipeDurationModel invalidModel = RecipeDurationModel.Builder.
                basedOn(actualResponse.getModel()).
                setPrepHours(MAX_PREP_TIME / 60 + 1).
                build();
        RecipeDurationRequest invalidRequest = new RecipeDurationRequest(
                VALID_NEW_EMPTY.getId(), DO_NOT_CLONE, invalidModel
        );
        handler.execute(SUT, invalidRequest, getUseCaseCallback());
        // Assert
        assertEquals(noOfExpectedErrors, actualResponse.getFailReasons().size());
        FailReason actualFailReason = actualResponse.getFailReasons().get(0);
        assertEquals(FailReason.INVALID_PREP_TIME, actualFailReason);
    }

    @Test
    public void startNewRecipeId_validPrepHours_resultVALID_CHANGED() {
        // Arrange
        givenNewModel();
        // Act
        RecipeDurationModel validModel = RecipeDurationModel.Builder.
                basedOn(actualResponse.getModel()).
                setPrepHours(VALID_NEW_PREP_TIME_VALID.getPrepTime() / 60).
                build();
        RecipeDurationRequest validRequest = new RecipeDurationRequest(
                VALID_NEW_EMPTY.getId(), DO_NOT_CLONE, validModel
        );
        handler.execute(SUT, validRequest, getUseCaseCallback());
        // Assert
        assertEquals(RecipeState.ComponentState.VALID_CHANGED, actualResponse.getState());
    }

    @Test
    public void startNewRecipeId_validPrepHours_failReasonNONE() {
        // Arrange
        givenNewModel();
        // Act
        RecipeDurationModel validModel = RecipeDurationModel.Builder.
                basedOn(actualResponse.getModel()).
                setPrepHours(VALID_NEW_PREP_TIME_VALID.getPrepTime() / 60).
                build();
        RecipeDurationRequest validRequest = new RecipeDurationRequest(
                VALID_NEW_EMPTY.getId(), DO_NOT_CLONE, validModel
        );
        handler.execute(SUT, validRequest, getUseCaseCallback());
        // Assert
        assertEquals(FailReason.NONE, actualResponse.getFailReasons().get(0));
    }

    @Test
    public void startNewRecipeId_validPrepHours_prepHoursSaved() {
        // Arrange
        givenNewModel();
        // Act
        RecipeDurationModel validModel = RecipeDurationModel.Builder.
                basedOn(actualResponse.getModel()).
                setPrepHours(VALID_NEW_PREP_TIME_VALID.getPrepTime() / 60).
                build();
        RecipeDurationRequest validPrepHoursRequest = new RecipeDurationRequest(
                VALID_NEW_EMPTY.getId(), DO_NOT_CLONE, validModel
        );
        handler.execute(SUT, validPrepHoursRequest, getUseCaseCallback());
        // Assert
        verify(repoMock).save(eq(VALID_NEW_PREP_TIME_VALID));
    }

    @Test
    public void startNewRecipeId_invalidPrepMinutes_invalidValueNotSaved() {
        // Arrange
        givenNewModel();
        // Act
        RecipeDurationModel invalidModel = RecipeDurationModel.Builder.
                basedOn(actualResponse.getModel()).
                setPrepMinutes(MAX_PREP_TIME + 1).
                build();
        RecipeDurationRequest invalidRequest = new RecipeDurationRequest(
                VALID_NEW_EMPTY.getId(), DO_NOT_CLONE, invalidModel
        );
        handler.execute(SUT, invalidRequest, getUseCaseCallback());
        verifyNoMoreInteractions(repoMock);
    }

    @Test
    public void startNewRecipeId_invalidPrepMinutes_resultINVALID_CHANGED() {
        // Arrange
        givenNewModel();
        // Act
        RecipeDurationModel invalidModel = RecipeDurationModel.Builder.
                basedOn(actualResponse.getModel()).
                setPrepMinutes(MAX_PREP_TIME + 1).
                build();
        RecipeDurationRequest invalidRequest = new RecipeDurationRequest(
                VALID_NEW_EMPTY.getId(), DO_NOT_CLONE, invalidModel
        );
        handler.execute(SUT, invalidRequest, getUseCaseCallback());
        // Assert
        assertEquals(RecipeState.ComponentState.INVALID_CHANGED, actualResponse.getState());
    }

    @Test
    public void startNewRecipeId_invalidPrepMinutes_FaiReasonINVALID_PREP_TIME() {
        // Arrange
        givenNewModel();
        // Act
        RecipeDurationModel invalidModel = RecipeDurationModel.Builder.
                basedOn(actualResponse.getModel()).
                setPrepMinutes(MAX_PREP_TIME + 1).
                build();
        RecipeDurationRequest invalidRequest = new RecipeDurationRequest(
                VALID_NEW_EMPTY.getId(), DO_NOT_CLONE, invalidModel
        );
        handler.execute(SUT, invalidRequest, getUseCaseCallback());
        // Assert
        assertEquals(1, actualResponse.getFailReasons().size());
        assertEquals(FailReason.INVALID_PREP_TIME, actualResponse.getFailReasons().get(0));
    }

    @Test
    public void startNewRecipeId_validPrepMinutes_resultVALID_CHANGED() {
        // Arrange
        givenNewModel();
        // Act
        RecipeDurationModel validModel = RecipeDurationModel.Builder.
                basedOn(actualResponse.getModel()).
                setPrepMinutes(VALID_NEW_PREP_TIME_VALID.getPrepTime()).
                build();
        RecipeDurationRequest validRequest = new RecipeDurationRequest(
                VALID_NEW_EMPTY.getId(), DO_NOT_CLONE, validModel
        );
        handler.execute(SUT, validRequest, getUseCaseCallback());
        // Assert
        assertEquals(RecipeState.ComponentState.VALID_CHANGED, actualResponse.getState());
    }

    @Test
    public void startNewRecipeId_validPrepMinutes_failReasonNONE() {
        // Arrange
        givenNewModel();
        // Act
        RecipeDurationModel validModel = RecipeDurationModel.Builder.
                basedOn(actualResponse.getModel()).
                setPrepMinutes(VALID_NEW_PREP_TIME_VALID.getPrepTime()).
                build();
        RecipeDurationRequest validRequest = new RecipeDurationRequest(
                VALID_NEW_EMPTY.getId(), DO_NOT_CLONE, validModel
        );
        handler.execute(SUT, validRequest, getUseCaseCallback());
        // Assert
        assertEquals(1, actualResponse.getFailReasons().size());
        assertEquals(FailReason.NONE, actualResponse.getFailReasons().get(0));
    }

    @Test
    public void startNewRecipeId_validPrepMinutes_prepMinutesSaved() {
        // Arrange
        givenNewModel();
        // Act
        RecipeDurationModel validModel = RecipeDurationModel.Builder.
                basedOn(actualResponse.getModel()).
                setPrepMinutes(VALID_NEW_PREP_TIME_VALID.getPrepTime()).
                build();
        RecipeDurationRequest validRequest = new RecipeDurationRequest(
                VALID_NEW_EMPTY.getId(), DO_NOT_CLONE, validModel
        );
        handler.execute(SUT, validRequest, getUseCaseCallback());
        // Assert
        verify(repoMock).save(eq(VALID_NEW_PREP_TIME_VALID));
    }

    @Test
    public void startNewRecipeId_invalidCookHours_invalidValueNotSaved() {
        // Arrange
        givenNewModel();
        // Act
        RecipeDurationModel invalidModel = RecipeDurationModel.Builder.
                basedOn(actualResponse.getModel()).
                setCookHours(MAX_COOK_TIME / 60 + 1).
                build();
        RecipeDurationRequest invalidRequest = new RecipeDurationRequest(
                VALID_NEW_EMPTY.getId(), DO_NOT_CLONE, invalidModel
        );
        handler.execute(SUT, invalidRequest, getUseCaseCallback());
        // Assert
        verifyNoMoreInteractions(repoMock);
    }

    @Test
    public void startNewRecipeId_invalidCookHours_resultINVALID_CHANGED() {
        // Arrange
        givenNewModel();
        // Act
        RecipeDurationModel invalidModel = RecipeDurationModel.Builder.
                basedOn(actualResponse.getModel()).
                setCookHours(MAX_COOK_TIME / 60 + 1).
                build();
        RecipeDurationRequest invalidRequest = new RecipeDurationRequest(
                VALID_NEW_EMPTY.getId(), DO_NOT_CLONE, invalidModel
        );
        handler.execute(SUT, invalidRequest, getUseCaseCallback());
        // Assert
        assertEquals(RecipeState.ComponentState.INVALID_CHANGED, actualResponse.getState());
    }

    @Test
    public void startNewRecipeId_invalidCookHours_failReasonINVALID_COOK_TIME() {
        // Arrange
        givenNewModel();
        // Act
        RecipeDurationModel invalidModel = RecipeDurationModel.Builder.
                basedOn(actualResponse.getModel()).
                setCookHours(MAX_COOK_TIME / 60 + 1).
                build();
        RecipeDurationRequest invalidRequest = new RecipeDurationRequest(
                VALID_NEW_EMPTY.getId(), DO_NOT_CLONE, invalidModel
        );
        handler.execute(SUT, invalidRequest, getUseCaseCallback());
        // Assert
        assertEquals(1, actualResponse.getFailReasons().size());
        assertEquals(FailReason.INVALID_COOK_TIME, actualResponse.getFailReasons().get(0));
    }

    @Test
    public void startNewRecipeId_validCookHours_validValueSaved() {
        // Arrange
        givenNewModel();
        // Act
        RecipeDurationModel validModel = RecipeDurationModel.Builder.
                basedOn(actualResponse.getModel()).
                setCookHours(VALID_NEW_COOK_TIME_VALID.getCookTime() / 60).
                build();
        RecipeDurationRequest validRequest = new RecipeDurationRequest(
                VALID_NEW_EMPTY.getId(), DO_NOT_CLONE, validModel
        );
        handler.execute(SUT, validRequest, getUseCaseCallback());
        // Assert
        verify(repoMock).save(eq(VALID_NEW_COOK_TIME_VALID));
    }

    @Test
    public void startNewRecipeId_validCookHours_resultVALID_CHANGED() {
        // Arrange
        givenNewModel();
        // Act
        RecipeDurationModel validModel = RecipeDurationModel.Builder.
                basedOn(actualResponse.getModel()).
                setCookHours(VALID_NEW_COOK_TIME_VALID.getCookTime() / 60).
                build();
        RecipeDurationRequest validRequest = new RecipeDurationRequest(
                VALID_NEW_EMPTY.getId(), DO_NOT_CLONE, validModel
        );
        handler.execute(SUT, validRequest, getUseCaseCallback());
        // Assert
        assertEquals(RecipeState.ComponentState.VALID_CHANGED, actualResponse.getState());
    }

    @Test
    public void startNewRecipeId_validCookHours_failReasonNONE() {
        // Arrange
        givenNewModel();
        // Act
        RecipeDurationModel validModel = RecipeDurationModel.Builder.
                basedOn(actualResponse.getModel()).
                setCookHours(VALID_NEW_COOK_TIME_VALID.getCookTime() / 60).
                build();
        RecipeDurationRequest validRequest = new RecipeDurationRequest(
                VALID_NEW_EMPTY.getId(), DO_NOT_CLONE, validModel
        );
        handler.execute(SUT, validRequest, getUseCaseCallback());
        // Assert
        assertEquals(1, actualResponse.getFailReasons().size());
        assertEquals(FailReason.NONE, actualResponse.getFailReasons().get(0));
    }

    @Test
    public void startNewRecipeId_invalidCookMinutes_invalidValueNotSaved() {
        // Arrange
        givenNewModel();
        // Act
        RecipeDurationModel invalidModel = RecipeDurationModel.Builder.
                basedOn(actualResponse.getModel()).
                setCookMinutes(MAX_COOK_TIME + 1).
                build();
        RecipeDurationRequest request = new RecipeDurationRequest(
                VALID_NEW_EMPTY.getId(), DO_NOT_CLONE, invalidModel
        );
        handler.execute(SUT, request, getUseCaseCallback());
        // Assert
        verifyNoMoreInteractions(repoMock);
    }

    @Test
    public void startNewRecipeId_invalidCookMinutes_resultINVALID_CHANGED() {
        // Arrange
        givenNewModel();
        // Act
        RecipeDurationModel invalidModel = RecipeDurationModel.Builder.
                basedOn(actualResponse.getModel()).
                setCookMinutes(MAX_COOK_TIME + 1).
                build();
        RecipeDurationRequest request = new RecipeDurationRequest(
                VALID_NEW_EMPTY.getId(), DO_NOT_CLONE, invalidModel
        );
        handler.execute(SUT, request, getUseCaseCallback());
        // Assert
        assertEquals(RecipeState.ComponentState.INVALID_CHANGED, actualResponse.getState());
    }

    @Test
    public void startNewRecipeId_invalidCookMinutes_failReasonINVALID_COOK_TIME() {
        // Arrange
        givenNewModel();
        // Act
        RecipeDurationModel invalidModel = RecipeDurationModel.Builder.
                basedOn(actualResponse.getModel()).
                setCookMinutes(MAX_COOK_TIME + 1).
                build();
        RecipeDurationRequest request = new RecipeDurationRequest(
                VALID_NEW_EMPTY.getId(), DO_NOT_CLONE, invalidModel
        );
        handler.execute(SUT, request, getUseCaseCallback());
        // Assert
        assertEquals(1, actualResponse.getFailReasons().size());
        assertEquals(FailReason.INVALID_COOK_TIME, actualResponse.getFailReasons().get(0));
    }

    @Test
    public void startNewRecipeId_validCookMinutes_validValueSaved() {
        // Arrange
        givenNewModel();
        // Act
        RecipeDurationModel model = RecipeDurationModel.Builder.
                basedOn(actualResponse.getModel()).
                setCookMinutes(VALID_NEW_COOK_TIME_VALID.getCookTime())
                .build();
        RecipeDurationRequest request = new RecipeDurationRequest(
                VALID_NEW_EMPTY.getId(), DO_NOT_CLONE, model
        );
        handler.execute(SUT, request, getUseCaseCallback());
        // Assert
        verify(repoMock).save(eq(VALID_NEW_COOK_TIME_VALID));
    }

    @Test
    public void startNewRecipeId_validCookMinutes_resultVALID_CHANGED() {
        // Arrange
        givenNewModel();
        // Act
        RecipeDurationModel model = RecipeDurationModel.Builder.
                basedOn(actualResponse.getModel()).
                setCookMinutes(VALID_NEW_COOK_TIME_VALID.getCookTime())
                .build();
        RecipeDurationRequest request = new RecipeDurationRequest(
                VALID_NEW_EMPTY.getId(), DO_NOT_CLONE, model
        );
        handler.execute(SUT, request, getUseCaseCallback());
        // Assert
        assertEquals(RecipeState.ComponentState.VALID_CHANGED, actualResponse.getState());
    }

    @Test
    public void startNewRecipeId_validCookMinutes_failReasonNONE() {
        // Arrange
        givenNewModel();
        // Act
        RecipeDurationModel model = RecipeDurationModel.Builder.
                basedOn(actualResponse.getModel()).
                setCookMinutes(VALID_NEW_COOK_TIME_VALID.getCookTime())
                .build();
        RecipeDurationRequest request = new RecipeDurationRequest(
                VALID_NEW_EMPTY.getId(), DO_NOT_CLONE, model
        );
        handler.execute(SUT, request, getUseCaseCallback());
        // Assert
        assertEquals(1, actualResponse.getFailReasons().size());
        assertEquals(FailReason.NONE, actualResponse.getFailReasons().get(0));
    }

    @Test
    public void startValidExistingRecipeId_correctResultsReturned() {
        // Arrange
        givenValidExistingModel();
        // Assert
        assertEquals(MAX_PREP_TIME, actualResponse.getModel().getTotalPrepTime());
        assertEquals(MAX_COOK_TIME, actualResponse.getModel().getTotalCookTime());
    }

    @Test
    public void startValidExistingRecipeId_resultVALID_UNCHANGED() {
        // Arrange
        givenValidExistingModel();
        // Act
        // Assert
        assertEquals(RecipeState.ComponentState.VALID_UNCHANGED, actualResponse.getState());
    }

    @Test
    public void startWithCloned_existingAndNewRecipeId_databaseCalledWithExistingId() {
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
        givenClonedModel();
        // Act
        RecipeDurationModel model = RecipeDurationModel.Builder.
                basedOn(actualResponse.getModel()).
                setPrepHours(VALID_NEW_CLONED_PREP_TIME_UPDATED.getPrepTime() / 60).
                setPrepMinutes(VALID_NEW_CLONED_PREP_TIME_UPDATED.getPrepTime() % 60).
                build();
        RecipeDurationRequest request = new RecipeDurationRequest(
                VALID_NEW_EMPTY.getId(),
                DO_NOT_CLONE,
                model
        );
        handler.execute(SUT, request, getUseCaseCallback());
        // Assert
        verify(repoMock).save(VALID_NEW_CLONED_PREP_TIME_UPDATED);
    }

    @Test
    public void startWithCloned_cloneFromDataNotAvailable_newModelCreatedAndSavedWithNewRecipeId() {
        // Arrange
        whenTimeProviderCalledReturn(VALID_NEW_EMPTY.getCreateDate());
        // Act
        RecipeDurationModel model = RecipeDurationModel.Builder.getDefault().build();
        RecipeDurationRequest request = new RecipeDurationRequest(
                VALID_COMPLETE_FROM_ANOTHER_USER.getId(), VALID_NEW_EMPTY.getId(), model
        );
        // Act
        handler.execute(SUT, request, getUseCaseCallback());
        verify(repoMock).getById(eq(VALID_COMPLETE_FROM_ANOTHER_USER.getId()),
                repoCallback.capture());
        repoCallback.getValue().onDataNotAvailable();
        // Assert
        verify(repoMock).save(VALID_NEW_EMPTY);
    }

    // region helper methods -----------------------------------------------------------------------
    private Callback<RecipeDurationResponse> getUseCaseCallback() {

        return new Callback<RecipeDurationResponse>() {
            @Override
            public void onSuccess(RecipeDurationResponse response) {
                actualResponse = response;
            }

            @Override
            public void onError(RecipeDurationResponse response) {
                actualResponse = response;
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

    private void givenNewModel() {
        whenTimeProviderCalledReturn(VALID_NEW_EMPTY.getCreateDate());
        String recipeId = VALID_NEW_EMPTY.getId();
        RecipeDurationModel model = RecipeDurationModel.Builder.getDefault().setId(recipeId).build();

        RecipeDurationRequest request = new RecipeDurationRequest(
                recipeId, DO_NOT_CLONE, model
        );
        handler.execute(SUT, request, getUseCaseCallback());
        simulateNothingReturnedFromRepo(recipeId);
        verify(repoMock).save(VALID_NEW_EMPTY);
    }

    private void givenValidExistingModel() {
        // Arrange
        String recipeId = VALID_EXISTING_COMPLETE.getId();
        RecipeDurationModel model = RecipeDurationModel.Builder.getDefault().setId(recipeId).build();
        RecipeDurationRequest request = new RecipeDurationRequest(
                recipeId, DO_NOT_CLONE, model
        );
        // Act
        handler.execute(SUT, request, getUseCaseCallback());
        verify(repoMock).getById(eq(recipeId), repoCallback.capture());
        repoCallback.getValue().onEntityLoaded(VALID_EXISTING_COMPLETE);
    }

    private void givenClonedModel() {
        // Arrange
        whenTimeProviderCalledReturn(VALID_NEW_CLONED.getCreateDate());
        RecipeDurationModel model = RecipeDurationModel.Builder.getDefault().build();
        RecipeDurationRequest request = new RecipeDurationRequest(
                VALID_COMPLETE_FROM_ANOTHER_USER.getId(), VALID_NEW_EMPTY.getId(), model
        );
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