package com.example.peter.thekitchenmenu.domain.usecasenew.common;

import com.example.peter.thekitchenmenu.domain.usecasenew.common.componentname.UseCaseInvokerComponentName;
import com.example.peter.thekitchenmenu.domain.usecasenew.common.message.UseCaseCallback;
import com.example.peter.thekitchenmenu.domain.usecasenew.common.message.UseCaseRequest;
import com.example.peter.thekitchenmenu.domain.usecasenew.common.message.UseCaseResponse;
import com.example.peter.thekitchenmenu.domain.usecasenew.common.model.DomainModel;
import com.example.peter.thekitchenmenu.domain.usecasenew.common.model.DomainModel.UseCaseRequestModel;
import com.example.peter.thekitchenmenu.domain.usecasenew.common.model.DomainModel.UseCaseResponseModel;

public abstract class UseCaseInvoker {

    private static final String TAG = "tkm-" + UseCaseInvoker.class.getSimpleName() + ": ";

    protected int accessCount; // currently for testing purposes

    protected UseCaseRequest<? extends UseCaseRequestModel> useCaseRequest;
    UseCaseCallback<UseCaseResponse<? extends UseCaseResponseModel>> useCaseCallback;
    protected UseCaseInvokerComponentName requestOriginator;

    public <REQUEST extends UseCaseRequest<REQUEST_MODEL>,
            REQUEST_MODEL extends DomainModel.UseCaseRequestModel>
    void execute (REQUEST useCaseRequest,
                  UseCaseCallback<UseCaseResponse<? extends UseCaseResponseModel>> useCaseCallback) {

        accessCount++;
        System.out.println(TAG + "Request No:" + accessCount + " " + useCaseRequest);

        requestOriginator = getUseCaseRequest().getComponentName();

        setUseCaseRequest(useCaseRequest);
        setUseCaseCallback(useCaseCallback);

        run();
    }

    public void setUseCaseRequest(UseCaseRequest<? extends UseCaseRequestModel> useCaseRequest) {
        this.useCaseRequest = useCaseRequest;
    }

    public UseCaseRequest<? extends UseCaseRequestModel> getUseCaseRequest() {
        return useCaseRequest;
    }

    void setUseCaseCallback(UseCaseCallback<UseCaseResponse<? extends UseCaseResponseModel>> useCaseCallback) {
        this.useCaseCallback = useCaseCallback;
    }

    public UseCaseCallback<UseCaseResponse<? extends UseCaseResponseModel>> getUseCaseCallback() {
        return useCaseCallback;
    }

    void run() {
        execute(useCaseRequest);
    }

    protected abstract void execute(UseCaseRequest<? extends UseCaseRequestModel> request);
}
