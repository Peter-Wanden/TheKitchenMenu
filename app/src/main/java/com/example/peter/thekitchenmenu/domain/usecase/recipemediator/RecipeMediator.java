package com.example.peter.thekitchenmenu.domain.usecase.recipemediator;

import com.example.peter.thekitchenmenu.domain.UseCaseCommand;
import com.example.peter.thekitchenmenu.domain.UseCaseHandler;
import com.example.peter.thekitchenmenu.domain.usecase.recipecourse.RecipeCourse;
import com.example.peter.thekitchenmenu.domain.usecase.recipecourse.RecipeCourseRequest;
import com.example.peter.thekitchenmenu.domain.usecase.recipecourse.RecipeCourseResponse;
import com.example.peter.thekitchenmenu.domain.usecase.recipeduration.RecipeDuration;
import com.example.peter.thekitchenmenu.domain.usecase.recipeduration.RecipeDurationRequest;
import com.example.peter.thekitchenmenu.domain.usecase.recipeduration.RecipeDurationResponse;
import com.example.peter.thekitchenmenu.domain.usecase.recipeidentity.RecipeIdentity;
import com.example.peter.thekitchenmenu.domain.usecase.recipeidentity.RecipeIdentityRequest;
import com.example.peter.thekitchenmenu.domain.usecase.recipeidentity.RecipeIdentityResponse;
import com.example.peter.thekitchenmenu.domain.usecase.recipeportions.RecipePortions;
import com.example.peter.thekitchenmenu.domain.usecase.recipeportions.RecipePortionsRequest;
import com.example.peter.thekitchenmenu.domain.usecase.recipeportions.RecipePortionsResponse;
import com.example.peter.thekitchenmenu.domain.usecase.recipestate.RecipeState;
import com.example.peter.thekitchenmenu.domain.usecase.recipestate.RecipeStateRequest;

import java.util.HashMap;
import java.util.LinkedHashMap;

import static com.example.peter.thekitchenmenu.domain.usecase.recipestate.RecipeState.*;

/**
 * A recipe is made up of a complex set of independently changing data structures each controlled by
 * its own use case. The {@link RecipeMediator} aggregates those use cases using the mediator and
 * composite patterns.
 */
public class RecipeMediator extends RecipeMediatorAbstract<RecipeMediatorResponse> {

    private static final String TAG = "tkm-" + RecipeMediator.class.getSimpleName() + ": ";

    private final UseCaseHandler handler;

    private RecipeState recipeState;

    private final RecipeIdentity identity;
    private boolean isIdentityLoading;
    private final RecipeDuration duration;
    private boolean isDurationLoading;
    private final RecipeCourse course;
    private boolean isCoursesLoading;
    private final RecipePortions portions;
    private boolean isPortionsLoading;

    private HashMap<ComponentName, ComponentState> componentStates = new LinkedHashMap<>();

    private RecipeMediatorResponse response = new RecipeMediatorResponse();

    public RecipeMediator(UseCaseHandler handler,
                          RecipeIdentity identity,
                          RecipeDuration duration,
                          RecipeCourse course,
                          RecipePortions portions) {
        this.handler = handler;
        this.identity = identity;
        this.duration = duration;
        this.course = course;
        this.portions = portions;
    }

    public void getAllRecipeData(String recipeId,
                                 RecipeMediator.Callback<RecipeMediatorResponse> callback) {

        setMediatorCallback(callback);
        getIdentityData(recipeId);
        getDurationData(recipeId);
        getCourseData(recipeId);
        getPortionsData(recipeId);
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
                        componentStates.put(ComponentName.IDENTITY, response.getState());
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

        handler.execute(
                duration,
                request,
                new UseCaseCommand.Callback<RecipeDurationResponse>() {

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

    private void getCourseData(String recipeId) {
        isCoursesLoading = true;
        RecipeCourseRequest request = RecipeCourseRequest.Builder.getDefault().
                setRecipeId(recipeId).
                build();
        handler.execute(
                course,
                request, new UseCaseCommand.Callback<RecipeCourseResponse>() {

                    @Override
                    public void onSuccess(RecipeCourseResponse response) {
                        isCoursesLoading = false;
                        RecipeMediator.this.response.setCourseResponse(response);
                        sendGetAllResponse();
                    }

                    @Override
                    public void onError(RecipeCourseResponse response) {
                        isCoursesLoading = false;
                        RecipeMediator.this.response.setCourseResponse(response);
                        sendGetAllResponse();
                    }
                });
    }

    private void getPortionsData(String recipeId) {
        isPortionsLoading = true;
        RecipePortionsRequest request = RecipePortionsRequest.Builder.getDefault().
                setRecipeId(recipeId).
                build();
        handler.execute(
                portions,
                request,
                new UseCaseCommand.Callback<RecipePortionsResponse>() {

                    @Override
                    public void onSuccess(RecipePortionsResponse response) {
                        isPortionsLoading = false;
                        RecipeMediator.this.response.setPortionsResponse(response);
                        sendGetAllResponse();
                    }

                    @Override
                    public void onError(RecipePortionsResponse response) {
                        isPortionsLoading = false;
                        RecipeMediator.this.response.setPortionsResponse(response);
                        sendGetAllResponse();
                    }
                });
    }

    private void sendGetAllResponse() {
        if (!isIdentityLoading && !isDurationLoading && !isCoursesLoading && !isPortionsLoading) {
            getMediatorCallback().onSuccess(response);
        }
    }

    private void updateRecipeState(UseCaseCommand.Response  response) {

        if (response instanceof RecipeDurationResponse) {
            ComponentState state = ((RecipeDurationResponse) response).getState();
        }
    }
}
