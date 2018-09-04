package com.anddymao.shadertoy.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.anddymao.shadertoy.R;
import com.anddymao.shadertoy.filter.FilterItem;

import java.util.List;

public class FilterAdapter extends RecyclerView.Adapter<FilterAdapter.FilterHolder> {

    private List<FilterItem> filters;
    private Context context;
    private int selected = 0;

    public FilterAdapter(Context context, List<FilterItem> filters) {
        this.filters = filters;
        this.context = context;
    }

    @Override
    public FilterHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.filter_item_layout,
                parent, false);
        FilterHolder viewHolder = new FilterHolder(view);
        viewHolder.thumbImage = (ImageView) view
                .findViewById(R.id.filter_thumb_image);
        viewHolder.filterName = (TextView) view
                .findViewById(R.id.filter_thumb_name);
        viewHolder.filterRoot = (FrameLayout) view
                .findViewById(R.id.filter_root);
        viewHolder.thumbSelected = (FrameLayout) view
                .findViewById(R.id.filter_thumb_selected);
        viewHolder.thumbSelected_bg = view.
                findViewById(R.id.filter_thumb_selected_bg);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(FilterHolder holder, final int position) {
        final FilterItem filterItem = filters.get(position);
        holder.thumbImage.setImageResource(filterItem.mIcon);
        holder.filterName.setText(filterItem.mName);
        int color = R.color.filter_color_blue_dark;

        holder.filterName.setBackgroundColor(context.getResources().getColor(color));

        if (position == selected) {
            holder.thumbSelected.setVisibility(View.VISIBLE);
            holder.thumbSelected_bg.setBackgroundColor(context.getResources().getColor(color));
            holder.thumbSelected_bg.setAlpha(0.7f);
        } else {
            holder.thumbSelected.setVisibility(View.GONE);
        }

        holder.filterRoot.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (selected == position) {
                    onFilterChangeListener.onFilterChanged(filterItem);
                    return;
                }
                int lastSelected = selected;
                selected = position;
                notifyItemChanged(lastSelected);
                notifyItemChanged(position);
                onFilterChangeListener.onFilterChanged(filterItem);
            }
        });
    }

    @Override
    public int getItemCount() {
        return filters == null ? 0 : filters.size();
    }

    class FilterHolder extends RecyclerView.ViewHolder {
        ImageView thumbImage;
        TextView filterName;
        FrameLayout thumbSelected;
        FrameLayout filterRoot;
        View thumbSelected_bg;

        public FilterHolder(View itemView) {
            super(itemView);
        }
    }

    public interface onFilterChangeListener {
        void onFilterChanged(FilterItem filterItem);
    }

    private onFilterChangeListener onFilterChangeListener;

    public void setOnFilterChangeListener(onFilterChangeListener onFilterChangeListener) {
        this.onFilterChangeListener = onFilterChangeListener;
    }
}
