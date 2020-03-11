package com.example.peter.thekitchenmenu.domain.usecase.recipe.recipe;

import com.example.peter.thekitchenmenu.domain.usecase.CommonFailReason;
import com.example.peter.thekitchenmenu.domain.usecase.FailReasons;
import com.example.peter.thekitchenmenu.domain.usecase.recipe.RecipeResponseBase;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.annotation.Nonnull;

import static com.example.peter.thekitchenmenu.domain.usecase.recipe.recipestate.RecipeStateCalculator.*;

public final class RecipeResponse extends RecipeResponseBase {

    @Nonnull
    private Metadata metadata;
    @Nonnull
    private Model model;

    private RecipeResponse(@Nonnull String id,
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
        RecipeResponse that = (RecipeResponse) o;
        return id.equals(that.id) &&
                metadata.equals(that.metadata) &&
                model.equals(that.model);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, metadata, model);
    }

    public static class Builder {
        private String id;
        private Metadata metadata;
        private Model model;

        public static Builder getDefault() {
            return new Builder().
                    setId("").
                    setMetadata(
                            Metadata.Builder.
                                    getDefault().
                                    build()).
                    setModel(
                            new Model.Builder().
                                    setParentId("").
                                    build());
        }

        public Builder setId(String id) {
            this.id = id;
            return this;
        }

        public Builder setMetadata(Metadata metadata) {
            this.metadata = metadata;
            return this;
        }

        public Builder setModel(Model model) {
            this.model = model;
            return this;
        }

        public RecipeResponse build() {
            return new RecipeResponse(
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
            Metadata metadata = (Metadata) o;
            return createDate == metadata.createDate &&
                    lasUpdate == metadata.lasUpdate &&
                    state == metadata.state &&
                    failReasons.equals(metadata.failReasons);
        }

        @Override
        public int hashCode() {
            return Objects.hash(state, failReasons, createDate, lasUpdate);
        }

        @Nonnull
        @Override
        public String toString() {
            return "Metadata{" +
                    "state=" + state +
                    ", failReasons=" + failReasons +
                    ", createDate=" + createDate +
                    ", lasUpdate=" + lasUpdate +
                    '}';
        }

        public static class Builder {
            private ComponentState state;
            private List<FailReasons> failReasons;
            private long createDate;
            private long lasUpdate;

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
        private String parentId;

        public Model(@Nonnull String parentId) {
            this.parentId = parentId;
        }

        @Nonnull
        public String getParentId() {
            return parentId;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Model model = (Model) o;
            return parentId.equals(model.parentId);
        }

        @Override
        public int hashCode() {
            return Objects.hash(parentId);
        }

        @Override
        public String toString() {
            return "Model{" +
                    "parentId='" + parentId + '\'' +
                    '}';
        }

        public static class Builder {
            String parentId;

            public Builder setParentId(String parentId) {
                this.parentId = parentId;
                return this;
            }

            public Model build() {
                return new Model(
                        parentId
                );
            }
        }
    }
}
