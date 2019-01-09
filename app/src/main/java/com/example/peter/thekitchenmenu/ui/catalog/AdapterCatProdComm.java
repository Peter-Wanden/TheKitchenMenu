package com.example.peter.thekitchenmenu.ui.catalog;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.peter.thekitchenmenu.R;
import com.example.peter.thekitchenmenu.app.Constants;
import com.example.peter.thekitchenmenu.data.model.ProductModel;
import com.example.peter.thekitchenmenu.utils.Converters;
import com.squareup.picasso.Picasso;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class AdapterCatProdComm
        extends
        RecyclerView.Adapter<AdapterCatProdComm.AdapterCatProdViewHolder> {

    private static final String TAG = AdapterCatProdComm.class.getSimpleName();

    // The context we use for utility methods, app resources and layout inflaters
    private final Context context;

    // List storing the database query result
    private List<ProductModel> listProductModel;

    // Click interface
    final private OnClickVmProd clickHandler;

    AdapterCatProdComm(Context context, OnClickVmProd clickHandler) {
        this.context = context;
        this.clickHandler = clickHandler;
    }

    /* View holder */
    @NonNull
    @Override
    public AdapterCatProdViewHolder onCreateViewHolder(
            @NonNull ViewGroup viewGroup,
            int viewType) {

        View view = LayoutInflater
                .from(context)
                .inflate(R.layout.list_item_product, viewGroup, false);

        return new AdapterCatProdViewHolder(view);
    }

    @Override
    public void onBindViewHolder(
            @NonNull AdapterCatProdViewHolder holder,
            int position) {

        /* Get the product at the passed in position */
        ProductModel productModel = listProductModel.get(position);

        /* Set the description */
        holder.descTV.setText(productModel.getDescription());

        // TODO - Picasso, add image caching.
        /* Get and set the image */
        if (!productModel.getFbStorageImageUri().equals("")) {
            Picasso.get().load(productModel.getFbStorageImageUri()).into(holder.prodIV);
        } else {
            Picasso.get().load(R.drawable.placeholder).into(holder.prodIV);
        }

        /* Set the pack size */
        holder.packSizeTV.setText(String.valueOf(productModel.getPackSize()));

        /* Set the unit of measure */
        holder.UoMTV.setText(Converters.getUnitOfMeasureString
                (context, productModel.getUnitOfMeasure()));
    }

    /* Returns the number of items in the adapter */
    @Override
    public int getItemCount() {
        if (listProductModel == null) return 0;
        return listProductModel.size();
    }

    /* Getter for the current list of products */
    public List<ProductModel> getProducts() {
        return listProductModel;
    }

    /*
     * When the data changes, this method updates the list of products and notifies the adapter to
     * use the new values in it
     */
    public void setProducts(List<ProductModel> vmListProd) {
        listProductModel = vmListProd;
        notifyDataSetChanged();
    }

    /* Inner class for creating ViewHolders */
    class AdapterCatProdViewHolder
            extends
            RecyclerView.ViewHolder
            implements
            View.OnClickListener {

        final TextView descTV;
        final TextView packSizeTV;
        final TextView UoMTV;
        final ImageView prodIV;

        AdapterCatProdViewHolder(View itemView) {
            super(itemView);

            descTV = itemView.findViewById(R.id.list_item_product_tv_description);
            packSizeTV = itemView.findViewById(R.id.list_item_product_tv_pack_size);
            UoMTV = itemView.findViewById(R.id.list_item_product_tv_label_unit_of_measure);
            prodIV = itemView.findViewById(R.id.list_item_product_iv_product_image);

            itemView.setOnClickListener(this);
        }

        public void onClick(View v) {

            // Get the product from the adapter at the clicked position
            ProductModel productModel = listProductModel.get(getAdapterPosition());

            // Find out if this user was the creator of the product
            boolean isCreator = Constants.getUserId().getValue().
                    equals(productModel.getCreatedBy());

            // Click handler for this product type
            clickHandler.onClick(productModel, isCreator);
        }
    }
}




