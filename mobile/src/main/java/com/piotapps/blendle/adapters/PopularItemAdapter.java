package com.piotapps.blendle.adapters;

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

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * {@link android.support.v7.widget.RecyclerView.Adapter} to use for Popular items
 */
public class PopularItemAdapter extends RecyclerView.Adapter<PopularItemAdapter.ViewHolder> {

    private List<PopularItems.EmbeddedList.PopularItem> items;

    public PopularItemAdapter(List<PopularItems.EmbeddedList.PopularItem> items) {
        this.items = items;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.item_popular, parent, false);

        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final PopularItems.EmbeddedList.PopularItem pi = items.get(position);

        ItemManifest.ImageHolder.ImageSizes is = pi.getFirstImage();
        if (is != null) {
            Picasso.with(holder.mCover.getContext()).load(is.getUrlMedium()).into(holder.mCover);
        }
        holder.mTitle.setText(Html.fromHtml(pi.getHeadline()));
    }

    @Override
    public int getItemCount() {
        return items != null ? items.size() : 0;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        @InjectView(R.id.popularitem_title)
        TextView mTitle;
        @InjectView(R.id.popularitem_image)
        ImageView mCover;

        public ViewHolder(View view) {
            super(view);
            ButterKnife.inject(this, view);
        }
    }

}
