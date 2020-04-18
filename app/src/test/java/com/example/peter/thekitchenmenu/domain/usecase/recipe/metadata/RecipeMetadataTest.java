package com.example.peter.thekitchenmenu.domain.usecase.recipe.metadata;

import com.example.peter.thekitchenmenu.commonmocks.UseCaseSchedulerMock;
import com.example.peter.thekitchenmenu.data.repository.source.local.recipe.metadata.datasource.parent.RecipeMetadataParentEntity;
import com.example.peter.thekitchenmenu.data.repository.source.local.dataadapter.PrimitiveDataSource;
import com.example.peter.thekitchenmenu.domain.model.CommonFailReason;
import com.example.peter.thekitchenmenu.domain.usecase.UseCase;
import com.example.peter.thekitchenmenu.domain.usecase.UseCaseHandler;
import com.example.peter.thekitchenmenu.domain.utils.TimeProvider;
import com.example.peter.thekitchenmenu.domain.utils.UniqueIdProvider;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import static com.example.peter.thekitchenmenu.domain.usecase.recipe.metadata.RecipeMetadata.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class RecipeMetadataTest {
    private static final String TAG = "tkm-" + RecipeMetadataTest.class.getSimpleName() + ": ";

    // region constants ----------------------------------------------------------------------------
    // endregion constants -------------------------------------------------------------------------

    // region helper fields ------------------------------------------------------------------------
//    @Mock
//    RepositoryRecipeComponentState repoRecipeMetadataMock;
    @Captor
    ArgumentCaptor<PrimitiveDataSource.GetPrimitiveCallback<RecipeMetadataParentEntity>> repoMetadataCallback;
    @Mock
    UniqueIdProvider idProviderMock;
    @Mock
    TimeProvider timeProviderMock;
    private UseCaseHandler handler;

    private HashMap<ComponentName, ComponentState> componentStates;
    private RecipeMetadataResponse onSuccessResponse;
    private RecipeMetadataResponse onErrorResponse;

    // endregion helper fields ---------------------------------------------------------------------

    private com.example.peter.thekitchenmenu.domain.usecase.recipe.metadata.RecipeMetadata SUT;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        SUT = givenUseCase();

    }

    private com.example.peter.thekitchenmenu.domain.usecase.recipe.metadata.RecipeMetadata givenUseCase() {
        handler = new UseCaseHandler(new UseCaseSchedulerMock());
        componentStates = new HashMap<>();

        final Set<ComponentName> requiredComponents = new HashSet<>();
        requiredComponents.add(ComponentName.IDENTITY);
        requiredComponents.add(ComponentName.COURSE);
        requiredComponents.add(ComponentName.DURATION);
        requiredComponents.add(ComponentName.PORTIONS);

//        return new com.example.peter.thekitchenmenu.domain.usecase.recipe.metadata.RecipeMetadata(
//                timeProviderMock,
//                repoRecipeMetadataMock,
//                requiredComponents
//        );
        return null;
    }

    // TODO -
    //  - save data

    @Test
    public void newId_databaseCalled_responseDATA_UNAVAILABLE() {
        // Arrange
//        String recipeId = TestDataRecipeMetadataEntity.getNewInvalidParent().getDataId();
        // Act
//        givenNewIdAsInitialRequest(recipeId);
        // Assert response
        assertTrue(onErrorResponse.getModel().getFailReasons().contains(CommonFailReason.DATA_UNAVAILABLE));
    }

    @Test
    public void missingComponent_stateDATA_UNAVAILABLE_failReasonMISSING_COMPONENTS() {
        // Arrange/execute initial request
//        String recipeId = TestDataRecipeMetadataEntity.getNewInvalidParent().getDataId();
//        givenNewIdAsInitialRequest(recipeId);

        // Arrange MISSING_COMPONENT state (portions component missing)
        componentStates.put(ComponentName.IDENTITY, ComponentState.VALID_UNCHANGED);
        componentStates.put(ComponentName.DURATION, ComponentState.VALID_UNCHANGED);
        componentStates.put(ComponentName.COURSE, ComponentState.VALID_UNCHANGED);

        RecipeMetadataRequest.Model model = new RecipeMetadataRequest.Model.Builder().
                getDefault().
                setComponentStates(componentStates).
                build();

//        RecipeMetadataRequest request = new RecipeMetadataRequest.Builder().
//                setDataId(recipeId).
//                setModel(model).
//                build();

        // Act
//        handler.execute(SUT, request, new MetadataCallbackClient());
        // Assert
        assertEquals(RecipeState.DATA_UNAVAILABLE, onErrorResponse.getModel().getState());
        assertTrue(onErrorResponse.getModel().
                getFailReasons().
                contains(FailReason.MISSING_COMPONENTS));
    }

    @Test
    public void invalidUnchanged_stateINVALID_UNCHANGED_failReasonINVALID_COMPONENTS() {
        // Arrange
//        String recipeId = TestDataRecipeMetadataEntity.getNewInvalidParent().getDataId();
//        givenNewIdAsInitialRequest(recipeId);

        // Arrange INVALID_UNCHANGED / INVALID_COMPONENTS state
        componentStates.put(ComponentName.IDENTITY, ComponentState.VALID_UNCHANGED);
        componentStates.put(ComponentName.DURATION, ComponentState.VALID_UNCHANGED);
        componentStates.put(ComponentName.COURSE, ComponentState.VALID_UNCHANGED);
        componentStates.put(ComponentName.PORTIONS, ComponentState.INVALID_UNCHANGED);

        RecipeMetadataRequest.Model model = new RecipeMetadataRequest.Model.Builder().
                getDefault().
                setComponentStates(componentStates).
                build();

//        RecipeMetadataRequest request = new RecipeMetadataRequest.Builder().
//                setDataId(recipeId).
//                setModel(model).
//                build();

        // Act
//        handler.execute(SUT, request, new MetadataCallbackClient());
        // Assert
        assertEquals(RecipeState.INVALID_UNCHANGED, onErrorResponse.getModel().getState());
        assertTrue(onErrorResponse.getModel().getFailReasons().
                contains(com.example.peter.thekitchenmenu.domain.usecase.recipe.metadata.RecipeMetadata.FailReason.INVALID_COMPONENTS));
    }

    @Test
    public void invalidChanged_stateINVALID_CHANGED_failReasonINVALID_COMPONENTS() {
        // Arrange
//        String recipeId = TestDataRecipeMetadataEntity.getNewInvalidParent().getDataId();
//        givenNewIdAsInitialRequest(recipeId);

        // Arrange INVALID_CHANGED / INVALID_COMPONENTS state
        componentStates.put(com.example.peter.thekitchenmenu.domain.usecase.recipe.metadata.RecipeMetadata.ComponentName.IDENTITY, com.example.peter.thekitchenmenu.domain.usecase.recipe.metadata.RecipeMetadata.ComponentState.VALID_UNCHANGED);
        componentStates.put(com.example.peter.thekitchenmenu.domain.usecase.recipe.metadata.RecipeMetadata.ComponentName.DURATION, com.example.peter.thekitchenmenu.domain.usecase.recipe.metadata.RecipeMetadata.ComponentState.VALID_UNCHANGED);
        componentStates.put(com.example.peter.thekitchenmenu.domain.usecase.recipe.metadata.RecipeMetadata.ComponentName.COURSE, com.example.peter.thekitchenmenu.domain.usecase.recipe.metadata.RecipeMetadata.ComponentState.VALID_UNCHANGED);
        componentStates.put(com.example.peter.thekitchenmenu.domain.usecase.recipe.metadata.RecipeMetadata.ComponentName.PORTIONS, com.example.peter.thekitchenmenu.domain.usecase.recipe.metadata.RecipeMetadata.ComponentState.INVALID_CHANGED);

        RecipeMetadataRequest.Model model = new RecipeMetadataRequest.Model.Builder().
                getDefault().
                setComponentStates(componentStates).
                build();

//        RecipeMetadataRequest request = new RecipeMetadataRequest.Builder().
//                setDataId(recipeId).
//                setModel(model).
//                build();

        // Act
//        handler.execute(SUT, request, new MetadataCallbackClient());
        // Assert
        assertEquals(RecipeState.INVALID_CHANGED, onErrorResponse.getModel().getState());
        assertTrue(onErrorResponse.getModel().getFailReasons().
                contains(com.example.peter.thekitchenmenu.domain.usecase.recipe.metadata.RecipeMetadata.FailReason.INVALID_COMPONENTS));
    }

    @Test
    public void validUnchanged_stateVALID_UNCHANGED_failReasonNONE() {
        // Arrange
//        String recipeId = TestDataRecipeMetadataEntity.getNewInvalidParent().getDataId();
//        givenNewIdAsInitialRequest(recipeId);

        // Arrange VALID_UNCHANGED / CommonFailReason.NONE state
        componentStates.put(ComponentName.IDENTITY, ComponentState.VALID_UNCHANGED);
        componentStates.put(ComponentName.DURATION, ComponentState.VALID_UNCHANGED);
        componentStates.put(ComponentName.COURSE, ComponentState.VALID_UNCHANGED);
        componentStates.put(ComponentName.PORTIONS, ComponentState.VALID_UNCHANGED);

        RecipeMetadataRequest.Model model = new RecipeMetadataRequest.Model.Builder().
                getDefault().
                setComponentStates(componentStates).
                build();

//        RecipeMetadataRequest request = new RecipeMetadataRequest.Builder().
//                setDataId(recipeId).
//                setModel(model).
//                build();

        // Act
//        handler.execute(SUT, request, new MetadataCallbackClient());
        // Assert
        assertEquals(RecipeState.VALID_UNCHANGED, onSuccessResponse.getModel().getState());
//        assertTrue(onSuccessResponse.getModel().getFailReasons().
//                contains(com.example.peter.thekitchenmenu.domain.usecase.recipe.metadata.RecipeMetadata.FailReason.NONE));
    }

    @Test
    public void validChanged_stateVALID_CHANGED_failReasonNONE() {
        // Arrange
//        String recipeId = TestDataRecipeMetadataEntity.getNewInvalidParent().getDataId();
//        givenNewIdAsInitialRequest(recipeId);

        // Arrange VALID_CHANGED / CommonFailReason.NONE state
        componentStates.put(com.example.peter.thekitchenmenu.domain.usecase.recipe.metadata.RecipeMetadata.ComponentName.IDENTITY, com.example.peter.thekitchenmenu.domain.usecase.recipe.metadata.RecipeMetadata.ComponentState.VALID_UNCHANGED);
        componentStates.put(com.example.peter.thekitchenmenu.domain.usecase.recipe.metadata.RecipeMetadata.ComponentName.DURATION, com.example.peter.thekitchenmenu.domain.usecase.recipe.metadata.RecipeMetadata.ComponentState.VALID_UNCHANGED);
        componentStates.put(com.example.peter.thekitchenmenu.domain.usecase.recipe.metadata.RecipeMetadata.ComponentName.COURSE, com.example.peter.thekitchenmenu.domain.usecase.recipe.metadata.RecipeMetadata.ComponentState.VALID_UNCHANGED);
        componentStates.put(com.example.peter.thekitchenmenu.domain.usecase.recipe.metadata.RecipeMetadata.ComponentName.PORTIONS, com.example.peter.thekitchenmenu.domain.usecase.recipe.metadata.RecipeMetadata.ComponentState.VALID_CHANGED);

        RecipeMetadataRequest.Model model = new RecipeMetadataRequest.Model.Builder().
                getDefault().
                setComponentStates(componentStates).
                build();

//        RecipeMetadataRequest request = new RecipeMetadataRequest.Builder().
//                setDataId(recipeId).
//                setModel(model).
//                build();

        // Act
//        handler.execute(SUT, request, new MetadataCallbackClient());

        // Assert
        assertEquals(com.example.peter.thekitchenmenu.domain.usecase.recipe.metadata.RecipeMetadata.RecipeState.VALID_CHANGED, onSuccessResponse.getModel().getState());
//        assertTrue(onSuccessResponse.getModel().getFailReasons().
//                contains(com.example.peter.thekitchenmenu.domain.usecase.recipe.metadata.RecipeMetadata.FailReason.NONE));
    }



    // region helper methods -----------------------------------------------------------------------
    private void givenNewIdAsInitialRequest(String id) {
        // Arrange/execute initial request
//        when(timeProviderMock.getCurrentTimeInMills()).
//                thenReturn(TestDataRecipeMetadataEntity.getNewInvalidParent().getCreateDate());

        RecipeMetadataRequest initialRequest = new RecipeMetadataRequest.Builder().getDefault().
                setDataId(id).
                build();

        handler.execute(SUT, initialRequest, new MetadataCallbackClient());
        whenRepoRecipeMetadataCalledThenReturnDataUnavailable(id);
    }

    private void whenRepoRecipeMetadataCalledThenReturnDataUnavailable(String id) {
//        verify(repoRecipeMetadataMock).getById(eq(id), repoMetadataCallback.capture());
        repoMetadataCallback.getValue().onDataUnavailable();
    }
    // endregion helper methods --------------------------------------------------------------------

    // region helper classes -----------------------------------------------------------------------
    private class MetadataCallbackClient implements UseCase.Callback<RecipeMetadataResponse> {
        private static final String TAG = "MetadataCallbackClient: ";

        @Override
        public void onSuccess(RecipeMetadataResponse response) {
            System.out.println(RecipeMetadataTest.TAG + TAG + "onSuccess: " + response);
            onSuccessResponse = response;
        }

        @Override
        public void onError(RecipeMetadataResponse response) {
            System.out.println(RecipeMetadataTest.TAG + TAG + "onError: " + response);
            onErrorResponse = response;
        }
    }
    // endregion helper classes --------------------------------------------------------------------


}