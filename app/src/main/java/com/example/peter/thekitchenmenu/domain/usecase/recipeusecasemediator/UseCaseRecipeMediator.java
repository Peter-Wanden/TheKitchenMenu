package com.example.peter.thekitchenmenu.domain.usecase.recipeusecasemediator;

/**
 * A recipe is made up of a complex set of independently changing data structures each controlled by
 * its own use case.
 * The {@link UseCaseRecipeMediator} acts as a mediator between the use cases.
 * It listens to controllers through its input port, passes data to the relevant use cases for
 * processing and if appropriate, collects and send results to presenters listening on its output
 * port.
 * This separates controllers, use cases and presenters from each other enforcing the dependency
 * rule.
 */
public class UseCaseRecipeMediator {

}
