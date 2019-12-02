package com.example.peter.thekitchenmenu.domain;

/**
 * Use cases are the entry points to the domain layer.
 *
 * @param <Q> the request type
 * @param <P> the response type
 */
public interface UseCase<Q extends UseCase.Request, P extends UseCase.Response>{

    void setRequest(Q request);

    Q getRequest();

    UseCase.Callback<P> getUseCaseCallback();

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
