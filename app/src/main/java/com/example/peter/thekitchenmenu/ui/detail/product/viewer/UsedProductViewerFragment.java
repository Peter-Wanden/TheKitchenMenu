package com.example.peter.thekitchenmenu.ui.detail.product.viewer;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.Observable;
import androidx.fragment.app.Fragment;

import com.example.peter.thekitchenmenu.R;
import com.example.peter.thekitchenmenu.databinding.UsedProductViewerBinding;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class UsedProductViewerFragment extends Fragment {

    private static final String TAG = "tkm-UsedProductViewerFr";

    public static final String ARGUMENT_PRODUCT_ID = "PRODUCT_ID";

    private UsedProductViewerBinding binding;
    private UsedProductViewerViewModel viewModel;
    private FloatingActionButton fab;

    public static UsedProductViewerFragment newInstance(String productId) {

        Bundle arguments = new Bundle();
        arguments.putString(ARGUMENT_PRODUCT_ID, productId);
        UsedProductViewerFragment fragment = new UsedProductViewerFragment();
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
                R.layout.used_product_viewer,
                container,
                false);

        setViewModel();
        setBindingInstanceVariables();
        setupFab();
        setHasOptionsMenu(true);
        subscribeToNavigationChanges();

        return binding.getRoot();
    }

    private void setViewModel() {
        viewModel = ProductViewerActivity.obtainUsedProductViewerViewModel(requireActivity());
    }

    private void setBindingInstanceVariables() {
        binding.setViewModel(viewModel);
    }

    private void setupFab() {
        fab = getActivity().findViewById(R.id.product_viewer_activity_fab);
        fab.setOnClickListener(v -> viewModel.onFabClicked());
    }

    private void subscribeToNavigationChanges() {
        viewModel.getSetFabIcon().observe(this, this::setFabIcon);

        // Resets the options menu to the logic in onPrepareOptionsMenu()
        viewModel.isInUsedList.addOnPropertyChangedCallback(
                new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable sender, int propertyId) {
                requireActivity().invalidateOptionsMenu();
            }
        });
    }

    private void setFabIcon(boolean setEditIconIfTrue) {

        if (setEditIconIfTrue) fab.setImageResource(R.drawable.ic_edit_white);
        else fab.setImageResource(R.drawable.ic_format_list_bulleted_white);
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.menu_product_viewer_fragment, menu);
    }

    @Override
    public void onPrepareOptionsMenu(@NonNull Menu menu) {
        super.onPrepareOptionsMenu(menu);

        if (viewModel.isInUsedList.get()) {
            menu.findItem(R.id.menu_item_remove_used_product).setVisible(true);
        } else {
            menu.findItem(R.id.menu_item_remove_used_product).setVisible(false);
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_item_remove_used_product:
                viewModel.removeUsedProduct();
                return true;
        }
        return false;
    }
}