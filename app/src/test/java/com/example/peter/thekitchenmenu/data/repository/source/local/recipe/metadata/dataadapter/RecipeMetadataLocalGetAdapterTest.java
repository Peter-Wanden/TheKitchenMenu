package com.example.peter.thekitchenmenu.data.repository.source.local.recipe.metadata.dataadapter;

import com.example.peter.thekitchenmenu.data.repository.recipe.metadata.TestDataRecipeMetadata;
import com.example.peter.thekitchenmenu.data.repository.source.local.dataadapter.PrimitiveDataSource.GetPrimitiveCallback;
import com.example.peter.thekitchenmenu.data.repository.source.local.recipe.metadata.datasource.TestDataRecipeMetadataEntity;
import com.example.peter.thekitchenmenu.data.repository.source.local.recipe.metadata.datasource.componentstate.RecipeComponentStateEntity;
import com.example.peter.thekitchenmenu.data.repository.source.local.recipe.metadata.datasource.componentstate.RecipeComponentStateLocalDataSource;
import com.example.peter.thekitchenmenu.data.repository.source.local.recipe.metadata.datasource.failreason.RecipeFailReasonEntity;
import com.example.peter.thekitchenmenu.data.repository.source.local.recipe.metadata.datasource.failreason.RecipeFailReasonsLocalDataSource;
import com.example.peter.thekitchenmenu.data.repository.source.local.recipe.metadata.datasource.parent.RecipeMetadataParentEntity;
import com.example.peter.thekitchenmenu.data.repository.source.local.recipe.metadata.datasource.parent.RecipeMetadataParentLocalDataSource;
import com.example.peter.thekitchenmenu.domain.usecase.recipe.component.metadata.RecipeMetadataPersistenceModel;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;

import static com.example.peter.thekitchenmenu.data.repository.DomainDataAccess.GetAllDomainModelsCallback;
import static com.example.peter.thekitchenmenu.data.repository.DomainDataAccess.GetDomainModelCallback;
import static com.example.peter.thekitchenmenu.data.repository.source.local.dataadapter.PrimitiveDataSource.GetAllPrimitiveCallback;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.verify;

public class RecipeMetadataLocalGetAdapterTest {

    private static final String TAG = "tkm-" + RecipeMetadataLocalGetAdapterTest.class.
            getSimpleName() + ": ";

    // region constants ----------------------------------------------------------------------------
    // endregion constants -------------------------------------------------------------------------

    // region helper fields ------------------------------------------------------------------------
    @Mock
    RecipeMetadataParentLocalDataSource parentDataSourceMock;
    @Captor
    ArgumentCaptor<GetPrimitiveCallback<RecipeMetadataParentEntity>> parentCallback;
    @Captor
    ArgumentCaptor<GetAllPrimitiveCallback<RecipeMetadataParentEntity>> parentListCallback;
    @Mock
    RecipeComponentStateLocalDataSource componentStateDataSourceMock;
    @Captor
    ArgumentCaptor<GetAllPrimitiveCallback<RecipeComponentStateEntity>>
            componentStateDataSourceCallback;
    @Mock
    RecipeFailReasonsLocalDataSource failReasonsDataSourceMock;
    @Captor
    ArgumentCaptor<GetAllPrimitiveCallback<RecipeFailReasonEntity>> failReasonDataSourceCallback;
    // endregion helper fields ---------------------------------------------------------------------

    private RecipeMetadataLocalGetAdapter SUT;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        SUT = givenUseCase();
    }

    private RecipeMetadataLocalGetAdapter givenUseCase() {
        return new RecipeMetadataLocalGetAdapter(
                parentDataSourceMock,
                componentStateDataSourceMock,
                failReasonsDataSourceMock);
    }

    @Test
    public void getByDataId_DATA_UNAVAILABLE() {
        // Arrange
        String dataId = "";
        GetDomainModelCallbackClient callbackClient = new GetDomainModelCallbackClient();
        // Act
        SUT.getByDataId(dataId, callbackClient);
        // Assert
        verify(parentDataSourceMock).getByDataId(eq(dataId), parentCallback.capture());
        parentCallback.getValue().onDataUnavailable();
        assertTrue(callbackClient.onModelUnavailable);
    }

    @Test
    public void getByDataId_domainModelReturned() {
        // Arrange
        String dataId = TestDataRecipeMetadata.getDataUnavailable().getDataId();
        GetDomainModelCallbackClient callbackClient = new GetDomainModelCallbackClient();
        // Act
        SUT.getByDataId(dataId, callbackClient);
        // Assert parent requested
        verify(parentDataSourceMock).getByDataId(eq(dataId),
                parentCallback.capture()
        );
        // Return parent
        parentCallback.getValue().onEntityLoaded(TestDataRecipeMetadataEntity.
                getDataUnavailableParentEntity()
        );
        // Assert component states called
        verify(componentStateDataSourceMock).getAllByParentDataId(eq(dataId),
                componentStateDataSourceCallback.capture()
        );
        // Return component states
        componentStateDataSourceCallback.getValue().onAllLoaded(TestDataRecipeMetadataEntity.
                getDataUnavailableComponentStateEntities()
        );
        // Assert fail reasons called
        verify(failReasonsDataSourceMock).getAllByParentDataId(eq(dataId),
                failReasonDataSourceCallback.capture()
        );
        // Return fail reasons
        failReasonDataSourceCallback.getValue().onAllLoaded(TestDataRecipeMetadataEntity.
                        getDataUnavailableFailReasonEntities()
        );
        // Assert result
        assertEquals(TestDataRecipeMetadata.getDataUnavailable(),
                callbackClient.domainModel
        );
    }

    @Test
    public void getActiveByDomainId_returnMostRecentDomainModel() {
        // Arrange
        RecipeMetadataPersistenceModel expectedModel = TestDataRecipeMetadata.getValidChangedThree();
        String domainId = expectedModel.getDomainId();
        String parentDataId = expectedModel.getDataId();

        GetDomainModelCallbackClient callbackClient = new GetDomainModelCallbackClient();

        // Act
        SUT.getActiveByDomainId(domainId, callbackClient);

        // Assert parent data source called
        verify(parentDataSourceMock).getAllByDomainId(eq(domainId),
                parentListCallback.capture()
        );
        // Return parent entity list
        parentListCallback.getValue().onAllLoaded(TestDataRecipeMetadataEntity.
                getValidChangedParentEntityList());
        // Assert child called with correct parent data Id
        verify(failReasonsDataSourceMock).getAllByParentDataId(eq(parentDataId),
                failReasonDataSourceCallback.capture()
        );
        // Return fail reasons
        failReasonDataSourceCallback.getValue().onAllLoaded(TestDataRecipeMetadataEntity.
                        getValidChanged0FailReasonEntities()
        );
        // Assert child called with correct data Id
        verify(componentStateDataSourceMock).getAllByParentDataId(eq(parentDataId),
                componentStateDataSourceCallback.capture());
        // Return component state entities
        componentStateDataSourceCallback.getValue().onAllLoaded(TestDataRecipeMetadataEntity.
                        getValidChanged0ComponentStateEntities()
        );

        assertEquals(expectedModel, callbackClient.domainModel);
    }

    @Test
    public void getAllActive_returnOnlyMostRecentModels() {
        // Arrange
        String domainId = TestDataRecipeMetadata.getValidChangedThree().getDomainId();
        GetAllCallbackClient getAllCallbackClient = new GetAllCallbackClient ();

        String correctParentDataId = TestDataRecipeMetadataEntity.
                getValidChanged0ParentEntity().
                getDataId();

        // Act
        SUT.getAllActive(getAllCallbackClient);
        // Assert parent data source called
        verify(parentDataSourceMock).getAll(parentListCallback.capture());
        // Return all parents
        parentListCallback.getValue().onAllLoaded(TestDataRecipeMetadataEntity.
                getValidChangedParentEntityList());

        // Assert parent entities requested
        verify(parentDataSourceMock).getAllByDomainId(eq(domainId), parentListCallback.capture());
        parentListCallback.getValue().onAllLoaded(TestDataRecipeMetadataEntity.
                getValidChangedParentEntityList());
        // Assert fail reasons data source called with correct parent data Id
        verify(failReasonsDataSourceMock).getAllByParentDataId(eq(correctParentDataId),
                failReasonDataSourceCallback.capture()
        );
        failReasonDataSourceCallback.getValue().onAllLoaded(TestDataRecipeMetadataEntity.
                getValidChanged0FailReasonEntities()
        );
        // Assert component states data source called with correct parent data id
        verify(componentStateDataSourceMock).getAllByParentDataId(eq(correctParentDataId),
                componentStateDataSourceCallback.capture());
        componentStateDataSourceCallback.getValue().onAllLoaded(
                TestDataRecipeMetadataEntity.getValidChanged0ComponentStateEntities()
        );

        assertEquals(TestDataRecipeMetadata.getValidChangedThree(), getAllCallbackClient.models.get(0));
    }

    // region helper methods -----------------------------------------------------------------------
    // endregion helper methods --------------------------------------------------------------------

    // region helper classes -----------------------------------------------------------------------
    private static class GetDomainModelCallbackClient
            implements GetDomainModelCallback<RecipeMetadataPersistenceModel> {

        private static final String TAG = RecipeMetadataLocalGetAdapterTest.TAG +
                "GetModelCallbackClient: ";

        private RecipeMetadataPersistenceModel domainModel;
        private boolean onModelUnavailable;

        @Override
        public void dataSourceOnDomainModelLoaded(RecipeMetadataPersistenceModel model) {
            System.out.println(TAG + model);
            this.domainModel = model;
        }

        @Override
        public void dataSourceOnDomainModelUnavailable() {
            System.out.println(TAG + "onModelUnavailable");
            onModelUnavailable = true;
        }
    }

    private static class GetAllCallbackClient
            implements GetAllDomainModelsCallback<RecipeMetadataPersistenceModel> {

        private static final String TAG = "tkm-" + RecipeMetadataLocalGetAdapterTest.TAG +
                "GetAllModelsCallbackClient: ";

        private List<RecipeMetadataPersistenceModel> models = new ArrayList<>();

        @Override
        public void onAllLoaded(List<RecipeMetadataPersistenceModel> models) {
            System.out.println(TAG + models);
            this.models = models;
        }

        @Override
        public void onModelsUnavailable() {
            System.out.println(TAG + "onModelsUnavailable");
        }
    }
    // endregion helper classes --------------------------------------------------------------------
}