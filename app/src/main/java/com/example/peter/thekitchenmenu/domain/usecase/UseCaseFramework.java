package com.example.peter.thekitchenmenu.domain.usecase;


// An extraction of commonalities within use cases
public abstract class UseCaseFramework extends UseCaseBase {

    @Override
    protected <REQUEST extends Request> void execute(REQUEST request) {

    }
}
