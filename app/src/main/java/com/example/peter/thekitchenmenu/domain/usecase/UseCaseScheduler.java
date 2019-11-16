
package com.example.peter.thekitchenmenu.domain.usecase;

/**
 * Interface for schedulers, see {@link UseCaseThreadPoolScheduler}.
 */
public interface UseCaseScheduler {

    void execute(Runnable runnable);

    <V extends UseCase.ResponseValues> void notifyResponse(
            final V response,
            final UseCase.UseCaseCallback<V> useCaseCallback);

    <V extends UseCase.ResponseValues> void onError(
            final V response,
            final UseCase.UseCaseCallback<V> useCaseCallback);
}
