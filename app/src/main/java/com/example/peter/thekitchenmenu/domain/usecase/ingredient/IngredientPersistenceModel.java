package com.example.peter.thekitchenmenu.domain.usecase.ingredient;

import com.example.peter.thekitchenmenu.app.Constants;
import com.example.peter.thekitchenmenu.domain.entity.unitofmeasure.UnitOfMeasureConstants;
import com.example.peter.thekitchenmenu.domain.model.PersistenceModel;

import java.util.Objects;

import javax.annotation.Nonnull;

import static com.example.peter.thekitchenmenu.domain.usecase.ingredient.Ingredient.CREATE_NEW_INGREDIENT;

public final class IngredientPersistenceModel implements PersistenceModel {

    @Nonnull
    private final String id;
    @Nonnull
    private final String ingredientId;
    @Nonnull
    private final String name;
    @Nonnull
    private final String description;
    private final double conversionFactor;
    @Nonnull
    private final String userId;
    private final long createDate;
    private final long lastUpdate;

    private IngredientPersistenceModel(@Nonnull String id,
                                       @Nonnull String ingredientId,
                                       @Nonnull String name,
                                       @Nonnull String description,
                                       double conversionFactor,
                                       @Nonnull String userId,
                                       long createDate,
                                       long lastUpdate) {
        this.id = id;
        this.ingredientId = ingredientId;
        this.name = name;
        this.description = description;
        this.conversionFactor = conversionFactor;
        this.userId = userId;
        this.createDate = createDate;
        this.lastUpdate = lastUpdate;
    }

    @Override
    public String getDataId() {
        return null;
    }

    @Nonnull
    public String getIngredientId() {
        return ingredientId;
    }

    @Nonnull
    public String getName() {
        return name;
    }

    @Nonnull
    public String getDescription() {
        return description;
    }

    public double getConversionFactor() {
        return conversionFactor;
    }

    @Nonnull
    public String getUserId() {
        return userId;
    }

    public long getCreateDate() {
        return createDate;
    }

    public long getLastUpdate() {
        return lastUpdate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        IngredientPersistenceModel that = (IngredientPersistenceModel) o;
        return id.equals(that.id) &&
                ingredientId.equals(that.ingredientId) &&
                name.equals(that.name) &&
                description.equals(that.description) &&
                Double.compare(that.conversionFactor, conversionFactor) == 0 &&
                userId.equals(that.userId) &&
                createDate == that.createDate &&
                lastUpdate == that.lastUpdate;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, ingredientId, name, description, conversionFactor, userId,
                createDate, lastUpdate);
    }

    @Nonnull
    @Override
    public String toString() {
        return "IngredientModel{" +
                "id='" + id + '\'' +
                ", ingredientId='" + ingredientId + '\'' +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", conversionFactor=" + conversionFactor +
                ", userId='" + userId + '\'' +
                ", createDate=" + createDate +
                ", lastUpdate=" + lastUpdate +
                '}';
    }

    public static class Builder {
        private String id;
        private String ingredientId;
        private String name;
        private String description;
        private double conversionFactor;
        private String createdBy;
        private long createDate;
        private long lastUpdate;

        public static Builder basedOnPersistenceModel(@Nonnull IngredientPersistenceModel model) {
            return new Builder().
                    setId(model.getDataId()).
                    setIngredientId(model.getIngredientId()).
                    setName(model.getName()).
                    setDescription(model.getDescription()).
                    setConversionFactor(model.getConversionFactor()).
                    setCreatedBy(model.getUserId()).
                    setCreateDate(model.getCreateDate()).
                    setLastUpdate(model.getLastUpdate());
        }

        public Builder setId(String id) {
            this.id = id;
            return this;
        }

        public Builder setIngredientId(String ingredientId) {
            this.ingredientId = ingredientId;
            return this;
        }

        public Builder setName(String name) {
            this.name = name;
            return this;
        }

        public Builder setDescription(String description) {
            this.description = description;
            return this;
        }

        public Builder setConversionFactor(double conversionFactor) {
            this.conversionFactor = conversionFactor;
            return this;
        }

        public Builder setCreatedBy(String createdBy) {
            this.createdBy = createdBy;
            return this;
        }

        public Builder setCreateDate(long createDate) {
            this.createDate = createDate;
            return this;
        }

        public Builder setLastUpdate(long lastUpdate) {
            this.lastUpdate = lastUpdate;
            return this;
        }

        public Builder getDefault() {
            id = "";
            ingredientId = CREATE_NEW_INGREDIENT;
            name = "";
            description = "";
            conversionFactor = UnitOfMeasureConstants.NO_CONVERSION_FACTOR;
            createdBy = Constants.getUserId();
            createDate = 0L;
            lastUpdate = 0L;

            return this;
        }

        public IngredientPersistenceModel build() {
            return new IngredientPersistenceModel(
                    id,
                    ingredientId,
                    name,
                    description,
                    conversionFactor,
                    createdBy,
                    createDate,
                    lastUpdate);
        }
    }
}
