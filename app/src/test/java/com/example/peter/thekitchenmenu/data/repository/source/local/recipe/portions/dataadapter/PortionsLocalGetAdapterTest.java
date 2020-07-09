package com.example.peter.thekitchenmenu.data.repository.source.local.recipe.portions.dataadapter;

import com.example.peter.thekitchenmenu.data.repository.DomainDataAccess.GetAllDomainModelsCallback;
import com.example.peter.thekitchenmenu.data.repository.DomainDataAccess.GetDomainModelCallback;
import com.example.peter.thekitchenmenu.data.repository.recipe.metadata.TestDataRecipeMetadata;
import com.example.peter.thekitchenmenu.data.repository.recipe.portions.TestDataRecipePortions;
import com.example.peter.thekitchenmenu.data.repository.source.local.dataadapter.PrimitiveDataSource.GetAllPrimitiveCallback;
import com.example.peter.thekitchenmenu.data.repository.source.local.dataadapter.PrimitiveDataSource.GetPrimitiveCallback;
import com.example.peter.thekitchenmenu.data.repository.source.local.recipe.portions.datasource.RecipePortionsEntity;
import com.example.peter.thekitchenmenu.data.repository.source.local.recipe.portions.datasource.RecipePortionsLocalDataSource;
import com.example.peter.thekitchenmenu.data.repository.source.local.recipe.portions.datasource.TestDataRecipePortionsEntity;
import com.example.peter.thekitchenmenu.domain.usecase.recipe.component.portions.RecipePortionsPersistenceModel;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.verify;

public class PortionsLocalGetAdapterTest {

    private static final String TAG = "tkm-" + PortionsLocalGetAdapterTest.class.getSimpleName() +
            ": ";

    // region constants ----------------------------------------------------------------------------
    // endregion constants -------------------------------------------------------------------------

    // region helper fields ------------------------------------------------------------------------
    @Mock
    RecipePortionsLocalDataSource repoMock;
    @Captor
    ArgumentCaptor<GetPrimitiveCallback<RecipePortionsEntity>> repoCallbackMock;
    @Captor
    ArgumentCaptor<GetAllPrimitiveCallback<RecipePortionsEntity>> repoGetAllCallback;

    private GetDomainModelCallbackClient callbackClient;
    private GetAllDomainModelsCallbackClient getAllCallbackClient;
    // endregion helper fields ---------------------------------------------------------------------

    private PortionsLocalGetAdapter SUT;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        callbackClient = new GetDomainModelCallbackClient();
        getAllCallbackClient = new GetAllDomainModelsCallbackClient();

        SUT = givenSystemUnderTest();
    }

    private PortionsLocalGetAdapter givenSystemUnderTest() {
        return new PortionsLocalGetAdapter(
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
        verify(repoMock).getByDataId(eq(dataId), repoCallbackMock.capture());
        repoCallbackMock.getValue().onDataUnavailable();

        assertTrue(
                callbackClient.isModelUnavailable
        );
    }

    @Test
    public void getByDataId_domainModelReturned() {
        // Arrange
        RecipePortionsPersistenceModel modelUnderTest = TestDataRecipePortions.
                getExistingValidNinePortions();
        // Act
        SUT.getByDataId(modelUnderTest.getDataId(), callbackClient);
        // Assert
        verify(repoMock).getByDataId(eq(modelUnderTest.getDataId()), repoCallbackMock.capture());
        repoCallbackMock.getValue().onEntityLoaded(TestDataRecipePortionsEntity.
                getExistingValidNinePortions()
        );

        assertEquals(
                modelUnderTest,
                callbackClient.model
        );
    }

    @Test
    public void getAllByDomainId_MODELS_UNAVAILABLE() {
        // Arrange
        String domainId = "idNotInTestData";
        // Act
        SUT.getAllByDomainId(domainId, getAllCallbackClient);
        // Assert
        verify(repoMock).getAllByDomainId(eq(domainId), repoGetAllCallback.capture());
        repoGetAllCallback.getValue().onDataUnavailable();

        assertTrue(
                getAllCallbackClient.isModelUnavailable
        );
    }

    @Test
    public void getAllByDomainId_returnAllModelsForDomainId() {
        // Arrange
        String domainId = TestDataRecipeMetadata.getInvalidDefault().getDomainId();
        List<RecipePortionsPersistenceModel> models = TestDataRecipePortions.
                getAllByDomainId(domainId);
        // Act
        SUT.getAllByDomainId(domainId, getAllCallbackClient);
        // Assert
        verify(repoMock).getAllByDomainId(eq(domainId), repoGetAllCallback.capture()
        );
        repoGetAllCallback.getValue().onAllLoaded(TestDataRecipePortionsEntity.
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
        String domainId = "idNotInTestData";
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
        RecipePortionsPersistenceModel modelUnderTest = new RecipePortionsPersistenceModel.Builder().
                getDefault().build();
        for (RecipePortionsPersistenceModel m : TestDataRecipePortions.getAllNew()) {
            if (m.getLastUpdate() > lastUpdate) {
                modelUnderTest = m;
                lastUpdate = m.getLastUpdate();
            }
        }
        // Act
        SUT.getActiveByDomainId(modelUnderTest.getDomainId(), callbackClient);
        // Assert
        verify(repoMock).getAllByDomainId(
                eq(modelUnderTest.getDomainId()),
                repoGetAllCallback.capture()
        );
        repoGetAllCallback.getValue().onAllLoaded(TestDataRecipePortionsEntity.
                getAllByDomainId(modelUnderTest.getDomainId())
        );
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
                getAllCallbackClient.isModelUnavailable
        );
    }

    @Test
    public void getAll_returnAllModels() {
        // Arrange
        List<RecipePortionsPersistenceModel> modelsUnderTest = TestDataRecipePortions.getAll();
        // Act
        SUT.getAll(getAllCallbackClient);
        // Assert
        verify(repoMock).getAll(repoGetAllCallback.capture());
        repoGetAllCallback.getValue().onAllLoaded(TestDataRecipePortionsEntity.getAll());

        assertEquals(
                modelsUnderTest,
                getAllCallbackClient.models
        );
    }

    // region helper methods -----------------------------------------------------------------------
    // endregion helper methods --------------------------------------------------------------------

    // region helper classes -----------------------------------------------------------------------
    private static class GetDomainModelCallbackClient
            implements GetDomainModelCallback<RecipePortionsPersistenceModel> {

        private static final String TAG = PortionsLocalGetAdapterTest.TAG +
                GetDomainModelCallbackClient.class.getSimpleName() + ": ";

        private RecipePortionsPersistenceModel model;
        private boolean isModelUnavailable;

        @Override
        public void onPersistenceModelLoaded(RecipePortionsPersistenceModel m) {
            System.out.println(TAG + m);
            model = m;
        }

        @Override
        public void onPersistenceModelUnavailable() {
            isModelUnavailable = true;
            System.out.println(TAG + "isModelUnavailable=" + isModelUnavailable);
        }
    }

    private static class GetAllDomainModelsCallbackClient
            implements GetAllDomainModelsCallback<RecipePortionsPersistenceModel> {

        private static final String TAG = PortionsLocalGetAdapterTest.TAG +
                GetAllDomainModelsCallbackClient.class.getSimpleName() + ": ";

        private List<RecipePortionsPersistenceModel> models;
        private boolean isModelUnavailable;

        @Override
        public void onAllDomainModelsLoaded(List<RecipePortionsPersistenceModel> m) {
            System.out.println(TAG + "onAllLoaded: " + m);
            models = m;
        }

        @Override
        public void onDomainModelsUnavailable() {
            isModelUnavailable = true;
            System.out.println(TAG + "isModelUnavailable" + isModelUnavailable);
        }
    }
    // endregion helper classes --------------------------------------------------------------------
}