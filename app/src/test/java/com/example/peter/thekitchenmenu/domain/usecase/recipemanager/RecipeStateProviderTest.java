package com.example.peter.thekitchenmenu.domain.usecase.recipemanager;

import com.example.peter.thekitchenmenu.commonmocks.UseCaseSchedulerMock;
import com.example.peter.thekitchenmenu.data.entity.RecipeEntity;
import com.example.peter.thekitchenmenu.data.repository.DataSource;
import com.example.peter.thekitchenmenu.data.repository.RepositoryRecipe;
import com.example.peter.thekitchenmenu.domain.UseCaseHandler;
import com.example.peter.thekitchenmenu.domain.utils.TimeProvider;
import com.example.peter.thekitchenmenu.domain.utils.UniqueIdProvider;

import org.junit.*;
import org.mockito.*;

import static org.junit.Assert.assertEquals;

public class RecipeStateProviderTest {

    // region constants ----------------------------------------------------------------------------
    // endregion constants -------------------------------------------------------------------------

    // region helper fields ------------------------------------------------------------------------
    private UseCaseHandler handler;
    private RecipeManagerResponse actualResponse;
    @Mock
    RepositoryRecipe repoMock;
    @Captor
    ArgumentCaptor<DataSource.GetEntityCallback<RecipeEntity>> recipeRepoCallback;
    @Mock
    TimeProvider timeProviderMock;
    @Mock
    UniqueIdProvider idProviderMock;
    // endregion helper fields ---------------------------------------------------------------------

    private RecipeStateProvider SUT;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        handler = new UseCaseHandler(new UseCaseSchedulerMock());
        SUT = givenUseCase();
    }

    private RecipeStateProvider givenUseCase() {
        return new RecipeStateProvider(repoMock, timeProviderMock, idProviderMock);
    }



    // region helper methods -----------------------------------------------------------------------
    // endregion helper methods --------------------------------------------------------------------

    // region helper classes -----------------------------------------------------------------------
    // endregion helper classes --------------------------------------------------------------------


}