package com.example.peter.thekitchenmenu.domain.usecase.recipe.recipecourse;

import com.example.peter.thekitchenmenu.domain.usecase.FailReasons;
import com.example.peter.thekitchenmenu.domain.usecase.UseCaseCommand;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

import javax.annotation.Nonnull;

import static com.example.peter.thekitchenmenu.domain.usecase.recipe.recipestate.RecipeStateCalculator.*;

public final class RecipeCourseResponse implements UseCaseCommand.Response {
    @Nonnull
    private final ComponentState state;
    @Nonnull
    private final List<FailReasons> failReasons;
    @Nonnull
    private final HashMap<RecipeCourse.Course, RecipeCourseModel> courseList;

    public RecipeCourseResponse(@Nonnull ComponentState status,
                                @Nonnull List<FailReasons> failReasons,
                                @Nonnull HashMap<RecipeCourse.Course, RecipeCourseModel> courseList)
    {
        this.state = status;
        this.failReasons = failReasons;
        this.courseList = courseList;
    }

    @Nonnull
    public ComponentState getState() {
        return state;
    }

    @Nonnull
    public List<FailReasons> getFailReasons() {
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
        return state == that.state &&
                failReasons.equals(that.failReasons) &&
                courseList.equals(that.courseList);
    }

    @Override
    public int hashCode() {
        return Objects.hash(state, failReasons, courseList);
    }

    @Override
    public String toString() {
        return "RecipeCourseResponse{" +
                "state=" + state +
                ", failReasons=" + failReasons +
                ", courseList=" + courseList +
                '}';
    }

    public static class Builder {
        private ComponentState status;
        private List<FailReasons> failReasons;
        private HashMap<RecipeCourse.Course, RecipeCourseModel> courseList;

        public static Builder getDefault() {
            return new Builder().
                    setStatus(ComponentState.INVALID_UNCHANGED).
                    setFailReasons(getDefaultFailReason()).
                    setCourseList(new HashMap<>());
        }

        public Builder setStatus(ComponentState status) {
            this.status = status;
            return this;
        }

        public Builder setFailReasons(List<FailReasons> failReasons) {
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

        private static List<FailReasons> getDefaultFailReason() {
            List<FailReasons> defaultFailReason = new LinkedList<>();
            defaultFailReason.add(RecipeCourse.FailReason.NO_COURSES_SET);
            return defaultFailReason;
        }
    }
}
