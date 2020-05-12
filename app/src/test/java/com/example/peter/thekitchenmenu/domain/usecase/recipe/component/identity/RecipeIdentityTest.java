package com.example.peter.thekitchenmenu.domain.usecase.recipe.component.identity;

import com.example.peter.thekitchenmenu.commonmocks.UseCaseSchedulerMock;
import com.example.peter.thekitchenmenu.data.repository.DomainDataAccess.GetDomainModelCallback;
import com.example.peter.thekitchenmenu.data.repository.recipe.RepositoryRecipeIdentity;
import com.example.peter.thekitchenmenu.data.repository.recipe.identity.TestDataRecipeIdentity;
import com.example.peter.thekitchenmenu.domain.model.CommonFailReason;
import com.example.peter.thekitchenmenu.domain.model.FailReasons;
import com.example.peter.thekitchenmenu.domain.usecase.UseCase;
import com.example.peter.thekitchenmenu.domain.usecase.UseCaseHandler;
import com.example.peter.thekitchenmenu.domain.usecase.recipe.component.metadata.RecipeMetadata;
import com.example.peter.thekitchenmenu.domain.usecase.recipe.component.metadata.RecipeMetadata.ComponentState;
import com.example.peter.thekitchenmenu.domain.usecase.textvalidation.TextValidator;
import com.example.peter.thekitchenmenu.domain.usecase.textvalidation.TextValidatorTest;
import com.example.peter.thekitchenmenu.domain.utils.TimeProvider;
import com.example.peter.thekitchenmenu.domain.utils.UniqueIdProvider;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class RecipeIdentityTest {

    private static final String TAG = "tkm-" + RecipeIdentityTest.class.getSimpleName() + ": ";

    // region constants ----------------------------------------------------------------------------
    public static final int TITLE_MIN_LENGTH = TextValidatorTest.SHORT_TEXT_MIN_LENGTH;
    public static final int TITLE_MAX_LENGTH = TextValidatorTest.SHORT_TEXT_MAX_LENGTH;
    public static final int DESCRIPTION_MIN_LENGTH = TextValidatorTest.LONG_TEXT_MIN_LENGTH;
    public static final int DESCRIPTION_MAX_LENGTH = TextValidatorTest.LONG_TEXT_MAX_LENGTH;
    // endregion constants -------------------------------------------------------------------------

    // region helper fields ------------------------------------------------------------------------
    @Mock
    RepositoryRecipeIdentity repoIdentityMock;
    @Captor
    ArgumentCaptor<GetDomainModelCallback<RecipeIdentityPersistenceModel>> repoCallback;
    @Mock
    TimeProvider timeProviderMock;
    @Mock
    UniqueIdProvider idProvider;

    private UseCaseHandler handler;
    private IdentityCallbackClient callbackClient;
    private int expectedNoOfFailReasons;

    // endregion helper fields ---------------------------------------------------------------------

    private RecipeIdentity SUT;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        SUT = givenUseCase();
    }

    private RecipeIdentity givenUseCase() {
        handler = new UseCaseHandler(new UseCaseSchedulerMock());
        callbackClient = new IdentityCallbackClient();

        TextValidator textValidator = new TextValidator.Builder().
                setShortTextMinLength(TITLE_MIN_LENGTH).
                setShortTextMaxLength(TITLE_MAX_LENGTH).
                setLongTextMinLength(DESCRIPTION_MIN_LENGTH).
                setLongTextMaxLength(DESCRIPTION_MAX_LENGTH).
                build();

        return new RecipeIdentity(
                repoIdentityMock,
                idProvider,
                timeProviderMock,
                handler,
                textValidator);
    }

    @Test
    public void newRequest_stateINVALID_UNCHANGED_failReasonDATA_UNAVAILABLE() {
        // Arrange
        RecipeIdentityPersistenceModel modelUnderTest = TestDataRecipeIdentity.
                getInvalidNewEmpty();
        // Act
        simulateNewInitialisationRequest(modelUnderTest);
        // Assert
        assertEquals(
                ComponentState.INVALID_UNCHANGED,
                    callbackClient.onErrorResponse.
                            getMetadata().
                            getState()
        );
        assertTrue(
                callbackClient.onErrorResponse.
                        getMetadata().
                        getFailReasons().
                        contains(CommonFailReason.DATA_UNAVAILABLE)
        );
    }

    @Test
    public void newRequest_titleTooLongDescriptionTooLong_stateINVALID_CHANGED() {
        // Arrange
        RecipeIdentityPersistenceModel modelUnderTest = TestDataRecipeIdentity.
                getInvalidNewTitleTooLongDescriptionTooLong();

        simulateNewInitialisationRequest(modelUnderTest);

        RecipeIdentityRequest.Model model = new RecipeIdentityRequest.Model.Builder().
                setTitle(modelUnderTest.getTitle()).
                setDescription(modelUnderTest.getDescription()).
                build();

        RecipeIdentityRequest request = new RecipeIdentityRequest.Builder().
                setDataId(callbackClient.onErrorResponse.getDataId()).
                setDomainId(callbackClient.onErrorResponse.getDomainId()).
                setModel(model).
                build();

        // Act
        handler.execute(SUT, request, callbackClient);
        // Assert
        assertEquals(
                ComponentState.INVALID_CHANGED,
                callbackClient.onErrorResponse.getMetadata().getState()
        );
    }

    @Test
    public void newRequest_titleTooLongDescriptionTooLong_failReasonsTITLE_TOO_LONG_DESCRIPTION_TOO_LONG() {
        // Arrange
        RecipeIdentityPersistenceModel modelUnderTest = TestDataRecipeIdentity.
                getInvalidNewTitleTooLongDescriptionTooLong();
        expectedNoOfFailReasons = 2;

        simulateNewInitialisationRequest(modelUnderTest);

        RecipeIdentityRequest.Model model = new RecipeIdentityRequest.Model.Builder().
                setTitle(modelUnderTest.getTitle()).
                setDescription(modelUnderTest.getDescription()).
                build();

        RecipeIdentityRequest request = new RecipeIdentityRequest.Builder().
                setDataId(callbackClient.onErrorResponse.getDataId()).
                setDomainId(callbackClient.onErrorResponse.getDomainId()).
                setModel(model).
                build();

        // Act
        handler.execute(SUT, request, callbackClient);
        // Assert
        List<FailReasons> failReasons = callbackClient.onErrorResponse.
                getMetadata().
                getFailReasons();

        assertEquals(
                expectedNoOfFailReasons,
                failReasons.size()
        );
        assertTrue(failReasons.contains(RecipeIdentity.FailReason.TITLE_TOO_LONG));
        assertTrue(failReasons.contains(RecipeIdentity.FailReason.DESCRIPTION_TOO_LONG));
    }

    @Test
    public void newRequest_titleTooShortDescriptionValid_stateINVALID_CHANGED() {
        // Arrange
        RecipeIdentityPersistenceModel modelUnderTest = TestDataRecipeIdentity.
                getInvalidNewTitleTooShortDescriptionValid();
        expectedNoOfFailReasons = 1;

        simulateNewInitialisationRequest(modelUnderTest);

        RecipeIdentityRequest.Model model = new RecipeIdentityRequest.Model.Builder().
                setTitle(modelUnderTest.getTitle()).
                setDescription(modelUnderTest.getDescription()).
                build();

        RecipeIdentityRequest request = new RecipeIdentityRequest.Builder().
                setDataId(callbackClient.onErrorResponse.getDataId()).
                setDomainId(callbackClient.onErrorResponse.getDomainId()).
                setModel(model).
                build();

        // Act
        handler.execute(SUT, request, callbackClient);

        // Assert
        assertEquals(
                ComponentState.INVALID_CHANGED,
                callbackClient.onErrorResponse.getMetadata().getState()
        );

        List<FailReasons> failReasons = callbackClient.onErrorResponse.
                getMetadata().
                getFailReasons();
        assertEquals(
                expectedNoOfFailReasons,
                failReasons.size()
        );
        assertTrue(failReasons.contains(RecipeIdentity.FailReason.TITLE_TOO_SHORT));
    }

    @Test
    public void newRequest_titleTooShort_stateINVALID_CHANGED() {
        // Arrange
        RecipeIdentityPersistenceModel modelUnderTest = TestDataRecipeIdentity.
                getInvalidNewTitleTooShort();
        expectedNoOfFailReasons = 1;

        simulateNewInitialisationRequest(modelUnderTest);

        RecipeIdentityRequest.Model model = new RecipeIdentityRequest.Model.Builder().
                setTitle(modelUnderTest.getTitle()).
                setDescription(modelUnderTest.getDescription()).
                build();

        RecipeIdentityRequest request = new RecipeIdentityRequest.Builder().
                setDataId(callbackClient.onErrorResponse.getDataId()).
                setDomainId(callbackClient.onErrorResponse.getDomainId()).
                setModel(model).
                build();
        // Act
        handler.execute(SUT, request, callbackClient);

        // Assert
        assertEquals(
                ComponentState.INVALID_CHANGED,
                callbackClient.onErrorResponse.getMetadata().getState()
        );
        List<FailReasons> failReasons = callbackClient.onErrorResponse.
                getMetadata().
                getFailReasons();
        assertEquals(
                expectedNoOfFailReasons,
                failReasons.size()
        );
        assertTrue(failReasons.contains(RecipeIdentity.FailReason.TITLE_TOO_SHORT));
    }

    @Test
    public void newRequest_titleValid_valuesPersisted() {
        // Arrange
        RecipeIdentityPersistenceModel modelUnderTest = TestDataRecipeIdentity.
                getValidNewTitleValid();

        simulateNewInitialisationRequest(modelUnderTest);

        RecipeIdentityRequest.Model model = new RecipeIdentityRequest.Model.Builder().
                setTitle(modelUnderTest.getTitle()).
                setDescription(modelUnderTest.getDescription()).
                build();

        RecipeIdentityRequest request = new RecipeIdentityRequest.Builder().
                setDataId(callbackClient.onErrorResponse.getDataId()).
                setDomainId(callbackClient.onErrorResponse.getDomainId()).
                setModel(model).
                build();

        // title updated, new persistence model created
        when(timeProviderMock.getCurrentTimeInMills()).thenReturn(modelUnderTest.getLastUpdate());
        when(idProvider.getUId()).thenReturn(modelUnderTest.getDataId());
        // Act
        handler.execute(SUT, request, callbackClient);
        // Assert
        verify(repoIdentityMock).save(eq(modelUnderTest));
    }

    @Test
    public void newRequest_titleValid_stateVALID_CHANGED() {
        // Arrange
        RecipeIdentityPersistenceModel modelUnderTest = TestDataRecipeIdentity.
                getValidNewTitleValid();
        expectedNoOfFailReasons = 1;

        simulateNewInitialisationRequest(modelUnderTest);

        RecipeIdentityRequest.Model model = new RecipeIdentityRequest.Model.Builder().
                setTitle(modelUnderTest.getTitle()).
                setDescription(modelUnderTest.getDescription()).
                build();

        RecipeIdentityRequest request = new RecipeIdentityRequest.Builder().
                setDataId(callbackClient.onErrorResponse.getDataId()).
                setDomainId(callbackClient.onErrorResponse.getDomainId()).
                setModel(model).
                build();

        // new persistence model created, requires date stamp and new data id
        when(timeProviderMock.getCurrentTimeInMills()).thenReturn(modelUnderTest.getLastUpdate());
        when(idProvider.getUId()).thenReturn(modelUnderTest.getDataId());
        // Act
        handler.execute(SUT, request, callbackClient);
        // Assert
        assertEquals(
                RecipeMetadata.ComponentState.VALID_CHANGED,
                callbackClient.onSuccessResponse.getMetadata().getState()
        );
        List<FailReasons> failReasons = callbackClient.onSuccessResponse.
                getMetadata().
                getFailReasons();
        assertEquals(
                expectedNoOfFailReasons,
                failReasons.size()
        );
        assertTrue(failReasons.contains(CommonFailReason.NONE));
    }

    @Test
    public void newRequest_titleValidDescriptionValid_valuesPersisted() {
        // Arrange
        RecipeIdentityPersistenceModel modelUnderTest = TestDataRecipeIdentity.
                getValidNewComplete();

        simulateNewInitialisationRequest(modelUnderTest);

        RecipeIdentityRequest.Model model = new RecipeIdentityRequest.Model.Builder().
                setTitle(modelUnderTest.getTitle()).
                setDescription(modelUnderTest.getDescription()).
                build();

        RecipeIdentityRequest request = new RecipeIdentityRequest.Builder().
                setDataId(callbackClient.onErrorResponse.getDataId()).
                setDomainId(callbackClient.onErrorResponse.getDomainId()).
                setModel(model).
                build();

        // new persistence model created, requires date stamp and new data id
        when(timeProviderMock.getCurrentTimeInMills()).thenReturn(modelUnderTest.getLastUpdate());
        when(idProvider.getUId()).thenReturn(modelUnderTest.getDataId());
        // Act
        handler.execute(SUT, request, callbackClient);

        // Assert
        verify(repoIdentityMock).save(modelUnderTest);
        assertEquals(
                RecipeMetadata.ComponentState.VALID_CHANGED,
                callbackClient.onSuccessResponse.getMetadata().getState()
        );
    }

    @Test
    public void newRequest_titleValid_then_descriptionValid_stateVALID_CHANGED() {
        // Arrange
        RecipeIdentityPersistenceModel modelUnderTest = TestDataRecipeIdentity.
                getValidNewComplete();
        expectedNoOfFailReasons = 1;

        // Request 1: initialisation request
        simulateNewInitialisationRequest(modelUnderTest);

        // Request 2: Valid new title request
        RecipeIdentityRequest.Model titleModel = new RecipeIdentityRequest.Model.Builder().
                getDefault().
                setTitle(modelUnderTest.getTitle()).
                build();
        RecipeIdentityRequest titleRequest = new RecipeIdentityRequest.Builder().
                setDataId(callbackClient.onErrorResponse.getDataId()).
                setDomainId(callbackClient.onErrorResponse.getDomainId()).
                setModel(titleModel).
                build();

        // new persistence model created, requires date stamp and new data id
        when(timeProviderMock.getCurrentTimeInMills()).thenReturn(modelUnderTest.getLastUpdate());
        when(idProvider.getUId()).thenReturn(modelUnderTest.getDataId());
        // Act
        handler.execute(SUT, titleRequest, callbackClient);

        // Request 3: valid new description request, copy previous values from last response
        RecipeIdentityRequest.Model descriptionModel = new RecipeIdentityRequest.Model.Builder().
                basedOnResponseModel(callbackClient.onSuccessResponse.getModel()).
                setDescription(modelUnderTest.getDescription()).
                build();
        RecipeIdentityRequest descriptionRequest = new RecipeIdentityRequest.Builder().
                setDataId(callbackClient.onSuccessResponse.getDataId()).
                setDomainId(callbackClient.onSuccessResponse.getDomainId()).
                setModel(descriptionModel).
                build();

        // 2nd persistence model created, requires date stamp and new data id
        when(timeProviderMock.getCurrentTimeInMills()).thenReturn(modelUnderTest.getLastUpdate());
        when(idProvider.getUId()).thenReturn(modelUnderTest.getDataId());
        // Act
        handler.execute(SUT, descriptionRequest, callbackClient);

        // Assert second save equal to test model
        verify(repoIdentityMock).save(eq(modelUnderTest));

        // Assert state
        assertEquals(
                RecipeMetadata.ComponentState.VALID_CHANGED,
                callbackClient.onSuccessResponse.getMetadata().getState()
        );
        List<FailReasons> failReasons = callbackClient.onSuccessResponse.
                getMetadata().
                getFailReasons();
        assertEquals(
                expectedNoOfFailReasons,
                failReasons.size()
        );
        assertTrue(failReasons.contains(CommonFailReason.NONE)
        );
    }

    @Test
    public void existingRequest_titleValidDescriptionValid_VALID_UNCHANGED() {
        // Arrange
        RecipeIdentityPersistenceModel modelUnderTest = TestDataRecipeIdentity.
                getValidExistingTitleValidDescriptionValid();
        // Act
        simulateExistingInitialisationRequest(modelUnderTest);
        // Assert
        assertEquals(
                ComponentState.VALID_UNCHANGED,
                callbackClient.onSuccessResponse.getMetadata().getState()
        );
    }

    @Test
    public void existingRequest_titleValidDescriptionValid_correctValuesReturned() {
        // Arrange
        RecipeIdentityPersistenceModel modelUnderTest = TestDataRecipeIdentity.
                getValidExistingTitleValidDescriptionValid();
        // Act
        simulateExistingInitialisationRequest(modelUnderTest);

        // Assert metadata
        assertEquals(
                modelUnderTest.getDataId(),
                callbackClient.onSuccessResponse.getDataId()
        );
        assertEquals(
                modelUnderTest.getDomainId(),
                callbackClient.onSuccessResponse.getDomainId()
        );
        assertEquals(
                modelUnderTest.getCreateDate(),
                callbackClient.onSuccessResponse.getMetadata().getCreateDate()
        );
        assertEquals(
                modelUnderTest.getLastUpdate(),
                callbackClient.onSuccessResponse.getMetadata().getLasUpdate()
        );

        // Assert domain data
        assertEquals(
                modelUnderTest.getTitle(),
                callbackClient.onSuccessResponse.getModel().getTitle()
        );
        assertEquals(
                modelUnderTest.getDescription(),
                callbackClient.onSuccessResponse.getModel().getDescription()
        );
    }

    @Test
    public void existingRequest_titleTooShort_stateINVALID_UNCHANGED() {
        // Arrange
        RecipeIdentityPersistenceModel modelUnderTest = TestDataRecipeIdentity.
                getInvalidExistingTitleTooShort();
        // Act
        simulateExistingInitialisationRequest(modelUnderTest);
        // Assert
        assertEquals(
                RecipeMetadata.ComponentState.INVALID_UNCHANGED,
                callbackClient.onErrorResponse.getMetadata().getState()
        );
    }

    @Test
    public void existingRequest_titleTooShort_failReasonsTITLE_TOO_SHORT() {
        // Arrange
        RecipeIdentityPersistenceModel modelUnderTest = TestDataRecipeIdentity.
                getInvalidExistingTitleTooShort();
        expectedNoOfFailReasons = 1;
        // Act
        simulateExistingInitialisationRequest(modelUnderTest);
        // Assert
        List<FailReasons> failReasons = callbackClient.onErrorResponse.
                getMetadata().
                getFailReasons();
        assertEquals(
                expectedNoOfFailReasons,
                failReasons.size()
        );
        assertTrue(failReasons.contains(RecipeIdentity.FailReason.TITLE_TOO_SHORT));
    }

    @Test
    public void existingRequest_titleTooLong_stateINVALID_UNCHANGED() {
        // Arrange
        RecipeIdentityPersistenceModel modelUnderTest = TestDataRecipeIdentity.
                getInvalidExistingTitleTooLong();
        // Act
        simulateExistingInitialisationRequest(modelUnderTest);
        // Assert
        assertEquals(
                RecipeMetadata.ComponentState.INVALID_UNCHANGED,
                callbackClient.onErrorResponse.getMetadata().getState()
        );
    }

    @Test
    public void existingRequest_titleValidDescriptionTooLong_stateINVALID_UNCHANGED() {
        // Arrange
        RecipeIdentityPersistenceModel modelUnderTest = TestDataRecipeIdentity.
                getInvalidExistingTitleValidDescriptionTooLong();
        // Act
        simulateExistingInitialisationRequest(modelUnderTest);
        // Assert
        assertEquals(
                RecipeMetadata.ComponentState.INVALID_UNCHANGED,
                callbackClient.onErrorResponse.getMetadata().getState()
        );
    }

    @Test
    public void existingRequest_titleValidDescriptionTooLong_failReasonsDESCRIPTION_TOO_LONG() {
        // Arrange
        RecipeIdentityPersistenceModel modelUnderTest = TestDataRecipeIdentity.
                getInvalidExistingTitleValidDescriptionTooLong();
        expectedNoOfFailReasons = 1;
        // Act
        simulateExistingInitialisationRequest(modelUnderTest);
        // Assert
        List<FailReasons> failReasons = callbackClient.onErrorResponse.
                getMetadata().
                getFailReasons();
        assertEquals(
                expectedNoOfFailReasons,
                failReasons.size()
        );
        assertTrue(failReasons.contains(RecipeIdentity.FailReason.DESCRIPTION_TOO_LONG));
    }

    @Test
    public void existingRequest_titleTooShortDescriptionTooLong_stateINVALID_UNCHANGED() {
        // Arrange
        RecipeIdentityPersistenceModel modelUnderTest = TestDataRecipeIdentity.
                getInvalidExistingTitleTooShortDescriptionTooLong();
        // Act
        simulateExistingInitialisationRequest(modelUnderTest);
        // Assert
        assertEquals(
                RecipeMetadata.ComponentState.INVALID_UNCHANGED,
                callbackClient.onErrorResponse.getMetadata().getState()
        );
    }

    @Test
    public void existingRequest_titleTooShortDescriptionTooLong_failReasonsTITLE_TOO_SHORT_DESCRIPTION_TOO_LONG() {
        // Arrange
        RecipeIdentityPersistenceModel modelUnderTest = TestDataRecipeIdentity.
                getInvalidExistingTitleTooShortDescriptionTooLong();
        expectedNoOfFailReasons = 2;
        // Act
        simulateExistingInitialisationRequest(modelUnderTest);
        // Assert
        List<FailReasons> failReasons = callbackClient.onErrorResponse.getMetadata().getFailReasons();
        assertEquals(
                expectedNoOfFailReasons,
                failReasons.size()
        );
        assertTrue(failReasons.contains(RecipeIdentity.FailReason.TITLE_TOO_SHORT));
        assertTrue(failReasons.contains(RecipeIdentity.FailReason.DESCRIPTION_TOO_LONG));
    }

    @Test
    public void existingRequest_titleTooLongDescriptionTooLong_stateINVALID_UNCHANGED() {
        // Arrange
        RecipeIdentityPersistenceModel modelUnderTest = TestDataRecipeIdentity.
                getInvalidExistingTitleTooLongDescriptionTooLong();
        // Act
        simulateExistingInitialisationRequest(modelUnderTest);
        // Assert
        assertEquals(
                RecipeMetadata.ComponentState.INVALID_UNCHANGED,
                callbackClient.onErrorResponse.getMetadata().getState()
        );
    }

    @Test
    public void existingRequest_titleTooLongDescriptionTooLong_failReasonsTITLE_TOO_LONG_DESCRIPTION_TOO_LONG() {
        // Arrange
        RecipeIdentityPersistenceModel modelUnderTest = TestDataRecipeIdentity.
                getInvalidExistingTitleTooLongDescriptionTooLong();
        expectedNoOfFailReasons = 2;
        // Act
        simulateExistingInitialisationRequest(modelUnderTest);
        // Assert
        List<FailReasons> failReasons = callbackClient.onErrorResponse.
                getMetadata().
                getFailReasons();
        assertEquals(
                expectedNoOfFailReasons, failReasons.size()
        );
        assertTrue(failReasons.contains(RecipeIdentity.FailReason.TITLE_TOO_LONG));
        assertTrue(failReasons.contains(RecipeIdentity.FailReason.DESCRIPTION_TOO_LONG));
    }

    @Test
    public void existingRequest_titleValidDescriptionDefault_stateVAILD_UNCHANGED() {
        // Arrange
        RecipeIdentityPersistenceModel modelUnderTest = TestDataRecipeIdentity.
                getValidExistingTitleValid();
        // Act
        simulateExistingInitialisationRequest(modelUnderTest);
        // Assert
        assertEquals(
                RecipeMetadata.ComponentState.VALID_UNCHANGED,
                callbackClient.onSuccessResponse.getMetadata().getState()
        );
    }

    @Test
    public void existingRequest_titleValidDescriptionDefault_failReasonsNONE() {
        // Arrange
        RecipeIdentityPersistenceModel modelUnderTest = TestDataRecipeIdentity.
                getValidExistingTitleValid();
        expectedNoOfFailReasons = 1;
        // Act
        simulateExistingInitialisationRequest(modelUnderTest);
        // Assert
        List<FailReasons> failReasons = callbackClient.onSuccessResponse.
                getMetadata().
                getFailReasons();
        assertEquals(
                expectedNoOfFailReasons,
                failReasons.size()
        );
        assertTrue(failReasons.contains(CommonFailReason.NONE));
    }

    @Test
    public void existingRequest_titleValidDescriptionValid_stateVALID_UNCHANGED() {
        // Arrange
        RecipeIdentityPersistenceModel modelUnderTest = TestDataRecipeIdentity.
                getValidExistingTitleValid();
        // Act
        simulateExistingInitialisationRequest(modelUnderTest);
        // Assert
        assertEquals(
                RecipeMetadata.ComponentState.VALID_UNCHANGED,
                callbackClient.onSuccessResponse.getMetadata().getState());
    }

    @Test
    public void existingRequest_titleValidDescriptionValid_failReasonsNONE() {
        // Arrange
        RecipeIdentityPersistenceModel modelUnderTest = TestDataRecipeIdentity.
                getValidExistingTitleValidDescriptionValid();
        expectedNoOfFailReasons = 1;
        // Act
        simulateExistingInitialisationRequest(modelUnderTest);
        // Assert
        List<FailReasons> failReasons = callbackClient.onSuccessResponse.
                getMetadata().
                getFailReasons();
        assertEquals(
                expectedNoOfFailReasons,
                failReasons.size()
        );
        assertTrue(failReasons.contains(CommonFailReason.NONE));
    }

    @Test
    public void existingRequest_titleTooLong_failReasonsTITLE_TOO_LONG() {
        // Arrange
        RecipeIdentityPersistenceModel modelUnderTest = TestDataRecipeIdentity.
                getInvalidExistingTitleTooLong();
        // Act
        simulateExistingInitialisationRequest(modelUnderTest);
        // Assert
        assertEquals(1, callbackClient.onErrorResponse.getMetadata().getFailReasons().size());
        assertTrue(callbackClient.onErrorResponse.
                getMetadata().
                getFailReasons().
                contains(RecipeIdentity.FailReason.TITLE_TOO_LONG));
    }

    // region helper methods -----------------------------------------------------------------------
    private void simulateNewInitialisationRequest(
            RecipeIdentityPersistenceModel modelUnderTest) {
        // Arrange - data id requested when creating a new persistence model
        when(idProvider.getUId()).thenReturn(TestDataRecipeIdentity.
                getInvalidNewEmpty().
                getDataId());
        // create date and last update requested when creating a new persistence model
        when(timeProviderMock.getCurrentTimeInMills()).thenReturn(TestDataRecipeIdentity.
                getInvalidNewEmpty().
                getCreateDate());

        RecipeIdentityRequest initialisationRequest = new RecipeIdentityRequest.Builder().
                getDefault().
                setDomainId(modelUnderTest.getDomainId()).
                build();
        // Act
        handler.execute(SUT, initialisationRequest, callbackClient);
        // Assert repo called, no model found, return model unavailable
        verify(repoIdentityMock).getActiveByDomainId(
                eq(modelUnderTest.getDomainId()),
                repoCallback.capture()
        );
        repoCallback.getValue().onModelUnavailable();
    }

    private void simulateExistingInitialisationRequest(
            RecipeIdentityPersistenceModel modelUnderTest) {
        // Arrange
        RecipeIdentityRequest initialisationRequest = new RecipeIdentityRequest.Builder().
                getDefault().
                setDomainId(modelUnderTest.getDomainId()).
                build();
        // Act
        handler.execute(SUT, initialisationRequest, callbackClient);
        // Assert
        verify(repoIdentityMock).getActiveByDomainId(
                eq(modelUnderTest.getDomainId()),
                repoCallback.capture()
        );
        repoCallback.getValue().onModelLoaded(modelUnderTest);
    }
    // endregion helper methods --------------------------------------------------------------------

    // region helper classes -----------------------------------------------------------------------
    private static class IdentityCallbackClient
            implements UseCase.Callback<RecipeIdentityResponse> {

        private final String TAG = RecipeIdentityTest.TAG +
                IdentityCallbackClient.class.getSimpleName() + ": ";

        private RecipeIdentityResponse onSuccessResponse;
        private RecipeIdentityResponse onErrorResponse;

        @Override
        public void onSuccess(RecipeIdentityResponse r) {
            System.out.println(TAG + "onSuccess: " + r);
            onSuccessResponse = r;
        }

        @Override
        public void onError(RecipeIdentityResponse r) {
            System.out.println(TAG + "onError: " + r);
            onErrorResponse = r;
        }
    }
    // endregion helper classes --------------------------------------------------------------------
}