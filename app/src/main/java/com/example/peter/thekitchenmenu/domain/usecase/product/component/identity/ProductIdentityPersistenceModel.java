package com.example.peter.thekitchenmenu.domain.usecase.product.component.identity;

import com.example.peter.thekitchenmenu.domain.usecasenew.model.BaseDomainPersistenceModel;

import java.util.Objects;

import javax.annotation.Nonnull;

public class ProductIdentityPersistenceModel
        extends BaseDomainPersistenceModel {

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
            domainModel = new ProductIdentityPersistenceModel();
        }

        @Override
        public Builder getDefault() {
            domainModel.dataId = "";
            domainModel.domainId = "";
            domainModel.name = "";
            domainModel.description = "";
            domainModel.category = ProductIdentity.Category.NON_FOOD;
            domainModel.createDate = 0L;
            domainModel.lastUpdate = 0L;
            return this;
        }

        @Override
        public Builder basedOnRequestModel(@Nonnull ProductIdentityPersistenceModel model) {
            domainModel.dataId = model.getDataId();
            domainModel.domainId = model.getDomainId();
            domainModel.name = model.getName();
            domainModel.description = model.getDescription();
            domainModel.category = model.getCategory();
            domainModel.createDate = model.getCreateDate();
            domainModel.lastUpdate = model.getLastUpdate();
            return self();
        }

        public Builder setDataId(String dataId) {
            domainModel.dataId = dataId;
            return self();
        }

        public Builder setDomainId(String domainId) {
            domainModel.domainId = domainId;
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

        public Builder setCategory(ProductIdentity.Category category) {
            domainModel.category = category;
            return self();
        }

        public Builder setCreateDate(long createDate) {
            domainModel.createDate = createDate;
            return self();
        }

        public Builder setLastUpdate(long lastUpdate) {
            domainModel.lastUpdate = lastUpdate;
            return self();
        }

        @Override
        protected Builder self() {
            return this;
        }
    }
}
