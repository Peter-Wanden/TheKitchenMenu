package com.example.peter.thekitchenmenu.domain.usecase.common;

import com.example.peter.thekitchenmenu.data.repository.DomainDataAccess;
import com.example.peter.thekitchenmenu.data.repository.Repository;
import com.example.peter.thekitchenmenu.data.repository.recipe.RepositoryRecipeMetadata;
import com.example.peter.thekitchenmenu.domain.model.BaseDomainModel;
import com.example.peter.thekitchenmenu.domain.model.BasePersistenceModel;
import com.example.peter.thekitchenmenu.domain.model.DomainModelBuilder;
import com.example.peter.thekitchenmenu.domain.usecase.common.usecasemessage.UseCaseMessageModelDataId;
import com.example.peter.thekitchenmenu.domain.usecase.recipe.component.metadata.RecipeMetadataPersistenceModel;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Objects;

import javax.annotation.Nonnull;

import static org.junit.Assert.*;

public class UseCaseElementTest {
    // region constants ----------------------------------------------------------------------------
    private static final String DATA_ID = "DATA_ID";
    private static final String DIFFERENT_DATA_ID = "DIFFERENT_DATA_ID";
    private static final String DOMAIN_ID = "DOMAIN_ID";
    private static final String DIFFERENT_DOMAIN_ID = "DIFFERENT_DOMAIN_ID";
    // endregion constants -------------------------------------------------------------------------

    // region helper fields ------------------------------------------------------------------------
    @Mock
    RepositoryRecipeMetadata repoMock;
    @Captor
    ArgumentCaptor<DomainDataAccess<RecipeMetadataPersistenceModel>> repoCallbackCaptor;
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
    public void emptyUseCase_noDataIdNoDomainId_isReprocessCurrentDomainModel() {
        // Arrange
        // A default 'empty' request returns the current state of the use case. Works for all use
        // cases
        UseCaseElementRequest request = new UseCaseElementRequest.Builder().getDefault().build();
        // Act
        SUT.execute(request, new DummyUseCaseCallback());
        // Assert
        assertTrue(SUT.isReprocessCurrentDomainModel);
    }

    @Test
    public void emptyUseCase_noDataIdHasDomainId_isLoadDomainModelByDomainId() {
        // Arrange
        UseCaseElementRequest request = new UseCaseElementRequest.Builder().
                getDefault().
                setDomainId(DOMAIN_ID).
                build();
        // Act
        SUT.execute(request, new DummyUseCaseCallback());
        // Assert
        assertTrue(SUT.isLoadDomainModelByDomainId);
    }

    @Test
    public void emptyUseCase_hasDataIdNoDomainId_isLoadDomainModelByDataId() {
        // Arrange
        UseCaseElementRequest request = new UseCaseElementRequest.Builder().
                getDefault().
                setDataId(DATA_ID).
                build();
        // Act
        SUT.execute(request, new DummyUseCaseCallback());
        // Assert
        assertTrue(SUT.isLoadDomainModelByDataId);
    }

    @Test
    public void emptyUseCase_hasDataIdHasDomainId_isLoadDomainModelByDataId() {
        // Arrange
        UseCaseElementRequest request = new UseCaseElementRequest.Builder().
                getDefault().
                setDataId(DATA_ID).
                setDomainId(DOMAIN_ID).build();
        // Act
        SUT.execute(request, new DummyUseCaseCallback());
        // Assert
        assertTrue(SUT.isLoadDomainModelByDataId);
    }
    // endregion empty use case tests --------------------------------------------------------------

    // region loaded use case tests ----------------------------------------------------------------
    @Test
    public void loadedUseCase_noDataIdNoDomainId_isReprocessCurrentDomainModel() {
        // Arrange
        emptyUseCase_hasDataIdHasDomainId_isLoadDomainModelByDataId();

        UseCaseElementRequest request = new UseCaseElementRequest.Builder().getDefault().build();
        // Act
        SUT.execute(request, new DummyUseCaseCallback());
        // Assert
        assertTrue(SUT.isReprocessCurrentDomainModel);
    }

    @Test
    public void loadedUseCase_noDataIdHasDomainIdEqualToUseCaseId_isProcessRequestDomainModel() {
        // Arrange
        emptyUseCase_hasDataIdHasDomainId_isLoadDomainModelByDataId();

        UseCaseElementRequest request = new UseCaseElementRequest.Builder().
                getDefault().
                setDomainId(DOMAIN_ID).
                build();
        // Act
        SUT.execute(request, new DummyUseCaseCallback());
        // Assert
        assertTrue(SUT.isProcessRequestDomainModel);
    }

    @Test
    public void loadedUseCase_noDataIdHasDomainIdNotEqualToUseCaseId_isLoadDomainModelByDomainId() {
        // Arrange
        emptyUseCase_hasDataIdHasDomainId_isLoadDomainModelByDataId();

        UseCaseElementRequest request = new UseCaseElementRequest.Builder().
                getDefault().
                setDomainId(DIFFERENT_DOMAIN_ID).
                build();
        // Act
        SUT.execute(request, new DummyUseCaseCallback());
        // Assert
        assertTrue(SUT.isLoadDomainModelByDomainId);
    }

    @Test
    public void loadedUseCase_hasDataIdEqualToUseCaseDataIdNoDomainId_isProcessRequestDomainModel() {
        // Arrange
        emptyUseCase_hasDataIdHasDomainId_isLoadDomainModelByDataId();

        UseCaseElementRequest request = new UseCaseElementRequest.Builder().
                getDefault().
                setDataId(DATA_ID).
                build();
        // Act
        SUT.execute(request, new DummyUseCaseCallback());
        // Assert
        assertTrue(SUT.isProcessRequestDomainModel);
    }

    @Test
    public void loadedUseCase_hasDataIdNotEqualToUseCaseDataIdNoDomainId_isLoadDomainModelByDataId() {
        // Arrange
        emptyUseCase_hasDataIdHasDomainId_isLoadDomainModelByDataId();

        UseCaseElementRequest request = new UseCaseElementRequest.Builder().
                getDefault().
                setDataId(DIFFERENT_DATA_ID).
                build();
        // Act
        SUT.execute(request, new DummyUseCaseCallback());
        // Assert
        assertTrue(SUT.isLoadDomainModelByDataId);
    }

    @Test
    public void loadedUseCase_hasDataIdEqualToUseCaseDataIdHasDomainIdEqualToUseCaseDomainId_UseCaseDomainDataSourceREQUEST() {
        // Arrange
        emptyUseCase_hasDataIdHasDomainId_isLoadDomainModelByDataId();

        UseCaseElementRequest request = new UseCaseElementRequest.Builder().
                getDefault().
                setDataId(DATA_ID).
                setDomainId(DOMAIN_ID).
                build();
        // Act
        SUT.execute(request, new DummyUseCaseCallback());
        // Assert
        assertTrue(SUT.isProcessRequestDomainModel);
    }

    @Test
    public void loadedUseCase_hasDataIdNotEqualToUseCaseDataIdHasDomainIdEqualToUseCaseDomainId_isLoadDomainModelByDataId() {
        // Arrange
        emptyUseCase_hasDataIdHasDomainId_isLoadDomainModelByDataId();

        UseCaseElementRequest request = new UseCaseElementRequest.Builder().
                getDefault().
                setDataId(DIFFERENT_DATA_ID).
                setDomainId(DOMAIN_ID).
                build();
        // Act
        SUT.execute(request, new DummyUseCaseCallback());
        // Assert
        assertTrue(SUT.isLoadDomainModelByDataId);
    }
    // endregion loaded use case tests -------------------------------------------------------------

    // region helper methods -----------------------------------------------------------------------
    // endregion helper methods --------------------------------------------------------------------

    // region helper classes -----------------------------------------------------------------------
    // Helper class to assist in testing abstract UseCaseElement
    public static class UseCaseElementInheritor
            extends
            UseCaseElement<
                    Repository<TestPersistenceModel>,
                    TestPersistenceModel,
                    UseCaseElementInheritor.DomainModel>{

        private static final String TAG = "tkm-" + UseCaseElementInheritor.class.
                getSimpleName() + ": ";

        static final class DomainModel implements com.example.peter.thekitchenmenu.domain.model.DomainModel {
            String comparisonString;
        }

        private boolean isLoadDomainModelByDataId;
        private boolean isLoadDomainModelByDomainId;
        private boolean isReprocessCurrentDomainModel;
        private boolean isProcessRequestDomainModel;

        public UseCaseElementInheritor() {
        }

        @Override
        protected void getPersistenceModelByDataId() {
            isLoadDomainModelByDataId = true;
            System.out.println(TAG + "loading data by dataId");
        }

        @Override
        protected void getPersistenceModelByDomainId() {
            isLoadDomainModelByDomainId = true;
            System.out.println(TAG + "loading data by domain Id");
        }

//        @Override
//        protected void reprocessDomainModel() {
//            isReprocessCurrentDomainModel = true;
//            System.out.println(TAG + "processing use case data");
//        }


        @Override
        protected DomainModel createUseCaseModelFromDefaultValues() {
            return new DomainModel();
        }

        @Override
        protected void setupForRequestModelProcessing() {
            isProcessRequestDomainModel = true;
            System.out.println(TAG + "processing request domain model");
        }

        @Override
        public void onPersistenceModelLoaded(TestPersistenceModel model) {

        }

        @Override
        public void onPersistenceModelUnavailable() {

        }

        @Override
        protected DomainModel createUseCaseModelFromRequestModel() {
            return new DomainModel();
        }

        @Override
        protected DomainModel createUseCaseModelFromPersistenceModel(@Nonnull TestPersistenceModel persistenceModel) {
            return new DomainModel();
        }

        @Override
        protected void initialiseUseCase() {

        }

        @Override
        protected void validateDomainModelElements() {

        }

        @Override
        protected void save() {

        }

        @Override
        protected void archivePreviousState(long currentTime) {

        }

        @Override
        protected void buildResponse() {

        }
    }

    // Helper request class
    public static class UseCaseElementRequest
            extends
            UseCaseMessageModelDataId<UseCaseElementRequest.RequestDomainModel>
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
                UseCaseMessageModelDataIdBuilder
                        <Builder, UseCaseElementRequest, RequestDomainModel> {

            public Builder() {
                message = new UseCaseElementRequest();
            }

            @Override
            public Builder getDefault() {
                message.dataId = "";
                message.domainId = "";
                message.model = new RequestDomainModel.Builder().getDefault().build();
                return self();
            }

            @Override
            protected Builder self() {
                return this;
            }
        }

        private static class RequestDomainModel
                extends
                BaseDomainModel {

            // For comparison purposes
            private String comparisonString;

            @Override
            public boolean equals(Object o) {
                if (this == o) return true;
                if (o == null || getClass() != o.getClass()) return false;
                RequestModel that = (RequestModel) o;
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
                    DomainModelBuilder<Builder, RequestModel> {

                public Builder() {
                    domainModel = new RequestModel();
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
    private static class DummyUseCaseCallback
            implements
            UseCaseBase.Callback<UseCaseBase.Response> {
        @Override
        public void onUseCaseSuccess(UseCaseBase.Response response) {

        }

        @Override
        public void onUseCaseError(UseCaseBase.Response response) {

        }
    }

    private static class TestPersistenceModel
            extends
            BasePersistenceModel {
    }
    // endregion helper classes --------------------------------------------------------------------
}