package com.timotiusoktorio.popularmovies.data.source;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;

import com.timotiusoktorio.popularmovies.R;
import com.timotiusoktorio.popularmovies.data.model.Movie;
import com.timotiusoktorio.popularmovies.data.source.local.MovieDbHelper;
import com.timotiusoktorio.popularmovies.data.source.remote.MovieApiService;
import com.timotiusoktorio.popularmovies.util.ConnectivityHelper;

import java.util.List;

public class MovieLoader extends AsyncTaskLoader<List<Movie>> {

    private String mSectionUrl;
    private MovieDbHelper mDbHelper;
    private MovieApiService mApiService;
    private List<Movie> mCachedMovies;

    public MovieLoader(Context context, String sectionUrl) {
        super(context);
        mSectionUrl = sectionUrl;
        mDbHelper = MovieDbHelper.getInstance(context);
        mApiService = MovieApiService.getInstance(context);
    }

    @Override
    protected void onStartLoading() {
        if (mCachedMovies != null) deliverResult(mCachedMovies);
        else forceLoad();
    }

    @Override
    public List<Movie> loadInBackground() {
        if (mSectionUrl.equals(getContext().getString(R.string.label_favorites))) {
            return mDbHelper.getMovies();
        }
        if (ConnectivityHelper.isNetworkAvailable(getContext())) {
            return mApiService.getMovies(mSectionUrl);
        }
        return null;
    }

    @Override
    public void deliverResult(List<Movie> data) {
        mCachedMovies = data;
        super.deliverResult(data);
    }
}