package com.piotapps.blendle.services;

import android.annotation.TargetApi;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.service.dreams.DreamService;
import android.text.Html;
import android.widget.ImageView;
import android.widget.TextView;

import com.piotapps.blendle.R;
import com.piotapps.blendle.pojo.ItemManifest;
import com.piotapps.blendle.pojo.PopularItems;
import com.piotapps.blendle.utils.StorageUtils;
import com.squareup.picasso.Picasso;

import java.util.List;

//TODO
@TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
public class BlendleDreamService extends DreamService {

    private static final int TIME = 5000;

    // TODO better UI specific for dreams
    private TextView tvTitle;
    private ImageView imgCover;

    private List<PopularItems.EmbeddedList.PopularItem> items;
    private Handler itemsHandler;
    private int currentItem;

    @Override
    public void onAttachedToWindow() {
        super.onAttachedToWindow();

        setFullscreen(true);
        // TODO: make interactive for opening articles
        setInteractive(false);

        setContentView(R.layout.item_popular);
        tvTitle = (TextView)findViewById(R.id.popularitem_title);
        imgCover = (ImageView) findViewById(R.id.popularitem_image);

        itemsHandler = new Handler(Looper.getMainLooper());
        currentItem = 0;
    }

    @Override
    public void onDreamingStarted() {
        super.onDreamingStarted();

        items = StorageUtils.getSavedPopularItems(getApplicationContext());
        itemsHandler.post(showNextItem);
    }

    private final Runnable showNextItem = new Runnable() {
        @Override
        public void run() {
            currentItem++;
            if (currentItem >= items.size()) {
                // Reset to start
                currentItem = 0;
            }

            // TODO add animation (fade? newspaper-like?)
            showItem(items.get(currentItem));

            // Reschedule for next item
            itemsHandler.postDelayed(showNextItem, TIME);
        }
    };

    private void showItem(PopularItems.EmbeddedList.PopularItem item) {
        tvTitle.setText(Html.fromHtml(item.getHeadline()));

        ItemManifest.ImageHolder.ImageSizes is = item.getFirstImage();
        if (is != null) {
            Picasso.with(getApplicationContext()).load(is.getUrlMedium()).into(imgCover);
        } else {
            imgCover.setImageDrawable(null);
        }
    }

    @Override
    public void onDreamingStopped() {
        super.onDreamingStopped();
    }

    @Override
    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();
    }
}
