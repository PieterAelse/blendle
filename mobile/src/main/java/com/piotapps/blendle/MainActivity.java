package com.piotapps.blendle;

import android.os.Bundle;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.WindowCompat;
import android.support.v7.app.ActionBar;
import android.view.View;
import android.view.Window;
import android.widget.ProgressBar;

import com.piotapps.blendle.fragments.PopularTodayFragment;
import com.squareup.picasso.Picasso;

public class MainActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        showMessage("Welcome! This is Blendle by Pieter Otten"); // TODO extract

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.main_fragmentHolder, PopularTodayFragment.newInstance())
                .commit();

        // TODO: add check if daydream is enabled
    }
}
