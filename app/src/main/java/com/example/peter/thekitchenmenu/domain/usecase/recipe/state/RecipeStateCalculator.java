package com.example.peter.thekitchenmenu.domain.usecase.recipe.state;

import com.example.peter.thekitchenmenu.domain.usecase.FailReasons;
import com.example.peter.thekitchenmenu.domain.usecase.UseCase;
import com.example.peter.thekitchenmenu.domain.usecase.recipe.metadata.RecipeMetadata;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Set;

public class RecipeStateCalculator extends UseCase {

    private static final String TAG = "tkm-" + RecipeStateCalculator.class.getSimpleName() + ": ";

    private RecipeMetadata.RecipeState recipeState;
    private final List<FailReasons> failReasons;
    private final Set<RecipeMetadata.ComponentName> requiredComponents;
    private HashMap<RecipeMetadata.ComponentName, RecipeMetadata.ComponentState> componentStates;
    private boolean hasRequiredComponents;
    private boolean hasInvalidModels;

    public RecipeStateCalculator() {
        recipeState = RecipeMetadata.RecipeState.COMPLETE;
        failReasons = new ArrayList<>();

        requiredComponents = new HashSet<>();
        requiredComponents.add(RecipeMetadata.ComponentName.IDENTITY);
        requiredComponents.add(RecipeMetadata.ComponentName.COURSE);
        requiredComponents.add(RecipeMetadata.ComponentName.DURATION);
        requiredComponents.add(RecipeMetadata.ComponentName.PORTIONS);
    }

    @Override
    protected <Q extends Request> void execute(Q request) {
        RecipeStateRequest stateRequest = (RecipeStateRequest) request;
        System.out.println(TAG + stateRequest);

        recipeState = RecipeMetadata.RecipeState.COMPLETE;
        failReasons.clear();
        componentStates = stateRequest.getComponentStates();

        calculateState();
    }

    private void calculateState() {
        checkForRequiredComponents();
        checkForInvalidComponents();
        checkForValidComponents();

        buildResponse();
    }

    private void checkForRequiredComponents() {
        int noOfRequiredComponents = requiredComponents.size();
        int noOfRequiredComponentsSubmitted = 0;

        for (RecipeMetadata.ComponentName requiredComponentName : requiredComponents) {
            if (componentStates.containsKey(requiredComponentName) &&
                    componentStates.get(requiredComponentName) != RecipeMetadata.ComponentState.DATA_UNAVAILABLE) {
                noOfRequiredComponentsSubmitted++;
            } else {
                componentStates.put(requiredComponentName, RecipeMetadata.ComponentState.DATA_UNAVAILABLE);
                recipeState = RecipeMetadata.RecipeState.DATA_UNAVAILABLE;
                addFailReasonMissingModels();
            }
        }
        hasRequiredComponents = noOfRequiredComponentsSubmitted >= noOfRequiredComponents;
    }

    private void addFailReasonMissingModels() {
        if (!failReasons.contains(RecipeMetadata.FailReason.MISSING_COMPONENTS)) {
            failReasons.add(RecipeMetadata.FailReason.MISSING_COMPONENTS);
        }
    }

    private void checkForInvalidComponents() {
//        if (hasRequiredComponents) {
//            for (RecipeMetadata.ComponentName componentName : componentStates.keySet()) {
//                RecipeMetadata.ComponentState componentState = componentStates.get(componentName);
//
//                if (componentState == RecipeMetadata.ComponentState.INVALID_UNCHANGED &&
//                        recipeState.getStateLevel() > RecipeMetadata.RecipeState.INVALID_UNCHANGED.stateLevel) {
//
//                    recipeState = RecipeMetadata.RecipeState.INVALID_UNCHANGED;
//                    addFailReasonInvalidComponents();
//
//                } else if (componentState == RecipeMetadata.ComponentState.INVALID_CHANGED &&
//                        recipeState.getStateLevel() > RecipeMetadata.RecipeState.INVALID_CHANGED.stateLevel) {
//
//                    recipeState = RecipeMetadata.RecipeState.INVALID_CHANGED;
//                    addFailReasonInvalidComponents();
//                }
//            }
//        }
    }

    private void addFailReasonInvalidComponents() {
        hasInvalidModels = true;
        failReasons.add(RecipeMetadata.FailReason.INVALID_COMPONENTS);
    }

    private void checkForValidComponents() {
//        if (hasRequiredComponents && !hasInvalidModels) {
//            for (RecipeMetadata.ComponentName componentName : componentStates.keySet()) {
//
//                RecipeMetadata.ComponentState componentState = componentStates.get(componentName);
//                if (componentState == RecipeMetadata.ComponentState.VALID_UNCHANGED &&
//                        recipeState.getStateLevel() > RecipeMetadata.RecipeState.VALID_UNCHANGED.stateLevel) {
//
//                    recipeState = RecipeMetadata.RecipeState.VALID_UNCHANGED;
//                    addFailReasonNone();
//
//                } else if (componentState == RecipeMetadata.ComponentState.VALID_CHANGED &&
//                        recipeState.getStateLevel() > RecipeMetadata.RecipeState.VALID_CHANGED.stateLevel) {
//
//                    recipeState = RecipeMetadata.RecipeState.VALID_CHANGED;
//                    addFailReasonNone();
//                }
//            }
//        }
    }

    private void addFailReasonNone() {
        if (!failReasons.contains(RecipeMetadata.FailReason.NONE)) {
            failReasons.add(RecipeMetadata.FailReason.NONE);
        }
    }

    private void buildResponse() {
        RecipeStateResponse recipeStateResponse = new RecipeStateResponse(
                RecipeMetadata.RecipeState.getStateFromSeverityLevel(recipeState.getStateLevel()),
                new ArrayList<>(failReasons),
                new LinkedHashMap<>(componentStates)
        );
        sendResponse(recipeStateResponse);
    }

    private void sendResponse(RecipeStateResponse response) {
        System.out.println(TAG + response);
        if (isValid()) {
            getUseCaseCallback().onSuccess(response);
        } else {
            getUseCaseCallback().onError(response);
        }
    }

    private boolean isValid() {
        return recipeState == RecipeMetadata.RecipeState.VALID_UNCHANGED ||
                recipeState == RecipeMetadata.RecipeState.VALID_CHANGED ||
                recipeState == RecipeMetadata.RecipeState.COMPLETE;
    }
}
