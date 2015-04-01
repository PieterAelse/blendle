package com.piotapps.blendle;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Parcelable;
import android.preference.DialogPreference;
import android.text.Html;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.piotapps.blendle.api.GetOneItemTask;
import com.piotapps.blendle.pojo.ItemManifest;
import com.piotapps.blendle.pojo.PopularItems;
import com.piotapps.blendle.utils.Utils;
import com.software.shell.fab.ActionButton;
import com.squareup.picasso.Picasso;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

// TODO: explain
public class ArticleActivity extends BaseActivity implements GetOneItemTask.AsynCallback {

    public static final String EXTRA_ARTICLE_INSTANCE = "extra_article_instance";

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        setContentView(R.layout.activity_article);
        ButterKnife.inject(this);
        btnBuy.setVisibility(View.VISIBLE);

        if (getIntent() != null && getIntent().hasExtra(EXTRA_ARTICLE_INSTANCE)) {
            final PopularItems.EmbeddedList.PopularItem article = (PopularItems.EmbeddedList.PopularItem) getIntent().getSerializableExtra(EXTRA_ARTICLE_INSTANCE);

            showArticle(article);
            
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD_MR1) {
                final NfcAdapter nfcAdapter = NfcAdapter.getDefaultAdapter(getApplicationContext());
                if (nfcAdapter != null) {

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
                        // Show hint of this function after small delay :)
                       delayHandler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                AlertDialog.Builder adBuilder = new AlertDialog.Builder(ArticleActivity.this);
                                adBuilder.setTitle(R.string.dialog_title_nfc_sharing).setMessage(R.string.dialog_message_nfc_sharing);
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
                        }, 2500);

                        // Create NDEF records. AAR helps us starting the Blendle app on the other side :)
                        NdefRecord articleUri = NdefRecord.createUri(article.getLinks().getSelf());
                        NdefRecord aar = NdefRecord.createApplicationRecord(BuildConfig.APPLICATION_ID);
                        // Create NDEF message (containing the records)
                        NdefMessage ndefMessage = new NdefMessage(new NdefRecord[]{articleUri, aar});

                        nfcAdapter.setNdefPushMessage(ndefMessage, this);
                    } else {
                        // TODO when time left: fall back to older API's for sharing an URI/AAR
                    }
                } // Else: no NFC present on this device
            }
        } // Else: may be started by NFC, check in onResume
    }

    @OnClick(R.id.article_btnBuy)
    public void buyArticle() {
        showMessage(getString(R.string.message_article_bought));
    }

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

        btnBuy.playShowAnimation();
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

    private void processIntent(Intent intent) {
        Parcelable[] rawMsgs = intent.getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES);
        NdefMessage msg = (NdefMessage) rawMsgs[0];
        // record 0 contains the MIME type, record 1 is the AAR, if present
        String selfLink = "https://" + new String(msg.getRecords()[0].getPayload()).trim();

        // Reset intent
        setIntent(new Intent());

        // Check if user has internet
        if (checkInternetConnection()) {
            // To load the article
            new GetOneItemTask(this).execute(selfLink);
        }

    }


    @Override
    public void started() {
        progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void progress() {

    }

    @Override
    public void ended(PopularItems.EmbeddedList.PopularItem pi) {
        progressBar.setVisibility(View.GONE);
        showArticle(pi);
    }
}
