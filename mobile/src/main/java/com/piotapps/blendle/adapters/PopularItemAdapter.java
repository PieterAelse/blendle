package com.piotapps.blendle.adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.piotapps.blendle.R;
import com.piotapps.blendle.pojo.ItemManifest;
import com.piotapps.blendle.pojo.PopularItems;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * {@link android.support.v7.widget.RecyclerView.Adapter} to use for Popular items.
 * <p>
 * Uses the ItemViewHolder pattern and ButterKnife for optimal performance.
 */
public class PopularItemAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int TYPE_ITEM = 0;
    private static final int TYPE_FOOTER = 1;

    private boolean showLoading;
    private List<PopularItems.EmbeddedList.PopularItem> items;

    public PopularItemAdapter() {
        items = new ArrayList<>();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case TYPE_ITEM:
                View item = LayoutInflater
                        .from(parent.getContext())
                        .inflate(R.layout.item_popular, parent, false);

                return new ItemViewHolder(item);
            case TYPE_FOOTER:
                View footer = LayoutInflater
                        .from(parent.getContext())
                        .inflate(R.layout.item_loading, parent, false);
                return new LoadingViewHolder(footer);
        }

        throw new RuntimeException("No matching viewtype found for type " + viewType);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof ItemViewHolder) {
            final ItemViewHolder item = (ItemViewHolder)holder;
            final PopularItems.EmbeddedList.PopularItem pi = items.get(position);

            ItemManifest.ImageHolder.ImageSizes is = pi.getFirstImage();
            if (is != null) {
                Picasso.with(item.mCover.getContext()).load(is.getUrlMedium()).into(item.mCover);
            } else {
                item.mCover.setImageDrawable(null);
            }
            item.mTitle.setText(Html.fromHtml(pi.getHeadline()));
        }
    }

    @Override
    public int getItemCount() {
        if (showLoading) {
            return items.size() + 1;
        }
        return items.size();
    }

    @Override
    public int getItemViewType(int position) {
        if (showLoading && position == items.size()) {
            return TYPE_FOOTER;
        }

        return TYPE_ITEM;
    }

    public boolean isShowLoading() {
        return showLoading;
    }

    public void setShowLoading(boolean showLoading) {
        if (!showLoading) {
            notifyItemRemoved(items.size());
        }
        this.showLoading = showLoading;
    }

    public void addItems(@NonNull List<PopularItems.EmbeddedList.PopularItem> newItems) {
        items.addAll(newItems);
        notifyDataSetChanged();
    }

    public List<PopularItems.EmbeddedList.PopularItem> getAllItems() {
        return items;
    }

    public static class ItemViewHolder extends RecyclerView.ViewHolder {
        @InjectView(R.id.popularitem_title)
        TextView mTitle;
        @InjectView(R.id.popularitem_image)
        ImageView mCover;

        public ItemViewHolder(View view) {
            super(view);
            ButterKnife.inject(this, view);
        }
    }

    public static class LoadingViewHolder extends RecyclerView.ViewHolder {
        public LoadingViewHolder(View view) {
            super(view);
        }
    }

}
