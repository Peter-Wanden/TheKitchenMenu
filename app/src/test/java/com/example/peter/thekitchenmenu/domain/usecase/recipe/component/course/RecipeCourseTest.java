package com.example.peter.thekitchenmenu.domain.usecase.recipe.component.course;

import com.example.peter.thekitchenmenu.commonmocks.UseCaseSchedulerMock;
import com.example.peter.thekitchenmenu.data.repository.DomainDataAccess.GetAllDomainModelsCallback;
import com.example.peter.thekitchenmenu.data.repository.recipe.course.TestDataRecipeCoursePersistenceModel;
import com.example.peter.thekitchenmenu.data.repository.recipe.metadata.TestDataRecipeMetadata;
import com.example.peter.thekitchenmenu.data.repository.recipe.RepositoryRecipeCourse;
import com.example.peter.thekitchenmenu.domain.model.CommonFailReason;
import com.example.peter.thekitchenmenu.domain.model.FailReasons;
import com.example.peter.thekitchenmenu.domain.usecase.UseCaseHandler;
import com.example.peter.thekitchenmenu.domain.usecase.UseCase;
import com.example.peter.thekitchenmenu.domain.usecase.recipe.macro.recipe.RecipeRequest;
import com.example.peter.thekitchenmenu.domain.usecase.recipe.metadata.RecipeMetadata;
import com.example.peter.thekitchenmenu.domain.usecase.recipe.metadata.RecipeMetadataPersistenceModel;
import com.example.peter.thekitchenmenu.domain.utils.TimeProvider;
import com.example.peter.thekitchenmenu.domain.utils.UniqueIdProvider;

import org.junit.*;
import org.mockito.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static com.example.peter.thekitchenmenu.domain.usecase.recipe.component.course.RecipeCourse.*;

import static com.example.peter.thekitchenmenu.domain.usecase.recipe.metadata.RecipeMetadata.*;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.*;
import static org.mockito.Mockito.*;
import static org.junit.Assert.assertEquals;

public class RecipeCourseTest {

    private static final String TAG = "tkm-" + RecipeCourseTest.class.getSimpleName() + ": ";

    // region constants ----------------------------------------------------------------------------
    private static final RecipeMetadataPersistenceModel RECIPE_METADATA_VALID_UNCHANGED =
            TestDataRecipeMetadata.getValidUnchanged();
    private static final int NO_COURSES = 0;
    // endregion constants -------------------------------------------------------------------------

    // region helper fields ------------------------------------------------------------------------
    @Mock
    RepositoryRecipeCourse repoCourseMock;
    @Captor
    ArgumentCaptor<GetAllDomainModelsCallback<RecipeCoursePersistenceModel>> repoCourseCallback;
    @Mock
    UniqueIdProvider idProviderMock;
    @Mock
    TimeProvider timeProviderMock;
    @Captor
    ArgumentCaptor<RecipeCoursePersistenceModel> capturedPersistentModel;

    private UseCaseHandler handler;

    private RecipeCourseResponse onSuccessResponse;
    private RecipeCourseResponse onErrorResponse;
    // endregion helper fields ---------------------------------------------------------------------

    private RecipeCourse SUT;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        handler = new UseCaseHandler(new UseCaseSchedulerMock());
        SUT = givenUseCase();
    }

    private RecipeCourse givenUseCase() {
        return new RecipeCourse(
                repoCourseMock,
                idProviderMock,
                timeProviderMock
        );
    }

    @Test
    public void newRequest_domainIdWithNoCourses_emptyListReturned_INVALID_UNCHANGED() {
        // Arrange
        int expectedCourseListSize = MINIMUM_COURSE_LIST_SIZE - 1;
        RecipeCourseRequest request = new RecipeCourseRequest.Builder().
                getDefault().
                setDomainId("idNotInTestData").
                build();

        // Act
        handler.execute(SUT, request, getCallback());
        // Assert
        verifyRepoCalledAndReturnMatchingCourses(request.getDomainId());

        assertEquals(
                ComponentState.INVALID_UNCHANGED,
                onErrorResponse.getMetadata().getState()
        );
        assertTrue(
                onErrorResponse.
                        getMetadata().
                        getFailReasons().
                        contains(CommonFailReason.DATA_UNAVAILABLE)
        );
        assertEquals(
                expectedCourseListSize,
                onErrorResponse.getModel().getCourseList().size()
        );
    }

    @Test
    public void newRequest_coursesDeactivated_lastUpdateAndActiveFlagUpdatedInRepo_INVALID_CHANGED() {
        // Arrange
        String domainId = TestDataRecipeCoursePersistenceModel.NEW_RECIPE_ID;

        int noOfCoursesToDeactivate = 2;

        List<RecipeCoursePersistenceModel> newActiveModels =
                TestDataRecipeCoursePersistenceModel.
                        getNewActiveCourses();

        List<RecipeCoursePersistenceModel> expectedDeactivatedModels =
                TestDataRecipeCoursePersistenceModel.
                        getNewDeactivatedRecipeCourses();
        // Completes the updated timestamp
        when(timeProviderMock.getCurrentTimeInMills()).thenReturn(
                expectedDeactivatedModels.
                        get(0).
                        getLastUpdate()
        );

        RecipeCourseRequest initialisationRequest = new RecipeCourseRequest.Builder().
                getDefault().
                setDomainId(domainId).
                build();

        // Act
        handler.execute(SUT, initialisationRequest, getCallback());
        // Assert
        verify(repoCourseMock).getAllActiveByDomainId(eq(domainId), repoCourseCallback.capture());
        repoCourseCallback.getValue().onAllLoaded(newActiveModels);

        // Arrange - deactivate courses request
        RecipeCourseRequest deactivateRequest = new RecipeCourseRequest.Builder().
                basedOnResponse(onSuccessResponse).
                setModel(new RecipeCourseRequest.Model.Builder().
                        setCourseList(new ArrayList<>()). // Empty list will deactivate all courses
                        build()).
                build();

        // Act
        handler.execute(SUT, deactivateRequest, getCallback());

        // Assert courses updated and deactivated
        verify(repoCourseMock, times(noOfCoursesToDeactivate)).update(
                capturedPersistentModel.
                        capture()
        );
        List<RecipeCoursePersistenceModel> actualDeactivatedModels = capturedPersistentModel.
                getAllValues();

        assertEquals(
                expectedDeactivatedModels,
                actualDeactivatedModels
        );

        // Assert state
        ComponentState expectedState = ComponentState.INVALID_CHANGED;
        ComponentState actualState = onErrorResponse.getMetadata().getState();
        assertEquals(
                expectedState,
                actualState
        );
        // Assert fail reasons
        assertTrue(
                onErrorResponse.
                        getMetadata().
                        getFailReasons().
                        contains(CommonFailReason.DATA_UNAVAILABLE));
    }

    @Test
    public void newRequest_idWithNoCourses_thenAddCourses_VALID_CHANGED() {
        // Arrange
        List<RecipeCoursePersistenceModel> expectedModels = TestDataRecipeCoursePersistenceModel.
                getNewActiveCourses();

        String domainId = expectedModels.get(0).getDomainId();
        long expectedDateTime = expectedModels.get(0).getCreateDate();

        whenTimeProviderReturnTime(expectedDateTime);

        when(idProviderMock.getUId()).thenReturn(
                expectedModels.get(0).getDataId(),
                expectedModels.get(1).getDataId());

        RecipeCourseRequest initialisationRequest = new RecipeCourseRequest.Builder().
                getDefault().
                setDomainId(domainId).
                build();

        // Act
        handler.execute(SUT, initialisationRequest, getCallback());

        // Assert request for data
        verify(repoCourseMock).getAllActiveByDomainId(eq(domainId), repoCourseCallback.capture());
        repoCourseCallback.getValue().onModelsUnavailable();

        // Arrange - add new courses request
        int numberOfAddedCourses = expectedModels.size();
        RecipeCourseRequest addCoursesRequest = new RecipeCourseRequest.Builder().
                basedOnResponse(onErrorResponse).
                setModel(new RecipeCourseRequest.Model.Builder().
                        setCourseList(
                                Arrays.asList(
                                        expectedModels.get(0).getCourse(),
                                        expectedModels.get(1).getCourse()
                                )
                        ).
                        build()
                ).
                build();

        // Act
        handler.execute(SUT, addCoursesRequest, getCallback());
        // Assert
        verify(repoCourseMock, times(numberOfAddedCourses)).save(capturedPersistentModel.capture());

        // Assert models saved are equal in content to expected models
        List<RecipeCoursePersistenceModel> expectedCoursePersistentModels =
                TestDataRecipeCoursePersistenceModel.getNewActiveCourses();
        List<RecipeCoursePersistenceModel> actualCoursePersistentModels =
                capturedPersistentModel.getAllValues();

        assertEquals(
                expectedCoursePersistentModels,
                actualCoursePersistentModels
        );

        // Assert state
        assertEquals(
                ComponentState.VALID_CHANGED,
                onSuccessResponse.getMetadata().getState()
        );
    }

    @Test
    public void existingRequest_persistenceCalledWithCorrectId() {
        // Arrange
        String domainId = RECIPE_METADATA_VALID_UNCHANGED.getDomainId();

        RecipeCourseRequest initialisationRequest = new RecipeCourseRequest.Builder().
                getDefault().
                setDomainId(domainId).
                build();
        // Act
        handler.execute(SUT, initialisationRequest, getCallback());
        // Assert
        verify(repoCourseMock).getAllActiveByDomainId(eq(domainId), repoCourseCallback.capture());
    }

    @Test
    public void existingRequest_completeListOfModelsReturned_VALID_UNCHANGED() {
        // Arrange
        String domainId = RECIPE_METADATA_VALID_UNCHANGED.getDomainId();

        RecipeCourseRequest initialisationRequest = new RecipeCourseRequest.Builder().
                getDefault().
                setDomainId(domainId).
                build();
        // Act
        handler.execute(SUT, initialisationRequest, getCallback());
        // Assert
        verifyRepoCalledAndReturnMatchingCourses(domainId);

        int expectedNumberOfModels = TestDataRecipeCoursePersistenceModel.
                getAllExistingActiveByDomainId(domainId).size();
        int actualNumberOfModels = onSuccessResponse.
                getModel().
                getCourseList().
                size();

        assertEquals(expectedNumberOfModels, actualNumberOfModels);
        // No data has been modified, just returned
        assertEquals(ComponentState.VALID_UNCHANGED, onSuccessResponse.getMetadata().getState());
        assertTrue(onSuccessResponse.getMetadata().getFailReasons().
                contains(CommonFailReason.NONE));
    }

    @Test
    public void existingRequest_allModelsDeleted_INVALID_CHANGED() {
        // Arrange - initialisation request load and return data
        String domainId = RECIPE_METADATA_VALID_UNCHANGED.getDomainId();

        RecipeCourseRequest initialisationRequest = new RecipeCourseRequest.Builder().
                getDefault().
                setDomainId(domainId).
                build();
        // Act
        handler.execute(SUT, initialisationRequest, getCallback());
        // Assert
        verifyRepoCalledAndReturnMatchingCourses(domainId);
        // Assert - verify all courses returned
        int expectedNoOfCourses = TestDataRecipeCoursePersistenceModel.
                getAllExistingActiveByDomainId(domainId).size();
        int actualNoOfCourses = onSuccessResponse.
                getModel().
                getCourseList().
                size();
        assertEquals(expectedNoOfCourses, actualNoOfCourses);

        // Arrange - second request, with no courses:
        RecipeCourseRequest requestWitNoCourses = new RecipeCourseRequest.Builder().
                getDefault().
                setDomainId(domainId).
                build();
        // Act
        handler.execute(SUT, requestWitNoCourses, getCallback());
        // Assert expected sate after courses removed
        assertEquals(ComponentState.INVALID_CHANGED, onErrorResponse.
                getMetadata().
                getState());
        assertTrue(onErrorResponse.
                getMetadata().
                getFailReasons().
                contains(CommonFailReason.DATA_UNAVAILABLE));
        assertTrue(onErrorResponse.
                getModel().
                getCourseList().
                isEmpty());
    }

    // region helper methods -----------------------------------------------------------------------
    private UseCase.Callback<RecipeCourseResponse> getCallback() {
        return new UseCase.Callback<RecipeCourseResponse>() {

            private final String TAG = RecipeCourseTest.TAG + this.getClass().getSimpleName()
                    + ": ";

            @Override
            public void onSuccess(RecipeCourseResponse response) {
                System.out.println(TAG + "onSuccess: " + response);
                onSuccessResponse = response;
            }

            @Override
            public void onError(RecipeCourseResponse response) {
                System.out.println(TAG + "onError: " + response);
                onErrorResponse = response;
            }
        };
    }

    private void verifyRepoCalledAndReturnMatchingCourses(String recipeId) {
        // Confirm repo called and capture the callback
        verify(repoCourseMock).getAllActiveByDomainId(eq(recipeId), repoCourseCallback.capture());
        // Find the matching values in the test data and return the callback with results
        List<RecipeCoursePersistenceModel> courses = TestDataRecipeCoursePersistenceModel.
                getAllExistingActiveByDomainId(recipeId);
        if (courses.size() > 0) {
            System.out.println(TAG + courses.size() + " courses returned from test data.");
            repoCourseCallback.getValue().onAllLoaded(courses);
        } else {
            System.out.println(TAG + "no courses were returned from test data.");
            repoCourseCallback.getValue().onModelsUnavailable();
        }
    }

    private void whenTimeProviderReturnTime(long time) {
        when(timeProviderMock.getCurrentTimeInMills()).thenReturn(time);
    }

    private void sendInitialRequestForNewRecipeCourse(String domainId) {
        // Arrange
        RecipeCourseRequest initialRequest = new RecipeCourseRequest.Builder().getDefault().
                setDomainId(domainId).
                build();

        // Act
        handler.execute(SUT, initialRequest, getCallback());
        // Assert
        verifyRepoCalledAndReturnMatchingCourses(initialRequest.getDomainId());
        // Assert component state
        assertEquals(
                ComponentState.INVALID_UNCHANGED,
                onErrorResponse.getMetadata().getState()
        );
        // Assert course list
        assertEquals(
                NO_COURSES,
                onErrorResponse.getModel().getCourseList().size());
    }
    // endregion helper methods --------------------------------------------------------------------

    // region helper classes -----------------------------------------------------------------------
    // endregion helper classes --------------------------------------------------------------------
}