package com.example.peter.thekitchenmenu.domain.usecase.recipe;

import com.example.peter.thekitchenmenu.domain.usecase.UseCase;

public abstract class RecipeRequestAbstract implements UseCase.Request {

    protected String id;
    protected String cloneToId;

    public String getId() {
        return id;
    }

    public String getCloneToId() {
        return cloneToId;
    }
}
