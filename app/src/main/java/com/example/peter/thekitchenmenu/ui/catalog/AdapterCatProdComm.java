package com.example.peter.thekitchenmenu.ui.catalog;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.peter.thekitchenmenu.R;
import com.example.peter.thekitchenmenu.data.model.ProductCommunity;
import com.example.peter.thekitchenmenu.utils.Converters;
import com.squareup.picasso.Picasso;

import java.util.List;

public class AdapterCatProdComm
        extends
        RecyclerView.Adapter<AdapterCatProdComm.AdapterCatProdViewHolder> {

    private static final String LOG_TAG = AdapterCatProdComm.class.getSimpleName();

    // The context we use for utility methods, app resources and layout inflaters
    private final Context mContext;

    // List storing the database query result
    private List<ProductCommunity> mProdComm;

    // The user Id of the current user
    private String mUserId;

    // Click interface
    //final private AdapterCatProdClickHandler mProductClickHandler;

    /* Constructor */
    public AdapterCatProdComm(Context context) {

        mContext = context;
        //mProductClickHandler = clickHandler;
    }

    /* View holder */
    @NonNull
    @Override
    public AdapterCatProdViewHolder onCreateViewHolder(
            @NonNull ViewGroup viewGroup,
            int viewType) {

        View view = LayoutInflater
                .from(mContext)
                .inflate(R.layout.list_item_product, viewGroup, false);

        return new AdapterCatProdViewHolder(view);
    }

    @Override
    public void onBindViewHolder(
            @NonNull AdapterCatProdViewHolder holder,
            int position) {

        /* Get the product at the passed in position */
        ProductCommunity prodComm = mProdComm.get(position);

        /* Set the description */
        holder.descTV.setText(prodComm.getDescription());

        /* Get and set the image */
        if (!prodComm.getFbStorageImageUri().equals("")) {
            Picasso.get().load(prodComm.getFbStorageImageUri()).into(holder.prodIV);
        } else {
            Picasso.get().load(R.drawable.placeholder).into(holder.prodIV);
        }

        /* Set the pack size */
        holder.packSizeTV.setText(String.valueOf(prodComm.getPackSize()));

        /* Set the unit of measure */
        holder.UoMTV.setText(Converters.getUnitOfMeasureString
                (mContext, prodComm.getUnitOfMeasure()));
    }

    /* Returns the number of items in the adapter */
    @Override
    public int getItemCount() {
        if (mProdComm == null) return 0;
        return mProdComm.size();
    }

    /* Getter for the current list of products */
    public List<ProductCommunity> getProducts() {
        return mProdComm;
    }

    /*
     * When the data changes, this method updates the list of products and notifies the adapter to
     * use the new values in it
     */
    public void setProducts(List<ProductCommunity> prodComms) {
        mProdComm = prodComms;
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

        /* Constructor for the AdapterCatProdViewHolder.class */
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
            ProductCommunity prodComm = mProdComm.get(getAdapterPosition());

            // Find out if this user was the creator of the product
            boolean isCreator = mUserId.equals(prodComm.getCreatedBy());

            // Click handler for this product type
            //mProductClickHandler.onClick(prodComm, isCreator);
        }
    }

    /* Interface that executes the onProductClicked method in the host Activity / Fragment. */
//    public interface AdapterCatProdClickHandler {
//        void onClick(ProductCommunity clickedProduct, boolean isCreator);
//    }

    /* Setter for the user Id. The setting of this field is essential. */
    public void setUserId (String userId){
        Log.e(LOG_TAG, "ADAPTER - userID has been updated to: " + userId);
        mUserId = userId;
    }
}




