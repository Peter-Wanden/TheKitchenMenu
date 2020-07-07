package com.example.peter.thekitchenmenu.domain.usecase.ingredient;

import com.example.peter.thekitchenmenu.domain.entity.unitofmeasure.UnitOfMeasureConstants;
import com.example.peter.thekitchenmenu.domain.model.DomainModelBuilder;
import com.example.peter.thekitchenmenu.domain.usecase.common.UseCaseBase;
import com.example.peter.thekitchenmenu.domain.model.BaseDomainModel;
import com.example.peter.thekitchenmenu.domain.usecase.common.usecasemessage.UseCaseMessageModelDataId;

import java.util.Objects;

import javax.annotation.Nonnull;

public final class IngredientRequest
        extends UseCaseMessageModelDataId<IngredientRequest.Model>
        implements UseCaseBase.Request {

    @Nonnull
    @Override
    public String toString() {
        return "IngredientRequest{" +
                "dataId='" + dataId + '\'' +
                ", domainId='" + domainId + '\'' +
                ", model=" + model +
                '}';
    }

    private IngredientRequest() {}

    public static class Builder
            extends UseCaseMessageModelDataIdBuilder<Builder, IngredientRequest, Model> {

        public Builder() {
            message = new IngredientRequest();
        }

        public Builder getDefault() {
            message.dataId = "";
            message.domainId = "";
            message.model = new Model.Builder().getDefault().build();
            return self();
        }

        @Override
        protected Builder self() {
            return this;
        }
    }

    public static final class Model extends BaseDomainModel {
        private String name;
        private String description;
        private double conversionFactor;

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Model model = (Model) o;
            return Double.compare(model.conversionFactor, conversionFactor) == 0 &&
                    Objects.equals(name, model.name) &&
                    Objects.equals(description, model.description);
        }

        @Override
        public int hashCode() {
            return Objects.hash(name, description, conversionFactor);
        }

        @Nonnull
        @Override
        public String toString() {
            return "Model{" +
                    "name='" + name + '\'' +
                    ", description='" + description + '\'' +
                    ", conversionFactor=" + conversionFactor +
                    '}';
        }

        public String getName() {
            return name;
        }

        public String getDescription() {
            return description;
        }

        public double getConversionFactor() {
            return conversionFactor;
        }

        public static class Builder extends DomainModelBuilder<Builder, Model> {

            public Builder() {
                domainModel = new Model();
            }

            public Builder getDefault() {
                domainModel.name = "";
                domainModel.description = "";
                domainModel.conversionFactor = UnitOfMeasureConstants.DEFAULT_CONVERSION_FACTOR;
                return self();
            }

            public Builder setName(String name) {
                domainModel.name = name;
                return self();
            }

            public Builder setDescription(String description) {
                domainModel.description = description;
                return self();
            }

            public Builder setConversionFactor(double conversionFactor) {
                domainModel.conversionFactor = conversionFactor;
                return self();
            }

            @Override
            protected Builder self() {
                return this;
            }
        }
    }
}
