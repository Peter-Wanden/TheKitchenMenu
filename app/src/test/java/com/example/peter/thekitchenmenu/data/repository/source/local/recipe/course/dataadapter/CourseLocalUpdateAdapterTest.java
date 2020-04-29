package com.example.peter.thekitchenmenu.data.repository.source.local.recipe.course.dataadapter;

import com.example.peter.thekitchenmenu.data.repository.recipe.course.TestDataRecipeCourse;
import com.example.peter.thekitchenmenu.data.repository.source.local.recipe.course.datasource.RecipeCourseEntity;
import com.example.peter.thekitchenmenu.data.repository.source.local.recipe.course.datasource.RecipeCourseLocalDataSource;
import com.example.peter.thekitchenmenu.data.repository.source.local.recipe.course.datasource.TestDataRecipeCourseEntity;
import com.example.peter.thekitchenmenu.domain.usecase.recipe.component.course.RecipeCoursePersistenceModel;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.Assert.*;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.verify;

public class CourseLocalUpdateAdapterTest {

    // region constants ----------------------------------------------------------------------------
    // endregion constants -------------------------------------------------------------------------

    // region helper fields ------------------------------------------------------------------------
    @Mock
    RecipeCourseLocalDataSource repoMock;
    // endregion helper fields ---------------------------------------------------------------------

    private CourseLocalUpdateAdapter SUT;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        SUT = givenSystemUnderTest();

    }

    private CourseLocalUpdateAdapter givenSystemUnderTest() {
        return new CourseLocalUpdateAdapter(
                repoMock
        );
    }

    @Test
    public void update() {
        // Arrange
        RecipeCoursePersistenceModel modelUnderTest = TestDataRecipeCourse.
                getExistingActiveRecipeCourseZero();
        RecipeCourseEntity expectedModelSaved = TestDataRecipeCourseEntity.
                getExistingActiveRecipeCourseZero();
        // Act
        SUT.update(modelUnderTest);
        // Assert
        verify(repoMock).update(eq(expectedModelSaved));
    }

    // region helper methods -----------------------------------------------------------------------
    // endregion helper methods --------------------------------------------------------------------

    // region helper classes -----------------------------------------------------------------------

    // endregion helper classes --------------------------------------------------------------------
}