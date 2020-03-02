package com.example.peter.thekitchenmenu.domain.usecase;

public abstract class UseCase {

    private UseCase.Request request;
    private Callback<UseCase.Response> callback;

    public <Q extends UseCase.Request> void setRequest(Q request) {
        this.request = request;
    }

    public UseCase.Request getRequest() {
        return request;
    }

    public Callback<UseCase.Response> getUseCaseCallback() {
        return callback;
    }

    public void setUseCaseCallback(Callback<UseCase.Response> callback) {
        this.callback = callback;
    }

    public void run() {
        execute(request);
    }

    protected abstract <Q extends UseCase.Request> void execute(Q request);

    /**
     * Data passed to a request.
     */
    public interface Request {
    }

    /**
     * Data received from a request.
     */
    public interface Response {
    }

    public interface Callback<R> {
        void onSuccess(R response);

        void onError(R response);
    }
}
