package com.example.peter.thekitchenmenu.domain.usecasenew.recipe.component.portions;

import com.example.peter.thekitchenmenu.data.repository.DomainDataAccess.GetDomainModelCallback;
import com.example.peter.thekitchenmenu.data.repository.recipe.RecipePortionsUseCasseDataAccess;
import com.example.peter.thekitchenmenu.data.repository.recipe.portions.TestDataRecipePortions;
import com.example.peter.thekitchenmenu.domain.usecasenew.common.failreasons.CommonFailReason;
import com.example.peter.thekitchenmenu.domain.usecasenew.common.failreasons.FailReasons;
import com.example.peter.thekitchenmenu.domain.usecasenew.common.message.UseCaseCallback;
import com.example.peter.thekitchenmenu.domain.usecasenew.common.message.UseCaseResponse;
import com.example.peter.thekitchenmenu.domain.usecasenew.common.metadata.ComponentState;
import com.example.peter.thekitchenmenu.domain.usecasenew.common.metadata.UseCaseMetadataModel;
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

import static com.example.peter.thekitchenmenu.data.repository.recipe.portions.TestDataRecipePortions.MIN_SERVINGS;
import static com.example.peter.thekitchenmenu.data.repository.recipe.portions.TestDataRecipePortions.MIN_SITTINGS;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

public class RecipePortionsUseCaseTest {

//    private static final String TAG = "tkm-" + RecipePortionsUseCaseTest.class.getSimpleName() +
//            ": ";

    // region constants ----------------------------------------------------------------------------
    public static final int MAX_SERVINGS = TestDataRecipePortions.MAX_SERVINGS;
    public static final int MAX_SITTINGS = TestDataRecipePortions.MAX_SITTINGS;
    // endregion constants -------------------------------------------------------------------------

    // region helper fields ------------------------------------------------------------------------
    @Mock
    RecipePortionsUseCasseDataAccess dataAccessMock;
    @Captor
    ArgumentCaptor<GetDomainModelCallback<RecipePortionsUseCasePersistenceModel>> dataAccessCallback;
    @Mock
    TimeProvider timeProviderMock;
    @Mock
    UniqueIdProvider idProviderMock;

    private UseCaseResponse<RecipePortionsUseCaseResponseModel> onSuccessResponse;
    private UseCaseResponse<RecipePortionsUseCaseResponseModel> onErrorResponse;
    // endregion helper fields ---------------------------------------------------------------------

    private RecipePortionsUseCase SUT;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        SUT = givenUseCase();
    }

    private RecipePortionsUseCase givenUseCase() {
        RecipePortionsDomainModelConverter converter = new RecipePortionsDomainModelConverter(
                timeProviderMock, idProviderMock
        );
        return new RecipePortionsUseCase(dataAccessMock, converter, MAX_SERVINGS, MAX_SITTINGS);
    }

    @Test
    public void emptyRequest_stateVALID_DEFAULT_failReasonsDATA_UNAVAILABLE() {
        // Arrange
        // Act
        SUT.execute(
                new RecipePortionsUseCaseRequest.Builder().getDefault().build(),
                new UseCaseCallbackImplementer()
        );

        // Assert
        UseCaseResponse<RecipePortionsUseCaseResponseModel> response = onErrorResponse;
        UseCaseMetadataModel metadata = response.getUseCaseMetadataModel();
        RecipePortionsUseCaseResponseModel responseModel = response.getResponseModel();

        ComponentState expectedState = ComponentState.VALID_DEFAULT;
        ComponentState actualState = metadata.getComponentState();
        assertEquals(
                expectedState,
                actualState
        );

        List<FailReasons> expectedFailReasons = Collections.singletonList(
                CommonFailReason.DATA_UNAVAILABLE);
        List<FailReasons> actualFailReasons = metadata.getFailReasons();
        assertEquals(
                expectedFailReasons,
                actualFailReasons
        );

        RecipePortionsUseCaseResponseModel expectedModel = new RecipePortionsUseCaseResponseModel.Builder()
                .getDefault()
                .build();
        assertEquals(
                expectedModel,
                responseModel
        );
    }

    @Test
    public void newRequest_stateVALID_DEFAULT_failReasonsNONE_DATA_UNAVAILABLE() {
        // Arrange
        // this persistence models values represent the default state of the use case.
        // Use case default states are not persisted, however this 'fake' model has been
        // created for testing purposes.
        RecipePortionsUseCasePersistenceModel modelUnderTest = TestDataRecipePortions.
                getNewActiveDefault();

        // Act / Assert
        // execute and assert persistence calls
        simulateDataUnavailable(modelUnderTest);

        // Assert response values
        UseCaseResponse<RecipePortionsUseCaseResponseModel> response = onErrorResponse;
        UseCaseMetadataModel metadata = response.getUseCaseMetadataModel();
        RecipePortionsUseCaseResponseModel responseModel = response.getResponseModel();

        ComponentState expectedState = ComponentState.VALID_DEFAULT;
        ComponentState actualState = metadata.getComponentState();
        assertEquals(
                expectedState,
                actualState
        );

        List<FailReasons> expectedFailReasons = Collections.singletonList(
                CommonFailReason.DATA_UNAVAILABLE);
        List<FailReasons> actualFailReasons = metadata.getFailReasons();
        assertEquals(
                expectedFailReasons,
                actualFailReasons
        );

        assertDomainData(modelUnderTest, responseModel);
    }

    @Test
    public void newRequest_stateINVALID_CHANGED_failReasonsSERVINGS_TOO_LOW_DATA_UNAVAILABLE() {
        // Arrange
        // arrange invalid persistence model to hold test data
        RecipePortionsUseCasePersistenceModel modelUnderTest = TestDataRecipePortions
                .getNewInvalidServingsTooLowSittingsValid();

        // initialise use case
        simulateDataUnavailable(modelUnderTest);

        RecipePortionsUseCaseRequest request = new RecipePortionsUseCaseRequest.Builder()
                .basedOnResponse(onErrorResponse)
                .setRequestModel(new RecipePortionsUseCaseRequestModel.Builder()
                        .setServings(modelUnderTest.getServings())
                        .setSittings(modelUnderTest.getSittings())
                        .build())
                .build();

        // Act
        SUT.execute(request, new UseCaseCallbackImplementer());

        // Assert response values
        UseCaseResponse<RecipePortionsUseCaseResponseModel> response = onErrorResponse;
        UseCaseMetadataModel metadata = response.getUseCaseMetadataModel();
        RecipePortionsUseCaseResponseModel responseModel = response.getResponseModel();

        ComponentState expectedState = ComponentState.INVALID_CHANGED;
        ComponentState actualState = metadata.getComponentState();
        assertEquals(
                expectedState,
                actualState
        );

        List<FailReasons> expectedFailReasons = Arrays.asList(
                RecipePortionsUseCaseFailReason.SERVINGS_TOO_LOW,
                CommonFailReason.DATA_UNAVAILABLE);
        List<FailReasons> actualFailReasons = metadata.getFailReasons();
        assertEquals(
                expectedFailReasons,
                actualFailReasons
        );
        assertDomainData(modelUnderTest, responseModel);
    }

    @Test
    public void newRequest_stateINVALID_CHANGED_failReasonsSERVINGS_TOO_HIGH_DATA_UNAVAILABLE() {
        // Arrange
        // arrange invalid persistence model to hold test data
        RecipePortionsUseCasePersistenceModel modelUnderTest = TestDataRecipePortions.
                getNewInvalidServingsTooHighSittingsValid();

        // initialise use case
        simulateDataUnavailable(modelUnderTest);

        RecipePortionsUseCaseRequest request = new RecipePortionsUseCaseRequest.Builder()
                .basedOnResponse(onErrorResponse)
                .setRequestModel(new RecipePortionsUseCaseRequestModel.Builder()
                        .setServings(modelUnderTest.getServings())
                        .setSittings(modelUnderTest.getSittings())
                        .build())
                .build();

        // Act
        SUT.execute(request, new UseCaseCallbackImplementer());

        // Assert response values
        UseCaseResponse<RecipePortionsUseCaseResponseModel> response = onErrorResponse;
        UseCaseMetadataModel metadata = response.getUseCaseMetadataModel();
        RecipePortionsUseCaseResponseModel responseModel = response.getResponseModel();

        ComponentState expectedState = ComponentState.INVALID_CHANGED;
        ComponentState actualState = metadata.getComponentState();
        assertEquals(
                expectedState,
                actualState
        );

        List<FailReasons> expectedFailReasons = Arrays.asList(
                RecipePortionsUseCaseFailReason.SERVINGS_TOO_HIGH,
                CommonFailReason.DATA_UNAVAILABLE);
        List<FailReasons> actualFailReasons = metadata.getFailReasons();
        assertEquals(
                expectedFailReasons,
                actualFailReasons
        );
        assertDomainData(modelUnderTest, responseModel);
    }

    @Test
    public void newRequest_stateINVALID_CHANGED_failReasonsSITTINGS_TOO_LOW_DATA_UNAVAILABLE() {
        // Arrange
        RecipePortionsUseCasePersistenceModel modelUnderTest = TestDataRecipePortions
                .getNewInvalidValidServingsValidSittingsTooLow();

        // initialise use case
        simulateDataUnavailable(modelUnderTest);

        RecipePortionsUseCaseRequest request = new RecipePortionsUseCaseRequest.Builder()
                .basedOnResponse(onErrorResponse)
                .setRequestModel(new RecipePortionsUseCaseRequestModel.Builder()
                        .setServings(modelUnderTest.getServings())
                        .setSittings(modelUnderTest.getSittings())
                        .build())
                .build();

        // Act
        SUT.execute(request, new UseCaseCallbackImplementer());

        // Assert response values
        UseCaseResponse<RecipePortionsUseCaseResponseModel> response = onErrorResponse;
        UseCaseMetadataModel metadata = response.getUseCaseMetadataModel();
        RecipePortionsUseCaseResponseModel responseModel = response.getResponseModel();

        ComponentState expectedState = ComponentState.INVALID_CHANGED;
        ComponentState actualState = metadata.getComponentState();
        assertEquals(
                expectedState,
                actualState
        );

        List<FailReasons> expectedFailReasons = Arrays.asList(
                RecipePortionsUseCaseFailReason.SITTINGS_TOO_LOW,
                CommonFailReason.DATA_UNAVAILABLE);
        List<FailReasons> actualFailReasons = metadata.getFailReasons();
        assertEquals(
                expectedFailReasons,
                actualFailReasons
        );
        assertDomainData(modelUnderTest, responseModel);
    }

    @Test
    public void newRequest_stateINVALID_CHANGED_failReasonsSITTINGS_TOO_HIGH_DATA_UNAVAINABLE() {
        // Arrange
        RecipePortionsUseCasePersistenceModel modelUnderTest = TestDataRecipePortions
                .getNewInvalid_validServingsInvalidSittingsTooHigh();

        // initialise use case
        simulateDataUnavailable(modelUnderTest);

        RecipePortionsUseCaseRequest request = new RecipePortionsUseCaseRequest.Builder()
                .basedOnResponse(onErrorResponse)
                .setRequestModel(new RecipePortionsUseCaseRequestModel.Builder()
                        .setServings(modelUnderTest.getServings())
                        .setSittings(modelUnderTest.getSittings())
                        .build())
                .build();

        // Act
        SUT.execute(request, new UseCaseCallbackImplementer());

        // Assert response values
        UseCaseResponse<RecipePortionsUseCaseResponseModel> response = onErrorResponse;
        UseCaseMetadataModel metadata = response.getUseCaseMetadataModel();
        RecipePortionsUseCaseResponseModel responseModel = response.getResponseModel();

        ComponentState expectedState = ComponentState.INVALID_CHANGED;
        ComponentState actualState = metadata.getComponentState();
        assertEquals(
                expectedState,
                actualState
        );

        List<FailReasons> expectedFailReasons = Arrays.asList(
                RecipePortionsUseCaseFailReason.SITTINGS_TOO_HIGH,
                CommonFailReason.DATA_UNAVAILABLE);
        List<FailReasons> actualFailReasons = metadata.getFailReasons();
        assertEquals(
                expectedFailReasons,
                actualFailReasons
        );
        assertDomainData(modelUnderTest, responseModel);
    }

    @Test
    public void newRequest_stateINVALID_CHANGED_failReasonsINVALID_SITTINGS_INVALID_SERVINGS_DATA_UNAVAILABLE() {
        // Arrange
        RecipePortionsUseCasePersistenceModel modelUnderTest = TestDataRecipePortions
                .getNewInvalidServingsTooHighSittingsTooHigh();

        // initialise use case
        simulateDataUnavailable(modelUnderTest);

        RecipePortionsUseCaseRequest request = new RecipePortionsUseCaseRequest.Builder()
                .basedOnResponse(onErrorResponse)
                .setRequestModel(new RecipePortionsUseCaseRequestModel.Builder()
                        .setServings(modelUnderTest.getServings())
                        .setSittings(modelUnderTest.getSittings())
                        .build())
                .build();

        // Act
        SUT.execute(request, new UseCaseCallbackImplementer());

        // Assert response values
        UseCaseResponse<RecipePortionsUseCaseResponseModel> response = onErrorResponse;
        UseCaseMetadataModel metadata = response.getUseCaseMetadataModel();
        RecipePortionsUseCaseResponseModel responseModel = response.getResponseModel();

        ComponentState expectedState = ComponentState.INVALID_CHANGED;
        ComponentState actualState = metadata.getComponentState();
        assertEquals(
                expectedState,
                actualState
        );

        List<FailReasons> expectedFailReasons = Arrays.asList(
                RecipePortionsUseCaseFailReason.SERVINGS_TOO_HIGH,
                RecipePortionsUseCaseFailReason.SITTINGS_TOO_HIGH,
                CommonFailReason.DATA_UNAVAILABLE);
        List<FailReasons> actualFailReasons = metadata.getFailReasons();
        assertEquals(
                expectedFailReasons,
                actualFailReasons
        );
        assertDomainData(modelUnderTest, responseModel);
    }

    @Test
    public void newRequest_validServingsValidSittings_stateVALID_CHANGED_failReasonsNONE() {
        // Arrange
        RecipePortionsUseCasePersistenceModel modelUnderTest = TestDataRecipePortions
                .getNewValidServingsValidSittings();

        // initialise use case
        simulateDataUnavailable(modelUnderTest);

        RecipePortionsUseCaseRequest request = new RecipePortionsUseCaseRequest.Builder()
                .basedOnResponse(onErrorResponse)
                .setRequestModel(new RecipePortionsUseCaseRequestModel.Builder()
                        .setServings(modelUnderTest.getServings())
                        .setSittings(modelUnderTest.getSittings())
                        .build())
                .build();

        // Act
        SUT.execute(request, new UseCaseCallbackImplementer());

        // Assert response values
        UseCaseResponse<RecipePortionsUseCaseResponseModel> response = onSuccessResponse;
        UseCaseMetadataModel metadata = response.getUseCaseMetadataModel();
        RecipePortionsUseCaseResponseModel responseModel = response.getResponseModel();

        ComponentState expectedState = ComponentState.VALID_CHANGED;
        ComponentState actualState = metadata.getComponentState();
        assertEquals(
                expectedState,
                actualState
        );

        List<FailReasons> expectedFailReasons = Collections.singletonList(CommonFailReason.NONE);
        List<FailReasons> actualFailReasons = metadata.getFailReasons();
        assertEquals(
                expectedFailReasons,
                actualFailReasons
        );
        assertDomainData(modelUnderTest, responseModel);
    }

    @Test
    public void newRequest_validRequestValuesPersisted_invalidRequestValuesNotPersisted_validRequestOldValuesArchivedNewValuesPersisted() {
        // Arrange
        // This test represents a flow of data coming into the use case
        // - start with empty initialised use case
        RecipePortionsUseCasePersistenceModel initialModel =
                new RecipePortionsUseCasePersistenceModel.Builder()
                        .setDataId("dataId-recipePortions-id1")
                        .setDomainId("domainId-recipePortions-id1")
                        .setServings(1)
                        .setSittings(1)
                        .setCreateDate(0L)
                        .setLastUpdate(0L)
                        .build();

        // Act / Assert use case initialised
        simulateDataUnavailable(initialModel);
        assertDomainData(initialModel, onErrorResponse.getResponseModel());

        // Arrange
        // - request with valid domain data, values persisted
        RecipePortionsUseCasePersistenceModel expectedFirstSavedModel =
                new RecipePortionsUseCasePersistenceModel.Builder()
                        .basedOnModel(initialModel)
                        .setDataId("dataId-recipePortions-id2")
                        .setSittings(MAX_SITTINGS)
                        .setServings(MAX_SERVINGS)
                        .setCreateDate(10L)
                        .setLastUpdate(10L)
                        .build();

        RecipePortionsUseCaseRequest validRequest = new RecipePortionsUseCaseRequest.Builder()
                .basedOnResponse(onErrorResponse)
                .setRequestModel(new RecipePortionsUseCaseRequestModel.Builder()
                        .setServings(expectedFirstSavedModel.getServings())
                        .setSittings(expectedFirstSavedModel.getSittings())
                        .build())
                .build();

        // data id and timestamp for save
        when(idProviderMock.getUId()).thenReturn(expectedFirstSavedModel.getDataId());
        when(timeProviderMock.getCurrentTimeInMills()).thenReturn(expectedFirstSavedModel.getCreateDate());

        // Act
        SUT.execute(validRequest, new UseCaseCallbackImplementer());

        // Assert
        verify(dataAccessMock).save(eq(expectedFirstSavedModel));
        assertDomainData(expectedFirstSavedModel, onSuccessResponse.getResponseModel());

        // Arrange
        // - request with invalid data, values not persisted
        RecipePortionsUseCasePersistenceModel invalidModel =
                new RecipePortionsUseCasePersistenceModel.Builder()
                        .basedOnModel(expectedFirstSavedModel)
                        .setSittings(MIN_SITTINGS - 1)
                        .setServings(MIN_SERVINGS - 1)
                        .build();

        RecipePortionsUseCaseRequest invalidRequest = new RecipePortionsUseCaseRequest.Builder()
                .basedOnResponse(onSuccessResponse)
                .setRequestModel(new RecipePortionsUseCaseRequestModel.Builder()
                        .setServings(invalidModel.getServings())
                        .setSittings(invalidModel.getSittings())
                        .build())
                .build();

        // Act
        SUT.execute(invalidRequest, new UseCaseCallbackImplementer());

        // Assert
        verifyNoMoreInteractions(dataAccessMock);
        assertDomainData(invalidModel, onErrorResponse.getResponseModel());

        // Arrange
        // - finally, request with valid data, old model archived, new model saved
        RecipePortionsUseCasePersistenceModel expectedFirstModelArchived =
                new RecipePortionsUseCasePersistenceModel.Builder()
                        .basedOnModel(expectedFirstSavedModel)
                        .setLastUpdate(20L)
                        .build();

        RecipePortionsUseCasePersistenceModel expectedNewSavedModel =
                new RecipePortionsUseCasePersistenceModel.Builder()
                        .basedOnModel(expectedFirstModelArchived)
                        .setDataId("dataId-recipePortions-id1000")
                        .setServings(7)
                        .setSittings(7)
                        .setCreateDate(50L)
                        .setLastUpdate(50L)
                        .build();

        RecipePortionsUseCaseRequest lastValidRequest = new RecipePortionsUseCaseRequest.Builder()
                .basedOnResponse(onErrorResponse)
                .setRequestModel(new RecipePortionsUseCaseRequestModel.Builder()
                        .setServings(expectedNewSavedModel.getServings())
                        .setSittings(expectedNewSavedModel.getSittings())
                        .build())
                .build();

        // arrange new id for saved model
        when(idProviderMock.getUId()).thenReturn(expectedNewSavedModel.getDataId());
        // arrange two timestamps, one for archived model and one for saved model
        when(timeProviderMock.getCurrentTimeInMills()).thenReturn(
                expectedFirstModelArchived.getLastUpdate(),
                expectedNewSavedModel.getCreateDate()
        );

        // Act
        SUT.execute(lastValidRequest, new UseCaseCallbackImplementer());

        // Assert
        // assert old persistence model archived and new model saved
        List<RecipePortionsUseCasePersistenceModel> expectedSavedModels = Arrays.asList(
                expectedFirstSavedModel,
                expectedFirstModelArchived,
                expectedNewSavedModel
        );

        ArgumentCaptor<RecipePortionsUseCasePersistenceModel> ac =
                ArgumentCaptor.forClass(RecipePortionsUseCasePersistenceModel.class);
        verify(dataAccessMock, times(expectedSavedModels.size())).save(ac.capture());

        List<RecipePortionsUseCasePersistenceModel> actualSavedModels = ac.getAllValues();

        assertEquals(
                expectedSavedModels,
                actualSavedModels
        );

        // Assert response values
        UseCaseResponse<RecipePortionsUseCaseResponseModel> response = onSuccessResponse;
        UseCaseMetadataModel metadata = response.getUseCaseMetadataModel();
        RecipePortionsUseCaseResponseModel responseModel = response.getResponseModel();

        ComponentState expectedState = ComponentState.VALID_CHANGED;
        ComponentState actualState = metadata.getComponentState();
        assertEquals(
                expectedState,
                actualState
        );

        List<FailReasons> expectedFailReasons = Collections.singletonList(CommonFailReason.NONE);
        List<FailReasons> actualFailReasons = metadata.getFailReasons();
        assertEquals(
                expectedFailReasons,
                actualFailReasons
        );
        assertDomainData(expectedNewSavedModel, responseModel);
    }

    @Test
    public void existingRequest_invalidValuesLoaded_stateINVALID_UNCHANGED_failReasonsINVALID_SERVINGS_INVALID_SITTINGS() {
        // Arrange
        RecipePortionsUseCasePersistenceModel modelUnderTest = TestDataRecipePortions
                .getNewInvalidServingsTooHighSittingsTooHigh();

        // Act / Assert
        // assert persistence called and return data
        simulateLoadPersistenceModel(modelUnderTest);

        // Assert response values
        UseCaseResponse<RecipePortionsUseCaseResponseModel> response = onErrorResponse;
        UseCaseMetadataModel metadata = response.getUseCaseMetadataModel();
        RecipePortionsUseCaseResponseModel responseModel = response.getResponseModel();

        ComponentState expectedState = ComponentState.INVALID_UNCHANGED;
        ComponentState actualState = metadata.getComponentState();
        assertEquals(
                expectedState,
                actualState
        );

        List<FailReasons> expectedFailReasons = Arrays.asList(
                RecipePortionsUseCaseFailReason.SERVINGS_TOO_HIGH,
                RecipePortionsUseCaseFailReason.SITTINGS_TOO_HIGH
        );
        List<FailReasons> actualFailReasons = metadata.getFailReasons();
        assertEquals(
                expectedFailReasons,
                actualFailReasons
        );
        assertDomainData(modelUnderTest, responseModel);
    }

    @Test
    public void existingRequest_validValuesLoaded_stateVALID_UNCHANGED_failReasonsNONE() {
        // Arrange
        // arrange valid persistence model
        RecipePortionsUseCasePersistenceModel modelUnderTest = TestDataRecipePortions
                .getNewValidServingsValidSittings();

        // Act / Assert data loaded
        simulateLoadPersistenceModel(modelUnderTest);

        // Assert response values
        UseCaseResponse<RecipePortionsUseCaseResponseModel> response = onSuccessResponse;
        UseCaseMetadataModel metadata = response.getUseCaseMetadataModel();
        RecipePortionsUseCaseResponseModel responseModel = response.getResponseModel();

        ComponentState expectedState = ComponentState.VALID_UNCHANGED;
        ComponentState actualState = metadata.getComponentState();
        assertEquals(
                expectedState,
                actualState
        );

        List<FailReasons> expectedFailReasons = Collections.singletonList(CommonFailReason.NONE);
        List<FailReasons> actualFailReasons = metadata.getFailReasons();
        assertEquals(
                expectedFailReasons,
                actualFailReasons
        );
        assertDomainData(modelUnderTest, responseModel);
    }

    // region helper methods -----------------------------------------------------------------------
    private void simulateDataUnavailable(RecipePortionsUseCasePersistenceModel modelUnderTest) {
        // Arrange / act, initialise use case
        SUT.execute(
                new RecipePortionsUseCaseRequest.Builder()
                        .getDefault()
                        .setDomainId(modelUnderTest.getDomainId())
                        .build(),
                new UseCaseCallbackImplementer()
        );

        // Assert
        verify(dataAccessMock).getByDomainId(eq(modelUnderTest.getDomainId()),
                dataAccessCallback.capture()
        );
        dataAccessCallback.getValue().onPersistenceModelUnavailable();
    }

    private void simulateLoadPersistenceModel(RecipePortionsUseCasePersistenceModel modelUnderTest) {
        // Arrange / Act, load persistence model
        SUT.execute(
                new RecipePortionsUseCaseRequest.Builder()
                        .getDefault()
                        .setDomainId(modelUnderTest.getDomainId())
                        .build(),
                new UseCaseCallbackImplementer()
        );

        // Assert
        verify(dataAccessMock).getByDomainId(eq(modelUnderTest.getDomainId()),
                dataAccessCallback.capture()
        );
        dataAccessCallback.getValue().onPersistenceModelLoaded(modelUnderTest);
    }

    private void assertDomainData(RecipePortionsUseCasePersistenceModel modelUnderTest, RecipePortionsUseCaseResponseModel responseModel) {
        int expectedSittings = modelUnderTest.getSittings();
        int actualSittings = responseModel.getSittings();
        assertEquals(
                expectedSittings,
                actualSittings
        );
        int expectedServings = modelUnderTest.getServings();
        int actualServings = responseModel.getServings();
        assertEquals(
                expectedServings,
                actualServings
        );
        int expectedPortions = modelUnderTest.getServings() * modelUnderTest.getSittings();
        int actualPortions = responseModel.getPortions();
        assertEquals(
                expectedPortions,
                actualPortions
        );
    }
    // endregion helper methods --------------------------------------------------------------------

    // region helper classes -----------------------------------------------------------------------
    private final class UseCaseCallbackImplementer
            implements
            UseCaseCallback<UseCaseResponse<RecipePortionsUseCaseResponseModel>> {

        @Override
        public void onSuccess(UseCaseResponse<RecipePortionsUseCaseResponseModel> response) {
            onSuccessResponse = response;
        }

        @Override
        public void onError(UseCaseResponse<RecipePortionsUseCaseResponseModel> response) {
            onErrorResponse = response;
        }
    }
    // endregion helper classes --------------------------------------------------------------------
}