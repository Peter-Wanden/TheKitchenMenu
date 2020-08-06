package com.example.peter.thekitchenmenu.domain.usecase.recipe.recipelist;

import com.example.peter.thekitchenmenu.data.repository.DomainDataAccess;
import com.example.peter.thekitchenmenu.data.repository.recipe.RecipeMetadataUseCaseDataAccess;
import com.example.peter.thekitchenmenu.domain.usecase.common.UseCaseBase;
import com.example.peter.thekitchenmenu.domain.usecase.factory.UseCaseFactory;
import com.example.peter.thekitchenmenu.domain.usecasenew.common.UseCaseHandler;
import com.example.peter.thekitchenmenu.domain.usecasenew.recipe.invoker.metadata.RecipeMacroMetadataUseCasePersistenceModel;
import com.example.peter.thekitchenmenu.domain.usecase.recipe.macro.RecipeUseCaseCallback;
import com.example.peter.thekitchenmenu.domain.usecase.recipe.macro.recipe.Recipe;
import com.example.peter.thekitchenmenu.domain.usecase.recipe.macro.recipe.RecipeRequest;
import com.example.peter.thekitchenmenu.domain.usecase.recipe.macro.recipe.RecipeResponse;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nonnull;

public class RecipeList
        extends
        UseCaseBase
        implements
        DomainDataAccess.GetAllDomainModelsCallback<RecipeMacroMetadataUseCasePersistenceModel> {

    private static final String TAG = "tkm:" + RecipeList.class.getSimpleName() +
            ": ";

    public enum RecipeListFilter {
        NONE,
        ALL_RECIPES,
        FAVORITE_RECIPES
    }

    @Nonnull
    private final UseCaseFactory factory;
    @Nonnull
    private final UseCaseHandler handler;
    @Nonnull
    private final RecipeMetadataUseCaseDataAccess repository;

    private List<Recipe> recipes;

    private RecipeListRequest.Model requestModel;
    private RecipeListFilter filter;
    private boolean isNewRequest;
    private int accessCount;

    public RecipeList(@Nonnull UseCaseHandler handler,
                      @Nonnull UseCaseFactory factory,
                      @Nonnull RecipeMetadataUseCaseDataAccess repository) {
        this.handler = handler;
        this.factory = factory;
        this.repository = repository;

        requestModel = new RecipeListRequest.Model.Builder().getDefault().build();
        filter = RecipeListFilter.NONE;
        recipes = new ArrayList<>();
    }

    @Override
    protected <Q extends Request> void execute(Q request) {
        accessCount ++;
        RecipeListRequest r = (RecipeListRequest) request;
        requestModel = r.getDomainModel();
        System.out.println(TAG + "Request No: " + accessCount + " - " + r);

        if (isNewRequest()) {
            filter = requestModel.getFilter();
            loadData();
        }
    }

    private boolean isNewRequest() {
        return isNewRequest = filter != requestModel.getFilter();
    }

    private void loadData() {
        System.out.println(TAG + "loadDataCalled");
        if (RecipeListFilter.ALL_RECIPES == filter) {
            repository.getAll(this);
        } else {
            throw new UnsupportedOperationException("RequestFilter not recognised: " + filter);
        }
    }

    @Override
    public void onAllDomainModelsLoaded(List<RecipeMacroMetadataUseCasePersistenceModel> metadataModels) {
        System.out.println(TAG + "onAllLoadedCalled");

        for (RecipeMacroMetadataUseCasePersistenceModel metadataModel : metadataModels) {
            Recipe recipe = factory.getRecipeUseCase();
            recipes.add(recipe);

            // Request the recipe load its data
            RecipeRequest request = new RecipeRequest.Builder().
                    getDefault().
                    setDomainId(metadataModel.getDomainId()).
                    build();

            handler.executeAsync(recipe, request, new RecipeUseCaseCallback<RecipeResponse>() {
                @Override
                protected void processResponse(RecipeResponse response) {
                    if (metadataModels.size() == recipes.size()) {
                        buildResponse();
                    }
                }
            });
        }
    }

    @Override
    public void onDomainModelsUnavailable() {
        getUseCaseCallback().onUseCaseError(new RecipeListResponse.Builder().getDefault().build());
    }

    private void buildResponse() {
        System.out.println(TAG + "buildResponse called");
        RecipeListResponse response = new RecipeListResponse.Builder().
                getDefault().
                setDomainModel(new RecipeListResponse.Model.Builder().
                        getDefault().
                        setRecipes(recipes).
                        build()).
                build();

        if (recipes.size() > 0) {
            getUseCaseCallback().onUseCaseSuccess(response);
        } else {
            getUseCaseCallback().onUseCaseError(response);
        }
    }
}
