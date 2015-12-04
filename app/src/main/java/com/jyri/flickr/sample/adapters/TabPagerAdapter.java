package com.jyri.flickr.sample.adapters;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.jyri.flickr.sample.utils.BundleKeys;
import com.jyri.flickr.sample.fragments.ItemGridFragment;
import com.jyri.flickr.sample.utils.image.ImageMetaData;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class TabPagerAdapter extends FragmentStatePagerAdapter {

    private Context mContext;
    private String[] mTabs;

    private Map<String, ArrayList<ImageMetaData>>   mMetaData = new HashMap<>();
    private Map<String ,ArrayList<String>>          mLinks = new HashMap<>();

    public TabPagerAdapter(FragmentManager fm, String[] tabs, Context context) {
        super(fm);
        mTabs = tabs;
        mContext = context;
    }

    @Override
    public Fragment getItem(int position) {
        
        String title = mTabs[position];

        ArrayList<String> links = mLinks.get(title);
        if (links == null) {
            links = new ArrayList<>();
        }

        Bundle args = new Bundle();
        args.putString(BundleKeys.KEY_TITLE, mTabs[position]);
        args.putStringArrayList(BundleKeys.KEY_LINKS, links);
        args.putInt(BundleKeys.KEY_POSITION, position);

        ItemGridFragment fragment = (ItemGridFragment) Fragment.instantiate(mContext, ItemGridFragment.class.getName(), args);
        fragment.setAdapterImageData(mMetaData.get(mTabs[position]));
        return fragment;
    }

    @Override
    public int getCount() {
        return mTabs.length;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return mTabs[position];
    }

    public void setDataForTitle(String title, ArrayList<ImageMetaData> data, boolean override) {
        if (mMetaData.containsKey(title)) {
            if (override) {
                mMetaData.get(title).clear();
            } else {
                mMetaData.get(title).addAll(data);
            }
            mLinks.get(title).clear();
        } else {
            mMetaData.put(title, data);
        }

        ArrayList<String> links = new ArrayList<>();
        for (ImageMetaData d : data) {
            links.add(d.getImageURL());
        }
        mLinks.put(title, links);
    }

    public void refreshUI(int position) {
        notifyDataSetChanged();
    }

}
