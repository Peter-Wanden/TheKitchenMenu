package com.example.peter.thekitchenmenu.data.repository.source.local.recipe.identity.dataadapter;

import com.example.peter.thekitchenmenu.data.repository.recipe.metadata.TestDataRecipeMetadata;
import com.example.peter.thekitchenmenu.data.repository.source.local.recipe.identity.datasource.RecipeIdentityLocalDataSource;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.verify;

public class IdentityLocalDeleteAdapterTest {

    // region constants ----------------------------------------------------------------------------
    private static final String DATA_ID = TestDataRecipeMetadata.getValidChangedThree().getDataId();
    private static final String DOMAIN_ID = TestDataRecipeMetadata.getValidChangedThree().getDomainId();
    // endregion constants -------------------------------------------------------------------------

    // region helper fields ------------------------------------------------------------------------
    @Mock
    RecipeIdentityLocalDataSource repoMock;
    // endregion helper fields ---------------------------------------------------------------------

    private IdentityLocalDeleteAdapter SUT;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        SUT = givenSystemUnderTest();
    }

    private IdentityLocalDeleteAdapter givenSystemUnderTest() {
        return new IdentityLocalDeleteAdapter(
                repoMock
        );
    }

    @Test
    public void deleteByDataId() {
        // Arrange
        // Act
        SUT.deleteByDataId(DATA_ID);
        // Assert
        verify(repoMock).deleteByDataId(eq(DATA_ID));
    }

    @Test
    public void deleteAllByDomainId() {
        // Arrange
        // Act
        SUT.deleteAllByDomainId(DOMAIN_ID);
        // Assert
        verify(repoMock).deleteAllByDomainId(eq(DOMAIN_ID));
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