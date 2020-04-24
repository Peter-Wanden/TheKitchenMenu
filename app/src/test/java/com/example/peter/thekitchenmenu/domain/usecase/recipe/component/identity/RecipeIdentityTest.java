package com.example.peter.thekitchenmenu.domain.usecase.recipe.component.identity;

import com.example.peter.thekitchenmenu.commonmocks.UseCaseSchedulerMock;
import com.example.peter.thekitchenmenu.data.repository.DomainDataAccess.GetDomainModelCallback;
import com.example.peter.thekitchenmenu.data.repository.recipe.identity.TestDataRecipeIdentity;
import com.example.peter.thekitchenmenu.data.repository.source.local.dataadapter.PrimitiveDataSource.GetPrimitiveCallback;
import com.example.peter.thekitchenmenu.data.repository.source.local.recipe.identity.datasource.RecipeIdentityEntity;
import com.example.peter.thekitchenmenu.data.repository.recipe.RepositoryRecipeIdentity;
import com.example.peter.thekitchenmenu.domain.model.CommonFailReason;
import com.example.peter.thekitchenmenu.domain.model.FailReasons;
import com.example.peter.thekitchenmenu.domain.usecase.UseCaseHandler;
import com.example.peter.thekitchenmenu.domain.usecase.UseCase;
import com.example.peter.thekitchenmenu.domain.usecase.recipe.metadata.RecipeMetadata;
import com.example.peter.thekitchenmenu.domain.usecase.recipe.metadata.RecipeMetadata.ComponentState;
import com.example.peter.thekitchenmenu.domain.usecase.textvalidation.TextValidator;
import com.example.peter.thekitchenmenu.domain.utils.UniqueIdProvider;
import com.example.peter.thekitchenmenu.testdata.TestDataRecipeIdentityEntity;
import com.example.peter.thekitchenmenu.domain.utils.TimeProvider;

import org.junit.*;
import org.mockito.*;

import java.util.List;

import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.*;
import static org.junit.Assert.assertEquals;

public class RecipeIdentityTest {

    private static final String TAG = "tkm-" + RecipeIdentityTest.class.getSimpleName() + ": ";

    // region constants ----------------------------------------------------------------------------
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
    private RecipeIdentityResponse onSuccessResponse;
    private RecipeIdentityResponse onErrorResponse;

    private static final int SHORT_TEXT_MIN_LENGTH = 3;
    private static final int SHORT_TEXT_MAX_LENGTH = 70;
    private static final int LONG_TEXT_MIN_LENGTH = 0;
    private static final int LONG_TEXT_MAX_LENGTH = 500;

    public static final int TITLE_MIN_LENGTH = SHORT_TEXT_MIN_LENGTH;
    public static final int TITLE_MAX_LENGTH = SHORT_TEXT_MAX_LENGTH;
    public static final int DESCRIPTION_MIN_LENGTH = LONG_TEXT_MIN_LENGTH;
    public static final int DESCRIPTION_MAX_LENGTH = LONG_TEXT_MAX_LENGTH;
    // endregion helper fields ---------------------------------------------------------------------

    private RecipeIdentity SUT;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        handler = new UseCaseHandler(new UseCaseSchedulerMock());
        SUT = givenUseCase();
    }

    private RecipeIdentity givenUseCase() {
        TextValidator textValidator = new TextValidator.Builder().
                setShortTextMinLength(SHORT_TEXT_MIN_LENGTH).
                setShortTextMaxLength(SHORT_TEXT_MAX_LENGTH).
                setLongTextMinLength(LONG_TEXT_MIN_LENGTH).
                setLongTextMaxLength(LONG_TEXT_MAX_LENGTH).
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
        RecipeIdentityPersistenceModel testModel = TestDataRecipeIdentity.
                getInvalidNewEmpty();
        // Act
        simulateNewInitialisationRequest(testModel);
        // Assert
        assertEquals(
                ComponentState.INVALID_UNCHANGED,
                onErrorResponse.getMetadata().getState());

        assertTrue(
                onErrorResponse.getMetadata().
                        getFailReasons().
                        contains(CommonFailReason.DATA_UNAVAILABLE)
        );
    }

    @Test
    public void newRequest_titleTooLongDescriptionTooLong_stateINVALID_CHANGED() {
        // Arrange
        RecipeIdentityPersistenceModel testModel = TestDataRecipeIdentity.
                getInvalidNewTitleTooLongDescriptionTooLong();

        simulateNewInitialisationRequest(testModel);

        RecipeIdentityRequest.Model model = new RecipeIdentityRequest.Model.Builder().
                setTitle(testModel.getTitle()).
                setDescription(testModel.getDescription()).
                build();

        RecipeIdentityRequest request = new RecipeIdentityRequest.Builder().
                setDataId(onErrorResponse.getDataId()).
                setDomainId(onErrorResponse.getDomainId()).
                setModel(model).
                build();

        // Act
        handler.execute(SUT, request, callbackClient);
        // Assert
        assertEquals(
                ComponentState.INVALID_CHANGED,
                onErrorResponse.getMetadata().getState()
        );
    }

    @Test
    public void newRequest_titleTooLongDescriptionTooLong_failReasonsTITLE_TOO_LONG_DESCRIPTION_TOO_LONG() {
        // Arrange
        RecipeIdentityPersistenceModel testModel = TestDataRecipeIdentity.
                getInvalidNewTitleTooLongDescriptionTooLong();

        simulateNewInitialisationRequest(testModel);

        RecipeIdentityRequest.Model model = new RecipeIdentityRequest.Model.Builder().
                setTitle(testModel.getTitle()).
                setDescription(testModel.getDescription()).
                build();

        RecipeIdentityRequest request = new RecipeIdentityRequest.Builder().
                setDataId(onErrorResponse.getDataId()).
                setDomainId(onErrorResponse.getDomainId()).
                setModel(model).
                build();

        // Act
        handler.execute(SUT, request, callbackClient);
        // Assert
        List<FailReasons> failReasons = onErrorResponse.getMetadata().getFailReasons();
        assertTrue(failReasons.contains(RecipeIdentity.FailReason.TITLE_TOO_LONG));
        assertTrue(failReasons.contains(RecipeIdentity.FailReason.DESCRIPTION_TOO_LONG));
    }

    @Test
    public void newRequest_titleTooShortDescriptionValid_stateINVALID_CHANGED() {
        // Arrange
        RecipeIdentityPersistenceModel testModel = TestDataRecipeIdentity.
                getInvalidNewTitleTooShortDescriptionValid();

        simulateNewInitialisationRequest(testModel);

        RecipeIdentityRequest.Model model = new RecipeIdentityRequest.Model.Builder().
                setTitle(testModel.getTitle()).
                setDescription(testModel.getDescription()).
                build();

        RecipeIdentityRequest request = new RecipeIdentityRequest.Builder().
                setDataId(onErrorResponse.getDataId()).
                setDomainId(onErrorResponse.getDomainId()).
                setModel(model).
                build();

        // Act
        handler.execute(SUT, request, callbackClient);

        // Assert
        assertEquals(
                ComponentState.INVALID_CHANGED,
                onErrorResponse.getMetadata().getState()
        );
        List<FailReasons> failReasons = onErrorResponse.getMetadata().getFailReasons();
        assertTrue(failReasons.contains(RecipeIdentity.FailReason.TITLE_TOO_SHORT));
    }

    @Test
    public void newRequest_titleTooShort_stateINVALID_CHANGED() {
        // Arrange
        RecipeIdentityPersistenceModel testModel = TestDataRecipeIdentity.
                getInvalidNewTitleTooShort();

        simulateNewInitialisationRequest(testModel);

        RecipeIdentityRequest.Model model = new RecipeIdentityRequest.Model.Builder().
                setTitle(testModel.getTitle()).
                setDescription(testModel.getDescription()).
                build();

        RecipeIdentityRequest request = new RecipeIdentityRequest.Builder().
                setDataId(onErrorResponse.getDataId()).
                setDomainId(onErrorResponse.getDomainId()).
                setModel(model).
                build();
        // Act
        handler.execute(SUT, request, callbackClient);

        // Assert
        assertEquals(
                ComponentState.INVALID_CHANGED,
                onErrorResponse.getMetadata().getState()
        );
        List<FailReasons> failReasons = onErrorResponse.getMetadata().getFailReasons();
        assertTrue(failReasons.contains(RecipeIdentity.FailReason.TITLE_TOO_SHORT));
    }

    @Test
    public void newRequest_titleValid_valuesPersisted() {
        // Arrange
        RecipeIdentityPersistenceModel testModel = TestDataRecipeIdentity.
                getValidNewTitleValid();

        simulateNewInitialisationRequest(testModel);

        RecipeIdentityRequest.Model model = new RecipeIdentityRequest.Model.Builder().
                setTitle(testModel.getTitle()).
                setDescription(testModel.getDescription()).
                build();

        RecipeIdentityRequest request = new RecipeIdentityRequest.Builder().
                setDataId(onErrorResponse.getDataId()).
                setDomainId(onErrorResponse.getDomainId()).
                setModel(model).
                build();

        // title updated, new persistence model created
        when(timeProviderMock.getCurrentTimeInMills()).thenReturn(testModel.getLastUpdate());
        when(idProvider.getUId()).thenReturn(testModel.getDataId());
        // Act
        handler.execute(SUT, request, callbackClient);
        // Assert
        verify(repoIdentityMock).save(eq(testModel));
    }

    @Test
    public void newRequest_titleValid_stateVALID_CHANGED() {
        // Arrange
        RecipeIdentityPersistenceModel testModel = TestDataRecipeIdentity.
                getValidNewTitleValid();

        simulateNewInitialisationRequest(testModel);

        RecipeIdentityRequest.Model model = new RecipeIdentityRequest.Model.Builder().
                setTitle(testModel.getTitle()).
                setDescription(testModel.getDescription()).
                build();

        RecipeIdentityRequest request = new RecipeIdentityRequest.Builder().
                setDataId(onErrorResponse.getDataId()).
                setDomainId(onErrorResponse.getDomainId()).
                setModel(model).
                build();

        // new persistence model created, requires date stamp and new data id
        when(timeProviderMock.getCurrentTimeInMills()).thenReturn(testModel.getLastUpdate());
        when(idProvider.getUId()).thenReturn(testModel.getDataId());
        // Act
        handler.execute(SUT, request, callbackClient);
        // Assert
        assertEquals(
                RecipeMetadata.ComponentState.VALID_CHANGED,
                onSuccessResponse.getMetadata().getState()
        );
        List<FailReasons> failReasons = onSuccessResponse.getMetadata().getFailReasons();
        assertTrue(failReasons.contains(CommonFailReason.NONE));
    }

    @Test
    public void newRequest_titleValidDescriptionValid_valuesPersisted() {
        // Arrange
        RecipeIdentityPersistenceModel testModel = TestDataRecipeIdentity.
                getValidNewComplete();

        simulateNewInitialisationRequest(testModel);

        RecipeIdentityRequest.Model model = new RecipeIdentityRequest.Model.Builder().
                setTitle(testModel.getTitle()).
                setDescription(testModel.getDescription()).
                build();

        RecipeIdentityRequest request = new RecipeIdentityRequest.Builder().
                setDataId(onErrorResponse.getDataId()).
                setDomainId(onErrorResponse.getDomainId()).
                setModel(model).
                build();

        // new persistence model created, requires date stamp and new data id
        when(timeProviderMock.getCurrentTimeInMills()).thenReturn(testModel.getLastUpdate());
        when(idProvider.getUId()).thenReturn(testModel.getDataId());
        // Act
        handler.execute(SUT, request, callbackClient);

        // Assert
        verify(repoIdentityMock).save(testModel);
        assertEquals(
                RecipeMetadata.ComponentState.VALID_CHANGED,
                onSuccessResponse.getMetadata().getState()
        );
    }

    @Test
    public void newRequest_titleValidThenDescriptionValid_stateVALID_CHANGED() {
        // Arrange
        RecipeIdentityPersistenceModel testModel = TestDataRecipeIdentity.
                getValidNewComplete();

        simulateNewInitialisationRequest(testModel);

        // Request 1: Arrange valid new title request
        RecipeIdentityRequest.Model titleModel = new RecipeIdentityRequest.Model.Builder().
                getDefault().
                setTitle(testModel.getTitle()).
                build();
        RecipeIdentityRequest titleRequest = new RecipeIdentityRequest.Builder().
                setDataId(onErrorResponse.getDataId()).
                setDomainId(onErrorResponse.getDomainId()).
                setModel(titleModel).
                build();

        // new persistence model created, requires date stamp and new data id
        when(timeProviderMock.getCurrentTimeInMills()).thenReturn(testModel.getLastUpdate());
        when(idProvider.getUId()).thenReturn(testModel.getDataId());
        // Act
        handler.execute(SUT, titleRequest, callbackClient);

        // Request 2: Arrange valid new description request, copy previous values from last response
        RecipeIdentityRequest.Model descriptionModel = new RecipeIdentityRequest.Model.Builder().
                basedOnResponseModel(onSuccessResponse.getModel()).
                setDescription(testModel.getDescription()).
                build();
        RecipeIdentityRequest descriptionRequest = new RecipeIdentityRequest.Builder().
                setDataId(onSuccessResponse.getDataId()).
                setDomainId(onSuccessResponse.getDomainId()).
                setModel(descriptionModel).
                build();

        // another new persistence model created, requires date stamp and new data id
        when(timeProviderMock.getCurrentTimeInMills()).thenReturn(testModel.getLastUpdate());
        when(idProvider.getUId()).thenReturn(testModel.getDataId());
        // Act
        handler.execute(SUT, descriptionRequest, callbackClient);

        // Assert second save equal to test model
        verify(repoIdentityMock).save(eq(testModel));

        // Assert state
        assertEquals(
                RecipeMetadata.ComponentState.VALID_CHANGED,
                onSuccessResponse.getMetadata().getState()
        );
        List<FailReasons> failReasons = onSuccessResponse.getMetadata().getFailReasons();
        assertTrue(failReasons.contains(CommonFailReason.NONE)
        );
    }

    @Test
    public void existingRequest_titleValidDescriptionValid_VALID_UNCHANGED() {
        // Arrange
        RecipeIdentityPersistenceModel testModel = TestDataRecipeIdentity.
                getValidExistingTitleValidDescriptionValid();
        // Act
        simulateExistingInitialisationRequest(testModel);
        // Assert
        assertEquals(
                RecipeMetadata.ComponentState.VALID_UNCHANGED,
                onSuccessResponse.getMetadata().getState()
        );
    }

    @Test
    public void existingRequest_titleTooShort_stateINVALID_UNCHANGED() {
        // Arrange
        RecipeIdentityPersistenceModel testModel = TestDataRecipeIdentity.
                getInvalidExistingTitleTooShort();
        // Act
        simulateExistingInitialisationRequest(testModel);
        // Assert
        assertEquals(
                RecipeMetadata.ComponentState.INVALID_UNCHANGED,
                onErrorResponse.getMetadata().getState()
        );
    }

    @Test
    public void existingRequest_titleTooShort_failReasonsTITLE_TOO_SHORT() {
        // Arrange
        RecipeIdentityPersistenceModel testModel = TestDataRecipeIdentity.
                getInvalidExistingTitleTooShort();
        // Act
        simulateExistingInitialisationRequest(testModel);
        // Assert
        List<FailReasons> failReasons = onErrorResponse.getMetadata().getFailReasons();
        assertTrue(failReasons.contains(RecipeIdentity.FailReason.TITLE_TOO_SHORT));
    }

    @Test
    public void existingRequest_titleTooLong_stateINVALID_UNCHANGED() {
        // Arrange
        RecipeIdentityPersistenceModel testModel = TestDataRecipeIdentity.
                getInvalidExistingTitleTooLong();
        // Act
        simulateExistingInitialisationRequest(testModel);
        // Assert
        assertEquals(
                RecipeMetadata.ComponentState.INVALID_UNCHANGED,
                onErrorResponse.getMetadata().getState()
        );
    }

    @Test
    public void existingRequest_titleValidDescriptionTooLong_stateINVALID_UNCHANGED() {
        // Arrange
        RecipeIdentityPersistenceModel testModel = TestDataRecipeIdentity.
                getInvalidExistingTitleValidDescriptionTooLong();
        // Act
        simulateExistingInitialisationRequest(testModel);
        // Assert
        assertEquals(
                RecipeMetadata.ComponentState.INVALID_UNCHANGED,
                onErrorResponse.getMetadata().getState()
        );
    }

    @Test
    public void existingRequest_titleValidDescriptionTooLong_failReasonsDESCRIPTION_TOO_LONG() {
        // Arrange
        RecipeIdentityPersistenceModel testModel = TestDataRecipeIdentity.
                getInvalidExistingTitleValidDescriptionTooLong();
        // Act
        simulateExistingInitialisationRequest(testModel);
        // Assert
        List<FailReasons> failReasons = onErrorResponse.getMetadata().getFailReasons();
        assertTrue(failReasons.contains(RecipeIdentity.FailReason.DESCRIPTION_TOO_LONG));
    }

    @Test
    public void existingRequest_titleTooShortDescriptionTooLong_stateINVALID_UNCHANGED() {
        // Arrange
        RecipeIdentityPersistenceModel testModel = TestDataRecipeIdentity.
                getInvalidExistingTitleTooShortDescriptionTooLong();
        // Act
        simulateExistingInitialisationRequest(testModel);
        // Assert
        assertEquals(
                RecipeMetadata.ComponentState.INVALID_UNCHANGED,
                onErrorResponse.getMetadata().getState()
        );
    }

    @Test
    public void existingRequest_titleTooShortDescriptionTooLong_failReasonsTITLE_TOO_SHORT_DESCRIPTION_TOO_LONG() {
        // Arrange
        int expectedNumberOfFailReasons = 2;
        RecipeIdentityPersistenceModel testModel = TestDataRecipeIdentity.
                getInvalidExistingTitleTooShortDescriptionTooLong();
        // Act
        simulateExistingInitialisationRequest(testModel);
        // Assert
        List<FailReasons> failReasons = onErrorResponse.getMetadata().getFailReasons();
        assertEquals(
                expectedNumberOfFailReasons,
                failReasons.size()
        );
        assertTrue(failReasons.contains(RecipeIdentity.FailReason.TITLE_TOO_SHORT));
        assertTrue(failReasons.contains(RecipeIdentity.FailReason.DESCRIPTION_TOO_LONG));
    }

    @Test
    public void existingRequest_titleTooLongDescriptionTooLong_stateINVALID_UNCHANGED() {
        // Arrange
        RecipeIdentityPersistenceModel testModel = TestDataRecipeIdentity.
                getInvalidExistingTitleTooLongDescriptionTooLong();
        // Act
        simulateExistingInitialisationRequest(testModel);
        // Assert
        assertEquals(
                RecipeMetadata.ComponentState.INVALID_UNCHANGED,
                onErrorResponse.getMetadata().getState()
        );
    }

    @Test
    public void existingRequest_titleTooLongDescriptionTooLong_failReasonsTITLE_TOO_LONG_DESCRIPTION_TOO_LONG() {
        // Arrange
        int expectedNumberOfFailReasons = 2;
        RecipeIdentityPersistenceModel testModel = TestDataRecipeIdentity.
                getInvalidExistingTitleTooLongDescriptionTooLong();
        // Act
        simulateExistingInitialisationRequest(testModel);
        // Assert
        List<FailReasons> failReasons = onErrorResponse.getMetadata().getFailReasons();
        assertEquals(
                expectedNumberOfFailReasons, failReasons.size()
        );
        assertTrue(failReasons.contains(RecipeIdentity.FailReason.TITLE_TOO_LONG));
        assertTrue(failReasons.contains(RecipeIdentity.FailReason.DESCRIPTION_TOO_LONG));
    }

    @Test
    public void existingRequest_titleValid_stateVAILD_UNCHANGED() {
        // Arrange
        RecipeIdentityPersistenceModel testModel = TestDataRecipeIdentity.
                getValidExistingTitleValid();
        // Act
        simulateExistingInitialisationRequest(testModel);
        // Assert
        assertEquals(
                RecipeMetadata.ComponentState.VALID_UNCHANGED,
                onSuccessResponse.getMetadata().getState()
        );
    }

    @Test
    public void existingRequest_titleValidDescriptionDefault_failReasonsNONE() {
        // Arrange
        RecipeIdentityPersistenceModel testModel = TestDataRecipeIdentity.
                getValidExistingTitleValid();
        // Act
        simulateExistingInitialisationRequest(testModel);
        // Assert
        assertTrue(onSuccessResponse.
                getMetadata().
                getFailReasons().
                contains(CommonFailReason.NONE)
        );
    }

    @Test
    public void existingRequest_titleValidDescriptionValid_stateVALID_UNCHANGED() {
        // Arrange
        RecipeIdentityPersistenceModel testModel = TestDataRecipeIdentity.
                getValidExistingTitleValid();
        // Act
        simulateExistingInitialisationRequest(testModel);
        // Assert
        assertEquals(
                RecipeMetadata.ComponentState.VALID_UNCHANGED,
                onSuccessResponse.getMetadata().getState());
    }

    @Test
    public void existingRequest_titleValidDescriptionValid_failReasonsNONE() {
        // Arrange
        RecipeIdentityPersistenceModel testModel = TestDataRecipeIdentity.
                getValidExistingTitleValidDescriptionValid();
        // Act
        simulateExistingInitialisationRequest(testModel);
        // Assert
        assertEquals(1, onSuccessResponse.getMetadata().getFailReasons().size());
        assertTrue(onSuccessResponse.
                getMetadata().
                getFailReasons().
                contains(CommonFailReason.NONE)
        );
    }

    @Test
    public void existingRequest_titleTooLong_failReasonsTITLE_TOO_LONG() {
        // Arrange
        RecipeIdentityPersistenceModel testModel = TestDataRecipeIdentity.
                getInvalidExistingTitleTooLong();
        // Act
        simulateExistingInitialisationRequest(testModel);
        // Assert
        assertEquals(1, onErrorResponse.getMetadata().getFailReasons().size());
        assertTrue(onErrorResponse.
                getMetadata().
                getFailReasons().
                contains(RecipeIdentity.FailReason.TITLE_TOO_LONG));
    }

    // region helper methods -----------------------------------------------------------------------
    private void simulateNewInitialisationRequest(RecipeIdentityPersistenceModel modelUnderTest) {
        // Arrange
        callbackClient = new IdentityCallbackClient();

        // data id requested when creating a new persistence model
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
        // Assert
        verify(repoIdentityMock).getActiveByDomainId(
                eq(modelUnderTest.getDomainId()),
                repoCallback.capture()
        );
        repoCallback.getValue().onModelUnavailable();
    }

    private void simulateExistingInitialisationRequest(
            RecipeIdentityPersistenceModel modelUnderTest) {
        // Arrange
        callbackClient = new IdentityCallbackClient();

        RecipeIdentityRequest initialisationRequest = new RecipeIdentityRequest.Builder().
                getDefault().
                setDomainId(modelUnderTest.getDomainId()).
                build();
        // Act
        handler.execute(SUT, initialisationRequest, callbackClient);
        verify(repoIdentityMock).getActiveByDomainId(
                eq(modelUnderTest.getDomainId()),
                repoCallback.capture()
        );
        repoCallback.getValue().onModelLoaded(modelUnderTest);
    }
    // endregion helper methods --------------------------------------------------------------------

    // region helper classes -----------------------------------------------------------------------
    private class IdentityCallbackClient
            implements UseCase.Callback<RecipeIdentityResponse> {

        private final String TAG = RecipeIdentityTest.TAG +
                IdentityCallbackClient.class.getSimpleName() + ": ";

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