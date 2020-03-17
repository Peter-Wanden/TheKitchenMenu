package com.example.peter.thekitchenmenu.domain.usecase.recipe.macro.recipe;

import androidx.core.util.Pair;

import com.example.peter.thekitchenmenu.domain.usecase.CommonFailReason;
import com.example.peter.thekitchenmenu.domain.usecase.UseCase;
import com.example.peter.thekitchenmenu.domain.usecase.UseCaseHandler;
import com.example.peter.thekitchenmenu.domain.usecase.recipe.component.RecipeComponentRequest;
import com.example.peter.thekitchenmenu.domain.usecase.recipe.component.RecipeComponentResponse;
import com.example.peter.thekitchenmenu.domain.usecase.recipe.macro.RecipeUseCaseCallback;
import com.example.peter.thekitchenmenu.domain.usecase.recipe.metadata.RecipeMetadata;
import com.example.peter.thekitchenmenu.domain.usecase.recipe.metadata.RecipeMetadataRequest;
import com.example.peter.thekitchenmenu.domain.usecase.recipe.metadata.RecipeMetadataResponse;
import com.example.peter.thekitchenmenu.domain.usecase.recipe.component.recipecourse.RecipeCourseRequest;
import com.example.peter.thekitchenmenu.domain.usecase.recipe.component.recipecourse.RecipeCourseResponse;
import com.example.peter.thekitchenmenu.domain.usecase.recipe.component.recipeduration.RecipeDurationRequest;
import com.example.peter.thekitchenmenu.domain.usecase.recipe.component.recipeduration.RecipeDurationResponse;
import com.example.peter.thekitchenmenu.domain.usecase.recipe.component.recipeidentity.RecipeIdentityRequest;
import com.example.peter.thekitchenmenu.domain.usecase.recipe.component.recipeidentity.RecipeIdentityResponse;
import com.example.peter.thekitchenmenu.domain.usecase.recipe.component.recipeportions.RecipePortionsRequest;
import com.example.peter.thekitchenmenu.domain.usecase.recipe.component.recipeportions.RecipePortionsResponse;
import com.example.peter.thekitchenmenu.domain.usecase.recipe.state.RecipeStateCalculator;
import com.example.peter.thekitchenmenu.domain.usecase.recipe.state.RecipeStateRequest;
import com.example.peter.thekitchenmenu.domain.usecase.recipe.state.RecipeStateResponse;
import com.example.peter.thekitchenmenu.domain.usecase.recipe.component.recipeidentity.RecipeIdentity;
import com.example.peter.thekitchenmenu.domain.usecase.recipe.component.recipecourse.RecipeCourse;
import com.example.peter.thekitchenmenu.domain.usecase.recipe.component.recipeduration.RecipeDuration;
import com.example.peter.thekitchenmenu.domain.usecase.recipe.component.recipeportions.RecipePortions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

import static com.example.peter.thekitchenmenu.domain.usecase.recipe.state.RecipeStateCalculator.*;
import static com.example.peter.thekitchenmenu.domain.usecase.recipe.state.RecipeStateCalculator.ComponentName.*;

/**
 * Acts as a command mediator enabling recipe components to work together
 */
public class Recipe extends UseCase {

    private static final String TAG = "tkm-" + Recipe.class.getSimpleName() + ": ";

    enum RequestType {
        CREATE,
        EDIT,
        FAVORITE,
        COPY,
        DELETE
    }

    public static final String CREATE_NEW_RECIPE = "CREATE_NEW_RECIPE";

    public interface RecipeStateListener {
        void recipeStateChanged(RecipeStateResponse response);
    }

    private final UseCaseHandler handler;

    private final RecipeMetadata recipeMetaData;
    private final RecipeIdentity identity;
    private final RecipeCourse course;
    private final RecipeDuration duration;
    private final RecipePortions portions;
    private final RecipeStateCalculator recipeStateCalculator;

    private RecipeResponse recipeResponse;
    private RecipeStateResponse recipeStateResponse;
    private ComponentName requestOriginator;
    private String id = "";

    private final List<Pair<ComponentName, UseCase.Callback<? extends RecipeComponentResponse>>>
            componentCallbacks = new ArrayList<>();
    private final HashMap<ComponentName, UseCase.Response> componentResponses =
            new LinkedHashMap<>();
    private final HashMap<ComponentName, ComponentState> componentStates =
            new LinkedHashMap<>();
    private final List<RecipeStateListener> stateListeners =
            new ArrayList<>();
    private final List<UseCase.Callback<RecipeResponse>> macroCallbacks =
            new ArrayList<>();

    public Recipe(UseCaseHandler handler,
                  RecipeStateCalculator recipeStateCalculator,
                  RecipeMetadata recipeMetaData,
                  RecipeIdentity identity,
                  RecipeCourse course,
                  RecipeDuration duration,
                  RecipePortions portions) {

        this.handler = handler;
        this.recipeStateCalculator = recipeStateCalculator;
        recipeStateResponse = RecipeStateResponse.Builder.getDefault().build();
        recipeResponse = RecipeResponse.Builder.getDefault().build();
        this.recipeMetaData = recipeMetaData;
        this.identity = identity;
        this.course = course;
        this.duration = duration;
        this.portions = portions;
    }

    @Override
    public <Q extends Request> void execute(Q request) {

        if (request instanceof RecipeRequest) {
            requestOriginator = RECIPE_MACRO;
            processRecipeMacroRequest((RecipeRequest) request);
            return;
        }

        RecipeComponentRequest recipeRequest = (RecipeComponentRequest) request;

        if (request instanceof RecipeMetadataRequest) {
            requestOriginator = RECIPE_METADATA;
        } else if (request instanceof RecipeIdentityRequest) {
            requestOriginator = IDENTITY;
        } else if (request instanceof RecipeCourseRequest) {
            requestOriginator = COURSE;
        } else if (request instanceof RecipeDurationRequest) {
            requestOriginator = DURATION;
        } else if (request instanceof RecipePortionsRequest) {
            requestOriginator = PORTIONS;
        }

        if (isNewRequest(recipeRequest.getId())) {
            id = recipeRequest.getId();
            startComponents();

        } else if (RECIPE_METADATA.equals(requestOriginator)) {
            processRecipeRequest((RecipeMetadataRequest) request);
        } else if (IDENTITY.equals(requestOriginator)) {
            processIdentityRequest((RecipeIdentityRequest) request);
        } else if (COURSE.equals(requestOriginator)) {
            processCourseRequest((RecipeCourseRequest) request);
        } else if (DURATION.equals(requestOriginator)) {
            processDurationRequest((RecipeDurationRequest) request);
        } else if (PORTIONS.equals(requestOriginator)) {
            processPortionsRequest((RecipePortionsRequest) request);
        }
    }

    private boolean isNewRequest(String recipeId) {
        return !this.id.equals(recipeId);
    }

    private void processRecipeMacroRequest(RecipeRequest request) {
        id = request.getRecipeId();
        startComponents();
    }

    private void startComponents() {
        componentResponses.clear();
        componentStates.clear();

        startRecipeMetaDataComponent();
        startIdentityComponent();
        startCourseComponent();
        startDurationComponent();
        startPortionsComponent();
    }

    private void startRecipeMetaDataComponent() {
        handler.execute(
                recipeMetaData,
                new RecipeMetadataRequest.Builder().
                        setId(id).
                        build(),
                new RecipeCallback()
        );
    }

    private void processRecipeRequest(RecipeMetadataRequest request) {
        handler.execute(recipeMetaData, request, new RecipeCallback()
        );
    }

    private class RecipeCallback extends RecipeUseCaseCallback<RecipeMetadataResponse> {
        @Override
        protected void processResponse(RecipeMetadataResponse response) {
            addComponentResponse(RECIPE_METADATA, response);
            checkComponentsUpdated();
        }
    }

    private void startIdentityComponent() {
        handler.execute(
                identity,
                new RecipeIdentityRequest.Builder().
                        setId(id).
                        build(),
                new IdentityCallback()
        );
    }

    private void processIdentityRequest(RecipeIdentityRequest request) {
        handler.execute(identity, request, new IdentityCallback()
        );
    }

    private class IdentityCallback extends RecipeUseCaseCallback<RecipeIdentityResponse> {
        @Override
        protected void processResponse(RecipeIdentityResponse response) {
            addComponentState(IDENTITY, response.getMetadata().getState());
            addComponentResponse(IDENTITY, response);
            checkComponentsUpdated();
        }
    }

    private void startCourseComponent() {
        handler.execute(
                course,
                new RecipeCourseRequest.Builder().
                        setId(id).
                        build(),
                new CourseCallback()
        );
    }

    private void processCourseRequest(RecipeCourseRequest request) {
        handler.execute(course, request, new CourseCallback());
    }

    private class CourseCallback extends RecipeUseCaseCallback<RecipeCourseResponse> {
        @Override
        protected void processResponse(RecipeCourseResponse response) {
            addComponentState(COURSE, response.getMetadata().getState());
            addComponentResponse(COURSE, response);
            checkComponentsUpdated();
        }
    }

    private void startDurationComponent() {
        handler.execute(
                duration,
                new RecipeDurationRequest.Builder().
                        getDefault().
                        setId(id).
                        build(),
                new DurationCallback()
        );
    }

    private void processDurationRequest(RecipeDurationRequest request) {
        handler.execute(duration, request, new DurationCallback()
        );
    }

    private class DurationCallback extends RecipeUseCaseCallback<RecipeDurationResponse> {
        @Override
        protected void processResponse(RecipeDurationResponse response) {
            addComponentState(DURATION, response.getMetadata().getState());
            addComponentResponse(DURATION, response);
            checkComponentsUpdated();
        }
    }

    private void startPortionsComponent() {
        handler.execute(
                portions,
                new RecipePortionsRequest.Builder().
                        setId(id).
                        build(),
                new PortionsCallback()
        );
    }

    private void processPortionsRequest(RecipePortionsRequest request) {
        handler.execute(portions, request, new PortionsCallback()
        );
    }

    private class PortionsCallback extends RecipeUseCaseCallback<RecipePortionsResponse> {
        @Override
        protected void processResponse(RecipePortionsResponse response) {
            addComponentState(PORTIONS, response.getMetadata().getState());
            addComponentResponse(PORTIONS, response);
            checkComponentsUpdated();
        }
    }

    private void addComponentState(ComponentName componentName, ComponentState componentState) {
        componentStates.put(componentName, componentState);
    }

    private void addComponentResponse(ComponentName componentName, UseCase.Response response) {
        componentResponses.put(componentName, response);
    }

    private void checkComponentsUpdated() {
        System.out.println(TAG + "componentStatesUpdated: " + componentResponses.keySet());
        if (isAllComponentsUpdated()) {
            updateRecipeState();
        }
    }

    private boolean isAllComponentsUpdated() {
        return componentResponses.containsKey(RECIPE_METADATA) &&
                componentResponses.containsKey(IDENTITY) &&
                componentResponses.containsKey(COURSE) &&
                componentResponses.containsKey(DURATION) &&
                componentResponses.containsKey(PORTIONS);
    }

    private void updateRecipeState() {
        System.out.println(TAG + "updateRecipeState called");
        RecipeStateRequest request = new RecipeStateRequest(componentStates);
        handler.execute(
                recipeStateCalculator,
                request,
                new RecipeStateCallback()
        );
    }

    private class RecipeStateCallback extends RecipeUseCaseCallback<RecipeStateResponse> {
        @Override
        protected void processResponse(RecipeStateResponse response) {
            notifyStateListeners(response);
        }
    }

    public void registerStateListener(RecipeStateListener listener) {
        stateListeners.add(listener);
    }

    private void notifyStateListeners(RecipeStateResponse response) {
        if (!recipeStateResponse.equals(response)) {
            recipeStateResponse = response;
            for (RecipeStateListener listener : stateListeners) {
                listener.recipeStateChanged(response);
            }
        }
        notifyMacroCallbacks();
    }

    public void unregisterStateListener(RecipeStateListener listener) {
        stateListeners.remove(listener);
    }

    public void registerMacroCallback(UseCase.Callback<RecipeResponse> callback) {
        macroCallbacks.add(callback);
    }

    private void notifyMacroCallbacks() {
        RecipeResponse response = getMacroResponse();

        if (isMacroResponseChanged(response)) {
            for (UseCase.Callback<RecipeResponse> callback : macroCallbacks) {
                if (isValid()) {
                    callback.onSuccess(response);
                } else {
                    callback.onError(response);
                }
            }
        }
        notifyComponentCallbacks();
    }

    private void unregisterMacroCallback(UseCase.Callback<RecipeResponse> callback) {
        macroCallbacks.remove(callback);
    }

    private RecipeResponse getMacroResponse() {
        return new RecipeResponse.Builder().
                setId(id).
                setRecipeStateResponse(recipeStateResponse).
                setComponentResponses(componentResponses).
                build();
    }

    private boolean isMacroResponseChanged(RecipeResponse recipeResponse) {
        return !this.recipeResponse.equals(recipeResponse);
    }

    private boolean isValid() {
        return recipeStateResponse.getFailReasons().contains(FailReason.NONE);
    }

    public void registerComponentCallback(Pair<ComponentName, UseCase.Callback
            <? extends RecipeComponentResponse>> callback) {
        componentCallbacks.add(callback);
    }

    private void notifyComponentCallbacks() {
        System.out.println(TAG + "notifyComponentCallbacks called:" + componentCallbacks);
        System.out.println(TAG + "componentResponses:" + componentResponses);

        for (Pair<ComponentName, UseCase.Callback<? extends RecipeComponentResponse>> callbackPair :
                componentCallbacks) {
            ComponentName componentName = callbackPair.first;

            if (RECIPE_METADATA.equals(componentName)) {
                RecipeMetadataResponse response = (RecipeMetadataResponse)
                        componentResponses.get(componentName);
                //noinspection unchecked
                UseCase.Callback<RecipeMetadataResponse> callback =
                        (UseCase.Callback<RecipeMetadataResponse>) callbackPair.second;

                if (response.getMetadata().getFailReasons().contains(CommonFailReason.NONE)) {
                    callback.onSuccess(response);
                } else {
                    callback.onError(response);
                }

            } else if (IDENTITY.equals(componentName)) {
                RecipeIdentityResponse response = (RecipeIdentityResponse)
                        componentResponses.get(componentName);
                //noinspection unchecked
                UseCase.Callback<RecipeIdentityResponse> callback =
                        (UseCase.Callback<RecipeIdentityResponse>) callbackPair.second;

                if (response.getMetadata().getFailReasons().contains(CommonFailReason.NONE)) {
                    callback.onSuccess(response);
                } else {
                    callback.onError(response);
                }

            } else if (COURSE.equals(componentName)) {
                RecipeCourseResponse response = (RecipeCourseResponse)
                        componentResponses.get(componentName);

                //noinspection unchecked
                UseCase.Callback<RecipeCourseResponse> callback =
                        (UseCase.Callback<RecipeCourseResponse>) callbackPair.second;

                if (response.getMetadata().getFailReasons().contains(CommonFailReason.NONE)) {
                    callback.onSuccess(response);
                } else {
                    callback.onError(response);
                }

            } else if (DURATION.equals(componentName)) {
                RecipeDurationResponse response = (RecipeDurationResponse)
                        componentResponses.get(componentName);
                //noinspection unchecked
                UseCase.Callback<RecipeDurationResponse> callback =
                        (UseCase.Callback<RecipeDurationResponse>) callbackPair.second;

                if (response.getMetadata().getFailReasons().contains(CommonFailReason.NONE)) {
                    callback.onSuccess(response);
                } else {
                    callback.onError(response);
                }

            } else if (PORTIONS.equals(componentName)) {
                RecipePortionsResponse response = (RecipePortionsResponse)
                        componentResponses.get(componentName);
                //noinspection unchecked
                UseCase.Callback<RecipePortionsResponse> callback =
                        (UseCase.Callback<RecipePortionsResponse>) callbackPair.second;

                if (response.getMetadata().getFailReasons().contains(CommonFailReason.NONE)) {
                    callback.onSuccess(response);
                } else {
                    callback.onError(response);
                }
            }
        }
        notifyRequestOriginator();
    }

    private void unregisterComponentCallback(Pair<ComponentName, UseCase.Callback
            <? extends UseCase.Response>> callback) {
        componentCallbacks.remove(callback);
    }

    private void notifyRequestOriginator() {
        System.out.println(TAG + "notifyRequestOriginator called:" + requestOriginator);

        switch (requestOriginator) {
            case RECIPE_MACRO:
                RecipeResponse macroResponse = (RecipeResponse) componentResponses.
                        get(RECIPE_MACRO);

                if (macroResponse.getRecipeStateResponse().getFailReasons().
                        contains(CommonFailReason.NONE)) {

                    getUseCaseCallback().onSuccess(macroResponse);
                } else {
                    getUseCaseCallback().onError(macroResponse);
                }
                break;

            case RECIPE_METADATA:
                RecipeMetadataResponse metadataResponse = (RecipeMetadataResponse)
                        componentResponses.get(RECIPE_METADATA);

                if (metadataResponse.getMetadata().getFailReasons().
                        contains(CommonFailReason.NONE)) {

                    getUseCaseCallback().onSuccess(metadataResponse);
                } else {
                    getUseCaseCallback().onError(metadataResponse);
                }
                break;

            case IDENTITY:
                RecipeIdentityResponse identityResponse = (RecipeIdentityResponse)
                        componentResponses.get(IDENTITY);

                if (identityResponse.getMetadata().getFailReasons().
                        contains(CommonFailReason.NONE)) {

                    getUseCaseCallback().onSuccess(identityResponse);
                } else {
                    getUseCaseCallback().onError(identityResponse);
                }
                break;

            case COURSE:
                RecipeCourseResponse courseResponse = (RecipeCourseResponse)
                        componentResponses.get(COURSE);

                if (courseResponse.getMetadata().getFailReasons().
                        contains(CommonFailReason.NONE)) {

                    getUseCaseCallback().onSuccess(courseResponse);
                } else {
                    getUseCaseCallback().onError(courseResponse);
                }
                break;

            case DURATION:
                RecipeDurationResponse durationResponse = (RecipeDurationResponse)
                        componentResponses.get(DURATION);

                if (durationResponse.getMetadata().getFailReasons().
                        contains(CommonFailReason.NONE)) {

                    getUseCaseCallback().onSuccess(durationResponse);
                } else {
                    getUseCaseCallback().onError(durationResponse);
                }
                break;

            case PORTIONS:
                RecipePortionsResponse portionsResponse = (RecipePortionsResponse)
                        componentResponses.get(PORTIONS);

                if (portionsResponse.getMetadata().getFailReasons().
                        contains(CommonFailReason.NONE)) {
                    getUseCaseCallback().onSuccess(portionsResponse);
                } else {
                    getUseCaseCallback().onError(portionsResponse);
                }
            default:
                UnsupportedOperationException e = new UnsupportedOperationException(
                        "Recipe component not recognised"
                );
                e.printStackTrace();
        }
    }
}
