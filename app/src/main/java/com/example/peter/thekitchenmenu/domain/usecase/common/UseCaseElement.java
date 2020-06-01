package com.example.peter.thekitchenmenu.domain.usecase.common;

import com.example.peter.thekitchenmenu.domain.model.BaseDomainModel;
import com.example.peter.thekitchenmenu.domain.usecase.common.usecasemessage.UseCaseMessageModelDataId;

public abstract class UseCaseElement<
        REQUEST_DOMAIN_MODEL extends BaseDomainModel>
        extends
        UseCaseBase {

    private static final String TAG = "tkm-" + "UseCaseElement" + ": ";

    private static final String NO_ID = "";

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

        System.out.println(TAG + "requestNo=" + accessCount + " " + request);

        requestDataId = r.getDataId();
        requestDomainId = r.getDomainId();
        requestDomainModel = (REQUEST_DOMAIN_MODEL) r.getDomainModel();

        if (requestHasDataId()) { // data id has priority over domain id
            if (useCaseDataId.equals(requestDataId)) {
                processDomainId();
            } else {
                useCaseDataId = requestDataId;
                useCaseDomainId = requestDomainId;
                isNewRequest = true;
                loadDataByDataId();
            }
        } else if (requestHasDomainId()) {
            processDomainId();
        } else {
            // request with no data or domain id indicates requester is requesting current state
            buildResponse();
        }
    }

    private void processDomainId() {
        if (requestHasDomainId()) {
            if (useCaseDomainId.equals(requestDomainId)) {
                isNewRequest = false;
                processDomainModel();
            } else {
                this.useCaseDomainId = requestDomainId;
                isNewRequest = true;
                loadDataByDomainId();
            }
        } else {
            if (useCaseHasDomainId()) {
                isNewRequest = false;
                processDomainModel();
            } else {
                isNewRequest = true;
                loadDataByDataId();
            }
        }
    }

    private boolean requestHasDataId() {
        return !NO_ID.equals(requestDataId);
    }

    private boolean requestHasDomainId() {
        return !NO_ID.equals(requestDomainId);
    }

    private boolean useCaseHasDomainId() {
        return !NO_ID.equals(useCaseDomainId);
    }

    protected abstract void loadDataByDataId();

    protected abstract void loadDataByDomainId();

    protected abstract void processDomainModel();

    protected abstract boolean isDomainModelChanged();

    protected abstract void buildResponse();
}
