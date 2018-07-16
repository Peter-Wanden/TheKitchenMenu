package com.example.peter.thekitchenmenu.ui.catalog;

import android.content.Context;
import android.database.Cursor;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.example.peter.thekitchenmenu.R;
import com.example.peter.thekitchenmenu.data.TKMContract;

public class ProductCatalogAdapter
        extends
        RecyclerView.Adapter<ProductCatalogAdapter.ProductCatalogAdapterViewHolder> {

    /* The context we use to utility methods, app resources and layout inflaters */
    private final Context mContext;

    /* Cursor storing the database qurey result */
    private Cursor mCursor;

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

        int layoutForThisItem = R.layout.list_item_product;

        View view = LayoutInflater
                .from(mContext)
                .inflate(layoutForThisItem, viewGroup, false);

        return new ProductCatalogAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductCatalogAdapterViewHolder holder, int position) {

        /* Get the product at the passed in position */
        mCursor.moveToPosition(position);

    }

    /* Returns the number of items in the adapter */
    @Override
    public int getItemCount() {
        if (null == mCursor) return 0;
        return mCursor.getCount();
    }

    /* Swaps out the adapters cursor data */
    public void swapCursor (Cursor newCursor) {
        mCursor = newCursor;
        notifyDataSetChanged();
    }

    class ProductCatalogAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        final TextView descriptionTV;
        final TextView retailerTV;
        final TextView packSizeTV;
        final TextView UoMTV;

        ProductCatalogAdapterViewHolder(View itemView) {
            super(itemView);

            descriptionTV = itemView.findViewById(R.id.list_item_product_description);
            retailerTV = itemView.findViewById(R.id.list_item_product_retailer);
            packSizeTV = itemView.findViewById(R.id.list_item_product_pack_size);
            UoMTV = itemView.findViewById(R.id.list_item_product_unit_of_measure);

            itemView.setOnClickListener(this);
        }

        public void onClick(View v) {
            int adapterPosition = getAdapterPosition();
            mCursor.moveToPosition(adapterPosition);
            int productId = mCursor.getInt(mCursor.getColumnIndex(TKMContract.ProductEntry._ID));
            mClickHandler.onClick(productId);
        }
    }
}



