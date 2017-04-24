package com.shentuo.popularmovies.ui;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.shentuo.popularmovies.R;
import com.shentuo.popularmovies.model.Review;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ShentuoZhan on 24/4/17.
 */

public class ReviewsAdapter extends RecyclerView.Adapter<ReviewsAdapter.ReviewViewHolder> {
    private static final String TAG = ReviewsAdapter.class.getSimpleName();
    private Context context;
    private List<Review> mData;

    public ReviewsAdapter() {
        mData = new ArrayList<>();
    }

    public void addReviewItem(Review review) {
        mData.add(review);
    }

    public void clearItems() {
        mData = new ArrayList<>();
    }

    @Override
    public ReviewsAdapter.ReviewViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();
        int layoutIdForListItem = R.layout.reviews_list_item;
        LayoutInflater inflater = LayoutInflater.from(context);

        View view = inflater.inflate(layoutIdForListItem, parent, false);
        ReviewsAdapter.ReviewViewHolder viewHolder = new ReviewsAdapter.ReviewViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ReviewsAdapter.ReviewViewHolder holder, int position) {
        holder.bind(position);
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    class ReviewViewHolder extends RecyclerView.ViewHolder {
        TextView listItemTrailerView;

        public ReviewViewHolder(View itemView) {
            super(itemView);
            listItemTrailerView = (TextView) itemView.findViewById(R.id.tv_item_review);
        }

        void bind(int listIndex) {
            listItemTrailerView.setText(mData.get(listIndex).getAuthor() + ":\n" + mData.get(listIndex).getContent());
        }
    }
}