package com.jyri.flickr.sample.activities;

import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.jyri.flickr.sample.adapters.TabPagerAdapter;
import com.jyri.flickr.sample.googleio.SlidingTabLayout;
import com.jyri.flickr.sample.utils.image.ImageMetaData;
import com.zadaa.sample.jyri.sampleapp.R;
import com.jyri.flickr.sample.fragments.ItemGridFragment;
import com.jyri.flickr.sample.utils.downloader.ImageDataLoader;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private final String API_KEY = "02a731943b15af46a768e3e10c883fcd";

    public static final String TITLES[] = {
            "Jackets",
            "Bags",
            "Shoes"
    };

    private int mSelectedPageIndex = 0;

    private ImageDataLoader     mLoader;
    private TabPagerAdapter mTabPagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mLoader =                   new ImageDataLoader(API_KEY);
        mTabPagerAdapter =          new TabPagerAdapter(getSupportFragmentManager(),
                                        TITLES, getApplicationContext());
        ViewPager viewPager =       (ViewPager) findViewById(R.id.pager);
        SlidingTabLayout tabs =    (SlidingTabLayout) findViewById(R.id.tabs);

        viewPager.setAdapter(mTabPagerAdapter);

        downloadContent();

        tabs.setDistributeEvenly(true);
        tabs.setCustomTabColorizer(new SlidingTabLayout.TabColorizer() {
            @Override
            public int getIndicatorColor(int position) {
                return ContextCompat.getColor(getApplicationContext(), R.color.tabsScrollColor);
            }
        });

        tabs.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                Log.d(getClass().getSimpleName(), "onPageSelected " + position);
                mSelectedPageIndex = position;
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });

        tabs.setViewPager(viewPager);
    }

    private void downloadContent() {
        for(final String title : TITLES) {
            mLoader.fetchImageInfo(title, 10, new ImageDataLoader.ImageDataCallback() {
                @Override
                public void onMetaDataLoaded(List<ImageMetaData> imageData) {
                    mTabPagerAdapter.setDataForTitle(title, (ArrayList<ImageMetaData>) imageData, true);

                    for(Fragment fragment : getSupportFragmentManager().getFragments()) {
                        if (fragment instanceof ItemGridFragment) {
                            if (((ItemGridFragment) fragment).getAdapter().getTitle().equals(title)) {
                                //ImageDataHelper helper = new ImageDataHelper();
                                //((ItemGridFragment) fragment).setAdapterData(helper.getLinkList(imageData));
                                ((ItemGridFragment) fragment).setAdapterImageData(imageData);
                            }

                        }
                    }

                }
            });
        }
    }

    private class ImageDataHelper {
        public List<String> getLinkList(List<ImageMetaData> data) {
            ArrayList<String> links = new ArrayList<>();

            for (ImageMetaData d : data) {
                links.add(d.getImageURL());
            }

            return links;
        }
    }
}
