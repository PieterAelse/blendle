package com.piotapps.blendle;

import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.piotapps.blendle.api.GetOneItemTask;
import com.piotapps.blendle.interfaces.BlendleApiCallback;
import com.piotapps.blendle.pojo.ItemManifest;
import com.piotapps.blendle.pojo.PopularItem;
import com.piotapps.blendle.pojo.PopularItems;
import com.piotapps.blendle.utils.Utils;
import com.software.shell.fab.ActionButton;
import com.squareup.picasso.Picasso;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

/**
 * Shows a single article
 * <p>
 * This article can be passed using {@link #EXTRA_ARTICLE_INSTANCE} or
 * the user received an article using NFC (Android Beam)
 */
public class ArticleActivity extends BaseActivity {

    /** Extra to use to pass the article to show */
    public static final String EXTRA_ARTICLE_INSTANCE = "extra_article_instance";

    // Views
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
    @InjectView(R.id.article_btnBuy)
    ActionButton btnBuy;
    @InjectView(R.id.article_progress)
    ProgressBar progressBar;

    private PopularItem article;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Show the 'up' button
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // UI stuff
        setContentView(R.layout.activity_article);
        ButterKnife.inject(this);

        if (getIntent() != null && getIntent().hasExtra(EXTRA_ARTICLE_INSTANCE)) {
            // Get and show article
            article = (PopularItem) getIntent().getSerializableExtra(EXTRA_ARTICLE_INSTANCE);
            showArticle();

            // Check NFC presence and if so setup article sharing
            checkAndSetupNfc();
        }
        // Else: may be started by NFC, check in onResume
    }

    @Override
    public void onResume() {
        super.onResume();
        // Check to see that the Activity started due to an Android Beam
        if (getIntent() != null && NfcAdapter.ACTION_NDEF_DISCOVERED.equals(getIntent().getAction())) {
            processIntent(getIntent());
        }
    }

    @Override
    public void onNewIntent(Intent intent) {
        // onResume gets called after this to handle the intent
        setIntent(intent);
    }

    @OnClick(R.id.article_btnBuy)
    public void buyArticle() {
        btnBuy.hide();
        showMessage(getString(R.string.message_article_bought));
    }

    private void showArticle() {
        // Provider logo (fallback to app icon on error)
        final String logoUrl = Utils.getLogoUrl(article.getItemProvider());
        Picasso.with(imgLogo.getContext()).load(logoUrl).error(R.mipmap.ic_launcher).into(imgLogo);

        // Item photo
        final ItemManifest.ImageHolder.ImageSizes is = article.getFirstImage();
        if (is != null) {
            imgCover.setVisibility(View.VISIBLE);
            Picasso.with(imgCover.getContext()).load(is.getUrlMedium()).into(imgCover);
            // TODO: use images according to screensize/dpi/connectionType
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

        // Show the buy button
        btnBuy.playShowAnimation();
    }

    @TargetApi(Build.VERSION_CODES.GINGERBREAD_MR1)
    private void checkAndSetupNfc() {
        // First check if NFC API is present in this Android version (>=10)
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.GINGERBREAD_MR1) {
            return;
        }

        // NFC API present, check for hardware
        final NfcAdapter nfcAdapter = NfcAdapter.getDefaultAdapter(getApplicationContext());
        if (nfcAdapter == null) {
            return;
        }

        // Check NFC API version for setting a message to share
        // API14 introduced some useful methods, so we'll currently only support that one
        // TODO when time left: also implement older API's for sharing an URI/AAR
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
            return;
        }

        setupNfcArticleSharing(nfcAdapter);
    }

    @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
    private void setupNfcArticleSharing(final NfcAdapter nfcAdapter) {
        // Create NDEF records. AAR helps us starting the Blendle app on the other side :)
        final NdefRecord articleUri = NdefRecord.createUri(article.getLinks().getSelf());
        final NdefRecord aar = NdefRecord.createApplicationRecord(BuildConfig.APPLICATION_ID);
        // Create NDEF message (containing the records)
        final NdefMessage ndefMessage = new NdefMessage(new NdefRecord[]{articleUri, aar});
        // Set message to share
        nfcAdapter.setNdefPushMessage(ndefMessage, this);

        // Show hint of this function after a small delay :)
        delayHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                final AlertDialog.Builder adBuilder = createAlertDialog(R.string.dialog_title_nfc_sharing, R.string.dialog_message_nfc_sharing);
                adBuilder.setPositiveButton(R.string.dialog_btn_cool, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                            nfcAdapter.invokeBeam(ArticleActivity.this);
                        } else {
                            showMessage(getString(R.string.message_nfc_share_try));
                        }
                    }
                });
                adBuilder.setNegativeButton(R.string.dialog_btn_cancel, null);
                adBuilder.show();
            }
        }, UI_MESSAGES_DELAY);
    }

    private void processIntent(Intent intent) {
        // Get the NdefMessage from the intent
        final Parcelable[] rawMsgs = intent.getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES);
        final NdefMessage msg = (NdefMessage) rawMsgs[0];
        // Record 0 contains the MIME type, record 1 is the AAR, if present
        // So look at record 0 because Android already uses record 1 (AAR) to start this app
        final String articleLink = "https://" + new String(msg.getRecords()[0].getPayload()).trim();

        // Reset intent
        setIntent(new Intent());

        // Check if user has internet
        if (checkInternetConnection()) {
            // To load the article
            new GetOneItemTask(blendleApiCallback).execute(articleLink);
        }
    }

    /** Implementation of the different states and results of using the API */
    private final BlendleApiCallback blendleApiCallback = new BlendleApiCallback() {
        @Override
        public void started() {
            progressBar.setVisibility(View.VISIBLE);
        }

        @Override
        public void onError() {
            progressBar.setVisibility(View.GONE);
            showMessage(R.string.message_error_loading);
        }

        @Override
        public void endedSingleItem(PopularItem popularItem) {
            progressBar.setVisibility(View.GONE);

            // Show in UI
            article = popularItem;
            showArticle();
        }

        @Override
        public void endedItemList(PopularItems popularItems) {
            // Not used here
        }
    };
}
