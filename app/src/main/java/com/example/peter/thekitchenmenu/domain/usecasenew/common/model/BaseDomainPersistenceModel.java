package com.example.peter.thekitchenmenu.domain.usecasenew.common.model;

public abstract class BaseDomainPersistenceModel
        extends
        BaseDomainModel
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

    protected abstract static class PersistenceModelBuilder<
            SELF extends PersistenceModelBuilder<SELF, PERSISTENCE_MODEL>,
            PERSISTENCE_MODEL extends BaseDomainPersistenceModel>
            extends
            BaseDomainModelBuilder<SELF, PERSISTENCE_MODEL> {

        public PersistenceModelBuilder(PERSISTENCE_MODEL domainModel) {
            super(domainModel);
        }

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
