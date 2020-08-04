package com.example.peter.thekitchenmenu.domain.usecase.recipe.macro.recipecopy;

import android.annotation.SuppressLint;

import com.example.peter.thekitchenmenu.domain.usecasenew.recipe.component.RecipeComponentName;
import com.example.peter.thekitchenmenu.domain.usecasenew.common.failreasons.FailReasons;
import com.example.peter.thekitchenmenu.domain.usecase.common.UseCaseBase;
import com.example.peter.thekitchenmenu.domain.usecasenew.common.UseCaseHandler;
import com.example.peter.thekitchenmenu.domain.usecase.recipe.component.course.RecipeCourseRequest;
import com.example.peter.thekitchenmenu.domain.usecase.recipe.component.course.RecipeCourseResponse;
import com.example.peter.thekitchenmenu.domain.usecase.recipe.component.duration.RecipeDurationRequest;
import com.example.peter.thekitchenmenu.domain.usecase.recipe.component.duration.RecipeDurationResponse;
import com.example.peter.thekitchenmenu.domain.usecase.recipe.component.identity.RecipeIdentityRequest;
import com.example.peter.thekitchenmenu.domain.usecase.recipe.component.identity.RecipeIdentityResponse;
import com.example.peter.thekitchenmenu.domain.usecase.recipe.component.portions.RecipePortionsRequest;
import com.example.peter.thekitchenmenu.domain.usecase.recipe.component.portions.RecipePortionsResponse;
import com.example.peter.thekitchenmenu.domain.usecase.recipe.macro.RecipeUseCaseCallback;
import com.example.peter.thekitchenmenu.domain.usecase.recipe.macro.recipe.Recipe;
import com.example.peter.thekitchenmenu.domain.usecase.recipe.macro.recipe.RecipeRequest;
import com.example.peter.thekitchenmenu.domain.usecase.recipe.macro.recipe.RecipeResponse;
import com.example.peter.thekitchenmenu.domain.utils.UniqueIdProvider;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Nonnull;

public class RecipeCopy extends UseCaseBase {

    public enum FailReason implements FailReasons {
        SOURCE_DATA_ERROR(999);

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

    private List<RecipeComponentName> copyCompleteList = new ArrayList<>();

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
        RecipeRequest request = new RecipeRequest.Builder().
                setDataId(destinationId).
                build();

        handler.executeAsync(destinationRecipe, request, new RecipeListener());
    }

    private class RecipeListener extends RecipeUseCaseCallback<RecipeResponse> {
        @Override
        protected void processResponse(RecipeResponse response) {
            destinationData = response;
            loadSourceRecipe(sourceId);
        }
    }

    private void loadSourceRecipe(String sourceId) {
        RecipeRequest request = new RecipeRequest.Builder().setDomainId(sourceId).build();
        handler.executeAsync(sourceRecipe, request, new SourceCallback());
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
                getDomainModel().
                getComponentResponses().
                get(RecipeComponentName.IDENTITY);

        RecipeIdentityRequest request = new RecipeIdentityRequest.Builder().
                basedOnResponse(response).
                setDataId(destinationId).
                build();

        handler.executeAsync(destinationRecipe, request, new IdentityCallback());
    }

    private class IdentityCallback extends RecipeUseCaseCallback<RecipeIdentityResponse> {
        @Override
        protected void processResponse(RecipeIdentityResponse response) {
            System.out.println(response);
            // assert business data in source and destination are the same
            copyCompleteList.add(RecipeComponentName.IDENTITY);
            checkDataCopyComplete();
        }
    }

    private void copyCourse() {
        RecipeCourseResponse response = (RecipeCourseResponse) sourceData.
                getDomainModel().
                getComponentResponses().
                get(RecipeComponentName.COURSE);

        RecipeCourseRequest request = new RecipeCourseRequest.Builder().
                basedOnResponse(response).
                setDataId(destinationId).
                build();

        handler.executeAsync(destinationRecipe, request, new CourseCallback());
    }

    private class CourseCallback extends RecipeUseCaseCallback<RecipeCourseResponse> {
        @Override
        protected void processResponse(RecipeCourseResponse response) {
            // assert business data in source and destination are the same
            copyCompleteList.add(RecipeComponentName.COURSE);
            checkDataCopyComplete();
        }
    }

    private void copyDuration() {
        RecipeDurationResponse response = (RecipeDurationResponse) sourceData.
                getDomainModel().
                getComponentResponses().
                get(RecipeComponentName.DURATION);

        RecipeDurationRequest request = new RecipeDurationRequest.Builder().
                basedOnResponse(response).
                setDataId(destinationId).
                build();

        handler.executeAsync(destinationRecipe, request, new DurationCallback());
    }

    private class DurationCallback extends RecipeUseCaseCallback<RecipeDurationResponse> {
        @Override
        protected void processResponse(RecipeDurationResponse response) {
            // assert business data in source and destination are the same
            copyCompleteList.add(RecipeComponentName.DURATION);
            checkDataCopyComplete();
        }
    }

    private void copyPortions() {
        RecipePortionsResponse response = (RecipePortionsResponse) sourceData.
                getDomainModel().
                getComponentResponses().get(RecipeComponentName.PORTIONS);

        RecipePortionsRequest request = new RecipePortionsRequest.Builder().
                basedOnResponse(response).
                setDataId(destinationId).
                build();

        handler.executeAsync(destinationRecipe, request, new PortionsCallback());
    }

    private class PortionsCallback extends RecipeUseCaseCallback<RecipePortionsResponse> {
        @Override
        protected void processResponse(RecipePortionsResponse response) {
            // assert business data in source and destination are the same
            copyCompleteList.add(RecipeComponentName.PORTIONS);
            checkDataCopyComplete();
        }
    }

    private void checkDataCopyComplete() {
        if (copyCompleteList.contains(RecipeComponentName.IDENTITY) &&
                copyCompleteList.contains(RecipeComponentName.COURSE) &&
                copyCompleteList.contains(RecipeComponentName.DURATION) &&
                copyCompleteList.contains(RecipeComponentName.PORTIONS)) {
            verifyCopy();
        }
    }

    // TODO - verify the data and state copied
    private void verifyCopy() {
        buildResponse();
    }

    private void buildResponse() {
//        RecipeCopyResponse response = new RecipeCopyResponse(destinationRecipe);
//
//        if (destinationData.
//                getModel().
//                getRecipeStateResponse().
//                getFailReasons().
//                contains(CommonFailReason.NONE)) {
//            getUseCaseCallback().onSuccess(response);
//        } else {
//            getUseCaseCallback().onError(response);
//        }
    }
}
