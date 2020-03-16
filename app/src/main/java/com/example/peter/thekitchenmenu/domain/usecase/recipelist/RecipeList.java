package com.example.peter.thekitchenmenu.domain.usecase.recipelist;

import com.example.peter.thekitchenmenu.domain.usecase.UseCase;
import com.example.peter.thekitchenmenu.domain.usecase.UseCaseFactory;
import com.example.peter.thekitchenmenu.domain.usecase.UseCaseHandler;
import com.example.peter.thekitchenmenu.domain.usecase.recipe.recipemacro.RecipeMacro;
import com.example.peter.thekitchenmenu.domain.usecase.recipe.recipemacro.RecipeMacroResponse;
import com.example.peter.thekitchenmenu.domain.usecase.recipe.recipemetadata.RecipeMetadataRequest;

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

    private final List<RecipeMacro> recipeMacros = new ArrayList<>();
    private final List<RecipeMacroResponse> recipeMacroRespons = new ArrayList<>();

    public RecipeList(UseCaseFactory factory,
                      UseCaseHandler handler) {
        this.factory = factory;
        this.handler = handler;
    }

    @Override
    protected <Q extends Request> void execute(Q request) {
        RecipeListRequest rlr = (RecipeListRequest) request;

        System.out.println(TAG + rlr);
        if (rlr.getFilter() == RecipeFilter.ALL) {
            getAllRecipes();
        } else if (rlr.getFilter() == RecipeFilter.FAVORITE)
            getFavorites();
    }

    private void getAllRecipes() {
        // TODO - Create a list of all recipeMacros using a RecipeMacro for each RecipeMacro :)

        RecipeMacro recipeMacro = factory.provideRecipeMacro();
        RecipeMetadataRequest request = new RecipeMetadataRequest.Builder().
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
