package com.example.peter.thekitchenmenu.domain.usecase.recipe.component.course;

import com.example.peter.thekitchenmenu.commonmocks.UseCaseSchedulerMock;
import com.example.peter.thekitchenmenu.data.repository.DomainDataAccess.GetAllDomainModelsCallback;
import com.example.peter.thekitchenmenu.data.repository.recipe.RepositoryRecipeCourse;
import com.example.peter.thekitchenmenu.data.repository.recipe.course.TestDataRecipeCourse;
import com.example.peter.thekitchenmenu.data.repository.recipe.metadata.TestDataRecipeMetadata;
import com.example.peter.thekitchenmenu.domain.model.UseCaseMetadataModel;
import com.example.peter.thekitchenmenu.domain.usecase.common.failreasons.CommonFailReason;
import com.example.peter.thekitchenmenu.domain.usecase.common.UseCaseBase;
import com.example.peter.thekitchenmenu.domain.usecase.common.UseCaseHandler;
import com.example.peter.thekitchenmenu.domain.usecase.common.failreasons.FailReasons;
import com.example.peter.thekitchenmenu.domain.usecase.recipe.component.course.RecipeCourse.Course;
import com.example.peter.thekitchenmenu.domain.usecase.recipe.component.metadata.RecipeMetadataPersistenceModel;
import com.example.peter.thekitchenmenu.domain.utils.TimeProvider;
import com.example.peter.thekitchenmenu.domain.utils.UniqueIdProvider;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static com.example.peter.thekitchenmenu.domain.usecase.recipe.component.course.RecipeCourse.MINIMUM_COURSE_LIST_SIZE;
import static com.example.peter.thekitchenmenu.domain.usecase.recipe.component.metadata.RecipeMetadata.ComponentState;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class RecipeCourseTest {

    private static final String TAG = "tkm-" + RecipeCourseTest.class.getSimpleName() + ": ";

    // region constants ----------------------------------------------------------------------------
    private static final RecipeMetadataPersistenceModel RECIPE_METADATA_VALID_UNCHANGED =
            TestDataRecipeMetadata.getValidUnchanged();
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
        RecipeCourseRequest request = new RecipeCourseRequest.Builder().
                getDefault().
                setDomainId("idNotInTestData").
                build();

        // Act
        SUT.execute(request, new CourseCallbackClient());
        // Assert
        verifyRepoCalledAndReturnMatchingCourses(request.getDomainId());
        UseCaseMetadataModel metadata = onErrorResponse.getMetadata();

        assertEquals(
                ComponentState.INVALID_UNCHANGED,
                metadata.getComponentState()
        );

        FailReasons[] expectedFailReasons = new FailReasons[]{CommonFailReason.DATA_UNAVAILABLE};
        FailReasons[] actualFailReasons = metadata.getFailReasons().toArray(new FailReasons[0]);
        assertArrayEquals(
                expectedFailReasons,
                actualFailReasons
        );
        // Assert course list is empty
        assertEquals(0, onErrorResponse.getDomainModel().getCourseList().size());
    }

    @Test
    public void newRequest_coursesDeactivated_lastUpdateAndActiveFlagUpdatedInRepo_INVALID_CHANGED() {
        // Arrange
        String domainId = TestDataRecipeCourse.NEW_RECIPE_ID;

        int noOfCoursesToDeactivate = 2;

        List<RecipeCoursePersistenceModel> newActiveModels = new ArrayList<>(TestDataRecipeCourse.
                getNewActiveCourses());

        List<RecipeCoursePersistenceModel> expectedDeactivatedModels = new ArrayList<>(
                TestDataRecipeCourse.getNewDeactivatedRecipeCourses());
        // Completes the updated timestamp
        when(timeProviderMock.getCurrentTimeInMills()).thenReturn(expectedDeactivatedModels.
                get(0).
                getLastUpdate()
        );

        RecipeCourseRequest initialisationRequest = new RecipeCourseRequest.Builder().
                getDefault().
                setDomainId(domainId).
                build();

        // Act
        SUT.execute(initialisationRequest, new CourseCallbackClient());
        // Assert
        verify(repoCourseMock).getAllActiveByDomainId(eq(domainId), repoCourseCallback.capture());
        repoCourseCallback.getValue().onAllDomainModelsLoaded(newActiveModels);

        // Arrange - deactivate courses request
        RecipeCourseRequest deactivateRequest = new RecipeCourseRequest.Builder().
                basedOnResponse(onSuccessResponse).
                setDomainModel(
                        new RecipeCourseRequest.DomainModel.Builder().
                                setCourseList(
                                        new HashSet<>()). // Empty list deactivates all courses
                                build()).
                build();

        // Act
        SUT.execute(deactivateRequest, new CourseCallbackClient());

        // Assert courses updated and deactivated
        verify(repoCourseMock, times(noOfCoursesToDeactivate)).update(
                capturedPersistentModel.capture()
        );
        List<RecipeCoursePersistenceModel> actualDeactivatedModels = capturedPersistentModel.
                getAllValues();

        assertEquals(
                expectedDeactivatedModels,
                actualDeactivatedModels
        );

        // Assert state
        ComponentState expectedState = ComponentState.INVALID_CHANGED;
        ComponentState actualState = onErrorResponse.getMetadata().getComponentState();
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
        List<RecipeCoursePersistenceModel> expectedModels = new ArrayList<>(TestDataRecipeCourse.
                getNewActiveCourses());

        RecipeCoursePersistenceModel firstCourseToAddPersistenceModel = expectedModels.get(0);
        RecipeCoursePersistenceModel secondCourseToAddPersistenceModel = expectedModels.get(1);
        Set<Course> coursesToAdd = new HashSet<>();
        coursesToAdd.add(firstCourseToAddPersistenceModel.getCourse());
        coursesToAdd.add(secondCourseToAddPersistenceModel.getCourse());

        String domainId = firstCourseToAddPersistenceModel.getDomainId();
        long expectedDateTime = firstCourseToAddPersistenceModel.getCreateDate();

        whenTimeProviderReturnTime(expectedDateTime);

        when(idProviderMock.getUId()).thenReturn(
                firstCourseToAddPersistenceModel.getDataId(),
                secondCourseToAddPersistenceModel.getDataId());

        RecipeCourseRequest initialisationRequest = new RecipeCourseRequest.Builder().
                getDefault().
                setDomainId(domainId).
                build();

        // Act
        SUT.execute(initialisationRequest, new CourseCallbackClient());

        // Assert request for data
        verify(repoCourseMock).getAllActiveByDomainId(eq(domainId), repoCourseCallback.capture());
        repoCourseCallback.getValue().onDomainModelsUnavailable();

        // Arrange - add new courses request
        int numberOfAddedCourses = expectedModels.size();

        RecipeCourseRequest addCoursesRequest = new RecipeCourseRequest.Builder().
                basedOnResponse(onErrorResponse).
                setDomainModel(
                        new RecipeCourseRequest.DomainModel.Builder().
                                setCourseList(coursesToAdd).
                                build()
                ).
                build();

        // Act
        SUT.execute(addCoursesRequest, new CourseCallbackClient());
        // Assert
        verify(repoCourseMock, times(numberOfAddedCourses)).save(capturedPersistentModel.capture());

        // Assert models saved are equal in content to expected models
        List<RecipeCoursePersistenceModel> actualCoursePersistentModels =
                capturedPersistentModel.getAllValues();

        assertEquals(
                expectedModels,
                actualCoursePersistentModels
        );

        // Assert state
        assertEquals(
                ComponentState.VALID_CHANGED,
                onSuccessResponse.getMetadata().getComponentState()
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
        handler.executeAsync(SUT, initialisationRequest, new CourseCallbackClient());
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
        handler.executeAsync(SUT, initialisationRequest, new CourseCallbackClient());
        // Assert
        verifyRepoCalledAndReturnMatchingCourses(domainId);

        int expectedNumberOfModels = TestDataRecipeCourse.
                getAllExistingActiveByDomainId(domainId).size();
        int actualNumberOfModels = onSuccessResponse.
                getDomainModel().
                getCourseList().
                size();

        assertEquals(expectedNumberOfModels, actualNumberOfModels);
        // No data has been modified, just returned
        assertEquals(ComponentState.VALID_UNCHANGED, onSuccessResponse.getMetadata().getComponentState());
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
        handler.executeAsync(SUT, initialisationRequest, new CourseCallbackClient());
        // Assert
        verifyRepoCalledAndReturnMatchingCourses(domainId);
        // Assert - verify all courses returned
        int expectedNoOfCourses = TestDataRecipeCourse.
                getAllExistingActiveByDomainId(domainId).size();
        int actualNoOfCourses = onSuccessResponse.
                getDomainModel().
                getCourseList().
                size();
        assertEquals(expectedNoOfCourses, actualNoOfCourses);

        // Arrange - second request, with no courses:
        RecipeCourseRequest requestWitNoCourses = new RecipeCourseRequest.Builder().
                getDefault().
                setDomainId(domainId).
                build();
        // Act
        handler.executeAsync(SUT, requestWitNoCourses, new CourseCallbackClient());
        // Assert expected sate after courses removed
        assertEquals(ComponentState.INVALID_CHANGED, onErrorResponse.
                getMetadata().
                getComponentState());
        assertTrue(onErrorResponse.
                getMetadata().
                getFailReasons().
                contains(CommonFailReason.DATA_UNAVAILABLE));
        assertTrue(onErrorResponse.
                getDomainModel().
                getCourseList().
                isEmpty());
    }

    // region helper methods -----------------------------------------------------------------------
    private void verifyRepoCalledAndReturnMatchingCourses(String recipeId) {
        // Confirm repo called and capture the callback
        verify(repoCourseMock).getAllActiveByDomainId(eq(recipeId), repoCourseCallback.capture());
        // Find matching values in the test data and return  results
        List<RecipeCoursePersistenceModel> courses = new ArrayList<>(TestDataRecipeCourse.
                getAllExistingActiveByDomainId(recipeId));

        if (courses.size() > 0) {
            System.out.println(TAG + courses.size() + " courses returned from test data.");
            repoCourseCallback.getValue().onAllDomainModelsLoaded(courses);
        } else {
            System.out.println(TAG + "no courses were returned from test data.");
            repoCourseCallback.getValue().onDomainModelsUnavailable();
        }
    }

    private void whenTimeProviderReturnTime(long time) {
        when(timeProviderMock.getCurrentTimeInMills()).thenReturn(time);
    }
    // endregion helper methods --------------------------------------------------------------------

    // region helper classes -----------------------------------------------------------------------
    private final class CourseCallbackClient
            implements
            UseCaseBase.Callback<RecipeCourseResponse> {
        @Override
        public void onUseCaseSuccess(RecipeCourseResponse response) {
            System.out.println(TAG + "onSuccess: " + response);
            onSuccessResponse = response;
        }

        @Override
        public void onUseCaseError(RecipeCourseResponse response) {
            System.out.println(TAG + "onError: " + response);
            onErrorResponse = response;
        }
    }
    // endregion helper classes --------------------------------------------------------------------
}