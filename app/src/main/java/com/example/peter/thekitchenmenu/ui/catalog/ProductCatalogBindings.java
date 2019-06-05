package com.example.peter.thekitchenmenu.ui.catalog;

import androidx.databinding.BindingAdapter;
import androidx.lifecycle.LiveData;
import androidx.recyclerview.widget.RecyclerView;

import com.example.peter.thekitchenmenu.data.entity.ProductEntity;

import java.util.List;

/**
 * Contains {@link BindingAdapter}s for the {@link ProductEntity} list.
 */
public class ProductCatalogBindings {

    @SuppressWarnings("unchecked")
    @BindingAdapter("app:productList")
    public static void setProducts(RecyclerView recyclerView, List<ProductEntity> products) {

        CatalogRecyclerAdapter adapter = (CatalogRecyclerAdapter) recyclerView.getAdapter();
        if (adapter != null) adapter.setProducts(products);
    }
}
