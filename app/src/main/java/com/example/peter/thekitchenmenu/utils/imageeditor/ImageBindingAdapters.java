package com.example.peter.thekitchenmenu.utils.imageeditor;

import android.util.Log;
import android.widget.ImageView;

import androidx.databinding.BindingAdapter;

import com.bumptech.glide.Glide;
import com.example.peter.thekitchenmenu.R;

public class ImageBindingAdapters {

    private static final String TAG = "UniversalBindingAdapter";

    @BindingAdapter(value = {
            "app:localLargeImageUri",
            "app:remoteLargeImageUri",
            "app:webImageUrl"},
            requireAll = false)
    public static void setImage(ImageView imageView,
                         String localUri,
                         String remoteUri,
                         String webUrl) {

        String uri;

        Log.d(TAG, "tkm - setImage: uri's received: " +
                "\nlocal:" + localUri +
                "\nremote: " + remoteUri +
                "\nweb: " + webUrl);

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