package com.example.peter.thekitchenmenu.ui.catalog;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.example.peter.thekitchenmenu.R;
import com.example.peter.thekitchenmenu.data.model.UsedProductDataModel;
import com.example.peter.thekitchenmenu.databinding.UsedProductListItemBinding;

import java.util.List;

public class CatalogUsedRecyclerAdapter extends RecyclerView.Adapter<CatalogUsedRecyclerAdapter.ViewHolder> {

    private static final String TAG = "tkm-CatalogUsedAdapter";

    private final CatalogProductsViewModel viewModel;
    private List<UsedProductDataModel> usedProductDataModels;

    public CatalogUsedRecyclerAdapter(CatalogProductsViewModel viewModel) {
        this.viewModel = viewModel;
    }

    /* View holder */
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {

        UsedProductListItemBinding binding = DataBindingUtil.inflate(
                LayoutInflater.from(viewGroup.getContext()),
                R.layout.used_product_list_item,
                viewGroup,
                false);

        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final UsedProductDataModel usedProductModel = usedProductDataModels.get(position);
        holder.bind(usedProductModel);
    }

    @Override
    public int getItemCount() {
        if (usedProductDataModels == null) return 0;
        return usedProductDataModels.size();
    }

    public List<UsedProductDataModel> getUsedProductDataModels() {
        return usedProductDataModels;
    }

    void setUsedProductDataModels(List<UsedProductDataModel> usedProductDataModels) {
        this.usedProductDataModels = usedProductDataModels;
        notifyDataSetChanged();
    }

    /* Inner class for creating ViewHolders */
    class ViewHolder extends RecyclerView.ViewHolder {
        UsedProductListItemBinding binding;

        UsedProductItemUserActionsListener listener = usedProduct ->
                viewModel.getOpenProductEvent().setValue(usedProduct.getProduct().getId());

        ViewHolder(UsedProductListItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        void bind(UsedProductDataModel usedProductDataModel) {
            binding.setUsedProduct(usedProductDataModel);
            binding.setListener(listener);
            binding.executePendingBindings();
        }
    }
}
