package com.example.peter.thekitchenmenu.domain.usecase.recipe.recipeduration;

import com.example.peter.thekitchenmenu.domain.usecase.CommonFailReason;
import com.example.peter.thekitchenmenu.domain.usecase.FailReasons;
import com.example.peter.thekitchenmenu.domain.usecase.recipe.RecipeResponseBase;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.annotation.Nonnull;

import static com.example.peter.thekitchenmenu.domain.usecase.recipe.recipestate.RecipeStateCalculator.*;

public final class RecipeDurationResponse extends RecipeResponseBase {
    @Nonnull
    private final Metadata metadata;
    @Nonnull
    private final Model model;

    private RecipeDurationResponse(@Nonnull String id,
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
        RecipeDurationResponse response = (RecipeDurationResponse) o;
        return id.equals(response.id) &&
                metadata == response.metadata &&
                model.equals(response.model);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, metadata, model);
    }

    @Nonnull
    @Override
    public String toString() {
        return "RecipeDurationResponse{" +
                "id=" + id +
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
                    setMetadata(
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

        public Builder setMetadata(Metadata metadata) {
            this.metadata = metadata;
            return this;
        }

        public Builder setModel(Model model) {
            this.model = model;
            return this;
        }

        public RecipeDurationResponse build() {
            return new RecipeDurationResponse(
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
        private final int prepHours;
        private final int prepMinutes;
        private final int totalPrepTime;
        private final int cookHours;
        private final int cookMinutes;
        private final int totalCookTime;
        private final int totalTime;
        private final long createDate;
        private final long lastUpdate;

        private Model(int prepHours,
                      int prepMinutes,
                      int totalPrepTime,
                      int cookHours,
                      int cookMinutes,
                      int totalCookTime,
                      int totalTime,
                      long createDate,
                      long lastUpdate) {
            this.prepHours = prepHours;
            this.prepMinutes = prepMinutes;
            this.totalPrepTime = totalPrepTime;
            this.cookHours = cookHours;
            this.cookMinutes = cookMinutes;
            this.totalCookTime = totalCookTime;
            this.totalTime = totalTime;
            this.createDate = createDate;
            this.lastUpdate = lastUpdate;
        }

        public int getPrepHours() {
            return prepHours;
        }

        public int getPrepMinutes() {
            return prepMinutes;
        }

        public int getTotalPrepTime() {
            return totalPrepTime;
        }

        public int getCookHours() {
            return cookHours;
        }

        public int getCookMinutes() {
            return cookMinutes;
        }

        public int getTotalCookTime() {
            return totalCookTime;
        }

        public int getTotalTime() {
            return totalTime;
        }

        public long getCreateDate() {
            return createDate;
        }

        public long getLastUpdate() {
            return lastUpdate;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Model model = (Model) o;
            return prepHours == model.prepHours &&
                    prepMinutes == model.prepMinutes &&
                    totalPrepTime == model.totalPrepTime &&
                    cookHours == model.cookHours &&
                    cookMinutes == model.cookMinutes &&
                    totalCookTime == model.totalCookTime &&
                    totalTime == model.totalTime &&
                    createDate == model.createDate &&
                    lastUpdate == model.lastUpdate;
        }

        @Override
        public int hashCode() {
            return Objects.hash(prepHours, prepMinutes, totalPrepTime, cookHours, cookMinutes,
                    totalCookTime, totalTime, createDate, lastUpdate);
        }

        @Override
        public String toString() {
            return "Model{" +
                    "prepHours=" + prepHours +
                    ", prepMinutes=" + prepMinutes +
                    ", totalPrepTime=" + totalPrepTime +
                    ", cookHours=" + cookHours +
                    ", cookMinutes=" + cookMinutes +
                    ", totalCookTime=" + totalCookTime +
                    ", totalTime=" + totalTime +
                    ", createDate=" + createDate +
                    ", lastUpdate=" + lastUpdate +
                    '}';
        }

        public static class Builder {
            private int prepHours;
            private int prepMinutes;
            private int totalPrepTime;
            private int cookHours;
            private int cookMinutes;
            private int totalCookTime;
            private int totalTime;
            private long createDate;
            private long lastUpdate;

            public static Builder getDefault() {
                return new Builder().
                        setPrepHours(0).
                        setPrepMinutes(0).
                        setTotalPrepTime(0).
                        setCookHours(0).
                        setCookMinutes(0).
                        setTotalCookTime(0).
                        setTotalTime(0).
                        setCreateDate(0L).
                        setLastUpdate(0L);
            }

            public Builder setPrepHours(int prepHours) {
                this.prepHours = prepHours;
                return this;
            }

            public Builder setPrepMinutes(int prepMinutes) {
                this.prepMinutes = prepMinutes;
                return this;
            }

            public Builder setTotalPrepTime(int totalPrepTime) {
                this.totalPrepTime = totalPrepTime;
                return this;
            }

            public Builder setCookHours(int cookHours) {
                this.cookHours = cookHours;
                return this;
            }

            public Builder setCookMinutes(int cookMinutes) {
                this.cookMinutes = cookMinutes;
                return this;
            }

            public Builder setTotalCookTime(int totalCookTime) {
                this.totalCookTime = totalCookTime;
                return this;
            }

            public Builder setTotalTime(int totalTime) {
                this.totalTime = totalTime;
                return this;
            }

            public Builder setCreateDate(long createDate) {
                this.createDate = createDate;
                return this;
            }

            public Builder setLastUpdate(long lastUpdate) {
                this.lastUpdate = lastUpdate;
                return this;
            }

            public RecipeDurationResponse.Model build() {
                return new RecipeDurationResponse.Model(
                        prepHours,
                        prepMinutes,
                        totalPrepTime,
                        cookHours,
                        cookMinutes,
                        totalCookTime,
                        totalTime,
                        createDate,
                        lastUpdate
                );
            }
        }
    }
}