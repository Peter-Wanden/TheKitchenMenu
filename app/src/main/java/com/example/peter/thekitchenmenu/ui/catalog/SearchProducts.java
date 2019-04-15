package com.example.peter.thekitchenmenu.ui.catalog;

import android.app.SearchManager;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.example.peter.thekitchenmenu.R;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class SearchProducts extends AppCompatActivity {

    private static final String TAG = SearchProducts.class.getSimpleName();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_prod);

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

            Log.e(TAG, "tkm - Intent action is SEARCH");
            Log.e(TAG, "tkm - Query is: " + intent.getStringExtra(SearchManager.QUERY));
            Log.e(TAG, "tkm - Originating class is: " + b.getString(TAG));
            Log.e(TAG, "tkm - Intent extra data is: " + intent.getData());

        } else if (Intent.ACTION_VIEW.equals(intent.getAction())) {

            Log.d(TAG, "tkm - Intent action is VIEW" );
            Log.d(TAG, "tkm - Query is: " + intent.getStringExtra(SearchManager.QUERY));
            Log.d(TAG, "tkm - Originating class is: " + b.getString(TAG));
            Log.e(TAG, "tkm - Intent extra data is: " + intent.getData());
        }
    }
}
