package com.example.peter.thekitchenmenu.data.repository.source.local.recipe.identity.dataadapter;

import com.example.peter.thekitchenmenu.data.repository.recipe.identity.TestDataRecipeIdentity;
import com.example.peter.thekitchenmenu.data.repository.recipe.metadata.TestDataRecipeMetadata;
import com.example.peter.thekitchenmenu.data.repository.source.local.recipe.identity.datasource.RecipeIdentityEntity;
import com.example.peter.thekitchenmenu.data.repository.source.local.recipe.identity.datasource.RecipeIdentityLocalDataSource;
import com.example.peter.thekitchenmenu.data.repository.source.local.recipe.identity.datasource.TestDataRecipeIdentityEntity;
import com.example.peter.thekitchenmenu.domain.usecase.recipe.component.identity.RecipeIdentityPersistenceModel;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;

import static com.example.peter.thekitchenmenu.data.repository.DomainDataAccess.*;
import static com.example.peter.thekitchenmenu.data.repository.source.local.dataadapter.PrimitiveDataSource.*;
import static org.junit.Assert.*;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.verify;

public class IdentityLocalGetAdapterTest {

    private static final String TAG = "tkm-" + IdentityLocalGetAdapterTest.class.getSimpleName() +
            ": ";

    // region constants ----------------------------------------------------------------------------
    // endregion constants -------------------------------------------------------------------------

    // region helper fields ------------------------------------------------------------------------
    @Mock
    RecipeIdentityLocalDataSource repoMock;
    @Captor
    ArgumentCaptor<GetPrimitiveCallback<RecipeIdentityEntity>> repoCallback;
    @Captor
    ArgumentCaptor<GetAllPrimitiveCallback<RecipeIdentityEntity>> repoGetAllCallback;

    private GetDomainModelCallbackClient callbackClient;
    private GetAllDomainModelsCallbackClient getAllCallbackClient;

    // endregion helper fields ---------------------------------------------------------------------

    private IdentityLocalGetAdapter SUT;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        callbackClient = new GetDomainModelCallbackClient();
        getAllCallbackClient = new GetAllDomainModelsCallbackClient();

        SUT = givenSystemUnderTest();
    }

    private IdentityLocalGetAdapter givenSystemUnderTest() {
        return new IdentityLocalGetAdapter(
                repoMock
        );
    }

    @Test
    public void getByDataId_MODELS_UNAVAILABLE() {
        // Arrange
        String dataId = "dataIdNotInTestData";
        // Act
        SUT.getByDataId(dataId, callbackClient);
        // Assert
        verify(repoMock).getByDataId(eq(dataId), repoCallback.capture());
        repoCallback.getValue().onDataUnavailable();

        assertTrue(
                callbackClient.isModelUnavailable
        );
    }

    @Test
    public void getByDataId_domainModelReturned() {
        // Arrange
        RecipeIdentityPersistenceModel modelUnderTest = TestDataRecipeIdentity.
                getValidExistingTitleValidDescriptionValid();
        // Act
        SUT.getByDataId(modelUnderTest.getDataId(), callbackClient);
        // Assert
        verify(repoMock).getByDataId(eq(modelUnderTest.getDataId()), repoCallback.capture()
        );
        repoCallback.getValue().onEntityLoaded(TestDataRecipeIdentityEntity.
                getValidExistingTitleValidDescriptionValid()
        );
        assertEquals(
                modelUnderTest,
                callbackClient.model
        );
    }

    @Test
    public void getAllByDomainId_MODELS_UNAVAILABLE() {
        // Arrange
        String domainId = "domainIdNotInTestData";
        // Act
        SUT.getAllByDomainId(domainId, getAllCallbackClient);
        // Assert
        verify(repoMock).getAllByDomainId(eq(domainId), repoGetAllCallback.capture());
        repoGetAllCallback.getValue().onDataUnavailable();

        assertTrue(
                getAllCallbackClient.isModelsUnavailable
        );
    }

    @Test
    public void getAllByDomainId_returnAllModelsForDomainId() {
        // Arrange
        String domainId = TestDataRecipeMetadata.getInvalidDefault().getDomainId();
        List<RecipeIdentityPersistenceModel> models = TestDataRecipeIdentity.
                getAllByDomainId(domainId);
        // Act
        SUT.getAllByDomainId(domainId, getAllCallbackClient);
        // Assert
        verify(repoMock).getAllByDomainId(eq(domainId), repoGetAllCallback.capture()
        );
        repoGetAllCallback.getValue().onAllLoaded(TestDataRecipeIdentityEntity.
                getAllByDomainId(domainId)
        );
        assertEquals(
                models,
                getAllCallbackClient.models
        );
    }

    @Test
    public void getActiveByDomainId_MODEL_UNAVAILABLE() {
        // Arrange
        String domainId = "domainIdNotInTestData";
        // Act
        SUT.getActiveByDomainId(domainId, callbackClient);
        // Assert
        verify(repoMock).getAllByDomainId(
                eq(domainId),
                repoGetAllCallback.capture()
        );
        repoGetAllCallback.getValue().onDataUnavailable();

        assertTrue(
                callbackClient.isModelUnavailable
        );
    }

    @Test
    public void getActiveByDomainId_returnMostRecentModel() {
        // Arrange
        long lastUpdate = 0L;
        RecipeIdentityPersistenceModel modelUnderTest = new RecipeIdentityPersistenceModel.Builder().
                getDefault().build();
        for (RecipeIdentityPersistenceModel m : TestDataRecipeIdentity.getAllNew()) {
            if (m.getLastUpdate() > lastUpdate) {
                modelUnderTest = m;
                lastUpdate = m.getLastUpdate();
            }
        }
        // Act
        SUT.getActiveByDomainId(modelUnderTest.getDomainId(), callbackClient);
        // Assert database called, return list of entities
        verify(repoMock).getAllByDomainId(eq(modelUnderTest.getDomainId()),
                repoGetAllCallback.capture()
        );
        repoGetAllCallback.getValue().onAllLoaded(TestDataRecipeIdentityEntity.getAllNew()
        );
        // Assert correct entity converted to model and returned
        assertEquals(
                modelUnderTest,
                callbackClient.model
        );
    }

    @Test
    public void getAll_MODELS_UNAVAILABLE() {
        // Arrange
        // Act
        SUT.getAll(getAllCallbackClient);
        // Assert
        verify(repoMock).getAll(repoGetAllCallback.capture());
        repoGetAllCallback.getValue().onDataUnavailable();

        assertTrue(
                getAllCallbackClient.isModelsUnavailable
        );
    }

    @Test
    public void getAll_returnAllModels() {
        // Arrange
        List<RecipeIdentityPersistenceModel> modelsUnderTest = TestDataRecipeIdentity.getAll();
        // Act
        SUT.getAll(getAllCallbackClient);
        // Assert
        verify(repoMock).getAll(repoGetAllCallback.capture());
        repoGetAllCallback.getValue().onAllLoaded(TestDataRecipeIdentityEntity.getAll());

        assertEquals(
                modelsUnderTest,
                getAllCallbackClient.models
        );
    }

    // region helper methods -----------------------------------------------------------------------
    // endregion helper methods --------------------------------------------------------------------

    // region helper classes -----------------------------------------------------------------------
    private static class GetDomainModelCallbackClient
            implements GetDomainModelCallback<RecipeIdentityPersistenceModel> {

        private static final String TAG = IdentityLocalGetAdapterTest.TAG +
                GetDomainModelCallbackClient.class.getSimpleName() + ": ";

        private RecipeIdentityPersistenceModel model;
        private boolean isModelUnavailable;

        @Override
        public void onPersistenceModelLoaded(RecipeIdentityPersistenceModel m) {
            System.out.println(TAG + model);
            model = m;
        }

        @Override
        public void onPersistenceModelUnavailable() {
            isModelUnavailable = true;
            System.out.println(TAG + "isModelUnavailable=" + isModelUnavailable);
        }
    }

    private static class GetAllDomainModelsCallbackClient
            implements GetAllDomainModelsCallback<RecipeIdentityPersistenceModel> {

        private static final String TAG = IdentityLocalGetAdapterTest.TAG +
                GetAllDomainModelsCallbackClient.class.getSimpleName() + ": ";

        private List<RecipeIdentityPersistenceModel> models;
        private boolean isModelsUnavailable;

        @Override
        public void onAllDomainModelsLoaded(List<RecipeIdentityPersistenceModel> m) {
            System.out.println(TAG + m);
            models = m;
        }

        @Override
        public void onDomainModelsUnavailable() {
            isModelsUnavailable = true;
            System.out.println(TAG + "isModelUnavailable=" + isModelsUnavailable);
        }
    }
    // endregion helper classes --------------------------------------------------------------------
}