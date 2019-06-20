package com.example.peter.thekitchenmenu.ui.catalog;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.example.peter.thekitchenmenu.R;
import com.example.peter.thekitchenmenu.data.entity.ProductEntity;
import com.example.peter.thekitchenmenu.data.model.ProductModel;
import com.example.peter.thekitchenmenu.databinding.ProductListItemBinding;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

public class CatalogAllRecyclerAdapter
        extends RecyclerView.Adapter<CatalogAllRecyclerAdapter.ViewHolder> {

    private static final String TAG = "tkm-CatalogAdapter";

    private final CatalogProductsViewModel viewModel;
    private List<ProductModel> productModelList;

    CatalogAllRecyclerAdapter(CatalogProductsViewModel viewModel) {
        this.viewModel = viewModel;
    }

    /* View holder */
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {

        ProductListItemBinding binding = DataBindingUtil.inflate(
                LayoutInflater.from(viewGroup.getContext()),
                R.layout.product_list_item,
                viewGroup,
                false);

        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final ProductModel productModel = productModelList.get(position);
        holder.bind(productModel);
    }

    @Override
    public int getItemCount() {
        if (productModelList == null) return 0;
        return productModelList.size();
    }

    /* Getter for the current list of products */
    public List<ProductModel> getProducts() {
        return productModelList;
    }

    public void setProductModels(List<ProductModel> productModelList) {
        this.productModelList = productModelList;
        notifyDataSetChanged();
    }

    /* Inner class for creating ViewHolders */
    class ViewHolder extends RecyclerView.ViewHolder {
        ProductListItemBinding binding;

        ProductItemUserActionsListener listener = new ProductItemUserActionsListener() {
            @Override
            public void onProductClicked(ProductModel productModel) {
                viewModel.getOpenProductEvent().setValue(productModel.getProduct().getId());
            }

            @Override
            public void onAddToFavoritesClicked(ProductModel productModel) {
                viewModel.getAddToFavoritesEvent().setValue(productModel.getProduct().getId());
            }

            @Override
            public void onRemoveFromFavoritesClicked(ProductModel productModel) {
                viewModel.removeFromFavorites(productModel.getProduct().getId());
            }
        };

        ViewHolder(ProductListItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        void bind(ProductModel product) {
            binding.setProductModel(product);
            binding.setListener(listener);
            binding.executePendingBindings();
        }
    }
}




