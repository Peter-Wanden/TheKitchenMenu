package com.example.peter.thekitchenmenu.commonmocks;

import com.example.peter.thekitchenmenu.domain.UseCaseAbstract;
import com.example.peter.thekitchenmenu.domain.UseCaseScheduler;

public class UseCaseSchedulerMock implements UseCaseScheduler {
    @Override
    public void execute(Runnable runnable) {
        runnable.run();
    }

    @Override
    public <V extends UseCaseAbstract.Response> void notifyResponse(
            V response, UseCaseAbstract.Callback<V> callback) {
        callback.onSuccess(response);
    }

    @Override
    public <V extends UseCaseAbstract.Response> void onError(
            V response, UseCaseAbstract.Callback<V> callback) {
        callback.onError(response);
    }
}
