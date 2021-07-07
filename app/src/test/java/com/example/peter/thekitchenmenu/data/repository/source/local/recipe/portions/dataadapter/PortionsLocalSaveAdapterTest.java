package com.example.peter.thekitchenmenu.data.repository.source.local.recipe.portions.dataadapter;

import com.example.peter.thekitchenmenu.data.repository.recipe.portions.TestDataRecipePortions;
import com.example.peter.thekitchenmenu.data.repository.source.local.recipe.portions.datasource.RecipePortionsEntity;
import com.example.peter.thekitchenmenu.data.repository.source.local.recipe.portions.datasource.RecipePortionsLocalDataSource;
import com.example.peter.thekitchenmenu.data.repository.source.local.recipe.portions.datasource.TestDataRecipePortionsEntity;
import com.example.peter.thekitchenmenu.domain.usecasenew.recipe.component.portions.RecipePortionsUseCasePersistenceModel;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.verify;

public class PortionsLocalSaveAdapterTest {

    // region constants ----------------------------------------------------------------------------
    // endregion constants -------------------------------------------------------------------------

    // region helper fields ------------------------------------------------------------------------
    @Mock
    RecipePortionsLocalDataSource repoMock;
    // endregion helper fields ---------------------------------------------------------------------

    private PortionsLocalSaveAdapter SUT;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        SUT = givenSystemUnderTest();

    }

    private PortionsLocalSaveAdapter givenSystemUnderTest() {
        return new PortionsLocalSaveAdapter(
                repoMock
        );
    }

    @Test
    public void save() {
        // Arrange
        RecipePortionsUseCasePersistenceModel modelUnderTest = TestDataRecipePortions.
                getExistingValidNinePortions();
        RecipePortionsEntity expectedSaved = TestDataRecipePortionsEntity.
                getExistingValidNinePortions();
        // Act
        SUT.save(modelUnderTest);
        // Assert
        verify(repoMock).save(eq(expectedSaved));
    }

    // region helper methods -----------------------------------------------------------------------
    // endregion helper methods --------------------------------------------------------------------

    // region helper classes -----------------------------------------------------------------------

    // endregion helper classes --------------------------------------------------------------------
}