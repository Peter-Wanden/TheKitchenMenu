package com.example.peter.thekitchenmenu.data.repository.source.local.recipe.duration.dataadapter;

import com.example.peter.thekitchenmenu.data.repository.recipe.duration.TestDataRecipeDuration;
import com.example.peter.thekitchenmenu.data.repository.source.local.recipe.duration.datasource.RecipeDurationEntity;
import com.example.peter.thekitchenmenu.data.repository.source.local.recipe.duration.datasource.RecipeDurationLocalDataSource;
import com.example.peter.thekitchenmenu.data.repository.source.local.recipe.duration.datasource.TestDataRecipeDurationEntity;
import com.example.peter.thekitchenmenu.domain.usecase.recipe.component.duration.RecipeDurationPersistenceModel;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.verify;

public class DurationLocalSaveAdapterTest {

    // region constants ----------------------------------------------------------------------------
    // endregion constants -------------------------------------------------------------------------

    // region helper fields ------------------------------------------------------------------------
    @Mock
    RecipeDurationLocalDataSource repoMock;
    // endregion helper fields ---------------------------------------------------------------------

    private DurationLocalSaveAdapter SUT;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        SUT = givenSystemUnderTest();
    }

    private DurationLocalSaveAdapter givenSystemUnderTest() {
        return new DurationLocalSaveAdapter(
                repoMock
        );
    }

    @Test
    public void save() {
        // Arrange
        RecipeDurationPersistenceModel modelUnderTest = TestDataRecipeDuration.
                getExistingValidPrepTimeValidCookTime();
        RecipeDurationEntity expectedSaved = TestDataRecipeDurationEntity.
                getValidExistingComplete();
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