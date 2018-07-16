package com.example.peter.thekitchenmenu.ui.catalog;

import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v4.content.Loader;
import android.support.v4.app.LoaderManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;

import com.example.peter.thekitchenmenu.R;

public class ProductCatalogActivity
        extends
        AppCompatActivity
        implements
        LoaderManager.LoaderCallbacks<Cursor>,
        ProductCatalogAdapter.ProductCatalogAdapterOnClickHandler {

    /* Identifier for the Product data loader */
    private static final int PRODUCT_LOADER = 100;

    /* Adapter for the product list view */
    public ProductCatalogAdapter mCatalogAdapter;

    /* RecyclerView for this activity */
    private RecyclerView mRecyclerView;

    /* Progress bar shown when loading data */
    private ProgressBar mLoadingIndicator;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_catalog);

        /* Get a reference to the views */
        mRecyclerView = findViewById(R.id.product_catalog_rv);
        mLoadingIndicator = findViewById(R.id.activity_product_catalog_loading_indicator);

        /* Create and set the layout manager */
        LinearLayoutManager layoutManager =
                new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setHasFixedSize(true);

        /* Create the adapter and pass in the this class context and the listener (which is also
        this class as this class implements the click handler. */
        mCatalogAdapter = new ProductCatalogAdapter(this, this);

        showLoading();

        getSupportLoaderManager().initLoader(PRODUCT_LOADER, null, this);


    }

    @NonNull
    @Override
    public Loader onCreateLoader(int i, @Nullable Bundle bundle) {
        return null;
    }

    @Override
    public void onLoadFinished(@NonNull Loader<Cursor> loader, Cursor cursor) {

    }

    @Override
    public void onLoaderReset(@NonNull Loader loader) {

    }

    @Override
    public void onClick(int productId) {

    }

    /* Shows the loading indicator and hides other views */
    private void showLoading() {
        mRecyclerView.setVisibility(View.INVISIBLE);
        mLoadingIndicator.setVisibility(View.VISIBLE);
    }

    /* Hides the loading indicator and shows other views */
    private void showProducts () {
        mLoadingIndicator.setVisibility(View.INVISIBLE);
        mRecyclerView.setVisibility(View.VISIBLE);
    }
}
