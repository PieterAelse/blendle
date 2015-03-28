package com.piotapps.blendle.fragments;

import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

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
        recyclerView.setAdapter(adapter);
        recyclerView.setOnScrollListener(new MyRecyclerScrollListener());
        recyclerView.setHorizontalScrollBarEnabled(!inPortrait);
        recyclerView.setVerticalScrollBarEnabled(inPortrait);

//        updateUIToLoadingState();
//        adapter.addItems(StorageUtils.getSavedPopularItems(getActivity().getApplicationContext())); // TODO: if we use this, also save next URL to load
        fetchMoreItems();
    }

    private void fetchMoreItems() {
        // TODO add wifi/3g connection check
        if (nextToLoadUrl == null) {
            showMessage("END OF POPULAR ITEMS REACHED. SHOW EASTER EGGY");
            return;
        }

        new GetItemsTask(this).execute(nextToLoadUrl);
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
