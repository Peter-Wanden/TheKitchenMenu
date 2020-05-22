package com.example.peter.thekitchenmenu.data.repository.source.local.recipe.duration.dataadapter;

import com.example.peter.thekitchenmenu.data.repository.recipe.metadata.TestDataRecipeMetadata;
import com.example.peter.thekitchenmenu.data.repository.source.local.recipe.duration.datasource.RecipeDurationLocalDataSource;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.verify;

public class DurationLocalDeleteAdapterTest {

    // region constants ----------------------------------------------------------------------------
    // endregion constants -------------------------------------------------------------------------

    // region helper fields ------------------------------------------------------------------------
    @Mock
    RecipeDurationLocalDataSource repoMock;

    private String dataId = TestDataRecipeMetadata.getValidChangedThree().getDataId();
    private String domainId = TestDataRecipeMetadata.getValidChangedThree().getDomainId();
    // endregion helper fields ---------------------------------------------------------------------

    private DurationLocalDeleteAdapter SUT;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        SUT = givenSystemUnderTest();

    }

    private DurationLocalDeleteAdapter givenSystemUnderTest() {
        return new DurationLocalDeleteAdapter(
                repoMock
        );
    }

    @Test
    public void deleteByDataId() {
        // Arrange
        // Act
        SUT.deleteByDataId(dataId);
        // Assert
        verify(repoMock).deleteByDataId(eq(dataId));
    }

    @Test
    public void deleteAllByDomainId() {
        // Arrange
        // Act
        SUT.deleteAllByDomainId(domainId);
        // Assert
        verify(repoMock).deleteAllByDomainId(eq(domainId));
    }

    @Test
    public void deleteAll() {
        // Arrange
        // Act
        SUT.deleteAll();
        // Assert
        verify(repoMock).deleteAll();
    }

    // region helper methods -----------------------------------------------------------------------
    // endregion helper methods --------------------------------------------------------------------

    // region helper classes -----------------------------------------------------------------------

    // endregion helper classes --------------------------------------------------------------------
}