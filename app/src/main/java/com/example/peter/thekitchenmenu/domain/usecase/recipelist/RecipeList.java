package com.example.peter.thekitchenmenu.domain.usecase.recipelist;

import com.example.peter.thekitchenmenu.domain.usecase.UseCase;
import com.example.peter.thekitchenmenu.domain.usecase.UseCaseFactory;
import com.example.peter.thekitchenmenu.domain.usecase.UseCaseHandler;
import com.example.peter.thekitchenmenu.domain.usecase.recipe.recipemacro.RecipeMacro;
import com.example.peter.thekitchenmenu.domain.usecase.recipe.recipemacro.RecipeMacroResponse;
import com.example.peter.thekitchenmenu.domain.usecase.recipe.recipe.RecipeRequest;

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

    private final List<RecipeMacro> recipeMacros = new ArrayList<>();
    private final List<RecipeMacroResponse> recipeMacroRespons = new ArrayList<>();

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
        // TODO - Create a list of all recipeMacros using a RecipeMacro for each RecipeMacro :)

        RecipeMacro<RecipeRequest, RecipeMacroResponse> recipeMacro = factory.provideRecipeMacro();
        RecipeRequest request = RecipeRequest.Builder.
                getDefault().
                setId("recipeId").
                build();

        handler.execute(recipeMacro, request, getRecipeResponseCallback());
    }

    private UseCase.Callback<RecipeMacroResponse> getRecipeResponseCallback() {
        return new UseCase.Callback<RecipeMacroResponse>() {
            @Override
            public void onSuccess(RecipeMacroResponse response) {
                recipeMacroRespons.add(response);
            }

            @Override
            public void onError(RecipeMacroResponse response) {
                recipeMacroRespons.add(response);
            }
        };
    }

    private void getFavorites() {
        // TODO
    }
}
