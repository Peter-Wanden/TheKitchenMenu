package com.example.peter.thekitchenmenu.domain.usecase.recipe.component.duration;

import com.example.peter.thekitchenmenu.domain.usecase.UseCaseResponse;
import com.example.peter.thekitchenmenu.domain.usecase.UseCaseMetadata;
import com.example.peter.thekitchenmenu.domain.usecase.UseCaseDomainModel;

import java.util.Objects;

import javax.annotation.Nonnull;

public final class RecipeDurationResponse extends UseCaseResponse<RecipeDurationResponse.Model> {

    @Nonnull
    @Override
    public String toString() {
        return "RecipeDurationResponse{" +
                "id=" + id +
                ", metadata=" + metadata +
                ", model=" + model +
                '}';
    }

    public static class Builder extends UseCaseResponseBuilder<
                    Builder,
                    RecipeDurationResponse,
                    Model> {

        public Builder() {
            response = new RecipeDurationResponse();
        }

        public Builder getDefault() {
            return new Builder().
                    setId("").
                    setMetadata(new UseCaseMetadata.Builder().
                            getDefault().
                            build()).
                    setModel(new Model.Builder().
                            getDefault().
                            build());
        }

        @Override
        protected Builder self() {
            return this;
        }
    }

    public static final class Model extends UseCaseDomainModel {

        private int prepHours;
        private int prepMinutes;
        private int totalPrepTime;
        private int cookHours;
        private int cookMinutes;
        private int totalCookTime;
        private int totalTime;
        private long createDate;
        private long lastUpdate;

        private Model(){}

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

        public static class Builder extends DomainModelBuilder<
                                Builder,
                                Model> {

            public Builder() {
                model = new Model();
            }

            public Builder getDefault() {
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
                model.prepHours = prepHours;
                return self();
            }

            public Builder setPrepMinutes(int prepMinutes) {
                model.prepMinutes = prepMinutes;
                return self();
            }

            public Builder setTotalPrepTime(int totalPrepTime) {
                model.totalPrepTime = totalPrepTime;
                return self();
            }

            public Builder setCookHours(int cookHours) {
                model.cookHours = cookHours;
                return self();
            }

            public Builder setCookMinutes(int cookMinutes) {
                model.cookMinutes = cookMinutes;
                return self();
            }

            public Builder setTotalCookTime(int totalCookTime) {
                model.totalCookTime = totalCookTime;
                return self();
            }

            public Builder setTotalTime(int totalTime) {
                model.totalTime = totalTime;
                return self();
            }

            public Builder setCreateDate(long createDate) {
                model.createDate = createDate;
                return self();
            }

            public Builder setLastUpdate(long lastUpdate) {
                model.lastUpdate = lastUpdate;
                return self();
            }

            @Override
            protected Builder self() {
                return this;
            }
        }
    }
}