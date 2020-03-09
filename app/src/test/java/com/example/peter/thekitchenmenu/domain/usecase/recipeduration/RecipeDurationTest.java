package com.example.peter.thekitchenmenu.domain.usecase.recipeduration;

import com.example.peter.thekitchenmenu.commonmocks.UseCaseSchedulerMock;
import com.example.peter.thekitchenmenu.data.entity.RecipeDurationEntity;
import com.example.peter.thekitchenmenu.data.repository.DataSource;
import com.example.peter.thekitchenmenu.data.repository.RepositoryRecipeDuration;
import com.example.peter.thekitchenmenu.domain.usecase.CommonFailReason;
import com.example.peter.thekitchenmenu.domain.usecase.UseCase;
import com.example.peter.thekitchenmenu.domain.usecase.UseCaseHandler;
import com.example.peter.thekitchenmenu.domain.usecase.recipe.recipe.RecipeResponse;
import com.example.peter.thekitchenmenu.domain.usecase.recipe.recipeduration.RecipeDuration;
import com.example.peter.thekitchenmenu.domain.usecase.recipe.recipeduration.RecipeDurationRequest;
import com.example.peter.thekitchenmenu.domain.usecase.recipe.recipeduration.RecipeDurationResponse;
import com.example.peter.thekitchenmenu.domain.usecase.recipe.recipestate.RecipeStateCalculator;
import com.example.peter.thekitchenmenu.domain.utils.TimeProvider;
import com.example.peter.thekitchenmenu.testdata.TestDataRecipeDurationEntity;

import org.junit.*;
import org.mockito.*;

import javax.annotation.Nonnull;

import static com.example.peter.thekitchenmenu.domain.usecase.recipe.recipeduration.RecipeDuration.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

public class RecipeDurationTest {

    private static final String TAG = "tag-" + RecipeDurationTest.class.getSimpleName() + ": ";

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
    public static int MAX_PREP_TIME = TestDataRecipeDurationEntity.getMaxPrepTime();
    public static int MAX_COOK_TIME = TestDataRecipeDurationEntity.getMaxCookTime();
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
    public void startNewId_componentStateDATA_UNAVAILABLE() {
        // Arrange
        String recipeId = VALID_NEW_EMPTY.getId();
        // Act
        setupForNewDuration(recipeId);
        // Assert
        assertEquals(RecipeStateCalculator.ComponentState.DATA_UNAVAILABLE, durationOnErrorResponse.getState());
    }

    @Test
    public void startNewId_invalidPrepHours_invalidValueNotSaved() {
        // Arrange
        String recipeId = VALID_NEW_EMPTY.getId();
        setupForNewDuration(recipeId);
        RecipeDurationRequest.Model invalidModel = RecipeDurationRequest.Model.Builder.
                basedOnDurationResponseModel(durationOnErrorResponse.getModel()).
                setPrepHours(MAX_PREP_TIME / 60 + 1).
                build();
        RecipeDurationRequest request = new RecipeDurationRequest.Builder().
                setId(recipeId).
                setCloneToId("").
                setModel(invalidModel).
                build();
        // Act
        handler.execute(SUT, request, getUseCaseCallback());
        // Assert
        verifyNoMoreInteractions(repoMock);
    }

    @Test
    public void startNewId_invalidPrepHours_resultINVALID_CHANGED() {
        // Arrange
        String recipeId = VALID_NEW_EMPTY.getId();
        setupForNewDuration(recipeId);
        RecipeDurationRequest.Model invalidModel = RecipeDurationRequest.Model.Builder.
                basedOnDurationResponseModel(durationOnErrorResponse.getModel()).
                setPrepHours(MAX_PREP_TIME / 60 + 1).
                build();
        RecipeDurationRequest request = new RecipeDurationRequest.Builder().
                setId(recipeId).
                setCloneToId("").
                setModel(invalidModel).
                build();
        // Act
        handler.execute(SUT, request, getUseCaseCallback());
        // Assert
        assertEquals(RecipeStateCalculator.ComponentState.INVALID_CHANGED,
                durationOnErrorResponse.getState());
    }

    @Test
    public void startNewId_invalidPrepHours_FaiReasonINVALID_PREP_TIME() {
        // Arrange
        String recipeId = VALID_NEW_EMPTY.getId();
        setupForNewDuration(recipeId);
        RecipeDurationRequest.Model invalidModel = RecipeDurationRequest.Model.Builder.
                basedOnDurationResponseModel(durationOnErrorResponse.getModel()).
                setPrepHours(MAX_PREP_TIME / 60 + 1).
                build();
        RecipeDurationRequest request = new RecipeDurationRequest.Builder().
                setId(recipeId).
                setCloneToId("").
                setModel(invalidModel).
                build();
        // Act
        handler.execute(SUT, request, getUseCaseCallback());
        // Assert
        assertEquals(1, durationOnErrorResponse.getFailReasons().size());
        assertTrue(durationOnErrorResponse.getFailReasons().contains(FailReason.INVALID_PREP_TIME));
    }

    @Test
    public void startNewId_validPrepHours_resultVALID_CHANGED() {
        // Arrange
        String recipeId = VALID_NEW_EMPTY.getId();
        setupForNewDuration(recipeId);
        RecipeDurationRequest.Model validModel = RecipeDurationRequest.Model.Builder.
                basedOnDurationResponseModel(durationOnErrorResponse.getModel()).
                setPrepHours(VALID_NEW_PREP_TIME_VALID.getPrepTime() / 60).
                build();
        RecipeDurationRequest validRequest = new RecipeDurationRequest.Builder().
                setId(recipeId).
                setCloneToId("").
                setModel(validModel).
                build();

        // Act
        handler.execute(SUT, validRequest, getUseCaseCallback());
        // Assert
        assertEquals(RecipeStateCalculator.ComponentState.VALID_CHANGED, durationOnSuccessResponse.getState());
    }

    @Test
    public void startNewId_validPrepHours_failReasonNONE() {
        // Arrange
        String recipeId = VALID_NEW_EMPTY.getId();
        setupForNewDuration(recipeId);
        RecipeDurationRequest.Model validModel = RecipeDurationRequest.Model.Builder.
                basedOnDurationResponseModel(durationOnErrorResponse.getModel()).
                setPrepHours(VALID_NEW_PREP_TIME_VALID.getPrepTime() / 60).
                build();
        RecipeDurationRequest validRequest = new RecipeDurationRequest.Builder().
                setId(recipeId).
                setCloneToId("").
                setModel(validModel).
                build();

        // Act
        handler.execute(SUT, validRequest, getUseCaseCallback());
        // Assert
        assertTrue(durationOnSuccessResponse.getFailReasons().contains(CommonFailReason.NONE));
    }

    @Test
    public void startNewId_validPrepHours_prepHoursSaved() {
        // Arrange
        String recipeId = VALID_NEW_EMPTY.getId();
        whenTimeProviderCalledReturn(VALID_NEW_PREP_TIME_VALID.getCreateDate());
        setupForNewDuration(recipeId);
        RecipeDurationRequest.Model validModel = RecipeDurationRequest.Model.Builder.
                basedOnDurationResponseModel(durationOnErrorResponse.getModel()).
                setPrepHours(VALID_NEW_PREP_TIME_VALID.getPrepTime() / 60).
                build();
        RecipeDurationRequest validRequest = new RecipeDurationRequest.Builder().
                setId(recipeId).
                setCloneToId("").
                setModel(validModel).
                build();

        // Act
        handler.execute(SUT, validRequest, getUseCaseCallback());
        // Assert
        verify(repoMock).save(eq(VALID_NEW_PREP_TIME_VALID));
    }

    @Test
    public void startNewId_invalidPrepMinutes_invalidValueNotSaved() {
        // Arrange
        String recipeId = VALID_NEW_EMPTY.getId();
        setupForNewDuration(recipeId);
        RecipeDurationRequest.Model invalidModel = RecipeDurationRequest.Model.Builder.
                basedOnDurationResponseModel(durationOnErrorResponse.getModel()).
                setPrepMinutes(MAX_PREP_TIME + 1).
                build();
        RecipeDurationRequest request = new RecipeDurationRequest.Builder().
                setId(recipeId).
                setCloneToId("").
                setModel(invalidModel).
                build();
        // Act
        handler.execute(SUT, request, getUseCaseCallback());
        // Assert
        verifyNoMoreInteractions(repoMock);
    }

    @Test
    public void startNewId_invalidPrepMinutes_resultINVALID_CHANGED() {
        // Arrange
        String recipeId = VALID_NEW_EMPTY.getId();
        setupForNewDuration(recipeId);
        RecipeDurationRequest.Model invalidModel = RecipeDurationRequest.Model.Builder.
                basedOnDurationResponseModel(durationOnErrorResponse.getModel()).
                setPrepMinutes(MAX_PREP_TIME + 1).
                build();
        RecipeDurationRequest request = new RecipeDurationRequest.Builder().
                setId(recipeId).
                setCloneToId("").
                setModel(invalidModel).
                build();
        // Act
        handler.execute(SUT, request, getUseCaseCallback());
        // Assert
        assertEquals(RecipeStateCalculator.ComponentState.INVALID_CHANGED,
                durationOnErrorResponse.getState());
    }

    @Test
    public void startNewId_invalidPrepMinutes_FaiReasonINVALID_PREP_TIME() {
        // Arrange
        String recipeId = VALID_NEW_EMPTY.getId();
        setupForNewDuration(recipeId);
        RecipeDurationRequest.Model invalidModel = RecipeDurationRequest.Model.Builder.
                basedOnDurationResponseModel(durationOnErrorResponse.getModel()).
                setPrepMinutes(MAX_PREP_TIME + 1).
                build();
        RecipeDurationRequest request = new RecipeDurationRequest.Builder().
                setId(recipeId).
                setCloneToId("").
                setModel(invalidModel).
                build();
        // Act
        handler.execute(SUT, request, getUseCaseCallback());
        // Assert
        assertEquals(1, durationOnErrorResponse.getFailReasons().size());
        assertTrue(durationOnErrorResponse.getFailReasons().
                contains(FailReason.INVALID_PREP_TIME));
    }

    @Test
    public void startNewId_validPrepMinutes_resultVALID_CHANGED() {
        // Arrange
        String recipeId = VALID_NEW_EMPTY.getId();
        setupForNewDuration(recipeId);
        RecipeDurationRequest.Model validModel = RecipeDurationRequest.Model.Builder.
                basedOnDurationResponseModel(durationOnErrorResponse.getModel()).
                setPrepMinutes(VALID_NEW_PREP_TIME_VALID.getPrepTime()).
                build();
        RecipeDurationRequest validRequest = new RecipeDurationRequest.Builder().
                setId(recipeId).
                setCloneToId("").
                setModel(validModel).
                build();
        // Act
        handler.execute(SUT, validRequest, getUseCaseCallback());
        // Assert
        assertEquals(RecipeStateCalculator.ComponentState.VALID_CHANGED,
                durationOnSuccessResponse.getState());
    }

    @Test
    public void startNewId_validPrepMinutes_failReasonNONE() {
        // Arrange
        String recipeId = VALID_NEW_EMPTY.getId();
        setupForNewDuration(recipeId);
        RecipeDurationRequest.Model validModel = RecipeDurationRequest.Model.Builder.
                basedOnDurationResponseModel(durationOnErrorResponse.getModel()).
                setPrepMinutes(VALID_NEW_PREP_TIME_VALID.getPrepTime()).
                build();
        RecipeDurationRequest validRequest = new RecipeDurationRequest.Builder().
                setId(recipeId).
                setCloneToId("").
                setModel(validModel).
                build();
        // Act
        handler.execute(SUT, validRequest, getUseCaseCallback());
        // Assert
        assertEquals(1, durationOnSuccessResponse.getFailReasons().size());
        assertTrue(durationOnSuccessResponse.getFailReasons().contains(CommonFailReason.NONE));
    }

    @Test
    public void startNewId_validPrepMinutes_prepMinutesSaved() {
        // Arrange
        String recipeId = VALID_NEW_EMPTY.getId();
        whenTimeProviderCalledReturn(VALID_NEW_PREP_TIME_VALID.getCreateDate());
        setupForNewDuration(recipeId);
        RecipeDurationRequest.Model validModel = RecipeDurationRequest.Model.Builder.
                basedOnDurationResponseModel(durationOnErrorResponse.getModel()).
                setPrepMinutes(VALID_NEW_PREP_TIME_VALID.getPrepTime()).
                build();
        RecipeDurationRequest validRequest = new RecipeDurationRequest.Builder().
                setId(recipeId).
                setCloneToId("").
                setModel(validModel).
                build();
        // Act
        handler.execute(SUT, validRequest, getUseCaseCallback());
        // Assert
        verify(repoMock).save(eq(VALID_NEW_PREP_TIME_VALID));
    }

    @Test
    public void startNewId_invalidCookHours_invalidValueNotSaved() {
        // Arrange
        String recipeId = VALID_NEW_EMPTY.getId();
        setupForNewDuration(recipeId);
        RecipeDurationRequest.Model invalidModel = RecipeDurationRequest.Model.Builder.
                basedOnDurationResponseModel(durationOnErrorResponse.getModel()).
                setCookHours(MAX_COOK_TIME / 60 + 1).
                build();
        RecipeDurationRequest invalidRequest = new RecipeDurationRequest.Builder().
                setId(recipeId).
                setCloneToId("").
                setModel(invalidModel).
                build();
        // Act
        handler.execute(SUT, invalidRequest, getUseCaseCallback());
        // Assert
        verifyNoMoreInteractions(repoMock);
    }

    @Test
    public void startNewId_invalidCookHours_resultINVALID_CHANGED() {
        // Arrange
        String recipeId = VALID_NEW_EMPTY.getId();
        setupForNewDuration(recipeId);
        RecipeDurationRequest.Model invalidModel = RecipeDurationRequest.Model.Builder.
                basedOnDurationResponseModel(durationOnErrorResponse.getModel()).
                setCookHours(MAX_COOK_TIME / 60 + 1).
                build();
        RecipeDurationRequest invalidRequest = new RecipeDurationRequest.Builder().
                setId(recipeId).
                setCloneToId("").
                setModel(invalidModel).
                build();
        // Act
        handler.execute(SUT, invalidRequest, getUseCaseCallback());
        // Assert
        assertEquals(RecipeStateCalculator.ComponentState.INVALID_CHANGED,
                durationOnErrorResponse.getState());
    }

    @Test
    public void startNewId_invalidCookHours_failReasonINVALID_COOK_TIME() {
        // Arrange
        String recipeId = VALID_NEW_EMPTY.getId();
        setupForNewDuration(recipeId);
        RecipeDurationRequest.Model invalidModel = RecipeDurationRequest.Model.Builder.
                basedOnDurationResponseModel(durationOnErrorResponse.getModel()).
                setCookHours(MAX_COOK_TIME / 60 + 1).
                build();
        RecipeDurationRequest invalidRequest = new RecipeDurationRequest.Builder().
                setId(recipeId).
                setCloneToId("").
                setModel(invalidModel).
                build();
        // Act
        handler.execute(SUT, invalidRequest, getUseCaseCallback());
        // Assert
        assertEquals(1, durationOnErrorResponse.getFailReasons().size());
        assertTrue(durationOnErrorResponse.getFailReasons().contains(FailReason.INVALID_COOK_TIME));
    }

    @Test
    public void startNewId_validCookHours_validValueSaved() {
        // Arrange
        String recipeId = VALID_NEW_EMPTY.getId();
        whenTimeProviderCalledReturn(VALID_NEW_COOK_TIME_VALID.getCreateDate());
        setupForNewDuration(recipeId);
        RecipeDurationRequest.Model validModel = RecipeDurationRequest.Model.Builder.
                basedOnDurationResponseModel(durationOnErrorResponse.getModel()).
                setCookHours(VALID_NEW_COOK_TIME_VALID.getCookTime() / 60).
                build();
        RecipeDurationRequest validRequest = new RecipeDurationRequest.Builder().
                setId(recipeId).
                setCloneToId("").
                setModel(validModel).
                build();
        // Act
        handler.execute(SUT, validRequest, getUseCaseCallback());
        // Assert
        verify(repoMock).save(eq(VALID_NEW_COOK_TIME_VALID));
    }

    @Test
    public void startNewId_validCookHours_resultVALID_CHANGED() {
        // Arrange
        String recipeId = VALID_NEW_EMPTY.getId();
        setupForNewDuration(recipeId);
        RecipeDurationRequest.Model validModel = RecipeDurationRequest.Model.Builder.
                basedOnDurationResponseModel(durationOnErrorResponse.getModel()).
                setCookHours(VALID_NEW_COOK_TIME_VALID.getCookTime() / 60).
                build();
        RecipeDurationRequest validRequest = new RecipeDurationRequest.Builder().
                setId(recipeId).
                setCloneToId("").
                setModel(validModel).
                build();
        // Act
        handler.execute(SUT, validRequest, getUseCaseCallback());
        // Assert
        assertEquals(RecipeStateCalculator.ComponentState.VALID_CHANGED,
                durationOnSuccessResponse.getState());
    }

    @Test
    public void startNewId_validCookHours_failReasonNONE() {
        // Arrange
        String recipeId = VALID_NEW_EMPTY.getId();
        setupForNewDuration(recipeId);
        RecipeDurationRequest.Model validModel = RecipeDurationRequest.Model.Builder.
                basedOnDurationResponseModel(durationOnErrorResponse.getModel()).
                setCookHours(VALID_NEW_COOK_TIME_VALID.getCookTime() / 60).
                build();
        RecipeDurationRequest validRequest = new RecipeDurationRequest.Builder().
                setId(recipeId).
                setCloneToId("").
                setModel(validModel).
                build();
        // Act
        handler.execute(SUT, validRequest, getUseCaseCallback());
        // Assert
        assertEquals(1, durationOnSuccessResponse.getFailReasons().size());
        assertTrue(durationOnSuccessResponse.getFailReasons().contains(CommonFailReason.NONE));
    }

    @Test
    public void startNewId_invalidCookMinutes_invalidValueNotSaved() {
        // Arrange
        String recipeId = VALID_NEW_EMPTY.getId();
        setupForNewDuration(recipeId);
        RecipeDurationRequest.Model invalidModel = RecipeDurationRequest.Model.Builder.
                basedOnDurationResponseModel(durationOnErrorResponse.getModel()).
                setCookMinutes(MAX_COOK_TIME + 1).
                build();
        RecipeDurationRequest request = new RecipeDurationRequest.Builder().
                setId(recipeId).
                setCloneToId("").
                setModel(invalidModel).
                build();
        // Act
        handler.execute(SUT, request, getUseCaseCallback());
        // Assert
        verifyNoMoreInteractions(repoMock);
    }

    @Test
    public void startNewId_invalidCookMinutes_resultINVALID_CHANGED() {
        // Arrange
        String recipeId = VALID_NEW_EMPTY.getId();
        setupForNewDuration(recipeId);
        RecipeDurationRequest.Model invalidModel = RecipeDurationRequest.Model.Builder.
                basedOnDurationResponseModel(durationOnErrorResponse.getModel()).
                setCookMinutes(MAX_COOK_TIME + 1).
                build();
        RecipeDurationRequest request = new RecipeDurationRequest.Builder().
                setId(recipeId).
                setCloneToId("").
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
    public void startNewId_invalidCookMinutes_failReasonINVALID_COOK_TIME() {
        // Arrange
        String recipeId = VALID_NEW_EMPTY.getId();
        setupForNewDuration(recipeId);
        RecipeDurationRequest.Model invalidModel = RecipeDurationRequest.Model.Builder.
                basedOnDurationResponseModel(durationOnErrorResponse.getModel()).
                setCookMinutes(MAX_COOK_TIME + 1).
                build();
        RecipeDurationRequest request = new RecipeDurationRequest.Builder().
                setId(recipeId).
                setCloneToId("").
                setModel(invalidModel).
                build();
        // Act
        handler.execute(SUT, request, getUseCaseCallback());
        // Assert
        assertEquals(1, durationOnErrorResponse.getFailReasons().size());
        assertTrue(durationOnErrorResponse.getFailReasons().contains(FailReason.INVALID_COOK_TIME));
    }

    @Test
    public void startNewId_validCookMinutes_validValueSaved() {
        // Arrange
        String recipeId = VALID_NEW_EMPTY.getId();
        whenTimeProviderCalledReturn(VALID_NEW_COOK_TIME_VALID.getCreateDate());

        setupForNewDuration(recipeId);

        RecipeDurationRequest.Model model = RecipeDurationRequest.Model.Builder.
                basedOnDurationResponseModel(durationOnErrorResponse.getModel()).
                setCookMinutes(MAX_COOK_TIME)
                .build();
        RecipeDurationRequest request = new RecipeDurationRequest.Builder().
                setId(recipeId).
                setCloneToId("").
                setModel(model).
                build();
        // Act
        handler.execute(SUT, request, getUseCaseCallback());
        // Assert
        System.out.println(TAG + VALID_NEW_COOK_TIME_VALID);
        verify(repoMock).save(eq(VALID_NEW_COOK_TIME_VALID));
    }

    @Test
    public void startNewId_validCookMinutes_resultVALID_CHANGED() {
        // Arrange
        String recipeId = VALID_NEW_EMPTY.getId();
        setupForNewDuration(recipeId);
        whenTimeProviderCalledReturn(VALID_NEW_COOK_TIME_VALID.getCreateDate());
        RecipeDurationRequest.Model model = RecipeDurationRequest.Model.Builder.
                basedOnDurationResponseModel(durationOnErrorResponse.getModel()).
                setCookMinutes(VALID_NEW_COOK_TIME_VALID.getCookTime())
                .build();
        RecipeDurationRequest request = new RecipeDurationRequest.Builder().
                setId(recipeId).
                setCloneToId("").
                setModel(model).
                build();
        // Act
        handler.execute(SUT, request, getUseCaseCallback());
        // Assert
        assertEquals(RecipeStateCalculator.ComponentState.VALID_CHANGED,
                durationOnSuccessResponse.getState());
    }

    @Test
    public void startNewId_validCookMinutes_failReasonNONE() {
        // Arrange
        String recipeId = VALID_NEW_EMPTY.getId();
        whenTimeProviderCalledReturn(VALID_NEW_COOK_TIME_VALID.getCreateDate());
        setupForNewDuration(recipeId);
        RecipeDurationRequest.Model model = RecipeDurationRequest.Model.Builder.
                basedOnDurationResponseModel(durationOnErrorResponse.getModel()).
                setCookMinutes(VALID_NEW_COOK_TIME_VALID.getCookTime())
                .build();
        RecipeDurationRequest request = new RecipeDurationRequest.Builder().
                setId(recipeId).
                setCloneToId("").
                setModel(model).
                build();
        // Act
        handler.execute(SUT, request, getUseCaseCallback());
        // Assert
        assertEquals(1, durationOnSuccessResponse.getFailReasons().size());
        assertTrue(durationOnSuccessResponse.getFailReasons().contains(CommonFailReason.NONE));
    }

    @Test
    public void startNewId_invalidPrepTimeInvalidCookTime_failReasonsINVALID_PREP_TIME_INVALID_COOK_TIME() {
        // Arrange
        String recipeId = VALID_NEW_EMPTY.getId();
        setupForNewDuration(recipeId);
        RecipeDurationRequest.Model model = RecipeDurationRequest.Model.Builder.
                basedOnDurationResponseModel(durationOnErrorResponse.getModel()).
                setPrepHours(MAX_PREP_TIME / 60).setPrepMinutes(1).
                setCookHours(MAX_COOK_TIME / 60).setCookMinutes(1)
                .build();
        RecipeDurationRequest request = new RecipeDurationRequest.Builder().
                setId(recipeId).
                setCloneToId("").
                setModel(model).
                build();
        // Act
        handler.execute(SUT, request, getUseCaseCallback());
        // Assert
        assertEquals(RecipeStateCalculator.ComponentState.INVALID_CHANGED,
                durationOnErrorResponse.getState());
        assertEquals(2, durationOnErrorResponse.getFailReasons().size());
        assertTrue(durationOnErrorResponse.getFailReasons().contains(FailReason.INVALID_PREP_TIME));
        assertTrue(durationOnErrorResponse.getFailReasons().contains(FailReason.INVALID_COOK_TIME));
    }

    @Test
    public void startNewId_validPrepTimeValidCookTime_failReasonsNONE() {
        // Arrange
        String recipeId = VALID_NEW_EMPTY.getId();
        whenTimeProviderCalledReturn(VALID_NEW_COOK_TIME_VALID.getCreateDate());
        setupForNewDuration(recipeId);
        RecipeDurationRequest.Model model = RecipeDurationRequest.Model.Builder.
                basedOnDurationResponseModel(durationOnErrorResponse.getModel()).
                setPrepHours(MAX_PREP_TIME / 60 - 1).setPrepMinutes(59).
                setCookHours(MAX_COOK_TIME / 60 - 1).setCookMinutes(59)
                .build();
        RecipeDurationRequest request = new RecipeDurationRequest.Builder().
                setId(recipeId).
                setCloneToId("").
                setModel(model).
                build();
        // Act
        handler.execute(SUT, request, getUseCaseCallback());
        // Assert
        System.out.println(durationOnSuccessResponse);
    }

    @Test
    public void startValidExistingId_correctValuesReturned() {
        // Arrange
        String recipeId = VALID_EXISTING_COMPLETE.getId();
        // Act
        givenValidExistingModel(recipeId);
        // Assert
        assertEquals(MAX_PREP_TIME, durationOnSuccessResponse.getModel().getTotalPrepTime());
        assertEquals(MAX_COOK_TIME, durationOnSuccessResponse.getModel().getTotalCookTime());
    }

    @Test
    public void startValidExistingId_resultVALID_UNCHANGED() {
        // Arrange
        String recipeId = VALID_EXISTING_COMPLETE.getId();
        // Act
        givenValidExistingModel(recipeId);
        // Assert
        assertEquals(RecipeStateCalculator.ComponentState.VALID_UNCHANGED,
                durationOnSuccessResponse.getState());
    }

    @Test
    public void startClone_existingAndNewId_databaseCalledWithExistingId() {
        // Act
        givenClonedModel();
    }

    @Test
    public void startClone_existingAndNewId_existingClonedAndSavedWithNewRecipeId() {
        // Arrange
        // Act
        givenClonedModel();
        // Assert
        verify(repoMock).save(eq(VALID_NEW_CLONED));
    }

    @Test
    public void startClone_prepTimeChanged_savedWithUpdatedPrepTime() {
        // Arrange
        whenTimeProviderCalledReturn(VALID_NEW_CLONED_PREP_TIME_UPDATED.getCreateDate());
        givenClonedModel();
        RecipeDurationRequest.Model model = RecipeDurationRequest.Model.Builder.
                basedOnDurationResponseModel(durationOnSuccessResponse.getModel()).
                setPrepHours(VALID_NEW_CLONED_PREP_TIME_UPDATED.getPrepTime() / 60).
                setPrepMinutes(VALID_NEW_CLONED_PREP_TIME_UPDATED.getPrepTime() % 60).
                build();
        RecipeDurationRequest request = new RecipeDurationRequest.Builder().
                setId(VALID_NEW_EMPTY.getId()).
                setCloneToId("").
                setModel(model).
                build();
        // Act
        handler.execute(SUT, request, getUseCaseCallback());
        // Assert
        verify(repoMock).save(VALID_NEW_CLONED_PREP_TIME_UPDATED);
    }

    @Test
    public void startClone_attemptCloneFromDataUnavailable_componentStateDATA_UNAVAILABLE() {
        // Arrange
        String cloneFromRecipeId = VALID_COMPLETE_FROM_ANOTHER_USER.getId();
        String cloneToRecipeId = VALID_NEW_EMPTY.getId();
        whenTimeProviderCalledReturn(VALID_NEW_EMPTY.getCreateDate());

        RecipeDurationRequest request = RecipeDurationRequest.Builder.
                getDefault().
                setId(cloneFromRecipeId).
                setCloneToId(cloneToRecipeId).
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

    private void setupForNewDuration(String recipeId) {

        RecipeDurationRequest request = RecipeDurationRequest.Builder.
                getDefault().
                setId(recipeId).
                build();

        handler.execute(SUT, request, getUseCaseCallback());
        simulateNothingReturnedFromRepo(recipeId);
    }

    private void givenValidExistingModel(String recipeId) {
        // Arrange
        RecipeDurationRequest request = RecipeDurationRequest.Builder.
                getDefault().
                setId(recipeId).build();
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
                setId(VALID_COMPLETE_FROM_ANOTHER_USER.getId()).
                setCloneToId(VALID_NEW_EMPTY.getId()).
                build();
        // Act
        handler.execute(SUT, request, getUseCaseCallback());
        verify(repoMock).getById(eq(VALID_COMPLETE_FROM_ANOTHER_USER.getId()),
                repoCallback.capture());
        repoCallback.getValue().onEntityLoaded(VALID_COMPLETE_FROM_ANOTHER_USER);
    }
    // endregion helper methods --------------------------------------------------------------------

    // region helper classes -----------------------------------------------------------------------
    private static class RecipeResponseCallback implements UseCase.Callback<RecipeResponse> {

        private static final String TAG = "tkm-" + RecipeResponseCallback.class.getSimpleName() +
                ": ";

        private RecipeResponse response;

        @Override
        public void onSuccess(RecipeResponse response) {
            System.out.println(RecipeDurationTest.TAG + TAG + "onSuccess:" + response);
            this.response = response;
        }

        @Override
        public void onError(RecipeResponse response) {
            System.out.println(RecipeDurationTest.TAG + TAG + "onError:" + response);
            this.response = response;
        }

        public RecipeResponse getResponse() {
            return response;
        }

        @Nonnull
        @Override
        public String toString() {
            return "RecipeResponseCallback{" +
                    "response=" + response +
                    '}';
        }
    }
    // endregion helper classes --------------------------------------------------------------------
}