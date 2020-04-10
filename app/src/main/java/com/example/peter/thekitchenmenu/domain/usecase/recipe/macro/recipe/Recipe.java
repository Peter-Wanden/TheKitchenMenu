package com.example.peter.thekitchenmenu.domain.usecase.recipe.macro.recipe;


import androidx.core.util.Pair;

import com.example.peter.thekitchenmenu.domain.model.CommonFailReason;
import com.example.peter.thekitchenmenu.domain.model.FailReasons;
import com.example.peter.thekitchenmenu.domain.usecase.UseCase;
import com.example.peter.thekitchenmenu.domain.usecase.UseCaseHandler;
import com.example.peter.thekitchenmenu.domain.usecase.recipe.macro.RecipeUseCaseCallback;
import com.example.peter.thekitchenmenu.domain.usecase.recipe.metadata.RecipeMetadata;
import com.example.peter.thekitchenmenu.domain.usecase.recipe.metadata.RecipeMetadataRequest;
import com.example.peter.thekitchenmenu.domain.usecase.recipe.metadata.RecipeMetadataResponse;
import com.example.peter.thekitchenmenu.domain.usecase.recipe.component.course.RecipeCourseRequest;
import com.example.peter.thekitchenmenu.domain.usecase.recipe.component.course.RecipeCourseResponse;
import com.example.peter.thekitchenmenu.domain.usecase.recipe.component.duration.RecipeDurationRequest;
import com.example.peter.thekitchenmenu.domain.usecase.recipe.component.duration.RecipeDurationResponse;
import com.example.peter.thekitchenmenu.domain.usecase.recipe.component.identity.RecipeIdentityRequest;
import com.example.peter.thekitchenmenu.domain.usecase.recipe.component.identity.RecipeIdentityResponse;
import com.example.peter.thekitchenmenu.domain.usecase.recipe.component.portions.RecipePortionsRequest;
import com.example.peter.thekitchenmenu.domain.usecase.recipe.component.portions.RecipePortionsResponse;
import com.example.peter.thekitchenmenu.domain.usecase.recipe.component.identity.RecipeIdentity;
import com.example.peter.thekitchenmenu.domain.usecase.recipe.component.course.RecipeCourse;
import com.example.peter.thekitchenmenu.domain.usecase.recipe.component.duration.RecipeDuration;
import com.example.peter.thekitchenmenu.domain.usecase.recipe.component.portions.RecipePortions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

import static com.example.peter.thekitchenmenu.domain.usecase.recipe.metadata.RecipeMetadata.*;
import static com.example.peter.thekitchenmenu.domain.usecase.recipe.metadata.RecipeMetadata.ComponentName.*;

/**
 * A use case interactor acting as a command mediator enabling recipe components to work
 * together.
 * All recipe component requests should be routed through an instance of this class.
 * The first request sent through, regardless of type, will only load and return data. Subsequent
 * requests will perform the requested operation.
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

    public interface RecipeMetadataListener {
        void recipeStateChanged(RecipeMetadataResponse response);
    }

    private final UseCaseHandler handler;

    private final RecipeIdentity identity;
    private final RecipeCourse course;
    private final RecipeDuration duration;
    private final RecipePortions portions;
    private final RecipeMetadata recipeMetadata;

    private RecipeResponse recipeResponse;
    private RecipeMetadataResponse recipeMetadataResponse;
    private ComponentName requestOriginator;
    private String dataId = "";
    private String recipeId = "";

    private final HashMap<ComponentName, UseCase.Response>
            componentResponses = new LinkedHashMap<>();
    private final HashMap<ComponentName, ComponentState>
            componentStates = new LinkedHashMap<>();

    // Listeners
    private final List<Pair<ComponentName, Callback<? extends UseCase.Response>>>
            componentListeners = new ArrayList<>();
    private final List<RecipeMetadataListener>
            metaDataListeners = new ArrayList<>();
    private final List<UseCase.Callback<RecipeResponse>>
            recipeResponseListeners = new ArrayList<>();

    public Recipe(UseCaseHandler handler,
                  RecipeMetadata recipeMetadata,
                  RecipeIdentity identity,
                  RecipeCourse course,
                  RecipeDuration duration,
                  RecipePortions portions) {

        this.handler = handler;

        recipeResponse = new RecipeResponse.Builder().getDefault().build();
        recipeMetadataResponse = new RecipeMetadataResponse.Builder().getDefault().build();

        this.recipeMetadata = recipeMetadata;
        this.identity = identity;
        this.course = course;
        this.duration = duration;
        this.portions = portions;
    }

    @Override
    public <Q extends Request> void execute(Q request) {
        extractRequestOriginator(request);
        RecipeRequest r = (RecipeRequest) request;

        if (isNewRequest(r.getDomainId()) || RECIPE == requestOriginator) {
            dataId = r.getDataId();
            recipeId = r.getDataId();
            startComponents();
        } else {
            processRequest(request);
        }
    }

    private <Q extends Request> void extractRequestOriginator(Q request) {
        if (request instanceof RecipeRequest) {
            requestOriginator = RECIPE;
        } else if (request instanceof RecipeIdentityRequest) {
            requestOriginator = IDENTITY;
        } else if (request instanceof RecipeCourseRequest) {
            requestOriginator = COURSE;
        } else if (request instanceof RecipeDurationRequest) {
            requestOriginator = DURATION;
        } else if (request instanceof RecipePortionsRequest) {
            requestOriginator = PORTIONS;
        } else {
            throw new UnsupportedOperationException("Request type not supported:" + request);
        }
    }

    private boolean isNewRequest(String recipeId) {
        return !this.recipeId.equals(recipeId);
    }

    private void startComponents() {
        componentResponses.clear();
        componentStates.clear();

        startRecipeMetadataComponent();
        startIdentityComponent();
        startCourseComponent();
        startDurationComponent();
        startPortionsComponent();
    }

    private <Q extends Request> void processRequest(Q request) {
        switch (requestOriginator) {
            case IDENTITY:
                processIdentityRequest((RecipeIdentityRequest) request);
                break;
            case COURSE:
                processCourseRequest((RecipeCourseRequest) request);
                break;
            case DURATION:
                processDurationRequest((RecipeDurationRequest) request);
                break;
            case PORTIONS:
                processPortionsRequest((RecipePortionsRequest) request);
                break;
            default:
                throw new UnsupportedOperationException("Unsupported request: " + requestOriginator);
        }
    }

    private void startRecipeMetadataComponent() {
        handler.execute(
                recipeMetadata,
                new RecipeMetadataRequest.Builder().
                        getDefault().
                        setDataId(recipeId).
                        build(),
                new RecipeMetadataCallback()
        );
    }

    private class RecipeMetadataCallback extends RecipeUseCaseCallback<RecipeMetadataResponse> {
        @Override
        protected void processResponse(RecipeMetadataResponse response) {
            addComponentResponse(RECIPE_METADATA, response);
            recipeMetadataResponse = response;
            checkComponentsUpdated();
        }
    }

    private void startIdentityComponent() {
        handler.execute(
                identity,
                new RecipeIdentityRequest.Builder().
                        getDefault().
                        setDataId(recipeId).
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
                        getDefault().
                        setDataId(recipeId).
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
                        setDataId(recipeId).
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
                        getDefault().
                        setDataId(recipeId).
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
            updateRecipeMetadata();
        }
    }

    private boolean isAllComponentsUpdated() {
        return componentResponses.containsKey(RECIPE_METADATA) &&
                componentResponses.containsKey(IDENTITY) &&
                componentResponses.containsKey(COURSE) &&
                componentResponses.containsKey(DURATION) &&
                componentResponses.containsKey(PORTIONS);
    }

    private void updateRecipeMetadata() {
        System.out.println(TAG + "updateRecipeMetadata called");

        RecipeMetadataRequest request = new RecipeMetadataRequest.Builder().
                setDataId(recipeId).
                setModel(new RecipeMetadataRequest.Model.Builder().
                        setParentId(recipeMetadataResponse.getModel().getParentId()).
                        setComponentStates(componentStates).
                        build()).
                build();

        handler.execute(recipeMetadata, request, new RecipeStateCallback());
    }

    private class RecipeStateCallback extends RecipeUseCaseCallback<RecipeMetadataResponse> {
        @Override
        protected void processResponse(RecipeMetadataResponse response) {
            processRecipeMetadataResponse(response);
        }
    }

    private void processRecipeMetadataResponse(RecipeMetadataResponse response) {
        if (isMetadataChanged(response)) {
            // todo - update recipe metadata with recipe state response

        }
        notifyMetadataListeners(response);
    }

    public void registerMetadataListener(RecipeMetadataListener listener) {
        metaDataListeners.add(listener);
    }

    private void notifyMetadataListeners(RecipeMetadataResponse response) {
        if (isMetadataChanged(response)) {
            recipeMetadataResponse = response;
            for (RecipeMetadataListener listener : metaDataListeners) {
                listener.recipeStateChanged(response);
            }
        }
        notifyRecipeCallbacks();
    }

    private boolean isMetadataChanged(RecipeMetadataResponse response) {
        return !recipeMetadataResponse.equals(response);
    }

    public void unregisterStateListener(RecipeMetadataListener listener) {
        metaDataListeners.remove(listener);
    }

    public void registerRecipeCallback(UseCase.Callback<RecipeResponse> callback) {
        recipeResponseListeners.add(callback);
    }

    private void notifyRecipeCallbacks() {
        RecipeResponse response = getRecipeResponse();

        if (isRecipeResponseChanged(response)) {
            for (UseCase.Callback<RecipeResponse> callback : recipeResponseListeners) {
                if (isValid()) {
                    callback.onSuccess(response);
                } else {
                    callback.onError(response);
                }
            }
        }
        notifyComponentCallbacks();
    }

    private void unregisterRecipeCallback(UseCase.Callback<RecipeResponse> callback) {
        recipeResponseListeners.remove(callback);
    }

    private RecipeResponse getRecipeResponse() {
        return new RecipeResponse.Builder().setDataId(dataId).
                setDomainId(recipeId).
                setModel(getRecipeResponseModel()).
                build();
    }

    private RecipeResponse.Model getRecipeResponseModel() {
        return new RecipeResponse.Model.Builder().
                setComponentResponses(componentResponses).
                build();
    }

    private boolean isRecipeResponseChanged(RecipeResponse recipeResponse) {
        return !this.recipeResponse.equals(recipeResponse);
    }

    private boolean isValid() {
        return recipeMetadataResponse.getModel().getFailReasons().contains(CommonFailReason.NONE);
    }

    public void registerComponentCallback(Pair<
            ComponentName,
            UseCase.Callback<? extends UseCase.Response>> callbackPair) {
        componentListeners.add(callbackPair);
    }

    @SuppressWarnings("unchecked")
    private void notifyComponentCallbacks() {
        System.out.println(TAG + "notifyComponentCallbacks called:" + componentListeners);
        System.out.println(TAG + "componentResponses:" + componentResponses);

        for (Pair<
                ComponentName,
                UseCase.Callback<? extends UseCase.Response>>
                callbackPair : componentListeners) {
            ComponentName componentName = callbackPair.first;

            if (IDENTITY.equals(componentName)) {
                RecipeIdentityResponse response = (RecipeIdentityResponse)
                        componentResponses.get(componentName);

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
        componentListeners.remove(callback);
    }

    private void notifyRequestOriginator() {
        System.out.println(TAG + "notifyRequestOriginator called:" + requestOriginator);

        List<FailReasons> f;

        switch (requestOriginator) {
            case RECIPE:
                RecipeResponse recipeResponse = new RecipeResponse.Builder().
                        setDataId(dataId).
                        setDomainId(recipeId).
                        setModel(new RecipeResponse.Model.Builder().
                                setComponentResponses(componentResponses).
                                build()).
                        build();

                f = ((RecipeMetadataResponse) componentResponses.get(RECIPE_METADATA)).
                        getModel().
                        getFailReasons();

                if (f.contains(CommonFailReason.NONE)) {
                    getUseCaseCallback().onSuccess(recipeResponse);
                } else {
                    getUseCaseCallback().onError(recipeResponse);
                }
                break;

            case RECIPE_METADATA:
                sendResponse(
                        componentResponses.get(RECIPE_METADATA),
                        ((RecipeMetadataResponse) componentResponses.get(RECIPE_METADATA)).
                                getModel().
                                getFailReasons());

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
                break;

            default:
                UnsupportedOperationException e = new UnsupportedOperationException(
                        "Recipe component not recognised: " + requestOriginator
                );
                e.printStackTrace();
        }
    }

    private void sendResponse(UseCase.Response r, List<FailReasons> f) {

        if (f.contains(CommonFailReason.NONE)) {
            getUseCaseCallback().onSuccess(r);
        } else {
            getUseCaseCallback().onError(r);
        }
    }
}
