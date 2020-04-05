package com.example.peter.thekitchenmenu.domain.usecase;

/**
 * Base class for all requests and responses messages which operate on domain data.
 * The data id represents an instance of state encapsulated in a domain data model.
 * Domain models are final, so each time a domain models state changes, a new model
 * with data id is created and sent to the persistence framework. This provides a
 * complete history of all data generated through the life of a domain model, from
 * its creation to its eventual archiving.
 */
public abstract class UseCaseDomainMessageBase
        implements UseCase.Message {
    // The id for an instance of state of domain data as stored in the data layer.
    protected String dataId;
    // The id of the domain model, eg. recipeId, productId, ingredientId etc.
    protected String domainId;

    public String getDataId() {
        return dataId;
    }

    public String getDomainId() {
        return domainId;
    }

    public static abstract class UseCaseMessageBuilder<
            SELF extends UseCaseMessageBuilder, // The builder class
            M extends UseCaseDomainMessageBase> { // The request / response class

        // The request or response class
        protected M message;

        public abstract SELF getDefault();

        public SELF setDataId(String dataId) {
            message.dataId = dataId;
            return self();
        }

        public SELF setDomainId(String domainId) {
            message.domainId = domainId;
            return self();
        }

        public M build() {
            return message;
        }

        // When implemented in a concrete class, return 'this'
        protected abstract SELF self();
    }
}
