package com.example.peter.thekitchenmenu.utils;

import android.content.Context;
import android.widget.EditText;

import com.example.peter.thekitchenmenu.R;
import com.example.peter.thekitchenmenu.databinding.ProductEditorBinding;
import com.example.peter.thekitchenmenu.utils.UnitOfMeasure.UnitOfMeasure;
import com.example.peter.thekitchenmenu.utils.UnitOfMeasure.UnitOfMeasureClassSelector;

import static com.example.peter.thekitchenmenu.utils.UnitOfMeasure.UnitOfMeasureConstants.*;

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

            unitOfMeasure.setMeasurement(NO_INPUT);

            String rawMeasurement = editText.getText().toString();

            if (rawMeasurement.isEmpty()) {

                setResultToViewModel(unitOfMeasure);

            } else {

                try {

                    unitOfMeasure.setMeasurement(Double.parseDouble(rawMeasurement));

                } catch (NumberFormatException e) {

                    setResultToViewModel(unitOfMeasure);
                }
            }

            if (unitOfMeasure.getBaseSiUnits() <= MAX_MASS) {
                setResultToViewModel(unitOfMeasure);

            } else {
                String massError = editText.getContext().getString(
                        R.string.max_mass_exceeded_error,
                        unitOfMeasure.getMax(),
                        unitOfMeasure.getUnitsAsString());
                editText.setError(massError);
            }
        }
    }

    private void setResultToViewModel(UnitOfMeasure unitOfMeasure) {
        productEditor.getProductEditorViewModel().setPackSize(unitOfMeasure.getBaseSiUnits());
        productEditor.getProductEditorViewModel().setPackSizeValidated(true);
    }
}
