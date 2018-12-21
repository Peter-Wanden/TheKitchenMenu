package com.example.peter.thekitchenmenu.ui.catalog;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.peter.thekitchenmenu.R;
import com.example.peter.thekitchenmenu.data.model.VmProd;
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
    private final Context mContext;

    // List storing the database query result
    private List<VmProd> mListVmProd;

    // The user Id of the current user
    private String mUserId;

    // Click interface
    final private OnClickVmProd mClickHandler;

    /* Constructor */
    AdapterCatProdComm(Context context, OnClickVmProd clickHandler) {
        mContext = context;
        mClickHandler = clickHandler;
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
        VmProd vmProd = mListVmProd.get(position);

        /* Set the description */
        holder.descTV.setText(vmProd.getDescription());

        // TODO - Picasso, add image caching.
        /* Get and set the image */
        if (!vmProd.getFbStorageImageUri().equals("")) {
            Picasso.get().load(vmProd.getFbStorageImageUri()).into(holder.prodIV);
        } else {
            Picasso.get().load(R.drawable.placeholder).into(holder.prodIV);
        }

        /* Set the pack size */
        holder.packSizeTV.setText(String.valueOf(vmProd.getPackSize()));

        /* Set the unit of measure */
        holder.UoMTV.setText(Converters.getUnitOfMeasureString
                (mContext, vmProd.getUnitOfMeasure()));
    }

    /* Returns the number of items in the adapter */
    @Override
    public int getItemCount() {
        if (mListVmProd == null) return 0;
        return mListVmProd.size();
    }

    /* Getter for the current list of products */
    public List<VmProd> getProducts() {
        return mListVmProd;
    }

    /*
     * When the data changes, this method updates the list of products and notifies the adapter to
     * use the new values in it
     */
    public void setProducts(List<VmProd> vmListProd) {
        mListVmProd = vmListProd;
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
            VmProd vmProd = mListVmProd.get(getAdapterPosition());

            // Find out if this user was the creator of the product
            boolean isCreator = mUserId.equals(vmProd.getCreatedBy());

            // Click handler for this product type
            mClickHandler.onClick(vmProd, isCreator);
        }
    }

    /* Setter for the user Id. The setting of this field is essential. */
    void setUserId(String userId){
        mUserId = userId;
    }
}




