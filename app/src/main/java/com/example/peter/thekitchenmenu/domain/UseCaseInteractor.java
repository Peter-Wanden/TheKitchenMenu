package com.example.peter.thekitchenmenu.domain;

public abstract class UseCaseInteractor
        <Q extends UseCaseCommand.Request, P extends UseCaseCommand.Response>
        implements UseCaseCommand<Q, P> {

    public enum Result {
        DATA_UNAVAILABLE,
        INVALID_UNCHANGED,
        VALID_UNCHANGED,
        INVALID_CHANGED,
        VALID_CHANGED,
    }

    private Q request;
    private Callback<P> callback;

    @Override
    public void setRequest(Q request) {
        this.request = request;
    }

    @Override
    public Q getRequest() {
        return request;
    }

    @Override
    public Callback<P> getUseCaseCallback() {
        return callback;
    }

    @Override
    public void setUseCaseCallback(Callback<P> callback) {
        this.callback = callback;
    }

    @Override
    public void run() {
        execute(request);
    }

    protected abstract void execute(Q request);
}
