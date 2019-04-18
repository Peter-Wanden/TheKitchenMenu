package com.example.peter.thekitchenmenu.utils.unitofmeasure;

import com.example.peter.thekitchenmenu.utils.ProductMeasurementHandler;
import com.example.peter.thekitchenmenu.viewmodels.ProductMeasurementViewModel;

import org.junit.Test;

import static androidx.test.core.app.ApplicationProvider.getApplicationContext;
import static org.junit.Assert.*;

public class ProductMeasurementHandlerTest {

    ProductMeasurementViewModel viewModel = new
            ProductMeasurementViewModel(getApplicationContext());

    ProductMeasurementHandler measurementHandler = new
            ProductMeasurementHandler(getApplicationContext(), viewModel);

    @Test
    public void testNumberOfItemsUpdated() {

    }

}