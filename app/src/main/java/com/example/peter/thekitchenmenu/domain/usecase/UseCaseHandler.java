package com.example.peter.thekitchenmenu.domain.usecase;

/**
 * Runs {@link UseCase}s using a {@link UseCaseScheduler}.
 */
public class UseCaseHandler {

    private static UseCaseHandler INSTANCE;
    private final UseCaseScheduler useCaseScheduler;

    public UseCaseHandler(UseCaseScheduler useCaseScheduler) {
        this.useCaseScheduler = useCaseScheduler;
    }

    public static UseCaseHandler getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new UseCaseHandler(new UseCaseThreadPoolScheduler());
        }
        return INSTANCE;
    }

    public <T extends UseCase.Request, R extends UseCase.Response> void execute(
            final UseCase useCase,
            T request,
            UseCase.Callback<R> callback) {

        useCase.setRequest(request);
        useCase.setUseCaseCallback(new UseCaseHandler.UiCallbackWrapper(callback, this));

        useCaseScheduler.execute(useCase::run);
    }

    public <V extends UseCase.Response> void notifyResponse(final V response,
                        final UseCase.Callback<V> callback) {
        useCaseScheduler.notifyResponse(response, callback);
    }

    private <V extends UseCase.Response>
    void notifyError(final V response,
                     final UseCase.Callback<V> callback) {
        useCaseScheduler.onError(response, callback);
    }

    private static final class UiCallbackWrapper<V extends UseCase.Response>
            implements UseCase.Callback<V> {

        private final UseCase.Callback<V> callback;
        private final UseCaseHandler handler;

        public UiCallbackWrapper(UseCase.Callback<V> callback,
                                 UseCaseHandler handler) {
            this.callback = callback;
            this.handler = handler;
        }

        @Override
        public void onSuccess(V response) {
            handler.notifyResponse(response, callback);
        }

        @Override
        public void onError(V response) {
            handler.notifyError(response, callback);
        }
    }
}
