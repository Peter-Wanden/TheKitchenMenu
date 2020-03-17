package com.example.peter.thekitchenmenu.domain.usecase.recipe.macro;

import com.example.peter.thekitchenmenu.domain.usecase.UseCase;

public abstract class RecipeUseCaseCallback<R extends UseCase.Response>
        implements UseCase.Callback<R> {

    @Override
    public void onSuccess(R response) {
        processResponse(response);
    }

    @Override
    public void onError(R response) {
        processResponse(response);
    }

    protected abstract void processResponse(R response);
}
