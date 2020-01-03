package com.example.peter.thekitchenmenu.domain.usecase.recipeduration;

import com.example.peter.thekitchenmenu.domain.UseCaseInteractor;

import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

import javax.annotation.Nonnull;

public class RecipeDurationResponse implements UseCaseInteractor.Response {
    @Nonnull
    private final RecipeDuration.Result result;
    @Nonnull
    private final List<RecipeDuration.FailReason> failReasons;
    @Nonnull
    private final RecipeDurationModel model;

    public RecipeDurationResponse(@Nonnull RecipeDuration.Result result,
                                  @Nonnull List<RecipeDuration.FailReason> failReasons,
                                  @Nonnull RecipeDurationModel model) {
        this.result = result;
        this.failReasons = failReasons;
        this.model = model;
    }

    @Nonnull
    public RecipeDuration.Result getResult() {
        return result;
    }

    @Nonnull
    public List<RecipeDuration.FailReason> getFailReasons() {
        return failReasons;
    }

    @Nonnull
    public RecipeDurationModel getModel() {
        return model;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RecipeDurationResponse response = (RecipeDurationResponse) o;
        return result == response.result &&
                failReasons.equals(response.failReasons) &&
                model.equals(response.model);
    }

    @Override
    public int hashCode() {
        return Objects.hash(result, failReasons, model);
    }

    @Nonnull
    @Override
    public String toString() {
        return "RecipeDurationResponse{" +
                "result=" + result +
                ", failReasons=" + failReasons +
                ", model=" + model +
                '}';
    }

    public static class Builder {
        private RecipeDuration.Result result;
        private List<RecipeDuration.FailReason> failReasons;
        private RecipeDurationModel model;

        public static Builder getDefault() {
            return new Builder().
                    setResult(RecipeDuration.Result.INVALID_UNCHANGED).
                    setFailReasons(getDefaultFailReasons()).
                    setModel(RecipeDurationModel.Builder.
                            getDefault().
                            build());
        }

        public Builder setResult(RecipeDuration.Result result) {
            this.result = result;
            return this;
        }

        public Builder setFailReasons(List<RecipeDuration.FailReason> failReasons) {
            this.failReasons = failReasons;
            return this;
        }

        public Builder setModel(RecipeDurationModel model) {
            this.model = model;
            return this;
        }

        public RecipeDurationResponse build() {
            return new RecipeDurationResponse(
                    result,
                    failReasons,
                    model
            );
        }

        private static List<RecipeDuration.FailReason> getDefaultFailReasons() {
            List<RecipeDuration.FailReason> defaultFailReasons = new LinkedList<>();
            defaultFailReasons.add(RecipeDuration.FailReason.NONE);
            return defaultFailReasons;
        }
    }
}