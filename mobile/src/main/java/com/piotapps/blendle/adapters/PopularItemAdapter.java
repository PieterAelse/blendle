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

    public interface OnItemSelectedListener {
        void onItemSelected(PopularItems.EmbeddedList.PopularItem pi);
    }

    private interface OnInternalItemSelectedListener {
        void onItemSelected(int position);
    }

    private boolean showLoading;
    private List<PopularItems.EmbeddedList.PopularItem> items;
    private OnItemSelectedListener listener;

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

                return new ItemViewHolder(item, internalItemSelectedListener);
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
                Picasso.with(item.imgCover.getContext()).load(is.getUrlMedium()).into(item.imgCover);
            } else {
                item.imgCover.setImageDrawable(null);
            }
            item.tvTitle.setText(Html.fromHtml(pi.getHeadline()));

            // Set the position to tag to use in listeners
            item.position = position;
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

    private final OnInternalItemSelectedListener internalItemSelectedListener = new OnInternalItemSelectedListener() {
        @Override
        public void onItemSelected(final int position) {
            if (listener != null) {
                listener.onItemSelected(items.get(position));
            }
        }
    };

    public void setOnItemSelectedListener(OnItemSelectedListener onItemSelectedListener) {
        listener = onItemSelectedListener;
    }

    public void removeOnItemSelectedListener() {
        listener = null;
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

    public static class ItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        @InjectView(R.id.popularitem_title)
        TextView tvTitle;
        @InjectView(R.id.popularitem_image)
        ImageView imgCover;

        private OnInternalItemSelectedListener listener;
        int position;

        public ItemViewHolder(View view, OnInternalItemSelectedListener listener) {
            super(view);
            ButterKnife.inject(this, view);

            this.listener = listener;
            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            listener.onItemSelected(position);
        }
    }

    public static class LoadingViewHolder extends RecyclerView.ViewHolder {
        public LoadingViewHolder(View view) {
            super(view);
        }
    }

}
