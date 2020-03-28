package com.example.peter.thekitchenmenu.domain.usecase;

/**
 * Base class for all requests that operate on domain data.
 */
public abstract class UseCaseDomainRequest implements UseCase.Request {

    // The id for an instance of state stored by a domain model. It is changed each time a domain
    // model changes state and stores new state data. This is used to keep a record of all changes.
    // Each time data state changes for a domain ID, the domain data .
    protected String dataId;
    // The id of the domain model, eg. recipeId, productId, ingredientId
    protected String domainId;

    public String getDataId() {
        return dataId;
    }

    public String getDomainId() {
        return domainId;
    }
}
