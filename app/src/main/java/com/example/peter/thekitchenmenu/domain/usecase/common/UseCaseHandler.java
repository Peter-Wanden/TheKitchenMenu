package com.example.peter.thekitchenmenu.domain.usecase.common;

/**
 * Runs {@link UseCaseBase}s using a {@link UseCaseScheduler}.
 */
public class UseCaseHandler {

    private static final class UiCallbackWrapper<RESPONSE extends UseCaseBase.Response>
            implements
            UseCaseBase.Callback<RESPONSE> {

        private final UseCaseBase.Callback<RESPONSE> callback;
        private final UseCaseHandler handler;

        public UiCallbackWrapper(UseCaseBase.Callback<RESPONSE> callback,
                                 UseCaseHandler handler) {
            this.callback = callback;
            this.handler = handler;
        }

        @Override
        public void onUseCaseSuccess(RESPONSE response) {
            handler.notifyResponse(response, callback);
        }

        @Override
        public void onUseCaseError(RESPONSE response) {
            handler.notifyError(response, callback);
        }
    }

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

    public <REQUEST extends UseCaseBase.Request, RESPONSE extends UseCaseBase.Response> void executeAsync(
            final UseCaseBase useCase,
            REQUEST request,
            UseCaseBase.Callback<RESPONSE> callback) {

        useCase.setRequest(request);
        useCase.setUseCaseCallback(new UseCaseHandler.UiCallbackWrapper(callback, this));

        useCaseScheduler.execute(useCase::run);
    }

    public <RESPONSE extends UseCaseBase.Response> void notifyResponse(
            final RESPONSE response,
            final UseCaseBase.Callback<RESPONSE> callback) {
        useCaseScheduler.onSuccessResponse(response, callback);
    }

    private <RESPONSE extends UseCaseBase.Response> void notifyError(
            final RESPONSE response,
            final UseCaseBase.Callback<RESPONSE> callback) {
        useCaseScheduler.onErrorResponse(response, callback);
    }
}
