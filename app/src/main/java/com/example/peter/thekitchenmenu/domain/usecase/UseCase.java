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
     * Allows for the abstraction of commonalities between Requests and Responses
     */
    public interface Message {

    }

    /**
     * Data passed to a request.
     */
    public interface Request extends Message {
    }

    /**
     * Data received after a request has been processed.
     */
    public interface Response extends Message {
    }

    public interface Callback<R> {

        void onUseCaseSuccess(R response);

        void onUseCaseError(R response);
    }
}
