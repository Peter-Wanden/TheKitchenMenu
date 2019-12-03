package com.example.peter.thekitchenmenu.domain;

public interface UseCaseScheduler {

    void execute(Runnable runnable);

    <V extends UseCaseCommand.Response> void notifyResponse(
            final V response,
            final UseCaseCommand.Callback<V> callback);

    <V extends UseCaseCommand.Response> void onError(
            final V response,
            final UseCaseCommand.Callback<V> callback);
}
