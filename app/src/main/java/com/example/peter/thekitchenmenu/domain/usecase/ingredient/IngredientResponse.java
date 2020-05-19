package com.example.peter.thekitchenmenu.domain.usecase.ingredient;

import com.example.peter.thekitchenmenu.domain.entity.unitofmeasure.UnitOfMeasureConstants;
import com.example.peter.thekitchenmenu.domain.usecase.UseCase;
import com.example.peter.thekitchenmenu.domain.usecase.BaseDomainMessageModelMetadata;
import com.example.peter.thekitchenmenu.domain.usecase.BaseDomainModel;
import com.example.peter.thekitchenmenu.domain.usecase.UseCaseMetadata;

import java.util.Objects;

import javax.annotation.Nonnull;

public final class IngredientResponse
        extends BaseDomainMessageModelMetadata<IngredientResponse.Model>
        implements UseCase.Response {

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
            extends UseCaseMessageMetadataBuilder<Builder, IngredientResponse, Model> {

        public Builder() {
            message = new IngredientResponse();
        }

        public Builder getDefault() {
            message.dataId = "";
            message.domainId = "";
            message.metadata = new UseCaseMetadata.Builder().getDefault().build();
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
                model = new Model();
            }

            public Builder getDefault() {
                model.name = "";
                model.description = "";
                model.conversionFactor = UnitOfMeasureConstants.DEFAULT_CONVERSION_FACTOR;
                return new Builder().
                        setName("").
                        setDescription("").
                        setConversionFactor(0.);
            }

            public Builder setName(String name) {
                model.name = name;
                return self();
            }

            public Builder setDescription(String description) {
                model.description = description;
                return self();
            }

            public Builder setConversionFactor(double conversionFactor) {
                model.conversionFactor = conversionFactor;
                return self();
            }

            @Override
            protected Builder self() {
                return this;
            }
        }
    }
}
