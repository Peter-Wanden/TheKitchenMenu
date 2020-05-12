package com.example.peter.thekitchenmenu.data.repository.source.local.recipe.metadata.dataadapter;

import com.example.peter.thekitchenmenu.data.repository.recipe.metadata.TestDataRecipeMetadata;
import com.example.peter.thekitchenmenu.data.repository.source.local.recipe.metadata.datasource.TestDataRecipeMetadataEntity;
import com.example.peter.thekitchenmenu.data.repository.source.local.recipe.metadata.datasource.componentstate.RecipeComponentStateEntity;
import com.example.peter.thekitchenmenu.data.repository.source.local.recipe.metadata.datasource.componentstate.RecipeComponentStateLocalDataSource;
import com.example.peter.thekitchenmenu.data.repository.source.local.recipe.metadata.datasource.failreason.RecipeFailReasonsLocalDataSource;
import com.example.peter.thekitchenmenu.data.repository.source.local.recipe.metadata.datasource.parent.RecipeMetadataParentLocalDataSource;
import com.example.peter.thekitchenmenu.domain.usecase.recipe.component.metadata.RecipeMetadataPersistenceModel;
import com.example.peter.thekitchenmenu.domain.utils.UniqueIdProvider;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;

import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.verify;

public class RecipeMetadataLocalSaveAdapterTest {

    private static final String TAG = "tkm-" + RecipeMetadataLocalSaveAdapter.class.
            getSimpleName() + ": ";

    // region constants ----------------------------------------------------------------------------
    // endregion constants -------------------------------------------------------------------------

    // region helper fields ------------------------------------------------------------------------
    @Mock
    RecipeMetadataParentLocalDataSource repoParentMock;
    @Mock
    RecipeComponentStateLocalDataSource repoComponentStateMock;
    @Mock
    RecipeFailReasonsLocalDataSource repoFailReasonsMock;
    @Mock
    UniqueIdProvider idProviderMock;
    // endregion helper fields ---------------------------------------------------------------------

    private RecipeMetadataLocalSaveAdapter SUT;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        SUT = givenSystemUnderTest();

    }

    private RecipeMetadataLocalSaveAdapter givenSystemUnderTest() {
        return new RecipeMetadataLocalSaveAdapter(
                repoParentMock,
                repoComponentStateMock,
                repoFailReasonsMock,
                idProviderMock
        );
    }

    @Test
    public void save() {
        // Arrange
        RecipeMetadataPersistenceModel modelUnderTest = TestDataRecipeMetadata.getValidChanged0();
        List<RecipeComponentStateEntity> stateEntities = TestDataRecipeMetadataEntity.
                getValidChanged0ComponentStateEntities();
        RecipeComponentStateEntity[] states =
                stateEntities.toArray(new RecipeComponentStateEntity[0]);

        System.out.println(TAG + Arrays.toString(states));

        // Act
        SUT.save(modelUnderTest);
        // Assert
        verify(repoParentMock).save(eq(TestDataRecipeMetadataEntity.
                getValidChanged0ParentEntity()));
        verify(repoComponentStateMock).save(eq(states));
    }

    // region helper methods -----------------------------------------------------------------------
    // endregion helper methods --------------------------------------------------------------------

    // region helper classes -----------------------------------------------------------------------

    // endregion helper classes --------------------------------------------------------------------
}