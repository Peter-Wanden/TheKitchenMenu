package com.example.peter.thekitchenmenu.domain.usecase.common;

import com.example.peter.thekitchenmenu.data.repository.DomainDataAccess.GetDomainModelCallback;
import com.example.peter.thekitchenmenu.domain.usecase.common.failreasons.CommonFailReason;
import com.example.peter.thekitchenmenu.domain.usecase.common.failreasons.FailReasons;
import com.example.peter.thekitchenmenu.domain.usecase.common.helperclasses.TestDomainModelConverter;
import com.example.peter.thekitchenmenu.domain.usecase.common.helperclasses.TestUseCase;
import com.example.peter.thekitchenmenu.domain.usecase.common.helperclasses.TestUseCaseDataAccess;
import com.example.peter.thekitchenmenu.domain.usecase.common.helperclasses.TestUseCasePersistenceModel;
import com.example.peter.thekitchenmenu.domain.usecase.common.helperclasses.TestUseCaseRequest;
import com.example.peter.thekitchenmenu.domain.usecase.common.helperclasses.TestUseCaseRequestModel;
import com.example.peter.thekitchenmenu.domain.usecase.common.helperclasses.TestUseCaseResponse;
import com.example.peter.thekitchenmenu.domain.utils.TimeProvider;
import com.example.peter.thekitchenmenu.domain.utils.UniqueIdProvider;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class UseCaseDataTest {

    // region constants ----------------------------------------------------------------------------
    private static final String DATA_ID = "DATA_ID";
    private static final String DIFFERENT_DATA_ID = "DIFFERENT_DATA_ID";
    private static final String DOMAIN_ID = "DOMAIN_ID";
    private static final String DIFFERENT_DOMAIN_ID = "DIFFERENT_DOMAIN_ID";
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

    private TestUseCaseResponse onSuccessResponse;
    private TestUseCaseResponse onErrorResponse;

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
        return new TestUseCase(
                dataAccessMock,
                converter,
                idProviderMock,
                timeProviderMock
        );
    }

    @Test
    public void emptyUseCase_requestHasNoDataIds_failReasonDATA_UNAVAILABLE() {
        // Arrange
        // A default 'empty' request returns the current state and domain data of the use case.
        TestUseCaseRequest request = new TestUseCaseRequest.Builder()
                .getDefault()
                .build();
        // Act
        SUT.execute(request, new UseCaseCallbackImplementer());
        // Assert
        FailReasons expectedFailReason = CommonFailReason.DATA_UNAVAILABLE; // no data loaded
        assertTrue(
                onErrorResponse.getMetadata().getFailReasons().contains(expectedFailReason)
        );
        String expectedResponseModelValue = SUT.useCaseModelDefaultValue;
        String actualResponseModelValue = onErrorResponse.getDomainModel().getResponseModelString();
        assertEquals(
                expectedResponseModelValue,
                actualResponseModelValue
        );
    }

    @Test
    public void emptyUseCase_requestHasNoDataIdHasDomainId_getPersistenceModelByDomainId() {
        // Arrange
        TestUseCaseRequest request = new TestUseCaseRequest.Builder()
                .getDefault()
                .setDomainId(DOMAIN_ID)
                .build();
        // Act
        SUT.execute(request, new UseCaseCallbackImplementer());
        // Assert getPersistenceModelByDomainId() method called by asserting data access called
        verify(dataAccessMock).getByDomainId(eq(DOMAIN_ID), dataAccessCallbackCaptor.capture());
    }

    @Test
    public void emptyUseCase_requestHasDataIdNoDomainId_getPersistenceModelByDataId() {
        // Arrange
        TestUseCaseRequest request = new TestUseCaseRequest.Builder()
                .getDefault()
                .setDataId(DATA_ID)
                .build();
        // Act
        SUT.execute(request, new UseCaseCallbackImplementer());
        // Assert
        verify(dataAccessMock).getByDataId(eq(DATA_ID), dataAccessCallbackCaptor.capture());
    }

    @Test
    public void emptyUseCase_requestHasDataIdHasDomainId_getPersistenceModelByDataId() {
        // Arrange
        TestUseCaseRequest request = new TestUseCaseRequest.Builder()
                .getDefault()
                .setDataId(DATA_ID)
                .setDomainId(DOMAIN_ID)
                .build();
        // Act
        SUT.execute(request, new UseCaseCallbackImplementer());
        // Assert
        verify(dataAccessMock).getByDataId(eq(DATA_ID), dataAccessCallbackCaptor.capture());
    }

    @Test
    public void emptyUseCase_loadByDataId_dataUnavailable() {
        // Arrange
        TestUseCaseRequest request = new TestUseCaseRequest.Builder().
                getDefault().
                setDataId(DATA_ID).
                build();
        // Act
        SUT.execute(request, new UseCaseCallbackImplementer());
        // Assert
        // assert data access called
        verify(dataAccessMock).getByDataId(eq(DATA_ID), dataAccessCallbackCaptor.capture());
        dataAccessCallbackCaptor.getValue().onPersistenceModelUnavailable();
        // assert default model created
        FailReasons expectedFailReason = CommonFailReason.DATA_UNAVAILABLE; // no data loaded
        assertTrue(
                onErrorResponse.getMetadata().getFailReasons().contains(expectedFailReason)
        );
        String expectedResponseModelValue = SUT.useCaseModelDefaultValue;
        String actualResponseModelValue = onErrorResponse.getDomainModel().getResponseModelString();
        assertEquals(
                expectedResponseModelValue,
                actualResponseModelValue
        );
    }

    @Test
    public void emptyUseCase_loadByDataId_persistenceModelLoaded() {
        // Arrange
        TestUseCasePersistenceModel expectedModel = new TestUseCasePersistenceModel.Builder()
                .getDefault()
                .setDataId(DATA_ID)
                .setDomainId(DOMAIN_ID)
                .setPersistenceModelString("expectedPersistenceModelString")
                .setCreateDate(10L)
                .setLastUpdate(20L)
                .build();

        TestUseCaseRequest request = new TestUseCaseRequest.Builder().
                getDefault().
                setDataId(DATA_ID).
                build();
        // Act
        SUT.execute(request, new UseCaseCallbackImplementer());
        // Assert
        // verify data access
        verify(dataAccessMock).getByDataId(eq(DATA_ID), dataAccessCallbackCaptor.capture());
        dataAccessCallbackCaptor.getValue().onPersistenceModelLoaded(expectedModel);
        // assert response values
        TestUseCaseResponse actualResponse = onSuccessResponse;
        assertEquals(
                expectedModel.getDataId(),
                actualResponse.getDataId()
        );
        assertEquals(
                expectedModel.getDomainId(),
                actualResponse.getDomainId()
        );
        assertEquals(
                expectedModel.getPersistenceModelString(),
                actualResponse.getDomainModel().getResponseModelString()
        );
    }

    @Test
    public void loadedUseCase_requestHasNoDataIds_currentInternalDomainDataReturned() {
        // note: if a request is received without ids the current state is reprocessed starting
        // by initialising the use case
        // Arrange
        // get the previous test persistence model
        TestUseCasePersistenceModel expectedModel = new TestUseCasePersistenceModel.Builder()
                .getDefault()
                .setDataId(DATA_ID)
                .setDomainId(DOMAIN_ID)
                .setPersistenceModelString("expectedPersistenceModelString")
                .setCreateDate(10L)
                .setLastUpdate(20L)
                .build();

        // execute previous test to load data
        emptyUseCase_loadByDataId_persistenceModelLoaded(); // load data into the use case

        // default request to return current internal domain data
        TestUseCaseRequest request = new TestUseCaseRequest.Builder().
                getDefault().
                build();
        // Act
        SUT.execute(request, new UseCaseCallbackImplementer());
        // Assert
        TestUseCaseResponse actualResponse = onSuccessResponse;
        assertEquals(
                expectedModel.getDataId(),
                actualResponse.getDataId()
        );
        assertEquals(
                expectedModel.getDomainId(),
                actualResponse.getDomainId()
        );
        assertEquals(
                expectedModel.getPersistenceModelString(),
                actualResponse.getDomainModel().getResponseModelString()
        );
    }

    @Test
    public void loadedUseCase_requestHasNoDataIdHasDomainIdEqualToUseCaseId_requestModelProcessed() {
        // Arrange
        // get the previous test persistence model and update the data id
        TestUseCasePersistenceModel expectedModel = new TestUseCasePersistenceModel.Builder()
                .getDefault()
                .setDataId(DIFFERENT_DATA_ID)
                .setDomainId(DOMAIN_ID)
                .setPersistenceModelString("expectedPersistenceModelString")
                .setCreateDate(10L)
                .setLastUpdate(20L)
                .build();

        // loads persistence model into use case
        emptyUseCase_loadByDataId_persistenceModelLoaded();

        String expectedModelValue = "expectedModelValue";

        // loads request model string into use case
        TestUseCaseRequest request = new TestUseCaseRequest.Builder()
                .getDefault()
                .setDomainId(DOMAIN_ID)
                .setModel(new TestUseCaseRequestModel.Builder()
                        .setRequestModelString(expectedModelValue)
                        .build())
                .build();
        // as we are changing the domain data the previous domain model will archived and a new one
        // created. Therefore a new data id will be required
        when(idProviderMock.getUId()).thenReturn(expectedModel.getDataId());

        // Act
        SUT.execute(request, new UseCaseCallbackImplementer());

        // Assert
        TestUseCaseResponse actualResponse = onSuccessResponse;
        assertEquals(
                expectedModel.getDataId(),
                actualResponse.getDataId()
        );
        assertEquals(
                expectedModel.getDomainId(),
                actualResponse.getDomainId()
        );
        assertEquals(
                expectedModelValue,
                actualResponse.getDomainModel().getResponseModelString()
        );
    }

    @Test
    public void loadedUseCase_requestHasNoDataIdDomainIdNotEqualToUseCaseId_getPersistenceModelByDomainId() {
        // Arrange
        // loads persistence model into use case
        emptyUseCase_loadByDataId_persistenceModelLoaded();

        TestUseCaseRequest request = new TestUseCaseRequest.Builder().
                setDomainId(DIFFERENT_DOMAIN_ID).
                build();
        // Act
        SUT.execute(request, new UseCaseCallbackImplementer());
        // Assert, use case attempts to load by different domain id
        verify(dataAccessMock).getByDomainId(eq(DIFFERENT_DOMAIN_ID), dataAccessCallbackCaptor.capture());
    }

    @Test
    public void loadedUseCase_requestHasDataIdEqualToUseCaseDataIdNoDomainId_requestDomainModelProcessed() {
        // Arrange
        // loads persistence model into use case
        emptyUseCase_loadByDataId_persistenceModelLoaded();

        TestUseCaseRequest request = new TestUseCaseRequest.Builder()
                .getDefault()
                .setDataId(DATA_ID)
                .setModel(new TestUseCaseRequestModel.Builder()
                        .setRequestModelString("expectedUseCaseModelValue")
                        .build())
                .build();
        // as we are changing the value of the domain data in the use case a new data id will be
        // required
        when(idProviderMock.getUId()).thenReturn(DIFFERENT_DATA_ID);
        // Act
        SUT.execute(request, new UseCaseCallbackImplementer());
        // Assert
        TestUseCaseResponse actualResponse = onSuccessResponse;
        String expectedResponseValue = request.getModel().getRequestModelString();
        String actualResponseValue = actualResponse.getDomainModel().getResponseModelString();
        assertEquals(
                expectedResponseValue,
                actualResponseValue
        );
        assertEquals(DIFFERENT_DATA_ID, actualResponse.getDataId());
    }

    @Test
    public void loadedUseCase_requestHasDataIdNotEqualToUseCaseNoDomainId_getPersistenceModelByDataId() {
        // Arrange
        // loads persistence model string into use case
        emptyUseCase_loadByDataId_persistenceModelLoaded();

        TestUseCaseRequest request = new TestUseCaseRequest.Builder()
                .getDefault()
                .setDataId(DIFFERENT_DATA_ID)
                .build();
        // Act
        SUT.execute(request, new UseCaseCallbackImplementer());
        // Assert
        verify(dataAccessMock).getByDataId(eq(DIFFERENT_DATA_ID), dataAccessCallbackCaptor.capture());
    }

    @Test
    public void loadedUseCase_requestHasDataIdEqualToUseCaseHasDomainIdEqualToUseCase_requestModelIsProcessed() {
        // Arrange
        // loads persistence model into use case
        emptyUseCase_loadByDataId_persistenceModelLoaded();

        String expectedResponseModelValue = "expectedResponseModelValue";
        TestUseCaseRequest request = new TestUseCaseRequest.Builder()
                .getDefault()
                .setDataId(DATA_ID)
                .setDomainId(DOMAIN_ID)
                .setModel(new TestUseCaseRequestModel.Builder()
                        .setRequestModelString(expectedResponseModelValue)
                        .build())
                .build();
        // Act
        SUT.execute(request, new UseCaseCallbackImplementer());
        // Assert
        assertEquals(
                expectedResponseModelValue,
                onSuccessResponse.getDomainModel().getResponseModelString()
        );
    }

    @Test
    public void loadedUseCase_requestDataIdNotEqualToUseCaseDomainIdEqualToUseCase_getPersistenceModelByDataId() {
        // Arrange
        // loads persistence model into use case
        emptyUseCase_loadByDataId_persistenceModelLoaded();

        TestUseCaseRequest request = new TestUseCaseRequest.Builder()
                .getDefault()
                .setDataId(DIFFERENT_DATA_ID)
                .setDomainId(DOMAIN_ID)
                .build();
        // Act
        SUT.execute(request, new UseCaseCallbackImplementer());
        // Assert
        verify(dataAccessMock).getByDataId(eq(DIFFERENT_DATA_ID), dataAccessCallbackCaptor.capture());
    }

    // todo test for archiving and saving of models

    // region helper methods -----------------------------------------------------------------------
    // endregion helper methods --------------------------------------------------------------------

    // region helper classes -----------------------------------------------------------------------
    private class UseCaseCallbackImplementer
            implements
            UseCaseBase.Callback<TestUseCaseResponse> {

        @Override
        public void onUseCaseSuccess(TestUseCaseResponse testResponse) {
            onSuccessResponse = testResponse;
        }

        @Override
        public void onUseCaseError(TestUseCaseResponse testResponse) {
            onErrorResponse = testResponse;
        }
    }
    // endregion helper classes --------------------------------------------------------------------
}