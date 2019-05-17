package com.example.peter.thekitchenmenu.ui.detail.product.editor;

import android.app.Application;
import android.content.res.Resources;
import android.widget.EditText;

import com.example.peter.thekitchenmenu.R;

public class ProductIdentityTextValidationHandler {

    private static final String TAG = "ProductTextValidationHa";

    private enum ValidateTextLength {TOO_SHORT, TOO_LONG, VALIDATED}

    private ProductIdentityViewModel viewModel;
    private Resources resources;

    private int viewId;
    private String newText;

    ProductIdentityTextValidationHandler(Application applicationContext,
                                                ProductIdentityViewModel viewModel) {
        this.viewModel = viewModel;
        resources = applicationContext.getResources();
    }

    // TODO - Introduce a cleanString() method that strips out spaces and escape characters from Editable's.
    // TODO - Refactor: Create a class for each type of validation i.e. length

    public void validateDescription(EditText editableDescription) {

        viewId = editableDescription.getId();
        newText = editableDescription.getText().toString();

        if (fieldHasChanged()) {

            ValidateTextLength result = validateTextLength();
            publishResult(editableDescription, result);
        }
    }

    public void validateMadeBy(EditText editableMadeBy) {

        viewId = editableMadeBy.getId();
        newText = editableMadeBy.getText().toString();

        if (fieldHasChanged()) {

            ValidateTextLength result = validateTextLength();
            publishResult(editableMadeBy, result);
        }
    }

    private boolean fieldHasChanged() {

        if (viewModel.getExistingIdentityModel() != null) {

            switch (viewId) {

                case R.id.editable_description:

                    return !viewModel.getUpdatedIdentityModel().getDescription().equals(newText);

                case R.id.editable_made_by:

                    return !viewModel.getUpdatedIdentityModel().getMadeBy().equals(newText);

                default:
                    return false;
            }
        }
        return false;
    }

    private ValidateTextLength validateTextLength() {

        int minimumLength = resources.getInteger(R.integer.user_input_text_shortest_length);
        int maximumLength = resources.getInteger(R.integer.user_input_text_longest_length);

        if (newText.length() < minimumLength) return ValidateTextLength.TOO_SHORT;

        else if (newText.length() > maximumLength) return ValidateTextLength.TOO_LONG;

        else return ValidateTextLength.VALIDATED;
    }

    private void publishResult(EditText editable, ValidateTextLength result) {

        String lengthTooShort = resources.getString(R.string.validation_text_too_short);
        String lengthTooLong = resources.getString(R.string.validation_text_too_long);

        switch (result) {

            case TOO_SHORT:

                editable.setError(lengthTooShort);
                publishResultToViewModel(result);

                break;

            case TOO_LONG:

                editable.setError(lengthTooLong);
                publishResultToViewModel(result);

                break;

            case VALIDATED:

                publishResultToViewModel(result);

                break;
        }
    }

    private void publishResultToViewModel(ValidateTextLength result) {

        if (viewModel.getExistingIdentityModel() != null) {

            switch (viewId) {

                case R.id.editable_description:

                    viewModel.getUpdatedIdentityModel().setDescription(newText);

                    if (result == ValidateTextLength.VALIDATED)
                        viewModel.setDescriptionValidated(true);

                    else viewModel.setDescriptionValidated(false);

                    break;

                case R.id.editable_made_by:

                    viewModel.getUpdatedIdentityModel().setMadeBy(newText);

                    if (result == ValidateTextLength.VALIDATED)
                        viewModel.setMadeByValidated(true);

                    else viewModel.setMadeByValidated(false);

                    break;
            }
            viewModel.getExistingIdentityModel().setValue(viewModel.getUpdatedIdentityModel());
        }
    }
}
