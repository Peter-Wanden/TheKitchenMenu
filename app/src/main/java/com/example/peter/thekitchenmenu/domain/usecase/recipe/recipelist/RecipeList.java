package com.example.peter.thekitchenmenu.domain.usecase.recipe.recipelist;

import com.example.peter.thekitchenmenu.domain.usecase.UseCase;
import com.example.peter.thekitchenmenu.domain.usecase.UseCaseFactory;
import com.example.peter.thekitchenmenu.domain.usecase.UseCaseHandler;
import com.example.peter.thekitchenmenu.domain.usecase.recipe.component.metadata.RecipeMetadataRequest;
import com.example.peter.thekitchenmenu.domain.usecase.recipe.macro.recipe.Recipe;
import com.example.peter.thekitchenmenu.domain.usecase.recipe.macro.recipe.RecipeResponse;

import java.util.ArrayList;
import java.util.List;

public class RecipeList extends UseCase {

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

    private final List<Recipe> recipeMacros = new ArrayList<>();
    private final List<RecipeResponse> recipeResponse = new ArrayList<>();

    public RecipeList(UseCaseFactory factory,
                      UseCaseHandler handler) {
        this.factory = factory;
        this.handler = handler;
    }

    @Override
    protected <Q extends Request> void execute(Q request) {
        RecipeListRequest recipeListRequest = (RecipeListRequest) request;

        System.out.println(TAG + recipeListRequest);
        if (recipeListRequest.getFilter() == RecipeFilter.ALL) {
            getAllRecipes();
        } else if (recipeListRequest.getFilter() == RecipeFilter.FAVORITE)
            getFavorites();
    }

    private void getAllRecipes() {
        // TODO - Create a list of all recipeMacros using a RecipeMacro for each RecipeMacro :)

        Recipe recipeMacro = factory.provideRecipeMacro();
        RecipeMetadataRequest request = new RecipeMetadataRequest.Builder().
                getDefault().
                setDataId("recipeId").
                build();

        handler.execute(recipeMacro, request, getRecipeResponseCallback());
    }

    private UseCase.Callback<RecipeResponse> getRecipeResponseCallback() {
        return new UseCase.Callback<RecipeResponse>() {
            @Override
            public void onSuccess(RecipeResponse response) {
                recipeResponse.add(response);
            }

            @Override
            public void onError(RecipeResponse response) {
                recipeResponse.add(response);
            }
        };
    }

    private void getFavorites() {
        // TODO
    }
}
