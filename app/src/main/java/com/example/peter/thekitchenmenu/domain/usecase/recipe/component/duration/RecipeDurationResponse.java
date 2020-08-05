package com.example.peter.thekitchenmenu.domain.usecase.recipe.component.duration;

import com.example.peter.thekitchenmenu.domain.usecase.common.usecasemessage.UseCaseMessageModelDataIdMetadata;
import com.example.peter.thekitchenmenu.domain.usecasenew.common.model.BaseDomainModel;
import com.example.peter.thekitchenmenu.domain.usecase.common.UseCaseBase;
import com.example.peter.thekitchenmenu.domain.usecasenew.common.metadata.UseCaseMetadataModel;

import java.util.Objects;

import javax.annotation.Nonnull;

public final class RecipeDurationResponse
        extends
        UseCaseMessageModelDataIdMetadata<RecipeDurationResponse.DomainModel>
        implements
        UseCaseBase.Response {

    @Nonnull
    @Override
    public String toString() {
        return "RecipeDurationResponse{" +
                "dataId='" + dataId + '\'' +
                ", domainId='" + domainId + '\'' +
                ", metadata=" + metadata +
                ", model=" + model +
                "'}'";
    }

    private RecipeDurationResponse() {}

    public static class Builder
            extends
            MessageModelDataIdMetadataBuilder<
                    Builder,
                    RecipeDurationResponse,
                    DomainModel> {

        public Builder() {
            message = new RecipeDurationResponse();
        }

        @Override
        public Builder getDefault() {
            message.dataId = "";
            message.domainId = "";
            message.metadata = new UseCaseMetadataModel.Builder().getDefault().build();
            message.model = new DomainModel.Builder().getDefault().build();
            return self();
        }

        @Override
        protected Builder self() {
            return this;
        }
    }

    public static final class DomainModel
            extends
            BaseDomainModel {

        private int prepHours;
        private int prepMinutes;
        private int totalPrepTime;
        private int cookHours;
        private int cookMinutes;
        private int totalCookTime;
        private int totalTime;

        private DomainModel(){}

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
            if (o == null || getClass() != o.getClass()) return false;
            DomainModel domainModel = (DomainModel) o;
            return prepHours == domainModel.prepHours &&
                    prepMinutes == domainModel.prepMinutes &&
                    totalPrepTime == domainModel.totalPrepTime &&
                    cookHours == domainModel.cookHours &&
                    cookMinutes == domainModel.cookMinutes &&
                    totalCookTime == domainModel.totalCookTime &&
                    totalTime == domainModel.totalTime;
        }

        @Override
        public int hashCode() {
            return Objects.hash(prepHours, prepMinutes, totalPrepTime, cookHours, cookMinutes,
                    totalCookTime, totalTime);
        }

        @Nonnull
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
                    '}';
        }

        public static class Builder
                extends
                BaseDomainModelBuilder<Builder, DomainModel> {

            public Builder() {
                super(new DomainModel());
            }

            @Override
            public Builder basedOnModel(DomainModel model) {
                return null;
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
            protected Builder self() {
                return this;
            }
        }
    }
}