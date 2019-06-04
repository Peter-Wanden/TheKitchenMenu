package com.example.peter.thekitchenmenu.ui.catalog;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.peter.thekitchenmenu.R;
import com.example.peter.thekitchenmenu.app.Constants;
import com.example.peter.thekitchenmenu.data.entity.ProductEntity;
import com.example.peter.thekitchenmenu.utils.Converters;
import com.squareup.picasso.Picasso;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class CatalogRecyclerAdapter
        extends
        RecyclerView.Adapter<CatalogRecyclerAdapter.AdapterViewHolder> {

    private static final String TAG = "tkm-ProductCatalogAdapter";
    private final Context context;

    private List<ProductEntity> productList;
    final private OnClickProduct clickHandler;

    CatalogRecyclerAdapter(Context context, OnClickProduct clickHandler) {
        this.context = context;
        this.clickHandler = clickHandler;
    }

    /* View holder */
    @NonNull
    @Override
    public AdapterViewHolder onCreateViewHolder(
            @NonNull ViewGroup viewGroup,
            int viewType) {

        View view = LayoutInflater
                .from(context)
                .inflate(R.layout.list_item_product, viewGroup, false);

        return new AdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(
            @NonNull AdapterViewHolder holder,
            int position) {

        ProductEntity product = productList.get(position);
        holder.descriptionTV.setText(product.getDescription());

        // TODO - Picasso, add image caching.
        /* Get and set the image */
        if (product.getWebImageUrl() != null && !product.getWebImageUrl().isEmpty()) {
            Picasso.get().load(product.getRemoteSmallImageUri()).into(holder.productImageIV);
        } else {
            Picasso.get().load(R.drawable.placeholder).into(holder.productImageIV);
        }

        /* Set the pack size */
        holder.baseUnitsTV.setText(String.valueOf(product.getBaseUnits()));

        /* Set the unit of measure */
        holder.unitOfMeasureTV.setText(Converters.getUnitOfMeasureString
                (context, product.getUnitOfMeasureSubtype()));
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
    class AdapterViewHolder
            extends
            RecyclerView.ViewHolder
            implements
            View.OnClickListener {

        final TextView descriptionTV;
        final TextView baseUnitsTV;
        final TextView unitOfMeasureTV;
        final ImageView productImageIV;

        AdapterViewHolder(View itemView) {
            super(itemView);

            descriptionTV = itemView.findViewById(R.id.list_item_product_tv_description);
            baseUnitsTV = itemView.findViewById(R.id.list_item_product_tv_pack_size);
            unitOfMeasureTV = itemView.findViewById(R.id.list_item_product_tv_label_unit_of_measure);
            productImageIV = itemView.findViewById(R.id.list_item_product_iv_product_image);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {

            // Get the product from the adapter at the clicked position
            ProductEntity product = productList.get(getAdapterPosition());

            // Find out if this user was the creator of the product_uneditable
            boolean isCreator = Constants.getUserId().getValue().
                    equals(product.getCreatedBy());

            // Click handler for this product_uneditable type
            clickHandler.onClickProduct(product, isCreator);
        }
    }
}




