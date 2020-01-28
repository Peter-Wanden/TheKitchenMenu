package com.example.peter.thekitchenmenu.domain.usecase.recipe;

import com.example.peter.thekitchenmenu.domain.usecase.UseCase;
import com.example.peter.thekitchenmenu.domain.usecase.UseCaseCommand;
import com.example.peter.thekitchenmenu.domain.usecase.UseCaseHandler;
import com.example.peter.thekitchenmenu.domain.usecase.recipe.recipestate.RecipeStateCalculator;
import com.example.peter.thekitchenmenu.domain.usecase.recipe.recipestate.RecipeStateRequest;
import com.example.peter.thekitchenmenu.domain.usecase.recipe.recipestate.RecipeStateResponse;
import com.example.peter.thekitchenmenu.domain.usecase.recipeidentity.RecipeIdentity;
import com.example.peter.thekitchenmenu.domain.usecase.recipeidentity.RecipeIdentityRequest;
import com.example.peter.thekitchenmenu.domain.usecase.recipeidentity.RecipeIdentityResponse;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

import static com.example.peter.thekitchenmenu.domain.usecase.recipe.recipestate.RecipeStateCalculator.*;

public class Recipe<Q extends UseCaseCommand.Request> extends UseCase<Q, RecipeResponse> {

    private static final String TAG = "tkm-" + Recipe.class.getSimpleName() + ": ";

    public interface Listener {
        void recipeStateChanged(RecipeStateResponse response);
    }

    private String recipeId = "";
    public static final String DO_NOT_CLONE = "";

    private final UseCaseHandler handler;

    private final RecipeStateCalculator stateCalculator;
    private RecipeStateResponse recipeStateResponse;
    private final RecipeIdentity identity;
    private RecipeIdentityResponse identityResponse;

    private final List<Listener> recipeClientListeners = new ArrayList<>();
    private final HashMap<ComponentName, ComponentState> componentStates = new LinkedHashMap<>();

    public Recipe(UseCaseHandler handler,
                  RecipeStateCalculator stateCalculator,
                  RecipeIdentity identity) {
        this.handler = handler;
        this.stateCalculator = stateCalculator;
        this.identity = identity;
    }

    @Override
    protected void execute(Q request) {
        if (request instanceof RecipeRequest) {
            RecipeRequest recipeRequest = (RecipeRequest) request;

            if (isNewRequest(recipeRequest.getRecipeId())) {
                startComponents(recipeRequest.getRecipeId());
            } else {
                sendResponse();
            }

        } else if (request instanceof RecipeIdentityRequest) {
            RecipeIdentityRequest identityRequest = (RecipeIdentityRequest) request;

            if (isNewRequest(identityRequest.getRecipeId())) {
                startComponents(identityRequest.getRecipeId());
            } else {
                handler.execute(identity, identityRequest, getIdentityCallback());
            }
        }
    }

    private void startComponents(String recipeId) {
        System.out.println(TAG + "startComponentsCalled with id:" + recipeId);
        handler.execute(
                identity,
                RecipeIdentityRequest.Builder.getDefault().setRecipeId(recipeId).build(),
                getIdentityCallback());
    }

    private UseCase.Callback<RecipeIdentityResponse> getIdentityCallback() {
        return new UseCase.Callback<RecipeIdentityResponse>() {
            @Override
            public void onSuccess(RecipeIdentityResponse response) {
                System.out.println(TAG + "identityResponseOnSuccess:" + response);
                identityResponse = response;
                componentStates.put(ComponentName.IDENTITY, response.getState());
                updateRecipeState();
            }
            @Override
            public void onError(RecipeIdentityResponse response) {
                System.out.println(TAG + "identityResponseOnError:" + response);
                identityResponse = response;
                componentStates.put(ComponentName.IDENTITY, response.getState());
                updateRecipeState();
            }
        };
    }

    private boolean isNewRequest(String recipeId) {
        if (this.recipeId.equals(recipeId)) {
            System.out.println(TAG + "isExistingRequest");
            return false;
        } else {
            this.recipeId = recipeId;
            System.out.println(TAG + "isNewRequest");
            return true;
        }
    }

    private void updateRecipeState() {
        RecipeStateRequest recipeStateRequest = new RecipeStateRequest(componentStates);
        handler.execute(stateCalculator, recipeStateRequest, getStateRequestCallback());
    }

    private UseCase.Callback<RecipeStateResponse> getStateRequestCallback() {
        return new UseCase.Callback<RecipeStateResponse>() {
            @Override
            public void onSuccess(RecipeStateResponse response) {
                System.out.println(TAG + "recipeStateResponseOnSuccess:" + response);
                recipeStateResponse = response;
                notifyClientListeners();
            }
            @Override
            public void onError(RecipeStateResponse response) {
                System.out.println(TAG + "recipeStateResponseOnError:" + response);
                recipeStateResponse = response;
                notifyClientListeners();
            }
        };
    }

    private void notifyClientListeners() {
        for (Listener listener : recipeClientListeners) {
            listener.recipeStateChanged(recipeStateResponse);
        }
        sendResponse();
    }

    public void registerClientListener(Listener listener) {
        recipeClientListeners.add(listener);
    }

    public void unRegisterClientListener(Listener listener) {
        recipeClientListeners.remove(listener);
    }

    private void sendResponse() {
        RecipeResponse response = new RecipeResponse(
                recipeStateResponse.getState(),
                recipeStateResponse.getFailReasons(),
                identityResponse
        );

        if (isRecipeStateResponseValid()) {
            getUseCaseCallback().onSuccess(response);
        } else {
            getUseCaseCallback().onError(response);
        }
    }

    private boolean isRecipeStateResponseValid() {
        RecipeState recipeState = recipeStateResponse.getState();
        return recipeState == RecipeState.VALID_UNCHANGED || recipeState == RecipeState.VALID_CHANGED;
    }
}
