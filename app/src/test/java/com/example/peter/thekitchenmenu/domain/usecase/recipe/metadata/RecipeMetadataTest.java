package com.example.peter.thekitchenmenu.domain.usecase.recipe.metadata;

import com.example.peter.thekitchenmenu.app.Constants;
import com.example.peter.thekitchenmenu.commonmocks.UseCaseSchedulerMock;
import com.example.peter.thekitchenmenu.data.repository.DomainDataAccess.GetDomainModelCallback;
import com.example.peter.thekitchenmenu.data.repository.recipe.RepositoryRecipeMetadata;
import com.example.peter.thekitchenmenu.data.repository.recipe.metadata.TestDataRecipeMetadata;
import com.example.peter.thekitchenmenu.domain.model.CommonFailReason;
import com.example.peter.thekitchenmenu.domain.model.FailReasons;
import com.example.peter.thekitchenmenu.domain.usecase.UseCase;
import com.example.peter.thekitchenmenu.domain.usecase.UseCaseHandler;
import com.example.peter.thekitchenmenu.domain.usecase.UseCaseMetadata;
import com.example.peter.thekitchenmenu.domain.usecase.recipe.component.metadata.RecipeMetadata;
import com.example.peter.thekitchenmenu.domain.usecase.recipe.component.metadata.RecipeMetadata.ComponentName;
import com.example.peter.thekitchenmenu.domain.usecase.recipe.component.metadata.RecipeMetadata.ComponentState;
import com.example.peter.thekitchenmenu.domain.usecase.recipe.component.metadata.RecipeMetadataPersistenceModel;
import com.example.peter.thekitchenmenu.domain.usecase.recipe.component.metadata.RecipeMetadataRequest;
import com.example.peter.thekitchenmenu.domain.usecase.recipe.component.metadata.RecipeMetadataResponse;
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
import java.util.List;
import java.util.Set;

import static com.example.peter.thekitchenmenu.domain.usecase.recipe.component.metadata.RecipeMetadata.FailReason;
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

    // Tests the initial request on which all other tests depend
    @Test
    public void testNewInitialRequest() {
        // Arrange
        RecipeMetadataPersistenceModel modelUnderTest = TestDataRecipeMetadata.getDataUnavailable();
        // Expected default component states
        componentStates.put(ComponentName.IDENTITY, ComponentState.INVALID_UNCHANGED);
        componentStates.put(ComponentName.DURATION, ComponentState.INVALID_UNCHANGED);
        componentStates.put(ComponentName.COURSE, ComponentState.INVALID_UNCHANGED);
        componentStates.put(ComponentName.PORTIONS, ComponentState.INVALID_UNCHANGED);

        String recipeId = TestDataRecipeMetadata.NEW_RECIPE_DOMAIN_ID;

        // Act
        givenNewIdAsInitialRequest(recipeId);

        // Assert response values
        assertEquals(
                modelUnderTest.getDataId(),
                onErrorResponse.getDataId()
        );
        assertEquals(
                modelUnderTest.getDomainId(),
                onErrorResponse.getDomainId()
        );

        // Assert metadata state
        UseCaseMetadata responseMetadata = onErrorResponse.getMetadata();
        assertEquals(
                ComponentState.INVALID_UNCHANGED,
                responseMetadata.getState()
        );
        // Assert metadata fail reasons
        int expectedNoOfFailReasons = 2;
        List<FailReasons> failReasons = responseMetadata.getFailReasons();
        assertEquals(
                expectedNoOfFailReasons,
                failReasons.size()
        );
        assertTrue(
                failReasons.contains(CommonFailReason.DATA_UNAVAILABLE)
        );
        assertTrue(
                failReasons.contains(FailReason.MISSING_COMPONENTS)
        );
        assertEquals(
                Constants.getUserId(),
                responseMetadata.getCreatedBy()
        );
        assertEquals(
                modelUnderTest.getCreateDate(),
                responseMetadata.getCreateDate()
        );
        assertEquals(
                modelUnderTest.getLastUpdate(),
                responseMetadata.getLasUpdate()
        );

        // Assert response model
        RecipeMetadataResponse.Model responseModel = onErrorResponse.getModel();
        assertEquals(
                modelUnderTest.getParentDomainId(),
                responseModel.getParentDomainId()
        );
        assertEquals(
                componentStates,
                responseModel.getComponentStates()
        );
    }

    // region componentStatesTests
    @Test
    public void missingComponent_stateDATA_UNAVAILABLE_failReasonMISSING_COMPONENTS() {
        // Arrange/execute initial request
        String recipeId = TestDataRecipeMetadata.NEW_RECIPE_DOMAIN_ID;
        givenNewIdAsInitialRequest(recipeId);

        // Arrange MISSING_COMPONENT state (portions missing)
        componentStates.put(ComponentName.IDENTITY, ComponentState.VALID_UNCHANGED);
        componentStates.put(ComponentName.DURATION, ComponentState.VALID_UNCHANGED);
        componentStates.put(ComponentName.COURSE, ComponentState.VALID_UNCHANGED);

        RecipeMetadataRequest.Model model = new RecipeMetadataRequest.Model.Builder().
                basedOnResponseModel(onErrorResponse.getModel()).
                setComponentStates(componentStates).
                build();

        RecipeMetadataRequest request = new RecipeMetadataRequest.Builder().
                basedOnResponse(onErrorResponse).
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
    public void invalidComponent_stateINVALID_UNCHANGED_failReasonINVALID_COMPONENTS() {
        // Arrange
        String recipeId = TestDataRecipeMetadata.NEW_RECIPE_DOMAIN_ID;
        givenNewIdAsInitialRequest(recipeId);

        // Arrange INVALID_UNCHANGED / INVALID_COMPONENTS state
        componentStates.put(ComponentName.IDENTITY, ComponentState.VALID_UNCHANGED);
        componentStates.put(ComponentName.DURATION, ComponentState.VALID_UNCHANGED);
        componentStates.put(ComponentName.COURSE, ComponentState.VALID_UNCHANGED);
        componentStates.put(ComponentName.PORTIONS, ComponentState.INVALID_UNCHANGED);

        RecipeMetadataRequest.Model model = new RecipeMetadataRequest.Model.Builder().
                basedOnResponseModel(onErrorResponse.getModel()).
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
                contains(FailReason.INVALID_COMPONENTS)
        );
    }

    @Test
    public void invalidChangedComponent_stateINVALID_CHANGED_failReasonINVALID_COMPONENTS() {
        // Arrange
        String recipeId = TestDataRecipeMetadata.NEW_RECIPE_DOMAIN_ID;
        givenNewIdAsInitialRequest(recipeId);

        // Arrange INVALID_CHANGED / INVALID_COMPONENTS state
        componentStates.put(ComponentName.IDENTITY, ComponentState.VALID_UNCHANGED);
        componentStates.put(ComponentName.DURATION, ComponentState.VALID_UNCHANGED);
        componentStates.put(ComponentName.COURSE, ComponentState.VALID_UNCHANGED);
        componentStates.put(ComponentName.PORTIONS, ComponentState.INVALID_CHANGED);

        RecipeMetadataRequest.Model model = new RecipeMetadataRequest.Model.Builder().
                basedOnResponseModel(onErrorResponse.getModel()).
                setComponentStates(componentStates).
                build();

        RecipeMetadataRequest request = new RecipeMetadataRequest.Builder().
                basedOnResponse(onErrorResponse).
                setModel(model).
                build();

        // Act
        handler.execute(SUT, request, new MetadataCallbackClient());
        // Assert
        assertEquals(
                ComponentState.INVALID_CHANGED,
                onErrorResponse.getMetadata().getState()
        );
        assertTrue(
                onErrorResponse.getMetadata().getFailReasons().
                        contains(FailReason.INVALID_COMPONENTS)
        );
    }

    @Test
    public void validUnchangedComponent_stateVALID_UNCHANGED_failReasonNONE() {
        // Arrange
        String recipeId = TestDataRecipeMetadata.NEW_RECIPE_DOMAIN_ID;
        givenNewIdAsInitialRequest(recipeId);

        // Arrange VALID_UNCHANGED / CommonFailReason.NONE state
        componentStates.put(ComponentName.IDENTITY, ComponentState.VALID_UNCHANGED);
        componentStates.put(ComponentName.DURATION, ComponentState.VALID_UNCHANGED);
        componentStates.put(ComponentName.COURSE, ComponentState.VALID_UNCHANGED);
        componentStates.put(ComponentName.PORTIONS, ComponentState.VALID_UNCHANGED);

        RecipeMetadataRequest.Model model = new RecipeMetadataRequest.Model.Builder().
                basedOnResponseModel(onErrorResponse.getModel()).
                setComponentStates(componentStates).
                build();

        RecipeMetadataRequest request = new RecipeMetadataRequest.Builder().
                basedOnResponse(onErrorResponse).
                setModel(model).
                build();

        // Act
        handler.execute(SUT, request, new MetadataCallbackClient()
        );
        // Assert
        assertEquals(
                ComponentState.VALID_UNCHANGED,
                onSuccessResponse.getMetadata().getState()
        );
        assertTrue(onSuccessResponse.getMetadata().getFailReasons().
                contains(CommonFailReason.NONE));
    }

    @Test
    public void validChangedComponent_recipeCourse_INVALID_UNCHANGED() {
        // Arrange
        String recipeId = TestDataRecipeMetadata.NEW_RECIPE_DOMAIN_ID;
        givenNewIdAsInitialRequest(recipeId);
        // Act
        // Arrange VALID_UNCHANGED / CommonFailReason.NONE state
        componentStates.put(ComponentName.IDENTITY, ComponentState.INVALID_UNCHANGED);
        componentStates.put(ComponentName.COURSE, ComponentState.VALID_CHANGED);
        componentStates.put(ComponentName.DURATION, ComponentState.INVALID_UNCHANGED);
        componentStates.put(ComponentName.PORTIONS, ComponentState.INVALID_UNCHANGED);

        // Assert
        RecipeMetadataRequest request = new RecipeMetadataRequest.Builder().
                basedOnResponse(onErrorResponse).
                setModel(
                        new RecipeMetadataRequest.Model.Builder().
                                basedOnResponseModel(onErrorResponse.getModel()).
                                setComponentStates(componentStates).build()).
                build();

        // Act
        handler.execute(SUT, request, new MetadataCallbackClient()
        );
        // Assert
        assertEquals(
                ComponentState.VALID_CHANGED,
                onErrorResponse.getModel().getComponentStates().get(ComponentName.COURSE)
        );
        assertEquals(
                ComponentState.INVALID_CHANGED,
                onErrorResponse.getMetadata().getState()
        );
    }

    @Test
    public void validChanged_stateVALID_CHANGED_failReasonNONE() {
        // Arrange
        String recipeId = TestDataRecipeMetadata.NEW_RECIPE_DOMAIN_ID;
        givenNewIdAsInitialRequest(recipeId);

        // Arrange VALID_CHANGED / CommonFailReason.NONE state
        componentStates.put(ComponentName.IDENTITY, ComponentState.VALID_UNCHANGED);
        componentStates.put(ComponentName.DURATION, ComponentState.VALID_UNCHANGED);
        componentStates.put(ComponentName.COURSE, ComponentState.VALID_UNCHANGED);
        componentStates.put(ComponentName.PORTIONS, ComponentState.VALID_CHANGED);

        RecipeMetadataRequest.Model model = new RecipeMetadataRequest.Model.Builder().
                getDefault().
                setComponentStates(componentStates).
                build();

        RecipeMetadataRequest request = new RecipeMetadataRequest.Builder().
                basedOnResponse(onErrorResponse).
                setModel(model).
                build();

        // Act
        handler.execute(SUT, request, new MetadataCallbackClient());

        // Assert
        assertEquals(
                ComponentState.VALID_CHANGED,
                onSuccessResponse.getMetadata().getState()
        );
        assertTrue(onSuccessResponse.getMetadata().getFailReasons().
                contains(CommonFailReason.NONE)
        );
    }

    @Test
    public void invalidChanged_stateINVALID_CHANGED() {
        // Arrange
        String recipeId = TestDataRecipeMetadata.NEW_RECIPE_DOMAIN_ID;

        RecipeMetadataRequest initialRequest = new RecipeMetadataRequest.Builder().
                getDefault().
                setDomainId(recipeId).
                build();

        when(idProviderMock.getUId()).thenReturn(TestDataRecipeMetadata.
                getDataUnavailable().
                getDataId()
        );
        when(timeProviderMock.getCurrentTimeInMills()).thenReturn(TestDataRecipeMetadata.
                getDataUnavailable().
                getCreateDate()
        );
        // Act
        handler.execute(SUT, initialRequest, new MetadataCallbackClient());

        // Assert
        verify(repoMock).getActiveByDomainId(eq(recipeId), repoMetadataCallback.capture());
        repoMetadataCallback.getValue().onModelUnavailable();

        // Arrange for second request
        componentStates.put(ComponentName.IDENTITY, ComponentState.INVALID_UNCHANGED);
        componentStates.put(ComponentName.COURSE, ComponentState.INVALID_UNCHANGED);
        componentStates.put(ComponentName.DURATION, ComponentState.INVALID_UNCHANGED);
        componentStates.put(ComponentName.PORTIONS, ComponentState.INVALID_UNCHANGED);


        RecipeMetadataRequest secondRequest = new RecipeMetadataRequest.Builder().
                basedOnResponse(onErrorResponse).
                setModel(
                        new RecipeMetadataRequest.Model.Builder().
                                basedOnResponseModel(onErrorResponse.getModel()).
                                setComponentStates(componentStates).
                                build()).
                build();

        // Act
        handler.execute(SUT, secondRequest, new MetadataCallbackClient());

        // Arrange for third request
        componentStates.clear();

        componentStates.put(ComponentName.IDENTITY, ComponentState.INVALID_UNCHANGED);
        componentStates.put(ComponentName.COURSE, ComponentState.VALID_CHANGED);
        componentStates.put(ComponentName.DURATION, ComponentState.INVALID_UNCHANGED);
        componentStates.put(ComponentName.PORTIONS, ComponentState.INVALID_UNCHANGED);

        RecipeMetadataRequest request = new RecipeMetadataRequest.Builder().
                basedOnResponse(onErrorResponse).
                setModel(
                        new RecipeMetadataRequest.Model.Builder().
                                basedOnResponseModel(onErrorResponse.getModel()).
                                setComponentStates(componentStates).
                                build()
                ).
                build();

        // Act
        handler.execute(SUT, request, new MetadataCallbackClient());

//        // Assert
//        assertEquals(
//                ComponentState.INVALID_CHANGED,
//                onErrorResponse.getMetadata().getState()
//        );
    }

    // endregion componentStatesTests

    // region persistenceTests
    @Test
    public void newRequestId_persistenceCalled_DATA_UNAVAILABLE() {
        // Arrange
        String recipeId = TestDataRecipeMetadata.NEW_RECIPE_DOMAIN_ID;
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
    public void invalidUnchanged_persistenceCalled_stateINVALID_UNCHANGED() {
        // Arrange
        String recipeId = TestDataRecipeMetadata.getInvalidUnchanged().getDomainId();

        RecipeMetadataRequest r = new RecipeMetadataRequest.Builder().getDefault().
                setDomainId(recipeId).build();
        // Act
        handler.execute(SUT, r, new MetadataCallbackClient()
        );
        // Assert
        verify(repoMock).getActiveByDomainId(eq(recipeId), repoMetadataCallback.capture()
        );
        repoMetadataCallback.getValue().onModelLoaded(TestDataRecipeMetadata.
                getInvalidUnchanged()
        );
        assertEquals(
                ComponentState.INVALID_UNCHANGED,
                onErrorResponse.getMetadata().getState()
        );
        assertTrue(onErrorResponse.getMetadata().getFailReasons().
                contains(FailReason.INVALID_COMPONENTS)
        );
    }

    @Test
    public void invalidChanged_persistenceCalled_stateINVALID_CHANGED() {
        // Arrange
        String recipeId = TestDataRecipeMetadata.getInvalidChanged().getDomainId();

        RecipeMetadataRequest r = new RecipeMetadataRequest.Builder().getDefault().
                setDomainId(recipeId).build();
        // Act
        handler.execute(SUT, r, new MetadataCallbackClient()
        );
        // Assert
        verify(repoMock).getActiveByDomainId(eq(recipeId), repoMetadataCallback.capture()
        );
        repoMetadataCallback.getValue().onModelLoaded(TestDataRecipeMetadata.
                getInvalidChanged()
        );
        assertEquals(
                ComponentState.INVALID_CHANGED,
                onErrorResponse.getMetadata().getState()
        );
        assertTrue(onErrorResponse.getMetadata().getFailReasons().
                contains(FailReason.INVALID_COMPONENTS)
        );
    }

    @Test
    public void validUnchanged_persistenceCalled_stateVALID_UNCHANGED() {
        // Arrange
        String recipeId = TestDataRecipeMetadata.getValidUnchanged().getDomainId();

        RecipeMetadataRequest r = new RecipeMetadataRequest.Builder().getDefault().
                setDomainId(recipeId).build();
        // Act
        handler.execute(SUT, r, new MetadataCallbackClient()
        );
        // Assert
        verify(repoMock).getActiveByDomainId(eq(recipeId), repoMetadataCallback.capture()
        );
        repoMetadataCallback.getValue().onModelLoaded(TestDataRecipeMetadata.getValidUnchanged()
        );
        assertEquals(
                ComponentState.VALID_UNCHANGED,
                onSuccessResponse.getMetadata().getState()
        );
        assertTrue(
                onSuccessResponse.getMetadata().getFailReasons().contains(CommonFailReason.NONE)
        );
    }

    @Test
    public void validChanged_persistenceCalled_stateVALID_CHANGED() {
        // Arrange
        String recipeId = TestDataRecipeMetadata.getValidChanged0().getDomainId();

        RecipeMetadataRequest r = new RecipeMetadataRequest.Builder().getDefault().
                setDomainId(recipeId).build();
        // Act
        handler.execute(SUT, r, new MetadataCallbackClient()
        );
        // Assert
        verify(repoMock).getActiveByDomainId(eq(recipeId), repoMetadataCallback.capture());
        repoMetadataCallback.getValue().onModelLoaded(TestDataRecipeMetadata.getValidChanged0()
        );
        assertEquals(
                ComponentState.VALID_CHANGED,
                onSuccessResponse.getMetadata().getState()
        );
        assertTrue(onSuccessResponse.getMetadata().getFailReasons().
                contains(CommonFailReason.NONE)
        );
    }

    @Test
    public void dataChanged_persistenceModelSaved() {
        // Arrange - first transaction, loads previous state
        RecipeMetadataPersistenceModel testModelOne = TestDataRecipeMetadata.getValidChanged2();
        String recipeId = testModelOne.getDomainId();

        RecipeMetadataRequest initialiseRequest = new RecipeMetadataRequest.Builder().
                getDefault().
                setDomainId(recipeId).
                build();
        // Act
        handler.execute(SUT, initialiseRequest, new MetadataCallbackClient()
        );
        // Assert
        verify(repoMock).getActiveByDomainId(eq(recipeId), repoMetadataCallback.capture()
        );
        repoMetadataCallback.getValue().onModelLoaded(testModelOne);
        assertEquals(
                ComponentState.INVALID_UNCHANGED,
                onErrorResponse.getMetadata().getState()
        );

        // Arrange - second transaction
        RecipeMetadataPersistenceModel testModelTwo = TestDataRecipeMetadata.getValidChanged1();

        RecipeMetadataRequest requestTwo = new RecipeMetadataRequest.Builder().
                basedOnResponse(onErrorResponse).
                setModel(
                        new RecipeMetadataRequest.Model.Builder().
                                basedOnResponseModel(onErrorResponse.getModel()).
                                setComponentStates(testModelTwo.getComponentStates()).
                                build()).
                build();
        // Data has changed, provide new dataId and lastUpdate for save
        when(idProviderMock.getUId()).thenReturn(testModelTwo.getDataId()
        );
        when(timeProviderMock.getCurrentTimeInMills()).thenReturn(TestDataRecipeMetadata.
                getValidChanged1().getLastUpdate()
        );
        // Act
        handler.execute(SUT, requestTwo, new MetadataCallbackClient()
        );
        // Assert model saved
        verify(repoMock).save(eq(testModelTwo));

        // Arrange - third transaction
        RecipeMetadataPersistenceModel testModelThree = TestDataRecipeMetadata.getValidChanged0();

        RecipeMetadataRequest requestThree = new RecipeMetadataRequest.Builder().
                basedOnResponse(onErrorResponse).
                setModel(
                        new RecipeMetadataRequest.Model.Builder().
                                basedOnResponseModel(onErrorResponse.getModel()).
                                setComponentStates(testModelThree.getComponentStates()).
                                build()).
                build();
        // Data has changed again, provide new dataId and lastUpdate for save
        when(idProviderMock.getUId()).thenReturn(testModelThree.getDataId()
        );
        when(timeProviderMock.getCurrentTimeInMills()).thenReturn(TestDataRecipeMetadata.
                getValidChanged0().getLastUpdate()
        );
        // Act
        handler.execute(SUT, requestThree, new MetadataCallbackClient()
        );
        // Assert new model saved
        verify(repoMock).save(eq(testModelThree)
        );
    }
    // endregion persistenceTests

    // region helper methods -----------------------------------------------------------------------
    private void givenNewIdAsInitialRequest(String domainId) {
        // Arrange/execute initial request
        when(timeProviderMock.getCurrentTimeInMills()).thenReturn(TestDataRecipeMetadata.
                getDataUnavailable().getCreateDate());

        when(idProviderMock.getUId()).thenReturn(TestDataRecipeMetadata.
                getDataUnavailable().getDataId());

        RecipeMetadataRequest initialRequest = new RecipeMetadataRequest.Builder().
                getDefault().
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
        public void onUseCaseSuccess(RecipeMetadataResponse response) {
            System.out.println(RecipeMetadataTest.TAG + TAG + "onSuccess: " + response);
            onSuccessResponse = response;
        }

        @Override
        public void onUseCaseError(RecipeMetadataResponse response) {
            System.out.println(RecipeMetadataTest.TAG + TAG + "onError: " + response);
            onErrorResponse = response;
        }
    }
    // endregion helper classes --------------------------------------------------------------------
}