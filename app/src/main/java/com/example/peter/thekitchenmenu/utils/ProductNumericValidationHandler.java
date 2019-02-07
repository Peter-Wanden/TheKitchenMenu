package com.example.peter.thekitchenmenu.utils;

import android.content.Context;
import android.util.Log;
import android.widget.EditText;

import com.example.peter.thekitchenmenu.databinding.ProductEditorBinding;

public class ProductNumericValidationHandler {

    private static final String TAG = "ProductNumericValidatio";

    private ProductEditorBinding productEditor;
    private Context context;

    public void setBinding(Context context, ProductEditorBinding productEditor) {
        this.productEditor = productEditor;
        this.context = context;
    }

    public void validatePackSize(EditText editText, int unitOfMeasure) {
        if (productEditor.editablePackSize != null) {
            Log.d(TAG, "validatePackSize: pack size is: " + editText.getText().toString());
            Log.d(TAG, "validatePackSize: old unit of measure is: " + unitOfMeasure);
        }
    }

    private void setResultToViewModel(int packSizeAsBaseUnits) {
        productEditor.getProductEditorViewModel().setPackSizeInt(packSizeAsBaseUnits);
    }
}
