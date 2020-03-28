package com.example.peter.thekitchenmenu.domain.usecase;

/**
 * Base class for a domain data model extended as the domain data model in use case requests and
 * responses.
 */
public abstract class UseCaseDomainModel {

    /**
     * Base class for an {@link DomainModelBuilder}
     * Always override self() in extending classes to return 'this'.
     * The use of self in this manner will return the last class in the inheritance tree.
     * @param <SELF> the {@link DomainModelBuilder} extending this base builder.
     * @param <M> the {@link UseCaseDomainModel} being built.
     */
    public static abstract class DomainModelBuilder<
            SELF extends DomainModelBuilder<SELF, M>,
            M extends UseCaseDomainModel> {

        protected M model;

        public abstract SELF getDefault();

        protected SELF self() {
            // noinspection unchecked
            return (SELF) this;
        }

        public M build() {
            return model;
        }
    }
}
