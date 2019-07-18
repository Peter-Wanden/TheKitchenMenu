package com.example.peter.thekitchenmenu.ui.detail.product.favoriteproducteditor;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.example.peter.thekitchenmenu.R;
import com.example.peter.thekitchenmenu.databinding.FavoriteProductEditorFragmentBinding;
import com.example.peter.thekitchenmenu.ui.detail.product.producteditor.ProductEditorActivity;
import com.example.peter.thekitchenmenu.utils.CurrencyInputWatcher;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;

public class FavoriteProductEditorFragment extends Fragment {

    private static final String TAG = "tkm-FavProductEditFrag";

    private FavoriteProductEditorFragmentBinding binding;
    private FavoriteProductEditorViewModel viewModel;

    public FavoriteProductEditorFragment() {}

    static FavoriteProductEditorFragment newInstance(String productId) {
        Bundle arguments = new Bundle();
        arguments.putString(ProductEditorActivity.EXTRA_PRODUCT_ID, productId);
        FavoriteProductEditorFragment fragment = new FavoriteProductEditorFragment();
        fragment.setArguments(arguments);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        binding = DataBindingUtil.inflate(
                inflater,
                R.layout.favorite_product_editor_fragment,
                container,
                false);

        viewModel = obtainViewModel();
        setBindingInstanceVariables();

        binding.setLifecycleOwner(this);
        return binding.getRoot();
    }

    private void setBindingInstanceVariables() {
        binding.setViewModel(viewModel);
    }

    private FavoriteProductEditorViewModel obtainViewModel() {
        return FavoriteProductEditorActivity.obtainFavoriteProductEditorViewModel(
                requireActivity());
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        subscribeToEvents();
        loadData();
        setUpPricingInput();
    }

    private void subscribeToEvents() {
        // TODO Change these to observable Strings that fire straight at the view from the model
        viewModel.getCanSaveFavorite().observe(this, this::setOptionsMenu);
        viewModel.getRetailerErrorEvent().observe(this, this::retailerError);
        viewModel.getLocationRoomErrorEvent().observe(this, this::locationRoomError);
        viewModel.getLocationInRoomErrorEvent().observe(this, this::locationInRoomError);
        viewModel.getPriceErrorEvent().observe(this, this::priceError);
    }

    private void loadData() {
        viewModel.start(getArguments().getString(ProductEditorActivity.EXTRA_PRODUCT_ID));
    }

    private void retailerError(String retailerError) {
        binding.editableRetailer.setError(retailerError);
    }

    private void locationRoomError(String locationRoomError) {
        binding.editableLocationRoom.setError(locationRoomError);
    }

    private void locationInRoomError(String locationInRoomError) {
        binding.editableLocationInRoom.setError(locationInRoomError);
    }

    private void priceError(String priceError) {
        binding.editablePrice.setError(priceError);
    }

    private void setUpPricingInput() {
        binding.editablePrice.addTextChangedListener(
                new CurrencyInputWatcher(binding.editablePrice));
    }

    private void setOptionsMenu(boolean hasOptionMenu) {
        setHasOptionsMenu(hasOptionMenu);
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.menu_favorite_product_editor_fragment, menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.menu_item_save_favorite) {
            viewModel.saveFavoriteProduct();
            return true;
        }
        return false;
    }
}
