package com.example.peter.thekitchenmenu.data.repository.source.local.recipe.duration.dataadapter;

import com.example.peter.thekitchenmenu.data.repository.DomainDataAccess.GetDomainModelCallback;
import com.example.peter.thekitchenmenu.data.repository.recipe.duration.TestDataRecipeDuration;
import com.example.peter.thekitchenmenu.data.repository.source.local.recipe.duration.datasource.RecipeDurationEntity;
import com.example.peter.thekitchenmenu.data.repository.source.local.recipe.duration.datasource.RecipeDurationLocalDataSource;
import com.example.peter.thekitchenmenu.data.repository.source.local.recipe.duration.datasource.TestDataRecipeDurationEntity;
import com.example.peter.thekitchenmenu.domain.usecase.recipe.component.duration.RecipeDurationPersistenceModel;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;

import static com.example.peter.thekitchenmenu.data.repository.DomainDataAccess.*;
import static com.example.peter.thekitchenmenu.data.repository.source.local.dataadapter.PrimitiveDataSource.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.verify;

public class DurationLocalGetAdapterTest {

    private static final String TAG = "tkm-" + DurationLocalGetAdapterTest.class.getSimpleName() +
            ": ";

    // region constants ----------------------------------------------------------------------------
    // endregion constants -------------------------------------------------------------------------

    // region helper fields ------------------------------------------------------------------------
    @Mock
    RecipeDurationLocalDataSource repoMock;
    @Captor
    ArgumentCaptor<GetPrimitiveCallback<RecipeDurationEntity>> repoCallback;
    @Captor
    ArgumentCaptor<GetAllPrimitiveCallback<RecipeDurationEntity>> repoGetAllCallback;
    // endregion helper fields ---------------------------------------------------------------------

    private DurationLocalGetAdapter SUT;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        SUT = givenUseCase();
    }

    private DurationLocalGetAdapter givenUseCase() {
        return new DurationLocalGetAdapter(
                repoMock
        );
    }

    @Test
    public void getByDataId_DATA_UNAVAILABLE() {
        // Arrange
        String dataId = "dataIdNotInTestData";
        DomainModelCallbackClient callbackClient = new DomainModelCallbackClient();
        // Act
        SUT.getByDataId(dataId, callbackClient);
        // Assert database called and return data unavailable
        verify(repoMock).getByDataId(eq(dataId), repoCallback.capture());
        repoCallback.getValue().onDataUnavailable();
        // Assert
        assertTrue(callbackClient.onModelUnavailable);
    }

    @Test
    public void getByDataId_domainModelReturned() {
        // Arrange
        RecipeDurationPersistenceModel expectedModel = TestDataRecipeDuration.getValidExistingComplete();
        DomainModelCallbackClient callbackClient = new DomainModelCallbackClient();
        // Act
        SUT.getByDataId(expectedModel.getDataId(), callbackClient);
        // Assert
        verify(repoMock).getByDataId(eq(expectedModel.getDataId()), repoCallback.capture());
        repoCallback.getValue().onEntityLoaded(TestDataRecipeDurationEntity.
                getValidExistingComplete());

        assertEquals(
                expectedModel,
                callbackClient.model
        );
    }

    @Test
    public void getActiveModelByDomainId_returnMostRecentModel() {
        // Arrange
        long lastUpdate = 0L;
        RecipeDurationPersistenceModel expectedModel = new RecipeDurationPersistenceModel.Builder().
                getDefault().build();
        for (RecipeDurationPersistenceModel m : TestDataRecipeDuration.getAllNew()) {
            if (m.getLastUpdate() > lastUpdate) {
                expectedModel = m;
            }
        }
        DomainModelCallbackClient callbackClient = new DomainModelCallbackClient();
        // Act
        SUT.getActiveByDomainId(expectedModel.getDomainId(), callbackClient);
        // Assert database called, return list of entities
        verify(repoMock).getAllByDomainId(eq(expectedModel.getDomainId()), repoGetAllCallback.capture());
        repoGetAllCallback.getValue().onAllLoaded(TestDataRecipeDurationEntity.getAllNew());
        // Assert correct entity converted to model and returned
        assertEquals(
                expectedModel,
                callbackClient.model
        );
    }

    @Test
    public void getAll_returnsAllModels() {
        // Arrange
        GetAllCallbackClient getAllCallbackClient = new GetAllCallbackClient();
        // Act
        SUT.getAll(getAllCallbackClient);
        // Assert database called and return all new
        verify(repoMock).getAll(repoGetAllCallback.capture());
        repoGetAllCallback.getValue().onAllLoaded(TestDataRecipeDurationEntity.getAllNew());
        // Assert models returned
        assertEquals(
                TestDataRecipeDuration.getAllNew(),
                getAllCallbackClient.models
        );
    }

    // region helper methods -----------------------------------------------------------------------
    // endregion helper methods --------------------------------------------------------------------

    // region helper classes -----------------------------------------------------------------------
    private static class DomainModelCallbackClient
            implements GetDomainModelCallback<RecipeDurationPersistenceModel> {

        private final String TAG = DurationLocalGetAdapterTest.TAG +
                "DomainModeCallback: ";
        boolean onModelUnavailable;
        RecipeDurationPersistenceModel model;

        @Override
        public void onModelLoaded(RecipeDurationPersistenceModel m) {
            System.out.println(TAG + "onSuccess: " + m);
            model = m;
        }

        @Override
        public void onModelUnavailable() {
            System.out.println(TAG + "onModelUnavailable");
            onModelUnavailable = true;
        }
    }

    private static class GetAllCallbackClient
            implements GetAllDomainModelsCallback<RecipeDurationPersistenceModel> {

        private final String TAG = DurationLocalGetAdapterTest.TAG + "GetAllCallback: ";

        List<RecipeDurationPersistenceModel> models;
        boolean onModelsUnavailable;

        @Override
        public void onAllLoaded(List<RecipeDurationPersistenceModel> m) {
            System.out.println(TAG + "onSuccess: " + m);
            models = m;
        }

        @Override
        public void onModelsUnavailable() {
            System.out.println(TAG + "onModelsUnavailable");
            onModelsUnavailable = true;
        }
    }
    // endregion helper classes --------------------------------------------------------------------
}
