package com.example.android.movies;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by Joseph Costlow on 17-Feb-17.
 */

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MovieAdapterViewHolder> {

    private static final String TAG = MovieAdapter.class.getSimpleName();

    ArrayList<Movie> movieList;
    Context context;
    int adapterPosition;

    public MovieAdapter(Context context, ArrayList<Movie> movieList) {
        this.context = context;
        this.movieList = movieList;
    }

    public void swap(ArrayList<Movie> movies) {
        movieList.clear();
        movieList.addAll(movies);
        notifyDataSetChanged();
    }

    @Override
    public MovieAdapterViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        Context context = viewGroup.getContext();
        int layoutId = R.layout.list_item;
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;

        View view = inflater.inflate(layoutId, viewGroup, shouldAttachToParentImmediately);
        MovieAdapterViewHolder holder = new MovieAdapterViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(MovieAdapterViewHolder holder, int position) {

        Picasso.with(context).load(movieList.get(position).moviePosterPath).into(holder.mPoster);
    }

    @Override
    public int getItemCount() {

        if (movieList.isEmpty()) return 0;

        return movieList.size();
    }

    public class MovieAdapterViewHolder extends RecyclerView.ViewHolder {

        public ImageView mPoster;
        String currentPoster;
        String currentTitle;
        String currentReleaseDate;
        String currentOverview;
        String currentRating;
        int currentId;

        public MovieAdapterViewHolder(final View itemView) {
            super(itemView);
            final Context context = itemView.getContext();

//            find image view
            mPoster = (ImageView) itemView.findViewById(R.id.iv_poster);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

//                    adapter position to determine which movie is being clicked
                    adapterPosition = getAdapterPosition();

//                    set movie information to be sent
                    currentPoster = movieList.get(adapterPosition).moviePosterPath;
                    currentTitle = movieList.get(adapterPosition).movieTitle;
                    currentReleaseDate = movieList.get(adapterPosition).movieReleaseDate;
                    currentOverview = movieList.get(adapterPosition).movieOverview;
                    currentRating = movieList.get(adapterPosition).movieRating;
                    currentId = movieList.get(adapterPosition).movieId;

//                    create intent to Details Activity and add movie data as extras
                    Intent intent = new Intent(context, DetailsActivity.class);
                    intent.putExtra(context.getString(R.string.intent_to_details_position_key), adapterPosition);
                    intent.putExtra(context.getString(R.string.intent_to_details_title_key), currentTitle);
                    intent.putExtra(context.getString(R.string.intent_to_details_release_date_key), currentReleaseDate);
                    intent.putExtra(context.getString(R.string.intent_to_details_overview_key), currentOverview);
                    intent.putExtra(context.getString(R.string.intent_to_details_rating_key), currentRating);
                    intent.putExtra(context.getString(R.string.intent_to_details_poster_key), currentPoster);
                    intent.putExtra(context.getString(R.string.intent_to_details_id_key), currentId);
                    context.startActivity(intent);
                }
            });
        }
    }
}