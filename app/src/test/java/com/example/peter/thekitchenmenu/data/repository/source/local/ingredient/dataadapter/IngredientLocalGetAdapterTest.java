package com.example.peter.thekitchenmenu.data.repository.source.local.ingredient.dataadapter;

import com.example.peter.thekitchenmenu.data.repository.DomainDataAccess;
import com.example.peter.thekitchenmenu.data.repository.ingredient.TestDataIngredient;
import com.example.peter.thekitchenmenu.data.repository.source.local.dataadapter.PrimitiveDataSource.GetAllPrimitiveCallback;
import com.example.peter.thekitchenmenu.data.repository.source.local.dataadapter.PrimitiveDataSource.GetPrimitiveCallback;
import com.example.peter.thekitchenmenu.data.repository.source.local.ingredient.datasource.IngredientEntity;
import com.example.peter.thekitchenmenu.data.repository.source.local.ingredient.datasource.IngredientLocalDataSource;
import com.example.peter.thekitchenmenu.domain.usecase.ingredient.IngredientPersistenceModel;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;

import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.verify;

public class IngredientLocalGetAdapterTest {

    private static final String TAG = "tkm-" + IngredientLocalGetAdapterTest.class.getSimpleName()
            + ": ";

    // region constants ----------------------------------------------------------------------------
    // endregion constants -------------------------------------------------------------------------

    // region helper fields ------------------------------------------------------------------------
    @Mock
    IngredientLocalDataSource repoMock;
    @Captor
    ArgumentCaptor<GetPrimitiveCallback<IngredientEntity>> repoCallback;
    @Captor
    ArgumentCaptor<GetAllPrimitiveCallback<IngredientEntity>> repoGetAllCallback;

    private GetDomainModelCallbackClient callbackClient;
    private GetAllDomainModelsCallbackClient getAllCallbackClient;
    // endregion helper fields ---------------------------------------------------------------------

    private IngredientLocalGetAdapter SUT;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        callbackClient = new GetDomainModelCallbackClient();
        getAllCallbackClient = new GetAllDomainModelsCallbackClient();

        SUT = givenSystemUnderTest();
    }

    private IngredientLocalGetAdapter givenSystemUnderTest() {
        return new IngredientLocalGetAdapter(
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
        IngredientPersistenceModel modelUnderTest = TestDataIngredient.
                getValidExistingNameValidDescriptionValid();

        // Act
        // Assert
    }

    @Test
    public void getAllByDomainId_MODELS_UNAVAILABLE() {
        // Arrange
        // Act
        // Assert
    }

    @Test
    public void getAllByDomainId_returnAllModelsForDomainId() {
        // Arrange
        // Act
        // Assert
    }

    @Test
    public void getActiveByDomainId_MODEL_UNAVAILABLE() {
        // Arrange
        // Act
        // Assert
    }

    @Test
    public void getActiveByDomainId_returnMostRecentModel() {
        // Arrange
        // Act
        // Assert
    }

    @Test
    public void getAll_MODELS_UNAVAILABLE() {
        // Arrange
        // Act
        // Assert
    }

    @Test
    public void getAll_returnAllModels() {
        // Arrange
        // Act
        // Assert
    }

    // region helper methods -----------------------------------------------------------------------
    // endregion helper methods --------------------------------------------------------------------

    // region helper classes -----------------------------------------------------------------------
    private static class GetDomainModelCallbackClient implements
            DomainDataAccess.GetDomainModelCallback<IngredientPersistenceModel> {

        private static final String TAG = IngredientLocalGetAdapterTest.TAG +
                GetDomainModelCallbackClient.class.getSimpleName() + ": ";

        private IngredientPersistenceModel model;
        private boolean isModelUnavailable;

        @Override
        public void onModelLoaded(IngredientPersistenceModel m) {
            System.out.println(TAG + m);
            model = m;
        }

        @Override
        public void onModelUnavailable() {
            isModelUnavailable = true;
            System.out.println(TAG + "isModelUnavailable=" + isModelUnavailable);
        }
    }

    private static class GetAllDomainModelsCallbackClient
            implements DomainDataAccess.GetAllDomainModelsCallback<IngredientPersistenceModel> {

        private static final String TAG = IngredientLocalGetAdapterTest.TAG +
                GetAllDomainModelsCallbackClient.class.getSimpleName() + ": ";

        private List<IngredientPersistenceModel> models;
        private boolean isModelsUnavailable;

        @Override
        public void onAllLoaded(List<IngredientPersistenceModel> m) {
            System.out.println(TAG + m);
            models = m;
        }

        @Override
        public void onModelsUnavailable() {
            isModelsUnavailable = true;
            System.out.println(TAG + "isModelsUnavailable=" + isModelsUnavailable);
        }
    }
    // endregion helper classes --------------------------------------------------------------------
}