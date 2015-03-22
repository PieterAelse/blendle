package com.piotapps.blendle;

import android.os.Bundle;

import com.piotapps.blendle.api.APIConstants;
import com.piotapps.blendle.api.GetItemsTask;
import com.piotapps.blendle.interfaces.AsynCallback;
import com.piotapps.blendle.pojo.PopularItems;

public class MainActivity extends BaseActivity implements AsynCallback {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        showMessage("Welcome! This is Blendle by Pieter Otten"); // TODO extract

        new GetItemsTask(this).execute(APIConstants.URL_POPULAR_ITEMS);
    }

    @Override
    public void started() {

    }

    @Override
    public void progress() {

    }

    @Override
    public void ended(PopularItems pi) {

    }
}
