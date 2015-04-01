package com.piotapps.blendle.fragments;

import android.annotation.TargetApi;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.widget.ProgressBar;

import com.piotapps.blendle.ArticleActivity;
import com.piotapps.blendle.BaseActivity;
import com.piotapps.blendle.R;
import com.piotapps.blendle.adapters.PopularItemAdapter;
import com.piotapps.blendle.api.APIConstants;
import com.piotapps.blendle.api.GetItemsTask;
import com.piotapps.blendle.pojo.PopularItems;
import com.piotapps.blendle.utils.StorageUtils;
import com.piotapps.blendle.utils.Utils;

import java.util.Arrays;

import butterknife.ButterKnife;
import butterknife.InjectView;

import static com.nineoldandroids.view.ViewPropertyAnimator.animate;

/**
 * TODO
 */
public class PopularTodayFragment extends BaseFragment implements GetItemsTask.AsynCallback {

    private static final String KEY_URL_TO_LOAD_NEXT = "url_to_load_next";

    @InjectView(R.id.main_header)
    Toolbar header;
    @InjectView(R.id.main_swiperefreshlayout)
    SwipeRefreshLayout swipeRefreshLayout;
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

        // Hide header and padding when in landscape
        if(!inPortrait) {
            header.setVisibility(View.GONE);
            final int padding = recyclerView.getPaddingLeft();
            recyclerView.setPadding(padding, padding, padding, padding);
        }

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // Create new adapter to reset all current items
                adapter = new PopularItemAdapter();
                adapter.setOnItemSelectedListener(onItemSelectedListener);
                recyclerView.setAdapter(adapter);

                // Reset link
                nextToLoadUrl = APIConstants.URL_POPULAR_ITEMS;

                // And load the items :)
                fetchMoreItems();
            }
        });
        swipeRefreshLayout.setColorSchemeResources(R.color.blende_red_dark, R.color.blende_red_dark_transparent, R.color.blende_red_transparent, R.color.blende_red);

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
            final Intent articleIntent = new Intent(getActivity().getApplicationContext(), ArticleActivity.class);
            articleIntent.putExtra(ArticleActivity.EXTRA_ARTICLE_INSTANCE, pi);
            startActivity(articleIntent);
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
        // Check internet
        if (Utils.hasInternetConnection(getActivity().getApplicationContext())) {

            // Check if not at the end (if that's even possible?
            if (nextToLoadUrl == null) {
                showMessage(R.string.message_endofpopular_easter);
                return;
            }

            // Fetch the next page on a background thread
            new GetItemsTask(this).execute(nextToLoadUrl);
        } else {
            // No internet, show message
            showMessage(R.string.message_no_internet);
        }
    }

    private void updateUIToLoadingState() {
        if (nextToLoadUrl.equals(APIConstants.URL_POPULAR_ITEMS)) {
            swipeRefreshLayout.setRefreshing(isLoadingItems);
            // Show/Hide main progressbar
            progress.setVisibility(isLoadingItems ? View.VISIBLE : View.GONE);
        } else {
            adapter.setShowLoading(isLoadingItems);
            //Show/Hide progressbar in actionbar
            ((BaseActivity)getActivity()).setSupportProgressBarIndeterminateVisibility(isLoadingItems);
        }
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR1)
    public void hideHeader() {
        swipeRefreshLayout.setProgressViewOffset(false, 0, header.getHeight());

        if (Utils.canAnimate()) {
            header.animate().translationY(-header.getHeight()).setInterpolator(new AccelerateInterpolator(1.5f));
        } else {
            animate(header).translationY(-header.getHeight()).setInterpolator(new AccelerateInterpolator(1.5f));
        }
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR1)
    public void showHeader() {
        swipeRefreshLayout.setProgressViewOffset(false, 0, (int)(1.5 * header.getHeight()));

        if (Utils.canAnimate()) {
            header.animate().translationY(0).setInterpolator(new DecelerateInterpolator(1.5f));
        } else {
            animate(header).translationY(0).setInterpolator(new DecelerateInterpolator(1.5f));
        }
    }

    private final class MyRecyclerScrollListener extends RecyclerView.OnScrollListener {

        private static final int ITEMS_OFFSET = 2;
        private static final int HIDE_THRESHOLD = 20;
        private int scrolledDistance = 0;
        private boolean controlsVisible = true;

        public void onScrollStateChanged(RecyclerView recyclerView, int newState){
            if(newState == RecyclerView.SCROLL_STATE_IDLE) {
                if (!isLoadingItems && (layoutManager.findLastVisibleItemPosition() >= adapter.getItemCount() - 1 - ITEMS_OFFSET)) {
                    adapter.setShowLoading(true);
                    fetchMoreItems();
                }
            }
        }

        public void onScrolled(RecyclerView recyclerView, int dx, int dy){
            if (scrolledDistance > HIDE_THRESHOLD && controlsVisible) {
                hideHeader();
                controlsVisible = false;
                scrolledDistance = 0;
            } else if (scrolledDistance < -HIDE_THRESHOLD && !controlsVisible) {
                showHeader();
                controlsVisible = true;
                scrolledDistance = 0;
            }

            if((controlsVisible && dy>0) || (!controlsVisible && dy<0)) {
                scrolledDistance += dy;
            }
        }
    }
}
