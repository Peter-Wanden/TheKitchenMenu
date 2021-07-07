package com.example.peter.thekitchenmenu.data.repository.source.local.ingredient.dataadapter;

import com.example.peter.thekitchenmenu.data.repository.recipe.metadata.TestDataRecipeMetadata;
import com.example.peter.thekitchenmenu.data.repository.source.local.ingredient.datasource.IngredientLocalDataSource;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.verify;

public class IngredientLocalDeleteAdapterTest {

    // region constants ----------------------------------------------------------------------------
    private static final String DATA_ID = TestDataRecipeMetadata.getValidChanged().getDataId();
    private static final String DOMAIN_ID = TestDataRecipeMetadata.getValidChanged().getDomainId();
    // endregion constants -------------------------------------------------------------------------

    // region helper fields ------------------------------------------------------------------------
    @Mock
    IngredientLocalDataSource repoMock;
    // endregion helper fields ---------------------------------------------------------------------

    private IngredientLocalDeleteAdapter SUT;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        SUT = givenSystemUnderTest();
    }

    private IngredientLocalDeleteAdapter givenSystemUnderTest() {
        return new IngredientLocalDeleteAdapter(
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
        verify(repoMock).deleteAllByDomainId(DOMAIN_ID);
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