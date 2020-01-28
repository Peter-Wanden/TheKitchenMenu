package com.example.peter.thekitchenmenu.domain.usecase.recipecourse;

import com.example.peter.thekitchenmenu.domain.usecase.UseCaseCommand;
import com.example.peter.thekitchenmenu.domain.usecase.recipe.recipestate.RecipeStateCalculator;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

import javax.annotation.Nonnull;

public final class RecipeCourseResponse implements UseCaseCommand.Response {
    @Nonnull
    private final RecipeStateCalculator.ComponentState status;
    @Nonnull
    private final List<RecipeCourse.FailReason> failReasons;
    @Nonnull
    private final HashMap<RecipeCourse.Course, RecipeCourseModel> courseList;

    public RecipeCourseResponse(@Nonnull RecipeStateCalculator.ComponentState status,
                                @Nonnull List<RecipeCourse.FailReason> failReasons,
                                @Nonnull HashMap<RecipeCourse.Course, RecipeCourseModel> courseList)
    {
        this.status = status;
        this.failReasons = failReasons;
        this.courseList = courseList;
    }

    @Nonnull
    public RecipeStateCalculator.ComponentState getStatus() {
        return status;
    }

    @Nonnull
    public List<RecipeCourse.FailReason> getFailReasons() {
        return failReasons;
    }

    @Nonnull
    public HashMap<RecipeCourse.Course, RecipeCourseModel> getCourseList() {
        return courseList;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RecipeCourseResponse that = (RecipeCourseResponse) o;
        return status == that.status &&
                failReasons.equals(that.failReasons) &&
                courseList.equals(that.courseList);
    }

    @Override
    public int hashCode() {
        return Objects.hash(status, failReasons, courseList);
    }

    @Override
    public String toString() {
        return "RecipeCourseResponse{" +
                "status=" + status +
                ", failReasons=" + failReasons +
                ", courseList=" + courseList +
                '}';
    }

    public static class Builder {
        private RecipeStateCalculator.ComponentState status;
        private List<RecipeCourse.FailReason> failReasons;
        private HashMap<RecipeCourse.Course, RecipeCourseModel> courseList;

        public static Builder getDefault() {
            return new Builder().
                    setStatus(RecipeStateCalculator.ComponentState.INVALID_UNCHANGED).
                    setFailReasons(getDefaultFailReason()).
                    setCourseList(new HashMap<>());
        }

        public Builder setStatus(RecipeStateCalculator.ComponentState status) {
            this.status = status;
            return this;
        }

        public Builder setFailReasons(List<RecipeCourse.FailReason> failReasons) {
            this.failReasons = failReasons;
            return this;
        }

        public Builder setCourseList(HashMap<RecipeCourse.Course, RecipeCourseModel> courseList) {
            this.courseList = courseList;
            return this;
        }

        public RecipeCourseResponse build() {
            return new RecipeCourseResponse(
                    status,
                    failReasons,
                    courseList
            );
        }

        private static List<RecipeCourse.FailReason> getDefaultFailReason() {
            List<RecipeCourse.FailReason> defaultFailReason = new LinkedList<>();
            defaultFailReason.add(RecipeCourse.FailReason.NO_COURSES_SET);
            return defaultFailReason;
        }
    }
}
