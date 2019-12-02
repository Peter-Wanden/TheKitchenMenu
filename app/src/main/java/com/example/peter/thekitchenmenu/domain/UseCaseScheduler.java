package com.example.peter.thekitchenmenu.domain;

public interface UseCaseScheduler {

    void execute(Runnable runnable);

    <V extends UseCase.Response> void notifyResponse(
            final V response,
            final UseCase.Callback<V> callback);

    <V extends UseCase.Response> void onError(
            final V response,
            final UseCase.Callback<V> callback);
}
