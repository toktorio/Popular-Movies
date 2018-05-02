package com.timotiusoktorio.popularmovies.ui.movies;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;

import com.timotiusoktorio.popularmovies.data.model.Movie;
import com.timotiusoktorio.popularmovies.data.source.MovieLoader;

import java.util.List;

class MoviesPresenter implements MoviesContract.Presenter, LoaderManager.LoaderCallbacks<List<Movie>> {

    private Context mContext;
    private MoviesSection mSection;
    private LoaderManager mLoaderManager;
    private MoviesContract.View mView;

    MoviesPresenter(Context context, MoviesSection section, LoaderManager loaderManager, MoviesContract.View view) {
        mContext = context;
        mSection = section;
        mLoaderManager = loaderManager;
        mView = view;
    }

    @Override
    public void initDataLoad() {
        mView.showLoadingIndicator();
        mLoaderManager.initLoader(mSection.getId(), null, this);
    }

    @Override
    public void refreshData() {
        mView.showLoadingIndicator();
        mLoaderManager.restartLoader(mSection.getId(), null, this);
    }

    @NonNull
    @Override
    public Loader<List<Movie>> onCreateLoader(int id, Bundle args) {
        return new MovieLoader(mContext, mSection.getUrl());
    }

    @Override
    public void onLoadFinished(@NonNull Loader<List<Movie>> loader, List<Movie> data) {
        if (data != null && !data.isEmpty()) mView.showMovies(data);
        else mView.showEmptyView();
    }

    @Override
    public void onLoaderReset(@NonNull Loader<List<Movie>> loader) {
        // Not implemented.
    }
}