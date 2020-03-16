package com.example.peter.thekitchenmenu.domain.usecase.recipe.recipemacro;

import androidx.core.util.Pair;

import com.example.peter.thekitchenmenu.domain.usecase.CommonFailReason;
import com.example.peter.thekitchenmenu.domain.usecase.UseCase;
import com.example.peter.thekitchenmenu.domain.usecase.UseCaseHandler;
import com.example.peter.thekitchenmenu.domain.usecase.recipe.RecipeRequest;
import com.example.peter.thekitchenmenu.domain.usecase.recipe.RecipeResponse;
import com.example.peter.thekitchenmenu.domain.usecase.recipe.recipemetadata.RecipeMetadata;
import com.example.peter.thekitchenmenu.domain.usecase.recipe.recipemetadata.RecipeMetadataRequest;
import com.example.peter.thekitchenmenu.domain.usecase.recipe.recipemetadata.RecipeMetadataResponse;
import com.example.peter.thekitchenmenu.domain.usecase.recipe.recipecourse.RecipeCourseRequest;
import com.example.peter.thekitchenmenu.domain.usecase.recipe.recipecourse.RecipeCourseResponse;
import com.example.peter.thekitchenmenu.domain.usecase.recipe.recipeduration.RecipeDurationRequest;
import com.example.peter.thekitchenmenu.domain.usecase.recipe.recipeduration.RecipeDurationResponse;
import com.example.peter.thekitchenmenu.domain.usecase.recipe.recipeidentity.RecipeIdentityRequest;
import com.example.peter.thekitchenmenu.domain.usecase.recipe.recipeidentity.RecipeIdentityResponse;
import com.example.peter.thekitchenmenu.domain.usecase.recipe.recipeportions.RecipePortionsRequest;
import com.example.peter.thekitchenmenu.domain.usecase.recipe.recipeportions.RecipePortionsResponse;
import com.example.peter.thekitchenmenu.domain.usecase.recipe.recipestate.RecipeStateCalculator;
import com.example.peter.thekitchenmenu.domain.usecase.recipe.recipestate.RecipeStateRequest;
import com.example.peter.thekitchenmenu.domain.usecase.recipe.recipestate.RecipeStateResponse;
import com.example.peter.thekitchenmenu.domain.usecase.recipe.recipeidentity.RecipeIdentity;
import com.example.peter.thekitchenmenu.domain.usecase.recipe.recipecourse.RecipeCourse;
import com.example.peter.thekitchenmenu.domain.usecase.recipe.recipeduration.RecipeDuration;
import com.example.peter.thekitchenmenu.domain.usecase.recipe.recipeportions.RecipePortions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

import static com.example.peter.thekitchenmenu.domain.usecase.recipe.recipestate.RecipeStateCalculator.*;
import static com.example.peter.thekitchenmenu.domain.usecase.recipe.recipestate.RecipeStateCalculator.ComponentName.*;

/**
 * Acts as a command mediator enabling recipe components to work together
 */
public class RecipeMacro extends UseCase {

    private static final String TAG = "tkm-" + RecipeMacro.class.getSimpleName() + ": ";

    enum RequestType {
        CREATE,
        EDIT,
        FAVORITE,
        CLONE,
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

    private RecipeMacroResponse recipeMacroResponse;
    private RecipeStateResponse recipeStateResponse;
    private ComponentName requestOriginator;
    private String id = "";
    private boolean isCloned;
    private RecipeRequest request;

    private final List<Pair<ComponentName, UseCase.Callback<? extends RecipeResponse>>>
            componentCallbacks = new ArrayList<>();
    private final HashMap<ComponentName, UseCase.Response> componentResponses =
            new LinkedHashMap<>();
    private final HashMap<ComponentName, ComponentState> componentStates =
            new LinkedHashMap<>();
    private final List<RecipeStateListener> stateListeners =
            new ArrayList<>();
    private final List<UseCase.Callback<RecipeMacroResponse>> macroCallbacks =
            new ArrayList<>();

    public RecipeMacro(UseCaseHandler handler,
                       RecipeStateCalculator recipeStateCalculator,
                       RecipeMetadata recipeMetaData,
                       RecipeIdentity identity,
                       RecipeCourse course,
                       RecipeDuration duration,
                       RecipePortions portions) {

        this.handler = handler;
        this.recipeStateCalculator = recipeStateCalculator;
        recipeStateResponse = RecipeStateResponse.Builder.getDefault().build();
        recipeMacroResponse = RecipeMacroResponse.Builder.getDefault().build();
        this.recipeMetaData = recipeMetaData;
        this.identity = identity;
        this.course = course;
        this.duration = duration;
        this.portions = portions;
    }

    @Override
    public <Q extends Request> void execute(Q request) {

        this.request = (RecipeRequest) request;

        if (request instanceof RecipeMetadataRequest) {
            requestOriginator = RECIPE;
        } else if (request instanceof RecipeIdentityRequest) {
            requestOriginator = IDENTITY;
        } else if (request instanceof RecipeCourseRequest) {
            requestOriginator = COURSE;
        } else if (request instanceof RecipeDurationRequest) {
            requestOriginator = DURATION;
        } else if (request instanceof RecipePortionsRequest) {
            requestOriginator = PORTIONS;
        }

        if (isNewRequest(this.request.getId())) {
            id = this.request.getId();
            startComponents();

        } else if (RECIPE.equals(requestOriginator)) {
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

    private void startComponents() {
        componentResponses.clear();
        componentStates.clear();

        startRecipeComponent();
        startIdentityComponent();
        startCourseComponent();
        startDurationComponent();
        startPortionsComponent();
    }

    private void startRecipeComponent() {
        handler.execute(
                recipeMetaData,
                new RecipeMetadataRequest.Builder().
                        setId(id).
                        build(),
                new RecipeCallback()
        );
    }

    private void processRecipeRequest(RecipeMetadataRequest request) {
        handler.execute(
                recipeMetaData,
                request,
                new RecipeCallback());
    }

    private class RecipeCallback extends RecipeMacroUseCaseCallback<RecipeMetadataResponse> {
        @Override
        protected void processResponse(RecipeMetadataResponse response) {
            addComponentResponse(RECIPE, response);
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
        handler.execute(
                identity,
                request,
                new IdentityCallback()
        );
    }

    private class IdentityCallback extends RecipeMacroUseCaseCallback<RecipeIdentityResponse> {
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
        handler.execute(
                course,
                request,
                new CourseCallback());
    }

    private class CourseCallback extends RecipeMacroUseCaseCallback<RecipeCourseResponse> {
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
        handler.execute(
                duration,
                request,
                new DurationCallback()
        );
    }

    private class DurationCallback extends RecipeMacroUseCaseCallback<RecipeDurationResponse> {
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
        handler.execute(
                portions,
                request,
                new PortionsCallback()
        );
    }

    private class PortionsCallback extends RecipeMacroUseCaseCallback<RecipePortionsResponse> {
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
            isCloned = false;
            updateRecipeState();
        }
    }

    private boolean isAllComponentsUpdated() {
        return componentResponses.containsKey(RECIPE) &&
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

    private class RecipeStateCallback extends RecipeMacroUseCaseCallback<RecipeStateResponse> {
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

    public void registerMacroCallback(UseCase.Callback<RecipeMacroResponse> callback) {
        macroCallbacks.add(callback);
    }

    private void notifyMacroCallbacks() {
        RecipeMacroResponse response = getMacroResponse();

        if (isMacroResponseChanged(response)) {
            for (UseCase.Callback<RecipeMacroResponse> callback : macroCallbacks) {
                if (isValid()) {
                    callback.onSuccess(response);
                } else {
                    callback.onError(response);
                }
            }
        }
        notifyComponentCallbacks();
    }

    private void unregisterMacroCallback(UseCase.Callback<RecipeMacroResponse> callback) {
        macroCallbacks.remove(callback);
    }

    private RecipeMacroResponse getMacroResponse() {
        return new RecipeMacroResponse.Builder().
                setId(id).
                setRecipeStateResponse(recipeStateResponse).
                setComponentResponses(componentResponses).
                build();
    }

    private boolean isMacroResponseChanged(RecipeMacroResponse recipeMacroResponse) {
        return !this.recipeMacroResponse.equals(recipeMacroResponse);
    }

    private boolean isValid() {
        return recipeStateResponse.getFailReasons().contains(FailReason.NONE);
    }

    public void registerComponentCallback(Pair<ComponentName, UseCase.Callback
            <? extends RecipeResponse>> callback) {
        componentCallbacks.add(callback);
    }

    private void notifyComponentCallbacks() {
        System.out.println(TAG + "notifyComponentCallbacks called:" + componentCallbacks);
        System.out.println(TAG + "componentResponses:" + componentResponses);

        for (Pair<ComponentName, UseCase.Callback<? extends RecipeResponse>> callbackPair :
                componentCallbacks) {
            ComponentName componentName = callbackPair.first;

            if (RECIPE.equals(componentName)) {
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
        if (RECIPE.equals(requestOriginator)) {
            RecipeMetadataResponse response = (RecipeMetadataResponse)
                    componentResponses.get(RECIPE);
            getUseCaseCallback().onSuccess(response);

        } else if (IDENTITY.equals(requestOriginator)) {
            RecipeIdentityResponse response = (RecipeIdentityResponse)
                    componentResponses.get(IDENTITY);

            if (response.getMetadata().getFailReasons().contains(CommonFailReason.NONE)) {
                getUseCaseCallback().onSuccess(response);
            } else {
                getUseCaseCallback().onError(response);
            }

        } else if (COURSE.equals(requestOriginator)) {
            RecipeCourseResponse response = (RecipeCourseResponse)
                    componentResponses.get(COURSE);

            if (response.getMetadata().getFailReasons().contains(CommonFailReason.NONE)) {
                getUseCaseCallback().onSuccess(response);
            } else {
                getUseCaseCallback().onError(response);
            }

        } else if (DURATION.equals(requestOriginator)) {
            RecipeDurationResponse response = (RecipeDurationResponse)
                    componentResponses.get(DURATION);

            if (response.getMetadata().getFailReasons().contains(CommonFailReason.NONE)) {
                getUseCaseCallback().onSuccess(response);
            } else {
                getUseCaseCallback().onError(response);
            }

        } else if (PORTIONS.equals(requestOriginator)) {
            RecipePortionsResponse response = (RecipePortionsResponse)
                    componentResponses.get(PORTIONS);

            if (response.getMetadata().getFailReasons().contains(CommonFailReason.NONE)) {
                getUseCaseCallback().onSuccess(response);
            } else {
                getUseCaseCallback().onError(response);
            }
        }
    }
}
