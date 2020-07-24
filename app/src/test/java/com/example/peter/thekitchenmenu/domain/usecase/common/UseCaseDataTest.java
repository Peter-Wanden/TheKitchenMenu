package com.example.peter.thekitchenmenu.domain.usecase.common;

import com.example.peter.thekitchenmenu.data.repository.DomainDataAccess.GetDomainModelCallback;
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
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.verify;

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
    // endregion helper fields ---------------------------------------------------------------------
    private TestUseCase SUT;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        SUT = givenUseCase();

    }

    private TestUseCase givenUseCase() {
        return new TestUseCase(
                dataAccessMock,
                new TestDomainModelConverter(),
                idProviderMock,
                timeProviderMock
        );
    }

    @Test
    public void emptyUseCase_requestHasNoDataIds_processDataElements() {
        // Arrange
        // A default 'empty' request returns the current state and domain data of the use case.
        TestUseCaseRequest request = new TestUseCaseRequest.Builder()
                .getDefault()
                .build();
        // Act
        SUT.execute(request, new UseCaseCallbackImplementer());
        // Assert
//        assertTrue(SUT.isInitialiseUseCase);
    }

    @Test
    public void emptyUseCase_requestHasNoDataIdHasDomainId_getPersistenceModelByDomainId() {
        // Arrange
        TestUseCaseRequest request = new TestUseCaseRequest.Builder().getDefault().setDomainId(DOMAIN_ID).build();
        // Act
        SUT.execute(request, new UseCaseCallbackImplementer());
        // Assert getPersistenceModelByDomainId() method called by asserting data access called
        verify(dataAccessMock).getByDomainId(eq(DOMAIN_ID), dataAccessCallbackCaptor.capture());
    }

    @Test
    public void emptyUseCase_requestHasDataIdNoDomainId_getPersistenceModelByDataId() {
        // Arrange
        TestUseCaseRequest request = new TestUseCaseRequest.Builder().getDefault().setDataId(DATA_ID).build();
        // Act
        SUT.execute(request, new UseCaseCallbackImplementer());
        // Assert
        verify(dataAccessMock).getByDataId(eq(DATA_ID), dataAccessCallbackCaptor.capture());
    }

    @Test
    public void emptyUseCase_requestHasDataIdHasDomainId_getPersistenceModelByDataId() {
        // Arrange
        TestUseCaseRequest request = new TestUseCaseRequest.Builder().
                getDefault().
                setDataId(DATA_ID).
                setDomainId(DOMAIN_ID).
                build();
        // Act
        SUT.execute(request, new UseCaseCallbackImplementer());
        // Assert
        verify(dataAccessMock).getByDataId(eq(DATA_ID), dataAccessCallbackCaptor.capture());
    }

    @Test
    public void emptyUseCase_loadByDataId_dataUnavailable() {
        // Arrange
        TestUseCaseRequest request = new TestUseCaseRequest.Builder().getDefault().setDataId(DATA_ID).build();
        // Act
        SUT.execute(request, new UseCaseCallbackImplementer());
        // Assert
        // assert data access called
        verify(dataAccessMock).getByDataId(eq(DATA_ID), dataAccessCallbackCaptor.capture());
        dataAccessCallbackCaptor.getValue().onPersistenceModelUnavailable();
        // assert default model created
//        assertTrue(SUT.isCreateUseCaseFromDefaultValues);
    }

    @Test
    public void emptyUseCase_loadByDataId_persistenceModelLoaded() {
        // Arrange
        TestUseCasePersistenceModel expectedPersistenceModel = new TestUseCasePersistenceModel.Builder()
                .getDefault()
                .setDataId(DATA_ID)
                .setDomainId(DOMAIN_ID)
                .setCreateDate(10L)
                .setLastUpdate(20L)
                .build();

        TestUseCaseRequest request = new TestUseCaseRequest.Builder().getDefault().setDataId(DATA_ID).build();
        // Act
        SUT.execute(request, new UseCaseCallbackImplementer());
        // Assert
        // verify data access
        verify(dataAccessMock).getByDataId(eq(DATA_ID), dataAccessCallbackCaptor.capture());
        dataAccessCallbackCaptor.getValue().onPersistenceModelLoaded(expectedPersistenceModel);

        // assert correct SUT internal values
        assertFalse(SUT.isChanged);
        assertEquals(
                expectedPersistenceModel.getDataId(),
                SUT.useCaseDataId
        );
        assertEquals(
                expectedPersistenceModel.getDomainId(),
                SUT.useCaseDomainId
        );
        assertEquals(
                expectedPersistenceModel,
                SUT.persistenceModel
        );
//        assertTrue(SUT.isInitialiseUseCase);
    }

    @Test
    public void loadedUseCase_requestHasNoDataIds_isInitialiseUseCase() {
        // note: if a request is received without ids the current state is reprocessed starting
        // by initialising the use case
        // Arrange
        emptyUseCase_loadByDataId_persistenceModelLoaded(); // load data into the use case
//        SUT.isInitialiseUseCase = false;

        TestUseCaseRequest request = new TestUseCaseRequest.Builder().getDefault().build();
        // Act
        SUT.execute(request, new UseCaseCallbackImplementer());
        // Assert
//        assertTrue(SUT.isInitialiseUseCase);
    }

    @Test
    public void loadedUseCase_requestHasNoDataIdHasDomainIdEqualToUseCaseId_setupForRequestModelProcessing() {
        // Arrange
        // we can tell if 'setupForRequestModelProcessing' has been called as the use case domain
        // model will have been assigned the values in the request model

        // loads persistence model string into use case
        emptyUseCase_loadByDataId_persistenceModelLoaded();

        // loads request model string into use case
        TestUseCaseRequest request = new TestUseCaseRequest.Builder()
                .getDefault()
                .setDomainId(DOMAIN_ID)
                .build();
        // Act
        SUT.execute(request, new UseCaseCallbackImplementer());
        // Assert
        String expectedUseCaseModelString = request.getModel().getRequestModelString();
        assertEquals(
                expectedUseCaseModelString,
                SUT.useCaseModel.getUseCaseModelString()
        );
    }

    @Test
    public void loadedUseCase_requestHasNoDataIdDomainIdNotEqualToUseCaseId_getPersistenceModelByDomainId() {
        // Arrange
        // loads persistence model string into use case
        emptyUseCase_loadByDataId_persistenceModelLoaded();

        TestUseCaseRequest request = new TestUseCaseRequest.Builder().setDomainId(DIFFERENT_DOMAIN_ID).build();
        // Act
        SUT.execute(request, new UseCaseCallbackImplementer());
        // Assert, different domain id received, use case attempts to load by different domain id
        verify(dataAccessMock).getByDomainId(eq(DIFFERENT_DOMAIN_ID), dataAccessCallbackCaptor.capture());
    }

    @Test
    public void loadedUseCase_requestHasDataIdEqualToUseCaseDataIdNoDomainId_isProcessRequestDomainModel() {
        // Arrange
        // loads persistence model string into use case
        emptyUseCase_loadByDataId_persistenceModelLoaded();

        TestUseCaseRequestModel requestModel = new TestUseCaseRequestModel("expectedUseCaseModelValue");
        TestUseCaseRequest request = new TestUseCaseRequest.Builder()
                .getDefault()
                .setDataId(DATA_ID)
                .setModel(requestModel)
                .build();
        // Act
        SUT.execute(request, new UseCaseCallbackImplementer());
        // Assert
        String expectedUseCaseValue = requestModel.getRequestModelString();
        String actualUseCaseValue = SUT.useCaseModel.getUseCaseModelString();
        assertEquals(
                expectedUseCaseValue,
                actualUseCaseValue
        );
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
    public void loadedUseCase_requestHasDataIdEqualToUseCaseHasDomainIdEqualToUseCase_setupForRequestModelProcessing() {
        // Arrange
        // loads persistence model string into use case
        emptyUseCase_loadByDataId_persistenceModelLoaded();

        TestUseCaseRequest request = new TestUseCaseRequest.Builder()
                .getDefault()
                .setDataId(DATA_ID)
                .setDomainId(DOMAIN_ID)
                .build();
        // Act
        SUT.execute(request, new UseCaseCallbackImplementer());
        // Assert
        assertTrue(SUT.isChanged); // request domain data is different from use case domain data
    }

    @Test
    public void loadedUseCase_requestDataIdNotEqualToUseCaseDomainIdEqualToUseCase_getPersistenceModelByDataId() {
        // Arrange
        // loads persistence model string into use case
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


    // region helper methods -----------------------------------------------------------------------
    // endregion helper methods --------------------------------------------------------------------

    // region helper classes -----------------------------------------------------------------------
    private static class UseCaseCallbackImplementer
            implements
            UseCaseBase.Callback<TestUseCaseResponse> {
        private static final String TAG = "tkm-" + UseCaseCallbackImplementer.class.
                getSimpleName() + ": ";

        private TestUseCaseResponse response;

        @Override
        public void onUseCaseSuccess(TestUseCaseResponse testResponse) {
            response = testResponse;
            System.out.println(TAG + response);
        }

        @Override
        public void onUseCaseError(TestUseCaseResponse testResponse) {
            response = testResponse;
            System.out.println(TAG + response);
        }
    }
    // endregion helper classes --------------------------------------------------------------------
}