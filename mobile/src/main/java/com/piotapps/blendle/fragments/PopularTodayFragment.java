package com.piotapps.blendle.fragments;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.piotapps.blendle.ArticleActivity;
import com.piotapps.blendle.BaseActivity;
import com.piotapps.blendle.R;
import com.piotapps.blendle.adapters.PopularItemAdapter;
import com.piotapps.blendle.api.APIConstants;
import com.piotapps.blendle.api.GetItemsTask;
import com.piotapps.blendle.interfaces.AsynCallback;
import com.piotapps.blendle.pojo.PopularItems;
import com.piotapps.blendle.utils.StorageUtils;

import java.util.Arrays;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * TODO
 */
public class PopularTodayFragment extends BaseFragment implements AsynCallback {

    private static final String KEY_URL_TO_LOAD_NEXT = "url_to_load_next";

    @InjectView(R.id.main_progress)
    ProgressBar progress;
    @InjectView(R.id.main_recyclerview)
    RecyclerView recyclerView;

    // Loading state
    private boolean isLoadingItems = false;

    // Fields
    private String nextToLoadUrl = APIConstants.URL_POPULAR_ITEMS;
    private PopularItemAdapter adapter;
    private LinearLayoutManager layoutManager;

    public static PopularTodayFragment newInstance() {
        return new PopularTodayFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_list, container, false);

        ButterKnife.inject(this, view);

        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        final boolean inPortrait = getActivity().getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT;

        layoutManager= new LinearLayoutManager(getActivity().getApplicationContext());
        layoutManager.setOrientation(inPortrait ? LinearLayoutManager.VERTICAL : LinearLayoutManager.HORIZONTAL);
        layoutManager.setSmoothScrollbarEnabled(true);
        recyclerView.setLayoutManager(layoutManager);

        adapter = new PopularItemAdapter();
        adapter.setOnItemSelectedListener(onItemSelectedListener);
        recyclerView.setAdapter(adapter);
        recyclerView.setOnScrollListener(new MyRecyclerScrollListener());
        recyclerView.setHorizontalScrollBarEnabled(!inPortrait);
        recyclerView.setVerticalScrollBarEnabled(inPortrait);
        recyclerView.setHasFixedSize(true);

        // Only fetch items when not rotated, else reload from storage
        if (savedInstanceState == null) {
            fetchMoreItems();
        } else {
            progress.setVisibility(View.GONE);
            nextToLoadUrl = savedInstanceState.getString(KEY_URL_TO_LOAD_NEXT);
            adapter.addItems(StorageUtils.getSavedPopularItems(getActivity().getApplicationContext()));
        }
    }

    private final PopularItemAdapter.OnItemSelectedListener onItemSelectedListener = new PopularItemAdapter.OnItemSelectedListener() {
        @Override
        public void onItemSelected(PopularItems.EmbeddedList.PopularItem pi) {
            startActivity(new Intent(getActivity().getApplicationContext(), ArticleActivity.class));
        }
    };


    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        // Save the url to load next for screen rotations etc.
        outState.putString(KEY_URL_TO_LOAD_NEXT, nextToLoadUrl);
    }

    @Override
    public void started() {
        isLoadingItems = true;
        updateUIToLoadingState();
    }

    @Override
    public void progress() {
        // TODO?
    }

    @Override
    public void ended(final PopularItems pi) {
        if(!isAdded()) return;

        isLoadingItems = false;
        updateUIToLoadingState();

        if (pi != null) {
            // Add items to adapter
            adapter.addItems(Arrays.asList(pi.getPopularItems()));

            // Save to storage
            StorageUtils.savePopularItems(getActivity().getApplicationContext(), adapter.getAllItems());

            // Save link to load next
            nextToLoadUrl = pi.getLinks().getNext();
        }
    }

    private void fetchMoreItems() {
        // TODO add wifi/3g connection check
        if (nextToLoadUrl == null) {
            showMessage("END OF POPULAR ITEMS REACHED. SHOW EASTER EGGY");
            return;
        }

        // Fetch the next page on a background thread
        new GetItemsTask(this).execute(nextToLoadUrl);
    }

    private void updateUIToLoadingState() {
        if (nextToLoadUrl.equals(APIConstants.URL_POPULAR_ITEMS)) {
            // Show/Hide main progressbar
            progress.setVisibility(isLoadingItems ? View.VISIBLE : View.GONE);
        } else {
            adapter.setShowLoading(isLoadingItems);
            //Show/Hide progressbar in actionbar
            ((BaseActivity)getActivity()).setSupportProgressBarIndeterminateVisibility(isLoadingItems);
        }
    }

    private final class MyRecyclerScrollListener extends RecyclerView.OnScrollListener {

        private static final int ITEMS_OFFSET = 3;

        public void onScrollStateChanged(RecyclerView recyclerView, int newState){
            if(newState == RecyclerView.SCROLL_STATE_IDLE) {
                if (!isLoadingItems && (layoutManager.findLastVisibleItemPosition() >= adapter.getItemCount() - 1 - ITEMS_OFFSET)) {
                    adapter.setShowLoading(true);
                    fetchMoreItems();
                }
            }
        }

        public void onScrolled(RecyclerView recyclerView, int dx, int dy){}
    }
}
