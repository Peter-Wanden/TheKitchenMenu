package com.example.peter.thekitchenmenu.utils;

import android.content.Context;
import android.util.Log;
import android.widget.EditText;

import com.example.peter.thekitchenmenu.R;
import com.example.peter.thekitchenmenu.data.entity.Product;
import com.example.peter.thekitchenmenu.databinding.ProductEditorBinding;
import com.example.peter.thekitchenmenu.utils.unitofmeasure.Measurement;
import com.example.peter.thekitchenmenu.utils.unitofmeasure.UnitOfMeasure;
import com.example.peter.thekitchenmenu.viewmodels.ProductEditorViewModel;

import static com.example.peter.thekitchenmenu.utils.unitofmeasure.UnitOfMeasureConstants.*;

public class ProductNumericValidationHandler {

    private static final String TAG = "ProductNumericValidatio";

    private Context context;
    private ProductEditorBinding productEditor;
    private ProductEditorViewModel viewModel;
    private Product product;
    private UnitOfMeasure unitOfMeasure;

    public void setBinding(Context context, ProductEditorBinding productEditor) {
        this.context = context;
        this.productEditor = productEditor;
        viewModel = productEditor.getProductEditorViewModel();
        product = viewModel.getProduct().getValue();
    }

    public void validateItemsInPack(EditText editableItemsInPack) {

        Log.d(TAG, "validateItemsInPack: called");

        if (productEditor.editableItemsInPack != null) {

            String rawItemsInPack = editableItemsInPack.getText().toString();

            if (!rawItemsInPack.isEmpty()) try {

                int numberOfItemsInPack = Integer.parseInt(rawItemsInPack);

                if (numberOfItemsInPack < MULTI_PACK_MINIMUM_NO_OF_ITEMS ||
                        numberOfItemsInPack > MULTI_PACK_MAXIMUM_NO_OF_ITEMS) {

                    String itemsInPackError =
                            editableItemsInPack.getContext().getResources().getString(
                                    R.string.input_error_items_in_pack,
                                    MULTI_PACK_MINIMUM_NO_OF_ITEMS,
                                    MULTI_PACK_MAXIMUM_NO_OF_ITEMS);

                    productEditor.editableItemsInPack.setError(itemsInPackError);
                }

            } catch (NumberFormatException e) {

                viewModel.getNumberOfItemsInPack().set(0);
            }
        }
    }

    // TODO - Validate for MAX_MASS in UnitOfMeasureConstants
    public void validatePackSize(EditText editableMeasurement) {

        Log.d(TAG, "validatePackSize: called");
        unitOfMeasure = viewModel.getUnitOfMeasure();
        processPackMeasurement(getMeasurement(editableMeasurement), editableMeasurement);
    }

    private int getMeasurement(EditText editableMeasurement) {

        String rawMeasurement = editableMeasurement.getText().toString();

        if (rawMeasurement.isEmpty()) return 0;

        else try {
            return Integer.parseInt(rawMeasurement);

        } catch (NumberFormatException e) {

            return 0;
        }
    }

    private void processPackMeasurement(int packMeasurement, EditText editableMeasurement) {

        int viewId = editableMeasurement.getId();

        if (packMeasurement > 0) {

            boolean measurementIsSet = false;

            if (viewId == R.id.pack_editable_measurement_one)
                measurementIsSet = unitOfMeasure.setPackMeasurementOne(packMeasurement);

             else if (viewId == R.id.pack_editable_measurement_two)
                measurementIsSet = unitOfMeasure.setPackMeasurementTwo(packMeasurement);

            if (measurementIsSet) {

                product.setBaseSiUnits(unitOfMeasure.getBaseSiUnits());
                viewModel.setPackSizeValidated(true);

            } else {

                Measurement measurement = unitOfMeasure.getMinAndMax();

                editableMeasurement.setError(
                        context.getResources().getString(
                                R.string.input_error_pack_size,
                                measurement.getType(),
                                measurement.getMeasurementTwo(),
                                measurement.getMeasurementUnitTwo(),
                                measurement.getMeasurementOne(),
                                measurement.getMeasurementUnitOne()));
            }

        } else {

            if (viewId == R.id.pack_editable_measurement_one)
            viewModel.getPackMeasurementOne().set(packMeasurement);

            else if (viewId == R.id.pack_editable_measurement_two)
                viewModel.getPackMeasurementTwo().set(packMeasurement);
        }
    }
}
