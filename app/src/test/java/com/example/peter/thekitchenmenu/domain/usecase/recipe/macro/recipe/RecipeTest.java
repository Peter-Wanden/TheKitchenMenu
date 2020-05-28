package com.example.peter.thekitchenmenu.domain.usecase.recipe.macro.recipe;

import androidx.core.util.Pair;


import com.example.peter.thekitchenmenu.data.repository.recipe.course.TestDataRecipeCourse;
import com.example.peter.thekitchenmenu.data.repository.recipe.duration.TestDataRecipeDuration;
import com.example.peter.thekitchenmenu.data.repository.recipe.identity.TestDataRecipeIdentity;
import com.example.peter.thekitchenmenu.data.repository.recipe.metadata.TestDataRecipeMetadata;
import com.example.peter.thekitchenmenu.data.repository.recipe.portions.TestDataRecipePortions;
import com.example.peter.thekitchenmenu.domain.model.CommonFailReason;
import com.example.peter.thekitchenmenu.domain.model.FailReasons;
import com.example.peter.thekitchenmenu.domain.usecase.UseCaseBase;
import com.example.peter.thekitchenmenu.domain.usecase.UseCaseHandler;
import com.example.peter.thekitchenmenu.domain.usecase.UseCaseMetadataModel;
import com.example.peter.thekitchenmenu.domain.usecase.recipe.component.course.RecipeCourse;
import com.example.peter.thekitchenmenu.domain.usecase.recipe.component.course.RecipeCoursePersistenceModel;
import com.example.peter.thekitchenmenu.domain.usecase.recipe.component.course.RecipeCourseRequest;
import com.example.peter.thekitchenmenu.domain.usecase.recipe.component.course.RecipeCourseResponse;
import com.example.peter.thekitchenmenu.domain.usecase.recipe.component.duration.RecipeDurationRequest;
import com.example.peter.thekitchenmenu.domain.usecase.recipe.component.duration.RecipeDurationResponse;
import com.example.peter.thekitchenmenu.domain.usecase.recipe.component.identity.RecipeIdentityPersistenceModel;
import com.example.peter.thekitchenmenu.domain.usecase.recipe.component.identity.RecipeIdentityRequest;
import com.example.peter.thekitchenmenu.domain.usecase.recipe.component.identity.RecipeIdentityResponse;
import com.example.peter.thekitchenmenu.domain.usecase.recipe.component.metadata.RecipeMetadata.ComponentName;
import com.example.peter.thekitchenmenu.domain.usecase.recipe.component.metadata.RecipeMetadata.ComponentState;
import com.example.peter.thekitchenmenu.domain.usecase.recipe.component.metadata.RecipeMetadata.FailReason;
import com.example.peter.thekitchenmenu.domain.usecase.recipe.component.metadata.RecipeMetadataPersistenceModel;
import com.example.peter.thekitchenmenu.domain.usecase.recipe.component.metadata.RecipeMetadataResponse;
import com.example.peter.thekitchenmenu.domain.usecase.recipe.component.portions.RecipePortionsRequest;
import com.example.peter.thekitchenmenu.domain.usecase.recipe.component.portions.RecipePortionsResponse;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

import javax.annotation.Nonnull;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

public class RecipeTest {

//    private static final String TAG = "tkm-" + RecipeTest.class.getSimpleName() + ": ";

    // region constants ----------------------------------------------------------------------------
    private static final String NEW_RECIPE_DOMAIN_ID = TestDataRecipeMetadata.NEW_RECIPE_DOMAIN_ID;
    // endregion constants -------------------------------------------------------------------------

    // region helper fields ------------------------------------------------------------------------
    private RecipeTestBase recipeTestBase;
    @Captor
    ArgumentCaptor<RecipeMetadataResponse> recipeMetadataCaptor;
    @Mock
    RecipeMetadataListener metadataListener1;
    @Mock
    RecipeMetadataListener metadataListener2;

    private UseCaseHandler handler;
    private Recipe SUT;

    // endregion helper fields ---------------------------------------------------------------------

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        recipeTestBase = new RecipeTestBase();
        recipeTestBase.setUp();

        handler = recipeTestBase.getHandler();
        SUT = recipeTestBase.getRecipe();

    }

    @Test
    public void recipeRequest_noId_emptyRecipeReturned() {
        RecipeRequest request = new RecipeRequest.Builder().getDefault().build();
        handler.executeAsync(SUT, request, new RecipeCallbackClient());


    }

    @Test
    public void recipeRequestNewId_invokerIssuesCommandToAllReceivers() {
        // Arrange
        RecipeRequest request = new RecipeRequest.Builder().
                getDefault().
                setDomainId(NEW_RECIPE_DOMAIN_ID).
                build();

        when(recipeTestBase.getIdProviderMock().getUId()).thenReturn("");

        // Act
        handler.executeAsync(SUT, request, new RecipeCallbackClient());

        // Assert persistence called for every component.
        verifyAllReposCalledAndReturnModelUnavailable(NEW_RECIPE_DOMAIN_ID);
    }

    @Test
    public void recipeRequestNewId_invokerIssuesCommand_metadataListenersUpdatedWithCorrectValues() {
        // Arrange
        String recipeId = NEW_RECIPE_DOMAIN_ID;

        RecipeRequest request = new RecipeRequest.Builder().
                getDefault().
                setDomainId(recipeId).
                build();

        SUT.registerMetadataListener(metadataListener1);
        SUT.registerMetadataListener(metadataListener2);

        // Act
        handler.executeAsync(SUT, request, new RecipeCallbackClient()
        );
        when(recipeTestBase.getIdProviderMock().getUId()).thenReturn("testDataId");

        // Assert persistence calls
        verifyAllReposCalledAndReturnModelUnavailable(recipeId);

        // Assert recipe metadata listener updated with correct value
        verify(metadataListener1).onRecipeMetadataChanged(recipeMetadataCaptor.capture()
        );
        RecipeMetadataResponse.Model model = recipeMetadataCaptor.getValue().getModel();
        UseCaseMetadataModel recipeMetadata = recipeMetadataCaptor.getValue().getMetadata();

        // Assert recipe state updated
        assertEquals(
                ComponentState.INVALID_UNCHANGED,
                recipeMetadata.getState()
        );
        // Assert all components responded
        assertEquals(
                recipeTestBase.getNoOfRequiredComponents(),
                model.getComponentStates().size()
        );
        // Assert identity component and state
        assertTrue(
                model.getComponentStates().containsKey(ComponentName.IDENTITY)
        );
        assertEquals(
                ComponentState.INVALID_UNCHANGED,
                model.getComponentStates().get(ComponentName.IDENTITY)
        );
        // Assert course component and state
        assertTrue(
                model.getComponentStates().containsKey(ComponentName.COURSE)
        );
        assertEquals(
                ComponentState.INVALID_UNCHANGED,
                model.getComponentStates().get(ComponentName.COURSE)
        );
        // Assert duration component and state
        assertTrue(
                model.getComponentStates().containsKey(ComponentName.DURATION)
        );
        assertEquals(
                ComponentState.INVALID_UNCHANGED,
                model.getComponentStates().get(ComponentName.DURATION)
        );
        // Assert portions component and state
        assertTrue(
                model.getComponentStates().containsKey(ComponentName.PORTIONS)
        );
        assertEquals(
                ComponentState.INVALID_UNCHANGED,
                model.getComponentStates().get(ComponentName.PORTIONS)
        );
    }

    @Test
    public void recipeRequestNewId_invokerIssuesCommand_recipeStateINVALID_UNCHANGED_INVALID_COMPONENTS() {
        // Arrange
        String recipeId = NEW_RECIPE_DOMAIN_ID;

        RecipeRequest request = new RecipeRequest.Builder().
                getDefault().
                setDomainId(recipeId).
                build();

        RecipeCallbackClient passedInCallback = new RecipeCallbackClient();
        RecipeCallbackClient registeredCallback = new RecipeCallbackClient();
        SUT.registerRecipeListener(registeredCallback);

        // Act
        handler.executeAsync(SUT, request, passedInCallback);

        // Assert database calls
        verifyAllReposCalledAndReturnModelUnavailable(recipeId);

        // Assert, callback updated with recipe response
        UseCaseMetadataModel metadata = registeredCallback.recipeMetadataResponse.getMetadata();

        ComponentState expectedRecipeState = ComponentState.INVALID_UNCHANGED;
        ComponentState actualRecipeState = metadata.getState();
        assertEquals(
                expectedRecipeState,
                actualRecipeState
        );

        FailReasons[] expectedFailReasons = new FailReasons[]{FailReason.INVALID_COMPONENTS};
        FailReasons[] actualFailReasons = metadata.getFailReasons().toArray(new FailReasons[0]);
        assertArrayEquals(
                expectedFailReasons,
                actualFailReasons
        );
    }

    @Test
    public void recipeRequestNewId_invokerIssuesCommand_registeredComponentListenerUpdated() {
        // Arrange
        String recipeId = NEW_RECIPE_DOMAIN_ID;

        // Request originator is Recipe component
        RecipeRequest request = new RecipeRequest.Builder().
                getDefault().
                setDomainId(recipeId).
                build();
        // Recipe callback (returns data for all components) passed in with request
        RecipeCallbackClient passedInCallback = new RecipeCallbackClient();

        // Registered callback listening for changes to identity data only
        RecipeIdentityCallbackClient registeredIdentityCallback = new RecipeIdentityCallbackClient();
        SUT.registerComponentListener(
                new Pair<>(ComponentName.IDENTITY, registeredIdentityCallback)
        );

        // Arbitrary value, not required for this test
        when(recipeTestBase.getIdProviderMock().getUId()).thenReturn("");

        // Act
        handler.executeAsync(SUT, request, passedInCallback);
        // Assert
        verifyAllReposCalledAndReturnModelUnavailable(recipeId);

        // Assert passed in callback and registered listener have equal values for identity component
        assertEquals(
                passedInCallback.identityOnError,
                registeredIdentityCallback.response
        );
    }

    @Test
    public void identityRequestNewId_initialRequest_invokerIssuesCommandToAllReceivers() {
        // Arrange
        String recipeId = NEW_RECIPE_DOMAIN_ID;

        // Request originator is identity component
        RecipeIdentityRequest identityRequest = new RecipeIdentityRequest.Builder().
                getDefault().
                setDomainId(recipeId).
                build();
        // Identity response callback to go with Identity request
        RecipeIdentityCallbackClient callback = new RecipeIdentityCallbackClient();
        // Act
        handler.executeAsync(SUT, identityRequest, callback);

        // Assert
        verifyAllReposCalledAndReturnModelUnavailable(recipeId);

        // Assert identity response received
        RecipeIdentityResponse response = callback.getResponse();
        assertTrue(
                response.getMetadata().getFailReasons().contains(CommonFailReason.DATA_UNAVAILABLE)
        );
    }

    @Test
    public void identityRequestNewId_invokerIssuesCommand_correctMacroComponentResponses() {
        // Arrange
        String recipeId = NEW_RECIPE_DOMAIN_ID;

        // Identity request and response callback
        RecipeIdentityRequest request = new RecipeIdentityRequest.Builder().
                getDefault().
                setDomainId(recipeId).
                build();
        RecipeIdentityCallbackClient callback = new RecipeIdentityCallbackClient();

        // Register RecipeCallback to get access to all recipe responses
        RecipeCallbackClient macroCallback = new RecipeCallbackClient();
        SUT.registerRecipeListener(macroCallback);
        // Act
        handler.executeAsync(SUT, request, callback);

        // Assert database calls
        verifyAllReposCalledAndReturnModelUnavailable(recipeId);

        // Assert
        // Expecting only one error from each component (DATA_UNAVAILABLE)
        int expectedNoOfFailReasons = 1;

        // Assert response from identity component
        RecipeIdentityResponse identityResponse = macroCallback.identityOnError;
        List<FailReasons> identityFailReasons = identityResponse.getMetadata().getFailReasons();
        assertEquals(
                ComponentState.INVALID_UNCHANGED,
                identityResponse.getMetadata().getState()
        );
        assertEquals(
                expectedNoOfFailReasons,
                identityFailReasons.size()
        );
        // Assert expected error
        assertTrue(identityFailReasons.contains(CommonFailReason.DATA_UNAVAILABLE)
        );

        // Assert response from courses component
        RecipeCourseResponse courseResponse = macroCallback.courseOnError;
        List<FailReasons> courseFailReasons = courseResponse.getMetadata().getFailReasons();
        assertEquals(
                ComponentState.INVALID_UNCHANGED,
                courseResponse.getMetadata().getState()
        );
        assertEquals(
                expectedNoOfFailReasons,
                courseFailReasons.size()
        );
        assertTrue(courseFailReasons.contains(CommonFailReason.DATA_UNAVAILABLE)
        );

        // Assert response from duration component
        RecipeDurationResponse durationResponse = macroCallback.durationOnError;
        List<FailReasons> durationFailReasons = durationResponse.getMetadata().getFailReasons();
        assertEquals(
                ComponentState.INVALID_UNCHANGED,
                durationResponse.getMetadata().getState()
        );
        assertEquals(
                expectedNoOfFailReasons,
                durationFailReasons.size()
        );
        assertTrue(durationFailReasons.contains(CommonFailReason.DATA_UNAVAILABLE)
        );

        // Assert response from portions component
        RecipePortionsResponse portionResponse = macroCallback.portionsOnError;
        List<FailReasons> portionsFailReasons = portionResponse.getMetadata().getFailReasons();
        assertEquals(
                ComponentState.INVALID_UNCHANGED,
                portionResponse.getMetadata().getState()
        );
        assertEquals(
                expectedNoOfFailReasons,
                portionsFailReasons.size()
        );
        assertTrue(portionsFailReasons.contains(CommonFailReason.DATA_UNAVAILABLE)
        );
    }

    @Test
    public void coursesRequestNewId_initialRequest_invokerIssuesCommandToAllReceivers() {
        // Arrange
        String recipeId = NEW_RECIPE_DOMAIN_ID;

        CourseCallbackClient callback = new CourseCallbackClient();
        RecipeCourseRequest courseRequest = new RecipeCourseRequest.Builder().
                getDefault().
                setDomainId(recipeId).
                build();
        // Act
        handler.executeAsync(SUT, courseRequest, callback);

        // Assert all components receive recipe ID and call their repos
        verifyAllReposCalledAndReturnModelUnavailable(recipeId);

        // Assert course response received
        RecipeCourseResponse response = callback.response;
        assertTrue(
                response.getMetadata().getFailReasons().contains(CommonFailReason.DATA_UNAVAILABLE)
        );
    }

    @Test
    public void durationRequestNewId_initialRequest_invokerIssuesCommandToAllReceivers() {
        // Arrange
        String recipeId = NEW_RECIPE_DOMAIN_ID;
        DurationCallbackClient callback = new DurationCallbackClient();

        RecipeDurationRequest durationRequest = new RecipeDurationRequest.Builder().
                getDefault().
                setDomainId(recipeId).
                build();

        // Act
        handler.executeAsync(SUT, durationRequest, callback);

        // Assert all components receive recipe ID and call their repos
        verifyAllReposCalledAndReturnModelUnavailable(recipeId);

        // Assert response received
        RecipeDurationResponse response = callback.getResponse();
        assertTrue(
                response.getMetadata().getFailReasons().
                        contains(CommonFailReason.DATA_UNAVAILABLE)
        );
    }

    @Test
    public void portionsRequestNewId_initialRequest_invokerIssuesCommandToAllReceivers() {
        // Arrange
        String recipeId = NEW_RECIPE_DOMAIN_ID;
        PortionCallbackClient callback = new PortionCallbackClient();

        RecipePortionsRequest portionsRequest = new RecipePortionsRequest.Builder().
                getDefault().
                setDomainId(recipeId).
                build();
        // Act
        handler.executeAsync(SUT, portionsRequest, callback);

        // Assert all components receive recipe ID and call their repos
        verifyAllReposCalledAndReturnModelUnavailable(recipeId);

        // Assert response received
        RecipePortionsResponse response = callback.getResponse();
        assertTrue(response.getMetadata().
                getFailReasons().
                contains(CommonFailReason.DATA_UNAVAILABLE)
        );
    }

    @Test
    public void identityRequestNewId_titleValidDescriptionValid_identityStateVALID_CHANGED() {
        // Arrange
        RecipeIdentityPersistenceModel modelUnderTest = TestDataRecipeIdentity.
                getValidExistingTitleValidDescriptionValid();
        ArgumentCaptor<RecipeIdentityPersistenceModel> persistenceModelCaptor = ArgumentCaptor.
                forClass(RecipeIdentityPersistenceModel.class);

        String recipeId = modelUnderTest.getDomainId();

        whenTimeProviderReturnTime(modelUnderTest.getCreateDate()
        );

        // Identity request 1, 'new request', initialise components, load data
        RecipeIdentityRequest initialRequest = new RecipeIdentityRequest.Builder().
                getDefault().
                setDomainId(recipeId).
                build();

        RecipeIdentityCallbackClient callbackClient = new RecipeIdentityCallbackClient();

        // Act
        handler.executeAsync(SUT, initialRequest, callbackClient);
        // Assert all component repos called
        verifyAllReposCalledAndReturnModelUnavailable(recipeId);

        // Identity Request 2, 'existing request', update title
        String validTitle = modelUnderTest.getTitle();

        RecipeIdentityRequest.DomainModel validTitleModel = new RecipeIdentityRequest.DomainModel.Builder().
                basedOnResponseModel(callbackClient.response.getModel()).
                setTitle(validTitle).
                build();

        RecipeIdentityRequest validTitleRequest = new RecipeIdentityRequest.Builder().
                basedOnResponse(callbackClient.response).
                setModel(validTitleModel).
                build();

        handler.executeAsync(SUT, validTitleRequest, callbackClient);

        // Identity Request 3, existing request, update description
        String validDescription = modelUnderTest.getDescription();
        RecipeIdentityRequest.DomainModel validTitleDescriptionModel = new RecipeIdentityRequest.DomainModel.
                Builder().basedOnResponseModel(callbackClient.response.getModel()).
                setDescription(validDescription).
                build();

        RecipeIdentityRequest validDescriptionRequest = new RecipeIdentityRequest.Builder().
                basedOnResponse(callbackClient.response).
                setModel(validTitleDescriptionModel).
                build();

        when(recipeTestBase.getIdProviderMock().getUId()).thenReturn(modelUnderTest.getDataId());
        when(recipeTestBase.getTimeProviderMock().getCurrentTimeInMills()).
                thenReturn(modelUnderTest.getLastUpdate());

        // Act
        handler.executeAsync(SUT, validDescriptionRequest, callbackClient);

        // Assert identity component saved
        int saveTitleThenSaveDescription = 2;
        verify(
                recipeTestBase.repoIdentityMock, times(saveTitleThenSaveDescription)).
                save(persistenceModelCaptor.capture()
                );

        assertEquals(
                modelUnderTest,
                persistenceModelCaptor.getValue()
        );

        // Assert identity component response values
        RecipeIdentityResponse response = callbackClient.response;
        assertEquals(
                validTitle,
                response.getModel().getTitle()
        );
        assertEquals(
                validDescription,
                response.getModel().getDescription()
        );

        // Assert identity component state
        assertEquals(
                ComponentState.VALID_CHANGED,
                response.getMetadata().getState()
        );
        int expectedNoOfFailReasons = 1;
        assertEquals(
                expectedNoOfFailReasons,
                response.getMetadata().getFailReasons().size()
        );
        assertTrue(response.getMetadata().getFailReasons().contains(CommonFailReason.NONE)
        );
    }

    @Test
    public void coursesRequestNewId_newCourseAdded_coursesStateVALID_CHANGED() {
        // Arrange
        RecipeCoursePersistenceModel modelUnderTest = TestDataRecipeCourse.
                getExistingActiveRecipeCourseZero();
        String recipeId = modelUnderTest.getDomainId();

        whenTimeProviderReturnTime(modelUnderTest.getCreateDate()
        );
        when(recipeTestBase.getIdProviderMock().getUId()).thenReturn(modelUnderTest.getDataId()
        );

        CourseCallbackClient callback = new CourseCallbackClient();
        SUT.registerMetadataListener(metadataListener1);

        // Arrange, initial request to load data
        RecipeCourseRequest initialRequest = new RecipeCourseRequest.Builder().
                getDefault().
                setDomainId(recipeId).
                build();

        // Act
        handler.executeAsync(SUT, initialRequest, callback);

        // Assert
        verifyAllReposCalledAndReturnModelUnavailable(recipeId);

        // Arrange, second request to add a course
        RecipeCourseRequest addCourseRequest = new RecipeCourseRequest.Builder().
                basedOnResponse(callback.response).
                setModel(
                        new RecipeCourseRequest.Model.Builder().
                                setCourseList(
                                        Collections.singletonList(RecipeCourse.Course.COURSE_ZERO)).
                                build()).
                build();

        // Act
        handler.executeAsync(SUT, addCourseRequest, callback);

        // Assert correct values saved
        verify(recipeTestBase.repoCourseMock).save(eq(modelUnderTest)
        );

        // Assert courses response
        RecipeCourseResponse response = callback.response;

        assertEquals(
                ComponentState.VALID_CHANGED,
                response.getMetadata().getState()
        );
        assertTrue(
                response.getModel().getCourseList().
                        containsKey(RecipeCourse.Course.COURSE_ZERO)
        );
        // Assert listener updated
        int expectedNumberOfUpdates = 2; // Once for initial request, once for add course request
        verify(
                metadataListener1, times(expectedNumberOfUpdates)).
                onRecipeMetadataChanged(recipeMetadataCaptor.capture()
                );

        ComponentState actualCourseComponentState = recipeMetadataCaptor.getValue().
                getModel().
                getComponentStates().
                get(ComponentName.COURSE);
        assertEquals(
                ComponentState.VALID_CHANGED,
                actualCourseComponentState
        );
    }

    @Test
    public void recipeRequestExistingId_validData_onlyRegisteredListenersNotified() {
        // Arrange
        RecipeMetadataPersistenceModel modelUnderTest = TestDataRecipeMetadata.getValidUnchanged();
        String recipeId = modelUnderTest.getDomainId();

        // A RecipeRequest / RecipeCallbackClient will return all data and state for a recipe
        RecipeRequest initialRequest = new RecipeRequest.Builder().
                getDefault().
                setDomainId(recipeId).
                build();
        RecipeCallbackClient recipeCallback = new RecipeCallbackClient();
        //
        SUT.registerMetadataListener(metadataListener1);
        SUT.registerMetadataListener(metadataListener2);
        SUT.unregisterMetadataListener(metadataListener2);

        // Act
        handler.executeAsync(SUT, initialRequest, recipeCallback);

        // Assert database called and return valid data for all components
        verifyAllReposCalledAndReturnValidExisting(recipeId);

        // Metadata listener response
        RecipeMetadataResponse expectedMetadataResponse = recipeCallback.recipeMetadataResponse;

        // Assert listener1 called and listener 2 not called
        verify(metadataListener1).onRecipeMetadataChanged(eq(expectedMetadataResponse));
        verifyNoMoreInteractions(metadataListener2);
    }

    @Test
    public void requestExistingId_validData_componentStateVALID_UNCHANGED() {
        // Arrange
        RecipeMetadataPersistenceModel modelUnderTest = TestDataRecipeMetadata.getValidUnchanged();
        String recipeId = modelUnderTest.getDomainId();

        // A RecipeRequest / RecipeCallbackClient will return all data and state for a recipe
        RecipeRequest initialRequest = new RecipeRequest.Builder().
                getDefault().
                setDomainId(recipeId).
                build();
        RecipeCallbackClient recipeCallback = new RecipeCallbackClient();

        // Act
        handler.executeAsync(SUT, initialRequest, recipeCallback);

        // Assert database called and return existing data
        verifyAllReposCalledAndReturnValidExisting(recipeId);

        // Assert recipe component states
        HashMap<ComponentName, ComponentState> componentStates = recipeCallback.
                recipeMetadataResponse.
                getModel().
                getComponentStates();
        assertEquals(
                ComponentState.VALID_UNCHANGED,
                componentStates.get(ComponentName.IDENTITY)
        );
        assertEquals(
                ComponentState.VALID_UNCHANGED,
                componentStates.get(ComponentName.COURSE)
        );
        assertEquals(
                ComponentState.VALID_UNCHANGED,
                componentStates.get(ComponentName.DURATION)
        );
        assertEquals(
                ComponentState.VALID_UNCHANGED,
                componentStates.get(ComponentName.PORTIONS)
        );
    }

    @Test
    public void recipeRequestExistingId_validData_recipeStateVALID_UNCHANGED() {
        // Arrange
        RecipeMetadataPersistenceModel modelUnderTest = TestDataRecipeMetadata.getValidUnchanged();
        String recipeId = modelUnderTest.getDomainId();

        // A RecipeRequest / RecipeCallbackClient will return all data and state for a recipe
        RecipeRequest initialRequest = new RecipeRequest.Builder().
                getDefault().
                setDomainId(recipeId).
                build();
        RecipeCallbackClient recipeCallback = new RecipeCallbackClient();

        // Act
        handler.executeAsync(SUT, initialRequest, recipeCallback);

        // Assert database called and return existing data
        verifyAllReposCalledAndReturnValidExisting(recipeId);

        // Assert correct recipe state
        ComponentState recipeState = recipeCallback.recipeMetadataResponse.getMetadata().getState();
        assertEquals(ComponentState.VALID_UNCHANGED, recipeState);
    }

    @Test
    public void recipeRequest_validId_registeredComponentCallbacksCalled() {
        // Arrange
        RecipeMetadataPersistenceModel modelUnderTest = TestDataRecipeMetadata.getValidUnchanged();
        String recipeId = modelUnderTest.getDomainId();

        // A RecipeRequest / RecipeCallbackClient will return all data and state for a recipe
        RecipeRequest initialRequest = new RecipeRequest.Builder().
                getDefault().
                setDomainId(recipeId).
                build();

        // Callback clients
        RecipeCallbackClient passedInRecipeCallback = new RecipeCallbackClient();
        RecipeCallbackClient registeredRecipeCallback = new RecipeCallbackClient();
        RecipeMetadataListener registeredMetadataCallback = new RecipeMetadataListener();
        RecipeIdentityCallbackClient registeredIdentityCallback = new RecipeIdentityCallbackClient();

        // Register callbacks
        SUT.registerRecipeListener(registeredRecipeCallback);
        SUT.registerMetadataListener(registeredMetadataCallback);
        SUT.registerComponentListener(new Pair<>(ComponentName.IDENTITY, registeredIdentityCallback)
        );

        // Act
        handler.executeAsync(SUT, initialRequest, passedInRecipeCallback);
        // Assert
        verifyAllReposCalledAndReturnValidExisting(recipeId);

        // Assert recipe response callback
        // Recipe callback
        UseCaseMetadataModel metadata = passedInRecipeCallback.recipeMetadataResponse.getMetadata();
        List<FailReasons> failReasons = metadata.getFailReasons();
        assertTrue(
                failReasons.contains(CommonFailReason.NONE)
        );

        // Assert recipe metadata callback
        UseCaseMetadataModel metadata1 = registeredMetadataCallback.response.getMetadata();
        ComponentState actualRecipeState = metadata1.getState();
        assertEquals(
                ComponentState.VALID_UNCHANGED,
                actualRecipeState
        );

        // Assert recipe identity component callback
        UseCaseMetadataModel metadata2 = registeredIdentityCallback.response.getMetadata();
        ComponentState identityComponentState = metadata2.getState();
        assertEquals(
                ComponentState.VALID_UNCHANGED,
                identityComponentState
        );
    }

    // region helper methods -----------------------------------------------------------------------
    private void verifyAllReposCalledAndReturnModelUnavailable(String recipeId) {
        verifyRepoIdentityCalledAndReturnDataUnavailable(recipeId);
        verifyRepoCoursesCalledAndReturnDataUnavailable(recipeId);
        verifyRepoDurationCalledAndReturnDataUnavailable(recipeId);
        verifyRepoPortionsCalledAndReturnDataUnavailable(recipeId);
        verifyRepoRecipeMetadataCalledAndReturnDataUnavailable(recipeId);
    }

    private void verifyRepoRecipeMetadataCalledAndReturnDataUnavailable(String recipeId) {
        verify(recipeTestBase.repoMetadataMock).getActiveByDomainId(eq(recipeId),
                recipeTestBase.repoMetadataCallback.capture());
        recipeTestBase.repoMetadataCallback.getValue().onModelUnavailable();
    }

    private void verifyRepoIdentityCalledAndReturnDataUnavailable(String recipeId) {
        verify(recipeTestBase.repoIdentityMock).getActiveByDomainId(eq(recipeId),
                recipeTestBase.repoIdentityCallback.capture());
        recipeTestBase.repoIdentityCallback.getValue().onModelUnavailable();
    }

    private void verifyRepoCoursesCalledAndReturnDataUnavailable(String recipeId) {
        verify(recipeTestBase.repoCourseMock).getAllActiveByDomainId(eq(recipeId),
                recipeTestBase.repoCourseCallback.capture());
        recipeTestBase.repoCourseCallback.getValue().onModelsUnavailable();
    }

    private void verifyRepoDurationCalledAndReturnDataUnavailable(String recipeId) {
        verify(recipeTestBase.repoDurationMock).getActiveByDomainId(eq(recipeId),
                recipeTestBase.repoDurationCallback.capture());
        recipeTestBase.repoDurationCallback.getValue().onModelUnavailable();
    }

    private void verifyRepoPortionsCalledAndReturnDataUnavailable(String recipeId) {
        verify(recipeTestBase.repoPortionsMock).getActiveByDomainId(eq(recipeId),
                recipeTestBase.repoPortionsCallback.capture());
        recipeTestBase.repoPortionsCallback.getValue().onModelUnavailable();
    }

    private void verifyAllReposCalledAndReturnValidExisting(String recipeId) {
        verify(recipeTestBase.repoMetadataMock).getActiveByDomainId(eq(recipeId),
                recipeTestBase.repoMetadataCallback.capture());
        recipeTestBase.repoMetadataCallback.getValue().onModelLoaded(TestDataRecipeMetadata.
                getActiveByDomainId(recipeId)
        );
        verify(recipeTestBase.repoIdentityMock).getActiveByDomainId(eq(recipeId),
                recipeTestBase.repoIdentityCallback.capture());
        recipeTestBase.repoIdentityCallback.getValue().onModelLoaded(TestDataRecipeIdentity.
                getActiveByDomainId(recipeId)
        );
        verify(recipeTestBase.repoCourseMock).getAllActiveByDomainId(eq(recipeId),
                recipeTestBase.repoCourseCallback.capture());
        recipeTestBase.repoCourseCallback.getValue().onAllLoaded(TestDataRecipeCourse.
                getAllExistingActiveByDomainId(recipeId)
        );
        verify(recipeTestBase.repoDurationMock).getActiveByDomainId(eq(recipeId),
                recipeTestBase.repoDurationCallback.capture());
        recipeTestBase.repoDurationCallback.getValue().onModelLoaded(TestDataRecipeDuration.
                getActiveByDomainId(recipeId)
        );
        verify(recipeTestBase.repoPortionsMock).getActiveByDomainId(eq(recipeId),
                recipeTestBase.repoPortionsCallback.capture());
        recipeTestBase.repoPortionsCallback.getValue().onModelLoaded(TestDataRecipePortions.
                getActiveByDomainId(recipeId)
        );
    }

    private void whenTimeProviderReturnTime(long time) {
        when(recipeTestBase.getTimeProviderMock().getCurrentTimeInMills()).thenReturn(time);
    }
    // endregion helper methods --------------------------------------------------------------------

    // region helper classes -----------------------------------------------------------------------
    private static class RecipeMetadataListener implements Recipe.RecipeMetadataListener {
        private RecipeMetadataResponse response;

        @Override
        public void onRecipeMetadataChanged(RecipeMetadataResponse response) {
            this.response = response;
        }

        public RecipeMetadataResponse getResponse() {
            return response;
        }
    }

    private static class RecipeCallbackClient implements UseCaseBase.Callback<RecipeResponse> {

        private final String TAG = "tkm-" + RecipeCallbackClient.class.
                getSimpleName() + ": ";

        private RecipeResponse recipeResponseOnSuccess;
        private RecipeResponse recipeResponseOnError;

        private RecipeMetadataResponse recipeMetadataResponse;

        private RecipeIdentityResponse identityOnSuccess;
        private RecipeIdentityResponse identityOnError;

        private RecipeCourseResponse courseOnSuccess;
        private RecipeCourseResponse courseOnError;

        private RecipeDurationResponse durationOnSuccess;
        private RecipeDurationResponse durationOnError;

        private RecipePortionsResponse portionsOnSuccess;
        private RecipePortionsResponse portionsOnError;

        @Override
        public void onUseCaseSuccess(RecipeResponse response) {
            if (response != null) {
                System.out.println(TAG + "recipeMacroResponseOnSuccess: " + response);
                recipeResponseOnSuccess = response;
                recipeMetadataResponse = (RecipeMetadataResponse) response.
                        getModel().
                        getComponentResponses().
                        get(ComponentName.RECIPE_METADATA);
                identityOnSuccess = (RecipeIdentityResponse) response.
                        getModel().
                        getComponentResponses().
                        get(ComponentName.IDENTITY);
                courseOnSuccess = (RecipeCourseResponse) response.
                        getModel().
                        getComponentResponses().
                        get(ComponentName.COURSE);
                durationOnSuccess = (RecipeDurationResponse) response.
                        getModel().
                        getComponentResponses().
                        get(ComponentName.DURATION);
                portionsOnSuccess = (RecipePortionsResponse) response.
                        getModel().
                        getComponentResponses().
                        get(ComponentName.PORTIONS);
            }
        }

        @Override
        public void onUseCaseError(RecipeResponse response) {
            if (response != null) {
                System.out.println(TAG + "recipeMacroResponseOnError: " + response);
                recipeResponseOnError = response;

                recipeMetadataResponse = (RecipeMetadataResponse) response.
                        getModel().
                        getComponentResponses().
                        get(ComponentName.RECIPE_METADATA);

                RecipeIdentityResponse identityResponse = (RecipeIdentityResponse) response.
                        getModel().
                        getComponentResponses().get(ComponentName.IDENTITY);

                if (identityResponse.
                        getMetadata().
                        getFailReasons().
                        contains(CommonFailReason.NONE)) {
                    identityOnSuccess = identityResponse;
                } else {
                    identityOnError = identityResponse;
                }

                RecipeCourseResponse courseResponse = (RecipeCourseResponse) response.
                        getModel().
                        getComponentResponses().
                        get(ComponentName.COURSE);

                if (courseResponse.getMetadata().getFailReasons().contains(CommonFailReason.NONE)) {
                    courseOnSuccess = courseResponse;
                } else {
                    courseOnError = courseResponse;
                }

                RecipeDurationResponse durationResponse = (RecipeDurationResponse) response.
                        getModel().
                        getComponentResponses().
                        get(ComponentName.DURATION);

                if (durationResponse.getMetadata().
                        getFailReasons().
                        contains(CommonFailReason.NONE)) {
                    durationOnSuccess = durationResponse;
                } else {
                    durationOnError = durationResponse;
                }

                RecipePortionsResponse portionsResponse = (RecipePortionsResponse) response.
                        getModel().
                        getComponentResponses().
                        get(ComponentName.PORTIONS);

                if (portionsResponse.getMetadata().
                        getFailReasons().
                        contains(CommonFailReason.NONE)) {
                    portionsOnSuccess = portionsResponse;
                } else {
                    portionsOnError = portionsResponse;
                }
            }
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            RecipeCallbackClient that = (RecipeCallbackClient) o;
            return Objects.equals(recipeResponseOnSuccess, that.recipeResponseOnSuccess) &&
                    Objects.equals(recipeResponseOnError, that.recipeResponseOnError) &&
                    Objects.equals(recipeMetadataResponse, that.recipeMetadataResponse) &&
                    Objects.equals(identityOnSuccess, that.identityOnSuccess) &&
                    Objects.equals(identityOnError, that.identityOnError) &&
                    Objects.equals(courseOnSuccess, that.courseOnSuccess) &&
                    Objects.equals(courseOnError, that.courseOnError) &&
                    Objects.equals(durationOnSuccess, that.durationOnSuccess) &&
                    Objects.equals(durationOnError, that.durationOnError) &&
                    Objects.equals(portionsOnSuccess, that.portionsOnSuccess) &&
                    Objects.equals(portionsOnError, that.portionsOnError);
        }

        @Override
        public int hashCode() {
            return Objects.hash(recipeResponseOnSuccess, recipeResponseOnError,
                    recipeMetadataResponse, identityOnSuccess, identityOnError, courseOnSuccess,
                    courseOnError, durationOnSuccess, durationOnError, portionsOnSuccess,
                    portionsOnError);
        }

        @Nonnull
        @Override
        public String toString() {
            return "MacroCallbackClient{" +
                    "TAG='" + TAG + '\'' +
                    ", recipeMacroResponseOnSuccess=" + recipeResponseOnSuccess +
                    ", recipeMacroResponseOnError=" + recipeResponseOnError +
                    ", recipeResponse=" + recipeMetadataResponse +
                    ", identityOnSuccess=" + identityOnSuccess +
                    ", identityOnError=" + identityOnError +
                    ", courseOnSuccess=" + courseOnSuccess +
                    ", courseOnError=" + courseOnError +
                    ", durationOnSuccess=" + durationOnSuccess +
                    ", durationOnError=" + durationOnError +
                    ", portionsOnSuccess=" + portionsOnSuccess +
                    ", portionsOnError=" + portionsOnError +
                    '}';
        }
    }

    private static class RecipeIdentityCallbackClient implements UseCaseBase.Callback<RecipeIdentityResponse> {

        private final String TAG = "tkm-" +
                RecipeIdentityCallbackClient.class.getSimpleName() + ": ";

        private RecipeIdentityResponse response;

        @Override
        public void onUseCaseSuccess(RecipeIdentityResponse response) {
            System.out.println(TAG + response);
            this.response = response;
        }

        @Override
        public void onUseCaseError(RecipeIdentityResponse response) {
            System.out.println(TAG + response);
            this.response = response;
        }

        public RecipeIdentityResponse getResponse() {
            return response;
        }

        @Nonnull
        @Override
        public String toString() {
            return "IdentityCallbackClient{" +
                    ", response=" + response +
                    '}';
        }
    }

    private static class DurationCallbackClient implements UseCaseBase.Callback<RecipeDurationResponse> {

        private final String TAG = "tkm-" + DurationCallbackClient.class.getSimpleName()
                + ": ";

        private RecipeDurationResponse response;

        @Override
        public void onUseCaseSuccess(RecipeDurationResponse response) {
            System.out.println(TAG + "onSuccess:" + response);
            this.response = response;
        }

        @Override
        public void onUseCaseError(RecipeDurationResponse response) {
            System.out.println(TAG + "onError:" + response);
            this.response = response;
        }

        public RecipeDurationResponse getResponse() {
            return response;
        }

        @Nonnull
        @Override
        public String toString() {
            return "DurationCallbackClient{" +
                    "response=" + response +
                    '}';
        }
    }

    private static class CourseCallbackClient implements UseCaseBase.Callback<RecipeCourseResponse> {

        private final String TAG = "tkm-" + CourseCallbackClient.class.getSimpleName() + ": ";

        private RecipeCourseResponse response;

        @Override
        public void onUseCaseSuccess(RecipeCourseResponse response) {
            System.out.println(TAG + response);
            this.response = response;
        }

        @Override
        public void onUseCaseError(RecipeCourseResponse response) {
            System.out.println(TAG + response);
            this.response = response;
        }

        public RecipeCourseResponse getResponse() {
            return response;
        }

        @Nonnull
        @Override
        public String toString() {
            return "CourseCallbackClient{" +
                    "response=" + response +
                    '}';
        }
    }

    private static class PortionCallbackClient implements UseCaseBase.Callback<RecipePortionsResponse> {

        private final String TAG = "tkm-" + PortionCallbackClient.class.getSimpleName() + ": ";

        private RecipePortionsResponse response;

        @Override
        public void onUseCaseSuccess(RecipePortionsResponse response) {
            System.out.println(TAG + "onSuccess:" + response);
            this.response = response;
        }

        @Override
        public void onUseCaseError(RecipePortionsResponse response) {
            System.out.println(TAG + "onError:" + response);
            this.response = response;
        }

        public RecipePortionsResponse getResponse() {
            return response;
        }

        @Nonnull
        @Override
        public String toString() {
            return "PortionCallbackClient{" +
                    "response=" + response +
                    '}';
        }
    }
    // endregion helper classes --------------------------------------------------------------------

    // region external -----
    public Recipe getRecipe() {
        return SUT;
    }
    // endregion external --
}