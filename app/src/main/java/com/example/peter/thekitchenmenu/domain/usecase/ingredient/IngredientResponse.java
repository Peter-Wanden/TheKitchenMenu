package com.example.peter.thekitchenmenu.domain.usecase.ingredient;

import com.example.peter.thekitchenmenu.domain.entity.unitofmeasure.UnitOfMeasureConstants;
import com.example.peter.thekitchenmenu.domain.usecase.common.UseCaseBase;
import com.example.peter.thekitchenmenu.domain.usecase.common.usecasemessage.UseCaseMessageModelDataIdMetadata;
import com.example.peter.thekitchenmenu.domain.model.BaseDomainModel;
import com.example.peter.thekitchenmenu.domain.model.UseCaseMetadataModel;

import java.util.Objects;

import javax.annotation.Nonnull;

public final class IngredientResponse
        extends UseCaseMessageModelDataIdMetadata<IngredientResponse.Model>
        implements UseCaseBase.Response {

    @Nonnull
    @Override
    public String toString() {
        return "IngredientResponse{" +
                "dataId='" + dataId + '\'' +
                ", domainId='" + domainId + '\'' +
                ", metadata=" + metadata +
                ", model=" + model +
                '}';
    }

    private IngredientResponse() {}

    public static class Builder
            extends MessageModelDataIdMetadataBuilder<Builder, IngredientResponse, Model> {

        public Builder() {
            message = new IngredientResponse();
        }

        public Builder getDefault() {
            message.dataId = "";
            message.domainId = "";
            message.metadata = new UseCaseMetadataModel.Builder().getDefault().build();
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

        private Model() {}

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
                return new Builder().
                        setName("").
                        setDescription("").
                        setConversionFactor(0.);
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
