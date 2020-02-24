package com.example.peter.thekitchenmenu.domain.usecase.recipemediator;

import androidx.core.util.Pair;

import com.example.peter.thekitchenmenu.commonmocks.UseCaseSchedulerMock;
import com.example.peter.thekitchenmenu.data.entity.RecipeCourseEntity;
import com.example.peter.thekitchenmenu.data.entity.RecipeDurationEntity;
import com.example.peter.thekitchenmenu.data.entity.RecipeEntity;
import com.example.peter.thekitchenmenu.data.entity.RecipeIdentityEntity;
import com.example.peter.thekitchenmenu.data.entity.RecipePortionsEntity;
import com.example.peter.thekitchenmenu.data.repository.DataSource;
import com.example.peter.thekitchenmenu.data.repository.RepositoryRecipe;
import com.example.peter.thekitchenmenu.data.repository.RepositoryRecipeCourse;
import com.example.peter.thekitchenmenu.data.repository.RepositoryRecipeDuration;
import com.example.peter.thekitchenmenu.data.repository.RepositoryRecipeIdentity;
import com.example.peter.thekitchenmenu.data.repository.RepositoryRecipePortions;
import com.example.peter.thekitchenmenu.domain.usecase.CommonFailReason;
import com.example.peter.thekitchenmenu.domain.usecase.UseCase;
import com.example.peter.thekitchenmenu.domain.usecase.UseCaseHandler;
import com.example.peter.thekitchenmenu.domain.usecase.recipe.recipe.Recipe;
import com.example.peter.thekitchenmenu.domain.usecase.recipe.recipe.RecipeResponse;
import com.example.peter.thekitchenmenu.domain.usecase.recipe.recipemacro.RecipeMacro;
import com.example.peter.thekitchenmenu.domain.usecase.recipe.recipemacro.RecipeMacroResponse;
import com.example.peter.thekitchenmenu.domain.usecase.recipe.recipe.RecipeRequest;
import com.example.peter.thekitchenmenu.domain.usecase.recipe.recipeduration.RecipeDurationRequest;
import com.example.peter.thekitchenmenu.domain.usecase.recipe.recipestate.RecipeStateCalculator;
import com.example.peter.thekitchenmenu.domain.usecase.recipe.recipestate.RecipeStateResponse;
import com.example.peter.thekitchenmenu.domain.usecase.recipe.recipeidentity.RecipeIdentity;
import com.example.peter.thekitchenmenu.domain.usecase.recipe.recipeidentity.RecipeIdentityRequest;
import com.example.peter.thekitchenmenu.domain.usecase.recipe.recipeidentity.RecipeIdentityResponse;
import com.example.peter.thekitchenmenu.domain.usecase.recipe.recipecourse.RecipeCourse;
import com.example.peter.thekitchenmenu.domain.usecase.recipe.recipecourse.RecipeCourseRequest;
import com.example.peter.thekitchenmenu.domain.usecase.recipe.recipecourse.RecipeCourseResponse;
import com.example.peter.thekitchenmenu.domain.usecase.recipe.recipeduration.RecipeDuration;
import com.example.peter.thekitchenmenu.domain.usecase.recipe.recipeduration.RecipeDurationResponse;
import com.example.peter.thekitchenmenu.domain.usecase.recipeduration.RecipeDurationTest;
import com.example.peter.thekitchenmenu.domain.usecase.recipeidentity.RecipeIdentityTest;
import com.example.peter.thekitchenmenu.domain.usecase.recipe.recipeportions.RecipePortions;
import com.example.peter.thekitchenmenu.domain.usecase.recipe.recipeportions.RecipePortionsRequest;
import com.example.peter.thekitchenmenu.domain.usecase.recipe.recipeportions.RecipePortionsResponse;
import com.example.peter.thekitchenmenu.domain.usecase.recipeportions.RecipePortionsTest;
import com.example.peter.thekitchenmenu.domain.usecase.textvalidation.TextValidator;
import com.example.peter.thekitchenmenu.domain.utils.TimeProvider;
import com.example.peter.thekitchenmenu.domain.utils.UniqueIdProvider;
import com.example.peter.thekitchenmenu.testdata.TestDataRecipeCourseEntity;
import com.example.peter.thekitchenmenu.testdata.TestDataRecipeDurationEntity;
import com.example.peter.thekitchenmenu.testdata.TestDataRecipeEntity;
import com.example.peter.thekitchenmenu.testdata.TestDataRecipeIdentityEntity;
import com.example.peter.thekitchenmenu.testdata.TestDataRecipePortionsEntity;

import org.junit.*;
import org.mockito.*;

import java.util.HashMap;
import java.util.List;
import java.util.Objects;

import javax.annotation.Nonnull;

import static com.example.peter.thekitchenmenu.domain.usecase.recipe.recipestate.RecipeStateCalculator.*;
import static com.example.peter.thekitchenmenu.domain.usecase.recipe.recipestate.RecipeStateCalculator.ComponentName.COURSE;
import static com.example.peter.thekitchenmenu.domain.usecase.recipe.recipestate.RecipeStateCalculator.ComponentName.DURATION;
import static com.example.peter.thekitchenmenu.domain.usecase.recipe.recipestate.RecipeStateCalculator.ComponentName.IDENTITY;
import static com.example.peter.thekitchenmenu.domain.usecase.recipe.recipestate.RecipeStateCalculator.ComponentName.PORTIONS;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

public class RecipeMacroTest {

    private static final String TAG = "tkm-" + RecipeMacroTest.class.getSimpleName() + ": ";

    // region constants ----------------------------------------------------------------------------
    private static final RecipeEntity INVALID_NEW_RECIPE =
            TestDataRecipeEntity.getNewInvalid();

    private static final RecipeIdentityEntity IDENTITY_INVALID_NEW_EMPTY =
            TestDataRecipeIdentityEntity.getInvalidNewEmpty();
    private static final RecipeIdentityEntity IDENTITY_VALID_NEW_COMPLETE =
            TestDataRecipeIdentityEntity.getValidNewComplete();

    private static final RecipeEntity RECIPE_VALID_EXISTING =
            TestDataRecipeEntity.getValidExisting();
    private static final RecipeIdentityEntity IDENTITY_VALID_EXISTING_COMPLETE =
            TestDataRecipeIdentityEntity.getValidExistingTitleValidDescriptionValid();
    private static final RecipeDurationEntity DURATION_VALID_EXISTING_COMPLETE =
            TestDataRecipeDurationEntity.getValidExistingComplete();
    private static final RecipePortionsEntity PORTIONS_VALID_EXISTING_NINE =
            TestDataRecipePortionsEntity.getExistingValidNinePortions();
    private static final List<RecipeCourseEntity> COURSES_VALID_EXISTING_ALL =
            TestDataRecipeCourseEntity.getAllRecipeCourses();
    // endregion constants -------------------------------------------------------------------------

    // region helper fields ------------------------------------------------------------------------
    @Mock
    RepositoryRecipe repoRecipeMock;
    @Captor
    ArgumentCaptor<DataSource.GetEntityCallback<RecipeEntity>> repoRecipeCallback;
    @Mock
    RepositoryRecipeIdentity repoIdentityMock;
    @Captor
    ArgumentCaptor<DataSource.GetEntityCallback<RecipeIdentityEntity>> repoIdentityCallback;
    @Mock
    RepositoryRecipeCourse repoCourseMock;
    @Captor
    ArgumentCaptor<DataSource.GetAllCallback<RecipeCourseEntity>> repoCourseCallback;
    @Mock
    RepositoryRecipeDuration repoDurationMock;
    @Captor
    ArgumentCaptor<DataSource.GetEntityCallback<RecipeDurationEntity>> repoDurationCallback;
    @Mock
    RepositoryRecipePortions repoPortionsMock;
    @Captor
    ArgumentCaptor<DataSource.GetEntityCallback<RecipePortionsEntity>> repoPortionsCallback;
    @Mock
    TimeProvider timeProviderMock;
    @Mock
    UniqueIdProvider idProviderMock;
    @Captor
    ArgumentCaptor<RecipeStateResponse> recipeStateCaptor;

    private UseCaseHandler handler;
    @Mock
    private StateListenerClient recipeStateListener1;
    @Mock
    private StateListenerClient recipeStateListener2;

    private RecipeMacro SUT;

    // endregion helper fields ---------------------------------------------------------------------

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        SUT = givenUseCase();
    }

    private RecipeMacro givenUseCase() {
        TextValidator textValidator = new TextValidator.Builder().
                setShortTextMinLength(RecipeIdentityTest.TITLE_MIN_LENGTH).
                setShortTextMaxLength(RecipeIdentityTest.TITLE_MAX_LENGTH).
                setLongTextMinLength(RecipeIdentityTest.DESCRIPTION_MIN_LENGTH).
                setLongTextMaxLength(RecipeIdentityTest.DESCRIPTION_MAX_LENGTH).
                build();

        UseCaseHandler handler = new UseCaseHandler(new UseCaseSchedulerMock());
        this.handler = handler;

        Recipe recipe = new Recipe(
                repoRecipeMock,
                timeProviderMock
        );

        RecipeIdentity identity = new RecipeIdentity(
                repoIdentityMock,
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

        RecipeStateCalculator stateCalculator = new RecipeStateCalculator();

        return new RecipeMacro<>(
                handler,
                stateCalculator,
                recipe,
                identity,
                course,
                duration,
                portions);
    }

    // todo - test for delete and favorite as separate macro commands?

    @Test
    public void recipeRequestNewId_invokerIssuesCommand_toAllReceivers() {
        // Arrange
        String recipeId = INVALID_NEW_RECIPE.getId();

        RecipeCallbackClient callback = new RecipeCallbackClient();
        RecipeRequest request = RecipeRequest.Builder.
                getDefault().
                setId(recipeId).
                build();
        // Act
        handler.execute(SUT, request, callback);
        // Assert database called for every component, meaning command has been issued.
        verifyAllReposCalledAndReturnDataUnavailable(recipeId);
    }

    @Test
    public void recipeRequestNewId_invokerIssuesCommand_stateListenersUpdatedWithCorrectValues() {
        // Arrange
        String recipeId = INVALID_NEW_RECIPE.getId();
        RecipeRequest request = RecipeRequest.Builder.
                getDefault().
                setId(recipeId).
                build();
        RecipeCallbackClient callback = new RecipeCallbackClient();

        // Act
        SUT.registerStateListener(recipeStateListener1);
        handler.execute(SUT, request, callback);

        // Assert database calls
        verifyAllReposCalledAndReturnDataUnavailable(recipeId);

        // Assert recipe state listener updated with correct value
        verify(recipeStateListener1).recipeStateChanged(recipeStateCaptor.capture());
        RecipeStateResponse recipeStateResponse = recipeStateCaptor.getValue();
        assertEquals(RecipeState.DATA_UNAVAILABLE, recipeStateResponse.getState());

        // Assert recipe state updated
        assertEquals(RecipeState.DATA_UNAVAILABLE, recipeStateResponse.getState());
        assertEquals(4, recipeStateResponse.getComponentStates().size());

        // Assert identity component status
        assertTrue(recipeStateResponse.getComponentStates().containsKey(IDENTITY));
        assertEquals(ComponentState.DATA_UNAVAILABLE, recipeStateResponse.getComponentStates().
                get(IDENTITY));

        // Assert course component state
        assertTrue(recipeStateResponse.getComponentStates().containsKey(COURSE));
        assertEquals(ComponentState.DATA_UNAVAILABLE, recipeStateResponse.getComponentStates().
                get(COURSE));

        // Assert duration component state
        assertTrue(recipeStateResponse.getComponentStates().containsKey(DURATION));
        assertEquals(ComponentState.DATA_UNAVAILABLE, recipeStateResponse.getComponentStates().
                get(DURATION));

        // Assert portions component state
        assertTrue(recipeStateResponse.getComponentStates().containsKey(ComponentName.PORTIONS));
        assertEquals(ComponentState.DATA_UNAVAILABLE, recipeStateResponse.getComponentStates().get(
                ComponentName.PORTIONS));
    }

    @Test
    public void recipeRequestNewId_invokerIssuesCommand_useCaseCallbackINVALID_UNCHANGED_INVALID_COMPONENTS() {
        // Arrange
        String recipeId = INVALID_NEW_RECIPE.getId();
        RecipeRequest request = RecipeRequest.Builder.
                getDefault().
                setId(recipeId).
                build();
        RecipeCallbackClient passedInCallback = new RecipeCallbackClient();
        MacroCallbackClient registeredMacroResponseCallback = new MacroCallbackClient();
        // Act
        SUT.registerMacroCallback(registeredMacroResponseCallback);
        handler.execute(SUT, request, passedInCallback);

        // Assert database calls
        verifyAllReposCalledAndReturnDataUnavailable(recipeId);
        // Assert recipeResponse called back
        System.out.println(TAG + passedInCallback);

        // Assert, callback updated with recipe macro response
        System.out.println(TAG + registeredMacroResponseCallback);

        assertEquals(RecipeState.DATA_UNAVAILABLE,
                registeredMacroResponseCallback.getRecipeMacroResponseOnError().
                        getRecipeStateResponse().
                        getState());
        assertTrue(registeredMacroResponseCallback.getRecipeMacroResponseOnError().
                getRecipeStateResponse().
                getFailReasons().
                contains(FailReason.MISSING_COMPONENTS));
    }

    @Test
    public void recipeRequestNewId_invokerIssuesCommand_recipeResponseListenersUpdated() {
        // Arrange
        String recipeId = INVALID_NEW_RECIPE.getId();
        RecipeRequest request = RecipeRequest.Builder.
                getDefault().
                setId(recipeId).
                build();
        RecipeCallbackClient passedInCallback = new RecipeCallbackClient();
        MacroCallbackClient registeredCallback = new MacroCallbackClient();
        // Act
        SUT.registerMacroCallback(registeredCallback);
        handler.execute(SUT, request, passedInCallback);
        // Assert
        verifyAllReposCalledAndReturnDataUnavailable(recipeId);
        // Assert recipe response registered callback updated
        System.out.println(TAG + "  passedInCallback:" + passedInCallback);
        System.out.println(TAG + "registeredCallback:" + registeredCallback);
//        assertEquals(callback, registeredCallback);
    }

    @Test
    public void identityRequestNewId_initialRequest_invokerIssuesCommandToAllReceivers() {
        // Arrange
        String recipeId = INVALID_NEW_RECIPE.getId();
        IdentityCallbackClient callback = new IdentityCallbackClient();

        RecipeIdentityRequest identityRequest = RecipeIdentityRequest.Builder.getDefault().
                setId(recipeId).
                build();
        // Act
        handler.execute(SUT, identityRequest, callback);

        // Assert all components receive recipe ID and call their repos
        verifyAllReposCalledAndReturnDataUnavailable(recipeId);

        // Assert identity response received
        RecipeIdentityResponse response = callback.getResponse();
        assertTrue(response.getFailReasons().contains(CommonFailReason.DATA_UNAVAILABLE));
    }

    @Test
    public void identityRequestNewId_invokerIssuesCommand_correctMacroComponentResponses() {
        // Arrange
        String recipeId = INVALID_NEW_RECIPE.getId();
        RecipeIdentityRequest request = RecipeIdentityRequest.Builder.
                getDefault().
                setId(recipeId).
                build();
        IdentityCallbackClient callback = new IdentityCallbackClient();

        MacroCallbackClient macroCallback = new MacroCallbackClient();
        // Act
        SUT.registerMacroCallback(macroCallback);
        handler.execute(SUT, request, callback);

        // Assert database calls
        verifyAllReposCalledAndReturnDataUnavailable(recipeId);

        // Assert response from identity component
        RecipeIdentityResponse identityResponse = macroCallback.getIdentityOnError();
        assertEquals(ComponentState.DATA_UNAVAILABLE, identityResponse.getState());
        assertEquals(1, identityResponse.getFailReasons().size());
        assertTrue(identityResponse.getFailReasons().contains(CommonFailReason.DATA_UNAVAILABLE));

        // Assert response from courses component
        RecipeCourseResponse courseResponse = macroCallback.getCourseOnError();
        assertEquals(ComponentState.DATA_UNAVAILABLE, courseResponse.getState());
        assertEquals(1, courseResponse.getFailReasons().size());
        assertTrue(courseResponse.getFailReasons().contains(CommonFailReason.DATA_UNAVAILABLE));

        // Assert response from duration component
        RecipeDurationResponse durationResponse = macroCallback.getDurationOnError();
        assertEquals(ComponentState.DATA_UNAVAILABLE, durationResponse.getState());
        assertEquals(1, durationResponse.getFailReasons().size());
        assertTrue(durationResponse.getFailReasons().contains(CommonFailReason.DATA_UNAVAILABLE));

        // Assert response from portions component
        RecipePortionsResponse portionResponse = macroCallback.getPortionsOnError();
        assertEquals(ComponentState.DATA_UNAVAILABLE, portionResponse.getState());
        assertEquals(1, portionResponse.getFailReasons().size());
        assertTrue(portionResponse.getFailReasons().contains(CommonFailReason.DATA_UNAVAILABLE));
    }

    @Test
    public void coursesRequestNewId_initialRequest_invokerIssuesCommandToAllReceivers() {
        // Arrange
        String recipeId = INVALID_NEW_RECIPE.getId();
        CourseCallbackClient callback = new CourseCallbackClient();

        RecipeCourseRequest courseRequest = RecipeCourseRequest.Builder.getDefault().
                setId(recipeId).
                build();
        // Act
        handler.execute(SUT, courseRequest, callback);

        // Assert all components receive recipe ID and call their repos
        verifyAllReposCalledAndReturnDataUnavailable(recipeId);

        // Assert course response received
        RecipeCourseResponse response = callback.getResponse();
        assertTrue(response.getFailReasons().contains(CommonFailReason.DATA_UNAVAILABLE));
    }

    @Test
    public void durationRequestNewId_initialRequest_invokerIssuesCommandToAllReceivers() {
        // Arrange
        String recipeId = INVALID_NEW_RECIPE.getId();
        DurationCallbackClient callback = new DurationCallbackClient();

        RecipeDurationRequest durationRequest = RecipeDurationRequest.Builder.getDefault().
                setId(recipeId).
                build();

        // Act
        handler.execute(SUT, durationRequest, callback);

        // Assert all components receive recipe ID and call their repos
        verifyAllReposCalledAndReturnDataUnavailable(recipeId);

        // Assert response received
        RecipeDurationResponse response = callback.getResponse();
        assertTrue(response.getFailReasons().contains(CommonFailReason.DATA_UNAVAILABLE));
    }

    @Test
    public void portionsRequestNewId_initialRequest_invokerIssuesCommandToAllReceivers() {
        // Arrange
        String recipeId = INVALID_NEW_RECIPE.getId();
        PortionCallbackClient callback = new PortionCallbackClient();

        RecipePortionsRequest portionsRequest = RecipePortionsRequest.Builder.getDefault().
                setId(recipeId).
                build();
        // Act
        handler.execute(SUT, portionsRequest, callback);

        // Assert all components receive recipe ID and call their repos
        verifyAllReposCalledAndReturnDataUnavailable(recipeId);

        // Assert response received
        RecipePortionsResponse response = callback.getResponse();
        assertTrue(response.getFailReasons().contains(CommonFailReason.DATA_UNAVAILABLE));
    }

    @Test
    public void identityRequestNewId_titleValidDescriptionValid_identityStateVALID_CHANGED() {
        // Arrange
        String recipeId = IDENTITY_INVALID_NEW_EMPTY.getId();
        IdentityCallbackClient callback = new IdentityCallbackClient();

        ArgumentCaptor<RecipeIdentityEntity> identityEntity = ArgumentCaptor.
                forClass(RecipeIdentityEntity.class);
        whenTimeProviderReturnTime(IDENTITY_VALID_NEW_COMPLETE.getCreateDate());

        RecipeIdentityRequest firstRequest = RecipeIdentityRequest.Builder.
                getDefault().
                setId(recipeId).
                build();

        // Act - RecipeIdentityRequest/Response 1 - new request
        handler.execute(SUT, firstRequest, callback);
        // Assert all component repos called
        verifyAllReposCalledAndReturnDataUnavailable(recipeId);

        // RecipeIdentityRequest/Response 2 - existing request
        String validTitle = IDENTITY_VALID_NEW_COMPLETE.getTitle();
        RecipeIdentityRequest.Model validTitleModel = RecipeIdentityRequest.Model.Builder.
                basedOnIdentityResponseModel(
                        callback.
                                getResponse().
                                getModel()).
                setTitle(validTitle).
                build();

        RecipeIdentityRequest validTitleRequest = new RecipeIdentityRequest.Builder().
                setId(recipeId).
                setModel(validTitleModel).
                build();
        handler.execute(SUT, validTitleRequest, callback);

        // RecipeIdentityRequest/Response 3
        String validDescription = IDENTITY_VALID_NEW_COMPLETE.getDescription();
        RecipeIdentityRequest.Model validTitleDescriptionModel = RecipeIdentityRequest.Model.Builder.
                basedOnIdentityResponseModel(
                        callback.
                                getResponse().
                                getModel()).
                setDescription(validDescription).
                build();

        RecipeIdentityRequest validDescriptionRequest = new RecipeIdentityRequest.Builder().
                setId(recipeId).
                setModel(validTitleDescriptionModel).
                build();

        // Act
        handler.execute(SUT, validDescriptionRequest, callback);

        // Assert identity component saved
        verify(repoIdentityMock, times((2))).save(identityEntity.capture());
        assertEquals(IDENTITY_VALID_NEW_COMPLETE, identityEntity.getValue());

        // Assert identity component response values
        RecipeIdentityResponse response = callback.getResponse();
        assertEquals(validTitle, response.getModel().getTitle());
        assertEquals(validDescription, response.getModel().getDescription());

        // Assert identity component state
        assertEquals(ComponentState.VALID_CHANGED, response.getState());
        assertEquals(1, response.getFailReasons().size());
        assertTrue(response.getFailReasons().contains(CommonFailReason.NONE));
    }

    @Test
    public void coursesRequestNewId_newCourseAdded_coursesStateVALID_CHANGED() {
        // Arrange
        CourseCallbackClient callback = new CourseCallbackClient();
        RecipeCourseEntity expectedCourseEntity = TestDataRecipeCourseEntity.getRecipeCourseZero();
        ArgumentCaptor<RecipeCourseEntity> actualCourseEntity = ArgumentCaptor.forClass(RecipeCourseEntity.class);

        String recipeId = expectedCourseEntity.getRecipeId();

        whenTimeProviderReturnTime(expectedCourseEntity.getCreateDate());
        when(idProviderMock.getUId()).thenReturn(expectedCourseEntity.getId());

        RecipeCourseRequest initialRequest = RecipeCourseRequest.Builder.getDefault().
                setId(recipeId).
                build();

        // Act
        SUT.registerStateListener(recipeStateListener1);
        handler.execute(SUT, initialRequest, callback);

        // Assert
        verifyAllReposCalledAndReturnDataUnavailable(recipeId);

        // Arrange
        RecipeCourseRequest addCourseRequest = new RecipeCourseRequest.Builder().
                setId(recipeId).
                setCourse(RecipeCourse.Course.COURSE_ZERO).
                setAddCourse(true).
                build();

        // Act
        handler.execute(SUT, addCourseRequest, callback);

        // Assert correct values saved
        verify(repoCourseMock).save(actualCourseEntity.capture());
        assertEquals(expectedCourseEntity, actualCourseEntity.getValue());

        // Assert courses response
        RecipeCourseResponse courseResponse = callback.getResponse();
        assertEquals(ComponentState.VALID_CHANGED, courseResponse.getState());
        assertTrue(courseResponse.getCourseList().containsKey(RecipeCourse.Course.COURSE_ZERO));

        // Assert listener updated
        verify(recipeStateListener1, times((2))).recipeStateChanged(recipeStateCaptor.capture());
        assertEquals(ComponentState.VALID_CHANGED, recipeStateCaptor.getValue().
                getComponentStates().get(COURSE));
    }

    @Test
    public void recipeRequestExistingId_validData_onlyRegisteredListenersNotified() {
        // Arrange
        String recipeId = RECIPE_VALID_EXISTING.getId();
        RecipeCallbackClient callback = new RecipeCallbackClient();

        RecipeRequest request = RecipeRequest.Builder.
                getDefault().
                setId(recipeId).
                build();
        // Act
        SUT.registerStateListener(recipeStateListener1);
        SUT.registerStateListener(recipeStateListener2);
        SUT.unregisterStateListener(recipeStateListener2);
        handler.execute(SUT, request, callback);

        // Assert database called and return valid data for all components
        verifyAllReposCalledAndReturnValidExisting(recipeId);

        // Assert listeners called
        verify(recipeStateListener1).recipeStateChanged(any(RecipeStateResponse.class));
        verifyNoMoreInteractions(recipeStateListener2);
    }

    @Test
    public void recipeRequestExistingId_validData_componentStateVALID_UNCHANGED() {
        // Arrange
        String recipeId = RECIPE_VALID_EXISTING.getId();
        RecipeCallbackClient callback = new RecipeCallbackClient();
        RecipeRequest request = RecipeRequest.Builder.
                getDefault().
                setId(recipeId).
                build();
        // Act
        SUT.registerStateListener(recipeStateListener1);
        SUT.registerStateListener(recipeStateListener2);
        handler.execute(SUT, request, callback);

        // Assert database called and return data
        verifyAllReposCalledAndReturnValidExisting(recipeId);

        // Assert listeners called
        verify(recipeStateListener1).recipeStateChanged(recipeStateCaptor.capture());
        verify(recipeStateListener2).recipeStateChanged(recipeStateCaptor.capture());

        // Assert recipe component states
        HashMap<ComponentName, ComponentState> componentStates = recipeStateCaptor.getValue().
                getComponentStates();
        assertEquals(ComponentState.VALID_UNCHANGED, componentStates.get(IDENTITY));
        assertEquals(ComponentState.VALID_UNCHANGED, componentStates.get(COURSE));
        assertEquals(ComponentState.VALID_UNCHANGED, componentStates.get(DURATION));
        assertEquals(ComponentState.VALID_UNCHANGED, componentStates.get(PORTIONS));
    }

    @Test
    public void recipeRequestExistingId_validData_recipeStateVALID_UNCHANGED() {
        // Arrange
        String recipeId = RECIPE_VALID_EXISTING.getId();
        RecipeCallbackClient callback = new RecipeCallbackClient();

        RecipeRequest request = RecipeRequest.Builder.
                getDefault().
                setId(recipeId).
                build();

        // Act
        SUT.registerStateListener(recipeStateListener1);
        handler.execute(SUT, request, callback);

        // Assert database called by components and return valid data
        verifyAllReposCalledAndReturnValidExisting(recipeId);

        // Assert listeners called
        verify(recipeStateListener1).recipeStateChanged(recipeStateCaptor.capture());

        // Assert correct recipe state
        RecipeState recipeState = recipeStateCaptor.getValue().getState();
        assertEquals(RecipeState.VALID_UNCHANGED, recipeState);
    }

    @Test
    public void recipeRequest_validId_registeredComponentCallbacksCalled() {
        // Arrange
        String recipeId = RECIPE_VALID_EXISTING.getId();

        MacroCallbackClient macroCallbackClient = new MacroCallbackClient();
        IdentityCallbackClient identityCallbackClient = new IdentityCallbackClient();

        RecipeRequest request = RecipeRequest.Builder.
                getDefault().
                setId(recipeId).
                build();
        RecipeCallbackClient callback = new RecipeCallbackClient();

        // Act
        SUT.registerStateListener(recipeStateListener1);
        SUT.registerMacroCallback(macroCallbackClient);
        SUT.registerComponentCallback(new Pair<>(IDENTITY, identityCallbackClient));

        handler.execute(SUT, request, callback);
        // Assert
        verifyAllReposCalledAndReturnValidExisting(recipeId);

        // Assert recipe state listeners updated
        verify(recipeStateListener1).recipeStateChanged(recipeStateCaptor.capture());
        RecipeState recipeStateListenerState = recipeStateCaptor.getValue().getState();
        assertEquals(RecipeState.VALID_UNCHANGED, recipeStateListenerState);

        // Assert recipe macro response updated
        RecipeState recipeStateMacroState = macroCallbackClient.
                getRecipeMacroResponseOnSuccess().
                getRecipeStateResponse().
                getState();
        assertEquals(RecipeState.VALID_UNCHANGED, recipeStateMacroState);

        // Assert recipe component callback
        ComponentState identityComponentState = identityCallbackClient.getResponse().getState();
        assertEquals(ComponentState.VALID_UNCHANGED, identityComponentState);

        System.out.println(TAG + recipeStateListener1.getStateResponse());
    }

    @Test
    public void recipeRequest_clone_allComponentsCloned() {
        // Arrange
        RecipeEntity cloneFromRecipe = TestDataRecipeEntity.getValidFromAnotherUser();
        RecipeEntity cloneToRecipe = TestDataRecipeEntity.getValidNewCloned();

        RecipeIdentityEntity cloneFromIdentity = TestDataRecipeIdentityEntity.
                getValidCompleteFromAnotherUser();
        RecipeIdentityEntity cloneToIdentity = TestDataRecipeIdentityEntity.
                getValidCompleteAfterCloned();

        List<RecipeCourseEntity> cloneFromCourses = TestDataRecipeCourseEntity.
                getAllByRecipeId(cloneFromRecipe.getId());

        RecipeDurationEntity cloneFromDuration = TestDataRecipeDurationEntity.
                getValidCompleteFromAnotherUser();
        RecipeDurationEntity cloneToDuration = TestDataRecipeDurationEntity.getValidNewCloned();

        RecipePortionsEntity cloneFromPortions = TestDataRecipePortionsEntity.
                getExistingValidNinePortions();
        RecipePortionsEntity cloneToPortions = TestDataRecipePortionsEntity.
                getExistingValidClone();
        when(idProviderMock.getUId()).thenReturn(cloneToPortions.getId());

        when(timeProviderMock.getCurrentTimeInMills()).
                thenReturn(cloneToRecipe.getCreateDate());

        RecipeRequest cloneRequest = new RecipeRequest.Builder().
                setId(cloneFromRecipe.getId()).
                setCloneToId(cloneToRecipe.getId()).
                build();
        RecipeCallbackClient recipeCallback = new RecipeCallbackClient();

        // Act
        handler.execute(SUT, cloneRequest, recipeCallback);

        // Assert
        verify(repoRecipeMock).getById(eq(cloneFromRecipe.getId()),
                repoRecipeCallback.capture());
        repoRecipeCallback.getValue().onEntityLoaded(cloneFromRecipe);
        // Assert recipe entity cloned to new ID
        verify(repoRecipeMock).save(cloneToRecipe);

        verify(repoIdentityMock).getById(eq(cloneFromRecipe.getId()),
                repoIdentityCallback.capture());
        repoIdentityCallback.getValue().onEntityLoaded(cloneFromIdentity);
        // Assert identity cloned to new ID
        verify(repoIdentityMock).save(cloneToIdentity);

        ArgumentCaptor<RecipeCourseEntity> courseEntityCaptor = ArgumentCaptor.
                forClass(RecipeCourseEntity.class);
        verify(repoCourseMock).getCoursesForRecipe(eq(cloneFromRecipe.getId()),
                repoCourseCallback.capture());
        repoCourseCallback.getValue().onAllLoaded(cloneFromCourses);
        // Assert courses cloned to new ID
        verify(repoCourseMock, times(cloneFromCourses.size())).save(courseEntityCaptor.capture());

        verify(repoDurationMock).getById(eq(cloneFromRecipe.getId()),
                repoDurationCallback.capture());
        repoDurationCallback.getValue().onEntityLoaded(cloneFromDuration);
        // Assert duration cloned to new ID
        verify(repoDurationMock).save(cloneToDuration);

        verify(repoPortionsMock).getPortionsForRecipe(eq(cloneFromRecipe.getId()),
                repoPortionsCallback.capture());
        repoPortionsCallback.getValue().onEntityLoaded(cloneFromPortions);
        // Assert portions cloned to new ID
        verify(repoPortionsMock).save(eq(cloneToPortions));
    }

    // region helper methods -----------------------------------------------------------------------
    private void verifyRepoRecipeCalledAndReturnDataUnavailable(String recipeId) {
        verify(repoRecipeMock).getById(eq(recipeId), repoRecipeCallback.capture());
        repoRecipeCallback.getValue().onDataNotAvailable();
    }

    private void verifyRepoIdentityCalledAndReturnDataUnavailable(String recipeId) {
        verify(repoIdentityMock).getById(eq(recipeId), repoIdentityCallback.capture());
        repoIdentityCallback.getValue().onDataNotAvailable();
    }

    private void verifyRepoCoursesCalledAndReturnDataUnavailable(String recipeId) {
        verify(repoCourseMock).getCoursesForRecipe(eq(recipeId), repoCourseCallback.capture());
        repoCourseCallback.getValue().onDataNotAvailable();
    }

    private void verifyRepoDurationCalledAndReturnDataUnavailable(String recipeId) {
        verify(repoDurationMock).getById(eq(recipeId), repoDurationCallback.capture());
        repoDurationCallback.getValue().onDataNotAvailable();
    }

    private void verifyRepoPortionsCalledAndReturnDataUnavailable(String recipeId) {
        verify(repoPortionsMock).getPortionsForRecipe(eq(recipeId), repoPortionsCallback.capture());
        repoPortionsCallback.getValue().onDataNotAvailable();
    }

    private void verifyAllReposCalledAndReturnDataUnavailable(String recipeId) {
        verifyRepoRecipeCalledAndReturnDataUnavailable(recipeId);
        verifyRepoIdentityCalledAndReturnDataUnavailable(recipeId);
        verifyRepoCoursesCalledAndReturnDataUnavailable(recipeId);
        verifyRepoDurationCalledAndReturnDataUnavailable(recipeId);
        verifyRepoPortionsCalledAndReturnDataUnavailable(recipeId);
    }

    private void verifyAllReposCalledAndReturnValidExisting(String recipeId) {
        verify(repoRecipeMock).getById(eq(recipeId), repoRecipeCallback.capture());
        repoRecipeCallback.getValue().onEntityLoaded(RECIPE_VALID_EXISTING);

        verify(repoIdentityMock).getById(eq(recipeId), repoIdentityCallback.capture());
        repoIdentityCallback.getValue().onEntityLoaded(IDENTITY_VALID_EXISTING_COMPLETE);

        verify(repoCourseMock).getCoursesForRecipe(eq(recipeId), repoCourseCallback.capture());
        repoCourseCallback.getValue().onAllLoaded(TestDataRecipeCourseEntity.getAllByRecipeId(recipeId));

        verify(repoDurationMock).getById(eq(recipeId), repoDurationCallback.capture());
        repoDurationCallback.getValue().onEntityLoaded(DURATION_VALID_EXISTING_COMPLETE);

        verify(repoPortionsMock).getPortionsForRecipe(eq(recipeId), repoPortionsCallback.capture());
        repoPortionsCallback.getValue().onEntityLoaded(PORTIONS_VALID_EXISTING_NINE);
    }

    private void whenTimeProviderReturnTime(long time) {
        when(timeProviderMock.getCurrentTimeInMills()).thenReturn(time);
    }
    // endregion helper methods --------------------------------------------------------------------

    // region helper classes -----------------------------------------------------------------------
    private static class StateListenerClient implements RecipeMacro.RecipeStateListener {

        private static final String TAG = "tkm-" + StateListenerClient.class.getSimpleName() + ": ";

        private RecipeStateResponse stateResponse;

        @Override
        public void recipeStateChanged(RecipeStateResponse stateResponse) {
            this.stateResponse = stateResponse;
        }

        RecipeStateResponse getStateResponse() {
            return stateResponse;
        }

        @Nonnull
        @Override
        public String toString() {
            return "StateListenerClient{" +
                    "stateResponse=" + stateResponse +
                    '}';
        }
    }

    private class MacroCallbackClient implements UseCase.Callback<RecipeMacroResponse> {

        private final String TAG = "tkm-" + MacroCallbackClient.class.
                getSimpleName() + ": ";

        private RecipeMacroResponse recipeMacroResponseOnSuccess;
        private RecipeMacroResponse recipeMacroResponseOnError;

        private RecipeResponse recipeResponse;

        private RecipeIdentityResponse identityOnSuccess;
        private RecipeIdentityResponse identityOnError;

        private RecipeCourseResponse courseOnSuccess;
        private RecipeCourseResponse courseOnError;

        private RecipeDurationResponse durationOnSuccess;
        private RecipeDurationResponse durationOnError;

        private RecipePortionsResponse portionsOnSuccess;
        private RecipePortionsResponse portionsOnError;

        @Override
        public void onSuccess(RecipeMacroResponse response) {
            if (response != null) {
                System.out.println(TAG + "recipeMacroResponseOnSuccess: " + response);
                recipeMacroResponseOnSuccess = response;
                recipeResponse = (RecipeResponse) response.getComponentResponses().
                        get(ComponentName.RECIPE);
                identityOnSuccess = (RecipeIdentityResponse) response.getComponentResponses().
                        get(IDENTITY);
                courseOnSuccess = (RecipeCourseResponse) response.getComponentResponses().
                        get(COURSE);
                durationOnSuccess = (RecipeDurationResponse) response.getComponentResponses().
                        get(DURATION);
                portionsOnSuccess = (RecipePortionsResponse) response.getComponentResponses().
                        get(ComponentName.PORTIONS);
            }
        }

        @Override
        public void onError(RecipeMacroResponse response) {
            if (response != null) {
                System.out.println(TAG + "recipeMacroResponseOnError: " + response);
                recipeMacroResponseOnError = response;

                recipeResponse = (RecipeResponse) response.getComponentResponses().
                        get(ComponentName.RECIPE);

                RecipeIdentityResponse identityResponse = (RecipeIdentityResponse)
                        response.getComponentResponses().get(IDENTITY);
                if (identityResponse.getFailReasons().contains(CommonFailReason.NONE)) {
                    identityOnSuccess = identityResponse;
                } else {
                    identityOnError = identityResponse;
                }

                RecipeCourseResponse courseResponse = (RecipeCourseResponse)
                        response.getComponentResponses().get(COURSE);
                if (courseResponse.getFailReasons().contains(CommonFailReason.NONE)) {
                    courseOnSuccess = courseResponse;
                } else {
                    courseOnError = courseResponse;
                }

                RecipeDurationResponse durationResponse = (RecipeDurationResponse)
                        response.getComponentResponses().get(DURATION);
                if (durationResponse.getFailReasons().contains(CommonFailReason.NONE)) {
                    durationOnSuccess = durationResponse;
                } else {
                    durationOnError = durationResponse;
                }

                RecipePortionsResponse portionsResponse = (RecipePortionsResponse)
                        response.getComponentResponses().get(ComponentName.PORTIONS);
                if (portionsResponse.getFailReasons().contains(CommonFailReason.NONE)) {
                    portionsOnSuccess = portionsResponse;
                } else {
                    portionsOnError = portionsResponse;
                }
            }
        }

        RecipeMacroResponse getRecipeMacroResponseOnSuccess() {
            return recipeMacroResponseOnSuccess;
        }

        RecipeMacroResponse getRecipeMacroResponseOnError() {
            return recipeMacroResponseOnError;
        }

        public RecipeResponse getRecipeResponse() {
            return recipeResponse;
        }

        public RecipeIdentityResponse getIdentityOnSuccess() {
            return identityOnSuccess;
        }

        RecipeIdentityResponse getIdentityOnError() {
            return identityOnError;
        }

        public RecipeCourseResponse getCourseOnSuccess() {
            return courseOnSuccess;
        }

        RecipeCourseResponse getCourseOnError() {
            return courseOnError;
        }

        public RecipeDurationResponse getDurationOnSuccess() {
            return durationOnSuccess;
        }

        RecipeDurationResponse getDurationOnError() {
            return durationOnError;
        }

        public RecipePortionsResponse getPortionsOnSuccess() {
            return portionsOnSuccess;
        }

        RecipePortionsResponse getPortionsOnError() {
            return portionsOnError;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            MacroCallbackClient that = (MacroCallbackClient) o;
            return Objects.equals(recipeMacroResponseOnSuccess, that.recipeMacroResponseOnSuccess) &&
                    Objects.equals(recipeMacroResponseOnError, that.recipeMacroResponseOnError) &&
                    Objects.equals(recipeResponse, that.recipeResponse) &&
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
            return Objects.hash(recipeMacroResponseOnSuccess, recipeMacroResponseOnError,
                    recipeResponse, identityOnSuccess, identityOnError, courseOnSuccess,
                    courseOnError, durationOnSuccess, durationOnError, portionsOnSuccess,
                    portionsOnError);
        }

        @Nonnull
        @Override
        public String toString() {
            return "MacroCallbackClient{" +
                    "TAG='" + TAG + '\'' +
                    ", recipeMacroResponseOnSuccess=" + recipeMacroResponseOnSuccess +
                    ", recipeMacroResponseOnError=" + recipeMacroResponseOnError +
                    ", recipeResponse=" + recipeResponse +
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

    private class RecipeCallbackClient implements UseCase.Callback<RecipeResponse> {

        private RecipeResponse response;

        @Override
        public void onSuccess(RecipeResponse response) {
            this.response = response;
        }

        @Override
        public void onError(RecipeResponse response) {
            this.response = response;
        }

        public RecipeResponse getResponse() {
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

    private class IdentityCallbackClient implements UseCase.Callback<RecipeIdentityResponse> {

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

    private class DurationCallbackClient implements UseCase.Callback<RecipeDurationResponse> {

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

    private class CourseCallbackClient implements UseCase.Callback<RecipeCourseResponse> {

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

    private class PortionCallbackClient implements UseCase.Callback<RecipePortionsResponse> {

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