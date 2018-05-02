package com.timotiusoktorio.popularmovies.ui.movies;

import com.timotiusoktorio.popularmovies.data.model.Movie;

import java.util.List;

interface MoviesContract {

    interface View {

        void showLoadingIndicator();

        void showMovies(List<Movie> movies);

        void showEmptyView();
    }

    interface Presenter {

        void initDataLoad();

        void refreshData();
    }
}