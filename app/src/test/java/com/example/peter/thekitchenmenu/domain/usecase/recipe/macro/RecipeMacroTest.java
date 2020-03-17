package com.example.peter.thekitchenmenu.domain.usecase.recipe.macro;

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
import com.example.peter.thekitchenmenu.domain.usecase.recipe.macro.recipe.Recipe;
import com.example.peter.thekitchenmenu.domain.usecase.recipe.macro.recipe.RecipeRequest;
import com.example.peter.thekitchenmenu.domain.usecase.recipe.macro.recipe.RecipeResponse;
import com.example.peter.thekitchenmenu.domain.usecase.recipe.metadata.RecipeMetadata;
import com.example.peter.thekitchenmenu.domain.usecase.recipe.metadata.RecipeMetadataResponse;
import com.example.peter.thekitchenmenu.domain.usecase.recipe.metadata.RecipeMetadataRequest;
import com.example.peter.thekitchenmenu.domain.usecase.recipe.component.recipeduration.RecipeDurationRequest;
import com.example.peter.thekitchenmenu.domain.usecase.recipe.state.RecipeStateCalculator;
import com.example.peter.thekitchenmenu.domain.usecase.recipe.state.RecipeStateResponse;
import com.example.peter.thekitchenmenu.domain.usecase.recipe.component.recipeidentity.RecipeIdentity;
import com.example.peter.thekitchenmenu.domain.usecase.recipe.component.recipeidentity.RecipeIdentityRequest;
import com.example.peter.thekitchenmenu.domain.usecase.recipe.component.recipeidentity.RecipeIdentityResponse;
import com.example.peter.thekitchenmenu.domain.usecase.recipe.component.recipecourse.RecipeCourse;
import com.example.peter.thekitchenmenu.domain.usecase.recipe.component.recipecourse.RecipeCourseRequest;
import com.example.peter.thekitchenmenu.domain.usecase.recipe.component.recipecourse.RecipeCourseResponse;
import com.example.peter.thekitchenmenu.domain.usecase.recipe.component.recipeduration.RecipeDuration;
import com.example.peter.thekitchenmenu.domain.usecase.recipe.component.recipeduration.RecipeDurationResponse;
import com.example.peter.thekitchenmenu.domain.usecase.recipe.recipeduration.RecipeDurationTest;
import com.example.peter.thekitchenmenu.domain.usecase.recipe.recipeidentity.RecipeIdentityTest;
import com.example.peter.thekitchenmenu.domain.usecase.recipe.component.recipeportions.RecipePortions;
import com.example.peter.thekitchenmenu.domain.usecase.recipe.component.recipeportions.RecipePortionsRequest;
import com.example.peter.thekitchenmenu.domain.usecase.recipe.component.recipeportions.RecipePortionsResponse;
import com.example.peter.thekitchenmenu.domain.usecase.recipe.recipeportions.RecipePortionsTest;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

import javax.annotation.Nonnull;

import static com.example.peter.thekitchenmenu.domain.usecase.recipe.state.RecipeStateCalculator.*;
import static com.example.peter.thekitchenmenu.domain.usecase.recipe.state.RecipeStateCalculator.ComponentName.COURSE;
import static com.example.peter.thekitchenmenu.domain.usecase.recipe.state.RecipeStateCalculator.ComponentName.DURATION;
import static com.example.peter.thekitchenmenu.domain.usecase.recipe.state.RecipeStateCalculator.ComponentName.IDENTITY;
import static com.example.peter.thekitchenmenu.domain.usecase.recipe.state.RecipeStateCalculator.ComponentName.PORTIONS;
import static com.example.peter.thekitchenmenu.domain.usecase.recipe.state.RecipeStateCalculator.ComponentName.RECIPE_METADATA;
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

    @Mock
    private StateListenerClient recipeStateListener1;
    @Mock
    private StateListenerClient recipeStateListener2;

    private UseCaseHandler handler;
    private Recipe SUT;

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

        RecipeMetadata recipeMetaData = new RecipeMetadata(
                timeProviderMock,
                repoRecipeMock
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

        return new Recipe(
                handler,
                stateCalculator,
                recipeMetaData,
                identity,
                course,
                duration,
                portions);
    }

    // todo - test for delete and favorite as separate macro commands?

    @Test
    public void newId_invokerIssuesCommandToAllReceivers() {
        // Arrange
        String recipeId = INVALID_NEW_RECIPE.getId();

        MacroCallbackClient callback = new MacroCallbackClient();
        RecipeRequest request = new RecipeRequest(recipeId);
        // Act
        handler.execute(SUT, request, callback);

        // Assert database called for every component, meaning command has been issued.
        verifyAllReposCalledAndReturnDataUnavailable(recipeId);
    }

    @Test
    public void macroRequestNewId_invokerIssuesCommand_stateListenersUpdatedWithCorrectValues() {
        // Arrange
        String recipeId = INVALID_NEW_RECIPE.getId();
        MacroCallbackClient callback = new MacroCallbackClient();
        RecipeRequest request = new RecipeRequest(recipeId);

        // Act
        SUT.registerStateListener(recipeStateListener1);
        SUT.registerStateListener(recipeStateListener2);
        handler.execute(SUT, request, callback);

        // Assert database calls
        verifyAllReposCalledAndReturnDataUnavailable(recipeId);

        // Assert recipe state listener updated with correct value
        verify(recipeStateListener1).recipeStateChanged(recipeStateCaptor.capture());
        RecipeStateResponse recipeStateResponse = recipeStateCaptor.getValue();
        assertEquals(RecipeState.INVALID_UNCHANGED, recipeStateResponse.getState());

        // Assert recipe state updated
        assertEquals(RecipeState.INVALID_UNCHANGED, recipeStateResponse.getState());
        assertEquals(4, recipeStateResponse.getComponentStates().size());

        // Assert identity component status
        assertTrue(recipeStateResponse.getComponentStates().containsKey(IDENTITY));
        assertEquals(ComponentState.INVALID_UNCHANGED, recipeStateResponse.getComponentStates().
                get(IDENTITY));

        // Assert course component state
        assertTrue(recipeStateResponse.getComponentStates().containsKey(COURSE));
        assertEquals(ComponentState.INVALID_UNCHANGED, recipeStateResponse.getComponentStates().
                get(COURSE));

        // Assert duration component state
        assertTrue(recipeStateResponse.getComponentStates().containsKey(DURATION));
        assertEquals(ComponentState.INVALID_UNCHANGED, recipeStateResponse.getComponentStates().
                get(DURATION));

        // Assert portions component state
        assertTrue(recipeStateResponse.getComponentStates().containsKey(ComponentName.PORTIONS));
        assertEquals(ComponentState.INVALID_UNCHANGED, recipeStateResponse.getComponentStates().get(
                ComponentName.PORTIONS));
    }

    @Test
    public void recipeRequestNewId_invokerIssuesCommand_useCaseCallbackINVALID_UNCHANGED_MISSING_COMPONENTS() {
        // Arrange
        String recipeId = INVALID_NEW_RECIPE.getId();
        RecipeMetadataRequest request = new RecipeMetadataRequest.Builder().
                getDefault().
                setId(recipeId).
                build();

        RecipeMetadataCallback passedInCallback = new RecipeMetadataCallback();
        MacroCallbackClient registeredMacroResponseCallback = new MacroCallbackClient();
        // Act
        SUT.registerMacroCallback(registeredMacroResponseCallback);
        handler.execute(SUT, request, passedInCallback);

        // Assert database calls
        verifyAllReposCalledAndReturnDataUnavailable(recipeId);

        // Assert recipeMetadataCallback
        System.out.println(TAG + "passedInCallback: " + passedInCallback);
//        assertEquals();

        // Assert, callback updated with recipe macro response
        assertEquals(RecipeState.INVALID_UNCHANGED, registeredMacroResponseCallback.
                getRecipeResponseOnError().
                        getRecipeStateResponse().
                        getState());

        assertTrue(registeredMacroResponseCallback.
                getRecipeResponseOnError().
                getRecipeStateResponse().
                getFailReasons().
                contains(FailReason.INVALID_COMPONENTS));
    }

    @Test
    public void macroRequestNewId_invokerIssuesCommand_recipeResponseListenersUpdated() {
        // Arrange
        String recipeId = INVALID_NEW_RECIPE.getId();
        RecipeRequest request = new RecipeRequest(recipeId);
        MacroCallbackClient callback = new MacroCallbackClient();
        RecipeMetadataCallback registeredCallback = new RecipeMetadataCallback();
        // Act
        SUT.registerComponentCallback(new Pair<>(RECIPE_METADATA, registeredCallback));
        handler.execute(SUT, request, callback);
        // Assert
        verifyAllReposCalledAndReturnDataUnavailable(recipeId);
        // Assert recipe response registered callback updated

//        assertEquals(callback, registeredCallback);
    }

    @Test
    public void identityRequestNewId_initialRequest_invokerIssuesCommandToAllReceivers() {
        // Arrange
        String recipeId = INVALID_NEW_RECIPE.getId();
        IdentityCallbackClient callback = new IdentityCallbackClient();

        RecipeIdentityRequest identityRequest = new RecipeIdentityRequest.Builder().
                getDefault().
                setId(recipeId).
                build();
        // Act
        handler.execute(SUT, identityRequest, callback);

        // Assert all components receive recipe ID and call their repos
        verifyAllReposCalledAndReturnDataUnavailable(recipeId);

        // Assert identity response received
        RecipeIdentityResponse response = callback.getResponse();
        assertTrue(response.getMetadata().getFailReasons().contains(CommonFailReason.DATA_UNAVAILABLE));
    }

    @Test
    public void identityRequestNewId_invokerIssuesCommand_correctMacroComponentResponses() {
        // Arrange
        String recipeId = INVALID_NEW_RECIPE.getId();
        RecipeIdentityRequest request = new RecipeIdentityRequest.Builder().
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
        assertEquals(ComponentState.INVALID_UNCHANGED, identityResponse.getMetadata().getState());

        assertEquals(1, identityResponse.getMetadata().
                getFailReasons().size());

        assertTrue(identityResponse.getMetadata().
                getFailReasons().
                contains(CommonFailReason.DATA_UNAVAILABLE));

        // Assert response from courses component
        RecipeCourseResponse courseResponse = macroCallback.getCourseOnError();
        assertEquals(ComponentState.INVALID_UNCHANGED, courseResponse.getMetadata().getState());

        assertEquals(1, courseResponse.getMetadata().
                getFailReasons().
                size());

        assertTrue(courseResponse.getMetadata().
                getFailReasons().
                contains(CommonFailReason.DATA_UNAVAILABLE));

        // Assert response from duration component
        RecipeDurationResponse durationResponse = macroCallback.getDurationOnError();
        assertEquals(ComponentState.INVALID_UNCHANGED, durationResponse.getMetadata().getState());

        assertEquals(1, durationResponse.getMetadata().
                getFailReasons().
                size());

        assertTrue(durationResponse.getMetadata().
                getFailReasons().
                contains(CommonFailReason.DATA_UNAVAILABLE));

        // Assert response from portions component
        RecipePortionsResponse portionResponse = macroCallback.getPortionsOnError();
        assertEquals(ComponentState.INVALID_UNCHANGED, portionResponse.getMetadata().getState());

        assertEquals(1, portionResponse.getMetadata().
                getFailReasons().
                size());

        assertTrue(portionResponse.getMetadata().
                getFailReasons().
                contains(CommonFailReason.DATA_UNAVAILABLE));
    }

    @Test
    public void coursesRequestNewId_initialRequest_invokerIssuesCommandToAllReceivers() {
        // Arrange
        String recipeId = INVALID_NEW_RECIPE.getId();
        CourseCallbackClient callback = new CourseCallbackClient();

        RecipeCourseRequest courseRequest = new RecipeCourseRequest.Builder().getDefault().
                setId(recipeId).
                build();
        // Act
        handler.execute(SUT, courseRequest, callback);

        // Assert all components receive recipe ID and call their repos
        verifyAllReposCalledAndReturnDataUnavailable(recipeId);

        // Assert course response received
        RecipeCourseResponse response = callback.getResponse();
        assertTrue(response.getMetadata().getFailReasons().contains(CommonFailReason.DATA_UNAVAILABLE));
    }

    @Test
    public void durationRequestNewId_initialRequest_invokerIssuesCommandToAllReceivers() {
        // Arrange
        String recipeId = INVALID_NEW_RECIPE.getId();
        DurationCallbackClient callback = new DurationCallbackClient();

        RecipeDurationRequest durationRequest = new RecipeDurationRequest.Builder().
                getDefault().
                setId(recipeId).
                build();

        // Act
        handler.execute(SUT, durationRequest, callback);

        // Assert all components receive recipe ID and call their repos
        verifyAllReposCalledAndReturnDataUnavailable(recipeId);

        // Assert response received
        RecipeDurationResponse response = callback.getResponse();
        assertTrue(response.getMetadata().
                getFailReasons().
                contains(CommonFailReason.DATA_UNAVAILABLE));
    }

    @Test
    public void portionsRequestNewId_initialRequest_invokerIssuesCommandToAllReceivers() {
        // Arrange
        String recipeId = INVALID_NEW_RECIPE.getId();
        PortionCallbackClient callback = new PortionCallbackClient();

        RecipePortionsRequest portionsRequest = new RecipePortionsRequest.Builder().
                getDefault().
                setId(recipeId).
                build();
        // Act
        handler.execute(SUT, portionsRequest, callback);

        // Assert all components receive recipe ID and call their repos
        verifyAllReposCalledAndReturnDataUnavailable(recipeId);

        // Assert response received
        RecipePortionsResponse response = callback.getResponse();
        assertTrue(response.getMetadata().
                getFailReasons().
                contains(CommonFailReason.DATA_UNAVAILABLE));
    }

    @Test
    public void identityRequestNewId_titleValidDescriptionValid_identityStateVALID_CHANGED() {
        // Arrange
        String recipeId = IDENTITY_INVALID_NEW_EMPTY.getId();
        IdentityCallbackClient callback = new IdentityCallbackClient();

        ArgumentCaptor<RecipeIdentityEntity> identityEntity = ArgumentCaptor.
                forClass(RecipeIdentityEntity.class);
        whenTimeProviderReturnTime(IDENTITY_VALID_NEW_COMPLETE.getCreateDate());

        RecipeIdentityRequest firstRequest = new RecipeIdentityRequest.Builder().
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
        assertEquals(ComponentState.VALID_CHANGED, response.getMetadata().getState());
        assertEquals(1, response.getMetadata().getFailReasons().size());
        assertTrue(response.
                getMetadata().
                getFailReasons().
                contains(CommonFailReason.NONE));
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

        RecipeCourseRequest.Model initialModel = new RecipeCourseRequest.Model.Builder().
                getDefault().
                build();
        RecipeCourseRequest initialRequest = new RecipeCourseRequest.Builder().getDefault().
                setId(recipeId).
                setModel(initialModel).
                build();

        // Act
        SUT.registerStateListener(recipeStateListener1);
        handler.execute(SUT, initialRequest, callback);

        // Assert
        verifyAllReposCalledAndReturnDataUnavailable(recipeId);

        List<RecipeCourse.Course> courses = new ArrayList<>();
        courses.add(RecipeCourse.Course.COURSE_ZERO);
        // Arrange
        RecipeCourseRequest addCourseRequest = new RecipeCourseRequest.Builder().
                setId(recipeId).
                setModel(new RecipeCourseRequest.Model.Builder().
                        setCourseList(courses).build()).
                build();

        // Act
        handler.execute(SUT, addCourseRequest, callback);

        // Assert correct values saved
        verify(repoCourseMock).save(actualCourseEntity.capture());
        assertEquals(expectedCourseEntity, actualCourseEntity.getValue());

        // Assert courses response
        RecipeCourseResponse courseResponse = callback.getResponse();
        assertEquals(ComponentState.VALID_CHANGED, courseResponse.getMetadata().getState());
        assertTrue(courseResponse.
                getModel().
                getCourseList().
                containsKey(RecipeCourse.Course.COURSE_ZERO));

        // Assert listener updated
        verify(recipeStateListener1, times((2))).recipeStateChanged(recipeStateCaptor.capture());
        assertEquals(ComponentState.VALID_CHANGED, recipeStateCaptor.getValue().
                getComponentStates().get(COURSE));
    }

    @Test
    public void recipeRequestExistingId_validData_onlyRegisteredListenersNotified() {
        // Arrange
        String recipeId = RECIPE_VALID_EXISTING.getId();
        RecipeMetadataCallback callback = new RecipeMetadataCallback();

        RecipeMetadataRequest request = new RecipeMetadataRequest.Builder().
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
        RecipeMetadataCallback callback = new RecipeMetadataCallback();
        RecipeMetadataRequest request = new RecipeMetadataRequest.Builder().
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
        RecipeMetadataCallback callback = new RecipeMetadataCallback();

        RecipeMetadataRequest request = new RecipeMetadataRequest.Builder().
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

        RecipeMetadataRequest request = new RecipeMetadataRequest.Builder().
                getDefault().
                setId(recipeId).
                build();
        RecipeMetadataCallback callback = new RecipeMetadataCallback();

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
                getRecipeResponseOnSuccess().
                getRecipeStateResponse().
                getState();
        assertEquals(RecipeState.VALID_UNCHANGED, recipeStateMacroState);

        // Assert recipe component callback
        ComponentState identityComponentState = identityCallbackClient.
                getResponse().
                getMetadata().
                getState();
        assertEquals(ComponentState.VALID_UNCHANGED, identityComponentState);

        System.out.println(TAG + recipeStateListener1.getStateResponse());
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
    private static class StateListenerClient implements Recipe.RecipeStateListener {

        private static final String TAG = "tkm-" + StateListenerClient.class.getSimpleName() + ": ";

        private RecipeStateResponse stateResponse;

        @Override
        public void recipeStateChanged(RecipeStateResponse stateResponse) {
            System.out.println(TAG + stateResponse);
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

    private static class MacroCallbackClient implements UseCase.Callback<RecipeResponse> {

        private final String TAG = "tkm-" + MacroCallbackClient.class.
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
                recipeMetadataResponse = (RecipeMetadataResponse) response.getComponentResponses().
                        get(ComponentName.RECIPE_METADATA);
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
        public void onError(RecipeResponse response) {
            if (response != null) {
                System.out.println(TAG + "recipeMacroResponseOnError: " + response);
                recipeResponseOnError = response;

                recipeMetadataResponse = (RecipeMetadataResponse) response.getComponentResponses().
                        get(ComponentName.RECIPE_METADATA);

                RecipeIdentityResponse identityResponse = (RecipeIdentityResponse)
                        response.getComponentResponses().get(IDENTITY);
                if (identityResponse.
                        getMetadata().
                        getFailReasons().
                        contains(CommonFailReason.NONE)) {
                    identityOnSuccess = identityResponse;
                } else {
                    identityOnError = identityResponse;
                }

                RecipeCourseResponse courseResponse = (RecipeCourseResponse)
                        response.getComponentResponses().get(COURSE);
                if (courseResponse.getMetadata().getFailReasons().contains(CommonFailReason.NONE)) {
                    courseOnSuccess = courseResponse;
                } else {
                    courseOnError = courseResponse;
                }

                RecipeDurationResponse durationResponse = (RecipeDurationResponse)
                        response.getComponentResponses().get(DURATION);
                if (durationResponse.getMetadata().
                        getFailReasons().
                        contains(CommonFailReason.NONE)) {
                    durationOnSuccess = durationResponse;
                } else {
                    durationOnError = durationResponse;
                }

                RecipePortionsResponse portionsResponse = (RecipePortionsResponse)
                        response.getComponentResponses().get(ComponentName.PORTIONS);
                if (portionsResponse.getMetadata().
                        getFailReasons().
                        contains(CommonFailReason.NONE)) {
                    portionsOnSuccess = portionsResponse;
                } else {
                    portionsOnError = portionsResponse;
                }
            }
        }

        RecipeResponse getRecipeResponseOnSuccess() {
            return recipeResponseOnSuccess;
        }

        RecipeResponse getRecipeResponseOnError() {
            return recipeResponseOnError;
        }

        RecipeIdentityResponse getIdentityOnError() {
            return identityOnError;
        }

        RecipeCourseResponse getCourseOnError() {
            return courseOnError;
        }

        RecipeDurationResponse getDurationOnError() {
            return durationOnError;
        }


        RecipePortionsResponse getPortionsOnError() {
            return portionsOnError;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            MacroCallbackClient that = (MacroCallbackClient) o;
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