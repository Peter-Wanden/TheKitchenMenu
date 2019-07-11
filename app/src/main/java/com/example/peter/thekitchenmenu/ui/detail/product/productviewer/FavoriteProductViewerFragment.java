package com.example.peter.thekitchenmenu.ui.detail.product.productviewer;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.view.menu.MenuBuilder;
import androidx.appcompat.view.menu.MenuPopupHelper;
import androidx.appcompat.widget.PopupMenu;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import com.example.peter.thekitchenmenu.R;
import com.example.peter.thekitchenmenu.databinding.FavoriteProductViewerFragmentBinding;
import com.example.peter.thekitchenmenu.ui.detail.product.favoriteproducteditor.FavoriteProductEditorFragment;

public class FavoriteProductViewerFragment extends Fragment {

    private static final String TAG = "tkm-FavProdViewerFrag";

    private FavoriteProductViewerFragmentBinding binding;
    private FavoriteProductViewerViewModel viewModel;

    static FavoriteProductViewerFragment newInstance(String favoriteProductId) {

        Bundle arguments = new Bundle();
        arguments.putString(
                FavoriteProductEditorFragment.ARGUMENT_FAVORITE_PRODUCT_ID,
                favoriteProductId);
        FavoriteProductViewerFragment fragment = new FavoriteProductViewerFragment();
        fragment.setArguments(arguments);

        return fragment;
    }

    @Override
    public void onResume() {
        super.onResume();

        if (getArguments() != null)
            viewModel.start(getArguments().getString(
                    FavoriteProductEditorFragment.ARGUMENT_FAVORITE_PRODUCT_ID));
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        binding = DataBindingUtil.inflate(
                inflater,
                R.layout.favorite_product_viewer_fragment,
                container,
                false);

        setViewModel();
        setBindingInstanceVariables();
        setHasOptionsMenu(true);
        setupPopupMenu();

        return binding.getRoot();
    }

    private void setViewModel() {
        viewModel = ProductViewerActivity.obtainFavoriteProductViewerViewModel(requireActivity());
    }

    private void setBindingInstanceVariables() {
        binding.setViewModel(viewModel);
    }

    private void setupPopupMenu() {
        PopupMenu popupMenu = new PopupMenu(this.getContext(), binding.floatingContextMenuButton);
        popupMenu.getMenuInflater().inflate(
                R.menu.menu_favorite_viewer_fragment, popupMenu.getMenu());

        popupMenu.setOnMenuItemClickListener(menuItem -> {
            viewModel.favoritePopUpMenuItemSelected(menuItem.getItemId());
            return true;
        });

        MenuPopupHelper menuHelper = new MenuPopupHelper(
                getContext(),
                (MenuBuilder) popupMenu.getMenu(),
                binding.floatingContextMenuButton);
        menuHelper.setForceShowIcon(true);

        binding.floatingContextMenuButton.setOnClickListener(view -> menuHelper.show());
    }
}