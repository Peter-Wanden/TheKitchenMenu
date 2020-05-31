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
    private static final String TAG = "tkm-" + UseCaseElementRequestFilteringTest.class.
            getSimpleName() + ": ";
    private static final String DATA_ID = "DATA_ID";
    private static final String DOMAIN_ID = "DOMAIN_ID";
    private static final String DIFFERENT_DATA_ID = "DIFFERENT_DATA_ID";
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
        // equality if Id's
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

    private UseCaseElementInheritor givenUseCase() {
        return new UseCaseElementInheritor();
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
            useCaseDomainModel = new BaseDomainModel(){};
        }

        @Override
        protected void loadDataByDataId() {
            System.out.println(UseCaseElementRequestFilteringTest.TAG + TAG +
                    "loadingDataByDataId");
            isLoadingDataByDataId = true;
        }

        @Override
        protected void loadDataByDomainId() {
            System.out.println(UseCaseElementRequestFilteringTest.TAG + TAG +
                    "loading data by domain Id");
            isLoadingDataByDomainId = true;
        }

        @Override
        protected void processDomainModelChanges() {
            System.out.println(UseCaseElementRequestFilteringTest.TAG + TAG +
                    "processing domain model changes");
            isProcessingDomainModelChanges = true;

            System.out.println(TAG + "use case domain Id and request domain Id are equal");
            if (useCaseDomainModel.equals(requestDomainModel)) {
                System.out.println(TAG + "use case domain model and request domain model are equal");
                buildResponse();
            } else {
                System.out.println(TAG + "use case domain model and request domain model are " +
                        "different");
                processDomainModelChanges();
            }
        }

        @Override
        protected void buildResponse() {
            System.out.println(UseCaseElementRequestFilteringTest.TAG + TAG +
                    "building response");
            isBuildingResponse = true;
        }
    }
    
    // Helper request class
    private static class UseCaseElementRequest 
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

        private UseCaseElementRequest() {}
        
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

    // We will not be receiving a response, but we need a callback, so create a Dummy callback class.
    private static class DummyUseCaseCallback implements UseCaseBase.Callback {
        @Override
        public void onUseCaseSuccess(Object o) {

        }

        @Override
        public void onUseCaseError(Object o) {

        }
    }
    // endregion helper classes --------------------------------------------------------------------

}