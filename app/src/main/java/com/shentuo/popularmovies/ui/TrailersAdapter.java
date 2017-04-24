package com.shentuo.popularmovies.ui;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.shentuo.popularmovies.R;
import com.shentuo.popularmovies.model.Trailer;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ShentuoZhan on 24/4/17.
 */

public class TrailersAdapter extends RecyclerView.Adapter<TrailersAdapter.TrailerViewHolder> {
    private static final String TAG = TrailersAdapter.class.getSimpleName();
    final private TrailersAdapter.ListItemClickListener mOnClickListener;
    private Context context;
    private List<Trailer> mData;

    public interface ListItemClickListener {
        void onListItemClick(String jsonString);
    }

    public TrailersAdapter(TrailersAdapter.ListItemClickListener listener) {
        mOnClickListener = listener;
        mData = new ArrayList<>();
    }

    public void addTrailerItem(Trailer trailer) {
        mData.add(trailer);
    }

    public void clearItems() {
        mData = new ArrayList<>();
    }

    @Override
    public TrailersAdapter.TrailerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();
        int layoutIdForListItem = R.layout.trailers_list_item;
        LayoutInflater inflater = LayoutInflater.from(context);

        View view = inflater.inflate(layoutIdForListItem, parent, false);
        TrailersAdapter.TrailerViewHolder viewHolder = new TrailersAdapter.TrailerViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(TrailersAdapter.TrailerViewHolder holder, int position) {
        holder.bind(position);
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    class TrailerViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView listItemTrailerView;

        public TrailerViewHolder(View itemView) {
            super(itemView);
            listItemTrailerView = (TextView) itemView.findViewById(R.id.tv_item_trailer);
            itemView.setOnClickListener(this);
        }

        void bind(int listIndex) {
            listItemTrailerView.setText(mData.get(listIndex).getName());
        }

        @Override
        public void onClick(View view) {
            int clickedPosition = getAdapterPosition();
            mOnClickListener.onListItemClick(mData.get(clickedPosition).getKey());
        }
    }
}
