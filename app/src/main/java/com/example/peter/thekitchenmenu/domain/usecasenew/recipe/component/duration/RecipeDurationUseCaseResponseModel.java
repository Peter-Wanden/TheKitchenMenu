package com.example.peter.thekitchenmenu.domain.usecasenew.recipe.component.duration;

import com.example.peter.thekitchenmenu.domain.usecasenew.model.BaseDomainModel;
import com.example.peter.thekitchenmenu.domain.usecasenew.model.BaseDomainModelBuilder;
import com.example.peter.thekitchenmenu.domain.usecasenew.model.DomainModel;

import javax.annotation.Nonnull;

public class RecipeDurationUseCaseResponseModel
        extends
        BaseDomainModel
        implements
        DomainModel.UseCaseResponseModel {

    private int prepHours;
    private int prepMinutes;
    private int totalPrepTime;
    private int cookHours;
    private int cookMinutes;
    private int totalCookTime;
    private int totalTime;

    private RecipeDurationUseCaseResponseModel() {}

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof RecipeDurationUseCaseResponseModel)) return false;

        RecipeDurationUseCaseResponseModel that = (RecipeDurationUseCaseResponseModel) o;

        if (prepHours != that.prepHours) return false;
        if (prepMinutes != that.prepMinutes) return false;
        if (totalPrepTime != that.totalPrepTime) return false;
        if (cookHours != that.cookHours) return false;
        if (cookMinutes != that.cookMinutes) return false;
        if (totalCookTime != that.totalCookTime) return false;
        return totalTime == that.totalTime;
    }

    @Override
    public int hashCode() {
        int result = prepHours;
        result = 31 * result + prepMinutes;
        result = 31 * result + totalPrepTime;
        result = 31 * result + cookHours;
        result = 31 * result + cookMinutes;
        result = 31 * result + totalCookTime;
        result = 31 * result + totalTime;
        return result;
    }

    @Nonnull
    @Override
    public String toString() {
        return "RecipeDurationUseCaseResponseModel{" +
                "prepHours=" + prepHours +
                ", prepMinutes=" + prepMinutes +
                ", totalPrepTime=" + totalPrepTime +
                ", cookHours=" + cookHours +
                ", cookMinutes=" + cookMinutes +
                ", totalCookTime=" + totalCookTime +
                ", totalTime=" + totalTime +
                '}';
    }

    public static class Builder
            extends
            BaseDomainModelBuilder<Builder, RecipeDurationUseCaseResponseModel> {

        public Builder() {
            domainModel = new RecipeDurationUseCaseResponseModel();
        }

        @Override
        public Builder getDefault() {
            domainModel.prepHours = 0;
            domainModel.prepMinutes = 0;
            domainModel.totalPrepTime = 0;
            domainModel.cookHours = 0;
            domainModel.cookMinutes = 0;
            domainModel.totalCookTime = 0;
            domainModel.totalTime = 0;
            return self();
        }

        public Builder setPrepHours(int prepHours) {
            domainModel.prepHours = prepHours;
            return self();
        }

        public Builder setPrepMinutes(int prepMinutes) {
            domainModel.prepMinutes = prepMinutes;
            return self();
        }

        public Builder setTotalPrepTime(int totalPrepTime) {
            domainModel.totalPrepTime = totalPrepTime;
            return self();
        }

        public Builder setCookHours(int cookHours) {
            domainModel.cookHours = cookHours;
            return self();
        }

        public Builder setCookMinutes(int cookMinutes) {
            domainModel.cookMinutes = cookMinutes;
            return self();
        }

        public Builder setTotalCookTime(int totalCookTime) {
            domainModel.totalCookTime = totalCookTime;
            return self();
        }

        public Builder setTotalTime(int totalTime) {
            domainModel.totalTime = totalTime;
            return self();
        }

        @Override
        public Builder basedOnModel(RecipeDurationUseCaseResponseModel model) {
            domainModel.prepHours = model.prepHours;
            domainModel.prepMinutes = model.prepMinutes;
            domainModel.totalPrepTime = model.totalPrepTime;
            domainModel.cookHours = model.cookHours;
            domainModel.cookMinutes = model.cookMinutes;
            domainModel.totalCookTime = model.totalCookTime;
            domainModel.totalTime = model.totalTime;
            return self();
        }

        @Override
        protected Builder self() {
            return this;
        }
    }
}
