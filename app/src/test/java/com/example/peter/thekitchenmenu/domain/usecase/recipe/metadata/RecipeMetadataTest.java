package com.example.peter.thekitchenmenu.domain.usecase.recipe.metadata;

import com.example.peter.thekitchenmenu.commonmocks.UseCaseSchedulerMock;
import com.example.peter.thekitchenmenu.data.repository.DomainDataAccess.GetDomainModelCallback;
import com.example.peter.thekitchenmenu.data.repository.recipe.RepositoryRecipeMetadata;
import com.example.peter.thekitchenmenu.data.repository.recipe.metadata.TestDataRecipeMetadata;
import com.example.peter.thekitchenmenu.domain.model.CommonFailReason;
import com.example.peter.thekitchenmenu.domain.usecase.UseCase;
import com.example.peter.thekitchenmenu.domain.usecase.UseCaseHandler;
import com.example.peter.thekitchenmenu.domain.usecase.recipe.metadata.RecipeMetadata.ComponentState;
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

import static com.example.peter.thekitchenmenu.domain.usecase.recipe.metadata.RecipeMetadata.ComponentName;
import static com.example.peter.thekitchenmenu.domain.usecase.recipe.metadata.RecipeMetadata.FailReason;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class RecipeMetadataTest {
    private static final String TAG = "tkm-" + RecipeMetadataTest.class.getSimpleName() + ": ";

    // region constants ----------------------------------------------------------------------------
    // endregion constants -------------------------------------------------------------------------

    // region helper fields ------------------------------------------------------------------------
    @Mock
    RepositoryRecipeMetadata repoMock;
    @Captor
    ArgumentCaptor<GetDomainModelCallback<RecipeMetadataPersistenceModel>> repoMetadataCallback;
    @Mock
    UniqueIdProvider idProviderMock;
    @Mock
    TimeProvider timeProviderMock;

    private UseCaseHandler handler;
    private HashMap<ComponentName, ComponentState> componentStates;
    private RecipeMetadataResponse onSuccessResponse;
    private RecipeMetadataResponse onErrorResponse;

    // endregion helper fields ---------------------------------------------------------------------

    private RecipeMetadata SUT;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        SUT = givenUseCase();

    }

    private RecipeMetadata givenUseCase() {
        handler = new UseCaseHandler(new UseCaseSchedulerMock());
        componentStates = new HashMap<>();

        final Set<ComponentName> requiredComponents = new HashSet<>();
        requiredComponents.add(ComponentName.IDENTITY);
        requiredComponents.add(ComponentName.COURSE);
        requiredComponents.add(ComponentName.DURATION);
        requiredComponents.add(ComponentName.PORTIONS);

        return new RecipeMetadata(
                repoMock,
                idProviderMock,
                timeProviderMock,
                requiredComponents
        );
    }

    // TODO - save data

    @Test
    public void newId_databaseCalled_DATA_UNAVAILABLE() {
        // Arrange
        String recipeId = TestDataRecipeMetadata.NEW_RECIPE_DOMAIN_ID;

        when(idProviderMock.getUId()).thenReturn(TestDataRecipeMetadata.
                getDataUnavailable().getDataId());
        // Act
        givenNewIdAsInitialRequest(recipeId);
        // Assert response
        assertEquals(
                ComponentState.INVALID_UNCHANGED,
                onErrorResponse.getMetadata().getState()
        );
        assertTrue(
                onErrorResponse.getMetadata().getFailReasons().
                        contains(CommonFailReason.DATA_UNAVAILABLE)
        );
    }

    @Test
    public void missingComponent_stateINVALID_UNCHANGED_failReasonMISSING_COMPONENTS() {
        // Arrange/execute initial request
        String recipeId = TestDataRecipeMetadata.NEW_RECIPE_DOMAIN_ID;
        givenNewIdAsInitialRequest(recipeId);

        // Arrange MISSING_COMPONENT state (portions missing)
        componentStates.put(ComponentName.IDENTITY, ComponentState.VALID_UNCHANGED);
        componentStates.put(ComponentName.DURATION, ComponentState.VALID_UNCHANGED);
        componentStates.put(ComponentName.COURSE, ComponentState.VALID_UNCHANGED);

        RecipeMetadataRequest.Model model = new RecipeMetadataRequest.Model.Builder().
                getDefault().
                setComponentStates(componentStates).
                build();

        RecipeMetadataRequest request = new RecipeMetadataRequest.Builder().
                setDomainId(recipeId).
                setModel(model).
                build();

        // Act
        handler.execute(SUT, request, new MetadataCallbackClient());
        // Assert
        assertEquals(
                ComponentState.INVALID_UNCHANGED,
                onErrorResponse.getMetadata().getState()
        );
        assertTrue(onErrorResponse.getMetadata().
                getFailReasons().
                contains(FailReason.MISSING_COMPONENTS)
        );
    }

    @Test
    public void invalidUnchanged_stateINVALID_UNCHANGED_failReasonINVALID_COMPONENTS() {
        // Arrange
        String recipeId = TestDataRecipeMetadata.NEW_RECIPE_DOMAIN_ID;
        givenNewIdAsInitialRequest(recipeId);

        // Arrange INVALID_UNCHANGED / INVALID_COMPONENTS state
        componentStates.put(ComponentName.IDENTITY, ComponentState.VALID_UNCHANGED);
        componentStates.put(ComponentName.DURATION, ComponentState.VALID_UNCHANGED);
        componentStates.put(ComponentName.COURSE, ComponentState.VALID_UNCHANGED);
        componentStates.put(ComponentName.PORTIONS, ComponentState.INVALID_UNCHANGED);

        RecipeMetadataRequest.Model model = new RecipeMetadataRequest.Model.Builder().
                getDefault().
                setComponentStates(componentStates).
                build();

        RecipeMetadataRequest r = new RecipeMetadataRequest.Builder().
                basedOnResponse(onErrorResponse).
                setModel(model).
                build();

        // Act
        handler.execute(SUT, r, new MetadataCallbackClient());
        // Assert
        assertEquals(
                ComponentState.INVALID_UNCHANGED,
                onErrorResponse.getMetadata().getState()
        );
        assertTrue(onErrorResponse.getMetadata().getFailReasons().
                contains(RecipeMetadata.FailReason.INVALID_COMPONENTS)
        );
    }

    @Test
    public void invalidChanged_stateINVALID_CHANGED_failReasonINVALID_COMPONENTS() {
        // Arrange
//        String recipeId = TestDataRecipeMetadataEntity.getNewInvalidParent().getDataId();
//        givenNewIdAsInitialRequest(recipeId);

        // Arrange INVALID_CHANGED / INVALID_COMPONENTS state
        componentStates.put(RecipeMetadata.ComponentName.IDENTITY, RecipeMetadata.ComponentState.VALID_UNCHANGED);
        componentStates.put(RecipeMetadata.ComponentName.DURATION, RecipeMetadata.ComponentState.VALID_UNCHANGED);
        componentStates.put(RecipeMetadata.ComponentName.COURSE, RecipeMetadata.ComponentState.VALID_UNCHANGED);
        componentStates.put(RecipeMetadata.ComponentName.PORTIONS, RecipeMetadata.ComponentState.INVALID_CHANGED);

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
        assertEquals(
                ComponentState.INVALID_CHANGED,
                onErrorResponse.getMetadata().getState()
        );
        assertTrue(
                onErrorResponse.getMetadata().getFailReasons().
                contains(RecipeMetadata.FailReason.INVALID_COMPONENTS));
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
        assertEquals(
                ComponentState.VALID_UNCHANGED,
                onSuccessResponse.getMetadata().getState()
        );
        assertTrue(onSuccessResponse.getMetadata().getFailReasons().
                contains(CommonFailReason.NONE));
    }

    @Test
    public void validChanged_stateVALID_CHANGED_failReasonNONE() {
        // Arrange
//        String recipeId = TestDataRecipeMetadataEntity.getNewInvalidParent().getDataId();
//        givenNewIdAsInitialRequest(recipeId);

        // Arrange VALID_CHANGED / CommonFailReason.NONE state
        componentStates.put(RecipeMetadata.ComponentName.IDENTITY, RecipeMetadata.ComponentState.VALID_UNCHANGED);
        componentStates.put(RecipeMetadata.ComponentName.DURATION, RecipeMetadata.ComponentState.VALID_UNCHANGED);
        componentStates.put(RecipeMetadata.ComponentName.COURSE, RecipeMetadata.ComponentState.VALID_UNCHANGED);
        componentStates.put(RecipeMetadata.ComponentName.PORTIONS, RecipeMetadata.ComponentState.VALID_CHANGED);

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
        assertEquals(
                ComponentState.VALID_CHANGED,
                onSuccessResponse.getMetadata().getState()
        );
        assertTrue(onSuccessResponse.getMetadata().getFailReasons().
                contains(CommonFailReason.NONE)
        );
    }


    // region helper methods -----------------------------------------------------------------------
    private void givenNewIdAsInitialRequest(String domainId) {
        // Arrange/execute initial request
        when(timeProviderMock.getCurrentTimeInMills()).thenReturn(TestDataRecipeMetadata.
                getDataUnavailable().getCreateDate());

        when(idProviderMock.getUId()).thenReturn(TestDataRecipeMetadata.
                getDataUnavailable().getDataId());

        RecipeMetadataRequest initialRequest = new RecipeMetadataRequest.Builder().getDefault().
                setDomainId(domainId).
                build();

        handler.execute(SUT, initialRequest, new MetadataCallbackClient());

        whenRepoCalledReturnDataUnavailable(domainId);
    }

    private void whenRepoCalledReturnDataUnavailable(String domainId) {
        verify(repoMock).getActiveByDomainId(eq(domainId), repoMetadataCallback.capture());
        repoMetadataCallback.getValue().onModelUnavailable();
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