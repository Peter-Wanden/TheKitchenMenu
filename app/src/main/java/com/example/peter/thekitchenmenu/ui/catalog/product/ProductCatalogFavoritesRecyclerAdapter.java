package com.example.peter.thekitchenmenu.ui.catalog.product;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.example.peter.thekitchenmenu.R;
import com.example.peter.thekitchenmenu.data.model.FavoriteProductModel;
import com.example.peter.thekitchenmenu.databinding.FavoriteProductListItemBinding;

import java.util.ArrayList;
import java.util.List;

public class ProductCatalogFavoritesRecyclerAdapter
        extends RecyclerView.Adapter<ProductCatalogFavoritesRecyclerAdapter.ViewHolder>
        implements Filterable {

    private static final String TAG = "tkm-CatalogFavAdapter";

    private final ProductCatalogViewModel viewModel;
    private List<FavoriteProductModel> favoriteProductModels;
    private List<FavoriteProductModel> favoriteProductModelListFull;

    ProductCatalogFavoritesRecyclerAdapter(ProductCatalogViewModel viewModel) {
        this.viewModel = viewModel;
    }

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

    @Override
    public Filter getFilter() {
        return filterResults;
    }

    private Filter filterResults = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            List<FavoriteProductModel> filteredList = new ArrayList<>();

            if (constraint == null || constraint.length() == 0) {
                filteredList.addAll(favoriteProductModelListFull);
            } else {
                String filterPattern = constraint.toString().toLowerCase().trim();

                for (FavoriteProductModel model : favoriteProductModelListFull) {
                    String description = model.getProduct().getDescription().toLowerCase();

                    if (description.contains(filterPattern)) {
                        filteredList.add(model);
                    }
                }
            }
            FilterResults results = new FilterResults();
            results.values = filteredList;
            return results;
        }

        @Override
        protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
            favoriteProductModels.clear();
            favoriteProductModels.addAll((List) filterResults.values);
            notifyDataSetChanged();
        }
    };

    public List<FavoriteProductModel> getFavoriteProductModels() {
        return favoriteProductModels;
    }

    void setFavoriteProductModels(List<FavoriteProductModel> favoriteProductModels) {
        this.favoriteProductModels = favoriteProductModels;
        favoriteProductModelListFull = new ArrayList<>(favoriteProductModels);
        notifyDataSetChanged();
    }

    /* Inner class for creating ViewHolders */
    class ViewHolder extends RecyclerView.ViewHolder {
        FavoriteProductListItemBinding binding;

        FavoriteProductItemUserActionsListener listener = new FavoriteProductItemUserActionsListener() {
            @Override
            public void onFavoriteProductClicked(FavoriteProductModel favoriteProduct) {
                viewModel.getOpenProductEvent().setValue(favoriteProduct.getProduct().getId());
            }

            @Override
            public void onRemoveFromFavoritesClicked(FavoriteProductModel favoriteProductModel) {
                viewModel.removeFromFavorites(favoriteProductModel.getFavoriteProduct().getId());
            }
        };

        ViewHolder(FavoriteProductListItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        void bind(FavoriteProductModel favoriteProductModel) {
            binding.setFavoriteProductModel(favoriteProductModel);
            binding.setListener(listener);
            binding.executePendingBindings();
        }
    }
}
