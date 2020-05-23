package com.example.peter.thekitchenmenu.domain.usecase;

/**
 * Base class for a domain data model.
 */
public abstract class BaseDomainModel {

    /**
     * Base class for an {@link DomainModelBuilder}
     * Always override self() in extending classes to return 'this'.
     * The use of self in this manner will return the last class in the inheritance tree.
     * @param <SELF> the {@link DomainModelBuilder} extending this base builder.
     * @param <DOMAIN_MODEL> the {@link BaseDomainModel} being built.
     */
    public static abstract class DomainModelBuilder<
            SELF extends DomainModelBuilder<SELF, DOMAIN_MODEL>,
            DOMAIN_MODEL extends BaseDomainModel> {

        protected DOMAIN_MODEL domainModel;

        public abstract SELF getDefault();

        protected SELF self() {
            // noinspection unchecked
            return (SELF) this;
        }

        public DOMAIN_MODEL build() {
            return domainModel;
        }
    }
}
