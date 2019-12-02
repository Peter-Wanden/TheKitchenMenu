package com.example.peter.thekitchenmenu.domain;

public abstract class UseCaseAbstract
        <Q extends UseCase.Request, P extends UseCase.Response> implements UseCase<Q, P> {

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
