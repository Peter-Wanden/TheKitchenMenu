package com.example.peter.thekitchenmenu.domain.usecase.recipemediator;

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
import com.example.peter.thekitchenmenu.domain.usecase.UseCase;
import com.example.peter.thekitchenmenu.domain.usecase.UseCaseCommand;
import com.example.peter.thekitchenmenu.domain.usecase.UseCaseHandler;
import com.example.peter.thekitchenmenu.domain.usecase.recipe.Recipe;
import com.example.peter.thekitchenmenu.domain.usecase.recipe.RecipeRequest;
import com.example.peter.thekitchenmenu.domain.usecase.recipe.RecipeResponse;
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

import javax.annotation.Nonnull;

import static com.example.peter.thekitchenmenu.domain.usecase.recipe.Recipe.DO_NOT_CLONE;
import static com.example.peter.thekitchenmenu.domain.usecase.recipe.recipestate.RecipeStateCalculator.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

public class RecipeTest {

    private static final String TAG = "tkm-" + RecipeTest.class.getSimpleName() + ": ";

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
    UniqueIdProvider idProvider;
    @Captor
    ArgumentCaptor<RecipeStateResponse> recipeStateCaptor;

    private UseCaseHandler handler;
    @Mock
    private RecipeStateListener recipeStateListener1;
    @Mock
    private RecipeStateListener recipeStateListener2;

    private Recipe SUT;
    private RecipeResponse recipeResponseOnSuccess;
    private RecipeResponse recipeResponseOnError;

    private RecipeIdentity identity;
    private RecipeIdentityResponse identityOnSuccess;
    private RecipeIdentityResponse identityOnError;

    private RecipeCourse course;
    private RecipeCourseResponse courseOnSuccess;
    private RecipeCourseResponse courseOnError;

    private RecipeDuration duration;
    private RecipeDurationResponse durationOnSuccess;
    private RecipeDurationResponse durationOnError;

    private RecipePortions portions;
    private RecipePortionsResponse portionsOnSuccess;
    private RecipePortionsResponse portionsOnError;

    // endregion helper fields ---------------------------------------------------------------------

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        SUT = givenUseCase();
    }

    private Recipe givenUseCase() {
        TextValidator textValidator = new TextValidator.Builder().
                setShortTextMinLength(RecipeIdentityTest.TITLE_MIN_LENGTH).
                setShortTextMaxLength(RecipeIdentityTest.TITLE_MAX_LENGTH).
                setLongTextMinLength(RecipeIdentityTest.DESCRIPTION_MIN_LENGTH).
                setLongTextMaxLength(RecipeIdentityTest.DESCRIPTION_MAX_LENGTH).
                build();

        UseCaseHandler handler = new UseCaseHandler(new UseCaseSchedulerMock());
        this.handler = handler;

        identity = new RecipeIdentity(
                repoIdentityMock,
                timeProviderMock,
                handler,
                textValidator
        );

        course = new RecipeCourse(
                repoCourseMock,
                idProvider,
                timeProviderMock
        );

        duration = new RecipeDuration(
                repoDurationMock,
                timeProviderMock,
                RecipeDurationTest.MAX_PREP_TIME,
                RecipeDurationTest.MAX_COOK_TIME
        );

        portions = new RecipePortions(
                repoPortionsMock,
                idProvider,
                timeProviderMock,
                RecipePortionsTest.MAX_SERVINGS,
                RecipePortionsTest.MAX_SITTINGS
        );

        RecipeStateCalculator stateCalculator = new RecipeStateCalculator();

        return new Recipe<>(
                repoRecipeMock,
                handler,
                stateCalculator,
                identity,
                course,
                duration,
                portions);
    }

    // todo - test for clone, delete and favorite as separate macro commands

    @Test
    public void recipeRequestNewId_invokerIssuesCommandToAllReceivers() {
        // Arrange
        String recipeId = INVALID_NEW_RECIPE.getId();
        RecipeRequest request = new RecipeRequest(recipeId);
        ClassImplementsRecipeResponseCallback callback = new ClassImplementsRecipeResponseCallback();
        // Act
        handler.execute(SUT, request, callback);
        // Assert database called for every component, meaning command has been issued.
        verifyAllReposCalledAndReturnDataUnavailable(recipeId);
    }

    @Test
    public void recipeRequestNewId_invokerIssuesCommand_listenersUpdatedWithCorrectRecipeStateValues() {
        // Arrange
        String recipeId = INVALID_NEW_RECIPE.getId();
        RecipeRequest request = new RecipeRequest(recipeId);
        ClassImplementsRecipeResponseCallback callback = new ClassImplementsRecipeResponseCallback();

        // Act
        SUT.registerRecipeStateListener(recipeStateListener1);
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
        assertTrue(recipeStateResponse.getComponentStates().containsKey(ComponentName.IDENTITY));
        assertEquals(ComponentState.DATA_UNAVAILABLE, recipeStateResponse.getComponentStates().
                get(ComponentName.IDENTITY));

        // Assert course component state
        assertTrue(recipeStateResponse.getComponentStates().containsKey(ComponentName.COURSE));
        assertEquals(ComponentState.DATA_UNAVAILABLE, recipeStateResponse.getComponentStates().
                get(ComponentName.COURSE));

        // Assert duration component state
        assertTrue(recipeStateResponse.getComponentStates().containsKey(ComponentName.DURATION));
        assertEquals(ComponentState.DATA_UNAVAILABLE, recipeStateResponse.getComponentStates().
                get(ComponentName.DURATION));

        // Assert portions component state
        assertTrue(recipeStateResponse.getComponentStates().containsKey(ComponentName.PORTIONS));
        assertEquals(ComponentState.DATA_UNAVAILABLE, recipeStateResponse.getComponentStates().get(
                ComponentName.PORTIONS));
    }

    @Test
    public void recipeRequestNewId_invokerIssuesCommand_responseINVALID_UNCHANGED_INVALID_MODELS() {
        // Arrange
        String recipeId = INVALID_NEW_RECIPE.getId();
        RecipeRequest request = new RecipeRequest(recipeId);
        ClassImplementsRecipeResponseCallback callback = new ClassImplementsRecipeResponseCallback();

        // Act
        handler.execute(SUT, request, getRecipeResponseCallback());

        // Assert database calls
        verifyAllReposCalledAndReturnDataUnavailable(recipeId);
        // Assert, request originator (callback) updated with recipe response
        assertEquals(RecipeState.DATA_UNAVAILABLE,
                recipeResponseOnError.
                getRecipeStateResponse().
                getState());
        assertTrue(recipeResponseOnError.
                getRecipeStateResponse().
                getFailReasons().
                contains(FailReason.MISSING_COMPONENTS));
    }

    @Test
    public void recipeRequestNewId_invokerIssuesCommand_correctComponentResponses() {
        // Arrange
        String recipeId = INVALID_NEW_RECIPE.getId();
        RecipeRequest request = new RecipeRequest(recipeId);
        ClassImplementsRecipeResponseCallback recipeResponseCallback =
                new ClassImplementsRecipeResponseCallback();

        // Act
        SUT.registerRecipeStateListener(recipeStateListener1);
        handler.execute(SUT, request, recipeResponseCallback);

        // Assert database calls
        verifyAllReposCalledAndReturnDataUnavailable(recipeId);

        // Assert response from identity component
        RecipeIdentityResponse identityResponse = (RecipeIdentityResponse) recipeResponseOnError.
                getComponentResponses().get(ComponentName.IDENTITY);
        assertEquals(ComponentState.DATA_UNAVAILABLE, identityResponse.getState());
        assertEquals(1, identityResponse.getFailReasons().size());
        assertTrue(identityResponse.getFailReasons().contains(
                RecipeIdentity.FailReason.DATA_UNAVAILABLE));

        // Assert response from courses component
        RecipeCourseResponse courseResponse = (RecipeCourseResponse) recipeResponseOnError.
                getComponentResponses().get(ComponentName.COURSE);
        assertEquals(ComponentState.DATA_UNAVAILABLE, courseResponse.getState());
        assertEquals(1, courseResponse.getFailReasons().size());
        assertTrue(courseResponse.getFailReasons().contains(
                RecipeCourse.FailReason.DATA_UNAVAILABLE));

        // Assert response from duration component
        RecipeDurationResponse durationResponse = (RecipeDurationResponse) recipeResponseOnError.
                getComponentResponses().get(ComponentName.DURATION);
        assertEquals(ComponentState.DATA_UNAVAILABLE, durationResponse.getState());
        assertEquals(1, courseResponse.getFailReasons().size());
        assertTrue(durationResponse.getFailReasons().contains(
                RecipeDuration.FailReason.DATA_UNAVAILABLE));

        // Assert response from
        RecipePortionsResponse portionResponse = (RecipePortionsResponse) recipeResponseOnError.
                getComponentResponses().get(ComponentName.PORTIONS);
        assertEquals(ComponentState.DATA_UNAVAILABLE, portionResponse.getState());
        assertEquals(1, portionResponse.getFailReasons().size());
        assertTrue(portionResponse.getFailReasons().contains(
                RecipePortions.FailReason.DATA_UNAVAILABLE));
    }

    @Test
    public void identityRequestNewId_initialRequest_invokerIssuesCommandToAllReceivers() {
        // Arrange
        String recipeId = INVALID_NEW_RECIPE.getId();
        RecipeIdentityRequest identityRequest = RecipeIdentityRequest.Builder.getDefault().
                setRecipeId(recipeId).
                build();

        // Act
        SUT.registerRecipeStateListener(recipeStateListener1);
        handler.execute(SUT, identityRequest, getCallback());

        // Assert
        verifyAllReposCalledAndReturnDataUnavailable(recipeId);

    }

    @Test
    public void coursesRequestNewId_initialRequest_invokerIssuesCommandToAllReceivers() {
        // Arrange
        String recipeId = INVALID_NEW_RECIPE.getId();
        RecipeCourseRequest coursesRequest = RecipeCourseRequest.Builder.getDefault().
                setRecipeId(recipeId).
                build();

        // Act
        SUT.registerRecipeStateListener(recipeStateListener1);
        handler.execute(SUT, coursesRequest, getCallback());

        // Assert
        verifyAllReposCalledAndReturnDataUnavailable(recipeId);
    }

    @Test
    public void durationRequestNewId_initialRequest_invokerIssuesCommandToAllReceivers() {
        // Arrange
        String recipeId = INVALID_NEW_RECIPE.getId();
        RecipeDurationRequest durationRequest = RecipeDurationRequest.Builder.getDefault().
                setRecipeId(recipeId).
                build();

        // Act
        SUT.registerRecipeStateListener(recipeStateListener1);
        handler.execute(SUT, durationRequest, getCallback());

        // Assert
        verifyAllReposCalledAndReturnDataUnavailable(recipeId);
    }

    @Test
    public void portionsRequestNewId_initialRequest_invokerIssuesCommandToAllReceivers() {
        // Arrange
        String recipeId = INVALID_NEW_RECIPE.getId();
        RecipePortionsRequest portionsRequest = RecipePortionsRequest.Builder.getDefault().
                setRecipeId(recipeId).
                build();

        // Act
        SUT.registerRecipeStateListener(recipeStateListener1);
        handler.execute(SUT, portionsRequest, getCallback());

        // Assert
        verifyAllReposCalledAndReturnDataUnavailable(recipeId);
    }

    @Test
    public void identityRequestNewId_titleValidDescriptionValid_identityStateVALID_CHANGED() {
        // Arrange
        String recipeId = IDENTITY_INVALID_NEW_EMPTY.getId();
        ArgumentCaptor<RecipeIdentityEntity> identityEntity = ArgumentCaptor.forClass(RecipeIdentityEntity.class);
        whenTimeProviderReturnTime(IDENTITY_VALID_NEW_COMPLETE.getCreateDate());

        // Act - Request/Response 1 - new request
        RecipeIdentityRequest firstRequest = RecipeIdentityRequest.Builder.
                getDefault().
                setRecipeId(recipeId).
                build();
        SUT.registerRecipeStateListener(recipeStateListener1);
        handler.execute(SUT, firstRequest, getCallback());
        verifyAllReposCalledAndReturnDataUnavailable(recipeId);

        // Request/Response 2 - existing request
        String validTitle = IDENTITY_VALID_NEW_COMPLETE.getTitle();
        RecipeIdentityRequest.Model validTitleModel = RecipeIdentityRequest.Model.Builder.
                basedOnIdentityResponseModel(identityOnError.getModel()).
                setTitle(validTitle).
                build();
        RecipeIdentityRequest validTitleRequest = new RecipeIdentityRequest.Builder().
                setRecipeId(recipeId).
                setCloneToRecipeId(DO_NOT_CLONE).
                setModel(validTitleModel).
                build();
        handler.execute(SUT, validTitleRequest, getCallback());

        // Request/Response 3
        String validDescription = IDENTITY_VALID_NEW_COMPLETE.getDescription();
        RecipeIdentityRequest.Model validTitleDescriptionModel = RecipeIdentityRequest.Model.Builder.
                basedOnIdentityResponseModel(identityOnSuccess.getModel()).
                setDescription(validDescription).
                build();
        RecipeIdentityRequest validDescriptionRequest = new RecipeIdentityRequest.Builder().
                setRecipeId(recipeId).
                setCloneToRecipeId(DO_NOT_CLONE).
                setModel(validTitleDescriptionModel).
                build();

        // Act
        handler.execute(SUT, validDescriptionRequest, getCallback());

        // Assert identity component save
        verify(repoIdentityMock, times((2))).save(identityEntity.capture());
        assertEquals(IDENTITY_VALID_NEW_COMPLETE, identityEntity.getValue());

        // Assert identity component response values
        assertEquals(validTitle, identityOnSuccess.getModel().getTitle());
        assertEquals(validDescription, identityOnSuccess.getModel().getDescription());

        // Assert identity component state
        assertEquals(ComponentState.VALID_CHANGED, identityOnSuccess.getState());
        assertEquals(1, identityOnSuccess.getFailReasons().size());
        assertTrue(identityOnSuccess.getFailReasons().contains(RecipeIdentity.FailReason.NONE));

        // Assert client listeners updated
        verify(recipeStateListener1, times((2))).recipeStateChanged(recipeStateCaptor.capture());

        for (RecipeStateResponse recipeResponse : recipeStateCaptor.getAllValues()) {
            System.out.println(TAG + "recipeStateResponse:" + recipeResponse);
        }

        assertEquals(RecipeState.DATA_UNAVAILABLE, recipeStateCaptor.getValue().getState());
        assertTrue(recipeStateCaptor.getValue().getFailReasons().contains(FailReason.MISSING_COMPONENTS));
    }

    @Test
    public void coursesRequestNewId_newCourseAdded_coursesStateVALID_CHANGED() {
        // Arrange
        RecipeCourseEntity expectedCourseEntity = TestDataRecipeCourseEntity.getRecipeCourseZero();
        ArgumentCaptor<RecipeCourseEntity> actualCourseEntity = ArgumentCaptor.forClass(RecipeCourseEntity.class);

        String recipeId = expectedCourseEntity.getRecipeId();

        whenTimeProviderReturnTime(expectedCourseEntity.getCreateDate());
        when(idProvider.getUId()).thenReturn(expectedCourseEntity.getId());

        RecipeCourseRequest initialRequest = RecipeCourseRequest.Builder.getDefault().
                setRecipeId(recipeId).
                build();

        // Act
        SUT.registerRecipeStateListener(recipeStateListener1);
        handler.execute(SUT, initialRequest, getCallback());

        // Assert
        verifyAllReposCalledAndReturnDataUnavailable(recipeId);

        // Arrange
        RecipeCourseRequest addCourseRequest = new RecipeCourseRequest.Builder().
                setRecipeId(recipeId).
                setCloneToRecipeId(DO_NOT_CLONE).
                setCourse(RecipeCourse.Course.COURSE_ZERO).
                setAddCourse(true).
                build();

        // Act
        handler.execute(SUT, addCourseRequest, getCallback());

        // Assert correct values saved
        verify(repoCourseMock).save(actualCourseEntity.capture());
        assertEquals(expectedCourseEntity, actualCourseEntity.getValue());

        // Assert courses response
        RecipeCourseResponse response = (RecipeCourseResponse) recipeResponseOnError.
                getComponentResponses().get(ComponentName.COURSE);
        assertEquals(ComponentState.VALID_CHANGED, response.getState());

        // Assert listener updated
        verify(recipeStateListener1, times((2))).recipeStateChanged(recipeStateCaptor.capture());
        assertEquals(ComponentState.VALID_CHANGED, recipeStateCaptor.getValue().
                getComponentStates().get(ComponentName.COURSE));
    }

    @Test
    public void recipeRequestExistingId_validData_onlyRegisteredListenersNotified() {
        // Arrange
        String recipeId = RECIPE_VALID_EXISTING.getId();
        RecipeRequest request = new RecipeRequest(recipeId);

        // Act
        SUT.registerRecipeStateListener(recipeStateListener1);
        SUT.registerRecipeStateListener(recipeStateListener2);
        SUT.unregisterRecipeStateListener(recipeStateListener2);
        handler.execute(SUT, request, getCallback());

        // Assert database called and return valid data for all components
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

        // Assert listeners called
        verify(recipeStateListener1).recipeStateChanged(any(RecipeStateResponse.class));
        verifyNoMoreInteractions(recipeStateListener2);
    }

    @Test
    public void recipeRequestExistingId_validData_componentStateVALID_UNCHANGED() {
        // Arrange
        String recipeId = RECIPE_VALID_EXISTING.getId();
        RecipeRequest request = new RecipeRequest(recipeId);
        // Act
        SUT.registerRecipeStateListener(recipeStateListener1);
        SUT.registerRecipeStateListener(recipeStateListener2);
        handler.execute(SUT, request, getCallback());

        // Assert database called and return data
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

        // Assert listeners called
        verify(recipeStateListener1).recipeStateChanged(recipeStateCaptor.capture());
        verify(recipeStateListener2).recipeStateChanged(recipeStateCaptor.capture());


        // Assert recipe component states
        HashMap<ComponentName, ComponentState> componentStates = recipeStateCaptor.getValue().
                getComponentStates();
        assertEquals(ComponentState.VALID_UNCHANGED, componentStates.get(ComponentName.IDENTITY));
        assertEquals(ComponentState.VALID_UNCHANGED, componentStates.get(ComponentName.COURSE));
    }

    @Test
    public void existingId_validData_recipeStateVALID_UNCHANGED() {
        // Arrange
        String recipeId = RECIPE_VALID_EXISTING.getId();
        RecipeRequest request = new RecipeRequest(recipeId);

        // Act
        SUT.registerRecipeStateListener(recipeStateListener1);
        SUT.registerRecipeStateListener(recipeStateListener2);
        handler.execute(SUT, request, getCallback());

        // Assert database called by components and return valid data
        verifyAllReposCalledAndReturnValidExisting(recipeId);

        // Assert listeners called
        verify(recipeStateListener1).recipeStateChanged(recipeStateCaptor.capture());
        verify(recipeStateListener2).recipeStateChanged(recipeStateCaptor.capture());


        // Assert correct recipe state
        RecipeState recipeState = recipeStateCaptor.getValue().getState();
        assertEquals(RecipeState.VALID_UNCHANGED, recipeState);
    }

    @Test
    // Recipe receives a global request, queries all components and pushes responses to
    // all registered listeners
    public void recipeRequest_validId_registeredComponentCallbacksCalled() {
        // Arrange
        String recipeId = RECIPE_VALID_EXISTING.getId();

        ClassImplementsRecipeResponseCallback classListeningForRecipeResponses =
                new ClassImplementsRecipeResponseCallback();
        ClassImplementsRecipeResponseCallback classThatUsesRecipeIdentity =
                new ClassImplementsRecipeResponseCallback();

        RecipeRequest request = new RecipeRequest(recipeId);

        // Act
        SUT.registerRecipeStateListener(recipeStateListener1);
        SUT.registerRecipeResponseCallback(classListeningForRecipeResponses);
        SUT.registerRecipeResponseCallback(classThatUsesRecipeIdentity);

        handler.execute(SUT, request, getCallback());
        // Assert
        verifyAllReposCalledAndReturnValidExisting(recipeId);
        // Assert recipe state listeners updated
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

    private UseCase.Callback<RecipeResponse> getCallback() {
        return new UseCase.Callback<RecipeResponse>() {

            @Override
            public void onSuccess(RecipeResponse response) {
                if (response != null) {
                    System.out.println(TAG + "recipeResponseOnSuccess: " + response);
                    recipeResponseOnSuccess = response;
                    identityOnSuccess = (RecipeIdentityResponse) response.getComponentResponses().
                            get(ComponentName.IDENTITY);
                    courseOnSuccess = (RecipeCourseResponse) response.getComponentResponses().
                            get(ComponentName.COURSE);
                    durationOnSuccess = (RecipeDurationResponse) response.getComponentResponses().
                            get(ComponentName.DURATION);
                    portionsOnSuccess = (RecipePortionsResponse) response.getComponentResponses().
                            get(ComponentName.PORTIONS);
                }
            }

            @Override
            public void onError(RecipeResponse response) {
                if (response != null) {
                    System.out.println(TAG + "recipeResponseOnError: " + response);
                    recipeResponseOnError = response;

                    RecipeIdentityResponse identityResponse = (RecipeIdentityResponse)
                            response.getComponentResponses().get(ComponentName.IDENTITY);
                    if (identityResponse.getFailReasons().contains(RecipeIdentity.FailReason.NONE)) {
                        identityOnSuccess = identityResponse;
                    } else {
                        identityOnError = identityResponse;
                    }

                    RecipeCourseResponse courseResponse = (RecipeCourseResponse)
                            response.getComponentResponses().get(ComponentName.COURSE);
                    if (courseResponse.getFailReasons().contains(RecipeCourse.FailReason.NONE)) {
                        courseOnSuccess = courseResponse;
                    } else {
                        courseOnError = courseResponse;
                    }

                    RecipeDurationResponse durationResponse = (RecipeDurationResponse)
                            response.getComponentResponses().get(ComponentName.DURATION);
                    if (durationResponse.getFailReasons().contains(RecipeDuration.FailReason.NONE)) {
                        durationOnSuccess = durationResponse;
                    } else {
                        durationOnError = durationResponse;
                    }

                    RecipePortionsResponse portionsResponse = (RecipePortionsResponse)
                            response.getComponentResponses().get(ComponentName.PORTIONS);
                    if (portionsResponse.getFailReasons().contains(RecipePortions.FailReason.NONE)) {
                        portionsOnSuccess = portionsResponse;
                    } else {
                        portionsOnError = portionsResponse;
                    }
                }
            }
        };
    }

    private void whenTimeProviderReturnTime(long time) {
        when(timeProviderMock.getCurrentTimeInMills()).thenReturn(time);
    }
    // endregion helper methods --------------------------------------------------------------------
    private UseCase.Callback<RecipeResponse> getRecipeResponseCallback() {
        return new UseCase.Callback<RecipeResponse>() {
            @Override
            public void onSuccess(RecipeResponse response) {
                System.out.println(response);
                recipeResponseOnSuccess = response;
            }

            @Override
            public void onError(RecipeResponse response) {
                System.out.println(response);
                recipeResponseOnError = response;
            }
        };
    }

    // region helper classes -----------------------------------------------------------------------
    private static class RecipeStateListener implements Recipe.RecipeStateListener {

        private static final String TAG = "tkm-" + RecipeStateListener.class.getSimpleName() + ": ";

        private RecipeStateResponse stateResponse;

        @Override
        public void recipeStateChanged(RecipeStateResponse stateResponse) {
            this.stateResponse = stateResponse;
        }

        public RecipeStateResponse getStateResponse() {
            return stateResponse;
        }

        @Nonnull
        @Override
        public String toString() {
            return "RecipeStateListener{" +
                    "stateResponse=" + stateResponse +
                    '}';
        }

    }

    private class ClassImplementsRecipeResponseCallback implements UseCase.Callback<RecipeResponse> {

        private final String TAG = "tkm-" + ClassImplementsRecipeResponseCallback.class.
                getSimpleName() + ": ";

        @Override
        public void onSuccess(RecipeResponse response) {
            if (response != null) {
                System.out.println(TAG + "recipeResponseOnSuccess: " + response);
                RecipeTest.this.recipeResponseOnSuccess = response;
                identityOnSuccess = (RecipeIdentityResponse) response.getComponentResponses().
                        get(ComponentName.IDENTITY);
                courseOnSuccess = (RecipeCourseResponse) response.getComponentResponses().
                        get(ComponentName.COURSE);
                durationOnSuccess = (RecipeDurationResponse) response.getComponentResponses().
                        get(ComponentName.DURATION);
                portionsOnSuccess = (RecipePortionsResponse) response.getComponentResponses().
                        get(ComponentName.PORTIONS);
            }
        }

        @Override
        public void onError(RecipeResponse response) {
            if (response != null) {
                System.out.println(TAG + "recipeResponseOnError: " + response);
                recipeResponseOnError = response;

                RecipeIdentityResponse identityResponse = (RecipeIdentityResponse)
                        response.getComponentResponses().get(ComponentName.IDENTITY);
                if (identityResponse.getFailReasons().contains(RecipeIdentity.FailReason.NONE)) {
                    identityOnSuccess = identityResponse;
                } else {
                    identityOnError = identityResponse;
                }

                RecipeCourseResponse courseResponse = (RecipeCourseResponse)
                        response.getComponentResponses().get(ComponentName.COURSE);
                if (courseResponse.getFailReasons().contains(RecipeCourse.FailReason.NONE)) {
                    courseOnSuccess = courseResponse;
                } else {
                    courseOnError = courseResponse;
                }

                RecipeDurationResponse durationResponse = (RecipeDurationResponse)
                        response.getComponentResponses().get(ComponentName.DURATION);
                if (durationResponse.getFailReasons().contains(RecipeDuration.FailReason.NONE)) {
                    durationOnSuccess = durationResponse;
                } else {
                    durationOnError = durationResponse;
                }

                RecipePortionsResponse portionsResponse = (RecipePortionsResponse)
                        response.getComponentResponses().get(ComponentName.PORTIONS);
                if (portionsResponse.getFailReasons().contains(RecipePortions.FailReason.NONE)) {
                    portionsOnSuccess = portionsResponse;
                } else {
                    portionsOnError = portionsResponse;
                }
            }
        }
    }

    private static class ClassImplementsIdentityResponseCallback
            implements UseCase.Callback<RecipeIdentityResponse> {

        private static final String TAG = "tkm-" +
                ClassImplementsIdentityResponseCallback.class.getSimpleName() + ": ";

        @Override
        public void onSuccess(RecipeIdentityResponse response) {
            System.out.println(TAG + response);
        }

        @Override
        public void onError(RecipeIdentityResponse response) {
            System.out.println(TAG + response);
        }
    }
    // endregion helper classes --------------------------------------------------------------------
}