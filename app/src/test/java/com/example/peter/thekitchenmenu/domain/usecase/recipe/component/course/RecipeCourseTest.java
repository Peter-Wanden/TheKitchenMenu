package com.example.peter.thekitchenmenu.domain.usecase.recipe.component.course;

import com.example.peter.thekitchenmenu.commonmocks.UseCaseSchedulerMock;
import com.example.peter.thekitchenmenu.data.repository.DomainDataAccess.GetAllDomainModelsCallback;
import com.example.peter.thekitchenmenu.data.repository.recipe.course.TestDataRecipeCoursePersistenceModel;
import com.example.peter.thekitchenmenu.data.repository.recipe.metadata.TestDataRecipeMetadata;
import com.example.peter.thekitchenmenu.data.repository.recipe.RepositoryRecipeCourse;
import com.example.peter.thekitchenmenu.domain.model.CommonFailReason;
import com.example.peter.thekitchenmenu.domain.usecase.UseCaseHandler;
import com.example.peter.thekitchenmenu.domain.usecase.UseCase;
import com.example.peter.thekitchenmenu.domain.usecase.recipe.metadata.RecipeMetadataPersistenceModel;
import com.example.peter.thekitchenmenu.domain.utils.TimeProvider;
import com.example.peter.thekitchenmenu.domain.utils.UniqueIdProvider;

import org.junit.*;
import org.mockito.*;

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
        handler.execute(SUT, request, getCallback());
        // Assert
        verifyRepoCalledAndReturnMatchingCourses(request.getDomainId());
        assertEquals(ComponentState.INVALID_UNCHANGED, onErrorResponse.getMetadata().getState());
        assertTrue(onErrorResponse.getMetadata().getFailReasons().
                contains(CommonFailReason.DATA_UNAVAILABLE));
        assertEquals(0, onErrorResponse.getModel().getCourseList().size());
    }

    @Test
    public void newRequest_idWithNoCourses_thenAddCourse_VALID_CHANGED() {
        // Arrange
        String dataId = "testId";
        long expectedDateTime = 10L;
        int expectedCourseListSize = 0;
        whenTimeProviderReturnTime(expectedDateTime);
        when(idProviderMock.getUId()).thenReturn(dataId);

        RecipeCourseRequest initialRequest = new RecipeCourseRequest.Builder().getDefault().
                setDomainId("idNotInTestData").
                build();

        // Act
        handler.execute(SUT, initialRequest, getCallback());
        // Assert
        verifyRepoCalledAndReturnMatchingCourses(initialRequest.getDomainId());
        // Assert component state
        assertEquals(ComponentState.INVALID_UNCHANGED, onErrorResponse.getMetadata().getState());
        // Assert course list
        assertEquals(expectedCourseListSize, onErrorResponse.getModel().getCourseList().size());

        // Arrange - add new course
        RecipeCourse.Course addedCourse = Course.COURSE_ONE;
        List<Course> list = Collections.singletonList(addedCourse);

        RecipeCourseRequest r = new RecipeCourseRequest.Builder().
                basedOnResponse(onErrorResponse).
                setModel(new RecipeCourseRequest.Model.Builder().
                        setCourseList(list).
                        build()
                ).
                build();

        System.out.println(TAG + r);

        // Act
        handler.execute(SUT, r, getCallback());
        // Assert
        verify(repoCourseMock).save(capturedPersistentModel.capture());
        RecipeCoursePersistenceModel savedModel = capturedPersistentModel.getValue();

        assertEquals(addedCourse, savedModel.getCourse());
        assertEquals(expectedDateTime, savedModel.getCreateDate());
        assertEquals(dataId, savedModel.getDataId());
        assertEquals(ComponentState.VALID_CHANGED, onSuccessResponse.getMetadata().getState());
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
        verify(repoCourseMock).getAllByDomainId(eq(domainId), repoCourseCallback.capture());
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
                getAllByDomainId(domainId).size();
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
                getAllByDomainId(domainId).size();
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
        verify(repoCourseMock).getAllByDomainId(eq(recipeId), repoCourseCallback.capture());
        // Find the matching values in the test data and return the callback with results
        List<RecipeCoursePersistenceModel> courses = TestDataRecipeCoursePersistenceModel.
                getAllByDomainId(recipeId);
        if (courses.size() > 0) {
            System.out.println(TAG + "there are " + courses.size() + "courses returned from test data.");
            repoCourseCallback.getValue().onAllLoaded(courses);
        } else {
            System.out.println(TAG + "no courses were returned from test data.");
            repoCourseCallback.getValue().onModelsUnavailable();
        }
    }

    private void whenTimeProviderReturnTime(long time) {
        when(timeProviderMock.getCurrentTimeInMills()).thenReturn(time);
    }
    // endregion helper methods --------------------------------------------------------------------

    // region helper classes -----------------------------------------------------------------------
    // endregion helper classes --------------------------------------------------------------------
}