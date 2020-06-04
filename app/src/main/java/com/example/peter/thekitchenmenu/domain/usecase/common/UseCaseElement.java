package com.example.peter.thekitchenmenu.domain.usecase.common;

import com.example.peter.thekitchenmenu.domain.model.BaseDomainModel;
import com.example.peter.thekitchenmenu.domain.usecase.common.failreasons.CommonFailReason;
import com.example.peter.thekitchenmenu.domain.usecase.common.failreasons.FailReasons;
import com.example.peter.thekitchenmenu.domain.usecase.common.usecasemessage.UseCaseMessageModelDataId;
import com.example.peter.thekitchenmenu.domain.usecase.recipe.component.identity.RecipeIdentityResponse;
import com.example.peter.thekitchenmenu.domain.usecase.recipe.component.metadata.RecipeMetadata;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.example.peter.thekitchenmenu.domain.usecase.common.usecasemessage.UseCaseMessageModelDataId.NO_ID;

public abstract class UseCaseElement
        <REQUEST_DOMAIN_MODEL extends BaseDomainModel, PERSISTENCE_MODEL extends >
        extends
        UseCaseBase {

    private static final String TAG = "tkm-" + "UseCaseElement" + ": ";

    protected String useCaseDataId = NO_ID;
    protected String useCaseDomainId = NO_ID;

    protected REQUEST_DOMAIN_MODEL useCaseDomainModel;
    protected REQUEST_DOMAIN_MODEL requestDomainModel;

    protected List<FailReasons> failReasons = new ArrayList<>();

    protected int accessCount;
    protected boolean isChanged;
    protected boolean isDomainDataUnavailable;

    @Override
    protected <REQUEST extends Request> void execute(REQUEST request) {
        accessCount++;
        UseCaseMessageModelDataId<REQUEST_DOMAIN_MODEL> r =
                (UseCaseMessageModelDataId<REQUEST_DOMAIN_MODEL>) request;

        System.out.println(TAG + "Request No:" + accessCount + " " + request);

        String requestDataId = r.getDataId() == null ? NO_ID : r.getDataId();
        String requestDomainId = r.getDomainId() == null ? NO_ID : r.getDomainId();
        requestDomainModel = (REQUEST_DOMAIN_MODEL) r.getDomainModel();

        boolean requestHasDataId = !NO_ID.equals(requestDataId);
        boolean requestHasDomainId = !NO_ID.equals(requestDomainId);
        boolean requestHasNoIdentifiers = !requestHasDataId && !requestHasDomainId;
        boolean dataIdsAreNotEqual = requestHasDataId &&
                !requestDataId.equals(useCaseDataId);
        boolean domainIdIsAreNotEqual = requestHasDomainId &&
                !requestDomainId.equals(useCaseDomainId);

        if (requestHasNoIdentifiers) {
            reprocessCurrentDomainModel();
        } else {
            useCaseDataId = requestDataId;
            useCaseDomainId = requestDomainId;

            if (dataIdsAreNotEqual) {
                loadDomainModelByDataId();
            } else if (domainIdIsAreNotEqual) {
                loadDomainModelByDomainId();
            } else {
                processRequestDomainModel();
            }
        }
    }

    protected abstract void loadDomainModelByDataId();

    protected abstract void loadDomainModelByDomainId();

    protected abstract void reprocessCurrentDomainModel();

    protected abstract void processRequestDomainModel();

    protected void sendResponse(RecipeIdentityResponse response) {
        System.out.println(TAG + "Response No:" + accessCount + " - " + response);
        if (isDomainModelValid()) {
            getUseCaseCallback().onUseCaseSuccess(response);
        } else {
            getUseCaseCallback().onUseCaseError(response);
        }
    }

    protected boolean isDomainModelValid() {
        FailReasons[] noFailReasons = new FailReasons[]{CommonFailReason.NONE};
        FailReasons[] currentFailReasons = failReasons.toArray(new FailReasons[0]);

        return Arrays.equals(
                noFailReasons,
                currentFailReasons
        );
    }

    protected RecipeMetadata.ComponentState getComponentState() {
        return isDomainModelValid()
                ?
                (isChanged ?
                        RecipeMetadata.ComponentState.VALID_CHANGED :
                        RecipeMetadata.ComponentState.VALID_UNCHANGED)
                :
                (isChanged ?
                        RecipeMetadata.ComponentState.INVALID_CHANGED :
                        RecipeMetadata.ComponentState.INVALID_UNCHANGED);
    }

    protected List<FailReasons> getFailReasons() {
        if (isDomainDataUnavailable) {
            if (!failReasons.contains(CommonFailReason.DATA_UNAVAILABLE)) {
                failReasons.add(CommonFailReason.DATA_UNAVAILABLE);
            }
        }
        return failReasons;
    }
}
