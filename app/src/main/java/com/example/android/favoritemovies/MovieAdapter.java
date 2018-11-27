package com.example.android.favoritemovies;

import android.content.Context;
import android.content.Intent;
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

    private final Context mContext;
    private final List<MovieTMDB> mData;

    public MovieAdapter(Context mContext, List<MovieTMDB> mData) {
        this.mContext = mContext;
        this.mData = mData;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater mInflater = LayoutInflater.from(mContext);
        View view = mInflater.inflate(R.layout.cardview_item_movie, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, final int i) {

        Picasso.get().load(Constantes.BASE_URL_IMAGE+mData.get(i).getPosterPath()).into(viewHolder.img_movie_thumbnail);

        viewHolder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(mContext,MovieActivity.class);

                intent.putExtra("Title", mData.get(i).getTitle());
                intent.putExtra("Overview", mData.get(i).getOverview());
                intent.putExtra("Rating", mData.get(i).getRating());
                intent.putExtra("PosterPath", Constantes.BASE_URL_IMAGE+mData.get(i).getPosterPath());
                intent.putExtra("ReleaseDate", mData.get(i).getReleaseDate());
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

                mContext.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public static class ViewHolder extends  RecyclerView.ViewHolder {

        final ImageView img_movie_thumbnail;
        final CardView cardView ;

        ViewHolder(@NonNull View itemView) {
            super(itemView);

            img_movie_thumbnail = itemView.findViewById(R.id.movie_img_id);
            cardView = itemView.findViewById(R.id.cardview_id);
        }

    }
}
