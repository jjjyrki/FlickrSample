package com.jyri.flickr.sample.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import com.jyri.flickr.sample.activities.ItemActivity;
import com.jyri.flickr.sample.adapters.GridViewAdapter;
import com.jyri.flickr.sample.utils.BundleKeys;
import com.jyri.flickr.sample.utils.downloader.BitmapDownloader;
import com.jyri.flickr.sample.utils.image.ImageMetaData;
import com.zadaa.sample.jyri.sampleapp.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by keijo on 2.12.2015.
 */
public class ItemGridFragment extends Fragment {

    private List<ImageMetaData> mMeta = new ArrayList<>();

    public void setAdapterImageData(List<ImageMetaData> data) {
        mMeta = data;

        // Download bitmaps when new data is set.
        downloadBitmapsForFragment();

        GridViewAdapter adapter = getAdapter();
        if (adapter != null) {
            adapter.setImageData(data);
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        Bundle args = getArguments();

        String title =              args.getString(BundleKeys.KEY_TITLE);
        int position =              args.getInt(BundleKeys.KEY_POSITION);

        GridView view = (GridView)inflater.inflate(R.layout.layout_grid_view, container, false);

        // Open up a view that shows details of the produc.
        view.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ImageMetaData meta = mMeta.get(position);
                Intent intent = new Intent(getContext(), ItemActivity.class);

                intent.putExtra(BundleKeys.KEY_DESCRIPTION,     meta.getImageURL());
                intent.putExtra(BundleKeys.KEY_TITLE,           meta.getTitle());
                intent.putExtra(BundleKeys.KEY_URL,             meta.getImageURL());
                intent.putExtra(BundleKeys.KEY_POSITION,        position);

                getActivity().startActivityForResult(intent, 0);
            }
        });

        if (mMeta == null) {
            mMeta = new ArrayList<>();
        }

        GridViewAdapter adapter = new GridViewAdapter(this, position, title, mMeta);
        view.setAdapter(adapter);

        return view;
    }

    public GridViewAdapter getAdapter() {
        GridView view = (GridView) getView();
        GridViewAdapter adapter = null;
        if (view != null)
            adapter = (GridViewAdapter) view.getAdapter();
        return adapter;
    }

    // Download bitmaps based on the data set in mMeta
    // If mMeta is null, do nothing.
    public void downloadBitmapsForFragment() {
        if (mMeta != null) {
            for (final ImageMetaData data : mMeta) {
                BitmapDownloader.downloadBitmap(null, data.getImageURL(), new Handler() {
                    @Override
                    public void handleMessage(Message msg) {
                        GridViewAdapter adapter = getAdapter();
                        switch (msg.what) {
                            case BitmapDownloader.DOWNLOAD_SUCCESS:
                                if (adapter != null) adapter.notifyDataSetChanged();
                                break;
                            case BitmapDownloader.ALREADY_DOWNLOADED:
                                if (adapter != null) adapter.notifyDataSetChanged();
                                break;
                            default:
                        }

                    }
                });
            }
        }
    }
}
