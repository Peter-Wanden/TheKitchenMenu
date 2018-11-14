package com.example.peter.thekitchenmenu.ui.catalog;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.example.peter.thekitchenmenu.R;
import com.example.peter.thekitchenmenu.data.model.Product;
import com.example.peter.thekitchenmenu.data.model.ProductCommunity;
import com.example.peter.thekitchenmenu.utils.Converters;
import com.squareup.picasso.Picasso;

import java.util.List;

public class AdapterCatalogProduct
        extends
        RecyclerView.Adapter<AdapterCatalogProduct.AdapterCatalogProductViewHolder> {

    private static final String LOG_TAG = AdapterCatalogProduct.class.getSimpleName();

    // The context we use for utility methods, app resources and layout inflaters
    private final Context mContext;

    // List storing the database query result
    private List<Product> mProducts;

    // The user Id of the current user
    private String mUserId;

    // Click interface
    final private AdapterCatalogProductClickHandler mProductClickHandler;

    /* Constructor */
    public AdapterCatalogProduct(
            Context context,
            AdapterCatalogProductClickHandler clickHandler) {

        mContext = context;
        mProductClickHandler = clickHandler;
    }

    /* View holder */
    @NonNull
    @Override
    public AdapterCatalogProductViewHolder onCreateViewHolder(
            @NonNull ViewGroup viewGroup,
            int viewType) {

        View view = LayoutInflater
                .from(mContext)
                .inflate(R.layout.list_item_product, viewGroup, false);

        return new AdapterCatalogProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(
            @NonNull AdapterCatalogProductViewHolder holder,
            int position) {

        /* Get the product at the passed in position */
        Product product = mProducts.get(position);

        /* Set the description */
        holder.descriptionTV.setText(product.getDescription());

        /* Get and set the image */
        if (!product.getFbStorageImageUri().equals("")) {
            Picasso.get().load(product.getFbStorageImageUri()).into(holder.productIV);
        } else {
            Picasso.get().load(R.drawable.placeholder).into(holder.productIV);
        }

        /* Set the pack size */
        holder.packSizeTV.setText(String.valueOf(product.getPackSize()));

        /* Set the unit of measure */
        holder.UoMTV.setText(Converters.getUnitOfMeasureString
                (mContext, product.getUnitOfMeasure()));
    }

    /* Returns the number of items in the adapter */
    @Override
    public int getItemCount() {
        if (mProducts == null) return 0;
        return mProducts.size();
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
    }

    /* Inner class for creating ViewHolders */
    class AdapterCatalogProductViewHolder
            extends
            RecyclerView.ViewHolder
            implements
            View.OnClickListener {

        final TextView descriptionTV;
        final TextView packSizeTV;
        final TextView UoMTV;
        final ImageView productIV;

        /* Constructor for the AdapterCatalogProductViewHolder.class */
        AdapterCatalogProductViewHolder(View itemView) {
            super(itemView);

            descriptionTV = itemView.findViewById(R.id.list_item_product_tv_description);
            packSizeTV = itemView.findViewById(R.id.list_item_product_tv_pack_size);
            UoMTV = itemView.findViewById(R.id.list_item_product_tv_label_unit_of_measure);
            productIV = itemView.findViewById(R.id.list_item_product_iv_product_image);

            itemView.setOnClickListener(this);
        }

        public void onClick(View v) {

            // Get the product from the adapter at the clicked position
            Product product = mProducts.get(getAdapterPosition());

            // Find out if this user was the creator of the product
            boolean isCreator = mUserId.equals(product.getCreatedBy());

            mProductClickHandler.onClick(product, isCreator);
        }
    }

    /* Interface that executes the onProductClicked method in the host Activity / Fragment. */
    public interface AdapterCatalogProductClickHandler {
        void onClick (Product clickedProduct, boolean isCreator);
    }

    /* Setter for the user Id. The setting of this field is essential. */
    public void setUserId (String userId){
        mUserId = userId;
    }
}




