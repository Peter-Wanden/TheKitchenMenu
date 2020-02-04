package com.example.peter.thekitchenmenu.domain.usecase.recipelist;

import com.example.peter.thekitchenmenu.domain.usecase.UseCase;
import com.example.peter.thekitchenmenu.domain.usecase.UseCaseFactory;
import com.example.peter.thekitchenmenu.domain.usecase.UseCaseHandler;
import com.example.peter.thekitchenmenu.domain.usecase.recipe.Recipe;
import com.example.peter.thekitchenmenu.domain.usecase.recipe.RecipeRequest;
import com.example.peter.thekitchenmenu.domain.usecase.recipe.RecipeResponse;

import java.util.ArrayList;
import java.util.List;

public class RecipeList extends UseCase<RecipeListRequest, RecipeListResponse> {

    private static final String TAG = "tkm:" + RecipeList.class.getSimpleName() +
            ": ";

    public enum RecipeFilter {
        ALL,
        FAVORITE
    }

    public enum ResultStatus {
        DATA_LOADING_ERROR,
        DATA_NOT_AVAILABLE,
        RESULT_OK
    }

    private final UseCaseFactory factory;
    private final UseCaseHandler handler;

    private List<RecipeListItemModel> recipeListItemModels = new ArrayList<>();

    private final List<Recipe> recipes = new ArrayList<>();
    private final List<RecipeResponse> recipeResponses = new ArrayList<>();

    public RecipeList(UseCaseFactory factory,
                      UseCaseHandler handler) {
        this.factory = factory;
        this.handler = handler;
    }

    @Override
    protected void execute(RecipeListRequest request) {
        System.out.println(TAG + request);
        if (request.getFilter() == RecipeFilter.ALL) {
            getAllRecipes();
        } else if (request.getFilter() == RecipeFilter.FAVORITE)
            getFavorites();
    }

    private void getAllRecipes() {
        // TODO - Create a list of all recipes using a Recipe for each Recipe :)

        Recipe<RecipeRequest> recipe = factory.provideRecipe();
        RecipeRequest request = new RecipeRequest("recipeId");

        handler.execute(recipe, request, getRecipeResponseCallback());
    }

    private UseCase.Callback<RecipeResponse> getRecipeResponseCallback() {
        return new UseCase.Callback<RecipeResponse>() {
            @Override
            public void onSuccess(RecipeResponse response) {
                recipeResponses.add(response);
            }

            @Override
            public void onError(RecipeResponse response) {
                recipeResponses.add(response);
            }
        };
    }

    private void getFavorites() {
        // TODO
    }
}
