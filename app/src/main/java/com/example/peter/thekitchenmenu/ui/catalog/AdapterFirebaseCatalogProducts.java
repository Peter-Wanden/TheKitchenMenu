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
import com.example.peter.thekitchenmenu.utils.Converters;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.squareup.picasso.Picasso;

/**
 * This Adapter talks directly to Firebase
 */
public class AdapterFirebaseCatalogProducts
        extends
        FirebaseRecyclerAdapter<
                Product,
                AdapterFirebaseCatalogProducts.AdapterFirebaseCatalogProductViewHolder> {

     static final String LOG_TAG = AdapterFirebaseCatalogProducts.class.getSimpleName();

    // The context we use for utility methods, app resources and layout inflaters
    private final Context mContext;

    // The user Id of the current user
    private String mUserId;

    // Click interface
    final private AdapterFirebaseCatalogProductsClickHandler mProductClickHandler;


    /**
     * Initialize a {@link RecyclerView.Adapter} that listens to a Firebase query. See
     * {@link FirebaseRecyclerOptions} for configuration options.
     *
     * @param options
     */
    public AdapterFirebaseCatalogProducts(
            Context context,
            AdapterFirebaseCatalogProductsClickHandler productClickHandler,
            @NonNull FirebaseRecyclerOptions options) {

        super(options);
        mContext = context;
        mProductClickHandler = productClickHandler;
    }

    @Override
    protected void onBindViewHolder(
            @NonNull AdapterFirebaseCatalogProductViewHolder holder,
            int position,
            @NonNull Product product) {


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

    @NonNull
    @Override
    public AdapterFirebaseCatalogProductViewHolder onCreateViewHolder(
            @NonNull ViewGroup parent,
            int viewType) {

        View view = LayoutInflater
                .from(mContext)
                .inflate(R.layout.list_item_product, parent, false);

        return new AdapterFirebaseCatalogProductViewHolder(view);
    }

    /* Inner class for creating ViewHolders */
    class AdapterFirebaseCatalogProductViewHolder
            extends
            RecyclerView.ViewHolder
            implements
            View.OnClickListener {

        final TextView descriptionTV;
        final TextView packSizeTV;
        final TextView UoMTV;
        final ImageView productIV;

        /* Constructor for the AdapterCatalogProductViewHolder.class */
        AdapterFirebaseCatalogProductViewHolder(View itemView) {
            super(itemView);

            descriptionTV = itemView.findViewById(R.id.list_item_product_tv_description);
            packSizeTV = itemView.findViewById(R.id.list_item_product_tv_pack_size);
            UoMTV = itemView.findViewById(R.id.list_item_product_tv_label_unit_of_measure);
            productIV = itemView.findViewById(R.id.list_item_product_iv_product_image);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {

        }
    }

    /* Interface that executes the onProductClicked method in the host Activity / Fragment. */
    public interface AdapterFirebaseCatalogProductsClickHandler {
        void onClick (Product clickedProduct, boolean isCreator);
    }

    /* Setter for the user Id. The setting of this field is essential. */
    public void setUserId (String userId){
        mUserId = userId;
    }
}
