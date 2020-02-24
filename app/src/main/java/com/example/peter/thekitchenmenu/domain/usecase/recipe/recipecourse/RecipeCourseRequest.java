package com.example.peter.thekitchenmenu.domain.usecase.recipe.recipecourse;

import com.example.peter.thekitchenmenu.domain.usecase.recipe.RecipeRequestAbstract;

import java.util.Objects;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import static com.example.peter.thekitchenmenu.domain.usecase.recipe.recipe.Recipe.DO_NOT_CLONE;

public final class RecipeCourseRequest extends RecipeRequestAbstract {
    @Nullable
    private final RecipeCourse.Course course;
    private final boolean addCourse;

    private RecipeCourseRequest(@Nonnull String id,
                                @Nonnull String cloneToId,
                                @Nullable RecipeCourse.Course course,
                                boolean addCourse) {
        this.id = id;
        this.cloneToId = cloneToId;
        this.course = course;
        this.addCourse = addCourse;
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
                id.equals(request.id) &&
                cloneToId.equals(request.cloneToId) &&
                course == request.course;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, cloneToId, course, addCourse);
    }

    @Override
    public String toString() {
        return "RecipeCourseRequest{" +
                "id='" + id + '\'' +
                ", cloneToId='" + cloneToId + '\'' +
                ", course=" + course +
                ", addCourse=" + addCourse +
                '}';
    }

    public static class Builder {
        private String id;
        private String cloneToId;
        private RecipeCourse.Course course;
        private boolean isAddCourse;

        public static Builder getDefault() {
            return new Builder().
                    setId("").
                    setCloneToId(DO_NOT_CLONE).
                    setCourse(null).
                    setAddCourse(false);
        }

        public Builder setId(String id) {
            this.id = id;
            return this;
        }

        public Builder setCloneToId(String cloneToId) {
            this.cloneToId = cloneToId;
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
                    id,
                    cloneToId,
                    course,
                    isAddCourse
            );
        }
    }
}
