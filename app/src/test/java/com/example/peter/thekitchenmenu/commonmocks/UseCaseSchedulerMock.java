package com.example.peter.thekitchenmenu.commonmocks;

import com.example.peter.thekitchenmenu.domain.usecase.UseCase;
import com.example.peter.thekitchenmenu.domain.usecase.UseCaseScheduler;

public class UseCaseSchedulerMock implements UseCaseScheduler {
    @Override
    public void execute(Runnable runnable) {
        runnable.run();
    }

    @Override
    public <V extends UseCase.Response> void notifyResponse(
            V response, UseCase.Callback<V> callback) {
        callback.onSuccess(response);
    }

    @Override
    public <V extends UseCase.Response> void onError(
            V response, UseCase.Callback<V> callback) {
        callback.onError(response);
    }
}
