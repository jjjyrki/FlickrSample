package com.jyri.flickr.sample.utils.image;

import android.graphics.Bitmap;
import android.util.LruCache;

/**
 *  Singleton bitmap cache for storing downloaded bitmaps.
 */

public class BitmapCache {

    private static BitmapCache mInstance;
    private LruCache<String, Bitmap> mMemoryCache;

    /**
     * Create cache for downloaded bitmaps with half of the size of the total available memory.
     * Singleton.
     */
    private BitmapCache() {
        final int maxMemory = (int) (Runtime.getRuntime().maxMemory() / 1024);
        final int cacheSize = maxMemory / 2;

        mMemoryCache = new LruCache<String, Bitmap>(cacheSize) {
            @Override
            protected int sizeOf(String key, Bitmap bitmap) {
                return bitmap.getByteCount() / 1024;
            }

        };
    }

    private static void init() {
        mInstance = new BitmapCache();
    }

    // Initialize signleton.
    public static BitmapCache getInstance() {
        if (mInstance == null) {
            mInstance.init();
        }

        return mInstance;
    }

    /**
     *  Add bitmap to cache if it doesn't exist there yet.
     */
    public void addBitmapToMemoryCache(String key, Bitmap bitmap) {
        if (getBitmapFromMemCache(key) == null) {
            mMemoryCache.put(key, bitmap);
        }
    }

    public Bitmap getBitmapFromMemCache(String key) {
        Bitmap bitmap = mMemoryCache.get(key);

        if (bitmap != null) {
            return bitmap;
        }
        return null;
    }

    /**
     *  Get the total size of the cache in KB.
     */
    public int getCacheSize() {
        return mMemoryCache.size();
    }

}
