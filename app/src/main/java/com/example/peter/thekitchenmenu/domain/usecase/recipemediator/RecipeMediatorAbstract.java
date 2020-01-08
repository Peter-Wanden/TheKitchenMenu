package com.example.peter.thekitchenmenu.domain.usecase.recipemediator;

public abstract class RecipeMediatorAbstract <P extends RecipeMediatorAbstract.Response> {

    private Callback<P> callback;

    public Callback<P> getMediatorCallback() {
        return callback;
    }

    public void setMediatorCallback(Callback<P> callback) {
        this.callback = callback;
    }

    interface Response {
    }

    interface Callback<R> {
        void onSuccess(R response);
        void onError(R response);
    }
}
