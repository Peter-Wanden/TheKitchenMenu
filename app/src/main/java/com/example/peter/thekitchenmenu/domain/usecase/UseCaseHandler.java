package com.example.peter.thekitchenmenu.domain.usecase;


/**
 * Runs {@link UseCase}s using a {@link UseCaseScheduler}.
 */
public class UseCaseHandler {

    private static UseCaseHandler INSTANCE;
    private final UseCaseScheduler mUseCaseScheduler;

    public UseCaseHandler(UseCaseScheduler useCaseScheduler) {
        mUseCaseScheduler = useCaseScheduler;
    }

    public static UseCaseHandler getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new UseCaseHandler(new UseCaseThreadPoolScheduler());
        }
        return INSTANCE;
    }

    public <T extends UseCase.RequestValues,
            R extends UseCase.ResponseValues> void execute(
                final UseCase<T, R> useCase,
                T requestValues,
                UseCase.UseCaseCallback<R> callback) {
                    useCase.setRequestValues(requestValues);
                    useCase.setUseCaseCallback(new UiCallbackWrapper(callback, this));

        mUseCaseScheduler.execute(useCase::run);
    }

    public <V extends UseCase.ResponseValues> void notifyResponse(
            final V response,
            final UseCase.UseCaseCallback<V> useCaseCallback) {
        mUseCaseScheduler.notifyResponse(response, useCaseCallback);
    }

    private <V extends UseCase.ResponseValues> void notifyError(
            final V response,
            final UseCase.UseCaseCallback<V> useCaseCallback) {
        mUseCaseScheduler.onError(response, useCaseCallback);
    }

    private static final class UiCallbackWrapper<
            V extends UseCase.ResponseValues>
            implements UseCase.UseCaseCallback<V> {

        private final UseCase.UseCaseCallback<V> mCallback;
        private final UseCaseHandler mUseCaseHandler;

        public UiCallbackWrapper(UseCase.UseCaseCallback<V> callback,
                                 UseCaseHandler useCaseHandler) {
            mCallback = callback;
            mUseCaseHandler = useCaseHandler;
        }

        @Override
        public void onSuccess(V response) {
            mUseCaseHandler.notifyResponse(response, mCallback);
        }

        @Override
        public void onError(V response) {
            mUseCaseHandler.notifyError(response, mCallback);
        }
    }
}
