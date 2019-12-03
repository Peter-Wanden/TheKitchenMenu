package com.example.peter.thekitchenmenu.domain;

/**
 * Runs {@link UseCaseCommandAbstract}s using a {@link UseCaseScheduler}.
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

    public <T extends UseCaseCommandAbstract.Request, R extends UseCaseCommandAbstract.Response> void execute(
            final UseCaseCommandAbstract<T, R> useCase, T request, UseCaseCommandAbstract.Callback<R> callback) {
        useCase.setRequest(request);
        useCase.setUseCaseCallback(new UseCaseHandler.UiCallbackWrapper(callback, this));

        useCaseScheduler.execute(useCase::run);
    }

    public <V extends UseCaseCommandAbstract.Response> void notifyResponse(
            final V response,
            final UseCaseCommandAbstract.Callback<V> callback) {
        useCaseScheduler.notifyResponse(response, callback);
    }

    private <V extends UseCaseCommandAbstract.Response> void notifyError(
            final V response,
            final UseCaseCommandAbstract.Callback<V> callback) {
        useCaseScheduler.onError(response, callback);
    }

    private static final class UiCallbackWrapper<V extends UseCaseCommandAbstract.Response>
            implements UseCaseCommandAbstract.Callback<V> {

        private final UseCaseCommandAbstract.Callback<V> callback;
        private final UseCaseHandler handler;

        public UiCallbackWrapper(UseCaseCommandAbstract.Callback<V> callback,
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
