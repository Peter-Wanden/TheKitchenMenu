package com.example.peter.thekitchenmenu.utils;

import android.app.Application;
import android.content.res.Resources;
import android.widget.EditText;

import com.example.peter.thekitchenmenu.R;
import com.example.peter.thekitchenmenu.viewmodels.ProductIdentityViewModel;

public class ProductTextValidationHandler {

    private static final String TAG = "ProductTextValidationHa";

    private enum ValidateTextLength {TOO_SHORT, TOO_LONG, VALIDATED}

    private ProductIdentityViewModel viewModel;
    private Resources resources;

    private int viewId;
    private String newText;

    public ProductTextValidationHandler(Application applicationContext,
                                        ProductIdentityViewModel viewModel) {
        this.viewModel = viewModel;
        resources = applicationContext.getResources();
    }

    // TODO - Introduce a cleanString() method that strips out spaces and escape characters from Editable's.
    // TODO - Refactor: Create a class for each type of validation i.e. length, number ranges etc.
    // TODO - Make general classes for text validation so as to not duplicate this class each time text validation is required

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

        if (viewModel.getIdentityModel() != null) {

            switch (viewId) {

                case R.id.editable_description:

                    return !viewModel.getIdentityModel().getDescription().equals(newText);

                case R.id.editable_made_by:

                    return !viewModel.getIdentityModel().getMadeBy().equals(newText);

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
                viewModel.setDescriptionValidated(false);

                break;

            case TOO_LONG:

                editable.setError(lengthTooLong);
                viewModel.setDescriptionValidated(false);

                break;

            case VALIDATED:

                publishResultToViewModel();

                break;
        }
    }

    private void publishResultToViewModel() {

        if (viewModel.getIdentityModel() != null) {

            switch (viewId) {

                case R.id.editable_description:

                    viewModel.getIdentityModel().setDescription(newText);
                    viewModel.setDescriptionValidated(true);

                    break;

                case R.id.editable_made_by:

                    viewModel.getIdentityModel().setMadeBy(newText);
                    viewModel.setMadeByValidated(true);

                    break;
            }
        }
    }
}
