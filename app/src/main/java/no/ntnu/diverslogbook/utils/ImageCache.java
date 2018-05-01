package no.ntnu.diverslogbook.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Stores, loads and manages images from DiskCache.
 *
 * @author Lars Johan
 */
public abstract class ImageCache {

    /**
     * Store an image in cache
     * @param context The application context
     * @param key The name of the cached file to save
     * @param bitmap The bitmap to save
     */
    public static void put(Context context, String key, Bitmap bitmap) {
        try {
            File image = new File(context.getCacheDir(), key);
            FileOutputStream fos = new FileOutputStream(image);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);
            fos.flush();
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Load an image from cache, if present
     * @param context The application context
     * @param key The name of the cached file to load
     * @return The cached bitmap or null
     */
    public static Bitmap get(Context context, String key) {
        try {
            File image = new File(context.getCacheDir(), key);
            FileInputStream is = new FileInputStream(image);
            Bitmap loadedImage = BitmapFactory.decodeStream(is);
            is.close();
            return loadedImage;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
