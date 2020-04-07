package com.example.peter.thekitchenmenu.ui.catalog.product;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;

import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.example.peter.thekitchenmenu.R;
import com.example.peter.thekitchenmenu.data.model.ProductModel;
import com.example.peter.thekitchenmenu.databinding.FavoriteProductListItemBinding;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nonnull;

public class ProductCatalogFavoritesRecyclerAdapter
        extends RecyclerView.Adapter<ProductCatalogFavoritesRecyclerAdapter.ViewHolder>
        implements Filterable {

    private static final String TAG = "tkm-" + ProductCatalogFavoritesRecyclerAdapter.class.
            getSimpleName() + ":";

    private final ProductCatalogViewModel viewModel;
    private List<ProductModel> productModelList;
    private List<ProductModel> productModelListFull;

    ProductCatalogFavoritesRecyclerAdapter(ProductCatalogViewModel viewModel) {
        this.viewModel = viewModel;
    }

    @Nonnull
    @Override
    public ViewHolder onCreateViewHolder(@Nonnull ViewGroup viewGroup, int viewType) {

        FavoriteProductListItemBinding binding = DataBindingUtil.inflate(
                LayoutInflater.from(viewGroup.getContext()),
                R.layout.favorite_product_list_item,
                viewGroup,
                false);

        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@Nonnull ViewHolder holder, int position) {
        final ProductModel productModel = productModelList.get(position);
        holder.bind(productModel);
    }

    @Override
    public int getItemCount() {
        if (productModelList == null) return 0;
        return productModelList.size();
    }

    @Override
    public Filter getFilter() {
        return filterResults;
    }

    private Filter filterResults = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            List<ProductModel> filteredList = new ArrayList<>();

            if (constraint == null || constraint.length() == 0) {
                filteredList.addAll(productModelListFull);
            } else {
                String filterPattern = constraint.toString().toLowerCase().trim();

                for (ProductModel model : productModelListFull) {
                    String description = model.getProductEntity().getDescription().toLowerCase();

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
            productModelList.clear();
            productModelList.addAll((List) filterResults.values);
            notifyDataSetChanged();
        }
    };

    public List<ProductModel> getProductModelList() {
        return productModelList;
    }

    void setProductModelList(List<ProductModel> productModelList) {
        this.productModelList = productModelList;
        productModelListFull = new ArrayList<>(productModelList);
        notifyDataSetChanged();
    }

    /* Inner class for creating ViewHolders */
    class ViewHolder extends RecyclerView.ViewHolder {
        FavoriteProductListItemBinding binding;

        FavoriteItemNavigator listener = new FavoriteItemNavigator() {
            @Override
            public void onFavoriteProductClicked(ProductModel productModel) {
                viewModel.viewProduct(productModel);
            }

            @Override
            public void onRemoveFromFavoritesClicked(ProductModel productModel) {
                viewModel.removeFromFavorites(productModel.getFavoriteProductEntity().getId());
            }
        };

        ViewHolder(FavoriteProductListItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        void bind(ProductModel productModel) {
            binding.setProductModel(productModel);
            binding.setListener(listener);
            binding.executePendingBindings();
        }
    }
}
