package com.example.peter.thekitchenmenu.domain.usecase.product.component.identity;

import com.example.peter.thekitchenmenu.domain.model.BasePersistenceModel;

import java.util.Objects;

import javax.annotation.Nonnull;

public class ProductIdentityPersistenceModel
        extends BasePersistenceModel {

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
            extends
            PersistenceModelBuilder<Builder, ProductIdentityPersistenceModel>  {

        public Builder() {
            persistenceModel = new ProductIdentityPersistenceModel();
        }

        @Override
        public Builder getDefault() {
            persistenceModel.dataId = "";
            persistenceModel.domainId = "";
            persistenceModel.name = "";
            persistenceModel.description = "";
            persistenceModel.category = ProductIdentity.Category.NON_FOOD;
            persistenceModel.createDate = 0L;
            persistenceModel.lastUpdate = 0L;
            return this;
        }

        @Override
        public Builder basedOnModel(@Nonnull ProductIdentityPersistenceModel model) {
            persistenceModel.dataId = model.getDataId();
            persistenceModel.domainId = model.getDomainId();
            persistenceModel.name = model.getName();
            persistenceModel.description = model.getDescription();
            persistenceModel.category = model.getCategory();
            persistenceModel.createDate = model.getCreateDate();
            persistenceModel.lastUpdate = model.getLastUpdate();
            return self();
        }

        public Builder setDataId(String dataId) {
            persistenceModel.dataId = dataId;
            return self();
        }

        public Builder setDomainId(String domainId) {
            persistenceModel.domainId = domainId;
            return self();
        }

        public Builder setName(String name) {
            persistenceModel.name = name;
            return self();
        }

        public Builder setDescription(String description) {
            persistenceModel.description = description;
            return self();
        }

        public Builder setCategory(ProductIdentity.Category category) {
            persistenceModel.category = category;
            return self();
        }

        public Builder setCreateDate(long createDate) {
            persistenceModel.createDate = createDate;
            return self();
        }

        public Builder setLastUpdate(long lastUpdate) {
            persistenceModel.lastUpdate = lastUpdate;
            return self();
        }

        @Override
        protected Builder self() {
            return this;
        }
    }
}
