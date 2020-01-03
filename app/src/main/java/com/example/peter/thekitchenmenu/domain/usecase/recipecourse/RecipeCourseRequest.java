package com.example.peter.thekitchenmenu.domain.usecase.recipecourse;

import com.example.peter.thekitchenmenu.domain.UseCaseInteractor;

import java.util.Objects;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class RecipeCourseRequest implements UseCaseInteractor.Request {
    @Nonnull
    private final String recipeId;
    @Nonnull
    private final String cloneToRecipeId;
    @Nullable
    private final RecipeCourse.Course course;
    private final boolean addCourse;

    public RecipeCourseRequest(@Nonnull String recipeId,
                   @Nonnull String cloneToRecipeId,
                   @Nullable RecipeCourse.Course course,
                   boolean addCourse) {
        this.recipeId = recipeId;
        this.cloneToRecipeId = cloneToRecipeId;
        this.course = course;
        this.addCourse = addCourse;
    }

    @Nonnull
    public String getRecipeId() {
        return recipeId;
    }

    @Nonnull
    public String getCloneToRecipeId() {
        return cloneToRecipeId;
    }

    @Nullable
    public RecipeCourse.Course getCourse() {
        return course;
    }

    public boolean isAddCourse() {
        return addCourse;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RecipeCourseRequest request = (RecipeCourseRequest) o;
        return addCourse == request.addCourse &&
                recipeId.equals(request.recipeId) &&
                cloneToRecipeId.equals(request.cloneToRecipeId) &&
                course == request.course;
    }

    @Override
    public int hashCode() {
        return Objects.hash(recipeId, cloneToRecipeId, course, addCourse);
    }

    @Override
    public String toString() {
        return "Request{" +
                "recipeId='" + recipeId + '\'' +
                ", cloneToRecipeId='" + cloneToRecipeId + '\'' +
                ", course=" + course +
                ", addCourse=" + addCourse +
                '}';
    }
}
