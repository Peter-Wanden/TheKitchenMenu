package com.example.peter.thekitchenmenu.data.repository;

import android.content.Context;

import androidx.annotation.NonNull;

import com.example.peter.thekitchenmenu.app.AppExecutors;
import com.example.peter.thekitchenmenu.data.source.local.ProductLocalDataSource;
import com.example.peter.thekitchenmenu.data.source.local.TKMDatabase;
import com.example.peter.thekitchenmenu.data.source.remote.ProductRemoteDataSource;

import static androidx.core.util.Preconditions.checkNotNull;

public class DatabaseInjection {

    public static ProductRepository provideProductsRepository(@NonNull Context context) {
        checkNotNull(context);
        TKMDatabase database = TKMDatabase.getInstance(context, new AppExecutors());
        return ProductRepository.getInstance(ProductRemoteDataSource.getInstance(),
                ProductLocalDataSource.getInstance(
                        new AppExecutors(), database.productEntityDao()));
    }
}