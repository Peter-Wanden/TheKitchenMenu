package com.example.peter.thekitchenmenu.domain.usecase;

public abstract class UseCaseBase {

    // Base for request response messages
    interface Message {
    }

    // Data passed to a request
    public interface Request extends Message {
    }

    // Data received after a request has been processed
    public interface Response extends Message {
    }

    public interface Callback<RESPONSE> {
        void onUseCaseSuccess(RESPONSE response);

        void onUseCaseError(RESPONSE response);
    }

    private UseCaseBase.Request request;
    private Callback<UseCaseBase.Response> callback;

    public <REQUEST extends UseCaseBase.Request> void setRequest(REQUEST request) {
        this.request = request;
    }

    public UseCaseBase.Request getRequest() {
        return request;
    }

    public Callback<UseCaseBase.Response> getUseCaseCallback() {
        return callback;
    }

    void setUseCaseCallback(Callback<UseCaseBase.Response> callback) {
        this.callback = callback;
    }

    void run() {
        execute(request);
    }

    // For running use cases on the current thread (handler not required).
    // -- DO NOT USE ON THE UI THREAD IF USE CASE USES SYNCHRONOUS OR ASYNCHRONOUS METHODS --
    public <REQUEST extends Request, RESPONSE extends Response> void execute (
            REQUEST request, Callback<RESPONSE> callback) {

        setRequest(request);
        setUseCaseCallback((Callback<Response>) callback);

        run();
    }

    protected abstract <REQUEST extends UseCaseBase.Request> void execute(REQUEST request);
}
