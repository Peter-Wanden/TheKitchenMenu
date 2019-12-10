package com.example.peter.thekitchenmenu.commonmocks;

import com.example.peter.thekitchenmenu.domain.UseCaseInteractor;
import com.example.peter.thekitchenmenu.domain.UseCaseScheduler;

public class UseCaseSchedulerMock implements UseCaseScheduler {
    @Override
    public void execute(Runnable runnable) {
        runnable.run();
    }

    @Override
    public <V extends UseCaseInteractor.Response> void notifyResponse(
            V response, UseCaseInteractor.Callback<V> callback) {
        callback.onSuccess(response);
    }

    @Override
    public <V extends UseCaseInteractor.Response> void onError(
            V response, UseCaseInteractor.Callback<V> callback) {
        callback.onError(response);
    }
}
