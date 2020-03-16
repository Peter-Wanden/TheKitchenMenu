package com.example.peter.thekitchenmenu.domain.usecase.recipe.recipemetadata;

import com.example.peter.thekitchenmenu.commonmocks.UseCaseSchedulerMock;
import com.example.peter.thekitchenmenu.data.entity.RecipeEntity;
import com.example.peter.thekitchenmenu.data.repository.DataSource;
import com.example.peter.thekitchenmenu.data.repository.RepositoryRecipe;
import com.example.peter.thekitchenmenu.domain.usecase.UseCaseHandler;
import com.example.peter.thekitchenmenu.domain.utils.TimeProvider;
import com.example.peter.thekitchenmenu.domain.utils.UniqueIdProvider;

import org.junit.Before;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.verify;

public class RecipeTest {

    // region constants ----------------------------------------------------------------------------
    // endregion constants -------------------------------------------------------------------------

    // region helper fields ------------------------------------------------------------------------
    @Mock
    RepositoryRecipe repoRecipeMock;
    @Captor
    ArgumentCaptor<DataSource.GetEntityCallback<RecipeEntity>> repoRecipeCallback;
    @Mock
    UniqueIdProvider idProviderMock;
    @Mock
    TimeProvider timeProviderMock;
    private UseCaseHandler handler;
    private RecipeMetadataResponse response;
    // endregion helper fields ---------------------------------------------------------------------

    private RecipeMetadata SUT;

    @Before
    public void setup() throws Exception {
        MockitoAnnotations.initMocks(this);
        SUT = givenUseCase();

    }

    private RecipeMetadata givenUseCase() {
        handler = new UseCaseHandler(new UseCaseSchedulerMock());
        return new RecipeMetadata(
                timeProviderMock,
                repoRecipeMock
        );
    }



    // region helper methods -----------------------------------------------------------------------
    // endregion helper methods --------------------------------------------------------------------

    // region helper classes -----------------------------------------------------------------------
    // endregion helper classes --------------------------------------------------------------------


}