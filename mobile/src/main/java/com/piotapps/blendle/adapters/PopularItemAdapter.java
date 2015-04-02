package com.piotapps.blendle.adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.piotapps.blendle.R;
import com.piotapps.blendle.pojo.ItemManifest;
import com.piotapps.blendle.pojo.PopularItem;
import com.piotapps.blendle.utils.Utils;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * {@link android.support.v7.widget.RecyclerView.Adapter} to use for Popular items.
 * <p>
 * Uses the ItemViewHolder pattern and ButterKnife for optimal performance.
 * <p>
 * Uses a {@link OnItemSelectedListener} to let caller listen for item clicks. Use
 * {@link #setOnItemSelectedListener(OnItemSelectedListener)} and {@link #removeOnItemSelectedListener()}
 */
public class PopularItemAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    // Item view types
    private static final int TYPE_ITEM = 0;
    private static final int TYPE_FOOTER = 1;

    public interface OnItemSelectedListener {
        void onItemSelected(PopularItem pi);
    }

    private boolean showLoading;
    private List<PopularItem> items;
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
            final PopularItem pi = items.get(position);

            // Provider logo (fallback to app icon on error)
            final String logoUrl = Utils.getLogoUrl(pi.getItemProvider());
            Picasso.with(item.imgLogo.getContext()).load(logoUrl).error(R.mipmap.ic_launcher).into(item.imgLogo);

            // Item photo
            final ItemManifest.ImageHolder.ImageSizes is = pi.getFirstImage();
            if (is != null) {
                item.imgCover.setVisibility(View.VISIBLE);
                Picasso.with(item.imgCover.getContext()).load(is.getUrlMedium()).into(item.imgCover);
            } else {
                item.imgCover.setImageDrawable(null);
            }
            item.imgCover.setVisibility(is != null ? View.VISIBLE : View.GONE);

            // Price
            item.tvPrice.setText(pi.getPrice());

            // Headline1
            Utils.setTextOrHide(item.tvHeadline, pi.getHeadline1());

            // Content
            Utils.setTextOrHide(item.tvContent, pi.getContent());

            // Set the position to tag to use in OnClickListener
            holder.itemView.setTag(position);

            // Set on click listener
            holder.itemView.setOnClickListener(onItemClickListener);
        }
    }

    @Override
    public int getItemCount() {
        if (showLoading) {
            return items.size() + 1; // Account for loadingview
        }
        return items.size();
    }

    @Override
    public int getItemViewType(int position) {
        if (showLoading && position == items.size()) {
            return TYPE_FOOTER; // Account for loadingview
        }

        return TYPE_ITEM;
    }

    private final View.OnClickListener onItemClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (listener != null) {
                final int position = (int)v.getTag();
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

    public void addItems(@NonNull List<PopularItem> newItems) {
        // Add to current items
        items.addAll(newItems);
        // Notify changed!
        notifyDataSetChanged();
    }

    public List<PopularItem> getAllItems() {
        return items;
    }

    public static class ItemViewHolder extends RecyclerView.ViewHolder {
        @InjectView(R.id.popularitem_logo)
        ImageView imgLogo;
        @InjectView(R.id.popularitem_price)
        TextView tvPrice;
        @InjectView(R.id.popularitem_image)
        ImageView imgCover;
        @InjectView(R.id.popularitem_headline)
        TextView tvHeadline;
        @InjectView(R.id.popularitem_content)
        TextView tvContent;

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
