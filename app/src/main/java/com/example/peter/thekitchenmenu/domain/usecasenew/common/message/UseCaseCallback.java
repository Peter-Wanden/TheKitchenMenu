package com.example.peter.thekitchenmenu.domain.usecasenew.common.message;

public interface UseCaseCallback<USE_CASE_RESPONSE> {

    void onSuccess(USE_CASE_RESPONSE useCaseResponse);

    void onError(USE_CASE_RESPONSE useCaseResponse);
}
