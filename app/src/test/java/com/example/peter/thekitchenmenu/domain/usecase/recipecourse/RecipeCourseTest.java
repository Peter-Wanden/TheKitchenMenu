package com.example.peter.thekitchenmenu.domain.usecase.recipecourse;

import com.example.peter.thekitchenmenu.commonmocks.UseCaseSchedulerMock;
import com.example.peter.thekitchenmenu.data.entity.RecipeCourseEntity;
import com.example.peter.thekitchenmenu.data.entity.RecipeEntity;
import com.example.peter.thekitchenmenu.data.repository.DataSource;
import com.example.peter.thekitchenmenu.data.repository.RepositoryRecipeCourse;
import com.example.peter.thekitchenmenu.domain.UseCaseHandler;
import com.example.peter.thekitchenmenu.domain.UseCaseInteractor;
import com.example.peter.thekitchenmenu.domain.usecase.recipestate.RecipeState;
import com.example.peter.thekitchenmenu.testdata.TestDataRecipeCourseEntity;
import com.example.peter.thekitchenmenu.testdata.TestDataRecipeEntity;
import com.example.peter.thekitchenmenu.domain.utils.TimeProvider;
import com.example.peter.thekitchenmenu.domain.utils.UniqueIdProvider;

import org.junit.*;
import org.mockito.*;

import java.util.List;

import static com.example.peter.thekitchenmenu.domain.usecase.recipecourse.RecipeCourse.DO_NOT_CLONE;
import static com.example.peter.thekitchenmenu.testdata.TestDataRecipeCourseEntity.getAllByRecipeId;
import static com.example.peter.thekitchenmenu.testdata.TestDataRecipeEntity.getValidExisting;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.*;
import static org.mockito.Mockito.*;
import static org.junit.Assert.assertEquals;

public class RecipeCourseTest {

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
    private RecipeCourseResponse actualResponse;
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
    public void executeNewRequest_recipeId_persistenceCalledWithCorrectId() {
        // Arrange
        request = getRequest(EXISTING_RECIPE_ID, DO_NOT_CLONE, null, false);
        // Act
        handler.execute(SUT, request, getCallback());
        // Assert
        verify(repoCourseMock).getCoursesForRecipe(eq(EXISTING_RECIPE_ID), repoCourseCallback.capture());
    }

    @Test
    public void executeNewRequest_recipeIdWithNoCourses_emptyListReturned_IsValidFalse() {
        // Arrange
        request = getRequest("IdNotInTestData", DO_NOT_CLONE, null, false);
        // Act
        handler.execute(SUT, request, getCallback());
        // Assert
        confirmRepoCourseCalledAndReturnMatchingCourses(request.getRecipeId());
        assertEquals(RecipeState.ComponentState.DATA_UNAVAILABLE, actualResponse.getStatus());
        assertEquals(0, actualResponse.getCourseList().size());
    }

    @Test
    public void executeNewRequest_recipeId_completeListOfModelsReturned() {
        // Arrange
        request = getRequest(EXISTING_RECIPE_ID, DO_NOT_CLONE, null, false);
        // Act
        handler.execute(SUT, request, getCallback());
        // Assert
        confirmRepoCourseCalledAndReturnMatchingCourses(request.getRecipeId());

        int expectedNumberOfModels = TestDataRecipeCourseEntity.
                getAllByRecipeId(EXISTING_RECIPE_ID).
                size();
        int actualNumberOfModels = actualResponse.
                getCourseList().
                size();

        assertEquals(expectedNumberOfModels, actualNumberOfModels);
        // No data has been modified, just data returned
        assertEquals(RecipeState.ComponentState.VALID_UNCHANGED, actualResponse.getStatus());
    }

    @Test
    public void executeNewRequest_cloneFromAndToIds_persistenceCalledWithCloneFromId() {
        // Arrange
        request = getRequest(EXISTING_RECIPE_ID, NEW_RECIPE_ID, null, false);
        // Act
        handler.execute(SUT, request, getCallback());
        // Assert
        verify(repoCourseMock).getCoursesForRecipe(eq(EXISTING_RECIPE_ID), anyObject());
    }

    @Test
    public void executeNewRequest_cloneFromAndToIds_modelsClonedToNewId() {
        // Arrange
        when(timeProviderMock.getCurrentTimeInMills()).thenReturn(10L);
        when(idProviderMock.getUId()).thenReturn(NEW_RECIPE_ID);

        request = getRequest(EXISTING_RECIPE_ID, NEW_RECIPE_ID, null, false);
        // Act
        handler.execute(SUT, request, getCallback());
        // Assert
        confirmRepoCourseCalledAndReturnMatchingCourses(request.getRecipeId());
        // Confirm the correct number of entities have been cloned
        int expectedNumberOfClonesSaved = TestDataRecipeCourseEntity.
                getAllByRecipeId(EXISTING_RECIPE_ID).
                size();
        verify(repoCourseMock, times(expectedNumberOfClonesSaved)).save(entityCaptor.capture());
        // Confirm all entities have been saved with the cloneToId
        for (RecipeCourseEntity entity : entityCaptor.getAllValues()) {
            assertEquals(NEW_RECIPE_ID, entity.getRecipeId());
        }
        assertEquals(RecipeState.ComponentState.VALID_CHANGED, actualResponse.getStatus());
    }

    @Test
    public void executeExistingRequest_cloneFromAndToIds_whenDeleteCourse_courseDeletedFromCloneToId() {
        // Arrange
        when(timeProviderMock.getCurrentTimeInMills()).thenReturn(10L);
        whenIdProviderReturnMockDatabaseIds();

        request = getRequest(EXISTING_RECIPE_ID, NEW_RECIPE_ID, null, false);
        // Act
        handler.execute(SUT, request, getCallback());
        // Assert
        confirmRepoCourseCalledAndReturnMatchingCourses(request.getRecipeId());
        // confirm target is in results
        assertTrue(actualResponse.getCourseList().containsKey(RecipeCourse.Course.COURSE_ONE));
        // confirm target has correct recipeId
        String expectedRecipeId = actualResponse.getCourseList().
                get(RecipeCourse.Course.COURSE_ONE).getRecipeId();
        assertEquals(NEW_RECIPE_ID, expectedRecipeId);
        // Arrange request to delete target
        // Get targets database id
        String targetsDataBaseId = actualResponse.getCourseList().
                get(RecipeCourse.Course.COURSE_ONE).getId();
        // request delete target
        request = getRequest(
                NEW_RECIPE_ID,
                DO_NOT_CLONE,
                RecipeCourse.Course.COURSE_ONE,
                false);
        // Act
        handler.execute(SUT, request, getCallback());
        // Assert - confirm target deleted from database and list
        verify(repoCourseMock).deleteById(eq(targetsDataBaseId));
        assertNull(actualResponse.getCourseList().get(RecipeCourse.Course.COURSE_ONE));
        // confirm data has changed
        if (actualResponse.getCourseList().size() > 0) {
            assertEquals(RecipeState.ComponentState.VALID_CHANGED, actualResponse.getStatus());
        } else {
            assertEquals(RecipeState.ComponentState.INVALID_CHANGED, actualResponse.getStatus());
        }
    }

    // region helper methods -----------------------------------------------------------------------
    private RecipeCourseRequest getRequest(String recipeId,
                                            String cloneToRecipeId,
                                            RecipeCourse.Course course,
                                            boolean isAddCourse) {
        return new RecipeCourseRequest.Builder().
                setRecipeId(recipeId).
                setCloneToRecipeId(cloneToRecipeId).
                setCourse(course).
                setAddCourse(isAddCourse).
                build();
    }

    private UseCaseInteractor.Callback<RecipeCourseResponse> getCallback() {
        return new UseCaseInteractor.Callback<RecipeCourseResponse>() {

            @Override
            public void onSuccess(RecipeCourseResponse response) {
                RecipeCourseTest.this.actualResponse = response;

            }

            @Override
            public void onError(RecipeCourseResponse response) {
                RecipeCourseTest.this.actualResponse = response;
            }
        };
    }

    private void confirmRepoCourseCalledAndReturnMatchingCourses(String recipeId) {
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
        when(idProviderMock.getUId()).thenReturn("1","2", "3", "4", "5", "6", "7", "8");
    }
    // endregion helper methods --------------------------------------------------------------------

    // region helper classes -----------------------------------------------------------------------
    // endregion helper classes --------------------------------------------------------------------
}