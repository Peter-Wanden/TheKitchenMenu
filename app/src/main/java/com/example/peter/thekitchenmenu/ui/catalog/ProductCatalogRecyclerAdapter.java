package com.example.peter.thekitchenmenu.ui.catalog;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.peter.thekitchenmenu.R;
import com.example.peter.thekitchenmenu.app.Constants;
import com.example.peter.thekitchenmenu.data.model.ObservableProductModel;
import com.example.peter.thekitchenmenu.utils.Converters;
import com.squareup.picasso.Picasso;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class ProductCatalogRecyclerAdapter
        extends
        RecyclerView.Adapter<ProductCatalogRecyclerAdapter.AdapterCatProdViewHolder> {

    private static final String TAG = ProductCatalogRecyclerAdapter.class.getSimpleName();
    private final Context context;

    private List<ObservableProductModel> listObservableProductModel;

    final private OnClickProduct clickHandler;

    ProductCatalogRecyclerAdapter(Context context, OnClickProduct clickHandler) {
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

        /* Get the product_uneditable at the passed in position */
        ObservableProductModel observableProductModel = listObservableProductModel.get(position);

        /* Set the description */
        holder.descTV.setText(observableProductModel.getDescription());

        // TODO - Picasso, add image caching.
        /* Get and set the image */
        if (!observableProductModel.getRemoteImageUri().equals("")) {
            Picasso.get().load(observableProductModel.getRemoteImageUri()).into(holder.prodIV);
        } else {
            Picasso.get().load(R.drawable.placeholder).into(holder.prodIV);
        }

        /* Set the pack size */
        holder.packSizeTV.setText(String.valueOf(observableProductModel.getBaseSiUnits()));

        /* Set the unit of measure */
        holder.UoMTV.setText(Converters.getUnitOfMeasureString
                (context, observableProductModel.getUnitOfMeasureSubType()));
    }

    /* Returns the number of items in the adapter */
    @Override
    public int getItemCount() {
        if (listObservableProductModel == null) return 0;
        return listObservableProductModel.size();
    }

    /* Getter for the current list of products */
    public List<ObservableProductModel> getProducts() {
        return listObservableProductModel;
    }

    /*
     * When the data changes, this method updates the list of products and notifies the adapter to
     * use the new values in it
     */
    public void setProducts(List<ObservableProductModel> vmListProd) {
        listObservableProductModel = vmListProd;
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

            // Get the product_uneditable from the adapter at the clicked position
            ObservableProductModel observableProductModel = listObservableProductModel.get(getAdapterPosition());

            // Find out if this user was the creator of the product_uneditable
            boolean isCreator = Constants.getUserId().getValue().
                    equals(observableProductModel.getCreatedBy());

            // Click handler for this product_uneditable type
            clickHandler.onClick(observableProductModel, isCreator);
        }
    }
}




