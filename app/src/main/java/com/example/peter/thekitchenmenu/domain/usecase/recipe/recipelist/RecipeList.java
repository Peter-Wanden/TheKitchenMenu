package com.example.peter.thekitchenmenu.domain.usecase.recipe.recipelist;

import com.example.peter.thekitchenmenu.data.repository.DomainDataAccess;
import com.example.peter.thekitchenmenu.data.repository.recipe.RepositoryRecipeMetadata;
import com.example.peter.thekitchenmenu.domain.usecase.UseCase;
import com.example.peter.thekitchenmenu.domain.usecase.UseCaseFactory;
import com.example.peter.thekitchenmenu.domain.usecase.UseCaseHandler;
import com.example.peter.thekitchenmenu.domain.usecase.recipe.component.metadata.RecipeMetadataPersistenceModel;
import com.example.peter.thekitchenmenu.domain.usecase.recipe.macro.RecipeUseCaseCallback;
import com.example.peter.thekitchenmenu.domain.usecase.recipe.macro.recipe.Recipe;
import com.example.peter.thekitchenmenu.domain.usecase.recipe.macro.recipe.RecipeRequest;
import com.example.peter.thekitchenmenu.domain.usecase.recipe.macro.recipe.RecipeResponse;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nonnull;

public class RecipeList
        extends
        UseCase
        implements
        DomainDataAccess.GetAllDomainModelsCallback<RecipeMetadataPersistenceModel> {

    private static final String TAG = "tkm:" + RecipeList.class.getSimpleName() +
            ": ";

    public enum RecipeListFilter {
        NONE,
        ALL_RECIPES,
        FAVORITE_RECIPES
    }

    public enum ResultStatus {
        DATA_LOADING_ERROR,
        DATA_NOT_AVAILABLE,
        RESULT_OK
    }

    @Nonnull
    private final UseCaseFactory factory;
    @Nonnull
    private final UseCaseHandler handler;
    @Nonnull
    private final RepositoryRecipeMetadata repository;

    private List<Recipe> recipes;

    private RecipeListRequest.Model requestModel;
    private RecipeListFilter filter;
    private boolean isNewRequest;
    private int accessCount;

    public RecipeList(@Nonnull UseCaseHandler handler,
                      @Nonnull UseCaseFactory factory,
                      @Nonnull RepositoryRecipeMetadata repository) {
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
        requestModel = r.getModel();
        System.out.println(TAG + "Request No: " + accessCount + " - " + r);

        if (isNewRequest(r)) {
            filter = requestModel.getFilter();
            loadData();
        }
    }

    private boolean isNewRequest(RecipeListRequest r) {
        return isNewRequest = filter != requestModel.getFilter();
    }

    private void loadData() {
        if (filter == RecipeListFilter.ALL_RECIPES) {
            repository.getAll(this);
        }
    }

    @Override
    public void onAllLoaded(List<RecipeMetadataPersistenceModel> models) {
        models.forEach((metadataModel) -> {
            Recipe recipe = factory.getRecipeUseCase();
            RecipeRequest request = new RecipeRequest.Builder().getDefault().build();
            handler.execute(recipe, request, new RecipeUseCaseCallback<RecipeResponse>() {
                @Override
                protected void processResponse(RecipeResponse response) {
                    recipes.add(recipe);
                    if (models.size() == recipes.size()) {
                        buildResponse();
                    }
                }
            });
        });
    }

    @Override
    public void onModelsUnavailable() {

    }

    private void buildResponse() {
        
    }
}
