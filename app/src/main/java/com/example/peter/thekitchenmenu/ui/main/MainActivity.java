package com.example.peter.thekitchenmenu.ui.main;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import com.example.peter.thekitchenmenu.R;
import com.example.peter.thekitchenmenu.data.databaseRemote.RemoteSignIn;
import com.example.peter.thekitchenmenu.ui.catalog.ProductCatalogMain;
import com.google.android.material.navigation.NavigationView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    RemoteSignIn remoteSignIn;

    private DrawerLayout drawerLayout;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        remoteSignIn = new RemoteSignIn(this);

        initialiseViews();
    }

    private void initialiseViews() {

        Toolbar toolbar = findViewById(R.id.main_toolbar);
        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeAsUpIndicator(R.drawable.ic_menu);

        drawerLayout = findViewById(R.id.drawer_layout);

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(menuItem -> {
            menuItem.setChecked(true);
            drawerLayout.closeDrawers();

            // Add code here to update the UI based on the item selected
            // For example, swap UI fragments here
            switch (menuItem.getItemId()) {
                case R.id.sign_out:
                    remoteSignIn.signOut(this);
                    return true;
                case R.id.nav_products:
                    launchProductActivity();
                    return true;
            }
            return true;
        });
    }

    void launchProductActivity() {
        Intent launchProductActivity = new Intent(
                MainActivity.this, ProductCatalogMain.class);
        startActivity(launchProductActivity);
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                drawerLayout.openDrawer(GravityCompat.START);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        super.onResume();
        remoteSignIn.authStateListener(true);
    }

    @Override
    protected void onPause() {
        super.onPause();
        remoteSignIn.authStateListener(false);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        remoteSignIn.signInResult(requestCode, resultCode, data);
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
    }
}
