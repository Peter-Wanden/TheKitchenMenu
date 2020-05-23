package com.example.peter.thekitchenmenu.domain.usecase.recipe.component.duration;

import com.example.peter.thekitchenmenu.commonmocks.UseCaseSchedulerMock;
import com.example.peter.thekitchenmenu.data.repository.DomainDataAccess.GetDomainModelCallback;
import com.example.peter.thekitchenmenu.data.repository.recipe.RepositoryRecipeDuration;
import com.example.peter.thekitchenmenu.data.repository.recipe.duration.TestDataRecipeDuration;
import com.example.peter.thekitchenmenu.domain.model.CommonFailReason;
import com.example.peter.thekitchenmenu.domain.model.FailReasons;
import com.example.peter.thekitchenmenu.domain.usecase.UseCaseBase;
import com.example.peter.thekitchenmenu.domain.usecase.UseCaseHandler;
import com.example.peter.thekitchenmenu.domain.usecase.recipe.component.metadata.RecipeMetadata;
import com.example.peter.thekitchenmenu.domain.utils.TimeProvider;
import com.example.peter.thekitchenmenu.domain.utils.UniqueIdProvider;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;

import static com.example.peter.thekitchenmenu.domain.usecase.recipe.component.duration.RecipeDuration.FailReason;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

public class RecipeDurationTest {

//    private static final String TAG = "tag-" + RecipeDurationTest.class.getSimpleName() + ": ";

    // region constants ----------------------------------------------------------------------------
    public static int MAX_PREP_TIME = TestDataRecipeDuration.MAX_PREP_TIME;
    public static int MAX_COOK_TIME = TestDataRecipeDuration.MAX_COOK_TIME;

    private static final RecipeDurationPersistenceModel VALID_NEW_EMPTY =
            TestDataRecipeDuration.getValidNew();
    private static final String VALID_NEW_EMPTY_DOMAIN_ID = VALID_NEW_EMPTY.getDomainId();

    private static final RecipeDurationPersistenceModel VALID_NEW_PREP_TIME_VALID =
            TestDataRecipeDuration.getValidNewPrepTimeValid();
    private static final RecipeDurationPersistenceModel VALID_NEW_COOK_TIME_VALID =
            TestDataRecipeDuration.getValidNewCookTimeValid();

    private static final RecipeDurationPersistenceModel VALID_EXISTING_COMPLETE =
            TestDataRecipeDuration.getValidExistingComplete();
    private static final String VALID_EXISTING_COMPLETE_DOMAIN_ID =
            VALID_EXISTING_COMPLETE.getDomainId();

    private static final RecipeDurationPersistenceModel INVALID_EXISTING_COMPLETE =
            TestDataRecipeDuration.getInvalidExistingComplete();

    // endregion constants -------------------------------------------------------------------------

    // region helper fields ------------------------------------------------------------------------
    private UseCaseHandler handler;
    @Mock
    RepositoryRecipeDuration repoMock;
    @Captor
    ArgumentCaptor<GetDomainModelCallback<RecipeDurationPersistenceModel>> repoCallback;
    @Mock
    TimeProvider timeProviderMock;
    @Mock
    UniqueIdProvider idProviderMock;

    private RecipeDurationResponse durationOnSuccessResponse;
    private RecipeDurationResponse durationOnErrorResponse;
    private int expectedNoOfFailReasons;
    // endregion helper fields ---------------------------------------------------------------------

    private RecipeDuration SUT;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        SUT = givenUseCase();
    }

    private RecipeDuration givenUseCase() {
        handler = new UseCaseHandler(new UseCaseSchedulerMock());
        return new RecipeDuration(
                repoMock,
                timeProviderMock,
                idProviderMock,
                MAX_PREP_TIME,
                MAX_COOK_TIME);
    }

    @Test
    public void newRequest_componentStateINVALID_UNCHANGED() {
        // Arrange / Act
        simulateNewInitialisationRequest();
        // Assert
        assertEquals(
                RecipeMetadata.ComponentState.INVALID_UNCHANGED,
                durationOnErrorResponse.getMetadata().getState()
        );
        assertTrue(
                durationOnErrorResponse.
                        getMetadata().
                        getFailReasons().
                        contains(CommonFailReason.DATA_UNAVAILABLE)
        );
    }

    @Test
    public void newRequest_invalidPrepHours_invalidValueNotSaved() {
        // Arrange
        simulateNewInitialisationRequest();

        RecipeDurationRequest.Model invalidRequestModel = new RecipeDurationRequest.Model.Builder().
                basedOnResponseModel(durationOnErrorResponse.getModel()).
                setPrepHours(MAX_PREP_TIME / 60 + 1).
                build();
        RecipeDurationRequest request = new RecipeDurationRequest.Builder().
                setDomainId(VALID_NEW_EMPTY_DOMAIN_ID).
                setModel(invalidRequestModel).
                build();

        // Act
        handler.executeAsync(SUT, request, getUseCaseCallback());
        // Assert
        verifyNoMoreInteractions(repoMock);
    }

    @Test
    public void newRequest_invalidPrepHours_resultINVALID_CHANGED() {
        // Arrange
        simulateNewInitialisationRequest();

        RecipeDurationRequest.Model invalidModel = new RecipeDurationRequest.Model.Builder().
                basedOnResponseModel(durationOnErrorResponse.getModel()).
                setPrepHours(MAX_PREP_TIME / 60 + 1).
                build();
        RecipeDurationRequest request = new RecipeDurationRequest.Builder().
                setDomainId(VALID_NEW_EMPTY_DOMAIN_ID).
                setModel(invalidModel).
                build();
        // Act
        handler.executeAsync(SUT, request, getUseCaseCallback());
        // Assert
        assertEquals(
                RecipeMetadata.ComponentState.INVALID_CHANGED,
                durationOnErrorResponse.getMetadata().getState()
        );
    }

    @Test
    public void newRequest_invalidPrepHours_FaiReasonINVALID_PREP_TIME() {
        // Arrange
        simulateNewInitialisationRequest();
        expectedNoOfFailReasons = 1;

        RecipeDurationRequest.Model invalidModel = new RecipeDurationRequest.Model.Builder().
                basedOnResponseModel(durationOnErrorResponse.getModel()).
                setPrepHours(MAX_PREP_TIME / 60 + 1).
                build();
        RecipeDurationRequest request = new RecipeDurationRequest.Builder().
                setDomainId(VALID_NEW_EMPTY_DOMAIN_ID).
                setModel(invalidModel).
                build();
        // Act
        handler.executeAsync(SUT, request, getUseCaseCallback());
        // Assert
        assertEquals(
                expectedNoOfFailReasons,
                durationOnErrorResponse.
                        getMetadata().
                        getFailReasons().
                        size()
        );
        assertTrue(
                durationOnErrorResponse.
                        getMetadata().
                        getFailReasons().
                        contains(RecipeDuration.FailReason.INVALID_PREP_TIME));
    }

    @Test
    public void newRequest_validPrepHours_resultVALID_CHANGED() {
        // Arrange
        simulateNewInitialisationRequest();

        RecipeDurationRequest.Model validModel = new RecipeDurationRequest.Model.Builder().
                basedOnResponseModel(durationOnErrorResponse.getModel()).
                setPrepHours(VALID_NEW_PREP_TIME_VALID.getPrepTime() / 60).
                build();
        RecipeDurationRequest validRequest = new RecipeDurationRequest.Builder().
                setDomainId(VALID_NEW_EMPTY_DOMAIN_ID).
                setModel(validModel).
                build();
        // Act
        handler.executeAsync(SUT, validRequest, getUseCaseCallback());
        // Assert
        assertEquals(
                RecipeMetadata.ComponentState.VALID_CHANGED,
                durationOnSuccessResponse.getMetadata().getState()
        );
    }

    @Test
    public void newRequest_validPrepHours_failReasonNONE() {
        // Arrange
        simulateNewInitialisationRequest();

        RecipeDurationRequest.Model validModel = new RecipeDurationRequest.Model.Builder().
                basedOnResponseModel(durationOnErrorResponse.getModel()).
                setPrepHours(VALID_NEW_PREP_TIME_VALID.getPrepTime() / 60).
                build();
        RecipeDurationRequest validRequest = new RecipeDurationRequest.Builder().
                setDomainId(VALID_NEW_EMPTY_DOMAIN_ID).
                setModel(validModel).
                build();
        // Act
        handler.executeAsync(SUT, validRequest, getUseCaseCallback());
        // Assert
        assertTrue(
                durationOnSuccessResponse.
                        getMetadata().
                        getFailReasons().
                        contains(CommonFailReason.NONE));
    }

    @Test
    public void newRequest_validPrepHours_prepHoursSaved() {
        // Arrange
        whenTimeProviderCalledReturn(VALID_NEW_PREP_TIME_VALID.getCreateDate());
        when(idProviderMock.getUId()).thenReturn(VALID_NEW_PREP_TIME_VALID.getDataId());

        simulateNewInitialisationRequest();

        RecipeDurationRequest.Model validModel = new RecipeDurationRequest.Model.Builder().
                basedOnResponseModel(durationOnErrorResponse.getModel()).
                setPrepHours(VALID_NEW_PREP_TIME_VALID.getPrepTime() / 60).
                build();
        RecipeDurationRequest validRequest = new RecipeDurationRequest.Builder().
                setDomainId(VALID_NEW_EMPTY_DOMAIN_ID).
                setModel(validModel).
                build();

        // Act
        handler.executeAsync(SUT, validRequest, getUseCaseCallback());
        // Assert
        verify(repoMock).save(eq(VALID_NEW_PREP_TIME_VALID));
    }

    @Test
    public void newRequest_invalidPrepMinutes_invalidValueNotSaved() {
        // Arrange
        simulateNewInitialisationRequest();

        RecipeDurationRequest.Model invalidModel = new RecipeDurationRequest.Model.Builder().
                basedOnResponseModel(durationOnErrorResponse.getModel()).
                setPrepMinutes(MAX_PREP_TIME + 1).
                build();
        RecipeDurationRequest request = new RecipeDurationRequest.Builder().
                setDomainId(VALID_NEW_EMPTY_DOMAIN_ID).
                setModel(invalidModel).
                build();
        // Act
        handler.executeAsync(SUT, request, getUseCaseCallback());
        // Assert
        verifyNoMoreInteractions(repoMock);
    }

    @Test
    public void newRequest_invalidPrepMinutes_resultINVALID_CHANGED() {
        // Arrange
        simulateNewInitialisationRequest();

        RecipeDurationRequest.Model invalidModel = new RecipeDurationRequest.Model.Builder().
                basedOnResponseModel(durationOnErrorResponse.getModel()).
                setPrepMinutes(MAX_PREP_TIME + 1).
                build();
        RecipeDurationRequest request = new RecipeDurationRequest.Builder().
                setDomainId(VALID_NEW_EMPTY_DOMAIN_ID).
                setModel(invalidModel).
                build();
        // Act
        handler.executeAsync(SUT, request, getUseCaseCallback());
        // Assert
        assertEquals(
                RecipeMetadata.ComponentState.INVALID_CHANGED,
                durationOnErrorResponse.getMetadata().getState()
        );
    }

    @Test
    public void newRequest_invalidPrepMinutes_FaiReasonINVALID_PREP_TIME() {
        // Arrange
        simulateNewInitialisationRequest();
        expectedNoOfFailReasons = 1;
        RecipeDurationRequest.Model invalidModel = new RecipeDurationRequest.Model.Builder().
                basedOnResponseModel(durationOnErrorResponse.getModel()).
                setPrepMinutes(MAX_PREP_TIME + 1).
                build();
        RecipeDurationRequest request = new RecipeDurationRequest.Builder().
                setDomainId(VALID_NEW_EMPTY_DOMAIN_ID).
                setModel(invalidModel).
                build();
        // Act
        handler.executeAsync(SUT, request, getUseCaseCallback());
        // Assert
        assertEquals(expectedNoOfFailReasons, durationOnErrorResponse.getMetadata().getFailReasons().size());
        assertTrue(durationOnErrorResponse.getMetadata().getFailReasons().
                contains(FailReason.INVALID_PREP_TIME));
    }

    @Test
    public void newRequest_validPrepMinutes_resultVALID_CHANGED() {
        // Arrange
        simulateNewInitialisationRequest();

        RecipeDurationRequest.Model validModel = new RecipeDurationRequest.Model.Builder().
                basedOnResponseModel(durationOnErrorResponse.getModel()).
                setPrepMinutes(VALID_NEW_PREP_TIME_VALID.getPrepTime()).
                build();
        RecipeDurationRequest validRequest = new RecipeDurationRequest.Builder().
                setDomainId(VALID_NEW_EMPTY_DOMAIN_ID).
                setModel(validModel).
                build();
        // Act
        handler.executeAsync(SUT, validRequest, getUseCaseCallback());
        // Assert
        assertEquals(
                RecipeMetadata.ComponentState.VALID_CHANGED,
                durationOnSuccessResponse.getMetadata().getState()
        );
    }

    @Test
    public void newRequest_validPrepMinutes_failReasonNONE() {
        // Arrange
        simulateNewInitialisationRequest();
        expectedNoOfFailReasons = 1;

        RecipeDurationRequest.Model validModel = new RecipeDurationRequest.Model.Builder().
                basedOnResponseModel(durationOnErrorResponse.getModel()).
                setPrepMinutes(VALID_NEW_PREP_TIME_VALID.getPrepTime()).
                build();
        RecipeDurationRequest validRequest = new RecipeDurationRequest.Builder().
                setDomainId(VALID_NEW_EMPTY_DOMAIN_ID).
                setModel(validModel).
                build();
        // Act
        handler.executeAsync(SUT, validRequest, getUseCaseCallback());
        // Assert
        List<FailReasons> failReasons = durationOnSuccessResponse.getMetadata().getFailReasons();
        assertEquals(
                expectedNoOfFailReasons,
                failReasons.size()
        );
        assertTrue(failReasons.contains(CommonFailReason.NONE));
    }

    @Test
    public void newRequest_validPrepMinutes_prepMinutesSaved() {
        simulateNewInitialisationRequest();
        when(idProviderMock.getUId()).thenReturn(VALID_NEW_PREP_TIME_VALID.getDataId());
        whenTimeProviderCalledReturn(VALID_NEW_PREP_TIME_VALID.getCreateDate());

        RecipeDurationRequest.Model validModel = new RecipeDurationRequest.Model.Builder().
                basedOnResponseModel(durationOnErrorResponse.getModel()).
                setPrepMinutes(VALID_NEW_PREP_TIME_VALID.getPrepTime()).
                build();
        RecipeDurationRequest validRequest = new RecipeDurationRequest.Builder().
                setDomainId(VALID_NEW_EMPTY_DOMAIN_ID).
                setModel(validModel).
                build();
        // Act
        handler.executeAsync(SUT, validRequest, getUseCaseCallback());
        // Assert
        verify(repoMock).save(eq(VALID_NEW_PREP_TIME_VALID));
    }

    @Test
    public void newRequest_invalidCookHours_invalidValueNotSaved() {
        // Arrange
        simulateNewInitialisationRequest();

        RecipeDurationRequest.Model invalidModel = new RecipeDurationRequest.Model.Builder().
                basedOnResponseModel(durationOnErrorResponse.getModel()).
                setCookHours(MAX_COOK_TIME / 60 + 1).
                build();
        RecipeDurationRequest invalidRequest = new RecipeDurationRequest.Builder().
                setDomainId(VALID_NEW_EMPTY_DOMAIN_ID).
                setModel(invalidModel).
                build();
        // Act
        handler.executeAsync(SUT, invalidRequest, getUseCaseCallback());
        // Assert
        verifyNoMoreInteractions(repoMock);
    }

    @Test
    public void newRequest_invalidCookHours_resultINVALID_CHANGED() {
        // Arrange
        simulateNewInitialisationRequest();

        RecipeDurationRequest.Model invalidModel = new RecipeDurationRequest.Model.Builder().
                basedOnResponseModel(durationOnErrorResponse.getModel()).
                setCookHours(MAX_COOK_TIME / 60 + 1).
                build();
        RecipeDurationRequest invalidRequest = new RecipeDurationRequest.Builder().
                setDomainId(VALID_NEW_EMPTY_DOMAIN_ID).
                setModel(invalidModel).
                build();
        // Act
        handler.executeAsync(SUT, invalidRequest, getUseCaseCallback());
        // Assert
        assertEquals(
                RecipeMetadata.ComponentState.INVALID_CHANGED,
                durationOnErrorResponse.getMetadata().getState()
        );
    }

    @Test
    public void newRequest_invalidCookHours_failReasonINVALID_COOK_TIME() {
        // Arrange
        simulateNewInitialisationRequest();
        expectedNoOfFailReasons = 1;

        RecipeDurationRequest.Model invalidModel = new RecipeDurationRequest.Model.Builder().
                basedOnResponseModel(durationOnErrorResponse.getModel()).
                setCookHours(MAX_COOK_TIME / 60 + 1).
                build();
        RecipeDurationRequest invalidRequest = new RecipeDurationRequest.Builder().
                setDomainId(VALID_NEW_EMPTY_DOMAIN_ID).
                setModel(invalidModel).
                build();
        // Act
        handler.executeAsync(SUT, invalidRequest, getUseCaseCallback());
        // Assert
        List<FailReasons> failReasons = durationOnErrorResponse.getMetadata().getFailReasons();
        assertEquals(
                expectedNoOfFailReasons,
                failReasons.size()
        );
        assertTrue(failReasons.contains(FailReason.INVALID_COOK_TIME));
    }

    @Test
    public void newRequest_validCookHours_validValueSaved() {
        // Arrange
        whenTimeProviderCalledReturn(VALID_NEW_COOK_TIME_VALID.getCreateDate());
        when(idProviderMock.getUId()).thenReturn(VALID_NEW_COOK_TIME_VALID.getDataId());

        simulateNewInitialisationRequest();

        RecipeDurationRequest.Model validModel = new RecipeDurationRequest.Model.Builder().
                basedOnResponseModel(durationOnErrorResponse.getModel()).
                setCookHours(VALID_NEW_COOK_TIME_VALID.getCookTime() / 60).
                build();
        RecipeDurationRequest validRequest = new RecipeDurationRequest.Builder().
                setDomainId(VALID_NEW_EMPTY_DOMAIN_ID).
                setModel(validModel).
                build();
        // Act
        handler.executeAsync(SUT, validRequest, getUseCaseCallback());
        // Assert
        verify(repoMock).save(eq(VALID_NEW_COOK_TIME_VALID));
    }

    @Test
    public void newRequest_validCookHours_resultVALID_CHANGED() {
        simulateNewInitialisationRequest();

        RecipeDurationRequest.Model validModel = new RecipeDurationRequest.Model.Builder().
                basedOnResponseModel(durationOnErrorResponse.getModel()).
                setCookHours(VALID_NEW_COOK_TIME_VALID.getCookTime() / 60).
                build();
        RecipeDurationRequest validRequest = new RecipeDurationRequest.Builder().
                setDomainId(VALID_NEW_EMPTY_DOMAIN_ID).
                setModel(validModel).
                build();
        // Act
        handler.executeAsync(SUT, validRequest, getUseCaseCallback());
        // Assert
        assertEquals(
                RecipeMetadata.ComponentState.VALID_CHANGED,
                durationOnSuccessResponse.getMetadata().getState()
        );
    }

    @Test
    public void newRequest_validCookHours_failReasonNONE() {
        expectedNoOfFailReasons = 1;
        simulateNewInitialisationRequest();

        RecipeDurationRequest.Model validModel = new RecipeDurationRequest.Model.Builder().
                basedOnResponseModel(durationOnErrorResponse.getModel()).
                setCookHours(VALID_NEW_COOK_TIME_VALID.getCookTime() / 60).
                build();
        RecipeDurationRequest validRequest = new RecipeDurationRequest.Builder().
                setDomainId(VALID_NEW_EMPTY_DOMAIN_ID).
                setModel(validModel).
                build();
        // Act
        handler.executeAsync(SUT, validRequest, getUseCaseCallback());
        // Assert
        List<FailReasons> failReasons = durationOnSuccessResponse.getMetadata().getFailReasons();
        assertEquals(
                expectedNoOfFailReasons,
                failReasons.size()
        );
        assertTrue(failReasons.contains(CommonFailReason.NONE));
    }

    @Test
    public void newRequest_invalidCookMinutes_invalidValueNotSaved() {
        // Arrange
        simulateNewInitialisationRequest();

        RecipeDurationRequest.Model invalidModel = new RecipeDurationRequest.Model.Builder().
                basedOnResponseModel(durationOnErrorResponse.getModel()).
                setCookMinutes(MAX_COOK_TIME + 1).
                build();
        RecipeDurationRequest request = new RecipeDurationRequest.Builder().
                setDomainId(VALID_NEW_EMPTY_DOMAIN_ID).
                setModel(invalidModel).
                build();
        // Act
        handler.executeAsync(SUT, request, getUseCaseCallback());
        // Assert
        verifyNoMoreInteractions(repoMock);
    }

    @Test
    public void newRequest_invalidCookMinutes_resultINVALID_CHANGED() {
        // Arrange
        simulateNewInitialisationRequest();

        RecipeDurationRequest.Model invalidModel = new RecipeDurationRequest.Model.Builder().
                basedOnResponseModel(durationOnErrorResponse.getModel()).
                setCookMinutes(MAX_COOK_TIME + 1).
                build();
        RecipeDurationRequest request = new RecipeDurationRequest.Builder().
                setDomainId(VALID_NEW_EMPTY_DOMAIN_ID).
                setModel(invalidModel).
                build();
        // Act
        handler.executeAsync(SUT, request, getUseCaseCallback());
        // Assert
        assertEquals(
                RecipeMetadata.ComponentState.INVALID_CHANGED,
                durationOnErrorResponse.getMetadata().getState());
    }

    @Test
    public void newRequest_invalidCookMinutes_failReasonINVALID_COOK_TIME() {
        // Arrange
        expectedNoOfFailReasons = 1;
        simulateNewInitialisationRequest();

        RecipeDurationRequest.Model invalidModel = new RecipeDurationRequest.Model.Builder().
                basedOnResponseModel(durationOnErrorResponse.getModel()).
                setCookMinutes(MAX_COOK_TIME + 1).
                build();
        RecipeDurationRequest request = new RecipeDurationRequest.Builder().
                setDomainId(VALID_NEW_EMPTY_DOMAIN_ID).
                setModel(invalidModel).
                build();
        // Act
        handler.executeAsync(SUT, request, getUseCaseCallback());
        // Assert
        List<FailReasons> failReasons = durationOnErrorResponse.getMetadata().getFailReasons();

        assertEquals(
                expectedNoOfFailReasons,
                failReasons.size()
        );
        assertTrue(failReasons.contains(FailReason.INVALID_COOK_TIME));
    }

    @Test
    public void startNewId_validCookMinutes_validValueSaved() {
        // Arrange
        RecipeDurationPersistenceModel modelUnderTest = TestDataRecipeDuration.
                getValidNewCookTimeValid();

        whenTimeProviderCalledReturn(modelUnderTest.getCreateDate());
        whenIdProviderThenReturn(modelUnderTest.getDataId());

        simulateNewInitialisationRequest();

        RecipeDurationRequest.Model model = new RecipeDurationRequest.Model.Builder().
                basedOnResponseModel(durationOnErrorResponse.getModel()).
                setCookMinutes(MAX_COOK_TIME)
                .build();
        RecipeDurationRequest request = new RecipeDurationRequest.Builder().
                setDomainId(VALID_NEW_EMPTY_DOMAIN_ID).
                setModel(model).
                build();
        // Act
        handler.executeAsync(SUT, request, getUseCaseCallback());
        // Assert
        verify(repoMock).save(eq(VALID_NEW_COOK_TIME_VALID));
    }

    @Test
    public void newRequest_validCookMinutes_stateVALID_CHANGED() {
        // Arrange
        whenTimeProviderCalledReturn(VALID_NEW_COOK_TIME_VALID.getCreateDate());
        when(idProviderMock.getUId()).thenReturn(VALID_NEW_COOK_TIME_VALID.getDataId());

        simulateNewInitialisationRequest();

        RecipeDurationRequest.Model model = new RecipeDurationRequest.Model.Builder().
                basedOnResponseModel(durationOnErrorResponse.getModel()).
                setCookMinutes(MAX_COOK_TIME)
                .build();
        RecipeDurationRequest request = new RecipeDurationRequest.Builder().
                setDomainId(VALID_NEW_EMPTY_DOMAIN_ID).
                setModel(model).
                build();
        // Act
        handler.executeAsync(SUT, request, getUseCaseCallback());
        // Assert
        assertEquals(
                RecipeMetadata.ComponentState.VALID_CHANGED,
                durationOnSuccessResponse.getMetadata().getState()
        );
    }

    @Test
    public void newRequest_validCookMinutes_failReasonNONE() {
        // Arrange
        whenTimeProviderCalledReturn(VALID_NEW_COOK_TIME_VALID.getCreateDate());
        when(idProviderMock.getUId()).thenReturn(VALID_NEW_COOK_TIME_VALID.getDataId());
        expectedNoOfFailReasons = 1;

        simulateNewInitialisationRequest();

        RecipeDurationRequest.Model model = new RecipeDurationRequest.Model.Builder().
                basedOnResponseModel(durationOnErrorResponse.getModel()).
                setCookMinutes(MAX_COOK_TIME)
                .build();
        RecipeDurationRequest request = new RecipeDurationRequest.Builder().
                setDomainId(VALID_NEW_EMPTY_DOMAIN_ID).
                setModel(model).
                build();
        // Act
        handler.executeAsync(SUT, request, getUseCaseCallback());
        // Assert
        List<FailReasons> failReasons = durationOnSuccessResponse.getMetadata().getFailReasons();
        assertEquals(
                expectedNoOfFailReasons,
                failReasons.size()
        );
        assertTrue(failReasons.contains(CommonFailReason.NONE));
    }

    @Test
    public void newRequest_invalidPrepAndInvalidCookTime_failReasonsINVALID_PREP_TIME_INVALID_COOK_TIME() {
        // Arrange
        expectedNoOfFailReasons = 2;
        simulateNewInitialisationRequest();

        RecipeDurationRequest.Model model = new RecipeDurationRequest.Model.Builder().
                basedOnResponseModel(durationOnErrorResponse.getModel()).
                setPrepHours(MAX_PREP_TIME / 60).
                setPrepMinutes(1).
                setCookHours(MAX_COOK_TIME / 60).
                setCookMinutes(1)
                .build();
        RecipeDurationRequest request = new RecipeDurationRequest.Builder().
                setDomainId(VALID_NEW_EMPTY_DOMAIN_ID).
                setModel(model).
                build();
        // Act
        handler.executeAsync(SUT, request, getUseCaseCallback());
        // Assert
        assertEquals(
                RecipeMetadata.ComponentState.INVALID_CHANGED,
                durationOnErrorResponse.getMetadata().getState()
        );

        List<FailReasons> failReasons = durationOnErrorResponse.getMetadata().getFailReasons();
        assertEquals(
                expectedNoOfFailReasons,
                failReasons.size()
        );
        assertTrue(failReasons.contains(FailReason.INVALID_PREP_TIME));
        assertTrue(failReasons.contains(FailReason.INVALID_COOK_TIME));
    }

    @Test
    public void newRequest_validPrepTimeValidCookTime_failReasonsNONE() {
        // Arrange
        whenTimeProviderCalledReturn(VALID_NEW_COOK_TIME_VALID.getCreateDate());
        when(idProviderMock.getUId()).thenReturn(VALID_NEW_COOK_TIME_VALID.getDataId());
        expectedNoOfFailReasons = 1;

        simulateNewInitialisationRequest();

        RecipeDurationRequest.Model model = new RecipeDurationRequest.Model.Builder().
                basedOnResponseModel(durationOnErrorResponse.getModel()).
                setPrepHours(MAX_PREP_TIME / 60 - 1).
                setPrepMinutes(59).
                setCookHours(MAX_COOK_TIME / 60 - 1).
                setCookMinutes(59)
                .build();
        RecipeDurationRequest request = new RecipeDurationRequest.Builder().
                setDomainId(VALID_NEW_EMPTY_DOMAIN_ID).
                setModel(model).
                build();
        // Act
        handler.executeAsync(SUT, request, getUseCaseCallback());
        // Assert
        List<FailReasons> failReasons = durationOnSuccessResponse.getMetadata().getFailReasons();
        assertEquals(
                expectedNoOfFailReasons,
                failReasons.size()
        );
        assertTrue(failReasons.contains(CommonFailReason.NONE));
    }

    @Test
    public void existingRequest_validPrepAndCookTime_correctValuesReturned() {
        // Arrange / Act
        sendInitialiseRequestForValidExistingModel();
        // Assert
        assertEquals(
                MAX_PREP_TIME,
                durationOnSuccessResponse.getModel().getTotalPrepTime()
        );
        assertEquals(
                MAX_COOK_TIME,
                durationOnSuccessResponse.getModel().getTotalCookTime()
        );
    }

    @Test
    public void existingRequest_validPrepAndCookTime_statusVALID_UNCHANGED() {
        // Arrange / Act
        sendInitialiseRequestForValidExistingModel();
        // Assert
        assertEquals(
                RecipeMetadata.ComponentState.VALID_UNCHANGED,
                durationOnSuccessResponse.getMetadata().getState()
        );
    }

    @Test
    public void existingRequest_invalidPrepAndCookTime_statusINVALID_UNCHANGED() {
        // Arrange
        RecipeDurationRequest r = new RecipeDurationRequest.Builder().
                getDefault().
                setDomainId(INVALID_EXISTING_COMPLETE.getDomainId()).
                build();
        // Act
        handler.executeAsync(SUT, r, getUseCaseCallback());
        // Assert
        verify(repoMock).getActiveByDomainId(eq(INVALID_EXISTING_COMPLETE.getDomainId()),
                repoCallback.capture());
        repoCallback.getValue().onModelLoaded(INVALID_EXISTING_COMPLETE);

        assertEquals(
                RecipeMetadata.ComponentState.INVALID_UNCHANGED,
                durationOnErrorResponse.getMetadata().getState()
        );
    }

    // region helper methods -----------------------------------------------------------------------
    private UseCaseBase.Callback<RecipeDurationResponse> getUseCaseCallback() {

        return new UseCaseBase.Callback<RecipeDurationResponse>() {
            @Override
            public void onSuccessResponse(RecipeDurationResponse response) {
                durationOnSuccessResponse = response;
            }

            @Override
            public void onErrorResponse(RecipeDurationResponse response) {
                durationOnErrorResponse = response;
            }
        };
    }

    private void whenTimeProviderCalledReturn(long time) {
        when(timeProviderMock.getCurrentTimeInMills()).thenReturn(time);
    }

    private void whenIdProviderThenReturn(String dataId) {
        when(idProviderMock.getUId()).thenReturn(dataId);
    }

    private void simulateNewInitialisationRequest() {
        // Arrange
        RecipeDurationRequest request = new RecipeDurationRequest.Builder().
                getDefault().
                setDomainId(RecipeDurationTest.VALID_NEW_EMPTY_DOMAIN_ID).
                build();

        handler.executeAsync(SUT, request, getUseCaseCallback());
        simulateModelUnavailableReturnedFromRepo();
    }

    private void simulateNewInitialRequest(RecipeDurationPersistenceModel modelUnderTest) {

    }

    private void simulateModelUnavailableReturnedFromRepo() {
        verify(repoMock).getActiveByDomainId(eq(RecipeDurationTest.VALID_NEW_EMPTY_DOMAIN_ID),
                repoCallback.capture());
        repoCallback.getValue().onModelUnavailable();
    }

    private void sendInitialiseRequestForValidExistingModel() {
        // Arrange
        RecipeDurationRequest request = new RecipeDurationRequest.Builder().
                getDefault().
                setDomainId(VALID_EXISTING_COMPLETE_DOMAIN_ID).
                build();
        // Act
        handler.executeAsync(SUT, request, getUseCaseCallback());
        verify(repoMock).getActiveByDomainId(eq(VALID_EXISTING_COMPLETE_DOMAIN_ID),
                repoCallback.capture());
        repoCallback.getValue().onModelLoaded(VALID_EXISTING_COMPLETE);
    }
    // endregion helper methods --------------------------------------------------------------------

    // region helper classes -----------------------------------------------------------------------
    // endregion helper classes --------------------------------------------------------------------
}