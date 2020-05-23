package com.example.peter.thekitchenmenu.domain.usecase.recipe.component.portions;

import com.example.peter.thekitchenmenu.commonmocks.UseCaseSchedulerMock;
import com.example.peter.thekitchenmenu.data.repository.DomainDataAccess.GetDomainModelCallback;
import com.example.peter.thekitchenmenu.data.repository.recipe.RepositoryRecipePortions;
import com.example.peter.thekitchenmenu.data.repository.recipe.metadata.TestDataRecipeMetadata;
import com.example.peter.thekitchenmenu.data.repository.recipe.portions.TestDataRecipePortions;
import com.example.peter.thekitchenmenu.domain.model.CommonFailReason;
import com.example.peter.thekitchenmenu.domain.model.FailReasons;
import com.example.peter.thekitchenmenu.domain.usecase.UseCaseBase;
import com.example.peter.thekitchenmenu.domain.usecase.UseCaseHandler;
import com.example.peter.thekitchenmenu.domain.usecase.recipe.component.metadata.RecipeMetadata.ComponentState;
import com.example.peter.thekitchenmenu.domain.utils.TimeProvider;
import com.example.peter.thekitchenmenu.domain.utils.UniqueIdProvider;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;

import static com.example.peter.thekitchenmenu.domain.usecase.recipe.component.portions.RecipePortions.FailReason;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

public class RecipePortionsTest {

    private static final String TAG = "tkm-" + RecipePortionsTest.class.getSimpleName() + ": ";

    // region constants ----------------------------------------------------------------------------
    public static final int MAX_SERVINGS = TestDataRecipePortions.MAX_SERVINGS;
    public static final int MAX_SITTINGS = TestDataRecipePortions.MAX_SITTINGS;
    // endregion constants -------------------------------------------------------------------------

    // region helper fields ------------------------------------------------------------------------
    private UseCaseHandler handler;
    @Mock
    RepositoryRecipePortions repoPortionsMock;
    @Captor
    ArgumentCaptor<GetDomainModelCallback<RecipePortionsPersistenceModel>> repoPortionsCallback;
    @Mock
    TimeProvider timeProviderMock;
    @Mock
    UniqueIdProvider idProviderMock;

    private List<FailReasons> failReasons;
    private int expectedNoOfFailReasons;
    private PortionsCallbackClient callbackClient;
    private RecipePortionsResponse portionsOnSuccessResponse;
    private RecipePortionsResponse portionsOnErrorResponse;

    // endregion helper fields ---------------------------------------------------------------------

    private RecipePortions SUT;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        handler = new UseCaseHandler(new UseCaseSchedulerMock());
        SUT = givenUseCase();
    }

    private RecipePortions givenUseCase() {
        return new RecipePortions(
                repoPortionsMock,
                idProviderMock,
                timeProviderMock,
                MAX_SERVINGS,
                MAX_SITTINGS
        );
    }

    @Test
    public void newRequest_defaultModelGenerated_failReasonDATA_UNAVAILABLE() {
        // Arrange // Act
        expectedNoOfFailReasons = 1;
        simulateNewInitialisationRequest();
        // Assert
        assertEquals(
                ComponentState.INVALID_UNCHANGED,
                portionsOnErrorResponse.getMetadata().getState()
        );
        failReasons = portionsOnErrorResponse.getMetadata().getFailReasons();
        assertEquals(
                expectedNoOfFailReasons,
                failReasons.size()
        );
        assertTrue(failReasons.contains(CommonFailReason.DATA_UNAVAILABLE)
        );
    }

    @Test
    public void newRequest_defaultModelGenerated_stateVALID_UNCHANGED() {
        // Arrange // Act
        simulateNewInitialisationRequest();
        // Assert
        assertEquals(
                ComponentState.INVALID_UNCHANGED,
                portionsOnErrorResponse.getMetadata().getState()
        );
    }

    @Test
    public void newRequest_invalidServingsInvalidSittings_resultINVALID_CHANGED() {
        // Arrange
        RecipePortionsPersistenceModel modelUnderTest = TestDataRecipePortions.
                getNewInvalidTooHighServingsInvalidTooHighSittings();
        expectedNoOfFailReasons = 2;

        simulateNewInitialisationRequest();

        RecipePortionsRequest.Model model = new RecipePortionsRequest.Model.Builder().
                setServings(modelUnderTest.getServings()).
                setSittings(modelUnderTest.getSittings()).
                build();

        RecipePortionsRequest request = new RecipePortionsRequest.Builder().
                basedOnResponse(portionsOnErrorResponse).
                setModel(model).
                build();

        handler.executeAsync(SUT, request, callbackClient);
        // Assert
        assertEquals(
                ComponentState.INVALID_CHANGED,
                portionsOnErrorResponse.getMetadata().getState()
        );
        assertEquals(
                expectedNoOfFailReasons,
                portionsOnErrorResponse.getMetadata().getFailReasons().size()
        );
        failReasons = portionsOnErrorResponse.getMetadata().getFailReasons();
        assertTrue(failReasons.contains(FailReason.SERVINGS_TOO_HIGH));
        assertTrue(failReasons.contains(FailReason.SITTINGS_TOO_HIGH));
    }

    @Test
    public void newRequest_invalidServingsValidSittings_resultINVALID_CHANGED() {
        // Arrange
        RecipePortionsPersistenceModel modelUnderTest = TestDataRecipePortions.
                getNewInvalidTooHighServingsValidSittings();
        expectedNoOfFailReasons = 1;

        simulateNewInitialisationRequest();

        RecipePortionsRequest.Model model = new RecipePortionsRequest.Model.Builder().
                setServings(modelUnderTest.getServings()).
                setSittings(modelUnderTest.getSittings()).
                build();

        RecipePortionsRequest request = new RecipePortionsRequest.Builder().
                basedOnResponse(portionsOnErrorResponse).
                setModel(model).
                build();

        handler.executeAsync(SUT, request, callbackClient);
        // Assert
        failReasons = portionsOnErrorResponse.getMetadata().getFailReasons();
        assertEquals(
                expectedNoOfFailReasons,
                failReasons.size()
        );
        assertTrue(failReasons.contains(FailReason.SERVINGS_TOO_HIGH)
        );
        assertEquals(
                ComponentState.INVALID_CHANGED,
                portionsOnErrorResponse.getMetadata().getState()
        );
    }

    @Test
    public void newRequest_validServingsInvalidSittings_resultINVALID_CHANGED() {
        // Arrange
        RecipePortionsPersistenceModel modelUnderTest = TestDataRecipePortions.
                getNewValidServingsInvalidTooHighSittings();

        expectedNoOfFailReasons = 1;

        simulateNewInitialisationRequest();

        RecipePortionsRequest.Model model = new RecipePortionsRequest.Model.Builder().
                setServings(modelUnderTest.getServings()).
                setSittings(modelUnderTest.getSittings()).
                build();

        RecipePortionsRequest request = new RecipePortionsRequest.Builder().
                basedOnResponse(portionsOnErrorResponse).
                setModel(model).
                build();

        handler.executeAsync(SUT, request, callbackClient);
        // Assert
        assertEquals(
                ComponentState.INVALID_CHANGED,
                portionsOnErrorResponse.getMetadata().getState()
        );
        failReasons = portionsOnErrorResponse.getMetadata().getFailReasons();
        assertEquals(
                expectedNoOfFailReasons,
                failReasons.size()
        );
        assertTrue(failReasons.contains(FailReason.SITTINGS_TOO_HIGH));
    }

    @Test
    public void newRequest_validServingsValidSittings_resultVALID_CHANGED() {
        // Arrange
        RecipePortionsPersistenceModel modelUnderTest = TestDataRecipePortions.
                getNewValidServingsValidSittings();
        expectedNoOfFailReasons = 1;

        simulateNewInitialisationRequest();

        RecipePortionsRequest.Model model = new RecipePortionsRequest.Model.Builder().
                setServings(modelUnderTest.getServings()).
                setSittings(modelUnderTest.getSittings()).
                build();

        RecipePortionsRequest request = new RecipePortionsRequest.Builder().
                basedOnResponse(portionsOnErrorResponse).
                setModel(model).
                build();

        // Required for save of new valid model
        whenIdProviderReturn(modelUnderTest.getDataId());
        whenTimeProviderReturn(modelUnderTest.getLastUpdate());

        // Act
        handler.executeAsync(SUT, request, callbackClient);

        // Assert
        assertEquals(
                ComponentState.VALID_CHANGED,
                portionsOnSuccessResponse.getMetadata().getState()
        );

        failReasons = portionsOnSuccessResponse.getMetadata().getFailReasons();
        assertEquals(
                expectedNoOfFailReasons,
                failReasons.size()
        );
        assertTrue(failReasons.contains(CommonFailReason.NONE)
        );
    }

    @Test
    public void newRequest_validServingsValidSittings_saved() {
        // Arrange
        RecipePortionsPersistenceModel modelUnderTest = TestDataRecipePortions.
                getNewValidServingsValidSittings();

        simulateNewInitialisationRequest();

        RecipePortionsRequest.Model model = new RecipePortionsRequest.Model.Builder().
                setServings(modelUnderTest.getServings()).
                setSittings(modelUnderTest.getSittings()).
                build();

        RecipePortionsRequest request = new RecipePortionsRequest.Builder().
                basedOnResponse(portionsOnErrorResponse).
                setModel(model).
                build();

        // Required for save of new valid model
        whenIdProviderReturn(modelUnderTest.getDataId()
        );
        whenTimeProviderReturn(modelUnderTest.getLastUpdate()
        );
        // Act
        handler.executeAsync(SUT, request, callbackClient);

        // Assert
        verify(repoPortionsMock).save(eq(modelUnderTest));
    }

    @Test
    public void existingRequest_validExistingValuesLoaded_equalValuesInResponse() {
        // Arrange
        RecipePortionsPersistenceModel modelUnderTest = TestDataRecipePortions.
                getExistingValidNinePortions();

        // Act
        simulateExistingInitialisationRequest(modelUnderTest);

        // Assert values equal
        assertEquals(
                modelUnderTest.getDataId(),
                portionsOnSuccessResponse.getDataId()
        );
        assertEquals(
                modelUnderTest.getDomainId(),
                portionsOnSuccessResponse.getDomainId()
        );
        assertEquals(
                modelUnderTest.getServings(),
                portionsOnSuccessResponse.getModel().getServings()
        );
        assertEquals(
                modelUnderTest.getSittings(),
                portionsOnSuccessResponse.getModel().getSittings()
        );
        assertEquals(
                modelUnderTest.getCreateDate(),
                portionsOnSuccessResponse.getMetadata().getCreateDate()
        );
        assertEquals(
                modelUnderTest.getLastUpdate(),
                portionsOnSuccessResponse.getMetadata().getLasUpdate()
        );
    }

    @Test
    public void existingRequest_validExistingValues_resultVALID_UNCHANGED() {
        // Arrange
        RecipePortionsPersistenceModel modelUnderTest = TestDataRecipePortions.
                getExistingValidNinePortions();
        expectedNoOfFailReasons = 1;

        // Act
        simulateExistingInitialisationRequest(modelUnderTest);

        // Assert
        assertEquals(
                ComponentState.VALID_UNCHANGED,
                portionsOnSuccessResponse.getMetadata().getState()
        );
        failReasons = portionsOnSuccessResponse.getMetadata().getFailReasons();
        assertEquals(
                expectedNoOfFailReasons,
                failReasons.size()
        );
        assertTrue(failReasons.contains(CommonFailReason.NONE));
    }

    @Test
    public void existingRequest_invalidUpdatedServings_invalidValueNotSaved() {
        // Arrange
        RecipePortionsPersistenceModel initialModel = TestDataRecipePortions.
                getExistingValidNinePortions();
        // Act // Assert
        simulateExistingInitialisationRequest(initialModel);

        // Arrange second request with invalid servings
        RecipePortionsRequest.Model invalidModel = new RecipePortionsRequest.Model.Builder().
                basedResponseModel(portionsOnSuccessResponse.getModel()).
                setServings(MAX_SERVINGS + 1).
                build();
        RecipePortionsRequest invalidRequest = new RecipePortionsRequest.Builder().
                basedOnResponse(portionsOnSuccessResponse).
                setModel(invalidModel).
                build();
        // Act
        handler.executeAsync(SUT, invalidRequest, callbackClient);
        // Assert
        verifyNoMoreInteractions(repoPortionsMock);
    }

    @Test
    public void existingRequest_validUpdatedServings_saved() {
        // Arrange initial model to load
        RecipePortionsPersistenceModel initialModel = TestDataRecipePortions.
                getExistingValidNinePortions();
        // Expected model after modification request
        RecipePortionsPersistenceModel modelUnderTest = TestDataRecipePortions.
                getExistingValidUpdatedServings();
        // new data id for updated model
        whenIdProviderReturn(modelUnderTest.getDataId());
        // new last updated timestamp for updated model
        whenTimeProviderReturn(modelUnderTest.getLastUpdate());

        // Act // Assert
        simulateExistingInitialisationRequest(initialModel);

        // Arrange updated model
        RecipePortionsRequest.Model model = new RecipePortionsRequest.Model.Builder().
                basedResponseModel(portionsOnSuccessResponse.getModel()).
                setServings(modelUnderTest.getServings()).
                build();
        RecipePortionsRequest validRequest = new RecipePortionsRequest.Builder().
                basedOnResponse(portionsOnSuccessResponse).
                setModel(model).
                build();
        // Act
        handler.executeAsync(SUT, validRequest, callbackClient);
        // Assert
        verify(repoPortionsMock).save(eq(modelUnderTest));
    }

    @Test
    public void existingRequest_invalidUpdatedSittings_invalidValueNotSaved() {
        // Arrange initial model to load
        RecipePortionsPersistenceModel initialModel = TestDataRecipePortions.
                getExistingValidNinePortions();

        // Act // Assert
        simulateExistingInitialisationRequest(initialModel);

        // Arrange update request
        RecipePortionsRequest.Model model = new RecipePortionsRequest.Model.Builder().
                basedResponseModel(portionsOnSuccessResponse.getModel()).
                setSittings(MAX_SITTINGS + 1).
                build();
        RecipePortionsRequest request = new RecipePortionsRequest.Builder().
                basedOnResponse(portionsOnSuccessResponse).
                setModel(model).
                build();

        // Act
        handler.executeAsync(SUT, request, callbackClient);
        // Assert
        verifyNoMoreInteractions(repoPortionsMock);

        assertTrue(portionsOnErrorResponse.getMetadata().
                getFailReasons().
                contains(FailReason.SITTINGS_TOO_HIGH)
        );
    }

    @Test
    public void existingId_validUpdatedSittings_saved() {
        // Arrange initial model to load
        RecipePortionsPersistenceModel initialModel = TestDataRecipePortions.
                getExistingValidNinePortions();

        RecipePortionsPersistenceModel modelUnderTest = TestDataRecipePortions.
                getExistingValidUpdatedSittings();

        // new data id for updated model
        whenIdProviderReturn(modelUnderTest.getDataId());
        // new last update timestamp for updated model
        whenTimeProviderReturn(modelUnderTest.getLastUpdate());

        // Act // Assert
        simulateExistingInitialisationRequest(initialModel);

        // Arrange update request
        RecipePortionsRequest.Model model = new RecipePortionsRequest.Model.Builder().
                basedResponseModel(portionsOnSuccessResponse.getModel()).
                setSittings(MAX_SITTINGS).
                build();
        RecipePortionsRequest request = new RecipePortionsRequest.Builder().
                basedOnResponse(portionsOnSuccessResponse).
                setModel(model).
                build();

        // Act
        handler.executeAsync(SUT, request, callbackClient);
        // Assert
        verify(repoPortionsMock).save(eq(modelUnderTest));
    }

    // region helper methods -----------------------------------------------------------------------
    private void simulateNewInitialisationRequest() {
        // Arrange
        callbackClient = new PortionsCallbackClient();

        RecipePortionsRequest initialisationRequest = new RecipePortionsRequest.Builder().
                getDefault().
                setDomainId(TestDataRecipeMetadata.getDataUnavailable().getDomainId()).
                build();

        // provide time for creation of new model
        whenTimeProviderReturn(TestDataRecipeMetadata.getDataUnavailable().getCreateDate());

        // Act
        handler.executeAsync(SUT, initialisationRequest, callbackClient);

        // Assert repo called, no model found, return model unavailable
        verify(repoPortionsMock).getActiveByDomainId(
                eq(TestDataRecipeMetadata.getDataUnavailable().getDomainId()),
                repoPortionsCallback.capture()
        );
        repoPortionsCallback.getValue().onModelUnavailable();
    }

    private void simulateExistingInitialisationRequest(
            RecipePortionsPersistenceModel modelUnderTest) {
        // Arrange
        callbackClient = new PortionsCallbackClient();

        RecipePortionsRequest request = new RecipePortionsRequest.Builder().
                getDefault().
                setDomainId(modelUnderTest.getDomainId()).
                build();
        // Act
        handler.executeAsync(SUT, request, callbackClient);
        // Assert
        verify(repoPortionsMock).getActiveByDomainId(
                eq(modelUnderTest.getDomainId()),
                repoPortionsCallback.capture()
        );
        repoPortionsCallback.getValue().onModelLoaded(modelUnderTest);
    }

    private void whenIdProviderReturn(String id) {
        when(idProviderMock.getUId()).thenReturn(id);
    }

    private void whenTimeProviderReturn(long time) {
        when(timeProviderMock.getCurrentTimeInMills()).thenReturn(time);
    }
    // endregion helper methods --------------------------------------------------------------------

    // region helper classes -----------------------------------------------------------------------
    private class PortionsCallbackClient
            implements UseCaseBase.Callback<RecipePortionsResponse> {

        private final String TAG = RecipePortionsTest.TAG +
                PortionsCallbackClient.class.getSimpleName() + ": ";

        @Override
        public void onSuccessResponse(RecipePortionsResponse r) {
            System.out.println(TAG + "onSuccess: " + r);
            portionsOnSuccessResponse = r;
        }

        @Override
        public void onErrorResponse(RecipePortionsResponse r) {
            System.out.println(TAG + "onError: " + r);
            portionsOnErrorResponse = r;
        }
    }
    // endregion helper classes --------------------------------------------------------------------
}