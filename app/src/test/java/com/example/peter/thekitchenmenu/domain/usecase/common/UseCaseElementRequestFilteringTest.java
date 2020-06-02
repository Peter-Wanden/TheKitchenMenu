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
    private static final String DATA_ID = "DATA_ID";
    private static final String DIFFERENT_DATA_ID = "DIFFERENT_DATA_ID";
    private static final String DOMAIN_ID = "DOMAIN_ID";
    private static final String DIFFERENT_DOMAIN_ID = "DIFFERENT_DOMAIN_ID";
    // endregion constants -------------------------------------------------------------------------

    // region helper fields ------------------------------------------------------------------------
    // endregion helper fields ---------------------------------------------------------------------

    private UseCaseElementInheritor SUT;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        SUT = givenUseCase();
    }

    private UseCaseElementInheritor givenUseCase() {
        return new UseCaseElementInheritor();
    }

    @Test
    public void testSendingInNullValues() {
        // todo
    }

    // region empty use case tests -----------------------------------------------------------------
    // An empty use case is one that is yet to receive its first request
    @Test
    public void emptyUseCase_noDataIdNoDomainId_processUseCaseData() {
        // Arrange
        // A default 'empty' request returns the current state of the use case for all use cases
        UseCaseElementRequest request = new UseCaseElementRequest.Builder().getDefault().build();
        // Act
        SUT.execute(request, new DummyUseCaseCallback());
        // Assert
        assertTrue(SUT.isProcessingUseCaseData);
    }

    @Test
    public void emptyUseCase_noDataIdHasDomainId_loadDataByDomainId() {
        // Arrange
        UseCaseElementRequest request = new UseCaseElementRequest.Builder().
                getDefault().
                setDomainId(DOMAIN_ID).
                build();
        // Act
        SUT.execute(request, new DummyUseCaseCallback());
        // Assert
        assertTrue(SUT.isLoadingDataByDomainId);
    }

    @Test
    public void emptyUseCase_hasDataIdNoDomainId_loadDataByDataId() {
        // Arrange
        UseCaseElementRequest request = new UseCaseElementRequest.Builder().
                getDefault().
                setDataId(DATA_ID).
                build();
        // Act
        SUT.execute(request, new DummyUseCaseCallback());
        // Assert
        assertTrue(SUT.isLoadingDataByDataId);
    }

    @Test
    public void emptyUseCase_hasDataIdHasDomainId_loadDataByDataId() {
        // Arrange
        UseCaseElementRequest request = new UseCaseElementRequest.Builder().
                getDefault().
                setDataId(DATA_ID).
                setDomainId(DOMAIN_ID).build();
        // Act
        SUT.execute(request, new DummyUseCaseCallback());
        // Assert
        assertTrue(SUT.isLoadingDataByDataId);
    }
    // endregion empty use case tests --------------------------------------------------------------

    // region loaded use case tests ----------------------------------------------------------------

    @Test
    public void loadedUseCase_noDataIdNoDomainId_processUseCaseData() {
        // Arrange
        emptyUseCase_hasDataIdHasDomainId_loadDataByDataId();

        UseCaseElementRequest request = new UseCaseElementRequest.Builder().getDefault().build();
        // Act
        SUT.execute(request, new DummyUseCaseCallback());
        // Assert
        assertTrue(SUT.isProcessingUseCaseData);
    }

    @Test
    public void loadedUseCase_noDataIdHasDomainIdEqualToUseCaseId_processRequestDomainModel() {
        // Arrange
        emptyUseCase_hasDataIdHasDomainId_loadDataByDataId();

        UseCaseElementRequest request = new UseCaseElementRequest.Builder().
                getDefault().
                setDomainId(DOMAIN_ID).
                build();
        // Act
        SUT.execute(request, new DummyUseCaseCallback());
        // Assert
        assertTrue(SUT.isProcessingRequestDomainData);
    }

    @Test
    public void loadedUseCase_noDataIdHasDomainIdNotEqualToUseCaseId_loadDataByDomainId() {
        // Arrange
        emptyUseCase_hasDataIdHasDomainId_loadDataByDataId();

        UseCaseElementRequest request = new UseCaseElementRequest.Builder().
                getDefault().
                setDomainId(DIFFERENT_DOMAIN_ID).
                build();
        // Act
        SUT.execute(request, new DummyUseCaseCallback());
        // Assert
        assertTrue(SUT.isLoadingDataByDomainId);
    }

    @Test
    public void loadedUseCase_hasDataIdEqualToUseCaseDataIdNoDomainId_processRequestDomainModel() {
        // Arrange
        emptyUseCase_hasDataIdHasDomainId_loadDataByDataId();

        UseCaseElementRequest request = new UseCaseElementRequest.Builder().
                getDefault().
                setDataId(DATA_ID).
                build();
        // Act
        SUT.execute(request, new DummyUseCaseCallback());
        // Assert
        assertTrue(SUT.isProcessingRequestDomainData);
    }

    @Test
    public void loadedUseCase_hasDataIdNotEqualToUseCaseDataIdNoDomainId_loadDataByDataId() {
        // Arrange
        emptyUseCase_hasDataIdHasDomainId_loadDataByDataId();

        UseCaseElementRequest request = new UseCaseElementRequest.Builder().
                getDefault().
                setDataId(DIFFERENT_DATA_ID).
                build();
        // Act
        SUT.execute(request, new DummyUseCaseCallback());
        // Assert
        assertTrue(SUT.isLoadingDataByDataId);
    }

    @Test
    public void loadedUseCase_hasDataIdEqualToUseCaseDataIdHasDomainIdEqualToUseCaseDomainId_processRequestDomainModel() {
        // Arrange
        emptyUseCase_hasDataIdHasDomainId_loadDataByDataId();

        UseCaseElementRequest request = new UseCaseElementRequest.Builder().
                getDefault().
                setDataId(DATA_ID).
                setDomainId(DOMAIN_ID).
                build();
        // Act
        SUT.execute(request, new DummyUseCaseCallback());
        // Assert
        assertTrue(SUT.isProcessingRequestDomainData);
    }

    @Test
    public void loadedUseCase_hasDataIdNotEqualToUseCaseDataIdHasDomainIdEqualToUseCaseDomainId_loadDataByDataId() {
        // Arrange
        emptyUseCase_hasDataIdHasDomainId_loadDataByDataId();

        UseCaseElementRequest request = new UseCaseElementRequest.Builder().
                getDefault().
                setDataId(DIFFERENT_DATA_ID).
                setDomainId(DOMAIN_ID).
                build();
        // Act
        SUT.execute(request, new DummyUseCaseCallback());
        // Assert
        assertTrue(SUT.isLoadingDataByDataId);
    }
    // endregion loaded use case tests -------------------------------------------------------------

    // region helper methods -----------------------------------------------------------------------
    // endregion helper methods --------------------------------------------------------------------

    // region helper classes -----------------------------------------------------------------------
    // Helper class to assist in testing abstract UseCaseElement
    private static class UseCaseElementInheritor
            extends
            UseCaseElement<UseCaseElementRequest.DomainModel> {

        private static final String TAG = "tkm-" + UseCaseElementInheritor.class.
                getSimpleName() + ": ";

        private boolean isLoadingDataByDataId;
        private boolean isLoadingDataByDomainId;
        private boolean isProcessingUseCaseData;
        private boolean isProcessingRequestDomainData;

        public UseCaseElementInheritor() {
            useCaseDomainModel = new UseCaseElementRequest.DomainModel() {
            };
        }

        @Override
        protected void loadDataByDataId() {
            System.out.println(TAG + "loading data by dataId");
            isLoadingDataByDataId = true;
        }

        @Override
        protected void loadDataByDomainId() {
            System.out.println(TAG + "loading data by domain Id");
            isLoadingDataByDomainId = true;
        }

        @Override
        protected void processUseCaseDomainData() {
            System.out.println(TAG + "processing use case data");
            isProcessingUseCaseData = true;
            isDomainDataChanged();
        }

        @Override
        protected void processRequestDomainData() {
            System.out.println(TAG + "processing request domain model");
            isProcessingRequestDomainData = true;
        }

        @Override
        protected boolean isDomainDataChanged() {
            System.out.println(TAG + "requestDomainModel=" + requestDomainModel +
                    " useCaseDomainModel=" + useCaseDomainModel);
            return false;
        }
    }

    // Helper request class
    public static class UseCaseElementRequest
            extends
            UseCaseMessageModelDataId<UseCaseElementRequest.DomainModel>
            implements UseCaseBase.Request {

        private UseCaseElementRequest() {
        }

        @Nonnull
        @Override
        public String toString() {
            return "UseCaseElementRequest{" +
                    "dataId='" + dataId + '\'' +
                    ", domainId='" + domainId + '\'' +
                    ", model=" + model +
                    '}';
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