package com.example.peter.thekitchenmenu.domain.usecase.common;

import com.example.peter.thekitchenmenu.domain.model.BaseDomainModel;
import com.example.peter.thekitchenmenu.domain.usecase.common.usecasemessage.UseCaseMessageModelDataId;

import org.junit.Before;
import org.junit.Test;
import org.mockito.MockitoAnnotations;

import java.util.Objects;

import javax.annotation.Nonnull;

import static org.junit.Assert.*;

public class UseCaseElementRequestFilteringTest {
    // region constants ----------------------------------------------------------------------------
//    private static final String TAG = "tkm-" + UseCaseElementRequestFilteringTest.class.
//            getSimpleName() + ": ";

    private static final String DATA_ID = "DATA_ID";
    private static final String DIFFERENT_DATA_ID = "DIFFERENT_DATA_ID";
    private static final String DOMAIN_ID = "DOMAIN_ID";
    private static final String DIFFERENT_DOMAIN_ID = "DIFFERENT_DOMAIN_ID";
    private static final String COMPARISON_STRING = "COMPARISON_STRING";
    private static final String DIFFERENT_COMPARISON_STRING = "DIFFERENT_COMPARISON_STRING";
    // endregion constants -------------------------------------------------------------------------

    // region helper fields ------------------------------------------------------------------------
    // endregion helper fields ---------------------------------------------------------------------

    private UseCaseElementInheritor SUT;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        SUT = givenUseCase();
    }

    @Test
    public void testSendingInNullValues() {
        // todo
    }

    private UseCaseElementInheritor givenUseCase() {
        return new UseCaseElementInheritor();
    }

    @Test
    public void requestHasDataId_useCaseHasNoDataId_loadDataByDataId() {
        UseCaseElementRequest request = new UseCaseElementRequest.Builder().
                getDefault().
                setDataId(DATA_ID).
                build();

        SUT.execute(request, new DummyUseCaseCallback());

        assertTrue(SUT.isLoadingDataByDataId);
    }

    @Test
    public void requestHasDataId_useCaseHasEqualDataId_processDomainIdCalled() {
        // Arrange
        // This will require two transactions:
        //  first to set the data Id in the use case, second to compare the request and use
        //  case data Id's. So send the same request twice
        UseCaseElementRequest request = new UseCaseElementRequest.Builder().
                getDefault().
                setDataId(DATA_ID).
                setDomainId(DOMAIN_ID).
                build();

        // Act
        SUT.execute(request, new DummyUseCaseCallback());
        SUT.execute(request, new DummyUseCaseCallback());

        // Assert
        // All options for processing by data Id have been exhausted, so process by domainId is
        // called with the domain Id
        assertEquals(
                DOMAIN_ID,
                SUT.useCaseDomainId
        );
    }

    @Test
    public void requestHasDataId_useCaseHasDifferentDataId_loadDataByDataIdCalled() {
        // Arrange
        // This will require two transactions:
        //  first to set the data Id in the use case, second to compare the request and use
        //  case data Id's. So send the same request twice
        UseCaseElementRequest request = new UseCaseElementRequest.Builder().
                getDefault().
                setDataId(DATA_ID).
                setDomainId(DOMAIN_ID).
                build();
        UseCaseElementRequest requestWithDifferentDataId = new UseCaseElementRequest.Builder().
                getDefault().
                setDataId(DIFFERENT_DATA_ID).
                setDomainId(DOMAIN_ID).
                build();

        // Act
        SUT.execute(request, new DummyUseCaseCallback());
        SUT.execute(requestWithDifferentDataId, new DummyUseCaseCallback());

        // Assert
        assertTrue(SUT.isLoadingDataByDataId);
    }

    @Test
    public void requestHasNoDataId_requestHasDomainId_processDomainIdCalled() {
        // Arrange
        UseCaseElementRequest request = new UseCaseElementRequest.Builder().
                getDefault().
                setDomainId(DOMAIN_ID).
                build();
        // Act
        SUT.execute(request, new DummyUseCaseCallback());

        // Assert
        assertEquals(
                DOMAIN_ID,
                SUT.useCaseDomainId
        );
    }

    @Test
    public void requestHasNoDataId_requestHasNoDomainId_buildingResponse() {
        // Arrange
        UseCaseElementRequest request = new UseCaseElementRequest.Builder().getDefault().build();
        // Act
        SUT.execute(request, new DummyUseCaseCallback());
        // Assert
        assertTrue(SUT.isBuildingResponse);
    }

    @Test
    public void requestHasDomainId_useCaseHasEqualDomainId_processDomainModelChanges() {
        // Arrange
        // Requires two requests, one to populate use case domain Id, the second for testing
        // equality of Id's
        UseCaseElementRequest request = new UseCaseElementRequest.Builder().
                getDefault().setDataId(DATA_ID).
                setDomainId(DOMAIN_ID).
                build();
        // Act
        SUT.execute(request, new DummyUseCaseCallback()); // populate the use case Id's
        SUT.execute(request, new DummyUseCaseCallback()); // test for equality of Id's

        // Assert
        assertTrue(SUT.isProcessingDomainModelChanges);
    }

    @Test
    public void requestHasDomainId_useCaseHasUnequalDomainId_loadDataByDomainId() {
        // Arrange
        // Requires two requests. One to populate the domainId, the second to send a different
        // domain Id
        UseCaseElementRequest request = new UseCaseElementRequest.Builder().
                getDefault().
                setDataId(DATA_ID).
                setDomainId(DOMAIN_ID).
                build();

        UseCaseElementRequest differentDomainIdRequest = new UseCaseElementRequest.Builder().
                getDefault().
                setDataId(DATA_ID).
                setDomainId(DIFFERENT_DOMAIN_ID).
                build();

        // Act
        SUT.execute(request, new DummyUseCaseCallback());
        SUT.execute(differentDomainIdRequest, new DummyUseCaseCallback());

        // Assert
        assertTrue(SUT.isLoadingDataByDomainId);
    }

    @Test
    public void requestAndUseCaseHaveEqualDataId_requestHasDomainIdOmittedUseCaseHasDomainId_processDomainModelChanges() {
        // Arrange
        UseCaseElementRequest request = new UseCaseElementRequest.Builder().
                getDefault().
                setDataId(DATA_ID).
                setDomainId(DOMAIN_ID).
                build();

        UseCaseElementRequest requestWithDomainIdOmitted = new UseCaseElementRequest.Builder().
                getDefault().
                setDataId(DATA_ID).
                build();

        // Act
        SUT.execute(request, new DummyUseCaseCallback());
        SUT.execute(requestWithDomainIdOmitted, new DummyUseCaseCallback());

        // Assert
        assertTrue(SUT.isProcessingDomainModelChanges);
    }

    @Test
    public void requestAndUseCaseHaveEqualDataId_requestHasDomainIdOmittedUseCaseHasNoDomainId_loadDataByDataId() {
        // Arrange
        UseCaseElementRequest request = new UseCaseElementRequest.Builder().
                getDefault().
                setDataId(DATA_ID).
                build();

        UseCaseElementRequest requestWithDomainIdOmitted = new UseCaseElementRequest.Builder().
                getDefault().
                setDataId(DATA_ID).
                build();

        // Act
        SUT.execute(request, new DummyUseCaseCallback());
        SUT.execute(requestWithDomainIdOmitted, new DummyUseCaseCallback());

        // Assert
        assertTrue(SUT.isLoadingDataByDataId);
    }

    @Test
    public void requestAndUseCaseDataIdAreEqual_requestAndUseCaseDomainIdsAreEqual_processDomainModelChanges() {
        // Arrange
        // With this test, no data has changed, so resend existing state
        UseCaseElementRequest request = new UseCaseElementRequest.Builder().
                getDefault().
                setDataId(DATA_ID).
                setDomainId(DOMAIN_ID).
                setDomainModel(
                        new UseCaseElementRequest.DomainModel.Builder().
                                getDefault().
                                setComparisonString(COMPARISON_STRING).
                                build()).
                build();

        // Act
        SUT.execute(request, new DummyUseCaseCallback());
        SUT.execute(request, new DummyUseCaseCallback());

        // Assert
        assertTrue(SUT.isProcessingDomainModelChanges);
    }

    // region helper methods -----------------------------------------------------------------------
    // endregion helper methods --------------------------------------------------------------------

    // region helper classes -----------------------------------------------------------------------
    // Helper class to assist in testing abstract UseCaseElement
    private static class UseCaseElementInheritor extends UseCaseElement {

        private static final String TAG = "tkm-" + UseCaseElementInheritor.class.
                getSimpleName() + ": ";

        private boolean isLoadingDataByDataId;
        private boolean isLoadingDataByDomainId;
        private boolean isProcessingDomainModelChanges;
        private boolean isBuildingResponse;

        public UseCaseElementInheritor() {
            useCaseDomainModel = new BaseDomainModel() {
            };
        }

        @Override
        protected void loadDataByDataId() {
            System.out.println(TAG + "loadingDataByDataId");
            isLoadingDataByDataId = true;
        }

        @Override
        protected void loadDataByDomainId() {
            System.out.println(TAG + "loading data by domain Id");
            isLoadingDataByDomainId = true;
        }

        @Override
        protected void processDomainModel() {
            System.out.println(TAG + "processing domain model changes");
            isProcessingDomainModelChanges = true;
            isDomainModelChanged();
        }

        @Override
        protected boolean isDomainModelChanged() {
            System.out.println(TAG + "requestDomainModel=" + requestDomainModel +
                    " useCaseDomainModel=" + useCaseDomainModel);
            return false;
        }

        @Override
        protected void buildResponse() {
            System.out.println(TAG + "building response");
            isBuildingResponse = true;
        }
    }

    // Helper request class
    public static class UseCaseElementRequest
            extends
            UseCaseMessageModelDataId<UseCaseElementRequest.DomainModel>
            implements UseCaseBase.Request {

        @Nonnull
        @Override
        public String toString() {
            return "UseCaseElementRequest{" +
                    "dataId='" + dataId + '\'' +
                    ", domainId='" + domainId + '\'' +
                    ", model=" + model +
                    '}';
        }

        private UseCaseElementRequest() {
        }

        public static class Builder
                extends
                UseCaseMessageModelDataIdBuilder<Builder, UseCaseElementRequest, DomainModel> {

            public Builder() {
                message = new UseCaseElementRequest();
            }

            @Override
            public Builder getDefault() {
                message.dataId = "";
                message.domainId = "";
                message.model = new DomainModel.Builder().getDefault().build();
                return self();
            }

            @Override
            protected Builder self() {
                return this;
            }
        }

        private static class DomainModel
                extends
                BaseDomainModel {

            // For comparison purposes
            private String comparisonString;

            @Override
            public boolean equals(Object o) {
                if (this == o) return true;
                if (o == null || getClass() != o.getClass()) return false;
                DomainModel that = (DomainModel) o;
                return Objects.equals(comparisonString, that.comparisonString);
            }

            @Override
            public int hashCode() {
                return Objects.hash(comparisonString);
            }

            @Nonnull
            @Override
            public String toString() {
                return "DomainModel{" +
                        "comparisonString='" + comparisonString + '\'' +
                        '}';
            }

            private static class Builder
                    extends
                    DomainModelBuilder<Builder, UseCaseElementRequest.DomainModel> {

                public Builder() {
                    domainModel = new DomainModel();
                }

                @Override
                public Builder getDefault() {
                    domainModel.comparisonString = "";
                    return self();
                }

                public Builder setComparisonString(String comparisonString) {
                    domainModel.comparisonString = comparisonString;
                    return self();
                }

                @Override
                protected Builder self() {
                    return this;
                }
            }
        }
    }

    // We will not be receiving a response, but we need a callback so create a Dummy callback class.
    private static class DummyUseCaseCallback implements UseCaseBase.Callback<UseCaseBase.Response> {
        @Override
        public void onUseCaseSuccess(UseCaseBase.Response response) {

        }

        @Override
        public void onUseCaseError(UseCaseBase.Response response) {

        }
    }
    // endregion helper classes --------------------------------------------------------------------

}