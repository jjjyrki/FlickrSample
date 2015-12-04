package com.jyri.flickr.sample.utils.flickr;

import com.jyri.flickr.sample.utils.image.ImageDataParams;
import com.jyri.flickr.sample.utils.image.ImageMetaData;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

/**
 *  Helper class for accessing Flickr's REST API.
 */

public class FlickrHelper {


    /**
     *      Builds search URL for FlickrHelper REST API.
     */
    public static String getSearchURL(String apiKey, String search, int maxResults) {
        StringBuffer url = new StringBuffer(FlickrURLs.BASE_SEARCH);
        String searchTerm;
        try {
            searchTerm = URLEncoder.encode(search, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return "";
        }

        url.append(apiKey);
        url.append("&format=json&per_page=");
        url.append(maxResults);
        url.append("&sort=relevance");
        url.append("&content_type=1&media=photos");
        url.append("&text=");
        url.append(searchTerm);

        return url.toString();
    }


    /**
     *     Parse JSON object received from FlickrHelper.
     */
    public static List<ImageMetaData> parseJSONObject(JSONObject in) throws IOException {
        try {
            List<ImageMetaData> retVal = new ArrayList<ImageMetaData>();
            JSONArray photos = in.getJSONObject("photos").getJSONArray("photo");

            for(int i = 0; i < photos.length(); i++) {
                JSONObject photoObj = photos.getJSONObject(i);

                ImageDataParams params = new ImageDataParams();
                params.id = photoObj.getLong("id");
                params.title = photoObj.getString("title");
                params.farm = photoObj.getInt("farm");
                params.server = photoObj.getInt("server");
                params.secret = photoObj.getString("secret");

                ImageMetaData img = new ImageMetaData(params);

                retVal.add(img);
            }

            return retVal;
        } catch (JSONException e) {
            throw new IOException("JSON ERROR " + e.getMessage());
        }
    }

    public static String getImageURL(ImageMetaData data) {
        StringBuffer url = new StringBuffer(baseImageURL(data));
        url.append((".jpg"));
        return url.toString();
    }

    public static String getThumbnailURL(ImageMetaData data) {
        StringBuffer url = new StringBuffer(baseImageURL(data));
        url.append("_s.jpg");
        return url.toString();
    }

    private static String baseImageURL(ImageMetaData data) {

        StringBuffer url = new StringBuffer("http://farm");
        url.append(data.getFarm());
        url.append(".static.flickr.com/");
        url.append(data.getServer());
        url.append("/");
        url.append(data.getId());
        url.append("_");
        url.append(data.getSecret());

        return url.toString();

    }

}
