package com.example.peter.thekitchenmenu.domain.usecase.recipe.component.course;

import com.example.peter.thekitchenmenu.data.repository.DomainDataAccess.GetDomainModelCallback;
import com.example.peter.thekitchenmenu.data.repository.recipe.RecipeCourseUseCaseDataAccess;
import com.example.peter.thekitchenmenu.data.repository.recipe.course.TestDataRecipeCourse;
import com.example.peter.thekitchenmenu.domain.usecasenew.common.metadata.UseCaseMetadataModel;
import com.example.peter.thekitchenmenu.domain.usecase.common.UseCaseBase;
import com.example.peter.thekitchenmenu.domain.usecasenew.common.failreasons.CommonFailReason;
import com.example.peter.thekitchenmenu.domain.usecasenew.common.failreasons.FailReasons;
import com.example.peter.thekitchenmenu.domain.usecasenew.recipe.component.course.Course;
import com.example.peter.thekitchenmenu.domain.usecasenew.recipe.component.course.RecipeCourseUseCaseFailReason;
import com.example.peter.thekitchenmenu.domain.usecasenew.recipe.component.course.RecipeCourseUseCasePersistenceModel;
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

import com.example.peter.thekitchenmenu.domain.usecasenew.common.metadata.ComponentState;
import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

public class RecipeCourseTest {

    private static final String TAG = "tkm-" + RecipeCourseTest.class.getSimpleName() + ": ";

    // region constants ----------------------------------------------------------------------------
    // endregion constants -------------------------------------------------------------------------

    // region helper fields ------------------------------------------------------------------------
    @Mock
    RecipeCourseUseCaseDataAccess repoCourseMock;
    @Captor
    ArgumentCaptor<GetDomainModelCallback<RecipeCourseUseCasePersistenceModel>> repoCourseCallback;
    @Mock
    UniqueIdProvider idProviderMock;
    @Mock
    TimeProvider timeProviderMock;

    private RecipeCourseResponse courseOnSuccessResponse;
    private RecipeCourseResponse courseOnErrorResponse;
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
    public void newRequest_defaultNoCoursesReturned_stateINVALID_DEFAULT() {
        // Arrange
        RecipeCourseUseCasePersistenceModel expectedDefaultValues = TestDataRecipeCourse.
                getNewActiveDefaultNoCourses();

        RecipeCourseRequest request = new RecipeCourseRequest.Builder().
                getDefault().
                setDomainId(expectedDefaultValues.getDomainId()).
                build();

        // Act
        SUT.execute(request, new CourseCallbackClient());

        // Assert
        // Assert persistence calls
        verify(repoCourseMock).getByDomainId(eq(expectedDefaultValues.getDomainId()),
                repoCourseCallback.capture());
        repoCourseCallback.getValue().onPersistenceModelUnavailable();

        // assert no save
        verifyNoMoreInteractions(repoCourseMock);

        // Assert response values
        RecipeCourseResponse response = courseOnErrorResponse;

        // assert correct id's
        String expectedDataId = "";
        assertEquals(
                expectedDataId,
                response.getDataId()
        );
        assertEquals(
                expectedDefaultValues.getDomainId(),
                response.getDomainId()
        );

        // assert response metadata
        UseCaseMetadataModel metadata = courseOnErrorResponse.getMetadata();

        ComponentState expectedState = ComponentState.INVALID_DEFAULT;
        ComponentState actualComponentState = metadata.getComponentState();
        assertEquals(
                expectedState,
                actualComponentState
        );

        // assert fail reasons
        List<FailReasons> expectedFailReasons = Arrays.asList(
                RecipeCourseUseCaseFailReason.NO_COURSE_SELECTED,
                CommonFailReason.DATA_UNAVAILABLE
        );
        List<FailReasons> actualFailReasons = metadata.getFailReasons();

        assertEquals(
                expectedFailReasons,
                actualFailReasons
        );

        // Assert course list is empty
        List<Course> expectedCourseList = new ArrayList<>();
        List<Course> actualCourseList = courseOnErrorResponse.getDomainModel().getCourses();
        assertEquals(
                expectedCourseList,
                actualCourseList
        );
    }

    @Test
    public void newRequest_addCourseOne_VALID_CHANGED() {
        // Arrange
        // execute and test a new domain Id only request that returns default state
        newRequest_defaultNoCoursesReturned_stateINVALID_DEFAULT();

        // Arrange persistent model that represents state after adding domain data
        RecipeCourseUseCasePersistenceModel expectedCourseOneSaveModel = TestDataRecipeCourse.
                getNewActiveCourseOne();

        // Arrange request to add course one
        RecipeCourseRequest.DomainModel model = new RecipeCourseRequest.DomainModel.Builder().
                setCourseList(expectedCourseOneSaveModel.getCourses()).
                build();
        RecipeCourseRequest addCourseOneRequest = new RecipeCourseRequest.Builder().
                basedOnResponse(courseOnErrorResponse).
                setDomainModel(model).
                build();

        // new times for new course model save
        when(timeProviderMock.getCurrentTimeInMills()).thenReturn(
                expectedCourseOneSaveModel.getCreateDate()
        );
        // data id for course one persistence model new state
        when(idProviderMock.getUId()).thenReturn(
                expectedCourseOneSaveModel.getDataId()
        );

        // Act
        SUT.execute(addCourseOneRequest, new CourseCallbackClient());

        // Assert new persistence model saved
        verify(repoCourseMock).save(eq(expectedCourseOneSaveModel));

        // Assert correct response values
        RecipeCourseResponse response = courseOnSuccessResponse;
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
        RecipeCourseUseCasePersistenceModel expectedArchivedCourseOne = TestDataRecipeCourse.
                getNewArchivedCourseOne();

        // Arrange persistent model that represents state after adding course two
        RecipeCourseUseCasePersistenceModel expectedCourseOneAndTwoSaveModel = TestDataRecipeCourse.
                getNewActiveCourseOneAndTwo();

        // Arrange request to add course two
        RecipeCourseRequest.DomainModel model = new RecipeCourseRequest.DomainModel.Builder().
                setCourseList(expectedCourseOneAndTwoSaveModel.getCourses()).
                build();
        RecipeCourseRequest addCourseTwoRequest = new RecipeCourseRequest.Builder().
                basedOnResponse(courseOnSuccessResponse).
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
        RecipeCourseResponse response = courseOnSuccessResponse;
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
        RecipeCourseUseCasePersistenceModel expectedArchivedModelForCourseOneAndTwo = TestDataRecipeCourse.
                getNewArchivedCourseOneAndTwo();

        // Arrange persistent model that represents state after removing course one
        RecipeCourseUseCasePersistenceModel expectedActiveModelAfterCourseOneRemoved = TestDataRecipeCourse.
                getNewActiveAfterCourseOneRemoved();

        // Arrange request to remove course one
        RecipeCourseRequest.DomainModel model = new RecipeCourseRequest.DomainModel.Builder().
                setCourseList(expectedActiveModelAfterCourseOneRemoved.getCourses()).
                build();
        RecipeCourseRequest removeCourseOnRequest = new RecipeCourseRequest.Builder().
                basedOnResponse(courseOnSuccessResponse).
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
        RecipeCourseResponse response = courseOnSuccessResponse;
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
    public void newRequest_removeAllCourses_INVALID_DEFAULT() {
        // Arrange
        // execute and test a new empty request that adds two courses, then removes one
        newRequest_removeCourseOne_VALID_CHANGED();

        // Arrange request to remove course two
        RecipeCourseRequest.DomainModel model = new RecipeCourseRequest.DomainModel.Builder().
                setCourseList(new ArrayList<>()).
                build();
        RecipeCourseRequest removeCourseOnRequest = new RecipeCourseRequest.Builder().
                basedOnResponse(courseOnSuccessResponse).
                setDomainModel(model).
                build();

        // Act
        SUT.execute(removeCourseOnRequest, new CourseCallbackClient());

        // Assert
        // assert old state not archived (as new state is invalid as no data(no courses))
        verifyNoMoreInteractions(repoCourseMock);

        // Assert correct metadata response values
        RecipeCourseResponse response = courseOnErrorResponse;
        UseCaseMetadataModel metadata = response.getMetadata();

        // Assert state
        ComponentState expectedState = ComponentState.INVALID_DEFAULT;
        ComponentState actualState = metadata.getComponentState();
        assertEquals(
                expectedState,
                actualState
        );

        // Assert fail reasons
        List<FailReasons> expectedFailReasons = Collections.singletonList(
                RecipeCourseUseCaseFailReason.NO_COURSE_SELECTED
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
        RecipeCourseUseCasePersistenceModel expectedAllCoursesModel = TestDataRecipeCourse.
                getExistingActiveWithAllCourses();

        // arrange request to get all courses
        RecipeCourseRequest request = new RecipeCourseRequest.Builder().
                getDefault().
                setDomainId(expectedAllCoursesModel.getDomainId()).
                build();

        // Act
        SUT.execute(request, new CourseCallbackClient());

        // Assert
        verify(repoCourseMock).getByDomainId(eq(expectedAllCoursesModel.getDomainId()),
                repoCourseCallback.capture());
        repoCourseCallback.getValue().onPersistenceModelLoaded(expectedAllCoursesModel);

        RecipeCourseResponse.DomainModel responseDomainModel = courseOnSuccessResponse.getDomainModel();
        UseCaseMetadataModel metadata = courseOnSuccessResponse.getMetadata();

        int expectedNumberOfModels = expectedAllCoursesModel.getCourses().size();
        int actualNumberOfModels = responseDomainModel.getCourses().size();
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
    public void existingRequest_allModelsDeleted_INVALID_DEFAULT() {
        // Arrange
        // arrange test for testing the return of all courses
        existingRequest_completeListOfModelsReturned_VALID_UNCHANGED();

        // arrange a request for removing all courses
        RecipeCourseRequest request = new RecipeCourseRequest.Builder().
                basedOnResponse(courseOnSuccessResponse).
                setDomainModel(
                        new RecipeCourseRequest.DomainModel.Builder().
                                getDefault().
                                build()).
                build();

        // Act
        SUT.execute(request, new CourseCallbackClient());

        // Assert
        // assert no data saved (as new state is invalid)
        verifyNoMoreInteractions(repoCourseMock);

        // assert response data
        RecipeCourseResponse.DomainModel domainModel = courseOnErrorResponse.getDomainModel();
        List<Course> expectedCourses = new ArrayList<>();
        List<Course> actualCourses = domainModel.getCourses();
        assertEquals(
                expectedCourses,
                actualCourses
        );

        // Assert metadata
        UseCaseMetadataModel metadata = courseOnErrorResponse.getMetadata();

        ComponentState expectedState = ComponentState.INVALID_DEFAULT;
        ComponentState actualState = metadata.getComponentState();
        assertEquals(
                expectedState,
                actualState
        );

        List<FailReasons> expectedFailReasons = Collections.singletonList(
                RecipeCourseUseCaseFailReason.NO_COURSE_SELECTED
        );
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
            courseOnSuccessResponse = response;
        }

        @Override
        public void onUseCaseError(RecipeCourseResponse response) {
            System.out.println(TAG + "onError: " + response);
            courseOnErrorResponse = response;
        }
    }
    // endregion helper classes --------------------------------------------------------------------
}