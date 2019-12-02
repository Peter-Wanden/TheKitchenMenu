package com.example.peter.thekitchenmenu.domain;

/**
 * Runs {@link UseCaseAbstract}s using a {@link UseCaseScheduler}.
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

    public <T extends UseCaseAbstract.Request, R extends UseCaseAbstract.Response> void execute(
            final UseCaseAbstract<T, R> useCase, T request, UseCaseAbstract.Callback<R> callback) {
        useCase.setRequest(request);
        useCase.setUseCaseCallback(new UseCaseHandler.UiCallbackWrapper(callback, this));

        useCaseScheduler.execute(useCase::run);
    }

    public <V extends UseCaseAbstract.Response> void notifyResponse(
            final V response,
            final UseCaseAbstract.Callback<V> callback) {
        useCaseScheduler.notifyResponse(response, callback);
    }

    private <V extends UseCaseAbstract.Response> void notifyError(
            final V response,
            final UseCaseAbstract.Callback<V> callback) {
        useCaseScheduler.onError(response, callback);
    }

    private static final class UiCallbackWrapper<V extends UseCaseAbstract.Response>
            implements UseCaseAbstract.Callback<V> {

        private final UseCaseAbstract.Callback<V> callback;
        private final UseCaseHandler handler;

        public UiCallbackWrapper(UseCaseAbstract.Callback<V> callback,
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
