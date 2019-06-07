package com.example.peter.thekitchenmenu.utils.imageeditor;

import android.util.Log;
import android.widget.ImageView;

import androidx.databinding.BindingAdapter;

import com.bumptech.glide.Glide;
import com.example.peter.thekitchenmenu.R;

public class ImageBindingAdapters {

    private static final String TAG = "tkm-ImageBindingAdapter";

    @BindingAdapter(value = {
            "app:localLargeImageUri",
            "app:remoteLargeImageUri",
            "app:webImageUrl"},
            requireAll = false)
    public static void setImage(ImageView imageView,
                         String localUri,
                         String remoteUri,
                         String webUrl) {

        String uri = "";
        // TODO - Extract this business logic
        if (remoteUri != null && !remoteUri.isEmpty()) uri = remoteUri;
        else if (localUri != null && !localUri.isEmpty()) uri = localUri;
        else if (webUrl != null && !webUrl.isEmpty()) uri = webUrl;

        setImageToView(imageView, uri);
    }

    @BindingAdapter(value = {
            "app:remoteSmallImageUri",
            "app:webImageUrl"},
            requireAll = false)
    public static void setSmallImage(ImageView imageView,
                                     String smallImageUri,
                                     String webUrl) {
        String uri = "";
        if (smallImageUri != null && !smallImageUri.isEmpty()) uri = smallImageUri;
        else if (webUrl != null && !webUrl.isEmpty()) uri = webUrl;

        setImageToView(imageView, uri);
    }

    private static void setImageToView(ImageView imageView, String uri) {
        Glide.with(imageView.getContext())
                .load(uri)
                .placeholder(R.drawable.placeholder)
                .into(imageView);

    }
}