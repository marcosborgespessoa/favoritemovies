package com.example.android.favoritemovies;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.List;

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.ViewHolder> {

    private Context mContext;
    private List<MovieTMDB> mData;
    private final MovieAdapterOnClickHandler mClickHandler;

    public MovieAdapter(Context mContext, MovieAdapterOnClickHandler clickHandler) {
        this.mContext = mContext;
        this.mClickHandler = clickHandler;
    }

    public interface MovieAdapterOnClickHandler {
        void onClick(MovieTMDB movie);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        LayoutInflater mInflater = LayoutInflater.from(viewGroup.getContext());
        View view = mInflater.inflate(R.layout.cardview_item_movie, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int i) {
        MovieTMDB movie = mData.get(i);
        try {
            Picasso.get().load(NetworkUtils.buildImageFilmUrl(movie.getPosterPath()).toString()).into(viewHolder.img_movie_thumbnail);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        if (mData == null) {
            return 0;
        }
        return mData.size();
    }

    public void setData(List<MovieTMDB> data) {
        mData = data;
        notifyDataSetChanged();
    }

    public class ViewHolder extends  RecyclerView.ViewHolder implements View.OnClickListener {
        final ImageView img_movie_thumbnail;
        final CardView cardView ;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            img_movie_thumbnail = itemView.findViewById(R.id.movie_img_id);
            cardView = itemView.findViewById(R.id.cardview_id);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int adapterPosition = getAdapterPosition();
            MovieTMDB filmInfo = mData.get(adapterPosition);
            mClickHandler.onClick(filmInfo);
        }
    }
}
