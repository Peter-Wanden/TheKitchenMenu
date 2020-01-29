package com.example.peter.thekitchenmenu.domain.usecase.recipe;

import com.example.peter.thekitchenmenu.domain.usecase.UseCase;
import com.example.peter.thekitchenmenu.domain.usecase.UseCaseCommand;
import com.example.peter.thekitchenmenu.domain.usecase.UseCaseHandler;
import com.example.peter.thekitchenmenu.domain.usecase.recipe.recipestate.RecipeStateCalculator;
import com.example.peter.thekitchenmenu.domain.usecase.recipe.recipestate.RecipeStateRequest;
import com.example.peter.thekitchenmenu.domain.usecase.recipe.recipestate.RecipeStateResponse;
import com.example.peter.thekitchenmenu.domain.usecase.recipe.recipeidentity.RecipeIdentity;
import com.example.peter.thekitchenmenu.domain.usecase.recipe.recipeidentity.RecipeIdentityRequest;
import com.example.peter.thekitchenmenu.domain.usecase.recipe.recipeidentity.RecipeIdentityResponse;
import com.example.peter.thekitchenmenu.domain.usecase.recipe.recipecourse.RecipeCourse;
import com.example.peter.thekitchenmenu.domain.usecase.recipe.recipecourse.RecipeCourseRequest;
import com.example.peter.thekitchenmenu.domain.usecase.recipe.recipecourse.RecipeCourseResponse;
import com.example.peter.thekitchenmenu.domain.usecase.recipeduration.RecipeDuration;
import com.example.peter.thekitchenmenu.domain.usecase.recipeduration.RecipeDurationRequest;
import com.example.peter.thekitchenmenu.domain.usecase.recipeduration.RecipeDurationResponse;

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

    private final RecipeStateCalculator recipeStateCalculator;
    private RecipeStateResponse recipeStateResponse;

    private final RecipeIdentity identity;
    private RecipeIdentityResponse identityResponse;
    private final RecipeCourse courses;
    private RecipeCourseResponse courseResponse;
    private final RecipeDuration duration;
    private RecipeDurationResponse durationResponse;

    private final List<Listener> recipeClientListeners = new ArrayList<>();
    private final HashMap<ComponentName, ComponentState> componentStates = new LinkedHashMap<>();

    public Recipe(UseCaseHandler handler,
                  RecipeStateCalculator recipeStateCalculator,
                  RecipeIdentity identity,
                  RecipeCourse courses, RecipeDuration duration) {
        this.handler = handler;
        this.recipeStateCalculator = recipeStateCalculator;
        this.identity = identity;
        this.courses = courses;
        this.duration = duration;
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
        } else if (request instanceof RecipeCourseRequest) {

            RecipeCourseRequest courseRequest = (RecipeCourseRequest) request;
            if (isNewRequest(courseRequest.getRecipeId())) {
                startComponents(courseRequest.getRecipeId());
            } else {
                handler.execute(courses, courseRequest, getCoursesCallback());
            }
        } else if (request instanceof RecipeDurationRequest) {
            RecipeDurationRequest durationRequest = (RecipeDurationRequest) request;
            if (isNewRequest(durationRequest.getRecipeId())) {
                startComponents(durationRequest.getRecipeId());
            } else {
                handler.execute(duration, durationRequest, getDurationCallback());
            }
        }
    }

    private void startComponents(String recipeId) {
        componentStates.clear();

        handler.execute(
                identity,
                RecipeIdentityRequest.Builder.getDefault().setRecipeId(recipeId).build(),
                getIdentityCallback()
        );
        handler.execute(
                courses,
                RecipeCourseRequest.Builder.getDefault().setRecipeId(recipeId).build(),
                getCoursesCallback()
        );
        handler.execute(
                duration,
                RecipeDurationRequest.Builder.getDefault().setRecipeId(recipeId).build(),
                getDurationCallback()
        );
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

    private UseCase.Callback<RecipeCourseResponse> getCoursesCallback() {
        return new UseCase.Callback<RecipeCourseResponse>() {
            @Override
            public void onSuccess(RecipeCourseResponse response) {
                System.out.println(TAG + "coursesResponseOnSuccess:" + response);
                courseResponse = response;
                componentStates.put(ComponentName.COURSE, response.getState());
                updateRecipeState();
            }

            @Override
            public void onError(RecipeCourseResponse response) {
                System.out.println(TAG + "coursesResponseOnError:" + response);
                courseResponse = response;
                componentStates.put(ComponentName.COURSE, response.getState());
                updateRecipeState();
            }
        };
    }

    private UseCase.Callback<RecipeDurationResponse> getDurationCallback() {
        return new UseCase.Callback<RecipeDurationResponse>() {
            @Override
            public void onSuccess(RecipeDurationResponse response) {
                System.out.println(TAG + "durationResponseOnSuccess:" + response);
                durationResponse = response;
                componentStates.put(ComponentName.DURATION, response.getState());
                updateRecipeState();
            }

            @Override
            public void onError(RecipeDurationResponse response) {
                System.out.println(TAG + "durationResponseOnError:" + response);
                durationResponse = response;
                componentStates.put(ComponentName.DURATION, response.getState());
                updateRecipeState();
            }
        };
    }

    private void updateRecipeState() {
        if (isAllComponentStatesUpdated()) {
            RecipeStateRequest stateRequest = new RecipeStateRequest(componentStates);
            handler.execute(recipeStateCalculator, stateRequest, getStateRequestCallback());
        }
    }

    private boolean isAllComponentStatesUpdated() {
        return componentStates.containsKey(ComponentName.IDENTITY) &&
                componentStates.containsKey(ComponentName.COURSE) &&
                componentStates.containsKey(ComponentName.DURATION);
    }

    private UseCase.Callback<RecipeStateResponse> getStateRequestCallback() {
        return new UseCase.Callback<RecipeStateResponse>() {
            @Override
            public void onSuccess(RecipeStateResponse response) {
                System.out.println(TAG + "recipeStateResponseOnSuccess:" + response);
                if (!response.equals(recipeStateResponse)) {
                    recipeStateResponse = response;
                    notifyClientListeners();
                } else {
                    sendResponse();
                }
            }

            @Override
            public void onError(RecipeStateResponse response) {
                System.out.println(TAG + "recipeStateResponseOnError:" + response);
                if (!response.equals(recipeStateResponse)) {
                    recipeStateResponse = response;
                    notifyClientListeners();
                } else {
                    sendResponse();
                }
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
                identityResponse,
                courseResponse,
                durationResponse
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
