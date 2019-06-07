package com.example.peter.thekitchenmenu.ui.catalog;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.peter.thekitchenmenu.BR;
import com.example.peter.thekitchenmenu.R;
import com.example.peter.thekitchenmenu.app.Constants;
import com.example.peter.thekitchenmenu.data.entity.ProductEntity;
import com.example.peter.thekitchenmenu.databinding.ProductListItemBinding;
import com.example.peter.thekitchenmenu.utils.Converters;
import com.squareup.picasso.Picasso;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

public class CatalogRecyclerAdapter
        extends RecyclerView.Adapter<CatalogRecyclerAdapter.AdapterViewHolder> {

    private static final String TAG = "tkm-CatalogAdapter";

    private final Context context;
    private final CatalogProductsViewModel viewModel;
    private List<ProductEntity> productList;

    CatalogRecyclerAdapter(Context context, CatalogProductsViewModel viewModel) {
        this.context = context;
        this.viewModel = viewModel;
    }

    /* View holder */
    @NonNull
    @Override
    public CatalogRecyclerAdapter.AdapterViewHolder onCreateViewHolder(
            @NonNull ViewGroup viewGroup,
            int viewType) {

        ProductListItemBinding binding = DataBindingUtil.inflate(
                LayoutInflater.from(viewGroup.getContext()),
                R.layout.product_list_item,
                viewGroup,
                false);

        return new AdapterViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterViewHolder holder, int position) {
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
    class AdapterViewHolder extends RecyclerView.ViewHolder {
        ProductListItemBinding binding;

        ProductItemUserActionsListener listener = product ->
                viewModel.getOpenProductEvent().setValue(product.getId());

        AdapterViewHolder(ProductListItemBinding binding) {
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




