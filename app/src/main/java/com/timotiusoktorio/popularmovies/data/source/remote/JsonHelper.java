package com.timotiusoktorio.popularmovies.data.source.remote;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.timotiusoktorio.popularmovies.data.model.Cast;
import com.timotiusoktorio.popularmovies.data.model.Movie;
import com.timotiusoktorio.popularmovies.data.model.Review;
import com.timotiusoktorio.popularmovies.data.model.Trailer;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static com.timotiusoktorio.popularmovies.data.source.remote.MovieApiContract.TMDB_JSON_AUTHOR;
import static com.timotiusoktorio.popularmovies.data.source.remote.MovieApiContract.TMDB_JSON_BACKDROP_PATH;
import static com.timotiusoktorio.popularmovies.data.source.remote.MovieApiContract.TMDB_JSON_CAST;
import static com.timotiusoktorio.popularmovies.data.source.remote.MovieApiContract.TMDB_JSON_CHARACTER;
import static com.timotiusoktorio.popularmovies.data.source.remote.MovieApiContract.TMDB_JSON_CONTENT;
import static com.timotiusoktorio.popularmovies.data.source.remote.MovieApiContract.TMDB_JSON_GENRES;
import static com.timotiusoktorio.popularmovies.data.source.remote.MovieApiContract.TMDB_JSON_ID;
import static com.timotiusoktorio.popularmovies.data.source.remote.MovieApiContract.TMDB_JSON_KEY;
import static com.timotiusoktorio.popularmovies.data.source.remote.MovieApiContract.TMDB_JSON_NAME;
import static com.timotiusoktorio.popularmovies.data.source.remote.MovieApiContract.TMDB_JSON_OVERVIEW;
import static com.timotiusoktorio.popularmovies.data.source.remote.MovieApiContract.TMDB_JSON_POSTER_PATH;
import static com.timotiusoktorio.popularmovies.data.source.remote.MovieApiContract.TMDB_JSON_PROFILE_PATH;
import static com.timotiusoktorio.popularmovies.data.source.remote.MovieApiContract.TMDB_JSON_RELEASE_DATE;
import static com.timotiusoktorio.popularmovies.data.source.remote.MovieApiContract.TMDB_JSON_RESULTS;
import static com.timotiusoktorio.popularmovies.data.source.remote.MovieApiContract.TMDB_JSON_RUNTIME;
import static com.timotiusoktorio.popularmovies.data.source.remote.MovieApiContract.TMDB_JSON_TITLE;
import static com.timotiusoktorio.popularmovies.data.source.remote.MovieApiContract.TMDB_JSON_TYPE;
import static com.timotiusoktorio.popularmovies.data.source.remote.MovieApiContract.TMDB_JSON_VOTE_AVERAGE;

public class JsonHelper {

    public static List<Movie> extractMoviesFromJsonString(@NonNull String jsonString) throws JSONException {
        List<Movie> movies = new ArrayList<>();
        JSONObject root = new JSONObject(jsonString);
        JSONArray results = root.getJSONArray(TMDB_JSON_RESULTS);
        for (int i = 0; i < results.length(); i++) {
            JSONObject object = results.getJSONObject(i);
            long id = object.getLong(TMDB_JSON_ID);
            String posterPath = object.getString(TMDB_JSON_POSTER_PATH);
            Movie movie = new Movie(id, posterPath);
            movies.add(movie);
        }
        return movies;
    }

    public static Movie extractMovieDetailsFromJsonString(@NonNull String jsonString) throws JSONException {
        Movie movie = new Movie();
        JSONObject root = new JSONObject(jsonString);
        movie.setId(root.getLong(TMDB_JSON_ID));
        movie.setPosterPath(root.getString(TMDB_JSON_POSTER_PATH));
        movie.setBackdropPath(root.getString(TMDB_JSON_BACKDROP_PATH));
        movie.setTitle(root.getString(TMDB_JSON_TITLE));
        movie.setReleaseDate(root.getString(TMDB_JSON_RELEASE_DATE));
        movie.setRuntime(root.getString(TMDB_JSON_RUNTIME));
        movie.setGenres(getGenresInStringFormat(root.getJSONArray(TMDB_JSON_GENRES)));
        movie.setOverview(root.getString(TMDB_JSON_OVERVIEW));
        movie.setVoteAverage(root.getDouble(TMDB_JSON_VOTE_AVERAGE));
        return movie;
    }

    private static String getGenresInStringFormat(@NonNull JSONArray genres) throws JSONException {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < genres.length(); i++) {
            JSONObject object = genres.getJSONObject(i);
            builder.append(object.getString(TMDB_JSON_NAME));
            if (i != (genres.length() - 1)) {
                builder.append(", ");
            }
        }
        return builder.toString();
    }

    public static List<Cast> extractCastsFromJsonString(@Nullable String jsonString) throws JSONException {
        if (jsonString == null) {
            return null;
        }
        List<Cast> casts = new ArrayList<>();
        JSONObject root = new JSONObject(jsonString);
        JSONArray castArray = root.getJSONArray(TMDB_JSON_CAST);
        for (int i = 0; i < castArray.length(); i++) {
            JSONObject object = castArray.getJSONObject(i);
            int id = object.getInt(TMDB_JSON_ID);
            String profilePath = object.getString(TMDB_JSON_PROFILE_PATH);
            String name = object.getString(TMDB_JSON_NAME);
            String character = object.getString(TMDB_JSON_CHARACTER);
            Cast cast = new Cast(id, profilePath, name, character);
            casts.add(cast);
        }
        return casts;
    }

    public static List<Trailer> extractTrailersFromJsonString(@Nullable String jsonString) throws JSONException {
        if (jsonString == null) {
            return null;
        }
        List<Trailer> trailers = new ArrayList<>();
        JSONObject root = new JSONObject(jsonString);
        JSONArray results = root.getJSONArray(TMDB_JSON_RESULTS);
        for (int i = 0; i < results.length(); i++) {
            JSONObject object = results.getJSONObject(i);
            String key = object.getString(TMDB_JSON_KEY);
            String name = object.getString(TMDB_JSON_NAME);
            String type = object.getString(TMDB_JSON_TYPE);
            Trailer trailer = new Trailer(key, name, type);
            trailers.add(trailer);
        }
        return trailers;
    }

    public static List<Review> extractReviewsFromJsonString(@Nullable String jsonString, @NonNull String movieTitle) throws JSONException {
        if (jsonString == null) {
            return null;
        }
        List<Review> reviews = new ArrayList<>();
        JSONObject root = new JSONObject(jsonString);
        JSONArray results = root.getJSONArray(TMDB_JSON_RESULTS);
        for (int i = 0; i < results.length(); i++) {
            JSONObject object = results.getJSONObject(i);
            String author = object.getString(TMDB_JSON_AUTHOR);
            String content = object.getString(TMDB_JSON_CONTENT);
            Review review = new Review(movieTitle, author, content);
            reviews.add(review);
        }
        return reviews;
    }
}