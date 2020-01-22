package com.example.peter.thekitchenmenu.domain.usecase.textvalidation;

import com.example.peter.thekitchenmenu.domain.usecase.FailReasons;
import com.example.peter.thekitchenmenu.domain.usecase.UseCase;

public class TextValidator extends UseCase<TextValidatorRequest, TextValidatorResponse> {

    private static final String TAG = "tkm-" + TextValidator.class.getSimpleName() + ": ";

    public enum TextType {
        SHORT_TEXT,
        LONG_TEXT
    }

    public enum FailReason implements FailReasons {
        TOO_SHORT,
        TOO_LONG,
        NONE
    }

    private final int shortTextMinLength;
    private final int shortTextMaxLength;
    private final int longTextMinLength;
    private final int longTextMaxLength;

    private TextValidator(int shortTextMinLength,
                          int shortTextMaxLength,
                          int longTextMinLength,
                          int longTextMaxLength) {
        this.shortTextMinLength = shortTextMinLength;
        this.shortTextMaxLength = shortTextMaxLength;
        this.longTextMinLength = longTextMinLength;
        this.longTextMaxLength = longTextMaxLength;
    }

    @Override
    protected void execute(TextValidatorRequest request) {
        System.out.println(TAG + request);
        if (request.getType() == TextType.SHORT_TEXT) {
            validateShortText(request);
        } else if (request.getType() == TextType.LONG_TEXT) {
            validateLongText(request);
        } else {
            throw new UnsupportedOperationException("Unknown requestType");
        }
    }

    private void validateShortText(TextValidatorRequest request) {
        String text = request.getModel().getText().trim();
        if (text.length() < shortTextMinLength) {
            sendTextTooShortResponse(request);
        } else if (text.length() > shortTextMaxLength) {
            sendTextTooLongResponse(request);
        } else {
            sendTextValidatedResponse(request);
        }
    }

    private void validateLongText(TextValidatorRequest request) {
        String text = request.getModel().getText().trim();
        if (text.length() < longTextMinLength) {
            sendTextTooShortResponse(request);
        } else if (text.length() > longTextMaxLength) {
            sendTextTooLongResponse(request);
        } else {
            sendTextValidatedResponse(request);
        }
    }

    private void sendTextTooShortResponse(TextValidatorRequest request) {
        TextValidatorResponse.Builder responseBuilder = new TextValidatorResponse.Builder().
                setFailReason(FailReason.TOO_SHORT).
                setModel(request.getModel());
        responseBuilder = addRequiredLengths(request, responseBuilder);

        TextValidatorResponse response = responseBuilder.build();
        System.out.println(TAG + response);
        getUseCaseCallback().onError(response);
    }

    private void sendTextTooLongResponse(TextValidatorRequest request) {
        TextValidatorResponse.Builder responseBuilder = new TextValidatorResponse.Builder().
                setFailReason(FailReason.TOO_LONG).
                setModel(request.getModel());
        responseBuilder = addRequiredLengths(request, responseBuilder);

        TextValidatorResponse response = responseBuilder.build();
        System.out.println(TAG + response);
        getUseCaseCallback().onError(response);
    }

    private void sendTextValidatedResponse(TextValidatorRequest request) {
        TextValidatorResponse.Builder responseBuilder = new TextValidatorResponse.Builder().
                setFailReason(FailReason.NONE).
                setModel(request.getModel());
        responseBuilder = addRequiredLengths(request, responseBuilder);

        TextValidatorResponse response = responseBuilder.build();
        System.out.println(TAG + response);
        getUseCaseCallback().onSuccess(response);
    }

    private TextValidatorResponse.Builder addRequiredLengths(TextValidatorRequest request,
                                                             TextValidatorResponse.Builder responseBuilder) {
        if (request.getType() == TextType.SHORT_TEXT) {
            responseBuilder = addShortTextLengths(responseBuilder);

        } else if (request.getType() == TextType.LONG_TEXT) {
            responseBuilder = addLongTextLengths(responseBuilder);
        }
        return responseBuilder;
    }

    private TextValidatorResponse.Builder addShortTextLengths(TextValidatorResponse.Builder responseBuilder) {
        return responseBuilder.
                setMinLength(String.valueOf(shortTextMinLength)).
                setMaxLength(String.valueOf(shortTextMaxLength));
    }

    private TextValidatorResponse.Builder addLongTextLengths(TextValidatorResponse.Builder responseBuilder) {
        return responseBuilder.
                setMinLength(String.valueOf(longTextMinLength)).
                setMaxLength(String.valueOf(longTextMaxLength));
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
