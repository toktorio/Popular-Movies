package com.timotiusoktorio.popularmovies.ui.moviedetails;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.util.Log;

import com.timotiusoktorio.popularmovies.R;
import com.timotiusoktorio.popularmovies.data.model.Cast;
import com.timotiusoktorio.popularmovies.data.model.Movie;
import com.timotiusoktorio.popularmovies.data.model.Review;
import com.timotiusoktorio.popularmovies.data.model.Trailer;
import com.timotiusoktorio.popularmovies.data.source.MovieDetailsLoader;
import com.timotiusoktorio.popularmovies.data.source.local.MovieDbHelper;
import com.timotiusoktorio.popularmovies.data.source.remote.MovieApiContract;

import java.util.List;

class DetailsPresenter implements DetailsContract.Presenter, LoaderManager.LoaderCallbacks<Movie> {

    private Context mContext;
    private long mMovieId;
    private LoaderManager mLoaderManager;
    private MovieDbHelper mDbHelper;
    private DetailsContract.View mView;
    private DetailsFragment.DetailsFragmentListener mFragmentListener;
    private Movie mMovie;
    private boolean mIsMoviePersisted;

    DetailsPresenter(Context context, long movieId, LoaderManager loaderManager, MovieDbHelper dbHelper,
                     DetailsContract.View view, DetailsFragment.DetailsFragmentListener fragmentListener) {
        mContext = context;
        mMovieId = movieId;
        mLoaderManager = loaderManager;
        mDbHelper = dbHelper;
        mView = view;
        mFragmentListener = fragmentListener;
    }

    @Override
    public void initDataLoad() {
        mView.showLoadingIndicator();
        mLoaderManager.initLoader(2, null, this);
    }

    @Override
    public void refreshData() {
        mView.showLoadingIndicator();
        mLoaderManager.restartLoader(2, null, this);
    }

    @Override
    public void toggleFavorite() {
        if (mIsMoviePersisted) mDbHelper.deleteMovie(mMovieId);
        else mDbHelper.saveMovie(mMovie);
        mIsMoviePersisted = !mIsMoviePersisted;
        mView.updateFavoriteFabIcon(getFavoriteFabIcon());
        mView.showFavoriteToggledSnackbar(mIsMoviePersisted);
        mFragmentListener.onFavoriteToggled();
    }

    private int getFavoriteFabIcon() {
        return mIsMoviePersisted ? R.drawable.ic_favorite_white : R.drawable.ic_favorite_outline;
    }

    @NonNull
    @Override
    public Loader<Movie> onCreateLoader(int id, Bundle args) {
        return new MovieDetailsLoader(mContext, mMovieId);
    }

    @Override
    public void onLoadFinished(@NonNull Loader<Movie> loader, Movie movie) {
        if (movie != null) {
            mMovie = movie;
            mIsMoviePersisted = mDbHelper.isMovieSaved(mMovieId);
            mView.showMovieDetails(movie);
            mView.updateFavoriteFabIcon(getFavoriteFabIcon());
            mView.updateShareIntent(buildShareIntentMessage());

            List<Cast> casts = movie.getCasts();
            if (casts != null && !casts.isEmpty()) {
                mView.showMovieCasts(casts);
            }

            List<Trailer> trailers = movie.getTrailers();
            if (trailers != null && !trailers.isEmpty()) {
                mView.showMovieTrailers(trailers);
            }

            List<Review> reviews = movie.getReviews();
            if (reviews != null && !reviews.isEmpty()) {
                mView.showMovieReviews(reviews);
            }
        } else {
            mView.showEmptyView();
        }
    }

    @NonNull
    private String buildShareIntentMessage() {
        Trailer firstTrailer = mMovie.getTrailers().get(0);
        String trailerYoutubeUrl = MovieApiContract.YOUTUBE_VIDEO_URL + firstTrailer.getKey();
        return mContext.getString(R.string.string_format_share_intent, mMovie.getTitle(), trailerYoutubeUrl);
    }

    @Override
    public void onLoaderReset(@NonNull Loader<Movie> loader) {
        // Not implemented.
    }
}