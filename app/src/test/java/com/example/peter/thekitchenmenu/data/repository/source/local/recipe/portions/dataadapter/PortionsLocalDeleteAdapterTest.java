package com.example.peter.thekitchenmenu.data.repository.source.local.recipe.portions.dataadapter;

import com.example.peter.thekitchenmenu.data.repository.recipe.metadata.TestDataRecipeMetadata;
import com.example.peter.thekitchenmenu.data.repository.source.local.recipe.portions.datasource.RecipePortionsLocalDataSource;

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

public class PortionsLocalDeleteAdapterTest {

    // region constants ----------------------------------------------------------------------------
    // endregion constants -------------------------------------------------------------------------

    // region helper fields ------------------------------------------------------------------------
    @Mock
    RecipePortionsLocalDataSource repoMock;

    private String dataId = TestDataRecipeMetadata.getValidChanged0().getDataId();
    private String domainId = TestDataRecipeMetadata.getValidChanged0().getDomainId();
    // endregion helper fields ---------------------------------------------------------------------

    private PortionsLocalDeleteAdapter SUT;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        SUT = givenSystemUnderTest();

    }

    private PortionsLocalDeleteAdapter givenSystemUnderTest() {
        return new PortionsLocalDeleteAdapter(
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