package com.example.peter.thekitchenmenu.domain.usecasenew.common;

import com.example.peter.thekitchenmenu.domain.usecasenew.common.message.UseCaseCallback;
import com.example.peter.thekitchenmenu.domain.usecasenew.common.message.UseCaseRequest;
import com.example.peter.thekitchenmenu.domain.usecasenew.common.message.UseCaseResponse;
import com.example.peter.thekitchenmenu.domain.usecasenew.model.DomainModel;

public abstract class UseCase<
        REQUEST_MODEL extends DomainModel.RequestModel,
        RESPONSE_MODEL extends DomainModel.ResponseModel> {

    public static final String NO_ID = "";

    protected UseCaseRequest<REQUEST_MODEL> useCaseRequest;
    protected UseCaseCallback<UseCaseResponse<RESPONSE_MODEL>> useCaseCallback;

    public void execute(UseCaseRequest<REQUEST_MODEL> useCaseRequest,
                        UseCaseCallback<UseCaseResponse<RESPONSE_MODEL>> useCaseCallback) {
        setUseCaseRequest(useCaseRequest);
        setUseCaseCallback(useCaseCallback);

        run();
    }

    protected abstract void execute(UseCaseRequest<REQUEST_MODEL> useCaseRequest);

    void run() {
        execute(useCaseRequest);
    }

    public UseCaseRequest<REQUEST_MODEL> getUseCaseRequest() {
        return useCaseRequest;
    }

    public void setUseCaseRequest(UseCaseRequest<REQUEST_MODEL> useCaseRequest) {
        this.useCaseRequest = useCaseRequest;
    }

    void setUseCaseCallback(UseCaseCallback<UseCaseResponse<RESPONSE_MODEL>> useCaseCallback) {
        this.useCaseCallback = useCaseCallback;
    }

    public UseCaseCallback<UseCaseResponse<RESPONSE_MODEL>> getUseCaseCallback() {
        return useCaseCallback;
    }
}
