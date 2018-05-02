package com.timotiusoktorio.popularmovies.ui.movies;

import com.timotiusoktorio.popularmovies.R;
import com.timotiusoktorio.popularmovies.data.source.remote.MovieApiContract;

public enum MoviesSection {

    POPULAR(0, R.string.label_most_popular, MovieApiContract.TMDB_MOVIE_URL_POPULAR),
    TOP_RATED(1, R.string.label_top_rated, MovieApiContract.TMDB_MOVIE_URL_TOP_RATED),
    NOW_PLAYING(2, R.string.label_now_playing, MovieApiContract.TMDB_MOVIE_URL_NOW_PLAYING),
    UPCOMING(3, R.string.label_coming_soon, MovieApiContract.TMDB_MOVIE_URL_UPCOMING),
    FAVORITES(4, R.string.label_favorites, "Favorites");

    private int id;
    private int titleResId;
    private String url;

    MoviesSection(int id, int titleResId, String url) {
        this.id = id;
        this.titleResId = titleResId;
        this.url = url;
    }

    public int getId() {
        return id;
    }

    public int getTitleResId() {
        return titleResId;
    }

    public String getUrl() {
        return url;
    }

    public static MoviesSection fromId(int sectionId) {
        for (MoviesSection section : MoviesSection.values()) {
            if (section.getId() == sectionId)
                return section;
        }
        return POPULAR;
    }
}