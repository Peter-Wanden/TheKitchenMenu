package com.example.peter.thekitchenmenu.domain.usecase;

/**
 * Base class for all requests that operate on domain data. The data id represents an instance of
 * state encapsulated in a domain data model. Domain models are final, so each time a domain models
 * state changes a new model with data id is created and sent to the persistence framework. This
 * provides a complete history of all data generated through the life of a domain model, from its
 * creation to its eventual archiving.
 */
public abstract class UseCaseDomainRequest implements UseCase.Request {
    // The id for an instance of state of domain data.
    protected String dataId;
    // The id of the domain model, eg. recipeId, productId, ingredientId etc.
    protected String domainId;

    public String getDataId() {
        return dataId;
    }

    public String getDomainId() {
        return domainId;
    }
}
