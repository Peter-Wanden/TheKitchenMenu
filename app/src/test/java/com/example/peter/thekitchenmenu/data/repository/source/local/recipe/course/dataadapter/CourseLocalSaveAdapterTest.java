package com.example.peter.thekitchenmenu.data.repository.source.local.recipe.course.dataadapter;

import com.example.peter.thekitchenmenu.data.repository.recipe.course.TestDataRecipeCourse;
import com.example.peter.thekitchenmenu.data.repository.source.local.recipe.course.datasource.courseitem.RecipeCourseEntity;
import com.example.peter.thekitchenmenu.data.repository.source.local.recipe.course.datasource.courseitem.RecipeCourseItemLocalDataSource;
import com.example.peter.thekitchenmenu.data.repository.source.local.recipe.course.datasource.TestDataRecipeCourseEntity;
import com.example.peter.thekitchenmenu.data.repository.source.local.recipe.course.datasource.parent.RecipeCourseParentLocalDataSource;
import com.example.peter.thekitchenmenu.domain.utils.UniqueIdProvider;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.verify;

public class CourseLocalSaveAdapterTest {

    // region constants ----------------------------------------------------------------------------
    // endregion constants -------------------------------------------------------------------------

    // region helper fields ------------------------------------------------------------------------
    @Mock
    RecipeCourseParentLocalDataSource parentRepo;
    @Mock
    RecipeCourseItemLocalDataSource courseItemRepoMock;
    @Mock
    UniqueIdProvider idProviderMock;
    // endregion helper fields ---------------------------------------------------------------------

    private CourseLocalSaveAdapter SUT;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        SUT = givenSystemUnderTest();
    }

    private CourseLocalSaveAdapter givenSystemUnderTest() {
        return new CourseLocalSaveAdapter(
                parentRepo,
                courseItemRepoMock,
                idProviderMock);
    }

    @Test
    public void save() {
        // Arrange
//        RecipeCoursePersistenceModelItem modelUnderTest = TestDataRecipeCourse.
//                getExistingActiveRecipeCourseZero();
//        RecipeCourseEntity expectedModel = TestDataRecipeCourseEntity.
//                getExistingActiveRecipeCourseZero();
//        // Act
//        SUT.save(modelUnderTest);
//        // Assert
//        verify(courseItemRepoMock).save(eq(expectedModel));
    }

    // region helper methods -----------------------------------------------------------------------
    // endregion helper methods --------------------------------------------------------------------

    // region helper classes -----------------------------------------------------------------------

    // endregion helper classes --------------------------------------------------------------------
}