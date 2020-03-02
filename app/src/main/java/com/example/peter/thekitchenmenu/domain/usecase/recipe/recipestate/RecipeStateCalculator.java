package com.example.peter.thekitchenmenu.domain.usecase.recipe.recipestate;

import android.annotation.SuppressLint;

import com.example.peter.thekitchenmenu.domain.usecase.FailReasons;
import com.example.peter.thekitchenmenu.domain.usecase.UseCase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class RecipeStateCalculator extends UseCase {

    private static final String TAG = "tkm-" + RecipeStateCalculator.class.getSimpleName() + ": ";

    public enum RecipeState {
        DATA_UNAVAILABLE(0),
        INVALID_UNCHANGED(1),
        INVALID_CHANGED(2),
        VALID_CHANGED(3),
        VALID_UNCHANGED(4),
        COMPLETE(5);

        private final int stateLevel;

        @SuppressLint("UseSparseArrays")
        private static Map<Integer, RecipeState> recipeStates = new HashMap<>();

        RecipeState(int stateLevel) {
            this.stateLevel = stateLevel;
        }

        static {
            for (RecipeState recipeState : RecipeState.values()) {
                recipeStates.put(recipeState.stateLevel, recipeState);
            }
        }

        public static RecipeState getStateFromSeverityLevel(int severityLevel) {
            return recipeStates.get(severityLevel);
        }

        public int getStateLevel() {
            return stateLevel;
        }
    }

    public enum FailReason implements FailReasons {
        MISSING_COMPONENTS,
        INVALID_COMPONENTS,
        NONE
    }

    public enum ComponentName {
        IDENTITY,
        DURATION,
        COURSE,
        PORTIONS,
        TEXT_VALIDATOR,
        RECIPE
    }

    public enum ComponentState {
        DATA_UNAVAILABLE,
        INVALID_UNCHANGED,
        INVALID_CHANGED,
        VALID_UNCHANGED,
        VALID_CHANGED
    }

    private RecipeState recipeState;
    private final List<FailReasons> failReasons;
    private final Set<ComponentName> requiredComponents;
    private HashMap<ComponentName, ComponentState> componentStates;
    private boolean hasRequiredComponents;
    private boolean hasInvalidModels;

    public RecipeStateCalculator() {
        recipeState = RecipeState.COMPLETE;
        failReasons = new ArrayList<>();

        requiredComponents = new HashSet<>();
        requiredComponents.add(ComponentName.IDENTITY);
        requiredComponents.add(ComponentName.COURSE);
        requiredComponents.add(ComponentName.DURATION);
        requiredComponents.add(ComponentName.PORTIONS);
    }

    @Override
    protected <Q extends Request> void execute(Q request) {
        RecipeStateRequest rsr = (RecipeStateRequest) request;

        System.out.println(TAG + rsr);
        recipeState = RecipeState.COMPLETE;
        failReasons.clear();
        componentStates = rsr.getComponentStates();

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

        for (ComponentName requiredComponentName : requiredComponents) {
            if (componentStates.containsKey(requiredComponentName) &&
                    componentStates.get(requiredComponentName) != ComponentState.DATA_UNAVAILABLE) {
                noOfRequiredComponentsSubmitted++;
            } else {
                componentStates.put(requiredComponentName, ComponentState.DATA_UNAVAILABLE);
                recipeState = RecipeState.DATA_UNAVAILABLE;
                addFailReasonMissingModels();
            }
        }
        hasRequiredComponents = noOfRequiredComponentsSubmitted >= noOfRequiredComponents;
    }

    private void addFailReasonMissingModels() {
        if (!failReasons.contains(FailReason.MISSING_COMPONENTS)) {
            failReasons.add(FailReason.MISSING_COMPONENTS);
        }
    }

    private void checkForInvalidComponents() {
        if (hasRequiredComponents) {
            for (ComponentName componentName : componentStates.keySet()) {
                ComponentState componentState = componentStates.get(componentName);

                if (componentState == ComponentState.INVALID_UNCHANGED &&
                        recipeState.getStateLevel() > RecipeState.INVALID_UNCHANGED.stateLevel) {

                    recipeState = RecipeState.INVALID_UNCHANGED;
                    addFailReasonInvalidComponents();

                } else if (componentState == ComponentState.INVALID_CHANGED &&
                        recipeState.getStateLevel() > RecipeState.INVALID_CHANGED.stateLevel) {

                    recipeState = RecipeState.INVALID_CHANGED;
                    addFailReasonInvalidComponents();
                }
            }
        }
    }

    private void addFailReasonInvalidComponents() {
        hasInvalidModels = true;
        failReasons.add(FailReason.INVALID_COMPONENTS);
    }

    private void checkForValidComponents() {
        if (hasRequiredComponents && !hasInvalidModels) {
            for (ComponentName componentName : componentStates.keySet()) {

                ComponentState componentState = componentStates.get(componentName);
                if (componentState == ComponentState.VALID_UNCHANGED &&
                        recipeState.getStateLevel() > RecipeState.VALID_UNCHANGED.stateLevel) {

                    recipeState = RecipeState.VALID_UNCHANGED;
                    addFailReasonNone();

                } else if (componentState == ComponentState.VALID_CHANGED &&
                        recipeState.getStateLevel() > RecipeState.VALID_CHANGED.stateLevel) {

                    recipeState = RecipeState.VALID_CHANGED;
                    addFailReasonNone();
                }
            }
        }
    }

    private void addFailReasonNone() {
        if (!failReasons.contains(FailReason.NONE)) {
            failReasons.add(FailReason.NONE);
        }
    }

    private void buildResponse() {
        RecipeStateResponse recipeStateResponse = new RecipeStateResponse(
                RecipeState.getStateFromSeverityLevel(recipeState.getStateLevel()),
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
        return recipeState == RecipeState.VALID_UNCHANGED ||
                recipeState == RecipeState.VALID_CHANGED ||
                recipeState == RecipeState.COMPLETE;
    }
}
