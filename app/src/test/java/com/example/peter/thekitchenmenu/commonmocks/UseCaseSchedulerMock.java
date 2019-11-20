package com.example.peter.thekitchenmenu.commonmocks;

import com.example.peter.thekitchenmenu.domain.usecase.UseCase;
import com.example.peter.thekitchenmenu.domain.usecase.UseCaseScheduler;

public class UseCaseSchedulerMock implements UseCaseScheduler {
    @Override
    public void execute(Runnable runnable) {
        runnable.run();
    }

    @Override
    public <V extends UseCase.ResponseValues> void notifyResponse(
            V response, UseCase.UseCaseCallback<V> useCaseCallback) {
        useCaseCallback.onSuccess(response);
    }

    @Override
    public <V extends UseCase.ResponseValues> void onError(
            V response, UseCase.UseCaseCallback<V> useCaseCallback) {
        useCaseCallback.onError(response);
    }
}
