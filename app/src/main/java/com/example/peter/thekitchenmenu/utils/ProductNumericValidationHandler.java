package com.example.peter.thekitchenmenu.utils;

import android.content.Context;
import android.widget.EditText;

import com.example.peter.thekitchenmenu.R;
import com.example.peter.thekitchenmenu.databinding.ProductEditorBinding;
import com.example.peter.thekitchenmenu.utils.UnitOfMeasure.UnitOfMeasure;
import com.example.peter.thekitchenmenu.utils.UnitOfMeasure.UnitOfMeasureClassSelector;

import static com.example.peter.thekitchenmenu.utils.UnitOfMeasure.UnitOfMeasureConstants.MAX_MASS;
import static com.example.peter.thekitchenmenu.utils.UnitOfMeasure.UnitOfMeasureConstants.NO_INPUT;

public class ProductNumericValidationHandler {

    private static final String TAG = "ProductNumericValidatio";

    private ProductEditorBinding productEditor;
    private Context context;

    public void setBinding(Context context, ProductEditorBinding productEditor) {
        this.productEditor = productEditor;
        this.context = context;
    }

    // TODO - Validate for MAX_MASS in UnitOfMeasureConstants
    public void validatePackSize(EditText editText, int unitOfMeasureSelected) {
        if (productEditor.editablePackSize != null) {

            UnitOfMeasure unitOfMeasure = UnitOfMeasureClassSelector.
                    getUnitOfMeasureClass(context, unitOfMeasureSelected);
            double measurement = NO_INPUT;

            String rawMeasurement = editText.getText().toString();
            if (!rawMeasurement.isEmpty()) measurement = Double.parseDouble(rawMeasurement);

            if (measurement == NO_INPUT) {
                setResultToViewModel((int)measurement);

            } else {
                unitOfMeasure.setMeasurement(measurement);
                int baseSiUnits = unitOfMeasure.getBaseSiUnits();

                if (baseSiUnits <= MAX_MASS) {
                    setResultToViewModel(baseSiUnits);

                } else {
                    String massError = editText.getContext().getString(
                            R.string.max_mass_exceeded_error,
                            unitOfMeasure.getMax(),
                            unitOfMeasure.getUnitAsString());
                    editText.setError(massError);
                }
            }
        }
    }

    private void setResultToViewModel(int packSizeBaseSiUnits) {
        productEditor.getProductEditorViewModel().setPackSize(packSizeBaseSiUnits);
        productEditor.getProductEditorViewModel().setPackSizeValidated(true);
    }
}
