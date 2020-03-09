package com.example.peter.thekitchenmenu.domain.usecase.recipe.recipemacro.recipemacroclone;

import com.example.peter.thekitchenmenu.domain.usecase.FailReasons;
import com.example.peter.thekitchenmenu.domain.usecase.UseCase;
import com.example.peter.thekitchenmenu.domain.usecase.UseCaseHandler;
import com.example.peter.thekitchenmenu.domain.usecase.recipe.recipeidentity.RecipeIdentityRequest;
import com.example.peter.thekitchenmenu.domain.usecase.recipe.recipeidentity.RecipeIdentityResponse;
import com.example.peter.thekitchenmenu.domain.usecase.recipe.recipemacro.RecipeMacro;
import com.example.peter.thekitchenmenu.domain.usecase.recipe.recipemacro.RecipeMacroRequest;
import com.example.peter.thekitchenmenu.domain.usecase.recipe.recipemacro.RecipeMacroResponse;
import com.example.peter.thekitchenmenu.domain.usecase.recipe.recipemacro.RecipeMacroUseCaseCallback;
import com.example.peter.thekitchenmenu.domain.utils.UniqueIdProvider;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nonnull;

import static com.example.peter.thekitchenmenu.domain.usecase.recipe.recipestate.RecipeStateCalculator.ComponentName.IDENTITY;

public class RecipeMacroClone extends UseCase {

    enum FailReason implements FailReasons {
        SOURCE_DATA_ERROR
    }

    @Nonnull
    private UseCaseHandler handler;
    @Nonnull
    private UniqueIdProvider idProvider;
    @Nonnull
    private final RecipeMacro recipeSource;
    @Nonnull
    private final RecipeMacro recipeDestination;

    private RecipeMacroResponse sourceData;
    private RecipeMacroResponse destinationData;
    private boolean isDestinationInitialised;
    private List<FailReasons> failReasons = new ArrayList<>();

    public RecipeMacroClone(@Nonnull UseCaseHandler handler,
                            @Nonnull UniqueIdProvider idProvider,
                            @Nonnull RecipeMacro recipeSource,
                            @Nonnull RecipeMacro recipeDestination) {
        this.handler = handler;
        this.idProvider = idProvider;
        this.recipeSource = recipeSource;
        this.recipeDestination = recipeDestination;
    }

    @Override
    protected <Q extends Request> void execute(Q request) {
        initialiseDestinationRecipe();
        loadSourceRecipe(((RecipeCloneRequest) request).getId());
    }

    private void initialiseDestinationRecipe() {
        String destinationId = idProvider.getUId();
        RecipeMacroRequest request = new RecipeMacroRequest(destinationId);
        handler.execute(recipeDestination, request, new DestinationListener());
    }

    private class DestinationListener extends RecipeMacroUseCaseCallback<RecipeMacroResponse> {
        @Override
        protected void processResponse(RecipeMacroResponse response) {
            isDestinationInitialised = true;
            destinationData = response;
        }
    }

    private void loadSourceRecipe(String cloneFromId) {
        RecipeMacroRequest request = new RecipeMacroRequest(cloneFromId);
        handler.execute(recipeSource, request, new SourceListener());
    }

    private class SourceListener extends RecipeMacroUseCaseCallback<RecipeMacroResponse> {
        @Override
        protected void processResponse(RecipeMacroResponse response) {
            sourceData = response;
            cloneData();
        }
    }

    private void cloneData() {
        if (isDestinationInitialised) {
            cloneIdentity();
        }
    }

    private void cloneIdentity() {
        RecipeIdentityResponse identityResponse = (RecipeIdentityResponse) sourceData.
                getComponentResponses().
                get(IDENTITY);
        RecipeIdentityRequest request = RecipeIdentityRequest.Builder.
                basedOnResponse(identityResponse).
                setId(destinationData.getId()).
                build();
        handler.execute(recipeDestination, request, new IdentityListener());
    }

    private class IdentityListener extends RecipeMacroUseCaseCallback<RecipeIdentityResponse> {
        @Override
        protected void processResponse(RecipeIdentityResponse response) {
            System.out.println(response);
        }
    }
}
