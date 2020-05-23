package com.example.peter.thekitchenmenu.domain.usecase.recipe.macro;

import com.example.peter.thekitchenmenu.domain.usecase.UseCaseBase;

/**
 * A shortened version of the {@link UseCaseBase.Callback}
 * @param <R> the use case callback
 */
public abstract class RecipeUseCaseCallback<R extends UseCaseBase.Response>
        implements UseCaseBase.Callback<R> {

    @Override
    public void onSuccessResponse(R response) {
        processResponse(response);
    }

    @Override
    public void onErrorResponse(R response) {
        processResponse(response);
    }

    protected abstract void processResponse(R response);
}
