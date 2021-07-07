package com.example.peter.thekitchenmenu.domain.usecasenew.recipe.component.duration;

import com.example.peter.thekitchenmenu.data.repository.DomainDataAccess.GetDomainModelCallback;
import com.example.peter.thekitchenmenu.data.repository.recipe.RecipeDurationUseCaseDataAccess;
import com.example.peter.thekitchenmenu.data.repository.recipe.duration.TestDataRecipeDuration;
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

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

public class RecipeDurationUseCaseTest {

    // region constants ----------------------------------------------------------------------------
    public static int MAX_PREP_TIME = TestDataRecipeDuration.MAX_PREP_TIME;
    public static int MAX_COOK_TIME = TestDataRecipeDuration.MAX_COOK_TIME;
    // endregion constants -------------------------------------------------------------------------

    // region helper fields ------------------------------------------------------------------------
    @Mock
    RecipeDurationUseCaseDataAccess dataAccessMock;
    @Captor
    ArgumentCaptor<GetDomainModelCallback<RecipeDurationUseCasePersistenceModel>> dataAccessCallback;
    @Mock
    UniqueIdProvider idProviderMock;
    @Mock
    TimeProvider timeProviderMock;

    private UseCaseResponse<RecipeDurationUseCaseResponseModel> onSuccessResponse;
    private UseCaseResponse<RecipeDurationUseCaseResponseModel> onErrorResponse;
    // endregion helper fields ---------------------------------------------------------------------

    private RecipeDurationUseCase SUT;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        SUT = givenUseCase();
    }

    private RecipeDurationUseCase givenUseCase() {
        RecipeDurationDomainModelConverter converter = new RecipeDurationDomainModelConverter(
                timeProviderMock, idProviderMock
        );
        return new RecipeDurationUseCase(dataAccessMock, converter, MAX_PREP_TIME, MAX_COOK_TIME);
    }

    @Test
    public void emptyRequest_stateVALID_DEFAULT_failReasonsDATA_UNAVAILABLE() {
        // Arrange
        RecipeDurationUseCaseRequest request = new RecipeDurationUseCaseRequest.Builder()
                .getDefault()
                .build();

        // Act
        SUT.execute(request, new UseCaseCallbackImplementer());

        // Assert
        // assert response values
        UseCaseResponse<RecipeDurationUseCaseResponseModel> response = onErrorResponse;
        UseCaseMetadataModel metadata = response.getUseCaseMetadataModel();
        RecipeDurationUseCaseResponseModel responseModel = response.getResponseModel();

        ComponentState expectedState = ComponentState.VALID_DEFAULT;
        ComponentState actualState = metadata.getComponentState();
        assertEquals(
                expectedState,
                actualState
        );

        List<FailReasons> expectedFailReasons = Collections.singletonList(
                CommonFailReason.DATA_UNAVAILABLE
        );
        List<FailReasons> actualFailReasons = metadata.getFailReasons();
        assertEquals(
                expectedFailReasons,
                actualFailReasons
        );

        RecipeDurationUseCaseResponseModel expectedModel = new RecipeDurationUseCaseResponseModel.Builder()
                .getDefault()
                .build();
        assertEquals(
                expectedModel,
                responseModel
        );
    }

    @Test
    public void newRequest_stateVALID_DEFAULT_failReasonsDATA_UNAVAILABLE() {
        // Arrange
        // this persistence model should never exist in production as default values are never
        // saved, however it exists here as it is a useful way of storing default state for testing.
        RecipeDurationUseCasePersistenceModel modelUnderTest = TestDataRecipeDuration.
                getNewActiveDefault();

        // Act / Assert
        // execute and assert persistence calls
        simulateDataUnavailable(modelUnderTest);

        // assert nothing saved
        verifyNoMoreInteractions(dataAccessMock);

        // assert response values
        UseCaseResponse<RecipeDurationUseCaseResponseModel> response = onErrorResponse;
        UseCaseMetadataModel metadata = response.getUseCaseMetadataModel();
        RecipeDurationUseCaseResponseModel responseModel = response.getResponseModel();

        ComponentState expectedState = ComponentState.VALID_DEFAULT;
        ComponentState actualState = metadata.getComponentState();
        assertEquals(
                expectedState,
                actualState
        );

        List<FailReasons> expectedFailReasons = Collections.singletonList(
                CommonFailReason.DATA_UNAVAILABLE
        );
        List<FailReasons> actualFailReasons = metadata.getFailReasons();
        assertEquals(
                expectedFailReasons,
                actualFailReasons
        );

        assertDomainData(modelUnderTest, responseModel);
    }

    @Test
    public void newRequest_invalidPrepHours_stateINVALID_CHANGED_failReasonsINVALID_PREP_TIME_DATA_UNAVAILABLE() {
        // Arrange
        // this persistence model would never exist in production as invalid data is not persisted,
        // however it exists here as it is a useful way of storing values for testing.
        RecipeDurationUseCasePersistenceModel modelUnderTest = TestDataRecipeDuration.
                getNewInvalidPrepHours();

        // arrange, initialise use case
        simulateDataUnavailable(modelUnderTest);

        // arrange invalid prep hours request
        RecipeDurationUseCaseRequestModel requestModel = new RecipeDurationUseCaseRequestModel.Builder()
                .basedOnResponseModel(onErrorResponse.getResponseModel())
                .setPrepHours(RecipeDurationTimeHelper.getHours(
                        modelUnderTest.getPrepTime()))
                .build();
        RecipeDurationUseCaseRequest request = new RecipeDurationUseCaseRequest.Builder()
                .basedOnResponse(onErrorResponse)
                .setRequestModel(requestModel)
                .build();

        // Act
        SUT.execute(request, new UseCaseCallbackImplementer());

        // Assert
        // assert invalid values not saved
        verifyNoMoreInteractions(dataAccessMock);

        // assert response values
        UseCaseResponse<RecipeDurationUseCaseResponseModel> response = onErrorResponse;
        UseCaseMetadataModel metadata = response.getUseCaseMetadataModel();
        RecipeDurationUseCaseResponseModel responseModel = response.getResponseModel();

        ComponentState expectedState = ComponentState.INVALID_CHANGED;
        ComponentState actualState = metadata.getComponentState();
        assertEquals(
                expectedState,
                actualState
        );

        List<FailReasons> expectedFailReasons = Arrays.asList(
                RecipeDurationUseCaseFailReason.INVALID_PREP_TIME,
                CommonFailReason.DATA_UNAVAILABLE
        );
        List<FailReasons> actualFailReasons = metadata.getFailReasons();
        assertEquals(
                expectedFailReasons,
                actualFailReasons
        );

        assertDomainData(modelUnderTest, responseModel);
    }

    @Test
    public void newRequest_validPrepHours_stateVALID_CHANGED_failReasonsNONE() {
        // Arrange
        // arrange expected save for valid data
        RecipeDurationUseCasePersistenceModel modelUnderTest = TestDataRecipeDuration.
                getNewValidPrepTime();

        // save will require timestamp and data id
        when(timeProviderMock.getCurrentTimeInMills()).thenReturn(modelUnderTest.getLastUpdate());
        when(idProviderMock.getUId()).thenReturn(modelUnderTest.getDataId());

        // arrange, initialise use case
        simulateDataUnavailable(modelUnderTest);

        RecipeDurationUseCaseRequest request = new RecipeDurationUseCaseRequest.Builder()
                .basedOnResponse(onErrorResponse)
                .setRequestModel(new RecipeDurationUseCaseRequestModel.Builder()
                        .basedOnResponseModel(onErrorResponse.getResponseModel())
                        .setPrepHours(RecipeDurationTimeHelper.getHours(modelUnderTest.getPrepTime()))
                        .build())
                .build();

        // Act
        SUT.execute(request, new UseCaseCallbackImplementer());

        // Assert
        assertValidResponseWithSave(modelUnderTest);
    }

    @Test
    public void newRequest_invalidPrepMinutes_stateINVALID_CHANGED_failReasonsINVALID_PREP_TIME() {
        // Arrange
        // arrange persistent model that SHOULD NOT be saved as represents error state
        RecipeDurationUseCasePersistenceModel modelUnderTest = TestDataRecipeDuration.
                getNewInvalidPrepMinutes();

        // arrange, initialise use case
        simulateDataUnavailable(modelUnderTest);

        RecipeDurationUseCaseRequest request = new RecipeDurationUseCaseRequest.Builder()
                .basedOnResponse(onErrorResponse)
                .setRequestModel(new RecipeDurationUseCaseRequestModel.Builder()
                        .setPrepMinutes(modelUnderTest.getPrepTime())
                        .build())
                .build();

        // Act
        SUT.execute(request, new UseCaseCallbackImplementer());

        // Assert
        // assert invalid data not saved
        verifyNoMoreInteractions(dataAccessMock);

        // assert response values
        UseCaseResponse<RecipeDurationUseCaseResponseModel> response = onErrorResponse;
        UseCaseMetadataModel metadata = response.getUseCaseMetadataModel();
        RecipeDurationUseCaseResponseModel responseModel = response.getResponseModel();

        ComponentState expectedState = ComponentState.INVALID_CHANGED;
        ComponentState actualState = metadata.getComponentState();
        assertEquals(
                expectedState,
                actualState
        );

        List<FailReasons> expectedFailReasons = Arrays.asList(
                RecipeDurationUseCaseFailReason.INVALID_PREP_TIME,
                CommonFailReason.DATA_UNAVAILABLE);
        List<FailReasons> actualFailReasons = metadata.getFailReasons();
        assertEquals(
                expectedFailReasons,
                actualFailReasons
        );

        assertDomainData(modelUnderTest, responseModel);
    }

    @Test
    public void newRequest_validPrepMinutes_stateVALID_CHANGED_failReasonsNONE() {
        // Arrange
        // Arrange persistence model that should be saved
        RecipeDurationUseCasePersistenceModel modelUnderTest = TestDataRecipeDuration.
                getNewValidPrepTime();

        // save will require timestamp and data id
        when(timeProviderMock.getCurrentTimeInMills()).thenReturn(modelUnderTest.getLastUpdate());
        when(idProviderMock.getUId()).thenReturn(modelUnderTest.getDataId());

        // arrange set up use case
        simulateDataUnavailable(modelUnderTest);

        // arrange change data request
        RecipeDurationUseCaseRequest request = new RecipeDurationUseCaseRequest.Builder()
                .basedOnResponse(onErrorResponse)
                .setRequestModel(new RecipeDurationUseCaseRequestModel.Builder()
                        .basedOnResponseModel(onErrorResponse.getResponseModel())
                        .setPrepMinutes(modelUnderTest.getPrepTime())
                        .build())
                .build();

        // Act
        SUT.execute(request, new UseCaseCallbackImplementer());

        // Assert
        assertValidResponseWithSave(modelUnderTest);
    }

    @Test
    public void newRequest_invalidCookHours_stateINVALID_CHANGED_failReasons_INVALID_COOK_HOURS_DATA_UNAVAILABLE() {
        // Arrange
        // arrange persistence model that SHOULD NOT BE SAVED
        RecipeDurationUseCasePersistenceModel modelUnderTest = TestDataRecipeDuration.
                getNewInvalidCookHours();

        // arrange initialise use case
        simulateDataUnavailable(modelUnderTest);

        RecipeDurationUseCaseRequest request = new RecipeDurationUseCaseRequest.Builder()
                .basedOnResponse(onErrorResponse)
                .setRequestModel(new RecipeDurationUseCaseRequestModel.Builder()
                        .setCookHours(RecipeDurationTimeHelper.getHours(modelUnderTest.getCookTime()))
                        .build())
                .build();

        // Act
        SUT.execute(request, new UseCaseCallbackImplementer());

        // Assert
        // verify invalid data not saved
        verifyNoMoreInteractions(dataAccessMock);

        // assert response values
        UseCaseResponse<RecipeDurationUseCaseResponseModel> response = onErrorResponse;
        UseCaseMetadataModel metadata = response.getUseCaseMetadataModel();
        RecipeDurationUseCaseResponseModel responseModel = response.getResponseModel();

        ComponentState expectedState = ComponentState.INVALID_CHANGED;
        ComponentState actualState = metadata.getComponentState();
        assertEquals(
                expectedState,
                actualState
        );

        List<FailReasons> expectedFailReasons = Arrays.asList(
                RecipeDurationUseCaseFailReason.INVALID_COOK_TIME,
                CommonFailReason.DATA_UNAVAILABLE);
        List<FailReasons> actualFailReasons = metadata.getFailReasons();
        assertEquals(
                expectedFailReasons,
                actualFailReasons
        );

        assertDomainData(modelUnderTest, responseModel);
    }

    @Test
    public void newRequest_validCookHours_stateVALID_CHANGED_failReasonsNONE() {
        // Arrange
        // Arrange persistence model that should be saved
        RecipeDurationUseCasePersistenceModel modelUnderTest = TestDataRecipeDuration.
                getNewValidCookTime();

        // save will require timestamp and data id
        when(timeProviderMock.getCurrentTimeInMills()).thenReturn(modelUnderTest.getLastUpdate());
        when(idProviderMock.getUId()).thenReturn(modelUnderTest.getDataId());

        // arrange set up use case
        simulateDataUnavailable(modelUnderTest);

        // arrange change data request
        RecipeDurationUseCaseRequest request = new RecipeDurationUseCaseRequest.Builder()
                .basedOnResponse(onErrorResponse)
                .setRequestModel(new RecipeDurationUseCaseRequestModel.Builder()
                        .basedOnResponseModel(onErrorResponse.getResponseModel())
                        .setCookHours(RecipeDurationTimeHelper.getHours(modelUnderTest.getCookTime()))
                        .build())
                .build();

        // Act
        SUT.execute(request, new UseCaseCallbackImplementer());

        // Assert
        assertValidResponseWithSave(modelUnderTest);
    }

    @Test
    public void newRequest_invalidCookMinutes_stateINVALID_CHANGED_failReasonsINVALID_COOK_TIME_DATA_UNAVAILABLE() {
        // Arrange
        // arrange persistence model that SHOULD NOT be saved
        RecipeDurationUseCasePersistenceModel modelUnderTest = TestDataRecipeDuration.
                getInvalidNewCookMinutes();

        // arrange initialise use case
        simulateDataUnavailable(modelUnderTest);

        RecipeDurationUseCaseRequest request = new RecipeDurationUseCaseRequest.Builder()
                .basedOnResponse(onErrorResponse)
                .setRequestModel(new RecipeDurationUseCaseRequestModel.Builder()
                        .setCookMinutes(modelUnderTest.getCookTime())
                        .build())
                .build();

        // Act
        SUT.execute(request, new UseCaseCallbackImplementer());

        // Assert
        // verify invalid data not saved
        verifyNoMoreInteractions(dataAccessMock);

        // assert response values
        UseCaseResponse<RecipeDurationUseCaseResponseModel> response = onErrorResponse;
        UseCaseMetadataModel metadata = response.getUseCaseMetadataModel();
        RecipeDurationUseCaseResponseModel responseModel = response.getResponseModel();

        ComponentState expectedState = ComponentState.INVALID_CHANGED;
        ComponentState actualState = metadata.getComponentState();
        assertEquals(
                expectedState,
                actualState
        );

        List<FailReasons> expectedFailReasons = Arrays.asList(
                RecipeDurationUseCaseFailReason.INVALID_COOK_TIME,
                CommonFailReason.DATA_UNAVAILABLE);
        List<FailReasons> actualFailReasons = metadata.getFailReasons();
        assertEquals(
                expectedFailReasons,
                actualFailReasons
        );

        assertDomainData(modelUnderTest, responseModel);
    }

    @Test
    public void newRequest_validCookMinutes_stateVALID_CHANGED_failReasonsNONE() {
        // Arrange
        // arrange valid persistence model
        RecipeDurationUseCasePersistenceModel modelUnderTest = TestDataRecipeDuration.
                getNewValidCookTime();

        when(idProviderMock.getUId()).thenReturn(modelUnderTest.getDataId());
        when(timeProviderMock.getCurrentTimeInMills()).thenReturn(modelUnderTest.getCreateDate());

        // arrange set up use case
        simulateDataUnavailable(modelUnderTest);

        // arrange change data request
        RecipeDurationUseCaseRequest request = new RecipeDurationUseCaseRequest.Builder()
                .basedOnResponse(onErrorResponse)
                .setRequestModel(new RecipeDurationUseCaseRequestModel.Builder()
                        .basedOnResponseModel(onErrorResponse.getResponseModel())
                        .setCookMinutes(modelUnderTest.getCookTime())
                        .build())
                .build();

        // Act
        SUT.execute(request, new UseCaseCallbackImplementer());

        // Assert
        assertValidResponseWithSave(modelUnderTest);
    }

    @Test
    public void newRequest_invalidPrepTimeInvalidCookTime_stateINVALID_CHANGED_failReasonsINVALID_PREP_TIME_INVALID_COOK_TIME() {
        // Arrange
        // arrange invalid persistence model
        RecipeDurationUseCasePersistenceModel modelUnderTest = TestDataRecipeDuration.
                getNewInvalidPrepTimeInvalidCookTime();

        // arrange initialise use case
        simulateDataUnavailable(modelUnderTest);

        RecipeDurationUseCaseRequest request = new RecipeDurationUseCaseRequest.Builder()
                .basedOnResponse(onErrorResponse)
                .setRequestModel(new RecipeDurationUseCaseRequestModel.Builder()
                        .setPrepHours(RecipeDurationTimeHelper.getHours(modelUnderTest.getPrepTime()))
                        .setPrepMinutes(RecipeDurationTimeHelper.getMinutes(modelUnderTest.getPrepTime()))
                        .setCookHours(RecipeDurationTimeHelper.getHours(modelUnderTest.getCookTime()))
                        .setCookMinutes(RecipeDurationTimeHelper.getMinutes(modelUnderTest.getCookTime()))
                        .build())
                .build();

        // Act
        SUT.execute(request, new UseCaseCallbackImplementer());

        // Assert
        // verify invalid data not saved
        verifyNoMoreInteractions(dataAccessMock);

        // assert response values
        UseCaseResponse<RecipeDurationUseCaseResponseModel> response = onErrorResponse;
        UseCaseMetadataModel metadata = response.getUseCaseMetadataModel();
        RecipeDurationUseCaseResponseModel responseModel = response.getResponseModel();

        ComponentState expectedState = ComponentState.INVALID_CHANGED;
        ComponentState actualState = metadata.getComponentState();
        assertEquals(
                expectedState,
                actualState
        );

        List<FailReasons> expectedFailReasons = Arrays.asList(
                RecipeDurationUseCaseFailReason.INVALID_PREP_TIME,
                RecipeDurationUseCaseFailReason.INVALID_COOK_TIME,
                CommonFailReason.DATA_UNAVAILABLE);
        List<FailReasons> actualFailReasons = metadata.getFailReasons();
        assertEquals(
                expectedFailReasons,
                actualFailReasons
        );

        assertDomainData(modelUnderTest, responseModel);
    }

    @Test
    public void newRequest_validPrepTimeValidCookTime_stateVALID_CHANGED_failReasonsNONE() {
        // Arrange
        // arrange valid persistence model
        RecipeDurationUseCasePersistenceModel modelUnderTest = TestDataRecipeDuration.
                getNewValidPrepTimeValidCookTime();

        // arrange timestamp and data id for save
        when(timeProviderMock.getCurrentTimeInMills()).thenReturn(modelUnderTest.getCreateDate());
        when(idProviderMock.getUId()).thenReturn(modelUnderTest.getDataId());

        simulateDataUnavailable(modelUnderTest);

        RecipeDurationUseCaseRequest request = new RecipeDurationUseCaseRequest.Builder()
                .basedOnResponse(onErrorResponse)
                .setRequestModel(new RecipeDurationUseCaseRequestModel.Builder()
                        .setPrepHours(RecipeDurationTimeHelper.getHours(modelUnderTest.getPrepTime()))
                        .setPrepMinutes(RecipeDurationTimeHelper.getMinutes(modelUnderTest.getPrepTime()))
                        .setCookHours(RecipeDurationTimeHelper.getHours(modelUnderTest.getCookTime()))
                        .setCookMinutes(RecipeDurationTimeHelper.getMinutes(modelUnderTest.getCookTime()))
                        .build())
                .build();

        // Act
        SUT.execute(request, new UseCaseCallbackImplementer());

        // Assert
        assertValidResponseWithSave(modelUnderTest);
    }

    @Test
    public void existingRequest_validPersistenceModelLoaded_stateVALID_UNCHANGED_failReasonsNONE() {
        // Arrange
        // Arrange
        RecipeDurationUseCasePersistenceModel modelUnderTest = TestDataRecipeDuration.
                getExistingValidPrepTimeValidCookTime();

        // Act
        simulateLoadPersistenceModel(modelUnderTest);

        // Assert
        // assert response values
        UseCaseResponse<RecipeDurationUseCaseResponseModel> response = onSuccessResponse;
        UseCaseMetadataModel metadata = response.getUseCaseMetadataModel();
        RecipeDurationUseCaseResponseModel responseModel = response.getResponseModel();

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

    @Test
    public void existingRequest_invalidPersistenceModelLoaded_stateINVALID_UNCHANGED_failReasonINVALID_PREP_TIME_INVALID_COOK_TIME() {
        // Arrange
        // Arrange
        RecipeDurationUseCasePersistenceModel modelUnderTest = TestDataRecipeDuration.
                getExistingInvalidPrepAndCookTime();

        // Act
        simulateLoadPersistenceModel(modelUnderTest);

        // Assert
        // assert response values
        UseCaseResponse<RecipeDurationUseCaseResponseModel> response = onErrorResponse;
        UseCaseMetadataModel metadata = response.getUseCaseMetadataModel();
        RecipeDurationUseCaseResponseModel responseModel = response.getResponseModel();

        ComponentState expectedState = ComponentState.INVALID_UNCHANGED;
        ComponentState actualState = metadata.getComponentState();
        assertEquals(
                expectedState,
                actualState
        );

        List<FailReasons> expectedFailReasons = Arrays.asList(
                RecipeDurationUseCaseFailReason.INVALID_PREP_TIME,
                RecipeDurationUseCaseFailReason.INVALID_COOK_TIME
        );
        List<FailReasons> actualFailReasons = metadata.getFailReasons();
        assertEquals(
                expectedFailReasons,
                actualFailReasons
        );

        assertDomainData(modelUnderTest, responseModel);
    }

    // region helper methods -----------------------------------------------------------------------
    private void simulateDataUnavailable(RecipeDurationUseCasePersistenceModel modelUnderTest) {
        // arrange, initialise use case
        SUT.execute(
                new RecipeDurationUseCaseRequest.Builder()
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

    private void simulateLoadPersistenceModel(RecipeDurationUseCasePersistenceModel modelUnderTest) {
        // Arrange
        RecipeDurationUseCaseRequest request = new RecipeDurationUseCaseRequest.Builder()
                .getDefault()
                .setDomainId(modelUnderTest.getDomainId())
                .build();

        // Act
        SUT.execute(request, new UseCaseCallbackImplementer());

        // Assert
        verify(dataAccessMock).getByDomainId(eq(modelUnderTest.getDomainId()),
                dataAccessCallback.capture()
        );
        dataAccessCallback.getValue().onPersistenceModelLoaded(modelUnderTest);
    }

    private void assertValidResponseWithSave(RecipeDurationUseCasePersistenceModel modelUnderTest) {
        // verify save of valid values
        verify(dataAccessMock).save(modelUnderTest);

        // assert response values
        UseCaseResponse<RecipeDurationUseCaseResponseModel> response = onSuccessResponse;
        UseCaseMetadataModel metadata = response.getUseCaseMetadataModel();
        RecipeDurationUseCaseResponseModel responseModel = response.getResponseModel();

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

    private void assertDomainData(RecipeDurationUseCasePersistenceModel modelUnderTest,
                                  RecipeDurationUseCaseResponseModel responseModel) {
        int expectedPrepHours = RecipeDurationTimeHelper.getHours(modelUnderTest.getPrepTime());
        int actualPrepHours = responseModel.getPrepHours();
        assertEquals(
                expectedPrepHours,
                actualPrepHours
        );
        int expectedPrepMinutes = RecipeDurationTimeHelper.getMinutes(modelUnderTest.getPrepTime());
        int actualPrepMinutes = responseModel.getPrepMinutes();
        assertEquals(
                expectedPrepMinutes,
                actualPrepMinutes
        );
        int expectedCookHours = RecipeDurationTimeHelper.getHours(modelUnderTest.getCookTime());
        int actualCookHours = responseModel.getCookHours();
        assertEquals(
                expectedCookHours,
                actualCookHours
        );
        int expectedCookMinutes = RecipeDurationTimeHelper.getMinutes(modelUnderTest.getCookTime());
        int actualCookMinutes = responseModel.getCookMinutes();
        assertEquals(
                expectedCookMinutes,
                actualCookMinutes
        );
        int expectedTotalTime = modelUnderTest.getPrepTime() + modelUnderTest.getCookTime();
        int actualTotalTime = responseModel.getTotalTime();
        assertEquals(
                expectedTotalTime,
                actualTotalTime
        );
    }
    // endregion helper methods --------------------------------------------------------------------

    // region helper classes -----------------------------------------------------------------------
    private final class UseCaseCallbackImplementer
            implements UseCaseCallback<UseCaseResponse<RecipeDurationUseCaseResponseModel>> {

        @Override
        public void onSuccess(UseCaseResponse<RecipeDurationUseCaseResponseModel> response) {
            onSuccessResponse = response;
        }

        @Override
        public void onError(UseCaseResponse<RecipeDurationUseCaseResponseModel> response) {
            onErrorResponse = response;
        }
    }
    // endregion helper classes --------------------------------------------------------------------
}