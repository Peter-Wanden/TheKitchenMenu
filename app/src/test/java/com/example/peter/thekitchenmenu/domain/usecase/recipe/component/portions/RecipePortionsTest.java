package com.example.peter.thekitchenmenu.domain.usecase.recipe.component.portions;

import com.example.peter.thekitchenmenu.data.repository.DomainDataAccess.GetDomainModelCallback;
import com.example.peter.thekitchenmenu.data.repository.recipe.DataAccessRecipePortions;
import com.example.peter.thekitchenmenu.data.repository.recipe.portions.TestDataRecipePortions;
import com.example.peter.thekitchenmenu.domain.model.UseCaseMetadataModel;
import com.example.peter.thekitchenmenu.domain.usecasenew.common.metadata.ComponentState;
import com.example.peter.thekitchenmenu.domain.usecase.common.UseCaseBase;
import com.example.peter.thekitchenmenu.domain.usecasenew.common.failreasons.CommonFailReason;
import com.example.peter.thekitchenmenu.domain.usecasenew.common.failreasons.FailReasons;
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
    @Mock
    DataAccessRecipePortions repoPortionsMock;
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
    public void newRequest_stateVALID_DEFAULT_failReasonNONE_DATA_UNAVAILABLE() {
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
        verify(repoPortionsMock).getByDomainId(
                eq(defaultModel.getDomainId()),
                repoPortionsCallback.capture()
        );
        repoPortionsCallback.getValue().onPersistenceModelUnavailable();

        // assert response values
        UseCaseMetadataModel metadata = portionsOnErrorResponse.getMetadata();

        assertEquals(
                ComponentState.VALID_DEFAULT,
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
    public void newRequest_stateVALID_DEFAULT() {
        // Arrange
        // Act
        // act simulate new default request / response
        newRequest_stateVALID_DEFAULT_failReasonNONE_DATA_UNAVAILABLE();
        // Assert
        assertEquals(
                ComponentState.VALID_DEFAULT,
                portionsOnErrorResponse.getMetadata().getComponentState()
        );
    }

    @Test
    public void newRequest_invalidServingsInvalidSittings_resultINVALID_CHANGED() {
        // Arrange
        RecipePortionsPersistenceModel modelUnderTest = TestDataRecipePortions.
                getNewInvalidTooHighServingsInvalidTooHighSittings();

        // act simulate new default request / response
        newRequest_stateVALID_DEFAULT_failReasonNONE_DATA_UNAVAILABLE();

        // Arrange
        RecipePortionsRequest.DomainModel model = new RecipePortionsRequest.DomainModel.Builder().
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
        newRequest_stateVALID_DEFAULT_failReasonNONE_DATA_UNAVAILABLE();

        // Arrange
        RecipePortionsRequest.DomainModel model = new RecipePortionsRequest.DomainModel.Builder().
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
        newRequest_stateVALID_DEFAULT_failReasonNONE_DATA_UNAVAILABLE();

        // Arrange
        RecipePortionsRequest.DomainModel model = new RecipePortionsRequest.DomainModel.Builder().
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
        newRequest_stateVALID_DEFAULT_failReasonNONE_DATA_UNAVAILABLE();

        // Arrange
        RecipePortionsRequest.DomainModel model = new RecipePortionsRequest.DomainModel.Builder().
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
        // simulate new default request / response
        newRequest_stateVALID_DEFAULT_failReasonNONE_DATA_UNAVAILABLE();

        // expected eventual state
        RecipePortionsPersistenceModel modelUnderTest = TestDataRecipePortions.
                getNewValidServingsValidSittings();

        // Arrange
        RecipePortionsRequest.DomainModel model = new RecipePortionsRequest.DomainModel.Builder().
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

        verify(repoPortionsMock).getByDomainId(
                eq(modelUnderTest.getDomainId()),
                repoPortionsCallback.capture()
        );
        repoPortionsCallback.getValue().onPersistenceModelLoaded(modelUnderTest);

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
                portionsOnSuccessResponse.getMetadata().getLastUpdate()
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
        RecipePortionsRequest.DomainModel invalidModel = new RecipePortionsRequest.DomainModel.Builder().
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
        // Arrange
        // arrange initial model to load
        RecipePortionsPersistenceModel initialModel = TestDataRecipePortions.
                getExistingValidNinePortions();

        // arrange initial model to archive
        RecipePortionsPersistenceModel initialModelArchived = TestDataRecipePortions.
                getArchivedValidNinePortions();

        // Expected model after modification request
        RecipePortionsPersistenceModel newStateModel = TestDataRecipePortions.
                getExistingValidUpdatedServings();

        // new data id for updated model
        when(idProviderMock.getUId()).thenReturn(newStateModel.getDataId());
        // new last updated timestamp for initial model archive and new dates for new model creation
        when(timeProviderMock.getCurrentTimeInMills()).thenReturn(newStateModel.getLastUpdate());

        // arrange initial request, get by data id
        RecipePortionsRequest initialRequest = new RecipePortionsRequest.Builder().
                getDefault().
                setDataId(initialModel.getDataId()).
                build();

        // Act
        SUT.execute(initialRequest, new PortionsCallbackClient());

        // Assert
        verify(repoPortionsMock).getByDataId(
                eq(initialModel.getDataId()),
                repoPortionsCallback.capture()
        );
        repoPortionsCallback.getValue().onPersistenceModelLoaded(initialModel);

        // Arrange updated model
        RecipePortionsRequest.DomainModel model = new RecipePortionsRequest.DomainModel.Builder().
                basedResponseModel(portionsOnSuccessResponse.getDomainModel()).
                setServings(newStateModel.getServings()).
                build();
        RecipePortionsRequest updateServingRequest = new RecipePortionsRequest.Builder().
                basedOnResponse(portionsOnSuccessResponse).
                setDomainModel(model).
                build();
        // Act
        SUT.execute(updateServingRequest, new PortionsCallbackClient());
        // Assert
        verify(repoPortionsMock).save(eq(initialModelArchived));
        verify(repoPortionsMock).save(eq(newStateModel));
    }

    @Test
    public void existingRequest_invalidUpdatedSittings_invalidValueNotSaved() {
        // Arrange
        // arrange initial model to load data
        RecipePortionsPersistenceModel initialModel = TestDataRecipePortions.
                getExistingValidNinePortions();

        // arrange initial request to load data
        RecipePortionsRequest initialRequest = new RecipePortionsRequest.Builder().
                getDefault().
                setDomainId(initialModel.getDomainId()).
                build();
        // Act
        SUT.execute(initialRequest, new PortionsCallbackClient());
        // Assert
        verify(repoPortionsMock).getByDomainId(
                eq(initialModel.getDomainId()),
                repoPortionsCallback.capture()
        );
        repoPortionsCallback.getValue().onPersistenceModelLoaded(initialModel);

        // arrange invalid sittings update request
        RecipePortionsRequest.DomainModel model = new RecipePortionsRequest.DomainModel.Builder().
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
        // assert invalid values not saved
        verifyNoMoreInteractions(repoPortionsMock);

        // assert fail reasons
        List<FailReasons> expectedFailReasons = Collections.singletonList(FailReason.SITTINGS_TOO_HIGH);
        List<FailReasons> actualFailReasons = portionsOnErrorResponse.getMetadata().getFailReasons();
        assertEquals(
                expectedFailReasons,
                actualFailReasons
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
        verify(repoPortionsMock).getByDomainId(
                eq(initialModel.getDomainId()),
                repoPortionsCallback.capture()
        );
        repoPortionsCallback.getValue().onPersistenceModelLoaded(initialModel);

        // Arrange update request
        // arrange last update timestamp for archived model
        when(timeProviderMock.getCurrentTimeInMills()).thenReturn(
                archivedInitialModel.getLastUpdate(), finalModelUnderTest.getCreateDate()
        );
        // arrange data Id for save of new state
        when(idProviderMock.getUId()).thenReturn(finalModelUnderTest.getDataId()
        );

        RecipePortionsRequest.DomainModel model = new RecipePortionsRequest.DomainModel.Builder().
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