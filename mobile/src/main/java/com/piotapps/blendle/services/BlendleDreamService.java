package com.piotapps.blendle.services;

import android.annotation.TargetApi;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.service.dreams.DreamService;
import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.piotapps.blendle.R;
import com.piotapps.blendle.pojo.ItemManifest;
import com.piotapps.blendle.pojo.PopularItem;
import com.piotapps.blendle.utils.StorageUtils;
import com.piotapps.blendle.utils.Utils;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * The Blendle popular items Daydream! Shows all saved popular items and fades to the next
 * article after some time.
 */
@TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
public class BlendleDreamService extends DreamService {

    private static final int INTERVAL_TIME = 8000;
    private static final int ANIM_TIME = 1000;

    // TODO: make interactive
    private static boolean INTERACTIVE = false; // Currently enables scrolling :)

    @InjectView(R.id.article_container)
    CardView container;
    @InjectView(R.id.article_logo)
    ImageView imgLogo;
    @InjectView(R.id.article_price)
    TextView tvPrice;
    @InjectView(R.id.article_image)
    ImageView imgCover;
    @InjectView(R.id.article_headline)
    TextView tvHeadline;
    @InjectView(R.id.article_content)
    TextView tvContent;

    private List<PopularItem> items;
    private Handler itemsHandler;
    private int currentItem;

    @Override
    public void onAttachedToWindow() {
        super.onAttachedToWindow();

        // Set Daydream settings
        setFullscreen(true);
        setInteractive(INTERACTIVE);

        // Set view
        setContentView(R.layout.daydream_articles);
        ButterKnife.inject(this, getWindow().getDecorView().getRootView());

        // Get handler and set first item
        itemsHandler = new Handler(Looper.getMainLooper());
        currentItem = 0;
    }

    @Override
    public void onDreamingStarted() {
        super.onDreamingStarted();

        // Begin showing articles!
        items = StorageUtils.getSavedPopularItems(getApplicationContext());
        itemsHandler.post(showNextItem);
    }

    /** 'task' to show the next popular item using a crossfade animation */
    private final Runnable showNextItem = new Runnable() {
        @Override
        public void run() {
            currentItem++;
            if (currentItem >= items.size()) {
                // Reset to start
                currentItem = 0;
            }

            // Do a kind of crossfade animation to the next article, which is loaded during the animation
            container.animate().alpha(0f).setDuration(ANIM_TIME).withEndAction(new Runnable() {
                @Override
                public void run() {
                    showArticle(items.get(currentItem));

                    container.animate().alpha(1.0f).setDuration(ANIM_TIME).setStartDelay(100).withEndAction(new Runnable() {
                        @Override
                        public void run() {
                            // Reschedule for next item
                            itemsHandler.postDelayed(showNextItem, INTERVAL_TIME);
                        }
                    });
                }
            });
        }
    };

    private void showArticle(PopularItem article) {
        // Provider logo
        final String logoUrl = Utils.getLogoUrl(article.getItemProvider());
        Picasso.with(imgLogo.getContext()).load(logoUrl).error(R.mipmap.ic_launcher).into(imgLogo);

        // Item photo
        final ItemManifest.ImageHolder.ImageSizes is = article.getFirstImage();
        if (is != null) {
            imgCover.setVisibility(View.VISIBLE);
            Picasso.with(imgCover.getContext()).load(is.getUrlMedium()).into(imgCover);
        } else {
            imgCover.setImageDrawable(null);
        }
        imgCover.setVisibility(is != null ? View.VISIBLE : View.GONE);

        // Price
        tvPrice.setText(article.getPrice());

        // Headline1
        Utils.setTextOrHide(tvHeadline, article.getHeadline1());

        // Content
        Utils.setTextOrHide(tvContent, article.getContent());
    }

}
