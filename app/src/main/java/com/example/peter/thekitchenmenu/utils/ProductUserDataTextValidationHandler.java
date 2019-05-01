package com.example.peter.thekitchenmenu.utils;

import android.app.Application;
import android.content.res.Resources;
import android.widget.EditText;

import com.example.peter.thekitchenmenu.R;
import com.example.peter.thekitchenmenu.viewmodels.ProductUserDataEditorViewModel;

public class ProductUserDataTextValidationHandler {

    private static final String TAG = "ProductTextValidationHa";

    private enum ValidateTextLength {TOO_SHORT, TOO_LONG, VALIDATED}

    private ProductUserDataEditorViewModel viewModel;
    private Resources resources;

    private int viewId;
    private String newText;

    public ProductUserDataTextValidationHandler(Application applicationContext,
                                                ProductUserDataEditorViewModel viewModel) {

        this.viewModel = viewModel;
        resources = applicationContext.getResources();
    }

    // TODO - Introduce a cleanString() method that strips out spaces and escape characters from Editable's.
    // TODO - Refactor: Create a class for each type of validation i.e. length

    public void validateRetailer(EditText editableRetailer) {

        viewId = editableRetailer.getId();
        newText = editableRetailer.getText().toString();

        if (fieldHasChanged()) {

            ValidateTextLength result = validateTextLength();
            publishResult(editableRetailer, result);
        }
    }

    public void validateLocationRoom(EditText editableLocationRoom) {

        viewId = editableLocationRoom.getId();
        newText = editableLocationRoom.getText().toString();

        if (fieldHasChanged()) {

            ValidateTextLength result = validateTextLength();
            publishResult(editableLocationRoom, result);
        }
    }

    public void validateLocationInRoom(EditText editableLocationInRoom) {

        viewId = editableLocationInRoom.getId();
        newText = editableLocationInRoom.getText().toString();

        if (fieldHasChanged()) {

            ValidateTextLength result = validateTextLength();
            publishResult(editableLocationInRoom, result);
        }
    }

    private boolean fieldHasChanged() {

        if (viewModel.getUserDataModel() != null) {

            switch (viewId) {

                case R.id.editable_retailer:

                    return !viewModel.getUserDataModel().getRetailer().equals(newText);

                case R.id.editable_location_room:

                    return !viewModel.getUserDataModel().getLocationRoom().equals(newText);

                case R.id.editable_location_in_room:

                    return !viewModel.getUserDataModel().getLocationInRoom().equals(newText);

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

        if (viewModel.getUserDataModel() != null) {

            switch (viewId) {

                case R.id.editable_retailer:

                    viewModel.getUserDataModel().setRetailer(newText);

                    if (result == ValidateTextLength.VALIDATED)
                        viewModel.setRetailerValidated(true);

                    else viewModel.setRetailerValidated(false);

                    break;

                case R.id.location_room:

                    viewModel.getUserDataModel().setLocationRoom(newText);

                    if (result == ValidateTextLength.VALIDATED)
                        viewModel.setLocationRoomValidated(true);

                    else viewModel.setLocationRoomValidated(false);

                    break;

                case R.id.location_in_room:

                    viewModel.getUserDataModel().setLocationInRoom(newText);

                    if (result == ValidateTextLength.VALIDATED)
                        viewModel.setLocationInRoomValidated(true);

                    else viewModel.setLocationInRoomValidated(false);
            }
        }
    }
}
