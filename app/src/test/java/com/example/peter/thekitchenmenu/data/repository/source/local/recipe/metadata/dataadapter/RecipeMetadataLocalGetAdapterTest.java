package com.example.peter.thekitchenmenu.data.repository.source.local.recipe.metadata.dataadapter;

import com.example.peter.thekitchenmenu.data.repository.recipe.metadata.TestDataRecipeMetadata;
import com.example.peter.thekitchenmenu.data.repository.source.local.dataadapter.PrimitiveDataSource.GetPrimitiveCallback;
import com.example.peter.thekitchenmenu.data.repository.source.local.recipe.metadata.TestDataRecipeMetadataEntity;
import com.example.peter.thekitchenmenu.data.repository.source.local.recipe.metadata.datasource.componentstate.RecipeComponentStateEntity;
import com.example.peter.thekitchenmenu.data.repository.source.local.recipe.metadata.datasource.componentstate.RecipeComponentStateLocalDataSource;
import com.example.peter.thekitchenmenu.data.repository.source.local.recipe.metadata.datasource.failreason.RecipeFailReasonEntity;
import com.example.peter.thekitchenmenu.data.repository.source.local.recipe.metadata.datasource.failreason.RecipeFailReasonsLocalDataSource;
import com.example.peter.thekitchenmenu.data.repository.source.local.recipe.metadata.datasource.parent.RecipeMetadataParentEntity;
import com.example.peter.thekitchenmenu.data.repository.source.local.recipe.metadata.datasource.parent.RecipeMetadataParentLocalDataSource;
import com.example.peter.thekitchenmenu.domain.model.FailReasons;
import com.example.peter.thekitchenmenu.domain.usecase.recipe.metadata.RecipeMetadataPersistenceModel;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;

import static com.example.peter.thekitchenmenu.data.repository.DomainDataAccess.*;
import static com.example.peter.thekitchenmenu.data.repository.source.local.dataadapter.PrimitiveDataSource.*;
import static com.example.peter.thekitchenmenu.domain.usecase.recipe.metadata.RecipeMetadata.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.verify;

public class RecipeMetadataLocalGetAdapterTest {

    private static final String TAG = "tkm-" + RecipeMetadataLocalGetAdapterTest.class.
            getSimpleName() + ": ";

    // region constants ----------------------------------------------------------------------------
    private static RecipeMetadataPersistenceModel DATA_UNAVAILABLE_MODEL =
            TestDataRecipeMetadata.getDataUnavailable();
    private static List<RecipeMetadataPersistenceModel> VALID_CHANGED_MODELS =
            TestDataRecipeMetadata.getValidChangedList();
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
    public void setup() throws Exception {
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
    public void getByDataId_validDataId_domainModelReturned() {
        // Arrange
        String dataId = DATA_UNAVAILABLE_MODEL.getDataId();
        GetDomainModelCallbackClient callbackClient = new GetDomainModelCallbackClient();

        // Act
        SUT.getByDataId(DATA_UNAVAILABLE_MODEL.getDataId(), callbackClient);

        // Assert
        verify(parentDataSourceMock).getByDataId(
                eq(dataId),
                parentCallback.capture()
        );
        parentCallback.getValue().onEntityLoaded(
                getRecipeMetadataParentEntityFromDomainModel(DATA_UNAVAILABLE_MODEL)
        );

        verify(componentStateDataSourceMock).getAllByParentDataId(
                eq(dataId),
                componentStateDataSourceCallback.capture()
        );
        componentStateDataSourceCallback.getValue().onAllLoaded(
                getComponentStateEntitiesFromDomainModel(DATA_UNAVAILABLE_MODEL)
        );

        verify(failReasonsDataSourceMock).getAllByParentDataId(
                eq(dataId),
                failReasonDataSourceCallback.capture()
        );
        failReasonDataSourceCallback.getValue().onAllLoaded(
                getRecipeFailReasonEntitiesFromDomainModel(DATA_UNAVAILABLE_MODEL)
        );

        assertEquals(DATA_UNAVAILABLE_MODEL, callbackClient.domainModel);

    }

    @Test
    public void getActiveModelFromDomainId_returnMostRecentDomainModel() {
        // Arrange
        String domainId = VALID_CHANGED_MODELS.get(0).getDomainId();
        GetDomainModelCallbackClient callbackClient = new GetDomainModelCallbackClient();
        List<RecipeMetadataParentEntity> parentEntities = new ArrayList<>();
        for (RecipeMetadataPersistenceModel m : TestDataRecipeMetadata.getValidChangedList()) {
            parentEntities.add(getRecipeMetadataParentEntityFromDomainModel(m));
        }
        // Act
        SUT.getActiveModelFromDomainId(domainId, callbackClient);
        // Assert
        verify(parentDataSourceMock).getAllByDomainId(eq(domainId), parentListCallback.capture());
        parentListCallback.getValue().onAllLoaded(parentEntities);


    }


    // region helper methods -----------------------------------------------------------------------
    public static RecipeMetadataParentEntity getRecipeMetadataParentEntityFromDomainModel(
            RecipeMetadataPersistenceModel model) {
        return new RecipeMetadataParentEntity.Builder().
                setDataId(model.getDataId()).
                setDomainId(model.getDomainId()).
                setRecipeParentDomainId(model.getParentDomainId()).
                setRecipeStateId(model.getRecipeState().getId()).
                setCreatedBy(model.getCreatedBy()).
                setCreateDate(model.getCreateDate()).
                setLastUpdate(model.getLastUpdate()).
                build();
    }

    public static List<RecipeFailReasonEntity> getRecipeFailReasonEntitiesFromDomainModel(
            RecipeMetadataPersistenceModel model) {

        List<RecipeFailReasonEntity> failReasonEntities = new ArrayList<>();
        int dataId = 0;
        for (FailReasons failReason : model.getFailReasons()) {
            failReasonEntities.add(
                    new RecipeFailReasonEntity(
                            String.valueOf(dataId),
                            model.getDataId(),
                            failReason.getId()
                    )
            );
            dataId++;
        }
        return failReasonEntities;
    }

    public static List<RecipeComponentStateEntity> getComponentStateEntitiesFromDomainModel(
            RecipeMetadataPersistenceModel model) {
        List<RecipeComponentStateEntity> componentStateEntities = new ArrayList<>();

        int dataId = 0;
        for (ComponentName componentName : model.getComponentStates().keySet()) {
            componentStateEntities.add(
                    new RecipeComponentStateEntity(
                            String.valueOf(dataId),
                            model.getDataId(),
                            componentName.getId(),
                            model.getComponentStates().get(componentName).getId()
                    )
            );
            dataId++;
        }
        return componentStateEntities;
    }
    // endregion helper methods --------------------------------------------------------------------

    // region helper classes -----------------------------------------------------------------------
    private static class GetDomainModelCallbackClient
            implements GetDomainModelCallback<RecipeMetadataPersistenceModel> {
        private static final String TAG = RecipeMetadataLocalGetAdapterTest.TAG +
                "DomainModelCallbackClient: ";

        private RecipeMetadataPersistenceModel domainModel;

        @Override
        public void onModelLoaded(RecipeMetadataPersistenceModel model) {
            System.out.println(TAG + model);
            this.domainModel = model;
        }

        @Override
        public void onModelUnavailable() {
            System.out.println("onDataUnavailable");
        }
    }
    // endregion helper classes --------------------------------------------------------------------
}