package com.jyri.flickr.sample.activities;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.zadaa.sample.jyri.sampleapp.R;
import com.jyri.flickr.sample.utils.BundleKeys;
import com.jyri.flickr.sample.utils.image.BitmapCache;

/**
 *  Detailed info about an item.
 */
public class ItemActivity extends Activity {

    Button mCloseButton;

    TextView mTextViewTitle;
    TextView mTextViewDescription;
    TextView mTextViewNumber;
    ImageView mImageViewProduct;

    String mTitle;
    String mDescription;
    String mImageURL;
    int mNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item);

        Bundle b = getIntent().getExtras();

        mTitle          = b.getString(BundleKeys.KEY_TITLE, "");
        mImageURL       = b.getString(BundleKeys.KEY_URL, "");
        mDescription    = b.getString(BundleKeys.KEY_DESCRIPTION, "");
        mNumber         = b.getInt(BundleKeys.KEY_POSITION);

        mCloseButton = (Button) findViewById(R.id.button_selection_close);
        mCloseButton.setText("CLOSE");
        mCloseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                setResult(RESULT_OK, intent);
                finish();
            }
        });

        mTextViewTitle = (TextView) findViewById(R.id.text_selection_title);
        mTextViewTitle.setText(mTitle);

        mTextViewNumber = (TextView) findViewById(R.id.text_selection_number);
        mTextViewNumber.setText("# " + mNumber);

        mTextViewDescription = (TextView) findViewById(R.id.text_selection_content);
        mTextViewDescription.setText(mDescription);

        mImageViewProduct = (ImageView) findViewById(R.id.image_selection_product);
        if (!mImageURL.isEmpty()) {
            Bitmap bitmap = BitmapCache.getInstance().getBitmapFromMemCache(mImageURL);
            if (bitmap != null) {
                mImageViewProduct.setImageBitmap(bitmap);
                mImageViewProduct.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(mImageURL));
                        startActivity(browserIntent);
                    }
                });
            }
        }
    }
}
