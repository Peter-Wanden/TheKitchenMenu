package com.example.peter.thekitchenmenu.domain.usecase.recipe;

import com.example.peter.thekitchenmenu.data.entity.RecipeEntity;
import com.example.peter.thekitchenmenu.data.repository.DataSource;
import com.example.peter.thekitchenmenu.data.repository.RepositoryRecipe;
import com.example.peter.thekitchenmenu.domain.usecase.FailReasons;
import com.example.peter.thekitchenmenu.domain.usecase.UseCase;
import com.example.peter.thekitchenmenu.domain.usecase.UseCaseHandler;
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

public class Recipe<Q extends UseCase.Request, P extends UseCase.Response>
        extends UseCase<Q, P>
        implements DataSource.GetEntityCallback<RecipeEntity> {

    private static final String TAG = "tkm-" + Recipe.class.getSimpleName() + ": ";

    public interface RecipeStateListener {
        void recipeStateChanged(RecipeStateResponse response);
    }

    private String recipeId = "";
    private String parentId = "";
    public static final String DO_NOT_CLONE = "";

    private final RepositoryRecipe repositoryRecipe;
    private RecipePersistenceModel persistenceModel;
    private List<FailReasons> failReasons = new ArrayList<>();

    private final UseCaseHandler handler;
    private final RecipeStateCalculator recipeStateCalculator;

    private RecipeStateResponse recipeStateResponse = RecipeStateResponse.Builder.
            getDefault().
            build();
    private RecipeResponse recipeResponse = RecipeResponse.Builder.
            getDefault().
            build();

    private ComponentName requestOriginator;

    private final RecipeIdentity identity;
    private final RecipeCourse course;
    private final RecipeDuration duration;
    private final RecipePortions portions;

    private final HashMap<ComponentName, UseCase.Callback<P>> componentCallbacks =
            new LinkedHashMap<>();
    private final HashMap<ComponentName, UseCase.Response> componentResponses =
            new LinkedHashMap<>();
    private final HashMap<ComponentName, ComponentState> componentStates =
            new LinkedHashMap<>();
    private final List<RecipeStateListener> recipeStateListeners =
            new ArrayList<>();
    private final List<UseCase.Callback<RecipeResponse>> recipeResponseCallbacks =
            new ArrayList<>();

    public Recipe(RepositoryRecipe repositoryRecipe,
                  UseCaseHandler handler,
                  RecipeStateCalculator recipeStateCalculator,
                  RecipeIdentity identity,
                  RecipeCourse course,
                  RecipeDuration duration,
                  RecipePortions portions) {
        this.repositoryRecipe = repositoryRecipe;
        this.handler = handler;
        this.recipeStateCalculator = recipeStateCalculator;
        this.identity = identity;
        this.course = course;
        this.duration = duration;
        this.portions = portions;
    }

    @Override
    protected void execute(Q request) {
        if (request instanceof RecipeRequest) {
            RecipeRequest recipeRequest = (RecipeRequest) request;
            requestOriginator = ComponentName.RECIPE;

            if (isNewRequest(recipeRequest.getRecipeId())) {
                loadData(recipeId);
            }
        } else if (request instanceof RecipeIdentityRequest) {
            RecipeIdentityRequest identityRequest = (RecipeIdentityRequest) request;
            requestOriginator = ComponentName.IDENTITY;

            if (isNewRequest(identityRequest.getRecipeId())) {
                loadData(recipeId);
            }
        } else if (request instanceof RecipeCourseRequest) {
            RecipeCourseRequest courseRequest = (RecipeCourseRequest) request;
            requestOriginator = ComponentName.COURSE;

            if (isNewRequest(courseRequest.getRecipeId())) {
                loadData(recipeId);
            }
        } else if (request instanceof RecipeDurationRequest) {
            RecipeDurationRequest durationRequest = (RecipeDurationRequest) request;
            requestOriginator = ComponentName.DURATION;

            if (isNewRequest(durationRequest.getRecipeId())) {
                loadData(recipeId);
            }
        } else if (request instanceof RecipePortionsRequest) {
            RecipePortionsRequest portionsRequest = (RecipePortionsRequest) request;
            requestOriginator = ComponentName.PORTIONS;

            if (isNewRequest(portionsRequest.getRecipeId())) {
                loadData(recipeId);
            }
        }
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

    private void loadData(String recipeId) {
        repositoryRecipe.getById(recipeId, this);
    }

    @Override
    public void onEntityLoaded(RecipeEntity entity) {
        persistenceModel = convertEntityToPersistenceModel(entity);
        startComponents();
    }

    @Override
    public void onDataNotAvailable() {
        persistenceModel = createNewPersistenceModel();
        startComponents();
    }

    private void startComponents() {
        componentResponses.clear();
        componentStates.clear();

        startIdentityComponent();
        startCourseComponent();
        startDurationComponent();
        startPortionsComponent();
    }

    private void startIdentityComponent() {
        handler.execute(
                identity,
                RecipeIdentityRequest.Builder.
                        getDefault().
                        setRecipeId(recipeId).
                        build(),
                new UseCase.Callback<RecipeIdentityResponse>() {
                    private ComponentName componentName = ComponentName.IDENTITY;
                    private RecipeIdentityResponse response;

                    @Override
                    public void onSuccess(RecipeIdentityResponse response) {
                        System.out.println(TAG + "onSuccess:" + response);
                        this.response = response;
                        processResponse();
                    }

                    @Override
                    public void onError(RecipeIdentityResponse response) {
                        System.out.println(TAG + "onError:" + response);
                        this.response = response;
                        processResponse();
                    }

                    private void processResponse() {
                        addComponentState(componentName, response.getState());
                        addComponentResponse(componentName, response);
                        checkComponentsUpdated();
                    }
                }
        );
    }

    private void startCourseComponent() {
        handler.execute(
                course,
                RecipeCourseRequest.Builder.
                        getDefault().
                        setRecipeId(recipeId).
                        build(),
                new UseCase.Callback<RecipeCourseResponse>() {
                    private ComponentName componentName = ComponentName.COURSE;
                    private RecipeCourseResponse response;

                    @Override
                    public void onSuccess(RecipeCourseResponse response) {
                        System.out.println(TAG + "onSuccess" + response);
                        this.response = response;
                        processResponse();
                    }

                    @Override
                    public void onError(RecipeCourseResponse response) {
                        System.out.println(TAG + "onError:" + response);
                        this.response = response;
                        processResponse();
                    }

                    private void processResponse() {
                        addComponentState(componentName, response.getState());
                        addComponentResponse(componentName, response);
                        checkComponentsUpdated();
                    }
                }
        );
    }

    private void startDurationComponent() {
        handler.execute(
                duration,
                RecipeDurationRequest.Builder.getDefault().setRecipeId(recipeId).build(),
                new UseCase.Callback<RecipeDurationResponse>() {
                    private ComponentName componentName = ComponentName.DURATION;
                    private RecipeDurationResponse response;

                    @Override
                    public void onSuccess(RecipeDurationResponse response) {
                        System.out.println(TAG + "onSuccess:" + response);
                        this.response = response;
                        processResponse();
                    }

                    @Override
                    public void onError(RecipeDurationResponse response) {
                        System.out.println(TAG + "onError:" + response);
                        this.response = response;
                        processResponse();
                    }

                    private void processResponse() {
                        addComponentState(componentName, response.getState());
                        addComponentResponse(componentName, response);
                        checkComponentsUpdated();
                    }
                }
        );
    }

    private void startPortionsComponent() {
        handler.execute(
                portions,
                RecipePortionsRequest.Builder.
                        getDefault().
                        setRecipeId(recipeId).
                        build(),
                new UseCase.Callback<RecipePortionsResponse>() {
                    private ComponentName componentName = ComponentName.PORTIONS;
                    private RecipePortionsResponse response;

                    @Override
                    public void onSuccess(RecipePortionsResponse response) {
                        System.out.println(TAG + "onSuccess:" + response);
                        this.response = response;
                        processResponse();
                    }

                    @Override
                    public void onError(RecipePortionsResponse response) {
                        System.out.println(TAG + "onError:" + response);
                        this.response = response;
                        processResponse();
                    }

                    private void processResponse() {
                        addComponentState(componentName, response.getState());
                        addComponentResponse(componentName, response);
                        checkComponentsUpdated();
                    }
                }
        );
    }

    private void addComponentState(ComponentName componentName, ComponentState componentState) {
        componentStates.put(componentName, componentState);
    }

    private void addComponentResponse(ComponentName componentName, UseCase.Response response) {
        componentResponses.put(componentName, response);
    }

    private void checkComponentsUpdated() {
        if (isAllComponentStatesUpdated()) {
            updateRecipeState();
        }
    }

    private boolean isAllComponentStatesUpdated() {
        // todo - change to ComponentName.size() == componentState.size()
        return componentStates.containsKey(ComponentName.IDENTITY) &&
                componentStates.containsKey(ComponentName.COURSE) &&
                componentStates.containsKey(ComponentName.DURATION) &&
                componentStates.containsKey(ComponentName.PORTIONS);
    }

    private void updateRecipeState() {
        RecipeStateRequest request = new RecipeStateRequest(componentStates);
        handler.execute(recipeStateCalculator, request, new UseCase.Callback<RecipeStateResponse>() {
            @Override
            public void onSuccess(RecipeStateResponse response) {
                System.out.println(TAG + "recipeStateResponseOnSuccess:" + response);
                notifyStateListenersIfRecipeStateChanged(response);
            }

            @Override
            public void onError(RecipeStateResponse response) {
                System.out.println(TAG + "recipeStateResponseOnError:" + response);
                notifyStateListenersIfRecipeStateChanged(response);
            }
        });
    }

    public void registerRecipeStateListener(RecipeStateListener listener) {
        recipeStateListeners.add(listener);
    }

    private void notifyStateListenersIfRecipeStateChanged(RecipeStateResponse response) {
        if (!recipeStateResponse.equals(response)) {
            recipeStateResponse = response;
            for (RecipeStateListener listener : recipeStateListeners) {
                listener.recipeStateChanged(response);
            }
        }
        notifyRecipeResponseCallbacks();
    }

    public void unregisterRecipeStateListener(RecipeStateListener listener) {
        recipeStateListeners.remove(listener);
    }

    public void registerRecipeResponseCallback(UseCase.Callback<RecipeResponse> callback) {
        recipeResponseCallbacks.add(callback);
    }

    private void notifyRecipeResponseCallbacks() {
        RecipeResponse response = getRecipeResponse();

        if (isRecipeResponseChanged(response)) {
            for (UseCase.Callback<RecipeResponse> callback : recipeResponseCallbacks) {
                if (recipeIsValid()) {
                    callback.onSuccess(response);
                } else {
                    callback.onError(response);
                }
            }
        }

        notifyComponentCallbacks();
    }

    private RecipeResponse getRecipeResponse() {
        return new RecipeResponse.Builder().
                setRecipeId(recipeId).
                setRecipeStateResponse(recipeStateResponse).
                setComponentResponses(componentResponses).
                build();
    }

    private void notifyComponentCallbacks() {
        // todo, only notify if component changed

        for (ComponentName componentName : componentCallbacks.keySet()) {

            if (ComponentName.IDENTITY.equals(componentName)) {
                RecipeIdentityResponse response = (RecipeIdentityResponse)
                        componentResponses.get(componentName);

                UseCase.Callback callback;
                if (ComponentName.IDENTITY.equals(requestOriginator)) {
                    callback = getUseCaseCallback();
                } else {
                    callback = componentCallbacks.get(componentName);
                }

                if (response.getFailReasons().contains(RecipeIdentity.FailReason.NONE)) {
                    callback.onSuccess(response);
                } else {
                    callback.onError(response);
                }

            } else if (ComponentName.COURSE.equals(componentName)) {
                RecipeCourseResponse response = (RecipeCourseResponse)
                        componentResponses.get(componentName);

                UseCase.Callback callback;
                if (ComponentName.COURSE.equals(requestOriginator)) {
                    callback = getUseCaseCallback();
                } else {
                    callback = componentCallbacks.get(componentName);
                }

                if (response.getFailReasons().contains(RecipeCourse.FailReason.NONE)) {
                    callback.onSuccess(response);
                } else {
                    callback.onError(response);
                }

            } else if (ComponentName.DURATION.equals(componentName)) {
                RecipeDurationResponse response = (RecipeDurationResponse)
                        componentResponses.get(componentName);

                UseCase.Callback callback;
                if (ComponentName.DURATION.equals(requestOriginator)) {
                    callback = getUseCaseCallback();
                } else {
                    callback = componentCallbacks.get(componentName);
                }

                if (response.getFailReasons().contains(RecipeDuration.FailReason.NONE)) {
                    callback.onSuccess(response);
                } else {
                    callback.onError(response);
                }

            } else if (ComponentName.PORTIONS.equals(componentName)) {
                RecipePortionsResponse response = (RecipePortionsResponse)
                        componentResponses.get(componentName);

                UseCase.Callback callback;
                if (ComponentName.PORTIONS.equals(requestOriginator)) {
                    callback = getUseCaseCallback();
                } else {
                    callback = componentCallbacks.get(componentName);
                }
                if (response.getFailReasons().contains(RecipePortions.FailReason.NONE)) {
                    callback.onSuccess(response);
                } else {
                    callback.onError(response);
                }
            }
        }
        if (ComponentName.RECIPE.equals(requestOriginator)) {
            RecipeResponse response = getRecipeResponse();

            UseCase.Callback callback = getUseCaseCallback();
            if (response.getRecipeStateResponse().getFailReasons().contains(FailReason.NONE)) {
                callback.onSuccess(response);
            } else {
                callback.onError(response);
            }
        }
    }

    private boolean recipeIsValid() {
        return recipeStateResponse.getFailReasons().contains(FailReason.NONE);
    }

    private boolean isRecipeResponseChanged(RecipeResponse recipeResponse) {
        return !this.recipeResponse.equals(recipeResponse);
    }

    private void unregisterRecipeResponseCallback(UseCase.Callback<RecipeResponse> callback) {
        recipeResponseCallbacks.remove(callback);
    }

    private RecipePersistenceModel createNewPersistenceModel() {
        return RecipePersistenceModel.Builder.
                getDefault().
                setId(recipeId).
                build();
    }

    private RecipePersistenceModel convertEntityToPersistenceModel(RecipeEntity entity) {
        return new RecipePersistenceModel.Builder().
                setId(entity.getId()).
                setParentId(entity.getParentId()).
                setCreatedBy(entity.getCreatedBy()).
                setCreateDate(entity.getCreateDate()).
                setLastUpdate(entity.getLastUpdate()).
                setDraft(entity.isDraft()).
                build();
    }
}
