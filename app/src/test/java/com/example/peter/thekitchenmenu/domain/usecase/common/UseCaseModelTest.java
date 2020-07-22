package com.example.peter.thekitchenmenu.domain.usecase.common;

import com.example.peter.thekitchenmenu.data.repository.DataAccess;
import com.example.peter.thekitchenmenu.data.repository.DomainDataAccess;
import com.example.peter.thekitchenmenu.data.repository.DomainDataAccess.GetDomainModelCallback;
import com.example.peter.thekitchenmenu.domain.model.BaseDomainModel;
import com.example.peter.thekitchenmenu.domain.model.DomainModel;
import com.example.peter.thekitchenmenu.domain.usecase.common.usecasemessage.UseCaseMessageModelDataId;
import com.example.peter.thekitchenmenu.domain.usecase.common.usecasemessage.UseCaseMessageModelDataIdMetadata;
import com.example.peter.thekitchenmenu.domain.usecase.common.usecasemessage.message.RequestWithId;
import com.example.peter.thekitchenmenu.domain.utils.TimeProvider;
import com.example.peter.thekitchenmenu.domain.utils.UniqueIdProvider;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import javax.annotation.Nonnull;

import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.verify;

public class UseCaseModelTest {

    private static final String TAG = "tkm-" + UseCaseModelTest.class.getSimpleName() + ": ";

    // region constants ----------------------------------------------------------------------------
    private static final String DATA_ID = "DATA_ID";
    private static final String DIFFERENT_DATA_ID = "DIFFERENT_DATA_ID";
    private static final String DOMAIN_ID = "DOMAIN_ID";
    private static final String DIFFERENT_DOMAIN_ID = "DIFFERENT_DOMAIN_ID";
    // endregion constants -------------------------------------------------------------------------

    // region helper fields ------------------------------------------------------------------------
    @Mock
    TestDataAccess dataAccessMock;
    @Captor
    ArgumentCaptor<GetDomainModelCallback<TestPersistenceModel>> dataAccessCallbackCaptor;
    @Mock
    UniqueIdProvider idProviderMock;
    @Mock
    TimeProvider timeProviderMock;
    // endregion helper fields ---------------------------------------------------------------------
    private UseCaseModelInheritor SUT;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        SUT = givenUseCase();

    }

    private UseCaseModelInheritor givenUseCase() {
        return new UseCaseModelInheritor(
                dataAccessMock,
                new TestDomainModelConverter(),
                idProviderMock,
                timeProviderMock
        );
    }

    // region empty use case tests -----------------------------------------------------------------
    @Test
    public void emptyUseCase_noDataIdNoDomainId_isReprocessCurrentDomainModel() {
        // Arrange
        // A default 'empty' request returns the current state and domain data of the use case.
        TestRequest request = new TestRequest();
        // Act
        SUT.execute(request, new UseCaseCallbackImplementer());
        // Assert
        assertTrue(SUT.isInitialiseUseCase);
    }

    @Test
    public void emptyUseCase_noDataIdHasDomainId_getPersistenceModelByDomainId() {
        // Arrange
        TestRequest request = new TestRequest.Builder().getDefault().setDomainId(DOMAIN_ID).build();
        // Act
        SUT.execute(request, new UseCaseCallbackImplementer());
        // Assert
        verify(dataAccessMock).getActiveByDomainId(eq(DOMAIN_ID), dataAccessCallbackCaptor.capture());
    }

    // region helper methods -----------------------------------------------------------------------
    // endregion helper methods --------------------------------------------------------------------

    // region helper classes -----------------------------------------------------------------------
    /**
     * A concrete class constructed to test the functionality
     */
    private static class UseCaseModelInheritor
            extends
            UseCaseModel<
                    TestDataAccess,
                    TestPersistenceModel,
                    TestUseCaseModel,
                    TestRequestModel,
                    TestResponseModel> {

        public static final String TAG = "tkm-" + UseCaseModelInheritor.class.getSimpleName() +
                ": ";

        // Spies recording method calls
        private boolean isLoadDomainModelByDataId;
        private boolean isLoadDomainModelByDomainId;
        private boolean isReprocessCurrentDomainModel;
        private boolean isProcessRequestDomainModel;
        private boolean isInitialiseUseCase;

        public UseCaseModelInheritor(TestDataAccess dataAccess,
                                     TestDomainModelConverter converter,
                                     UniqueIdProvider idProvider,
                                     TimeProvider timeProvider) {
            super(dataAccess, converter, idProvider, timeProvider);
        }

        @Override
        protected TestUseCaseModel createUseCaseModelFromDefaultValues() {
            System.out.println();
            return new TestUseCaseModel();
        }

        @Override
        protected void initialiseUseCase() {
            isInitialiseUseCase = true;
            System.out.println(TAG + "initialiseUseCase()");
        }
    }

    private static class TestDataAccess
            extends
            DataAccess<TestPersistenceModel> {
    }

    private static class TestPersistenceModel
            implements
            DomainModel.PersistenceModel {

        @Override
        public String getDataId() {
            return null;
        }

        @Override
        public String getDomainId() {
            return null;
        }

        @Override
        public long getCreateDate() {
            return 0;
        }

        @Override
        public long getLastUpdate() {
            return 0;
        }
    }

    private static class TestUseCaseModel
            implements
            DomainModel.UseCaseModel {
        private static final String TAG = "tkm-" + TestUseCaseModel.class.getSimpleName() + ": ";
        private String useCaseModelTestValue = "useCaseModelTestValue";
    }

    private static class TestRequest
            extends
            RequestWithId<TestRequestModel> {

        public TestRequest() {
        }

        public static class Builder
                extends
                RequestWithId.IdBuilder<Builder, TestRequest, TestRequestModel> {

            @Override
            public Builder getDefault() {
                message.dataId = NO_ID;
                message.domainId = NO_ID;
                message.model = new TestRequestModel();
                return self();
            }
        }
    }

    private static class TestRequestModel
            implements
            DomainModel.UseCaseRequestModel {
        private String requestModelString = "testRequestModelString";

        @Nonnull
        @Override
        public String toString() {
            return "TestRequestModel{" +
                    "requestModelString='" + requestModelString + '\'' +
                    '}';
        }
    }

    private static class TestResponse
            extends
            UseCaseMessageModelDataIdMetadata<TestResponseModel>
            implements
            UseCaseBase.Response {
    }

    private static class TestResponseModel
            extends
            BaseDomainModel
            implements
            DomainModel.UseCaseResponseModel {

    }

    private static class TestDomainModelConverter
            implements
            DomainModel.Converter<
            TestUseCaseModel,
            TestPersistenceModel,
            TestRequestModel,
            TestResponseModel
            > {

        @Override
        public TestUseCaseModel convertPersistenceToDomainModel(@Nonnull TestPersistenceModel model) {
            return null;
        }

        @Override
        public TestUseCaseModel convertRequestToUseCaseModel(@Nonnull TestRequestModel model) {
            return null;
        }

        @Override
        public TestPersistenceModel createNewPersistenceModel() {
            return null;
        }

        @Override
        public TestPersistenceModel createArchivedPersistenceModel(@Nonnull TestPersistenceModel model) {
            return null;
        }

        @Override
        public TestResponseModel convertUseCaseToResponseModel(@Nonnull TestUseCaseModel model) {
            return null;
        }
    }

    private static class UseCaseCallbackImplementer
            implements
            UseCaseBase.Callback<TestResponse> {

        @Override
        public void onUseCaseSuccess(TestResponse testResponse) {

        }

        @Override
        public void onUseCaseError(TestResponse testResponse) {

        }
    }
    // endregion helper classes --------------------------------------------------------------------
}