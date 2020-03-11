package com.example.peter.thekitchenmenu.domain.usecase.recipe;

import com.example.peter.thekitchenmenu.domain.usecase.UseCase;

public class RecipeResponseBase implements UseCase.Response {

    protected String id;

    public String getId() {
        return id;
    }
}
