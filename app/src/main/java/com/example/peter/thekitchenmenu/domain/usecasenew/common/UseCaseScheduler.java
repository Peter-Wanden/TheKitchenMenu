package com.example.peter.thekitchenmenu.domain.usecasenew.common;

import com.example.peter.thekitchenmenu.domain.usecase.common.UseCaseBase;

public interface UseCaseScheduler {

    void execute(Runnable runnable);

    <RESPONSE extends UseCaseBase.Response> void onSuccessResponse(
            final RESPONSE response,
            final UseCaseBase.Callback<RESPONSE> callback);

    <RESPONSE extends UseCaseBase.Response> void onErrorResponse(
            final RESPONSE response,
            final UseCaseBase.Callback<RESPONSE> callback);
}
