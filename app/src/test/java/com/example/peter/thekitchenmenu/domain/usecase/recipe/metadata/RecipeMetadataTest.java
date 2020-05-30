package com.example.peter.thekitchenmenu.domain.usecase.recipe.metadata;

import com.example.peter.thekitchenmenu.app.Constants;
import com.example.peter.thekitchenmenu.commonmocks.UseCaseSchedulerMock;
import com.example.peter.thekitchenmenu.data.repository.DomainDataAccess.GetDomainModelCallback;
import com.example.peter.thekitchenmenu.data.repository.recipe.RepositoryRecipeMetadata;
import com.example.peter.thekitchenmenu.data.repository.recipe.metadata.TestDataRecipeMetadata;
import com.example.peter.thekitchenmenu.domain.usecase.common.failreasons.CommonFailReason;
import com.example.peter.thekitchenmenu.domain.usecase.common.failreasons.FailReasons;
import com.example.peter.thekitchenmenu.domain.usecase.common.UseCaseBase;
import com.example.peter.thekitchenmenu.domain.usecase.common.UseCaseHandler;
import com.example.peter.thekitchenmenu.domain.model.UseCaseMetadataModel;
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
import java.util.Set;

import static com.example.peter.thekitchenmenu.domain.usecase.recipe.component.metadata.RecipeMetadata.FailReason;
import static org.junit.Assert.assertArrayEquals;
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
    private HashMap<ComponentName, ComponentState> componentStatesUnderTest;
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
        componentStatesUnderTest = new HashMap<>();

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

    @Test
    public void requestNoDomainId_useCaseNoDomainId_emptyStateReturned() {
        // Arrange
        RecipeMetadataRequest request = new RecipeMetadataRequest.Builder().getDefault().build();
        // Act
        SUT.execute(request, new MetadataCallbackClient()); // NON THREADED

        // Assert
        RecipeMetadataResponse expectedResponse = new RecipeMetadataResponse.Builder().
                getDefault().
                build();
        RecipeMetadataResponse actualResponse = onErrorResponse;

        assertEquals(
                expectedResponse,
                actualResponse
        );
    }

    // This test is large because it tests the initial request on which the remainder of tests
    // depend, therefore an assertion is performed on every data element.
    @Test
    public void testNewInitialRequest() {
        // Arrange
        RecipeMetadataPersistenceModel modelUnderTest = TestDataRecipeMetadata.getDataUnavailable();
        // Expected default component states
        componentStatesUnderTest = TestDataRecipeMetadata.getInvalidUnchangedComponentStates();

        String recipeId = TestDataRecipeMetadata.NEW_RECIPE_DOMAIN_ID;

        // Act
        givenNewIdAsInitialRequest(recipeId);

        // Assert response values
        RecipeMetadataResponse response = onErrorResponse;

        String expectedDataId = modelUnderTest.getDataId();
        String actualDataId = response.getDataId();
        assertEquals(
                expectedDataId,
                actualDataId
        );

        String expectedDomainId = modelUnderTest.getDomainId();
        String actualDomainId = response.getDomainId();
        assertEquals(
                expectedDomainId,
                actualDomainId
        );

        // Assert metadata state
        UseCaseMetadataModel metadata = response.getMetadata();

        ComponentState expectedRecipeState = modelUnderTest.getRecipeState();
        ComponentState actualRecipeState = metadata.getComponentState();
        assertEquals(
                expectedRecipeState,
                actualRecipeState
        );

        // Assert fail reasons
        FailReasons[] expectedFailReasons = new FailReasons[]{
                CommonFailReason.DATA_UNAVAILABLE,
                RecipeMetadata.FailReason.MISSING_COMPONENTS};
        FailReasons[] actualFailReasons = metadata.getFailReasons().toArray(new FailReasons[0]);
        assertArrayEquals(
                expectedFailReasons,
                actualFailReasons
        );

        String expectedUserId = Constants.getUserId();
        String actualUserId = metadata.getCreatedBy();
        assertEquals(
                expectedUserId,
                actualUserId
        );

        long expectedCreateDate = modelUnderTest.getCreateDate();
        long actualCreateDate = metadata.getCreateDate();
        assertEquals(
                expectedCreateDate,
                actualCreateDate
        );

        long expectedLastUpdate = modelUnderTest.getLastUpdate();
        long actualLastUpdate = metadata.getLasUpdate();
        assertEquals(
                expectedLastUpdate,
                actualLastUpdate
        );

        // Assert response model
        RecipeMetadataResponse.Model model = onErrorResponse.getModel();

        String expectedParentDomainId = modelUnderTest.getParentDomainId();
        String actualParentDomainId = model.getParentDomainId();
        assertEquals(
                expectedParentDomainId,
                actualParentDomainId
        );

        // Assert componentStates
        HashMap<ComponentName, ComponentState> expectedComponentStates = componentStatesUnderTest;
        HashMap<ComponentName, ComponentState> actualComponentStates = model.getComponentStates();
        assertEquals(
                expectedComponentStates,
                actualComponentStates
        );
    }

    // region componentStatesTests
    @Test
    public void missingComponent_stateDATA_UNAVAILABLE_failReasonMISSING_COMPONENTS() {
        // Arrange/execute initial request
        String recipeId = TestDataRecipeMetadata.NEW_RECIPE_DOMAIN_ID;
        givenNewIdAsInitialRequest(recipeId);

        // Arrange MISSING_COMPONENT state (portions missing)
        componentStatesUnderTest = TestDataRecipeMetadata.getInvalidMissingComponentsStates();

        RecipeMetadataRequest.Model model = new RecipeMetadataRequest.Model.Builder().
                basedOnResponseModel(onErrorResponse.getModel()).
                setComponentStates(componentStatesUnderTest).
                build();

        RecipeMetadataRequest request = new RecipeMetadataRequest.Builder().
                basedOnResponse(onErrorResponse).
                setDomainModel(model).
                build();

        // Act
        handler.executeAsync(SUT, request, new MetadataCallbackClient());
        // Assert
        assertEquals(
                ComponentState.INVALID_UNCHANGED,
                onErrorResponse.getMetadata().getComponentState()
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
        componentStatesUnderTest = TestDataRecipeMetadata.getInvalidUnchangedComponentStates();

        RecipeMetadataRequest.Model model = new RecipeMetadataRequest.Model.Builder().
                basedOnResponseModel(onErrorResponse.getModel()).
                setComponentStates(componentStatesUnderTest).
                build();

        RecipeMetadataRequest r = new RecipeMetadataRequest.Builder().
                basedOnResponse(onErrorResponse).
                setDomainModel(model).
                build();

        // Act
        handler.executeAsync(SUT, r, new MetadataCallbackClient());

        // Assert
        UseCaseMetadataModel metadata = onErrorResponse.getMetadata();

        ComponentState expectedComponentState = ComponentState.INVALID_UNCHANGED;
        ComponentState actualComponentState = metadata.getComponentState();
        assertEquals(
                expectedComponentState,
                actualComponentState
        );

        FailReasons[] expectedFailReasons = new FailReasons[]{FailReason.INVALID_COMPONENTS};
        FailReasons[] actualFailReasons = metadata.getFailReasons().toArray(new FailReasons[0]);
        assertArrayEquals(
                expectedFailReasons,
                actualFailReasons
        );
    }

    @Test
    public void invalidChangedComponent_stateINVALID_CHANGED_failReasonINVALID_COMPONENTS() {
        // Arrange
        String recipeId = TestDataRecipeMetadata.NEW_RECIPE_DOMAIN_ID;
        givenNewIdAsInitialRequest(recipeId);

        // Arrange INVALID_CHANGED / INVALID_COMPONENTS state
        componentStatesUnderTest = TestDataRecipeMetadata.getInvalidChangedComponentStates();

        RecipeMetadataRequest.Model model = new RecipeMetadataRequest.Model.Builder().
                basedOnResponseModel(onErrorResponse.getModel()).
                setComponentStates(componentStatesUnderTest).
                build();

        RecipeMetadataRequest request = new RecipeMetadataRequest.Builder().
                basedOnResponse(onErrorResponse).
                setDomainModel(model).
                build();

        // Act
        handler.executeAsync(SUT, request, new MetadataCallbackClient());

        // Assert
        UseCaseMetadataModel metadata = onErrorResponse.getMetadata();

        ComponentState expectedComponentState = ComponentState.INVALID_CHANGED;
        ComponentState actualComponentState = metadata.getComponentState();
        assertEquals(
                expectedComponentState,
                actualComponentState
        );

        FailReasons[] expectedFailReasons = new FailReasons[]{FailReason.INVALID_COMPONENTS};
        FailReasons[] actualFailReasons = metadata.getFailReasons().toArray(new FailReasons[0]);
        assertArrayEquals(
                expectedFailReasons,
                actualFailReasons
        );
    }

    @Test
    public void validUnchangedComponent_stateVALID_UNCHANGED_failReasonNONE() {
        // Arrange
        String recipeId = TestDataRecipeMetadata.NEW_RECIPE_DOMAIN_ID;
        givenNewIdAsInitialRequest(recipeId);

        // Arrange VALID_UNCHANGED / CommonFailReason.NONE state
        componentStatesUnderTest = TestDataRecipeMetadata.getValidUnchangedComponentStates();

        RecipeMetadataRequest.Model model = new RecipeMetadataRequest.Model.Builder().
                basedOnResponseModel(onErrorResponse.getModel()).
                setComponentStates(componentStatesUnderTest).
                build();

        RecipeMetadataRequest request = new RecipeMetadataRequest.Builder().
                basedOnResponse(onErrorResponse).
                setDomainModel(model).
                build();

        // Act
        handler.executeAsync(SUT, request, new MetadataCallbackClient()
        );

        // Assert
        UseCaseMetadataModel metadata = onSuccessResponse.getMetadata();

        ComponentState expectedRecipeState = ComponentState.VALID_UNCHANGED;
        ComponentState actualRecipeState = metadata.getComponentState();
        assertEquals(
                expectedRecipeState,
                actualRecipeState
        );

        FailReasons[] expectedFailReasons = new FailReasons[]{CommonFailReason.NONE};
        FailReasons[] actualFailReasons = metadata.getFailReasons().toArray(new FailReasons[0]);
        assertArrayEquals(
                expectedFailReasons,
                actualFailReasons
        );
    }

    @Test
    public void validChangedComponent_recipeCourse_INVALID_UNCHANGED() {
        // Arrange
        String recipeId = TestDataRecipeMetadata.NEW_RECIPE_DOMAIN_ID;
        givenNewIdAsInitialRequest(recipeId);

        // Arrange VALID_UNCHANGED / CommonFailReason.NONE state
        componentStatesUnderTest = TestDataRecipeMetadata.getInvalidUnchangedComponentStates();

        // Assert
        RecipeMetadataRequest request = new RecipeMetadataRequest.Builder().
                basedOnResponse(onErrorResponse).
                setDomainModel(
                        new RecipeMetadataRequest.Model.Builder().
                                basedOnResponseModel(onErrorResponse.getModel()).
                                setComponentStates(componentStatesUnderTest).build()).
                build();

        // Act
        handler.executeAsync(SUT, request, new MetadataCallbackClient()
        );
        // Assert
        UseCaseMetadataModel metadata = onErrorResponse.getMetadata();

        ComponentState expectedComponentState = ComponentState.INVALID_UNCHANGED;
        ComponentState actualComponentState = metadata.getComponentState();
        assertEquals(
                expectedComponentState,
                actualComponentState
        );

        FailReasons[] expectedFailReasons = new FailReasons[]{FailReason.INVALID_COMPONENTS};
        FailReasons[] actualFailReasons = metadata.getFailReasons().toArray(new FailReasons[0]);
        assertArrayEquals(
                expectedFailReasons,
                actualFailReasons
        );
    }

    @Test
    public void validChanged_stateVALID_CHANGED_failReasonNONE() {
        // Arrange
        String recipeId = TestDataRecipeMetadata.NEW_RECIPE_DOMAIN_ID;
        givenNewIdAsInitialRequest(recipeId);

        // Arrange VALID_CHANGED / CommonFailReason.NONE state
        componentStatesUnderTest = TestDataRecipeMetadata.getValidChangedComponentStates();

        RecipeMetadataRequest.Model model = new RecipeMetadataRequest.Model.Builder().
                getDefault().
                setComponentStates(componentStatesUnderTest).
                build();

        RecipeMetadataRequest request = new RecipeMetadataRequest.Builder().
                basedOnResponse(onErrorResponse).
                setDomainModel(model).
                build();

        // Act
        handler.executeAsync(SUT, request, new MetadataCallbackClient());

        // Assert
        UseCaseMetadataModel metadata = onSuccessResponse.getMetadata();

        ComponentState expectedComponentState = ComponentState.VALID_CHANGED;
        ComponentState actualComponentState = metadata.getComponentState();
        assertEquals(
                expectedComponentState,
                actualComponentState
        );

        FailReasons[] expectedFailReasons = new FailReasons[]{CommonFailReason.NONE};
        FailReasons[] actualFailReasons = metadata.getFailReasons().toArray(new FailReasons[0]);
        assertArrayEquals(
                expectedFailReasons,
                actualFailReasons
        );
    }

    @Test
    public void invalidChanged_stateINVALID_CHANGED() {
        // Arrange
        String recipeId = TestDataRecipeMetadata.NEW_RECIPE_DOMAIN_ID;
        // Arrange and execute first request
        givenNewIdAsInitialRequest(recipeId);

        // Arrange second request
        componentStatesUnderTest = TestDataRecipeMetadata.getInvalidUnchangedComponentStates();

        RecipeMetadataRequest secondRequest = new RecipeMetadataRequest.Builder().
                basedOnResponse(onErrorResponse).
                setDomainModel(
                        new RecipeMetadataRequest.Model.Builder().
                                basedOnResponseModel(onErrorResponse.getModel()).
                                setComponentStates(componentStatesUnderTest).
                                build()).
                build();

        // Act - execute second request
        handler.executeAsync(SUT, secondRequest, new MetadataCallbackClient());

        // Arrange for third request
        componentStatesUnderTest = TestDataRecipeMetadata.getInvalidChangedComponentStates();

        RecipeMetadataRequest request = new RecipeMetadataRequest.Builder().
                basedOnResponse(onErrorResponse).
                setDomainModel(
                        new RecipeMetadataRequest.Model.Builder().
                                basedOnResponseModel(onErrorResponse.getModel()).
                                setComponentStates(componentStatesUnderTest).
                                build()
                ).
                build();

        // Act
        handler.executeAsync(SUT, request, new MetadataCallbackClient());

        // Assert
        UseCaseMetadataModel metadata = onErrorResponse.getMetadata();

        ComponentState expectedComponentState = ComponentState.INVALID_CHANGED;
        ComponentState actualComponentState = metadata.getComponentState();
        assertEquals(
                expectedComponentState,
                actualComponentState
        );

        FailReasons[] expectedFailReasons = new FailReasons[]{FailReason.INVALID_COMPONENTS};
        FailReasons[] actualFailReasons = metadata.getFailReasons().toArray(new FailReasons[0]);
        assertArrayEquals(
                expectedFailReasons,
                actualFailReasons
        );
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
        UseCaseMetadataModel metadata = onErrorResponse.getMetadata();

        ComponentState expectedComponentState = ComponentState.INVALID_UNCHANGED;
        ComponentState actualComponentState = metadata.getComponentState();
        assertEquals(
                expectedComponentState,
                actualComponentState
        );

        FailReasons[] expectedFailReasons = new FailReasons[]{
                CommonFailReason.DATA_UNAVAILABLE,
                FailReason.MISSING_COMPONENTS};
        FailReasons[] actualFailReasons = metadata.getFailReasons().toArray(new FailReasons[0]);
        assertArrayEquals(
                expectedFailReasons,
                actualFailReasons
        );
    }

    @Test
    public void invalidUnchanged_persistenceCalled_stateINVALID_UNCHANGED() {
        // Arrange
        RecipeMetadataPersistenceModel modelUnderTest = TestDataRecipeMetadata.getInvalidUnchanged();
        String recipeId = modelUnderTest.getDomainId();

        RecipeMetadataRequest r = new RecipeMetadataRequest.Builder().
                getDefault().
                setDomainId(recipeId).
                build();
        // Act
        handler.executeAsync(SUT, r, new MetadataCallbackClient());

        // Assert
        verify(repoMock).getActiveByDomainId(eq(recipeId), repoMetadataCallback.capture());
        repoMetadataCallback.getValue().onModelLoaded(modelUnderTest);

        UseCaseMetadataModel metadata = onErrorResponse.getMetadata();

        ComponentState expectedComponentState = ComponentState.INVALID_UNCHANGED;
        ComponentState actualComponentState = metadata.getComponentState();
        assertEquals(
                expectedComponentState,
                actualComponentState
        );

        FailReasons[] expectedFailReasons = new FailReasons[]{FailReason.INVALID_COMPONENTS};
        FailReasons[] actualFailReasons = metadata.getFailReasons().toArray(new FailReasons[0]);
        assertArrayEquals(
                expectedFailReasons,
                actualFailReasons
        );
    }

    @Test
    public void invalidChanged_persistenceCalled_stateINVALID_CHANGED() {
        // Arrange
        RecipeMetadataPersistenceModel modelUnderTest = TestDataRecipeMetadata.getInvalidChanged();
        String recipeId = modelUnderTest.getDomainId();

        RecipeMetadataRequest r = new RecipeMetadataRequest.Builder().
                getDefault().
                setDomainId(recipeId).
                build();
        // Act
        handler.executeAsync(SUT, r, new MetadataCallbackClient()
        );
        // Assert
        verify(repoMock).getActiveByDomainId(eq(recipeId), repoMetadataCallback.capture());
        repoMetadataCallback.getValue().onModelLoaded(modelUnderTest);

        UseCaseMetadataModel metadata = onErrorResponse.getMetadata();

        ComponentState expectedComponentState = ComponentState.INVALID_CHANGED;
        ComponentState actualComponentState = metadata.getComponentState();
        assertEquals(
                expectedComponentState,
                actualComponentState
        );

        FailReasons[] expectedFailReasons = new FailReasons[]{FailReason.INVALID_COMPONENTS};
        FailReasons[] actualFailReasons = metadata.getFailReasons().toArray(new FailReasons[0]);
        assertArrayEquals(
                expectedFailReasons,
                actualFailReasons
        );
    }

    @Test
    public void validUnchanged_persistenceCalled_stateVALID_UNCHANGED() {
        // Arrange
        RecipeMetadataPersistenceModel modelUnderTest = TestDataRecipeMetadata.getValidUnchanged();
        String recipeId = modelUnderTest.getDomainId();

        RecipeMetadataRequest r = new RecipeMetadataRequest.Builder().
                getDefault().
                setDomainId(recipeId).
                build();
        // Act
        handler.executeAsync(SUT, r, new MetadataCallbackClient());

        // Assert
        verify(repoMock).getActiveByDomainId(eq(recipeId), repoMetadataCallback.capture());
        repoMetadataCallback.getValue().onModelLoaded(modelUnderTest);

        UseCaseMetadataModel metadata = onSuccessResponse.getMetadata();

        ComponentState expectedComponentState = ComponentState.VALID_UNCHANGED;
        ComponentState actualComponentState = metadata.getComponentState();
        assertEquals(
                expectedComponentState,
                actualComponentState
        );

        FailReasons[] expectedFailReasons = new FailReasons[]{CommonFailReason.NONE};
        FailReasons[] actualFailReasons = metadata.getFailReasons().toArray(new FailReasons[0]);
        assertArrayEquals(
                expectedFailReasons,
                actualFailReasons
        );
    }

    @Test
    public void requestNoDomainId_useCaseHasDomainId_currentStateReturned() {
        // Arrange
        // Load data from existing recipe
        validUnchanged_persistenceCalled_stateVALID_UNCHANGED();
        // Request current state returned by sending empty request
        RecipeMetadataRequest request = new RecipeMetadataRequest.Builder().getDefault().build();
        // Act
        handler.executeAsync(SUT, request, new MetadataCallbackClient());
        // Assert
        UseCaseMetadataModel metadata = onSuccessResponse.getMetadata();

        ComponentState expectedComponentState = ComponentState.VALID_UNCHANGED;
        ComponentState actualComponentState = metadata.getComponentState();
        assertEquals(
                expectedComponentState,
                actualComponentState
        );

        FailReasons[] expectedFailReasons = new FailReasons[]{CommonFailReason.NONE};
        FailReasons[] actualFailReasons = metadata.getFailReasons().toArray(new FailReasons[0]);
        assertArrayEquals(
                expectedFailReasons,
                actualFailReasons
        );
    }

    @Test
    public void validChanged_persistenceCalled_stateVALID_CHANGED() {
        // Arrange
        RecipeMetadataPersistenceModel modelUnderTest = TestDataRecipeMetadata.getValidChangedThree();
        String recipeId = modelUnderTest.getDomainId();

        RecipeMetadataRequest r = new RecipeMetadataRequest.Builder().
                getDefault().
                setDomainId(recipeId).
                build();
        // Act
        handler.executeAsync(SUT, r, new MetadataCallbackClient()
        );
        // Assert
        verify(repoMock).getActiveByDomainId(eq(recipeId), repoMetadataCallback.capture());
        repoMetadataCallback.getValue().onModelLoaded(modelUnderTest);

        UseCaseMetadataModel metadata = onSuccessResponse.getMetadata();

        ComponentState expectedComponentState = ComponentState.VALID_CHANGED;
        ComponentState actualComponentState = metadata.getComponentState();
        assertEquals(
                expectedComponentState,
                actualComponentState
        );

        FailReasons[] expectedFailReasons = new FailReasons[]{CommonFailReason.NONE};
        FailReasons[] actualFailReasons = metadata.getFailReasons().toArray(new FailReasons[0]);
        assertArrayEquals(
                expectedFailReasons,
                actualFailReasons
        );
    }

    @Test
    public void dataChanged_persistenceModelSaved() {
        // Arrange - first transaction, loads previous state
        RecipeMetadataPersistenceModel modelUnderTestOne = TestDataRecipeMetadata.getValidChangedOne();
        String recipeId = modelUnderTestOne.getDomainId();

        RecipeMetadataRequest initialiseRequest = new RecipeMetadataRequest.Builder().
                getDefault().
                setDomainId(recipeId).
                build();
        // Act
        handler.executeAsync(SUT, initialiseRequest, new MetadataCallbackClient()
        );

        // Assert
        verify(repoMock).getActiveByDomainId(eq(recipeId), repoMetadataCallback.capture());
        repoMetadataCallback.getValue().onModelLoaded(modelUnderTestOne);

        UseCaseMetadataModel metadata = onErrorResponse.getMetadata();

        ComponentState expectedModelUnderTestOneState = ComponentState.INVALID_UNCHANGED;
        ComponentState actualModelUnderTestOneState = metadata.getComponentState();
        assertEquals(
                expectedModelUnderTestOneState,
                actualModelUnderTestOneState
        );

        // Arrange - second transaction
        RecipeMetadataPersistenceModel modelUnderTestTwo = TestDataRecipeMetadata.getValidChangedTwo();

        RecipeMetadataRequest requestTwo = new RecipeMetadataRequest.Builder().
                basedOnResponse(onErrorResponse).
                setDomainModel(
                        new RecipeMetadataRequest.Model.Builder().
                                basedOnResponseModel(onErrorResponse.getModel()).
                                setComponentStates(modelUnderTestTwo.getComponentStates()).
                                build()).
                build();
        // Data has changed, provide new dataId and lastUpdate for save
        when(idProviderMock.getUId()).thenReturn(modelUnderTestTwo.getDataId()
        );
        when(timeProviderMock.getCurrentTimeInMills()).thenReturn(TestDataRecipeMetadata.
                getValidChangedTwo().getLastUpdate()
        );

        // Act
        handler.executeAsync(SUT, requestTwo, new MetadataCallbackClient()
        );

        // Assert model saved
        verify(repoMock).save(eq(modelUnderTestTwo));

        // Arrange - third transaction
        RecipeMetadataPersistenceModel testModelThree = TestDataRecipeMetadata.getValidChangedThree();

        RecipeMetadataRequest requestThree = new RecipeMetadataRequest.Builder().
                basedOnResponse(onErrorResponse).
                setDomainModel(
                        new RecipeMetadataRequest.Model.Builder().
                                basedOnResponseModel(onErrorResponse.getModel()).
                                setComponentStates(testModelThree.getComponentStates()).
                                build()).
                build();
        // Data has changed again, provide new dataId and lastUpdate for save
        when(idProviderMock.getUId()).thenReturn(testModelThree.getDataId()
        );
        when(timeProviderMock.getCurrentTimeInMills()).thenReturn(TestDataRecipeMetadata.
                getValidChangedThree().getLastUpdate()
        );

        // Act
        handler.executeAsync(SUT, requestThree, new MetadataCallbackClient()
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

        handler.executeAsync(SUT, initialRequest, new MetadataCallbackClient());

        whenRepoCalledReturnDataUnavailable(domainId);
    }

    private void whenRepoCalledReturnDataUnavailable(String domainId) {
        verify(repoMock).getActiveByDomainId(eq(domainId), repoMetadataCallback.capture());
        repoMetadataCallback.getValue().onModelUnavailable();
    }
    // endregion helper methods --------------------------------------------------------------------

    // region helper classes -----------------------------------------------------------------------
    private class MetadataCallbackClient
            implements
            UseCaseBase.Callback<RecipeMetadataResponse> {
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