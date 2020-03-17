package com.example.peter.thekitchenmenu.domain.usecase.recipe.macro.recipecopy;

import com.example.peter.thekitchenmenu.domain.usecase.CommonFailReason;
import com.example.peter.thekitchenmenu.domain.usecase.FailReasons;
import com.example.peter.thekitchenmenu.domain.usecase.UseCase;
import com.example.peter.thekitchenmenu.domain.usecase.UseCaseHandler;
import com.example.peter.thekitchenmenu.domain.usecase.recipe.component.recipecourse.RecipeCourseRequest;
import com.example.peter.thekitchenmenu.domain.usecase.recipe.component.recipecourse.RecipeCourseResponse;
import com.example.peter.thekitchenmenu.domain.usecase.recipe.component.recipeduration.RecipeDurationRequest;
import com.example.peter.thekitchenmenu.domain.usecase.recipe.component.recipeduration.RecipeDurationResponse;
import com.example.peter.thekitchenmenu.domain.usecase.recipe.component.recipeidentity.RecipeIdentityRequest;
import com.example.peter.thekitchenmenu.domain.usecase.recipe.component.recipeidentity.RecipeIdentityResponse;
import com.example.peter.thekitchenmenu.domain.usecase.recipe.macro.recipe.Recipe;
import com.example.peter.thekitchenmenu.domain.usecase.recipe.macro.recipe.RecipeRequest;
import com.example.peter.thekitchenmenu.domain.usecase.recipe.macro.recipe.RecipeResponse;
import com.example.peter.thekitchenmenu.domain.usecase.recipe.macro.RecipeUseCaseCallback;
import com.example.peter.thekitchenmenu.domain.usecase.recipe.component.recipeportions.RecipePortionsRequest;
import com.example.peter.thekitchenmenu.domain.usecase.recipe.component.recipeportions.RecipePortionsResponse;
import com.example.peter.thekitchenmenu.domain.utils.UniqueIdProvider;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nonnull;

import static com.example.peter.thekitchenmenu.domain.usecase.recipe.state.RecipeStateCalculator.*;

public class RecipeCopy extends UseCase {

    enum FailReason implements FailReasons {
        SOURCE_DATA_ERROR
    }

    @Nonnull
    private UseCaseHandler handler;
    @Nonnull
    private UniqueIdProvider idProvider;

    @Nonnull
    private final Recipe sourceRecipe;
    private String sourceId;
    private RecipeResponse sourceData;

    @Nonnull
    private final Recipe destinationRecipe;
    private String destinationId;
    private RecipeResponse destinationData;

    private List<ComponentName> copyCompleteList = new ArrayList<>();

    public RecipeCopy(@Nonnull UseCaseHandler handler,
                      @Nonnull UniqueIdProvider idProvider,
                      @Nonnull Recipe sourceRecipe,
                      @Nonnull Recipe destinationRecipe) {
        this.handler = handler;
        this.idProvider = idProvider;
        this.sourceRecipe = sourceRecipe;
        this.destinationRecipe = destinationRecipe;
    }

    @Override
    protected <Q extends Request> void execute(Q request) {
        copyCompleteList.clear();
        sourceId = (((RecipeCopyRequest) request).getSourceId());
        initialiseDestinationRecipe();
    }

    private void initialiseDestinationRecipe() {
        destinationId = idProvider.getUId();
        RecipeRequest request = new RecipeRequest(destinationId);

        handler.execute(destinationRecipe, request, new RecipeListener());
    }

    private class RecipeListener extends RecipeUseCaseCallback<RecipeResponse> {
        @Override
        protected void processResponse(RecipeResponse response) {
            destinationData = response;
            loadSourceRecipe(sourceId);
        }
    }

    private void loadSourceRecipe(String sourceId) {
        RecipeRequest request = new RecipeRequest(sourceId);
        handler.execute(sourceRecipe, request, new SourceCallback());
    }

    private class SourceCallback extends RecipeUseCaseCallback<RecipeResponse> {
        @Override
        protected void processResponse(RecipeResponse response) {
            sourceData = response;
            copyData();
        }
    }

    private void copyData() {
        copyIdentity();
        copyCourse();
        copyDuration();
        copyPortions();
    }

    private void copyIdentity() {
        RecipeIdentityResponse response = (RecipeIdentityResponse) sourceData.
                getComponentResponses().
                get(ComponentName.IDENTITY);

        RecipeIdentityRequest request = new RecipeIdentityRequest.Builder().
                basedOnResponse(response).
                setId(destinationId).
                build();

        handler.execute(destinationRecipe, request, new IdentityCallback());
    }

    private class IdentityCallback extends RecipeUseCaseCallback<RecipeIdentityResponse> {
        @Override
        protected void processResponse(RecipeIdentityResponse response) {
            System.out.println(response);
            // assert business data in source and destination are the same
            copyCompleteList.add(ComponentName.IDENTITY);
            checkDataCopyComplete();
        }
    }

    private void copyCourse() {
        RecipeCourseResponse response = (RecipeCourseResponse) sourceData.
                getComponentResponses().
                get(ComponentName.COURSE);

        RecipeCourseRequest request = new RecipeCourseRequest.Builder().
                basedOnResponse(response).
                setId(destinationId).
                build();

        handler.execute(destinationRecipe, request, new CourseCallback());
    }

    private class CourseCallback extends RecipeUseCaseCallback<RecipeCourseResponse> {
        @Override
        protected void processResponse(RecipeCourseResponse response) {
            // assert business data in source and destination are the same
            copyCompleteList.add(ComponentName.COURSE);
            checkDataCopyComplete();
        }
    }

    private void copyDuration() {
        RecipeDurationResponse response = (RecipeDurationResponse) sourceData.
                getComponentResponses().
                get(ComponentName.DURATION);

        RecipeDurationRequest request = new RecipeDurationRequest.Builder().
                basedOnResponse(response).
                setId(destinationId).
                build();

        handler.execute(destinationRecipe, request, new DurationCallback());
    }

    private class DurationCallback extends RecipeUseCaseCallback<RecipeDurationResponse> {
        @Override
        protected void processResponse(RecipeDurationResponse response) {
            // assert business data in source and destination are the same
            copyCompleteList.add(ComponentName.DURATION);
            checkDataCopyComplete();
        }
    }

    private void copyPortions() {
        RecipePortionsResponse response = (RecipePortionsResponse) sourceData.
                getComponentResponses().get(ComponentName.PORTIONS);

        RecipePortionsRequest request = new RecipePortionsRequest.Builder().
                basedOnResponse(response).
                setId(destinationId).
                build();

        handler.execute(destinationRecipe, request, new PortionsCallback());
    }

    private class PortionsCallback extends RecipeUseCaseCallback<RecipePortionsResponse> {
        @Override
        protected void processResponse(RecipePortionsResponse response) {
            // assert business data in source and destination are the same
            copyCompleteList.add(ComponentName.PORTIONS);
            checkDataCopyComplete();
        }
    }

    private void checkDataCopyComplete() {
        if (copyCompleteList.contains(ComponentName.IDENTITY) &&
                copyCompleteList.contains(ComponentName.COURSE) &&
                copyCompleteList.contains(ComponentName.DURATION) &&
                copyCompleteList.contains(ComponentName.PORTIONS)) {
            verifyCopy();
        }
    }

    // TODO - verify the data and state copied
    private void verifyCopy() {
        buildResponse();
    }

    private void buildResponse() {
        RecipeCopyResponse response = new RecipeCopyResponse(destinationRecipe);

        if (destinationData.getRecipeStateResponse().
                getFailReasons().
                contains(CommonFailReason.NONE)) {
            getUseCaseCallback().onSuccess(response);
        } else {
            getUseCaseCallback().onError(response);
        }
    }
}
