package com.example.peter.thekitchenmenu.domain.usecase.textvalidation;

import com.example.peter.thekitchenmenu.domain.UseCaseInteractor;

import java.util.Objects;

import javax.annotation.Nonnull;

public class TextValidator
        extends UseCaseInteractor<TextValidatorRequest, TextValidatorResponse> {

    private static final String TAG = "tkm-" + TextValidator.class.getSimpleName() + ":";

    public enum RequestType {
        SHORT_TEXT,
        LONG_TEXT
    }

    public enum Result {
        TOO_SHORT,
        TOO_LONG,
        VALID
    }

    private final int defaultShortTextMinLength;
    private final int defaultShortTextMaxLength;
    private final int defaultLongTextMinLength;
    private final int defaultLongTextMaxLength;

    private TextValidator(int defaultShortTextMinLength,
                          int defaultShortTextMaxLength,
                          int defaultLongTextMinLength,
                          int defaultLongTextMaxLength) {
        this.defaultShortTextMinLength = defaultShortTextMinLength;
        this.defaultShortTextMaxLength = defaultShortTextMaxLength;
        this.defaultLongTextMinLength = defaultLongTextMinLength;
        this.defaultLongTextMaxLength = defaultLongTextMaxLength;
    }

    @Override
    protected void execute(TextValidatorRequest request) {
        if (request.getType() == RequestType.SHORT_TEXT) {
            validateShortText(request);
        } else if (request.getType() == RequestType.LONG_TEXT) {
            validateLongText(request);
        } else {
            throw new UnsupportedOperationException("Unknown requestType");
        }
    }

    private void validateShortText(TextValidatorRequest request) {
        String text = request.getModel().getText().trim();
        if (text.length() < defaultShortTextMinLength) {
            sendTextTooShortResponse(request);
        } else if (text.length() > defaultShortTextMaxLength) {
            sendTextTooLongResponse(request);
        } else {
            sendTextValidatedResponse(request);
        }
    }

    private void validateLongText(TextValidatorRequest request) {
        String text = request.getModel().getText().trim();
        if (text.length() < defaultLongTextMinLength) {
            sendTextTooShortResponse(request);
        } else if (text.length() > defaultLongTextMaxLength) {
            sendTextTooLongResponse(request);
        } else {
            sendTextValidatedResponse(request);
        }
    }

    private void sendTextTooShortResponse(TextValidatorRequest request) {
        TextValidatorResponse.Builder responseBuilder = new TextValidatorResponse.Builder().
                setResult(Result.TOO_SHORT).
                setModel(request.getModel());
        responseBuilder = addRequiredLengths(request, responseBuilder);

        getUseCaseCallback().onError(responseBuilder.build());
    }

    private void sendTextTooLongResponse(TextValidatorRequest request) {
        TextValidatorResponse.Builder responseBuilder = new TextValidatorResponse.Builder().
                setResult(Result.TOO_LONG).
                setModel(request.getModel());
        responseBuilder = addRequiredLengths(request, responseBuilder);

        getUseCaseCallback().onError(responseBuilder.build());
    }

    private void sendTextValidatedResponse(TextValidatorRequest request) {
        TextValidatorResponse.Builder responseBuilder = new TextValidatorResponse.Builder().
                setResult(Result.VALID).
                setModel(request.getModel());
        responseBuilder = addRequiredLengths(request, responseBuilder);

        getUseCaseCallback().onSuccess(responseBuilder.build());
    }

    private TextValidatorResponse.Builder addRequiredLengths(TextValidatorRequest request,
                                                             TextValidatorResponse.Builder responseBuilder) {
        if (request.getType() == RequestType.SHORT_TEXT) {
            responseBuilder = addShortTextLengths(responseBuilder);

        } else if (request.getType() == RequestType.LONG_TEXT) {
            responseBuilder = addLongTextLengths(responseBuilder);
        }
        return responseBuilder;
    }

    private TextValidatorResponse.Builder addShortTextLengths(TextValidatorResponse.Builder responseBuilder) {
        return responseBuilder.
                setMinLength(String.valueOf(defaultShortTextMinLength)).
                setMaxLength(String.valueOf(defaultShortTextMaxLength));
    }

    private TextValidatorResponse.Builder addLongTextLengths(TextValidatorResponse.Builder responseBuilder) {
        return responseBuilder.
                setMinLength(String.valueOf(defaultLongTextMinLength)).
                setMaxLength(String.valueOf(defaultLongTextMaxLength));
    }

    public static class Builder {
        private int shortTextMinLength;
        private int shortTextMaxLength;
        private int longTextMinLength;
        private int longTextMaxLength;

        public Builder setShortTextMinLength(int shortTextMinLength) {
            this.shortTextMinLength = shortTextMinLength;
            return this;
        }

        public Builder setShortTextMaxLength(int shortTextMaxLength) {
            this.shortTextMaxLength = shortTextMaxLength;
            return this;
        }

        public Builder setLongTextMinLength(int longTextMinLength) {
            this.longTextMinLength = longTextMinLength;
            return this;
        }

        public Builder setLongTextMaxLength(int longTextMaxLength) {
            this.longTextMaxLength = longTextMaxLength;
            return this;
        }

        public TextValidator build() {
            return new TextValidator(
                    shortTextMinLength,
                    shortTextMaxLength,
                    longTextMinLength,
                    longTextMaxLength
            );
        }
    }
}
