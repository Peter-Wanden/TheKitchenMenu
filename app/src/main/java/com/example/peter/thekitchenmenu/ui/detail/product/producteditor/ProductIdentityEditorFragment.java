package com.example.peter.thekitchenmenu.ui.detail.product.producteditor;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import com.example.peter.thekitchenmenu.R;
import com.example.peter.thekitchenmenu.databinding.ProductEditorIdentityBinding;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;


public class ProductIdentityEditorFragment extends Fragment {

    private static final String TAG = "ProductIdentityEditorFr";

    private ProductEditorIdentityBinding binding;
    private ProductIdentityViewModel viewModel;

    static ProductIdentityEditorFragment newInstance() {
        return new ProductIdentityEditorFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        binding = DataBindingUtil.inflate(
                inflater,
                R.layout.product_editor_identity,
                container,
                false);

        View rootView = binding.getRoot();
        binding.setLifecycleOwner(this);

        setViewModel();
        setBindingInstanceVariables();
        setupSpinners();

        return rootView;
    }

    private void setViewModel() {
        viewModel = new ViewModelProvider(requireActivity()).get(ProductIdentityViewModel.class);
    }

    private void setBindingInstanceVariables() {
        binding.setViewModel(viewModel);
    }

    private void setupSpinners() {
        setupCategorySpinner();
        setUpShelfLifeSpinner();
    }

    private void setupCategorySpinner() {
        binding.spinnerCategory.setAdapter(ArrayAdapter.createFromResource(
                requireActivity(), R.array.product_category_options, R.layout.list_item_spinner));
    }

    private void setUpShelfLifeSpinner() {
        binding.spinnerShelfLife.setAdapter(ArrayAdapter.createFromResource(
                requireActivity(),
                R.array.shelf_life_options,
                R.layout.list_item_spinner));
    }
}
