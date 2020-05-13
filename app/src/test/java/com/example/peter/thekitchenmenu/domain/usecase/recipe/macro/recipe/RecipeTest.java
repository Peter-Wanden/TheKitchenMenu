package com.example.peter.thekitchenmenu.domain.usecase.recipe.macro.recipe;

import androidx.core.util.Pair;

import com.example.peter.thekitchenmenu.commonmocks.UseCaseSchedulerMock;
import com.example.peter.thekitchenmenu.data.repository.DomainDataAccess.GetAllDomainModelsCallback;
import com.example.peter.thekitchenmenu.data.repository.DomainDataAccess.GetDomainModelCallback;
import com.example.peter.thekitchenmenu.data.repository.recipe.RepositoryRecipeCourse;
import com.example.peter.thekitchenmenu.data.repository.recipe.RepositoryRecipeDuration;
import com.example.peter.thekitchenmenu.data.repository.recipe.RepositoryRecipeIdentity;
import com.example.peter.thekitchenmenu.data.repository.recipe.RepositoryRecipeMetadata;
import com.example.peter.thekitchenmenu.data.repository.recipe.RepositoryRecipePortions;
import com.example.peter.thekitchenmenu.data.repository.recipe.course.TestDataRecipeCourse;
import com.example.peter.thekitchenmenu.data.repository.recipe.duration.TestDataRecipeDuration;
import com.example.peter.thekitchenmenu.data.repository.recipe.identity.TestDataRecipeIdentity;
import com.example.peter.thekitchenmenu.data.repository.recipe.metadata.TestDataRecipeMetadata;
import com.example.peter.thekitchenmenu.data.repository.recipe.portions.TestDataRecipePortions;
import com.example.peter.thekitchenmenu.data.repository.source.local.recipe.metadata.datasource.parent.RecipeMetadataParentEntity;
import com.example.peter.thekitchenmenu.domain.model.CommonFailReason;
import com.example.peter.thekitchenmenu.domain.model.FailReasons;
import com.example.peter.thekitchenmenu.domain.usecase.UseCase;
import com.example.peter.thekitchenmenu.domain.usecase.UseCaseHandler;
import com.example.peter.thekitchenmenu.domain.usecase.UseCaseMetadata;
import com.example.peter.thekitchenmenu.domain.usecase.recipe.component.course.RecipeCourse;
import com.example.peter.thekitchenmenu.domain.usecase.recipe.component.course.RecipeCoursePersistenceModel;
import com.example.peter.thekitchenmenu.domain.usecase.recipe.component.course.RecipeCourseRequest;
import com.example.peter.thekitchenmenu.domain.usecase.recipe.component.course.RecipeCourseResponse;
import com.example.peter.thekitchenmenu.domain.usecase.recipe.component.duration.RecipeDuration;
import com.example.peter.thekitchenmenu.domain.usecase.recipe.component.duration.RecipeDurationPersistenceModel;
import com.example.peter.thekitchenmenu.domain.usecase.recipe.component.duration.RecipeDurationRequest;
import com.example.peter.thekitchenmenu.domain.usecase.recipe.component.duration.RecipeDurationResponse;
import com.example.peter.thekitchenmenu.domain.usecase.recipe.component.duration.RecipeDurationTest;
import com.example.peter.thekitchenmenu.domain.usecase.recipe.component.identity.RecipeIdentity;
import com.example.peter.thekitchenmenu.domain.usecase.recipe.component.identity.RecipeIdentityPersistenceModel;
import com.example.peter.thekitchenmenu.domain.usecase.recipe.component.identity.RecipeIdentityRequest;
import com.example.peter.thekitchenmenu.domain.usecase.recipe.component.identity.RecipeIdentityResponse;
import com.example.peter.thekitchenmenu.domain.usecase.recipe.component.identity.RecipeIdentityTest;
import com.example.peter.thekitchenmenu.domain.usecase.recipe.component.metadata.RecipeMetadata;
import com.example.peter.thekitchenmenu.domain.usecase.recipe.component.metadata.RecipeMetadata.ComponentName;
import com.example.peter.thekitchenmenu.domain.usecase.recipe.component.metadata.RecipeMetadata.ComponentState;
import com.example.peter.thekitchenmenu.domain.usecase.recipe.component.metadata.RecipeMetadata.FailReason;
import com.example.peter.thekitchenmenu.domain.usecase.recipe.component.metadata.RecipeMetadataPersistenceModel;
import com.example.peter.thekitchenmenu.domain.usecase.recipe.component.metadata.RecipeMetadataRequest;
import com.example.peter.thekitchenmenu.domain.usecase.recipe.component.metadata.RecipeMetadataResponse;
import com.example.peter.thekitchenmenu.domain.usecase.recipe.component.portions.RecipePortions;
import com.example.peter.thekitchenmenu.domain.usecase.recipe.component.portions.RecipePortionsPersistenceModel;
import com.example.peter.thekitchenmenu.domain.usecase.recipe.component.portions.RecipePortionsRequest;
import com.example.peter.thekitchenmenu.domain.usecase.recipe.component.portions.RecipePortionsResponse;
import com.example.peter.thekitchenmenu.domain.usecase.recipe.component.portions.RecipePortionsTest;
import com.example.peter.thekitchenmenu.domain.usecase.textvalidation.TextValidator;
import com.example.peter.thekitchenmenu.domain.utils.TimeProvider;
import com.example.peter.thekitchenmenu.domain.utils.UniqueIdProvider;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import javax.annotation.Nonnull;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

public class RecipeTest {

    private static final String TAG = "tkm-" + RecipeTest.class.getSimpleName() + ": ";

    // region constants ----------------------------------------------------------------------------
    private static final String NEW_RECIPE_DOMAIN_ID = TestDataRecipeMetadata.NEW_RECIPE_DOMAIN_ID;

    private static final RecipeMetadataParentEntity RECIPE_VALID_EXISTING = null;
    // endregion constants -------------------------------------------------------------------------

    // region helper fields ------------------------------------------------------------------------
    @Mock
    RepositoryRecipeMetadata repoMetadataMock;
    @Captor
    ArgumentCaptor<GetDomainModelCallback<RecipeMetadataPersistenceModel>> repoRecipeCallback;
    @Mock
    RepositoryRecipeIdentity repoIdentityMock;
    @Captor
    ArgumentCaptor<GetDomainModelCallback<RecipeIdentityPersistenceModel>> repoIdentityCallback;
    @Mock
    RepositoryRecipeCourse repoCourseMock;
    @Captor
    ArgumentCaptor<GetAllDomainModelsCallback<RecipeCoursePersistenceModel>> repoCourseCallback;
    @Mock
    RepositoryRecipeDuration repoDurationMock;
    @Captor
    ArgumentCaptor<GetDomainModelCallback<RecipeDurationPersistenceModel>> repoDurationCallback;
    @Mock
    RepositoryRecipePortions repoPortionsMock;
    @Captor
    ArgumentCaptor<GetDomainModelCallback<RecipePortionsPersistenceModel>> repoPortionsCallback;
    @Mock
    TimeProvider timeProviderMock;
    @Mock
    UniqueIdProvider idProviderMock;

    @Captor
    ArgumentCaptor<RecipeMetadataResponse> recipeMetadataCaptor;
    @Mock
    MetadataListener metadataListener1;
    @Mock
    MetadataListener metadataListener2;

    private UseCaseHandler handler;
    private Recipe SUT;
    private int noOfRequiredComponents;

    // endregion helper fields ---------------------------------------------------------------------

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        SUT = givenUseCase();
    }

    private Recipe givenUseCase() {
        handler = new UseCaseHandler(new UseCaseSchedulerMock());

        TextValidator textValidator = new TextValidator.Builder().
                setShortTextMinLength(RecipeIdentityTest.TITLE_MIN_LENGTH).
                setShortTextMaxLength(RecipeIdentityTest.TITLE_MAX_LENGTH).
                setLongTextMinLength(RecipeIdentityTest.DESCRIPTION_MIN_LENGTH).
                setLongTextMaxLength(RecipeIdentityTest.DESCRIPTION_MAX_LENGTH).
                build();

        final Set<ComponentName> requiredComponents = new HashSet<>();
        requiredComponents.add(ComponentName.IDENTITY);
        requiredComponents.add(ComponentName.COURSE);
        requiredComponents.add(ComponentName.DURATION);
        requiredComponents.add(ComponentName.PORTIONS);

        noOfRequiredComponents = requiredComponents.size();

        RecipeMetadata recipeMetadata = new RecipeMetadata(
                repoMetadataMock,
                idProviderMock,
                timeProviderMock,
                requiredComponents
        );
        RecipeIdentity identity = new RecipeIdentity(
                repoIdentityMock,
                idProviderMock,
                timeProviderMock,
                handler,
                textValidator
        );
        RecipeCourse course = new RecipeCourse(
                repoCourseMock,
                idProviderMock,
                timeProviderMock
        );
        RecipeDuration duration = new RecipeDuration(
                repoDurationMock,
                timeProviderMock,
                idProviderMock,
                RecipeDurationTest.MAX_PREP_TIME,
                RecipeDurationTest.MAX_COOK_TIME
        );
        RecipePortions portions = new RecipePortions(
                repoPortionsMock,
                idProviderMock,
                timeProviderMock,
                RecipePortionsTest.MAX_SERVINGS,
                RecipePortionsTest.MAX_SITTINGS
        );

        return new Recipe(
                handler,
                recipeMetadata,
                identity,
                course,
                duration,
                portions);
    }

    @Test
    public void recipeRequestNewId_invokerIssuesCommandToAllReceivers() {
        // Arrange
        RecipeRequest request = new RecipeRequest.Builder().
                getDefault().
                setDomainId(NEW_RECIPE_DOMAIN_ID).
                build();

        when(idProviderMock.getUId()).thenReturn("");

        // Act
        handler.execute(SUT, request, new RecipeCallbackClient());

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
        handler.execute(SUT, request, new RecipeCallbackClient()
        );
        when(idProviderMock.getUId()).thenReturn("testDataId");

        // Assert persistence calls
        verifyAllReposCalledAndReturnModelUnavailable(recipeId);

        // Assert recipe metadata listener updated with correct value
        verify(metadataListener1).onRecipeMetadataChanged(recipeMetadataCaptor.capture()
        );
        RecipeMetadataResponse.Model model = recipeMetadataCaptor.getValue().getModel();
        UseCaseMetadata recipeMetadata = recipeMetadataCaptor.getValue().getMetadata();

        // Assert recipe state updated
        assertEquals(
                ComponentState.INVALID_UNCHANGED,
                recipeMetadata.getState()
        );
        // Assert all components responded
        assertEquals(
                noOfRequiredComponents,
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
        SUT.registerRecipeCallback(registeredCallback);

        // Act
        handler.execute(SUT, request, passedInCallback);
        // Assert database calls
        verifyAllReposCalledAndReturnModelUnavailable(recipeId);

        // Assert, callback updated with recipe macro response
        assertEquals(
                ComponentState.INVALID_UNCHANGED,
                registeredCallback.recipeMetadataResponse.
                        getMetadata().
                        getState()
        );
        assertTrue(
                registeredCallback.recipeMetadataResponse.
                        getMetadata().
                        getFailReasons().
                        contains(FailReason.MISSING_COMPONENTS)
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
        IdentityCallbackClient registeredIdentityCallback = new IdentityCallbackClient();
        SUT.registerComponentCallback(
                new Pair<>(ComponentName.IDENTITY, registeredIdentityCallback)
        );

        // Arbitrary value, not required for this test
        when(idProviderMock.getUId()).thenReturn("");

        // Act
        handler.execute(SUT, request, passedInCallback);
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
        IdentityCallbackClient callback = new IdentityCallbackClient();
        // Act
        handler.execute(SUT, identityRequest, callback);

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
        IdentityCallbackClient callback = new IdentityCallbackClient();

        // Register RecipeCallback to get access to all recipe responses
        RecipeCallbackClient macroCallback = new RecipeCallbackClient();
        SUT.registerRecipeCallback(macroCallback);
        // Act
        handler.execute(SUT, request, callback);

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
        handler.execute(SUT, courseRequest, callback);

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
        handler.execute(SUT, durationRequest, callback);

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
        handler.execute(SUT, portionsRequest, callback);

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

        IdentityCallbackClient callbackClient = new IdentityCallbackClient();

        // Act
        handler.execute(SUT, initialRequest, callbackClient);
        // Assert all component repos called
        verifyAllReposCalledAndReturnModelUnavailable(recipeId);

        // Identity Request 2, 'existing request', update title
        String validTitle = modelUnderTest.getTitle();

        RecipeIdentityRequest.Model validTitleModel = new RecipeIdentityRequest.Model.Builder().
                basedOnResponseModel(callbackClient.response.getModel()).
                setTitle(validTitle).
                build();

        RecipeIdentityRequest validTitleRequest = new RecipeIdentityRequest.Builder().
                basedOnResponse(callbackClient.response).
                setModel(validTitleModel).
                build();

        handler.execute(SUT, validTitleRequest, callbackClient);

        // Identity Request 3, existing request, update description
        String validDescription = modelUnderTest.getDescription();
        RecipeIdentityRequest.Model validTitleDescriptionModel = new RecipeIdentityRequest.Model.
                Builder().basedOnResponseModel(callbackClient.response.getModel()).
                setDescription(validDescription).
                build();

        RecipeIdentityRequest validDescriptionRequest = new RecipeIdentityRequest.Builder().
                basedOnResponse(callbackClient.response).
                setModel(validTitleDescriptionModel).
                build();

        when(idProviderMock.getUId()).thenReturn(modelUnderTest.getDataId());
        when(timeProviderMock.getCurrentTimeInMills()).thenReturn(modelUnderTest.getLastUpdate());

        // Act
        handler.execute(SUT, validDescriptionRequest, callbackClient);

        // Assert identity component saved
        int saveTitleThenSaveDescription = 2;
        verify(
                repoIdentityMock, times(saveTitleThenSaveDescription)).
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
        when(idProviderMock.getUId()).thenReturn(modelUnderTest.getDataId()
        );

        CourseCallbackClient callback = new CourseCallbackClient();
        SUT.registerMetadataListener(metadataListener1);

        // Arrange, initial request to load data
        RecipeCourseRequest initialRequest = new RecipeCourseRequest.Builder().
                getDefault().
                setDomainId(recipeId).
                build();

        // Act
        handler.execute(SUT, initialRequest, callback);

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
        handler.execute(SUT, addCourseRequest, callback);

        // Assert correct values saved
        verify(repoCourseMock).save(eq(modelUnderTest)
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
        String recipeId =modelUnderTest.getDomainId();

        // A RecipeRequest / RecipeCallbackClient will return all data and state for a recipe
        RecipeRequest initialRequest = new RecipeRequest.Builder().
                getDefault().
                setDomainId(recipeId).
                build();
        RecipeCallbackClient recipeCallback = new RecipeCallbackClient();
        //
        SUT.registerMetadataListener(metadataListener1);
        SUT.registerMetadataListener(metadataListener2);
        SUT.unregisterStateListener(metadataListener2);

        // Act
        handler.execute(SUT, initialRequest, recipeCallback);

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

        RecipeMetadataRequest request = new RecipeMetadataRequest.Builder().
                getDefault().
                setDataId(recipeId).
                build();
        RecipeMetadataCallback callback = new RecipeMetadataCallback();

        // Act
        SUT.registerMetadataListener(metadataListener1);
        SUT.registerMetadataListener(metadataListener2);
        handler.execute(SUT, request, callback);

        // Assert database called and return data
        verifyAllReposCalledAndReturnValidExisting(recipeId);

        // Assert listeners called
        verify(metadataListener1).onRecipeMetadataChanged(recipeMetadataCaptor.capture());
        verify(metadataListener2).onRecipeMetadataChanged(recipeMetadataCaptor.capture());

        // Assert recipe component states
        HashMap<ComponentName, ComponentState> componentStates =
                recipeMetadataCaptor.getValue().getModel().getComponentStates();
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
        String recipeId = RECIPE_VALID_EXISTING.getDataId();
        RecipeMetadataCallback callback = new RecipeMetadataCallback();

        RecipeMetadataRequest request = new RecipeMetadataRequest.Builder().
                getDefault().
                setDataId(recipeId).
                build();

        // Act
        SUT.registerMetadataListener(metadataListener1);
        handler.execute(SUT, request, callback);

        // Assert database called by components and return valid data
        verifyAllReposCalledAndReturnValidExisting(recipeId);

        // Assert listeners called
        verify(metadataListener1).onRecipeMetadataChanged(recipeMetadataCaptor.capture());

        // Assert correct recipe state
        ComponentState recipeState = recipeMetadataCaptor.getValue().getMetadata().getState();
        assertEquals(ComponentState.VALID_UNCHANGED, recipeState);
    }

    @Test
    public void recipeRequest_validId_registeredComponentCallbacksCalled() {
        // Arrange
        String recipeId = RECIPE_VALID_EXISTING.getDataId();

        RecipeCallbackClient recipeCallbackClient = new RecipeCallbackClient();
        IdentityCallbackClient identityCallbackClient = new IdentityCallbackClient();

        RecipeMetadataRequest request = new RecipeMetadataRequest.Builder().
                getDefault().
                setDataId(recipeId).
                build();
        RecipeMetadataCallback callback = new RecipeMetadataCallback();

        // Act
        SUT.registerMetadataListener(metadataListener1);
        SUT.registerRecipeCallback(recipeCallbackClient);
        SUT.registerComponentCallback(new Pair<>(ComponentName.IDENTITY, identityCallbackClient));

        handler.execute(SUT, request, callback);
        // Assert
        verifyAllReposCalledAndReturnValidExisting(recipeId);

        // Assert recipe state listeners updated
        verify(metadataListener1).onRecipeMetadataChanged(recipeMetadataCaptor.capture());
        ComponentState recipeStateListenerState = recipeMetadataCaptor.getValue().getMetadata().getState();
        assertEquals(ComponentState.VALID_UNCHANGED, recipeStateListenerState);

        // Assert recipe macro response updated
//        ComponentState recipeStateMacroState = recipeCallbackClient.
//                getRecipeResponseOnSuccess().
//                getModel().
//                getRecipeStateResponse().
//                getState();
//        assertEquals(ComponentState.VALID_UNCHANGED, recipeStateMacroState);

        // Assert recipe component callback
        ComponentState identityComponentState = identityCallbackClient.
                getResponse().
                getMetadata().
                getState();
        assertEquals(ComponentState.VALID_UNCHANGED, identityComponentState);

        System.out.println(TAG + metadataListener1.getResponse());
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
        verify(repoMetadataMock).getActiveByDomainId(eq(recipeId), repoRecipeCallback.capture());
        repoRecipeCallback.getValue().onModelUnavailable();
    }

    private void verifyRepoIdentityCalledAndReturnDataUnavailable(String recipeId) {
        verify(repoIdentityMock).getActiveByDomainId(eq(recipeId), repoIdentityCallback.capture());
        repoIdentityCallback.getValue().onModelUnavailable();
    }

    private void verifyRepoCoursesCalledAndReturnDataUnavailable(String recipeId) {
        verify(repoCourseMock).getAllActiveByDomainId(eq(recipeId), repoCourseCallback.capture());
        repoCourseCallback.getValue().onModelsUnavailable();
    }

    private void verifyRepoDurationCalledAndReturnDataUnavailable(String recipeId) {
        verify(repoDurationMock).getActiveByDomainId(eq(recipeId), repoDurationCallback.capture());
        repoDurationCallback.getValue().onModelUnavailable();
    }

    private void verifyRepoPortionsCalledAndReturnDataUnavailable(String recipeId) {
        verify(repoPortionsMock).getActiveByDomainId(eq(recipeId), repoPortionsCallback.capture());
        repoPortionsCallback.getValue().onModelUnavailable();
    }

    private void verifyAllReposCalledAndReturnValidExisting(String recipeId) {
        verify(repoMetadataMock).getActiveByDomainId(eq(recipeId), repoRecipeCallback.capture());
        repoRecipeCallback.getValue().onModelLoaded(TestDataRecipeMetadata.
                getActiveByDomainId(recipeId)
        );
        verify(repoIdentityMock).getActiveByDomainId(eq(recipeId), repoIdentityCallback.capture());
        repoIdentityCallback.getValue().onModelLoaded(TestDataRecipeIdentity.
                getActiveByDomainId(recipeId)
        );
        verify(repoCourseMock).getAllActiveByDomainId(eq(recipeId), repoCourseCallback.capture());
        repoCourseCallback.getValue().onAllLoaded(TestDataRecipeCourse.
                getAllExistingActiveByDomainId(recipeId)
        );
        verify(repoDurationMock).getActiveByDomainId(eq(recipeId), repoDurationCallback.capture());
        repoDurationCallback.getValue().onModelLoaded(TestDataRecipeDuration.
                getActiveByDomainId(recipeId)
        );
        verify(repoPortionsMock).getActiveByDomainId(eq(recipeId), repoPortionsCallback.capture());
        repoPortionsCallback.getValue().onModelLoaded(TestDataRecipePortions.
                getActiveByDomainId(recipeId)
        );
    }

    private void whenTimeProviderReturnTime(long time) {
        when(timeProviderMock.getCurrentTimeInMills()).thenReturn(time);
    }
    // endregion helper methods --------------------------------------------------------------------

    // region helper classes -----------------------------------------------------------------------
    private static class MetadataListener implements Recipe.RecipeMetadataListener {
        private RecipeMetadataResponse response;

        @Override
        public void onRecipeMetadataChanged(RecipeMetadataResponse response) {
            this.response = response;
        }

        public RecipeMetadataResponse getResponse() {
            return response;
        }
    }

    private static class RecipeCallbackClient implements UseCase.Callback<RecipeResponse> {

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
        public void onSuccess(RecipeResponse response) {
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
        public void onError(RecipeResponse response) {
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

    private static class RecipeMetadataCallback implements UseCase.Callback<RecipeMetadataResponse> {

        private RecipeMetadataResponse response;

        @Override
        public void onSuccess(RecipeMetadataResponse response) {
            this.response = response;
        }

        @Override
        public void onError(RecipeMetadataResponse response) {
            this.response = response;
        }

        public RecipeMetadataResponse getResponse() {
            return response;
        }

        @Nonnull
        @Override
        public String toString() {
            return "RecipeCallbackClient{" +
                    "response=" + response +
                    '}';
        }
    }

    private static class IdentityCallbackClient implements UseCase.Callback<RecipeIdentityResponse> {

        private final String TAG = "tkm-" +
                IdentityCallbackClient.class.getSimpleName() + ": ";

        private RecipeIdentityResponse response;

        @Override
        public void onSuccess(RecipeIdentityResponse response) {
            System.out.println(TAG + response);
            this.response = response;
        }

        @Override
        public void onError(RecipeIdentityResponse response) {
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

    private static class DurationCallbackClient implements UseCase.Callback<RecipeDurationResponse> {

        private final String TAG = "tkm-" + DurationCallbackClient.class.getSimpleName()
                + ": ";

        private RecipeDurationResponse response;

        @Override
        public void onSuccess(RecipeDurationResponse response) {
            System.out.println(TAG + "onSuccess:" + response);
            this.response = response;
        }

        @Override
        public void onError(RecipeDurationResponse response) {
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

    private static class CourseCallbackClient implements UseCase.Callback<RecipeCourseResponse> {

        private final String TAG = "tkm-" + CourseCallbackClient.class.getSimpleName() + ": ";

        private RecipeCourseResponse response;

        @Override
        public void onSuccess(RecipeCourseResponse response) {
            System.out.println(TAG + response);
            this.response = response;
        }

        @Override
        public void onError(RecipeCourseResponse response) {
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

    private static class PortionCallbackClient implements UseCase.Callback<RecipePortionsResponse> {

        private final String TAG = "tkm-" + PortionCallbackClient.class.getSimpleName() + ": ";

        private RecipePortionsResponse response;

        @Override
        public void onSuccess(RecipePortionsResponse response) {
            System.out.println(TAG + "onSuccess:" + response);
            this.response = response;
        }

        @Override
        public void onError(RecipePortionsResponse response) {
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
}