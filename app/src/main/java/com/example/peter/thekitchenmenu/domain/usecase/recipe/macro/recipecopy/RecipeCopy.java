package com.example.peter.thekitchenmenu.domain.usecase.recipe.macro.recipecopy;

import android.annotation.SuppressLint;

import com.example.peter.thekitchenmenu.domain.model.CommonFailReason;
import com.example.peter.thekitchenmenu.domain.model.FailReasons;
import com.example.peter.thekitchenmenu.domain.usecase.UseCase;
import com.example.peter.thekitchenmenu.domain.usecase.UseCaseHandler;
import com.example.peter.thekitchenmenu.domain.usecase.recipe.component.course.RecipeCourseRequest;
import com.example.peter.thekitchenmenu.domain.usecase.recipe.component.course.RecipeCourseResponse;
import com.example.peter.thekitchenmenu.domain.usecase.recipe.component.duration.RecipeDurationRequest;
import com.example.peter.thekitchenmenu.domain.usecase.recipe.component.duration.RecipeDurationResponse;
import com.example.peter.thekitchenmenu.domain.usecase.recipe.component.identity.RecipeIdentityRequest;
import com.example.peter.thekitchenmenu.domain.usecase.recipe.component.identity.RecipeIdentityResponse;
import com.example.peter.thekitchenmenu.domain.usecase.recipe.macro.recipe.Recipe;
import com.example.peter.thekitchenmenu.domain.usecase.recipe.macro.recipe.RecipeRequest;
import com.example.peter.thekitchenmenu.domain.usecase.recipe.macro.recipe.RecipeResponse;
import com.example.peter.thekitchenmenu.domain.usecase.recipe.macro.RecipeUseCaseCallback;
import com.example.peter.thekitchenmenu.domain.usecase.recipe.component.portions.RecipePortionsRequest;
import com.example.peter.thekitchenmenu.domain.usecase.recipe.component.portions.RecipePortionsResponse;
import com.example.peter.thekitchenmenu.domain.usecase.recipe.metadata.RecipeMetadata;
import com.example.peter.thekitchenmenu.domain.utils.UniqueIdProvider;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Nonnull;

public class RecipeCopy extends UseCase {

    public enum FailReason implements FailReasons {
        SOURCE_DATA_ERROR();

        private final int id;

        @SuppressLint("UseSparseArrays")
        private static Map<Integer, FailReason> options = new HashMap<>();

        FailReason(int id) {
            this.id = id;
        }

        static {
            for (FailReason s : FailReason.values())
                options.put(s.id, s);
        }

        public static FailReason getById(int id) {
            return options.get(id);
        }

        @Override
        public int getId() {
            return id;
        }
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

    private List<RecipeMetadata.ComponentName> copyCompleteList = new ArrayList<>();

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
                getModel().
                getComponentResponses().
                get(RecipeMetadata.ComponentName.IDENTITY);

        RecipeIdentityRequest request = new RecipeIdentityRequest.Builder().
                basedOnResponse(response).
                setDataId(destinationId).
                build();

        handler.execute(destinationRecipe, request, new IdentityCallback());
    }

    private class IdentityCallback extends RecipeUseCaseCallback<RecipeIdentityResponse> {
        @Override
        protected void processResponse(RecipeIdentityResponse response) {
            System.out.println(response);
            // assert business data in source and destination are the same
            copyCompleteList.add(RecipeMetadata.ComponentName.IDENTITY);
            checkDataCopyComplete();
        }
    }

    private void copyCourse() {
        RecipeCourseResponse response = (RecipeCourseResponse) sourceData.
                getModel().
                getComponentResponses().
                get(RecipeMetadata.ComponentName.COURSE);

        RecipeCourseRequest request = new RecipeCourseRequest.Builder().
                basedOnResponse(response).
                setDataId(destinationId).
                build();

        handler.execute(destinationRecipe, request, new CourseCallback());
    }

    private class CourseCallback extends RecipeUseCaseCallback<RecipeCourseResponse> {
        @Override
        protected void processResponse(RecipeCourseResponse response) {
            // assert business data in source and destination are the same
            copyCompleteList.add(RecipeMetadata.ComponentName.COURSE);
            checkDataCopyComplete();
        }
    }

    private void copyDuration() {
        RecipeDurationResponse response = (RecipeDurationResponse) sourceData.
                getModel().
                getComponentResponses().
                get(RecipeMetadata.ComponentName.DURATION);

        RecipeDurationRequest request = new RecipeDurationRequest.Builder().
                basedOnResponse(response).
                setDataId(destinationId).
                build();

        handler.execute(destinationRecipe, request, new DurationCallback());
    }

    private class DurationCallback extends RecipeUseCaseCallback<RecipeDurationResponse> {
        @Override
        protected void processResponse(RecipeDurationResponse response) {
            // assert business data in source and destination are the same
            copyCompleteList.add(RecipeMetadata.ComponentName.DURATION);
            checkDataCopyComplete();
        }
    }

    private void copyPortions() {
        RecipePortionsResponse response = (RecipePortionsResponse) sourceData.
                getModel().
                getComponentResponses().get(RecipeMetadata.ComponentName.PORTIONS);

        RecipePortionsRequest request = new RecipePortionsRequest.Builder().
                basedOnResponse(response).
                setDataId(destinationId).
                build();

        handler.execute(destinationRecipe, request, new PortionsCallback());
    }

    private class PortionsCallback extends RecipeUseCaseCallback<RecipePortionsResponse> {
        @Override
        protected void processResponse(RecipePortionsResponse response) {
            // assert business data in source and destination are the same
            copyCompleteList.add(RecipeMetadata.ComponentName.PORTIONS);
            checkDataCopyComplete();
        }
    }

    private void checkDataCopyComplete() {
        if (copyCompleteList.contains(RecipeMetadata.ComponentName.IDENTITY) &&
                copyCompleteList.contains(RecipeMetadata.ComponentName.COURSE) &&
                copyCompleteList.contains(RecipeMetadata.ComponentName.DURATION) &&
                copyCompleteList.contains(RecipeMetadata.ComponentName.PORTIONS)) {
            verifyCopy();
        }
    }

    // TODO - verify the data and state copied
    private void verifyCopy() {
        buildResponse();
    }

    private void buildResponse() {
        RecipeCopyResponse response = new RecipeCopyResponse(destinationRecipe);

        if (destinationData.
                getModel().
                getRecipeStateResponse().
                getFailReasons().
                contains(CommonFailReason.NONE)) {
            getUseCaseCallback().onSuccess(response);
        } else {
            getUseCaseCallback().onError(response);
        }
    }
}
