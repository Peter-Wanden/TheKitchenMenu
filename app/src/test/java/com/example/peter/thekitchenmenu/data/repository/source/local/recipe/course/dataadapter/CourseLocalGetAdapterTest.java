package com.example.peter.thekitchenmenu.data.repository.source.local.recipe.course.dataadapter;

import com.example.peter.thekitchenmenu.data.repository.DomainDataAccess;
import com.example.peter.thekitchenmenu.data.repository.recipe.course.TestDataRecipeCourse;
import com.example.peter.thekitchenmenu.data.repository.source.local.dataadapter.PrimitiveDataSource.GetAllPrimitiveCallback;
import com.example.peter.thekitchenmenu.data.repository.source.local.dataadapter.PrimitiveDataSource.GetPrimitiveCallback;
import com.example.peter.thekitchenmenu.data.repository.source.local.recipe.course.datasource.courseitem.RecipeCourseEntity;
import com.example.peter.thekitchenmenu.data.repository.source.local.recipe.course.datasource.courseitem.RecipeCourseItemLocalDataSource;
import com.example.peter.thekitchenmenu.data.repository.source.local.recipe.course.datasource.TestDataRecipeCourseEntity;
import com.example.peter.thekitchenmenu.data.repository.source.local.recipe.course.datasource.parent.RecipeCourseParentLocalDataSource;
import com.example.peter.thekitchenmenu.domain.usecase.recipe.component.course.RecipeCourse.Course;
import com.example.peter.thekitchenmenu.domain.usecase.recipe.component.course.RecipeCoursePersistenceModel;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.verify;
import static org.junit.Assert.assertTrue;

public class CourseLocalGetAdapterTest {

    private static final String TAG = "tkm-" + CourseLocalGetAdapterTest.class.getSimpleName() +
            ": ";

    // region constants ----------------------------------------------------------------------------
    // endregion constants -------------------------------------------------------------------------

    // region helper fields ------------------------------------------------------------------------
    @Mock
    RecipeCourseParentLocalDataSource repoParent;
    @Mock
    RecipeCourseItemLocalDataSource repoItem;
    @Captor
    ArgumentCaptor<GetAllPrimitiveCallback<RecipeCourseEntity>> repoGetAllCallback;
    @Captor
    ArgumentCaptor<GetPrimitiveCallback<RecipeCourseEntity>> repoGetEntityCallback;

    private DomainModelCallbackClient getModelCallbackClient;
    private GetAllDomainModelsCallbackClient getAllCallbackClient;
    // endregion helper fields ---------------------------------------------------------------------

    private CourseLocalGetAdapter SUT;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        SUT = givenUseCase();
    }

    private CourseLocalGetAdapter givenUseCase() {
        return new CourseLocalGetAdapter(repoParent, repoItem);
    }

    @Test
    public void getByDataId_DATA_UNAVAILABLE() {
        // Arrange
        String dataId = "idNotInTestData";
        getModelCallbackClient = new DomainModelCallbackClient();
        // Act
//        SUT.getByDataId(dataId, getModelCallbackClient);
        // Assert
        verify(repoItem).getByDataId(eq(dataId), repoGetEntityCallback.capture());
        repoGetEntityCallback.getValue().onDataUnavailable();

        assertTrue(
                getModelCallbackClient.isModelUnavailable
        );
    }

    @Test
    public void getByDataId_domainModelReturned() {
//        // Arrange
//        RecipeCoursePersistenceModelItem modelUnderTest = TestDataRecipeCourse.
//                getExistingActiveRecipeCourseZero();
//        getModelCallbackClient = new DomainModelCallbackClient();
//        // Act
////        SUT.getByDataId(modelUnderTest.getDataId(), getModelCallbackClient);
//        // Assert
//        verify(repoItem).getByDataId(
//                eq(modelUnderTest.getDataId()),
//                repoGetEntityCallback.capture()
//        );
//        repoGetEntityCallback.getValue().onEntityLoaded(TestDataRecipeCourseEntity.
//                getExistingActiveRecipeCourseZero()
//        );
//        assertEquals(
//                modelUnderTest,
//                getModelCallbackClient.model
//        );
    }

    @Test
    public void getAllActiveByDomainId_activeModelsReturned() {
//        // Arrange
//        String domainId = TestDataRecipeCourse.EXISTING_RECIPE_ID;
//        List<RecipeCoursePersistenceModelItem> modelsUnderTest = new ArrayList<>(TestDataRecipeCourse.
//                getAllExistingActiveByDomainId(domainId));
//        getAllCallbackClient = new GetAllDomainModelsCallbackClient();
//        // Act
////        SUT.getAllActiveByDomainId(domainId, getAllCallbackClient);
//        // Assert
////        verify(repoMock).getAllByDomainId(eq(domainId), repoGetAllCallback.capture());
//        repoGetAllCallback.getValue().onAllLoaded(TestDataRecipeCourseEntity.
//                getAllExistingActiveByDomainId(domainId)
//        );
//
//        assertEquals(
//                modelsUnderTest,
//                getAllCallbackClient.models
//        );
    }

    @Test
    public void getAllByCourse_allModelsFromCourseReturned() {
//        // Arrange
//        Course course = Course.COURSE_ZERO;
//        List<RecipeCoursePersistenceModelItem> modelsUnderTest = new ArrayList<>(
//                TestDataRecipeCourse.getAllByCourse(course));
//        getAllCallbackClient = new GetAllDomainModelsCallbackClient();
//        // Act
//        SUT.getAllByCourse(course, getAllCallbackClient);
//        // Assert
//        verify(repoItem).getAllByCourseNo(eq(course.getId()), repoGetAllCallback.capture());
//        repoGetAllCallback.getValue().onAllLoaded(TestDataRecipeCourseEntity.
//                getAllByCourseNo(course.getId())
//        );
//
//        assertEquals(
//                modelsUnderTest,
//                getAllCallbackClient.models
//        );
    }

    @Test
    public void getAll() {
//        // Arrange
//        List<RecipeCoursePersistenceModelItem> modelsUnderTest = new ArrayList<>(
//                TestDataRecipeCourse.getAll());
//        getAllCallbackClient = new GetAllDomainModelsCallbackClient();
//        // Act
////        SUT.getAll(getAllCallbackClient);
//        // Assert
//        verify(repoItem).getAll(repoGetAllCallback.capture());
//        repoGetAllCallback.getValue().onAllLoaded(TestDataRecipeCourseEntity.getAll());
//
//        assertEquals(
//                modelsUnderTest,
//                getAllCallbackClient.models
//        );
    }

    // region helper methods -----------------------------------------------------------------------
    // endregion helper methods --------------------------------------------------------------------

    // region helper classes -----------------------------------------------------------------------
    private static class DomainModelCallbackClient
            implements DomainDataAccess.GetDomainModelCallback<RecipeCoursePersistenceModel> {

        private static final String TAG = CourseLocalGetAdapterTest.TAG +
                DomainModelCallbackClient.class.getSimpleName() + ": ";

        private RecipeCoursePersistenceModel model;
        private boolean isModelUnavailable;

        @Override
        public void onDomainModelLoaded(RecipeCoursePersistenceModel m) {
            System.out.println(TAG + m);
            model = m;
        }

        @Override
        public void onDomainModelUnavailable() {
            isModelUnavailable = true;
            System.out.println(TAG + "isModelUnavailable=" + isModelUnavailable);
        }
    }

    private static class GetAllDomainModelsCallbackClient
            implements DomainDataAccess.GetAllDomainModelsCallback<RecipeCoursePersistenceModel> {

        private static final String TAG = CourseLocalGetAdapterTest.TAG +
                GetAllDomainModelsCallbackClient.class.getSimpleName() + ": ";

        private List<RecipeCoursePersistenceModel> models;
        private boolean isModelsUnavailable;

        @Override
        public void onAllDomainModelsLoaded(List<RecipeCoursePersistenceModel> m) {
            System.out.println(TAG + m);
            models = m;
        }

        @Override
        public void onDomainModelsUnavailable() {
            System.out.println(TAG + "isModelsUnavailable=" + isModelsUnavailable);
            System.out.println(TAG + isModelsUnavailable);
        }
    }
    // endregion helper classes --------------------------------------------------------------------
}