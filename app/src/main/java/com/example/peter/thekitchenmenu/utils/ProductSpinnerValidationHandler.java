package com.example.peter.thekitchenmenu.utils;

import android.content.Context;
import android.widget.Spinner;

import com.example.peter.thekitchenmenu.databinding.ProductEditorBinding;

public class ProductSpinnerValidationHandler {

    private static final String TAG = "ProductSpinnerValidatio";

    private ProductEditorBinding productEditor;
    private Context context;

    public void setBinding(Context context, ProductEditorBinding productEditor) {
        this.productEditor = productEditor;
        this.context = context;
    }

    public void validateUnitOfMeasure(Spinner unitOfMeasureSpinner, int unitOfMeasureSelected) {
        if (unitOfMeasureSpinner != null) {

        }
    }
}
