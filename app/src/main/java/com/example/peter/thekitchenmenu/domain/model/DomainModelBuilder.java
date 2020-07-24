package com.example.peter.thekitchenmenu.domain.model;

/**
 * Base class for an {@link DomainModelBuilder}
 * Always override self() in extending classes to return 'this'.
 * The use of self in this manner will return the last class in the inheritance tree.
 * @param <SELF> the {@link DomainModelBuilder} extending this base builder.
 * @param <DOMAIN_MODEL> the {@link BaseDomainModel} being built.
 */
public abstract class DomainModelBuilder<
        SELF extends DomainModelBuilder<SELF, DOMAIN_MODEL>,
        DOMAIN_MODEL extends DomainModel> {

    protected DOMAIN_MODEL domainModel;

    public abstract SELF getDefault();

    protected abstract SELF self(); // when implementing, return 'this'

    public DOMAIN_MODEL build() {
        return domainModel;
    }
}
