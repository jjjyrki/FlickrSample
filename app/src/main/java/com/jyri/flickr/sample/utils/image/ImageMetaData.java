package com.jyri.flickr.sample.utils.image;

import com.jyri.flickr.sample.utils.flickr.FlickrHelper;

/**
 *
 * Cotainer class for all the data received from FlickrHelper REST API.
 *
 */

public class ImageMetaData {

    private int mFarm;
    private int mServer;
    private long mId;
    private String mSecret;
    private String mTitle;
    private String mGroup;

    public ImageMetaData(ImageDataParams params) {
        mFarm = params.farm;
        mServer = params.server;
        mId = params.id;
        mSecret = params.secret;
        mTitle = params.title;
        mGroup = params.group;
    }

    public int getFarm() {
        return mFarm;
    }

    public int getServer() {
        return mServer;
    }

    public long getId() {
        return mId;
    }

    public String getSecret() {
        return mSecret;
    }

    public String getTitle() {
        return mTitle;
    }

    public String getGroup() {
        return mGroup;
    }

    public String getThumbnailURL() {
        return FlickrHelper.getThumbnailURL(this);
    }

    public String getImageURL() {
        return FlickrHelper.getImageURL(this);
    }

}
