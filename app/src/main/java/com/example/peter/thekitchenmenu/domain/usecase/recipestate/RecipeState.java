package com.example.peter.thekitchenmenu.domain.usecase.recipestate;

public class RecipeState {

    public enum State {
        INVALID,
        VALID,
        COMPLETE
    }

    public enum ComponentName {
        COURSE,
        DURATION,
        IDENTITY,
        PORTIONS
    }

    public enum ComponentState {
        DATA_UNAVAILABLE,
        INVALID_UNCHANGED,
        VALID_UNCHANGED,
        INVALID_CHANGED,
        VALID_CHANGED
    }
}
