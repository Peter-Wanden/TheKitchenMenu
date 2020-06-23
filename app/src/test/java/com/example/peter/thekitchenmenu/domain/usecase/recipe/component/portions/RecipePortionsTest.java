package com.example.peter.thekitchenmenu.domain.usecase.recipe.component.portions;

import com.example.peter.thekitchenmenu.commonmocks.UseCaseSchedulerMock;
import com.example.peter.thekitchenmenu.data.repository.DomainDataAccess.GetDomainModelCallback;
import com.example.peter.thekitchenmenu.data.repository.recipe.RepositoryRecipePortions;
import com.example.peter.thekitchenmenu.data.repository.recipe.metadata.TestDataRecipeMetadata;
import com.example.peter.thekitchenmenu.data.repository.recipe.portions.TestDataRecipePortions;
import com.example.peter.thekitchenmenu.domain.model.UseCaseMetadataModel;
import com.example.peter.thekitchenmenu.domain.usecase.common.UseCaseBase;
import com.example.peter.thekitchenmenu.domain.usecase.common.UseCaseHandler;
import com.example.peter.thekitchenmenu.domain.usecase.common.failreasons.CommonFailReason;
import com.example.peter.thekitchenmenu.domain.usecase.common.failreasons.FailReasons;
import com.example.peter.thekitchenmenu.domain.usecase.recipe.component.metadata.RecipeMetadata.ComponentState;
import com.example.peter.thekitchenmenu.domain.utils.TimeProvider;
import com.example.peter.thekitchenmenu.domain.utils.UniqueIdProvider;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static com.example.peter.thekitchenmenu.domain.usecase.recipe.component.portions.RecipePortions.FailReason;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.times;
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
    public void newRequest_defaultModelGenerated_failReasonNONE_DATA_UNAVAILABLE() {
        // Arrange
        // This is the initial pre-test setup request for most tests cases, so check all return
        // values
        RecipePortionsPersistenceModel defaultModel = TestDataRecipePortions.
                getNewActiveDefault();

        RecipePortionsRequest request = new RecipePortionsRequest.Builder().
                getDefault().
                setDomainId(defaultModel.getDomainId()).
                build();

        // id and time for default save
        when(idProviderMock.getUId()).thenReturn(defaultModel.getDataId());
        when(timeProviderMock.getCurrentTimeInMills()).thenReturn(defaultModel.getCreateDate());

        // Act
        SUT.execute(request, new PortionsCallbackClient());

        // Assert persistence calls
        verify(repoPortionsMock).getActiveByDomainId(
                eq(defaultModel.getDomainId()),
                repoPortionsCallback.capture()
        );
        repoPortionsCallback.getValue().onDomainModelUnavailable();

        // assert response values
        UseCaseMetadataModel metadata = portionsOnErrorResponse.getMetadata();

        assertEquals(
                ComponentState.VALID_UNCHANGED,
                metadata.getComponentState()
        );
        List<FailReasons> expectedFailReasons = Arrays.asList(
                CommonFailReason.NONE,
                CommonFailReason.DATA_UNAVAILABLE
        );
        List<FailReasons> actualFailReasons = metadata.getFailReasons();
        assertEquals(
                expectedFailReasons,
                actualFailReasons
        );
    }

    @Test
    public void newRequest_defaultModelGenerated_stateVALID_UNCHANGED() {
        // Arrange
        // Act
        // act simulate new default request / response
        newRequest_defaultModelGenerated_failReasonNONE_DATA_UNAVAILABLE();
        // Assert
        assertEquals(
                ComponentState.VALID_UNCHANGED,
                portionsOnErrorResponse.getMetadata().getComponentState()
        );
    }

    @Test
    public void newRequest_invalidServingsInvalidSittings_resultINVALID_CHANGED() {
        // Arrange
        RecipePortionsPersistenceModel modelUnderTest = TestDataRecipePortions.
                getNewInvalidTooHighServingsInvalidTooHighSittings();

        // act simulate new default request / response
        newRequest_defaultModelGenerated_failReasonNONE_DATA_UNAVAILABLE();

        // Arrange
        RecipePortionsRequest.Model model = new RecipePortionsRequest.Model.Builder().
                setServings(modelUnderTest.getServings()).
                setSittings(modelUnderTest.getSittings()).
                build();

        RecipePortionsRequest request = new RecipePortionsRequest.Builder().
                basedOnResponse(portionsOnErrorResponse).
                setDomainModel(model).
                build();

        // Act
        SUT.execute(request, new PortionsCallbackClient());

        // Assert
        UseCaseMetadataModel metadata = portionsOnErrorResponse.getMetadata();
        assertEquals(
                ComponentState.INVALID_CHANGED,
                metadata.getComponentState()
        );

        List<FailReasons> expectedFailReasons = Arrays.asList(
                FailReason.SERVINGS_TOO_HIGH,
                FailReason.SITTINGS_TOO_HIGH,
                CommonFailReason.DATA_UNAVAILABLE
        );
        List<FailReasons> actualFailReasons = metadata.getFailReasons();
        assertEquals(
                expectedFailReasons,
                actualFailReasons
        );
    }

    @Test
    public void newRequest_invalidServingsValidSittings_resultINVALID_CHANGED() {
        // Arrange
        RecipePortionsPersistenceModel modelUnderTest = TestDataRecipePortions.
                getNewInvalidTooHighServingsValidSittings();

        // act simulate new default request / response
        newRequest_defaultModelGenerated_failReasonNONE_DATA_UNAVAILABLE();

        // Arrange
        RecipePortionsRequest.Model model = new RecipePortionsRequest.Model.Builder().
                setServings(modelUnderTest.getServings()).
                setSittings(modelUnderTest.getSittings()).
                build();

        RecipePortionsRequest request = new RecipePortionsRequest.Builder().
                basedOnResponse(portionsOnErrorResponse).
                setDomainModel(model).
                build();

        // Act
        SUT.execute(request, new PortionsCallbackClient());

        // Assert
        UseCaseMetadataModel metadata = portionsOnErrorResponse.getMetadata();
        assertEquals(
                ComponentState.INVALID_CHANGED,
                metadata.getComponentState()
        );

        List<FailReasons> expectedFailReasons = Arrays.asList(
                FailReason.SERVINGS_TOO_HIGH,
                CommonFailReason.DATA_UNAVAILABLE
        );
        List<FailReasons> actualFailReasons = metadata.getFailReasons();
        assertEquals(
                expectedFailReasons,
                actualFailReasons
        );
    }

    @Test
    public void newRequest_validServingsInvalidSittings_resultINVALID_CHANGED() {
        // Arrange
        RecipePortionsPersistenceModel modelUnderTest = TestDataRecipePortions.
                getNewValidServingsInvalidTooHighSittings();

        // act simulate new default request / response
        newRequest_defaultModelGenerated_failReasonNONE_DATA_UNAVAILABLE();

        // Arrange
        RecipePortionsRequest.Model model = new RecipePortionsRequest.Model.Builder().
                setServings(modelUnderTest.getServings()).
                setSittings(modelUnderTest.getSittings()).
                build();

        RecipePortionsRequest request = new RecipePortionsRequest.Builder().
                basedOnResponse(portionsOnErrorResponse).
                setDomainModel(model).
                build();

        // Act
        SUT.execute(request, new PortionsCallbackClient());

        // Assert
        UseCaseMetadataModel metadata = portionsOnErrorResponse.getMetadata();
        assertEquals(
                ComponentState.INVALID_CHANGED,
                metadata.getComponentState()
        );

        List<FailReasons> expectedFailReasons = Arrays.asList(
                FailReason.SITTINGS_TOO_HIGH,
                CommonFailReason.DATA_UNAVAILABLE
        );
        List<FailReasons> actualFailReasons = metadata.getFailReasons();
        assertEquals(
                expectedFailReasons,
                actualFailReasons
        );
    }

    @Test
    public void newRequest_validServingsValidSittings_resultVALID_CHANGED() {
        // Arrange
        RecipePortionsPersistenceModel modelUnderTest = TestDataRecipePortions.
                getNewValidServingsValidSittings();

        // act simulate new default request / response
        newRequest_defaultModelGenerated_failReasonNONE_DATA_UNAVAILABLE();

        // Arrange
        RecipePortionsRequest.Model model = new RecipePortionsRequest.Model.Builder().
                setServings(modelUnderTest.getServings()).
                setSittings(modelUnderTest.getSittings()).
                build();

        RecipePortionsRequest request = new RecipePortionsRequest.Builder().
                basedOnResponse(portionsOnErrorResponse).
                setDomainModel(model).
                build();

        // Required for save of new valid model
        when(idProviderMock.getUId()).thenReturn(modelUnderTest.getDataId());
        when(timeProviderMock.getCurrentTimeInMills()).thenReturn(modelUnderTest.getLastUpdate());

        // Act
        SUT.execute(request, new PortionsCallbackClient());

        // Assert
        UseCaseMetadataModel metadata = portionsOnSuccessResponse.getMetadata();
        assertEquals(
                ComponentState.VALID_CHANGED,
                metadata.getComponentState()
        );

        List<FailReasons> expectedFailReasons = Collections.singletonList(
                CommonFailReason.NONE
        );
        List<FailReasons> actualFailReasons = metadata.getFailReasons();
        assertEquals(
                expectedFailReasons,
                actualFailReasons
        );
    }

    @Test
    public void newRequest_validServingsValidSittings_saved() {
        // Arrange
        RecipePortionsPersistenceModel modelUnderTest = TestDataRecipePortions.
                getNewValidServingsValidSittings();

        // act simulate new default request / response
        newRequest_defaultModelGenerated_failReasonNONE_DATA_UNAVAILABLE();

        // Arrange
        RecipePortionsRequest.Model model = new RecipePortionsRequest.Model.Builder().
                setServings(modelUnderTest.getServings()).
                setSittings(modelUnderTest.getSittings()).
                build();

        RecipePortionsRequest request = new RecipePortionsRequest.Builder().
                basedOnResponse(portionsOnErrorResponse).
                setDomainModel(model).
                build();

        // Required for save of new valid model
        when(idProviderMock.getUId()).thenReturn(modelUnderTest.getDataId());
        when(timeProviderMock.getCurrentTimeInMills()).thenReturn(modelUnderTest.getLastUpdate());

        // Act
        SUT.execute(request, new PortionsCallbackClient());

        // Assert
        verify(repoPortionsMock).save(eq(modelUnderTest));
    }

    @Test
    public void existingRequest_validExistingValuesLoaded_equalValuesInResponse() {
        // Arrange
        RecipePortionsPersistenceModel modelUnderTest = TestDataRecipePortions.
                getExistingValidNinePortions();

        RecipePortionsRequest request = new RecipePortionsRequest.Builder().
                getDefault().
                setDomainId(modelUnderTest.getDomainId()).
                build();

        // Act
        SUT.execute(request, new PortionsCallbackClient());

        verify(repoPortionsMock).getActiveByDomainId(
                eq(modelUnderTest.getDomainId()),
                repoPortionsCallback.capture()
        );
        repoPortionsCallback.getValue().onDomainModelLoaded(modelUnderTest);

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
                portionsOnSuccessResponse.getDomainModel().getServings()
        );
        assertEquals(
                modelUnderTest.getSittings(),
                portionsOnSuccessResponse.getDomainModel().getSittings()
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
        // Arrange // Act
        existingRequest_validExistingValuesLoaded_equalValuesInResponse();

        // Assert
        UseCaseMetadataModel metadata = portionsOnSuccessResponse.getMetadata();
        assertEquals(
                ComponentState.VALID_UNCHANGED,
                metadata.getComponentState()
        );

        List<FailReasons> expectedFailReasons = Collections.singletonList(
                CommonFailReason.NONE
        );
        List<FailReasons> actualFailReasons = metadata.getFailReasons();
        assertEquals(
                expectedFailReasons,
                actualFailReasons
        );
    }

    @Test
    public void existingRequest_invalidUpdatedServings_invalidValueNotSaved() {
        // Arrange // Act // Assert
        existingRequest_validExistingValuesLoaded_equalValuesInResponse();

        RecipePortionsPersistenceModel modelUnderTest = TestDataRecipePortions.
                getExistingInvalidTooHighSittingsInvalidTooHighServings();

        // Arrange second request with invalid servings
        RecipePortionsRequest.Model invalidModel = new RecipePortionsRequest.Model.Builder().
                basedResponseModel(portionsOnSuccessResponse.getDomainModel()).
                setServings(modelUnderTest.getServings()).
                build();
        RecipePortionsRequest invalidRequest = new RecipePortionsRequest.Builder().
                basedOnResponse(portionsOnSuccessResponse).
                setDomainModel(invalidModel).
                build();
        // Act
        SUT.execute(invalidRequest, new PortionsCallbackClient());
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
        when(idProviderMock.getUId()).thenReturn(modelUnderTest.getDataId());
        // new last updated timestamp for updated model
        when(timeProviderMock.getCurrentTimeInMills()).thenReturn(modelUnderTest.getLastUpdate());

        // arrange initial request
        RecipePortionsRequest.Model initialRequestModel = new RecipePortionsRequest.Model.Builder().
                setServings(initialModel.getServings()).
                setSittings(initialModel.getSittings()).
                build();
        RecipePortionsRequest initialRequest = new RecipePortionsRequest.Builder().
                setDataId(initialModel.getDataId()).
                setDomainId(initialModel.getDomainId()).
                setDomainModel(initialRequestModel).
                build();

        // Act
        SUT.execute(initialRequest, new PortionsCallbackClient());

        // Assert
        verify(repoPortionsMock).getByDataId(
                eq(initialModel.getDataId()),
                repoPortionsCallback.capture()
        );
        repoPortionsCallback.getValue().onDomainModelLoaded(initialModel);

        // Arrange updated model
        RecipePortionsRequest.Model model = new RecipePortionsRequest.Model.Builder().
                basedResponseModel(portionsOnSuccessResponse.getDomainModel()).
                setServings(modelUnderTest.getServings()).
                build();
        RecipePortionsRequest validRequest = new RecipePortionsRequest.Builder().
                basedOnResponse(portionsOnSuccessResponse).
                setDomainModel(model).
                build();
        // Act
        SUT.execute(validRequest, new PortionsCallbackClient());
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
                basedResponseModel(portionsOnSuccessResponse.getDomainModel()).
                setSittings(MAX_SITTINGS + 1).
                build();
        RecipePortionsRequest request = new RecipePortionsRequest.Builder().
                basedOnResponse(portionsOnSuccessResponse).
                setDomainModel(model).
                build();

        // Act
        SUT.execute(request, new PortionsCallbackClient());
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

        RecipePortionsPersistenceModel archivedInitialModel = TestDataRecipePortions.
                getNewArchivedDefault();

        RecipePortionsPersistenceModel finalModelUnderTest = TestDataRecipePortions.
                getExistingValidUpdatedSittings();

        RecipePortionsRequest initialRequest = new RecipePortionsRequest.Builder().
                getDefault().
                setDomainId(initialModel.getDomainId()).
                build();

        // Act
        SUT.execute(initialRequest, new PortionsCallbackClient());

        // Assert
        verify(repoPortionsMock).getActiveByDomainId(
                eq(initialModel.getDomainId()),
                repoPortionsCallback.capture()
        );
        repoPortionsCallback.getValue().onDomainModelLoaded(initialModel);

        // Arrange update request
        // arrange last update timestamp for archived model
        when(timeProviderMock.getCurrentTimeInMills()).thenReturn(
                archivedInitialModel.getLastUpdate(), finalModelUnderTest.getCreateDate()
        );
        // arrange data Id for save of new state
        when(idProviderMock.getUId()).thenReturn(finalModelUnderTest.getDataId()
        );

        RecipePortionsRequest.Model model = new RecipePortionsRequest.Model.Builder().
                basedResponseModel(portionsOnSuccessResponse.getDomainModel()).
                setSittings(finalModelUnderTest.getSittings()).
                build();
        RecipePortionsRequest request = new RecipePortionsRequest.Builder().
                basedOnResponse(portionsOnSuccessResponse).
                setDomainModel(model).
                build();

        // Act
        SUT.execute(request, new PortionsCallbackClient());
        // Assert
        ArgumentCaptor<RecipePortionsPersistenceModel> ac = ArgumentCaptor.forClass(RecipePortionsPersistenceModel.class);
        verify(repoPortionsMock, times(2)).save(ac.capture());
        ac.getAllValues().forEach(models -> System.out.println(TAG + models));
    }

    // region helper methods -----------------------------------------------------------------------
    private void simulateNewInitialisationRequest() {
        RecipePortionsRequest initialisationRequest = new RecipePortionsRequest.Builder().
                getDefault().
                setDomainId(TestDataRecipeMetadata.getDataUnavailable().getDomainId()).
                build();

        // provide time for creation of new model
        whenTimeProviderReturn(TestDataRecipeMetadata.getDataUnavailable().getCreateDate());

        // Act
        handler.executeAsync(SUT, initialisationRequest, new PortionsCallbackClient());

        // Assert repo called, no model found, return model unavailable
        verify(repoPortionsMock).getActiveByDomainId(
                eq(TestDataRecipeMetadata.getDataUnavailable().getDomainId()),
                repoPortionsCallback.capture()
        );
        repoPortionsCallback.getValue().onDomainModelUnavailable();
    }

    private void simulateExistingInitialisationRequest(
            RecipePortionsPersistenceModel modelUnderTest) {
        // Arrange

        RecipePortionsRequest request = new RecipePortionsRequest.Builder().
                getDefault().
                setDomainId(modelUnderTest.getDomainId()).
                build();
        // Act
        handler.executeAsync(SUT, request, new PortionsCallbackClient());
        // Assert
        verify(repoPortionsMock).getActiveByDomainId(
                eq(modelUnderTest.getDomainId()),
                repoPortionsCallback.capture()
        );
        repoPortionsCallback.getValue().onDomainModelLoaded(modelUnderTest);
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
        public void onUseCaseSuccess(RecipePortionsResponse response) {
            System.out.println(TAG + "onSuccess: " + response);
            portionsOnSuccessResponse = response;
        }

        @Override
        public void onUseCaseError(RecipePortionsResponse response) {
            System.out.println(TAG + "onError: " + response);
            portionsOnErrorResponse = response;
        }
    }
    // endregion helper classes --------------------------------------------------------------------
}