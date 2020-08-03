package com.example.peter.thekitchenmenu.domain.usecase.recipe.component.duration;


import com.example.peter.thekitchenmenu.domain.usecasenew.model.BaseDomainModelBuilder;
import com.example.peter.thekitchenmenu.domain.usecase.common.UseCaseBase;
import com.example.peter.thekitchenmenu.domain.usecase.common.usecasemessage.UseCaseMessageModelDataId;
import com.example.peter.thekitchenmenu.domain.usecasenew.model.BaseDomainModel;

import java.util.Objects;

import javax.annotation.Nonnull;

public final class RecipeDurationRequest
        extends
        UseCaseMessageModelDataId<RecipeDurationRequest.DomainModel>
        implements
        UseCaseBase.Request {

    @Nonnull
    @Override
    public String toString() {
        return "RecipeDurationRequest{" +
                "dataId='" + dataId + '\'' +
                ", domainId='" + domainId + '\'' +
                ", model=" + model +
                '}';
    }

    private RecipeDurationRequest() {
    }

    public static class Builder
            extends
            UseCaseMessageModelDataIdBuilder<
                    Builder,
                    RecipeDurationRequest,
                    DomainModel> {

        public Builder() {
            message = new RecipeDurationRequest();
        }

        @Override
        public Builder getDefault() {
            message.dataId = "";
            message.domainId = "";
            message.model = new DomainModel.Builder().getDefault().build();
            return self();
        }

        public Builder basedOnResponse(RecipeDurationResponse response) {
            message.dataId = response.getDataId();
            message.domainId = response.getDomainId();
            message.model = new DomainModel.Builder().
                    basedOnResponseModel(response.getDomainModel()).
                    build();
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
        private int cookHours;
        private int cookMinutes;

        private DomainModel() {
        }

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
            if (o == null || getClass() != o.getClass()) return false;
            DomainModel domainModel = (DomainModel) o;
            return prepHours == domainModel.prepHours &&
                    prepMinutes == domainModel.prepMinutes &&
                    cookHours == domainModel.cookHours &&
                    cookMinutes == domainModel.cookMinutes;
        }

        @Override
        public int hashCode() {
            return Objects.hash(prepHours, prepMinutes, cookHours, cookMinutes);
        }

        @Nonnull
        @Override
        public String toString() {
            return "Model{" +
                    "prepHours=" + prepHours +
                    ", prepMinutes=" + prepMinutes +
                    ", cookHours=" + cookHours +
                    ", cookMinutes=" + cookMinutes +
                    '}';
        }

        public static class Builder
                extends
                BaseDomainModelBuilder<Builder, DomainModel> {

            public Builder() {
                domainModel = new DomainModel();
            }

            @Override
            public Builder basedOnRequestModel(DomainModel model) {
                return null;
            }

            @Override
            public Builder getDefault() {
                domainModel.prepHours = 0;
                domainModel.prepMinutes = 0;
                domainModel.cookHours = 0;
                domainModel.prepMinutes = 0;
                return self();
            }

            public Builder basedOnResponseModel(RecipeDurationResponse.DomainModel m) {
                domainModel.prepHours = m.getPrepHours();
                domainModel.prepMinutes = m.getPrepMinutes();
                domainModel.cookHours = m.getCookHours();
                domainModel.prepMinutes = m.getCookMinutes();
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
}