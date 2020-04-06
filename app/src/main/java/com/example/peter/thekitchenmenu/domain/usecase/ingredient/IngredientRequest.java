package com.example.peter.thekitchenmenu.domain.usecase.ingredient;

import com.example.peter.thekitchenmenu.domain.entity.unitofmeasure.UnitOfMeasureConstants;
import com.example.peter.thekitchenmenu.domain.usecase.UseCase;
import com.example.peter.thekitchenmenu.domain.usecase.UseCaseDomainModel;
import com.example.peter.thekitchenmenu.domain.usecase.UseCaseDomainMessageBaseModel;

import java.util.Objects;

import javax.annotation.Nonnull;

public final class IngredientRequest
        extends UseCaseDomainMessageBaseModel<IngredientRequest.Model>
        implements UseCase.Request {

    private IngredientRequest() {}

    public static class Builder
            extends UseCaseMessageBuilderModel<Builder, IngredientRequest, Model> {

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

    public static final class Model extends UseCaseDomainModel {
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
                model = new Model();
            }

            public Builder getDefault() {
                model.name = "";
                model.description = "";
                model.conversionFactor = UnitOfMeasureConstants.DEFAULT_CONVERSION_FACTOR;
                return self();
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
