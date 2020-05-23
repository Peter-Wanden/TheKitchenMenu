package com.example.peter.thekitchenmenu.commonmocks;

import com.example.peter.thekitchenmenu.domain.usecase.UseCaseBase;
import com.example.peter.thekitchenmenu.domain.usecase.UseCaseScheduler;

public class UseCaseSchedulerMock implements UseCaseScheduler {
    @Override
    public void execute(Runnable runnable) {
        runnable.run();
    }

    @Override
    public <V extends UseCaseBase.Response> void onSuccessResponse(
            V response, UseCaseBase.Callback<V> callback) {
        callback.onSuccessResponse(response);
    }

    @Override
    public <V extends UseCaseBase.Response> void onErrorResponse(
            V response, UseCaseBase.Callback<V> callback) {
        callback.onErrorResponse(response);
    }
}
