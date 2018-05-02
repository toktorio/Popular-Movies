package com.timotiusoktorio.popularmovies.data.source;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;

import com.timotiusoktorio.popularmovies.data.model.Movie;
import com.timotiusoktorio.popularmovies.data.source.local.MovieDbHelper;
import com.timotiusoktorio.popularmovies.data.source.remote.MovieApiService;
import com.timotiusoktorio.popularmovies.util.ConnectivityHelper;

public class MovieDetailsLoader extends AsyncTaskLoader<Movie> {

    private long mMovieId;
    private MovieDbHelper mDbHelper;
    private MovieApiService mApiService;
    private Movie mCachedMovie;

    public MovieDetailsLoader(Context context, long movieId) {
        super(context);
        mMovieId = movieId;
        mDbHelper = MovieDbHelper.getInstance(context);
        mApiService = MovieApiService.getInstance(context);
    }

    @Override
    protected void onStartLoading() {
        if (mCachedMovie != null) deliverResult(mCachedMovie);
        else forceLoad();
    }

    @Override
    public Movie loadInBackground() {
        return ConnectivityHelper.isNetworkAvailable(getContext()) ?
                mApiService.getMovieDetails(mMovieId) : mDbHelper.getMovie(mMovieId);
    }

    @Override
    public void deliverResult(Movie data) {
        mCachedMovie = data;
        super.deliverResult(data);
    }
}