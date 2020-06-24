package com.example.peter.thekitchenmenu.domain.model;

import java.util.Objects;

import javax.annotation.Nonnull;

public abstract class BaseDomainPersistenceModel
        extends BaseDomainModel
        implements DomainPersistenceModel {

    protected String dataId;
    protected String domainId;
    protected long createDate;
    protected long lastUpdate;

    @Override
    public String getDataId() {
        return dataId;
    }

    @Override
    public String getDomainId() {
        return domainId;
    }

    @Override
    public long getCreateDate() {
        return createDate;
    }

    @Override
    public long getLastUpdate() {
        return lastUpdate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof BaseDomainPersistenceModel)) return false;
        BaseDomainPersistenceModel that = (BaseDomainPersistenceModel) o;
        return createDate == that.createDate &&
                lastUpdate == that.lastUpdate &&
                Objects.equals(dataId, that.dataId) &&
                Objects.equals(domainId, that.domainId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(dataId, domainId, createDate, lastUpdate);
    }

    @Nonnull
    @Override
    public String toString() {
        return "BaseDomainPersistenceModel{" +
                "dataId='" + dataId + '\'' +
                ", domainId='" + domainId + '\'' +
                ", createDate=" + createDate +
                ", lastUpdate=" + lastUpdate +
                '}';
    }

    public abstract static class PersistenceModelBuilder<
            SELF extends PersistenceModelBuilder<SELF, DOMAIN_MODEL>,
                    DOMAIN_MODEL extends BaseDomainPersistenceModel>
            extends DomainModelBuilder<SELF, DOMAIN_MODEL> {

        public abstract SELF basedOnModel(DOMAIN_MODEL model);

        public SELF setDataId(String dataId) {
            domainModel.dataId = dataId;
            return self();
        }

        public SELF setDomainId(String domainId) {
            domainModel.domainId = domainId;
            return self();
        }

        public SELF setCreateDate(long createDate) {
            domainModel.createDate = createDate;
            return self();
        }

        public SELF setLastUpdate(long lastUpdate) {
            domainModel.lastUpdate = lastUpdate;
            return self();
        }
    }
}
