package com.example.peter.thekitchenmenu.utils.unitofmeasure;

import com.example.peter.thekitchenmenu.ui.detail.product.editor.ProductMeasurementHandler;
import com.example.peter.thekitchenmenu.ui.detail.product.editor.ProductMeasurementViewModel;

import org.junit.Test;

import static androidx.test.core.app.ApplicationProvider.getApplicationContext;

public class ProductMeasurementHandlerTest {

    ProductMeasurementViewModel viewModel = new
            ProductMeasurementViewModel(getApplicationContext());

    ProductMeasurementHandler measurementHandler = new
            ProductMeasurementHandler(viewModel);

    @Test
    public void testNumberOfItemsUpdated() {

    }

}