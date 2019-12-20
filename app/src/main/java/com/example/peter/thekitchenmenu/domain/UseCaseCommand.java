package com.example.peter.thekitchenmenu.domain;

/**
 * Use cases are the entry points to the domain layer. Use cases use Command pattern for
 * sending/receiving, request/response data structures between use cases and application layers.
 *
 * @param <Q> the request type
 * @param <P> the response type
 */
public interface UseCaseCommand<Q extends UseCaseCommand.Request, P extends UseCaseCommand.Response> {

    void setRequest(Q request);

    Q getRequest();

    UseCaseCommand.Callback<P> getUseCaseCallback();

    void setUseCaseCallback(Callback<P> callback);

    void run();

    /**
     * Data passed to a request.
     */
    interface Request {
    }

    /**
     * Data received from a request.
     */
    interface Response {
    }

    interface Callback<R> {
        void onSuccess(R response);
        void onError(R response);
    }
}
