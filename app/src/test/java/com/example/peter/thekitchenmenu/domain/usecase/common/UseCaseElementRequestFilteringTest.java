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
    public void requestHasDataId_useCaseHasNoDataId_loadDataByDataId() {
        UseCaseElementRequest request = new UseCaseElementRequest.Builder().
                getDefault().
                setDataId(DATA_ID).
                build();
    }

    private UseCaseElementInheritor givenUseCase() {
        return new UseCaseElementInheritor();
    }

    // region helper methods -----------------------------------------------------------------------
    // endregion helper methods --------------------------------------------------------------------

    // region helper classes -----------------------------------------------------------------------
    // Helper class to assist in testing abstract UseCaseElement
    private static class UseCaseElementInheritor extends UseCaseElement {

        boolean isLoadDataByDataId;

        @Override
        protected <REQUEST extends Request> void execute(REQUEST request) {
            UseCaseElementRequest r = (UseCaseElementRequest) request;            
        }

        @Override
        protected void loadDataByDataId(String dataId) {
            isLoadDataByDataId = true;
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
    // endregion helper classes --------------------------------------------------------------------

}