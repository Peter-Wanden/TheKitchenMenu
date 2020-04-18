package com.example.peter.thekitchenmenu.domain.usecase.recipe.component.course;

import com.example.peter.thekitchenmenu.domain.usecase.BasePersistence;

import java.util.Objects;

import javax.annotation.Nonnull;

public final class RecipeCoursePersistenceModel
        extends BasePersistence {

    private RecipeCourse.Course course;
    private boolean isActive;
    private long createDate;
    private long lasUpdate;

    private RecipeCoursePersistenceModel(){}

    @Nonnull
    public RecipeCourse.Course getCourse() {
        return course;
    }

    public boolean isActive() {
        return isActive;
    }

    public long getCreateDate() {
        return createDate;
    }

    public long getLastUpdate() {
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
                isActive == that.isActive &&
                createDate == that.createDate &&
                lasUpdate == that.lasUpdate;
    }

    @Override
    public int hashCode() {
        return Objects.hash(dataId, domainId, course, isActive, createDate, lasUpdate);
    }

    @Nonnull
    @Override
    public String toString() {
        return "RecipeCourseModel{" +
                "dataId='" + dataId + '\'' +
                ", domainId='" + domainId + '\'' +
                ", course=" + course +
                ", isInUse=" + isActive +
                ", createDate=" + createDate +
                ", lasUpdate=" + lasUpdate +
                '}';
    }

    public static class Builder
            extends DomainModelBuilder<Builder, RecipeCoursePersistenceModel> {

        public Builder() {
            model = new RecipeCoursePersistenceModel();
        }

        @Override
        public Builder getDefault() {
            model.dataId = "";
            model.domainId = "";
            model.course = RecipeCourse.Course.COURSE_ZERO;
            model.isActive = false;
            model.createDate = 0L;
            model.lasUpdate = 0L;
            return self();
        }

        public Builder basedOnModel (RecipeCoursePersistenceModel m) {
            model.dataId = m.getDataId();
            model.domainId = m.getDomainId();
            model.course = m.getCourse();
            model.isActive = m.isActive();
            model.createDate = m.getCreateDate();
            model.lasUpdate = m.getLastUpdate();
            return self();
        }

        public Builder setDataId(String dataId) {
            model.dataId = dataId;
            return self();
        }

        public Builder setDomainId(String recipeId) {
            model.domainId = recipeId;
            return self();
        }

        public Builder setCourse(RecipeCourse.Course course) {
            model.course = course;
            return self();
        }

        public Builder setIsActive(boolean isActive) {
            model.isActive = isActive;
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
    }
}
