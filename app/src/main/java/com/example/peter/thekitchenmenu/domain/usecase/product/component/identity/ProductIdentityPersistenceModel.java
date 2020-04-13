package com.example.peter.thekitchenmenu.domain.usecase.product.component.identity;

import com.example.peter.thekitchenmenu.domain.usecase.BasePersistence;

import java.util.Objects;

import javax.annotation.Nonnull;

public class ProductIdentityPersistenceModel
        extends BasePersistence {

    private String name;
    private String description;
    private ProductIdentity.Category category;
    private long createDate;
    private long lastUpdate;

    private ProductIdentityPersistenceModel(){}

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public ProductIdentity.Category getCategory() {
        return category;
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
        ProductIdentityPersistenceModel that = (ProductIdentityPersistenceModel) o;
        return dataId.equals(that.dataId) &&
                domainId.equals(that.domainId) &&
                name.equals(that.name) &&
                description.equals(that.description) &&
                category == that.category &&
                createDate == that.createDate &&
                lastUpdate == that.lastUpdate;

    }

    @Override
    public int hashCode() {
        return Objects.hash(dataId, domainId, name, description, category, createDate, lastUpdate);
    }

    @Nonnull
    @Override
    public String toString() {
        return "ProductIdentityPersistenceModel{" +
                "dataId='" + dataId + '\'' +
                ", domainId='" + domainId + '\'' +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", category=" + category +
                ", createDate=" + createDate +
                ", lastUpdate=" + lastUpdate +
                '}';
    }

    public static class Builder
            extends DomainModelBuilder<Builder, ProductIdentityPersistenceModel>  {

        public Builder() {
            model = new ProductIdentityPersistenceModel();
        }

        @Override
        public Builder getDefault() {
            model.dataId = "";
            model.domainId = "";
            model.name = "";
            model.description = "";
            model.category = ProductIdentity.Category.NON_FOOD;
            model.createDate = 0L;
            model.lastUpdate = 0L;
            return this;
        }

        public Builder setDataId(String dataId) {
            model.dataId = dataId;
            return self();
        }

        public Builder setDomainId(String domainId) {
            model.domainId = domainId;
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

        public Builder setCategory(ProductIdentity.Category category) {
            model.category = category;
            return self();
        }

        public Builder setCreateDate(long createDate) {
            model.createDate = createDate;
            return self();
        }

        public Builder setLastUpdate(long lastUpdate) {
            model.lastUpdate = lastUpdate;
            return self();
        }

        @Override
        protected Builder self() {
            return this;
        }
    }
}
