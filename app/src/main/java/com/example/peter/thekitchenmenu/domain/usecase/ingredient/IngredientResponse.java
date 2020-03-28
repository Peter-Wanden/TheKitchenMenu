package com.example.peter.thekitchenmenu.domain.usecase.ingredient;

import com.example.peter.thekitchenmenu.domain.usecase.UseCaseDomainModel;
import com.example.peter.thekitchenmenu.domain.usecase.UseCaseResponse;

import java.util.Objects;

import javax.annotation.Nonnull;

public final class IngredientResponse extends UseCaseResponse<IngredientResponse.Model> {

    private String ingredientId;

    public String getIngredientId() {
        return ingredientId;
    }

    @Nonnull
    @Override
    public String toString() {
        return "IngredientResponse{" +
                "id='" + id + '\'' +
                ", ingredientId='" + ingredientId + '\'' +
                ", metadata=" + metadata +
                ", model=" + model +
                '}';
    }

    public static class Builder
            extends UseCaseResponse.UseCaseResponseBuilder<
            Builder,
            IngredientResponse,
            Model> {

        public Builder() {
            response = new IngredientResponse();
        }

        public Builder getDefault() {
            return new Builder().
                    setId("").
                    setModel(new Model.Builder().
                            getDefault().
                            build());
        }

        public Builder setIngredientId(String ingredientId) {
            response.ingredientId = ingredientId;
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
