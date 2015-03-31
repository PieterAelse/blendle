package com.piotapps.blendle.services;

import android.annotation.TargetApi;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.service.dreams.DreamService;
import android.support.v7.widget.CardView;
import android.text.Html;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.piotapps.blendle.R;
import com.piotapps.blendle.pojo.ItemManifest;
import com.piotapps.blendle.pojo.PopularItems;
import com.piotapps.blendle.utils.StorageUtils;
import com.piotapps.blendle.utils.Utils;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

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

    private List<PopularItems.EmbeddedList.PopularItem> items;
    private Handler itemsHandler;
    private int currentItem;

    @Override
    public void onAttachedToWindow() {
        super.onAttachedToWindow();

        setFullscreen(true);

        setInteractive(INTERACTIVE);

        setContentView(R.layout.daydream_articles);
        ButterKnife.inject(this, getWindow().getDecorView().getRootView());

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

    private void showArticle(PopularItems.EmbeddedList.PopularItem article) {
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
        final String headline1 = article.getHeadline1();
        tvHeadline.setText(Html.fromHtml(headline1));
        tvHeadline.setVisibility(headline1 != null ? View.VISIBLE : View.GONE);

        // Content
        final String content = article.getContent();
        tvContent.setText(Html.fromHtml(content));
        tvContent.setVisibility(content != null ? View.VISIBLE : View.GONE);
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
