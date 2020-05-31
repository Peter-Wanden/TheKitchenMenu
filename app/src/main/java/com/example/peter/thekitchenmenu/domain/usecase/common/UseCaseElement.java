package com.example.peter.thekitchenmenu.domain.usecase.common;

import com.example.peter.thekitchenmenu.domain.model.BaseDomainModel;
import com.example.peter.thekitchenmenu.domain.usecase.common.usecasemessage.UseCaseMessageModelDataId;

public abstract class UseCaseElement
        extends
        UseCaseBase {

    private static final String TAG = "tkm-" + "UseCaseElement" + ": ";

    protected static final String NO_ID = "";

    protected String useCaseDataId = NO_ID;
    protected String requestDataId = NO_ID;

    protected String useCaseDomainId = NO_ID;
    protected String requestDomainId = NO_ID;

    protected BaseDomainModel useCaseDomainModel;
    protected BaseDomainModel requestDomainModel;
    int requestNo;

    @Override
    protected <REQUEST extends Request> void execute(REQUEST request) {
        requestNo++;
        UseCaseMessageModelDataId<BaseDomainModel> r =
                (UseCaseMessageModelDataId<BaseDomainModel>) request;

        // data id has priority over domain id
        requestDataId = r.getDataId();
        requestDomainId = r.getDomainId();
        System.out.println(TAG + "requestNo=" + requestNo + " useCaseDataId=" + useCaseDataId +
                " requestDataId=" + requestDataId);
        System.out.println(TAG + "requestNo=" + requestNo + " useCaseDomainId=" + useCaseDomainId +
                " requestDomainId=" + requestDomainId);

        if (requestHasDataId()) {
            System.out.println(TAG + "request has data id");
            if (useCaseHasNoDataId()) {
                System.out.println(TAG + "use case has no data id");
                useCaseDataId = requestDataId;
                loadDataByDataId();

            } else if (useCaseDataId.equals(requestDataId)) {
                System.out.println(TAG + "Data id's are equal");
                processDomainId();
            } else { // data Id has changed, load new data by data id
                System.out.println(TAG + "data ids are not equal");
                useCaseDataId = requestDataId;
                loadDataByDataId();
            }
        } else if (requestHasDomainId()){ // no data id present
            System.out.println(TAG + "request has no data id, request has domainId, processing by " +
                    "domainId");
            processDomainId();
        } else {
            System.out.println(TAG + "request has no data id, request has no domain id, " +
                    "building response");
            buildResponse();
        }
    }

    private void processDomainId() {
        if (requestHasDomainId()) {
            if (useCaseDomainId.equals(requestDomainId)) {
                System.out.println(TAG + "use case domain Id and request domain Id are equal, " +
                        "processing domain model changes");
                processDomainModelChanges();

            } else {
                System.out.println(TAG + "use case domain Id and request domain id are not equal," +
                        " loading data by domain Id");
                this.useCaseDomainId = requestDomainId;
                loadDataByDomainId();
            }
        } else { // has equal data Id's and no domain Id. Can this happen?
            System.out.println("SPECIAL CASE: use case dataId= " + useCaseDataId +
                    " requestDataId=" + requestDataId);
            loadDataByDataId();
        }
    }

    private boolean requestHasDataId() {
        return !NO_ID.equals(requestDataId);
    }

    private boolean useCaseHasNoDataId() {
        return NO_ID.equals(useCaseDataId);
    }

    private boolean requestHasDomainId() {
        return !NO_ID.equals(requestDomainId);
    }

    protected abstract void loadDataByDataId();

    protected abstract void loadDataByDomainId();

    protected abstract void processDomainModelChanges();

    protected abstract void buildResponse();
}
