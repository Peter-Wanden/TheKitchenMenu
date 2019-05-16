package com.example.peter.thekitchenmenu.utils.imageeditor;

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
import android.widget.ImageView;
import android.widget.Toast;

import com.example.peter.thekitchenmenu.R;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import androidx.loader.content.CursorLoader;

public class BitmapUtils {

    private static final String TAG = "BitmapUtils";
    // TODO - Use ForkJoinPool for a threaded implementation
    // TODO - See: https://docs.oracle.com/javase/tutorial/essential/concurrency/forkjoin.html
    // TODO - Can also use a callback if fast enough

    private BitmapUtils() {
        // Suppress default constructor for noninstantiability
        throw new AssertionError();
    }

    /**
     * Creates the temporary image file in the cache directory.
     *
     * @return The temporary image file.
     * @throws IOException Thrown if there is an error creating the file
     */
    public static File createTemporaryImageFile(Context context, String prefix) throws IOException {

        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss",
                Locale.getDefault()).format(new Date());

        String imageFileName = prefix + timeStamp + "_";

        File storageDir = context.getCacheDir();
        Log.d(TAG, "tkm - cache directory is: " + storageDir);
        Log.d(TAG, "tkm - Temp file name is: " + imageFileName);

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
     * @param imagePath The path of the photo to be resampled.
     * @return The re-sampled bitmap
     */
    public static Bitmap resamplePic(Context context, String imagePath) {

        // Get device screen size information
        DisplayMetrics metrics = new DisplayMetrics();
        WindowManager manager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        manager.getDefaultDisplay().getMetrics(metrics);

        int targetH = metrics.heightPixels;
        int targetW = metrics.widthPixels;

        // Get the dimensions of the original bitmap
        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        bmOptions.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(imagePath, bmOptions);
        int photoW = bmOptions.outWidth;
        int photoH = bmOptions.outHeight;

        // Determine how much to scale down the image
        int scaleFactor = Math.min(photoW / targetW, photoH / targetH);

        // Decode the image file into a Bitmap sized to fill the View
        bmOptions.inJustDecodeBounds = false;
        bmOptions.inSampleSize = scaleFactor;

        return BitmapFactory.decodeFile(imagePath);
    }

    public static Bitmap createScaledBitmap(Bitmap image, float imageSize, boolean filter) {

        float ratio = Math.min(imageSize / image.getWidth(), imageSize / image.getHeight());
        int width = Math.round(ratio * image.getWidth());
        int height = Math.round(ratio * image.getHeight());

        return Bitmap.createScaledBitmap(image, width, height, filter);
    }

    public static boolean saveBitmapToCache(Bitmap scaledBitMap, File imageFile) {

        try (FileOutputStream out = new FileOutputStream(imageFile)) {

            // Save the cropped bitmap to its image file
            scaledBitMap.compress(Bitmap.CompressFormat.JPEG, 80, out);
            return true;

        } catch (IOException e) {

            e.printStackTrace();
            return false;
        }
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
        } else Log.d(TAG, "tkm - deleteImageFile: Image file deleted");
    }

    /* As bitmap factory will only accept a path and not a Uri we have to do this. */
    // Credit: https://stackoverflow.com/questions/3401579/get-filename-and-path-from-uri-from-mediastore?rq=1
    public static String getAbsolutePathFromUri(Context context, Uri fileUri) {

        String[] projection = {MediaStore.Images.Media.DATA};

        CursorLoader loader = new CursorLoader(
                context,
                fileUri,
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
