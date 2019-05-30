package com.example.peter.thekitchenmenu.ui.catalog;

import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.peter.thekitchenmenu.R;

import com.example.peter.thekitchenmenu.data.model.ProductModel;
import com.example.peter.thekitchenmenu.databinding.FragmentCatalogProductsBinding;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class ProductCatalogUsersProducts
        extends Fragment
        implements OnClickProduct {

    private static final String TAG = "ProductCatalogUsersProducts";

    private ProductCatalogRecyclerAdapter catalogProductsAdapter;
    private ViewModelCatalogProducts viewModel;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        catalogProductsAdapter = new ProductCatalogRecyclerAdapter(requireActivity(), this);

        viewModel = ViewModelProviders.of(requireActivity()).get(ViewModelCatalogProducts.class);

        // Observes changes to view model ProdMy list and passes them to the adaptor.
        final Observer<List<ProductModel>> viewModelProd = vmProds
                -> catalogProductsAdapter.setProducts(vmProds);

        viewModel.getAllVmProdMy().observe(this, viewModelProd);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        FragmentCatalogProductsBinding mBinding = DataBindingUtil.inflate(
                inflater, R.layout.fragment_catalog_products, container, false);

        if (getResources().getBoolean(R.bool.is_tablet) ||
                getResources().getBoolean(R.bool.is_landscape)) {

            GridLayoutManager gridManager = new
                    GridLayoutManager(requireActivity().
                    getApplicationContext(), columnCalculator());

            mBinding.fragmentCatalogProductsRv.
                    setLayoutManager(gridManager);
        } else {
            LinearLayoutManager linearManager = new
                    LinearLayoutManager(requireActivity().getApplicationContext(),
                    RecyclerView.VERTICAL, false);

            mBinding.
                    fragmentCatalogProductsRv.
                    setLayoutManager(linearManager);
        }

        mBinding.fragmentCatalogProductsRv.setHasFixedSize(true);

        mBinding.fragmentCatalogProductsRv.setAdapter(catalogProductsAdapter);

        return mBinding.getRoot();
    }

    /**
     * Screen width column calculator
     */
    private int columnCalculator() {

        DisplayMetrics metrics = new DisplayMetrics();
        requireActivity().getWindowManager().getDefaultDisplay().getMetrics(metrics);

        // Width of smallest tablet
        int divider = 600;
        int width = metrics.widthPixels;
        int columns = width / divider;
        if (columns < 2) return 2;

        return columns;
    }

    @Override
    public void onClick(ProductModel productModel, boolean isCreator) {
        viewModel.selectedItem(productModel, isCreator);
    }
}
