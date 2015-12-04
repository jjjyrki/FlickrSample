package com.jyri.flickr.sample.utils.downloader;

import android.util.Log;

import com.jyri.flickr.sample.utils.flickr.FlickrHelper;
import com.jyri.flickr.sample.utils.image.ImageMetaData;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;

public class ImageDataLoader {

    private String mApiKey;

    public ImageDataLoader(String apiKey) {
        mApiKey = apiKey;
    }

    public void fetchImageInfo(final String searchTerm, final int maxResults, final ImageDataCallback callback) {
        final String searchURL = FlickrHelper.getSearchURL(mApiKey, searchTerm, maxResults);

        AsyncHttpClient client = new AsyncHttpClient();

        Log.d(getClass().getSimpleName(), "Http request: " + searchURL.toString());
        client.get(searchURL, new JsonHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseBody) {
                Log.d(getClass().getSimpleName(), "onSuccess!");
                try {
                    String jsonString = responseBody.substring(responseBody.indexOf("(") + 1, responseBody.indexOf(")"));
                    JSONObject object = new JSONObject(jsonString);

                    List<ImageMetaData> data = FlickrHelper.parseJSONObject(object);

                    callback.onMetaDataLoaded(data);

                } catch (JSONException e) {
                    Log.e(getClass().getSimpleName(), "JSONException when fetching data from " + searchURL);
                    Log.e(getClass().getSimpleName(), e.getMessage());
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Throwable e, JSONObject errorResponse) {
                handleHTTPFailure(-1, e);
            }

            @Override
            public void onFailure(int statusCode, Throwable e, JSONObject errorResponse) {
                handleHTTPFailure(statusCode, e);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable e, JSONObject errorResponse) {
                handleHTTPFailure(statusCode, e);
            }

            @Override
            public void onFailure(Throwable e, JSONArray errorResponse) {
                handleHTTPFailure(-1, e);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable e, JSONArray errorResponse) {
                handleHTTPFailure(statusCode, e);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseBody, Throwable e) {
                handleHTTPFailure(statusCode, e);

            }

            @Override
            public void onFailure(int statusCode, Throwable e, JSONArray errorResponse) {
                handleHTTPFailure(statusCode, e);
            }
        });
    }
   private void handleHTTPFailure(int statusCode, Throwable e) {
        Log.e(getClass().getSimpleName(), "Error " + statusCode + ", " + e.getMessage());
    }

    public interface ImageDataCallback {
        void onMetaDataLoaded(List<ImageMetaData> imageData);
    }
}

