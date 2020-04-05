package com.example.peter.thekitchenmenu.domain.usecase.recipe.component.course;

import com.example.peter.thekitchenmenu.domain.usecase.recipe.RecipePersistenceModel;

import java.util.Objects;

import javax.annotation.Nonnull;

public final class RecipeCoursePersistenceModel
        extends RecipePersistenceModel {

    private RecipeCourse.Course course;
    private long createDate;
    private long lasUpdate;

    private RecipeCoursePersistenceModel(){}

    @Nonnull
    public RecipeCourse.Course getCourse() {
        return course;
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
        RecipeCoursePersistenceModel that = (RecipeCoursePersistenceModel) o;
        return dataId.equals(that.dataId) &&
                domainId.equals(that.domainId) &&
                course == that.course &&
                createDate == that.createDate &&
                lasUpdate == that.lasUpdate;
    }

    @Override
    public int hashCode() {
        return Objects.hash(dataId, course, domainId, createDate, lasUpdate);
    }

    @Nonnull
    @Override
    public String toString() {
        return "RecipeCourseModel{" +
                "dataId='" + dataId + '\'' +
                ", recipeId='" + domainId + '\'' +
                ", course=" + course +
                ", createDate=" + createDate +
                ", lasUpdate=" + lasUpdate +
                '}';
    }

    public static class Builder extends DomainModelBuilder<
            Builder,
            RecipeCoursePersistenceModel> {

        public Builder() {
            model = new RecipeCoursePersistenceModel();
        }

        @Override
        public Builder getDefault() {
            model.dataId = "";
            model.domainId = "";
            model.course = RecipeCourse.Course.COURSE_ZERO;
            model.createDate = 0L;
            model.lasUpdate = 0L;
            return self();
        }

        public Builder setDataId(String dataId) {
            model.dataId = dataId;
            return self();
        }

        public Builder setRecipeId(String recipeId) {
            model.domainId = recipeId;
            return self();
        }

        public Builder setCourse(RecipeCourse.Course course) {
            model.course = course;
            return self();
        }

        public Builder setCreateDate(long createDate) {
            model.createDate = createDate;
            return self();
        }

        public Builder setLastUpdate(long lastUpdate) {
            model.lasUpdate = lastUpdate;
            return self();
        }

        @Override
        protected Builder self() {
            return super.self();
        }

        @Override
        public RecipeCoursePersistenceModel build() {
            return super.build();
        }
    }
}
