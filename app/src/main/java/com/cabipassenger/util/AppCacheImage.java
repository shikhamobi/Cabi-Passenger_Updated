package com.cabipassenger.util;

import android.graphics.Bitmap;
import android.support.v4.util.LruCache;
import android.widget.ImageView;

/**
 *this class convert the image to bitmap
 */
public class AppCacheImage {
    private static LruCache<String, Bitmap> mMemoryCache;

    public AppCacheImage() {

    }

    static {
        // Get max available VM memory, exceeding this amount will throw an
        // OutOfMemory exception. Stored in kilobytes as LruCache takes an
        // int in its constructor.
        final int maxMemory = (int) (Runtime.getRuntime().maxMemory() / 1024);

        // Use 1/8th of the available memory for this memory cache.
        final int cacheSize = maxMemory / 8;

        mMemoryCache = new LruCache<String, Bitmap>(cacheSize) {
            @Override
            protected int sizeOf(String key, Bitmap bitmap) {
                // The cache size will be measured in kilobytes rather than
                // number of items.
                return bitmap.getByteCount() / 1024;
            }
        };
    }
/**
 * this method is used to store the image to bitmap cache
 */

    public static void addBitmapToMemoryCache(String key, Bitmap bitmap) {
        if (key != null && bitmap != null)
            if (getBitmapFromMemCache(key) == null) {
                //System.out.println("Image... storing cache" + key);
                mMemoryCache.put(key, bitmap);
            }
    }
    /**
     * this method is used to get bitmap from cache
     * @return memory cache key
     */
    public static Bitmap getBitmapFromMemCache(String key) {
        return mMemoryCache.get(key);
    }

    public static boolean loadBitmap(String resId, ImageView imageView) {
//        final String imageKey = String.valueOf(resId);

        boolean imageAvailable = false;
        if (resId != null && imageView != null) {
            final String imageKey = resId;
            final Bitmap bitmap = getBitmapFromMemCache(imageKey);
            if (bitmap != null) {
              //  System.out.println("Image... Loaded from cache");
                imageView.setImageBitmap(bitmap);
                imageAvailable = true;
            }
        }
        return imageAvailable;
    }


}

