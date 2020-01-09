package com.example.peter.thekitchenmenu.domain.usecase.textvalidation;

import com.example.peter.thekitchenmenu.domain.UseCaseCommand;

import java.util.Objects;

import javax.annotation.Nonnull;

public final class TextValidatorResponse implements UseCaseCommand.Response {
    @Nonnull
    private final TextValidator.Result result;
    @Nonnull
    private final TextValidatorModel model;
    @Nonnull
    private String minLength;
    @Nonnull
    private String maxLength;

    public TextValidatorResponse(@Nonnull TextValidator.Result result,
                                 @Nonnull TextValidatorModel model,
                                 @Nonnull String minLength,
                                 @Nonnull String maxLength) {
        this.result = result;
        this.model = model;
        this.minLength = minLength;
        this.maxLength = maxLength;
    }

    @Nonnull
    public TextValidator.Result getResult() {
        return result;
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
        return result == response.result &&
                model.equals(response.model) &&
                minLength.equals(response.minLength) &&
                maxLength.equals(response.maxLength);
    }

    @Override
    public int hashCode() {
        return Objects.hash(result, model, minLength, maxLength);
    }

    @Nonnull
    @Override
    public String toString() {
        return "TextValidatorResponse{" +
                "result=" + result +
                ", model=" + model +
                ", minLength='" + minLength + '\'' +
                ", maxLength='" + maxLength + '\'' +
                '}';
    }

    public static class Builder {
        private TextValidator.Result result;
        private TextValidatorModel model;
        String minLength;
        String maxLength;

        public Builder setResult(TextValidator.Result result) {
            this.result = result;
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
                    result,
                    model,
                    minLength,
                    maxLength
            );
        }
    }
}
