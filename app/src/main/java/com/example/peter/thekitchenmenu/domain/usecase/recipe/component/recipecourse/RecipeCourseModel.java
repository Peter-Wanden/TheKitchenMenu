package com.example.peter.thekitchenmenu.domain.usecase.recipe.component.recipecourse;

import com.example.peter.thekitchenmenu.domain.model.PersistenceModel;

import java.util.Objects;

import javax.annotation.Nonnull;

public final class RecipeCourseModel implements PersistenceModel {
    @Nonnull
    private final String id;
    @Nonnull
    private final RecipeCourse.Course course;
    @Nonnull
    private final String recipeId;

    private final long createDate;
    private final long lasUpdate;

    public RecipeCourseModel(@Nonnull String id,
                 @Nonnull RecipeCourse.Course course,
                 @Nonnull String recipeId,
                 long createDate,
                 long lasUpdate) {
        this.id = id;
        this.course = course;
        this.recipeId = recipeId;
        this.createDate = createDate;
        this.lasUpdate = lasUpdate;
    }

    @Nonnull
    @Override
    public String getDataId() {
        return id;
    }

    @Nonnull
    public RecipeCourse.Course getCourse() {
        return course;
    }

    @Nonnull
    public String getRecipeId() {
        return recipeId;
    }

    public long getCreateDate() {
        return createDate;
    }

    public long getLasUpdate() {
        return lasUpdate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RecipeCourseModel that = (RecipeCourseModel) o;
        return createDate == that.createDate &&
                lasUpdate == that.lasUpdate &&
                id.equals(that.id) &&
                course == that.course &&
                recipeId.equals(that.recipeId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, course, recipeId, createDate, lasUpdate);
    }

    @Nonnull
    @Override
    public String toString() {
        return "RecipeCourseModel{" +
                "id='" + id + '\'' +
                ", course=" + course +
                ", recipeId='" + recipeId + '\'' +
                ", createDate=" + createDate +
                ", lasUpdate=" + lasUpdate +
                '}';
    }
}
