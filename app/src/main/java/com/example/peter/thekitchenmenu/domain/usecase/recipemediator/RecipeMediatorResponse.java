package com.example.peter.thekitchenmenu.domain.usecase.recipemediator;

import com.example.peter.thekitchenmenu.domain.usecase.recipecourse.RecipeCourseResponse;
import com.example.peter.thekitchenmenu.domain.usecase.recipeduration.RecipeDurationResponse;
import com.example.peter.thekitchenmenu.domain.usecase.recipeidentity.RecipeIdentityResponse;
import com.example.peter.thekitchenmenu.domain.usecase.recipeportions.RecipePortionsResponse;

public class RecipeMediatorResponse implements RecipeMediatorAbstract.Response {

    private RecipeIdentityResponse identityResponse;
    private RecipeDurationResponse durationResponse;
    private RecipeCourseResponse courseResponse;
    private RecipePortionsResponse portionsResponse;

    public RecipeIdentityResponse getIdentityResponse() {
        return identityResponse;
    }

    public void setIdentityResponse(RecipeIdentityResponse identityResponse) {
        this.identityResponse = identityResponse;
    }

    public RecipeDurationResponse getDurationResponse() {
        return durationResponse;
    }

    public void setDurationResponse(RecipeDurationResponse durationResponse) {
        this.durationResponse = durationResponse;
    }

    public RecipeCourseResponse getCourseResponse() {
        return courseResponse;
    }

    public void setCourseResponse(RecipeCourseResponse courseResponse) {
        this.courseResponse = courseResponse;
    }

    public RecipePortionsResponse getPortionsResponse() {
        return portionsResponse;
    }

    public void setPortionsResponse(RecipePortionsResponse portionsResponse) {
        this.portionsResponse = portionsResponse;
    }

    @Override
    public String toString() {
        return "RecipeMediatorResponse{" +
                "identityResponse=" + identityResponse +
                ", durationResponse=" + durationResponse +
                ", courseResponse=" + courseResponse +
                ", portionsResponse=" + portionsResponse +
                '}';
    }
}
