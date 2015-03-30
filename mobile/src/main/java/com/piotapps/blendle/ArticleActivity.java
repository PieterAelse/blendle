package com.piotapps.blendle;

import android.nfc.NfcManager;
import android.os.Build;
import android.os.Bundle;

// TODO: add NFC
public class ArticleActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD_MR1) {
            NfcManager nfcManager = (NfcManager) getSystemService(NFC_SERVICE);
            if (nfcManager != null && nfcManager.getDefaultAdapter() != null) {
                // TODO: Setup article-link sharing trough NFCs
            }
        }
    }
}
