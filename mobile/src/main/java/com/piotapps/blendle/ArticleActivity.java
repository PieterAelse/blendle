package com.piotapps.blendle;

import android.content.Intent;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;

import com.piotapps.blendle.api.GetOneItemTask;
import com.piotapps.blendle.pojo.PopularItems;

// TODO: add NFC
public class ArticleActivity extends BaseActivity implements GetOneItemTask.AsynCallback {

    public static final String EXTRA_ARTICLE_INSTANCE = "extra_article_instance";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        if (getIntent() != null && getIntent().hasExtra(EXTRA_ARTICLE_INSTANCE)) {
            PopularItems.EmbeddedList.PopularItem article = (PopularItems.EmbeddedList.PopularItem) getIntent().getSerializableExtra(EXTRA_ARTICLE_INSTANCE);

            showMessage("Showing: " + article.getHeadline());

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD_MR1) {
                NfcAdapter nfcAdapter = NfcAdapter.getDefaultAdapter(getApplicationContext());
                if (nfcAdapter != null) {

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
                        // Show hint of this function :) TODO add delay, make dialog and add invokeBeam() when pressed button
                        showMessage(R.string.message_nfc_hint);

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

    /**
     * Parses the NDEF Message from the intent and prints to the TextView
     */
    void processIntent(Intent intent) {
        Parcelable[] rawMsgs = intent.getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES);
        NdefMessage msg = (NdefMessage) rawMsgs[0];
        // record 0 contains the MIME type, record 1 is the AAR, if present
        String selfLink = "https://" + new String(msg.getRecords()[0].getPayload()).trim();

        showMessage(selfLink);
        new GetOneItemTask(this).execute(selfLink);
    }


    @Override
    public void started() {
        setContentView(R.layout.item_loading);
    }

    @Override
    public void progress() {

    }

    @Override
    public void ended(PopularItems.EmbeddedList.PopularItem pi) {
        showMessage("NFC received: " + pi.getHeadline());
    }
}
