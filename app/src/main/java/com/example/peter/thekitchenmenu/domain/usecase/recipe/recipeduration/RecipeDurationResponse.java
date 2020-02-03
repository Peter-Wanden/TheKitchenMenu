package com.example.peter.thekitchenmenu.domain.usecase.recipe.recipeduration;

import com.example.peter.thekitchenmenu.domain.usecase.FailReasons;
import com.example.peter.thekitchenmenu.domain.usecase.UseCaseCommand;

import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

import javax.annotation.Nonnull;

import static com.example.peter.thekitchenmenu.domain.usecase.recipe.recipestate.RecipeStateCalculator.*;

public final class RecipeDurationResponse implements UseCaseCommand.Response {
    @Nonnull
    private final ComponentState state;
    @Nonnull
    private final List<FailReasons> failReasons;
    @Nonnull
    private final Model model;

    private RecipeDurationResponse(@Nonnull ComponentState state,
                                  @Nonnull List<FailReasons> failReasons,
                                  @Nonnull Model model) {
        this.state = state;
        this.failReasons = failReasons;
        this.model = model;
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
    public Model getModel() {
        return model;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RecipeDurationResponse response = (RecipeDurationResponse) o;
        return state == response.state &&
                failReasons.equals(response.failReasons) &&
                model.equals(response.model);
    }

    @Override
    public int hashCode() {
        return Objects.hash(state, failReasons, model);
    }

    @Nonnull
    @Override
    public String toString() {
        return "RecipeDurationResponse{" +
                "state=" + state +
                ", failReasons=" + failReasons +
                ", model=" + model +
                '}';
    }

    public static class Builder {
        private ComponentState state;
        private List<FailReasons> failReasons;
        private Model model;

        public static Builder getDefault() {
            return new Builder().
                    setState(ComponentState.INVALID_UNCHANGED).
                    setFailReasons(getDefaultFailReasons()).
                    setModel(Model.Builder.
                            getDefault().
                            build());
        }

        public Builder setState(ComponentState state) {
            this.state = state;
            return this;
        }

        public Builder setFailReasons(List<FailReasons> failReasons) {
            this.failReasons = failReasons;
            return this;
        }

        public Builder setModel(Model model) {
            this.model = model;
            return this;
        }

        public RecipeDurationResponse build() {
            return new RecipeDurationResponse(
                    state,
                    failReasons,
                    model
            );
        }

        private static List<FailReasons> getDefaultFailReasons() {
            List<FailReasons> defaultFailReasons = new LinkedList<>();
            defaultFailReasons.add(RecipeDuration.FailReason.NONE);
            return defaultFailReasons;
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