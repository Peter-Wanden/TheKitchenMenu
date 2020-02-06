package com.example.peter.thekitchenmenu.domain.usecase.recipe;

import com.example.peter.thekitchenmenu.data.entity.RecipeEntity;
import com.example.peter.thekitchenmenu.data.repository.DataSource;
import com.example.peter.thekitchenmenu.data.repository.RepositoryRecipe;
import com.example.peter.thekitchenmenu.domain.usecase.UseCase;
import com.example.peter.thekitchenmenu.domain.usecase.UseCaseCommand;
import com.example.peter.thekitchenmenu.domain.usecase.UseCaseHandler;
import com.example.peter.thekitchenmenu.domain.usecase.recipe.recipestate.RecipePersistenceModel;
import com.example.peter.thekitchenmenu.domain.usecase.recipe.recipestate.RecipeStateCalculator;
import com.example.peter.thekitchenmenu.domain.usecase.recipe.recipestate.RecipeStateRequest;
import com.example.peter.thekitchenmenu.domain.usecase.recipe.recipestate.RecipeStateResponse;
import com.example.peter.thekitchenmenu.domain.usecase.recipe.recipeidentity.RecipeIdentity;
import com.example.peter.thekitchenmenu.domain.usecase.recipe.recipeidentity.RecipeIdentityRequest;
import com.example.peter.thekitchenmenu.domain.usecase.recipe.recipeidentity.RecipeIdentityResponse;
import com.example.peter.thekitchenmenu.domain.usecase.recipe.recipecourse.RecipeCourse;
import com.example.peter.thekitchenmenu.domain.usecase.recipe.recipecourse.RecipeCourseRequest;
import com.example.peter.thekitchenmenu.domain.usecase.recipe.recipecourse.RecipeCourseResponse;
import com.example.peter.thekitchenmenu.domain.usecase.recipe.recipeduration.RecipeDuration;
import com.example.peter.thekitchenmenu.domain.usecase.recipe.recipeduration.RecipeDurationRequest;
import com.example.peter.thekitchenmenu.domain.usecase.recipe.recipeduration.RecipeDurationResponse;
import com.example.peter.thekitchenmenu.domain.usecase.recipe.recipeportions.RecipePortions;
import com.example.peter.thekitchenmenu.domain.usecase.recipe.recipeportions.RecipePortionsRequest;
import com.example.peter.thekitchenmenu.domain.usecase.recipe.recipeportions.RecipePortionsResponse;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

import static com.example.peter.thekitchenmenu.domain.usecase.recipe.recipestate.RecipeStateCalculator.*;

public class Recipe<Q extends UseCase.Request>
        extends UseCase<Q, RecipeResponse>
        implements DataSource.GetEntityCallback<RecipeEntity> {

    private static final String TAG = "tkm-" + Recipe.class.getSimpleName() + ": ";

    public interface RecipeClientListener {
        void recipeStateChanged(RecipeStateResponse response);
    }
    private final List<RecipeClientListener> recipeClientListeners = new ArrayList<>();

    private String recipeId = "";
    private String parentId = "";
    public static final String DO_NOT_CLONE = "";

    private final RepositoryRecipe repositoryRecipe;
    private RecipePersistenceModel persistenceModel;

    private final UseCaseHandler handler;
    private final RecipeStateCalculator recipeStateCalculator;
    private RecipeStateResponse recipeStateResponse;

    private final RecipeIdentity identity;
    private final RecipeCourse course;
    private final RecipeDuration duration;
    private final RecipePortions portions;
    private final HashMap<ComponentName, UseCase.Response> componentResponses = new LinkedHashMap<>();
    private final HashMap<ComponentName, ComponentState> componentStates = new LinkedHashMap<>();

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
            if (isNewRequest(recipeRequest.getRecipeId())) {
                loadData(recipeRequest.getRecipeId());
            } else {
                sendResponse();
            }
        } else if (request instanceof RecipeIdentityRequest) {
            RecipeIdentityRequest identityRequest = (RecipeIdentityRequest) request;
            if (isNewRequest(identityRequest.getRecipeId())) {
                loadData(identityRequest.getRecipeId());
            } else {
                handler.execute(identity, identityRequest, getIdentityCallback());
            }
        } else if (request instanceof RecipeCourseRequest) {
            RecipeCourseRequest courseRequest = (RecipeCourseRequest) request;
            if (isNewRequest(courseRequest.getRecipeId())) {
                loadData(courseRequest.getRecipeId());
            } else {
                handler.execute(course, courseRequest, getCoursesCallback());
            }
        } else if (request instanceof RecipeDurationRequest) {
            RecipeDurationRequest durationRequest = (RecipeDurationRequest) request;
            if (isNewRequest(durationRequest.getRecipeId())) {
                loadData(durationRequest.getRecipeId());
            } else {
                handler.execute(duration, durationRequest, getDurationCallback());
            }
        } else if (request instanceof RecipePortionsRequest) {
            RecipePortionsRequest portionsRequest = (RecipePortionsRequest) request;
            if (isNewRequest(portionsRequest.getRecipeId())) {
                loadData(portionsRequest.getRecipeId());
            } else {
                handler.execute(portions, portionsRequest, getPortionsCallback());
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

    private void startComponents() {
        componentStates.clear();

        handler.execute(
                identity,
                RecipeIdentityRequest.Builder.getDefault().setRecipeId(recipeId).build(),
                getIdentityCallback()
        );
        handler.execute(
                course,
                RecipeCourseRequest.Builder.getDefault().setRecipeId(recipeId).build(),
                getCoursesCallback()
        );
        handler.execute(
                duration,
                RecipeDurationRequest.Builder.getDefault().setRecipeId(recipeId).build(),
                getDurationCallback()
        );
        handler.execute(
                portions,
                RecipePortionsRequest.Builder.getDefault().setRecipeId(recipeId).build(),
                getPortionsCallback()
        );
    }

    private UseCase.Callback<RecipeIdentityResponse> getIdentityCallback() {
        return new UseCase.Callback<RecipeIdentityResponse>() {
            @Override
            public void onSuccess(RecipeIdentityResponse response) {
                System.out.println(TAG + "identityOnSuccess:" + response);
                componentResponses.put(ComponentName.IDENTITY, response);
                componentStates.put(ComponentName.IDENTITY, response.getState());
                updateRecipeState();
            }

            @Override
            public void onError(RecipeIdentityResponse response) {
                System.out.println(TAG + "identityOnError:" + response);
                componentResponses.put(ComponentName.IDENTITY, response);
                componentStates.put(ComponentName.IDENTITY, response.getState());
                updateRecipeState();
            }
        };
    }

    private UseCase.Callback<RecipeCourseResponse> getCoursesCallback() {
        return new UseCase.Callback<RecipeCourseResponse>() {
            @Override
            public void onSuccess(RecipeCourseResponse response) {
                System.out.println(TAG + "coursesOnSuccess:" + response);
                componentResponses.put(ComponentName.COURSE, response);
                componentStates.put(ComponentName.COURSE, response.getState());
                updateRecipeState();
            }

            @Override
            public void onError(RecipeCourseResponse response) {
                System.out.println(TAG + "coursesOnError:" + response);
                componentResponses.put(ComponentName.COURSE, response);
                componentStates.put(ComponentName.COURSE, response.getState());
                updateRecipeState();
            }
        };
    }

    private UseCase.Callback<RecipeDurationResponse> getDurationCallback() {
        return new UseCase.Callback<RecipeDurationResponse>() {
            @Override
            public void onSuccess(RecipeDurationResponse response) {
                System.out.println(TAG + "durationOnSuccess:" + response);
                componentResponses.put(ComponentName.DURATION, response);
                componentStates.put(ComponentName.DURATION, response.getState());
                updateRecipeState();
            }

            @Override
            public void onError(RecipeDurationResponse response) {
                System.out.println(TAG + "durationOnError:" + response);
                componentResponses.put(ComponentName.DURATION, response);
                componentStates.put(ComponentName.DURATION, response.getState());
                updateRecipeState();
            }
        };
    }

    private UseCase.Callback<RecipePortionsResponse> getPortionsCallback() {
        return new UseCase.Callback<RecipePortionsResponse>() {
            @Override
            public void onSuccess(RecipePortionsResponse response) {
                System.out.println(TAG + "portionsOnSuccess:");
                componentResponses.put(ComponentName.PORTIONS, response);
                componentStates.put(ComponentName.PORTIONS, response.getState());
                updateRecipeState();
            }

            @Override
            public void onError(RecipePortionsResponse response) {
                System.out.println(TAG + "portionsOnError:");
                componentResponses.put(ComponentName.PORTIONS, response);
                componentStates.put(ComponentName.PORTIONS, response.getState());
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
                componentStates.containsKey(ComponentName.DURATION) &&
                componentStates.containsKey(ComponentName.PORTIONS);
    }

    private UseCase.Callback<RecipeStateResponse> getStateRequestCallback() {
        return new UseCase.Callback<RecipeStateResponse>() {
            @Override
            public void onSuccess(RecipeStateResponse response) {
                System.out.println(TAG + "recipeStateResponseOnSuccess:" + response);
                if (!response.equals(recipeStateResponse)) {
                    recipeStateResponse = response;
                    notifyClientListeners();
                    sendResponse();
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
                    sendResponse();
                } else {
                    sendResponse();
                }
            }
        };
    }

    private void notifyClientListeners() {
        System.out.println(TAG + "notifying listeners");
        for (RecipeClientListener listener : recipeClientListeners) {
            listener.recipeStateChanged(recipeStateResponse);
        }
    }

    public void registerClientListener(RecipeClientListener listener) {
        System.out.println(TAG + "client listener registered");
        recipeClientListeners.add(listener);
    }

    public void unRegisterClientListener(RecipeClientListener listener) {
        recipeClientListeners.remove(listener);
    }

    private void sendResponse() {
        RecipeResponse response = new RecipeResponse(

                recipeStateResponse.getState(),
                recipeStateResponse.getFailReasons(),
                componentResponses
        );

        if (isRecipeStateValid()) {
            getUseCaseCallback().onSuccess(response);
        } else {
            getUseCaseCallback().onError(response);
        }
    }

    private boolean isRecipeStateValid() {
        RecipeState recipeState = recipeStateResponse.getState();
        return recipeState == RecipeState.VALID_UNCHANGED || recipeState == RecipeState.VALID_CHANGED;
        // Or recipeState COMPLETE?
    }
}
