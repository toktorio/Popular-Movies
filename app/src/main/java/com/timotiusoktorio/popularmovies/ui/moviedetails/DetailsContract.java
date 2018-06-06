package com.timotiusoktorio.popularmovies.ui.moviedetails;

import com.timotiusoktorio.popularmovies.data.model.Cast;
import com.timotiusoktorio.popularmovies.data.model.Movie;
import com.timotiusoktorio.popularmovies.data.model.Review;
import com.timotiusoktorio.popularmovies.data.model.Trailer;

import java.util.List;

interface DetailsContract {

    interface View {

        void showLoadingIndicator();

        void showMovieDetails(Movie movie);

        void updateFavoriteFabIcon(int iconResId);

        void showMovieCasts(List<Cast> casts);

        void showMovieTrailers(List<Trailer> trailers);

        void updateShareIntent(String message);

        void showMovieReviews(List<Review> reviews);

        void showEmptyView();

        void showFavoriteToggledSnackbar(boolean isMovieSaved);
    }

    interface Presenter {

        void initDataLoad();

        void refreshData();

        void toggleFavorite();
    }
}