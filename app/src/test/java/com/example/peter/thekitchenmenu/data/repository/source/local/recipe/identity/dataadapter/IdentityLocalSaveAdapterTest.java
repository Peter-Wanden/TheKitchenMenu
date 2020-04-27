package com.example.peter.thekitchenmenu.data.repository.source.local.recipe.identity.dataadapter;

import com.example.peter.thekitchenmenu.data.repository.recipe.identity.TestDataRecipeIdentity;
import com.example.peter.thekitchenmenu.data.repository.source.local.recipe.identity.datasource.RecipeIdentityLocalDataSource;
import com.example.peter.thekitchenmenu.data.repository.source.local.recipe.identity.datasource.TestDataRecipeIdentityEntity;
import com.example.peter.thekitchenmenu.domain.usecase.recipe.component.identity.RecipeIdentityPersistenceModel;

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

public class IdentityLocalSaveAdapterTest {

    // region constants ----------------------------------------------------------------------------
    // endregion constants -------------------------------------------------------------------------

    // region helper fields ------------------------------------------------------------------------
    @Mock
    RecipeIdentityLocalDataSource repoMock;

    // endregion helper fields ---------------------------------------------------------------------

    private IdentityLocalSaveAdapter SUT;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        SUT = givenSystemUnderTest();
    }

    private IdentityLocalSaveAdapter givenSystemUnderTest() {
        return new IdentityLocalSaveAdapter(
                repoMock
        );
    }

    @Test
    public void save() {
        // Arrange
        RecipeIdentityPersistenceModel modelUnderTest = TestDataRecipeIdentity.getValidNewComplete();
        // Act
        SUT.save(modelUnderTest);
        // Assert
        verify(repoMock).save(eq(TestDataRecipeIdentityEntity.getValidNewComplete()));
    }

    // region helper methods -----------------------------------------------------------------------
    // endregion helper methods --------------------------------------------------------------------

    // region helper classes -----------------------------------------------------------------------

    // endregion helper classes --------------------------------------------------------------------
}