package com.example.peter.thekitchenmenu.ui.detail;

import android.content.Context;
import android.util.Log;
import android.widget.Spinner;

import com.example.peter.thekitchenmenu.databinding.ProductEditorBinding;

public class SpinnerOnItemSelected {

    private static final String TAG = "SpinnerOnItemSelected";

    private ProductEditorBinding productEditor;
    private Context context;

    void setBinding(Context context, ProductEditorBinding binding) {
        this.context = context;
        this.productEditor = binding;
    }

    public void setSelection(Spinner spinner, int position) {
        Log.d(TAG, "setSelection: spinner is: " + spinner.getId());
        Log.d(TAG, "setSelection: selection is: " + position);
        productEditor.getProductEditorViewModel().printProduct();
    }
}
