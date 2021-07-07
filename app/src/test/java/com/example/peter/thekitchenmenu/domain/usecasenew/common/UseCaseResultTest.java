package com.example.peter.thekitchenmenu.domain.usecasenew.common;

import com.example.peter.thekitchenmenu.app.Constants;
import com.example.peter.thekitchenmenu.commonmocks.StringMaker;
import com.example.peter.thekitchenmenu.data.repository.DomainDataAccess.GetDomainModelCallback;
import com.example.peter.thekitchenmenu.domain.usecasenew.common.failreasons.CommonFailReason;
import com.example.peter.thekitchenmenu.domain.usecasenew.common.failreasons.FailReasons;
import com.example.peter.thekitchenmenu.domain.usecasenew.common.helperclasses.TestDomainModelConverter;
import com.example.peter.thekitchenmenu.domain.usecasenew.common.helperclasses.TestRequest;
import com.example.peter.thekitchenmenu.domain.usecasenew.common.helperclasses.TestRequestModel;
import com.example.peter.thekitchenmenu.domain.usecasenew.common.helperclasses.TestResponseModel;
import com.example.peter.thekitchenmenu.domain.usecasenew.common.helperclasses.TestUseCase;
import com.example.peter.thekitchenmenu.domain.usecasenew.common.helperclasses.TestUseCaseDataAccess;
import com.example.peter.thekitchenmenu.domain.usecasenew.common.helperclasses.TestUseCaseFailReasons;
import com.example.peter.thekitchenmenu.domain.usecasenew.common.helperclasses.TestUseCasePersistenceModel;
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
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.verify;

public class UseCaseResultTest {

    // region constants ----------------------------------------------------------------------------
    // endregion constants -------------------------------------------------------------------------

    // region helper fields ------------------------------------------------------------------------
    @Mock
    TestUseCaseDataAccess dataAccessMock;
    @Captor
    ArgumentCaptor<GetDomainModelCallback<TestUseCasePersistenceModel>> dataAccessCallbackCaptor;
    @Mock
    UniqueIdProvider idProviderMock;
    @Mock
    TimeProvider timeProviderMock;

    private UseCaseResponse<TestResponseModel> onSuccessResponse;
    private UseCaseResponse<TestResponseModel> onErrorResponse;
    // endregion helper fields ---------------------------------------------------------------------

    private TestUseCase SUT;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        SUT = givenUseCase();

    }

    private TestUseCase givenUseCase() {
        TestDomainModelConverter converter = new TestDomainModelConverter(
                timeProviderMock, idProviderMock
        );
        return new TestUseCase(dataAccessMock, converter);
    }

    @Test
    public void newRequest_stateINVALID_DEFAULT_failReasonDATA_UNAVAILABLE() {
        // Arrange
        // an invalid default state occurs when the default values in the use case are invalid, and
        // the request has no data id's, and a persistence model has not loaded
        TestRequest request = new TestRequest.Builder()
                .getDefault()
                .build();
        // set the default use case domain model value to be invalid
        SUT.useCaseModelDefaultValue = new StringMaker()
                .makeStringOfLength(TestUseCase.MIN_STRING_LENGTH)
                .thenRemoveOneCharacter()
                .build();

        // Act
        SUT.execute(request, new UseCaseCallbackImplementer());

        // Assert
        UseCaseResponse<TestResponseModel> response = onErrorResponse;
        UseCaseMetadataModel metadata = response.getUseCaseMetadataModel();
        // assert fail reasons
        List<FailReasons> expectedFailReasons = Arrays.asList(
                TestUseCaseFailReasons.TEXT_TOO_SHORT,
                CommonFailReason.DATA_UNAVAILABLE);
        List<FailReasons> actualFailReasons = metadata.getFailReasons();
        assertEquals(
                expectedFailReasons,
                actualFailReasons
        );
        // assert state
        ComponentState expectedState = ComponentState.INVALID_DEFAULT;
        ComponentState actualState = metadata.getComponentState();
        assertEquals(
                expectedState,
                actualState
        );
        // assert dates. Dates are only generated when a valid persistence model is saved. In this
        // case a save has not taken place, therefore dates will be 0L
        assertEquals(0L, metadata.getCreateDate());
        assertEquals(0L, metadata.getLastUpdate());
        // assert creator, this will always be the current user
        assertEquals(Constants.getUserId(), metadata.getCreatedBy());
    }

    @Test
    public void newRequest_stateVALID_DEFAULT_failReasonDATA_UNAVAILABLE() {
        // Arrange
        // a valid default state occurs when the default values in the use case are valid, and
        // and the request has no data id's, and a persistence model has not loaded
        TestRequest request = new TestRequest.Builder()
                .getDefault()
                .build();
        // the default state of the use case domain model is a valid state, so no need to set it

        // Act
        SUT.execute(request, new UseCaseCallbackImplementer());

        // Assert
        UseCaseResponse<TestResponseModel> response = onErrorResponse;
        UseCaseMetadataModel metadata = response.getUseCaseMetadataModel();
        // assert fail reasons
        List<FailReasons> expectedFailReasons = Collections.singletonList(
                CommonFailReason.DATA_UNAVAILABLE);
        List<FailReasons> actualFailReasons = metadata.getFailReasons();
        assertEquals(
                expectedFailReasons,
                actualFailReasons
        );
        // assert state
        ComponentState expectedState = ComponentState.VALID_DEFAULT;
        ComponentState actualState = metadata.getComponentState();
        assertEquals(
                expectedState,
                actualState
        );
        // assert dates. Dates are only generated when a valid persistence model is saved. In this
        // case a save has not taken place, therefore dates will be 0L
        assertEquals(0L, metadata.getCreateDate());
        assertEquals(0L, metadata.getLastUpdate());
        // assert creator, this will always be the current user
        assertEquals(Constants.getUserId(), metadata.getCreatedBy());
    }

    @Test
    public void newRequest_loadValidPersistenceModel_stateVALID_UNCHANGED_failReasonNONE() {
        // Arrange
        TestUseCasePersistenceModel validPersistenceModel = new TestUseCasePersistenceModel.Builder()
                .setDataId("dataId")
                .setDomainId("domainId")
                .setPersistenceModelString(new StringMaker()
                        .makeStringOfLength(TestUseCase.MIN_STRING_LENGTH)
                        .build())
                .setCreateDate(10L)
                .setLastUpdate(10L)
                .build();

        TestRequest initialRequest = new TestRequest.Builder()
                .setDataId(validPersistenceModel.getDataId())
                .build();

        // Act
        SUT.execute(initialRequest, new UseCaseCallbackImplementer());

        // Assert
        verify(dataAccessMock).getByDataId(eq(validPersistenceModel.getDataId()),
                dataAccessCallbackCaptor.capture());
        dataAccessCallbackCaptor.getValue().onPersistenceModelLoaded(validPersistenceModel);

        // check valid persistence model loaded by testing response state values
        UseCaseResponse<TestResponseModel> response = onSuccessResponse;
        UseCaseMetadataModel metadata = response.getUseCaseMetadataModel();

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
    }

    @Test
    public void existingRequest_stateVALID_CHANGED_failReasonNONE() {
        // Arrange
        newRequest_loadValidPersistenceModel_stateVALID_UNCHANGED_failReasonNONE();

        // now change the domain model value to a new valid value
        TestRequest validDomainModelRequest = new TestRequest.Builder()
                .setDataId(onSuccessResponse.getDataId())
                .setDomainId(onSuccessResponse.getDomainId())
                .setRequestModel(
                        new TestRequestModel.Builder()
                                .setRequestModelString(
                                        new StringMaker()
                                                .makeStringOfLength(TestUseCase.MAX_STRING_LENGTH)
                                                .build())
                                .build())
                .build();

        // Act
        SUT.execute(validDomainModelRequest, new UseCaseCallbackImplementer());

        // Assert
        UseCaseResponse<TestResponseModel> actualResponse = onSuccessResponse;
        UseCaseMetadataModel metadata = actualResponse.getUseCaseMetadataModel();
        // assert state
        ComponentState expectedState = ComponentState.VALID_CHANGED;
        ComponentState actualState = metadata.getComponentState();
        assertEquals(
                expectedState,
                actualState
        );
        // assert fail reasons
        List<FailReasons> expectedFailReasons = Collections.singletonList(CommonFailReason.NONE);
        List<FailReasons> actualFailReasons = metadata.getFailReasons();
        assertEquals(
                expectedFailReasons,
                actualFailReasons
        );
    }

    @Test
    public void newRequest_loadInvalidPersistenceModel_stateINVALID_UNCHANGED_failReasonTEXT_TOO_SHORT() {
        // Arrange
        TestUseCasePersistenceModel invalidPersistenceModel = new TestUseCasePersistenceModel.Builder()
                .setDataId("dataId")
                .setDomainId("domainId")
                .setPersistenceModelString(new StringMaker()
                        .makeStringOfLength(TestUseCase.MIN_STRING_LENGTH)
                        .thenRemoveOneCharacter()
                        .build())
                .setCreateDate(10L)
                .setLastUpdate(10L)
                .build();

        TestRequest request = new TestRequest.Builder()
                .setDataId(invalidPersistenceModel.getDataId())
                .build();

        // Act
        SUT.execute(request, new UseCaseCallbackImplementer());

        // Assert
        verify(dataAccessMock).getByDataId(eq(invalidPersistenceModel.getDataId()),
                dataAccessCallbackCaptor.capture());
        dataAccessCallbackCaptor.getValue().onPersistenceModelLoaded(invalidPersistenceModel);

        // assert response values
        UseCaseResponse<TestResponseModel> response = onErrorResponse;
        UseCaseMetadataModel metadata = response.getUseCaseMetadataModel();

        ComponentState expectedState = ComponentState.INVALID_UNCHANGED;
        ComponentState actualComponentState = metadata.getComponentState();
        assertEquals(
                expectedState,
                actualComponentState
        );
        List<FailReasons> expectedFailReasons = Collections.singletonList(
                TestUseCaseFailReasons.TEXT_TOO_SHORT);
        List<FailReasons> actualFailReasons = metadata.getFailReasons();
        assertEquals(
                expectedFailReasons,
                actualFailReasons
        );
    }

    @Test
    public void existingRequest_makeDomainDataInvalid_stateINVALID_CHANGED_failReasonTEXT_TOO_LONG() {
        // Arrange
        // load a persistence model
        newRequest_loadValidPersistenceModel_stateVALID_UNCHANGED_failReasonNONE();

        TestRequest request = new TestRequest.Builder()
                .setDataId(onSuccessResponse.getDataId())
                .setDomainId(onSuccessResponse.getDomainId())
                .setRequestModel(
                        new TestRequestModel.Builder()
                                .setRequestModelString(
                                        new StringMaker()
                                                .makeStringOfLength(TestUseCase.MAX_STRING_LENGTH)
                                                .thenAddOneCharacter()
                                                .build())
                                .build())
                .build();
        // Act
        SUT.execute(request, new UseCaseCallbackImplementer());
        // Assert
        UseCaseResponse<TestResponseModel> response = onErrorResponse;
        UseCaseMetadataModel metadata = response.getUseCaseMetadataModel();

        ComponentState expectedState = ComponentState.INVALID_CHANGED;
        ComponentState actualState = metadata.getComponentState();
        assertEquals(
                expectedState,
                actualState
        );

        List<FailReasons> expectedFailReasons = Collections.singletonList(
                TestUseCaseFailReasons.TEXT_TOO_LONG);
        List<FailReasons> actualFailReasons = metadata.getFailReasons();
        assertEquals(
                expectedFailReasons,
                actualFailReasons
        );

    }

    // region helper methods -----------------------------------------------------------------------
    // endregion helper methods --------------------------------------------------------------------

    // region helper classes -----------------------------------------------------------------------

    private final class UseCaseCallbackImplementer
            implements
            UseCaseCallback<UseCaseResponse<TestResponseModel>> {
        @Override
        public void onSuccess(UseCaseResponse<TestResponseModel> response) {
            onSuccessResponse = response;
        }

        @Override
        public void onError(UseCaseResponse<TestResponseModel> response) {
            onErrorResponse = response;
        }
    }
    // endregion helper classes --------------------------------------------------------------------
}