package com.example.peter.thekitchenmenu.domain.usecase.recipemediator;

import com.example.peter.thekitchenmenu.domain.usecase.recipeduration.RecipeDurationResponse;
import com.example.peter.thekitchenmenu.domain.usecase.recipeidentity.RecipeIdentityResponse;

public class RecipeMediatorResponse implements RecipeMediatorAbstract.Response {

    private RecipeIdentityResponse identityResponse;
    private RecipeDurationResponse durationResponse;

    public RecipeIdentityResponse getIdentityResponse() {
        return identityResponse;
    }

    public RecipeMediatorResponse setIdentityResponse(RecipeIdentityResponse identityResponse) {
        this.identityResponse = identityResponse;
        return this;
    }

    public RecipeDurationResponse getDurationResponse() {
        return durationResponse;
    }

    public RecipeMediatorResponse setDurationResponse(RecipeDurationResponse durationResponse) {
        this.durationResponse = durationResponse;
        return this;
    }

    @Override
    public String toString() {
        return "RecipeMediatorResponse{" +
                "identityResponse=" + identityResponse +
                ", durationResponse=" + durationResponse +
                '}';
    }
}
