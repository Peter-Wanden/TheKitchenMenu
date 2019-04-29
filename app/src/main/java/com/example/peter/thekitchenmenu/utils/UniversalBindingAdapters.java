package com.example.peter.thekitchenmenu.utils;

import android.util.Log;
import android.widget.ImageView;

import androidx.databinding.BindingAdapter;

import com.bumptech.glide.Glide;
import com.example.peter.thekitchenmenu.R;

public class UniversalBindingAdapters {

    private static final String TAG = "UniversalBindingAdapter";

    @BindingAdapter(value = {"bind:localImageUri", "bind:remoteImageUri"}, requireAll = false)
    public static void setImage(ImageView imageView, String localImageUri, String remoteImageUri) {

        String uri;

        if (remoteImageUri != null && !remoteImageUri.isEmpty()) uri = remoteImageUri;
        else if (localImageUri != null && !localImageUri.isEmpty()) uri = localImageUri;
        else uri = "";

        if (localImageUri != null) Log.d(TAG, "tkm - setImage: local uri is: " + localImageUri);
        if (remoteImageUri != null) Log.d(TAG, "tkm - setImage: remote uri is: " + remoteImageUri);
        Log.d(TAG, "tkm - setImage: uri is:" + uri);

        Glide.with(imageView.getContext())
                .load(uri)
                .placeholder(R.drawable.placeholder)
                .centerCrop()
                .into(imageView);
    }
}