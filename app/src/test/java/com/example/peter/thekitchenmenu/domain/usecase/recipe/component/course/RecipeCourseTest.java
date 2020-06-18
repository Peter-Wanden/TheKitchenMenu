package com.example.peter.thekitchenmenu.domain.usecase.recipe.component.course;

import com.example.peter.thekitchenmenu.data.repository.DomainDataAccess.GetDomainModelCallback;
import com.example.peter.thekitchenmenu.data.repository.recipe.RepositoryRecipeCourse;
import com.example.peter.thekitchenmenu.data.repository.recipe.course.TestDataRecipeCourse;
import com.example.peter.thekitchenmenu.domain.model.UseCaseMetadataModel;
import com.example.peter.thekitchenmenu.domain.usecase.common.UseCaseBase;
import com.example.peter.thekitchenmenu.domain.usecase.common.failreasons.CommonFailReason;
import com.example.peter.thekitchenmenu.domain.usecase.common.failreasons.FailReasons;
import com.example.peter.thekitchenmenu.domain.usecase.recipe.component.course.RecipeCourse.Course;
import com.example.peter.thekitchenmenu.domain.usecase.recipe.component.course.RecipeCourse.FailReason;
import com.example.peter.thekitchenmenu.domain.utils.TimeProvider;
import com.example.peter.thekitchenmenu.domain.utils.UniqueIdProvider;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static com.example.peter.thekitchenmenu.domain.usecase.recipe.component.metadata.RecipeMetadata.ComponentState;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class RecipeCourseTest {

    private static final String TAG = "tkm-" + RecipeCourseTest.class.getSimpleName() + ": ";

    // region constants ----------------------------------------------------------------------------
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
        List<Course> expectedCourseList = new ArrayList<>();
        List<Course> actualCourseList = onErrorResponse.getDomainModel().getCourses();
        assertEquals(
                expectedCourseList,
                actualCourseList
        );
    }

    @Test
    public void newRequest_addCourseOne_VALID_CHANGED() {
        // Arrange
        // execute and test a new empty request that returns DEFAULT_NO_COURSES
        newRequest_defaultCourseListReturned_stateINVALID_UNCHANGED();

        // Arrange persistent model for archiving DEFAULT_NO_COURSES state
        RecipeCoursePersistenceModel expectedArchivedDefaultNoCoursesModel = TestDataRecipeCourse.
                getNewArchivedDefaultNoCourses();
        // Arrange persistent model that represents state after adding course one
        RecipeCoursePersistenceModel expectedCourseOneSaveModel = TestDataRecipeCourse.
                getNewActiveCourseOne();

        // Arrange request to add course one
        RecipeCourseRequest.DomainModel model = new RecipeCourseRequest.DomainModel.Builder().
                setCourseList(expectedCourseOneSaveModel.getCourses()).
                build();
        RecipeCourseRequest addCourseOneRequest = new RecipeCourseRequest.Builder().
                basedOnResponse(onErrorResponse).
                setDomainModel(model).
                build();

        // new times for archive and new course model save
        when(timeProviderMock.getCurrentTimeInMills()).thenReturn(
                expectedArchivedDefaultNoCoursesModel.getLastUpdate()
        );
        // data id for course one persistence model new state
        when(idProviderMock.getUId()).thenReturn(
                expectedCourseOneSaveModel.getDataId()
        );

        // Act
        SUT.execute(addCourseOneRequest, new CourseCallbackClient());

        // Assert old course archived
        verify(repoCourseMock).save(eq(expectedArchivedDefaultNoCoursesModel));
        // Assert new persistence model saved
        verify(repoCourseMock).save(eq(expectedCourseOneSaveModel));

        // Assert correct response values
        RecipeCourseResponse response = onSuccessResponse;
        UseCaseMetadataModel metadata = response.getMetadata();

        // Assert state
        ComponentState expectedState = ComponentState.VALID_CHANGED;
        ComponentState actualState = metadata.getComponentState();
        assertEquals(
                expectedState,
                actualState
        );

        // Assert fail reasons
        List<FailReasons> expectedFailReasons = Collections.singletonList(CommonFailReason.NONE);
        List<FailReasons> actualFailReasons = metadata.getFailReasons();
        assertEquals(
                expectedFailReasons,
                actualFailReasons
        );
    }

    @Test
    public void newRequest_addCourseOneAndTwo_VALID_CHANGED() {
        // Arrange
        // execute and test a new empty request that returns DEFAULT_NO_COURSES, then adds a course
        newRequest_addCourseOne_VALID_CHANGED();

        // Arrange persistent model for archiving the addCourseOne_VALID_CHANGED state
        RecipeCoursePersistenceModel expectedArchivedCourseOne = TestDataRecipeCourse.
                getNewArchivedCourseOne();

        // Arrange persistent model that represents state after adding course two
        RecipeCoursePersistenceModel expectedCourseOneAndTwoSaveModel = TestDataRecipeCourse.
                getNewActiveCourseOneAndTwo();

        // Arrange request to add course two
        RecipeCourseRequest.DomainModel model = new RecipeCourseRequest.DomainModel.Builder().
                setCourseList(expectedCourseOneAndTwoSaveModel.getCourses()).
                build();
        RecipeCourseRequest addCourseTwoRequest = new RecipeCourseRequest.Builder().
                basedOnResponse(onSuccessResponse).
                setDomainModel(model).
                build();

        // new times for archive last update and new course model save
        when(timeProviderMock.getCurrentTimeInMills()).thenReturn(
                expectedCourseOneAndTwoSaveModel.getLastUpdate()
        );
        // data id for persistence model new state
        when(idProviderMock.getUId()).thenReturn(
                expectedCourseOneAndTwoSaveModel.getDataId()
        );

        // Act
        SUT.execute(addCourseTwoRequest, new CourseCallbackClient());

        // Assert
        // Assert old course archived
        verify(repoCourseMock).save(eq(expectedArchivedCourseOne));
        // Assert new persistence model saved
        verify(repoCourseMock).save(eq(expectedCourseOneAndTwoSaveModel));

        // Assert correct response values
        RecipeCourseResponse response = onSuccessResponse;
        UseCaseMetadataModel metadata = response.getMetadata();

        // Assert state
        ComponentState expectedState = ComponentState.VALID_CHANGED;
        ComponentState actualState = metadata.getComponentState();
        assertEquals(
                expectedState,
                actualState
        );

        // Assert fail reasons
        List<FailReasons> expectedFailReasons = Collections.singletonList(CommonFailReason.NONE);
        List<FailReasons> actualFailReasons = metadata.getFailReasons();
        assertEquals(
                expectedFailReasons,
                actualFailReasons
        );
    }

    @Test
    public void newRequest_removeCourseOne_VALID_CHANGED() {
        // Arrange
        // execute and test a new empty request that returns VALID CHANGED, then adds two courses
        newRequest_addCourseOneAndTwo_VALID_CHANGED();

        // Arrange persistent model for archiving the addCourseOneAndTwo_VALID_CHANGED state
        RecipeCoursePersistenceModel expectedArchivedModelForCourseOneAndTwo = TestDataRecipeCourse.
                getNewArchivedCourseOneAndTwo();

        // Arrange persistent model that represents state after removing course one
        RecipeCoursePersistenceModel expectedActiveModelAfterCourseOneRemoved = TestDataRecipeCourse.
                getNewActiveAfterCourseOneRemoved();

        // Arrange request to remove course one
        RecipeCourseRequest.DomainModel model = new RecipeCourseRequest.DomainModel.Builder().
                setCourseList(expectedActiveModelAfterCourseOneRemoved.getCourses()).
                build();
        RecipeCourseRequest removeCourseOnRequest = new RecipeCourseRequest.Builder().
                basedOnResponse(onSuccessResponse).
                setDomainModel(model).
                build();

        // new times for archive last update and new course model save
        when(timeProviderMock.getCurrentTimeInMills()).thenReturn(
                expectedActiveModelAfterCourseOneRemoved.getLastUpdate()
        );
        // data id for persistence model new state
        when(idProviderMock.getUId()).thenReturn(
                expectedActiveModelAfterCourseOneRemoved.getDataId()
        );

        // Act
        SUT.execute(removeCourseOnRequest, new CourseCallbackClient());

        // Assert
        // Assert old course archived
        verify(repoCourseMock).save(eq(expectedArchivedModelForCourseOneAndTwo));
        // Assert new persistence model saved
        verify(repoCourseMock).save(eq(expectedActiveModelAfterCourseOneRemoved));

        // Assert correct metadata response values
        RecipeCourseResponse response = onSuccessResponse;
        UseCaseMetadataModel metadata = response.getMetadata();

        // Assert state
        ComponentState expectedState = ComponentState.VALID_CHANGED;
        ComponentState actualState = metadata.getComponentState();
        assertEquals(
                expectedState,
                actualState
        );

        // Assert fail reasons
        List<FailReasons> expectedFailReasons = Collections.singletonList(CommonFailReason.NONE);
        List<FailReasons> actualFailReasons = metadata.getFailReasons();
        assertEquals(
                expectedFailReasons,
                actualFailReasons
        );
    }

    @Test
    public void newRequest_removeAllCourses_INVALID_CHANGED() {
        // Arrange
        // execute and test a new empty request that adds two courses, then removes one
        newRequest_removeCourseOne_VALID_CHANGED();

        // Arrange persistent model for archiving the removeCourseOne_VALID_CHANGED state
        RecipeCoursePersistenceModel expectedArchivedModelForCourseTwo = TestDataRecipeCourse.
                getNewArchivedAfterCourseOneRemoved(); // only course two remaining in the archived model

        // Arrange persistent model that represents state after removing course two
        RecipeCoursePersistenceModel expectedActiveModelAfterAllCoursesRemoved =
                TestDataRecipeCourse.
                        getNewActiveCourseDefaultAfterAllCoursesRemoved(); // no courses left

        // Arrange request to remove course two
        RecipeCourseRequest.DomainModel model = new RecipeCourseRequest.DomainModel.Builder().
                setCourseList(new ArrayList<>()).
                build();
        RecipeCourseRequest removeCourseOnRequest = new RecipeCourseRequest.Builder().
                basedOnResponse(onSuccessResponse).
                setDomainModel(model).
                build();

        // new times for archive last update and new course model save
        when(timeProviderMock.getCurrentTimeInMills()).thenReturn(
                expectedActiveModelAfterAllCoursesRemoved.getLastUpdate()
        );
        // data id for persistence model new state
        when(idProviderMock.getUId()).thenReturn(
                expectedActiveModelAfterAllCoursesRemoved.getDataId()
        );

        // Act
        SUT.execute(removeCourseOnRequest, new CourseCallbackClient());

        // Assert
        // Assert old course archived
        verify(repoCourseMock).save(eq(expectedArchivedModelForCourseTwo));
        // Assert new persistence model saved
        verify(repoCourseMock).save(eq(expectedActiveModelAfterAllCoursesRemoved));

        // Assert correct metadata response values
        RecipeCourseResponse response = onErrorResponse;
        UseCaseMetadataModel metadata = response.getMetadata();

        // Assert state
        ComponentState expectedState = ComponentState.INVALID_CHANGED;
        ComponentState actualState = metadata.getComponentState();
        assertEquals(
                expectedState,
                actualState
        );

        // Assert fail reasons
        List<FailReasons> expectedFailReasons = Arrays.asList(
                FailReason.NO_COURSE_SELECTED,
                CommonFailReason.DATA_UNAVAILABLE
        );
        List<FailReasons> actualFailReasons = metadata.getFailReasons();
        assertEquals(
                expectedFailReasons,
                actualFailReasons
        );
    }

    @Test
    public void existingRequest_completeListOfModelsReturned_VALID_UNCHANGED() {
        // Arrange
        // arrange persistence model representing all courses
        RecipeCoursePersistenceModel expectedAllCoursesModel = TestDataRecipeCourse.
                getExistingActiveWithAllCourses();

        // arrange request to get all courses
        RecipeCourseRequest request = new RecipeCourseRequest.Builder().
                getDefault().
                setDomainId(expectedAllCoursesModel.getDomainId()).
                build();

        // Act
        SUT.execute(request, new CourseCallbackClient());

        // Assert
        verify(repoCourseMock).getActiveByDomainId(eq(expectedAllCoursesModel.getDomainId()),
                repoCourseCallback.capture());
        repoCourseCallback.getValue().onDomainModelLoaded(expectedAllCoursesModel);

        RecipeCourseResponse.Model responseModel = onSuccessResponse.getDomainModel();
        UseCaseMetadataModel metadata = onSuccessResponse.getMetadata();

        int expectedNumberOfModels = expectedAllCoursesModel.getCourses().size();
        int actualNumberOfModels = responseModel.getCourses().size();
        assertEquals(
                expectedNumberOfModels,
                actualNumberOfModels
        );

        assertEquals(
                ComponentState.VALID_UNCHANGED,
                metadata.getComponentState()
        );

        // assert fail reasons
        List<FailReasons> expectedFailReasons = Collections.singletonList(CommonFailReason.NONE);
        List<FailReasons> actualFailReasons = metadata.getFailReasons();
        assertEquals(
                expectedFailReasons,
                actualFailReasons
        );
    }

    @Test
    public void existingRequest_allModelsDeleted_INVALID_CHANGED() {
        // Arrange
        // arrange persistence model representing all courses
        RecipeCoursePersistenceModel expectedAllCoursesModel = TestDataRecipeCourse.
                getExistingActiveWithAllCourses();

        // arrange persistence model representing all courses persistent model archived
        RecipeCoursePersistenceModel expectedArchivedModel = TestDataRecipeCourse.
                getExistingArchivedWithAllCourses();

        // arrange persistent model representing new state after courses removed
        RecipeCoursePersistenceModel expectedNewStateAfterCoursesRemovedModel =
                TestDataRecipeCourse.getExistingActiveAfterAllCoursesRemoved();

        // arrange test for testing the return of all courses
        existingRequest_completeListOfModelsReturned_VALID_UNCHANGED();

        // arrange a request for removing all courses
        RecipeCourseRequest request = new RecipeCourseRequest.Builder().
                setDataId(expectedAllCoursesModel.getDataId()).
                setDomainId(expectedAllCoursesModel.getDomainId()).
                setDomainModel(
                        new RecipeCourseRequest.DomainModel.Builder().
                                getDefault().
                                build()).
                build();

        when(timeProviderMock.getCurrentTimeInMills()).
                thenReturn(expectedArchivedModel.getLastUpdate());
        when(idProviderMock.getUId()).
                thenReturn(expectedNewStateAfterCoursesRemovedModel.getDataId());

        // Act
        SUT.execute(request, new CourseCallbackClient());

        // Assert
        // assert model correctly archived
        verify(repoCourseMock).save(eq(expectedArchivedModel));

        // assert new state saved
        verify(repoCourseMock).save(eq(expectedNewStateAfterCoursesRemovedModel));

        // assert response data
        RecipeCourseResponse.Model model = onErrorResponse.getDomainModel();
        List<Course> expectedCourses = new ArrayList<>();
        List<Course> actualCourses = model.getCourses();
        assertEquals(
                expectedCourses,
                actualCourses
        );

        // Assert metadata
        UseCaseMetadataModel metadata = onErrorResponse.getMetadata();

        ComponentState expectedState = ComponentState.INVALID_CHANGED;
        ComponentState actualState = metadata.getComponentState();
        assertEquals(
                expectedState,
                actualState
        );

        List<FailReasons> expectedFailReasons = Arrays.asList(
                FailReason.NO_COURSE_SELECTED,
                CommonFailReason.DATA_UNAVAILABLE);
        List<FailReasons> actualFailReasons = metadata.getFailReasons();
        assertEquals(
                expectedFailReasons,
                actualFailReasons
        );

    }

    // region helper methods -----------------------------------------------------------------------
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