package com.example.peter.thekitchenmenu.domain.usecase;


import com.example.peter.thekitchenmenu.domain.usecase.common.UseCaseBase;

// An abstraction of commonalities within use cases
public abstract class UseCaseFramework extends UseCaseBase {

    @Override
    protected <REQUEST extends Request> void execute(REQUEST request) {

    }
}
