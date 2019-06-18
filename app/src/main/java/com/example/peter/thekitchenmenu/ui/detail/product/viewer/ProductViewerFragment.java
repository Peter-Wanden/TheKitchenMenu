package com.example.peter.thekitchenmenu.ui.detail.product.viewer;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import com.example.peter.thekitchenmenu.R;
import com.example.peter.thekitchenmenu.databinding.ProductViewerDetailFragmentBinding;

public class ProductViewerFragment extends Fragment {

    private static final String TAG = "tkm-ProductViewerFrag";

    public static final String ARGUMENT_PRODUCT_ID = "PRODUCT_ID";

    private ProductViewerDetailFragmentBinding binding;
    private ProductViewerViewModel viewModel;

    public static ProductViewerFragment newInstance(String productId) {
        Bundle arguments = new Bundle();
        arguments.putString(ARGUMENT_PRODUCT_ID, productId);
        ProductViewerFragment fragment = new ProductViewerFragment();
        fragment.setArguments(arguments);
        return fragment;
    }

    @Override
    public void onResume() {
        super.onResume();
        viewModel.start(getArguments().getString(ARGUMENT_PRODUCT_ID));
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        binding = DataBindingUtil.inflate(
                inflater,
                R.layout.product_viewer_detail_fragment,
                container,
                false);

        setViewModel();
        setBindingInstanceVariables();

        return binding.getRoot();
    }

    private void setViewModel() {
        viewModel = ProductViewerActivity.obtainProductViewerViewModel(requireActivity());
    }

    private void setBindingInstanceVariables() {
        binding.setViewModel(viewModel);
    }
}
