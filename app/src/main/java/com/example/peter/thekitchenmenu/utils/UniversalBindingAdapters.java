package com.example.peter.thekitchenmenu.utils;

import android.widget.ImageView;

import androidx.databinding.BindingAdapter;

import com.bumptech.glide.Glide;
import com.example.peter.thekitchenmenu.R;

public class UniversalBindingAdapters {

    private static final String TAG = "UniversalBindingAdapter";

    @BindingAdapter(value = {
            "app:localImageUri",
            "app:remoteImageUri",
            "app:webImageUrl"},
            requireAll = false)
    public static void setImage(ImageView imageView,
                         String localUri,
                         String remoteUri,
                         String webUrl) {

        String uri;

        // TODO - Extract this business logic
        if (remoteUri != null && !remoteUri.isEmpty()) uri = remoteUri;
        else if (localUri != null && !localUri.isEmpty()) uri = localUri;
        else if (webUrl != null && !webUrl.isEmpty()) uri = webUrl;
        else uri = "";

        Glide.with(imageView.getContext())
                .load(uri)
                .placeholder(R.drawable.placeholder)
                .into(imageView);
    }
}