package com.example.peter.thekitchenmenu.domain.usecase.recipemediator;

import com.example.peter.thekitchenmenu.domain.UseCaseCommand;
import com.example.peter.thekitchenmenu.domain.UseCaseHandler;
import com.example.peter.thekitchenmenu.domain.usecase.recipeduration.RecipeDuration;
import com.example.peter.thekitchenmenu.domain.usecase.recipeduration.RecipeDurationRequest;
import com.example.peter.thekitchenmenu.domain.usecase.recipeduration.RecipeDurationResponse;
import com.example.peter.thekitchenmenu.domain.usecase.recipeidentity.RecipeIdentity;
import com.example.peter.thekitchenmenu.domain.usecase.recipeidentity.RecipeIdentityModel;
import com.example.peter.thekitchenmenu.domain.usecase.recipeidentity.RecipeIdentityRequest;
import com.example.peter.thekitchenmenu.domain.usecase.recipeidentity.RecipeIdentityResponse;
import com.example.peter.thekitchenmenu.domain.usecase.recipestate.RecipeState;

import static com.example.peter.thekitchenmenu.domain.usecase.recipeidentity.RecipeIdentity.DO_NOT_CLONE;

/**
 * A recipe is made up of a complex set of independently changing data structures each controlled by
 * its own use case.
 * The {@link RecipeMediator} communicates with the use cases
 */
public class RecipeMediator extends RecipeMediatorAbstract<RecipeMediatorResponse> {

    private static final String TAG = "tkm-" + RecipeMediator.class.getSimpleName() + ": ";

    private final UseCaseHandler handler;

    private boolean isDurationLoading;
    private final RecipeIdentity identity;
    private boolean isIdentityLoading;
    private final RecipeDuration duration;

    private RecipeMediatorResponse response = new RecipeMediatorResponse();

    public RecipeMediator(UseCaseHandler handler,
                          RecipeIdentity identity,
                          RecipeDuration duration) {
        this.handler = handler;
        this.identity = identity;
        this.duration = duration;
    }

    public void getAllRecipeData(String recipeId,
                                 RecipeMediator.Callback<RecipeMediatorResponse> callback) {

        setMediatorCallback(callback);
        getIdentityData(recipeId);
        getDurationData(recipeId);
    }

    private void getIdentityData(String recipeId) {
        isIdentityLoading = true;
        RecipeIdentityRequest identityRequest = RecipeIdentityRequest.Builder.getDefault().
                setRecipeId(recipeId).
                build();

        handler.execute(
                identity,
                identityRequest,
                new UseCaseCommand.Callback<RecipeIdentityResponse>() {
            @Override
            public void onSuccess(RecipeIdentityResponse response) {
                isIdentityLoading = false;
                RecipeMediator.this.response.setIdentityResponse(response);
                sendGetAllResponse();
            }

            @Override
            public void onError(RecipeIdentityResponse response) {
                isIdentityLoading = false;
                RecipeMediator.this.response.setIdentityResponse(response);
                sendGetAllResponse();
            }
        });
    }

    private void getDurationData(String recipeId) {
        isDurationLoading = true;
        RecipeDurationRequest request = RecipeDurationRequest.Builder.getDefault().
                setRecipeId(recipeId).
                build();
        handler.execute(duration, request, new UseCaseCommand.Callback<RecipeDurationResponse>() {
            @Override
            public void onSuccess(RecipeDurationResponse response) {
                isDurationLoading = false;
                RecipeMediator.this.response.setDurationResponse(response);
                sendGetAllResponse();
            }

            @Override
            public void onError(RecipeDurationResponse response) {
                isDurationLoading = false;
                RecipeMediator.this.response.setDurationResponse(response);
                sendGetAllResponse();
            }
        });
    }

    private void sendGetAllResponse() {
        if (!isIdentityLoading && !isDurationLoading) {
            getMediatorCallback().onSuccess(response);
        }
    }

    public void executeDurationRequest(RecipeDurationRequest request,
                                       RecipeMediator.Callback<RecipeMediatorResponse> callback) {
        setMediatorCallback(callback);
        handler.execute(duration, request, new RecipeDuration.Callback<RecipeDurationResponse>() {

            @Override
            public void onSuccess(RecipeDurationResponse response) {
                updateRecipeState(response.getState());
                RecipeMediatorResponse mediatorResponse = new RecipeMediatorResponse();
                mediatorResponse.setDurationResponse(response);
                getMediatorCallback().onSuccess(mediatorResponse);
            }

            @Override
            public void onError(RecipeDurationResponse response) {
                updateRecipeState(response.getState());
                RecipeMediatorResponse mediatorResponse = new RecipeMediatorResponse();
                mediatorResponse.setDurationResponse(response);
                getMediatorCallback().onError(mediatorResponse);
            }
        });
    }

    private void updateRecipeState(RecipeState.ComponentState state) {
        // todo - update recipe state
    }
}
