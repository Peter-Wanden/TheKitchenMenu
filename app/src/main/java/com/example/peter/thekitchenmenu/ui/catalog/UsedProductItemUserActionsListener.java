package com.example.peter.thekitchenmenu.ui.catalog;

import com.example.peter.thekitchenmenu.data.model.UsedProductDataModel;

/**
 * Listener used with data binding to process user actions on a used product list item in a catalog
 */
public interface UsedProductItemUserActionsListener {

    void onUsedProductClicked(UsedProductDataModel usedProduct);
}
