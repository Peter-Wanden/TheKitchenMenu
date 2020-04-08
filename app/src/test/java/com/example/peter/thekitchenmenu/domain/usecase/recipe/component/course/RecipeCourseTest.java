package com.example.peter.thekitchenmenu.domain.usecase.recipe.component.course;

import com.example.peter.thekitchenmenu.commonmocks.UseCaseSchedulerMock;
import com.example.peter.thekitchenmenu.data.repository.source.local.recipe.course.datasource.RecipeCourseEntity;
import com.example.peter.thekitchenmenu.data.repository.source.local.recipe.metadata.datasource.parent.RecipeMetadataParentEntity;
import com.example.peter.thekitchenmenu.data.repository.dataadapter.primitive.PrimitiveDataSource;
import com.example.peter.thekitchenmenu.data.repository.recipe.RepositoryRecipeCourse;
import com.example.peter.thekitchenmenu.domain.model.CommonFailReason;
import com.example.peter.thekitchenmenu.domain.usecase.UseCaseHandler;
import com.example.peter.thekitchenmenu.domain.usecase.UseCase;
import com.example.peter.thekitchenmenu.testdata.TestDataRecipeCourseEntity;
import com.example.peter.thekitchenmenu.domain.utils.TimeProvider;
import com.example.peter.thekitchenmenu.domain.utils.UniqueIdProvider;

import org.junit.*;
import org.mockito.*;

import java.util.ArrayList;
import java.util.List;

import static com.example.peter.thekitchenmenu.domain.usecase.recipe.component.course.RecipeCourse.*;
import static com.example.peter.thekitchenmenu.testdata.TestDataRecipeCourseEntity.getAllByRecipeId;
import static com.example.peter.thekitchenmenu.testdata.TestDataRecipeMetadataEntity.getValidExisting;

import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.*;
import static org.mockito.Mockito.*;
import static org.junit.Assert.assertEquals;

public class RecipeCourseTest {

//    private static final String TAG = "tkm-" + RecipeCourseTest.class.getSimpleName() + ": ";

    // region constants ----------------------------------------------------------------------------
    private RecipeMetadataParentEntity VALID_EXISTING_RECIPE_ENTITY = getValidExisting();
    private String EXISTING_RECIPE_ID = VALID_EXISTING_RECIPE_ENTITY.getId();
    // endregion constants -------------------------------------------------------------------------

    // region helper fields ------------------------------------------------------------------------
    @Mock
    RepositoryRecipeCourse repoCourseMock;
    @Captor
    ArgumentCaptor<PrimitiveDataSource.GetAllPrimitiveCallback<RecipeCourseEntity>> repoCourseCallback;
    @Mock
    UniqueIdProvider idProviderMock;
    @Mock
    TimeProvider timeProviderMock;
    @Captor
    ArgumentCaptor<RecipeCourseEntity> entityCaptor;

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
    public void newRequest_idWithNoCourses_emptyListReturned_INVALID_UNCHANGED() {
        // Arrange
        RecipeCourseRequest request = new RecipeCourseRequest.Builder().
                getDefault().
                setDataId("idNotInTestData").
                build();
        // Act
        handler.execute(SUT, request, getCallback());
        // Assert
        verifyRepoCalledAndReturnMatchingCourses(request.getDataId());
        assertEquals(com.example.peter.thekitchenmenu.domain.usecase.recipe.metadata.RecipeMetadata.ComponentState.INVALID_UNCHANGED, onErrorResponse.getMetadata().getState());
        assertTrue(onErrorResponse.getMetadata().getFailReasons().
                contains(CommonFailReason.DATA_UNAVAILABLE));
        assertEquals(0, onErrorResponse.getModel().getCourseList().size());
    }

    @Test
    public void newRequest_idWithNoCourses_thenAddCourse_VALID_CHANGED() {
        // Arrange
        long time = 10L;
        whenTimeProviderReturnTime(time);
        String id = "testId";
        when(idProviderMock.getUId()).thenReturn(id);

        RecipeCourseRequest initialiseComponentRequest = new RecipeCourseRequest.Builder().
                setDataId("IdNotInTestData").
                setModel(new RecipeCourseRequest.Model.Builder().
                        getDefault().
                        build()
                ).
                build();

        // Act
        handler.execute(SUT, initialiseComponentRequest, getCallback());
        // Assert
        verifyRepoCalledAndReturnMatchingCourses(initialiseComponentRequest.getDataId());
        assertEquals(com.example.peter.thekitchenmenu.domain.usecase.recipe.metadata.RecipeMetadata.ComponentState.INVALID_UNCHANGED, onErrorResponse.getMetadata().getState());
        assertEquals(0, onErrorResponse.getModel().getCourseList().size());
        // Arrange
        List<Course> addCourseOne = new ArrayList<>();
        addCourseOne.add(Course.COURSE_ONE);
        RecipeCourseRequest request = new RecipeCourseRequest.Builder().
                setDataId("IdNotInTestData").
                setModel(new RecipeCourseRequest.Model.Builder().
                        setCourseList(addCourseOne).
                        build()
                ).
                build();
        // Act
        handler.execute(SUT, request, getCallback());
        // Assert
        verify(repoCourseMock).save(entityCaptor.capture());
        assertEquals(Course.COURSE_ONE.getCourseNo(), entityCaptor.getValue().getCourseNo());
        assertEquals(time, entityCaptor.getValue().getCreateDate());
        assertEquals(id, entityCaptor.getValue().getId());
        assertEquals(com.example.peter.thekitchenmenu.domain.usecase.recipe.metadata.RecipeMetadata.ComponentState.VALID_CHANGED, onSuccessResponse.getMetadata().getState());
    }

    @Test
    public void existingRequest_persistenceCalledWithCorrectId() {
        // Arrange
        RecipeCourseRequest initialiseComponentRequest = new RecipeCourseRequest.Builder().
                setDataId(EXISTING_RECIPE_ID).
                setModel(new RecipeCourseRequest.Model.Builder().
                        getDefault().
                        build()
                ).
                build();
        // Act
        handler.execute(SUT, initialiseComponentRequest, getCallback());
        // Assert
        verify(repoCourseMock).getAllByDomainId(eq(EXISTING_RECIPE_ID), repoCourseCallback.capture());
    }

    @Test
    public void existingRequest_completeListOfModelsReturned_VALID_UNCHANGED() {
        // Arrange
        RecipeCourseRequest initialiseComponentRequest = new RecipeCourseRequest.Builder().
                setDataId(EXISTING_RECIPE_ID).
                setModel(new RecipeCourseRequest.Model.Builder().
                        getDefault().
                        build()).
                build();
        // Act
        handler.execute(SUT, initialiseComponentRequest, getCallback());
        // Assert
        verifyRepoCalledAndReturnMatchingCourses(initialiseComponentRequest.getDataId());

        int expectedNumberOfModels = TestDataRecipeCourseEntity.
                getAllByRecipeId(EXISTING_RECIPE_ID).
                size();
        int actualNumberOfModels = onSuccessResponse.
                getModel().
                getCourseList().
                size();

        assertEquals(expectedNumberOfModels, actualNumberOfModels);
        // No data has been modified, just data returned
        assertEquals(com.example.peter.thekitchenmenu.domain.usecase.recipe.metadata.RecipeMetadata.ComponentState.VALID_UNCHANGED, onSuccessResponse.getMetadata().getState());
    }

    // TODO - text for create date and last update in metadata

    @Test
    public void existingRequest_allModelsDeleted_INVALID_CHANGED() {
        // Arrange first transaction
        RecipeCourseRequest initialiseComponentRequest = new RecipeCourseRequest.Builder().
                setDataId(EXISTING_RECIPE_ID).
                setModel(new RecipeCourseRequest.Model.Builder().getDefault().build()).
                build();
        // Act
        handler.execute(SUT, initialiseComponentRequest, getCallback());
        // Assert
        verifyRepoCalledAndReturnMatchingCourses(initialiseComponentRequest.getDataId());
        // Arrange
        RecipeCourseRequest removeAllCoursesRequest = new RecipeCourseRequest.Builder().
                setDataId(EXISTING_RECIPE_ID).
                setModel(new RecipeCourseRequest.Model.Builder().getDefault().build()).
                build();
        // Act
        handler.execute(SUT, removeAllCoursesRequest, getCallback());
        // Assert
        assertEquals(com.example.peter.thekitchenmenu.domain.usecase.recipe.metadata.RecipeMetadata.ComponentState.INVALID_CHANGED, onErrorResponse.
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
            @Override
            public void onSuccess(RecipeCourseResponse response) {
                onSuccessResponse = response;
            }

            @Override
            public void onError(RecipeCourseResponse response) {
                onErrorResponse = response;
            }
        };
    }

    private void verifyRepoCalledAndReturnMatchingCourses(String recipeId) {
        // Confirm repo called and capture the callback
        verify(repoCourseMock).getAllByDomainId(eq(recipeId), repoCourseCallback.capture());
        // Find the matching values in the test data and return the callback with results
        List<RecipeCourseEntity> courseEntityList = getAllByRecipeId(recipeId);
        if (courseEntityList.size() > 0) {
            repoCourseCallback.getValue().onAllLoaded(getAllByRecipeId(recipeId));
        } else {
            repoCourseCallback.getValue().onDataUnavailable();
        }
    }

    private void whenTimeProviderReturnTime(long time) {
        when(timeProviderMock.getCurrentTimeInMills()).thenReturn(time);
    }
    // endregion helper methods --------------------------------------------------------------------

    // region helper classes -----------------------------------------------------------------------
    // endregion helper classes --------------------------------------------------------------------
}