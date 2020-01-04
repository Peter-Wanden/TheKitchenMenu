package com.example.peter.thekitchenmenu.domain.usecase.recipecourse;

import com.example.peter.thekitchenmenu.domain.UseCaseInteractor;

import java.util.HashMap;
import java.util.Objects;

import javax.annotation.Nonnull;

public final class RecipeCourseResponse implements UseCaseInteractor.Response {
    @Nonnull
    private final HashMap<RecipeCourse.Course, RecipeCourseModel> courseList;
    private final boolean isChanged;
    private final boolean isValid;

    public RecipeCourseResponse(@Nonnull HashMap<RecipeCourse.Course, RecipeCourseModel> courseList,
                                boolean isChanged,
                                boolean isValid) {
        this.courseList = courseList;
        this.isChanged = isChanged;
        this.isValid = isValid;
    }

    @Nonnull
    public HashMap<RecipeCourse.Course, RecipeCourseModel> getCourseList() {
        return courseList;
    }

    public boolean isChanged() {
        return isChanged;
    }

    public boolean isValid() {
        return isValid;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RecipeCourseResponse response = (RecipeCourseResponse) o;
        return isChanged == response.isChanged &&
                isValid == response.isValid &&
                courseList.equals(response.courseList);
    }

    @Override
    public int hashCode() {
        return Objects.hash(courseList, isChanged, isValid);
    }

    @Nonnull
    @Override
    public String toString() {
        return "RecipeCourseResponse{" +
                "courseList=" + courseList +
                ", isChanged=" + isChanged +
                ", isModelValid=" + isValid +
                '}';
    }
}
