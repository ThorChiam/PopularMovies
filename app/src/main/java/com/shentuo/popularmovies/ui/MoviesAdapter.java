package com.shentuo.popularmovies.ui;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.shentuo.popularmovies.R;
import com.shentuo.popularmovies.model.Poster;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ShentuoZhan on 13/3/17.
 */

public class MoviesAdapter extends RecyclerView.Adapter<MoviesAdapter.PosterViewHolder> {
    private static final String TAG = MoviesAdapter.class.getSimpleName();
    final private ListItemClickListener mOnClickListener;
    private Context context;
    private List<Poster> mData;
    private final String BASE_IMAGE_URL = "http://image.tmdb.org/t/p/";
    private String imageSize = "w185";//"w92", "w154", "w185", "w342", "w500", "w780", or "original"

    public interface ListItemClickListener {
        void onListItemClick(String jsonString);
    }

    public MoviesAdapter(ListItemClickListener listener) {
        mOnClickListener = listener;
        mData = new ArrayList<>();
    }

    public void addPosterItem(Poster poster) {
        mData.add(poster);
    }

    public void clearItems() {
        mData = new ArrayList<>();
    }

    @Override
    public PosterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();
        int layoutIdForListItem = R.layout.movies_list_item;
        LayoutInflater inflater = LayoutInflater.from(context);

        View view = inflater.inflate(layoutIdForListItem, parent, false);
        PosterViewHolder viewHolder = new PosterViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(PosterViewHolder holder, int position) {
        holder.bind(position);
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    class PosterViewHolder extends RecyclerView.ViewHolder implements OnClickListener {
        ImageView listItemPosterView;

        public PosterViewHolder(View itemView) {
            super(itemView);
            listItemPosterView = (ImageView) itemView.findViewById(R.id.iv_item_poster);
            itemView.setOnClickListener(this);
        }

        void bind(int listIndex) {
            String imageURL = BASE_IMAGE_URL + imageSize + "/" + mData.get(listIndex).getPoster_path();
            Picasso.with(context).load(imageURL).into(listItemPosterView);
        }

        @Override
        public void onClick(View view) {
            int clickedPosition = getAdapterPosition();
            mOnClickListener.onListItemClick(mData.get(clickedPosition).toString());
        }
    }
}
