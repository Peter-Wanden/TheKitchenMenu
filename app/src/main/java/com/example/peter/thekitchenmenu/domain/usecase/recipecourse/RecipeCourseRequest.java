package com.example.peter.thekitchenmenu.domain.usecase.recipecourse;

import com.example.peter.thekitchenmenu.domain.UseCaseInteractor;

import java.util.Objects;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import static com.example.peter.thekitchenmenu.domain.usecase.recipecourse.RecipeCourse.DO_NOT_CLONE;

public final class RecipeCourseRequest implements UseCaseInteractor.Request {
    @Nonnull
    private final String recipeId;
    @Nonnull
    private final String cloneToRecipeId;
    @Nullable
    private final RecipeCourse.Course course;
    private final boolean addCourse;

    private RecipeCourseRequest(@Nonnull String recipeId,
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

    @Nonnull
    @Override
    public String toString() {
        return "RecipeCourseRequest{" +
                "recipeId='" + recipeId + '\'' +
                ", cloneToRecipeId='" + cloneToRecipeId + '\'' +
                ", course=" + course +
                ", addCourse=" + addCourse +
                '}';
    }

    public static class Builder {
        private String recipeId;
        private String cloneToRecipeId;
        private RecipeCourse.Course course;
        private boolean isAddCourse;

        public static Builder getDefault() {
            return new Builder().
                    setRecipeId("").
                    setCloneToRecipeId(DO_NOT_CLONE).
                    setCourse(null).
                    setAddCourse(false);
        }

        public Builder setRecipeId(String recipeId) {
            this.recipeId = recipeId;
            return this;
        }

        public Builder setCloneToRecipeId(String cloneToRecipeId) {
            this.cloneToRecipeId = cloneToRecipeId;
            return this;
        }

        public Builder setCourse(RecipeCourse.Course course) {
            this.course = course;
            return this;
        }

        public Builder setAddCourse(boolean addCourse) {
            isAddCourse = addCourse;
            return this;
        }

        public RecipeCourseRequest build() {
            return new RecipeCourseRequest(
                    recipeId,
                    cloneToRecipeId,
                    course,
                    isAddCourse
            );
        }
    }
}
