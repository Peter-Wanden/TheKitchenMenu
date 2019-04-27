package com.example.peter.thekitchenmenu.utils;

import android.widget.ImageView;

import androidx.databinding.BindingAdapter;

import com.bumptech.glide.Glide;
import com.example.peter.thekitchenmenu.R;

public class UniversalBindingAdapters {

    @BindingAdapter(value = {"bind:localImageUri", "bind:remoteImageUri"}, requireAll = false)
    public static void setImage(ImageView imageView, String localImageUri, String remoteImageUri) {

        String uri;

        if (remoteImageUri == null) uri = localImageUri;
        else uri = remoteImageUri;

        Glide.with(imageView.getContext())
                .load(uri)
                .placeholder(R.drawable.placeholder)
                .centerCrop()
                .into(imageView);
    }
}