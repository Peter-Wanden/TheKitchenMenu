package com.example.peter.thekitchenmenu.domain.usecase.recipe.component.duration;

import com.example.peter.thekitchenmenu.commonmocks.UseCaseSchedulerMock;
import com.example.peter.thekitchenmenu.data.repository.source.local.recipe.duration.RecipeDurationEntity;
import com.example.peter.thekitchenmenu.data.repository.PrimitiveDataSource;
import com.example.peter.thekitchenmenu.data.repository.recipe.RepositoryRecipeDuration;
import com.example.peter.thekitchenmenu.domain.model.CommonFailReason;
import com.example.peter.thekitchenmenu.domain.usecase.UseCase;
import com.example.peter.thekitchenmenu.domain.usecase.UseCaseHandler;
import com.example.peter.thekitchenmenu.domain.usecase.recipe.metadata.RecipeMetadata;
import com.example.peter.thekitchenmenu.domain.usecase.recipe.metadata.RecipeMetadataResponse;
import com.example.peter.thekitchenmenu.domain.utils.TimeProvider;
import com.example.peter.thekitchenmenu.testdata.TestDataRecipeDurationEntity;

import org.junit.*;
import org.mockito.*;

import javax.annotation.Nonnull;

import static com.example.peter.thekitchenmenu.domain.usecase.recipe.component.duration.RecipeDuration.*;
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

    // endregion constants -------------------------------------------------------------------------

    // region helper fields ------------------------------------------------------------------------
    private UseCaseHandler handler;
    @Mock
    RepositoryRecipeDuration repoMock;
    @Captor
    ArgumentCaptor<PrimitiveDataSource.GetEntityCallback<RecipeDurationEntity>> repoCallback;
    @Mock
    TimeProvider timeProviderMock;

    private RecipeDurationResponse durationOnSuccessResponse;
    private RecipeDurationResponse durationOnErrorResponse;

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
    public void newId_componentStateDATA_UNAVAILABLE() {
        // Arrange
        String recipeId = VALID_NEW_EMPTY.getDataId();
        // Act
        setupForNewDuration(recipeId);
        // Assert
        assertEquals(RecipeMetadata.ComponentState.INVALID_UNCHANGED, durationOnErrorResponse.
                getMetadata().
                getState());
        assertTrue(durationOnErrorResponse.getMetadata().
                getFailReasons().
                contains(CommonFailReason.DATA_UNAVAILABLE));
    }

    @Test
    public void newId_invalidPrepHours_invalidValueNotSaved() {
        // Arrange
        String recipeId = VALID_NEW_EMPTY.getDataId();
        setupForNewDuration(recipeId);
        RecipeDurationRequest.Model invalidModel = RecipeDurationRequest.Model.Builder.
                basedOnDurationResponseModel(durationOnErrorResponse.getModel()).
                setPrepHours(MAX_PREP_TIME / 60 + 1).
                build();
        RecipeDurationRequest request = new RecipeDurationRequest.Builder().
                setDataId(recipeId).
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
        String recipeId = VALID_NEW_EMPTY.getDataId();
        setupForNewDuration(recipeId);
        RecipeDurationRequest.Model invalidModel = RecipeDurationRequest.Model.Builder.
                basedOnDurationResponseModel(durationOnErrorResponse.getModel()).
                setPrepHours(MAX_PREP_TIME / 60 + 1).
                build();
        RecipeDurationRequest request = new RecipeDurationRequest.Builder().
                setDataId(recipeId).
                setModel(invalidModel).
                build();
        // Act
        handler.execute(SUT, request, getUseCaseCallback());
        // Assert
        assertEquals(RecipeMetadata.ComponentState.INVALID_CHANGED,
                durationOnErrorResponse.getMetadata().getState());
    }

    @Test
    public void startNewId_invalidPrepHours_FaiReasonINVALID_PREP_TIME() {
        // Arrange
        String recipeId = VALID_NEW_EMPTY.getDataId();
        setupForNewDuration(recipeId);
        RecipeDurationRequest.Model invalidModel = RecipeDurationRequest.Model.Builder.
                basedOnDurationResponseModel(durationOnErrorResponse.getModel()).
                setPrepHours(MAX_PREP_TIME / 60 + 1).
                build();
        RecipeDurationRequest request = new RecipeDurationRequest.Builder().
                setDataId(recipeId).
                setModel(invalidModel).
                build();
        // Act
        handler.execute(SUT, request, getUseCaseCallback());
        // Assert
        assertEquals(1, durationOnErrorResponse.
                getMetadata().
                getFailReasons().
                size());
        assertTrue(durationOnErrorResponse.
                getMetadata().
                getFailReasons().
                contains(RecipeDuration.FailReason.INVALID_PREP_TIME));
    }

    @Test
    public void startNewId_validPrepHours_resultVALID_CHANGED() {
        // Arrange
        String recipeId = VALID_NEW_EMPTY.getDataId();
        setupForNewDuration(recipeId);
        RecipeDurationRequest.Model validModel = RecipeDurationRequest.Model.Builder.
                basedOnDurationResponseModel(durationOnErrorResponse.getModel()).
                setPrepHours(VALID_NEW_PREP_TIME_VALID.getPrepTime() / 60).
                build();
        RecipeDurationRequest validRequest = new RecipeDurationRequest.Builder().
                setDataId(recipeId).
                setModel(validModel).
                build();

        // Act
        handler.execute(SUT, validRequest, getUseCaseCallback());
        // Assert
        assertEquals(RecipeMetadata.ComponentState.VALID_CHANGED,
                durationOnSuccessResponse.getMetadata().getState());
    }

    @Test
    public void startNewId_validPrepHours_failReasonNONE() {
        // Arrange
        String recipeId = VALID_NEW_EMPTY.getDataId();
        setupForNewDuration(recipeId);
        RecipeDurationRequest.Model validModel = RecipeDurationRequest.Model.Builder.
                basedOnDurationResponseModel(durationOnErrorResponse.getModel()).
                setPrepHours(VALID_NEW_PREP_TIME_VALID.getPrepTime() / 60).
                build();
        RecipeDurationRequest validRequest = new RecipeDurationRequest.Builder().
                setDataId(recipeId).
                setModel(validModel).
                build();

        // Act
        handler.execute(SUT, validRequest, getUseCaseCallback());
        // Assert
        assertTrue(durationOnSuccessResponse.
                getMetadata().
                getFailReasons().
                contains(CommonFailReason.NONE));
    }

    @Test
    public void startNewId_validPrepHours_prepHoursSaved() {
        // Arrange
        String recipeId = VALID_NEW_EMPTY.getDataId();
        whenTimeProviderCalledReturn(VALID_NEW_PREP_TIME_VALID.getCreateDate());
        setupForNewDuration(recipeId);
        RecipeDurationRequest.Model validModel = RecipeDurationRequest.Model.Builder.
                basedOnDurationResponseModel(durationOnErrorResponse.getModel()).
                setPrepHours(VALID_NEW_PREP_TIME_VALID.getPrepTime() / 60).
                build();
        RecipeDurationRequest validRequest = new RecipeDurationRequest.Builder().
                setDataId(recipeId).
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
        String recipeId = VALID_NEW_EMPTY.getDataId();
        setupForNewDuration(recipeId);
        RecipeDurationRequest.Model invalidModel = RecipeDurationRequest.Model.Builder.
                basedOnDurationResponseModel(durationOnErrorResponse.getModel()).
                setPrepMinutes(MAX_PREP_TIME + 1).
                build();
        RecipeDurationRequest request = new RecipeDurationRequest.Builder().
                setDataId(recipeId).
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
        String recipeId = VALID_NEW_EMPTY.getDataId();
        setupForNewDuration(recipeId);
        RecipeDurationRequest.Model invalidModel = RecipeDurationRequest.Model.Builder.
                basedOnDurationResponseModel(durationOnErrorResponse.getModel()).
                setPrepMinutes(MAX_PREP_TIME + 1).
                build();
        RecipeDurationRequest request = new RecipeDurationRequest.Builder().
                setDataId(recipeId).
                setModel(invalidModel).
                build();
        // Act
        handler.execute(SUT, request, getUseCaseCallback());
        // Assert
        assertEquals(RecipeMetadata.ComponentState.INVALID_CHANGED,
                durationOnErrorResponse.getMetadata().getState());
    }

    @Test
    public void startNewId_invalidPrepMinutes_FaiReasonINVALID_PREP_TIME() {
        // Arrange
        String recipeId = VALID_NEW_EMPTY.getDataId();
        setupForNewDuration(recipeId);
        RecipeDurationRequest.Model invalidModel = RecipeDurationRequest.Model.Builder.
                basedOnDurationResponseModel(durationOnErrorResponse.getModel()).
                setPrepMinutes(MAX_PREP_TIME + 1).
                build();
        RecipeDurationRequest request = new RecipeDurationRequest.Builder().
                setDataId(recipeId).
                setModel(invalidModel).
                build();
        // Act
        handler.execute(SUT, request, getUseCaseCallback());
        // Assert
        assertEquals(1, durationOnErrorResponse.getMetadata().getFailReasons().size());
        assertTrue(durationOnErrorResponse.getMetadata().getFailReasons().
                contains(FailReason.INVALID_PREP_TIME));
    }

    @Test
    public void startNewId_validPrepMinutes_resultVALID_CHANGED() {
        // Arrange
        String recipeId = VALID_NEW_EMPTY.getDataId();
        setupForNewDuration(recipeId);
        RecipeDurationRequest.Model validModel = RecipeDurationRequest.Model.Builder.
                basedOnDurationResponseModel(durationOnErrorResponse.getModel()).
                setPrepMinutes(VALID_NEW_PREP_TIME_VALID.getPrepTime()).
                build();
        RecipeDurationRequest validRequest = new RecipeDurationRequest.Builder().
                setDataId(recipeId).
                setModel(validModel).
                build();
        // Act
        handler.execute(SUT, validRequest, getUseCaseCallback());
        // Assert
        assertEquals(RecipeMetadata.ComponentState.VALID_CHANGED,
                durationOnSuccessResponse.getMetadata().getState());
    }

    @Test
    public void startNewId_validPrepMinutes_failReasonNONE() {
        // Arrange
        String recipeId = VALID_NEW_EMPTY.getDataId();
        setupForNewDuration(recipeId);
        RecipeDurationRequest.Model validModel = RecipeDurationRequest.Model.Builder.
                basedOnDurationResponseModel(durationOnErrorResponse.getModel()).
                setPrepMinutes(VALID_NEW_PREP_TIME_VALID.getPrepTime()).
                build();
        RecipeDurationRequest validRequest = new RecipeDurationRequest.Builder().
                setDataId(recipeId).
                setModel(validModel).
                build();
        // Act
        handler.execute(SUT, validRequest, getUseCaseCallback());
        // Assert
        assertEquals(1, durationOnSuccessResponse.getMetadata().
                getFailReasons().
                size());
        assertTrue(durationOnSuccessResponse.getMetadata().
                getFailReasons().
                contains(CommonFailReason.NONE));
    }

    @Test
    public void startNewId_validPrepMinutes_prepMinutesSaved() {
        // Arrange
        String recipeId = VALID_NEW_EMPTY.getDataId();
        whenTimeProviderCalledReturn(VALID_NEW_PREP_TIME_VALID.getCreateDate());
        setupForNewDuration(recipeId);
        RecipeDurationRequest.Model validModel = RecipeDurationRequest.Model.Builder.
                basedOnDurationResponseModel(durationOnErrorResponse.getModel()).
                setPrepMinutes(VALID_NEW_PREP_TIME_VALID.getPrepTime()).
                build();
        RecipeDurationRequest validRequest = new RecipeDurationRequest.Builder().
                setDataId(recipeId).
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
        String recipeId = VALID_NEW_EMPTY.getDataId();
        setupForNewDuration(recipeId);
        RecipeDurationRequest.Model invalidModel = RecipeDurationRequest.Model.Builder.
                basedOnDurationResponseModel(durationOnErrorResponse.getModel()).
                setCookHours(MAX_COOK_TIME / 60 + 1).
                build();
        RecipeDurationRequest invalidRequest = new RecipeDurationRequest.Builder().
                setDataId(recipeId).
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
        String recipeId = VALID_NEW_EMPTY.getDataId();
        setupForNewDuration(recipeId);
        RecipeDurationRequest.Model invalidModel = RecipeDurationRequest.Model.Builder.
                basedOnDurationResponseModel(durationOnErrorResponse.getModel()).
                setCookHours(MAX_COOK_TIME / 60 + 1).
                build();
        RecipeDurationRequest invalidRequest = new RecipeDurationRequest.Builder().
                setDataId(recipeId).
                setModel(invalidModel).
                build();
        // Act
        handler.execute(SUT, invalidRequest, getUseCaseCallback());
        // Assert
        assertEquals(RecipeMetadata.ComponentState.INVALID_CHANGED,
                durationOnErrorResponse.getMetadata().getState());
    }

    @Test
    public void startNewId_invalidCookHours_failReasonINVALID_COOK_TIME() {
        // Arrange
        String recipeId = VALID_NEW_EMPTY.getDataId();
        setupForNewDuration(recipeId);
        RecipeDurationRequest.Model invalidModel = RecipeDurationRequest.Model.Builder.
                basedOnDurationResponseModel(durationOnErrorResponse.getModel()).
                setCookHours(MAX_COOK_TIME / 60 + 1).
                build();
        RecipeDurationRequest invalidRequest = new RecipeDurationRequest.Builder().
                setDataId(recipeId).
                setModel(invalidModel).
                build();
        // Act
        handler.execute(SUT, invalidRequest, getUseCaseCallback());
        // Assert
        assertEquals(1, durationOnErrorResponse.getMetadata().
                getFailReasons().
                size());
        assertTrue(durationOnErrorResponse.getMetadata().
                getFailReasons().
                contains(FailReason.INVALID_COOK_TIME));
    }

    @Test
    public void startNewId_validCookHours_validValueSaved() {
        // Arrange
        String recipeId = VALID_NEW_EMPTY.getDataId();
        whenTimeProviderCalledReturn(VALID_NEW_COOK_TIME_VALID.getCreateDate());
        setupForNewDuration(recipeId);
        RecipeDurationRequest.Model validModel = RecipeDurationRequest.Model.Builder.
                basedOnDurationResponseModel(durationOnErrorResponse.getModel()).
                setCookHours(VALID_NEW_COOK_TIME_VALID.getCookTime() / 60).
                build();
        RecipeDurationRequest validRequest = new RecipeDurationRequest.Builder().
                setDataId(recipeId).
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
        String recipeId = VALID_NEW_EMPTY.getDataId();
        setupForNewDuration(recipeId);
        RecipeDurationRequest.Model validModel = RecipeDurationRequest.Model.Builder.
                basedOnDurationResponseModel(durationOnErrorResponse.getModel()).
                setCookHours(VALID_NEW_COOK_TIME_VALID.getCookTime() / 60).
                build();
        RecipeDurationRequest validRequest = new RecipeDurationRequest.Builder().
                setDataId(recipeId).
                setModel(validModel).
                build();
        // Act
        handler.execute(SUT, validRequest, getUseCaseCallback());
        // Assert
        assertEquals(RecipeMetadata.ComponentState.VALID_CHANGED,
                durationOnSuccessResponse.getMetadata().getState());
    }

    @Test
    public void startNewId_validCookHours_failReasonNONE() {
        // Arrange
        String recipeId = VALID_NEW_EMPTY.getDataId();
        setupForNewDuration(recipeId);
        RecipeDurationRequest.Model validModel = RecipeDurationRequest.Model.Builder.
                basedOnDurationResponseModel(durationOnErrorResponse.getModel()).
                setCookHours(VALID_NEW_COOK_TIME_VALID.getCookTime() / 60).
                build();
        RecipeDurationRequest validRequest = new RecipeDurationRequest.Builder().
                setDataId(recipeId).
                setModel(validModel).
                build();
        // Act
        handler.execute(SUT, validRequest, getUseCaseCallback());
        // Assert
        assertEquals(1, durationOnSuccessResponse.getMetadata().getFailReasons().size());
        assertTrue(durationOnSuccessResponse.getMetadata().getFailReasons().contains(CommonFailReason.NONE));
    }

    @Test
    public void startNewId_invalidCookMinutes_invalidValueNotSaved() {
        // Arrange
        String recipeId = VALID_NEW_EMPTY.getDataId();
        setupForNewDuration(recipeId);
        RecipeDurationRequest.Model invalidModel = RecipeDurationRequest.Model.Builder.
                basedOnDurationResponseModel(durationOnErrorResponse.getModel()).
                setCookMinutes(MAX_COOK_TIME + 1).
                build();
        RecipeDurationRequest request = new RecipeDurationRequest.Builder().
                setDataId(recipeId).
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
        String recipeId = VALID_NEW_EMPTY.getDataId();
        setupForNewDuration(recipeId);
        RecipeDurationRequest.Model invalidModel = RecipeDurationRequest.Model.Builder.
                basedOnDurationResponseModel(durationOnErrorResponse.getModel()).
                setCookMinutes(MAX_COOK_TIME + 1).
                build();
        RecipeDurationRequest request = new RecipeDurationRequest.Builder().
                setDataId(recipeId).
                setModel(invalidModel).
                build();
        // Act
        handler.execute(SUT, request, getUseCaseCallback());
        // Assert
        // Assert
        assertEquals(RecipeMetadata.ComponentState.INVALID_CHANGED,
                durationOnErrorResponse.getMetadata().getState());
    }

    @Test
    public void startNewId_invalidCookMinutes_failReasonINVALID_COOK_TIME() {
        // Arrange
        String recipeId = VALID_NEW_EMPTY.getDataId();
        setupForNewDuration(recipeId);
        RecipeDurationRequest.Model invalidModel = RecipeDurationRequest.Model.Builder.
                basedOnDurationResponseModel(durationOnErrorResponse.getModel()).
                setCookMinutes(MAX_COOK_TIME + 1).
                build();
        RecipeDurationRequest request = new RecipeDurationRequest.Builder().
                setDataId(recipeId).
                setModel(invalidModel).
                build();
        // Act
        handler.execute(SUT, request, getUseCaseCallback());
        // Assert
        assertEquals(1, durationOnErrorResponse.getMetadata().
                getFailReasons().
                size());
        assertTrue(durationOnErrorResponse.getMetadata().
                getFailReasons().
                contains(FailReason.INVALID_COOK_TIME));
    }

    @Test
    public void startNewId_validCookMinutes_validValueSaved() {
        // Arrange
        String recipeId = VALID_NEW_EMPTY.getDataId();
        whenTimeProviderCalledReturn(VALID_NEW_COOK_TIME_VALID.getCreateDate());

        setupForNewDuration(recipeId);

        RecipeDurationRequest.Model model = RecipeDurationRequest.Model.Builder.
                basedOnDurationResponseModel(durationOnErrorResponse.getModel()).
                setCookMinutes(MAX_COOK_TIME)
                .build();
        RecipeDurationRequest request = new RecipeDurationRequest.Builder().
                setDataId(recipeId).
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
        String recipeId = VALID_NEW_EMPTY.getDataId();
        setupForNewDuration(recipeId);
        whenTimeProviderCalledReturn(VALID_NEW_COOK_TIME_VALID.getCreateDate());
        RecipeDurationRequest.Model model = RecipeDurationRequest.Model.Builder.
                basedOnDurationResponseModel(durationOnErrorResponse.getModel()).
                setCookMinutes(VALID_NEW_COOK_TIME_VALID.getCookTime())
                .build();
        RecipeDurationRequest request = new RecipeDurationRequest.Builder().
                setDataId(recipeId).
                setModel(model).
                build();
        // Act
        handler.execute(SUT, request, getUseCaseCallback());
        // Assert
        assertEquals(RecipeMetadata.ComponentState.VALID_CHANGED,
                durationOnSuccessResponse.getMetadata().getState());
    }

    @Test
    public void startNewId_validCookMinutes_failReasonNONE() {
        // Arrange
        String recipeId = VALID_NEW_EMPTY.getDataId();
        whenTimeProviderCalledReturn(VALID_NEW_COOK_TIME_VALID.getCreateDate());
        setupForNewDuration(recipeId);
        RecipeDurationRequest.Model model = RecipeDurationRequest.Model.Builder.
                basedOnDurationResponseModel(durationOnErrorResponse.getModel()).
                setCookMinutes(VALID_NEW_COOK_TIME_VALID.getCookTime())
                .build();
        RecipeDurationRequest request = new RecipeDurationRequest.Builder().
                setDataId(recipeId).
                setModel(model).
                build();
        // Act
        handler.execute(SUT, request, getUseCaseCallback());
        // Assert
        assertEquals(1, durationOnSuccessResponse.getMetadata().
                getFailReasons().
                size());
        assertTrue(durationOnSuccessResponse.getMetadata().
                getFailReasons().
                contains(CommonFailReason.NONE));
    }

    @Test
    public void startNewId_invalidPrepTimeInvalidCookTime_failReasonsINVALID_PREP_TIME_INVALID_COOK_TIME() {
        // Arrange
        String recipeId = VALID_NEW_EMPTY.getDataId();
        setupForNewDuration(recipeId);
        RecipeDurationRequest.Model model = RecipeDurationRequest.Model.Builder.
                basedOnDurationResponseModel(durationOnErrorResponse.getModel()).
                setPrepHours(MAX_PREP_TIME / 60).setPrepMinutes(1).
                setCookHours(MAX_COOK_TIME / 60).setCookMinutes(1)
                .build();
        RecipeDurationRequest request = new RecipeDurationRequest.Builder().
                setDataId(recipeId).
                setModel(model).
                build();
        // Act
        handler.execute(SUT, request, getUseCaseCallback());
        // Assert
        assertEquals(RecipeMetadata.ComponentState.INVALID_CHANGED,
                durationOnErrorResponse.getMetadata().getState());
        assertEquals(2, durationOnErrorResponse.getMetadata().getFailReasons().size());
        assertTrue(durationOnErrorResponse.getMetadata().
                getFailReasons().
                contains(FailReason.INVALID_PREP_TIME));
        assertTrue(durationOnErrorResponse.getMetadata().
                getFailReasons().
                contains(FailReason.INVALID_COOK_TIME));
    }

    @Test
    public void startNewId_validPrepTimeValidCookTime_failReasonsNONE() {
        // Arrange
        String recipeId = VALID_NEW_EMPTY.getDataId();
        whenTimeProviderCalledReturn(VALID_NEW_COOK_TIME_VALID.getCreateDate());
        setupForNewDuration(recipeId);
        RecipeDurationRequest.Model model = RecipeDurationRequest.Model.Builder.
                basedOnDurationResponseModel(durationOnErrorResponse.getModel()).
                setPrepHours(MAX_PREP_TIME / 60 - 1).setPrepMinutes(59).
                setCookHours(MAX_COOK_TIME / 60 - 1).setCookMinutes(59)
                .build();
        RecipeDurationRequest request = new RecipeDurationRequest.Builder().
                setDataId(recipeId).
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
        String recipeId = VALID_EXISTING_COMPLETE.getDataId();
        // Act
        givenValidExistingModel(recipeId);
        // Assert
        assertEquals(MAX_PREP_TIME, durationOnSuccessResponse.getModel().getTotalPrepTime());
        assertEquals(MAX_COOK_TIME, durationOnSuccessResponse.getModel().getTotalCookTime());
    }

    @Test
    public void startValidExistingId_resultVALID_UNCHANGED() {
        // Arrange
        String recipeId = VALID_EXISTING_COMPLETE.getDataId();
        // Act
        givenValidExistingModel(recipeId);
        // Assert
        assertEquals(RecipeMetadata.ComponentState.VALID_UNCHANGED,
                durationOnSuccessResponse.getMetadata().getState());
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
        verify(repoMock).getByDataId(eq(recipeId), repoCallback.capture());
        repoCallback.getValue().onDataUnavailable();
    }

    private void setupForNewDuration(String recipeId) {
        RecipeDurationRequest request = new RecipeDurationRequest.Builder().
                getDefault().
                setDataId(recipeId).
                build();

        handler.execute(SUT, request, getUseCaseCallback());
        simulateNothingReturnedFromRepo(recipeId);
    }

    private void givenValidExistingModel(String recipeId) {
        // Arrange
        RecipeDurationRequest request = new RecipeDurationRequest.Builder().
                getDefault().
                setDataId(recipeId).build();
        // Act
        handler.execute(SUT, request, getUseCaseCallback());
        verify(repoMock).getByDataId(eq(recipeId), repoCallback.capture());
        repoCallback.getValue().onEntityLoaded(VALID_EXISTING_COMPLETE);
    }


    // endregion helper methods --------------------------------------------------------------------

    // region helper classes -----------------------------------------------------------------------
    private static class RecipeResponseCallback implements UseCase.Callback<RecipeMetadataResponse> {

        private static final String TAG = "tkm-" + RecipeResponseCallback.class.getSimpleName() +
                ": ";

        private RecipeMetadataResponse response;

        @Override
        public void onSuccess(RecipeMetadataResponse response) {
            System.out.println(RecipeDurationTest.TAG + TAG + "onSuccess:" + response);
            this.response = response;
        }

        @Override
        public void onError(RecipeMetadataResponse response) {
            System.out.println(RecipeDurationTest.TAG + TAG + "onError:" + response);
            this.response = response;
        }

        public RecipeMetadataResponse getResponse() {
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