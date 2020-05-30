package com.example.peter.thekitchenmenu.domain.usecase.common;

import com.example.peter.thekitchenmenu.domain.model.BaseDomainModel;
import com.example.peter.thekitchenmenu.domain.usecase.common.usecasemessage.UseCaseMessageModelDataId;

public abstract class UseCaseElement
        extends
        UseCaseBase {

    protected static final String NO_ID = "";
    private String dataId = NO_ID;
    private String domainId = NO_ID;

    @Override
    protected <REQUEST extends Request> void execute(REQUEST request) {
        UseCaseMessageModelDataId<BaseDomainModel> r =
                (UseCaseMessageModelDataId<BaseDomainModel>) request;

        // data id has priority over domain id
        String dataId = r.getDataId();
        if (requestHasDataId(dataId)) {
            if (useCaseHasNoDataId(dataId)) {
                loadDataByDataId(dataId);
            }
        }
    }

    protected boolean requestHasDataId(String dataId) {
        return !NO_ID.equals(dataId);
    }

    protected boolean useCaseHasNoDataId(String domainId) {
        return !NO_ID.equals(domainId);
    }

    protected abstract void loadDataByDataId(String dataId);
}
