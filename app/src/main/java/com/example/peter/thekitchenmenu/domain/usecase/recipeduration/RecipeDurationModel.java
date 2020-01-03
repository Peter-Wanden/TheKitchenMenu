package com.example.peter.thekitchenmenu.domain.usecase.recipeduration;

import com.example.peter.thekitchenmenu.domain.model.PersistenceModel;

import java.util.Objects;

import javax.annotation.Nonnull;

public class RecipeDurationModel implements PersistenceModel {
    @Nonnull
    private final String id;
    private final int prepHours;
    private final int prepMinutes;
    private final int totalPrepTime;
    private final int cookHours;
    private final int cookMinutes;
    private final int totalCookTime;
    private final int totalTime;
    private final long createDate;
    private final long lastUpdate;

    private RecipeDurationModel(@Nonnull String id,
                  int prepHours,
                  int prepMinutes,
                  int totalPrepTime,
                  int cookHours,
                  int cookMinutes,
                  int totalCookTime,
                  int totalTime,
                  long createDate,
                  long lastUpdate) {
        this.id = id;
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

    @Override
    @Nonnull
    public String getId() {
        return id;
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
        RecipeDurationModel model = (RecipeDurationModel) o;
        return prepHours == model.prepHours &&
                prepMinutes == model.prepMinutes &&
                totalPrepTime == model.totalPrepTime &&
                cookHours == model.cookHours &&
                cookMinutes == model.cookMinutes &&
                totalCookTime == model.totalCookTime &&
                totalTime == model.totalTime &&
                createDate == model.createDate &&
                lastUpdate == model.lastUpdate &&
                id.equals(model.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, prepHours, prepMinutes, totalPrepTime, cookHours, cookMinutes,
                totalCookTime, totalTime, createDate, lastUpdate);
    }

    @Override
    public String toString() {
        return "RecipeDurationModel{" +
                "id='" + id + '\'' +
                ", prepHours=" + prepHours +
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
        private String id;
        private int prepHours;
        private int prepMinutes;
        private int totalPrepTime;
        private int cookHours;
        private int cookMinutes;
        private int totalCookTime;
        private int totalTime;
        private long createDate;
        private long lastUpdate;

        public static Builder basedOn(@Nonnull RecipeDurationModel model) {
            return new Builder().
                    setId(model.getId()).
                    setPrepHours(model.getPrepHours()).
                    setPrepMinutes(model.getPrepMinutes()).
                    setTotalPrepTime(model.getTotalPrepTime()).
                    setCookHours(model.getCookHours()).
                    setCookMinutes(model.getCookMinutes()).
                    setTotalCookTime(model.getTotalCookTime()).
                    setTotalTime(model.getTotalTime()).
                    setCreateDate(model.getCreateDate()).
                    setLastUpdate(model.getLastUpdate());
        }

        public static Builder getDefault() {
            return new Builder().
                    setId("").
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

        public Builder setId(String id) {
            this.id = id;
            return this;
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

        public RecipeDurationModel build() {
            return new RecipeDurationModel(
                    id,
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
