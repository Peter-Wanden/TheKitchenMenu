package com.example.peter.thekitchenmenu.domain.model;

import java.util.Objects;

import javax.annotation.Nonnull;

public abstract class BasePersistenceModel
        implements
        DomainModel.PersistenceModel {

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
        if (!(o instanceof BasePersistenceModel)) return false;
        BasePersistenceModel that = (BasePersistenceModel) o;
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
            SELF extends PersistenceModelBuilder<SELF, PERSISTENCE_MODEL>,
            PERSISTENCE_MODEL extends BasePersistenceModel> {

        protected PERSISTENCE_MODEL persistenceModel;

        public abstract SELF basedOnModel(PERSISTENCE_MODEL model);

        public abstract SELF getDefault();

        public SELF setDataId(String dataId) {
            persistenceModel.dataId = dataId;
            return self();
        }

        public SELF setDomainId(String domainId) {
            persistenceModel.domainId = domainId;
            return self();
        }

        public SELF setCreateDate(long createDate) {
            persistenceModel.createDate = createDate;
            return self();
        }

        public SELF setLastUpdate(long lastUpdate) {
            persistenceModel.lastUpdate = lastUpdate;
            return self();
        }

        public PERSISTENCE_MODEL build() {
            return persistenceModel;
        }

        protected SELF self() {
            // noinspection unchecked
            return (SELF) this;
        }
    }
}
