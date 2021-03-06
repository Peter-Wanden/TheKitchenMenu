package com.example.peter.thekitchenmenu.domain.usecase.recipe.macro.recipe;

import androidx.core.util.Pair;

import com.example.peter.thekitchenmenu.domain.usecasenew.common.failreasons.CommonFailReason;
import com.example.peter.thekitchenmenu.domain.usecasenew.common.failreasons.FailReasons;
import com.example.peter.thekitchenmenu.domain.usecase.common.usecasemessage.UseCaseMessageModelDataId;
import com.example.peter.thekitchenmenu.domain.usecase.common.UseCaseBase;
import com.example.peter.thekitchenmenu.domain.usecasenew.common.UseCaseHandler;
import com.example.peter.thekitchenmenu.domain.usecase.recipe.component.course.RecipeCourse;
import com.example.peter.thekitchenmenu.domain.usecase.recipe.component.course.RecipeCourseRequest;
import com.example.peter.thekitchenmenu.domain.usecase.recipe.component.course.RecipeCourseResponse;
import com.example.peter.thekitchenmenu.domain.usecase.recipe.component.duration.RecipeDuration;
import com.example.peter.thekitchenmenu.domain.usecase.recipe.component.duration.RecipeDurationRequest;
import com.example.peter.thekitchenmenu.domain.usecase.recipe.component.duration.RecipeDurationResponse;
import com.example.peter.thekitchenmenu.domain.usecase.recipe.component.identity.RecipeIdentity;
import com.example.peter.thekitchenmenu.domain.usecase.recipe.component.identity.RecipeIdentityRequest;
import com.example.peter.thekitchenmenu.domain.usecase.recipe.component.identity.RecipeIdentityResponse;
import com.example.peter.thekitchenmenu.domain.usecase.recipe.component.metadata.RecipeMetadata;
import com.example.peter.thekitchenmenu.domain.usecase.recipe.component.metadata.RecipeMetadataRequest;
import com.example.peter.thekitchenmenu.domain.usecase.recipe.component.metadata.RecipeMetadataResponse;
import com.example.peter.thekitchenmenu.domain.usecase.recipe.component.portions.RecipePortions;
import com.example.peter.thekitchenmenu.domain.usecase.recipe.component.portions.RecipePortionsRequest;
import com.example.peter.thekitchenmenu.domain.usecase.recipe.component.portions.RecipePortionsResponse;
import com.example.peter.thekitchenmenu.domain.usecase.recipe.macro.RecipeUseCaseCallback;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

import com.example.peter.thekitchenmenu.domain.usecasenew.recipe.component.RecipeComponentNameName;
import static com.example.peter.thekitchenmenu.domain.usecasenew.recipe.component.RecipeComponentNameName.COURSE;
import static com.example.peter.thekitchenmenu.domain.usecasenew.recipe.component.RecipeComponentNameName.DURATION;
import static com.example.peter.thekitchenmenu.domain.usecasenew.recipe.component.RecipeComponentNameName.IDENTITY;
import static com.example.peter.thekitchenmenu.domain.usecasenew.recipe.component.RecipeComponentNameName.PORTIONS;
import static com.example.peter.thekitchenmenu.domain.usecasenew.recipe.component.RecipeComponentNameName.RECIPE;
import static com.example.peter.thekitchenmenu.domain.usecasenew.recipe.component.RecipeComponentNameName.RECIPE_METADATA;

import com.example.peter.thekitchenmenu.domain.usecasenew.common.metadata.ComponentState;

/**
 * A use case interactor utilising the command macro pattern enabling recipe components to work
 * together.
 * All recipe component requests should be routed through an instance of this class.
 * The first request sent through, regardless of type, will only load and return data. Subsequent
 * requests will perform the requested operation.
 */
public class Recipe extends UseCaseBase {

    private static final String TAG = "tkm-" + Recipe.class.getSimpleName() + ": ";

    public static final String CREATE_NEW_RECIPE = "CREATE_NEW_RECIPE";

    public interface RecipeMetadataListener {
        void onRecipeMetadataChanged(RecipeMetadataResponse response);
    }

    private final UseCaseHandler handler;

    private final RecipeIdentity identity;
    private final RecipeCourse course;
    private final RecipeDuration duration;
    private final RecipePortions portions;
    private final RecipeMetadata recipeMetadata;

    private RecipeResponse recipeResponse;
    private RecipeMetadataResponse recipeMetadataResponse;
    private RecipeComponentNameName requestOriginator;
    private String dataId = "";
    private String recipeDomainId = "";

    private final HashMap<RecipeComponentNameName, UseCaseBase.Response>
            componentResponses = new LinkedHashMap<>();
    private final HashMap<RecipeComponentNameName, ComponentState>
            componentStates = new LinkedHashMap<>();

    // Listeners
    private final List<Pair<RecipeComponentNameName, Callback<? extends UseCaseBase.Response>>>
            componentListeners = new ArrayList<>();
    private final List<RecipeMetadataListener>
            metaDataListeners = new ArrayList<>();
    private final List<UseCaseBase.Callback<RecipeResponse>>
            recipeResponseListeners = new ArrayList<>();

    private int requestNo;

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
        requestNo ++;
        System.out.println(TAG + "Request No:" + requestNo);

        extractRequestOriginator(request);
        // Downcast to BaseDomainMessage to get access to data and domain Id's
        UseCaseMessageModelDataId r = (UseCaseMessageModelDataId) request;

        if (isNewRequest(r.getDomainId()) || RECIPE == requestOriginator) {
            dataId = r.getDataId();
            recipeDomainId = r.getDomainId();
            startComponents();
            System.out.println(TAG + "startComponents");
        } else {
            processRequest(request);
            System.out.println(TAG + "processRequest");
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
        } else if (request instanceof RecipeMetadata) {
            throw new UnsupportedOperationException("\nTo access recipe metadata use a " +
                    "RecipeRequest and extract recipe metadata from the response, or " +
                    "register a RecipeMetadataListener to this macro instance.");
        } else {
            throw new UnsupportedOperationException("\nRequest type not supported: " + request);
        }
        System.out.println(TAG + "extractRequestOriginator: " + requestOriginator);
    }

    private boolean isNewRequest(String recipeId) {
        return !this.recipeDomainId.equals(recipeId);
    }

    private void startComponents() {
        componentResponses.clear();
        componentStates.clear();

        // send a MessageModelDataId request to all classes
        // MessageModelDataIdRequest request = new MessageModelDataIdRequest.getDefault.build
        // recipeComponents.forEach((component) () -> execute.(request, MessageModelClientListener))
        // in use case: if request instance of MessageModelDataIdRequest, load or return data
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
        System.out.println(TAG + "startRecipeMetadataComponent");

        RecipeMetadataRequest request = new RecipeMetadataRequest.Builder().
                getDefault().
                setDomainId(recipeDomainId).
                build();
        System.out.println(TAG + request);
        handler.executeAsync(recipeMetadata, request, new RecipeMetadataCallback());
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
        handler.executeAsync(
                identity,
                new RecipeIdentityRequest.Builder().
                        getDefault().
                        setDomainId(recipeDomainId).
                        build(),
                new IdentityCallback()
        );
    }

    private void processIdentityRequest(RecipeIdentityRequest request) {
        handler.executeAsync(identity, request, new IdentityCallback()
        );
    }

    private class IdentityCallback extends RecipeUseCaseCallback<RecipeIdentityResponse> {
        @Override
        protected void processResponse(RecipeIdentityResponse response) {
            addComponentState(IDENTITY, response.getMetadata().getComponentState());
            addComponentResponse(IDENTITY, response);
            checkComponentsUpdated();
        }
    }

    private void startCourseComponent() {
        handler.executeAsync(
                course,
                new RecipeCourseRequest.Builder().
                        getDefault().
                        setDomainId(recipeDomainId).
                        build(),
                new CourseCallback()
        );
    }

    private void processCourseRequest(RecipeCourseRequest request) {
        handler.executeAsync(course, request, new CourseCallback());
    }

    private class CourseCallback extends RecipeUseCaseCallback<RecipeCourseResponse> {
        @Override
        protected void processResponse(RecipeCourseResponse response) {
            addComponentState(COURSE, response.getMetadata().getComponentState()
            );
            addComponentResponse(COURSE, response);
            checkComponentsUpdated();
        }
    }

    private void startDurationComponent() {
        handler.executeAsync(
                duration,
                new RecipeDurationRequest.Builder().
                        getDefault().
                        setDomainId(recipeDomainId).
                        build(),
                new DurationCallback()
        );
    }

    private void processDurationRequest(RecipeDurationRequest request) {
        handler.executeAsync(duration, request, new DurationCallback()
        );
    }

    private class DurationCallback extends RecipeUseCaseCallback<RecipeDurationResponse> {
        @Override
        protected void processResponse(RecipeDurationResponse response) {
            addComponentState(DURATION, response.getMetadata().getComponentState());
            addComponentResponse(DURATION, response);
            checkComponentsUpdated();
        }
    }

    private void startPortionsComponent() {
        handler.executeAsync(
                portions,
                new RecipePortionsRequest.Builder().
                        getDefault().
                        setDomainId(recipeDomainId).
                        build(),
                new PortionsCallback()
        );
    }

    private void processPortionsRequest(RecipePortionsRequest request) {
        handler.executeAsync(portions, request, new PortionsCallback()
        );
    }

    private class PortionsCallback extends RecipeUseCaseCallback<RecipePortionsResponse> {
        @Override
        protected void processResponse(RecipePortionsResponse response) {
            addComponentState(PORTIONS, response.getMetadata().getComponentState()
            );
            addComponentResponse(PORTIONS, response);

            checkComponentsUpdated();
        }
    }

    private void addComponentState(RecipeComponentNameName componentName, ComponentState componentState) {
        componentStates.put(componentName, componentState);
    }

    private void addComponentResponse(RecipeComponentNameName componentName, UseCaseBase.Response response) {
        componentResponses.put(componentName, response);
    }

    private void checkComponentsUpdated() {
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
        RecipeMetadataRequest request = new RecipeMetadataRequest.Builder().
                setDataId(recipeMetadataResponse.getDataId()).
                setDomainId(recipeDomainId).
                setDomainModel(
                        new RecipeMetadataRequest.DomainModel.Builder().
                                setComponentStates(componentStates).
                                build()).
                build();

        handler.executeAsync(recipeMetadata, request, new RecipeStateCallback());
    }

    private class RecipeStateCallback extends RecipeUseCaseCallback<RecipeMetadataResponse> {
        @Override
        protected void processResponse(RecipeMetadataResponse response) {
            if (isMetadataChanged(response)) {
                recipeMetadataResponse = response;
                addComponentResponse(RECIPE_METADATA, response);

            }
            notifyMetadataListeners(response);
        }
    }

    public void registerMetadataListener(RecipeMetadataListener listener) {
        metaDataListeners.add(listener);
    }

    private void notifyMetadataListeners(RecipeMetadataResponse response) {
        for (RecipeMetadataListener listener : metaDataListeners) {
            listener.onRecipeMetadataChanged(response);
        }

        notifyRecipeListeners();
    }

    private boolean isMetadataChanged(RecipeMetadataResponse response) {
        return !recipeMetadataResponse.equals(response);
    }

    public void unregisterMetadataListener(RecipeMetadataListener listener) {
        metaDataListeners.remove(listener);
    }

    public void registerRecipeListener(UseCaseBase.Callback<RecipeResponse> listener) {
        recipeResponseListeners.add(listener);
    }

    private void notifyRecipeListeners() {
        RecipeResponse response = getRecipeResponse();

        if (isRecipeResponseChanged(response)) {
            for (UseCaseBase.Callback<RecipeResponse> callback : recipeResponseListeners) {
                if (isValid()) {
                    callback.onUseCaseSuccess(response);
                } else {
                    callback.onUseCaseError(response);
                }
            }
        }
        notifyComponentListeners();
    }

    private void unregisterRecipeListener(UseCaseBase.Callback<RecipeResponse> listener) {
        recipeResponseListeners.remove(listener);
    }

    private RecipeResponse getRecipeResponse() {
        return new RecipeResponse.Builder().
                setDataId(dataId).
                setDomainId(recipeDomainId).
                setDomainModel(getRecipeResponseModel()).
                build();
    }

    private RecipeResponse.Model getRecipeResponseModel() {
        return new RecipeResponse.Model.Builder().
                getDefault().
                setComponentResponses(componentResponses).
                build();
    }

    private boolean isRecipeResponseChanged(RecipeResponse recipeResponse) {
        return !this.recipeResponse.equals(recipeResponse);
    }

    private boolean isValid() {
        return recipeMetadataResponse.getMetadata().getFailReasons().contains(CommonFailReason.NONE);
    }

    public void registerComponentListener(Pair<
            RecipeComponentNameName,
            UseCaseBase.Callback<? extends UseCaseBase.Response>> callbackPair) {
        componentListeners.add(callbackPair);
    }

    @SuppressWarnings("unchecked")
    private void notifyComponentListeners() {
        for (Pair<
                RecipeComponentNameName,
                UseCaseBase.Callback<? extends UseCaseBase.Response>>
                callbackPair : componentListeners) {

            RecipeComponentNameName componentName = callbackPair.first;

            if (IDENTITY.equals(componentName)) {
                RecipeIdentityResponse response = (RecipeIdentityResponse)
                        componentResponses.get(componentName);

                UseCaseBase.Callback<RecipeIdentityResponse> callback =
                        (UseCaseBase.Callback<RecipeIdentityResponse>) callbackPair.second;

                if (response.getMetadata().getFailReasons().contains(CommonFailReason.NONE)) {
                    callback.onUseCaseSuccess(response);
                } else {
                    callback.onUseCaseError(response);
                }

            } else if (COURSE.equals(componentName)) {
                RecipeCourseResponse response = (RecipeCourseResponse)
                        componentResponses.get(componentName);

                UseCaseBase.Callback<RecipeCourseResponse> callback =
                        (UseCaseBase.Callback<RecipeCourseResponse>) callbackPair.second;

                if (response.getMetadata().getFailReasons().contains(CommonFailReason.NONE)) {
                    callback.onUseCaseSuccess(response);
                } else {
                    callback.onUseCaseError(response);
                }

            } else if (DURATION.equals(componentName)) {
                RecipeDurationResponse response = (RecipeDurationResponse)
                        componentResponses.get(componentName);

                UseCaseBase.Callback<RecipeDurationResponse> callback =
                        (UseCaseBase.Callback<RecipeDurationResponse>) callbackPair.second;

                if (response.getMetadata().getFailReasons().contains(CommonFailReason.NONE)) {
                    callback.onUseCaseSuccess(response);
                } else {
                    callback.onUseCaseError(response);
                }

            } else if (PORTIONS.equals(componentName)) {
                RecipePortionsResponse response = (RecipePortionsResponse)
                        componentResponses.get(componentName);

                UseCaseBase.Callback<RecipePortionsResponse> callback =
                        (UseCaseBase.Callback<RecipePortionsResponse>) callbackPair.second;

                if (response.getMetadata().getFailReasons().contains(CommonFailReason.NONE)) {
                    callback.onUseCaseSuccess(response);
                } else {
                    callback.onUseCaseError(response);
                }
            }
        }
        notifyRequestOriginator();
    }

    public void unregisterComponentListener(Pair<RecipeComponentNameName, UseCaseBase.Callback
            <? extends UseCaseBase.Response>> callback) {
        componentListeners.remove(callback);
    }

    private void notifyRequestOriginator() {
        System.out.println(TAG + "notifyRequestOriginator: " + requestOriginator);

        List<FailReasons> failReasons;

        switch (requestOriginator) {
            case RECIPE:
                RecipeResponse recipeResponse = new RecipeResponse.Builder().
                        getDefault().
                        setDataId(dataId).
                        setDomainId(recipeDomainId).
                        setDomainModel(
                                new RecipeResponse.Model.Builder().
                                        setComponentResponses(componentResponses).
                                        build()).
                        build();

                failReasons = ((RecipeMetadataResponse) componentResponses.get(RECIPE_METADATA)).
                        getMetadata().
                        getFailReasons();

                sendResponse(recipeResponse, failReasons);
                break;

            case RECIPE_METADATA:
                sendResponse(
                        componentResponses.get(RECIPE_METADATA),
                        ((RecipeMetadataResponse) componentResponses.get(RECIPE_METADATA)).
                                getMetadata().
                                getFailReasons()
                );
                break;

            case IDENTITY:
                sendResponse(
                        componentResponses.get(IDENTITY),
                        ((RecipeIdentityResponse) componentResponses.get(IDENTITY)).
                                getMetadata().
                                getFailReasons()
                );
                break;

            case COURSE:
                sendResponse(
                        componentResponses.get(COURSE),
                        ((RecipeCourseResponse) componentResponses.get(COURSE)).
                                getMetadata().
                                getFailReasons()
                );
                break;

            case DURATION:
                sendResponse(
                        componentResponses.get(DURATION),
                        ((RecipeDurationResponse) componentResponses.get(DURATION)).
                                getMetadata().
                                getFailReasons()
                );
                break;

            case PORTIONS:
                sendResponse(
                        componentResponses.get(PORTIONS),
                        ((RecipePortionsResponse) componentResponses.get(PORTIONS)).
                                getMetadata().
                                getFailReasons()
                );
                break;

            default:
                UnsupportedOperationException e = new UnsupportedOperationException(
                        "Recipe component not recognised: " + requestOriginator
                );
                e.printStackTrace();
        }
    }

    private void sendResponse(UseCaseBase.Response r, List<FailReasons> failReasons) {
        if (failReasons.contains(CommonFailReason.NONE)) {
            getUseCaseCallback().onUseCaseSuccess(r);
        } else {
            getUseCaseCallback().onUseCaseError(r);
        }
    }
}
