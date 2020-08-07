package com.example.peter.thekitchenmenu.domain.businessentity.textvalidation;

import com.example.peter.thekitchenmenu.domain.businessentity.BusinessEntity;
import com.example.peter.thekitchenmenu.domain.businessentity.BusinessEntityResponse;

public class TextValidationBusinessEntity
        extends
        BusinessEntity<TextValidationModel> {

    public static final String TAG = "tkm-" + TextValidationBusinessEntity.class.getSimpleName() +
            ": ";

    private final int shortTextMinLength;
    private final int shortTextMaxLength;
    private final int longTextMinLength;
    private final int longTextMaxLength;

    public TextValidationBusinessEntity(int shortTextMinLength,
                                        int shortTextMaxLength,
                                        int longTextMinLength,
                                        int longTextMaxLength) {
        this.shortTextMinLength = shortTextMinLength;
        this.shortTextMaxLength = shortTextMaxLength;
        this.longTextMinLength = longTextMinLength;
        this.longTextMaxLength = longTextMaxLength;
    }

    @Override
    protected void beginProcessingDomainModel() {
        if (model.getText() == null) {
            failReasons.add(TextValidationFailReason.TEXT_NULL);
            sendResponse();

        } else if (model.getTextLength().equals(TextValidationBusinessEntityTextLength.SHORT_TEXT)) {
            validateShortText(model.getText());
        } else if (model.getTextLength().equals(TextValidationBusinessEntityTextLength.LONG_TEXT)) {
            validateLongText(model.getText());
        } else {
            throw new UnsupportedOperationException("Unknown request type: " + model);
        }
    }

    private void validateShortText(String text) {
        if (model.getText() == null) {
            failReasons.add(TextValidationFailReason.TEXT_NULL);
        } else if (text.length() < shortTextMinLength) {
            failReasons.add(TextValidationFailReason.TEXT_TOO_SHORT);
        } else if (text.length() > shortTextMaxLength) {
            failReasons.add(TextValidationFailReason.TEXT_TOO_LONG);
        }
        sendResponse();
    }

    private void validateLongText(String text) {
        if (text.length() < longTextMinLength) {
            failReasons.add(TextValidationFailReason.TEXT_TOO_SHORT);
        } else if (text.length() > longTextMaxLength) {
            failReasons.add(TextValidationFailReason.TEXT_TOO_LONG);
        }
        sendResponse();
    }

    @Override
    protected void sendResponse() {
        callback.onProcessed(new BusinessEntityResponse<>(request.getModel(), failReasons));
    }
}