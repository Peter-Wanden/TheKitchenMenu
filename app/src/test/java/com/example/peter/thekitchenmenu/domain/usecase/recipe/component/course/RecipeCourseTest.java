package com.example.peter.thekitchenmenu.domain.usecase.recipe.component.course;

import com.example.peter.thekitchenmenu.data.repository.DomainDataAccess.GetDomainModelCallback;
import com.example.peter.thekitchenmenu.data.repository.recipe.RepositoryRecipeCourse;
import com.example.peter.thekitchenmenu.data.repository.recipe.course.TestDataRecipeCourse;
import com.example.peter.thekitchenmenu.data.repository.recipe.metadata.TestDataRecipeMetadata;
import com.example.peter.thekitchenmenu.domain.model.UseCaseMetadataModel;
import com.example.peter.thekitchenmenu.domain.usecase.common.failreasons.CommonFailReason;
import com.example.peter.thekitchenmenu.domain.usecase.common.UseCaseBase;
import com.example.peter.thekitchenmenu.domain.usecase.common.failreasons.FailReasons;
import com.example.peter.thekitchenmenu.domain.usecase.recipe.component.course.RecipeCourse.Course;
import com.example.peter.thekitchenmenu.domain.usecase.recipe.component.course.RecipeCourse.FailReason;
import com.example.peter.thekitchenmenu.domain.usecase.recipe.component.metadata.RecipeMetadataPersistenceModel;
import com.example.peter.thekitchenmenu.domain.utils.TimeProvider;
import com.example.peter.thekitchenmenu.domain.utils.UniqueIdProvider;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;

import static com.example.peter.thekitchenmenu.domain.usecase.recipe.component.metadata.RecipeMetadata.ComponentState;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.eq;
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
    ArgumentCaptor<GetDomainModelCallback<RecipeCoursePersistenceModel>> repoCourseCallback;
    @Mock
    UniqueIdProvider idProviderMock;
    @Mock
    TimeProvider timeProviderMock;
    @Captor
    ArgumentCaptor<RecipeCoursePersistenceModel> capturedPersistentModel;

    private RecipeCourseResponse onSuccessResponse;
    private RecipeCourseResponse onErrorResponse;
    // endregion helper fields ---------------------------------------------------------------------

    private RecipeCourse SUT;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
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
    public void newRequest_defaultCourseListReturned_stateINVALID_UNCHANGED() {
        // Arrange
        RecipeCoursePersistenceModel expectedDefaultCourseSave = TestDataRecipeCourse.
                getNewActiveDefaultNoCourses();

        RecipeCourseRequest request = new RecipeCourseRequest.Builder().
                getDefault().
                setDomainId(expectedDefaultCourseSave.getDomainId()).
                build();

        when(idProviderMock.getUId()).thenReturn(
                expectedDefaultCourseSave.getDataId()
        );
        when(timeProviderMock.getCurrentTimeInMills()).thenReturn(
                expectedDefaultCourseSave.getCreateDate()
        );

        // Act
        SUT.execute(request, new CourseCallbackClient());

        // Assert
        verify(repoCourseMock).getActiveByDomainId(eq(expectedDefaultCourseSave.getDomainId()),
                repoCourseCallback.capture());
        repoCourseCallback.getValue().onDomainModelUnavailable();

        verify(repoCourseMock).save(eq(expectedDefaultCourseSave));

        UseCaseMetadataModel metadata = onErrorResponse.getMetadata();

        assertEquals(
                ComponentState.INVALID_UNCHANGED,
                metadata.getComponentState()
        );

        // Assert fail reasons
        FailReasons[] expectedFailReasons = new FailReasons[]
                {FailReason.NO_COURSE_SELECTED, CommonFailReason.DATA_UNAVAILABLE};
        FailReasons[] actualFailReasons = metadata.getFailReasons().toArray(new FailReasons[0]);

        assertArrayEquals(
                expectedFailReasons,
                actualFailReasons
        );

        // Assert course list is empty
        Course[] expectedCourseList = new Course[]
                {Course.DEFAULT_NO_COURSES};

        Course[] actualCourseList = onErrorResponse.
                getDomainModel().
                getCourseList().
                toArray(new Course[0]);

        assertArrayEquals(
                expectedCourseList,
                actualCourseList
        );
    }

    @Test
    public void newRequest_addCourseOne_VALID_CHANGED() {
        // Arrange
        // execute and test a new empty request that returns DEFAULT_NO_COURSES
        newRequest_defaultCourseListReturned_stateINVALID_UNCHANGED();

        // Arrange persistent model for archiving DEFAULT_NO_COURSES old state
        RecipeCoursePersistenceModel expectedArchivedDefaultNoCoursesModel = TestDataRecipeCourse.
                getNewArchivedDefaultNoCourses();
        // Arrange persistent model to add course one
        RecipeCoursePersistenceModel expectedCourseOneSaveModel = TestDataRecipeCourse.
                getNewActiveRecipeCourseOne();

        // Arrange request and model to add course one
        RecipeCourseRequest.DomainModel model = new RecipeCourseRequest.DomainModel.Builder().
                setCourseList(expectedCourseOneSaveModel.getCourses()).
                build();
        RecipeCourseRequest addCourseOneRequest = new RecipeCourseRequest.Builder().
                basedOnResponse(onErrorResponse).
                setDomainModel(model).
                build();

        // new times for archive and new course model save
        when(timeProviderMock.getCurrentTimeInMills()).thenReturn(
                expectedArchivedDefaultNoCoursesModel.getLastUpdate(), // old model last update
                expectedCourseOneSaveModel.getCreateDate(), // new active model time
                expectedCourseOneSaveModel.getLastUpdate() // new active model time
        );
        // new data id for course one persistence model
        when(idProviderMock.getUId()).thenReturn(
                expectedCourseOneSaveModel.getDataId()
        );

        // Act
        SUT.execute(addCourseOneRequest, new CourseCallbackClient());

        // Assert old course archived
        verify(repoCourseMock).save(eq(expectedArchivedDefaultNoCoursesModel));
//        // Assert new persistence model saved
//        verify(repoCourseMock).save(eq(expectedCourseOneSaveModel));
//
//        // Assert correct response values
//        RecipeCourseResponse response = onSuccessResponse;
//        UseCaseMetadataModel metadata = response.getMetadata();
//
//        // Assert state
//        ComponentState expectedState = ComponentState.VALID_CHANGED;
//        ComponentState actualState = metadata.getComponentState();
//        assertEquals(
//                expectedState,
//                actualState
//        );
//
//        FailReasons[] expectedFailReasons = new FailReasons[]{CommonFailReason.NONE};
//        FailReasons[] actualFailReasons = metadata.getFailReasons().toArray(new FailReasons[0]);
//        assertArrayEquals(
//                expectedFailReasons,
//                actualFailReasons
//        );
    }

    @Test
    public void newRequest_deactivatedCoursesCorrectlyArchived_stateINVALID_CHANGED() {
//        // Arrange
//        String domainId = TestDataRecipeCourse.NEW_RECIPE_ID;
//
//        RecipeCoursePersistenceModel newActivatedModel = TestDataRecipeCourse.getNewActiveCourses();
//
//        List<RecipeCoursePersistenceModel> expectedArchivedModels = new ArrayList<>(
//                TestDataRecipeCourse.getNewArchivedRecipeCourses());
//        int noOfCoursesToDeactivate = newActivatedModels.size();
//
//        // Completes the updated timestamp for archived courses
//        when(timeProviderMock.getCurrentTimeInMills()).thenReturn(expectedArchivedModels.
//                get(0).
//                getLastUpdate()
//        );
//
//        RecipeCourseRequest initialisationRequest = new RecipeCourseRequest.Builder().
//                getDefault().
//                setDomainId(domainId).
//                build();
//
//        // Act
//        SUT.execute(initialisationRequest, new CourseCallbackClient());
//        // Assert and return
//        verify(repoCourseMock).getActiveByDomainId(eq(domainId), repoCourseCallback.capture());
//        repoCourseCallback.getValue().onDomainModelUnavailable();
//
//        // Add courses
//
//
//        verify(repoCourseMock).getActiveByDomainId(eq(domainId), repoCourseCallback.capture());
//        repoCourseCallback.getValue().onDomainModelLoaded(newActivatedModels);
//
//        // Arrange - deactivate courses request
//        RecipeCourseRequest deactivateRequest = new RecipeCourseRequest.Builder().
//                basedOnResponse(onSuccessResponse).
//                setDomainModel(
//                        new RecipeCourseRequest.DomainModel.Builder().
//                                setCourseList(
//                                        new HashSet<>()). // Empty list deactivates all courses
//                                build()).
//                build();
//
//        // Act
//        SUT.execute(deactivateRequest, new CourseCallbackClient());
//
//        // Assert courses updated and deactivated
//        verify(repoCourseMock, times(noOfCoursesToDeactivate)).update(
//                capturedPersistentModel.capture()
//        );
//        List<RecipeCoursePersistenceModel> actualDeactivatedModels = capturedPersistentModel.
//                getAllValues();
//
//        assertEquals(
//                expectedDeactivatedModels,
//                actualDeactivatedModels
//        );
//
//        // Assert state
//        ComponentState expectedState = ComponentState.INVALID_CHANGED;
//        ComponentState actualState = onErrorResponse.getMetadata().getComponentState();
//        assertEquals(
//                expectedState,
//                actualState
//        );
//        // Assert fail reasons
//        assertTrue(
//                onErrorResponse.
//                        getMetadata().
//                        getFailReasons().
//                        contains(CommonFailReason.DATA_UNAVAILABLE));
    }

    @Test
    public void newRequest_idWithNoCourses_thenAddCourses_VALID_CHANGED() {
//        // Arrange
//        List<RecipeCoursePersistenceModelItem> expectedModels = new ArrayList<>(TestDataRecipeCourse.
//                getNewActiveCourses());
//
//        RecipeCoursePersistenceModelItem firstCourseToAddPersistenceModel = expectedModels.get(0);
//        RecipeCoursePersistenceModelItem secondCourseToAddPersistenceModel = expectedModels.get(1);
//        Set<Course> coursesToAdd = new HashSet<>();
//        coursesToAdd.add(firstCourseToAddPersistenceModel.getCourse());
//        coursesToAdd.add(secondCourseToAddPersistenceModel.getCourse());
//
//        String domainId = firstCourseToAddPersistenceModel.getDomainId();
//        long expectedDateTime = firstCourseToAddPersistenceModel.getCreateDate();
//
//        whenTimeProviderReturnTime(expectedDateTime);
//
//        when(idProviderMock.getUId()).thenReturn(
//                firstCourseToAddPersistenceModel.getDataId(),
//                secondCourseToAddPersistenceModel.getDataId());
//
//        RecipeCourseRequest initialisationRequest = new RecipeCourseRequest.Builder().
//                getDefault().
//                setDomainId(domainId).
//                build();
//
//        // Act
//        SUT.execute(initialisationRequest, new CourseCallbackClient());
//
//        // Assert request for data
//        verify(repoCourseMock).getActiveByDomainId(eq(domainId), repoCourseCallback.capture());
//        repoCourseCallback.getValue().onDomainModelUnavailable();
//
//        // Arrange - add new courses request
//        int numberOfAddedCourses = expectedModels.size();
//
//        RecipeCourseRequest addCoursesRequest = new RecipeCourseRequest.Builder().
//                basedOnResponse(onErrorResponse).
//                setDomainModel(
//                        new RecipeCourseRequest.DomainModel.Builder().
//                                setCourseList(coursesToAdd).
//                                build()
//                ).
//                build();
//
//        // Act
//        SUT.execute(addCoursesRequest, new CourseCallbackClient());
//        // Assert
//        verify(repoCourseMock, times(numberOfAddedCourses)).save(capturedPersistentModel.capture());
//
//        // Assert models saved are equal in content to expected models
//        List<RecipeCoursePersistenceModelItem> actualCoursePersistentModels =
//                capturedPersistentModel.getAllValues();
//
//        assertEquals(
//                expectedModels,
//                actualCoursePersistentModels
//        );
//
//        // Assert state
//        assertEquals(
//                ComponentState.VALID_CHANGED,
//                onSuccessResponse.getMetadata().getComponentState()
//        );
    }

    @Test
    public void existingRequest_persistenceCalledWithCorrectId() {
//        // Arrange
//        String domainId = RECIPE_METADATA_VALID_UNCHANGED.getDomainId();
//
//        RecipeCourseRequest initialisationRequest = new RecipeCourseRequest.Builder().
//                getDefault().
//                setDomainId(domainId).
//                build();
//        // Act
//        handler.executeAsync(SUT, initialisationRequest, new CourseCallbackClient());
//        // Assert
//        verify(repoCourseMock).getAllActiveByDomainId(eq(domainId), repoCourseCallback.capture());
    }

    @Test
    public void existingRequest_completeListOfModelsReturned_VALID_UNCHANGED() {
//        // Arrange
//        String domainId = RECIPE_METADATA_VALID_UNCHANGED.getDomainId();
//
//        RecipeCourseRequest initialisationRequest = new RecipeCourseRequest.Builder().
//                getDefault().
//                setDomainId(domainId).
//                build();
//        // Act
//        handler.executeAsync(SUT, initialisationRequest, new CourseCallbackClient());
//        // Assert
//        verifyRepoCalledAndReturnMatchingCourses(domainId);
//
//        int expectedNumberOfModels = TestDataRecipeCourse.
//                getAllExistingActiveByDomainId(domainId).size();
//        int actualNumberOfModels = onSuccessResponse.
//                getDomainModel().
//                getCourseList().
//                size();
//
//        assertEquals(expectedNumberOfModels, actualNumberOfModels);
//        // No data has been modified, just returned
//        assertEquals(ComponentState.VALID_UNCHANGED, onSuccessResponse.getMetadata().getComponentState());
//        assertTrue(onSuccessResponse.getMetadata().getFailReasons().
//                contains(CommonFailReason.NONE));
    }

    @Test
    public void existingRequest_allModelsDeleted_INVALID_CHANGED() {
//        // Arrange - initialisation request load and return data
//        String domainId = RECIPE_METADATA_VALID_UNCHANGED.getDomainId();
//
//        RecipeCourseRequest initialisationRequest = new RecipeCourseRequest.Builder().
//                getDefault().
//                setDomainId(domainId).
//                build();
//        // Act
//        handler.executeAsync(SUT, initialisationRequest, new CourseCallbackClient());
//        // Assert
//        verifyRepoCalledAndReturnMatchingCourses(domainId);
//
//        // Assert - verify all courses returned
//        int expectedNoOfCourses = TestDataRecipeCourse.
//                getAllExistingActiveByDomainId(domainId).size();
//        int actualNoOfCourses = onSuccessResponse.
//                getDomainModel().
//                getCourseList().
//                size();
//        assertEquals(expectedNoOfCourses, actualNoOfCourses);
//
//        // Arrange - second request, with no courses:
//        RecipeCourseRequest requestWitNoCourses = new RecipeCourseRequest.Builder().
//                getDefault().
//                setDomainId(domainId).
//                build();
//        // Act
//        handler.executeAsync(SUT, requestWitNoCourses, new CourseCallbackClient());
//        // Assert expected sate after courses removed
//        assertEquals(ComponentState.INVALID_CHANGED, onErrorResponse.
//                getMetadata().
//                getComponentState());
//        assertTrue(onErrorResponse.
//                getMetadata().
//                getFailReasons().
//                contains(CommonFailReason.DATA_UNAVAILABLE));
//        assertTrue(onErrorResponse.
//                getDomainModel().
//                getCourseList().
//                isEmpty());
    }

    // region helper methods -----------------------------------------------------------------------
    private void verifyRepoCalledAndReturnMatchingCourses(String recipeDomainId) {
//        // Confirm repo called and capture the callback
//        verify(repoCourseMock).getActiveByDomainId(eq(recipeDomainId), repoCourseCallback.capture());
//        // Find matching values in the test data and return  results
//        List<RecipeCoursePersistenceModelItem> persistenceModelItems = new ArrayList<>(
//                TestDataRecipeCourse.getAllExistingActiveByDomainId(recipeDomainId));
//
//        long createDate = persistenceModelItems.isEmpty() ? 0L : Long.MAX_VALUE;
//        long lastUpdate = 0L;
//
//        for (RecipeCoursePersistenceModelItem item : persistenceModelItems) {
//            createDate = Math.min(createDate, item.getCreateDate());
//            lastUpdate = Math.max(lastUpdate, item.getLastUpdate());
//        }
//
//        RecipeCoursePersistenceModel model = new RecipeCoursePersistenceModel.Builder().
//                getDefault().
//                setDomainId(recipeDomainId).
//                setCourses(new HashSet<>(persistenceModelItems)).
//                setCreateDate(createDate).
//                setLastUpdate(lastUpdate).
//                build();
//
//        if (persistenceModelItems.size() > 0) {
//            System.out.println(TAG + persistenceModelItems.size() + " courses returned from test data.");
//            repoCourseCallback.getValue().onDomainModelLoaded(model);
//        } else {
//            System.out.println(TAG + "no courses were returned from test data.");
//            repoCourseCallback.getValue().onDomainModelUnavailable();
//        }
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