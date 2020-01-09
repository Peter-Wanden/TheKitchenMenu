package com.example.peter.thekitchenmenu.domain.usecase.recipestate;

import java.util.HashMap;
import java.util.LinkedHashMap;

public class RecipeState {

    public enum State {
        INVALID_UNCHANGED,
        INVALID_CHANGED,
        VALID_UNCHANGED,
        VALID_CHANGED,
        COMPLETE
    }

    public enum FailReason {
        MISSING_MODELS,
        INVALID_MODELS,
        NONE
    }

    public enum ComponentName {
        IDENTITY,
        DURATION,
        COURSE,
        PORTIONS
    }

    public enum ComponentState {
        DATA_UNAVAILABLE,
        INVALID_UNCHANGED,
        VALID_UNCHANGED,
        INVALID_CHANGED,
        VALID_CHANGED
    }

    private HashMap<ComponentName, ComponentState> componentStateList = new LinkedHashMap<>();


}
