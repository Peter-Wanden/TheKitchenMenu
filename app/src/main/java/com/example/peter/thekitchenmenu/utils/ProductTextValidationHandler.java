package com.example.peter.thekitchenmenu.utils;

import android.content.Context;
import android.text.Editable;
import android.util.Log;

import static com.example.peter.thekitchenmenu.data.entity.Product.*;

import com.example.peter.thekitchenmenu.R;
import com.example.peter.thekitchenmenu.databinding.ProductEditorBinding;

import androidx.core.util.Pair;


public class ProductTextValidationHandler {
    private ProductEditorBinding productEditor;
    private Context context;

    public void setBinding(Context context, ProductEditorBinding productEditor) {
        this.productEditor = productEditor;
        this.context = context;
    }

    // TODO - Introduce a cleanString() method that strips out spaces and escape characters from Editable's.

    public void validateDescription(Editable textToValidate) {
        if (productEditor.editableDescription != null) {
            ValidateTextLength validateLengthResult = validateTextLength(textToValidate);
            publishResult(new Pair<>(DESCRIPTION, validateLengthResult));
        }
    }

    public void validateMadeBy(Editable textToValidate) {
        if (productEditor.editableMadeBy != null) {
            ValidateTextLength validateLengthResult = validateTextLength(textToValidate);
            publishResult(new Pair<>(MADE_BY, validateLengthResult));
        }
    }

    // TODO - Refactor: Create a class for each type of validation i.e. length, number ranges etc.
    private ValidateTextLength validateTextLength(Editable textToValidate) {
        int minimumLength = context.getResources().getInteger(R.integer.user_input_text_shortest_length);
        int maximumLength = context.getResources().getInteger(R.integer.user_input_text_longest_length);

        if (textToValidate.length() < minimumLength) {
            return ValidateTextLength.TOO_SHORT;

        } else if (textToValidate.length() > maximumLength) {
            return ValidateTextLength.TOO_LONG;

        } else {
            return ValidateTextLength.VALIDATED;
        }
    }

    private enum ValidateTextLength {
        TOO_SHORT,
        TOO_LONG,
        VALIDATED
    }

    private void publishResult(Pair<String, ValidateTextLength> validationResult) {
        String lengthTooShort = context.getResources().getString(R.string.validation_text_too_short);
        String lengthTooLong = context.getResources().getString(R.string.validation_text_too_long);

        String fieldToVerify = validationResult.first;
        ValidateTextLength textLengthResult = validationResult.second;

        final Boolean fieldIsInvalid = false;
        final Boolean fieldIsValid = true;

        if (fieldToVerify != null && textLengthResult != null) {

            switch (textLengthResult) {
                case TOO_SHORT:
                    if (fieldToVerify.equals(DESCRIPTION)){
                        productEditor.editableDescription.setError(lengthTooShort);
                        setValidationResultToViewModel(new Pair<>(DESCRIPTION, fieldIsInvalid));

                    } else if (fieldToVerify.equals(MADE_BY)) {
                        productEditor.editableMadeBy.setError(lengthTooShort);
                        setValidationResultToViewModel(new Pair<>(MADE_BY, fieldIsInvalid));
                    }
                    break;

                case TOO_LONG:
                    if (fieldToVerify.equals(DESCRIPTION)) {
                        productEditor.editableDescription.setError(lengthTooLong);
                        setValidationResultToViewModel(new Pair<>(MADE_BY, fieldIsInvalid));

                    } else if (fieldToVerify.equals(MADE_BY)) {
                        productEditor.editableMadeBy.setError(lengthTooLong);
                        setValidationResultToViewModel(new Pair<>(MADE_BY, fieldIsInvalid));
                    }
                    break;

                case VALIDATED:
                    if (fieldToVerify.equals(DESCRIPTION)) {
                        setValidationResultToViewModel(new Pair<>(DESCRIPTION, fieldIsValid));

                    } else if (fieldToVerify.equals(MADE_BY)) {
                        setValidationResultToViewModel(new Pair<>(MADE_BY, fieldIsValid));
                    }
                    break;
            }
        }
    }

    private void setValidationResultToViewModel(Pair<String, Boolean> validationResult) {
        productEditor.getProductEditorViewModel().setFieldValidation(validationResult);
    }
}
