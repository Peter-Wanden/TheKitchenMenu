package com.example.peter.thekitchenmenu.domain.usecase.textvalidation;


import com.example.peter.thekitchenmenu.domain.usecase.UseCase;

import java.util.Objects;

import javax.annotation.Nonnull;

public final class TextValidatorResponse implements UseCase.Response {
    @Nonnull
    private final TextValidator.FailReason failReason;
    @Nonnull
    private final TextValidatorModel model;
    @Nonnull
    private String minLength;
    @Nonnull
    private String maxLength;

    public TextValidatorResponse(@Nonnull TextValidator.FailReason failReason,
                                 @Nonnull TextValidatorModel model,
                                 @Nonnull String minLength,
                                 @Nonnull String maxLength) {
        this.failReason = failReason;
        this.model = model;
        this.minLength = minLength;
        this.maxLength = maxLength;
    }

    @Nonnull
    public TextValidator.FailReason getFailReason() {
        return failReason;
    }

    @Nonnull
    public TextValidatorModel getModel() {
        return model;
    }

    @Nonnull
    public String getMinLength() {
        return minLength;
    }

    @Nonnull
    public String getMaxLength() {
        return maxLength;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TextValidatorResponse response = (TextValidatorResponse) o;
        return failReason == response.failReason &&
                model.equals(response.model) &&
                minLength.equals(response.minLength) &&
                maxLength.equals(response.maxLength);
    }

    @Override
    public int hashCode() {
        return Objects.hash(failReason, model, minLength, maxLength);
    }

    @Nonnull
    @Override
    public String toString() {
        return "TextValidatorResponse{" +
                "failReason=" + failReason +
                ", model=" + model +
                ", minLength='" + minLength + '\'' +
                ", maxLength='" + maxLength + '\'' +
                '}';
    }

    public static class Builder {
        private TextValidator.FailReason failReason;
        private TextValidatorModel model;
        String minLength;
        String maxLength;

        public Builder setFailReason(TextValidator.FailReason failReason) {
            this.failReason = failReason;
            return this;
        }

        public Builder setModel(TextValidatorModel model) {
            this.model = model;
            return this;
        }

        public Builder setMinLength(String minLength) {
            this.minLength = minLength;
            return this;
        }

        public Builder setMaxLength(String maxLength) {
            this.maxLength = maxLength;
            return this;
        }

        public TextValidatorResponse build() {
            return new TextValidatorResponse(
                    failReason,
                    model,
                    minLength,
                    maxLength
            );
        }
    }
}
