package com.example.peter.thekitchenmenu.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.peter.thekitchenmenu.R;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import androidx.loader.content.CursorLoader;

public class BitmapUtils {
    // TODO - Use ForkJoinPool for a threaded implementation
    // TODO - See: https://docs.oracle.com/javase/tutorial/essential/concurrency/forkjoin.html

    private static final String TAG = BitmapUtils.class.getSimpleName();

    /* Image capture - Create unique file name */
    public static File createImageFile(Context context) throws IOException {

        @SuppressLint("SimpleDateFormat")
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss",
                Locale.getDefault()).format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";

        File storageDir = context.getExternalCacheDir();

        // Create temp file
        return File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",   /* suffix */
                storageDir      /* directory */
        );
    }

    /**
     * Re-samples the captured photo to fit the screen for better memory usage.
     *
     * @param context   The application context.
     * @param uri The Uri of the image to be re-sampled.
     * @return The re-sampled bitmap
     */
    public static Bitmap resampleImage(Context context, Uri uri, String imagePath) {

        Log.d(TAG, "tkm - resampleImage: image path is: " + imagePath);

        /* If imagePath is null we have been given a Uri, so decode to string */
        if (imagePath == null) {
            // Extract the image path from the Uri
            imagePath = getAbsolutePathFromUri(context, uri);
        }

        // Get device screen size information
        DisplayMetrics metrics = new DisplayMetrics();

        WindowManager manager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);

        if (manager != null) {

            manager.getDefaultDisplay().getMetrics(metrics);
        }

        int targetH = metrics.heightPixels;
        int targetW = metrics.widthPixels;

        // Get the dimensions of the original bitmap
        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        bmOptions.inJustDecodeBounds = true;

        BitmapFactory.decodeFile(imagePath, bmOptions);
        int imageW = bmOptions.outWidth;
        int imageH = bmOptions.outHeight;

        // Determine how much to scale down the image
        int scaleFactor = Math.min((imageW / targetW) / 2, (imageH / targetH) /2);

        // Decode the image file into a Bitmap sized to fill the View
        bmOptions.inJustDecodeBounds = false;
        bmOptions.inSampleSize = scaleFactor;

        return BitmapFactory.decodeFile(imagePath);
    }

    public static void deleteImageFile(Context context, String imagePath) {

        // Get the file
        File imageFile = new File(imagePath);

        // Delete the image
        boolean deleted = imageFile.delete();

        // If there is an error deleting the file, show a Toast
        if (!deleted) {
            String errorMessage = context.getString(R.string.delete_file_error);
            Toast.makeText(context, errorMessage, Toast.LENGTH_SHORT).show();
        }
    }

    /* As bitmap factory will only accept a path and not a Uri we have to do this. */
    // Credit: https://stackoverflow.com/questions/3401579/get-filename-and-path-from-uri-from-mediastore?rq=1
    public static String getAbsolutePathFromUri(Context context, Uri productImageUri) {

        String[] projection = {MediaStore.Images.Media.DATA};

        CursorLoader loader = new CursorLoader(
                context,
                productImageUri,
                projection,
                null,
                null,
                null);

        Cursor cursor = loader.loadInBackground();

        int column_index;

        if (cursor != null) {

            column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            String result = cursor.getString(column_index);
            cursor.close();

            return result;
        }

        return null;
    }

    public static void rotateImage(ImageView imageView) {

        Matrix matrix = new Matrix();
        matrix.postRotate(90);

        Bitmap productImage = ((BitmapDrawable) imageView.
                getDrawable()).
                getBitmap();

        Bitmap rotated = Bitmap.createBitmap(productImage, 0, 0,
                productImage.getWidth(), productImage.getHeight(), matrix, true);

        imageView.setImageBitmap(rotated);
    }
}
