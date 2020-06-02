package com.example.peter.thekitchenmenu.domain.usecase.common;

import com.example.peter.thekitchenmenu.domain.model.BaseDomainModel;
import com.example.peter.thekitchenmenu.domain.usecase.common.usecasemessage.UseCaseMessageModelDataId;

import static com.example.peter.thekitchenmenu.domain.usecase.common.usecasemessage.UseCaseMessageModelDataId.NO_ID;

public abstract class UseCaseElement<REQUEST_DOMAIN_MODEL extends BaseDomainModel>
        extends
        UseCaseBase {

    private static final String TAG = "tkm-" + "UseCaseElement" + ": ";

    protected String useCaseDataId = NO_ID;
    protected String useCaseDomainId = NO_ID;

    protected String requestDataId = NO_ID;
    protected String requestDomainId = NO_ID;

    protected REQUEST_DOMAIN_MODEL useCaseDomainModel;
    protected REQUEST_DOMAIN_MODEL requestDomainModel;
    protected boolean isNewRequest;
    protected int accessCount;

    @Override
    protected <REQUEST extends Request> void execute(REQUEST request) {
        accessCount++;
        UseCaseMessageModelDataId<REQUEST_DOMAIN_MODEL> r =
                (UseCaseMessageModelDataId<REQUEST_DOMAIN_MODEL>) request;

        System.out.println(TAG + "Request No:" + accessCount + " " + request);

        requestDataId = r.getDataId() == null ? NO_ID : r.getDataId();
        requestDomainId = r.getDomainId() == null ? NO_ID : r.getDomainId();
        requestDomainModel = (REQUEST_DOMAIN_MODEL) r.getDomainModel();

        if (NO_ID.equals(requestDataId) && NO_ID.equals(requestDomainId)) {
            processUseCaseDomainData();
        } else if (!NO_ID.equals(requestDataId) && !requestDataId.equals(useCaseDataId)) {
            useCaseDataId = requestDataId;
            useCaseDomainId = requestDomainId;
            loadDataByDataId();
        } else if (!NO_ID.equals(requestDomainId) && !requestDomainId.equals(useCaseDomainId)) {
            useCaseDomainId = requestDomainId;
            loadDataByDomainId();
        } else {
            processRequestDomainData();
        }
    }

    protected abstract void loadDataByDataId();

    protected abstract void loadDataByDomainId();

    protected abstract void processUseCaseDomainData();

    protected abstract void processRequestDomainData();

    protected abstract boolean isDomainDataChanged();
}
