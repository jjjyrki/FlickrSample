package com.jyri.flickr.sample.adapters;

import android.app.Activity;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.zadaa.sample.jyri.sampleapp.R;
import com.jyri.flickr.sample.fragments.ItemGridFragment;
import com.jyri.flickr.sample.utils.image.BitmapCache;
import com.jyri.flickr.sample.utils.image.ImageMetaData;

import java.util.ArrayList;
import java.util.List;


public class GridViewAdapter extends BaseAdapter {

    String mTitle = "";
    ItemGridFragment mFragment;
    List<String> mLinks = new ArrayList<>();
    List<ImageMetaData> mMeta = new ArrayList<>();
    int mPosition = -1;

    public GridViewAdapter(ItemGridFragment fragment, final int position, final String title, final List<ImageMetaData> meta) {
        super();
        mTitle = title;
        mFragment = fragment;
        mMeta = meta;
        mPosition = position;
    }

    public void setImageData(List<ImageMetaData> data) {
        mMeta = data;
        notifyDataSetChanged();
    }

    public String getTitle() {
        return mTitle;
    }

    @Override
    public int getCount() {
        return 10;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = ((Activity) mFragment.getContext()).getLayoutInflater();
        View view = convertView;

        if (view == null) {
            view =  inflater.inflate(R.layout.layout_grid_item, parent, false);
        }

        ImageView imageAvatar = (ImageView) view.findViewById(R.id.image_grid_view_avatar);
        imageAvatar.setScaleType(ImageView.ScaleType.FIT_CENTER);
        imageAvatar.setPadding(3, 3, 3, 3);

        ImageView imageProduct = (ImageView) view.findViewById(R.id.image_grid_view_content);
        imageProduct.setScaleType(ImageView.ScaleType.FIT_CENTER);
        imageProduct.setPadding(8, 8, 8, 8);
        TextView textTitle = (TextView) view.findViewById(R.id.text_grid_view_title);

        Bitmap bitmap;

        if (mMeta.size() > position) {
            textTitle.setText(mMeta.get(position).getTitle());
            bitmap = BitmapCache.getInstance().getBitmapFromMemCache(mMeta.get(position).getImageURL());

            if (bitmap != null) {
                imageProduct.setImageBitmap(bitmap);
                imageAvatar.setImageBitmap(bitmap);

            }
        }

        return view;
    }
}
