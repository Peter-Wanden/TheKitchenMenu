package com.example.peter.thekitchenmenu.commonmocks;

import com.example.peter.thekitchenmenu.domain.UseCaseCommandAbstract;
import com.example.peter.thekitchenmenu.domain.UseCaseScheduler;

public class UseCaseSchedulerMock implements UseCaseScheduler {
    @Override
    public void execute(Runnable runnable) {
        runnable.run();
    }

    @Override
    public <V extends UseCaseCommandAbstract.Response> void notifyResponse(
            V response, UseCaseCommandAbstract.Callback<V> callback) {
        callback.onSuccess(response);
    }

    @Override
    public <V extends UseCaseCommandAbstract.Response> void onError(
            V response, UseCaseCommandAbstract.Callback<V> callback) {
        callback.onError(response);
    }
}
