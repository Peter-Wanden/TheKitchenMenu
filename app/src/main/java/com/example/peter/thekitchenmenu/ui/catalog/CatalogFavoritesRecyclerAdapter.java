package com.example.peter.thekitchenmenu.ui.catalog;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.example.peter.thekitchenmenu.R;
import com.example.peter.thekitchenmenu.data.model.FavoriteProductModel;
import com.example.peter.thekitchenmenu.databinding.FavoriteProductListItemBinding;

import java.util.List;

public class CatalogFavoritesRecyclerAdapter extends RecyclerView.Adapter<CatalogFavoritesRecyclerAdapter.ViewHolder> {

    private static final String TAG = "tkm-CatalogFavAdapter";

    private final CatalogProductsViewModel viewModel;
    private List<FavoriteProductModel> favoriteProductModels;

    public CatalogFavoritesRecyclerAdapter(CatalogProductsViewModel viewModel) {
        this.viewModel = viewModel;
    }

    /* View holder */
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {

        FavoriteProductListItemBinding binding = DataBindingUtil.inflate(
                LayoutInflater.from(viewGroup.getContext()),
                R.layout.favorite_product_list_item,
                viewGroup,
                false);

        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final FavoriteProductModel favoriteProductModel = favoriteProductModels.get(position);
        holder.bind(favoriteProductModel);
    }

    @Override
    public int getItemCount() {
        if (favoriteProductModels == null) return 0;
        return favoriteProductModels.size();
    }

    public List<FavoriteProductModel> getFavoriteProductModels() {
        return favoriteProductModels;
    }

    void setFavoriteProductModels(List<FavoriteProductModel> favoriteProductModels) {
        this.favoriteProductModels = favoriteProductModels;
        notifyDataSetChanged();
    }

    /* Inner class for creating ViewHolders */
    class ViewHolder extends RecyclerView.ViewHolder {
        FavoriteProductListItemBinding binding;

        FavoriteProductItemUserActionsListener listener = favoriteProduct ->
                viewModel.getOpenProductEvent().setValue(favoriteProduct.getProduct().getId());

        ViewHolder(FavoriteProductListItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        void bind(FavoriteProductModel favoriteProductModel) {
            binding.setFavoriteProduct(favoriteProductModel);
            binding.setListener(listener);
            binding.executePendingBindings();
        }
    }
}
