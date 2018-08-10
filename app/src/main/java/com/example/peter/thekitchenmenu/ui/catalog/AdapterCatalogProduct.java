package com.example.peter.thekitchenmenu.ui.catalog;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.example.peter.thekitchenmenu.R;
import com.example.peter.thekitchenmenu.model.Product;
import com.example.peter.thekitchenmenu.utils.Converters;

import java.util.List;

public class AdapterCatalogProduct
        extends
        RecyclerView.Adapter<AdapterCatalogProduct.AdapterCatalogProductViewHolder> {

    private static final String LOG_TAG = AdapterCatalogProduct.class.getSimpleName();

    // The context we use to utility methods, app resources and layout inflaters
    private final Context mContext;

    // List storing the database query result
    private List<Product> mProducts;

    // The user Id of the current user
    private String mUserId;

    // Records if current user is creator of product
    private boolean mIsCreator = false;

    // Click handler
    private final ProductCatalogAdapterOnClickHandler mClickHandler;

    /* Click interface that receives on click messages */
    public interface ProductCatalogAdapterOnClickHandler {
        void onClick(Product product, boolean isOwner);
    }

    public AdapterCatalogProduct(Context context, ProductCatalogAdapterOnClickHandler listener) {
        mContext = context;
        mClickHandler = listener;
    }

    @NonNull
    @Override
    public AdapterCatalogProductViewHolder onCreateViewHolder(
            @NonNull ViewGroup viewGroup, int viewType) {

        View view = LayoutInflater
                .from(mContext)
                .inflate(R.layout.list_item_product, viewGroup, false);

        return new AdapterCatalogProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterCatalogProductViewHolder holder, int position) {

        /* Get the product at the passed in position */
        Product product = mProducts.get(position);

        /* Set the description */
        holder.descriptionTV.setText(product.getDescription());
        /* Set the created by me value */
        if(mUserId.equals(product.getCreatedBy())) {
            holder.createdByMeTv.setText(R.string.adapter_catalog_product_creator_yes);
        } else {
            holder.createdByMeTv.setText(R.string.adapter_catalog_product_creator_no);
        }

        /* Set the pack size */
        holder.packSizeTV.setText(String.valueOf(product.getPackSize()));
        /* Set the unit of measure */
        holder.UoMTV.setText(Converters.getUnitOfMeasureString
                (mContext, product.getUnitOfMeasure()));

        // Todo - Add a thumbnail of the product image
    }

    /* Returns the number of items in the adapter */
    @Override
    public int getItemCount() {
        if (mProducts == null) return 0;
        return mProducts.size();
    }

    /* Sets the user ID */
    public void setUserId(String userId) {
        mUserId = userId;
    }

    /* Getter for the current list of products */
    public List<Product> getProducts() {
        return mProducts;
    }

    /* When the data changes, this method updates the list of products and notifies the adapter to
    use the new values in it */
    public void setProducts(List<Product> products) {
        mProducts = products;
        notifyDataSetChanged();
        Log.e(LOG_TAG, "setProducts() called - Adapter updated.");
        int noOfItems = getItemCount();
        Log.e(LOG_TAG, "Adapter now has: " + noOfItems + " items");
    }

    /* Inserts a single product into the adapter */
    public void insertProduct(Product product){
        mProducts.add(product);
        notifyItemInserted(mProducts.size() -1);
    }

    /* Inner class for creating ViewHolders */
    class AdapterCatalogProductViewHolder
            extends
            RecyclerView.ViewHolder
            implements
            View.OnClickListener {

        final TextView descriptionTV;
        final TextView createdByMeTv;
        final TextView packSizeTV;
        final TextView UoMTV;

        /* Constructor for the AdapterCatalogProductViewHolder.class */
        AdapterCatalogProductViewHolder(View itemView) {
            super(itemView);

            descriptionTV = itemView.findViewById(R.id.list_item_product_description);
            createdByMeTv = itemView.findViewById(R.id.list_item_product_added_by_me);
            packSizeTV = itemView.findViewById(R.id.list_item_product_pack_size);
            UoMTV = itemView.findViewById(R.id.list_item_product_unit_of_measure);

            itemView.setOnClickListener(this);
        }

        public void onClick(View v) {
            // Get the product from the adapter at the clicked position
            Product product = mProducts.get(getAdapterPosition());
            // Find out if this user was the creator of the product
            mIsCreator = mUserId.equals(product.getCreatedBy());
            // Send the product and its creator bool to be processed by the click handler
            mClickHandler.onClick(product, mIsCreator);
        }
    }
}



