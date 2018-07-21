package com.example.peter.thekitchenmenu.ui.list;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.example.peter.thekitchenmenu.R;
import com.example.peter.thekitchenmenu.model.Product;

import java.util.List;

public class ProductCatalogAdapter
        extends
        RecyclerView.Adapter<ProductCatalogAdapter.ProductCatalogAdapterViewHolder> {

    /* The context we use to utility methods, app resources and layout inflaters */
    private final Context mContext;

    /* List storing the database query result */
    private List<Product> mProducts;

    /* Click handler */
    private final ProductCatalogAdapterOnClickHandler mClickHandler;

    /* Click interface that receives on click messages */
    public interface ProductCatalogAdapterOnClickHandler {
        void onClick(int productId);
    }

    public ProductCatalogAdapter(Context context, ProductCatalogAdapterOnClickHandler listener) {
        mContext = context;
        mClickHandler = listener;
    }

    @NonNull
    @Override
    public ProductCatalogAdapterViewHolder onCreateViewHolder(
            @NonNull ViewGroup viewGroup, int viewType) {

        View view = LayoutInflater
                .from(mContext)
                .inflate(R.layout.list_item_product, viewGroup, false);

        return new ProductCatalogAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductCatalogAdapterViewHolder holder, int position) {

        /* Get the product at the passed in position */
        Product product = mProducts.get(position);

        /* Set the description */
        holder.descriptionTV.setText(product.getDescription());
        /* Set the retailer */
        holder.retailerTV.setText(product.getRetailer());
        /* Set the pack size */
        holder.packSizeTV.setText(String.valueOf(product.getPackSize()));
        /* Set the unit of measure */
        holder.UoMTV.setText(getStringUnitOfMeasure(product.getUnitOfMeasure()));

    }

    /* Helper method to convert the unit of measure from an integer to a String value */
    private String getStringUnitOfMeasure(int requestUnitOfMeasure) {

        String unitOfMeasure;

        switch (requestUnitOfMeasure) {
            case 1:
                unitOfMeasure = mContext.getResources().getString(R.string.uom_option_1);
                break;
            case 2:
                unitOfMeasure = mContext.getResources().getString(R.string.uom_option_2);
                break;
            case 3:
                unitOfMeasure = mContext.getResources().getString(R.string.uom_option_3);
                break;
            default:
                unitOfMeasure = mContext.getResources().getString(R.string.uom_option_0);
                break;
        }
        return unitOfMeasure;
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
    class ProductCatalogAdapterViewHolder
            extends
            RecyclerView.ViewHolder
            implements
            View.OnClickListener {

        final TextView descriptionTV;
        final TextView retailerTV;
        final TextView packSizeTV;
        final TextView UoMTV;

        /* Constructor for the ProductCatalogAdapterViewHolder.class */
        ProductCatalogAdapterViewHolder(View itemView) {
            super(itemView);

            descriptionTV = itemView.findViewById(R.id.list_item_product_description);
            retailerTV = itemView.findViewById(R.id.list_item_product_retailer);
            packSizeTV = itemView.findViewById(R.id.list_item_product_pack_size);
            UoMTV = itemView.findViewById(R.id.list_item_product_unit_of_measure);

            itemView.setOnClickListener(this);
        }

        public void onClick(View v) {
            int productId = mProducts.get(getAdapterPosition()).getProductId();
            mClickHandler.onClick(productId);
        }
    }
}



