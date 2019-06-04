package com.example.peter.thekitchenmenu.ui.catalog;

import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.peter.thekitchenmenu.R;

import com.example.peter.thekitchenmenu.data.entity.ProductEntity;
import com.example.peter.thekitchenmenu.databinding.FragmentCatalogProductsBinding;
import com.example.peter.thekitchenmenu.ui.ViewModelHolder;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import static com.example.peter.thekitchenmenu.ui.catalog.CatalogActivity.PRODUCT_CATALOG_VIEW_MODEL_TAG;

public class CatalogAllFragment
        extends Fragment
        implements OnClickProduct {

    // TODO - make a super class, for FragmentCommunityProducts and FragmentMyProducts to inherit.
    private static final String TAG = "tkm-CatalogAllFragment";

    private CatalogRecyclerAdapter adapter;
    private CatalogProductsViewModel viewModel;

    public CatalogAllFragment(){}

    public static CatalogAllFragment newInstance() {
        return new CatalogAllFragment();
    }

    @Override
    public void onResume() {
        super.onResume();
        viewModel.loadAllProducts();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        adapter = new CatalogRecyclerAdapter(requireActivity(), this);
        viewModel = getViewModel();
    }

    private CatalogProductsViewModel getViewModel() {
        @SuppressWarnings("unchecked")
        ViewModelHolder<CatalogProductsViewModel> retainedViewModel =
                (ViewModelHolder<CatalogProductsViewModel>)
                        requireActivity().getSupportFragmentManager().
                        findFragmentByTag(PRODUCT_CATALOG_VIEW_MODEL_TAG);

        if (retainedViewModel != null && retainedViewModel.getViewModel() != null)
            return retainedViewModel.getViewModel();
        else return null;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        FragmentCatalogProductsBinding binding = DataBindingUtil.inflate(
                inflater, R.layout.fragment_catalog_products, container, false);


        if (getResources().getBoolean(R.bool.is_tablet) ||
                getResources().getBoolean(R.bool.is_landscape)) {

            GridLayoutManager gridManager = new GridLayoutManager((requireActivity())
                            .getApplicationContext(), columnCalculator());

            binding.fragmentCatalogProductsRv.setLayoutManager(gridManager);

        } else {
            LinearLayoutManager linearManager = new LinearLayoutManager(requireActivity()
                    .getApplicationContext(), RecyclerView.VERTICAL, false);

            binding.fragmentCatalogProductsRv.setLayoutManager(linearManager);
        }

        binding.fragmentCatalogProductsRv.setHasFixedSize(true);
        binding.fragmentCatalogProductsRv.setAdapter(adapter);

        return binding.getRoot();
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
    public void onClickProduct(ProductEntity selectedProduct, boolean isCreator) {
        viewModel.selectedItem(selectedProduct, isCreator);
    }
}
