package com.example.peter.thekitchenmenu.domain.usecasenew.common.model;

/**
 * Base class for a domain data model.
 */
public abstract class BaseDomainModel
        implements
        DomainModel {

    /**
     * Base class for an {@link BaseDomainModelBuilder}
     * Always override self() in extending classes to return 'this'.
     * The use of self in this manner will return the last class in the inheritance tree.
     * @param <SELF> the {@link BaseDomainModelBuilder} extending this base builder.
     * @param <DOMAIN_MODEL> the {@link BaseDomainModel} being built.
     */
    public abstract static class BaseDomainModelBuilder<
            SELF extends BaseDomainModelBuilder<SELF, DOMAIN_MODEL>,
            DOMAIN_MODEL extends BaseDomainModel> {

        public BaseDomainModelBuilder(DOMAIN_MODEL domainModel) {
            this.domainModel = domainModel;
        }

        protected DOMAIN_MODEL domainModel;

        public abstract SELF getDefault();

        public abstract SELF basedOnModel(DOMAIN_MODEL model);

        protected abstract SELF self(); // when implementing, return this

        public DOMAIN_MODEL build() {
            return domainModel;
        }
    }
}
