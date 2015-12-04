package com.jyri.flickr.sample.utils.downloader;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

import com.jyri.flickr.sample.utils.image.BitmapCache;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.BinaryHttpResponseHandler;

import org.apache.http.Header;

/**
 *
 *  Class for downloading Bitmaps from URLs.
 *  Invoke the downloader with static method downloadBitmap().
 */

public class BitmapDownloader {

    public static final int DOWNLOAD_SUCCESS = 1;
    public static final int DOWNLOAD_FAILED = 2;
    public static final int ALREADY_DOWNLOADED = 3;

    private ProgressBar mProgressBar;


    private BitmapDownloader() {
    }

    public static void downloadBitmap(final ProgressBar progressBar, final String url, final Handler h) {
        BitmapDownloader downloader = new BitmapDownloader(progressBar);
        downloader.downloadBitmapFromURL(url, h);
    }

    private BitmapDownloader(final ProgressBar progressBar) {
        mProgressBar = progressBar;
    }

    private void setProgressBarActive(boolean active) {
        if (mProgressBar != null) {
            if (active) {
                mProgressBar.setVisibility(View.VISIBLE);
            } else {
                mProgressBar.setVisibility(View.GONE);
            }
        }
    }

    private Bitmap decodeToBitmap(byte[] binaryData) throws Exception {

        BitmapFactory.Options opts = new BitmapFactory.Options();
        opts.inSampleSize = 2;

        Bitmap bitmap = BitmapFactory.decodeByteArray(binaryData, 0, binaryData.length, opts);

        if (bitmap == null) {
            throw new Exception("Error decoding binary data to bitmap.");
        } else
        {
            return bitmap;
        }
    }

    private void handleLoadedData(String url, byte[] binaryData, final Handler h) {
        BitmapCache provider = BitmapCache.getInstance();

        try {
            provider.addBitmapToMemoryCache(url, decodeToBitmap(binaryData));
        } catch (Exception e) {
            Log.e(getClass().getSimpleName(), "HandleLoadedData threw " + e.getMessage());

        } finally {
            setProgressBarActive(false);
            Message msg = h.obtainMessage(DOWNLOAD_SUCCESS);
            h.sendMessage(msg);

        }
    }

    private void downloadBitmapFromURL(final String url, final Handler h) {
        if (BitmapCache.getInstance().getBitmapFromMemCache(url) != null) {
            Log.d(getClass().getSimpleName(), "Skipped download, bitmap should be cached already");
            Message msg = h.obtainMessage(ALREADY_DOWNLOADED);
            h.sendMessage(msg);
            return;
        }

        setProgressBarActive(true);
        final AsyncHttpClient client = new AsyncHttpClient();

        client.get(url, new BinaryHttpResponseHandler() {
            @Override
            public void onSuccess(byte[] binaryData) {
                Log.d(getClass().getSimpleName(), "Download successful.");
                handleLoadedData(url, binaryData, h);
            }

            @Override
            public void onSuccess(int statusCode, byte[] binaryData) {
                Log.d(getClass().getSimpleName(),
                        "Download successful. Status code: " + statusCode);
                handleLoadedData(url, binaryData, h);
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] binaryData) {
                Log.d(getClass().getSimpleName(), "Download successful. Status code: "
                        + statusCode + ", headers: " + headers.length);
                handleLoadedData(url, binaryData, h);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers,
                                  byte[] binaryData, Throwable error) {

                Log.d(getClass().getSimpleName(), "Image downloading failed.");
                error.printStackTrace();
                setProgressBarActive(false);
                Message msg = h.obtainMessage(DOWNLOAD_FAILED);
                h.sendMessage(msg);
            }
        });

    }
}
