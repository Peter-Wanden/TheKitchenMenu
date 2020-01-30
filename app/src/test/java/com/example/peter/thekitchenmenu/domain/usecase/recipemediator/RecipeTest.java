package com.example.peter.thekitchenmenu.domain.usecase.recipemediator;

import com.example.peter.thekitchenmenu.commonmocks.UseCaseSchedulerMock;
import com.example.peter.thekitchenmenu.data.entity.RecipeCourseEntity;
import com.example.peter.thekitchenmenu.data.entity.RecipeDurationEntity;
import com.example.peter.thekitchenmenu.data.entity.RecipeEntity;
import com.example.peter.thekitchenmenu.data.entity.RecipeIdentityEntity;
import com.example.peter.thekitchenmenu.data.entity.RecipePortionsEntity;
import com.example.peter.thekitchenmenu.data.repository.DataSource;
import com.example.peter.thekitchenmenu.data.repository.RepositoryRecipeCourse;
import com.example.peter.thekitchenmenu.data.repository.RepositoryRecipeDuration;
import com.example.peter.thekitchenmenu.data.repository.RepositoryRecipeIdentity;
import com.example.peter.thekitchenmenu.domain.usecase.FailReasons;
import com.example.peter.thekitchenmenu.domain.usecase.UseCase;
import com.example.peter.thekitchenmenu.domain.usecase.UseCaseHandler;
import com.example.peter.thekitchenmenu.domain.usecase.recipe.Recipe;
import com.example.peter.thekitchenmenu.domain.usecase.recipe.RecipeRequest;
import com.example.peter.thekitchenmenu.domain.usecase.recipe.RecipeResponse;
import com.example.peter.thekitchenmenu.domain.usecase.recipe.recipestate.RecipeStateCalculator;
import com.example.peter.thekitchenmenu.domain.usecase.recipe.recipestate.RecipeStateResponse;
import com.example.peter.thekitchenmenu.domain.usecase.recipe.recipeidentity.RecipeIdentity;
import com.example.peter.thekitchenmenu.domain.usecase.recipe.recipeidentity.RecipeIdentityRequest;
import com.example.peter.thekitchenmenu.domain.usecase.recipe.recipeidentity.RecipeIdentityResponse;
import com.example.peter.thekitchenmenu.domain.usecase.recipe.recipecourse.RecipeCourse;
import com.example.peter.thekitchenmenu.domain.usecase.recipe.recipecourse.RecipeCourseRequest;
import com.example.peter.thekitchenmenu.domain.usecase.recipe.recipecourse.RecipeCourseResponse;
import com.example.peter.thekitchenmenu.domain.usecase.recipeduration.RecipeDuration;
import com.example.peter.thekitchenmenu.domain.usecase.recipeduration.RecipeDurationResponse;
import com.example.peter.thekitchenmenu.domain.usecase.recipeduration.RecipeDurationTest;
import com.example.peter.thekitchenmenu.domain.usecase.recipeidentity.RecipeIdentityTest;
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
    private static final RecipeEntity INVALID_NEW =
            TestDataRecipeEntity.getNewInvalid();

    private static final RecipeIdentityEntity IDENTITY_INVALID_NEW_EMPTY =
            TestDataRecipeIdentityEntity.getInvalidNewEmpty();
    private static final RecipeIdentityEntity IDENTITY_VALID_NEW_COMPLETE =
            TestDataRecipeIdentityEntity.getValidNewComplete();

    private static final RecipeEntity VALID_EXISTING_RECIPE =
            TestDataRecipeEntity.getValidExisting();
    private static final RecipeDurationEntity VALID_EXISTING_COMPLETE_DURATION =
            TestDataRecipeDurationEntity.getValidExistingComplete();
    private static final RecipeIdentityEntity VALID_EXISTING_COMPLETE_IDENTITY =
            TestDataRecipeIdentityEntity.getValidExistingTitleValidDescriptionValid();
    private static final RecipePortionsEntity VALID_EXISTING_PORTIONS =
            TestDataRecipePortionsEntity.getExistingValidNinePortions();

    // endregion constants -------------------------------------------------------------------------

    // region helper fields ------------------------------------------------------------------------
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
    TimeProvider timeProviderMock;
    @Mock
    UniqueIdProvider idProvider;
    @Captor
    ArgumentCaptor<RecipeStateResponse> recipeStateCaptor;

    private UseCaseHandler handler;
    @Mock
    private RecipeClient recipeClientListener1;
    @Mock
    private RecipeClient recipeClientListener2;

    private Recipe SUT;
    private RecipeResponse recipeOnSuccessResponse;
    private RecipeResponse recipeOnErrorResponse;

    private RecipeIdentity recipeIdentity;
    private RecipeIdentityResponse identityOnSuccessResponse;
    private RecipeIdentityResponse identityOnErrorResponse;

    private RecipeCourse recipeCourse;
    private RecipeCourseResponse courseOnSuccessResponse;
    private RecipeCourseResponse courseOnErrorResponse;

    private RecipeDuration recipeDuration;
    private RecipeDurationResponse durationOnSuccessResponse;
    private RecipeDurationResponse durationOnErrorResponse;

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

        recipeIdentity = new RecipeIdentity(
                repoIdentityMock,
                timeProviderMock,
                handler,
                textValidator
        );

        recipeCourse = new RecipeCourse(
                repoCourseMock,
                idProvider,
                timeProviderMock
        );

        recipeDuration = new RecipeDuration(
                repoDurationMock,
                timeProviderMock,
                RecipeDurationTest.MAX_PREP_TIME,
                RecipeDurationTest.MAX_COOK_TIME
        );

        RecipeStateCalculator stateCalculator = new RecipeStateCalculator();

        return new Recipe(handler, stateCalculator, recipeIdentity, recipeCourse, recipeDuration);
    }

    // todo - test for clone, delete and favorite as separate macro commands

    @Test
    public void recipeRequestNewId_invokerIssuesCommand() {
        // Arrange
        String recipeId = INVALID_NEW.getId();
        RecipeRequest request = new RecipeRequest(recipeId);

        // Act
        SUT.registerClientListener(recipeClientListener1);
        handler.execute(SUT, request, getCallback());

        // Assert database calls, return data not available
        verifyIdentityDatabaseCalledWithIdAndReturnDataUnavailable(recipeId);
        verifyCoursesDatabaseCalledWithIdAndReturnDataUnavailable(recipeId);
        verifyDurationDatabaseCalledWithIdAndReturnDataUnavailable(recipeId);
    }

    @Test
    public void recipeRequestNewId_invokerIssuesCommand_listenersUpdatedWithCorrectRecipeStateValues() {
        // Arrange
        String recipeId = INVALID_NEW.getId();
        RecipeRequest request = new RecipeRequest(recipeId);

        // Act
        SUT.registerClientListener(recipeClientListener1);
        handler.execute(SUT, request, getCallback());

        // Assert database calls
        verifyIdentityDatabaseCalledWithIdAndReturnDataUnavailable(recipeId);
        verifyCoursesDatabaseCalledWithIdAndReturnDataUnavailable(recipeId);
        verifyDurationDatabaseCalledWithIdAndReturnDataUnavailable(recipeId);

        // Assert recipe listener updated with correct recipe state
        verify(recipeClientListener1).recipeStateChanged(recipeStateCaptor.capture());
        RecipeStateResponse recipeStateResponse = recipeStateCaptor.getValue();

        // Assert recipe state updated
        assertEquals(RecipeState.DATA_UNAVAILABLE, recipeStateResponse.getState());
        assertEquals(3, recipeStateResponse.getComponentStates().size());

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
    }

    @Test
    public void recipeRequestNewId_invokerIssuesCommand_responseINVALID_UNCHANGED_INVALID_MODELS() {
        // Arrange
        String recipeId = INVALID_NEW.getId();
        RecipeRequest request = new RecipeRequest(recipeId);

        // Act
        SUT.registerClientListener(recipeClientListener1);
        handler.execute(SUT, request, getCallback());

        // Assert database calls
        verifyIdentityDatabaseCalledWithIdAndReturnDataUnavailable(recipeId);
        verifyCoursesDatabaseCalledWithIdAndReturnDataUnavailable(recipeId);

        // Assert, request originator updated with recipe response
        assertEquals(RecipeState.DATA_UNAVAILABLE, recipeOnErrorResponse.getRecipeState());
        assertTrue(recipeOnErrorResponse.getFailReasons().contains(FailReason.MISSING_COMPONENTS));
    }

    @Test
    public void recipeRequestNewId_invokerIssuesCommand_correctComponentResponses() {
        // Arrange
        String recipeId = INVALID_NEW.getId();
        RecipeRequest request = new RecipeRequest(recipeId);

        // Act
        SUT.registerClientListener(recipeClientListener1);
        handler.execute(SUT, request, getCallback());

        // Assert database calls
        verifyIdentityDatabaseCalledWithIdAndReturnDataUnavailable(recipeId);
        verifyCoursesDatabaseCalledWithIdAndReturnDataUnavailable(recipeId);

        // Assert response from identity component
        ComponentState identityState = recipeOnErrorResponse.getIdentityResponse().getState();
        assertEquals(ComponentState.DATA_UNAVAILABLE, identityState);
        List<FailReasons> identityFails = recipeOnErrorResponse.getIdentityResponse().getFailReasons();
        assertEquals(1, identityFails.size());
        assertTrue(identityFails.contains(RecipeIdentity.FailReason.DATA_UNAVAILABLE));

        // Assert response from courses component
        ComponentState coursesState = recipeOnErrorResponse.getCourseResponse().getState();
        assertEquals(ComponentState.DATA_UNAVAILABLE, coursesState);
        List<FailReasons> coursesFails = recipeOnErrorResponse.getCourseResponse().getFailReasons();
        assertEquals(1, coursesFails.size());
        assertTrue(coursesFails.contains(RecipeCourse.FailReason.NO_COURSES_SET));
    }

    @Test
    public void identityRequestNewId_initialRequest_invokerIssuesCommandToAllReceivers() {
        // Arrange
        String recipeId = INVALID_NEW.getId();
        RecipeIdentityRequest identityRequest = RecipeIdentityRequest.Builder.getDefault().
                setRecipeId(recipeId).
                build();

        // Act
        SUT.registerClientListener(recipeClientListener1);
        handler.execute(SUT, identityRequest, getCallback());

        // Assert
        verifyIdentityDatabaseCalledWithIdAndReturnDataUnavailable(recipeId);
        verifyCoursesDatabaseCalledWithIdAndReturnDataUnavailable(recipeId);
    }

    @Test
    public void coursesRequestNewId_initialRequest_invokerIssuesCommandToAllReceivers() {
        // Arrange
        String recipeId = INVALID_NEW.getId();
        RecipeCourseRequest coursesRequest = RecipeCourseRequest.Builder.getDefault().
                setRecipeId(recipeId).
                build();

        // Act
        SUT.registerClientListener(recipeClientListener1);
        handler.execute(SUT, coursesRequest, getCallback());

        // Assert
        verifyIdentityDatabaseCalledWithIdAndReturnDataUnavailable(recipeId);
        verifyCoursesDatabaseCalledWithIdAndReturnDataUnavailable(recipeId);
    }

    @Test
    public void identityRequestNewId_titleValidDescriptionValid_identityStateVALID_CHANGED() {
        // Arrange
        String recipeId = IDENTITY_INVALID_NEW_EMPTY.getId();
        ArgumentCaptor<RecipeIdentityEntity> identityEntity = ArgumentCaptor.forClass(RecipeIdentityEntity.class);
        whenTimeProviderReturnTime(IDENTITY_VALID_NEW_COMPLETE.getCreateDate());

        // Act - Request/Response 1
        RecipeIdentityRequest firstRequest = RecipeIdentityRequest.Builder.
                getDefault().
                setRecipeId(recipeId).
                build();
        SUT.registerClientListener(recipeClientListener1);
        handler.execute(SUT, firstRequest, getCallback());
        verifyIdentityDatabaseCalledWithIdAndReturnDataUnavailable(recipeId);
        verifyCoursesDatabaseCalledWithIdAndReturnDataUnavailable(recipeId);

        // Request/Response 2
        String validTitle = IDENTITY_VALID_NEW_COMPLETE.getTitle();
        RecipeIdentityRequest.Model validTitleModel = RecipeIdentityRequest.Model.Builder.
                basedOn(identityOnErrorResponse.getModel()).
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
                basedOn(identityOnSuccessResponse.getModel()).
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
        assertEquals(validTitle, identityOnSuccessResponse.getModel().getTitle());
        assertEquals(validDescription, identityOnSuccessResponse.getModel().getDescription());

        // Assert identity component state
        assertEquals(ComponentState.VALID_CHANGED, identityOnSuccessResponse.getState());
        assertEquals(1, identityOnSuccessResponse.getFailReasons().size());
        assertTrue(identityOnSuccessResponse.getFailReasons().contains(RecipeIdentity.FailReason.NONE));

        // Assert client listeners updated
        verify(recipeClientListener1, times((2))).recipeStateChanged(recipeStateCaptor.capture());

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
        SUT.registerClientListener(recipeClientListener1);

        // Act
        handler.execute(SUT, initialRequest, getCallback());

        // Assert
        verifyIdentityDatabaseCalledWithIdAndReturnDataUnavailable(recipeId);
        verifyCoursesDatabaseCalledWithIdAndReturnDataUnavailable(recipeId);

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
        assertEquals(ComponentState.VALID_CHANGED, recipeOnErrorResponse.getCourseResponse().getState());
    }

    @Test
    public void recipeRequestExistingId_validData_onlyRegisteredListenersNotified() {
        // Arrange
        String recipeId = VALID_EXISTING_RECIPE.getId();
        RecipeRequest request = new RecipeRequest(recipeId);

        // Act
        SUT.registerClientListener(recipeClientListener1);
        SUT.registerClientListener(recipeClientListener2);
        SUT.unRegisterClientListener(recipeClientListener2);
        handler.execute(SUT, request, getCallback());

        // Assert database called and return data
        verify(repoIdentityMock).getById(eq(recipeId), repoIdentityCallback.capture());
        repoIdentityCallback.getValue().onEntityLoaded(VALID_EXISTING_COMPLETE_IDENTITY);
        verify(repoCourseMock).getCoursesForRecipe(eq(recipeId), repoCourseCallback.capture());
        repoCourseCallback.getValue().onAllLoaded(TestDataRecipeCourseEntity.getAllByRecipeId(recipeId));

        // Assert listeners called
        verify(recipeClientListener1).recipeStateChanged(any(RecipeStateResponse.class));
        verifyNoMoreInteractions(recipeClientListener2);
    }

    @Test
    public void recipeRequestExistingId_validData_componentStateVALID_UNCHANGED() {
        // Arrange
        String recipeId = VALID_EXISTING_RECIPE.getId();
        RecipeRequest request = new RecipeRequest(recipeId);
        // Act
        SUT.registerClientListener(recipeClientListener1);
        SUT.registerClientListener(recipeClientListener2);
        handler.execute(SUT, request, getCallback());

        // Assert database called and return data
        verify(repoIdentityMock).getById(eq(recipeId), repoIdentityCallback.capture());
        repoIdentityCallback.getValue().onEntityLoaded(VALID_EXISTING_COMPLETE_IDENTITY);
        verify(repoCourseMock).getCoursesForRecipe(eq(recipeId), repoCourseCallback.capture());
        repoCourseCallback.getValue().onAllLoaded(TestDataRecipeCourseEntity.getAllByRecipeId(recipeId));

        // Assert listeners called
        verify(recipeClientListener1).recipeStateChanged(recipeStateCaptor.capture());
        verify(recipeClientListener2).recipeStateChanged(recipeStateCaptor.capture());

        // Assert recipe component states
        HashMap<ComponentName, ComponentState> componentStates = recipeStateCaptor.getValue().
                getComponentStates();
        assertEquals(ComponentState.VALID_UNCHANGED, componentStates.get(ComponentName.IDENTITY));
        assertEquals(ComponentState.VALID_UNCHANGED, componentStates.get(ComponentName.COURSE));
    }

    @Test
    public void existingId_validData_recipeStateVALID_UNCHANGED() {
        // Arrange
        String recipeId = VALID_EXISTING_RECIPE.getId();
        RecipeRequest request = new RecipeRequest(recipeId);

        // Act
        SUT.registerClientListener(recipeClientListener1);
        SUT.registerClientListener(recipeClientListener2);
        handler.execute(SUT, request, getCallback());

        // Assert database called by components and return data
        verify(repoIdentityMock).getById(eq(recipeId), repoIdentityCallback.capture());
        repoIdentityCallback.getValue().onEntityLoaded(VALID_EXISTING_COMPLETE_IDENTITY);
        verify(repoCourseMock).getCoursesForRecipe(eq(recipeId), repoCourseCallback.capture());
        repoCourseCallback.getValue().onAllLoaded(TestDataRecipeCourseEntity.getAllByRecipeId(recipeId));

        // Assert listeners called
        verify(recipeClientListener1).recipeStateChanged(recipeStateCaptor.capture());
        verify(recipeClientListener2).recipeStateChanged(recipeStateCaptor.capture());

        // Assert correct recipe state
        RecipeState recipeState = recipeStateCaptor.getValue().getState();
        assertEquals(RecipeState.VALID_UNCHANGED, recipeState);
    }

    // region helper methods -----------------------------------------------------------------------
    private void verifyIdentityDatabaseCalledWithIdAndReturnDataUnavailable(String recipeId) {
        verify(repoIdentityMock).getById(eq(recipeId), repoIdentityCallback.capture());
        repoIdentityCallback.getValue().onDataNotAvailable();
    }
    private void verifyCoursesDatabaseCalledWithIdAndReturnDataUnavailable(String recipeId) {
        verify(repoCourseMock).getCoursesForRecipe(eq(recipeId), repoCourseCallback.capture());
        repoCourseCallback.getValue().onDataNotAvailable();
    }
    private void verifyDurationDatabaseCalledWithIdAndReturnDataUnavailable(String recipeId) {
        verify(repoDurationMock).getById(eq(recipeId), repoDurationCallback.capture());
        repoDurationCallback.getValue().onDataNotAvailable();
    }

    private UseCase.Callback<RecipeResponse> getCallback() {
        return new UseCase.Callback<RecipeResponse>() {

            @Override
            public void onSuccess(RecipeResponse response) {
                if (response != null) {
                    System.out.println(TAG + "recipeResponseOnSuccess: " + response);
                    recipeOnSuccessResponse = response;
                    identityOnSuccessResponse = response.getIdentityResponse();
                    courseOnSuccessResponse = response.getCourseResponse();
                }
            }

            @Override
            public void onError(RecipeResponse response) {
                if (response != null) {
                    System.out.println(TAG + "recipeResponseOnError: " + response);
                    recipeOnErrorResponse = response;

                    if (response.getIdentityResponse().getFailReasons().
                            contains(RecipeIdentity.FailReason.NONE)) {
                        identityOnSuccessResponse = response.getIdentityResponse();
                    } else {
                        identityOnErrorResponse = response.getIdentityResponse();
                    }

                    if (response.getCourseResponse().getFailReasons().
                            contains(RecipeCourse.FailReason.NONE)) {
                        courseOnSuccessResponse = response.getCourseResponse();
                    } else {
                        courseOnErrorResponse = response.getCourseResponse();
                    }
                }
            }
        };
    }

    private void whenTimeProviderReturnTime(long time) {
        when(timeProviderMock.getCurrentTimeInMills()).thenReturn(time);
    }
    // endregion helper methods --------------------------------------------------------------------

    // region helper classes -----------------------------------------------------------------------
    private static class RecipeClient implements Recipe.Listener {

        @Override
        public void recipeStateChanged(RecipeStateResponse stateResponse) {

        }
    }
    // endregion helper classes --------------------------------------------------------------------
}