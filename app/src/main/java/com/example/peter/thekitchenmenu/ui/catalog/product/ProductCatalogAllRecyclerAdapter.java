package com.example.peter.thekitchenmenu.ui.catalog.product;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;

import com.example.peter.thekitchenmenu.R;
import com.example.peter.thekitchenmenu.data.model.ProductModel;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import javax.annotation.Nonnull;

public class ProductCatalogAllRecyclerAdapter
        extends RecyclerView.Adapter<ProductCatalogAllRecyclerAdapter.ViewHolder>
        implements Filterable {

    private final ProductCatalogViewModel viewModel;
    private List<ProductModel> productModelList;
    private List<ProductModel> productModelListFull;

    ProductCatalogAllRecyclerAdapter(ProductCatalogViewModel viewModel) {
        this.viewModel = viewModel;
    }

    /* View holder */
    @Nonnull
    @Override
    public ViewHolder onCreateViewHolder(@Nonnull ViewGroup viewGroup, int viewType) {

//        ProductListItemBinding binding = DataBindingUtil.inflate(
//                LayoutInflater.from(viewGroup.getContext()),
//                R.layout.product_list_item,
//                viewGroup,
//                false);
//
//        return new ViewHolder(binding);
        return null;
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

    /* Getter for the current list of products */
    public List<ProductModel> getProducts() {
        return productModelList;
    }

    void setProductModels(List<ProductModel> productModelList) {
        this.productModelList = productModelList;
        productModelListFull = new ArrayList<>(productModelList);
        notifyDataSetChanged();
    }

    /* Inner class for creating ViewHolders */
    class ViewHolder extends RecyclerView.ViewHolder {
//        ProductListItemBinding binding;

        ProductItemNavigator listener = new ProductItemNavigator() {
            @Override
            public void viewProduct(ProductModel productModel) {
                viewModel.viewProduct(productModel);
            }

            @Override
            public void addToFavorites(String productId) {
                viewModel.addToFavorites(productId);
            }

            @Override
            public void removeFromFavorites(String favoriteId) {
                viewModel.removeFromFavorites(favoriteId);
            }
        };

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
        }

//        ViewHolder(ProductListItemBinding binding) {
//            super(binding.getRoot());
//            this.binding = binding;
//        }

        void bind(ProductModel product) {
//            binding.setProductModel(product);
//            binding.setListener(listener);
//            binding.executePendingBindings();
        }
    }
}




