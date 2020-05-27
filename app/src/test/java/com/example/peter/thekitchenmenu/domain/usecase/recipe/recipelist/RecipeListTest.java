package com.example.peter.thekitchenmenu.domain.usecase.recipe.recipelist;

import com.example.peter.thekitchenmenu.commonmocks.UseCaseSchedulerMock;
import com.example.peter.thekitchenmenu.data.repository.DomainDataAccess.GetAllDomainModelsCallback;
import com.example.peter.thekitchenmenu.data.repository.recipe.RepositoryRecipeMetadata;
import com.example.peter.thekitchenmenu.data.repository.recipe.metadata.TestDataRecipeMetadata;
import com.example.peter.thekitchenmenu.domain.usecase.UseCaseBase;
import com.example.peter.thekitchenmenu.domain.usecase.UseCaseFactory;
import com.example.peter.thekitchenmenu.domain.usecase.UseCaseHandler;
import com.example.peter.thekitchenmenu.domain.usecase.recipe.component.metadata.RecipeMetadataPersistenceModel;
import com.example.peter.thekitchenmenu.domain.usecase.recipe.macro.recipe.Recipe;

import org.junit.*;
import org.mockito.*;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class RecipeListTest {

    // region constants ----------------------------------------------------------------------------
    private static final String TAG = "tkm-" + RecipeListTest.class.getSimpleName() + ": ";
    // endregion constants -------------------------------------------------------------------------

    // region helper fields ------------------------------------------------------------------------
    @Mock
    UseCaseFactory useCaseFactoryMock;
    @Mock
    RepositoryRecipeMetadata repoMetadataMock;
    @Captor
    ArgumentCaptor<GetAllDomainModelsCallback<RecipeMetadataPersistenceModel>>
            repoMetadataGetAllCallback;

    private UseCaseHandler handler;

    private RecipeListResponse onSuccessResponse;
    private RecipeListResponse onErrorResponse;

    // endregion helper fields ---------------------------------------------------------------------

    private RecipeList SUT;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        handler = new UseCaseHandler(new UseCaseSchedulerMock());

        SUT = givenUseCase();
    }

    private RecipeList givenUseCase() {
        return new RecipeList(
                handler,
                useCaseFactoryMock,
                repoMetadataMock);
    }

    @Test
    public void newRequest_getAll_recipeListReturned() {
        // Arrange
        // A list of metadata persistent models to load into RecipeList
        List<RecipeMetadataPersistenceModel> metadataModels = TestDataRecipeMetadata.getAll();
        int expectedNoOfRecipes = metadataModels.size();

        // A helper that provides mock recipes and testing capabilities
        RecipeListTestRecipeHelper listHelper = new RecipeListTestRecipeHelper();
        listHelper.setUp();
        listHelper.createRecipeMocksForMetadataModels(metadataModels);

        // When a recipe is requested from the factory, return a recipe from the list helper
        List<Recipe> recipes = listHelper.getRecipeList();
        Recipe firstRecipe = recipes.remove(0);
        Recipe[] recipeArray = new Recipe[expectedNoOfRecipes - 1];
        recipeArray = recipes.toArray(recipeArray);
        when(useCaseFactoryMock.getRecipeUseCase()).thenReturn(firstRecipe, recipeArray);

        // A list request to return all recipes
        RecipeListRequest request = new RecipeListRequest.Builder().
                getDefault().
                setModel(new RecipeListRequest.Model.Builder().
                        getDefault().
                        setFilter(RecipeList.RecipeListFilter.ALL_RECIPES).
                        build()).
                build();

        // Act
        handler.executeAsync(SUT, request, new RecipeListClient());

        // Assert metadata models requested and return metadata model list
        verify(repoMetadataMock).getAll(repoMetadataGetAllCallback.capture());
        repoMetadataGetAllCallback.getValue().onAllLoaded(metadataModels);
        // Assert recipe components have loaded their data
        listHelper.requestRecipeComponentsLoadData();

        RecipeListResponse response = onSuccessResponse;
        RecipeListResponse.Model model = response.getModel();

        assertEquals(
                expectedNoOfRecipes,
                model.getRecipes().size()
        );
    }

    // region helper methods -----------------------------------------------------------------------
    // endregion helper methods --------------------------------------------------------------------

    // region helper classes -----------------------------------------------------------------------
    private final class RecipeListClient
            implements
            UseCaseBase.Callback<RecipeListResponse> {

        private final String TAG = RecipeListTest.TAG + RecipeListClient.class.
                getSimpleName() + ": ";

        @Override
        public void onUseCaseSuccess(RecipeListResponse response) {
            System.out.println(TAG + "onSuccess: " + response);
            onSuccessResponse = response;
        }

        @Override
        public void onUseCaseError(RecipeListResponse response) {
            System.out.println(TAG + "onError: " + response);
            onErrorResponse = response;
        }
    }
    // endregion helper classes --------------------------------------------------------------------
}