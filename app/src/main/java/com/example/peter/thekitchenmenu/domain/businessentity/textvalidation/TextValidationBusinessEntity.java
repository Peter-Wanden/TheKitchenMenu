package com.example.peter.thekitchenmenu.domain.businessentity.textvalidation;

import android.annotation.SuppressLint;

import com.example.peter.thekitchenmenu.domain.businessentity.BusinessEntity;
import com.example.peter.thekitchenmenu.domain.usecase.common.failreasons.FailReasons;

import java.util.HashMap;
import java.util.Map;

public class TextValidationBusinessEntity
        extends
        BusinessEntity<TextValidationEntityModel> {

    public static final String TAG = "tkm-" + TextValidationBusinessEntity.class.getSimpleName() +
            ": ";

    public enum TextLength {
        SHORT_TEXT,
        LONG_TEXT
    }

    public enum FailReason implements FailReasons {
        TOO_SHORT(500),
        TOO_LONG(501),
        NULL_TEXT(502);

        private final int id;

        @SuppressLint("UseSparseArrays")
        private static Map<Integer, FailReason> options = new HashMap<>();

        FailReason(int id) {
            this.id = id;
        }

        static {
            for (FailReason fr : FailReason.values()) {
                options.put(fr.id, fr);
            }
        }

        public static FailReason getById(int id) {
            return options.get(id);
        }


        @Override
        public int getId() {
            return id;
        }
    }

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
    protected void processElements() {
        if (model.getText() == null) {
            failReasons.add(FailReason.NULL_TEXT);
            sendResponse();
        } else if (model.getTextLength().equals(TextLength.SHORT_TEXT)) {
            validateShortText(model.getText());
        } else if (model.getTextLength().equals(TextLength.LONG_TEXT)) {
            validateLongText(model.getText());
        } else {
            throw new UnsupportedOperationException("Unknown request type: " + model);
        }
    }

    private void validateShortText(String text) {
        if (model.getText() == null) {
            failReasons.add(FailReason.NULL_TEXT);
        } else if (text.length() < shortTextMinLength) {
            failReasons.add(FailReason.TOO_SHORT);
        } else if (text.length() > shortTextMaxLength) {
            failReasons.add(FailReason.TOO_LONG);
        }
        sendResponse();
    }

    private void validateLongText(String text) {
        if (text.length() < longTextMinLength) {
            failReasons.add(FailReason.TOO_SHORT);
        } else if (text.length() > longTextMaxLength) {
            failReasons.add(FailReason.TOO_LONG);
        }
        sendResponse();
    }

    @Override
    protected void sendResponse() {
        callback.onProcessed(new Response<>(request.getModel(), failReasons));
    }
}