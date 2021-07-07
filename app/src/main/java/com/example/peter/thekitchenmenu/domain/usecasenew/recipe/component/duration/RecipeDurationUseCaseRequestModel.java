package com.example.peter.thekitchenmenu.domain.usecasenew.recipe.component.duration;

import com.example.peter.thekitchenmenu.domain.usecasenew.common.model.BaseDomainModel;
import com.example.peter.thekitchenmenu.domain.usecasenew.common.model.DomainModel;

import javax.annotation.Nonnull;

public final class RecipeDurationUseCaseRequestModel
        extends
        BaseDomainModel
        implements
        DomainModel.UseCaseRequestModel {

    private int prepHours;
    private int prepMinutes;
    private int cookHours;
    private int cookMinutes;

    private RecipeDurationUseCaseRequestModel() {}

    public int getPrepHours() {
        return prepHours;
    }

    public int getPrepMinutes() {
        return prepMinutes;
    }

    public int getCookHours() {
        return cookHours;
    }

    public int getCookMinutes() {
        return cookMinutes;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof RecipeDurationUseCaseRequestModel)) return false;

        RecipeDurationUseCaseRequestModel that = (RecipeDurationUseCaseRequestModel) o;

        if (prepHours != that.prepHours) return false;
        if (prepMinutes != that.prepMinutes) return false;
        if (cookHours != that.cookHours) return false;
        return cookMinutes == that.cookMinutes;
    }

    @Override
    public int hashCode() {
        int result = prepHours;
        result = 31 * result + prepMinutes;
        result = 31 * result + cookHours;
        result = 31 * result + cookMinutes;
        return result;
    }

    @Nonnull
    @Override
    public String toString() {
        return "RecipeDurationUseCaseRequestModel{" +
                "prepHours=" + prepHours +
                ", prepMinutes=" + prepMinutes +
                ", cookHours=" + cookHours +
                ", cookMinutes=" + cookMinutes +
                '}';
    }

    public static class Builder
            extends
            BaseDomainModelBuilder<Builder, RecipeDurationUseCaseRequestModel> {

        public Builder() {
            super(new RecipeDurationUseCaseRequestModel());
        }

        @Override
        public Builder getDefault() {
            domainModel.prepHours = 0;
            domainModel.prepMinutes = 0;
            domainModel.cookHours = 0;
            domainModel.cookMinutes = 0;
            return self();
        }

        @Override
        public Builder basedOnModel(RecipeDurationUseCaseRequestModel requestModel) {
            domainModel.prepHours = requestModel.prepHours;
            domainModel.prepMinutes = requestModel.prepMinutes;
            domainModel.cookHours = requestModel.cookHours;
            domainModel.cookMinutes = requestModel.cookMinutes;
            return self();
        }

        public Builder basedOnResponseModel(RecipeDurationUseCaseResponseModel responseModel) {
            domainModel.prepHours = responseModel.getPrepHours();
            domainModel.prepMinutes = responseModel.getPrepMinutes();
            domainModel.cookHours = responseModel.getCookHours();
            domainModel.cookMinutes = responseModel.getCookMinutes();
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

        public Builder setCookHours(int cookHours) {
            domainModel.cookHours = cookHours;
            return self();
        }

        public Builder setCookMinutes(int cookMinutes) {
            domainModel.cookMinutes = cookMinutes;
            return self();
        }

        @Override
        protected Builder self() {
            return this;
        }
    }
}
