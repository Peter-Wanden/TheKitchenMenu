package com.example.peter.thekitchenmenu.domain.usecase.recipe.recipecourse;

import com.example.peter.thekitchenmenu.domain.usecase.CommonFailReason;
import com.example.peter.thekitchenmenu.domain.usecase.FailReasons;
import com.example.peter.thekitchenmenu.domain.usecase.recipe.RecipeResponseAbstract;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

import javax.annotation.Nonnull;

import static com.example.peter.thekitchenmenu.domain.usecase.recipe.recipestate.RecipeStateCalculator.*;

public final class RecipeCourseResponse extends RecipeResponseAbstract {
    @Nonnull
    private final ComponentState state;
    @Nonnull
    private final List<FailReasons> failReasons;
    @Nonnull
    private final HashMap<RecipeCourse.Course, RecipeCourseModel> courseList;

    public RecipeCourseResponse(@Nonnull String id,
                                @Nonnull ComponentState status,
                                @Nonnull List<FailReasons> failReasons,
                                @Nonnull HashMap<RecipeCourse.Course, RecipeCourseModel> courseList)
    {
        this.id = id;
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
        return id.equals(that.id) &&
                state == that.state &&
                failReasons.equals(that.failReasons) &&
                courseList.equals(that.courseList);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, state, failReasons, courseList);
    }

    @Override
    public String toString() {
        return "RecipeCourseResponse{" +
                "id=" + id +
                ", state=" + state +
                ", failReasons=" + failReasons +
                ", courseList=" + courseList +
                '}';
    }

    public static class Builder {
        private String id;
        private ComponentState status;
        private List<FailReasons> failReasons;
        private HashMap<RecipeCourse.Course, RecipeCourseModel> courseList;

        public static Builder getDefault() {
            return new Builder().
                    setId("").
                    setStatus(ComponentState.INVALID_UNCHANGED).
                    setFailReasons(getDefaultFailReason()).
                    setCourseList(new HashMap<>());
        }

        public Builder setId(String id) {
            this.id = id;
            return this;
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
                    id,
                    status,
                    failReasons,
                    courseList
            );
        }

        private static List<FailReasons> getDefaultFailReason() {
            List<FailReasons> defaultFailReason = new LinkedList<>();
            defaultFailReason.add(CommonFailReason.DATA_UNAVAILABLE);
            return defaultFailReason;
        }
    }
}
