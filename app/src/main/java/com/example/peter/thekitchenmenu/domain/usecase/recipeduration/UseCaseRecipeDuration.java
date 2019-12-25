package com.example.peter.thekitchenmenu.domain.usecase.recipeduration;

import com.example.peter.thekitchenmenu.data.entity.RecipeDurationEntity;
import com.example.peter.thekitchenmenu.data.repository.DataSource;
import com.example.peter.thekitchenmenu.data.repository.RepositoryRecipeDuration;
import com.example.peter.thekitchenmenu.domain.UseCaseInteractor;
import com.example.peter.thekitchenmenu.domain.utils.TimeProvider;

public class UseCaseRecipeDuration
        extends UseCaseInteractor<UseCaseRecipeDuration.Request, UseCaseRecipeDuration.Response>
        implements DataSource.GetEntityCallback<RecipeDurationEntity> {

    public enum Result {
        DATA_UNAVAILABLE,
        INVALID_UNCHANGED,
        VALID_UNCHANGED,
        INVALID_CHANGED,
        VALID_CHANGED,
    }

    private RepositoryRecipeDuration repository;
    private TimeProvider timeProvider;

    public UseCaseRecipeDuration(RepositoryRecipeDuration repository, TimeProvider timeProvider) {
        this.repository = repository;
        this.timeProvider = timeProvider;
    }

    @Override
    protected void execute(Request request) {

    }

    @Override
    public void onEntityLoaded(RecipeDurationEntity object) {

    }

    @Override
    public void onDataNotAvailable() {

    }

    public static final class Model {

    }

    public static final class Request implements UseCaseInteractor.Request {

    }

    public static final class Response implements UseCaseInteractor.Response {

    }
}
