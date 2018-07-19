package com.example.peter.thekitchenmenu.ui.navigation;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.peter.thekitchenmenu.R;
import com.example.peter.thekitchenmenu.ui.list.ProductCatalogActivity;

public class MainActivity extends AppCompatActivity {

    Button addProductButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        addProductButton = findViewById(R.id.activity_main_add_product);

        addProductButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getBaseContext(), ProductCatalogActivity.class);
                startActivity(intent);
            }
        });
    }
}
