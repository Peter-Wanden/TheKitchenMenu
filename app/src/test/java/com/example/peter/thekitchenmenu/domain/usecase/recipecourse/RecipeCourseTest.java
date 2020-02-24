package com.example.peter.thekitchenmenu.domain.usecase.recipecourse;

import com.example.peter.thekitchenmenu.commonmocks.UseCaseSchedulerMock;
import com.example.peter.thekitchenmenu.data.entity.RecipeCourseEntity;
import com.example.peter.thekitchenmenu.data.entity.RecipeEntity;
import com.example.peter.thekitchenmenu.data.repository.DataSource;
import com.example.peter.thekitchenmenu.data.repository.RepositoryRecipeCourse;
import com.example.peter.thekitchenmenu.domain.usecase.CommonFailReason;
import com.example.peter.thekitchenmenu.domain.usecase.UseCaseHandler;
import com.example.peter.thekitchenmenu.domain.usecase.UseCase;
import com.example.peter.thekitchenmenu.domain.usecase.recipe.recipecourse.RecipeCourse;
import com.example.peter.thekitchenmenu.domain.usecase.recipe.recipecourse.RecipeCourseRequest;
import com.example.peter.thekitchenmenu.domain.usecase.recipe.recipecourse.RecipeCourseResponse;
import com.example.peter.thekitchenmenu.domain.usecase.recipe.recipestate.RecipeStateCalculator;
import com.example.peter.thekitchenmenu.testdata.TestDataRecipeCourseEntity;
import com.example.peter.thekitchenmenu.testdata.TestDataRecipeEntity;
import com.example.peter.thekitchenmenu.domain.utils.TimeProvider;
import com.example.peter.thekitchenmenu.domain.utils.UniqueIdProvider;

import org.junit.*;
import org.mockito.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static com.example.peter.thekitchenmenu.domain.usecase.recipe.recipe.Recipe.DO_NOT_CLONE;
import static com.example.peter.thekitchenmenu.domain.usecase.recipe.recipestate.RecipeStateCalculator.*;
import static com.example.peter.thekitchenmenu.domain.usecase.recipe.recipecourse.RecipeCourse.*;
import static com.example.peter.thekitchenmenu.testdata.TestDataRecipeCourseEntity.getAllByRecipeId;
import static com.example.peter.thekitchenmenu.testdata.TestDataRecipeEntity.getValidExisting;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.*;
import static org.mockito.Mockito.*;
import static org.junit.Assert.assertEquals;

public class RecipeCourseTest {

    private static final String TAG = "tkm-" + RecipeCourseTest.class.getSimpleName() + ": ";

    // region constants ----------------------------------------------------------------------------
    private String NEW_RECIPE_ID = TestDataRecipeEntity.getNewInvalid().getId();
    private RecipeEntity VALID_EXISTING_RECIPE_ENTITY = getValidExisting();
    private String EXISTING_RECIPE_ID = VALID_EXISTING_RECIPE_ENTITY.getId();
    // endregion constants -------------------------------------------------------------------------

    // region helper fields ------------------------------------------------------------------------
    @Mock
    RepositoryRecipeCourse repoCourseMock;
    @Captor
    ArgumentCaptor<DataSource.GetAllCallback<RecipeCourseEntity>> repoCourseCallback;
    @Mock
    UniqueIdProvider idProviderMock;
    @Mock
    TimeProvider timeProviderMock;
    @Captor
    ArgumentCaptor<RecipeCourseEntity> entityCaptor;

    private UseCaseHandler handler;
    private RecipeCourseRequest request;

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
    public void newRequest_idWithNoCourses_emptyListReturned_DATA_UNAVAILABLE() {
        // Arrange
        RecipeCourseRequest request = RecipeCourseRequest.Builder.
                getDefault().
                setId("idNotInTestData").
                build();
        // Act
        handler.execute(SUT, request, getCallback());
        // Assert
        verifyRepoCalledAndReturnMatchingCourses(request.getId());
        assertEquals(ComponentState.DATA_UNAVAILABLE, onErrorResponse.getState());
        assertEquals(0, onErrorResponse.getCourseList().size());
    }

    @Test
    public void newRequest_idWithNoCourses_thenAddCourse_VALID_CHANGED() {
        // Arrange
        long time = 10L;
        whenTimeProviderReturnTime(time);
        String id = "testId";
        when(idProviderMock.getUId()).thenReturn(id);

        request = getRequest("IdNotInTestData", DO_NOT_CLONE, null, false);
        // Act
        handler.execute(SUT, request, getCallback());
        // Assert
        verifyRepoCalledAndReturnMatchingCourses(request.getId());
        assertEquals(ComponentState.DATA_UNAVAILABLE, onErrorResponse.getState());
        assertEquals(0, onErrorResponse.getCourseList().size());
        // Arrange
        request = getRequest(
                "IdNotInTestData",
                DO_NOT_CLONE,
                Course.COURSE_ONE,
                true);
        // Act
        handler.execute(SUT, request, getCallback());
        // Assert
        verify(repoCourseMock).save(entityCaptor.capture());
        assertEquals(Course.COURSE_ONE.getCourseNo(), entityCaptor.getValue().getCourseNo());
        assertEquals(time, entityCaptor.getValue().getCreateDate());
        assertEquals(id, entityCaptor.getValue().getId());
        assertEquals(RecipeStateCalculator.ComponentState.VALID_CHANGED, onSuccessResponse.getState());
    }

    @Test
    public void existingRequest_persistenceCalledWithCorrectId() {
        // Arrange
        request = getRequest(EXISTING_RECIPE_ID, DO_NOT_CLONE, null, false);
        // Act
        handler.execute(SUT, request, getCallback());
        // Assert
        verify(repoCourseMock).getCoursesForRecipe(eq(EXISTING_RECIPE_ID), repoCourseCallback.capture());
    }

    @Test
    public void existingRequest_completeListOfModelsReturned_VALID_UNCHANGED() {
        // Arrange
        request = getRequest(EXISTING_RECIPE_ID, DO_NOT_CLONE, null, false);
        // Act
        handler.execute(SUT, request, getCallback());
        // Assert
        verifyRepoCalledAndReturnMatchingCourses(request.getId());

        int expectedNumberOfModels = TestDataRecipeCourseEntity.
                getAllByRecipeId(EXISTING_RECIPE_ID).
                size();
        int actualNumberOfModels = onSuccessResponse.
                getCourseList().
                size();

        assertEquals(expectedNumberOfModels, actualNumberOfModels);
        // No data has been modified, just data returned
        assertEquals(ComponentState.VALID_UNCHANGED, onSuccessResponse.getState());
    }

    @Test
    public void existingRequest_allModelsDeleted_INVALID_CHANGED() {
        // Arrange first transaction
        request = getRequest(EXISTING_RECIPE_ID, DO_NOT_CLONE, null, false);
        // Act
        handler.execute(SUT, request, getCallback());
        // Assert
        verifyRepoCalledAndReturnMatchingCourses(request.getId());
        // Arrange
        RecipeCourseResponse recipeCourseResponse = new RecipeCourseResponse.Builder().
                setStatus(onSuccessResponse.getState()).
                setCourseList(new HashMap<>(onSuccessResponse.getCourseList())).
                setFailReasons(new ArrayList<>(onSuccessResponse.getFailReasons())).
                build();
        // Act - remove all component data models
        for (Course course : recipeCourseResponse.getCourseList().keySet()) {
            handler.execute(
                    SUT,
                    RecipeCourseRequest.Builder.
                            getDefault().
                            setId(EXISTING_RECIPE_ID).
                            setCourse(course).
                            setAddCourse(false).
                            build(),
                    getCallback()
            );
        }
        // Assert
        assertEquals(ComponentState.INVALID_CHANGED, onErrorResponse.getState());
        assertTrue(onErrorResponse.getCourseList().isEmpty());
        assertTrue(onErrorResponse.getFailReasons().contains(CommonFailReason.DATA_UNAVAILABLE));
    }

    @Test
    public void cloneRequest_persistenceCalledWithCloneFromId() {
        // Arrange
        request = getRequest(EXISTING_RECIPE_ID, NEW_RECIPE_ID, null, false);
        // Act
        handler.execute(SUT, request, getCallback());
        // Assert
        verify(repoCourseMock).getCoursesForRecipe(eq(EXISTING_RECIPE_ID), anyObject());
    }

    @Test
    public void cloneRequest_dataClonedToNewId_VALID_CHANGED() {
        // Arrange
        when(timeProviderMock.getCurrentTimeInMills()).thenReturn(10L);
        when(idProviderMock.getUId()).thenReturn(NEW_RECIPE_ID);

        request = getRequest(EXISTING_RECIPE_ID, NEW_RECIPE_ID, null, false);
        // Act
        handler.execute(SUT, request, getCallback());
        // Assert
        verifyRepoCalledAndReturnMatchingCourses(request.getId());
        // Confirm the correct number of entities have been cloned
        int expectedNumberOfClonesSaved = TestDataRecipeCourseEntity.
                getAllByRecipeId(EXISTING_RECIPE_ID).
                size();
        verify(repoCourseMock, times(expectedNumberOfClonesSaved)).save(entityCaptor.capture());
        // Confirm all entities have been saved with the cloneToId
        for (RecipeCourseEntity entity : entityCaptor.getAllValues()) {
            assertEquals(NEW_RECIPE_ID, entity.getRecipeId());
        }
        assertEquals(ComponentState.VALID_CHANGED, onSuccessResponse.getState());
    }

    @Test
    public void cloneRequest_whenDeleteCourse_courseDeletedFromCloneToId() {
        // Arrange
        when(timeProviderMock.getCurrentTimeInMills()).thenReturn(10L);
        whenIdProviderReturnMockDatabaseIds();

        request = getRequest(EXISTING_RECIPE_ID, NEW_RECIPE_ID, null, false);
        // Act
        handler.execute(SUT, request, getCallback());
        // Assert
        verifyRepoCalledAndReturnMatchingCourses(request.getId());
        // confirm target is in results
        assertTrue(onSuccessResponse.getCourseList().containsKey(Course.COURSE_ONE));
        // confirm target has correct recipeId
        String expectedRecipeId = onSuccessResponse.getCourseList().
                get(Course.COURSE_ONE).getRecipeId();
        assertEquals(NEW_RECIPE_ID, expectedRecipeId);
        // Arrange request to delete target
        // Get targets database id
        String targetsDataBaseId = onSuccessResponse.getCourseList().
                get(Course.COURSE_ONE).getId();
        // request delete target
        request = getRequest(
                NEW_RECIPE_ID,
                DO_NOT_CLONE,
                Course.COURSE_ONE,
                false);
        // Act
        handler.execute(SUT, request, getCallback());
        // Assert - confirm target deleted from database and list
        verify(repoCourseMock).deleteById(eq(targetsDataBaseId));
        assertNull(onSuccessResponse.getCourseList().get(Course.COURSE_ONE));
        // confirm data has changed
        if (onSuccessResponse.getCourseList().size() > 0) {
            assertEquals(ComponentState.VALID_CHANGED, onSuccessResponse.getState());
        } else {
            assertEquals(ComponentState.INVALID_CHANGED, onSuccessResponse.getState());
        }
    }

    // region helper methods -----------------------------------------------------------------------
    private RecipeCourseRequest getRequest(String recipeId,
                                           String cloneToRecipeId,
                                           Course course,
                                           boolean isAddCourse) {
        return new RecipeCourseRequest.Builder().
                setId(recipeId).
                setCloneToId(cloneToRecipeId).
                setCourse(course).
                setAddCourse(isAddCourse).
                build();
    }

    private UseCase.Callback<RecipeCourseResponse> getCallback() {
        return new UseCase.Callback<RecipeCourseResponse>() {

            @Override
            public void onSuccess(RecipeCourseResponse response) {
                onSuccessResponse = new RecipeCourseResponse.Builder().
                        setStatus(response.getState()).
                        setCourseList(response.getCourseList()).
                        setFailReasons(response.getFailReasons()).
                        build();
            }

            @Override
            public void onError(RecipeCourseResponse response) {
                onErrorResponse = new RecipeCourseResponse.Builder().
                        setStatus(response.getState()).
                        setCourseList(response.getCourseList()).
                        setFailReasons(response.getFailReasons()).
                        build();
            }
        };
    }

    private void verifyRepoCalledAndReturnMatchingCourses(String recipeId) {
        // Confirm repo called and capture the callback
        verify(repoCourseMock).getCoursesForRecipe(eq(recipeId), repoCourseCallback.capture());
        // Find the matching values in the test data and return the callback with results
        List<RecipeCourseEntity> courseEntityList = getAllByRecipeId(recipeId);
        if (courseEntityList.size() > 0) {
            repoCourseCallback.getValue().onAllLoaded(getAllByRecipeId(recipeId));
        } else {
            repoCourseCallback.getValue().onDataNotAvailable();
        }
    }

    private void whenIdProviderReturnMockDatabaseIds() {
        when(idProviderMock.getUId()).thenReturn("1", "2", "3", "4", "5", "6", "7", "8");
    }

    private void whenTimeProviderReturnTime(long time) {
        when(timeProviderMock.getCurrentTimeInMills()).thenReturn(time);
    }
    // endregion helper methods --------------------------------------------------------------------

    // region helper classes -----------------------------------------------------------------------
    // endregion helper classes --------------------------------------------------------------------
}