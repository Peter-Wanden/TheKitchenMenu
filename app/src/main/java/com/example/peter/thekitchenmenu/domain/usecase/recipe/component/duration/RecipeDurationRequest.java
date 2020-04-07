package com.example.peter.thekitchenmenu.domain.usecase.recipe.component.duration;


import com.example.peter.thekitchenmenu.domain.usecase.UseCase;
import com.example.peter.thekitchenmenu.domain.usecase.BaseDomainMessageModel;
import com.example.peter.thekitchenmenu.domain.usecase.BaseDomainModel;

import java.util.Objects;

import javax.annotation.Nonnull;

public final class RecipeDurationRequest
        extends BaseDomainMessageModel<RecipeDurationRequest.Model>
        implements UseCase.Request {

    private RecipeDurationRequest() {}

    public static class Builder
            extends UseCaseMessageBuilderModel<Builder, RecipeDurationRequest, Model> {

        public Builder() {
            message = new RecipeDurationRequest();
        }

        public Builder getDefault() {
            message.dataId = "";
            message.domainId = "";
            message.model = new Model.Builder().getDefault().build();
            return self();
        }

        public Builder basedOnResponse(RecipeDurationResponse response) {
            message.dataId = response.getDataId();
            message.domainId = response.getDomainId();
            message.model = new Model.Builder().
                    basedOnResponseModel(response.getModel()).
                    build();
            return self();
        }

        @Override
        protected Builder self() {
            return this;
        }
    }

    public static final class Model extends BaseDomainModel {
        private int prepHours;
        private int prepMinutes;
        private int cookHours;
        private int cookMinutes;

        private Model() {}

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
            Model model = (Model) o;
            return prepHours == model.prepHours &&
                    prepMinutes == model.prepMinutes &&
                    cookHours == model.cookHours &&
                    cookMinutes == model.cookMinutes;
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

        public static class Builder extends DomainModelBuilder<Builder, Model> {

            public Builder() {
                model = new Model();
            }

            public Builder getDefault() {
                model.prepHours = 0;
                model.prepMinutes = 0;
                model.cookHours = 0;
                model.prepMinutes = 0;
                return self();
            }

            public Builder basedOnResponseModel(RecipeDurationResponse.Model m) {
                model.prepHours = m.getPrepHours();
                model.prepMinutes = m.getPrepMinutes();
                model.cookHours = m.getCookHours();
                model.prepMinutes = m.getCookMinutes();
                return self();
            }

            public Builder setPrepHours(int prepHours) {
                model.prepHours = prepHours;
                return self();
            }

            public Builder setPrepMinutes(int prepMinutes) {
                model.prepMinutes = prepMinutes;
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

            @Override
            protected Builder self() {
                return this;
            }
        }
    }
}