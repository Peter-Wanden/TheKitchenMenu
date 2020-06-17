package com.example.peter.thekitchenmenu.data.repository.source.local.recipe.course.dataadapter;

import com.example.peter.thekitchenmenu.data.repository.recipe.metadata.TestDataRecipeMetadata;
import com.example.peter.thekitchenmenu.data.repository.source.local.recipe.course.datasource.courseitem.RecipeCourseItemLocalDataSource;
import com.example.peter.thekitchenmenu.data.repository.source.local.recipe.course.datasource.parent.RecipeCourseParentLocalDataSource;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.verify;

public class CourseLocalDeleteAdapterTest {

    // region constants ----------------------------------------------------------------------------
    // endregion constants -------------------------------------------------------------------------

    // region helper fields ------------------------------------------------------------------------
    @Mock
    RecipeCourseParentLocalDataSource parentRepo;
    @Mock
    RecipeCourseItemLocalDataSource courseItemRepoMock;

    private String dataId = TestDataRecipeMetadata.getValidChangedThree().getDataId();
    private String domainId = TestDataRecipeMetadata.getValidChangedThree().getDomainId();
    // endregion helper fields ---------------------------------------------------------------------

    private CourseLocalDeleteAdapter SUT;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        SUT = givenSystemUnderTest();

    }

    private CourseLocalDeleteAdapter givenSystemUnderTest() {
        return new CourseLocalDeleteAdapter(
                parentRepo, courseItemRepoMock
        );
    }

    @Test
    public void deleteByDataId() {
        // Arrange
        // Act
        SUT.deleteByDataId(dataId);
        // Assert
        verify(courseItemRepoMock).deleteByDataId(dataId);
    }

    @Test
    public void deleteAllByDomainId() {
        // Arrange
        // Act
        SUT.deleteAllByDomainId(domainId);
        // Assert
//        verify(courseItemRepoMock).deleteAllByDomainId(domainId);
    }

    @Test
    public void deleteAll() {
        // Arrange
        // Act
        SUT.deleteAll();
        // Assert
        verify(courseItemRepoMock).deleteAll();
    }

    // region helper methods -----------------------------------------------------------------------
    // endregion helper methods --------------------------------------------------------------------

    // region helper classes -----------------------------------------------------------------------

    // endregion helper classes --------------------------------------------------------------------
}