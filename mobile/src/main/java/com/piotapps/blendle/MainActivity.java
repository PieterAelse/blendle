package com.piotapps.blendle;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;

import com.piotapps.blendle.adapters.PopularItemAdapter;
import com.piotapps.blendle.api.APIConstants;
import com.piotapps.blendle.api.GetItemsTask;
import com.piotapps.blendle.interfaces.AsynCallback;
import com.piotapps.blendle.pojo.PopularItems;

import java.util.Arrays;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class MainActivity extends BaseActivity implements AsynCallback {

    @InjectView(R.id.main_progess)
    ProgressBar progress;
    @InjectView(R.id.main_recyclerview)
    RecyclerView recyclerView;

    private PopularItems popularItems;
    private PopularItemAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main);
        setContentView(R.layout.activity_list);

        ButterKnife.inject(this);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);

        showMessage("Welcome! This is Blendle by Pieter Otten"); // TODO extract

        new GetItemsTask(this).execute(APIConstants.URL_POPULAR_ITEMS);
    }

    @Override
    public void started() {
        progress.setVisibility(View.VISIBLE);
    }

    @Override
    public void progress() {
        // TODO?
    }

    @Override
    public void ended(final PopularItems pi) {
        progress.setVisibility(View.GONE);

        if (pi != null) {
            popularItems = pi;

            adapter = new PopularItemAdapter(Arrays.asList(popularItems.getPopularItems()));
            recyclerView.setAdapter(adapter);
        }
    }
}
