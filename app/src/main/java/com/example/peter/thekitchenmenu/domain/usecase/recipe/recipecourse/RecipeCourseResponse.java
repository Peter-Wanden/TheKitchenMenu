package com.example.peter.thekitchenmenu.domain.usecase.recipe.recipecourse;

import com.example.peter.thekitchenmenu.domain.usecase.CommonFailReason;
import com.example.peter.thekitchenmenu.domain.usecase.FailReasons;
import com.example.peter.thekitchenmenu.domain.usecase.recipe.RecipeResponseAbstract;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

import javax.annotation.Nonnull;

import static com.example.peter.thekitchenmenu.domain.usecase.recipe.recipestate.RecipeStateCalculator.*;

public final class RecipeCourseResponse extends RecipeResponseAbstract {
    @Nonnull
    private final Metadata metadata;
    @Nonnull
    private final Model model;

    public RecipeCourseResponse(@Nonnull String id,
                                @Nonnull Metadata metadata,
                                @Nonnull Model model) {
        this.id = id;
        this.metadata = metadata;
        this.model = model;
    }

    @Nonnull
    public Metadata getMetadata() {
        return metadata;
    }

    @Nonnull
    public Model getModel() {
        return model;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RecipeCourseResponse that = (RecipeCourseResponse) o;
        return id.equals(that.id) &&
                metadata.equals(that.metadata) &&
                model.equals(that.model);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, metadata, model);
    }

    @Nonnull
    @Override
    public String toString() {
        return "RecipeCourseResponse{" +
                "id='" + id + '\'' +
                ", metadata=" + metadata +
                ", model=" + model +
                '}';
    }

    public static class Builder {
        private String id;
        private Metadata metadata;
        private Model model;

        public static Builder getDefault() {
            return new Builder().
                    setId("").
                    setMetaData(
                            Metadata.Builder.
                                    getDefault().
                                    build()).
                    setModel(
                            Model.Builder.
                                    getDefault().
                                    build());
        }

        public Builder setId(String id) {
            this.id = id;
            return this;
        }

        public Builder setMetaData(Metadata metadata) {
            this.metadata = metadata;
            return this;
        }

        public Builder setModel(Model model) {
            this.model = model;
            return this;
        }

        public RecipeCourseResponse build() {
            return new RecipeCourseResponse(
                    id,
                    metadata,
                    model
            );
        }
    }

    public static final class Metadata {
        @Nonnull
        private final ComponentState state;
        @Nonnull
        private final List<FailReasons> failReasons;
        long createDate;
        long lasUpdate;

        private Metadata(@Nonnull ComponentState state,
                         @Nonnull List<FailReasons> failReasons,
                         long createDate,
                         long lasUpdate) {
            this.state = state;
            this.failReasons = failReasons;
            this.createDate = createDate;
            this.lasUpdate = lasUpdate;
        }

        @Nonnull
        public ComponentState getState() {
            return state;
        }

        @Nonnull
        public List<FailReasons> getFailReasons() {
            return failReasons;
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
            Metadata metaData = (Metadata) o;
            return state == metaData.state &&
                    failReasons.equals(metaData.failReasons) &&
                    createDate == metaData.createDate &&
                    lasUpdate == metaData.lasUpdate;
        }

        @Override
        public int hashCode() {
            return Objects.hash(state, failReasons, createDate, lasUpdate);
        }

        @Override
        public String toString() {
            return "MetaData{" +
                    "state=" + state +
                    ", failReasons=" + failReasons +
                    ", createDate=" + createDate +
                    ", lasUpdate=" + lasUpdate +
                    '}';
        }

        public static class Builder {
            private ComponentState state;
            private List<FailReasons> failReasons;
            long createDate;
            long lasUpdate;

            public static Builder getDefault() {
                return new Builder().
                        setState(ComponentState.INVALID_UNCHANGED).
                        setFailReasons(getDefaultFailReasons()).
                        setCreateDate(0L).
                        setLasUpdate(0L);
            }

            public Builder setState(ComponentState state) {
                this.state = state;
                return this;
            }

            public Builder setFailReasons(List<FailReasons> failReasons) {
                this.failReasons = failReasons;
                return this;
            }

            public Builder setCreateDate(long createDate) {
                this.createDate = createDate;
                return this;
            }

            public Builder setLasUpdate(long lasUpdate) {
                this.lasUpdate = lasUpdate;
                return this;
            }

            private static List<FailReasons> getDefaultFailReasons() {
                List<FailReasons> failReasons = new ArrayList<>();
                failReasons.add(CommonFailReason.DATA_UNAVAILABLE);
                return failReasons;
            }

            public Metadata build() {
                return new Metadata(
                        state,
                        failReasons,
                        createDate,
                        lasUpdate
                );
            }
        }
    }

    public static final class Model {
        @Nonnull
        private final HashMap<RecipeCourse.Course, RecipeCourseModel> courseList;

        private Model(@Nonnull HashMap<RecipeCourse.Course, RecipeCourseModel> courseList) {
            this.courseList = courseList;
        }

        @Nonnull
        public HashMap<RecipeCourse.Course, RecipeCourseModel> getCourseList() {
            return courseList;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Model model = (Model) o;
            return courseList.equals(model.courseList);
        }

        @Override
        public int hashCode() {
            return Objects.hash(courseList);
        }

        @Override
        public String toString() {
            return "Model{" +
                    "courseList=" + courseList +
                    '}';
        }

        public static class Builder {
            private HashMap<RecipeCourse.Course, RecipeCourseModel> courseList;

            public static Builder getDefault() {
                return new Builder().
                        setCourseList(getDefaultCourseList());
            }

            public Builder setCourseList(HashMap<RecipeCourse.Course, RecipeCourseModel> courseList) {
                this.courseList = courseList;
                return this;
            }

            public Model build() {
                return new Model(courseList);
            }

            private static HashMap<RecipeCourse.Course, RecipeCourseModel> getDefaultCourseList() {
                return new HashMap<>();
            }
        }
    }
}
