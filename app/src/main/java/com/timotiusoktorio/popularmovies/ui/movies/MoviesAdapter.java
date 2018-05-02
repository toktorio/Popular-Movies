package com.timotiusoktorio.popularmovies.ui.movies;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;
import com.timotiusoktorio.popularmovies.R;
import com.timotiusoktorio.popularmovies.data.model.Movie;
import com.timotiusoktorio.popularmovies.data.source.remote.MovieApiContract;

import java.util.List;

class MoviesAdapter extends RecyclerView.Adapter<MoviesAdapter.ViewHolder> {

    private List<Movie> mMovies;
    private MoviesFragment.MoviesFragmentListener mFragmentListener;

    MoviesAdapter(@NonNull List<Movie> movies, @NonNull MoviesFragment.MoviesFragmentListener listener) {
        mMovies = movies;
        mFragmentListener = listener;
    }

    void clearData() {
        mMovies.clear();
        notifyDataSetChanged();
    }

    void addData(@NonNull List<Movie> movies) {
        mMovies.addAll(movies);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_movies, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final Movie movie = mMovies.get(position);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mFragmentListener.onMovieClicked(movie.getId());
            }
        });

        String posterUrl = MovieApiContract.TMDB_MOVIE_POSTER_URL + movie.getPosterPath();
        Picasso.get().load(posterUrl).into(holder.movieImageView);
    }

    @Override
    public int getItemCount() {
        return mMovies.size();
    }

    @Override
    public long getItemId(int position) {
        return mMovies.get(position).getId();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        private ImageView movieImageView;

        ViewHolder(View itemView) {
            super(itemView);
            movieImageView = itemView.findViewById(R.id.movie_image_view);
        }
    }
}