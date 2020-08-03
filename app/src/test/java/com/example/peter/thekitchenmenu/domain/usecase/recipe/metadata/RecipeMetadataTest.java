package com.example.peter.thekitchenmenu.domain.usecase.recipe.metadata;

import com.example.peter.thekitchenmenu.commonmocks.UseCaseSchedulerMock;
import com.example.peter.thekitchenmenu.data.repository.DomainDataAccess.GetDomainModelCallback;
import com.example.peter.thekitchenmenu.data.repository.recipe.DataAccessRecipeMetadata;
import com.example.peter.thekitchenmenu.data.repository.recipe.metadata.TestDataRecipeMetadata;
import com.example.peter.thekitchenmenu.domain.usecasenew.common.metadata.UseCaseMetadataModel;
import com.example.peter.thekitchenmenu.domain.usecase.common.UseCaseBase;
import com.example.peter.thekitchenmenu.domain.usecasenew.common.UseCaseHandler;
import com.example.peter.thekitchenmenu.domain.usecasenew.common.failreasons.CommonFailReason;
import com.example.peter.thekitchenmenu.domain.usecasenew.common.failreasons.FailReasons;
import com.example.peter.thekitchenmenu.domain.usecase.recipe.component.metadata.RecipeMetadata;
import com.example.peter.thekitchenmenu.domain.usecase.recipe.component.metadata.RecipeMetadata.ComponentName;
import com.example.peter.thekitchenmenu.domain.usecasenew.common.metadata.ComponentState;
import com.example.peter.thekitchenmenu.domain.usecase.recipe.component.metadata.RecipeMetadata.FailReason;
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
import java.util.List;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

public class RecipeMetadataTest {
    private static final String TAG = "tkm-" + RecipeMetadataTest.class.getSimpleName() + ": ";

    // region constants ----------------------------------------------------------------------------
    // endregion constants -------------------------------------------------------------------------

    // region helper fields ------------------------------------------------------------------------
    @Mock
    DataAccessRecipeMetadata repoMock;
    @Captor
    ArgumentCaptor<GetDomainModelCallback<RecipeMetadataPersistenceModel>> repoMetadataCallback;
    @Mock
    UniqueIdProvider idProviderMock;
    @Mock
    TimeProvider timeProviderMock;

    private UseCaseHandler handler;
    private HashMap<ComponentName, ComponentState> expectedComponentStates;
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
        expectedComponentStates = new HashMap<>();

        return new RecipeMetadata(
                repoMock,
                idProviderMock,
                timeProviderMock,
                TestDataRecipeMetadata.requiredComponentNames,
                TestDataRecipeMetadata.additionalComponentNames
        );
    }

    // region defaultValueTests
    @Test
    public void defaultRequest_sentToDefaultUseCase_stateINVALID_DEFAULT() {
        // Arrange
        // A default request always returns the current state of the use case.
        // As this use case is currently 'empty' the default request should return the use case's
        // default values.
        RecipeMetadataPersistenceModel modelUnderTest = TestDataRecipeMetadata.
                getDefaultState();

        // arrange default request
        RecipeMetadataRequest request = new RecipeMetadataRequest.Builder().getDefault().build();

        // Act
        SUT.execute(request, new MetadataCallbackClient());

        // Assert
        UseCaseMetadataModel metadata = onErrorResponse.getMetadata();
        RecipeMetadataResponse.DomainModel model = onErrorResponse.getDomainModel();

        // assert no id's or times generated and no persistent access
        verifyNoMoreInteractions(idProviderMock);
        verifyNoMoreInteractions(timeProviderMock);
        verifyNoMoreInteractions(repoMock);

        // assert recipe state
        ComponentState expectedComponentState = modelUnderTest.getComponentState();
        ComponentState actualComponentState = metadata.getComponentState();
        assertEquals(
                expectedComponentState,
                actualComponentState
        );

        // assert recipe components states
        HashMap<ComponentName, ComponentState> expectedComponentStates = modelUnderTest.
                getComponentStates();
        HashMap<ComponentName, ComponentState> actualComponentStates = model.
                getComponentStates();
        assertEquals(
                expectedComponentStates,
                actualComponentStates
        );

        // assert fail reasons
        List<FailReasons> expectedFailReasons = modelUnderTest.getFailReasons();
        List<FailReasons> actualFailReasons = metadata.getFailReasons();
        assertEquals(
                expectedFailReasons,
                actualFailReasons
        );
    }

    @Test
    public void newRequest_sentToDefaultUseCase_stateINVALID_DEFAULT() {
        // Arrange
        // This test is large as it tests the initial request on which most other tests
        // depend, therefore an assertion is performed on every data element.
        RecipeMetadataPersistenceModel modelUnderTest = TestDataRecipeMetadata.
                getInvalidDefault();

        RecipeMetadataRequest initialRequest = new RecipeMetadataRequest.Builder().
                getDefault().
                setDomainId(modelUnderTest.getDomainId()).
                build();

        // Act
        SUT.execute(initialRequest, new MetadataCallbackClient());

        // Assert
        // assert correct id requested from persistence
        verify(repoMock).getByDomainId(eq(modelUnderTest.getDomainId()),
                repoMetadataCallback.capture()
        );
        repoMetadataCallback.getValue().onPersistenceModelUnavailable();

        // verify default values not saved
        verifyNoMoreInteractions(timeProviderMock);
        verifyNoMoreInteractions(idProviderMock);
        verifyNoMoreInteractions(repoMock);

        // Assert response values
        RecipeMetadataResponse response = onErrorResponse;
        UseCaseMetadataModel metadata = response.getMetadata();
        RecipeMetadataResponse.DomainModel domainModel = onErrorResponse.getDomainModel();

        // assert id's
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

        // assert metadata state
        // assert overall recipe component state
        ComponentState expectedRecipeState = modelUnderTest.getComponentState();
        ComponentState actualRecipeState = metadata.getComponentState();
        assertEquals(
                expectedRecipeState,
                actualRecipeState
        );

        // assert fail reasons
        List<FailReasons> expectedFailReasons = modelUnderTest.getFailReasons();
        List<FailReasons> actualFailReasons = metadata.getFailReasons();
        assertEquals(
                expectedFailReasons,
                actualFailReasons
        );

        // assert user id
        String expectedUserId = modelUnderTest.getCreatedBy();
        String actualUserId = metadata.getCreatedBy();
        assertEquals(
                expectedUserId,
                actualUserId
        );

        // assert timestamps, nothing saved, so default 0L
        long expectedCreateDate = modelUnderTest.getCreateDate();
        long actualCreateDate = metadata.getCreateDate();
        assertEquals(
                expectedCreateDate,
                actualCreateDate
        );

        long expectedLastUpdate = modelUnderTest.getLastUpdate();
        long actualLastUpdate = metadata.getLastUpdate();
        assertEquals(
                expectedLastUpdate,
                actualLastUpdate
        );

        // assert response model
        String expectedParentDomainId = modelUnderTest.getParentDomainId();
        String actualParentDomainId = domainModel.getParentDomainId();
        assertEquals(
                expectedParentDomainId,
                actualParentDomainId
        );

        // assert componentStates
        HashMap<ComponentName, ComponentState> expectedComponentStates = modelUnderTest.
                getComponentStates();
        HashMap<ComponentName, ComponentState> actualComponentStates = domainModel.
                getComponentStates();
        assertEquals(
                expectedComponentStates,
                actualComponentStates
        );
    }
    // endregion defaultValueTests

    // region componentStatesTests
    @Test
    public void newRequest_missingComponent_failReasonMISSING_REQUIRED_COMPONENTSStateINVALID_CHANGED() {
        // Arrange
        RecipeMetadataPersistenceModel modelUnderTest = TestDataRecipeMetadata.
                getInvalidMissingComponents();

        // arrange / act initial request
        newRequest_sentToDefaultUseCase_stateINVALID_DEFAULT();

        // Arrange MISSING_REQUIRED_COMPONENTS state (identity missing)
        RecipeMetadataRequest.DomainModel model = new RecipeMetadataRequest.DomainModel.Builder().
                basedOnResponseModel(onErrorResponse.getDomainModel()).
                setComponentStates(modelUnderTest.getComponentStates()).
                build();

        RecipeMetadataRequest request = new RecipeMetadataRequest.Builder().
                basedOnResponse(onErrorResponse).
                setDomainModel(model).
                build();

        // arrange id and time for save
        when(idProviderMock.getUId()).thenReturn(modelUnderTest.getDataId());
        when(timeProviderMock.getCurrentTimeInMills()).thenReturn(modelUnderTest.getCreateDate());

        // Act
        SUT.execute(request, new MetadataCallbackClient());
        // Assert
        // assert save
        verify(repoMock).save(eq(modelUnderTest));

        UseCaseMetadataModel metadata = onErrorResponse.getMetadata();

        // assert component state
        ComponentState expectedComponentState = modelUnderTest.getComponentState();
        ComponentState actualComponentState = metadata.getComponentState();
        assertEquals(
                expectedComponentState,
                actualComponentState
        );

        // assert fail reasons
        List<FailReasons> expectedFailReasons = modelUnderTest.getFailReasons();
        List<FailReasons> actualFailReasons = metadata.getFailReasons();
        assertEquals(
                expectedFailReasons,
                actualFailReasons
        );
    }

    @Test
    public void newRequest_invalidChangedComponent_stateINVALID_CHANGED() {
        // Arrange
        RecipeMetadataPersistenceModel modelUnderTest = TestDataRecipeMetadata.getInvalidChanged();

        newRequest_sentToDefaultUseCase_stateINVALID_DEFAULT();

        RecipeMetadataRequest.DomainModel model = new RecipeMetadataRequest.DomainModel.Builder().
                basedOnResponseModel(onErrorResponse.getDomainModel()).
                setComponentStates(modelUnderTest.getComponentStates()).
                build();

        RecipeMetadataRequest request = new RecipeMetadataRequest.Builder().
                basedOnResponse(onErrorResponse).
                setDomainModel(model).
                build();

        when(idProviderMock.getUId()).thenReturn(modelUnderTest.getDataId());
        when(timeProviderMock.getCurrentTimeInMills()).thenReturn(modelUnderTest.getCreateDate());

        // Act
        SUT.execute(request, new MetadataCallbackClient());

        // Assert
        // assert save
        verify(repoMock).save(eq(modelUnderTest));

        // assert response
        UseCaseMetadataModel metadata = onErrorResponse.getMetadata();

        ComponentState expectedComponentState = ComponentState.INVALID_CHANGED;
        ComponentState actualComponentState = metadata.getComponentState();
        assertEquals(
                expectedComponentState,
                actualComponentState
        );

        List<FailReasons> expectedFailReasons = modelUnderTest.getFailReasons();
        List<FailReasons> actualFailReasons = metadata.getFailReasons();
        assertEquals(
                expectedFailReasons,
                actualFailReasons
        );
    }

    @Test
    public void newRequest_validChangedComponent_stateVALID_CHANGED() {
        // Arrange
        RecipeMetadataPersistenceModel modelUnderTest = TestDataRecipeMetadata.
                getValidChanged();

        newRequest_sentToDefaultUseCase_stateINVALID_DEFAULT();

        RecipeMetadataRequest.DomainModel model = new RecipeMetadataRequest.DomainModel.Builder().
                basedOnResponseModel(onErrorResponse.getDomainModel()).
                setComponentStates(modelUnderTest.getComponentStates()).
                build();

        RecipeMetadataRequest request = new RecipeMetadataRequest.Builder().
                basedOnResponse(onErrorResponse).
                setDomainModel(model).
                build();

        when(idProviderMock.getUId()).thenReturn(modelUnderTest.getDataId());
        when(timeProviderMock.getCurrentTimeInMills()).thenReturn(modelUnderTest.getCreateDate());

        // Act
        SUT.execute(request, new MetadataCallbackClient());

        // Assert
        // assert save
        verify(repoMock).save(eq(modelUnderTest));

        // Assert
        UseCaseMetadataModel metadata = onSuccessResponse.getMetadata();

        ComponentState expectedRecipeState = modelUnderTest.getComponentState();
        ComponentState actualRecipeState = metadata.getComponentState();
        assertEquals(
                expectedRecipeState,
                actualRecipeState
        );

        List<FailReasons> expectedFailReasons = modelUnderTest.getFailReasons();
        List<FailReasons> actualFailReasons = metadata.getFailReasons();
        assertEquals(
                expectedFailReasons,
                actualFailReasons
        );
    }

    @Test
    public void existingRequest_oldStateINVALID_CHANGED_newStateVALID_CHANGED() {
        // Arrange
        RecipeMetadataPersistenceModel invalidModelToLoad = TestDataRecipeMetadata.
                getInvalidUnchanged();

        RecipeMetadataPersistenceModel validChangedModel = TestDataRecipeMetadata.getValidChanged();

        // Assert
        RecipeMetadataRequest requestGetInvalidModel = new RecipeMetadataRequest.Builder().
                getDefault().
                setDomainId(invalidModelToLoad.getDomainId()).
                build();

        // Act
        SUT.execute(requestGetInvalidModel, new MetadataCallbackClient());

        // Assert / return invalid model
        verify(repoMock).getByDomainId(eq(invalidModelToLoad.getDomainId()),
                repoMetadataCallback.capture()
        );
        repoMetadataCallback.getValue().onPersistenceModelLoaded(invalidModelToLoad);

        // now update with VALID_CHANGED states
        RecipeMetadataRequest validRequest = new RecipeMetadataRequest.Builder().
                basedOnResponse(onErrorResponse).
                setDomainModel(
                        new RecipeMetadataRequest.DomainModel.Builder().
                                basedOnResponseModel(onErrorResponse.getDomainModel()).
                                setComponentStates(validChangedModel.getComponentStates()).
                                build()).
                build();

        when(idProviderMock.getUId()).thenReturn(validChangedModel.getDataId());
        when(timeProviderMock.getCurrentTimeInMills()).thenReturn(validChangedModel.getCreateDate());

        // Act
        SUT.execute(validRequest, new MetadataCallbackClient());

        // Assert
        // assert save
        verify(repoMock).save(eq(validChangedModel));

        UseCaseMetadataModel metadata = onSuccessResponse.getMetadata();

        ComponentState expectedComponentState = validChangedModel.getComponentState();
        ComponentState actualComponentState = metadata.getComponentState();
        assertEquals(
                expectedComponentState,
                actualComponentState
        );

        List<FailReasons> expectedFailReasons = validChangedModel.getFailReasons();
        List<FailReasons> actualFailReasons = metadata.getFailReasons();
        assertEquals(
                expectedFailReasons,
                actualFailReasons
        );
    }

    @Test
    public void existingRequest_validChangedStateLoaded_defaultRequestStateVALID_UNCHANGED() {
        // Arrange
        RecipeMetadataPersistenceModel validChangedModel = TestDataRecipeMetadata.getValidChanged();

        RecipeMetadataPersistenceModel validUnchangedModel = TestDataRecipeMetadata.
                getInvalidUnchanged();

        RecipeMetadataRequest initialLoadRequest = new RecipeMetadataRequest.Builder().
                getDefault().
                setDataId(validChangedModel.getDataId()).
                build();

        // Act
        SUT.execute(initialLoadRequest, new MetadataCallbackClient());

        // Assert
        // assert load
        verify(repoMock).getByDataId(eq(validChangedModel.getDataId()),
                repoMetadataCallback.capture());
        repoMetadataCallback.getValue().onPersistenceModelLoaded(validChangedModel);

        // Arrange
        // arrange default request, returns current state, however in the metadata use case if state
        // has not changed, state will change to UNCHANGED
        RecipeMetadataRequest request = new RecipeMetadataRequest.Builder().getDefault().build();

        // Arrange id and times for save
        when(idProviderMock.getUId()).thenReturn(validChangedModel.getDataId());
        when(timeProviderMock.getCurrentTimeInMills()).thenReturn(validUnchangedModel.getLastUpdate());

        // Act
        SUT.execute(request, new MetadataCallbackClient());

        // Assert
        // assert save
        verify(repoMock).save(validUnchangedModel);

        // assert metadata
        UseCaseMetadataModel metadata = onSuccessResponse.getMetadata();

        ComponentState expectedComponentState = validChangedModel.getComponentState();
        ComponentState actualComponentState = metadata.getComponentState();
        assertEquals(
                expectedComponentState,
                actualComponentState
        );

        List<FailReasons> expectedFailReasons = validUnchangedModel.getFailReasons();
        List<FailReasons> actualFailReasons = metadata.getFailReasons();
        assertEquals(
                expectedFailReasons,
                actualFailReasons
        );
    }

    @Test
    public void invalidChanged_stateINVALID_CHANGED() {
        // Arrange
        String recipeId = TestDataRecipeMetadata.NEW_RECIPE_DOMAIN_ID;
        // Arrange and execute first request
//        givenDomainIdAsInitialRequestReturnDataUnavailable(recipeId);

        // Arrange second request

        RecipeMetadataRequest secondRequest = new RecipeMetadataRequest.Builder().
                basedOnResponse(onErrorResponse).
                setDomainModel(
                        new RecipeMetadataRequest.DomainModel.Builder().
                                basedOnResponseModel(onErrorResponse.getDomainModel()).
                                setComponentStates(expectedComponentStates).
                                build()).
                build();

        // Act - execute second request
        handler.executeAsync(SUT, secondRequest, new MetadataCallbackClient());

        // Arrange for third request

        RecipeMetadataRequest request = new RecipeMetadataRequest.Builder().
                basedOnResponse(onErrorResponse).
                setDomainModel(
                        new RecipeMetadataRequest.DomainModel.Builder().
                                basedOnResponseModel(onErrorResponse.getDomainModel()).
                                setComponentStates(expectedComponentStates).
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
//        givenDomainIdAsInitialRequestReturnDataUnavailable(recipeId);

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
                FailReason.MISSING_REQUIRED_COMPONENTS};
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
        verify(repoMock).getByDomainId(eq(recipeId), repoMetadataCallback.capture());
        repoMetadataCallback.getValue().onPersistenceModelLoaded(modelUnderTest);

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
        verify(repoMock).getByDomainId(eq(recipeId), repoMetadataCallback.capture());
        repoMetadataCallback.getValue().onPersistenceModelLoaded(modelUnderTest);

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
        verify(repoMock).getByDomainId(eq(recipeId), repoMetadataCallback.capture());
        repoMetadataCallback.getValue().onPersistenceModelLoaded(modelUnderTest);

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
        RecipeMetadataPersistenceModel modelUnderTest = TestDataRecipeMetadata.getValidChanged();
        String recipeId = modelUnderTest.getDomainId();

        RecipeMetadataRequest r = new RecipeMetadataRequest.Builder().
                getDefault().
                setDomainId(recipeId).
                build();
        // Act
        handler.executeAsync(SUT, r, new MetadataCallbackClient()
        );
        // Assert
        verify(repoMock).getByDomainId(eq(recipeId), repoMetadataCallback.capture());
        repoMetadataCallback.getValue().onPersistenceModelLoaded(modelUnderTest);

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
        RecipeMetadataPersistenceModel modelUnderTestOne = TestDataRecipeMetadata.getValidChanged();
        String recipeId = modelUnderTestOne.getDomainId();

        RecipeMetadataRequest initialiseRequest = new RecipeMetadataRequest.Builder().
                getDefault().
                setDomainId(recipeId).
                build();
        // Act
        handler.executeAsync(SUT, initialiseRequest, new MetadataCallbackClient()
        );

        // Assert
        verify(repoMock).getByDomainId(eq(recipeId), repoMetadataCallback.capture());
        repoMetadataCallback.getValue().onPersistenceModelLoaded(modelUnderTestOne);

        UseCaseMetadataModel metadata = onErrorResponse.getMetadata();

        ComponentState expectedModelUnderTestOneState = ComponentState.INVALID_UNCHANGED;
        ComponentState actualModelUnderTestOneState = metadata.getComponentState();
        assertEquals(
                expectedModelUnderTestOneState,
                actualModelUnderTestOneState
        );

        // Arrange - second transaction
        RecipeMetadataPersistenceModel modelUnderTestTwo = TestDataRecipeMetadata.getValidChanged();

        RecipeMetadataRequest requestTwo = new RecipeMetadataRequest.Builder().
                basedOnResponse(onErrorResponse).
                setDomainModel(
                        new RecipeMetadataRequest.DomainModel.Builder().
                                basedOnResponseModel(onErrorResponse.getDomainModel()).
                                setComponentStates(modelUnderTestTwo.getComponentStates()).
                                build()).
                build();
        // Data has changed, provide new dataId and lastUpdate for save
        when(idProviderMock.getUId()).thenReturn(modelUnderTestTwo.getDataId()
        );
        when(timeProviderMock.getCurrentTimeInMills()).thenReturn(TestDataRecipeMetadata.
                getValidChanged().getLastUpdate()
        );

        // Act
        handler.executeAsync(SUT, requestTwo, new MetadataCallbackClient()
        );

        // Assert model saved
        verify(repoMock).save(eq(modelUnderTestTwo));

        // Arrange - third transaction
        RecipeMetadataPersistenceModel testModelThree = TestDataRecipeMetadata.getValidChanged();

        RecipeMetadataRequest requestThree = new RecipeMetadataRequest.Builder().
                basedOnResponse(onErrorResponse).
                setDomainModel(
                        new RecipeMetadataRequest.DomainModel.Builder().
                                basedOnResponseModel(onErrorResponse.getDomainModel()).
                                setComponentStates(testModelThree.getComponentStates()).
                                build()).
                build();
        // Data has changed again, provide new dataId and lastUpdate for save
        when(idProviderMock.getUId()).thenReturn(testModelThree.getDataId()
        );
        when(timeProviderMock.getCurrentTimeInMills()).thenReturn(TestDataRecipeMetadata.
                getValidChanged().getLastUpdate()
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