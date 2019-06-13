package com.example.peter.thekitchenmenu.ui.catalog;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.example.peter.thekitchenmenu.R;
import com.example.peter.thekitchenmenu.data.entity.ProductEntity;
import com.example.peter.thekitchenmenu.databinding.ProductListItemBinding;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

public class CatalogAllRecyclerAdapter
        extends RecyclerView.Adapter<CatalogAllRecyclerAdapter.ViewHolder> {

    private static final String TAG = "tkm-CatalogAdapter";

    private final CatalogProductsViewModel viewModel;
    private List<ProductEntity> productList;

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
        final ProductEntity product = productList.get(position);
        holder.bind(product);
    }

    @Override
    public int getItemCount() {
        if (productList == null) return 0;
        return productList.size();
    }

    /* Getter for the current list of products */
    public List<ProductEntity> getProducts() {
        return productList;
    }

    public void setProducts(List<ProductEntity> productList) {
        this.productList = productList;
        notifyDataSetChanged();
    }

    /* Inner class for creating ViewHolders */
    class ViewHolder extends RecyclerView.ViewHolder {
        ProductListItemBinding binding;

        ProductItemUserActionsListener listener = product ->
                viewModel.getOpenProductEvent().setValue(product.getId());

        ViewHolder(ProductListItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        void bind(ProductEntity product) {
            binding.setProduct(product);
            binding.setListener(listener);
            binding.executePendingBindings();
        }
    }
}




