package com.example.peter.thekitchenmenu.data.repository.source.local.recipe.course.dataadapter;

import com.example.peter.thekitchenmenu.data.repository.recipe.course.TestDataRecipeCourse;
import com.example.peter.thekitchenmenu.data.repository.source.local.recipe.course.datasource.RecipeCourseEntity;
import com.example.peter.thekitchenmenu.data.repository.source.local.recipe.course.datasource.RecipeCourseLocalDataSource;
import com.example.peter.thekitchenmenu.data.repository.source.local.recipe.course.datasource.TestDataRecipeCourseEntity;
import com.example.peter.thekitchenmenu.domain.usecase.recipe.component.course.RecipeCoursePersistenceModelItem;

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
    RecipeCourseLocalDataSource repoMock;
    // endregion helper fields ---------------------------------------------------------------------

    private CourseLocalSaveAdapter SUT;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        SUT = givenSystemUnderTest();
    }

    private CourseLocalSaveAdapter givenSystemUnderTest() {
        return new CourseLocalSaveAdapter(
                repoMock
        );
    }

    @Test
    public void save() {
        // Arrange
        RecipeCoursePersistenceModelItem modelUnderTest = TestDataRecipeCourse.
                getExistingActiveRecipeCourseZero();
        RecipeCourseEntity expectedModel = TestDataRecipeCourseEntity.
                getExistingActiveRecipeCourseZero();
        // Act
        SUT.save(modelUnderTest);
        // Assert
        verify(repoMock).save(eq(expectedModel));
    }

    // region helper methods -----------------------------------------------------------------------
    // endregion helper methods --------------------------------------------------------------------

    // region helper classes -----------------------------------------------------------------------

    // endregion helper classes --------------------------------------------------------------------
}