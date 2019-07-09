package com.example.peter.thekitchenmenu.ui.detail.product.favoriteproducteditor;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.peter.thekitchenmenu.R;
import com.example.peter.thekitchenmenu.databinding.FavoriteProductEditorFragmentBinding;
import com.example.peter.thekitchenmenu.utils.CurrencyInputWatcher;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

public class FavoriteProductEditorFragment extends Fragment {

    private static final String TAG = "tkm-FavProductEditFrag";

    public static final String ARGUMENT_PRODUCT_ID = "PRODUCT_ID";
    public static final String ARGUMENT_FAVORITE_PRODUCT_ID = "FAVORITE_PRODUCT_ID";

    private FavoriteProductEditorFragmentBinding binding;
    private FavoriteProductEditorViewModel viewModel;

    static FavoriteProductEditorFragment newInstance(String productId,
                                                            String favoriteProductId) {

        Bundle arguments = new Bundle();
        arguments.putString(ARGUMENT_PRODUCT_ID, productId);
        arguments.putString(ARGUMENT_FAVORITE_PRODUCT_ID, favoriteProductId);
        FavoriteProductEditorFragment fragment = new FavoriteProductEditorFragment();
        fragment.setArguments(arguments);
        return fragment;
    }

    public FavoriteProductEditorFragment() {}

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        loadData();
        subscribeToEvents();
        setUpFab();
        setUpPricingInput();
    }

    private void loadData() {
        if (getArguments() != null)
            if (getArguments().getString(ARGUMENT_FAVORITE_PRODUCT_ID) != null) {
                viewModel.start(
                        getArguments().getString(ARGUMENT_PRODUCT_ID),
                        getArguments().getString(ARGUMENT_FAVORITE_PRODUCT_ID));
            } else {
                viewModel.start(
                        getArguments().getString(ARGUMENT_PRODUCT_ID),
                        null);
            }
    }

    private void subscribeToEvents() {
        viewModel.getRetailerErrorEvent().observe(this, this::retailerError);
        viewModel.getLocationRoomErrorEvent().observe(this, this::locationRoomError);
        viewModel.getLocationInRoomErrorEvent().observe(this, this::locationInRoomError);
        viewModel.getPriceErrorEvent().observe(this, this::priceError);
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

    private void setUpFab() {
        FloatingActionButton fab = getActivity().findViewById(R.id.favorite_product_editor_save_fab);
        fab.setOnClickListener(view -> viewModel.saveFavoriteProduct());
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

    private FavoriteProductEditorViewModel obtainViewModel() {
        return FavoriteProductEditorActivity.obtainFavoriteProductEditorViewModel(requireActivity());
    }

    private void setBindingInstanceVariables() {
        binding.setViewModel(viewModel);
    }

    private void setUpPricingInput() {
        binding.editablePrice.addTextChangedListener(new CurrencyInputWatcher(binding.editablePrice));
    }
}
