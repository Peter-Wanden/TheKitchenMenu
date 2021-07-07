package com.example.peter.thekitchenmenu.ui.catalog.product;

import android.app.SearchManager;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.example.peter.thekitchenmenu.R;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class SearchProducts extends AppCompatActivity {

    private static final String TAG = "tkm-" + SearchProducts.class.getSimpleName() + ":";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_products);

        handleIntent(getIntent());
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        handleIntent(getIntent());
    }

    private void handleIntent(Intent intent) {

        // Gets the originating class contextual data
        Bundle b = intent.getBundleExtra(SearchManager.APP_DATA);

        // Verifies the intent action
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {

            System.out.println(TAG + "tkm - Intent action is SEARCH");
            System.out.println(TAG + "tkm - Query is: " + intent.getStringExtra(SearchManager.QUERY));
            System.out.println(TAG + "tkm - Originating class is: " + b.getString(TAG));
            System.out.println(TAG + "tkm - Intent extra data is: " + intent.getData());

        } else if (Intent.ACTION_VIEW.equals(intent.getAction())) {

            System.out.println(TAG + "tkm - Intent action is VIEW" );
            System.out.println(TAG + "tkm - Query is: " + intent.getStringExtra(SearchManager.QUERY));
            System.out.println(TAG + "tkm - Originating class is: " + b.getString(TAG));
            System.out.println(TAG + "tkm - Intent extra data is: " + intent.getData());
        }
    }
}
